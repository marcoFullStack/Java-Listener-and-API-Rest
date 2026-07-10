package es.altia.chd.ticket.gestion.portlet;

import com.liferay.portal.kernel.portlet.bridges.mvc.BaseMVCActionCommand;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCActionCommand;
import com.liferay.portal.kernel.servlet.SessionErrors;
import com.liferay.portal.kernel.servlet.SessionMessages;
import com.liferay.portal.kernel.upload.UploadPortletRequest;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.PortalUtil;
import es.altia.chd.form.ticket.restapi.client.service.OdooApiClientService;
import es.altia.chd.ticket.gestion.constants.ChdGestionTicketPortletKeys;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import java.io.File;
import java.nio.file.Files;
import java.util.Base64;

@Component(
        property = {
                "javax.portlet.name=" + ChdGestionTicketPortletKeys.CHDGESTIONTICKET,
                "mvc.command.name=/ticket/post_message"
        },
        service = MVCActionCommand.class
)
public class PostmessageMVCActionCommand extends BaseMVCActionCommand {

    @Override
    protected void doProcessAction(ActionRequest actionRequest, ActionResponse actionResponse) throws Exception {
        String number = ParamUtil.getString(actionRequest, "ticketNumber").trim();
        String email = ParamUtil.getString(actionRequest, "email").trim();
        String accessToken = ParamUtil.getString(actionRequest, "accessToken").trim();
        String messageBody = ParamUtil.getString(actionRequest, "messageBody").trim();
        String endpoint = "http://77.226.250.29:8069/api/vLR7_4_3";

        UploadPortletRequest uploadRequest = PortalUtil.getUploadPortletRequest(actionRequest);

        String fileName = uploadRequest.getFileName("attachment");
        File file = uploadRequest.getFile("attachment");
        String base64File = "";
        String mimetype = "";

        if (file != null && file.exists()) {
            byte[] fileContent = Files.readAllBytes(file.toPath());
            base64File = Base64.getEncoder().encodeToString(fileContent);
            mimetype = uploadRequest.getContentType("attachment");
            if (mimetype == null) {
                mimetype = Files.probeContentType(file.toPath());
            }
        }

        try{
            _odooClientService.postMessage(number, email, accessToken, messageBody, base64File, fileName, mimetype, endpoint);
            SessionMessages.add(actionRequest, "mensaje-exito-comentario");
        }catch (Exception e){
            SessionErrors.add(actionRequest, "error-comunicacion-odoo");
        }


        // Set render parameters to stay on the ticket detail page
        actionResponse.setRenderParameter("mvcRenderCommandName", "/ticket/detalle");
        actionResponse.setRenderParameter("ticketId", number);
        actionResponse.setRenderParameter("token", accessToken);
        actionResponse.setRenderParameter("contactEmail", email);
    }
    @Reference
    private OdooApiClientService _odooClientService;

}