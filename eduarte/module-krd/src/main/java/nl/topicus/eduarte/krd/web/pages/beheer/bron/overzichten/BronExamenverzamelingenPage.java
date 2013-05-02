package nl.topicus.eduarte.krd.web.pages.beheer.bron.overzichten;

import java.util.List;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.dataproviders.GeneralFilteredSortableDataProvider;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.util.ComponentUtil;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanelPageLinkRowFactory;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.cobra.web.components.panels.bottomrow.ButtonAlignment;
import nl.topicus.cobra.web.components.panels.bottomrow.PageLinkButton;
import nl.topicus.eduarte.app.security.actions.Instelling;
import nl.topicus.eduarte.app.security.actions.OrganisatieEenheid;
import nl.topicus.eduarte.app.security.actions.SearchImplementsActions;
import nl.topicus.eduarte.krd.dao.helpers.BronExamenverzamenlingDataAccessHelper;
import nl.topicus.eduarte.krd.entities.bron.BronExamenverzameling;
import nl.topicus.eduarte.krd.principals.beheer.bron.BronOverzichtInzien;
import nl.topicus.eduarte.krd.web.components.panels.datapanel.table.BronExamenverzamelingTable;
import nl.topicus.eduarte.krd.web.components.panels.filter.BronExamenverzamelingZoekFilterPanel;
import nl.topicus.eduarte.krd.web.pages.beheer.bron.AbstractBronPage;
import nl.topicus.eduarte.krd.web.pages.beheer.bron.BronAlgemeenPage;
import nl.topicus.eduarte.krd.web.pages.beheer.bron.BronExamenverzamelingMakenPage;
import nl.topicus.eduarte.krd.zoekfilters.BronExamenmeldingZoekFilter;
import nl.topicus.eduarte.krd.zoekfilters.BronExamenverzamelingZoekFilter;
import nl.topicus.eduarte.web.components.panels.EduArteDataPanel;

import org.apache.wicket.Page;
import org.apache.wicket.markup.html.link.IPageLink;
import org.apache.wicket.markup.repeater.Item;

@PageInfo(title = "BRON Examenverzamelingen", menu = "Deelnemer")
@InPrincipal(BronOverzichtInzien.class)
@SearchImplementsActions( {Instelling.class, OrganisatieEenheid.class})
public class BronExamenverzamelingenPage extends AbstractBronPage
{
	private BronExamenverzamelingZoekFilter filter;

	public BronExamenverzamelingenPage(BronExamenverzamelingZoekFilter filter)
	{
		this.filter = filter;
		filter.addOrderByProperty("createdAt");
		filter.setAscending(false);
		GeneralFilteredSortableDataProvider<BronExamenverzameling, BronExamenverzamelingZoekFilter> provider =
			GeneralFilteredSortableDataProvider.of(filter,
				BronExamenverzamenlingDataAccessHelper.class);
		BronExamenverzamelingTable table = new BronExamenverzamelingTable();
		EduArteDataPanel<BronExamenverzameling> datapanel =
			new EduArteDataPanel<BronExamenverzameling>("datapanel", provider, table);
		datapanel.setRowFactory(new CustomDataPanelPageLinkRowFactory<BronExamenverzameling>(
			BronExamenMeldingenPage.class)
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick(Item<BronExamenverzameling> item)
			{
				BronExamenmeldingZoekFilter examenMeldingZF = new BronExamenmeldingZoekFilter();
				examenMeldingZF.setExamenverzameling(item.getModelObject());
				setResponsePage(new BronExamenMeldingenPage(examenMeldingZF));
			}
		});
		add(datapanel);
		add(new BronExamenverzamelingZoekFilterPanel("filter", filter, datapanel));
		createComponents();
	}

	@Override
	protected void fillBottomRow(BottomRowPanel panel)
	{
		panel.addButton(new PageLinkButton(panel, "BRON Startscherm", BronAlgemeenPage.class));
		panel.addButton(new PageLinkButton(panel, "Verzameling maken", ButtonAlignment.LEFT,
			new IPageLink()
			{
				private static final long serialVersionUID = 1L;

				@Override
				public Page getPage()
				{
					return new BronExamenverzamelingMakenPage(filter);
				}

				@Override
				public Class<BronExamenverzamelingMakenPage> getPageIdentity()
				{
					return BronExamenverzamelingMakenPage.class;
				}

			}));
		super.fillBottomRow(panel);
	}

	@Override
	public void getBookmarkConstructorArguments(List<Class< ? >> ctorArgTypes,
			List<Object> ctorArgValues)
	{
		super.getBookmarkConstructorArguments(ctorArgTypes, ctorArgValues);
		ctorArgTypes.add(BronExamenverzamelingZoekFilter.class);
		ctorArgValues.add(filter);
	}

	@Override
	protected void onDetach()
	{
		ComponentUtil.detachQuietly(filter);
		super.onDetach();
	}
}
