package nl.topicus.eduarte.web.pages.home;

import java.util.HashSet;
import java.util.Set;

import nl.topicus.cobra.dao.BatchDataAccessHelper;
import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.dao.helpers.DatabaseSelection;
import nl.topicus.cobra.dao.hibernate.HibernateSelection;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.web.components.datapanel.selection.ISelectionComponent;
import nl.topicus.cobra.web.components.panels.bottomrow.AbstractBottomRowButton;
import nl.topicus.cobra.web.components.panels.bottomrow.AnnulerenButton;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.eduarte.core.principals.Always;
import nl.topicus.eduarte.dao.helpers.EventAbonnementSettingDataAccessHelper;
import nl.topicus.eduarte.entities.groep.Groep;
import nl.topicus.eduarte.entities.signalering.settings.MedewerkerGroepAbonnering;
import nl.topicus.eduarte.web.components.menu.HomeMenuItem;
import nl.topicus.eduarte.web.pages.shared.AbstractGroepSelectiePage;
import nl.topicus.eduarte.web.pages.shared.AbstractSelectieTarget;
import nl.topicus.eduarte.web.pages.shared.HomePageContext;
import nl.topicus.eduarte.zoekfilters.GroepZoekFilter;

import org.apache.wicket.markup.html.link.Link;

@InPrincipal(Always.class)
public class SignaleringGroepenSelectiePage extends AbstractGroepSelectiePage<Groep>
{
	private static final long serialVersionUID = 1L;

	public SignaleringGroepenSelectiePage()
	{
		super(SignaleringGroepenSelectiePage.class, new HomePageContext("Signaleringsgroepen",
			HomeMenuItem.Signaleringsgroepen), GroepZoekFilter.createDefaultFilter(),
			createSelection(), new AbstractSelectieTarget<Groep, Groep>(
				SignaleringGroepenSelectiePage.class, "Opslaan")
			{
				private static final long serialVersionUID = 1L;

				@Override
				public Link<Void> createLink(String linkId,
						final ISelectionComponent<Groep, Groep> base)
				{
					return new Link<Void>(linkId)
					{
						private static final long serialVersionUID = 1L;

						@Override
						public void onClick()
						{
							saveSelection(new HashSet<Groep>(base.getSelectedElements()));
						}
					};
				}
			});
	}

	private static DatabaseSelection<Groep, Groep> createSelection()
	{
		HibernateSelection<Groep> ret = new HibernateSelection<Groep>(Groep.class);
		for (MedewerkerGroepAbonnering curAbonnering : DataAccessRegistry.getHelper(
			EventAbonnementSettingDataAccessHelper.class).getGroepAbonnementen(
			getIngelogdeMedewerker()))
		{
			ret.add(curAbonnering.getGroep());
		}
		return ret;
	}

	@Override
	public int getMaxResults()
	{
		return Integer.MAX_VALUE;
	}

	@Override
	public boolean allowEmptySelection()
	{
		return true;
	}

	private static void saveSelection(Set<Groep> selection)
	{
		EventAbonnementSettingDataAccessHelper helper =
			DataAccessRegistry.getHelper(EventAbonnementSettingDataAccessHelper.class);
		for (MedewerkerGroepAbonnering curAbonnering : helper
			.getGroepAbonnementen(getIngelogdeMedewerker()))
		{
			if (!selection.contains(curAbonnering.getGroep()))
			{
				curAbonnering.delete();
			}
			selection.remove(curAbonnering.getGroep());
		}
		for (Groep curGroep : selection)
		{
			MedewerkerGroepAbonnering newAbonnering = new MedewerkerGroepAbonnering();
			newAbonnering.setMedewerker(getIngelogdeMedewerker());
			newAbonnering.setGroep(curGroep);
			newAbonnering.save();
		}
		DataAccessRegistry.getHelper(BatchDataAccessHelper.class).batchExecute();
	}

	@Override
	protected AbstractBottomRowButton createTerugButton(BottomRowPanel panel)
	{
		return new AnnulerenButton(panel, SignaleringGroepenSelectiePage.class);
	}
}
