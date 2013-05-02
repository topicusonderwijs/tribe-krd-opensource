package nl.topicus.eduarte.entities.resultaatstructuur;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.UniqueConstraint;

import nl.topicus.eduarte.entities.organisatie.InstellingEntiteit;
import nl.topicus.eduarte.entities.personen.Deelnemer;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Index;

@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
@javax.persistence.Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"deelnemer",
	"resultaatstructuur"})})
@BatchSize(size = 1000)
public class DeelnemerResultaatVersie extends InstellingEntiteit
{
	private static final long serialVersionUID = 1L;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "deelnemer", nullable = false)
	@Index(name = "idx_deelnemerTV_deelnemer")
	private Deelnemer deelnemer;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "resultaatstructuur", nullable = false)
	@Index(name = "idx_deelnemerRV_rs")
	private Resultaatstructuur resultaatstructuur;

	@Column(nullable = false)
	private long versie;

	public DeelnemerResultaatVersie()
	{
	}

	public DeelnemerResultaatVersie(Deelnemer deelnemer, Resultaatstructuur resultaatstructuur)
	{
		setDeelnemer(deelnemer);
		setResultaatstructuur(resultaatstructuur);
	}

	public Deelnemer getDeelnemer()
	{
		return deelnemer;
	}

	public void setDeelnemer(Deelnemer deelnemer)
	{
		this.deelnemer = deelnemer;
	}

	public Resultaatstructuur getResultaatstructuur()
	{
		return resultaatstructuur;
	}

	public void setResultaatstructuur(Resultaatstructuur resultaatstructuur)
	{
		this.resultaatstructuur = resultaatstructuur;
	}

	public long getVersie()
	{
		return versie;
	}

	public void setVersie(long versie)
	{
		this.versie = versie;
	}

	public void increment()
	{
		setVersie(getVersie() + 1);
	}
}
