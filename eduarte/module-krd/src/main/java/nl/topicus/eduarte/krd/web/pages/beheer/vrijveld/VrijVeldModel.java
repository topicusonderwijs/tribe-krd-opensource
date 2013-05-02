package nl.topicus.eduarte.krd.web.pages.beheer.vrijveld;

import java.util.Date;

import nl.topicus.cobra.modelsv2.DefaultModelManager;
import nl.topicus.cobra.modelsv2.ModelFactory;
import nl.topicus.eduarte.entities.vrijevelden.VrijVeld;
import nl.topicus.eduarte.entities.vrijevelden.VrijVeldKeuzeOptie;
import nl.topicus.eduarte.web.models.secure.AbstractEntiteitEditPageModel;

/**
 * Model om een nieuwe VrijVeld aan te maken of een bestaande te bewerken.
 * 
 * @author hoeve
 */
public class VrijVeldModel extends AbstractEntiteitEditPageModel<VrijVeld>
{
	private static final long serialVersionUID = 1L;

	/**
	 * Creert een model op basis van een bestaande VrijVeld.
	 */
	public VrijVeldModel(VrijVeld vrijveld)
	{
		super(new Date());

		entiteitModel =
			ModelFactory.getCompoundChangeRecordingModel(vrijveld, getEntiteitManager());
	}

	@Override
	protected void setEntiteitManager()
	{
		entiteitManager = new DefaultModelManager(VrijVeldKeuzeOptie.class, VrijVeld.class);
	}

	@Override
	protected VrijVeld createDefaultT()
	{
		VrijVeld vrijveld = new VrijVeld();
		vrijveld.setActief(true);

		return vrijveld;
	}

	public VrijVeld getVrijVeld()
	{
		return getObject();
	}

	@Override
	public void save()
	{
		getEntiteitModel().saveObject();
		getEntiteitModel().getObject().commit();
	}

	public void addVrijVeldKeuzeOptie(VrijVeldKeuzeOptie optie)
	{
		VrijVeld vrijveld = getVrijVeld();
		optie.setVrijVeld(vrijveld);
		vrijveld.getVrijVeldKeuzeOpties().add(optie);
	}

	public void deleteVrijVeldKeuzeOptie(VrijVeldKeuzeOptie vrijVeldKeuzeOptie)
	{
		getVrijVeld().getVrijVeldKeuzeOpties().remove(vrijVeldKeuzeOptie);
		vrijVeldKeuzeOptie.setVrijVeld(null);
	}
}
