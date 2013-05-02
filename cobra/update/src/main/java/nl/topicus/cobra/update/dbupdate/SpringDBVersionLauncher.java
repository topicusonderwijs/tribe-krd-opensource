package nl.topicus.cobra.update.dbupdate;

import nl.topicus.cobra.update.ComponentScanner;

public class SpringDBVersionLauncher extends DBVersionLauncher
{

	@Override
	protected DBVersionManager getManager()
	{
		if (getDbHandle() == null)
		{
			throw new IllegalStateException(
				"this.dbHandle cannot be null; use setDbHandle() to populate this field");
		}
		if (getVersionDAO() == null)
		{
			throw new IllegalStateException(
				"this.versionDAO cannot be null; use setVersionDAO() to populate this field");
		}
		return new SpringDBVersionManager(getDbHandle(), getVersionDAO(), new ComponentScanner(),
			this);
	}

}
