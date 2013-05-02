package nl.topicus.eduarte.web.components.autoform;

import java.io.Serializable;

import nl.topicus.cobra.test.AllowedMethods;
import nl.topicus.cobra.web.components.form.modifier.ExtendedAjaxRefreshModifier;

/**
 * @author hoeve
 */
@AllowedMethods("public nl.topicus.cobra.web.components.form.modifier.ExtendedAjaxRefreshModifier(java.lang.String,java.io.Serializable[])")
public class EduArteAjaxRefreshModifier extends ExtendedAjaxRefreshModifier
{
	private static final long serialVersionUID = 1L;

	public EduArteAjaxRefreshModifier(String propertyName,
			Serializable... refreshComponentsAndFields)
	{
		super(propertyName, refreshComponentsAndFields);
	}
}
