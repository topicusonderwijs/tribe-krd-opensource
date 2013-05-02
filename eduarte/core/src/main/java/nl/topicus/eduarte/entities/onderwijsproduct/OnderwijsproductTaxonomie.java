package nl.topicus.eduarte.entities.onderwijsproduct;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.UniqueConstraint;

import nl.topicus.eduarte.entities.organisatie.InstellingEntiteit;
import nl.topicus.eduarte.entities.taxonomie.TaxonomieElement;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Index;

/**
 * Koppeltabel tussen onderwijsproduct en taxonomie waarmee aangegeven wordt voor welke
 * taxonomieelementen het onderwijsproduct relevant is.
 * 
 * @author loite
 */
@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
@javax.persistence.Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"taxonomieElement",
	"onderwijsproduct"})})
public class OnderwijsproductTaxonomie extends InstellingEntiteit
{
	private static final long serialVersionUID = 1L;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false, name = "taxonomieElement")
	@Index(name = "idx_ProdTaxonomie_taxonomie")
	private TaxonomieElement taxonomieElement;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false, name = "onderwijsproduct")
	@Index(name = "idx_ProdTaxonomie_product")
	private Onderwijsproduct onderwijsproduct;

	public OnderwijsproductTaxonomie()
	{
	}

	public OnderwijsproductTaxonomie(TaxonomieElement taxonomieElement,
			Onderwijsproduct onderwijsproduct)
	{
		setTaxonomieElement(taxonomieElement);
		setOnderwijsproduct(onderwijsproduct);
	}

	public TaxonomieElement getTaxonomieElement()
	{
		return taxonomieElement;
	}

	public void setTaxonomieElement(TaxonomieElement taxonomieElement)
	{
		this.taxonomieElement = taxonomieElement;
	}

	public Onderwijsproduct getOnderwijsproduct()
	{
		return onderwijsproduct;
	}

	public void setOnderwijsproduct(Onderwijsproduct onderwijsproduct)
	{
		this.onderwijsproduct = onderwijsproduct;
	}

}
