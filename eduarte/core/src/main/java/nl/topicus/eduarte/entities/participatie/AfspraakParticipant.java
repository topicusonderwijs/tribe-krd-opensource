package nl.topicus.eduarte.entities.participatie;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.entities.IdObject;
import nl.topicus.eduarte.dao.participatie.helpers.AfspraakParticpantDataAccessHelper;
import nl.topicus.eduarte.entities.contract.Contract;
import nl.topicus.eduarte.entities.groep.Groep;
import nl.topicus.eduarte.entities.organisatie.InstellingEntiteit;
import nl.topicus.eduarte.entities.participatie.enums.UitnodigingStatus;
import nl.topicus.eduarte.entities.personen.Deelnemer;
import nl.topicus.eduarte.entities.personen.Medewerker;
import nl.topicus.eduarte.entities.personen.Persoon;
import nl.topicus.eduarte.entities.security.authentication.MedewerkerAccount;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Index;

/**
 * De personen die aan een afspraak mee moeten doen. Dit kan een groep zijn, of
 * individuele personen (dus ook medewerkers of verzorgers) zijn.
 * 
 * @author loite
 */
@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
public class AfspraakParticipant extends InstellingEntiteit
{
	private static final long serialVersionUID = 1L;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "afspraak", nullable = false)
	@Index(name = "idx_AfspraakPart_afspraak")
	private Afspraak afspraak;

	/**
	 * De noise Groep die meedoet aan de afspraak.
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "groep", nullable = true)
	@Index(name = "idx_AfspraakPart_groep")
	private Groep groep;

	/**
	 * De PersoonlijkeGroep die meedoet aan de afspraak.
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "persoonlijkeGroep", nullable = true)
	@Index(name = "idx_AfspraakPart_pGroep")
	private PersoonlijkeGroep persoonlijkeGroep;

	/**
	 * De medewerker die meedoet aan de afspraak.
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "medewerker", nullable = true)
	@Index(name = "idx_AfspraakPart_medewerker")
	private Medewerker medewerker;

	/**
	 * De deelnemer die meedoet aan de afspraak.
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "deelnemer", nullable = true)
	@Index(name = "idx_AfspraakPart_deelnemer")
	private Deelnemer deelnemer;

	/**
	 * De externe die meedoet aan de afspraak.
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "externe", nullable = true)
	@Index(name = "idx_AfspraakPart_externe")
	private ExternPersoon externe;

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private UitnodigingStatus uitnodigingStatus;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "contract", nullable = true)
	@Index(name = "idx_AfspraakPart_contract")
	private Contract contract;

	@Column
	private boolean uitnodigingVerstuurd;

	public AfspraakParticipant()
	{
	}

	public Afspraak getAfspraak()
	{
		return afspraak;
	}

	public void setAfspraak(Afspraak afspraak)
	{
		this.afspraak = afspraak;
	}

	public Groep getGroep()
	{
		return groep;
	}

	public void setGroep(Groep groep)
	{
		this.groep = groep;
	}

	public PersoonlijkeGroep getPersoonlijkeGroep()
	{
		return persoonlijkeGroep;
	}

	public void setPersoonlijkeGroep(PersoonlijkeGroep persoonlijkeGroep)
	{
		this.persoonlijkeGroep = persoonlijkeGroep;
	}

	public Medewerker getMedewerker()
	{
		return medewerker;
	}

	public void setMedewerker(Medewerker medewerker)
	{
		this.medewerker = medewerker;
	}

	public Deelnemer getDeelnemer()
	{
		return deelnemer;
	}

	public void setDeelnemer(Deelnemer deelnemer)
	{
		this.deelnemer = deelnemer;
	}

	public void setExterne(ExternPersoon externe)
	{
		this.externe = externe;
	}

	public ExternPersoon getExterne()
	{
		return externe;
	}

	public boolean isAuteur()
	{
		Persoon persoon = getPersoon();
		return persoon != null && persoon.equals(getAfspraak().getAuteur());
	}

	/**
	 * @return De persoon die aan deze afspraakdeelnemer hangt, of null als er een
	 *         noiseGroep aan deze deelnemer gekoppeld is.
	 */
	public Persoon getPersoon()
	{
		if (getMedewerker() != null)
			return getMedewerker().getPersoon();
		else if (getDeelnemer() != null)
			return getDeelnemer().getPersoon();
		return null;
	}

	/**
	 * @return De entiteit (deelnemer, noiseGroep, medewerker) die aan deze
	 *         afspraakdeelnemer is gekoppeld.
	 */
	public IdObject getParticipantEntiteit()
	{
		if (getDeelnemer() != null)
			return getDeelnemer();
		else if (getMedewerker() != null)
			return getMedewerker();
		else if (getGroep() != null)
			return getGroep();
		else if (getExterne() != null)
			return getExterne();
		else if (getPersoonlijkeGroep() != null)
			return getPersoonlijkeGroep();
		else
			return getExterne();
	}

	/**
	 * @param entiteit
	 *            De entiteit (deelnemer, noiseGroep, medewerker) die gekoppeld moet
	 *            worden aan deze afspraakdeelnemer.
	 */
	public void setParticipantEntiteit(IdObject entiteit)
	{
		if (Deelnemer.class.isAssignableFrom(entiteit.getClass()))
			setDeelnemer((Deelnemer) entiteit);
		else if (Medewerker.class.isAssignableFrom(entiteit.getClass()))
			setMedewerker((Medewerker) entiteit);
		else if (MedewerkerAccount.class.isAssignableFrom(entiteit.getClass()))
			setMedewerker((Medewerker) entiteit);
		else if (Groep.class.isAssignableFrom(entiteit.getClass()))
			setGroep((Groep) entiteit);
		else if (PersoonlijkeGroep.class.isAssignableFrom(entiteit.getClass()))
			setPersoonlijkeGroep((PersoonlijkeGroep) entiteit);
		else if (ExternPersoon.class.isAssignableFrom(entiteit.getClass()))
			setExterne((ExternPersoon) entiteit);
		else
			throw new IllegalArgumentException("Cannot set participant entiteit to " + entiteit);
	}

	@Override
	public String toString()
	{
		return DataAccessRegistry.getHelper(AfspraakParticpantDataAccessHelper.class)
			.getRenderString(getParticipantEntiteit());
	}

	public void setUitnodigingStatus(UitnodigingStatus uitnodigingStatus)
	{
		this.uitnodigingStatus = uitnodigingStatus;
	}

	public UitnodigingStatus getUitnodigingStatus()
	{
		return uitnodigingStatus;
	}

	public void setUitnodigingVerstuurd(boolean uitnodigingVerstuurd)
	{
		this.uitnodigingVerstuurd = uitnodigingVerstuurd;
	}

	public boolean isUitnodigingVerstuurd()
	{
		return uitnodigingVerstuurd;
	}

	public void resetUitnodigingVerstuurd(boolean handmatigeAfspraak)
	{
		if (getAfspraak().getAfspraakType() != null)
			setUitnodigingVerstuurd(!getAfspraak().getAfspraakType().getUitnodigingenVersturen()
				.isEnabled(handmatigeAfspraak)
				&& UitnodigingStatus.DIRECTE_PLAATSING.equals(getUitnodigingStatus()));
	}

	@Override
	public void delete()
	{
		if (getExterne() != null)
			getExterne().delete();
		super.delete();
	}

	public Contract getContract()
	{
		return contract;
	}

	public void setContract(Contract contract)
	{
		this.contract = contract;
	}
}
