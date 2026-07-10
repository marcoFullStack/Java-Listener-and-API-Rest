create table CHDTICKET_Ticket (
	id_ LONG not null primary key,
	formInstanceRecordId LONG,
	formInstanceId LONG,
	groupId LONG,
	createDate DATE null,
	ticketId VARCHAR(75) null,
	token VARCHAR(75) null,
	email VARCHAR(75) null
);