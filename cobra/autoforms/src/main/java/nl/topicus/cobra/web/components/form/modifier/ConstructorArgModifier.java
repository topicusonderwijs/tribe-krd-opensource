package nl.topicus.cobra.web.components.form.modifier;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

import nl.topicus.cobra.reflection.InvocationFailedException;
import nl.topicus.cobra.reflection.MethodNotFoundException;
import nl.topicus.cobra.reflection.Property;
import nl.topicus.cobra.reflection.ReflectionUtil;
import nl.topicus.cobra.web.components.form.AutoFieldSet;
import nl.topicus.cobra.web.components.form.FieldProperties;

import org.apache.wicket.Component;
import org.apache.wicket.model.IDetachable;
import org.apache.wicket.model.IModel;

/**
 * Definieert extra constructor argumenten voor het instantieren van het veld. Dit kan
 * bijvoorbeeld gebruikt worden om een filter mee te geven aan een drop down. Er dient op
 * gelet te worden dat deze argumenten in de sessie terecht komen, en dus Serializable
 * moeten zijn en gedetached worden als het database entiteiten betreft. Als er models
 * meegegeven worden, zulen deze gedetached worden.
 * <p>
 * Er worden bij het instantieren twee constructoren geprobeerd: {@code <init>(id,
 * fieldSet, model, additional..)} en {@code <init>(id, model, additional..)}. Het
 * component dient tenminste 1 van die twee constructoren te hebben.
 * 
 * @author papegaaij
 */
public class ConstructorArgModifier extends SingleFieldAdapter
{
	private static final long serialVersionUID = 1L;

	private Serializable[] additionalArguments;

	/**
	 * Maakt een nieuwe ConstructorArgModifier voor de gegeven property.
	 * 
	 * @param propertyName
	 *            De naam van de property.
	 * @param additionalArguments
	 *            De extra argumenten die meegegeven moeten worden.
	 */
	public ConstructorArgModifier(String propertyName, Serializable... additionalArguments)
	{
		super(propertyName, Action.CREATION);
		this.additionalArguments = additionalArguments;
	}

	@Override
	public <T> Component createField(AutoFieldSet<T> fieldSet, String id, IModel< ? > model,
			Property<T, ? , ? > property, FieldProperties<T, ? , ? > fieldProperties)
	{
		ArrayList<Object> args = new ArrayList<Object>(additionalArguments.length + 3);
		args.add(id);
		args.add(fieldSet);
		args.add(model);
		args.addAll(Arrays.asList(additionalArguments));
		try
		{
			return ReflectionUtil.invokeConstructor(fieldProperties.getEditor(), args.toArray());
		}
		catch (InvocationFailedException e)
		{
			// ignoring exception for optional constructor
			if (!(e.getCause() instanceof MethodNotFoundException))
			{
				AutoFieldSet.LOG.info(e.getMessage(), e);
			}
		}
		// remove fieldset and try again
		args.remove(1);
		return ReflectionUtil.invokeConstructor(fieldProperties.getEditor(), args.toArray());
	}

	@Override
	public void detach()
	{
		super.detach();
		for (Serializable curArg : additionalArguments)
		{
			if (curArg instanceof IDetachable)
				((IDetachable) curArg).detach();
		}
	}
}
