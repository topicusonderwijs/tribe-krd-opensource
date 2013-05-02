package nl.topicus.cobra.web.components.form.modifier;

import java.lang.reflect.Method;

import nl.topicus.cobra.reflection.MethodNotFoundException;
import nl.topicus.cobra.reflection.Property;
import nl.topicus.cobra.reflection.ReflectionUtil;
import nl.topicus.cobra.web.components.form.AutoFieldSet;
import nl.topicus.cobra.web.components.form.AutoForm;
import nl.topicus.cobra.web.components.form.AutoFormValidator;
import nl.topicus.cobra.web.components.form.FieldProperties;

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>
 * FieldAdapter voor {@link AutoFormValidator}. Wanneer je de {@link AutoFormValidator}
 * van een entiteit wilt overschrijven is dit wat je nodig hebt. Het overschrijven is
 * omslachtig daarom hoef je het niet zelf te doen.
 * </p>
 * 
 * De bedoeling is dat je op je pagina een subclass maakt met de {@link AutoForm}
 * annotatie op de
 * {@link AutoFormValidatorFieldModifier#getValidators(AutoFieldSet, Property, FieldProperties)}
 * functie. Dit omdat je annotaties niet zelf kan aanmaken. Bijvoorbeeld:
 * 
 * <pre>
 * public class BegindatumFieldAdapter extends AutoFormValidatorFieldModifier
 * {
 * 	private static final long serialVersionUID = 1L;
 * 
 * 	public BegindatumFieldAdapter(String propertyName, Action action)
 * 	{
 * 		super(propertyName, action);
 * 	}
 * 
 * 	&#064;Override
 * 	&#064;AutoForm(validators = {
 * 		&#064;AutoFormValidator(formValidator = BegindatumVoorEinddatumValidator.class, otherProperties = {&quot;einddatum&quot;}),
 * 		&#064;AutoFormValidator(validator = DatumVandaagOfToekomstValidator.class)})
 * 	public IModel getValidators(AutoFieldSet fieldSet, Property property,
 * 			FieldProperties fieldProperties)
 * 	{
 * 		return super.getValidators(fieldSet, property, fieldProperties);
 * 	}
 * }
 * </pre>
 * 
 * @author hoeve
 */
public abstract class AutoFormValidatorFieldModifier extends MultiFieldAdapter
{
	private static final long serialVersionUID = 1L;

	private static final Logger log = LoggerFactory.getLogger(AutoFormValidatorFieldModifier.class);

	public AutoFormValidatorFieldModifier(String... propertyNames)
	{
		super(Action.VALIDATOR, propertyNames);
	}

	@Override
	public <T> IModel<AutoFormValidator[]> getValidators(AutoFieldSet<T> fieldSet,
			Property<T, ? , ? > property, FieldProperties<T, ? , ? > fieldProperties)
	{
		try
		{
			Method annotMethod =
				ReflectionUtil.findMethod(false, getClass(), "getValidators", AutoFieldSet.class,
					Property.class, FieldProperties.class);
			if (annotMethod != null)
			{
				AutoForm annotPropertyAutoForm = annotMethod.getAnnotation(AutoForm.class);
				return annotPropertyAutoForm != null ? new Model<AutoFormValidator[]>(
					annotPropertyAutoForm.validators()) : new Model<AutoFormValidator[]>();
			}
		}
		catch (MethodNotFoundException e)
		{
			// hier gaat iets fout
			log.error("Kan de functie getValidators niet vinden.", e);
		}

		return null;
	}
}
