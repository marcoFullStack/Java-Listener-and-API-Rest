package es.altia.chd.form.ticket.api;

import com.liferay.dynamic.data.mapping.data.provider.DDMDataProvider;
import com.liferay.dynamic.data.mapping.data.provider.DDMDataProviderException;
import com.liferay.dynamic.data.mapping.data.provider.DDMDataProviderInstanceSettings;
import com.liferay.dynamic.data.mapping.data.provider.DDMDataProviderOutputParametersSettings;
import com.liferay.dynamic.data.mapping.data.provider.DDMDataProviderRequest;
import com.liferay.dynamic.data.mapping.data.provider.DDMDataProviderResponse;
import com.liferay.dynamic.data.mapping.data.provider.settings.DDMDataProviderSettingsProvider;
import com.liferay.dynamic.data.mapping.model.DDMDataProviderInstance;
import com.liferay.dynamic.data.mapping.service.DDMDataProviderInstanceService;
import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.KeyValuePair;
import com.liferay.portal.kernel.util.Validator;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import java.net.HttpURLConnection;
import java.net.URL;

import java.util.ArrayList;
import java.util.List;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

@Component(
        property = {
                "ddm.data.provider.instance.id=odoo-data-provider-inst",
                "ddm.data.provider.name=Odoo Data Provider Oficial",
                "ddm.data.provider.type=odoo-data-provider-final",
                "languages=es_ES"
        },
        service = DDMDataProvider.class
)
public class OdooDataProvider implements DDMDataProvider {

    private static final Log _log = LogFactoryUtil.getLog(OdooDataProvider.class);

    private static final String API_KEY = "xVcZ57xCI0jWnBsW60k8yixqzceYKu0FI4WHPwp4vkhhUO75zx8lTw";

    private static final String ODOO_URL = "https://odoo15-valladolid.altia.es/api/vLR7_4_3/get_contacts" + "?domain=%5B('type_ids'%2C'in'%2C%5B2%5D)%5D&limit=500";

    @Override
    public DDMDataProviderResponse getData(DDMDataProviderRequest request) throws DDMDataProviderException {

        try {

            _log.info(">>>> LLAMANDO A ODOO");

            URL url = new URL(ODOO_URL);

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("x_api_key", API_KEY);
            connection.setRequestProperty("x-api-key", API_KEY);
            connection.setRequestProperty("Accept", "application/json");
            connection.setConnectTimeout(10000);
            connection.setReadTimeout(10000);
            int responseCode = connection.getResponseCode();
            _log.info(">>>> HTTP STATUS: " + responseCode);

            if (responseCode != 200) {
                throw new RuntimeException("Error HTTP Odoo: " + responseCode);
            }

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            StringBuilder responseBuilder = new StringBuilder();

            String line;

            while ((line = reader.readLine()) != null) {
                responseBuilder.append(line);
            }

            reader.close();

            String response = responseBuilder.toString();

            _log.info(">>>> RESPUESTA ODOO:");

            _log.info(response);

            OdooDataProviderSettings settings = _ddmDataProviderInstanceSettings.getSettings(_getDDMDataProviderInstance(request.getDDMDataProviderId()), OdooDataProviderSettings.class);

            return _createDDMDataProviderResponse(settings, response);
        }
        catch (Exception exception) {

            _log.error("ERROR EN DATA PROVIDER", exception);

            throw new DDMDataProviderException(exception);
        }
    }

    @Override
    public Class<?> getSettings() {
        return _ddmDataProviderSettingsProvider.getSettings();
    }

    private DDMDataProviderResponse
    _createDDMDataProviderResponse(OdooDataProviderSettings settings, String response) throws Exception {

        DDMDataProviderResponse.Builder builder = DDMDataProviderResponse.Builder.newBuilder();

        JSONObject jsonObject = JSONFactoryUtil.createJSONObject(response);

        JSONArray results = jsonObject.getJSONArray("results");

        _log.info(">>>> TOTAL RESULTADOS: " + results.length());

        if (settings.outputParameters() == null) {_log.error(">>>> outputParameters NULL");
            return builder.build();
        }

        _log.info(">>>> outputParameters OK: " + settings.outputParameters().length);

        for (DDMDataProviderOutputParametersSettings output : settings.outputParameters()) {
            String outputId = output.outputParameterId();
            String outputType = output.outputParameterType();
            String outputPath = output.outputParameterPath();

            _log.info(">>>> OUTPUT CONFIG -> ID: " + outputId + " | TYPE: " + outputType + " | PATH: " + outputPath);

            List<KeyValuePair> keyValuePairs = new ArrayList<>();

            for (int i = 0; i < results.length(); i++) {

                JSONObject ayto = results.getJSONObject(i);

                String id = ayto.getString("id");

                String valorVisual = ayto.getString(outputPath);

                _log.info(">>>> PARSEANDO -> ID: " + id + " | Valor (" + outputPath + "): " + valorVisual);

                if (Validator.isNotNull(valorVisual)) {keyValuePairs.add(new KeyValuePair(id, valorVisual));

                }
            }

            if ("list".equals(outputType) || "text".equals(outputType) || "key-value-pair".equals(outputType)) {

                _log.info(">>>> ENVIANDO " + keyValuePairs.size() + " OPCIONES AL BUILDER PARA EL TIPO: " + outputType);

                builder.withOutput(outputId, keyValuePairs);
            }
            else if ("number".equals(outputType)) {
                if (results.length() > 0) {
                    builder.withOutput(outputId, results.getJSONObject(0).getInt(outputPath));
                }
            }
        }

        return builder.build();
    }

    private DDMDataProviderInstance _getDDMDataProviderInstance(String instanceId) throws Exception {

        DDMDataProviderInstance instance = _ddmDataProviderInstanceService.fetchDataProviderInstanceByUuid(instanceId);

        if (instance != null) {
            return instance;
        }

        if (Validator.isNumber(instanceId)) {

            return _ddmDataProviderInstanceService.fetchDataProviderInstance(GetterUtil.getLong(instanceId));
        }

        return null;
    }

    @Reference
    private DDMDataProviderInstanceService _ddmDataProviderInstanceService;

    @Reference
    private DDMDataProviderInstanceSettings _ddmDataProviderInstanceSettings;

    @Reference(target = "(ddm.data.provider.type=odoo-data-provider-final)")
    private DDMDataProviderSettingsProvider _ddmDataProviderSettingsProvider;
}