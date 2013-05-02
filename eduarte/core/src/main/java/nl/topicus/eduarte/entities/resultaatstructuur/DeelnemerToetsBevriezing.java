package nl.topicus.eduarte.entities.resultaatstructuur;

import java.util.BitSet;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.UniqueConstraint;

import nl.topicus.cobra.util.JavaUtil;
import nl.topicus.eduarte.entities.organisatie.InstellingEntiteit;
import nl.topicus.eduarte.entities.personen.Deelnemer;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Index;

@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
@javax.persistence.Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"deelnemer", "toets"})})
public class DeelnemerToetsBevriezing extends InstellingEntiteit implements IBevriezing
{
	private static final long serialVersionUID = 1L;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "deelnemer", nullable = false)
	@Index(name = "idx_deelnemerTB_deelnemer")
	private Deelnemer deelnemer;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "toets", nullable = false)
	@Index(name = "idx_deelnemerTB_rs")
	private Toets toets;

	@Column(nullable = false)
	private long bevrorenPogingen;

	@Column(nullable = false)
	private boolean ingeleverd;

	public DeelnemerToetsBevriezing()
	{
	}

	public DeelnemerToetsBevriezing(Deelnemer deelnemer, Toets toets)
	{
		setDeelnemer(deelnemer);
		setToets(toets);
	}

	public Deelnemer getDeelnemer()
	{
		return deelnemer;
	}

	public void setDeelnemer(Deelnemer deelnemer)
	{
		this.deelnemer = deelnemer;
	}

	public Toets getToets()
	{
		return toets;
	}

	public void setToets(Toets toets)
	{
		this.toets = toets;
	}

	public long getBevrorenPogingen()
	{
		return bevrorenPogingen;
	}

	public void setBevrorenPogingen(long bevrorenPogingen)
	{
		this.bevrorenPogingen = bevrorenPogingen;
	}

	public BitSet getBevorenPogingenAsSet()
	{
		return JavaUtil.longToBitSet(getBevrorenPogingen());
	}

	public void setBevorenPogingenAsSet(BitSet pogingen)
	{
		setBevrorenPogingen(JavaUtil.bitSetToLong(pogingen));
	}

	public boolean isIngeleverd()
	{
		return ingeleverd;
	}

	public void setIngeleverd(boolean ingeleverd)
	{
		this.ingeleverd = ingeleverd;
	}
}
