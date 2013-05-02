package nl.topicus.cobra.util;

/**
 * A convenience class for retrieving the string value of a system property as a
 * privileged action. This class is directly copied from
 * sun.security.action.GetPropertyAction in order to avoid depending on the
 * sun.security.action package. &ltp&gtAn instance of this class can be used as the
 * argument of &ltcode&gtAccessController.doPrivileged</code>. &ltp&gtThe following code
 * retrieves the value of the system property named &ltcode>"prop"</code> as a privileged
 * action: &ltp> &ltpre> String s = (String) java.security.AccessController.doPrivileged(
 * new GetPropertyAction("prop")); </pre>
 * 
 * @author Roland Schemers
 * @author Ken Cavanaugh
 * @see java.security.PrivilegedAction
 * @see java.security.AccessController
 */
public class GetPropertyAction implements java.security.PrivilegedAction<String>
{
	private String theProp;

	private String defaultVal;

	/**
	 * Constructor that takes the name of the system property whose string value needs to
	 * be determined.
	 * 
	 * @param theProp
	 *            the name of the system property.
	 */
	public GetPropertyAction(String theProp)
	{
		this.theProp = theProp;
	}

	/**
	 * Constructor that takes the name of the system property and the default value of
	 * that property.
	 * 
	 * @param theProp
	 *            the name of the system property.
	 * @param defaultVal
	 *            the default value.
	 */
	public GetPropertyAction(String theProp, String defaultVal)
	{
		this.theProp = theProp;
		this.defaultVal = defaultVal;
	}

	/**
	 * Determines the string value of the system property whose name was specified in the
	 * constructor.
	 * 
	 * @return the string value of the system property, or the default value if there is
	 *         no property with that key.
	 */
	@Override
	public String run()
	{
		String value = System.getProperty(theProp);
		return (value == null) ? defaultVal : value;
	}
}
