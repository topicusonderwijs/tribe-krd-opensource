/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.cobra.templates.documents.jrxml;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRField;
import nl.topicus.cobra.templates.resolvers.FieldResolver;

/**
 * @author Laurens Hop
 */
public class FieldResolverDataSource implements JRDataSource
{
	private final FieldResolver resolver;

	private String iteratorKeyString = null;

	public FieldResolverDataSource(FieldResolver resolver)
	{
		this.resolver = resolver;
	}

	public FieldResolverDataSource(FieldResolver resolver, String iteratorKeyString)
	{
		this.resolver = resolver;
		this.iteratorKeyString = iteratorKeyString;
	}

	@Override
	public Object getFieldValue(JRField jrField)
	{
		String name = jrField.getName();
		Object result = resolver.resolve(name);
		if (result == null)
			result = resolver.resolve(jrField.getDescription());

		return result;
	}

	@Override
	public boolean next()
	{
		return resolver.next(iteratorKeyString) != null;
	}

}
