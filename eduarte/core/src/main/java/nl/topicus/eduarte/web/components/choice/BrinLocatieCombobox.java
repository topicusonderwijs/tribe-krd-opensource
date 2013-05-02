/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.web.components.choice;

import java.util.List;

import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.eduarte.app.EduArteContext;
import nl.topicus.eduarte.dao.helpers.BrinDataAccessHelper;
import nl.topicus.eduarte.entities.organisatie.Brin;
import nl.topicus.eduarte.web.components.choice.renderer.EntiteitToStringRenderer;
import nl.topicus.eduarte.zoekfilters.BrinZoekFilter;

import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;

/**
 * @author idserda
 */
public class BrinLocatieCombobox extends DropDownChoice<Brin>
{
	private static final long serialVersionUID = 1L;

	private static final class ListModel extends LoadableDetachableModel<List<Brin>>
	{
		private static final long serialVersionUID = 1L;

		@Override
		protected List<Brin> load()
		{
			BrinZoekFilter brinZoekFilter = new BrinZoekFilter();
			brinZoekFilter.setCode(EduArteContext.get().getInstelling().getBrincode().getCode());

			BrinDataAccessHelper brinDOA = DataAccessRegistry.getHelper(BrinDataAccessHelper.class);

			return brinDOA.list(brinZoekFilter);
		}
	}

	public BrinLocatieCombobox(String id)
	{
		this(id, null);
	}

	public BrinLocatieCombobox(String id, IModel<Brin> model)
	{
		super(id, model, new ListModel(), new EntiteitToStringRenderer());
	}
}