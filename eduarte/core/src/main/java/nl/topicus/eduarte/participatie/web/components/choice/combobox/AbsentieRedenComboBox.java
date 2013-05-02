/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.participatie.web.components.choice.combobox;

import java.util.List;

import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.util.ComponentUtil;
import nl.topicus.eduarte.dao.participatie.helpers.AbsentieRedenDataAccessHelper;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.entities.organisatie.Locatie;
import nl.topicus.eduarte.entities.organisatie.OrganisatieEenheid;
import nl.topicus.eduarte.entities.participatie.AbsentieReden;
import nl.topicus.eduarte.entities.participatie.enums.AbsentieSoort;
import nl.topicus.eduarte.participatie.zoekfilters.AbsentieRedenZoekFilter;
import nl.topicus.eduarte.zoekfilters.OrganisatieEenheidLocatieAuthorizationContext;
import nl.topicus.eduarte.zoekfilters.AbstractOrganisatieEenheidLocatieZoekFilter.SelectieMode;

import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;

/**
 * @author vandekamp
 */
public class AbsentieRedenComboBox extends DropDownChoice<AbsentieReden>
{
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor voor AutoForms
	 * 
	 * @param id
	 * @param model
	 */
	public AbsentieRedenComboBox(String id, IModel<AbsentieReden> model)
	{
		super(id, model, (IModel<List<AbsentieReden>>) null, new AbsentieRedenRenderer());
		setChoices(new AbsentieRedenModel(null, null, false, false));
	}

	public AbsentieRedenComboBox(String id, IModel<AbsentieReden> model, AbsentieSoort soort)
	{
		super(id, model, (IModel<List<AbsentieReden>>) null, new AbsentieRedenRenderer());
		setChoices(new AbsentieRedenModel(null, null, false, false, soort));
	}

	public AbsentieRedenComboBox(String id, IModel<AbsentieReden> model,
			IModel<Verbintenis> verbintenisModel, boolean required, boolean alleenActieveRedenen)
	{
		super(id, model, (IModel<List<AbsentieReden>>) null, new AbsentieRedenRenderer());
		Verbintenis verbintenis = verbintenisModel.getObject();
		setChoices(new AbsentieRedenModel(verbintenis.getOrganisatieEenheid(), verbintenis
			.getLocatie(), alleenActieveRedenen, false));
		setRequired(required);
	}

	/**
	 * Geeft een combobox met alle gedefinieerde absentie redenen voor deze instelling die
	 * niet gekoppeld zijn aan een specifieke organistatie-eenheid, eventueel required.
	 */
	public AbsentieRedenComboBox(String id, boolean required, boolean alleenActieveRedenen)
	{
		this(id, required, alleenActieveRedenen, false);
	}

	/**
	 * Geeft een combobox met alle gedefinieerde absentie redenen voor deze instelling die
	 * niet gekoppeld zijn aan een specifieke organistatie-eenheid, eventueel required.
	 */
	public AbsentieRedenComboBox(String id, boolean required, boolean alleenActieveRedenen,
			boolean alleenToegestaanVoorDeelnemers)
	{
		this(id, null, null, required, alleenActieveRedenen, alleenToegestaanVoorDeelnemers);
	}

	/**
	 * Geeft een combobox met alle gedefinieerde absentie redenen voor deze instelling die
	 * niet gekoppeld zijn aan een specifieke organistatie-eenheid, + de absentie redenen
	 * die gekoppeld zijn aan de meegegeven organisatieeenheid, evetueel required.
	 */
	public AbsentieRedenComboBox(String id, OrganisatieEenheid organisatieEenheid, Locatie locatie,
			boolean required, boolean alleenActieveRedenen, boolean alleenToegestaanVoorDeelnemers)
	{
		super(id, (IModel<List<AbsentieReden>>) null, new AbsentieRedenRenderer());
		setChoices(new AbsentieRedenModel(organisatieEenheid, locatie, alleenActieveRedenen,
			alleenToegestaanVoorDeelnemers));
		setRequired(required);
	}

	private class AbsentieRedenModel extends LoadableDetachableModel<List<AbsentieReden>>
	{
		private static final long serialVersionUID = 1L;

		private AbsentieRedenZoekFilter filter;

		public AbsentieRedenModel(OrganisatieEenheid organisatieEenheid, Locatie locatie,
				boolean alleenActieveRedenen, boolean alleenToegestaanVoorDeelnemers)
		{
			this(organisatieEenheid, locatie, alleenActieveRedenen, alleenToegestaanVoorDeelnemers,
				null);
		}

		public AbsentieRedenModel(OrganisatieEenheid organisatieEenheid, Locatie locatie,
				boolean alleenActieveRedenen, boolean alleenToegestaanVoorDeelnemers,
				AbsentieSoort absentieSoort)
		{
			filter = new AbsentieRedenZoekFilter();
			filter.setAuthorizationContext(new OrganisatieEenheidLocatieAuthorizationContext(
				AbsentieRedenComboBox.this));

			filter.setLocatie(locatie);
			if (organisatieEenheid != null)
			{
				filter.setOrganisatieEenheid(organisatieEenheid);
				filter.setOrganisatieEenheidSelectie(SelectieMode.PARENTS);
			}
			if (absentieSoort != null)
				filter.setAbsentieSoort(absentieSoort);
			filter.setAlleenToegestaanVoorDeelnemers(alleenToegestaanVoorDeelnemers);
			if (alleenActieveRedenen)
				filter.setActief(true);
			filter.addOrderByProperty("omschrijving");
		}

		@Override
		protected List<AbsentieReden> load()
		{
			return DataAccessRegistry.getHelper(AbsentieRedenDataAccessHelper.class).list(filter);
		}

		@Override
		protected void onDetach()
		{
			ComponentUtil.detachQuietly(filter);
			super.onDetach();
		}

	}

	private static class AbsentieRedenRenderer implements IChoiceRenderer<AbsentieReden>
	{
		private static final long serialVersionUID = 1L;

		@Override
		public Object getDisplayValue(AbsentieReden object)
		{
			return object.getOmschrijving();
		}

		@Override
		public String getIdValue(AbsentieReden object, int index)
		{
			return object.getId().toString();
		}

	}
}
