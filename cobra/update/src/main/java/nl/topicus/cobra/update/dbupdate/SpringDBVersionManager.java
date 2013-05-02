package nl.topicus.cobra.update.dbupdate;

import java.lang.reflect.Method;

import nl.topicus.cobra.update.ComponentScanner;
import nl.topicus.cobra.update.dbupdate.annotation.DBVersionUpdate;
import nl.topicus.cobra.update.dbupdate.dbaccess.DBVersionHandle;
import nl.topicus.cobra.update.dbupdate.dbaccess.DBVersionMarkDao;
import nl.topicus.cobra.update.dbupdate.entity.DBVersionMark;
import nl.topicus.cobra.update.dbupdate.entity.DBVersionMark.MARK_TYPE;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Subclass voor projecten die Spring transacties gebruiken
 * 
 * @author hoeve
 */
public class SpringDBVersionManager extends DBVersionManager
{
	private final Logger log = LoggerFactory.getLogger(getClass());

	public SpringDBVersionManager(DBVersionMarkDao versionDAO)
	{
		super(null, versionDAO, null, null);
	}

	public SpringDBVersionManager(DBVersionHandle dbHandle, DBVersionMarkDao versionDAO,
			ComponentScanner componentScanner, DBVersionCallback callback)
	{
		super(dbHandle, versionDAO, componentScanner, callback);
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	@Override
	protected void checkIsFinal(Class< ? > taskClass, String markName)
	{
		if (isFinal(taskClass))
		{
			DBVersionMark mark =
				new DBVersionMark(getVersion(taskClass), markName, MARK_TYPE.FINAL_TASK);
			getVersionDAO().saveWithoutCommit(mark);
		}
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	@Override
	protected void runTaskMethod(Object taskInstance, Class< ? > taskClass, String version,
			Method method, String markName) throws Exception
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

		if (method.getAnnotation(DBVersionUpdate.class).isFinal())
		{
			getVersionDAO().saveWithoutCommit(
				new DBVersionMark(version, markName, MARK_TYPE.OPERATION));
			log.debug("saving Mark " + markName);
		}
	}
}
