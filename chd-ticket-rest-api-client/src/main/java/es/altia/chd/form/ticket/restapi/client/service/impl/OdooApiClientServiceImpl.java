package es.altia.chd.form.ticket.restapi.client.service.impl;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import es.altia.chd.form.ticket.restapi.client.service.OdooApiClientService;
import es.altia.chd.form.ticket.restapi.dto.TicketCreateDTO;
import es.altia.chd.form.ticket.restapi.dto.TicketInfoDTO;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.List;
import java.util.Map;

@Component(service = OdooApiClientService.class)
public class OdooApiClientServiceImpl implements OdooApiClientService {

    private static final String API_ACCEPT = "application/json";
    private static final String API_CONTENT_TYPE = "application/x-www-form-urlencoded";

    private static final HttpClient CLIENT = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(10))
            .build();

    private static final ObjectMapper MAPPER = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    private String apiKey = "";

    @Activate
    protected void activate() {
        this.apiKey = "xVcZ57xCI0jWnBsW60k8yixqzceYKu0FI4WHPwp4vkhhUO75zx8lTw";
    }

    public TicketCreateDTO createTicket(String contactName, String contactEmail, Integer teamId, String name,
                                        String description, String endpoint, Integer contactId) {

        try {
            StringBuilder bodyBuilder = new StringBuilder();
            bodyBuilder.append("name=").append(URLEncoder.encode(name, StandardCharsets.UTF_8));
            bodyBuilder.append("&description=").append(URLEncoder.encode(description, StandardCharsets.UTF_8));
            bodyBuilder.append("&team_id=").append(teamId);

            if (contactId != null && contactId > 0) {
                bodyBuilder.append("&contact_id=").append(contactId);
            } else if (contactEmail != null && !contactEmail.isEmpty()) {
                bodyBuilder.append("&contact_email=").append(URLEncoder.encode(contactEmail, StandardCharsets.UTF_8));
            } else {
                bodyBuilder.append("&contact_name=").append(URLEncoder.encode(contactName, StandardCharsets.UTF_8));
            }

            String responseBody = doHttpRequest(endpoint, bodyBuilder.toString());
            return MAPPER.readValue(responseBody, TicketCreateDTO.class);

        } catch (Exception e) {
            throw new RuntimeException("[OdooApiClientService - createTicket] Error al crear el ticket", e);
        }
    }

    public TicketInfoDTO readTicket(String accessToken, String email, String ticketRef, String endpoint) {
        try {
            String path = endpoint + "/readTicket/custom";
            String body = "access_token=" + URLEncoder.encode(accessToken, StandardCharsets.UTF_8) +
                    "&email=" + URLEncoder.encode(email, StandardCharsets.UTF_8) +
                    "&ticket_ref=" + URLEncoder.encode(ticketRef, StandardCharsets.UTF_8);

            String responseBody = doHttpRequest(path, body);
            return MAPPER.readValue(responseBody, TicketInfoDTO.class);

        } catch (Exception e) {
            throw new RuntimeException("[OdooApiClientService - readTicket] Error al leer el ticket", e);
        }
    }

    public TicketInfoDTO postMessage(String number, String email, String accessToken, String messageBody,
                                     String attachments, String filename, String mimetype, String endpoint) {
        try {
            String path = endpoint + "/post_message/custom";
            String body = "number=" + URLEncoder.encode(number, StandardCharsets.UTF_8) +
                    "&email=" + URLEncoder.encode(email, StandardCharsets.UTF_8) +
                    "&access_token=" + URLEncoder.encode(accessToken, StandardCharsets.UTF_8) +
                    "&body=" + URLEncoder.encode(messageBody, StandardCharsets.UTF_8) +
                    "&attachments=" + URLEncoder.encode(attachments, StandardCharsets.UTF_8) +
                    "&filename=" + URLEncoder.encode(filename != null ? filename : "", StandardCharsets.UTF_8) +
                    "&mimetype=" + URLEncoder.encode(mimetype != null ? mimetype : "", StandardCharsets.UTF_8);

            String responseBody = doHttpRequest(path, body);
            return MAPPER.readValue(responseBody, TicketInfoDTO.class);

        } catch (Exception e) {
            throw new RuntimeException("[OdooApiClientService - postMessage] Error al enviar mensaje", e);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> getAyuntamientos() {
        try {
            String path = "https://odoo15-valladolid.altia.es/api/vLR7_4_3/get_contacts";
            String domainFilter = "[('type_ids','in',[2])]";
            String body = "domain=" + URLEncoder.encode(domainFilter, StandardCharsets.UTF_8) + "&limit=500";

            String responseBody = doHttpGetRequest(path + "?" + body);

            System.out.println(">>>> RESPUESTA DE ODOO GET_CONTACTS: " + responseBody);

            Map<String, Object> responseMap = MAPPER.readValue(responseBody, new TypeReference<Map<String, Object>>() {});

            if (responseMap != null && responseMap.containsKey("results")) {
                return (List<Map<String, Object>>) responseMap.get("results");
            }

            System.out.println(">>>> Ojo: El JSON no contenía la clave 'results'.");
            return java.util.Collections.emptyList();

        } catch (Exception e) {
            System.err.println("[OdooApiClientService] Error al conectar con Odoo get_contacts: " + e.getMessage());
            e.printStackTrace();
            return java.util.Collections.emptyList();
        }
    }

    // MÉTODO PARA PETICIONES PUT (Tickets)
    private String doHttpRequest(String path, String body) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(path))
                .header("Accept", API_ACCEPT)
                .header("x_api_key", apiKey)
                .PUT(BodyPublishers.ofString(body))
                .timeout(Duration.ofSeconds(10))
                .build();

        return sendRequest(request);
    }

    // MÉTODO NUEVO PARA PETICIONES GET (Ayuntamientos)
    private String doHttpGetRequest(String path) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(path))
                .header("Accept", API_ACCEPT)
                .header("x_api_key", apiKey)
                .header("x-api-key", apiKey)
                .GET()
                .timeout(Duration.ofSeconds(10))
                .build();

        return sendRequest(request);
    }

    private String sendRequest(HttpRequest request) throws IOException, InterruptedException {
        HttpResponse<String> response = CLIENT.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new RuntimeException("Error HTTP: " + response.statusCode() + " - " + response.body());
        }

        return response.body();
    }
}