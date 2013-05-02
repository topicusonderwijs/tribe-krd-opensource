package nl.topicus.eduarte.krd.web.pages.beheer.bron.foto;

import java.util.List;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.dataproviders.GeneralFilteredSortableDataProvider;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.cobra.web.components.panels.bottomrow.VerwijderButton;
import nl.topicus.eduarte.krd.dao.helpers.BronFotoRecordDataAccessHelper;
import nl.topicus.eduarte.krd.dao.helpers.BronFotobestandVerschilDataAccessHelper;
import nl.topicus.eduarte.krd.entities.bron.foto.BronFotobestand;
import nl.topicus.eduarte.krd.entities.bron.foto.BronFotobestandVerschil;
import nl.topicus.eduarte.krd.entities.bron.foto.bve.BronFotoRecord;
import nl.topicus.eduarte.krd.principals.deelnemer.bron.DeelnemerBronFotoInlezen;
import nl.topicus.eduarte.krd.web.components.panels.datapanel.table.BronFotobestandVerschilTable;
import nl.topicus.eduarte.krd.web.components.panels.filter.BronFotobestandVerschilZoekFilterPanel;
import nl.topicus.eduarte.krd.web.pages.beheer.bron.AbstractBronPage;
import nl.topicus.eduarte.krd.zoekfilters.BronFotobestandVerschilZoekFilter;
import nl.topicus.eduarte.web.components.panels.EduArteDataPanel;
import nl.topicus.eduarte.zoekfilters.OrganisatieEenheidLocatieAuthorizationContext;

/**
 * Pagina die de details van een bronfoto toont, en dan vooral de verschillen die gevonden
 * zijn.
 * 
 * @author loite
 */
@PageInfo(title = "BRON Fotobestand", menu = "Deelnemer > BRON > Fotobestanden > [fotobestand]")
@InPrincipal(DeelnemerBronFotoInlezen.class)
public class BronFotobestandPage extends AbstractBronPage
{
	private static final long serialVersionUID = 1L;

	private BronFotobestandVerschilZoekFilter filter;

	public BronFotobestandPage(BronFotobestand fotobestand)
	{
		filter = new BronFotobestandVerschilZoekFilter(fotobestand);
		filter.setAuthorizationContext(new OrganisatieEenheidLocatieAuthorizationContext(this));
		GeneralFilteredSortableDataProvider<BronFotobestandVerschil, BronFotobestandVerschilZoekFilter> provider =
			GeneralFilteredSortableDataProvider.of(filter,
				BronFotobestandVerschilDataAccessHelper.class);
		EduArteDataPanel<BronFotobestandVerschil> datapanel =
			new EduArteDataPanel<BronFotobestandVerschil>("datapanel", provider,
				new BronFotobestandVerschilTable());
		add(datapanel);
		BronFotobestandVerschilZoekFilterPanel filterPanel =
			new BronFotobestandVerschilZoekFilterPanel("filter", filter, datapanel);
		add(filterPanel);
		createComponents();
	}

	@Override
	public boolean supportsBookmarks()
	{
		return false;
	}

	@Override
	protected void fillBottomRow(BottomRowPanel panel)
	{
		panel.addButton(new VerwijderButton(panel)
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onClick()
			{
				// verwijder fotobestand
				BronFotobestand fotobestand = filter.getFotobestand();
				List<BronFotobestandVerschil> verschillen =
					DataAccessRegistry.getHelper(BronFotobestandVerschilDataAccessHelper.class)
						.getVerschillenList(fotobestand);
				for (BronFotobestandVerschil verschil : verschillen)
					verschil.delete();
				List<BronFotoRecord> records =
					DataAccessRegistry.getHelper(BronFotoRecordDataAccessHelper.class)
						.getAlleBronFotoRecords(fotobestand);
				for (BronFotoRecord record : records)
				{
					record.delete();
				}
				fotobestand.delete();
				fotobestand.commit();
				setResponsePage(new BronFotobestandenPage());
			}

			@Override
			public boolean isVisible()
			{
				return true;
			}
		});
	}
}
