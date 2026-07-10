package es.altia.chd.formTicket.rest;

import java.util.Collections;
import java.util.Set;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Application;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;


import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

@Component(
        property = {
                "osgi.jaxrs.application.base=/chd-tickets",
                "auth.verifier.guest.allowed=true",
                "liferay.auth.verifier=false",
                "liferay.oauth2=false"
        },
        service = Application.class
)
public class TicketRestApp extends Application {

    @Override
    public Set<Object> getSingletons() {
        return Collections.<Object>singleton(this);
    }

    @GET
    @Path("/status")
    @Produces(MediaType.APPLICATION_JSON)
    public Response checkStatus(
            @QueryParam("id") String paramId,
            @QueryParam("token") String paramToken,
            @QueryParam("email") String paramEmail) {


        System.out.println(">>> API LOG: Buscando ID: " + paramId + " | Email: " + paramEmail);

        if (paramId == null || paramId.isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"error\": \"El ID enviado es nulo o cero\"}").build();
        }

        try {

            Ticket ticket = _ticketLocalService.fetchTicketByOdooAuth(paramId, paramToken);

            if (ticket == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("{\"error\": \"Ticket no encontrado con esos datos\"}").build();
            }


            if (ticket.getContactoEmail().equalsIgnoreCase(paramEmail)) {

                String descJson = ticket.getDescription().replace("\"", "\\\"");

                return Response.ok("{\"status\": \"" + ticket.getStatus() +
                        "\", \"description\": \"" + descJson + "\"}").build();
            }

            return Response.status(Response.Status.FORBIDDEN)
                    .entity("{\"error\": \"El email no coincide\"}").build();

        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\": \"Error interno: " + e.getMessage() + "\"}").build();
        }
    }

    @Reference
    private TicketLocalService _ticketLocalService;
}