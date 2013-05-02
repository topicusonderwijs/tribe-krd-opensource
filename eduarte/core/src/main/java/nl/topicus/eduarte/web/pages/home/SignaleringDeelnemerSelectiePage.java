package nl.topicus.eduarte.web.pages.home;

import java.util.HashSet;
import java.util.Set;

import nl.topicus.cobra.dao.BatchDataAccessHelper;
import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.dao.helpers.DatabaseSelection;
import nl.topicus.cobra.dao.hibernate.ProjectedSelection;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.web.components.datapanel.selection.ISelectionComponent;
import nl.topicus.cobra.web.components.panels.bottomrow.AbstractBottomRowButton;
import nl.topicus.cobra.web.components.panels.bottomrow.AnnulerenButton;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.eduarte.core.principals.Always;
import nl.topicus.eduarte.dao.helpers.EventAbonnementSettingDataAccessHelper;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.entities.personen.Deelnemer;
import nl.topicus.eduarte.entities.signalering.settings.MedewerkerDeelnemerAbonnering;
import nl.topicus.eduarte.web.components.menu.HomeMenuItem;
import nl.topicus.eduarte.web.pages.shared.AbstractDeelnemerSelectiePage;
import nl.topicus.eduarte.web.pages.shared.AbstractSelectieTarget;
import nl.topicus.eduarte.web.pages.shared.HomePageContext;
import nl.topicus.eduarte.zoekfilters.VerbintenisZoekFilter;

import org.apache.wicket.markup.html.link.Link;

@InPrincipal(Always.class)
public class SignaleringDeelnemerSelectiePage extends AbstractDeelnemerSelectiePage<Deelnemer>
{
	private static final long serialVersionUID = 1L;

	public SignaleringDeelnemerSelectiePage()
	{
		super(SignaleringDeelnemerSelectiePage.class, new HomePageContext("Signaleringsdeelnemers",
			HomeMenuItem.Signaleringsdeelnemers), VerbintenisZoekFilter.getDefaultFilter(),
			createSelection(), new AbstractSelectieTarget<Deelnemer, Verbintenis>(
				SignaleringDeelnemerSelectiePage.class, "Opslaan")
			{
				private static final long serialVersionUID = 1L;

				@Override
				public Link<Void> createLink(String linkId,
						final ISelectionComponent<Deelnemer, Verbintenis> base)
				{
					return new Link<Void>(linkId)
					{
						private static final long serialVersionUID = 1L;

						@Override
						public void onClick()
						{
							saveSelection(new HashSet<Deelnemer>(base.getSelectedElements()));
						}
					};
				}
			});
	}

	private static DatabaseSelection<Deelnemer, Verbintenis> createSelection()
	{
		ProjectedSelection<Deelnemer, Verbintenis> ret =
			new ProjectedSelection<Deelnemer, Verbintenis>(Verbintenis.class, "deelnemer");
		for (MedewerkerDeelnemerAbonnering curAbonnering : DataAccessRegistry.getHelper(
			EventAbonnementSettingDataAccessHelper.class).getDeelnemerAbonnementen(
			getIngelogdeMedewerker()))
		{
			ret.add(curAbonnering.getDeelnemer().getVerbintenissen().get(0));
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

	private static void saveSelection(Set<Deelnemer> selection)
	{
		EventAbonnementSettingDataAccessHelper helper =
			DataAccessRegistry.getHelper(EventAbonnementSettingDataAccessHelper.class);
		for (MedewerkerDeelnemerAbonnering curAbonnering : helper
			.getDeelnemerAbonnementen(getIngelogdeMedewerker()))
		{
			if (!selection.contains(curAbonnering.getDeelnemer()))
			{
				curAbonnering.delete();
			}
			selection.remove(curAbonnering.getDeelnemer());
		}
		for (Deelnemer curDeelnemer : selection)
		{
			MedewerkerDeelnemerAbonnering newAbonnering = new MedewerkerDeelnemerAbonnering();
			newAbonnering.setMedewerker(getIngelogdeMedewerker());
			newAbonnering.setDeelnemer(curDeelnemer);
			newAbonnering.save();
		}
		DataAccessRegistry.getHelper(BatchDataAccessHelper.class).batchExecute();
	}

	@Override
	protected AbstractBottomRowButton createTerugButton(BottomRowPanel panel)
	{
		return new AnnulerenButton(panel, SignaleringDeelnemerSelectiePage.class);
	}
}
