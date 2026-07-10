package es.altia.chd.ticket.gestion.portlet;

import com.liferay.portal.kernel.portlet.bridges.mvc.MVCRenderCommand;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.util.Validator;

import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import es.altia.chd.form.ticket.restapi.client.service.OdooApiClientService;
import es.altia.chd.form.ticket.restapi.dto.TicketInfoDTO;
import es.altia.chd.ticket.gestion.constants.ChdGestionTicketPortletKeys;

@Component(
        immediate = true,
        property = {
                "javax.portlet.name=" + ChdGestionTicketPortletKeys.CHDGESTIONTICKET,
                "mvc.command.name=/ticket/detalle",
        },
        service = MVCRenderCommand.class
)

public class DetalleMVCRenderCommand implements MVCRenderCommand {

    @Override
    public String render(RenderRequest renderRequest, RenderResponse renderResponse){
        Object ticketOdoo = renderRequest.getAttribute("ticketOdoo");

        if(ticketOdoo == null) {
            ticketOdoo = recuperarTicketDeCookie(renderRequest);
        }

        if (ticketOdoo == null) {
            String odooTicketId = ParamUtil.getString(renderRequest, "ticketId");
            String token = ParamUtil.getString(renderRequest, "token");
            String email = ParamUtil.getString(renderRequest, "contactEmail");

            String endpoint = "http://77.226.250.29:8069/api/vLR7_4_3";

            if (Validator.isNotNull(odooTicketId) && Validator.isNotNull(token)) {
                try {
                    ticketOdoo = _odooClientService.readTicket(token, email, odooTicketId, endpoint);
                } catch (Exception e) {
                    return "/view.jsp";
                }
            }
        }
        if(ticketOdoo instanceof TicketInfoDTO) {
            TicketInfoDTO ticketDTO = (TicketInfoDTO) ticketOdoo;
            processTicketData(ticketDTO, renderRequest);
            renderRequest.setAttribute("ticketOdoo", ticketDTO);
            System.out.println("-------------- todo ok -----------------");
            return "/detalle.jsp";
        }
        return "/view.jsp";
    }

    private void processTicketData(TicketInfoDTO ticket, RenderRequest renderRequest) {
        String descripcionLimpia = TicketDataProcessor.processDescription(ticket);
        renderRequest.setAttribute("descripcionLimpia", descripcionLimpia);

        String state = TicketDataProcessor.normalizeState(ticket);
        renderRequest.setAttribute("state", state);

        boolean isClosed = TicketDataProcessor.isTicketClosed(state);
        renderRequest.setAttribute("isClosed", isClosed);
    }

    private TicketInfoDTO recuperarTicketDeCookie(RenderRequest renderRequest){
        javax.servlet.http.HttpServletRequest httpServletRequest = PortalUtil.getHttpServletRequest(renderRequest);
        javax.servlet.http.HttpServletRequest originalRequest = PortalUtil.getOriginalServletRequest(httpServletRequest);
        javax.servlet.http.Cookie[] cookies = originalRequest.getCookies();

        if (cookies != null) {
            for (javax.servlet.http.Cookie c : cookies){
            if ("TICKET_SESSION".equals(c.getName()) && com.liferay.portal.kernel.util.Validator.isNotNull(c.getValue())){
               try{
                   String[] data = c.getValue().split("#");
                   if(data.length >= 2){
                       String id = data[0];
                       String token = data[1];
                       String email = (data.length > 2) ? data[2] : "";
                       return _odooClientService.readTicket(token, email, id, "http://77.226.250.29:8069/api/vLR7_4_3");
                   }
               } catch(Exception e){
                   System.err.println("Error al recuperar el ticket de la cookie:");
               }
            }
            }
        }
        return null;
    }

    @Reference
    private OdooApiClientService _odooClientService;
}