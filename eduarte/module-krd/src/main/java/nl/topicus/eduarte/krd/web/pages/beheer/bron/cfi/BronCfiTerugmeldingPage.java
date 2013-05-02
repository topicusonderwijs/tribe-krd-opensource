package nl.topicus.eduarte.krd.web.pages.beheer.bron.cfi;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.modelsv2.ModelFactory;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.cobra.web.components.panels.bottomrow.VerwijderButton;
import nl.topicus.cobra.web.security.TargetBasedSecurePageLink;
import nl.topicus.eduarte.app.security.actions.Instelling;
import nl.topicus.eduarte.app.security.actions.OrganisatieEenheid;
import nl.topicus.eduarte.app.security.actions.SearchImplementsActions;
import nl.topicus.eduarte.krd.entities.bron.cfi.BronCfiRegelType;
import nl.topicus.eduarte.krd.entities.bron.cfi.BronCfiTerugmelding;
import nl.topicus.eduarte.krd.entities.bron.cfi.BronCfiTerugmeldingRegel;
import nl.topicus.eduarte.krd.principals.deelnemer.bron.DeelnemerBronFotoInlezen;
import nl.topicus.eduarte.krd.web.pages.beheer.bron.AbstractBronPage;

import org.apache.wicket.Page;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.IPageLink;
import org.apache.wicket.markup.repeater.RepeatingView;

/**
 * Pagina die de details van een CFI-terugmelding toont, en dan vooral de verschillen
 * 
 * @author vandekamp
 */
@PageInfo(title = "BRON CFI-terugmelding", menu = "Deelnemer > BRON > CFI-terugmelding > [terugmelding]")
@InPrincipal(DeelnemerBronFotoInlezen.class)
@SearchImplementsActions( {Instelling.class, OrganisatieEenheid.class})
public class BronCfiTerugmeldingPage extends AbstractBronPage
{
	private static final long serialVersionUID = 1L;

	public BronCfiTerugmeldingPage(BronCfiTerugmelding terugmelding)
	{
		setDefaultModel(ModelFactory.getCompoundModel(terugmelding));
		add(new Label("peildatum"));
		add(new Label("inleesdatum"));
		add(new Label("aanmaakdatum"));
		add(new Label("ingelezenDoor"));
		RepeatingView meldingen = new RepeatingView("meldingen");
		addRow(meldingen, BronCfiRegelType.BEK, terugmelding.getAantalBEK(), "0");
		addRow(meldingen, BronCfiRegelType.EXP, terugmelding.getAantalEXP(), "0");
		addRow(meldingen, BronCfiRegelType.SAG, terugmelding.getAantalSAG(), "nvt");
		addRow(meldingen, BronCfiRegelType.SIN, terugmelding.getAantalSIN(), "nvt");
		addRow(meldingen, BronCfiRegelType.SBH, terugmelding.getAantalSBH(), "0");
		addRow(meldingen, BronCfiRegelType.SBL, terugmelding.getAantalSBL(), "0");
		add(meldingen);
		createComponents();
	}

	private void addRow(RepeatingView meldingen, BronCfiRegelType regelType, int aantal,
			String aantalConflicten)
	{
		TargetBasedSecurePageLink<Void> link =
			new TargetBasedSecurePageLink<Void>(meldingen.newChildId(), getPageLink(regelType));
		link.add(new Label("omschrijving", regelType.getOmschrijving()));
		link.add(new Label("aantal", String.valueOf(aantal)));
		link.add(new Label("aantalConflicten", aantalConflicten));
		meldingen.add(link);
	}

	private IPageLink getPageLink(final BronCfiRegelType regelType)
	{
		return new IPageLink()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public Page getPage()
			{
				BronCfiTerugmelding terugmelding =
					(BronCfiTerugmelding) BronCfiTerugmeldingPage.this.getDefaultModelObject();
				return new BronCfiTerugmeldingRegelsPage(terugmelding, regelType);
			}

			@Override
			public Class< ? extends Page> getPageIdentity()
			{
				return BronCfiTerugmeldingRegelsPage.class;
			}
		};
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
				// verwijder CfiTerugmelding
				BronCfiTerugmelding melding =
					(BronCfiTerugmelding) BronCfiTerugmeldingPage.this.getDefaultModelObject();
				for (BronCfiTerugmeldingRegel regel : melding.getRegels())
					regel.delete();
				melding.delete();
				melding.commit();
				setResponsePage(new BronCfiTerugmeldingenPage());
			}

			@Override
			public boolean isVisible()
			{
				return true;
			}
		});
	}
}
