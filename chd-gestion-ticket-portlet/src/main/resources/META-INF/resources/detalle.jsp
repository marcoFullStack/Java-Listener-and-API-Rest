<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/init.jsp"%>
<%@ page import="es.altia.chd.form.ticket.restapi.dto.TicketInfoDTO" %>
<%@ page import="es.altia.chd.form.ticket.restapi.dto.ChatterMessage" %>
<%@ page import="es.altia.chd.form.ticket.restapi.dto.Attachment" %>
<%@ page import="es.altia.chd.ticket.gestion.portlet.TicketDataProcessor" %>
<%@ page import="com.liferay.portal.kernel.util.Validator" %>
<%@ page import="com.liferay.portal.kernel.servlet.SessionMessages" %>
<%@ page import="com.liferay.portal.kernel.servlet.SessionErrors" %>
<%
    TicketInfoDTO ticket = (TicketInfoDTO) request.getAttribute("ticketOdoo");
    if (ticket == null) {
        ticket = (TicketInfoDTO) renderRequest.getPortletSession().getAttribute("ticketOdoo", javax.portlet.PortletSession.APPLICATION_SCOPE);
    }
%>

<liferay-ui:success key="mensaje-exito-comentario" message="mensaje-exito-comentario" />
<liferay-ui:error key="error-envio-comentario" message="error-comunicacion-odoo" />
<liferay-ui:success key="mensaje-exito-acceso" message="mensaje-exito-acceso" >
    <%
    String ticketId = (String) SessionMessages.get(renderRequest, "mensaje-exito-acceso");
    %>
<liferay-ui:message arguments="<%= new Object[] {ticketId} %>" key="mensaje-exito-acceso" />
</liferay-ui:success>
<liferay-ui:error key="error-auth-ticket" message="error-auth-ticket" />
<liferay-ui:error key="error-tecnico-odoo" message="error-tecnico-odoo" />

<%
    TicketInfoDTO ticketOdoo = (TicketInfoDTO) request.getAttribute("ticketOdoo");
    String descripcionLimpia = (String) request.getAttribute("descripcionLimpia");
    String state = (String) request.getAttribute("state");
    if ((state == null || state.isEmpty()) && ticketOdoo != null) {
            state = TicketDataProcessor.normalizeState(ticketOdoo);
        } else if (state == null) {
            state = "nuevo";
        }
    if ((descripcionLimpia == null || descripcionLimpia.equals("null")) && ticketOdoo != null) {
            descripcionLimpia = TicketDataProcessor.processDescription(ticketOdoo);
        }
    boolean isClosed = (request.getAttribute("isClosed") != null) ? (Boolean) request.getAttribute("isClosed") : TicketDataProcessor.isTicketClosed(state);

%>

<portlet:renderURL var="backURL">
    <portlet:param name="jspPage" value="/view.jsp" />
</portlet:renderURL>

<div class="ticket-portlet container-fluid py-4">
    <a href="${backURL}" class="btn btn-link mb-3 p-0 text-muted">
        &larr; Volver a la consulta
    </a>

    <% if (ticketOdoo != null) { %>
        <div class="card shadow-sm border-0 mb-5" data-ticket-state="<%= state %>">
            <div class="card-header bg-white py-3">
                <div class="d-flex justify-content-between align-items-center">
                    <h5 class="state-color mb-0 font-weight-bold">
                        <%= ticketOdoo.number() %> &bull; Helpdesk Sistemas
                    </h5>
                    <span class="state-badge px-3 py-2">
                        <%= ticketOdoo.state().toUpperCase() %>
                    </span>
                </div>
            </div>

            <div class="card-body">
                <h2 class="mb-4 font-weight-bold text-dark"><%= ticketOdoo.name() %></h2>

                <div class="row text-secondary mb-4">
                    <div class="col-md-4">
                        <small class="d-block text-uppercase font-weight-bold">Creado en</small>
                        <span><%= ticketOdoo.getCreateDateFormatted() %></span>
                    </div>
                    <div class="col-md-8">
                        <small class="d-block text-uppercase font-weight-bold">Solicitante</small>
                        <span><%= ticketOdoo.partnerName() %> (<%= ticketOdoo.partnerEmail() %>)</span>
                    </div>
                </div>

                <hr>

                <div class="mt-4">
                    <h5 class="font-weight-bold text-dark">Descripción de la incidencia:</h5>
                    <div class="description-content" style="white-space: pre-wrap; line-height: 1.6;">
                        <%= descripcionLimpia %>
                    </div>
                </div>
            </div>
        </div>

        <% if (ticketOdoo.chatter() != null && !ticketOdoo.chatter().isEmpty()) { %>
            <div class="ticket-portlet mt-5">
                <div class="chatter-main-container shadow-sm rounded border bg-white p-4">
                    <h5 class="font-weight-bold text-dark mb-4 pl-2">Historial de conversación:</h5>
                    <div class="d-flex flex-column">
                        <%
                        String cliente = ticketOdoo.partnerName();
                        for (ChatterMessage msg : ticketOdoo.chatter()) {
                            boolean esCliente = msg.author().equalsIgnoreCase(cliente);
                            String fechaFormateada = TicketDataProcessor.formatDate(msg.date());
                        %>
                            <div class="d-flex <%= esCliente ? "justify-content-end" : "justify-content-start" %> mb-4">

                                <div class="message-bubble shadow-sm p-3"
                                     style="width: 90%;
                                            border-radius: 18px;
                                            background-color: <%= esCliente ? "#f8f9fa" : "#e3f2fd" %>;
                                            border: 1px solid <%= esCliente ? "#e9ecef" : "#cde3f5" %>;
                                            <%= esCliente ? "border-bottom-right-radius: 2px;" : "border-bottom-left-radius: 2px;" %>">

                                    <div class="d-flex align-items-center mb-2 <%= esCliente ? "flex-row-reverse justify-content-between" : "justify-content-between" %>">

                                        <strong style="font-size: 0.85rem; color: <%= esCliente ? "#6c757d" : "#0d6efd" %>;">
                                            <%= msg.author() %>
                                        </strong>

                                        <small class="text-muted" style="font-size: 0.7rem;">
                                            <%= fechaFormateada %>
                                        </small>
                                    </div>

                                    <div class="chatter-content text-dark" style="font-size: 0.95rem; line-height: 1.4; <%= esCliente ? "text-align: right;" : "" %>">
                                        <%= msg.body() %>
                                    </div>

                                    <% if (msg.attachments() != null && !msg.attachments().isEmpty()) { %>
                                        <div class="mt-2 pt-2 border-top d-flex <%= esCliente ? "justify-content-end" : "" %>" style="border-color: rgba(0,0,0,0.05) !important;">
                                            <% for (Attachment att : msg.attachments()) { %>
                                                <a href="data:<%= att.mimetype() %>;base64,<%= att.datas() %>"
                                                   download="<%= att.name() %>"
                                                   class="btn btn-sm btn-white border bg-white mt-1 shadow-sm ml-1"
                                                   style="border-radius: 10px; font-size: 0.8rem;">
                                                    <i class="fa fa-download text-secondary"></i> <%= att.name() %>
                                                </a>
                                            <% } %>
                                        </div>
                                    <% } %>
                                </div>
                            </div>
                        <% } %>
                    </div>
                </div>
            </div>
        <% } %>

        <div class="reply-section mt-4 bg-white rounded border bg-white p-4">
            <% if (isClosed) { %>
                <div class="alert alert-info border-0 shadow-sm mb-4">
                    <i class="fa fa-lock mr-2"></i>
                    <liferay-ui:message key="label-ticket-cerrado" />
                </div>
            <% } else { %>
            <h5 class="font-weight-bold text-dark">Añadir comentario:</h5>
            <% } %>
            <portlet:actionURL name="/ticket/post_message" var="postMessageURL"/>
            <form action="${postMessageURL}" method="post" name="<portlet:namespace />fm" enctype="multipart/form-data">
                <input type="hidden" name="<portlet:namespace />ticketNumber" value="<%= ticketOdoo.number() %>" />
                <input type="hidden" name="<portlet:namespace />accessToken" value="<%= ticketOdoo.accessToken() %>" />
                <input type="hidden" name="<portlet:namespace />email" value="<%= ticketOdoo.partnerEmail() %>" />

                <div class="form-group">
                    <textarea name="<portlet:namespace />messageBody"
                              class="form-control"
                              rows="4"
                              placeholder="<%= isClosed ? "Chat cerrado por estado del ticket." : "Escriba aquí su mensaje..." %>"
                              <%= isClosed ? "disabled" : "required" %>></textarea>
                </div>

                <div class="form-group mt-2">
                    <label class="text-secondary small font-weight-bold">Adjuntar archivos (opcional):</label>
                    <input type="file" name="<portlet:namespace />attachment" class="form-control-file" multiple <%= isClosed ? "disabled" : "" %> />
                </div>

                <div class="text-right mt-3">
                    <button type="submit" class="btn btn-primary px-4" <%= isClosed ? "disabled" : "" %>>
                        <%= isClosed ? "Consulta cerrada" : "Enviar mensaje" %>
                    </button>
                </div>
            </form>
        </div>

    <% } else { %>
        <div class="alert alert-warning shadow-sm">
            <i class="fa fa-exclamation-triangle"></i> No se han podido cargar los detalles del ticket. Por favor, vuelva a intentarlo.
        </div>
    <% } %>
</div>