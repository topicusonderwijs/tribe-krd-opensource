/*
 * $Id: StatelessPasswordTextField.java,v 1.1 2007-07-12 12:22:32 loite Exp $
 * $Revision: 1.1 $
 * $Date: 2007-07-12 12:22:32 $
 *
 * ====================================================================
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.cobra.web.components.text;

import java.util.Map;

import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.model.IModel;

/**
 * PasswordTextField dat automatische zorgt dat wachtwoordmanagers van browsers niet
 * werken.
 * 
 * @author dashorst
 * @author marrink
 */
public final class StatelessPasswordTextField extends PasswordTextField
{
	private static final long serialVersionUID = 1L;

	protected static final String[] EMPTY_STRING_ARRAY = new String[] {""};

	/**
	 * Houdt timestamp vast voor het genereren van unieke veldnamen, zodat deze niet door
	 * de browser vastgehouden kunnen worden.
	 */
	private long timestamp = System.currentTimeMillis();

	public StatelessPasswordTextField(String id, IModel<String> model)
	{
		super(id, model);
	}

	public StatelessPasswordTextField(String id)
	{
		super(id);
	}

	@Override
	public String getInputName()
	{
		// veiligheids dingetje
		return super.getInputName() + "_" + timestamp;
	}

	/**
	 * Gets the request parameters for this component as strings.
	 * 
	 * @return The values in the request for this component
	 */
	@Override
	public String[] getInputAsArray()
	{
		Map<String, String[]> params = getForm().getRequest().getParameterMap();
		String prefix = super.getInputName() + "_";
		String[] values = null;
		for (String key : params.keySet())
			if (key.startsWith(prefix))
			{
				values = params.get(key);
				break;
			}
		if (!isInputNullable())
		{
			if (values != null && values.length == 1 && values[0] == null)
			{
				// we the key got passed in (otherwise values would be null),
				// but the value was set to null.
				// As the servlet spec isn't clear on what to do with 'empty'
				// request values - most return an empty string, but some null -
				// we have to workaround here and deliberately set to an empty
				// string if the the component is not nullable (text components)
				return EMPTY_STRING_ARRAY;
			}
		}
		return values;
	}
}
