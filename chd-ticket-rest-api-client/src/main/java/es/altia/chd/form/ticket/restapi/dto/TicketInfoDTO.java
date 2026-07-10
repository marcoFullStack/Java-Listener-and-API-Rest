package es.altia.chd.form.ticket.restapi.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.List;

public record TicketInfoDTO(

    int id,
    String number,

    @JsonProperty("access_token")
    String accessToken,

    String state,

    @JsonProperty("state_code")
    boolean stateCode,

    String name,
    String description,

    @JsonProperty("partner_name")
    String partnerName,

    @JsonProperty("partner_email")
    String partnerEmail,

    @JsonProperty("create_date")
    String createDate,

    List<ChatterMessage> chatter

) implements Serializable {

    private static final long serialVersionUID = 1L;

    public String getCreateDateFormatted() {
        if (createDate == null || createDate.isBlank()) {
            return "";
        }
        try {
            String datePart = createDate.split(" ")[0];

            String[] parts = datePart.split("-");

            if (parts.length == 3) {
                return parts[2] + "/" + parts[1] + "/" + parts[0];
            }

            return createDate;
        } catch (Exception e) {
            return createDate;
        }
    }
}
