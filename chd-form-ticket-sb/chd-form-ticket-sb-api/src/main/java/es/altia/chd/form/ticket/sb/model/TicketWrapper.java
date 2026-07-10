/**
 * SPDX-FileCopyrightText: (c) 2026 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package es.altia.chd.form.ticket.sb.model;

import com.liferay.portal.kernel.model.ModelWrapper;
import com.liferay.portal.kernel.model.wrapper.BaseModelWrapper;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * This class is a wrapper for {@link Ticket}.
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see Ticket
 * @generated
 */
public class TicketWrapper
	extends BaseModelWrapper<Ticket> implements ModelWrapper<Ticket>, Ticket {

	public TicketWrapper(Ticket ticket) {
		super(ticket);
	}

	@Override
	public Map<String, Object> getModelAttributes() {
		Map<String, Object> attributes = new HashMap<String, Object>();

		attributes.put("id", getId());
		attributes.put("formInstanceRecordId", getFormInstanceRecordId());
		attributes.put("formInstanceId", getFormInstanceId());
		attributes.put("groupId", getGroupId());
		attributes.put("createDate", getCreateDate());
		attributes.put("ticketId", getTicketId());
		attributes.put("token", getToken());
		attributes.put("email", getEmail());

		return attributes;
	}

	@Override
	public void setModelAttributes(Map<String, Object> attributes) {
		Long id = (Long)attributes.get("id");

		if (id != null) {
			setId(id);
		}

		Long formInstanceRecordId = (Long)attributes.get(
			"formInstanceRecordId");

		if (formInstanceRecordId != null) {
			setFormInstanceRecordId(formInstanceRecordId);
		}

		Long formInstanceId = (Long)attributes.get("formInstanceId");

		if (formInstanceId != null) {
			setFormInstanceId(formInstanceId);
		}

		Long groupId = (Long)attributes.get("groupId");

		if (groupId != null) {
			setGroupId(groupId);
		}

		Date createDate = (Date)attributes.get("createDate");

		if (createDate != null) {
			setCreateDate(createDate);
		}

		String ticketId = (String)attributes.get("ticketId");

		if (ticketId != null) {
			setTicketId(ticketId);
		}

		String token = (String)attributes.get("token");

		if (token != null) {
			setToken(token);
		}

		String email = (String)attributes.get("email");

		if (email != null) {
			setEmail(email);
		}
	}

	@Override
	public Ticket cloneWithOriginalValues() {
		return wrap(model.cloneWithOriginalValues());
	}

	/**
	 * Returns the create date of this ticket.
	 *
	 * @return the create date of this ticket
	 */
	@Override
	public Date getCreateDate() {
		return model.getCreateDate();
	}

	/**
	 * Returns the email of this ticket.
	 *
	 * @return the email of this ticket
	 */
	@Override
	public String getEmail() {
		return model.getEmail();
	}

	/**
	 * Returns the form instance ID of this ticket.
	 *
	 * @return the form instance ID of this ticket
	 */
	@Override
	public long getFormInstanceId() {
		return model.getFormInstanceId();
	}

	/**
	 * Returns the form instance record ID of this ticket.
	 *
	 * @return the form instance record ID of this ticket
	 */
	@Override
	public long getFormInstanceRecordId() {
		return model.getFormInstanceRecordId();
	}

	/**
	 * Returns the group ID of this ticket.
	 *
	 * @return the group ID of this ticket
	 */
	@Override
	public long getGroupId() {
		return model.getGroupId();
	}

	/**
	 * Returns the ID of this ticket.
	 *
	 * @return the ID of this ticket
	 */
	@Override
	public long getId() {
		return model.getId();
	}

	/**
	 * Returns the primary key of this ticket.
	 *
	 * @return the primary key of this ticket
	 */
	@Override
	public long getPrimaryKey() {
		return model.getPrimaryKey();
	}

	/**
	 * Returns the ticket ID of this ticket.
	 *
	 * @return the ticket ID of this ticket
	 */
	@Override
	public String getTicketId() {
		return model.getTicketId();
	}

	/**
	 * Returns the token of this ticket.
	 *
	 * @return the token of this ticket
	 */
	@Override
	public String getToken() {
		return model.getToken();
	}

	@Override
	public void persist() {
		model.persist();
	}

	/**
	 * Sets the create date of this ticket.
	 *
	 * @param createDate the create date of this ticket
	 */
	@Override
	public void setCreateDate(Date createDate) {
		model.setCreateDate(createDate);
	}

	/**
	 * Sets the email of this ticket.
	 *
	 * @param email the email of this ticket
	 */
	@Override
	public void setEmail(String email) {
		model.setEmail(email);
	}

	/**
	 * Sets the form instance ID of this ticket.
	 *
	 * @param formInstanceId the form instance ID of this ticket
	 */
	@Override
	public void setFormInstanceId(long formInstanceId) {
		model.setFormInstanceId(formInstanceId);
	}

	/**
	 * Sets the form instance record ID of this ticket.
	 *
	 * @param formInstanceRecordId the form instance record ID of this ticket
	 */
	@Override
	public void setFormInstanceRecordId(long formInstanceRecordId) {
		model.setFormInstanceRecordId(formInstanceRecordId);
	}

	/**
	 * Sets the group ID of this ticket.
	 *
	 * @param groupId the group ID of this ticket
	 */
	@Override
	public void setGroupId(long groupId) {
		model.setGroupId(groupId);
	}

	/**
	 * Sets the ID of this ticket.
	 *
	 * @param id the ID of this ticket
	 */
	@Override
	public void setId(long id) {
		model.setId(id);
	}

	/**
	 * Sets the primary key of this ticket.
	 *
	 * @param primaryKey the primary key of this ticket
	 */
	@Override
	public void setPrimaryKey(long primaryKey) {
		model.setPrimaryKey(primaryKey);
	}

	/**
	 * Sets the ticket ID of this ticket.
	 *
	 * @param ticketId the ticket ID of this ticket
	 */
	@Override
	public void setTicketId(String ticketId) {
		model.setTicketId(ticketId);
	}

	/**
	 * Sets the token of this ticket.
	 *
	 * @param token the token of this ticket
	 */
	@Override
	public void setToken(String token) {
		model.setToken(token);
	}

	@Override
	public String toXmlString() {
		return model.toXmlString();
	}

	@Override
	protected TicketWrapper wrap(Ticket ticket) {
		return new TicketWrapper(ticket);
	}

}