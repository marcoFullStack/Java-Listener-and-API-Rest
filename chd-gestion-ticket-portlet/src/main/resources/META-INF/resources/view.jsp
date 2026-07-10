
<%@ include file="init.jsp" %>

<portlet:actionURL name="consultarTicket" var="consultarTicketURL"/>

<div class="ticket-portlet">
    <div class="tittle">
    <div class="locker">
        &#x1F510
    </div>
    <h1 style="font-size: 22px; font-weight: 700; margin-bottom: 6px;">Consulta tu ticket</h1>
    <p style="font-size: 14px; max-with: 400px; margin: 0 auto; line-height: 1.6;">Introduce los datos que recibiste en el email de confirmaci&oacute;n<span><br><span>cuando abriste la incidencia</p>
    </div>




    <div class="ticket-search-wrapper">
    <form action="${consultarTicketURL}" method="post">
        <liferay-ui:error key="error-ticket-no-encontrado" message="No se ha encontrado el ticket o los datos son incorrectos." />
        <label for="<portlet:namespace />contactEmail">CORREO ELECTR&Oacute;NICO</label>
        <input name="<portlet:namespace />contactEmail" id="<portlet:namespace />contactEmail" type="email" placeholder="usuario@empresa.com">
        <label for="<portlet:namespace />ticketId"> N&Uacute;MERO DE TICKET</label>
        <input name="<portlet:namespace />ticketId" id="<portlet:namespace />ticketId" type="text" placeholder="Ej: HD-2025-00482">
        <label for="<portlet:namespace />token"> TOKEN DE ACCESO</label>
        <div style="position: relative;">
            <input name="<portlet:namespace />token" id="<portlet:namespace />tokenField" type="password" placeholder="Token recibido por email" style="width: 100%; padding-right: 40px;" required>
            <button type="button" onclick="toggleToken(this, '<portlet:namespace />tokenField')" id="tokenToggle" class="toggle-password">
                <svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M1 12s4-8 11-8 11 8 11 8-4 8-11 8-11-8-11-8z"></path><circle cx="12" cy="12" r="3"></circle></svg>
            </button>
        </div>

        <button type="submit">Acceder al ticket</button>
    </form>

    <%--<c:if test="${not empty descripcion}">
        <div class="result-box" style="background-color: #f8f9fa; padding: 20px; border-radius: 8px; border-left: 5px solid #007bff;">
            <h4 class="mb-3">Detalles de la Solicitud:</h4>

            <div class="ticket-description-list">
                    ${descripcion}
            </div>
        </div>
    </c:if>--%>
    </div>

    <div class="addBox">
    <div class="info-icon">i</div>
    <p>El token de acceso se envi&oacute; a tu correo cuando se abri&oacute; la incidencia. Busca un email con el asunto <b>"Tu ticket ha sido registrado - HTXXXXX".</b></p>
    </div>
</div>