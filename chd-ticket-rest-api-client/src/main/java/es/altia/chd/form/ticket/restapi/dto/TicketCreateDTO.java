package es.altia.chd.form.ticket.restapi.dto;

public record TicketCreateDTO(
    int id,
    String subject,
    String number,
    String token,
    String state
) {}
