package nl.topicus.eduarte.entities.onderwijsproduct;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.UniqueConstraint;

import nl.topicus.eduarte.entities.organisatie.InstellingEntiteit;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Index;

/**
 * Koppeltabel tussen twee onderwijsproducten die aangeeft dat het ene onderwijsproduct de
 * opvolger (nieuwe versie) is van een ander onderwijsproduct.
 * 
 * @author loite
 */
@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
@javax.persistence.Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"oudProduct",
	"nieuwProduct"})})
public class OnderwijsproductOpvolger extends InstellingEntiteit
{
	private static final long serialVersionUID = 1L;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false, name = "oudProduct")
	@Index(name = "idx_Opvolger_oud")
	private Onderwijsproduct oudProduct;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false, name = "nieuwProduct")
	@Index(name = "idx_Opvolger_nieuw")
	private Onderwijsproduct nieuwProduct;

	public OnderwijsproductOpvolger()
	{
	}

	public OnderwijsproductOpvolger(Onderwijsproduct oudProduct, Onderwijsproduct nieuwProduct)
	{
		setOudProduct(oudProduct);
		setNieuwProduct(nieuwProduct);
	}

	public Onderwijsproduct getOudProduct()
	{
		return oudProduct;
	}

	public void setOudProduct(Onderwijsproduct oudProduct)
	{
		this.oudProduct = oudProduct;
	}

	public Onderwijsproduct getNieuwProduct()
	{
		return nieuwProduct;
	}

	public void setNieuwProduct(Onderwijsproduct nieuwProduct)
	{
		this.nieuwProduct = nieuwProduct;
	}

}
