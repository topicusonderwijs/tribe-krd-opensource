package nl.topicus.cobra.logging.log4j;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.log4j.RollingFileAppender;
import org.apache.log4j.helpers.LogLog;

/**
 * RollingFileAppender implementatie die rekening houdt met de context van de webapp.
 * 
 * @author boschman
 */
public class ContextAwareRollingFileAppender extends RollingFileAppender
{
	/**
	 * The directory in which log files are created. Wihtout a leading slash, this is
	 * relative to the Tomcat home directory.
	 */
	private String m_directory = "logs";

	/**
	 * The default constructor does nothing.
	 */
	public ContextAwareRollingFileAppender()
	{
	}

	public void setDirectory(String directory)
	{
		m_directory = directory;
	}

	/**
	 * @see org.apache.log4j.DailyRollingFileAppender#activateOptions()
	 */
	@Override
	public void activateOptions()
	{
		// Default filename
		String file = m_directory.concat("/").concat(getFile());

		try
		{
			InitialContext context = new InitialContext();

			String value = (String) context.lookup("java:comp/env/log4j/context");

			if (value != null && value.length() > 0)
			{
				file = m_directory.concat("/").concat(value).concat("/").concat(getFile());
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

		setFile(file);

		super.activateOptions();
	}
}
