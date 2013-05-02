/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.dao.hibernate;

import java.util.List;

import nl.topicus.cobra.dao.DataAccessException;
import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.dao.hibernate.HibernateDataAccessHelper;
import nl.topicus.cobra.dao.hibernate.HibernateSessionProvider;
import nl.topicus.cobra.dao.hibernate.QueryInterceptor;
import nl.topicus.cobra.security.RechtenSoort;
import nl.topicus.cobra.util.Asserts;
import nl.topicus.eduarte.app.EduArteApp;
import nl.topicus.eduarte.app.EduArteContext;
import nl.topicus.eduarte.app.EduArteModuleKey;
import nl.topicus.eduarte.core.principals.App;
import nl.topicus.eduarte.core.principals.Root;
import nl.topicus.eduarte.dao.helpers.InstellingDataAccessHelper;
import nl.topicus.eduarte.dao.helpers.RolDataAccessHelper;
import nl.topicus.eduarte.entities.organisatie.Instelling;
import nl.topicus.eduarte.entities.personen.Medewerker;
import nl.topicus.eduarte.entities.personen.Persoon;
import nl.topicus.eduarte.entities.security.authentication.AccountRol;
import nl.topicus.eduarte.entities.security.authentication.BeheerderAccount;
import nl.topicus.eduarte.entities.security.authentication.MedewerkerAccount;
import nl.topicus.eduarte.entities.security.authorization.AuthorisatieNiveau;
import nl.topicus.eduarte.entities.security.authorization.Recht;
import nl.topicus.eduarte.entities.security.authorization.Rol;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

/**
 * @author marrink
 */
public class InstellingHibernateDataAccessHelper extends HibernateDataAccessHelper<Instelling>
		implements InstellingDataAccessHelper
{

	/**
	 * @param provider
	 */
	public InstellingHibernateDataAccessHelper(HibernateSessionProvider provider,
			QueryInterceptor interceptor)
	{
		super(provider, interceptor);
	}

	/**
	 * @see nl.topicus.eduarte.dao.helpers.InstellingDataAccessHelper#get(java.lang.Long)
	 */
	@Override
	public Instelling get(Long id)
	{
		return get(Instelling.class, id);
	}

	/**
	 * Dit is een batch operatie dus nog zelf committen.
	 * 
	 */
	@Override
	public void setup(Instelling instelling, String rootUsername, String rootPassword)
	{
		Asserts.assertNotNull("instelling", instelling);
		Asserts.assertNotNull("rootUsername", rootUsername);
		Asserts.assertNotNull("rootPassword", rootPassword);

		if (instelling.getId() != null)
			throw new DataAccessException("Alleen nieuwe instellingen worden ondersteund");
		instelling.save();

		Rol instellingRoot = new Rol("Standaard root");
		instellingRoot.setCategorie("Standaard instellingsrollen");
		instellingRoot.setAuthorisatieNiveau(AuthorisatieNiveau.SUPER);
		instellingRoot.setRechtenSoort(RechtenSoort.INSTELLING);
		instellingRoot.save();
		Recht rootRecht =
			new Recht(instellingRoot, Root.class,
				nl.topicus.eduarte.app.security.actions.Instelling.class);
		rootRecht.save();

		Rol instellingApp = new Rol("Standaard applicatiebeheerder");
		instellingApp.setCategorie("Standaard instellingsrollen");
		instellingApp.setAuthorisatieNiveau(AuthorisatieNiveau.APPLICATIE);
		instellingApp.setRechtenSoort(RechtenSoort.INSTELLING);
		instellingApp.save();
		Recht appRecht =
			new Recht(instellingApp, App.class,
				nl.topicus.eduarte.app.security.actions.Instelling.class);
		appRecht.save();

		Rol orgApp = new Rol("Organisatie-eenheidbeheerder");
		orgApp.setCategorie("Standaard instellingsrollen");
		orgApp.setAuthorisatieNiveau(AuthorisatieNiveau.APPLICATIE);
		orgApp.setRechtenSoort(RechtenSoort.INSTELLING);
		orgApp.save();
		Recht orgAppRecht =
			new Recht(orgApp, App.class,
				nl.topicus.eduarte.app.security.actions.OrganisatieEenheid.class);
		orgAppRecht.save();

		BeheerderAccount root = new BeheerderAccount();
		root.setGebruikersnaam(rootUsername);
		root.setWachtwoord(rootPassword);
		root.setActief(true);
		root.setAuthorisatieNiveau(AuthorisatieNiveau.SUPER);

		root.save();
		AccountRol newRol = new AccountRol();
		newRol.setAccount(root);
		newRol.setRol(instellingRoot);
		newRol.save();
		root.getRoles().add(newRol);

		root.flush();
	}

	@Override
	public void createAppAccount(String username, String password)
	{
		Persoon persoon = new Persoon();
		persoon.setAchternaam(username);
		persoon.save();

		Medewerker medewerker = new Medewerker();
		medewerker.setPersoon(persoon);
		medewerker.setAfkorting(username.length() > 10 ? username.substring(0, 10) : username);
		medewerker.save();

		MedewerkerAccount account = new MedewerkerAccount(medewerker);
		account.setGebruikersnaam(username);
		account.setWachtwoord(password);
		account.setActief(true);
		account.setAuthorisatieNiveau(AuthorisatieNiveau.APPLICATIE);
		List<Rol> appRollen =
			DataAccessRegistry.getHelper(RolDataAccessHelper.class).getRollen(
				AuthorisatieNiveau.APPLICATIE);
		account.save();
		for (Rol rol : appRollen)
		{
			AccountRol newRol = new AccountRol();
			newRol.setAccount(account);
			newRol.setRol(rol);
			newRol.save();
			account.getRoles().add(newRol);
		}
	}

	@Override
	public List<Instelling> list()
	{
		Criteria criteria = createCriteria(Instelling.class);
		criteria.add(Restrictions.eq("actief", Boolean.TRUE));
		criteria.addOrder(Order.asc("naam"));
		return cachedTypedList(criteria);
	}

	@Override
	public Instelling get(String naam)
	{
		Criteria criteria = createCriteria(Instelling.class);
		criteria.add(Restrictions.eq("actief", Boolean.TRUE));
		criteria.add(Restrictions.eq("naam", naam));

		return cachedTypedUnique(criteria);
	}

	@Override
	public Instelling getInstellingByBrinNr(String brinNr)
	{
		if (brinNr != null)
		{
			Criteria criteria = createCriteria(Instelling.class);
			criteria.createAlias("brincode", "brincode");
			criteria.add(Restrictions.eq("actief", Boolean.TRUE));
			criteria.add(Restrictions.eq("brincode.code", brinNr));

			return cachedTypedUnique(criteria);
		}
		return null;
	}

	@Override
	public String getApplicationTitle()
	{
		if (EduArteContext.get().getOrganisatie().doUnproxy() instanceof Instelling
			&& EduArteApp.get().isModuleActive(EduArteModuleKey.HOGER_ONDERWIJS))
			return "Alluris";

		if (EduArteApp.get().isModuleActive(EduArteModuleKey.GROEPS_AUTHORISATIE))
			return "EduArte";

		return "Tribe KRD";
	}

}
