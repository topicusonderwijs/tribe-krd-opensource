package nl.topicus.eduarte.entities.participatie;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import nl.topicus.eduarte.entities.ViewEntiteit;
import nl.topicus.eduarte.entities.contract.Contract;
import nl.topicus.eduarte.entities.participatie.enums.UitnodigingStatus;
import nl.topicus.eduarte.entities.personen.Deelnemer;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Index;
import org.hibernate.annotations.Table;

/**
 * AfspraakDeelnemer correspondeerd met de afspraak deelnemer view, die aangeeft welke
 * deelnemers aan welke afspraak gekoppeld zijn. Deze view houdt rekening met koppelingen
 * via groepen, persoonlijke groepen of die direct zijn. De begin en eind datum van de
 * groepen en deelnames wordt gematched met de datum van de afspraak.
 * 
 * @author papegaaij
 * @author loite
 */
@Entity
@Immutable
@Cache(usage = CacheConcurrencyStrategy.READ_ONLY, region = "Instelling")
@Table(appliesTo = "AfspraakDeelnemer")
public class AfspraakDeelnemer extends ViewEntiteit
{
	private static final long serialVersionUID = 1L;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "afspraak", nullable = true)
	@Index(name = "idx_AfspraakD_afspraak")
	private Afspraak afspraak;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "deelnemer", nullable = true)
	@Index(name = "idx_AfspraakD_deelnemer")
	private Deelnemer deelnemer;

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private UitnodigingStatus uitnodigingStatus;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "contract", nullable = true)
	@Index(name = "idx_AfspraakD_contract")
	private Contract contract;

	public AfspraakDeelnemer()
	{
	}

	public void setDeelnemer(Deelnemer deelnemer)
	{
		this.deelnemer = deelnemer;
	}

	public Deelnemer getDeelnemer()
	{
		return deelnemer;
	}

	public void setAfspraak(Afspraak afspraak)
	{
		this.afspraak = afspraak;
	}

	public Afspraak getAfspraak()
	{
		return afspraak;
	}

	public UitnodigingStatus getUitnodigingStatus()
	{
		return uitnodigingStatus;
	}

	public void setUitnodigingStatus(UitnodigingStatus uitnodigingStatus)
	{
		this.uitnodigingStatus = uitnodigingStatus;
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
