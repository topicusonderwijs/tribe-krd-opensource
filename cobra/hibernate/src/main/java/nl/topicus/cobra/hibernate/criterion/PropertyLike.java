package nl.topicus.cobra.hibernate.criterion;

import org.hibernate.criterion.PropertyExpression;

/**
 * Expressie voor property1 like property2
 * 
 * @author loite
 */
public final class PropertyLike extends PropertyExpression
{
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor
	 * 
	 * @param propertyLeft
	 * @param propertyRight
	 */
	public PropertyLike(String propertyLeft, String propertyRight)
	{
		super(propertyLeft, propertyRight, " like ");
	}
}
