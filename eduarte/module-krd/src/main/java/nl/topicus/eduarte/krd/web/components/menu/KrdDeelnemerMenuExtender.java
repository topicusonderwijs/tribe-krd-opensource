package nl.topicus.eduarte.krd.web.components.menu;

import java.util.ArrayList;
import java.util.List;

import nl.topicus.cobra.web.components.menu.AbstractMenuExtender;
import nl.topicus.cobra.web.components.menu.DropdownMenuItem;
import nl.topicus.cobra.web.components.menu.IMenuItem;
import nl.topicus.cobra.web.components.menu.MenuItem;
import nl.topicus.eduarte.krd.web.pages.beheer.bron.overzichten.DeelnemerBronPage;
import nl.topicus.eduarte.krd.web.pages.deelnemer.examens.individueel.BereikbareDiplomasPage;
import nl.topicus.eduarte.krd.web.pages.deelnemer.examens.individueel.DeelnemerExamenPage;
import nl.topicus.eduarte.web.components.menu.DeelnemerMenu;
import nl.topicus.eduarte.web.components.menu.DeelnemerMenuItem;
import nl.topicus.eduarte.web.pages.deelnemer.onderwijs.DeelnemerAfgenomenOnderwijsproductenPage;
import nl.topicus.eduarte.web.pages.deelnemer.onderwijs.DeelnemerProductregelsPage;
import nl.topicus.eduarte.web.pages.deelnemer.resultaten.DeelnemerResultatenboomPage;
import nl.topicus.eduarte.web.pages.deelnemer.resultaten.DeelnemerResultatenmatrixPage;

public class KrdDeelnemerMenuExtender extends AbstractMenuExtender<DeelnemerMenu>
{
	private static final long serialVersionUID = 1L;

	@Override
	public List<IMenuItem> getMenuExtension(DeelnemerMenu menu)
	{
		List<IMenuItem> res = new ArrayList<IMenuItem>();
		DropdownMenuItem examenDrop = new DropdownMenuItem("Examens");
		res.add(examenDrop);
		examenDrop.add(new MenuItem(menu.createPageLink(DeelnemerExamenPage.class),
			DeelnemerMenuItem.Examens));
		examenDrop.add(new MenuItem(menu.createPageLink(BereikbareDiplomasPage.class),
			DeelnemerMenuItem.BereikbareDiplomas));

		// Onderwijs
		if (menu.getInschrijving() != null && menu.getInschrijving().getCohort() != null)
		{
			DropdownMenuItem dropdownOnderwijs = new DropdownMenuItem("Onderwijs");
			dropdownOnderwijs.add(new MenuItem(menu
				.createPageLink(DeelnemerAfgenomenOnderwijsproductenPage.class),
				DeelnemerMenuItem.AfgOnderwijsproducten));

			if (menu.getInschrijving().getOpleiding() != null)
				dropdownOnderwijs.add(new MenuItem(menu
					.createPageLink(DeelnemerProductregelsPage.class),
					DeelnemerMenuItem.Productregels));
			menu.addItem(dropdownOnderwijs);
		}

		// Resultaten
		DropdownMenuItem dropdownResultaten = new DropdownMenuItem(DeelnemerMenu.RESULTATEN_MENU);
		dropdownResultaten.add(new MenuItem(menu.createPageLink(DeelnemerResultatenboomPage.class),
			DeelnemerMenuItem.Resultatenboom));
		dropdownResultaten.add(new MenuItem(menu
			.createPageLink(DeelnemerResultatenmatrixPage.class),
			DeelnemerMenuItem.Resultatenmatrix));
		menu.addItem(dropdownResultaten);

		MenuItem bron =
			new MenuItem(menu.createPageLink(DeelnemerBronPage.class), DeelnemerMenuItem.Bron);
		res.add(bron);

		return res;
	}
}