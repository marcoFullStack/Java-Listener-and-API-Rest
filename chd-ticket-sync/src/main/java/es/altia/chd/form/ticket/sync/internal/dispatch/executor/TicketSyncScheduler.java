package es.altia.chd.form.ticket.sync.internal.dispatch.executor;

import com.liferay.adaptive.media.exception.AMRuntimeException.IOException;
import com.liferay.dispatch.executor.BaseDispatchTaskExecutor;
import com.liferay.dispatch.executor.DispatchTaskExecutor;
import com.liferay.dispatch.executor.DispatchTaskExecutorOutput;
import com.liferay.dispatch.model.DispatchTrigger;
import com.liferay.dynamic.data.mapping.model.DDMFormField;
import com.liferay.dynamic.data.mapping.model.DDMFormInstanceRecord;
import com.liferay.dynamic.data.mapping.model.DDMStructure;
import com.liferay.dynamic.data.mapping.model.LocalizedValue;
import com.liferay.dynamic.data.mapping.service.DDMFormInstanceRecordLocalService;
import com.liferay.dynamic.data.mapping.service.DDMStructureLocalService;
import com.liferay.dynamic.data.mapping.storage.DDMFormFieldValue;
import com.liferay.dynamic.data.mapping.storage.DDMFormValues;
import com.liferay.portal.configuration.module.configuration.ConfigurationProvider;
import com.liferay.portal.kernel.dao.orm.Disjunction;
import com.liferay.portal.kernel.dao.orm.DynamicQuery;
import com.liferay.portal.kernel.dao.orm.RestrictionsFactoryUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.module.configuration.ConfigurationException;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.Validator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import es.altia.chd.form.ticket.configuration.FormConfig;
import es.altia.chd.form.ticket.configuration.FormsTicketSystemConfiguration;
import es.altia.chd.form.ticket.restapi.client.service.OdooApiClientService;
import es.altia.chd.form.ticket.restapi.dto.TicketCreateDTO;
import es.altia.chd.form.ticket.sb.model.Ticket;
import es.altia.chd.form.ticket.sb.service.TicketLocalService;

@Component(
	property = { 
			"dispatch.task.executor.name=TicketSyncScheduler",
			"dispatch.task.executor.type=TicketSyncScheduler" },
	service = DispatchTaskExecutor.class)
public class TicketSyncScheduler extends BaseDispatchTaskExecutor {

	@Override
	public void doExecute(DispatchTrigger dispatchTrigger, DispatchTaskExecutorOutput dispatchTaskExecutorOutput)
			throws IOException, PortalException {

		if (_log.isInfoEnabled()) {
			_log.info("Ejecutando TicketSyncScheduler");
		}

		try {
			FormsTicketSystemConfiguration configuration = _configurationProvider
					.getCompanyConfiguration(FormsTicketSystemConfiguration.class, dispatchTrigger.getCompanyId());

			parseConfiguration(configuration.formsTicketConfigurations());

			DynamicQuery dynamicQuery = _ticketLocalService.dynamicQuery();

			Disjunction disjunction = RestrictionsFactoryUtil.disjunction();
			disjunction.add(RestrictionsFactoryUtil.isNull("ticketId"));
			disjunction.add(RestrictionsFactoryUtil.eq("ticketId", ""));

			dynamicQuery.add(disjunction);

			List<Object> results = _ticketLocalService.dynamicQuery(dynamicQuery);
			List<Ticket> pendingTickets = new ArrayList<Ticket>();

			for (Object obj : results) {
				pendingTickets.add((Ticket) obj);
			}

			_log.info("Tickets pendientes encontrados: " + pendingTickets.size());

			for (Ticket pendingTicket : pendingTickets) {
				_log.info("Ticket con id: " + pendingTicket.getId());

				try {
					DDMFormInstanceRecord record = _ddmFormInstanceRecordLocalService
							.fetchDDMFormInstanceRecord(pendingTicket.getFormInstanceRecordId());

					if (record == null) {
						_log.warn("No se encontró DDMFormInstanceRecord para formInstanceRecordId="
								+ pendingTicket.getFormInstanceRecordId());
						continue;
					}

					long formInstanceId = record.getFormInstanceId();

					if (!_formConfigMap.containsKey(formInstanceId)) {
						_log.info("El formulario " + formInstanceId + " no tiene configuración. Se omite.");
						continue;
					}

					FormConfig config = _formConfigMap.get(formInstanceId);

					String contactField = (config != null) ? config.getContactField() : null;
					String contactNameField = (config != null) ? config.getContactNameField() : null;
					String subjectField = (config != null) ? config.getSubjectField() : null;
					String grupo = (config != null) ? config.getGrupo() : null;
					String endpoint = (config != null) ? config.getEndpoint() : null;

					if (contactField == null || subjectField == null || grupo == null || endpoint == null) {

						_log.warn("Configuración incompleta para formInstanceId=" + formInstanceId);
						continue;
					}

					_log.info("Reprocesando ticket pendiente para RecordID: " + record.getFormInstanceRecordId());

					String contactName = "";
					String email = "";
					String asunto = "";
					StringBuffer description = new StringBuffer("");

					Locale threadLocale = LocaleUtil.getSiteDefault();

					DDMFormValues formValues = record.getDDMFormValues();

					long ddmStructureId = record.getFormInstance().getStructureId();

					DDMStructure ddmStructure = _ddmStructureLocalService.getDDMStructure(ddmStructureId);

					for (DDMFormFieldValue fv : formValues.getDDMFormFieldValues()) {
						if (fv == null || fv.getValue() == null) {
							continue;
						}

						String originalText = fv.getValue().getString(threadLocale);
						String value = originalText;

						Matcher matcher = _JSON_ARRAY_PATTERN.matcher(originalText);

						if (matcher.find()) {
							value = matcher.group(1);
						}

						String fieldRef = fv.getFieldReference();

						if (fieldRef.equalsIgnoreCase(contactField)) {
							email = value.toLowerCase().trim();
							value = email;
						} else if (Validator.isNotNull(contactNameField)
								&& fieldRef.equalsIgnoreCase(contactNameField)) {

							contactName = value.trim();
							value = contactName;
						} else if (fieldRef.equalsIgnoreCase(subjectField)) {
							asunto = value.trim();
							value = asunto;
						} else {
							value = StringUtils.capitalize(value.toLowerCase());
						}

						description.append("<p>");
						
						if (fv.getDDMFormField() != null && fv.getDDMFormField().getLabel() != null) {
							description.append(fv.getDDMFormField().getLabel().getString(threadLocale));
							description.append(": ");
						}

						if (fv.getType().equalsIgnoreCase("radio") || fv.getType().equalsIgnoreCase("select")) {

							String finalLabel = value;

							DDMFormField fieldDefinition = ddmStructure.getDDMFormField(fv.getName());

							if (fieldDefinition != null) {
								Map<String, LocalizedValue> options = fieldDefinition.getDDMFormFieldOptions()
										.getOptions();

								if (options != null && options.containsKey(value)) {
									finalLabel = options.get(value).getString(threadLocale);
								}
							}

							description.append(finalLabel);
						} else {
							description.append(value);
						}
						
						description.append("</p>");
					}

					TicketCreateDTO ticketDTO = null;
					try {
						ticketDTO = _odooApiClientService.createTicket(contactName, email, grupo, asunto,
								description.toString(), endpoint);

						_log.error("No creo el formulario");
					} catch (Exception e) {
						_log.error("Se ha producido un error al crear el ticket para el record "
								+ record.getFormInstanceRecordId(), e);
					}

					String token = "";
					String ticketId = "";

					if (ticketDTO != null) {
						token = ticketDTO.token();
						ticketId = ticketDTO.number();
					}

					// actualizamos el existente
					pendingTicket.setTicketId(ticketId);
					pendingTicket.setToken(token);
					pendingTicket.setEmail(email);

					_ticketLocalService.updateTicket(pendingTicket);

					_log.info("****************************************************");
					_log.info("   TICKET SINCRONIZADO CON ÉXITO");
					_log.info("   Ticket DB ID: " + pendingTicket.getPrimaryKey());
					_log.info("   ID para la API: " + ticketId);
					_log.info("   Email: " + pendingTicket.getEmail());
					_log.info("   Token: " + token);
					_log.info("   Descripción: " + description.toString());
					_log.info("****************************************************");
				} catch (Exception e) {
					_log.error("Error reprocesando ticket con id=" + pendingTicket.getPrimaryKey(), e);
				}
			}
		} catch (ConfigurationException e) {
			_log.error("Error en la configuración del sistema de tickets de los formularios: " + e.getMessage(), e);
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

		for (String line : formsTicketConfig) {

			String[] parts = line.split("\\,");

			if (parts.length != 6) {
				continue;
			}

			long formId = Long.parseLong(parts[0]);
			String contactField = parts[1];
			String contactNameField = parts[2];
			String contactSubject = parts[3];
			String grupo = parts[4];
			String endpoint = parts[5];

			_formConfigMap.put(formId, new FormConfig(contactField, contactNameField, contactSubject, grupo, endpoint));
		}
	}

	@Override
	public String getName() {
		return "VTCFomentoScheduler";
	}

	private static final Log _log = LogFactoryUtil.getLog(TicketSyncScheduler.class);

	@Reference
	private ConfigurationProvider _configurationProvider;

	@Reference
	private TicketLocalService _ticketLocalService;

	@Reference
	private DDMFormInstanceRecordLocalService _ddmFormInstanceRecordLocalService;

	@Reference
	private DDMStructureLocalService _ddmStructureLocalService;

	@Reference
	private OdooApiClientService _odooApiClientService;

	private Map<Long, FormConfig> _formConfigMap = new HashMap<>();

	private static final Pattern _JSON_ARRAY_PATTERN = Pattern.compile("\\[\"(.*)\"\\]");

}