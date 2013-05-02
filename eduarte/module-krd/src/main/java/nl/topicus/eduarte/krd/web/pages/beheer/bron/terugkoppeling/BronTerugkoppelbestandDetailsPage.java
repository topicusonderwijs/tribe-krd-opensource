package nl.topicus.eduarte.krd.web.pages.beheer.bron.terugkoppeling;

import java.util.List;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.modelsv2.ModelFactory;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.util.ComponentUtil;
import nl.topicus.cobra.web.components.IdBasedModelSelection;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.cobra.web.components.panels.bottomrow.TerugButton;
import nl.topicus.eduarte.app.security.actions.Instelling;
import nl.topicus.eduarte.app.security.actions.OrganisatieEenheid;
import nl.topicus.eduarte.app.security.actions.SearchImplementsActions;
import nl.topicus.eduarte.krd.entities.bron.BronMeldingStatus;
import nl.topicus.eduarte.krd.entities.bron.meldingen.terugkoppeling.IBronSignaal;
import nl.topicus.eduarte.krd.entities.bron.meldingen.terugkoppeling.IBronTerugkoppeling;
import nl.topicus.eduarte.krd.principals.beheer.bron.BronOverzichtInzien;
import nl.topicus.eduarte.krd.web.pages.beheer.bron.AbstractBronPage;
import nl.topicus.eduarte.krd.web.pages.beheer.bron.overzichten.BronMeldingenPage;
import nl.topicus.eduarte.krd.web.pages.beheer.bron.overzichten.BronSignalenPage;
import nl.topicus.eduarte.krd.zoekfilters.BronMeldingZoekFilter;
import nl.topicus.eduarte.krd.zoekfilters.BronSignaalZoekFilter;
import nl.topicus.eduarte.krd.zoekfilters.BronTerugkoppelingZoekFilter;
import nl.topicus.onderwijs.duo.bron.vo.waardelijsten.Ernst;

import org.apache.wicket.Page;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.IPageLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.repeater.RepeatingView;

@PageInfo(title = "BRON Terugkoppeling details", menu = "Deelnemer")
@InPrincipal(BronOverzichtInzien.class)
@SearchImplementsActions( {Instelling.class, OrganisatieEenheid.class})
public class BronTerugkoppelbestandDetailsPage extends AbstractBronPage
{
	private BronTerugkoppelingZoekFilter filter;

	public BronTerugkoppelbestandDetailsPage(IBronTerugkoppeling terugkoppeling,
			BronTerugkoppelingZoekFilter filter)
	{
		this.filter = filter;
		setDefaultModel(ModelFactory.getCompoundModelForObject(terugkoppeling));
		add(new Label("aanleverpuntNummer"));
		add(new Label("bronOnderwijssoort"));
		add(new Label("bRONBatchNummer"));
		add(new Label("datumTerugkoppeling"));
		add(new Label("createdAt"));
		add(new Label("batchesInBestand"));

		RepeatingView meldingen = new RepeatingView("meldingen");
		addRow(meldingen, "Aantal meldingen totaal", terugkoppeling.getAantalMeldingen(),
			(BronMeldingStatus) null);
		addRow(meldingen, "Aantal goedgekeurde meldingen", terugkoppeling
			.getAantalGoedgekeurdeMeldingen(), BronMeldingStatus.GOEDGEKEURD);
		addRow(meldingen, "Aantal afgekeurde meldingen", terugkoppeling
			.getAantalAfgekeurdeMeldingen(), BronMeldingStatus.AFGEKEURD);
		add(meldingen);

		RepeatingView signalen = new RepeatingView("signalen");
		int totaalSignalen = terugkoppeling.getAantalSignalen();
		int afkeurSignalen = terugkoppeling.getAantalAfkeurSignalen();
		addRow(signalen, "Aantal signalen totaal", totaalSignalen, (Ernst) null);
		addRow(signalen, "Aantal waarschuwingen", totaalSignalen - afkeurSignalen, Ernst.SIGNAAL);
		addRow(signalen, "Aantal afkeuringen", afkeurSignalen, Ernst.AFKEURING);
		add(signalen);

		createComponents();
	}

	@Override
	protected void onDetach()
	{
		ComponentUtil.detachQuietly(filter);
		super.onDetach();
	}

	private void addRow(RepeatingView meldingen, String omschrijving, int aantal,
			BronMeldingStatus status)
	{
		WebMarkupContainer row = new WebMarkupContainer(meldingen.newChildId());
		row.add(new Label("omschrijving", omschrijving));
		row.add(new Label("aantal", String.valueOf(aantal)));
		row.add(new BronMeldingZoekFilterLink("link", status));
		meldingen.add(row);
	}

	private void addRow(RepeatingView meldingen, String omschrijving, int aantal, Ernst ernst)
	{
		WebMarkupContainer row = new WebMarkupContainer(meldingen.newChildId());
		row.add(new Label("omschrijving", omschrijving));
		row.add(new Label("aantal", String.valueOf(aantal)));
		row.add(new BronSignaalZoekFilterLink("link", ernst));
		meldingen.add(row);
	}

	@Override
	protected void fillBottomRow(BottomRowPanel panel)
	{
		panel.addButton(new TerugButton(panel, new IPageLink()
		{
			private static final long serialVersionUID = 1L;

			@SuppressWarnings("unchecked")
			@Override
			public Class getPageIdentity()
			{
				return BronTerugkoppelbestandenPage.class;
			}

			@Override
			public Page getPage()
			{
				return new BronTerugkoppelbestandenPage(filter);
			}
		}));
		super.fillBottomRow(panel);
	}

	@Override
	public void getBookmarkConstructorArguments(List<Class< ? >> ctorArgTypes,
			List<Object> ctorArgValues)
	{
		super.getBookmarkConstructorArguments(ctorArgTypes, ctorArgValues);
		ctorArgTypes.add(IBronTerugkoppeling.class);
		ctorArgTypes.add(BronTerugkoppelingZoekFilter.class);
		ctorArgValues.add(BronTerugkoppelbestandDetailsPage.this.getDefaultModelObject());
		ctorArgValues.add(filter);
	}

	private IBronTerugkoppeling getTerugkoppelbestand()
	{
		return (IBronTerugkoppeling) BronTerugkoppelbestandDetailsPage.this.getDefaultModelObject();
	}

	public class BronMeldingZoekFilterLink extends Link<Void>
	{
		private static final long serialVersionUID = 1L;

		private final BronMeldingStatus status;

		public BronMeldingZoekFilterLink(String id, BronMeldingStatus status)
		{
			super(id);
			this.status = status;
		}

		@Override
		public void onClick()
		{
			BronMeldingZoekFilter meldingFilter = new BronMeldingZoekFilter();
			meldingFilter.setTerugkoppelbestand(getTerugkoppelbestand());
			if (status != null)
			{
				meldingFilter.setMeldingStatus(status);
			}
			setResponsePage(new BronMeldingenPage(meldingFilter));
		}
	}

	public class BronSignaalZoekFilterLink extends Link<Void>
	{
		private static final long serialVersionUID = 1L;

		private final Ernst ernst;

		public BronSignaalZoekFilterLink(String id, Ernst ernst)
		{
			super(id);
			this.ernst = ernst;
		}

		@Override
		public void onClick()
		{
			BronSignaalZoekFilter signaalFilter = new BronSignaalZoekFilter();
			signaalFilter.setErnst(ernst);
			signaalFilter.setTerugkoppelbestand(getTerugkoppelbestand());
			setResponsePage(new BronSignalenPage(signaalFilter,
				new IdBasedModelSelection<IBronSignaal>()));
		}
	}
}