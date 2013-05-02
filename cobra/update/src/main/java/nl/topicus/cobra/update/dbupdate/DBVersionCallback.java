package nl.topicus.cobra.update.dbupdate;

import java.util.List;

/**
 * Onderdeel van het nieuwe DBVersion tool
 * 
 * @author Chris Gunnink
 * @since 14 juli 2009
 */
public interface DBVersionCallback
{

	/**
	 * App specific code, such as convert scripts, adding/removing instances
	 * 
	 * @param taskInstances
	 *            != null
	 */
	public void afterTaskList(List<Object> taskInstances);

	/**
	 * App specific code, such as adding Spring managed Tasks, which depends on DAO's
	 * 
	 * @return List<?> or null
	 */
	public List<Object> getCustomTasks();

}