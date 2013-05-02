/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.web.components.choice;

import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.modelsv2.ModelFactory;
import nl.topicus.cobra.web.components.choice.AbstractAjaxDropDownChoice;
import nl.topicus.eduarte.dao.helpers.dbs.GesprekSamenvattingZinDataAccessHelper;
import nl.topicus.eduarte.entities.dbs.trajecten.GesprekSamenvattingZin;

import org.apache.wicket.model.IModel;

/**
 * @author loite
 */
public class GesprekSamenvattingZinCombobox extends
		AbstractAjaxDropDownChoice<GesprekSamenvattingZin>
{
	private static final long serialVersionUID = 1L;

	public GesprekSamenvattingZinCombobox(String id)
	{
		this(id, null);
	}

	public GesprekSamenvattingZinCombobox(String id, IModel<GesprekSamenvattingZin> model)
	{
		super(id, model, ModelFactory.getListModel(DataAccessRegistry.getHelper(
			GesprekSamenvattingZinDataAccessHelper.class).list(GesprekSamenvattingZin.class)),
			new GesprekSamenvattingZinRenderer());
	}
}
