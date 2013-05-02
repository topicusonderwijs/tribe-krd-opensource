package nl.topicus.eduarte.krd.web.pages.beheer.bron;

import java.util.ArrayList;
import java.util.List;

import nl.topicus.cobra.dataproviders.CollectionDataProvider;
import nl.topicus.cobra.entities.TransientIdObject;
import nl.topicus.cobra.modelsv2.DefaultModelManager;
import nl.topicus.cobra.modelsv2.ModelFactory;
import nl.topicus.cobra.modelsv2.ModelManager;
import nl.topicus.cobra.util.ComponentUtil;
import nl.topicus.eduarte.entities.inschrijving.Bekostigingsperiode;
import nl.topicus.eduarte.entities.inschrijving.Plaatsing;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis.Bekostigd;
import nl.topicus.eduarte.web.components.panels.EduArteDataPanel;
import nl.topicus.eduarte.web.components.panels.datapanel.table.BekostigingsperiodeSelecteerTable;

import org.apache.wicket.markup.html.form.RadioGroup;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;

public class BronBekostigingsperiodeBOSelecterenPanel extends Panel
{
	private static final long serialVersionUID = 1L;

	private RadioGroup<Bekostigingsperiode> geselecteerdeBekostiginsperiode;

	public BronBekostigingsperiodeBOSelecterenPanel(String id,
			IModel<Verbintenis> verbintenisModel,
			IModel<Bekostigingsperiode> bekostigingsperiodeModel)
	{
		super(id);

		final ModelManager manager = new DefaultModelManager(Bekostigingsperiode.class);

		CollectionDataProvider<Bekostigingsperiode> provider =
			new CollectionDataProvider<Bekostigingsperiode>(new BekostigingsperiodeListModel(
				verbintenisModel))
			{
				private static final long serialVersionUID = 1L;

				@Override
				public IModel<Bekostigingsperiode> model(Bekostigingsperiode object)
				{
					if (((TransientIdObject) object).isSaved())
						return ModelFactory.getCompoundModel(object);
					return manager.getModel(object, null);
				}
			};

		geselecteerdeBekostiginsperiode =
			new RadioGroup<Bekostigingsperiode>("geselecteerdePeriode", bekostigingsperiodeModel);

		EduArteDataPanel<Bekostigingsperiode> datapanel =
			new EduArteDataPanel<Bekostigingsperiode>("datapanel", provider,
				new BekostigingsperiodeSelecteerTable());
		datapanel.setButtonsVisible(false);
		datapanel.setTitleVisible(false);

		geselecteerdeBekostiginsperiode.add(datapanel);

		add(geselecteerdeBekostiginsperiode);
	}

	public RadioGroup<Bekostigingsperiode> getRadioGroupSelecteren()
	{
		return geselecteerdeBekostiginsperiode;
	}

	private class BekostigingsperiodeListModel extends
			LoadableDetachableModel<List<Bekostigingsperiode>>
	{
		private static final long serialVersionUID = 1L;

		private IModel<Verbintenis> verbintenisModel;

		public BekostigingsperiodeListModel(IModel<Verbintenis> verbintenisModel)
		{
			this.verbintenisModel = verbintenisModel;
		}

		@Override
		protected List<Bekostigingsperiode> load()
		{
			return getList();
		}

		protected List<Bekostigingsperiode> getList()
		{
			List<Bekostigingsperiode> lijst = new ArrayList<Bekostigingsperiode>();

			if (verbintenisModel != null)
			{
				Verbintenis verbintenis = getVerbintenis();

				if (verbintenis != null)
				{
					if (!verbintenis.getBekostigd().equals(Bekostigd.Gedeeltelijk))
					{
						Bekostigingsperiode periode = new Bekostigingsperiode();
						periode.setBekostigd(verbintenis.getBekostigd().equals(Bekostigd.Ja));
						periode.setBegindatum(verbintenis.getBegindatum());
						periode.setVerbintenis(verbintenis);
						lijst.add(periode);
					}
					else
					{
						for (Bekostigingsperiode periode : verbintenis.getBekostigingsperiodes())
						{
							lijst.add(periode);
						}
					}
				}
			}

			return lijst;
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

		@Override
		public void detach()
		{
			ComponentUtil.detachQuietly(verbintenisModel);
		}
	}
}