package es.altia.chd.form.ticket.restapi.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public record Attachment(

    int id,

    String name,

    @JsonProperty("datas")
    String datas,  // base64 encoded data

    @JsonProperty("mimetype")
    String mimetype

) implements Serializable {

    private static final long serialVersionUID = 1L;

}