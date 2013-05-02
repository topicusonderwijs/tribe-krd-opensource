package nl.topicus.cobra.web.components.datapanel;

import java.io.Serializable;

public class CriteriaBean implements Serializable
{
	private static final long serialVersionUID = 1L;

	private String expression;

	private String value;

	public CriteriaBean()
	{

	}

	public void setExpression(String expression)
	{
		this.expression = expression;
	}

	public String getExpression()
	{
		return expression;
	}

	public void setValue(String value)
	{
		this.value = value;
	}

	public String getValue()
	{
		return value;
	}

}