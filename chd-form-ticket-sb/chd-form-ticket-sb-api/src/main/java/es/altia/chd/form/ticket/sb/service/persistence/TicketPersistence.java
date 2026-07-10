/**
 * SPDX-FileCopyrightText: (c) 2026 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package es.altia.chd.form.ticket.sb.service.persistence;

import com.liferay.portal.kernel.service.persistence.BasePersistence;

import es.altia.chd.form.ticket.sb.exception.NoSuchTicketException;
import es.altia.chd.form.ticket.sb.model.Ticket;

import org.osgi.annotation.versioning.ProviderType;

/**
 * The persistence interface for the ticket service.
 *
 * <p>
 * Caching information and settings can be found in <code>portal.properties</code>
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see TicketUtil
 * @generated
 */
@ProviderType
public interface TicketPersistence extends BasePersistence<Ticket> {

	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this interface directly. Always use {@link TicketUtil} to access the ticket persistence. Modify <code>service.xml</code> and rerun ServiceBuilder to regenerate this interface.
	 */

	/**
	 * Returns the ticket where ticketId = &#63; and token = &#63; or throws a <code>NoSuchTicketException</code> if it could not be found.
	 *
	 * @param ticketId the ticket ID
	 * @param token the token
	 * @return the matching ticket
	 * @throws NoSuchTicketException if a matching ticket could not be found
	 */
	public Ticket findByOdooAuth(String ticketId, String token)
		throws NoSuchTicketException;

	/**
	 * Returns the ticket where ticketId = &#63; and token = &#63; or returns <code>null</code> if it could not be found. Uses the finder cache.
	 *
	 * @param ticketId the ticket ID
	 * @param token the token
	 * @return the matching ticket, or <code>null</code> if a matching ticket could not be found
	 */
	public Ticket fetchByOdooAuth(String ticketId, String token);

	/**
	 * Returns the ticket where ticketId = &#63; and token = &#63; or returns <code>null</code> if it could not be found, optionally using the finder cache.
	 *
	 * @param ticketId the ticket ID
	 * @param token the token
	 * @param useFinderCache whether to use the finder cache
	 * @return the matching ticket, or <code>null</code> if a matching ticket could not be found
	 */
	public Ticket fetchByOdooAuth(
		String ticketId, String token, boolean useFinderCache);

	/**
	 * Removes the ticket where ticketId = &#63; and token = &#63; from the database.
	 *
	 * @param ticketId the ticket ID
	 * @param token the token
	 * @return the ticket that was removed
	 */
	public Ticket removeByOdooAuth(String ticketId, String token)
		throws NoSuchTicketException;

	/**
	 * Returns the number of tickets where ticketId = &#63; and token = &#63;.
	 *
	 * @param ticketId the ticket ID
	 * @param token the token
	 * @return the number of matching tickets
	 */
	public int countByOdooAuth(String ticketId, String token);

	/**
	 * Returns the ticket where formInstanceRecordId = &#63; or throws a <code>NoSuchTicketException</code> if it could not be found.
	 *
	 * @param formInstanceRecordId the form instance record ID
	 * @return the matching ticket
	 * @throws NoSuchTicketException if a matching ticket could not be found
	 */
	public Ticket findByFormRecordId(long formInstanceRecordId)
		throws NoSuchTicketException;

	/**
	 * Returns the ticket where formInstanceRecordId = &#63; or returns <code>null</code> if it could not be found. Uses the finder cache.
	 *
	 * @param formInstanceRecordId the form instance record ID
	 * @return the matching ticket, or <code>null</code> if a matching ticket could not be found
	 */
	public Ticket fetchByFormRecordId(long formInstanceRecordId);

	/**
	 * Returns the ticket where formInstanceRecordId = &#63; or returns <code>null</code> if it could not be found, optionally using the finder cache.
	 *
	 * @param formInstanceRecordId the form instance record ID
	 * @param useFinderCache whether to use the finder cache
	 * @return the matching ticket, or <code>null</code> if a matching ticket could not be found
	 */
	public Ticket fetchByFormRecordId(
		long formInstanceRecordId, boolean useFinderCache);

	/**
	 * Removes the ticket where formInstanceRecordId = &#63; from the database.
	 *
	 * @param formInstanceRecordId the form instance record ID
	 * @return the ticket that was removed
	 */
	public Ticket removeByFormRecordId(long formInstanceRecordId)
		throws NoSuchTicketException;

	/**
	 * Returns the number of tickets where formInstanceRecordId = &#63;.
	 *
	 * @param formInstanceRecordId the form instance record ID
	 * @return the number of matching tickets
	 */
	public int countByFormRecordId(long formInstanceRecordId);

	/**
	 * Caches the ticket in the entity cache if it is enabled.
	 *
	 * @param ticket the ticket
	 */
	public void cacheResult(Ticket ticket);

	/**
	 * Caches the tickets in the entity cache if it is enabled.
	 *
	 * @param tickets the tickets
	 */
	public void cacheResult(java.util.List<Ticket> tickets);

	/**
	 * Creates a new ticket with the primary key. Does not add the ticket to the database.
	 *
	 * @param id the primary key for the new ticket
	 * @return the new ticket
	 */
	public Ticket create(long id);

	/**
	 * Removes the ticket with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * @param id the primary key of the ticket
	 * @return the ticket that was removed
	 * @throws NoSuchTicketException if a ticket with the primary key could not be found
	 */
	public Ticket remove(long id) throws NoSuchTicketException;

	public Ticket updateImpl(Ticket ticket);

	/**
	 * Returns the ticket with the primary key or throws a <code>NoSuchTicketException</code> if it could not be found.
	 *
	 * @param id the primary key of the ticket
	 * @return the ticket
	 * @throws NoSuchTicketException if a ticket with the primary key could not be found
	 */
	public Ticket findByPrimaryKey(long id) throws NoSuchTicketException;

	/**
	 * Returns the ticket with the primary key or returns <code>null</code> if it could not be found.
	 *
	 * @param id the primary key of the ticket
	 * @return the ticket, or <code>null</code> if a ticket with the primary key could not be found
	 */
	public Ticket fetchByPrimaryKey(long id);

	/**
	 * Returns all the tickets.
	 *
	 * @return the tickets
	 */
	public java.util.List<Ticket> findAll();

	/**
	 * Returns a range of all the tickets.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>TicketModelImpl</code>.
	 * </p>
	 *
	 * @param start the lower bound of the range of tickets
	 * @param end the upper bound of the range of tickets (not inclusive)
	 * @return the range of tickets
	 */
	public java.util.List<Ticket> findAll(int start, int end);

	/**
	 * Returns an ordered range of all the tickets.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>TicketModelImpl</code>.
	 * </p>
	 *
	 * @param start the lower bound of the range of tickets
	 * @param end the upper bound of the range of tickets (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of tickets
	 */
	public java.util.List<Ticket> findAll(
		int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator<Ticket>
			orderByComparator);

	/**
	 * Returns an ordered range of all the tickets.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>TicketModelImpl</code>.
	 * </p>
	 *
	 * @param start the lower bound of the range of tickets
	 * @param end the upper bound of the range of tickets (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @param useFinderCache whether to use the finder cache
	 * @return the ordered range of tickets
	 */
	public java.util.List<Ticket> findAll(
		int start, int end,
		com.liferay.portal.kernel.util.OrderByComparator<Ticket>
			orderByComparator,
		boolean useFinderCache);

	/**
	 * Removes all the tickets from the database.
	 */
	public void removeAll();

	/**
	 * Returns the number of tickets.
	 *
	 * @return the number of tickets
	 */
	public int countAll();

}