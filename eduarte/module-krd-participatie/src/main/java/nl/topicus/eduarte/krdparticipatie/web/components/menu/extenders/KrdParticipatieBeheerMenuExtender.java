package nl.topicus.eduarte.krdparticipatie.web.components.menu.extenders;

import java.util.ArrayList;
import java.util.List;

import nl.topicus.cobra.web.components.menu.AbstractMenuExtender;
import nl.topicus.cobra.web.components.menu.DropdownMenuItem;
import nl.topicus.cobra.web.components.menu.HorizontalSeperator;
import nl.topicus.cobra.web.components.menu.IMenuItem;
import nl.topicus.cobra.web.components.menu.MenuItem;
import nl.topicus.eduarte.krdparticipatie.web.components.menu.enums.ParticipatieBeheerMenuItem;
import nl.topicus.eduarte.krdparticipatie.web.pages.beheer.AbsentieRedenenPage;
import nl.topicus.eduarte.krdparticipatie.web.pages.beheer.IbgCertificaatUploadPage;
import nl.topicus.eduarte.krdparticipatie.web.pages.beheer.importeren.AbsentiemeldingenImporterenPage;
import nl.topicus.eduarte.krdparticipatie.web.pages.beheer.importeren.KRDWaarnemingenImporterenPage;
import nl.topicus.eduarte.krdparticipatie.web.pages.beheer.lesweek.LesweekIndelingKoppelingPage;
import nl.topicus.eduarte.krdparticipatie.web.pages.beheer.lesweek.LesweekindelingOverzichtPage;
import nl.topicus.eduarte.web.components.menu.BeheerMenu;

/**
 * @author loite
 */
public class KrdParticipatieBeheerMenuExtender extends AbstractMenuExtender<BeheerMenu>
{

	@Override
	public List<IMenuItem> getMenuExtension(BeheerMenu menu)
	{
		List<IMenuItem> ret = new ArrayList<IMenuItem>();
		DropdownMenuItem participatie = findSubmenu(menu, BeheerMenu.AANWEZIGHEIDMENUNAME);
		participatie.add(new MenuItem(AbsentiemeldingenImporterenPage.class,
			ParticipatieBeheerMenuItem.AbsentiemeldingenImporteren));
		participatie.add(new MenuItem(KRDWaarnemingenImporterenPage.class,
			ParticipatieBeheerMenuItem.KRDWaarnemingenImporteren));
		participatie.add(new HorizontalSeperator());
		participatie.add(new MenuItem(AbsentieRedenenPage.class,
			ParticipatieBeheerMenuItem.Absentieredenen));
		participatie.add(new MenuItem(LesweekindelingOverzichtPage.class,
			ParticipatieBeheerMenuItem.Lesweekindeling));
		participatie.add(new MenuItem(LesweekIndelingKoppelingPage.class,
			ParticipatieBeheerMenuItem.Lesweekkoppeling));
		participatie.add(new MenuItem(IbgCertificaatUploadPage.class,
			ParticipatieBeheerMenuItem.Verzuimloket));

		return ret;
	}
}
