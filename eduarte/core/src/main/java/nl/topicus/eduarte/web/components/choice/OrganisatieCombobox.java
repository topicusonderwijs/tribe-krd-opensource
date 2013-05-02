/*
 * Copyright (c) 2007, Topicus B.V. All rights
 * reserved.
 */
package nl.topicus.eduarte.web.components.choice;

import java.util.List;

import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.web.components.choice.AbstractAjaxDropDownChoice;
import nl.topicus.cobra.web.components.choice.render.ToStringRenderer;
import nl.topicus.eduarte.dao.helpers.OrganisatieDataAccessHelper;
import nl.topicus.eduarte.entities.organisatie.Organisatie;

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;

/**
 * Dropdown box voor het tonen en selecteren van alle organisaties uit het systeem.
 */
public class OrganisatieCombobox extends AbstractAjaxDropDownChoice<Organisatie>
{
	private static final long serialVersionUID = 1L;

	/**
	 * Creeert een DropDownChoice combobox.
	 * 
	 * @param id
	 *            het component id
	 * @param model
	 *            het model waarin de geselecteerde waarde gezet moet worden
	 * @param isRequired
	 */
	public OrganisatieCombobox(String id, IModel<Organisatie> model, boolean isRequired)
	{
		super(id, model, new OrganisatieModel(), new ToStringRenderer());
		setRequired(isRequired);
	}

	/**
	 * Creeert een DropDownChoice combobox. Deze constructor is bedoeld voor 'lazy' models
	 * zoals CompoundPropertyModels.
	 * 
	 * @param id
	 *            het component id
	 */
	public OrganisatieCombobox(String id)
	{
		super(id, new OrganisatieModel(), new ToStringRenderer());
	}

	public OrganisatieCombobox(String id, boolean isRequired)
	{
		super(id, new OrganisatieModel(), new ToStringRenderer());
		setRequired(isRequired);
	}

	private static class OrganisatieModel extends
			LoadableDetachableModel<List< ? extends Organisatie>>
	{
		private static final long serialVersionUID = 1L;

		@Override
		protected List< ? extends Organisatie> load()
		{
			return DataAccessRegistry.getHelper(OrganisatieDataAccessHelper.class)
				.getInlogDomeinen();
		}
	}
}
