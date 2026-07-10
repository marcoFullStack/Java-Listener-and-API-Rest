/**
 * SPDX-FileCopyrightText: (c) 2026 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package es.altia.chd.form.ticket.sb.service.persistence;

import com.liferay.portal.kernel.dao.orm.DynamicQuery;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.util.OrderByComparator;

import es.altia.chd.form.ticket.sb.model.Ticket;

import java.io.Serializable;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * The persistence utility for the ticket service. This utility wraps <code>es.altia.chd.form.ticket.sb.service.persistence.impl.TicketPersistenceImpl</code> and provides direct access to the database for CRUD operations. This utility should only be used by the service layer, as it must operate within a transaction. Never access this utility in a JSP, controller, model, or other front-end class.
 *
 * <p>
 * Caching information and settings can be found in <code>portal.properties</code>
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see TicketPersistence
 * @generated
 */
public class TicketUtil {

	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify this class directly. Modify <code>service.xml</code> and rerun ServiceBuilder to regenerate this class.
	 */

	/**
	 * @see com.liferay.portal.kernel.service.persistence.BasePersistence#clearCache()
	 */
	public static void clearCache() {
		getPersistence().clearCache();
	}

	/**
	 * @see com.liferay.portal.kernel.service.persistence.BasePersistence#clearCache(com.liferay.portal.kernel.model.BaseModel)
	 */
	public static void clearCache(Ticket ticket) {
		getPersistence().clearCache(ticket);
	}

	/**
	 * @see com.liferay.portal.kernel.service.persistence.BasePersistence#countWithDynamicQuery(DynamicQuery)
	 */
	public static long countWithDynamicQuery(DynamicQuery dynamicQuery) {
		return getPersistence().countWithDynamicQuery(dynamicQuery);
	}

	/**
	 * @see com.liferay.portal.kernel.service.persistence.BasePersistence#fetchByPrimaryKeys(Set)
	 */
	public static Map<Serializable, Ticket> fetchByPrimaryKeys(
		Set<Serializable> primaryKeys) {

		return getPersistence().fetchByPrimaryKeys(primaryKeys);
	}

	/**
	 * @see com.liferay.portal.kernel.service.persistence.BasePersistence#findWithDynamicQuery(DynamicQuery)
	 */
	public static List<Ticket> findWithDynamicQuery(DynamicQuery dynamicQuery) {
		return getPersistence().findWithDynamicQuery(dynamicQuery);
	}

	/**
	 * @see com.liferay.portal.kernel.service.persistence.BasePersistence#findWithDynamicQuery(DynamicQuery, int, int)
	 */
	public static List<Ticket> findWithDynamicQuery(
		DynamicQuery dynamicQuery, int start, int end) {

		return getPersistence().findWithDynamicQuery(dynamicQuery, start, end);
	}

	/**
	 * @see com.liferay.portal.kernel.service.persistence.BasePersistence#findWithDynamicQuery(DynamicQuery, int, int, OrderByComparator)
	 */
	public static List<Ticket> findWithDynamicQuery(
		DynamicQuery dynamicQuery, int start, int end,
		OrderByComparator<Ticket> orderByComparator) {

		return getPersistence().findWithDynamicQuery(
			dynamicQuery, start, end, orderByComparator);
	}

	/**
	 * @see com.liferay.portal.kernel.service.persistence.BasePersistence#update(com.liferay.portal.kernel.model.BaseModel)
	 */
	public static Ticket update(Ticket ticket) {
		return getPersistence().update(ticket);
	}

	/**
	 * @see com.liferay.portal.kernel.service.persistence.BasePersistence#update(com.liferay.portal.kernel.model.BaseModel, ServiceContext)
	 */
	public static Ticket update(Ticket ticket, ServiceContext serviceContext) {
		return getPersistence().update(ticket, serviceContext);
	}

	/**
	 * Returns the ticket where ticketId = &#63; and token = &#63; or throws a <code>NoSuchTicketException</code> if it could not be found.
	 *
	 * @param ticketId the ticket ID
	 * @param token the token
	 * @return the matching ticket
	 * @throws NoSuchTicketException if a matching ticket could not be found
	 */
	public static Ticket findByOdooAuth(String ticketId, String token)
		throws es.altia.chd.form.ticket.sb.exception.NoSuchTicketException {

		return getPersistence().findByOdooAuth(ticketId, token);
	}

	/**
	 * Returns the ticket where ticketId = &#63; and token = &#63; or returns <code>null</code> if it could not be found. Uses the finder cache.
	 *
	 * @param ticketId the ticket ID
	 * @param token the token
	 * @return the matching ticket, or <code>null</code> if a matching ticket could not be found
	 */
	public static Ticket fetchByOdooAuth(String ticketId, String token) {
		return getPersistence().fetchByOdooAuth(ticketId, token);
	}

	/**
	 * Returns the ticket where ticketId = &#63; and token = &#63; or returns <code>null</code> if it could not be found, optionally using the finder cache.
	 *
	 * @param ticketId the ticket ID
	 * @param token the token
	 * @param useFinderCache whether to use the finder cache
	 * @return the matching ticket, or <code>null</code> if a matching ticket could not be found
	 */
	public static Ticket fetchByOdooAuth(
		String ticketId, String token, boolean useFinderCache) {

		return getPersistence().fetchByOdooAuth(
			ticketId, token, useFinderCache);
	}

	/**
	 * Removes the ticket where ticketId = &#63; and token = &#63; from the database.
	 *
	 * @param ticketId the ticket ID
	 * @param token the token
	 * @return the ticket that was removed
	 */
	public static Ticket removeByOdooAuth(String ticketId, String token)
		throws es.altia.chd.form.ticket.sb.exception.NoSuchTicketException {

		return getPersistence().removeByOdooAuth(ticketId, token);
	}

	/**
	 * Returns the number of tickets where ticketId = &#63; and token = &#63;.
	 *
	 * @param ticketId the ticket ID
	 * @param token the token
	 * @return the number of matching tickets
	 */
	public static int countByOdooAuth(String ticketId, String token) {
		return getPersistence().countByOdooAuth(ticketId, token);
	}

	/**
	 * Returns the ticket where formInstanceRecordId = &#63; or throws a <code>NoSuchTicketException</code> if it could not be found.
	 *
	 * @param formInstanceRecordId the form instance record ID
	 * @return the matching ticket
	 * @throws NoSuchTicketException if a matching ticket could not be found
	 */
	public static Ticket findByFormRecordId(long formInstanceRecordId)
		throws es.altia.chd.form.ticket.sb.exception.NoSuchTicketException {

		return getPersistence().findByFormRecordId(formInstanceRecordId);
	}

	/**
	 * Returns the ticket where formInstanceRecordId = &#63; or returns <code>null</code> if it could not be found. Uses the finder cache.
	 *
	 * @param formInstanceRecordId the form instance record ID
	 * @return the matching ticket, or <code>null</code> if a matching ticket could not be found
	 */
	public static Ticket fetchByFormRecordId(long formInstanceRecordId) {
		return getPersistence().fetchByFormRecordId(formInstanceRecordId);
	}

	/**
	 * Returns the ticket where formInstanceRecordId = &#63; or returns <code>null</code> if it could not be found, optionally using the finder cache.
	 *
	 * @param formInstanceRecordId the form instance record ID
	 * @param useFinderCache whether to use the finder cache
	 * @return the matching ticket, or <code>null</code> if a matching ticket could not be found
	 */
	public static Ticket fetchByFormRecordId(
		long formInstanceRecordId, boolean useFinderCache) {

		return getPersistence().fetchByFormRecordId(
			formInstanceRecordId, useFinderCache);
	}

	/**
	 * Removes the ticket where formInstanceRecordId = &#63; from the database.
	 *
	 * @param formInstanceRecordId the form instance record ID
	 * @return the ticket that was removed
	 */
	public static Ticket removeByFormRecordId(long formInstanceRecordId)
		throws es.altia.chd.form.ticket.sb.exception.NoSuchTicketException {

		return getPersistence().removeByFormRecordId(formInstanceRecordId);
	}

	/**
	 * Returns the number of tickets where formInstanceRecordId = &#63;.
	 *
	 * @param formInstanceRecordId the form instance record ID
	 * @return the number of matching tickets
	 */
	public static int countByFormRecordId(long formInstanceRecordId) {
		return getPersistence().countByFormRecordId(formInstanceRecordId);
	}

	/**
	 * Caches the ticket in the entity cache if it is enabled.
	 *
	 * @param ticket the ticket
	 */
	public static void cacheResult(Ticket ticket) {
		getPersistence().cacheResult(ticket);
	}

	/**
	 * Caches the tickets in the entity cache if it is enabled.
	 *
	 * @param tickets the tickets
	 */
	public static void cacheResult(List<Ticket> tickets) {
		getPersistence().cacheResult(tickets);
	}

	/**
	 * Creates a new ticket with the primary key. Does not add the ticket to the database.
	 *
	 * @param id the primary key for the new ticket
	 * @return the new ticket
	 */
	public static Ticket create(long id) {
		return getPersistence().create(id);
	}

	/**
	 * Removes the ticket with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * @param id the primary key of the ticket
	 * @return the ticket that was removed
	 * @throws NoSuchTicketException if a ticket with the primary key could not be found
	 */
	public static Ticket remove(long id)
		throws es.altia.chd.form.ticket.sb.exception.NoSuchTicketException {

		return getPersistence().remove(id);
	}

	public static Ticket updateImpl(Ticket ticket) {
		return getPersistence().updateImpl(ticket);
	}

	/**
	 * Returns the ticket with the primary key or throws a <code>NoSuchTicketException</code> if it could not be found.
	 *
	 * @param id the primary key of the ticket
	 * @return the ticket
	 * @throws NoSuchTicketException if a ticket with the primary key could not be found
	 */
	public static Ticket findByPrimaryKey(long id)
		throws es.altia.chd.form.ticket.sb.exception.NoSuchTicketException {

		return getPersistence().findByPrimaryKey(id);
	}

	/**
	 * Returns the ticket with the primary key or returns <code>null</code> if it could not be found.
	 *
	 * @param id the primary key of the ticket
	 * @return the ticket, or <code>null</code> if a ticket with the primary key could not be found
	 */
	public static Ticket fetchByPrimaryKey(long id) {
		return getPersistence().fetchByPrimaryKey(id);
	}

	/**
	 * Returns all the tickets.
	 *
	 * @return the tickets
	 */
	public static List<Ticket> findAll() {
		return getPersistence().findAll();
	}

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
	public static List<Ticket> findAll(int start, int end) {
		return getPersistence().findAll(start, end);
	}

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
	public static List<Ticket> findAll(
		int start, int end, OrderByComparator<Ticket> orderByComparator) {

		return getPersistence().findAll(start, end, orderByComparator);
	}

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
	public static List<Ticket> findAll(
		int start, int end, OrderByComparator<Ticket> orderByComparator,
		boolean useFinderCache) {

		return getPersistence().findAll(
			start, end, orderByComparator, useFinderCache);
	}

	/**
	 * Removes all the tickets from the database.
	 */
	public static void removeAll() {
		getPersistence().removeAll();
	}

	/**
	 * Returns the number of tickets.
	 *
	 * @return the number of tickets
	 */
	public static int countAll() {
		return getPersistence().countAll();
	}

	public static TicketPersistence getPersistence() {
		return _persistence;
	}

	public static void setPersistence(TicketPersistence persistence) {
		_persistence = persistence;
	}

	private static volatile TicketPersistence _persistence;

}