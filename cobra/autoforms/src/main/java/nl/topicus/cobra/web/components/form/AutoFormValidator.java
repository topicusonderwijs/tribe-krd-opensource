package nl.topicus.cobra.web.components.form;

import nl.topicus.cobra.web.components.form.modifier.AutoFormValidatorFieldModifier;

import org.apache.wicket.Component;
import org.apache.wicket.markup.html.form.validation.IFormValidator;
import org.apache.wicket.validation.IValidator;

/**
 * Annotatie tbv het automagisch toevoegen van validatoren in {@link RenderMode#EDIT}.
 * Wanneer je dit op een speciale pagina wilt overschrijven moet je bij de
 * {@link AutoFormValidatorFieldModifier} zijn.
 * 
 * <p>
 * De opzet is als volgt:
 * <ul>
 * <li>De validator moet erven van IValidator</li>
 * <li>De constructor van de validator moet het {@link Component} als eerste argument
 * nemen.</li>
 * <li>De constructor van de validator moet de opgegeven overige {@link Component}en in de
 * opgegeven volgorde nemen.</li>
 * </ul>
 * </p>
 * 
 * <p>
 * Voorbeeld:
 * </p>
 * 
 * <pre>
 * &#064;AutoForm(validators={&#064;AutoFormValidator(formValidator=BegindatumVoorEinddatumValidator.class, &quot;einddatum&quot;)})
 * private Date begindatum;
 * </pre>
 * 
 * of
 * 
 * <pre>
 * &#064;AutoForm(validators = {@AutoFormValidator(validator = DatumInVerledenValidator.class)})
 * private Date begindatum;
 * </pre>
 * 
 * @author hoeve
 */
public @interface AutoFormValidator
{
	@SuppressWarnings("unchecked")
	Class< ? extends IValidator> validator() default IValidator.class;

	Class< ? extends IFormValidator> formValidator() default IFormValidator.class;

	String[] otherProperties() default {};
}
