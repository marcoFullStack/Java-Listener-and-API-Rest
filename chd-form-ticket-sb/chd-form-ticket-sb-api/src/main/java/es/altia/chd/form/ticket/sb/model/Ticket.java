/**
 * SPDX-FileCopyrightText: (c) 2026 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package es.altia.chd.form.ticket.sb.model;

import com.liferay.portal.kernel.annotation.ImplementationClassName;
import com.liferay.portal.kernel.model.PersistedModel;
import com.liferay.portal.kernel.util.Accessor;

import org.osgi.annotation.versioning.ProviderType;

/**
 * The extended model interface for the Ticket service. Represents a row in the &quot;CHDTICKET_Ticket&quot; database table, with each column mapped to a property of this class.
 *
 * @author Brian Wing Shun Chan
 * @see TicketModel
 * @generated
 */
@ImplementationClassName("es.altia.chd.form.ticket.sb.model.impl.TicketImpl")
@ProviderType
public interface Ticket extends PersistedModel, TicketModel {

	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify this interface directly. Add methods to <code>es.altia.chd.form.ticket.sb.model.impl.TicketImpl</code> and rerun ServiceBuilder to automatically copy the method declarations to this interface.
	 */
	public static final Accessor<Ticket, Long> ID_ACCESSOR =
		new Accessor<Ticket, Long>() {

			@Override
			public Long get(Ticket ticket) {
				return ticket.getId();
			}

			@Override
			public Class<Long> getAttributeClass() {
				return Long.class;
			}

			@Override
			public Class<Ticket> getTypeClass() {
				return Ticket.class;
			}

		};

}