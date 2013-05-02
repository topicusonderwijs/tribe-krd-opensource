package nl.topicus.eduarte.web.pages.deelnemerportaal.home;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.dataproviders.GeneralFilteredSortableDataProvider;
import nl.topicus.eduarte.dao.helpers.SignaalDataAccessHelper;
import nl.topicus.eduarte.entities.personen.Persoon;
import nl.topicus.eduarte.entities.signalering.Signaal;
import nl.topicus.eduarte.web.components.panels.EduArteDataPanel;
import nl.topicus.eduarte.web.components.panels.datapanel.table.SignaalTable;
import nl.topicus.eduarte.web.components.panels.filter.SignaalZoekFilterPanel;
import nl.topicus.eduarte.web.components.panels.signalen.EventHandlerRowFactory;
import nl.topicus.eduarte.web.components.panels.signalen.OngelezenRowFactoryDecorator;
import nl.topicus.eduarte.zoekfilters.SignaalZoekFilter;
import nl.topicus.eduarte.zoekfilters.SignaalZoekFilter.SignaalStatus;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.data.IDataProvider;

/**
 * Pagina waarin de gebruiker zijn signalen ziet. oude signalen kunnen gearchiveerd worden
 * zodat ze niet meer getoond worden. Nieuwe siganlen worden vet weergegeven.
 * 
 * @author marrink
 */
@PageInfo(title = "Overzicht signalen", menu = {"Home > Signalen > Overzicht signalen"})
public class DeelnemerportaalOverzichtSignalenPanel extends Panel
{
	private static final long serialVersionUID = 1L;

	private static final SignaalZoekFilter createFilter(Persoon ontvanger)
	{
		SignaalZoekFilter signalenFilter = new SignaalZoekFilter(ontvanger);
		signalenFilter.setStatus(SignaalStatus.OpenstaandeSignalen);
		signalenFilter.addOrderByProperty("id");
		signalenFilter.addOrderByProperty("ontvanger.achternaam");
		signalenFilter.addOrderByProperty("createdAt");
		signalenFilter.setAscending(false);

		return signalenFilter;
	}

	private EduArteDataPanel<Signaal> signaalDataPanel;

	public DeelnemerportaalOverzichtSignalenPanel(String id, Persoon ontvanger, int itemsPerPage)
	{
		super(id);
		setRenderBodyOnly(true);
		final SignaalZoekFilter signaalFilter = createFilter(ontvanger);
		IDataProvider<Signaal> signaalProvider =
			GeneralFilteredSortableDataProvider.of(signaalFilter, SignaalDataAccessHelper.class);

		signaalDataPanel =
			new EduArteDataPanel<Signaal>("signaalDataPanel", signaalProvider, new SignaalTable()
			{
				private static final long serialVersionUID = 1L;

				@Override
				protected void refreshDataPanel(AjaxRequestTarget target)
				{
					target.addComponent(signaalDataPanel);
				}
			});
		signaalDataPanel.setRowFactory(new OngelezenRowFactoryDecorator(
			new EventHandlerRowFactory()));
		signaalDataPanel.setItemsPerPage(itemsPerPage);
		add(signaalDataPanel);
		add(new SignaalZoekFilterPanel("filter", signaalFilter, signaalDataPanel));
	}
}
