/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.web.components.choice;

import java.util.List;

import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.web.components.choice.AbstractAjaxDropDownChoice;
import nl.topicus.eduarte.dao.helpers.GroepstypeDataAccessHelper;
import nl.topicus.eduarte.entities.groep.Groepstype;
import nl.topicus.eduarte.web.components.choice.renderer.EntiteitToStringRenderer;
import nl.topicus.eduarte.zoekfilters.GroepstypeZoekFilter;

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;

/**
 * @author hoeve
 */
public class GroepstypeCombobox extends AbstractAjaxDropDownChoice<Groepstype>
{
	private static final long serialVersionUID = 1L;

	public GroepstypeCombobox(String id)
	{
		this(id, null);
	}

	public GroepstypeCombobox(String id, IModel<Groepstype> model)
	{
		super(id, model, new ChoicesModel(null));
	}

	public GroepstypeCombobox(String id, IModel<Groepstype> model, Boolean plaatsingsgroep)
	{
		super(id, model, new ChoicesModel(plaatsingsgroep));
	}

	public GroepstypeCombobox(String id, IModel<Groepstype> model,
			IModel< ? extends List< ? extends Groepstype>> choices)
	{
		super(id, model, choices, new EntiteitToStringRenderer());
	}

	private static final class ChoicesModel extends LoadableDetachableModel<List<Groepstype>>
	{
		private static final long serialVersionUID = 1L;

		private final Boolean plaatsingsgroep;

		private ChoicesModel(Boolean plaatsingsgroep)
		{
			this.plaatsingsgroep = plaatsingsgroep;
		}

		@Override
		protected List<Groepstype> load()
		{
			GroepstypeZoekFilter filter = new GroepstypeZoekFilter();
			filter.setActief(true);
			filter.setPlaatsingsgroep(plaatsingsgroep);
			filter.addOrderByProperty("naam");

			return DataAccessRegistry.getHelper(GroepstypeDataAccessHelper.class).list(filter);
		}
	}
}
