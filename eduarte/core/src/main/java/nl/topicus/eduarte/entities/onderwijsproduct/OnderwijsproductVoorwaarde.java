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
 * Koppeltabel tussen twee onderwijsproducten die aangeeft dat het ene onderwijsproduct
 * voorwaardelijk is voor het andere onderwijsproduct.
 * 
 * @author loite
 */
@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
@javax.persistence.Table(uniqueConstraints = {@UniqueConstraint(columnNames = {
	"voorwaardelijkProduct", "voorwaardeVoor"})})
public class OnderwijsproductVoorwaarde extends InstellingEntiteit
{
	private static final long serialVersionUID = 1L;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false, name = "voorwaardelijkProduct")
	@Index(name = "idx_Voorwaarde_voorwaardelijk")
	private Onderwijsproduct voorwaardelijkProduct;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false, name = "voorwaardeVoor")
	@Index(name = "idx_Voorwaarde_voor")
	private Onderwijsproduct voorwaardeVoor;

	public OnderwijsproductVoorwaarde()
	{
	}

	public OnderwijsproductVoorwaarde(Onderwijsproduct voorwaardelijkProduct,
			Onderwijsproduct voorwaardeVoor)
	{
		setVoorwaardelijkProduct(voorwaardelijkProduct);
		setVoorwaardeVoor(voorwaardeVoor);
	}

	public Onderwijsproduct getVoorwaardelijkProduct()
	{
		return voorwaardelijkProduct;
	}

	public void setVoorwaardelijkProduct(Onderwijsproduct voorwaardelijkProduct)
	{
		this.voorwaardelijkProduct = voorwaardelijkProduct;
	}

	public Onderwijsproduct getVoorwaardeVoor()
	{
		return voorwaardeVoor;
	}

	public void setVoorwaardeVoor(Onderwijsproduct voorwaardeVoor)
	{
		this.voorwaardeVoor = voorwaardeVoor;
	}

}
