/**
 * SPDX-FileCopyrightText: (c) 2026 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package es.altia.chd.form.ticket.sb.service.impl;

import com.liferay.portal.aop.AopService;

import org.osgi.service.component.annotations.Component;

import es.altia.chd.form.ticket.sb.model.Ticket;
import es.altia.chd.form.ticket.sb.service.base.TicketLocalServiceBaseImpl;
import es.altia.chd.form.ticket.sb.service.persistence.TicketUtil;

/**
 * @author Brian Wing Shun Chan
 */
@Component(
	property = "model.class.name=es.altia.chd.form.ticket.sb.model.Ticket",
	service = AopService.class
)
public class TicketLocalServiceImpl extends TicketLocalServiceBaseImpl {
	
	public Ticket addTicket(
			long groupId, long formInstanceId, long recordId,
			String ticketId, String token, String email) {

		long id = counterLocalService.increment(Ticket.class.getName());
		Ticket ticket = TicketUtil.create(id);

		ticket.setGroupId(groupId);
		ticket.setFormInstanceId(formInstanceId);
		ticket.setFormInstanceRecordId(recordId);
		ticket.setToken(token);
		ticket.setEmail(email);
		ticket.setTicketId(ticketId);

		ticket.setCreateDate(new java.util.Date());

		return TicketUtil.update(ticket);
	}
	
	public Ticket fetchByOdooAuth(String odooTicketId, String odooToken) {
	    return TicketUtil.fetchByOdooAuth(odooTicketId, odooToken);
	}
}