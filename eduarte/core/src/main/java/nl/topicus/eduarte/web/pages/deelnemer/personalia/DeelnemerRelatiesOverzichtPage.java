/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.web.pages.deelnemer.personalia;

import java.util.List;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.dataproviders.ListModelDataProvider;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanel;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanelPageLinkRowFactory;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.eduarte.app.EduArteApp;
import nl.topicus.eduarte.core.principals.deelnemer.DeelnemerRelaties;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.entities.personen.AbstractRelatie;
import nl.topicus.eduarte.entities.personen.Deelnemer;
import nl.topicus.eduarte.entities.personen.PersoonExterneOrganisatie;
import nl.topicus.eduarte.entities.personen.Relatie;
import nl.topicus.eduarte.providers.DeelnemerProvider;
import nl.topicus.eduarte.web.components.factory.KRDModuleComponentFactory;
import nl.topicus.eduarte.web.components.menu.DeelnemerMenuItem;
import nl.topicus.eduarte.web.components.panels.EduArteDataPanel;
import nl.topicus.eduarte.web.components.panels.datapanel.table.RelatieTable;
import nl.topicus.eduarte.web.pages.deelnemer.AbstractDeelnemerPage;

import org.apache.wicket.Page;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;

/**
 * @author idserda
 */
@PageInfo(title = "Relaties", menu = {"Deelnemer > [deelnemer]", "Groep > [groep] > [deelnemer]"})
@InPrincipal(DeelnemerRelaties.class)
public class DeelnemerRelatiesOverzichtPage extends AbstractDeelnemerPage
{
	private static final long serialVersionUID = 1L;

	public DeelnemerRelatiesOverzichtPage(DeelnemerProvider provider)
	{
		this(provider.getDeelnemer(), provider.getDeelnemer()
			.getEersteInschrijvingOpPeildatum(true));
	}

	public DeelnemerRelatiesOverzichtPage(IModel<Deelnemer> deelnemer)
	{
		this(deelnemer.getObject());
	}

	public DeelnemerRelatiesOverzichtPage(Deelnemer deelnemer)
	{
		this(deelnemer, deelnemer.getEersteInschrijvingOpPeildatum(true));
	}

	public DeelnemerRelatiesOverzichtPage(Verbintenis inschrijving)
	{
		this(inschrijving.getDeelnemer(), inschrijving);
	}

	public DeelnemerRelatiesOverzichtPage(Deelnemer deelnemer, Verbintenis inschrijving)
	{
		super(DeelnemerMenuItem.Relaties, deelnemer, inschrijving);
		CustomDataPanel<AbstractRelatie> datapanel =
			new EduArteDataPanel<AbstractRelatie>("datapanelDeelnemerRelaties",
				new ListModelDataProvider<AbstractRelatie>(getRelatiesModel()),
				new RelatieTable<AbstractRelatie>(deelnemer.getPersoon().isMeerderjarig()));

		datapanel.setRowFactory(createRowFactory());
		add(datapanel);
		createComponents();
	}

	@Override
	protected void fillBottomRow(BottomRowPanel panel)
	{
		List<KRDModuleComponentFactory> factories =
			EduArteApp.get().getPanelFactories(KRDModuleComponentFactory.class);
		for (KRDModuleComponentFactory factory : factories)
		{
			factory.newPersoonAlsRelatieToevoegenKnop(panel, getContextDeelnemerModel(),
				getContextVerbintenisModel());
			factory.newOrganisatieAlsRelatieToevoegenKnop(panel, getContextDeelnemerModel(),
				getContextVerbintenisModel());
		}
	}

	private CustomDataPanelPageLinkRowFactory<AbstractRelatie> createRowFactory()
	{
		return new CustomDataPanelPageLinkRowFactory<AbstractRelatie>(null)
		{

			private static final long serialVersionUID = 1L;

			@Override
			protected Class< ? extends Page> getTarget(Item<AbstractRelatie> item)
			{
				AbstractRelatie obj = item.getModelObject();
				if (obj instanceof Relatie)
					return RelatieDetailPage.class;
				else
					return PersoonExterneOrganisatieDetailPage.class;
			}

			@Override
			protected void onClick(Item<AbstractRelatie> item)
			{
				AbstractRelatie obj = item.getModelObject();
				if (obj instanceof Relatie)
				{
					setResponsePage(new RelatieDetailPage(getContextDeelnemer(),
						getContextVerbintenis(), (Relatie) obj));
				}
				else if (obj instanceof PersoonExterneOrganisatie)
				{
					setResponsePage(new PersoonExterneOrganisatieDetailPage(getContextDeelnemer(),
						getContextVerbintenis(), (PersoonExterneOrganisatie) obj));
				}
			}
		};
	}

	private LoadableDetachableModel<List<AbstractRelatie>> getRelatiesModel()
	{
		return new LoadableDetachableModel<List<AbstractRelatie>>()
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected List<AbstractRelatie> load()
			{
				return getContextDeelnemer().getPersoon().getRelaties();
			}

		};
	}

}
