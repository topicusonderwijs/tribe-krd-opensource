package nl.topicus.eduarte.krd.web.pages.beheer.bron;

import java.util.ArrayList;
import java.util.List;

import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.eduarte.dao.helpers.ResultaatDataAccessHelper;
import nl.topicus.eduarte.dao.helpers.ToetsDataAccessHelper;
import nl.topicus.eduarte.entities.inschrijving.OnderwijsproductAfname;
import nl.topicus.eduarte.entities.inschrijving.Plaatsing;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.entities.onderwijsproduct.Onderwijsproduct;
import nl.topicus.eduarte.entities.resultaatstructuur.Resultaat;
import nl.topicus.eduarte.entities.resultaatstructuur.Toets;
import nl.topicus.eduarte.entities.resultaatstructuur.Toets.SoortToets;

import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.markup.html.form.RadioChoice;
import org.apache.wicket.markup.html.form.RadioGroup;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;

public class BronNT2ResultaatEDSelecteerPanel extends Panel
{
	private static final long serialVersionUID = 1L;

	private RadioGroup<Resultaat> geselecteerdeNT2Context;

	public BronNT2ResultaatEDSelecteerPanel(String id, IModel<Verbintenis> verbintenisModel,
			IModel<Resultaat> nt2ResultaatModel)
	{
		super(id);

		RadioChoice<Resultaat> choice =
			new RadioChoice<Resultaat>("toetsen", nt2ResultaatModel, new ResultaatListModel(
				verbintenisModel), new IChoiceRenderer<Resultaat>()
			{
				private static final long serialVersionUID = 1L;

				@Override
				public Object getDisplayValue(Resultaat object)
				{
					return object.getToets().getParent().getSoort().toString();
				}

				@Override
				public String getIdValue(Resultaat object, int index)
				{
					return object.getId().toString();
				}

			});
		add(choice);

	}

	public RadioGroup<Resultaat> getRadioGroupSelecteren()
	{
		return geselecteerdeNT2Context;
	}

	private class ResultaatListModel extends LoadableDetachableModel<List<Resultaat>>
	{
		private static final long serialVersionUID = 1L;

		private IModel<Verbintenis> verbintenisModel;

		public ResultaatListModel(IModel<Verbintenis> verbintenisModel)
		{
			this.verbintenisModel = verbintenisModel;
		}

		@Override
		protected List<Resultaat> load()
		{
			List<Resultaat> beschikbareResultaat = new ArrayList<Resultaat>();

			for (SoortToets toetsSoort : SoortToets.values())
			{
				if (toetsSoort.isNT2VaardigheidToets())
				{
					Resultaat resultaat = getResultaatVoor(toetsSoort);
					if (resultaat != null)
						beschikbareResultaat.add(resultaat);
				}
			}

			return beschikbareResultaat;
		}

		private Resultaat getResultaatVoor(SoortToets toetsSoort)
		{
			if (getVerbintenis() != null)
			{
				ToetsDataAccessHelper toetsHelper =
					DataAccessRegistry.getHelper(ToetsDataAccessHelper.class);

				// Let op: gaat er van uit dat deelnemer slechts 1 vak volgt dat NT2
				// resultaten heeft
				for (OnderwijsproductAfname afname : getVerbintenis().getOnderwijsproductAfnames())
				{
					Onderwijsproduct onderwijsproduct = afname.getOnderwijsproduct();

					Toets toets =
						toetsHelper.getToets(onderwijsproduct, getVerbintenis().getCohort(),
							toetsSoort);

					if (toets != null && getStartniveaToets(toets) != null)
					{
						ResultaatDataAccessHelper resultaatHelper =
							DataAccessRegistry.getHelper(ResultaatDataAccessHelper.class);

						Resultaat resultaat =
							resultaatHelper.getGeldendeResultaat(getStartniveaToets(toets),
								getVerbintenis().getDeelnemer());
						if (resultaat != null)
							return resultaat;
					}

				}

			}
			return null;
		}

		private Toets getStartniveaToets(Toets toets)
		{
			for (Toets childToets : toets.getChildren())
			{
				if (childToets.getSoort().equals(SoortToets.Instroomniveau))
					return childToets;
			}
			return null;
		}

		private Verbintenis getVerbintenis()
		{
			Object obj = verbintenisModel.getObject();

			if (obj instanceof Verbintenis)
				return (Verbintenis) obj;
			else if (obj instanceof Plaatsing)
				return ((Plaatsing) obj).getVerbintenis();

			return null;
		}
	}
}
