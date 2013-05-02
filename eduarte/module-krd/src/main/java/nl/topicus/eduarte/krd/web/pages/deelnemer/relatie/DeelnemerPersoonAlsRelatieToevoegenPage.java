/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.krd.web.pages.deelnemer.relatie;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.dataproviders.GeneralFilteredSortableDataProvider;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.types.personalia.Geslacht;
import nl.topicus.cobra.util.TimeUtil;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanel;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanelRowFactory;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.cobra.web.components.panels.bottomrow.TerugButton;
import nl.topicus.cobra.web.components.shortcut.CobraKeyAction;
import nl.topicus.cobra.web.security.RequiredSecurityCheck;
import nl.topicus.eduarte.app.security.checks.NietOverledenSecurityCheck;
import nl.topicus.eduarte.dao.helpers.NummerGeneratorDataAccessHelper;
import nl.topicus.eduarte.dao.helpers.RelatieDataAccessHelper;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.entities.personen.Deelnemer;
import nl.topicus.eduarte.entities.personen.Persoon;
import nl.topicus.eduarte.entities.personen.Relatie;
import nl.topicus.eduarte.krd.principals.deelnemer.relatie.DeelnemerRelatiePersoonWrite;
import nl.topicus.eduarte.web.components.menu.DeelnemerMenuItem;
import nl.topicus.eduarte.web.components.panels.EduArteDataPanel;
import nl.topicus.eduarte.web.components.panels.bottomrow.ModuleEditPageButton;
import nl.topicus.eduarte.web.components.panels.datapanel.table.PersoonKoppelTable;
import nl.topicus.eduarte.web.components.panels.filter.RelatieZoekFilterPanel;
import nl.topicus.eduarte.web.pages.IModuleEditPage;
import nl.topicus.eduarte.web.pages.SecurePage;
import nl.topicus.eduarte.web.pages.deelnemer.AbstractDeelnemerPage;
import nl.topicus.eduarte.web.pages.deelnemer.personalia.DeelnemerRelatiesOverzichtPage;
import nl.topicus.eduarte.zoekfilters.RelatieZoekFilter;

import org.apache.wicket.Page;
import org.apache.wicket.markup.html.link.IPageLink;

/**
 * @author idserda
 */
@PageInfo(title = "Persoon als relatie toevoegen", menu = {"Deelnemer > [deelnemer]",
	"Groep > [groep] > [deelnemer]"})
@InPrincipal(DeelnemerRelatiePersoonWrite.class)
@RequiredSecurityCheck(NietOverledenSecurityCheck.class)
public class DeelnemerPersoonAlsRelatieToevoegenPage extends AbstractDeelnemerPage implements
		IModuleEditPage<Deelnemer>
{
	private static final long serialVersionUID = 1L;

	public DeelnemerPersoonAlsRelatieToevoegenPage(Deelnemer deelnemer, Verbintenis verbintenis)
	{
		super(DeelnemerMenuItem.Relaties, deelnemer, verbintenis);
		RelatieZoekFilter filter = new RelatieZoekFilter();
		filter.setZoekPersonen(true);
		if (deelnemer.getPersoon().getEerstePersoonAdresOpPeildatum() != null)
		{
			filter.setPostcode(deelnemer.getPersoon().getEerstePersoonAdresOpPeildatum().getAdres()
				.getPostcode());
			filter.setHuisnummer(deelnemer.getPersoon().getEerstePersoonAdresOpPeildatum()
				.getAdres().getHuisnummer());
		}
		filter.setIdNotIn(getRelatieIDsPersonen());
		GeneralFilteredSortableDataProvider<Persoon, ? > dataprovider =
			getViezeDAHDieStiekemPersonenTerugGeeft(filter);

		CustomDataPanel<Persoon> datapanel =
			new EduArteDataPanel<Persoon>("datapanelRelatiesOverzicht", dataprovider,
				new PersoonKoppelTable()
				{
					private static final long serialVersionUID = 1L;

					@Override
					public void onClickKoppelen(Persoon persoon)
					{
						Relatie relatieNieuw = createNieuweRelatie();
						relatieNieuw.setRelatie(persoon);
						setResponsePage(new EditRelatiePage(relatieNieuw,
							new DeelnemerRelatiesOverzichtPage(getContextDeelnemerModel())));
					}
				});
		datapanel.setRowFactory(new CustomDataPanelRowFactory<Persoon>());
		add(datapanel);

		RelatieZoekFilterPanel filterPanel =
			new RelatieZoekFilterPanel("filter", filter, datapanel);
		add(filterPanel);

		createComponents();
	}

	@SuppressWarnings("unchecked")
	private GeneralFilteredSortableDataProvider<Persoon, ? > getViezeDAHDieStiekemPersonenTerugGeeft(
			RelatieZoekFilter filter)
	{
		return new GeneralFilteredSortableDataProvider(filter, RelatieDataAccessHelper.class);
	}

	@Override
	protected void fillBottomRow(BottomRowPanel panel)
	{
		ModuleEditPageButton<Relatie> toevoegen =
			new ModuleEditPageButton<Relatie>(panel, "Nieuwe relatie - Persoon",
				CobraKeyAction.TOEVOEGEN, Relatie.class, getSelectedMenuItem(), null, null)
			{
				private static final long serialVersionUID = 1L;

				@Override
				protected Relatie getEntity()
				{
					return createNieuweRelatie();
				}

				@Override
				protected SecurePage getReturnPage()
				{
					return new DeelnemerRelatiesOverzichtPage(getContextDeelnemerModel());
				}

			};

		panel.addButton(toevoegen);
		panel.addButton(new TerugButton(panel, new IPageLink()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public Page getPage()
			{
				return new DeelnemerRelatiesOverzichtPage(getContextDeelnemer(),
					getContextVerbintenis());
			}

			@Override
			public Class< ? extends SecurePage> getPageIdentity()
			{
				return DeelnemerPersoonAlsRelatieToevoegenPage.class;
			}

		}));

	}

	private Relatie createNieuweRelatie()
	{
		Relatie relatie = new Relatie();
		relatie.setDeelnemer(getContextDeelnemer().getPersoon());

		// TODO: Standaard PersoonContactgegevens toevoegen aan nieuwe verzorger
		Persoon verzorger = new Persoon();
		verzorger.setGeboortedatum(TimeUtil.getInstance().currentDate());
		verzorger.setGeslacht(Geslacht.Onbekend);
		NummerGeneratorDataAccessHelper generator =
			DataAccessRegistry.getHelper(NummerGeneratorDataAccessHelper.class);
		verzorger.setDebiteurennummer(generator.newDebiteurnummer());

		relatie.setRelatie(verzorger);

		return relatie;
	}

	private Collection<Long> getRelatieIDsPersonen()
	{
		Collection<Long> relatieIDs = new ArrayList<Long>();
		List<Relatie> extOrgList = getContextDeelnemer().getPersoon().getRelatiesRelatie();
		for (int i = 0; i < extOrgList.size(); i++)
		{
			relatieIDs.add(extOrgList.get(i).getRelatie().getId());
		}
		return relatieIDs;
	}

}
