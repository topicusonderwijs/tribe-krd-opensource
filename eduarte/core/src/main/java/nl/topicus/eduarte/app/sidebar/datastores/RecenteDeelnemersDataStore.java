package nl.topicus.eduarte.app.sidebar.datastores;

import java.util.ArrayList;
import java.util.List;

import nl.topicus.cobra.modelsv2.HibernateObjectListModel;
import nl.topicus.cobra.util.ComponentUtil;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.web.pages.SecurePage;
import nl.topicus.eduarte.web.pages.deelnemer.AbstractDeelnemerPage;

/**
 * Datastore voor de sidebar met de meest recente deelnemers.
 * 
 * @author loite
 */
public class RecenteDeelnemersDataStore extends AbstractSideBarDataStore
{
	private static final long serialVersionUID = 1L;

	private static final int STACK_SIZE = 5;

	private final List<Class< ? extends AbstractDeelnemerPage>> pageClasses =
		new ArrayList<Class< ? extends AbstractDeelnemerPage>>(STACK_SIZE);

	private final HibernateObjectListModel<List<Verbintenis>, Verbintenis> inschrijvingenModel =
		new HibernateObjectListModel<List<Verbintenis>, Verbintenis>(new ArrayList<Verbintenis>());

	public RecenteDeelnemersDataStore()
	{
	}

	public List<Verbintenis> getInschrijvingen()
	{
		return inschrijvingenModel.getObject();
	}

	@Override
	public void onBeforeRender(SecurePage page)
	{
		if (page instanceof AbstractDeelnemerPage)
		{
			AbstractDeelnemerPage deelnemerPage = (AbstractDeelnemerPage) page;
			Verbintenis inschrijving = deelnemerPage.getContextVerbintenis();
			if (inschrijving != null && page.supportsBookmarks())
			{
				ArrayList<Verbintenis> inschrijvingen =
					(ArrayList<Verbintenis>) inschrijvingenModel.getObject();

				if (inschrijvingen.size() == STACK_SIZE)
				{
					inschrijvingen.remove(STACK_SIZE - 1);
				}
				int index = 0;
				while (index < inschrijvingen.size())
				{
					Verbintenis bestaand = inschrijvingen.get(index);
					if (bestaand == null || bestaand.getDeelnemer() == null)
					{
						inschrijvingen.remove(index);
					}
					else if (bestaand.getDeelnemer().equals(inschrijving.getDeelnemer()))
					{
						inschrijvingen.remove(index);
					}
					else
					{
						index++;
					}
				}
				inschrijvingen.add(0, inschrijving);
				pageClasses.add(0, deelnemerPage.getClass());

				inschrijvingenModel.detach();
			}
		}
	}

	public void detach()
	{
		ComponentUtil.detachQuietly(inschrijvingenModel);
	}

	public HibernateObjectListModel<List<Verbintenis>, Verbintenis> getInschrijvingenModel()
	{
		return inschrijvingenModel;
	}

	public Class< ? extends AbstractDeelnemerPage> getDeelnemerPageClass(int index)
	{
		return pageClasses.get(index);
	}

}
