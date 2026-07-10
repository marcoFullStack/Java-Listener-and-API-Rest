package es.altia.chd.form.ticket.restapi.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.List;

public record ChatterMessage(

    int id,
    String date,
    String author,

    @JsonProperty("message_type")
    String messageType,

    String body,

    List<Attachment> attachments

) implements Serializable {

    private static final long serialVersionUID = 1L;

    public String getFormattedDate() {
        if (date == null || date.isEmpty()) return "";
        return date.contains(".") ? date.split("\\.")[0] : date;
    }
}