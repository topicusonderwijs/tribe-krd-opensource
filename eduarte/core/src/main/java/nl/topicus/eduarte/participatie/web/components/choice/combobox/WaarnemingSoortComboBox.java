/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.participatie.web.components.choice.combobox;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.modelsv2.ModelFactory;
import nl.topicus.cobra.util.ComponentUtil;
import nl.topicus.cobra.web.components.choice.AbstractAjaxDropDownChoice;
import nl.topicus.eduarte.dao.participatie.helpers.AbsentieRedenDataAccessHelper;
import nl.topicus.eduarte.entities.organisatie.OrganisatieEenheid;
import nl.topicus.eduarte.entities.participatie.AbsentieReden;
import nl.topicus.eduarte.entities.participatie.enums.WaarnemingSoort;
import nl.topicus.eduarte.participatie.zoekfilters.AbsentieRedenZoekFilter;
import nl.topicus.eduarte.zoekfilters.OrganisatieEenheidLocatieAuthorizationContext;

import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;

/**
 * @author vandekamp
 */
public class WaarnemingSoortComboBox extends AbstractAjaxDropDownChoice<Serializable>
{
	private static final long serialVersionUID = 1L;

	public WaarnemingSoortComboBox(String id, IModel<Serializable> model, boolean emptyChoice,
			OrganisatieEenheid organisatieEenheid, boolean metRedenen)
	{
		super(id, model, (IModel<List<Serializable>>) null, new WaarnemingSoortRenderer());
		setChoices(new WaarnemingSoortModel(emptyChoice, organisatieEenheid, metRedenen));
	}

	private class WaarnemingSoortModel extends LoadableDetachableModel<List<Serializable>>
	{
		private static final long serialVersionUID = 1L;

		private IModel<OrganisatieEenheid> organisatieEenheidModel;

		private boolean emptyChoice;

		private boolean metRedenen;

		public WaarnemingSoortModel(boolean emptyChoice, OrganisatieEenheid organisatieEenheid,
				boolean metRedenen)
		{
			this.organisatieEenheidModel = ModelFactory.getModel(organisatieEenheid);
			this.emptyChoice = emptyChoice;
			this.metRedenen = metRedenen;
		}

		@Override
		protected List<Serializable> load()
		{
			OrganisatieEenheid organisatieEenheid = organisatieEenheidModel.getObject();
			AbsentieRedenZoekFilter filter = new AbsentieRedenZoekFilter();
			filter.setAuthorizationContext(new OrganisatieEenheidLocatieAuthorizationContext(
				WaarnemingSoortComboBox.this));
			filter.setOrganisatieEenheid(organisatieEenheid);
			List<AbsentieReden> absentieRedenList =
				DataAccessRegistry.getHelper(AbsentieRedenDataAccessHelper.class)
					.getTonenBijWaarnemingen(filter);

			List<Serializable> waarnemingSoorten = new ArrayList<Serializable>();
			if (emptyChoice)
			{
				waarnemingSoorten.add("");
				waarnemingSoorten.add(WaarnemingSoort.Aanwezig);
			}
			else
			{
				waarnemingSoorten.add(WaarnemingSoort.Aanwezig);
				waarnemingSoorten.add(WaarnemingSoort.Afwezig);
				waarnemingSoorten.add(WaarnemingSoort.Nvt);

				if (metRedenen)
				{
					for (AbsentieReden absentieReden : absentieRedenList)
					{
						waarnemingSoorten.add(absentieReden);
					}
				}
			}
			return waarnemingSoorten;
		}

		@Override
		protected void onDetach()
		{
			super.onDetach();
			ComponentUtil.detachQuietly(organisatieEenheidModel);
		}
	}

	private static class WaarnemingSoortRenderer implements IChoiceRenderer<Object>
	{
		private static final long serialVersionUID = 1L;

		@Override
		public Object getDisplayValue(Object object)
		{
			if (object instanceof AbsentieReden)
			{
				AbsentieReden absentieReden = (AbsentieReden) object;
				return absentieReden.getAfkortingOmschrijving();
			}
			else if (object instanceof WaarnemingSoort)
			{
				WaarnemingSoort waarnemingSoort = (WaarnemingSoort) object;
				return waarnemingSoort.toString();
			}
			else if (object instanceof String)
			{
				return "";
			}
			else
				return null;
		}

		@Override
		public String getIdValue(Object object, int index)
		{
			if (object instanceof AbsentieReden)
			{
				return ((AbsentieReden) object).getId().toString();
			}
			else if (object instanceof WaarnemingSoort)
			{
				return ((WaarnemingSoort) object).toString();
			}
			else if (object instanceof String)
			{
				return "geen";
			}
			else
				return null;
		}
	}
}
