/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.participatie.web.components.choice.combobox;

import java.util.List;

import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.util.ComponentUtil;
import nl.topicus.eduarte.dao.participatie.helpers.MaatregelDataAccessHelper;
import nl.topicus.eduarte.entities.participatie.Maatregel;
import nl.topicus.eduarte.participatie.zoekfilters.MaatregelZoekFilter;
import nl.topicus.eduarte.zoekfilters.OrganisatieEenheidLocatieAuthorizationContext;

import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;

/**
 * @author vandekamp
 * @author jutten
 */
public class MaatregelComboBox extends DropDownChoice<Maatregel>
{
	private static final long serialVersionUID = 1L;

	public MaatregelComboBox(String id, IModel<Maatregel> model)
	{
		super(id, model, (IModel<List<Maatregel>>) null, new MaatregelRenderer());
		setChoices(new MaatregelModel());
	}

	/**
	 * Geeft een combobox met alle maatreegelen voor deze instelling die niet gekoppeld
	 * zijn aan een specifieke organistatie-eenheid, evetueel required.
	 * 
	 */
	public MaatregelComboBox(String id, boolean required)
	{
		super(id, (IModel<List<Maatregel>>) null, new MaatregelRenderer());
		setChoices(new MaatregelModel());
		setRequired(required);
	}

	private class MaatregelModel extends LoadableDetachableModel<List<Maatregel>>
	{
		private static final long serialVersionUID = 1L;

		private MaatregelZoekFilter filter;

		public MaatregelModel()
		{
			filter = new MaatregelZoekFilter();
			filter.setAuthorizationContext(new OrganisatieEenheidLocatieAuthorizationContext(
				MaatregelComboBox.this));
			filter.setActief(true);
			getObject();
		}

		@Override
		protected List<Maatregel> load()
		{
			return DataAccessRegistry.getHelper(MaatregelDataAccessHelper.class).list(filter);
		}

		@Override
		protected void onDetach()
		{
			ComponentUtil.detachQuietly(filter);
			super.onDetach();
		}
	}

	private static class MaatregelRenderer implements IChoiceRenderer<Maatregel>
	{
		private static final long serialVersionUID = 1L;

		@Override
		public Object getDisplayValue(Maatregel object)
		{
			return object.getOmschrijving();
		}

		@Override
		public String getIdValue(Maatregel object, int index)
		{
			return object.getId().toString();
		}
	}
}
