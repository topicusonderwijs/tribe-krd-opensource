/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.krd.web.components.choice;

import java.util.List;

import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.web.components.choice.AbstractAjaxDropDownChoice;
import nl.topicus.eduarte.krd.dao.helpers.BronAanleverpuntDataAccessHelper;
import nl.topicus.eduarte.krd.entities.bron.BronAanleverpunt;
import nl.topicus.eduarte.web.components.choice.renderer.EntiteitPropertyRenderer;

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;

/**
 * @author vandekamp
 */
public class BronAanleverpuntComboBox extends AbstractAjaxDropDownChoice<BronAanleverpunt>
{
	private static final long serialVersionUID = 1L;

	private static final class ListModel extends LoadableDetachableModel<List<BronAanleverpunt>>
	{
		private static final long serialVersionUID = 1L;

		@Override
		protected List<BronAanleverpunt> load()
		{
			return DataAccessRegistry.getHelper(BronAanleverpuntDataAccessHelper.class)
				.getBronAanleverpunten();
		}

	}

	public BronAanleverpuntComboBox(String id)
	{
		this(id, null);
	}

	public BronAanleverpuntComboBox(String id, IModel<BronAanleverpunt> model)
	{
		super(id, model, new ListModel(), new EntiteitPropertyRenderer("nummer"));
	}
}
