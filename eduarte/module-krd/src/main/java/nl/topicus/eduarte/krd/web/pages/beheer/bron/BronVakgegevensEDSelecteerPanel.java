package nl.topicus.eduarte.krd.web.pages.beheer.bron;

import java.util.ArrayList;
import java.util.List;

import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.dataproviders.CollectionDataProvider;
import nl.topicus.cobra.util.ComponentUtil;
import nl.topicus.eduarte.dao.helpers.OnderwijsproductAfnameContextDataAccessHelper;
import nl.topicus.eduarte.entities.inschrijving.Plaatsing;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.entities.onderwijsproduct.OnderwijsproductAfnameContext;
import nl.topicus.eduarte.web.components.panels.EduArteDataPanel;
import nl.topicus.eduarte.web.components.panels.datapanel.table.OnderwijsproductAfnameContextSelectTable;
import nl.topicus.eduarte.zoekfilters.OnderwijsproductAfnameContextZoekFilter;

import org.apache.wicket.markup.html.form.RadioGroup;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;

public class BronVakgegevensEDSelecteerPanel extends Panel
{
	private static final long serialVersionUID = 1L;

	private RadioGroup<OnderwijsproductAfnameContext> geselecteerdeOnderwijsproduct;

	public BronVakgegevensEDSelecteerPanel(String id, IModel<Verbintenis> verbintenisModel,
			IModel<OnderwijsproductAfnameContext> onderwijsproductAfnameModel)
	{
		super(id);

		OnderwijsproductAfnameContextZoekFilter filter =
			new OnderwijsproductAfnameContextZoekFilter();
		filter.setVerbintenis(verbintenisModel);

		CollectionDataProvider<OnderwijsproductAfnameContext> provider =
			new CollectionDataProvider<OnderwijsproductAfnameContext>(new VerbintenisModel(
				verbintenisModel));

		geselecteerdeOnderwijsproduct =
			new RadioGroup<OnderwijsproductAfnameContext>("geselecteerdeOnderwijsproduct",
				onderwijsproductAfnameModel);

		EduArteDataPanel<OnderwijsproductAfnameContext> datapanel =
			new EduArteDataPanel<OnderwijsproductAfnameContext>("datapanel", provider,
				new OnderwijsproductAfnameContextSelectTable());
		datapanel.setButtonsVisible(false);
		datapanel.setTitleVisible(false);

		geselecteerdeOnderwijsproduct.add(datapanel);

		add(geselecteerdeOnderwijsproduct);
	}

	public RadioGroup<OnderwijsproductAfnameContext> getRadioGroupSelecteren()
	{
		return geselecteerdeOnderwijsproduct;
	}

	private class VerbintenisModel extends
			LoadableDetachableModel<List<OnderwijsproductAfnameContext>>
	{
		private static final long serialVersionUID = 1L;

		private IModel<Verbintenis> verbintenisModel;

		public VerbintenisModel(IModel<Verbintenis> verbintenisModel)
		{
			this.verbintenisModel = verbintenisModel;
		}

		@Override
		protected List<OnderwijsproductAfnameContext> load()
		{
			if (verbintenisModel.getObject() != null)
				return DataAccessRegistry.getHelper(
					OnderwijsproductAfnameContextDataAccessHelper.class).listContexten(
					getVerbintenis());
			else
				return new ArrayList<OnderwijsproductAfnameContext>();
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