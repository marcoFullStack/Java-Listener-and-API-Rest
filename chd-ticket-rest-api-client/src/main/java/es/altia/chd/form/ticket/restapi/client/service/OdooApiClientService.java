package es.altia.chd.form.ticket.restapi.client.service;

import es.altia.chd.form.ticket.restapi.dto.TicketCreateDTO;
import es.altia.chd.form.ticket.restapi.dto.TicketInfoDTO;

import java.util.List;
import java.util.Map;

public interface OdooApiClientService {
    
    /**
     * Crea el ticket.
     * 
     * La respuesta incluye el id del ticket y el token de acceso/validación
     * 
     * @param contactName
     * @param contactEmail
     * @param teamId
     * @param name
     * @param description
     * @param endpoint
	 * @param contactId
     * @return
     */
    public TicketCreateDTO createTicket(String contactName, String contactEmail, Integer teamId,
		String name, String description, String endpoint, Integer contactId);
    
	
	/**
	 * Recupera la información del ticket.
	 * 
	 * @param accessToken
	 * @param email
	 * @param ticketRef
	 * @param endpoint
	 * @return
	 */
    public TicketInfoDTO readTicket(String accessToken, String email, String ticketRef, String endpoint);

    /**
     * Publica un mensaje asociado al ticket 
     * 
     * @param number
     * @param email
     * @param accessToken
     * @param messageBody
     * @param attachments base64 encoded file data
     * @param filename
     * @param mimetype
     * @param endpoint
     * @return
     */
    public TicketInfoDTO postMessage(String number, String email, String accessToken, String messageBody,
    		String attachments, String filename, String mimetype, String endpoint);

	public List<Map<String, Object>> getAyuntamientos();
}