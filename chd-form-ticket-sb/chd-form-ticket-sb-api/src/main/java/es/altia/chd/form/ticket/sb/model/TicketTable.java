/**
 * SPDX-FileCopyrightText: (c) 2026 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package es.altia.chd.form.ticket.sb.model;

import com.liferay.petra.sql.dsl.Column;
import com.liferay.petra.sql.dsl.base.BaseTable;

import java.sql.Types;

import java.util.Date;

/**
 * The table class for the &quot;CHDTICKET_Ticket&quot; database table.
 *
 * @author Brian Wing Shun Chan
 * @see Ticket
 * @generated
 */
public class TicketTable extends BaseTable<TicketTable> {

	public static final TicketTable INSTANCE = new TicketTable();

	public final Column<TicketTable, Long> id = createColumn(
		"id_", Long.class, Types.BIGINT, Column.FLAG_PRIMARY);
	public final Column<TicketTable, Long> formInstanceRecordId = createColumn(
		"formInstanceRecordId", Long.class, Types.BIGINT, Column.FLAG_DEFAULT);
	public final Column<TicketTable, Long> formInstanceId = createColumn(
		"formInstanceId", Long.class, Types.BIGINT, Column.FLAG_DEFAULT);
	public final Column<TicketTable, Long> groupId = createColumn(
		"groupId", Long.class, Types.BIGINT, Column.FLAG_DEFAULT);
	public final Column<TicketTable, Date> createDate = createColumn(
		"createDate", Date.class, Types.TIMESTAMP, Column.FLAG_DEFAULT);
	public final Column<TicketTable, String> ticketId = createColumn(
		"ticketId", String.class, Types.VARCHAR, Column.FLAG_DEFAULT);
	public final Column<TicketTable, String> token = createColumn(
		"token", String.class, Types.VARCHAR, Column.FLAG_DEFAULT);
	public final Column<TicketTable, String> email = createColumn(
		"email", String.class, Types.VARCHAR, Column.FLAG_DEFAULT);

	private TicketTable() {
		super("CHDTICKET_Ticket", TicketTable::new);
	}

}