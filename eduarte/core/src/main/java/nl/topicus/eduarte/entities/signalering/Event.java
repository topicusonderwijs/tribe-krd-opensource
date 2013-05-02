package nl.topicus.eduarte.entities.signalering;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Lob;

import nl.topicus.eduarte.entities.organisatie.InstellingEntiteit;
import nl.topicus.eduarte.entities.personen.Deelnemer;
import nl.topicus.eduarte.entities.security.authentication.Account;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public abstract class Event extends InstellingEntiteit
{
	private static final long serialVersionUID = 1L;

	@Column(length = 200)
	private String onderwerp;

	@Lob
	private String omschrijving;

	/**
	 * Wordt gebruikt om te bepalen of een event al eerder is opgetreden.
	 */
	@Column(length = 100)
	private String hash;

	public Event()
	{
	}

	public String getOnderwerp()
	{
		return onderwerp;
	}

	public void setOnderwerp(String onderwerp)
	{
		this.onderwerp = onderwerp;
	}

	public String getOmschrijving()
	{
		return omschrijving;
	}

	public void setOmschrijving(String omschrijving)
	{
		this.omschrijving = omschrijving;
	}

	public String getHash()
	{
		return hash;
	}

	public void setHash(String hash)
	{
		this.hash = hash;
	}

	/**
	 * Return true als het event niet opgeslagen hoeft te worden in de database als er
	 * geen ontvangers zijn. Dit kan nuttig zijn voor events waarvoor het niet mogelijk
	 * dat het event meerdere malen optreed (bijv bij het aanmaken van een object, of
	 * events die dagelijks optreden).
	 */
	public abstract boolean discardWhenNoReceivers();

	/**
	 * Berekend een unieke string uit de properties van het event. Deze string wordt
	 * gebruikt om te bepalen of het event al eerder is opgetreden. Geef null terug al het
	 * event altijd als uniek beschouwd moet worden.
	 */
	public abstract String berekenHash();

	/**
	 * Geeft aan of het toegestaan is de gegeven account het event te laten ontvangen.
	 * Standaard geeft dit true terug, maar subclasses kunnen dit gedrag overschrijven.
	 * 
	 * @param account
	 */
	public boolean isEventAllowed(Account account)
	{
		return true;
	}

	public Deelnemer getDeelnemer()
	{
		return null;
	}
}
