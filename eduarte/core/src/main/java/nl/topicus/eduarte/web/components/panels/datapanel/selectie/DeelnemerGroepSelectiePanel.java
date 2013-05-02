package nl.topicus.eduarte.web.components.panels.datapanel.selectie;

import nl.topicus.cobra.dao.helpers.DatabaseSelection;
import nl.topicus.cobra.dao.hibernate.HibernateSelection;
import nl.topicus.cobra.entities.IdObject;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanel;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanelContentDescription;
import nl.topicus.cobra.web.components.datapanel.selection.CheckboxSelectionColumn;
import nl.topicus.eduarte.dao.helpers.VerbintenisDataAccessHelper;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.web.components.panels.EduArteDataPanel;
import nl.topicus.eduarte.web.components.panels.datapanel.table.DeelnemerTable;
import nl.topicus.eduarte.zoekfilters.VerbintenisZoekFilter;

import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.data.IDataProvider;

public class DeelnemerGroepSelectiePanel<R extends IdObject> extends
		EduArteDatabaseSelectiePanel<R, Verbintenis, VerbintenisZoekFilter>
{
	private static final long serialVersionUID = 1L;

	public DeelnemerGroepSelectiePanel(String id, VerbintenisZoekFilter filter,
			DatabaseSelection<R, Verbintenis> selection)
	{
		super(id, filter, VerbintenisDataAccessHelper.class, selection);
	}

	@Override
	protected CheckboxSelectionColumn<R, Verbintenis> createSelectionColumn()
	{
		return new CheckboxSelectionColumn<R, Verbintenis>("selection", "", getSelection())
		{
			private static final long serialVersionUID = 1L;

			@Override
			@SuppressWarnings("unchecked")
			public boolean isRepeatWhenEqualToPrevRow()
			{
				// HibernateSelection heeft R == S, dus R is Verbintenis, dus overal
				// vinkje tonen, anders alleen bij deelnemers
				// Het lukt niet om hier <?> te gebruiken omdat de Sun compiler dat niet
				// leuk vindt (bug?)
				return getSelection() instanceof HibernateSelection;
			}
		};
	}

	@Override
	protected EduArteDataPanel<Verbintenis> createDataPanel(String id,
			IDataProvider<Verbintenis> provider,
			CustomDataPanelContentDescription<Verbintenis> contents)
	{
		return new EduArteDataPanel<Verbintenis>("datapanel", provider, contents)
		{
			private static final long serialVersionUID = 1L;

			/**
			 * Geef de deelnemer van de verbintenis terug ipv de verbintenis. Dit zorgt
			 * ervoor dat een deelnemer niet herhaald wordt op de volgende regel als het
			 * om dezelfde deelnemer gaat.
			 * 
			 * @see nl.topicus.cobra.web.components.datapanel.CustomDataPanel#getRowValue(Object
			 *      rowValue)
			 */
			@Override
			protected Object getRowValue(Verbintenis verbintenis)
			{
				if (verbintenis != null)
				{
					return verbintenis.getDeelnemer();
				}
				return null;
			}

			@Override
			public boolean isVisible()
			{
				return !getPage().hasErrorMessage();
			}
		};
	}

	@Override
	protected CustomDataPanelContentDescription<Verbintenis> createContentDescription()
	{
		return new DeelnemerTable();
	}

	@Override
	protected Panel createZoekFilterPanel(String id, VerbintenisZoekFilter filter,
			CustomDataPanel<Verbintenis> customDataPanel)
	{
		return new DeelnemerGroepZoekFilterPanel(id, filter, customDataPanel);
	}

	@Override
	protected String getEntityName()
	{
		return "deelnemers";
	}
}
