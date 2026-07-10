/**
 * SPDX-FileCopyrightText: (c) 2026 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package es.altia.chd.form.ticket.sb.model.impl;

import com.liferay.petra.lang.HashUtil;
import com.liferay.petra.string.StringBundler;
import com.liferay.portal.kernel.model.CacheModel;

import es.altia.chd.form.ticket.sb.model.Ticket;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import java.util.Date;

/**
 * The cache model class for representing Ticket in entity cache.
 *
 * @author Brian Wing Shun Chan
 * @generated
 */
public class TicketCacheModel implements CacheModel<Ticket>, Externalizable {

	@Override
	public boolean equals(Object object) {
		if (this == object) {
			return true;
		}

		if (!(object instanceof TicketCacheModel)) {
			return false;
		}

		TicketCacheModel ticketCacheModel = (TicketCacheModel)object;

		if (id == ticketCacheModel.id) {
			return true;
		}

		return false;
	}

	@Override
	public int hashCode() {
		return HashUtil.hash(0, id);
	}

	@Override
	public String toString() {
		StringBundler sb = new StringBundler(17);

		sb.append("{id=");
		sb.append(id);
		sb.append(", formInstanceRecordId=");
		sb.append(formInstanceRecordId);
		sb.append(", formInstanceId=");
		sb.append(formInstanceId);
		sb.append(", groupId=");
		sb.append(groupId);
		sb.append(", createDate=");
		sb.append(createDate);
		sb.append(", ticketId=");
		sb.append(ticketId);
		sb.append(", token=");
		sb.append(token);
		sb.append(", email=");
		sb.append(email);
		sb.append("}");

		return sb.toString();
	}

	@Override
	public Ticket toEntityModel() {
		TicketImpl ticketImpl = new TicketImpl();

		ticketImpl.setId(id);
		ticketImpl.setFormInstanceRecordId(formInstanceRecordId);
		ticketImpl.setFormInstanceId(formInstanceId);
		ticketImpl.setGroupId(groupId);

		if (createDate == Long.MIN_VALUE) {
			ticketImpl.setCreateDate(null);
		}
		else {
			ticketImpl.setCreateDate(new Date(createDate));
		}

		if (ticketId == null) {
			ticketImpl.setTicketId("");
		}
		else {
			ticketImpl.setTicketId(ticketId);
		}

		if (token == null) {
			ticketImpl.setToken("");
		}
		else {
			ticketImpl.setToken(token);
		}

		if (email == null) {
			ticketImpl.setEmail("");
		}
		else {
			ticketImpl.setEmail(email);
		}

		ticketImpl.resetOriginalValues();

		return ticketImpl;
	}

	@Override
	public void readExternal(ObjectInput objectInput) throws IOException {
		id = objectInput.readLong();

		formInstanceRecordId = objectInput.readLong();

		formInstanceId = objectInput.readLong();

		groupId = objectInput.readLong();
		createDate = objectInput.readLong();
		ticketId = objectInput.readUTF();
		token = objectInput.readUTF();
		email = objectInput.readUTF();
	}

	@Override
	public void writeExternal(ObjectOutput objectOutput) throws IOException {
		objectOutput.writeLong(id);

		objectOutput.writeLong(formInstanceRecordId);

		objectOutput.writeLong(formInstanceId);

		objectOutput.writeLong(groupId);
		objectOutput.writeLong(createDate);

		if (ticketId == null) {
			objectOutput.writeUTF("");
		}
		else {
			objectOutput.writeUTF(ticketId);
		}

		if (token == null) {
			objectOutput.writeUTF("");
		}
		else {
			objectOutput.writeUTF(token);
		}

		if (email == null) {
			objectOutput.writeUTF("");
		}
		else {
			objectOutput.writeUTF(email);
		}
	}

	public long id;
	public long formInstanceRecordId;
	public long formInstanceId;
	public long groupId;
	public long createDate;
	public String ticketId;
	public String token;
	public String email;

}