/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.dao.hibernate;

import java.io.Serializable;
import java.util.Date;

import nl.topicus.cobra.dao.DataAccessHelper;
import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.dao.EncryptionProvider;
import nl.topicus.cobra.dao.hibernate.AbstractZoekFilterDataAccessHelper;
import nl.topicus.cobra.dao.hibernate.CriteriaBuilder;
import nl.topicus.cobra.dao.hibernate.HibernateSessionProvider;
import nl.topicus.cobra.dao.hibernate.QueryInterceptor;
import nl.topicus.cobra.util.Asserts;
import nl.topicus.cobra.util.Counter;
import nl.topicus.cobra.util.StringUtil;
import nl.topicus.cobra.util.TimeUtil;
import nl.topicus.eduarte.dao.helpers.AccountDataAccessHelper;
import nl.topicus.eduarte.dao.helpers.MedewerkerDataAccessHelper;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.entities.organisatie.Organisatie;
import nl.topicus.eduarte.entities.personen.Deelnemer;
import nl.topicus.eduarte.entities.personen.Medewerker;
import nl.topicus.eduarte.entities.personen.Persoon;
import nl.topicus.eduarte.entities.security.authentication.Account;
import nl.topicus.eduarte.entities.security.authentication.DeelnemerAccount;
import nl.topicus.eduarte.entities.security.authentication.MedewerkerAccount;
import nl.topicus.eduarte.zoekfilters.AccountZoekFilter;

import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Helper class for accounts.
 * 
 * @author marrink
 */
public class AccountHibernateDataAccessHelper extends
		AbstractZoekFilterDataAccessHelper<Account, AccountZoekFilter> implements
		AccountDataAccessHelper
{
	/**
	 * Dummy implementatie voor het encrypten van wachtwoorden en dergelijke, doet niets
	 * aan encryptie!
	 */
	private static final class EduArteEncryptionProvider implements EncryptionProvider
	{
		/**
		 * Returns the original input unencrypted.
		 * 
		 * @see nl.topicus.cobra.dao.EncryptionProvider#encrypt(java.lang.String)
		 */
		@Override
		public String encrypt(String raw)
		{
			return raw;
		}

		/**
		 * @see nl.topicus.cobra.dao.EncryptionProvider#decrypt(java.lang.String)
		 */
		@Override
		public String decrypt(String encrypted)
		{
			return encrypted;
		}
	}

	private EncryptionProvider encryptionProvider;

	// counter to generate unique names
	private static final Counter usernameCounter = new Counter(Counter.CountMode.Up);

	// This counter probably causes no problems in a clustered environment

	private final int usernameLength;

	/**
	 * logger.
	 */
	private static final Logger log =
		LoggerFactory.getLogger(AccountHibernateDataAccessHelper.class);

	/**
	 * Constructor.
	 * 
	 * @param sessionProvider
	 *            geeft hibernate sessies uit
	 * @param encryptionProvider
	 *            encrypt data
	 */
	public AccountHibernateDataAccessHelper(HibernateSessionProvider sessionProvider,
			EncryptionProvider encryptionProvider, QueryInterceptor interceptor)
	{
		super(sessionProvider, interceptor);
		if (encryptionProvider == null)
		{
			log.warn("KryptonEncryptionProvider wordt gebruikt!");
			this.encryptionProvider = new EduArteEncryptionProvider();
		}
		else
		{
			this.encryptionProvider = encryptionProvider;
		}
		log.info("Gebruikte encryption provider: " + this.encryptionProvider.getClass());
		usernameLength = propertyLength(Account.class, "gebruikersnaam");
	}

	@Override
	public Account authenticate(String username, String password)
	{
		String cryptedPassword = encrypt(password);
		Criteria criteria = createCriteria(Account.class);
		criteria.add(Restrictions.eq("gebruikersnaam", username));
		criteria.add(Restrictions.eq("encryptedWachtwoord", cryptedPassword));
		criteria.add(Restrictions.eq("actief", Boolean.TRUE));
		return checkIsValid(cachedTypedUnique(criteria));
	}

	/**
	 * Controleerd of de account geldig is (inloggen is toegestaan met deze account)
	 * 
	 * @param account
	 * @return account of null als de account niet valid is.
	 */
	private Account checkIsValid(Account account)
	{
		return account != null && account.isValid() ? account : null;
	}

	@Override
	public String encrypt(String raw)
	{
		return encryptionProvider.encrypt(raw);
	}

	@Override
	public String decrypt(String encrypted)
	{
		return encryptionProvider.decrypt(encrypted);
	}

	@Override
	public boolean usernameExists(String username)
	{
		Criteria criteria = createCriteria(Account.class);
		criteria.add(Restrictions.eq("gebruikersnaam", username));

		Account existingAccount = cachedTypedUnique(criteria);
		if (existingAccount == null)
			return false;
		return true;
	}

	@Override
	public boolean usernameExists(String username, Account account)
	{
		Asserts.assertNotNull("account", account);

		Criteria criteria = createCriteria(Account.class);
		criteria.add(Restrictions.eq("gebruikersnaam", username));
		if (account.getIdAsSerializable() != null)
			criteria.add(Restrictions.ne("id", account.getIdAsSerializable()));

		Account existingAccount = cachedTypedUnique(criteria);
		if (existingAccount == null)
			return false;
		return true;
	}

	@Override
	public String suggestUsername(String namebase, Organisatie organisatie)
	{
		String base = namebase;
		if (StringUtil.isEmpty(namebase))
			base = null;
		String suggestion = suggest(base + String.valueOf(usernameCounter.currentValue()));
		String last = suggestion;
		int counter = 0;
		while (counter < 10 && usernameExists(suggestion))
		{
			suggestion = suggest(base);
			if (last.equals(suggestion))
				suggestion = suggest(base + String.valueOf(usernameCounter.next()));
			last = suggestion;
			counter++; // to prevent continous loop
		}
		return suggestion;
	}

	private String suggest(String base)
	{
		int next = usernameCounter.currentValue();
		if (base == null)
			next = usernameCounter.next();
		return StringUtil.truncate(base == null ? String.valueOf(next) : base, usernameLength,
			base == null ? null : String.valueOf(next));
	}

	/**
	 * Same as super, only addition is that accounts are always unproxied before returning
	 * them.
	 * 
	 * @see DataAccessHelper#get(java.lang.Class, java.io.Serializable)
	 */
	@Override
	public <R extends Account> R get(Class<R> class1, Serializable id)
	{
		return unproxy(super.get(class1, id));
	}

	@Override
	public Account getAccount(String gebruikersnaam)
	{
		Criteria criteria = createCriteria(Account.class);
		criteria.add(Restrictions.eq("gebruikersnaam", gebruikersnaam));
		criteria.add(Restrictions.eq("actief", Boolean.TRUE));
		return cachedTypedUnique(criteria);
	}

	@Override
	public Account authenticate(String gebruikersnaam)
	{
		return checkIsValid(getAccount(gebruikersnaam));
	}

	@Override
	protected Criteria createCriteria(AccountZoekFilter filter)
	{
		Criteria criteria = createCriteria(filter.getType().getType());
		CriteriaBuilder builder = new CriteriaBuilder(criteria);
		builder.addEquals("actief", filter.getActief());
		if (filter.getAuthorisatieNiveau() != null)
			builder
				.addIn("authorisatieNiveau", filter.getAuthorisatieNiveau().sameNiveauAndLower());
		builder.addILikeFixedMatchMode("gebruikersnaam", filter.getGebruikersnaam(),
			MatchMode.ANYWHERE);
		if (filter.getRol() != null)
		{
			builder.createAlias("roles", "roles");
			builder.addEquals("roles.rol", filter.getRol());
		}
		return criteria;
	}

	/**
	 * @param medewerker
	 * @return Returns the MedewerkerAccount
	 */
	@Override
	public Account get(Medewerker medewerker)
	{
		Criteria criteria = createCriteria(MedewerkerAccount.class);
		criteria.add(Restrictions.eq("medewerker", medewerker));
		return cachedTypedUnique(criteria);
	}

	@Override
	public Account get(Deelnemer deelnemer)
	{
		Criteria criteria = createCriteria(DeelnemerAccount.class);
		criteria.add(Restrictions.eq("deelnemer", deelnemer));
		return cachedTypedUnique(criteria);
	}

	@Override
	public Account get(Persoon persoon)
	{
		Medewerker medewerker =
			DataAccessRegistry.getHelper(MedewerkerDataAccessHelper.class).get(persoon);
		if (medewerker != null)
		{
			Account ret = get(medewerker);
			if (ret != null)
				return ret;
		}
		Deelnemer deelnemer = persoon.getDeelnemer();
		if (deelnemer != null)
		{
			Account ret = get(deelnemer);
			if (ret != null)
				return ret;
		}
		return null;
	}

	@Override
	public DeelnemerAccount authenticateDeelnemer(String gebruikersnaam, String wachtwoord)
	{
		String cryptedPassword = encrypt(wachtwoord);
		Criteria criteria = createCriteria(DeelnemerAccount.class);
		criteria.add(Restrictions.eq("gebruikersnaam", gebruikersnaam));
		criteria.add(Restrictions.eq("encryptedWachtwoord", cryptedPassword));
		criteria.add(Restrictions.eq("actief", Boolean.TRUE));
		return checkActiveDeelnemer((DeelnemerAccount) uncachedUnique(criteria));
	}

	private DeelnemerAccount checkActiveDeelnemer(DeelnemerAccount account)
	{
		if (account != null)
		{
			Date currentDate = TimeUtil.getInstance().currentDate();
			for (Verbintenis inschrijving : account.getDeelnemer().getVerbintenissen())
			{
				if (inschrijving.isActief(currentDate))
				{
					return account;
				}
			}
		}
		return null;
	}

}
