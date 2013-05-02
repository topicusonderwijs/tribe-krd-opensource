package nl.topicus.eduarte.app;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import nl.topicus.cobra.dao.BatchDataAccessHelper;
import nl.topicus.cobra.dao.DataAccessException;
import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.dao.helpers.SessionDataAccessHelper;
import nl.topicus.cobra.entities.IdObject;
import nl.topicus.cobra.util.TimeUtil;
import nl.topicus.eduarte.dao.helpers.AccountDataAccessHelper;
import nl.topicus.eduarte.dao.helpers.OrganisatieEenheidDataAccessHelper;
import nl.topicus.eduarte.entities.organisatie.IOrganisatieEenheidLocatieKoppelbaarEntiteit;
import nl.topicus.eduarte.entities.organisatie.Instelling;
import nl.topicus.eduarte.entities.organisatie.Locatie;
import nl.topicus.eduarte.entities.organisatie.Organisatie;
import nl.topicus.eduarte.entities.organisatie.OrganisatieEenheid;
import nl.topicus.eduarte.entities.personen.Deelnemer;
import nl.topicus.eduarte.entities.personen.Medewerker;
import nl.topicus.eduarte.entities.personen.Persoon;
import nl.topicus.eduarte.entities.security.authentication.Account;
import nl.topicus.eduarte.util.OrganisatieEenheidLocatieUtil;

import org.apache.wicket.model.IModel;

public class EduArteContext
{
	/**
	 * ThreadLocal met de context voor deze thread
	 */
	private static final ThreadLocal<EduArteContext> context = new ThreadLocal<EduArteContext>()
	{
		@Override
		protected EduArteContext initialValue()
		{
			return new EduArteContext();
		}
	};

	public static EduArteContext get()
	{
		return context.get();
	}

	/**
	 * Maakt de thread local voor het vasthouden van het huidige context (zoals
	 * peildatumodel en organisatie) leeg. <strong>NB. DEZE METHODE MOET ALTIJD
	 * AANGEROEPEN WORDEN BIJ HET OPRUIMEN VAN DE HUIDIGE THREAD IN EEN FINALLY BLOCK ALS
	 * CONTEXT GEZET IS</strong>
	 */
	public static void clearContext()
	{
		context.remove();
	}

	private IModel<Date> peildatumModel;

	private Organisatie organisatie;

	private Account account;

	private EduArteContext()
	{
	}

	private boolean isAttached(IdObject object)
	{
		try
		{
			if (!DataAccessRegistry.isInitialized() || object == null)
				return true;
			return DataAccessRegistry.getHelper(SessionDataAccessHelper.class)
				.getHibernateSessionProvider().getSession().contains(object);
		}
		catch (DataAccessException e)
		{
			// dit is in een testcase, waar we geen session hebben
			return true;
		}
	}

	@SuppressWarnings("unchecked")
	private <T> T reget(Class<T> clazz, Serializable id)
	{
		BatchDataAccessHelper<T> helper = DataAccessRegistry.getHelper(BatchDataAccessHelper.class);
		return helper.get(clazz, id);
	}

	/**
	 * Geeft het huidige peildatummodel die gezet is op de actuele thread.
	 * 
	 * @return het huidige peildatummodel of null als er geen peildatummodel gezet is.
	 */
	public IModel<Date> getPeildatumModel()
	{
		return peildatumModel;
	}

	/**
	 * Zet de thread local voor het vasthouden van het huidige peildatummodel tijdens het
	 * verwerken van o.a. database transacties.
	 * <p>
	 * <STRONG>NB. ROEP {@link #clearContext()} AAN ALS DE THREAD IS AFGELOPEN.
	 * 
	 * @param peildatumModel
	 */
	public void setPeildatumModel(IModel<Date> peildatumModel)
	{
		this.peildatumModel = peildatumModel;
	}

	/**
	 * @return De ingestelde peildatum voor de aanroepende thread, of anders vandaag als
	 *         geen peildatum ingesteld is.
	 */
	public Date getPeildatumOfVandaag()
	{
		if (peildatumModel == null || peildatumModel.getObject() == null)
		{
			return TimeUtil.getInstance().currentDate();
		}
		return peildatumModel.getObject();
	}

	/**
	 * @return De ingestelde peildatum voor de aanroepende thread
	 */
	public Date getPeildatum()
	{
		return peildatumModel == null ? null : (Date) peildatumModel.getObject();
	}

	/**
	 * Zet de thread local voor het vasthouden van de huidige organisatie tijdens het
	 * verwerken van o.a. database transacties.
	 * <p>
	 * <STRONG>NB. ROEP {@link #clearContext()} AAN ALS DE THREAD IS AFGELOPEN.
	 * 
	 */
	public void setOrganisatie(Organisatie organisatie)
	{
		this.organisatie = organisatie;
	}

	/**
	 * Geeft de huidige organisatie voor de aanroepende thread
	 */
	public Organisatie getOrganisatie()
	{
		if (!isAttached(organisatie))
			organisatie = reget(Organisatie.class, organisatie.getIdAsSerializable());
		return organisatie;
	}

	/**
	 * Geeft de huidige instelling voor de aanroepende thread. Als de huidige organisatie
	 * geen instellling is, geeft dit null
	 */
	public Instelling getInstelling()
	{
		Organisatie org = getOrganisatie();
		if (org == null)
			return null;

		org = (Organisatie) org.doUnproxy();
		if (org instanceof Instelling)
			return (Instelling) org;

		return null;
	}

	public EduArteContext copy()
	{
		EduArteContext ret = new EduArteContext();
		ret.setOrganisatie(getOrganisatie());
		ret.setAccount(getAccount());
		ret.setPeildatumModel(getPeildatumModel());
		return ret;
	}

	/**
	 * Voert de runnable uit in the context of the organisatie en herstelt de context na
	 * de uitvoer.
	 */
	public <R extends Runnable> R runInContext(R runnable, Organisatie contextOrg)
	{
		Organisatie oldOrg = getOrganisatie();
		try
		{
			setOrganisatie(contextOrg);
			runnable.run();
			return runnable;
		}
		finally
		{
			setOrganisatie(oldOrg);
		}
	}

	public <R extends Runnable> R runInContext(R runnable, EduArteContext newContext)
	{
		EduArteContext oldContext = get();
		try
		{
			context.set(newContext);
			runnable.run();
			return runnable;
		}
		finally
		{
			context.set(oldContext);
		}
	}

	/**
	 * Zet de thread local voor het vasthouden van de huidige gebruiker tijdens het
	 * verwerken van o.a. database transacties.
	 * <p>
	 * <STRONG>NB. ROEP {@link #clearContext()} AAN ALS DE THREAD IS AFGELOPEN.
	 * 
	 * @param account
	 */
	public void setAccount(Account account)
	{
		this.account = account;
	}

	/**
	 * Zet de thread local voor het vasthouden van de huidige gebruiker tijdens het
	 * verwerken van o.a. database transacties.
	 * <p>
	 * <STRONG>NB. ROEP {@link #clearContext()} AAN ALS DE THREAD IS AFGELOPEN.
	 * 
	 * @param accountId
	 */
	public void setAccountById(Long accountId)
	{
		if (accountId == null)
		{
			account = null;
		}
		else
		{
			AccountDataAccessHelper helper =
				DataAccessRegistry.getHelper(AccountDataAccessHelper.class);
			setAccount(helper.get(Account.class, accountId));
		}
	}

	/**
	 * Geeft de huidige gebruiker die gezet is op de actuele thread.
	 * 
	 * @return de huidige gebruiker of null als er geen gebruiker gezet is.
	 */
	public Account getAccount()
	{
		if (!isAttached(account))
			account = reget(Account.class, account.getIdAsSerializable());
		return account;
	}

	/**
	 * De medewerker die ingelogd is.
	 * 
	 * @return ingelogde medewerker of null als het geen medewerker is.
	 */
	public Medewerker getMedewerker()
	{
		return getAccount() == null ? null : getAccount().getMedewerker();
	}

	/**
	 * De deelnemer die ingelogd is.
	 * 
	 * @return ingelogde deelnemer of null als het geen deelnemer is.
	 */
	public Deelnemer getDeelnemer()
	{
		return getAccount() == null ? null : getAccount().getDeelnemer();
	}

	/**
	 * @return De ingelogde persoon.
	 */
	public Persoon getGebruiker()
	{
		return getAccount() == null ? null : getAccount().getEigenaar();
	}

	/**
	 * @return De ingelogde medewerker of deelnemer.
	 */
	public IOrganisatieEenheidLocatieKoppelbaarEntiteit< ? > getOrganisatieEenheidLocatieVanAccount()
	{
		IOrganisatieEenheidLocatieKoppelbaarEntiteit< ? > ret =
			getAccount().getOrganisatieEenheidLocatie();
		if (ret == null)
			throw new IllegalStateException("Het account heeft geen organisatie-eenheid-locatie");
		return ret;
	}

	public boolean isGebruikerAccount()
	{
		return getAccount() != null && getAccount().getEigenaar() != null;
	}

	/**
	 * @return De default organisatie-eenheid van de huidige gebruiker.
	 */
	public OrganisatieEenheid getDefaultOrganisatieEenheid()
	{
		List<OrganisatieEenheid> eenheden = getOrganisatieEenheden();
		return eenheden.isEmpty() ? null : eenheden.get(0);
	}

	/**
	 * @return De default organisatie-eenheid van de huidige gebruiker, of de root
	 *         organisatieeenheid, als de gebruiker er geen heeft.
	 */
	public OrganisatieEenheid getFirstOrInstellingOrganisatieEenheid()
	{
		List<OrganisatieEenheid> eenheden = getOrganisatieEenheden();
		return eenheden.isEmpty() ? DataAccessRegistry.getHelper(
			OrganisatieEenheidDataAccessHelper.class).getRootOrganisatieEenheid() : eenheden.get(0);
	}

	/**
	 * @return De actieve organisatie-eenheden van het ingelogde account, of als dit geen
	 *         organisatie-eenheden oplevert, de root organisatie-eenheid van de
	 *         instelling.
	 */
	public List<OrganisatieEenheid> getOrganisatieEenhedenOrInstelling()
	{
		List<OrganisatieEenheid> ret = getOrganisatieEenheden();
		return ret.isEmpty() ? Arrays.asList(DataAccessRegistry.getHelper(
			OrganisatieEenheidDataAccessHelper.class).getRootOrganisatieEenheid()) : ret;
	}

	/**
	 * @return De actieve organisatie-eenheden van het ingelogde account.
	 */
	public List<OrganisatieEenheid> getOrganisatieEenheden()
	{
		return getOrganisatieEenheden(null);
	}

	public List<OrganisatieEenheid> getOrganisatieEenheden(Locatie locatie)
	{
		IOrganisatieEenheidLocatieKoppelbaarEntiteit< ? > orgEhdLocVanAccount =
			getAccount().getOrganisatieEenheidLocatie();
		if (orgEhdLocVanAccount != null)
			return OrganisatieEenheidLocatieUtil.getActieveOrganisatieEenheden(orgEhdLocVanAccount
				.getOrganisatieEenheidLocatieKoppelingen(), locatie);
		else
			return Collections.emptyList();
	}

	/**
	 * @return De actieve locaties van het ingelogde account die gekoppeld zijn aan de
	 *         gegeven organisatie-eenheid.
	 */
	public List<Locatie> getLocaties(OrganisatieEenheid organisatieEenheid)
	{
		IOrganisatieEenheidLocatieKoppelbaarEntiteit< ? > orgEhdLocVanAccount =
			getAccount().getOrganisatieEenheidLocatie();
		if (orgEhdLocVanAccount != null)
			return OrganisatieEenheidLocatieUtil.getActieveLocaties(orgEhdLocVanAccount
				.getOrganisatieEenheidLocatieKoppelingen(), organisatieEenheid);
		else
			return Collections.emptyList();
	}
}
