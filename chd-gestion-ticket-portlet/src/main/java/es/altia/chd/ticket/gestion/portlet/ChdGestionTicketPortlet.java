package es.altia.chd.ticket.gestion.portlet;

import com.liferay.portal.kernel.portlet.bridges.mvc.MVCPortlet;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;

import javax.portlet.Portlet;
import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import es.altia.chd.form.ticket.restapi.client.service.OdooApiClientService;
import es.altia.chd.form.ticket.restapi.dto.TicketInfoDTO;
import es.altia.chd.ticket.gestion.constants.ChdGestionTicketPortletKeys;

import java.io.IOException;

@Component(
		property = {
				"com.liferay.portlet.footer-portlet-javascript=/js/main.js",
				"com.liferay.portlet.display-category=category.sample",
				"com.liferay.portlet.header-portlet-css=/css/main.css",
				"com.liferay.portlet.instanceable=true",
				"com.liferay.portlet.requires-namespaced-parameters=false",
				"javax.portlet.display-name=ChdGestionTicket",
				"javax.portlet.init-param.template-path=/",
				"javax.portlet.init-param.view-template=/view.jsp",
				"javax.portlet.name=" + ChdGestionTicketPortletKeys.CHDGESTIONTICKET,
				"javax.portlet.resource-bundle=content.Language",
				"javax.portlet.security-role-ref=power-user,user"
		},
		service = Portlet.class
)
public class ChdGestionTicketPortlet extends MVCPortlet {

	@Override
	public void doView(RenderRequest renderRequest, RenderResponse renderResponse)
			throws IOException, PortletException {

		String odooTicketId = ParamUtil.getString(renderRequest, "ticketId");
		String token = ParamUtil.getString(renderRequest, "token");
		String email = ParamUtil.getString(renderRequest, "contactEmail");

		String endpoint = "http://77.226.250.29:8069/api/vLR7_4_3";

		if (Validator.isNotNull(odooTicketId) && Validator.isNotNull(token)) {
			try {
				TicketInfoDTO ticketDTO = _odooClientService.readTicket(token, email, odooTicketId, endpoint);

				if (ticketDTO != null) {
					renderRequest.setAttribute("ticketOdoo", ticketDTO);
				}
			} catch (Exception e) {
				_log.error("Error al cargar el ticket en doView: " + e.getMessage());
			}
		}

		super.doView(renderRequest, renderResponse);
	}

	@Reference
	private OdooApiClientService _odooClientService;

	private static final Log _log = LogFactoryUtil.getLog(ChdGestionTicketPortlet.class);
}


