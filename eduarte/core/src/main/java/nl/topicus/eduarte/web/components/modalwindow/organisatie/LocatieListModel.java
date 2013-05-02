package nl.topicus.eduarte.web.components.modalwindow.organisatie;

import java.util.Iterator;
import java.util.List;

import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.util.TimeUtil;
import nl.topicus.eduarte.dao.helpers.LocatieDataAccessHelper;
import nl.topicus.eduarte.entities.organisatie.Locatie;
import nl.topicus.eduarte.entities.organisatie.OrganisatieEenheid;

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;

/**
 * Model om alle locaties uit de database te halen. De locaties worden vergeleken met het
 * organisatieenheidmodel. Als de locatie voorkomt in het organisatieenheidmodel, dan
 * wordt deze uit de locatielist gehaald. Men krijgt dus een lijst terug met locaties die
 * nog niet aan een organisatie eenheid zijn gekoppeld.
 * 
 * @author vanharen
 */
public class LocatieListModel extends LoadableDetachableModel<List<Locatie>>
{

	private static final long serialVersionUID = 1L;

	private IModel<OrganisatieEenheid> orgEhdModel;

	public LocatieListModel(IModel<OrganisatieEenheid> orgEhdModel)
	{
		this.orgEhdModel = orgEhdModel;
	}

	@Override
	protected List<Locatie> load()
	{
		OrganisatieEenheid orgEhd = orgEhdModel.getObject();
		List<Locatie> locatieList =
			DataAccessRegistry.getHelper(LocatieDataAccessHelper.class).list(
				TimeUtil.getInstance().currentDate());
		Iterator<Locatie> it = locatieList.iterator();
		while (it.hasNext())
			if (orgEhd.isAlleenVerbondenAanHuidigeEenheid(it.next()))
				it.remove();

		return locatieList;
	}

	@Override
	public void detach()
	{
		super.detach();
		orgEhdModel.detach();
	}
}
