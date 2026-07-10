create index IX_E6E2417F on CHDTICKET_Ticket (formInstanceRecordId);
create index IX_C3DCAAE8 on CHDTICKET_Ticket (ticketId[$COLUMN_LENGTH:75$], token[$COLUMN_LENGTH:75$]);