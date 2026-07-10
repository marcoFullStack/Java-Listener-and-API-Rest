/**
 * SPDX-FileCopyrightText: (c) 2026 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package es.altia.chd.form.ticket.sb.service.persistence.impl;

import com.liferay.petra.string.StringBundler;
import com.liferay.portal.kernel.configuration.Configuration;
import com.liferay.portal.kernel.dao.orm.EntityCache;
import com.liferay.portal.kernel.dao.orm.FinderCache;
import com.liferay.portal.kernel.dao.orm.FinderPath;
import com.liferay.portal.kernel.dao.orm.Query;
import com.liferay.portal.kernel.dao.orm.QueryPos;
import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.dao.orm.Session;
import com.liferay.portal.kernel.dao.orm.SessionFactory;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.ServiceContextThreadLocal;
import com.liferay.portal.kernel.service.persistence.impl.BasePersistenceImpl;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.portal.kernel.util.PropsKeys;
import com.liferay.portal.kernel.util.PropsUtil;
import com.liferay.portal.kernel.util.ProxyUtil;
import com.liferay.portal.kernel.util.SetUtil;
import com.liferay.portal.kernel.util.StringUtil;

import es.altia.chd.form.ticket.sb.exception.NoSuchTicketException;
import es.altia.chd.form.ticket.sb.model.Ticket;
import es.altia.chd.form.ticket.sb.model.TicketTable;
import es.altia.chd.form.ticket.sb.model.impl.TicketImpl;
import es.altia.chd.form.ticket.sb.model.impl.TicketModelImpl;
import es.altia.chd.form.ticket.sb.service.persistence.TicketPersistence;
import es.altia.chd.form.ticket.sb.service.persistence.TicketUtil;
import es.altia.chd.form.ticket.sb.service.persistence.impl.constants.CHDTICKETPersistenceConstants;

import java.io.Serializable;

import java.lang.reflect.InvocationHandler;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import javax.sql.DataSource;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;

/**
 * The persistence implementation for the ticket service.
 *
 * <p>
 * Caching information and settings can be found in <code>portal.properties</code>
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @generated
 */
@Component(service = TicketPersistence.class)
public class TicketPersistenceImpl
	extends BasePersistenceImpl<Ticket> implements TicketPersistence {

	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this class directly. Always use <code>TicketUtil</code> to access the ticket persistence. Modify <code>service.xml</code> and rerun ServiceBuilder to regenerate this class.
	 */
	public static final String FINDER_CLASS_NAME_ENTITY =
		TicketImpl.class.getName();

	public static final String FINDER_CLASS_NAME_LIST_WITH_PAGINATION =
		FINDER_CLASS_NAME_ENTITY + ".List1";

	public static final String FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION =
		FINDER_CLASS_NAME_ENTITY + ".List2";

	private FinderPath _finderPathWithPaginationFindAll;
	private FinderPath _finderPathWithoutPaginationFindAll;
	private FinderPath _finderPathCountAll;
	private FinderPath _finderPathFetchByOdooAuth;

	/**
	 * Returns the ticket where ticketId = &#63; and token = &#63; or throws a <code>NoSuchTicketException</code> if it could not be found.
	 *
	 * @param ticketId the ticket ID
	 * @param token the token
	 * @return the matching ticket
	 * @throws NoSuchTicketException if a matching ticket could not be found
	 */
	@Override
	public Ticket findByOdooAuth(String ticketId, String token)
		throws NoSuchTicketException {

		Ticket ticket = fetchByOdooAuth(ticketId, token);

		if (ticket == null) {
			StringBundler sb = new StringBundler(6);

			sb.append(_NO_SUCH_ENTITY_WITH_KEY);

			sb.append("ticketId=");
			sb.append(ticketId);

			sb.append(", token=");
			sb.append(token);

			sb.append("}");

			if (_log.isDebugEnabled()) {
				_log.debug(sb.toString());
			}

			throw new NoSuchTicketException(sb.toString());
		}

		return ticket;
	}

	/**
	 * Returns the ticket where ticketId = &#63; and token = &#63; or returns <code>null</code> if it could not be found. Uses the finder cache.
	 *
	 * @param ticketId the ticket ID
	 * @param token the token
	 * @return the matching ticket, or <code>null</code> if a matching ticket could not be found
	 */
	@Override
	public Ticket fetchByOdooAuth(String ticketId, String token) {
		return fetchByOdooAuth(ticketId, token, true);
	}

	/**
	 * Returns the ticket where ticketId = &#63; and token = &#63; or returns <code>null</code> if it could not be found, optionally using the finder cache.
	 *
	 * @param ticketId the ticket ID
	 * @param token the token
	 * @param useFinderCache whether to use the finder cache
	 * @return the matching ticket, or <code>null</code> if a matching ticket could not be found
	 */
	@Override
	public Ticket fetchByOdooAuth(
		String ticketId, String token, boolean useFinderCache) {

		ticketId = Objects.toString(ticketId, "");
		token = Objects.toString(token, "");

		Object[] finderArgs = null;

		if (useFinderCache) {
			finderArgs = new Object[] {ticketId, token};
		}

		Object result = null;

		if (useFinderCache) {
			result = finderCache.getResult(
				_finderPathFetchByOdooAuth, finderArgs, this);
		}

		if (result instanceof Ticket) {
			Ticket ticket = (Ticket)result;

			if (!Objects.equals(ticketId, ticket.getTicketId()) ||
				!Objects.equals(token, ticket.getToken())) {

				result = null;
			}
		}

		if (result == null) {
			StringBundler sb = new StringBundler(4);

			sb.append(_SQL_SELECT_TICKET_WHERE);

			boolean bindTicketId = false;

			if (ticketId.isEmpty()) {
				sb.append(_FINDER_COLUMN_ODOOAUTH_TICKETID_3);
			}
			else {
				bindTicketId = true;

				sb.append(_FINDER_COLUMN_ODOOAUTH_TICKETID_2);
			}

			boolean bindToken = false;

			if (token.isEmpty()) {
				sb.append(_FINDER_COLUMN_ODOOAUTH_TOKEN_3);
			}
			else {
				bindToken = true;

				sb.append(_FINDER_COLUMN_ODOOAUTH_TOKEN_2);
			}

			String sql = sb.toString();

			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(sql);

				QueryPos queryPos = QueryPos.getInstance(query);

				if (bindTicketId) {
					queryPos.add(ticketId);
				}

				if (bindToken) {
					queryPos.add(token);
				}

				List<Ticket> list = query.list();

				if (list.isEmpty()) {
					if (useFinderCache) {
						finderCache.putResult(
							_finderPathFetchByOdooAuth, finderArgs, list);
					}
				}
				else {
					if (list.size() > 1) {
						Collections.sort(list, Collections.reverseOrder());

						if (_log.isWarnEnabled()) {
							if (!useFinderCache) {
								finderArgs = new Object[] {ticketId, token};
							}

							_log.warn(
								"TicketPersistenceImpl.fetchByOdooAuth(String, String, boolean) with parameters (" +
									StringUtil.merge(finderArgs) +
										") yields a result set with more than 1 result. This violates the logical unique restriction. There is no order guarantee on which result is returned by this finder.");
						}
					}

					Ticket ticket = list.get(0);

					result = ticket;

					cacheResult(ticket);
				}
			}
			catch (Exception exception) {
				throw processException(exception);
			}
			finally {
				closeSession(session);
			}
		}

		if (result instanceof List<?>) {
			return null;
		}
		else {
			return (Ticket)result;
		}
	}

	/**
	 * Removes the ticket where ticketId = &#63; and token = &#63; from the database.
	 *
	 * @param ticketId the ticket ID
	 * @param token the token
	 * @return the ticket that was removed
	 */
	@Override
	public Ticket removeByOdooAuth(String ticketId, String token)
		throws NoSuchTicketException {

		Ticket ticket = findByOdooAuth(ticketId, token);

		return remove(ticket);
	}

	/**
	 * Returns the number of tickets where ticketId = &#63; and token = &#63;.
	 *
	 * @param ticketId the ticket ID
	 * @param token the token
	 * @return the number of matching tickets
	 */
	@Override
	public int countByOdooAuth(String ticketId, String token) {
		Ticket ticket = fetchByOdooAuth(ticketId, token);

		if (ticket == null) {
			return 0;
		}

		return 1;
	}

	private static final String _FINDER_COLUMN_ODOOAUTH_TICKETID_2 =
		"ticket.ticketId = ? AND ";

	private static final String _FINDER_COLUMN_ODOOAUTH_TICKETID_3 =
		"(ticket.ticketId IS NULL OR ticket.ticketId = '') AND ";

	private static final String _FINDER_COLUMN_ODOOAUTH_TOKEN_2 =
		"ticket.token = ?";

	private static final String _FINDER_COLUMN_ODOOAUTH_TOKEN_3 =
		"(ticket.token IS NULL OR ticket.token = '')";

	private FinderPath _finderPathFetchByFormRecordId;

	/**
	 * Returns the ticket where formInstanceRecordId = &#63; or throws a <code>NoSuchTicketException</code> if it could not be found.
	 *
	 * @param formInstanceRecordId the form instance record ID
	 * @return the matching ticket
	 * @throws NoSuchTicketException if a matching ticket could not be found
	 */
	@Override
	public Ticket findByFormRecordId(long formInstanceRecordId)
		throws NoSuchTicketException {

		Ticket ticket = fetchByFormRecordId(formInstanceRecordId);

		if (ticket == null) {
			StringBundler sb = new StringBundler(4);

			sb.append(_NO_SUCH_ENTITY_WITH_KEY);

			sb.append("formInstanceRecordId=");
			sb.append(formInstanceRecordId);

			sb.append("}");

			if (_log.isDebugEnabled()) {
				_log.debug(sb.toString());
			}

			throw new NoSuchTicketException(sb.toString());
		}

		return ticket;
	}

	/**
	 * Returns the ticket where formInstanceRecordId = &#63; or returns <code>null</code> if it could not be found. Uses the finder cache.
	 *
	 * @param formInstanceRecordId the form instance record ID
	 * @return the matching ticket, or <code>null</code> if a matching ticket could not be found
	 */
	@Override
	public Ticket fetchByFormRecordId(long formInstanceRecordId) {
		return fetchByFormRecordId(formInstanceRecordId, true);
	}

	/**
	 * Returns the ticket where formInstanceRecordId = &#63; or returns <code>null</code> if it could not be found, optionally using the finder cache.
	 *
	 * @param formInstanceRecordId the form instance record ID
	 * @param useFinderCache whether to use the finder cache
	 * @return the matching ticket, or <code>null</code> if a matching ticket could not be found
	 */
	@Override
	public Ticket fetchByFormRecordId(
		long formInstanceRecordId, boolean useFinderCache) {

		Object[] finderArgs = null;

		if (useFinderCache) {
			finderArgs = new Object[] {formInstanceRecordId};
		}

		Object result = null;

		if (useFinderCache) {
			result = finderCache.getResult(
				_finderPathFetchByFormRecordId, finderArgs, this);
		}

		if (result instanceof Ticket) {
			Ticket ticket = (Ticket)result;

			if (formInstanceRecordId != ticket.getFormInstanceRecordId()) {
				result = null;
			}
		}

		if (result == null) {
			StringBundler sb = new StringBundler(3);

			sb.append(_SQL_SELECT_TICKET_WHERE);

			sb.append(_FINDER_COLUMN_FORMRECORDID_FORMINSTANCERECORDID_2);

			String sql = sb.toString();

			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(sql);

				QueryPos queryPos = QueryPos.getInstance(query);

				queryPos.add(formInstanceRecordId);

				List<Ticket> list = query.list();

				if (list.isEmpty()) {
					if (useFinderCache) {
						finderCache.putResult(
							_finderPathFetchByFormRecordId, finderArgs, list);
					}
				}
				else {
					if (list.size() > 1) {
						Collections.sort(list, Collections.reverseOrder());

						if (_log.isWarnEnabled()) {
							if (!useFinderCache) {
								finderArgs = new Object[] {
									formInstanceRecordId
								};
							}

							_log.warn(
								"TicketPersistenceImpl.fetchByFormRecordId(long, boolean) with parameters (" +
									StringUtil.merge(finderArgs) +
										") yields a result set with more than 1 result. This violates the logical unique restriction. There is no order guarantee on which result is returned by this finder.");
						}
					}

					Ticket ticket = list.get(0);

					result = ticket;

					cacheResult(ticket);
				}
			}
			catch (Exception exception) {
				throw processException(exception);
			}
			finally {
				closeSession(session);
			}
		}

		if (result instanceof List<?>) {
			return null;
		}
		else {
			return (Ticket)result;
		}
	}

	/**
	 * Removes the ticket where formInstanceRecordId = &#63; from the database.
	 *
	 * @param formInstanceRecordId the form instance record ID
	 * @return the ticket that was removed
	 */
	@Override
	public Ticket removeByFormRecordId(long formInstanceRecordId)
		throws NoSuchTicketException {

		Ticket ticket = findByFormRecordId(formInstanceRecordId);

		return remove(ticket);
	}

	/**
	 * Returns the number of tickets where formInstanceRecordId = &#63;.
	 *
	 * @param formInstanceRecordId the form instance record ID
	 * @return the number of matching tickets
	 */
	@Override
	public int countByFormRecordId(long formInstanceRecordId) {
		Ticket ticket = fetchByFormRecordId(formInstanceRecordId);

		if (ticket == null) {
			return 0;
		}

		return 1;
	}

	private static final String
		_FINDER_COLUMN_FORMRECORDID_FORMINSTANCERECORDID_2 =
			"ticket.formInstanceRecordId = ?";

	public TicketPersistenceImpl() {
		Map<String, String> dbColumnNames = new HashMap<String, String>();

		dbColumnNames.put("id", "id_");

		setDBColumnNames(dbColumnNames);

		setModelClass(Ticket.class);

		setModelImplClass(TicketImpl.class);
		setModelPKClass(long.class);

		setTable(TicketTable.INSTANCE);
	}

	/**
	 * Caches the ticket in the entity cache if it is enabled.
	 *
	 * @param ticket the ticket
	 */
	@Override
	public void cacheResult(Ticket ticket) {
		entityCache.putResult(TicketImpl.class, ticket.getPrimaryKey(), ticket);

		finderCache.putResult(
			_finderPathFetchByOdooAuth,
			new Object[] {ticket.getTicketId(), ticket.getToken()}, ticket);

		finderCache.putResult(
			_finderPathFetchByFormRecordId,
			new Object[] {ticket.getFormInstanceRecordId()}, ticket);
	}

	private int _valueObjectFinderCacheListThreshold;

	/**
	 * Caches the tickets in the entity cache if it is enabled.
	 *
	 * @param tickets the tickets
	 */
	@Override
	public void cacheResult(List<Ticket> tickets) {
		if ((_valueObjectFinderCacheListThreshold == 0) ||
			((_valueObjectFinderCacheListThreshold > 0) &&
			 (tickets.size() > _valueObjectFinderCacheListThreshold))) {

			return;
		}

		for (Ticket ticket : tickets) {
			if (entityCache.getResult(
					TicketImpl.class, ticket.getPrimaryKey()) == null) {

				cacheResult(ticket);
			}
		}
	}

	/**
	 * Clears the cache for all tickets.
	 *
	 * <p>
	 * The <code>EntityCache</code> and <code>FinderCache</code> are both cleared by this method.
	 * </p>
	 */
	@Override
	public void clearCache() {
		entityCache.clearCache(TicketImpl.class);

		finderCache.clearCache(TicketImpl.class);
	}

	/**
	 * Clears the cache for the ticket.
	 *
	 * <p>
	 * The <code>EntityCache</code> and <code>FinderCache</code> are both cleared by this method.
	 * </p>
	 */
	@Override
	public void clearCache(Ticket ticket) {
		entityCache.removeResult(TicketImpl.class, ticket);
	}

	@Override
	public void clearCache(List<Ticket> tickets) {
		for (Ticket ticket : tickets) {
			entityCache.removeResult(TicketImpl.class, ticket);
		}
	}

	@Override
	public void clearCache(Set<Serializable> primaryKeys) {
		finderCache.clearCache(TicketImpl.class);

		for (Serializable primaryKey : primaryKeys) {
			entityCache.removeResult(TicketImpl.class, primaryKey);
		}
	}

	protected void cacheUniqueFindersCache(TicketModelImpl ticketModelImpl) {
		Object[] args = new Object[] {
			ticketModelImpl.getTicketId(), ticketModelImpl.getToken()
		};

		finderCache.putResult(
			_finderPathFetchByOdooAuth, args, ticketModelImpl);

		args = new Object[] {ticketModelImpl.getFormInstanceRecordId()};

		finderCache.putResult(
			_finderPathFetchByFormRecordId, args, ticketModelImpl);
	}

	/**
	 * Creates a new ticket with the primary key. Does not add the ticket to the database.
	 *
	 * @param id the primary key for the new ticket
	 * @return the new ticket
	 */
	@Override
	public Ticket create(long id) {
		Ticket ticket = new TicketImpl();

		ticket.setNew(true);
		ticket.setPrimaryKey(id);

		return ticket;
	}

	/**
	 * Removes the ticket with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * @param id the primary key of the ticket
	 * @return the ticket that was removed
	 * @throws NoSuchTicketException if a ticket with the primary key could not be found
	 */
	@Override
	public Ticket remove(long id) throws NoSuchTicketException {
		return remove((Serializable)id);
	}

	/**
	 * Removes the ticket with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * @param primaryKey the primary key of the ticket
	 * @return the ticket that was removed
	 * @throws NoSuchTicketException if a ticket with the primary key could not be found
	 */
	@Override
	public Ticket remove(Serializable primaryKey) throws NoSuchTicketException {
		Session session = null;

		try {
			session = openSession();

			Ticket ticket = (Ticket)session.get(TicketImpl.class, primaryKey);

			if (ticket == null) {
				if (_log.isDebugEnabled()) {
					_log.debug(_NO_SUCH_ENTITY_WITH_PRIMARY_KEY + primaryKey);
				}

				throw new NoSuchTicketException(
					_NO_SUCH_ENTITY_WITH_PRIMARY_KEY + primaryKey);
			}

			return remove(ticket);
		}
		catch (NoSuchTicketException noSuchEntityException) {
			throw noSuchEntityException;
		}
		catch (Exception exception) {
			throw processException(exception);
		}
		finally {
			closeSession(session);
		}
	}

	@Override
	protected Ticket removeImpl(Ticket ticket) {
		Session session = null;

		try {
			session = openSession();

			if (!session.contains(ticket)) {
				ticket = (Ticket)session.get(
					TicketImpl.class, ticket.getPrimaryKeyObj());
			}

			if (ticket != null) {
				session.delete(ticket);
			}
		}
		catch (Exception exception) {
			throw processException(exception);
		}
		finally {
			closeSession(session);
		}

		if (ticket != null) {
			clearCache(ticket);
		}

		return ticket;
	}

	@Override
	public Ticket updateImpl(Ticket ticket) {
		boolean isNew = ticket.isNew();

		if (!(ticket instanceof TicketModelImpl)) {
			InvocationHandler invocationHandler = null;

			if (ProxyUtil.isProxyClass(ticket.getClass())) {
				invocationHandler = ProxyUtil.getInvocationHandler(ticket);

				throw new IllegalArgumentException(
					"Implement ModelWrapper in ticket proxy " +
						invocationHandler.getClass());
			}

			throw new IllegalArgumentException(
				"Implement ModelWrapper in custom Ticket implementation " +
					ticket.getClass());
		}

		TicketModelImpl ticketModelImpl = (TicketModelImpl)ticket;

		if (isNew && (ticket.getCreateDate() == null)) {
			ServiceContext serviceContext =
				ServiceContextThreadLocal.getServiceContext();

			Date date = new Date();

			if (serviceContext == null) {
				ticket.setCreateDate(date);
			}
			else {
				ticket.setCreateDate(serviceContext.getCreateDate(date));
			}
		}

		Session session = null;

		try {
			session = openSession();

			if (isNew) {
				session.save(ticket);
			}
			else {
				ticket = (Ticket)session.merge(ticket);
			}
		}
		catch (Exception exception) {
			throw processException(exception);
		}
		finally {
			closeSession(session);
		}

		entityCache.putResult(TicketImpl.class, ticketModelImpl, false, true);

		cacheUniqueFindersCache(ticketModelImpl);

		if (isNew) {
			ticket.setNew(false);
		}

		ticket.resetOriginalValues();

		return ticket;
	}

	/**
	 * Returns the ticket with the primary key or throws a <code>com.liferay.portal.kernel.exception.NoSuchModelException</code> if it could not be found.
	 *
	 * @param primaryKey the primary key of the ticket
	 * @return the ticket
	 * @throws NoSuchTicketException if a ticket with the primary key could not be found
	 */
	@Override
	public Ticket findByPrimaryKey(Serializable primaryKey)
		throws NoSuchTicketException {

		Ticket ticket = fetchByPrimaryKey(primaryKey);

		if (ticket == null) {
			if (_log.isDebugEnabled()) {
				_log.debug(_NO_SUCH_ENTITY_WITH_PRIMARY_KEY + primaryKey);
			}

			throw new NoSuchTicketException(
				_NO_SUCH_ENTITY_WITH_PRIMARY_KEY + primaryKey);
		}

		return ticket;
	}

	/**
	 * Returns the ticket with the primary key or throws a <code>NoSuchTicketException</code> if it could not be found.
	 *
	 * @param id the primary key of the ticket
	 * @return the ticket
	 * @throws NoSuchTicketException if a ticket with the primary key could not be found
	 */
	@Override
	public Ticket findByPrimaryKey(long id) throws NoSuchTicketException {
		return findByPrimaryKey((Serializable)id);
	}

	/**
	 * Returns the ticket with the primary key or returns <code>null</code> if it could not be found.
	 *
	 * @param id the primary key of the ticket
	 * @return the ticket, or <code>null</code> if a ticket with the primary key could not be found
	 */
	@Override
	public Ticket fetchByPrimaryKey(long id) {
		return fetchByPrimaryKey((Serializable)id);
	}

	/**
	 * Returns all the tickets.
	 *
	 * @return the tickets
	 */
	@Override
	public List<Ticket> findAll() {
		return findAll(QueryUtil.ALL_POS, QueryUtil.ALL_POS, null);
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
	@Override
	public List<Ticket> findAll(int start, int end) {
		return findAll(start, end, null);
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
	@Override
	public List<Ticket> findAll(
		int start, int end, OrderByComparator<Ticket> orderByComparator) {

		return findAll(start, end, orderByComparator, true);
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
	@Override
	public List<Ticket> findAll(
		int start, int end, OrderByComparator<Ticket> orderByComparator,
		boolean useFinderCache) {

		FinderPath finderPath = null;
		Object[] finderArgs = null;

		if ((start == QueryUtil.ALL_POS) && (end == QueryUtil.ALL_POS) &&
			(orderByComparator == null)) {

			if (useFinderCache) {
				finderPath = _finderPathWithoutPaginationFindAll;
				finderArgs = FINDER_ARGS_EMPTY;
			}
		}
		else if (useFinderCache) {
			finderPath = _finderPathWithPaginationFindAll;
			finderArgs = new Object[] {start, end, orderByComparator};
		}

		List<Ticket> list = null;

		if (useFinderCache) {
			list = (List<Ticket>)finderCache.getResult(
				finderPath, finderArgs, this);
		}

		if (list == null) {
			StringBundler sb = null;
			String sql = null;

			if (orderByComparator != null) {
				sb = new StringBundler(
					2 + (orderByComparator.getOrderByFields().length * 2));

				sb.append(_SQL_SELECT_TICKET);

				appendOrderByComparator(
					sb, _ORDER_BY_ENTITY_ALIAS, orderByComparator);

				sql = sb.toString();
			}
			else {
				sql = _SQL_SELECT_TICKET;

				sql = sql.concat(TicketModelImpl.ORDER_BY_JPQL);
			}

			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(sql);

				list = (List<Ticket>)QueryUtil.list(
					query, getDialect(), start, end);

				cacheResult(list);

				if (useFinderCache) {
					finderCache.putResult(finderPath, finderArgs, list);
				}
			}
			catch (Exception exception) {
				throw processException(exception);
			}
			finally {
				closeSession(session);
			}
		}

		return list;
	}

	/**
	 * Removes all the tickets from the database.
	 *
	 */
	@Override
	public void removeAll() {
		for (Ticket ticket : findAll()) {
			remove(ticket);
		}
	}

	/**
	 * Returns the number of tickets.
	 *
	 * @return the number of tickets
	 */
	@Override
	public int countAll() {
		Long count = (Long)finderCache.getResult(
			_finderPathCountAll, FINDER_ARGS_EMPTY, this);

		if (count == null) {
			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(_SQL_COUNT_TICKET);

				count = (Long)query.uniqueResult();

				finderCache.putResult(
					_finderPathCountAll, FINDER_ARGS_EMPTY, count);
			}
			catch (Exception exception) {
				throw processException(exception);
			}
			finally {
				closeSession(session);
			}
		}

		return count.intValue();
	}

	@Override
	public Set<String> getBadColumnNames() {
		return _badColumnNames;
	}

	@Override
	protected EntityCache getEntityCache() {
		return entityCache;
	}

	@Override
	protected String getPKDBName() {
		return "id_";
	}

	@Override
	protected String getSelectSQL() {
		return _SQL_SELECT_TICKET;
	}

	@Override
	protected Map<String, Integer> getTableColumnsMap() {
		return TicketModelImpl.TABLE_COLUMNS_MAP;
	}

	/**
	 * Initializes the ticket persistence.
	 */
	@Activate
	public void activate() {
		_valueObjectFinderCacheListThreshold = GetterUtil.getInteger(
			PropsUtil.get(PropsKeys.VALUE_OBJECT_FINDER_CACHE_LIST_THRESHOLD));

		_finderPathWithPaginationFindAll = new FinderPath(
			FINDER_CLASS_NAME_LIST_WITH_PAGINATION, "findAll", new String[0],
			new String[0], true);

		_finderPathWithoutPaginationFindAll = new FinderPath(
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "findAll", new String[0],
			new String[0], true);

		_finderPathCountAll = new FinderPath(
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "countAll",
			new String[0], new String[0], false);

		_finderPathFetchByOdooAuth = new FinderPath(
			FINDER_CLASS_NAME_ENTITY, "fetchByOdooAuth",
			new String[] {String.class.getName(), String.class.getName()},
			new String[] {"ticketId", "token"}, true);

		_finderPathFetchByFormRecordId = new FinderPath(
			FINDER_CLASS_NAME_ENTITY, "fetchByFormRecordId",
			new String[] {Long.class.getName()},
			new String[] {"formInstanceRecordId"}, true);

		TicketUtil.setPersistence(this);
	}

	@Deactivate
	public void deactivate() {
		TicketUtil.setPersistence(null);

		entityCache.removeCache(TicketImpl.class.getName());
	}

	@Override
	@Reference(
		target = CHDTICKETPersistenceConstants.SERVICE_CONFIGURATION_FILTER,
		unbind = "-"
	)
	public void setConfiguration(Configuration configuration) {
	}

	@Override
	@Reference(
		target = CHDTICKETPersistenceConstants.ORIGIN_BUNDLE_SYMBOLIC_NAME_FILTER,
		unbind = "-"
	)
	public void setDataSource(DataSource dataSource) {
		super.setDataSource(dataSource);
	}

	@Override
	@Reference(
		target = CHDTICKETPersistenceConstants.ORIGIN_BUNDLE_SYMBOLIC_NAME_FILTER,
		unbind = "-"
	)
	public void setSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}

	@Reference
	protected EntityCache entityCache;

	@Reference
	protected FinderCache finderCache;

	private static final String _SQL_SELECT_TICKET =
		"SELECT ticket FROM Ticket ticket";

	private static final String _SQL_SELECT_TICKET_WHERE =
		"SELECT ticket FROM Ticket ticket WHERE ";

	private static final String _SQL_COUNT_TICKET =
		"SELECT COUNT(ticket) FROM Ticket ticket";

	private static final String _SQL_COUNT_TICKET_WHERE =
		"SELECT COUNT(ticket) FROM Ticket ticket WHERE ";

	private static final String _ORDER_BY_ENTITY_ALIAS = "ticket.";

	private static final String _NO_SUCH_ENTITY_WITH_PRIMARY_KEY =
		"No Ticket exists with the primary key ";

	private static final String _NO_SUCH_ENTITY_WITH_KEY =
		"No Ticket exists with the key {";

	private static final Log _log = LogFactoryUtil.getLog(
		TicketPersistenceImpl.class);

	private static final Set<String> _badColumnNames = SetUtil.fromArray(
		new String[] {"id"});

	@Override
	protected FinderCache getFinderCache() {
		return finderCache;
	}

}