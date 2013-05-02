package nl.topicus.eduarte.entities.dbs.trajecten;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import nl.topicus.cobra.entities.FieldPersistance;
import nl.topicus.cobra.entities.FieldPersistenceMode;
import nl.topicus.eduarte.entities.organisatie.InstellingEntiteit;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.ForeignKey;
import org.hibernate.annotations.Index;

@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
public class VervolgHandeling extends InstellingEntiteit
{
	private static final long serialVersionUID = 1L;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "vervolg", nullable = false)
	@ForeignKey(name = "FK_VervHand_vervolg")
	@Index(name = "idx_VervHand_vervolg")
	@FieldPersistance(FieldPersistenceMode.SAVE)
	private BegeleidingsHandeling vervolg;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "voorafgaand", nullable = false)
	@ForeignKey(name = "FK_VervHand_vooraf")
	@Index(name = "idx_VervHand_vooraf")
	@FieldPersistance(FieldPersistenceMode.SAVE)
	private BegeleidingsHandeling voorafgaand;

	public VervolgHandeling()
	{

	}

	public BegeleidingsHandeling getVervolg()
	{
		return vervolg;
	}

	public void setVervolg(BegeleidingsHandeling vervolg)
	{
		this.vervolg = vervolg;
	}

	public BegeleidingsHandeling getVoorafgaand()
	{
		return voorafgaand;
	}

	public void setVoorafgaand(BegeleidingsHandeling voorafgaand)
	{
		this.voorafgaand = voorafgaand;
	}
}
