package nl.topicus.eduarte.web.components.panels.datapanel.table;

import nl.topicus.cobra.util.TimeUtil;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanelContentDescription;
import nl.topicus.cobra.web.components.datapanel.CustomColumn.Positioning;
import nl.topicus.cobra.web.components.datapanel.columns.AjaxButtonColumn;
import nl.topicus.cobra.web.components.datapanel.columns.CustomPropertyColumn;
import nl.topicus.eduarte.entities.signalering.Signaal;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.model.IModel;

public abstract class SignaalTable extends CustomDataPanelContentDescription<Signaal>
{
	private static final long serialVersionUID = 1L;

	public SignaalTable()
	{
		super("Signalen");
		addColumn(new CustomPropertyColumn<Signaal>("Datum aangemaakt", "Datum", "createdAt",
			"createdAt"));
		addColumn(new CustomPropertyColumn<Signaal>("Omschrijving", "Omschrijving",
			"event.onderwerp"));

		addColumn(new CustomPropertyColumn<Signaal>("Deelnemernummer", "Deelnemernummer",
			"event.deelnemer.deelnemernummer").setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<Signaal>("Deelnemer", "Deelnemer",
			"event.deelnemer.persoon.volledigeNaam").setDefaultVisible(false));
		// addColumn(new CustomPropertyColumn("Plaatsingsgroep", "Plaatsingsgroep",
		// "event.deelnemer.plaatsingOpPeildatum.groep.naam").setDefaultVisible(false));

		addColumn(new ArchiveColumn("Archiveren", "Archiveren")
			.setPositioning(Positioning.FIXED_RIGHT));
		addColumn(new MarkAsReadColumn("Gelezen", "Gelezen")
			.setPositioning(Positioning.FIXED_RIGHT));
	}

	private class ArchiveColumn extends AjaxButtonColumn<Signaal>
	{
		private static final long serialVersionUID = 1L;

		public ArchiveColumn(String id, String header)
		{
			super(id, header);
		}

		@Override
		protected String getCssDisabled()
		{
			return "ui-icon-disk ui-icon";
		}

		@Override
		protected String getCssEnabled()
		{
			return "ui-icon-disk ui-icon";
		}

		@Override
		public String getCssClass()
		{
			return "unit_60";
		}

		@Override
		public void onClick(WebMarkupContainer item, IModel<Signaal> rowModel,
				AjaxRequestTarget target)
		{
			Signaal signaal = rowModel.getObject();
			signaal.setGearchiveerd(true);
			signaal.saveOrUpdate();
			signaal.commit();
			refreshDataPanel(target);
		}

		@Override
		public boolean isContentsVisible(IModel<Signaal> model)
		{
			Signaal signaal = model.getObject();
			return !signaal.isGearchiveerd();
		}
	}

	private class MarkAsReadColumn extends AjaxButtonColumn<Signaal>
	{
		private static final long serialVersionUID = 1L;

		public MarkAsReadColumn(String id, String header)
		{
			super(id, header);
		}

		@Override
		protected String getCssDisabled()
		{
			return "ui-icon-check ui-icon";
		}

		@Override
		protected String getCssEnabled()
		{
			return "ui-icon-check ui-icon";
		}

		@Override
		public String getCssClass()
		{
			return "unit_60";
		}

		@Override
		public void onClick(WebMarkupContainer item, IModel<Signaal> rowModel,
				AjaxRequestTarget target)
		{
			Signaal signaal = rowModel.getObject();
			signaal.setDatumGelezen(TimeUtil.getInstance().currentDate());
			signaal.saveOrUpdate();
			signaal.commit();
			refreshDataPanel(target);
		}

		@Override
		public boolean isContentsVisible(IModel<Signaal> model)
		{
			Signaal signaal = model.getObject();
			return signaal.getDatumGelezen() == null;
		}
	}

	protected abstract void refreshDataPanel(AjaxRequestTarget target);

}
