package nl.topicus.eduarte.web.components.panels.datapanel.table;

import java.util.List;

import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanelContentDescription;
import nl.topicus.eduarte.dao.helpers.VrijVeldDataAccessHelper;
import nl.topicus.eduarte.entities.vrijevelden.VrijVeld;
import nl.topicus.eduarte.entities.vrijevelden.VrijVeldCategorie;
import nl.topicus.eduarte.web.components.panels.datapanel.columns.VrijVeldColumn;
import nl.topicus.eduarte.zoekfilters.VrijVeldZoekFilter;

public class AbstractVrijVeldableTable<T> extends CustomDataPanelContentDescription<T>
{
	private static final long serialVersionUID = 1L;

	public AbstractVrijVeldableTable(String title)
	{
		super(title);
	}

	protected void createVrijVeldKolommen(VrijVeldCategorie categorie, String pathToVrijVeldable)
	{
		VrijVeldZoekFilter filter = new VrijVeldZoekFilter();
		filter.setCategorie(categorie);
		List<VrijVeld> vrijeVelden =
			DataAccessRegistry.getHelper(VrijVeldDataAccessHelper.class).list(filter);
		for (VrijVeld curVrijVeld : vrijeVelden)
		{
			createVrijVeldKolom(curVrijVeld, pathToVrijVeldable);
		}
	}

	protected void createVrijVeldKolom(VrijVeld vrijVeld, String pathToVrijVeldable)
	{
		addColumn(new VrijVeldColumn<T>(vrijVeld, pathToVrijVeldable).setDefaultVisible(false));
	}
}
