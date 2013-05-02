/*
 s * Copyright (c) 2005, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.cobra.models;

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Simpel model to return a different string then true or false for a boolean.
 * 
 * @author marrink
 */
public class BooleanModel implements IModel<Object>
{
	private static final Logger log = LoggerFactory.getLogger(BooleanModel.class);

	private static final long serialVersionUID = 1L;

	private String isTrue;

	private String isFalse;

	private IModel<Boolean> nested;

	public BooleanModel()
	{
		super();
		isTrue = Boolean.TRUE.toString();
		isFalse = Boolean.FALSE.toString();
	}

	public BooleanModel(String trueString, String falseString, boolean value)
	{
		isTrue = trueString;
		isFalse = falseString;
		nested = new Model<Boolean>(value);
	}

	public BooleanModel(String trueString, String falseString, IModel<Boolean> value)
	{
		isTrue = trueString;
		isFalse = falseString;
		nested = value;
	}

	public Object getObject()
	{
		Boolean value = nested.getObject();

		if (value == null)
		{
			log.warn("Boolean in model is null");
			return isFalse;
		}
		else if (value)
			return isTrue;
		else
			return isFalse;
	}

	public void setObject(Object object)
	{
		if (object instanceof Boolean)
			nested.setObject((Boolean) object);
		else
			throw new IllegalArgumentException("Only booleans allowed");
	}

	public void detach()
	{
		nested.detach();
	}
}
