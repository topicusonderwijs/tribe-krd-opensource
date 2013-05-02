package nl.topicus.cobra.util.dbupdate.task;

import java.sql.ResultSet;
import java.sql.SQLException;

import junit.framework.Assert;
import nl.topicus.cobra.update.dbupdate.annotation.DBVersionUpdate;
import nl.topicus.cobra.update.dbupdate.annotation.DBVersionUpdateTask;
import nl.topicus.cobra.update.dbupdate.dbaccess.DBVersionHandle;

import org.junit.Ignore;

/**
 * Test Task om te kijken of DBVersionHandle goed werkt, en of transacties goed werken
 * 
 * @author Chris Gunnink
 * @since 17 juli 2009
 */
@Ignore
@DBVersionUpdateTask(version = "1.3.1", isFinal = false)
public class TestUpdateTask
{

	private static final String TEST_VERSION = "1.01";

	private static final int TEST_ID = 100000;

	@DBVersionUpdate
	public void testHandleExecute(DBVersionHandle handle) throws SQLException
	{
		handle.executeSql("insert into dbversionmark(id, version) values(" + TEST_ID + ", '"
			+ TEST_VERSION + "')");
	}

	@DBVersionUpdate
	public void testColumnExists(DBVersionHandle handle) throws SQLException
	{
		Assert.assertTrue(handle.columnExists("dbVersIONmARK", "vErSion"));
		Assert.assertFalse(handle.columnExists("dbVersIONmARK", "veErSion"));
		Assert.assertFalse(handle.columnExists("dbVerIONmmARK", "vErSion"));
	}

	@DBVersionUpdate
	public void testIndexExists1(DBVersionHandle handle) throws SQLException
	{
		Assert.assertFalse(handle.indexExists("dbVersIONmARK", "lala"));
		Assert.assertTrue(handle.indexExists("leerling", "adres"));
	}

	@DBVersionUpdate
	public void testTableExists(DBVersionHandle handle) throws SQLException
	{
		Assert.assertTrue(handle.tableExists("dbVersIONmARK"));
		Assert.assertFalse(handle.tableExists("dbVesrsIONmARK"));
	}

	@DBVersionUpdate
	public void testHandleSelect(DBVersionHandle handle) throws SQLException
	{
		ResultSet result = handle.select("select * from dbversionmark where id = " + TEST_ID);
		while (result.next())
		{
			Assert.assertTrue(result.getString("version").equals(TEST_VERSION));
		}
	}

	@DBVersionUpdate
	public void testAddIndex(DBVersionHandle handle) throws SQLException
	{
		handle.addIndexOfNotExists("test_index", false, "dbversionmark", "mark", "version");
		Assert.assertTrue(handle.indexExists("dbversionmark", "test_index"));
	}

	@DBVersionUpdate
	public void testIndexExists2(DBVersionHandle handle) throws SQLException
	{
		Assert.assertTrue(handle.indexExists("dbversionmark", "test_index"));
	}

	@DBVersionUpdate
	public void testException(DBVersionHandle handle)
	{
		Exception e = null;
		try
		{
			handle.executeSql("select * from BESTAATNIET_@#$&*&@#");
		}
		catch (Exception e2)
		{
			e = e2;
		}
		Assert.assertTrue(e instanceof SQLException);
	}

	@DBVersionUpdate
	public void testDeleteRecord(DBVersionHandle handle) throws SQLException
	{
		handle.executeSql("delete from dbversionmark where id = " + TEST_ID);
	}

}