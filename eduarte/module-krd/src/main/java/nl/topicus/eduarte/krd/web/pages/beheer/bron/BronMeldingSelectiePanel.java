package nl.topicus.eduarte.krd.web.pages.beheer.bron;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import nl.topicus.cobra.dataproviders.CollectionDataProvider;
import nl.topicus.cobra.web.components.IdBasedModelSelection;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanel;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanelContentDescription;
import nl.topicus.cobra.web.components.datapanel.columns.ClickableCustomPropertyColumn;
import nl.topicus.eduarte.krd.entities.bron.meldingen.aanlevering.IBronMelding;
import nl.topicus.eduarte.krd.web.components.panels.datapanel.table.BronMeldingTable;
import nl.topicus.eduarte.krd.web.components.panels.filter.BronMeldingDeelnemerZoekFilterPanel;
import nl.topicus.eduarte.krd.zoekfilters.BronMeldingZoekFilter;
import nl.topicus.eduarte.web.components.panels.datapanel.selectie.EduArteSelectiePanel;
import nl.topicus.eduarte.web.pages.deelnemer.deelnemerkaart.DeelnemerkaartPage;

import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.IModel;

public class BronMeldingSelectiePanel extends
		EduArteSelectiePanel<Serializable, IBronMelding, BronMeldingZoekFilter>
{
	private static final long serialVersionUID = 1L;

	private IModel< ? extends List<IBronMelding>> meldingenModel;

	public BronMeldingSelectiePanel(String id, IdBasedModelSelection<IBronMelding> selection,
			IModel< ? extends List<IBronMelding>> meldingenModel)
	{
		super(id, new BronMeldingZoekFilter(), selection);
		this.meldingenModel = meldingenModel;
	}

	@Override
	protected CustomDataPanelContentDescription<IBronMelding> createContentDescription()
	{
		BronMeldingTable table = new BronMeldingTable();
		table.addColumn(new ClickableCustomPropertyColumn<IBronMelding>("Deelnemer", "Deelnemer",
			"deelnemer.deelnemernummer")
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onClick(IModel<IBronMelding> model)
			{
				IBronMelding melding = model.getObject();
				setResponsePage(new DeelnemerkaartPage(melding.getDeelnemer()));
			}
		});
		return table;
	}

	@Override
	protected IDataProvider<IBronMelding> createDataProvider(BronMeldingZoekFilter filter)
	{
		final BronMeldingZoekFilter meldingFilter = filter;
		return new CollectionDataProvider<IBronMelding>(meldingenModel)
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected Collection<IBronMelding> getCollection()
			{
				if (meldingFilter.getDeelnemerNummer() == null)
					return super.getCollection();
				List<IBronMelding> ret = new ArrayList<IBronMelding>();
				for (Object object : super.getCollection())
				{
					if (object instanceof IBronMelding)
					{
						IBronMelding melding = (IBronMelding) object;
						if (melding.getDeelnemer().getDeelnemernummer() == meldingFilter
							.getDeelnemerNummer())
							ret.add(melding);
					}
				}
				return ret;
			}
		};
	}

	@Override
	protected Panel createZoekFilterPanel(String id, BronMeldingZoekFilter filter,
			CustomDataPanel<IBronMelding> customDataPanel)
	{
		return new BronMeldingDeelnemerZoekFilterPanel(id, filter, customDataPanel);
	}

	@Override
	public List<Serializable> getSelectedElements()
	{
		throw new UnsupportedOperationException();
	}

	@Override
	protected void onDetach()
	{
		super.onDetach();
		meldingenModel.detach();
	}

	@Override
	protected String getEntityName()
	{
		return "meldingen";
	}
}
