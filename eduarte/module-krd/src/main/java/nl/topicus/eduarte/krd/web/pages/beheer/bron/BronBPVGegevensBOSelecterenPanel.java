package nl.topicus.eduarte.krd.web.pages.beheer.bron;

import java.util.ArrayList;
import java.util.List;

import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.dataproviders.CollectionDataProvider;
import nl.topicus.cobra.util.ComponentUtil;
import nl.topicus.eduarte.dao.helpers.BPVInschrijvingDataAccessHelper;
import nl.topicus.eduarte.entities.bpv.BPVInschrijving;
import nl.topicus.eduarte.entities.inschrijving.Plaatsing;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.web.components.panels.EduArteDataPanel;
import nl.topicus.eduarte.web.components.panels.datapanel.table.BPVInschrijvingSelecteerTable;
import nl.topicus.eduarte.zoekfilters.BPVInschrijvingZoekFilter;

import org.apache.wicket.markup.html.form.RadioGroup;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;

public class BronBPVGegevensBOSelecterenPanel extends Panel
{
	private static final long serialVersionUID = 1L;

	private RadioGroup<BPVInschrijving> geselecteerdeBpvInschrijving;

	public BronBPVGegevensBOSelecterenPanel(String id, IModel<Verbintenis> verbintenisModel,
			IModel<BPVInschrijving> bpvModel)
	{
		super(id);

		CollectionDataProvider<BPVInschrijving> provider =
			new CollectionDataProvider<BPVInschrijving>(new BPVInschrijvingModel(verbintenisModel));

		geselecteerdeBpvInschrijving =
			new RadioGroup<BPVInschrijving>("geselecteerdeBpvInschrijving", bpvModel);

		EduArteDataPanel<BPVInschrijving> datapanel =
			new EduArteDataPanel<BPVInschrijving>("datapanel", provider,
				new BPVInschrijvingSelecteerTable());
		datapanel.setButtonsVisible(false);
		datapanel.setTitleVisible(false);

		geselecteerdeBpvInschrijving.add(datapanel);

		add(geselecteerdeBpvInschrijving);
	}

	public RadioGroup<BPVInschrijving> getRadioGroupSelecteren()
	{
		return geselecteerdeBpvInschrijving;
	}

	private class BPVInschrijvingModel extends LoadableDetachableModel<List<BPVInschrijving>>
	{
		private static final long serialVersionUID = 1L;

		private IModel<Verbintenis> verbintenisModel;

		public BPVInschrijvingModel(IModel<Verbintenis> verbintenisModel)
		{
			this.verbintenisModel = verbintenisModel;
		}

		@Override
		protected List<BPVInschrijving> load()
		{
			BPVInschrijvingZoekFilter filter = new BPVInschrijvingZoekFilter();
			filter.setPeildatum(null);
			filter.setVerbintenis(getVerbintenis());

			if (verbintenisModel.getObject() != null)
				return DataAccessRegistry.getHelper(BPVInschrijvingDataAccessHelper.class).list(
					filter);
			else
				return new ArrayList<BPVInschrijving>();
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
		protected void onDetach()
		{
			ComponentUtil.detachQuietly(verbintenisModel);
			super.onDetach();
		}
	}
}
