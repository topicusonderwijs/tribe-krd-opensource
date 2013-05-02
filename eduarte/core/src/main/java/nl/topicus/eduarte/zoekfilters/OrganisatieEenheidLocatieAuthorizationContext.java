package nl.topicus.eduarte.zoekfilters;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.dao.hibernate.AuthorizationContext;
import nl.topicus.cobra.util.Asserts;
import nl.topicus.cobra.util.TimeUtil;
import nl.topicus.eduarte.app.security.models.IOrganisatieEenhedenLocatiesModel;
import nl.topicus.eduarte.app.security.models.OrganisatieEenhedenLocatiesModel;
import nl.topicus.eduarte.dao.helpers.OrganisatieEenheidDataAccessHelper;
import nl.topicus.eduarte.entities.organisatie.IOrganisatieEenheidLocatieKoppelEntiteit;
import nl.topicus.eduarte.entities.organisatie.OrganisatieEenheid;
import nl.topicus.eduarte.entities.organisatie.OrganisatieEenheidLocatie;

import org.apache.wicket.Component;
import org.apache.wicket.model.IDetachable;
import org.apache.wicket.security.checks.ComponentSecurityCheck;
import org.apache.wicket.security.checks.ISecurityCheck;

/**
 * Autorisatiecontext specifiek voor de combinatie Organisatie-eenheid - Locatie.
 * 
 * @author loite
 */
public class OrganisatieEenheidLocatieAuthorizationContext implements
		AuthorizationContext<IOrganisatieEenheidLocatieKoppelEntiteit< ? >>, IDetachable
{
	private static final long serialVersionUID = 1L;

	private IOrganisatieEenhedenLocatiesModel model;

	public OrganisatieEenheidLocatieAuthorizationContext(ISecurityCheck check)
	{
		model = new OrganisatieEenhedenLocatiesModel(check);
	}

	public OrganisatieEenheidLocatieAuthorizationContext(Component component)
	{
		model = new OrganisatieEenhedenLocatiesModel(new ComponentSecurityCheck(component));
	}

	public OrganisatieEenheidLocatieAuthorizationContext(IOrganisatieEenhedenLocatiesModel model)
	{
		this.model = model;
	}

	public Set<OrganisatieEenheid> getToegestaneOrganisatieEenheden()
	{
		Set<OrganisatieEenheid> res = new HashSet<OrganisatieEenheid>();
		if (isInstellingClearance())
		{
			res.addAll(DataAccessRegistry.getHelper(OrganisatieEenheidDataAccessHelper.class)
				.list());
			return res;
		}
		Date currentDate = TimeUtil.getInstance().currentDate();
		for (IOrganisatieEenheidLocatieKoppelEntiteit< ? > om : getAllowedElements())
		{
			if (om.isActief(currentDate))
				res.add(om.getOrganisatieEenheid());
		}
		return res;
	}

	public boolean isLocatieLoosAuthorisatie(OrganisatieEenheid organisatieEenheid)
	{
		if (isInstellingClearance())
			return true;

		Date currentDate = TimeUtil.getInstance().currentDate();
		for (IOrganisatieEenheidLocatieKoppelEntiteit< ? > om : getAllowedElements())
		{
			if (om.isActief(currentDate) && om.getLocatie() == null
				&& om.getOrganisatieEenheid().isParentOf(organisatieEenheid))
				return true;
		}
		return false;
	}

	public Set<OrganisatieEenheidLocatie> getToegestaneLocaties(
			OrganisatieEenheid organisatieEenheid)
	{
		Asserts.assertNotNull("organisatieEenheid", organisatieEenheid);
		Set<OrganisatieEenheidLocatie> res = new HashSet<OrganisatieEenheidLocatie>();
		if (isInstellingClearance())
		{
			res.addAll(organisatieEenheid.getAlleLocaties());
			return res;
		}
		Date currentDate = TimeUtil.getInstance().currentDate();
		for (IOrganisatieEenheidLocatieKoppelEntiteit< ? > om : getAllowedElements())
		{
			if (om.isActief(currentDate)
				&& om.getOrganisatieEenheid().isParentOf(organisatieEenheid))
			{
				OrganisatieEenheidLocatie curLocatie =
					organisatieEenheid.getLocatie(om.getLocatie());
				if (curLocatie != null)
					res.add(curLocatie);
			}
		}
		return res;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<IOrganisatieEenheidLocatieKoppelEntiteit< ? >> getAllowedElements()
	{
		return (List<IOrganisatieEenheidLocatieKoppelEntiteit< ? >>) model.getObject();
	}

	@Override
	public boolean isInstellingClearance()
	{
		return model.isInstellingClearance();
	}

	@Override
	public boolean isOrganisatieEenheidClearance()
	{
		return model.isOrganisatieEenheidClearance();
	}

	@Override
	public void detach()
	{
		model.detach();
	}
}
