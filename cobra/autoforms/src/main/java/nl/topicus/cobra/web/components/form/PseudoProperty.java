package nl.topicus.cobra.web.components.form;

import java.io.Serializable;

import nl.topicus.cobra.reflection.Property;

/**
 * PseudoProperty is een {@link Property} die niet in de class voorkomt. Dit kan
 * bijvoorbeeld handig zijn om velden te tonen in een AutoFieldSet die niet bij een
 * property horen.
 * 
 * @author papegaaij
 */
public class PseudoProperty<X, Y, Z> extends Property<X, Y, Z> implements Serializable
{
	private static final long serialVersionUID = 1L;

	protected PseudoProperty()
	{

	}

	public PseudoProperty(Class<Y> declaringClass, String name, Class<Z> type)
	{
		super(declaringClass, name, type, null, null, null);
	}

	@Override
	public boolean isWriteAllowed()
	{
		return true;
	}

	@Override
	public boolean isReadAllowed()
	{
		return true;
	}
}
