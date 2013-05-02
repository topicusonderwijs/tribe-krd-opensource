package nl.topicus.eduarte.krdparticipatie.web.pages.beheer.lesweek;

import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.modelsv2.DefaultModelManager;
import nl.topicus.cobra.modelsv2.ModelFactory;
import nl.topicus.cobra.util.ComponentUtil;
import nl.topicus.cobra.util.JavaUtil;
import nl.topicus.eduarte.dao.participatie.helpers.LesweekindelingOrganisatieEenheidLocatieDataAccesHelper;
import nl.topicus.eduarte.entities.organisatie.Locatie;
import nl.topicus.eduarte.entities.organisatie.OrganisatieEenheid;
import nl.topicus.eduarte.entities.participatie.LesweekIndeling;
import nl.topicus.eduarte.entities.participatie.LesweekIndelingOrganisatieEenheidLocatie;

import org.apache.wicket.model.IDetachable;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

public class LesweekKoppelingWrapper implements IDetachable
{

	private static final long serialVersionUID = 1L;

	private IModel<OrganisatieEenheid> orgEenheidModel;

	private IModel<Locatie> locatieModel;

	private IModel<LesweekIndelingOrganisatieEenheidLocatie> lesweekindelingOrganisatieEenheidLocatieModel =
		new Model<LesweekIndelingOrganisatieEenheidLocatie>();

	public LesweekKoppelingWrapper(IModel<OrganisatieEenheid> orgEenheidModel)
	{
		this(orgEenheidModel, null);
	}

	public LesweekKoppelingWrapper(IModel<OrganisatieEenheid> orgEenheidModel,
			IModel<Locatie> locatieModel)
	{
		this.orgEenheidModel = orgEenheidModel;
		this.locatieModel = locatieModel;
	}

	@Override
	public String toString()
	{
		if (orgEenheidModel.getObject() != null && locatieModel == null)
			return (orgEenheidModel.getObject()).toString();

		else if (locatieModel.getObject() != null)
		{
			Locatie locatie = locatieModel.getObject();
			return locatie.toString();
		}

		else
			return "Object is geen OrganisatieEenheid of OrganisatieEenheidLocatie";
	}

	public LesweekIndeling getLesweekindeling()
	{

		LesweekIndelingOrganisatieEenheidLocatie LesOrgLoc = null;
		if (orgEenheidModel.getObject() != null && locatieModel == null)
		{
			LesOrgLoc =
				(DataAccessRegistry
					.getHelper(LesweekindelingOrganisatieEenheidLocatieDataAccesHelper.class)
					.getOrganisatieEenheidLocatie(orgEenheidModel.getObject(), null));
		}
		else if (locatieModel.getObject() != null)
		{
			LesOrgLoc =
				(DataAccessRegistry
					.getHelper(LesweekindelingOrganisatieEenheidLocatieDataAccesHelper.class)
					.getOrganisatieEenheidLocatie(orgEenheidModel.getObject(), locatieModel
						.getObject()));
		}

		if (LesOrgLoc != null)
		{
			return LesOrgLoc.getLesweekndelingVanObject();
		}

		return null;
	}

	public void setLesweekindeling(LesweekIndeling lesweekIndeling)
	{
		LesweekIndelingOrganisatieEenheidLocatie lesOrgLoc = null;

		if (orgEenheidModel.getObject() != null && locatieModel == null)
		{
			lesOrgLoc =
				(DataAccessRegistry
					.getHelper(LesweekindelingOrganisatieEenheidLocatieDataAccesHelper.class)
					.getOrganisatieEenheidLocatie(orgEenheidModel.getObject(), null));

			if (lesweekIndeling == null && lesOrgLoc != null)
			{
				lesOrgLoc.delete();
				lesOrgLoc.commit();
			}

			else if (lesOrgLoc == null)
			{
				lesOrgLoc =
					new LesweekIndelingOrganisatieEenheidLocatie(orgEenheidModel.getObject(), null);
			}
			lesOrgLoc.setLesweekIndeling(lesweekIndeling);

		}
		else if (locatieModel.getObject() != null)
		{
			lesOrgLoc =
				(DataAccessRegistry
					.getHelper(LesweekindelingOrganisatieEenheidLocatieDataAccesHelper.class)
					.getOrganisatieEenheidLocatie(orgEenheidModel.getObject(), locatieModel
						.getObject()));

			if (lesweekIndeling == null && lesOrgLoc != null)
			{
				lesOrgLoc.delete();
				lesOrgLoc.commit();
			}

			else if (lesOrgLoc == null)
			{
				lesOrgLoc =
					new LesweekIndelingOrganisatieEenheidLocatie(orgEenheidModel.getObject(),
						locatieModel.getObject());
			}
			lesOrgLoc.setLesweekIndeling(lesweekIndeling);
		}

		lesweekindelingOrganisatieEenheidLocatieModel =
			ModelFactory.getModel(lesOrgLoc, new DefaultModelManager(
				LesweekIndelingOrganisatieEenheidLocatie.class));

		this.save();
	}

	@Override
	public boolean equals(Object obj)
	{
		if (obj instanceof LesweekKoppelingWrapper)
		{

			LesweekKoppelingWrapper oldWrapper = (LesweekKoppelingWrapper) obj;
			if (oldWrapper.getOrgEenheidModel().getObject() == orgEenheidModel.getObject())
			{
				if (JavaUtil.equalsOrBothNull(locatieModel, oldWrapper.getLocatieModel()))
				{
					return true;
				}
			}
		}
		return false;
	}

	public void save()
	{
		if (lesweekindelingOrganisatieEenheidLocatieModel.getObject() != null)
		{
			LesweekIndelingOrganisatieEenheidLocatie LesOrgLoc =
				(lesweekindelingOrganisatieEenheidLocatieModel.getObject());
			if (LesOrgLoc.getLesweekIndeling() != null)
			{
				LesOrgLoc.saveOrUpdate();
				LesOrgLoc.commit();
			}
		}
	}

	@Override
	public int hashCode()
	{
		int hash = getOrgEenheidModel().getObject().hashCode();
		if (getLocatieModel() != null && getLocatieModel().getObject() != null)
			hash = hash ^ getLocatieModel().getObject().hashCode();
		return hash;
	}

	@Override
	public void detach()
	{
		ComponentUtil.detachQuietly(orgEenheidModel);
		ComponentUtil.detachQuietly(locatieModel);
		ComponentUtil.detachQuietly(lesweekindelingOrganisatieEenheidLocatieModel);
	}

	public IModel<OrganisatieEenheid> getOrgEenheidModel()
	{
		return orgEenheidModel;
	}

	public IModel<Locatie> getLocatieModel()
	{
		return locatieModel;
	}
}
