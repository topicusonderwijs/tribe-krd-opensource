/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.cobra.web.behaviors;

import org.apache.wicket.Component;
import org.apache.wicket.behavior.SimpleAttributeModifier;
import org.apache.wicket.markup.ComponentTag;

/**
 * Simpele attribute modifier die een waarde toevoegd ipv vervangt.
 * 
 * @author marrink
 */
public class AppendingAttributeModifier extends SimpleAttributeModifier
{
	private static final long serialVersionUID = 1L;

	private String separator;

	/**
	 * Modifier met als separator een spatie.
	 * 
	 * @param attribute
	 * @param value
	 */
	public AppendingAttributeModifier(String attribute, CharSequence value)
	{
		this(attribute, value, " ");
	}

	public AppendingAttributeModifier(String attribute, CharSequence value, String separator)
	{
		super(attribute, value);
		this.separator = separator;
	}

	@Override
	public void onComponentTag(Component component, ComponentTag tag)
	{
		if (isEnabled(component))
		{
			CharSequence old = tag.getString(getAttribute());
			if (old != null)
				tag.put(getAttribute(), old.toString() + separator + getValue());
			else
				tag.put(getAttribute(), getValue());
		}
	}

	public String getSeparator()
	{
		return separator;
	}

	public void setSeparator(String separator)
	{
		this.separator = separator;
	}
}
