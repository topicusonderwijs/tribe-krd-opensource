package nl.topicus.cobra.update.dbupdate.dbaccess;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Onderdeel van het nieuwe DBVersion tool
 * 
 * @author Chris Gunnink
 * @since 14 juli 2009
 */
public interface DBVersionHandle
{
	public int executeSql(String query) throws SQLException;

	public ResultSet select(String query) throws SQLException;

	public void commitAndOpenNewTransaction();

	public void clearHibernateSession();

	/**
	 * Callback om openstaande resources, zoals Statements en ResultSets te sluiten; de
	 * Connection mag open blijven
	 * 
	 * @throws SQLException
	 */
	public void closeResources() throws SQLException;

	/**
	 * Callback om de openstaande Connection te sluiten
	 * 
	 * @throws SQLException
	 */
	public void closeConnection() throws SQLException;

	/**
	 * @param table
	 *            case insensitive
	 */
	public boolean tableExists(String table) throws SQLException;

	/**
	 * @param table
	 *            case insensitive
	 * @param column
	 *            case insensitive
	 */
	public boolean columnExists(String table, String column) throws SQLException;

	/**
	 * @param table
	 *            case insensitive
	 * @param index
	 *            case insensitive
	 */
	public boolean indexExists(String table, String index) throws SQLException;

	public void addForeignKeyOfNotExists(String name, String tableName, String columnName,
			String referencedTableName, String referencedColumnName, boolean cascadeDelete)
			throws SQLException;

	public void addIndexOfNotExists(String name, boolean unique, String tableName,
			String... columnName) throws SQLException;

}