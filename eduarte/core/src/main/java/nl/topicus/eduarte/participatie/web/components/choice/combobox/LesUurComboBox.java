/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.participatie.web.components.choice.combobox;

import java.util.ArrayList;
import java.util.List;

import nl.topicus.cobra.modelsv2.ModelFactory;
import nl.topicus.cobra.util.ComponentUtil;
import nl.topicus.eduarte.entities.organisatie.Locatie;
import nl.topicus.eduarte.entities.organisatie.OrganisatieEenheid;
import nl.topicus.eduarte.entities.participatie.LesuurIndeling;
import nl.topicus.eduarte.entities.participatie.LesweekIndeling;
import nl.topicus.eduarte.participatie.util.LesuurIndelingUtil;

import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;

/**
 * @author vandekamp
 */
public class LesUurComboBox extends DropDownChoice<LesuurIndeling>
{
	private static final long serialVersionUID = 1L;

	public LesUurComboBox(String id, IModel<LesuurIndeling> model,
			IModel<LesweekIndeling> lesweekIndelingModel)
	{
		super(id, model, new LesurenModel(lesweekIndelingModel), new LesurenRenderer());
	}

	/**
	 * Geeft een combobox met de gedefinieerde lesuren voor deze organisatieEenheid.
	 * 
	 */
	public LesUurComboBox(String id, IModel<LesuurIndeling> model,
			OrganisatieEenheid organisatieEenheid, Locatie locatie)
	{
		super(id, model, new LesurenModel(organisatieEenheid, locatie), new LesurenRenderer());
	}

	private static class LesurenModel extends LoadableDetachableModel<List<LesuurIndeling>>
	{
		private static final long serialVersionUID = 1L;

		private IModel<OrganisatieEenheid> organistieEenheidModel;

		private IModel<Locatie> locatieModel;

		private IModel<LesweekIndeling> lesweekIndelingModel = null;

		public LesurenModel(IModel<LesweekIndeling> lesweekIndelingModel)
		{
			this.lesweekIndelingModel = lesweekIndelingModel;
			getObject();
		}

		public LesurenModel(OrganisatieEenheid organisatieEenheid, Locatie locatie)
		{
			this.organistieEenheidModel = ModelFactory.getModel(organisatieEenheid);
			this.locatieModel = ModelFactory.getModel(locatie);
			getObject();
		}

		@Override
		protected List<LesuurIndeling> load()
		{
			if (lesweekIndelingModel != null)
			{
				LesweekIndeling indeling = lesweekIndelingModel.getObject();
				if (indeling != null)
					return indeling.getIndelingMetMeesteLestijden().getLesuurIndeling();
				return new ArrayList<LesuurIndeling>();
			}
			else
				return LesuurIndelingUtil.getLesuurIndelingen(locatieModel.getObject(),
					organistieEenheidModel.getObject());
		}

		@Override
		protected void onDetach()
		{
			super.onDetach();
			ComponentUtil.detachQuietly(organistieEenheidModel);
			ComponentUtil.detachQuietly(locatieModel);
			ComponentUtil.detachQuietly(lesweekIndelingModel);
		}

	}

	private static class LesurenRenderer implements IChoiceRenderer<LesuurIndeling>
	{
		private static final long serialVersionUID = 1L;

		@Override
		public Object getDisplayValue(LesuurIndeling object)
		{
			return object.getLesuur();
		}

		@Override
		public String getIdValue(LesuurIndeling object, int index)
		{
			return object.getId().toString();
		}

	}
}
