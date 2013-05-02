/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.krd.web.pages.beheer.bron.overzichten;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.dataproviders.CollectionDataProvider;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.util.ComponentUtil;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanelPageLinkRowFactory;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.cobra.web.components.panels.bottomrow.ButtonAlignment;
import nl.topicus.cobra.web.components.panels.bottomrow.PageLinkButton;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.entities.personen.Deelnemer;
import nl.topicus.eduarte.krd.dao.helpers.BronDataAccessHelper;
import nl.topicus.eduarte.krd.entities.bron.meldingen.aanlevering.IBronMelding;
import nl.topicus.eduarte.krd.principals.deelnemer.bron.DeelnemerBron;
import nl.topicus.eduarte.krd.web.components.panels.datapanel.table.DeelnemerBronMeldingTable;
import nl.topicus.eduarte.krd.web.pages.deelnemer.bron.DeelnemerBronMeldingToevoegenPage;
import nl.topicus.eduarte.krd.zoekfilters.BronMeldingZoekFilter;
import nl.topicus.eduarte.providers.DeelnemerProvider;
import nl.topicus.eduarte.web.components.menu.DeelnemerMenuItem;
import nl.topicus.eduarte.web.components.menu.main.CoreMainMenuItem;
import nl.topicus.eduarte.web.components.panels.EduArteDataPanel;
import nl.topicus.eduarte.web.pages.deelnemer.AbstractDeelnemerPage;
import nl.topicus.eduarte.zoekfilters.OrganisatieEenheidLocatieAuthorizationContext;

import org.apache.wicket.Page;
import org.apache.wicket.behavior.SimpleAttributeModifier;
import org.apache.wicket.markup.html.link.IPageLink;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.LoadableDetachableModel;

/**
 * @author idserda
 */
@PageInfo(title = "Bron", menu = "Deelnemer > Bron")
@InPrincipal(DeelnemerBron.class)
public class DeelnemerBronPage extends AbstractDeelnemerPage
{
	private final class MeldingListModel extends LoadableDetachableModel<List<IBronMelding>>
	{
		private static final long serialVersionUID = 1L;

		private BronMeldingZoekFilter filter;

		public MeldingListModel(BronMeldingZoekFilter filter)
		{
			this.filter = filter;
		}

		@Override
		protected List<IBronMelding> load()
		{
			List<IBronMelding> meldingen =
				DataAccessRegistry.getHelper(BronDataAccessHelper.class).getBronMeldingen(filter);
			Collections.sort(meldingen, new Comparator<IBronMelding>()
			{
				@Override
				public int compare(IBronMelding melding1, IBronMelding melding2)
				{
					return melding2.getCreatedAt().compareTo(melding1.getCreatedAt());
				}
			});
			return meldingen;
		}

		@Override
		protected void onDetach()
		{
			ComponentUtil.detachQuietly(filter);
			super.onDetach();
		}
	}

	public DeelnemerBronPage(DeelnemerProvider deelnemer)
	{
		this(deelnemer.getDeelnemer());
	}

	public DeelnemerBronPage(Deelnemer deelnemer)
	{
		this(deelnemer, deelnemer.getEersteInschrijvingOpPeildatum());
	}

	public DeelnemerBronPage(Deelnemer deelnemer, Verbintenis verbintenis)
	{
		super(DeelnemerMenuItem.Bron, deelnemer, verbintenis);

		BronMeldingZoekFilter filter = new BronMeldingZoekFilter(deelnemer);
		filter.setAuthorizationContext(new OrganisatieEenheidLocatieAuthorizationContext(this));
		CollectionDataProvider<IBronMelding> provider =
			new CollectionDataProvider<IBronMelding>(new MeldingListModel(filter));
		DeelnemerBronMeldingTable table = new DeelnemerBronMeldingTable();
		EduArteDataPanel<IBronMelding> datapanel =
			new EduArteDataPanel<IBronMelding>("datapanel", provider, table);
		datapanel.setRowFactory(new CustomDataPanelPageLinkRowFactory<IBronMelding>(
			BronMeldingDetailsPage.class)
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onRowCreated(Item<IBronMelding> item)
			{
				IBronMelding melding = item.getModelObject();
				String reden = melding.getReden();
				if (reden == null)
					reden = "";
				item.add(new SimpleAttributeModifier("title", reden));
			}

			@Override
			protected void onClick(Item<IBronMelding> item)
			{
				IBronMelding melding = item.getModelObject();
				setResponsePage(new BronMeldingDetailsPage(melding, DeelnemerBronPage.this,
					CoreMainMenuItem.Deelnemer));
			}
		});

		add(datapanel);

		createComponents();
	}

	@Override
	protected void fillBottomRow(BottomRowPanel panel)
	{
		panel.addButton(new PageLinkButton(panel, "Nieuwe melding", ButtonAlignment.RIGHT,
			new IPageLink()
			{
				private static final long serialVersionUID = 1L;

				@Override
				public Page getPage()
				{
					return new DeelnemerBronMeldingToevoegenPage(getContextDeelnemer());
				}

				@Override
				public Class<DeelnemerBronMeldingToevoegenPage> getPageIdentity()
				{
					return DeelnemerBronMeldingToevoegenPage.class;
				}
			}));
		super.fillBottomRow(panel);
	}
}