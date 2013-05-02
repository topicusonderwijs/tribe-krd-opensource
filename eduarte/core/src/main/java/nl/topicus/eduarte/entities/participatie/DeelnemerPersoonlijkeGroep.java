package nl.topicus.eduarte.entities.participatie;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import nl.topicus.cobra.dao.helpers.IgnoreInGebruik;
import nl.topicus.eduarte.entities.ViewEntiteit;
import nl.topicus.eduarte.entities.personen.Deelnemer;
import nl.topicus.eduarte.entities.personen.Persoon;
import nl.topicus.eduarte.providers.DeelnemerProvider;
import nl.topicus.eduarte.providers.PersoonProvider;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Table;

/**
 * DeelnemerPersoonlijkeGroep is een view die alle deelnemers oplevert die in een
 * persoonlijke groep zitten. Een deeelnemer kan direct in een persoonlijke groep
 * geplaatst zijn, of kan indirect via een andere persoonlijke groep in de persoonlijke
 * groep zitten.
 * 
 * @author loite
 */
@Entity
@Immutable
@Cache(usage = CacheConcurrencyStrategy.READ_ONLY, region = "Instelling")
@Table(appliesTo = "DeelnemerPersoonlijkeGroep")
public class DeelnemerPersoonlijkeGroep extends ViewEntiteit implements DeelnemerProvider,
		PersoonProvider
{
	private static final long serialVersionUID = 1L;

	/**
	 * De parent van de relatie. Dit is de groep waarin de deelnemers zitten.
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "groep", nullable = false)
	@IgnoreInGebruik
	private PersoonlijkeGroep groep;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "deelnemer", nullable = false)
	private Deelnemer deelnemer;

	@Temporal(value = TemporalType.DATE)
	@Column(nullable = false)
	private Date beginDatum;

	@Temporal(value = TemporalType.DATE)
	@Column(nullable = false)
	private Date eindDatum;

	public DeelnemerPersoonlijkeGroep()
	{
	}

	public PersoonlijkeGroep getGroep()
	{
		return groep;
	}

	public void setGroep(PersoonlijkeGroep groep)
	{
		this.groep = groep;
	}

	public Deelnemer getDeelnemer()
	{
		return deelnemer;
	}

	public void setDeelnemer(Deelnemer deelnemer)
	{
		this.deelnemer = deelnemer;
	}

	public Date getBeginDatum()
	{
		return beginDatum;
	}

	public void setBeginDatum(Date beginDatum)
	{
		this.beginDatum = beginDatum;
	}

	public Date getEindDatum()
	{
		return eindDatum;
	}

	public void setEindDatum(Date eindDatum)
	{
		this.eindDatum = eindDatum;
	}

	@Override
	public Persoon getPersoon()
	{
		return getDeelnemer().getPersoon();
	}
}
