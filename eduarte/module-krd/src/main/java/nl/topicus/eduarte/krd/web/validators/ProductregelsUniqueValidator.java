package nl.topicus.eduarte.krd.web.validators;

import java.util.HashMap;
import java.util.Map;

import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.entities.IdObject;
import nl.topicus.cobra.util.Asserts;
import nl.topicus.cobra.util.StringUtil;
import nl.topicus.eduarte.dao.helpers.ProductregelDataAccessHelper;
import nl.topicus.eduarte.entities.opleiding.Opleiding;

import org.apache.wicket.Component;
import org.apache.wicket.model.IModel;
import org.apache.wicket.util.lang.PropertyResolver;
import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.validator.AbstractValidator;

/**
 * Validator dat een bepaalde property van een productregel binnen 1 opleiding unique is.
 * Dit kan niet met de standaard UniqueConstraintValidator omdat landelijke productregels
 * niet direct aan de opleiding gekoppeld zijn, maar via het verbintenisgebied
 * 
 * @author vandekamp
 */
public class ProductregelsUniqueValidator<T> extends AbstractValidator<T>
{
	private static final long serialVersionUID = 1L;

	private final String objectNaam;

	private final Component rootComponent;

	private final String editableProperty;

	private final String[] properties;

	private IModel<Opleiding> opleidingModel;

	private String lidwoord = "de";

	/**
	 * Constructor
	 * 
	 * @param rootComponent
	 *            Het rootcomponent waaraan het databaseobject hangt, dus bijvoorbeeld het
	 *            form.
	 * @param objectNaam
	 *            De naam van het object dat gewijzigd wordt, bijvoorbeeld 'Leerling'.
	 * @param editableProperty
	 *            De naam van het property dat op het form gewijzigd kan worden,
	 *            bijvoorbeeld afkorting of leerlingnummer.
	 * @param properties
	 *            De overige properties waarmee in combinatie het wijzigbare property
	 *            uniek moet zijn, bijvoorbeeld {organisatie, parent}. Deze mogen niet
	 *            wijzigbaar zijn op het form.
	 */
	public ProductregelsUniqueValidator(Component rootComponent, String objectNaam,
			String editableProperty, IModel<Opleiding> opleidingModel, String... properties)
	{
		Asserts.assertNotNull("editableProperty", editableProperty);
		Asserts.assertNotNull("properties", properties);
		Asserts.assertNotNull("opleiding", opleidingModel.getObject());
		Asserts.assertGreaterThanZero("properties.length", properties.length);
		this.rootComponent = rootComponent;
		this.objectNaam = objectNaam;
		this.editableProperty = editableProperty;
		this.properties = properties;
		this.opleidingModel = opleidingModel;
	}

	@Override
	protected void onValidate(IValidatable<T> validatable)
	{
		Object value = validatable.getValue();
		if (value == null)
			return;
		IdObject object = (IdObject) rootComponent.getDefaultModelObject();
		Map<String, Object> propertiesMap = new HashMap<String, Object>(properties.length);

		if (!StringUtil.isEmpty(editableProperty))
			propertiesMap.put(editableProperty, value);

		for (String prop : properties)
		{
			propertiesMap.put(prop, PropertyResolver.getValue(prop, object));
		}

		if (!DataAccessRegistry.getHelper(ProductregelDataAccessHelper.class).isUnique(object,
			propertiesMap, opleidingModel.getObject()))
		{
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("value", value.toString());
			params.put("lidwoord", lidwoord);
			params.put("objectNaam", objectNaam.substring(objectNaam.lastIndexOf('.') + 1)
				.toLowerCase());
			error(validatable, "UniqueConstraintValidator.error", params);
		}
	}

	public Component getRootComponent()
	{
		return rootComponent;
	}
}
