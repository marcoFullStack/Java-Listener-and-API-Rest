package es.altia.chd.form.ticket.internal;

import com.liferay.counter.kernel.service.CounterLocalService;
import com.liferay.dynamic.data.mapping.model.DDMFormField;
import com.liferay.dynamic.data.mapping.model.DDMFormInstanceRecord;
import com.liferay.dynamic.data.mapping.model.DDMStructure;
import com.liferay.dynamic.data.mapping.model.LocalizedValue;
import com.liferay.dynamic.data.mapping.service.DDMStructureLocalService;
import com.liferay.dynamic.data.mapping.storage.DDMFormFieldValue;
import com.liferay.dynamic.data.mapping.storage.DDMFormValues;
import com.liferay.dynamic.data.mapping.util.DDMFormValuesToFieldsConverter;
import com.liferay.portal.configuration.module.configuration.ConfigurationProvider;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.BaseModelListener;
import com.liferay.portal.kernel.model.ModelListener;
import com.liferay.portal.kernel.module.configuration.ConfigurationException;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.kernel.util.Validator;
import es.altia.chd.form.ticket.configuration.FormConfig;
import es.altia.chd.form.ticket.configuration.FormsTicketSystemConfiguration;
import es.altia.chd.form.ticket.restapi.client.service.OdooApiClientService;
import es.altia.chd.form.ticket.restapi.dto.TicketCreateDTO;
import es.altia.chd.form.ticket.sb.model.Ticket;
import es.altia.chd.form.ticket.sb.service.TicketLocalService;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component(
        configurationPid = "es.altia.chd.form.ticket.configuration.FormsTicketSystemConfiguration",
        immediate = true,
        service = ModelListener.class
)
public class FormTicketListener extends BaseModelListener<DDMFormInstanceRecord> {

    private static final Pattern _JSON_ARRAY_PATTERN = Pattern.compile("\\[\"(.*?)\"\\]");

    @Override
    public void onAfterCreate(DDMFormInstanceRecord record) {
        try {
            long formInstanceId = record.getFormInstanceId();

            FormsTicketSystemConfiguration _configuration =
                    _configurationProvider.getCompanyConfiguration(FormsTicketSystemConfiguration.class, record.getCompanyId());

            parseConfiguration(_configuration.formsTicketConfigurations());

            if (!_formConfigMap.containsKey(formInstanceId)) {
                return;
            }

            FormConfig config = _formConfigMap.get(formInstanceId);
            String contactField = config != null ? config.getContactField() : null;
            String contactNameField = config != null ? config.getContactNameField() : null;
            String subjectField = config != null ? config.getSubjectField() : null;
            String grupo = config != null ? config.getGrupo() : null;

            String endpoint = config != null ? config.getEndpoint() : null;

            if (contactField == null || subjectField == null || grupo == null || endpoint == null) {
                _log.warn("Configuración incompleta para el formulario " + formInstanceId);
                return;
            }

            _log.info(">>> Formulario detectado. RecordID: " + record.getFormInstanceRecordId());

            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(2000);

                        String email = "";
                        String contactName = "";
                        String asunto = "";
                        String idGrupoSeleccionado = "";
                        StringBuilder description = new StringBuilder("");
                        Locale threadLocale = LocaleUtil.getSiteDefault();

                        DDMFormValues formValues = record.getDDMFormValues();
                        long ddmStructureId = record.getFormInstance().getStructureId();
                        DDMStructure ddmStructure = _ddmStructureLocalService.getDDMStructure(ddmStructureId);

                        List<DDMFormFieldValue> allValues = new ArrayList<>();
                        flattenValues(allValues, formValues.getDDMFormFieldValues());

                        for (DDMFormFieldValue fv : allValues) {
                            if (fv == null || fv.getValue() == null) continue;

                            String value = fv.getValue().getString(threadLocale);
                            Matcher matcher = _JSON_ARRAY_PATTERN.matcher(value);
                            if (matcher.find()) { value = matcher.group(1); }

                            String fieldRef = fv.getFieldReference();

                            if (fieldRef.equalsIgnoreCase(contactField)) {
                                email = value.toLowerCase().trim();
                            } else if (fieldRef.equalsIgnoreCase(contactNameField)) {
                                contactName = value.trim();
                            } else if (fieldRef.equalsIgnoreCase(subjectField)) {
                                asunto = value.trim();
                            }else if (fieldRef.equalsIgnoreCase(grupo)) {
                                idGrupoSeleccionado = value.trim();
                            }
                            description.append("<p>");
                            if (fv.getDDMFormField() != null && !fv.getType().equals("fieldset") && !value.isEmpty()) {
                                description.append("<b>")
                                        .append(fv.getDDMFormField().getLabel().getString(threadLocale))
                                        .append(": </b>");

                            }
                            if (fv.getType().equalsIgnoreCase("radio") || fv.getType().equalsIgnoreCase("select")) {
                                String finalLabel = value;
                                DDMFormField fieldDefinition = ddmStructure.getDDMFormField(fv.getName());

                                if(fieldDefinition != null){
                                    Map<String, LocalizedValue> options = fieldDefinition.getDDMFormFieldOptions().getOptions();

                                    if(options != null && options.containsKey(value)){
                                        finalLabel = options.get(value).getString(threadLocale);
                                    }
                                }

                                description.append(finalLabel);

                            } else {
                                description.append(value);
                            }
                            description.append("</p>");
                        }

                        _log.info("PROCESANDO ENVÍO A ODOO:");
                        _log.info(" - URL: [" + endpoint.trim() + "]");
                        _log.info(" - Email: [" + email + "] | Asunto: [" + asunto + "]");

                        TicketCreateDTO ticketDTO = null;

                        if (email.isEmpty() || asunto.isEmpty()) {
                            _log.error("ERROR: No se puede enviar a Odoo. Email o Asunto vacíos (revisa referencias TextXXXX).");
                            return;
                        } else {
                            try {
                                //Integer teamId = Integer.valueOf(grupo.trim());

                                Integer teamId = 1;

                                if (Validator.isNumber(idGrupoSeleccionado)) {
                                    teamId = Integer.valueOf(idGrupoSeleccionado);
                                    _log.info(">>>> ID de grupo recuperado correctamente del desplegable: [" + teamId + "]");
                                } else {
                                    _log.error(">>>> ¡ALERTA! El campo mapeado como grupo no devolvió un ID numérico. Valor en el formulario: ["
                                            + idGrupoSeleccionado + "]. Referencia del campo buscada: [" + grupo + "]. Usando ID por defecto: " + teamId);
                                }

                                ticketDTO = odooApiClientService.createTicket(
                                        contactName,
                                        email,
                                        teamId,        // Integer
                                        asunto,
                                        description.toString(),
                                        endpoint.trim(),
                                        null           // contactId (null para usar prioridad de email/nombre)
                                );
                            } catch (Exception e) {
                                _log.error("EXCEPCIÓN en cliente REST de Odoo: " + e.getMessage(), e);
                            }
                        }

                        String ticketId = (ticketDTO != null) ? ticketDTO.number() : "";
                        String token = (ticketDTO != null) ? ticketDTO.token() : "";

                        if (_ticketLocalService != null) {
                            Ticket nuevoTicket = _ticketLocalService.addTicket(
                                    record.getGroupId(), record.getFormInstanceId(), record.getFormInstanceRecordId(),
                                    ticketId, token, email
                            );
                            _ticketLocalService.updateTicket(nuevoTicket);
                            _log.info(">>> Proceso finalizado. Ticket Odoo: " + ticketId);
                        }

                    } catch (Exception e) {
                        _log.error("Error en el hilo del Listener: " + e.getMessage(), e);
                    }
                }
            }).start();

        } catch (ConfigurationException e) {
            _log.error("Error de configuración: " + e.getMessage());
        }
    }

    private void flattenValues(List<DDMFormFieldValue> allValues, List<DDMFormFieldValue> currentValues) {
        for (DDMFormFieldValue fv : currentValues) {
            allValues.add(fv);
            if (fv.getNestedDDMFormFieldValues() != null && !fv.getNestedDDMFormFieldValues().isEmpty()) {
                flattenValues(allValues, fv.getNestedDDMFormFieldValues());
            }
        }
    }

    /**
     * Parse el string con la configuración del sistema de tickets
     *
     * Ej: idFormInstance,contactField,endPoint
     *
     * @param formsTicketConfig
     */
    private void parseConfiguration(String[] formsTicketConfig) {
        _formConfigMap = new HashMap<>();
        if (formsTicketConfig == null) return;
        for (String line : formsTicketConfig) {
            // Dividimos por comas
            String[] parts = line.split("\\,");
            if (parts.length != 6) continue;
            try {
                long formId = Long.parseLong(parts[0].trim());
                _formConfigMap.put(formId, new FormConfig(
                        parts[1].trim(),
                        parts[2].trim(),
                        parts[3].trim(),
                        parts[4].trim(),
                        parts[5].trim()
                ));
            } catch (Exception e) {
                _log.error("Error parseando configuración: " + line);
            }
        }
    }

    private static final Log _log = LogFactoryUtil.getLog(FormTicketListener.class);
    private Map<Long, FormConfig> _formConfigMap;

    @Reference
    private OdooApiClientService odooApiClientService;

    @Reference(cardinality = org.osgi.service.component.annotations.ReferenceCardinality.OPTIONAL)
    private TicketLocalService _ticketLocalService;

    @Reference
    private ConfigurationProvider _configurationProvider;

    @Reference
    private Portal _portal;

    @Reference
    private DDMStructureLocalService _ddmStructureLocalService;

    @Reference
    private DDMFormValuesToFieldsConverter _ddmFormValuesToFieldsConverter;

    @Reference
    private CounterLocalService _counterLocalService;
}