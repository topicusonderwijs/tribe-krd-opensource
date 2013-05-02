/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.web.validators;

import java.util.HashMap;
import java.util.Map;

import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.eduarte.dao.helpers.SettingsDataAccessHelper;
import nl.topicus.eduarte.entities.settings.PasswordSetting;

import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.validator.AbstractValidator;

/**
 * Controleerd of het wachtwoord voldoet aan de eisen die er door de instelling aan
 * gegeven zijn.
 * 
 * @author marrink
 */
public class PasswordValidator extends AbstractValidator<String>
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	public PasswordValidator()
	{
	}

	/**
	 * @see org.apache.wicket.validation.validator.AbstractValidator#onValidate(org.apache.wicket.validation.IValidatable)
	 */
	@Override
	protected void onValidate(IValidatable<String> validatable)
	{
		String password = validatable.getValue();
		PasswordSetting setting =
			DataAccessRegistry.getHelper(SettingsDataAccessHelper.class).getSetting(
				PasswordSetting.class);
		if (!setting.getValue().validatePassword(password))
		{
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("eisen", setting.getValue().toString());
			error(validatable, params);
		}
	}

}
