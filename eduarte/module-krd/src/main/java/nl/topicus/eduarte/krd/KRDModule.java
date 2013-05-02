package nl.topicus.eduarte.krd;

import nl.topicus.cobra.security.IPrincipalSourceResolver;
import nl.topicus.cobra.security.SpringPrincipalSourceResolver;
import nl.topicus.eduarte.app.AbstractEduArteModule;
import nl.topicus.eduarte.app.EduArteModuleKey;
import nl.topicus.eduarte.app.security.EduArtePrincipal;
import nl.topicus.eduarte.entities.bpv.BPVInschrijving;
import nl.topicus.eduarte.entities.contract.Contract;
import nl.topicus.eduarte.entities.groep.Groep;
import nl.topicus.eduarte.entities.inschrijving.Intakegesprek;
import nl.topicus.eduarte.entities.inschrijving.Plaatsing;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.entities.inschrijving.Vooropleiding;
import nl.topicus.eduarte.entities.kenmerk.DeelnemerKenmerk;
import nl.topicus.eduarte.entities.kenmerk.ExterneOrganisatieKenmerk;
import nl.topicus.eduarte.entities.kenmerk.MedewerkerKenmerk;
import nl.topicus.eduarte.entities.opleiding.Opleiding;
import nl.topicus.eduarte.entities.organisatie.ExterneOrganisatie;
import nl.topicus.eduarte.entities.organisatie.Locatie;
import nl.topicus.eduarte.entities.organisatie.OrganisatieEenheid;
import nl.topicus.eduarte.entities.organisatie.SoortOrganisatieEenheid;
import nl.topicus.eduarte.entities.personen.Deelnemer;
import nl.topicus.eduarte.entities.personen.Medewerker;
import nl.topicus.eduarte.entities.personen.PersoonExterneOrganisatie;
import nl.topicus.eduarte.entities.personen.Relatie;
import nl.topicus.eduarte.entities.security.authentication.MedewerkerAccount;
import nl.topicus.eduarte.entities.taxonomie.TaxonomieElement;
import nl.topicus.eduarte.krd.web.components.menu.*;
import nl.topicus.eduarte.krd.web.pages.beheer.bron.BronAlgemeenPage;
import nl.topicus.eduarte.krd.web.pages.beheer.bron.BronInstellingenPage;
import nl.topicus.eduarte.krd.web.pages.beheer.bron.foto.BronFotobestandInlezenPage;
import nl.topicus.eduarte.krd.web.pages.beheer.bron.foto.BronFotobestandPage;
import nl.topicus.eduarte.krd.web.pages.beheer.bron.foto.BronFotobestandenPage;
import nl.topicus.eduarte.krd.web.pages.beheer.bron.terugkoppeling.BronTerugkoppelbestandDetailsPage;
import nl.topicus.eduarte.krd.web.pages.beheer.bron.terugkoppeling.BronTerugkoppelbestandInlezenPage;
import nl.topicus.eduarte.krd.web.pages.beheer.bron.terugkoppeling.BronTerugkoppelbestandenPage;
import nl.topicus.eduarte.krd.web.pages.beheer.contract.ContractEditPage;
import nl.topicus.eduarte.krd.web.pages.beheer.organisatie.extern.ExterneOrganisatieEditPage;
import nl.topicus.eduarte.krd.web.pages.beheer.organisatie.extern.ExterneOrganisatieKenmerkEditPage;
import nl.topicus.eduarte.krd.web.pages.deelnemer.EditDeelnemerPersonaliaPage;
import nl.topicus.eduarte.krd.web.pages.deelnemer.intake.EditIntakegesprekPage;
import nl.topicus.eduarte.krd.web.pages.deelnemer.kenmerk.DeelnemerKenmerkEditPage;
import nl.topicus.eduarte.krd.web.pages.deelnemer.onderwijs.DeelnemerProductregelsEditPage;
import nl.topicus.eduarte.krd.web.pages.deelnemer.onderwijs.OnderwijsproductenToevoegenPage1;
import nl.topicus.eduarte.krd.web.pages.deelnemer.relatie.EditPersoonExterneOrganisatiePage;
import nl.topicus.eduarte.krd.web.pages.deelnemer.relatie.EditRelatiePage;
import nl.topicus.eduarte.krd.web.pages.deelnemer.verbintenis.EditPlaatsingPage;
import nl.topicus.eduarte.krd.web.pages.deelnemer.verbintenis.EditVerbintenisPage;
import nl.topicus.eduarte.krd.web.pages.deelnemer.verbintenis.bpv.EditBPVInschrijvingPage;
import nl.topicus.eduarte.krd.web.pages.deelnemer.verbintenis.vooropleiding.EditVooropleidingPage;
import nl.topicus.eduarte.krd.web.pages.groep.GroepEditPage;
import nl.topicus.eduarte.krd.web.pages.intake.stap0.IntakeStap0Zoeken;
import nl.topicus.eduarte.krd.web.pages.medewerker.EditMedewerkerPage;
import nl.topicus.eduarte.krd.web.pages.medewerker.MedewerkerAanstellingEditPage;
import nl.topicus.eduarte.krd.web.pages.medewerker.MedewerkerAccountEditPage;
import nl.topicus.eduarte.krd.web.pages.medewerker.MedewerkerKenmerkEditPage;
import nl.topicus.eduarte.krd.web.pages.medewerker.NieuweMedewerkerPage;
import nl.topicus.eduarte.krd.web.pages.onderwijs.opleiding.OpleidingEditPage;
import nl.topicus.eduarte.krd.web.pages.onderwijs.opleiding.OpleidingHerstellenEditPage;
import nl.topicus.eduarte.krd.web.pages.taxonomie.EditTaxonomieElementPage;
import nl.topicus.eduarte.web.components.factory.KRDModuleComponentFactory;
import nl.topicus.eduarte.web.components.menu.*;
import nl.topicus.eduarte.web.pages.beheer.organisatie.LocatieEditPage;
import nl.topicus.eduarte.web.pages.beheer.organisatie.OrganisatieEenheidEditPage;
import nl.topicus.eduarte.web.pages.beheer.organisatie.SoortOrganisatieEenheidEditPage;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.request.target.coding.IndexedHybridUrlCodingStrategy;

/**
 * KRD module (wijzigschermen voor NAW-gegevens, inschrijfschermen etc)
 * 
 * @author loite
 */
public class KRDModule extends AbstractEduArteModule
{
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor
	 */
	public KRDModule()
	{
		super(EduArteModuleKey.KERNREGISTRATIE_DEELNEMERS);
	}

	@Override
	public String getVersion()
	{
		return "1.0";
	}

	/**
	 * @see nl.topicus.cobra.modules.AbstractCobraModule#registerMenuExtenders()
	 */
	@Override
	protected void registerMenuExtenders()
	{
		addMenuExtender(DeelnemerMenu.class, new KrdDeelnemerMenuExtender());
		addMenuExtender(OpleidingMenu.class, new KrdOpleidingMenuExtender());
		addMenuExtender(TaxonomieElementMenu.class, new KrdTaxonomieElementMenuExtender());
		addMenuExtender(BeheerMenu.class, new KrdBeheerMenuExtender());
		addMenuExtender(DeelnemerCollectiefMenu.class, new KrdDeelnemerCollectiefMenuExtender());
		addMenuExtender(OnderwijsCollectiefMenu.class, new KrdOnderwijsCollectiefMenuExtender());
		addMenuExtender(OnderwijsproductMenu.class, new KrdOnderwijsproductMenuExtender());
		addMenuExtender(RelatieBeheerMenu.class, new KrdRelatieBeheerMenuExtender());
		addMenuExtender(ContractMenu.class, new KrdContractMenuExtender());
	}

	@Override
	protected void registerModulePanels()
	{
		addModulePanelFactory(KRDModuleComponentFactory.class, new KRDModuleComponentFactoryImpl());
	}

	@Override
	protected void registerModuleEditPages()
	{
		super.registerModuleEditPages();
		addModuleEditPage(TaxonomieElement.class, OnderwijsCollectiefMenuItem.TaxonomieZoeken,
			EditTaxonomieElementPage.class);
		addModuleEditPage(TaxonomieElement.class, TaxonomieElementMenuItem.Algemeen,
			EditTaxonomieElementPage.class);
		addModuleEditPage(TaxonomieElement.class, TaxonomieElementMenuItem.Verbintenisgebieden,
			EditTaxonomieElementPage.class);
		addModuleEditPage(TaxonomieElement.class, TaxonomieElementMenuItem.Deelgebieden,
			EditTaxonomieElementPage.class);

		addModuleEditPage(Deelnemer.class, DeelnemerMenuItem.Personalia,
			EditDeelnemerPersonaliaPage.class);
		addModuleEditPage(Relatie.class, DeelnemerMenuItem.Relaties, EditRelatiePage.class);

		addModuleEditPage(Verbintenis.class, DeelnemerMenuItem.Verbintenis,
			EditVerbintenisPage.class);
		addModuleEditPage(Plaatsing.class, DeelnemerMenuItem.Verbintenis, EditPlaatsingPage.class);
		addModuleEditPage(Intakegesprek.class, DeelnemerMenuItem.Intake,
			EditIntakegesprekPage.class);
		addModuleEditPage(Vooropleiding.class, DeelnemerMenuItem.Vooropleidingen,
			EditVooropleidingPage.class);
		addModuleEditPage(DeelnemerKenmerk.class, DeelnemerMenuItem.Kenmerken,
			DeelnemerKenmerkEditPage.class);
		addModuleEditPage(Verbintenis.class, DeelnemerMenuItem.Productregels,
			DeelnemerProductregelsEditPage.class);
		addModuleEditPage(Verbintenis.class, DeelnemerMenuItem.AfgOnderwijsproducten,
			OnderwijsproductenToevoegenPage1.class);

		addModuleEditPage(PersoonExterneOrganisatie.class, DeelnemerMenuItem.Relaties,
			EditPersoonExterneOrganisatiePage.class);

		addModuleEditPage(Medewerker.class, MedewerkerCollectiefMenuItem.Zoeken,
			NieuweMedewerkerPage.class);
		addModuleEditPage(Medewerker.class, MedewerkerMenuItem.Personalia, EditMedewerkerPage.class);
		addModuleEditPage(MedewerkerAccount.class, MedewerkerMenuItem.Account,
			MedewerkerAccountEditPage.class);
		addModuleEditPage(Medewerker.class, MedewerkerMenuItem.Aanstelling,
			MedewerkerAanstellingEditPage.class);
		addModuleEditPage(MedewerkerKenmerk.class, MedewerkerMenuItem.Kenmerken,
			MedewerkerKenmerkEditPage.class);

		addModuleEditPage(Locatie.class, BeheerMenuItem.Locaties, LocatieEditPage.class);
		addModuleEditPage(OrganisatieEenheid.class, BeheerMenuItem.Organisatie_eenheden,
			OrganisatieEenheidEditPage.class);
		addModuleEditPage(SoortOrganisatieEenheid.class, BeheerMenuItem.SoortOrganisatie_Eenheden,
			SoortOrganisatieEenheidEditPage.class);

		addModuleEditPage(Opleiding.class, OnderwijsCollectiefMenuItem.OpleidingenZoeken,
			OpleidingEditPage.class);
		addModuleEditPage(Opleiding.class, OpleidingMenuItem.Opleidingkaart,
			OpleidingEditPage.class);
		addModuleEditPage(Opleiding.class, OnderwijsCollectiefMenuItem.Herstellen,
			OpleidingHerstellenEditPage.class);

		addModuleEditPage(Groep.class, GroepCollectiefMenuItem.GroepZoeken, GroepEditPage.class);
		addModuleEditPage(Groep.class, GroepMenuItem.Groepkaart, GroepEditPage.class);

		addModuleEditPage(ExterneOrganisatie.class, RelatieBeheerMenuItem.ExterneOrganisaties,
			ExterneOrganisatieEditPage.class);
		addModuleEditPage(ExterneOrganisatieKenmerk.class, ExterneOrganisatieMenuItem.Kenmerken,
			ExterneOrganisatieKenmerkEditPage.class);

		addModuleEditPage(Contract.class, RelatieBeheerMenuItem.Contracten, ContractEditPage.class);

		addModuleEditPage(BPVInschrijving.class, DeelnemerMenuItem.BPV,
			EditBPVInschrijvingPage.class);

	}

	@Override
	public void registerBookmarkablePageMounts(WebApplication app)
	{
		mount(app, "/deelnemers/intake", IntakeStap0Zoeken.class);
		mount(app, "/beheer/bron/algemeen", BronAlgemeenPage.class);
		mount(app, "/beheer/bron/foto/inlezen", BronFotobestandInlezenPage.class);
		mount(app, "/beheer/bron/foto/overzicht", BronFotobestandenPage.class);
		mount(app, "/beheer/bron/foto/details", BronFotobestandPage.class);
		mount(app, "/beheer/bron/instellingen", BronInstellingenPage.class);
		mount(app, "/beheer/bron/terugkoppel/overzicht", BronTerugkoppelbestandenPage.class);
		mount(app, "/beheer/bron/terugkoppel/inlezen", BronTerugkoppelbestandInlezenPage.class);
		mount(app, "/beheer/bron/terugkoppel/details", BronTerugkoppelbestandDetailsPage.class);
	}

	@Override
	public IPrincipalSourceResolver<EduArtePrincipal> getPrincipalSourceResolver()
	{
		return new SpringPrincipalSourceResolver<EduArtePrincipal>(
			"nl.topicus.eduarte.krd.principals", "nl.topicus.eduarte.krd.web.pages");
	}

	private void mount(WebApplication application, String path, Class< ? extends WebPage> page)
	{
		application.mount(new IndexedHybridUrlCodingStrategy(path, page));
	}
}
