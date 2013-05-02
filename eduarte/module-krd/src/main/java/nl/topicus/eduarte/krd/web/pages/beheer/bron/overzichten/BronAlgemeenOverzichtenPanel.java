package nl.topicus.eduarte.krd.web.pages.beheer.bron.overzichten;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import nl.topicus.cobra.web.security.TargetBasedSecurePageLink;
import nl.topicus.eduarte.krd.entities.bron.BronMeldingStatus;
import nl.topicus.eduarte.krd.entities.bron.BronSchooljaarStatus;
import nl.topicus.eduarte.krd.web.pages.beheer.bron.cfi.BronCfiTerugmeldingenPage;
import nl.topicus.eduarte.krd.web.pages.beheer.bron.foto.BronFotobestandenPage;
import nl.topicus.eduarte.krd.web.pages.beheer.bron.terugkoppeling.BronTerugkoppelbestandenPage;
import nl.topicus.eduarte.krd.zoekfilters.BronBatchZoekFilter;
import nl.topicus.eduarte.krd.zoekfilters.BronExamenverzamelingZoekFilter;
import nl.topicus.eduarte.krd.zoekfilters.BronMeldingZoekFilter;
import nl.topicus.eduarte.krd.zoekfilters.BronSignaalZoekFilter;
import nl.topicus.eduarte.krd.zoekfilters.BronTerugkoppelingZoekFilter;

import org.apache.wicket.Page;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.IPageLink;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

public class BronAlgemeenOverzichtenPanel extends Panel
{
	private static final long serialVersionUID = 1L;

	private IModel<BronSchooljaarStatus> schooljaarStatusModel;

	private class LinkLabel implements Serializable
	{
		private static final long serialVersionUID = 1L;

		private TargetBasedSecurePageLink<Void> pageLink;

		private String label;

		private LinkLabel(TargetBasedSecurePageLink<Void> pageLink, String label)
		{
			this.pageLink = pageLink;
			this.label = label;
		}

		public TargetBasedSecurePageLink<Void> getPageLink()
		{
			return pageLink;
		}

		public String getLabel()
		{
			return label;
		}
	}

	public BronAlgemeenOverzichtenPanel(String id,
			IModel<BronSchooljaarStatus> schooljaarStatusModel)
	{
		super(id);
		this.schooljaarStatusModel = schooljaarStatusModel;
		ListView<LinkLabel> listView = new ListView<LinkLabel>("listview", createLinks())
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(ListItem<LinkLabel> item)
			{
				LinkLabel linkLabel = item.getModelObject();
				TargetBasedSecurePageLink<Void> pageLink = linkLabel.getPageLink();
				pageLink.addOrReplace(new Label("label", linkLabel.getLabel()));
				item.add(pageLink);
			}
		};
		add(listView);
	}

	private List<LinkLabel> createLinks()
	{
		List<LinkLabel> links = new ArrayList<LinkLabel>();
		links.add(new LinkLabel(new TargetBasedSecurePageLink<Void>("link", new IPageLink()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public Page getPage()
			{
				return new BronBatchesPage(new BronBatchZoekFilter(schooljaarStatusModel));
			}

			@Override
			public Class< ? extends Page> getPageIdentity()
			{
				return BronBatchesPage.class;
			}

		}), "Batches"));
		links.add(new LinkLabel(new TargetBasedSecurePageLink<Void>("link", new IPageLink()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public Page getPage()
			{
				BronMeldingZoekFilter filter = new BronMeldingZoekFilter(schooljaarStatusModel);
				filter.setMeldingStatus(BronMeldingStatus.WACHTRIJ);
				return new BronMeldingenPage(filter);
			}

			@Override
			public Class< ? extends Page> getPageIdentity()
			{
				return BronMeldingenPage.class;
			}

		}), "Meldingen"));
		links.add(new LinkLabel(new TargetBasedSecurePageLink<Void>("link", new IPageLink()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public Page getPage()
			{
				return new BronExamenverzamelingenPage(new BronExamenverzamelingZoekFilter(
					schooljaarStatusModel));
			}

			@Override
			public Class< ? extends Page> getPageIdentity()
			{
				return BronExamenverzamelingenPage.class;
			}

		}), "Examenverzamelingen"));
		links.add(new LinkLabel(new TargetBasedSecurePageLink<Void>("link", new IPageLink()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public Page getPage()
			{
				return new BronTerugkoppelbestandenPage(new BronTerugkoppelingZoekFilter(
					schooljaarStatusModel));
			}

			@Override
			public Class< ? extends Page> getPageIdentity()
			{
				return BronTerugkoppelbestandenPage.class;
			}

		}), "Terugkoppelingen"));
		links.add(new LinkLabel(new TargetBasedSecurePageLink<Void>("link", new IPageLink()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public Page getPage()
			{
				return new BronSignalenPage(new BronSignaalZoekFilter(schooljaarStatusModel));
			}

			@Override
			public Class< ? extends Page> getPageIdentity()
			{
				return BronSignalenPage.class;
			}

		}), "Signalen"));
		links.add(new LinkLabel(new TargetBasedSecurePageLink<Void>("link",
			BronFotobestandenPage.class), "Fotobestanden"));
		links.add(new LinkLabel(new TargetBasedSecurePageLink<Void>("link",
			BronCfiTerugmeldingenPage.class), "Cfi-terugmeldingsoverzicht bestanden"));
		return links;
	}
}
