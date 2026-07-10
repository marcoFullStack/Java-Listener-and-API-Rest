package es.altia.chd.ticket.gestion.portlet;

import com.liferay.portal.kernel.portlet.LiferayPortletResponse;
import com.liferay.portal.kernel.portlet.LiferayPortletURL;
import com.liferay.portal.kernel.portlet.bridges.mvc.BaseMVCActionCommand;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCActionCommand;
import com.liferay.portal.kernel.servlet.SessionErrors;
import com.liferay.portal.kernel.servlet.SessionMessages;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.util.WebKeys;
import es.altia.chd.form.ticket.restapi.client.service.OdooApiClientService;
import es.altia.chd.form.ticket.restapi.dto.TicketInfoDTO;
import es.altia.chd.ticket.gestion.constants.ChdGestionTicketPortletKeys;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;

@Component(
        immediate = true,
        property = {
                "javax.portlet.name=" + ChdGestionTicketPortletKeys.CHDGESTIONTICKET,
                "mvc.command.name=consultarTicket"
        },
        service = MVCActionCommand.class
)
public class DetalleMVCActionCommand extends BaseMVCActionCommand {

    @Override
    protected void doProcessAction(ActionRequest actionRequest, ActionResponse actionResponse) throws Exception {

        String emailUser = ParamUtil.getString(actionRequest, "contactEmail").trim();
        String odooTicketId = ParamUtil.getString(actionRequest, "ticketId").toUpperCase().trim();
        String tokenUser = ParamUtil.getString(actionRequest, "token").trim();

        String endpoint = "http://77.226.250.29:8069/api/vLR7_4_3";

        try {
            System.out.println("Llamando a Odoo para: " + odooTicketId);

//            TicketInfoDTO ticketDTO = odooApiClient.readTicket(tokenUser, emailUser, odooTicketId, endpoint);
//
//            if (ticketDTO != null && ticketDTO.partnerEmail() != null && ticketDTO.partnerEmail().equalsIgnoreCase(emailUser)) {
//
//                actionRequest.setAttribute("ticketOdoo", ticketDTO);
//
//                SessionMessages.add(actionRequest, PortalUtil.getPortletId(actionRequest) + SessionMessages.KEY_SUFFIX_HIDE_DEFAULT_SUCCESS_MESSAGE);
//
//                SessionMessages.add(actionRequest, "mensaje-exito-acceso",  odooTicketId);
//
//                actionResponse.setRenderParameter("jspPage", "/detalle.jsp");
//
//                System.out.println("Ticket encontrado en Odoo. Redirigiendo a detalle...");
//            } else {
//                System.out.println("Ticket no encontrado en Odoo o el email no coincide.");
//                SessionErrors.add(actionRequest, "error-tecnico-odoo");
//                actionResponse.setRenderParameter("jspPage", "/view.jsp");
//            }

            TicketInfoDTO ticketDTO = odooApiClient.readTicket(tokenUser, emailUser, odooTicketId, endpoint);

            if (ticketDTO != null) {
                // Guardamos en el request para el primer render
                actionRequest.setAttribute("ticketOdoo", ticketDTO);

                // Guardamos en la sesión para que sobreviva
                actionRequest.getPortletSession().setAttribute(
                        "ticketOdoo", ticketDTO, javax.portlet.PortletSession.APPLICATION_SCOPE);

                String cookieValue = odooTicketId + "#" + tokenUser + "#" + emailUser;

                javax.servlet.http.Cookie ticketCookie = new javax.servlet.http.Cookie("TICKET_SESSION", cookieValue);
                ticketCookie.setMaxAge(600);
                ticketCookie.setPath("/");
                ticketCookie.setHttpOnly(true);

                javax.servlet.http.HttpServletResponse response = PortalUtil.getHttpServletResponse(actionResponse);
                response.addCookie(ticketCookie);

                // 1. Obtenemos la URL base del portal
                String portletId = (String) actionRequest.getAttribute(WebKeys.PORTLET_ID);

                // 2. Creamos una URL de Render limpia usando Liferay
                LiferayPortletResponse liferayPortletResponse = PortalUtil.getLiferayPortletResponse(actionResponse);
                LiferayPortletURL renderURL = liferayPortletResponse.createRenderURL();

                // 3. Le decimos qué comando de render ejecutar y qué ticket mostrar
                renderURL.setParameter("mvcRenderCommandName", "/ticket/detalle");
                renderURL.setParameter("ticketId", odooTicketId);

                actionResponse.sendRedirect(renderURL.toString());
            }


        } catch (Exception e) {
            System.err.println("Error técnico al consultar Odoo: " + e.getMessage());
            SessionErrors.add(actionRequest, "error-tecnico-odoo");
            e.printStackTrace();
            actionResponse.setRenderParameter("jspPage", "/view.jsp");
        }
    }

    @Reference
    private OdooApiClientService odooApiClient;
}
