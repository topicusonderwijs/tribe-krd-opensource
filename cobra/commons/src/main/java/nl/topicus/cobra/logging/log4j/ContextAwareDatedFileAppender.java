package nl.topicus.cobra.logging.log4j;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.log4j.helpers.LogLog;

/**
 * Omdat op de acceptatie omgeving dezelfde webapp meerdere keren gedeployed is onder een
 * verschillende context moet log4j rekening houden met deze context. Anders proberen
 * meerdere loggers naar dezelfde file te schrijven en dat gaat niet goed. Het lukt (mij)
 * niet om met de huidige log4j 1.2.15 versie via JNDI of log4j.xml de filename
 * afhankelijk van de context correct te zetten.
 * 
 * @author boschman
 */
public class ContextAwareDatedFileAppender extends DatedFileAppender
{
	/**
	 * The default constructor does nothing.
	 */
	public ContextAwareDatedFileAppender()
	{
	}

	/**
	 * Instantiate a <code>ContextAwareDailyRollingFileAppender</code> and open the file
	 * designated by <code>filename</code>. The opened filename will become the ouput
	 * destination for this appender.
	 * 
	 * @param directory
	 * @param prefix
	 * @param suffix
	 */
	public ContextAwareDatedFileAppender(String directory, String prefix, String suffix)
	{
		super(directory, prefix, suffix);
		activateOptions();
	}

	/**
	 * @see org.apache.log4j.DailyRollingFileAppender#activateOptions()
	 */
	@Override
	public void activateOptions()
	{
		try
		{
			InitialContext context = new InitialContext();

			String value = (String) context.lookup("java:comp/env/log4j/context");

			if (value != null && value.length() > 0)
			{
				setDirectory(getDirectory().concat("/").concat(value));
			}
			else
			{
				LogLog.warn("No context found for this logger, set JNDI property 'log4j/context'.");
			}
		}
		catch (NamingException e)
		{
			// ignore, will behave like the normal DatedFileAppender
		}

		super.activateOptions();
	}
}
