package nl.topicus.cobra.update.dbupdate.dbaccess;

import java.util.List;

import nl.topicus.cobra.update.dbupdate.entity.DBVersionMark;

/**
 * Onderdeel van het nieuwe DBVersion tool
 * 
 * @author Chris Gunnink
 * @since 14 juli 2009
 */
public interface DBVersionMarkDao
{

	public void saveWithoutCommit(DBVersionMark mark);

	/**
	 * Returns the current transaction or starts a new one when found none
	 */
	public void startTransaction();

	/**
	 * Commits the last started transaction
	 */
	public void commit();

	/**
	 * Rolls back the last started transaction; does nothing when there is nothing to
	 * rollback on
	 */
	public void rollback();

	/**
	 * Returns a specific DBVersionMark by its unique properties
	 * 
	 * @return {@link DBVersionMark} or null if not found
	 */
	public DBVersionMark get(String version, String mark);

	/**
	 * Returns all DBVersionMark's
	 * 
	 * @return unsorted DBVersionMark's or an empty list
	 */
	public List<DBVersionMark> getAll();

}