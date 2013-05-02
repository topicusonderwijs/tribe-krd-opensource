/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.participatie.web.components.choice.combobox;

import java.util.List;

import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.util.ComponentUtil;
import nl.topicus.eduarte.dao.participatie.helpers.PeriodeIndelingDataAccessHelper;
import nl.topicus.eduarte.entities.organisatie.OrganisatieEenheid;
import nl.topicus.eduarte.entities.participatie.PeriodeIndeling;
import nl.topicus.eduarte.participatie.zoekfilters.PeriodeIndelingZoekFilter;

import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.model.LoadableDetachableModel;

/**
 * @author vandekamp
 */
public class PeriodeIndelingComboBox extends DropDownChoice<PeriodeIndeling>
{
	private static final long serialVersionUID = 1L;

	/**
	 * Geeft een combobox met alle gedefinieerde periodeIndeling voor deze instelling die
	 * niet gekoppeld zijn aan een specifieke organistatie-eenheid.
	 * 
	 * @param id
	 */
	public PeriodeIndelingComboBox(String id)
	{
		this(id, null, true);
	}

	/**
	 * Geeft een combobox met alle gedefinieerde periodeIndeling voor deze instelling die
	 * niet gekoppeld zijn aan een specifieke organistatie-eenheid, + de periodes die
	 * gekoppeld zijn aan de meegegeven organisatieeenheid, evetueel required.
	 * 
	 * @param id
	 * @param organisatieEenheid
	 * @param required
	 */
	public PeriodeIndelingComboBox(String id, OrganisatieEenheid organisatieEenheid,
			boolean required)
	{
		super(id, new PeriodeIndelingModel(organisatieEenheid), new PeriodeIndelingRenderer());
		setRequired(required);
	}

	private static class PeriodeIndelingModel extends
			LoadableDetachableModel<List<PeriodeIndeling>>
	{
		private static final long serialVersionUID = 1L;

		private PeriodeIndelingZoekFilter filter;

		public PeriodeIndelingModel(OrganisatieEenheid organisatieEenheid)
		{
			filter = new PeriodeIndelingZoekFilter();
			filter.setOrganisatieEenheid(organisatieEenheid);
			getObject();
		}

		@Override
		protected List<PeriodeIndeling> load()
		{
			return DataAccessRegistry.getHelper(PeriodeIndelingDataAccessHelper.class).list(filter);
		}

		@Override
		protected void onDetach()
		{
			if (filter != null)
				ComponentUtil.detachQuietly(filter);
			super.onDetach();
		}

	}

	private static class PeriodeIndelingRenderer implements IChoiceRenderer<PeriodeIndeling>
	{
		private static final long serialVersionUID = 1L;

		@Override
		public Object getDisplayValue(PeriodeIndeling object)
		{
			PeriodeIndeling periodeIndeling = object;
			String displayValue = periodeIndeling.getOmschrijving();
			if (periodeIndeling.getOrganisatieEenheid() != null)
				displayValue += " (" + periodeIndeling.getOrganisatieEenheid().getNaam() + ")";
			return displayValue;
		}

		@Override
		public String getIdValue(PeriodeIndeling object, int index)
		{
			return (object).getId().toString();
		}

	}
}
