package nl.topicus.eduarte.krdparticipatie.web.components.menu.extenders;

import java.util.ArrayList;
import java.util.List;

import nl.topicus.cobra.web.components.menu.DropdownMenuItem;
import nl.topicus.cobra.web.components.menu.HorizontalSeperator;
import nl.topicus.cobra.web.components.menu.IMenuItem;
import nl.topicus.cobra.web.components.menu.MenuExtender;
import nl.topicus.cobra.web.components.menu.MenuItem;
import nl.topicus.eduarte.krdparticipatie.web.components.menu.enums.ParticipatieDeelnemerMenuItem;
import nl.topicus.eduarte.krdparticipatie.web.pages.deelnemer.dossier.DeelnemerAbsentiemeldingenPage;
import nl.topicus.eduarte.krdparticipatie.web.pages.deelnemer.dossier.DeelnemerBudgettenPage;
import nl.topicus.eduarte.krdparticipatie.web.pages.deelnemer.dossier.DeelnemerJaarOverzichtPage;
import nl.topicus.eduarte.krdparticipatie.web.pages.deelnemer.dossier.DeelnemerWaarnemingenPage;
import nl.topicus.eduarte.krdparticipatie.web.pages.deelnemer.dossier.ParticipatieMaandOverzichtPage;
import nl.topicus.eduarte.krdparticipatie.web.pages.deelnemer.dossier.ParticipatieWeekOverzichtPage;
import nl.topicus.eduarte.krdparticipatie.web.pages.deelnemer.rapportage.DeelnemerActiviteitWaarnemingTotalenPage;
import nl.topicus.eduarte.krdparticipatie.web.pages.verzuimloket.DeelnemerVerzuimloketPage;
import nl.topicus.eduarte.web.components.menu.DeelnemerMenu;

/**
 * @author loite
 */
public class KrdParticipatieDeelnemerMenuExtender implements MenuExtender<DeelnemerMenu>
{

	@Override
	public List<IMenuItem> getMenuExtension(DeelnemerMenu menu)
	{

		List<IMenuItem> ret = new ArrayList<IMenuItem>();
		DropdownMenuItem participatie = menu.createDropdown("Aanwezigheid");
		ret.add(participatie);
		participatie.add(new MenuItem(menu.createPageLink(DeelnemerAbsentiemeldingenPage.class),
			ParticipatieDeelnemerMenuItem.Absentiemeldingen));
		participatie.add(new MenuItem(menu.createPageLink(DeelnemerWaarnemingenPage.class),
			ParticipatieDeelnemerMenuItem.Waarnemingen));
		participatie.add(new MenuItem(menu.createPageLink(DeelnemerBudgettenPage.class),
			ParticipatieDeelnemerMenuItem.Budgetten));
		participatie.add(new HorizontalSeperator());
		participatie.add(new MenuItem(menu.createPageLink(ParticipatieWeekOverzichtPage.class),
			ParticipatieDeelnemerMenuItem.Weekoverzicht));
		participatie.add(new MenuItem(menu.createPageLink(ParticipatieMaandOverzichtPage.class),
			ParticipatieDeelnemerMenuItem.Maandoverzicht));
		participatie.add(new MenuItem(menu.createPageLink(DeelnemerJaarOverzichtPage.class),
			ParticipatieDeelnemerMenuItem.Jaaroverzicht));
		participatie.add(new MenuItem(menu
			.createPageLink(DeelnemerActiviteitWaarnemingTotalenPage.class),
			ParticipatieDeelnemerMenuItem.OnderwijsproductTotalen));
		participatie.add(new HorizontalSeperator());
		participatie.add(new MenuItem(menu.createPageLink(DeelnemerVerzuimloketPage.class),
			ParticipatieDeelnemerMenuItem.Verzuimloket));

		return ret;
	}
}
