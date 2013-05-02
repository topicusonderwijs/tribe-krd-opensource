package nl.topicus.cobra.update.dbupdate;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.SQLException;
import java.util.*;

import nl.topicus.cobra.comparators.NaturalOrderComparator;
import nl.topicus.cobra.update.ComponentScanner;
import nl.topicus.cobra.update.DBVersionUpdateListener;
import nl.topicus.cobra.update.dbupdate.annotation.DBVersionUpdate;
import nl.topicus.cobra.update.dbupdate.annotation.DBVersionUpdateTask;
import nl.topicus.cobra.update.dbupdate.annotation.DBVersionUpdate.UPDATE_TYPE;
import nl.topicus.cobra.update.dbupdate.dbaccess.DBVersionHandle;
import nl.topicus.cobra.update.dbupdate.dbaccess.DBVersionMarkDao;
import nl.topicus.cobra.update.dbupdate.entity.DBVersionMark;
import nl.topicus.cobra.update.dbupdate.entity.DBVersionMark.MARK_TYPE;
import nl.topicus.cobra.util.JavaUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.type.filter.AssignableTypeFilter;
import org.springframework.core.type.filter.TypeFilter;

/**
 * Onderdeel van het nieuwe DBVersion tool
 * 
 * @author Chris Gunnink
 * @since 14 juli 2009
 */
public class DBVersionManager
{

	private static final String VERSION_SNAPSHOT_SUFFIX = "-SNAPSHOT";

	private final Logger log = LoggerFactory.getLogger(getClass());

	private final DBVersionMarkDao versionDAO;

	private final ComponentScanner componentScanner;

	private final DBVersionHandle dbHandle;

	private final NaturalOrderComparator comparator;

	private final DBVersionCallback callback;

	private String basePackage;

	private List<DBVersionUpdateListener> listeners = Collections.emptyList();

	/**
	 * Constructor om ALLEEN de huidige DB versie op te vragen
	 */
	public DBVersionManager(DBVersionMarkDao versionDAO)
	{
		this(null, versionDAO, null, null);
	}

	/**
	 * Constructor op te updaten
	 */
	public DBVersionManager(DBVersionHandle dbHandle, DBVersionMarkDao versionDAO,
			ComponentScanner componentScanner, DBVersionCallback callback)
	{
		this.versionDAO = versionDAO;
		this.componentScanner = componentScanner;
		this.dbHandle = dbHandle;
		this.callback = callback;
		comparator = new NaturalOrderComparator();
	}

	/**
	 * Returns the current version of this schema Example: 1.2 means all final updates to
	 * (inclusive) 1.2 have been executed, but 1.2.2-SNAPSHOT means that the highest
	 * DBVersionMark was not part of a final Task
	 */
	public String getCurrentVersion()
	{
		DBVersionMark highest = null;
		for (DBVersionMark mark : versionDAO.getAll())
		{
			if (mark.getVersion() == null)
				continue;
			int compare =
				(highest != null) ? comparator.compare(mark.getVersion(), highest.getVersion()) : 1;
			if (compare > 0 || (compare == 0 && mark.getType() == MARK_TYPE.FINAL_TASK))
			{
				highest = mark;
			}
		}
		String version = (highest != null) ? highest.getVersion() : "0.0";
		if (highest != null && highest.getType() == MARK_TYPE.OPERATION)
		{
			version += VERSION_SNAPSHOT_SUFFIX;
		}
		return version;
	}

	public void updateTo(String version, String scanBasePackage)
	{
		this.basePackage = scanBasePackage;

		String currentVersion = getCurrentVersion();

		log.info("Updating from " + currentVersion + " to " + version);

		// als een Task eruit knalt met een Exception, en hij moet opnieuw uitgevoerd
		// worden, dan werkt dat niet want:
		// 1.21 < 1.21-SNAPSHOT; dus het achtervoegsel er weer afhalen
		if (currentVersion.endsWith(VERSION_SNAPSHOT_SUFFIX))
		{
			currentVersion =
				currentVersion.substring(0, currentVersion.length()
					- VERSION_SNAPSHOT_SUFFIX.length());
		}

		try
		{
			// binnen deze methode, is namelijk afhankelijk van de #basePackage
			scanForDBVersionUpdateListeners();

			List<Object> taskInstances = getScannedTaskInstances();
			if (callback.getCustomTasks() != null)
			{
				taskInstances.addAll(callback.getCustomTasks());
			}

			filterTaskInstances(taskInstances, version, currentVersion);
			sortTaskInstances(taskInstances);
			checkTaskIntegrity(taskInstances);

			for (Object taskInstance : taskInstances)
			{
				Class< ? > taskClass = taskInstance.getClass();
				String markName = getMarkName(taskClass);
				DBVersionMark mark = versionDAO.get(getVersion(taskClass), markName);
				if (mark == null || !isFinal(taskClass))
				{
					runTask(taskInstance);

					checkIsFinal(taskClass, markName);
				}
				else
				{
					log.info("Mark found for final Task " + taskClass.getName() + "; skipping...");
				}
			}
			getHandle().closeConnection();
			log.info("Successfully updated to " + getCurrentVersion());
		}
		catch (Exception e)
		{
			log.error("Failed update to " + version, e);
			versionDAO.rollback();
		}
	}

	protected void checkIsFinal(Class< ? > taskClass, String markName)
	{
		if (isFinal(taskClass))
		{
			versionDAO.startTransaction();
			DBVersionMark mark =
				new DBVersionMark(getVersion(taskClass), markName, MARK_TYPE.FINAL_TASK);
			versionDAO.saveWithoutCommit(mark);
			versionDAO.commit();
		}
	}

	/**
	 * Scans all Tasks, using filter useScan=true
	 * 
	 * @return All scanned task instances; empty List when found none
	 */
	private List<Object> getScannedTaskInstances() throws Exception
	{
		List<Class< ? >> allTasks =
			componentScanner.scanForClasses(DBVersionUpdateTask.class, basePackage);
		List<Class< ? >> filteredTasks = new ArrayList<Class< ? >>();
		for (Class< ? > taskClass : allTasks)
		{
			if (useScan(taskClass))
			{
				filteredTasks.add(taskClass);
			}
		}
		return getInstances(filteredTasks);
	}

	private void scanForDBVersionUpdateListeners()
	{
		TypeFilter filter = new AssignableTypeFilter(DBVersionUpdateListener.class);
		List<Class< ? extends DBVersionUpdateListener>> listenerClasses =
			componentScanner.scanForClasses(DBVersionUpdateListener.class, basePackage, filter);

		listeners = new ArrayList<DBVersionUpdateListener>();
		for (Class< ? extends DBVersionUpdateListener> listenerClass : listenerClasses)
		{
			try
			{
				listeners.add(listenerClass.newInstance());
			}
			catch (Exception e)
			{
				throw new RuntimeException(e);
			}
		}
	}

	/**
	 * Removes all unnecessary taskInstances where version v for every taskInstance
	 * updateToVersion < v || currentVersion > v
	 * 
	 * @require taskInstances != null
	 * @require updateToVersion != null
	 * @require currentVersion != null
	 */
	private void filterTaskInstances(List<Object> taskInstances, String updateToVersion,
			String currentVersion)
	{
		Iterator<Object> ix = taskInstances.iterator();

		while (ix.hasNext())
		{
			// alleen tasks met de juiste versie en useScan=true laten staan;
			String version = getVersion(ix.next().getClass());
			if (version != null)
			{
				if (comparator.compare(updateToVersion, version) < 0
					|| comparator.compare(currentVersion, version) > 0)
				{
					ix.remove();
				}
			}
		}
		if (callback != null)
		{
			callback.afterTaskList(taskInstances);
		}
	}

	/**
	 * Custom sort which takes into account the sorting with
	 * DBVersionUpdateTask.runAfter() property
	 * 
	 * @require taskClasses != null
	 */
	private void sortTaskInstances(List<Object> taskInstances)
	{
		Collections.sort(taskInstances, new Comparator<Object>()
		{
			@Override
			public int compare(Object object1, Object object2)
			{
				Class< ? > class1 = object1.getClass();
				Class< ? > class2 = object2.getClass();
				String version1 = getVersion(class1);
				String version2 = getVersion(class2);
				if (version1 == null && version2 != null)
					return 1;
				if (version1 != null && version2 == null)
					return -1;
				int compare = 0;
				if (version1 != null && version2 != null)
					compare = comparator.compare(version1, version2);
				if (compare == 0)
				{
					if (isAfter(class1, class2))
						return 1;
					if (isAfter(class2, class1))
						return -1;
				}
				return compare;
			}
		});
	}

	private List<Object> getInstances(List<Class< ? >> neededTasks) throws Exception
	{
		List<Object> instances = new ArrayList<Object>();
		for (Class< ? > taskClass : neededTasks)
		{
			try
			{
				instances.add(taskClass.newInstance());
			}
			catch (Exception e)
			{
				log.error("Could not instantiate Task " + taskClass.getName(), e);
				throw e;
			}
		}
		return instances;
	}

	/**
	 * @require taskInstance != null
	 */
	protected void runTask(Object taskInstance) throws Exception
	{
		class JuisteVolgordeVanDatabaseTasksComparator implements Comparator<Method>
		{
			@Override
			public int compare(Method m1, Method m2)
			{
				boolean m1HasAnnotation = m1.isAnnotationPresent(DBVersionUpdate.class);
				boolean m2HasAnnotation = m2.isAnnotationPresent(DBVersionUpdate.class);

				if (m1HasAnnotation && m2HasAnnotation)
				{
					DBVersionUpdate a1 = m1.getAnnotation(DBVersionUpdate.class);
					DBVersionUpdate a2 = m2.getAnnotation(DBVersionUpdate.class);
					int typeCompare = a1.type().compareTo(a2.type());
					if (typeCompare != 0)
						return typeCompare;

					return Integer.valueOf(a1.volgnummer()).compareTo(
						Integer.valueOf(a2.volgnummer()));
				}

				if (m1HasAnnotation)
					return -1;
				else if (m2HasAnnotation)
					return 1;
				return 0;
			}
		}

		Class< ? > taskClass = taskInstance.getClass();
		log
			.info("Processing Task; version: " + getVersion(taskClass) + " - "
				+ taskClass.getName());

		long taskStartTime = System.currentTimeMillis();

		String version = getVersion(taskClass);

		Method[] methods = taskClass.getDeclaredMethods();
		Arrays.sort(methods, new JuisteVolgordeVanDatabaseTasksComparator());

		for (DBVersionUpdateListener listener : listeners)
			listener.onBeforeDefaultUpdates();
		executeUpdatesForType(taskInstance, taskClass, version, methods, UPDATE_TYPE.DEFAULT);
		for (DBVersionUpdateListener listener : listeners)
			listener.onAfterDefaultUpdates();

		for (DBVersionUpdateListener listener : listeners)
			listener.onBeforeSchemaUpdates();
		executeUpdatesForType(taskInstance, taskClass, version, methods, UPDATE_TYPE.SCHEMA);
		for (DBVersionUpdateListener listener : listeners)
			listener.onAfterSchemaUpdates();

		for (DBVersionUpdateListener listener : listeners)
			listener.onBeforeDataUpdates();
		executeUpdatesForType(taskInstance, taskClass, version, methods, UPDATE_TYPE.DATA);
		for (DBVersionUpdateListener listener : listeners)
			listener.onAfterDataUpdates();

		for (DBVersionUpdateListener listener : listeners)
			listener.onBeforeConstraintUpdates();
		executeUpdatesForType(taskInstance, taskClass, version, methods, UPDATE_TYPE.CONSTRAINT);
		for (DBVersionUpdateListener listener : listeners)
			listener.onAfterConstraintUpdates();

		for (DBVersionUpdateListener listener : listeners)
			listener.onBeforeFinishUpdates();
		executeUpdatesForType(taskInstance, taskClass, version, methods, UPDATE_TYPE.FINISH);
		for (DBVersionUpdateListener listener : listeners)
			listener.onAfterFinishUpdates();

		long taskEndTime = System.currentTimeMillis();

		log.info("Processing task " + taskClass.getName() + " took "
			+ formatDuration(taskEndTime - taskStartTime) + ".");
	}

	private void executeUpdatesForType(Object taskInstance, Class< ? > taskClass, String version,
			Method[] methods, UPDATE_TYPE type) throws Exception
	{
		List<Method> updates = filterUpdateMethodsForType(methods, type);
		for (Method update : updates)
		{
			executeUpdateMethod(taskInstance, taskClass, version, update);
		}
	}

	private List<Method> filterUpdateMethodsForType(Method[] methods, UPDATE_TYPE type)
	{
		List<Method> filteredUpdates = new ArrayList<Method>();
		for (Method method : methods)
		{
			if (method.isAnnotationPresent(DBVersionUpdate.class))
			{
				if (method.getAnnotation(DBVersionUpdate.class).type() == type)
					filteredUpdates.add(method);
			}
		}
		return filteredUpdates;
	}

	private void executeUpdateMethod(Object taskInstance, Class< ? > taskClass, String version,
			Method method) throws Exception
	{
		String markName = getMarkName(taskClass, method);
		if (versionDAO.get(version, markName) == null)
		{
			long methodStartTime = System.currentTimeMillis();

			runTaskMethod(taskInstance, taskClass, version, method, markName);

			long methodEndTime = System.currentTimeMillis();

			log.info("Processing method " + method.getName() + " took "
				+ formatDuration(methodEndTime - methodStartTime) + ".");
		}
		else
		{
			log.debug("Skipping Mark " + markName);
		}
	}

	protected void runTaskMethod(Object taskInstance, Class< ? > taskClass, String version,
			Method method, String markName) throws Exception
	{
		versionDAO.startTransaction();
		try
		{
			log.info("Processing " + method.getName() + "...");

			if (method.getParameterTypes().length == 0)
			{
				method.invoke(taskInstance);
			}
			else
			{
				DBVersionHandle handle = getHandle();
				method.invoke(taskInstance, handle);
				handle.closeResources();
			}
		}
		catch (Exception e)
		{
			handleOperationInvokeExceptions(e, taskClass, method);
		}

		if (method.getAnnotation(DBVersionUpdate.class).isFinal())
		{
			versionDAO.saveWithoutCommit(new DBVersionMark(version, markName, MARK_TYPE.OPERATION));
			log.debug("saving Mark " + markName);
			versionDAO.commit();
			log.debug("commit");
		}
	}

	protected void handleOperationInvokeExceptions(Exception e, Class< ? > taskClass, Method method)
			throws Exception
	{
		if (e instanceof IllegalArgumentException)
		{
			log
				.error("Method "
					+ taskClass
					+ "."
					+ method.getName()
					+ "(); method should either have no arguments or one and only one argument of type "
					+ DBVersionHandle.class.getName());
			versionDAO.rollback();
			log.debug("rollback");
			throw e;
		}
		else if (e instanceof InvocationTargetException)
		{
			Exception e2 = (e.getCause() instanceof SQLException) ? (SQLException) e.getCause() : e;
			log.error("Method " + taskClass + "." + method.getName() + "() threw an Exception");
			versionDAO.rollback();
			log.debug("rollback");
			throw e2;
		}
		log.error("Could not invoke method " + taskClass + "." + method.getName() + "()");
		versionDAO.rollback();
		log.debug("rollback");
		throw e;
	}

	/**
	 * Determines whether class1 should be executed after class2 recursively
	 * 
	 * @require class1 != null && class2 != null
	 */
	private boolean isAfter(Class< ? > class1, Class< ? > class2)
	{
		Class< ? > runAfter = getRunAfterClass(class1);
		if (runAfter != null)
		{
			if (class2.equals(runAfter))
			{
				return true;
			}
			return isAfter(runAfter, class2);
		}
		return false;
	}

	/**
	 * @require taskClass != null && taskClass has annotation DBVersionUpdateTask
	 */
	protected String getVersion(Class< ? > taskClass)
	{
		String ret = taskClass.getAnnotation(DBVersionUpdateTask.class).version();
		return DBVersionUpdateTask.NO_VERSION.equals(ret) ? null : ret;
	}

	/**
	 * @require taskClass != null && taskClass has annotation DBVersionUpdateTask
	 */
	protected boolean isFinal(Class< ? > taskClass)
	{
		return taskClass.getAnnotation(DBVersionUpdateTask.class).isFinal();
	}

	/**
	 * @require taskClass != null && taskClass has annotation DBVersionUpdateTask
	 */
	protected boolean useScan(Class< ? > taskClass)
	{
		return taskClass.getAnnotation(DBVersionUpdateTask.class).useScan();
	}

	/**
	 * @require taskClass != null && taskClass has annotation DBVersionUpdateTask
	 * @return Class<?> or null if not provided
	 */
	protected Class< ? > getRunAfterClass(Class< ? > taskClass)
	{
		Class< ? > runAfter = taskClass.getAnnotation(DBVersionUpdateTask.class).runAfter();
		return !Object.class.equals(runAfter) ? runAfter : null;
	}

	/**
	 * Checks for:
	 * <ol>
	 * <li>multiple non-final Tasks in more than one version;</li>
	 * <li>final tasks with a higher version than a non-final task</li>
	 * </ol>
	 * 
	 * @see #checkTaskAnnotationIntegrity(Class)
	 * @require taskInstances != null
	 */
	private void checkTaskIntegrity(List<Object> taskInstances)
	{
		String firstNonFinalVersion = null;
		for (String version : getVersions(taskInstances))
		{
			boolean nonFinal = !isFinal(version, taskInstances);
			if (nonFinal)
			{
				if (firstNonFinalVersion != null && version != null)
				{
					throw new IllegalStateException("More than one non-final versions found: "
						+ firstNonFinalVersion + " and " + version);
				}
				if (version != null)
					firstNonFinalVersion = version;
			}
			else if (firstNonFinalVersion != null)
			{
				throw new IllegalStateException("Non-final version before a final version: "
					+ firstNonFinalVersion + " (non-final) and " + version + " (final)");
			}
		}
		for (Object taskInstance : taskInstances)
		{
			checkTaskAnnotationIntegrity(taskInstance.getClass());
		}
	}

	/**
	 * Checks whether the annotation structure for this taskClass is consistent
	 * 
	 * @require taskClass != null
	 * @throws IllegalArgumentException
	 *             when
	 *             <ul>
	 *             <li>runAfter property targets a class which is not annotated with
	 *             DBVersionUpdateTask</li>
	 *             <li>runAfter property targets a class with a higher version</li>
	 *             <li>circular reference: runAfter property targets a class which
	 *             (eventually) targets back the same class</li>
	 *             </ul>
	 */
	private void checkTaskAnnotationIntegrity(Class< ? > taskClass)
	{
		if (getVersion(taskClass) == null && isFinal(taskClass))
			throw new IllegalArgumentException("final task with no version found: " + taskClass
				+ " must either define a version or must not be final");
		checkCircularReference(taskClass, new LinkedHashSet<Class< ? >>());
	}

	/**
	 * @require taskClass != null
	 */
	private void checkCircularReference(Class< ? > taskClass, Set<Class< ? >> occurences)
	{
		Class< ? > runAfter = getRunAfterClass(taskClass);
		if (taskClass.equals(runAfter) || occurences.contains(taskClass))
		{
			throw new IllegalArgumentException("circular reference: runAfter property on "
				+ taskClass.getName()
				+ " targets a class which (eventually) targets back the same class");
		}
		if (runAfter != null)
		{
			DBVersionUpdateTask annotation = runAfter.getAnnotation(DBVersionUpdateTask.class);
			if (annotation == null)
			{
				throw new IllegalArgumentException("runAfter property on " + taskClass.getName()
					+ " targets a class which is not annotated with "
					+ DBVersionUpdateTask.class.getSimpleName() + ": " + runAfter.getName());
			}
			if (comparator.compare(getVersion(taskClass), getVersion(runAfter)) < 0)
			{
				throw new IllegalArgumentException("runAfter property on " + taskClass.getName()
					+ " (" + getVersion(taskClass) + ") targets a class with a higher version: "
					+ getVersion(runAfter));
			}

			occurences.add(taskClass);
			checkCircularReference(runAfter, occurences);
		}
	}

	/**
	 * Returns all distinct versions of taskInstances Note: the result order is the same
	 * as the order of taskInstances
	 * 
	 * @return != null
	 */
	private List<String> getVersions(List<Object> taskInstances)
	{
		Set<String> versions = new LinkedHashSet<String>();
		for (Object taskInstance : taskInstances)
		{
			versions.add(getVersion(taskInstance.getClass()));
		}
		return new ArrayList<String>(versions);
	}

	private boolean isFinal(String version, List<Object> taskInstances)
	{
		for (Object taskInstance : taskInstances)
		{
			if (JavaUtil.equalsOrBothNull(version, getVersion(taskInstance.getClass()))
				&& !isFinal(taskInstance.getClass()))
			{
				return false;
			}
		}
		return true;
	}

	private String getMarkName(Class< ? > clazz, Method method)
	{
		return clazz.getName() + "." + method.getName();
	}

	private String getMarkName(Class< ? > clazz)
	{
		return clazz.getName();
	}

	protected DBVersionHandle getHandle()
	{
		return dbHandle;
	}

	private String formatDuration(long milliseconds)
	{
		if (milliseconds >= 0)
		{
			double seconds = milliseconds / 1000.0;
			double minutes = seconds / 60.0;

			if (minutes >= 1.0)
			{
				return String.format("%.2f", minutes) + " "
					+ (minutes > 1.0 ? "minutes" : "minute");
			}

			if (seconds >= 1.0)
			{
				return String.format("%.2f", seconds) + " "
					+ (seconds > 1.0 ? "seconds" : "second");
			}

			return milliseconds + " milliseconds";
		}
		else
		{
			return "N/A";
		}
	}

	protected DBVersionMarkDao getVersionDAO()
	{
		return versionDAO;
	}

}