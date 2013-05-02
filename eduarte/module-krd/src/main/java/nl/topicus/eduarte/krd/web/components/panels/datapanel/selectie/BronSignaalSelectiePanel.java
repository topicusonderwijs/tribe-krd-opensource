package nl.topicus.eduarte.krd.web.components.panels.datapanel.selectie;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import nl.topicus.cobra.dataproviders.CollectionDataProvider;
import nl.topicus.cobra.web.components.Selection;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanel;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanelContentDescription;
import nl.topicus.cobra.web.components.datapanel.CustomColumn.Positioning;
import nl.topicus.cobra.web.components.datapanel.columns.AjaxButtonColumn;
import nl.topicus.cobra.web.components.datapanel.columns.ButtonColumn;
import nl.topicus.cobra.web.components.datapanel.selection.CheckboxSelectionColumn;
import nl.topicus.eduarte.krd.entities.bron.meldingen.terugkoppeling.IBronSignaal;
import nl.topicus.eduarte.krd.web.components.modalwindow.bron.BronSignaalOpmerkingModalWindow;
import nl.topicus.eduarte.krd.web.components.panels.datapanel.table.BronSignalenTable;
import nl.topicus.eduarte.krd.web.components.panels.filter.BronSignaalZoekFilterPanel;
import nl.topicus.eduarte.krd.web.pages.beheer.bron.overzichten.BronMeldingDetailsPage;
import nl.topicus.eduarte.krd.zoekfilters.BronSignaalZoekFilter;
import nl.topicus.eduarte.web.components.panels.datapanel.selectie.EduArteSelectiePanel;
import nl.topicus.eduarte.web.pages.SecurePage;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.IModel;

public class BronSignaalSelectiePanel extends
		EduArteSelectiePanel<Serializable, IBronSignaal, BronSignaalZoekFilter>
{
	private static final long serialVersionUID = 1L;

	private BronSignaalOpmerkingModalWindow window;

	public BronSignaalSelectiePanel(String id, BronSignaalZoekFilter filter,
			Selection<Serializable, IBronSignaal> selection)
	{
		super(id, filter, selection);
		add(window = new BronSignaalOpmerkingModalWindow("window"));
	}

	@Override
	protected CustomDataPanelContentDescription<IBronSignaal> createContentDescription()
	{
		BronSignalenTable table = new BronSignalenTable();
		table.addColumn(new ButtonColumn<IBronSignaal>("details", "Details", "actionItem",
			"actionItem_grey")
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick(WebMarkupContainer item, IModel<IBronSignaal> rowModel)
			{
				IBronSignaal signaal = rowModel.getObject();
				if (signaal.getAanleverMelding() != null)
					setResponsePage(new BronMeldingDetailsPage(signaal.getAanleverMelding(),
						(SecurePage) getPage()));
			}
		});
		table.addColumn(new AjaxButtonColumn<IBronSignaal>("Opmerking", "Opm")
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected String getCssDisabled()
			{
				return "comment";
			}

			@Override
			protected String getCssEnabled()
			{
				return "comment";
			}

			@Override
			public void onClick(WebMarkupContainer item, IModel<IBronSignaal> rowModel,
					AjaxRequestTarget target)
			{
				window.setSignaal(rowModel.getObject());
				window.show(target);
			}

		}.setPositioning(Positioning.FIXED_LEFT));
		return table;
	}

	@Override
	protected CheckboxSelectionColumn<Serializable, IBronSignaal> createSelectionColumn()
	{
		return new CheckboxSelectionColumn<Serializable, IBronSignaal>("Accoord", "Acc",
			getSelection());
	}

	@Override
	protected Panel createZoekFilterPanel(String id, BronSignaalZoekFilter filter,
			CustomDataPanel<IBronSignaal> customDataPanel)
	{
		return new BronSignaalZoekFilterPanel(id, filter, customDataPanel);
	}

	@Override
	protected IDataProvider<IBronSignaal> createDataProvider(final BronSignaalZoekFilter filter)
	{
		return new CollectionDataProvider<IBronSignaal>(new SignalenListModel(filter));
	}

	@Override
	public List<Serializable> getSelectedElements()
	{
		List<Serializable> ret = new ArrayList<Serializable>();
		for (IBronSignaal curSignaal : SignalenListModel.getSignalen(getFilter()))
		{
			if (getSelection().isSelected(curSignaal))
				ret.add(curSignaal);
		}
		return ret;
	}

	@Override
	protected String getEntityName()
	{
		return "signalen";
	}
}
