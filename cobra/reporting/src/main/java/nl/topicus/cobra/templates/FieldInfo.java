package nl.topicus.cobra.templates;

import java.io.Serializable;

/**
 * Class om veldinformatie op te slaan.
 * 
 * @author Laurens Hop
 */
@SuppressWarnings("unchecked")
public class FieldInfo implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String name;

	private Class fieldClass;

	private boolean valid;

	private String message;

	private String format;

	/**
	 * @param name
	 * @param fieldClass
	 * @param valid
	 * @param message
	 */
	public FieldInfo(String name, Class fieldClass, boolean valid, String message)
	{
		super();
		this.name = name;
		this.fieldClass = fieldClass;
		this.valid = valid;
		this.message = message;
	}

	/**
	 * @param name
	 */
	public void setName(String name)
	{
		this.name = name;
	}

	/**
	 * @param fieldClass
	 */
	public void setFieldClass(Class fieldClass)
	{
		this.fieldClass = fieldClass;
	}

	/**
	 * @param valid
	 */
	public void setValid(boolean valid)
	{
		this.valid = valid;
	}

	/**
	 * @param message
	 */
	public void setMessage(String message)
	{
		this.message = message;
	}

	/**
	 * @return name
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * @return fieldClass
	 */
	public Class getFieldClass()
	{
		return fieldClass;
	}

	/**
	 * @return valid
	 */
	public boolean isValid()
	{
		return valid;
	}

	/**
	 * @return message
	 */
	public String getMessage()
	{
		return message;
	}

	public String getFormat()
	{
		return format;
	}

	public void setFormat(String format)
	{
		this.format = format;
	}
}
