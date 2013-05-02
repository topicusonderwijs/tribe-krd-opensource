package nl.topicus.eduarte.krd.web.components.menu;

import java.util.ArrayList;
import java.util.List;

import nl.topicus.cobra.web.components.menu.AbstractMenuExtender;
import nl.topicus.cobra.web.components.menu.DropdownMenuItem;
import nl.topicus.cobra.web.components.menu.IMenuItem;
import nl.topicus.cobra.web.components.menu.MenuItem;
import nl.topicus.eduarte.krd.web.pages.deelnemer.collectief.aanmaken.CollectiefAanmakenOverzichtPage;
import nl.topicus.eduarte.krd.web.pages.deelnemer.collectief.bpv.BPVInschrijvingCollectiefEditOverzichtPage;
import nl.topicus.eduarte.krd.web.pages.deelnemer.collectief.bronmutatie.CollectiefBronmutatieOverzichtPage;
import nl.topicus.eduarte.krd.web.pages.deelnemer.collectief.orgehdwijzigen.OrganisatieEenheidLocatieCollectiefWijzigenOverzichtPage;
import nl.topicus.eduarte.krd.web.pages.deelnemer.collectief.verbintenis.VerbintenisCollectiefEditOverzichtPage;
import nl.topicus.eduarte.krd.web.pages.deelnemer.examens.CriteriumbankControleOverzichtPage;
import nl.topicus.eduarte.krd.web.pages.deelnemer.examens.DeelnemerKwalificatiePage;
import nl.topicus.eduarte.krd.web.pages.deelnemer.onderwijs.OndPrCollectieveAfnameStap1Page;
import nl.topicus.eduarte.krd.web.pages.deelnemer.onderwijs.OndPrKeuzeAangevenStap1Page;
import nl.topicus.eduarte.web.components.menu.DeelnemerCollectiefMenu;
import nl.topicus.eduarte.web.components.menu.DeelnemerCollectiefMenuItem;

/**
 * Extender voor het deelnemer gedeelte.
 * 
 * @author vandekamp
 */
public class KrdDeelnemerCollectiefMenuExtender extends
		AbstractMenuExtender<DeelnemerCollectiefMenu>
{
	@Override
	public List<IMenuItem> getMenuExtension(DeelnemerCollectiefMenu menu)
	{
		List<IMenuItem> ret = new ArrayList<IMenuItem>();

		DropdownMenuItem onderwijsproducten = new DropdownMenuItem("Onderwijs");
		onderwijsproducten.add(new MenuItem(OndPrCollectieveAfnameStap1Page.class,
			DeelnemerCollectiefMenuItem.OnderwijsproductenToekennen));
		onderwijsproducten.add(new MenuItem(OndPrKeuzeAangevenStap1Page.class,
			DeelnemerCollectiefMenuItem.KeuzesAangeven));

		// Onderstaande pagina is niet af, dus nog niet in het menu.
		// onderwijsproducten.add(new
		// MenuItem(DeelnemerKeuzesControlerenOverzichtPage.class,
		// DeelnemerCollectiefMenuItem.KeuzesControleren));

		ret.add(onderwijsproducten);

		DropdownMenuItem examens = new DropdownMenuItem("Examens");
		examens.add(new MenuItem(DeelnemerKwalificatiePage.class,
			DeelnemerCollectiefMenuItem.ActieOverzicht));
		examens.add(new MenuItem(CriteriumbankControleOverzichtPage.class,
			DeelnemerCollectiefMenuItem.Criteriumbankcontrole));
		ret.add(examens);

		DropdownMenuItem collectief = new DropdownMenuItem("Collectief");
		collectief.add(new MenuItem(VerbintenisCollectiefEditOverzichtPage.class,
			DeelnemerCollectiefMenuItem.Verbintenissen));
		collectief.add(new MenuItem(BPVInschrijvingCollectiefEditOverzichtPage.class,
			DeelnemerCollectiefMenuItem.BPVs));
		collectief.add(new MenuItem(OrganisatieEenheidLocatieCollectiefWijzigenOverzichtPage.class,
			DeelnemerCollectiefMenuItem.OrgEhdLocatieWijzigen));
		collectief.add(new MenuItem(CollectiefAanmakenOverzichtPage.class,
			DeelnemerCollectiefMenuItem.Aanmaken));
		collectief.add(new MenuItem(CollectiefBronmutatieOverzichtPage.class,
			DeelnemerCollectiefMenuItem.BronMutatiesAanmaken));
		ret.add(collectief);

		return ret;
	}
}
