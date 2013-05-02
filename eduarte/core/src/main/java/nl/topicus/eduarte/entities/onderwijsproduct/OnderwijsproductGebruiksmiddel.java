package nl.topicus.eduarte.entities.onderwijsproduct;

import javax.persistence.Column;
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
 * Koppeltabel tussen onderwijsproduct en gebruiksmiddel
 * 
 * @author loite
 */
@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
@javax.persistence.Table(name = "OndProdGebruiksmiddel", uniqueConstraints = {@UniqueConstraint(columnNames = {
	"onderwijsproduct", "gebruiksmiddel"})})
public class OnderwijsproductGebruiksmiddel extends InstellingEntiteit
{
	private static final long serialVersionUID = 1L;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false, name = "onderwijsproduct")
	@Index(name = "idx_ProdGebr_product")
	private Onderwijsproduct onderwijsproduct;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false, name = "gebruiksmiddel")
	@Index(name = "idx_ProdGebr_gebruiksmiddel")
	private Gebruiksmiddel gebruiksmiddel;

	/**
	 * Optioneel het aantal van het gebruiksmiddel dat benodigd is.
	 */
	@Column(nullable = true)
	private Integer aantal;

	public OnderwijsproductGebruiksmiddel()
	{
	}

	public Onderwijsproduct getOnderwijsproduct()
	{
		return onderwijsproduct;
	}

	public void setOnderwijsproduct(Onderwijsproduct onderwijsproduct)
	{
		this.onderwijsproduct = onderwijsproduct;
	}

	public Gebruiksmiddel getGebruiksmiddel()
	{
		return gebruiksmiddel;
	}

	public void setGebruiksmiddel(Gebruiksmiddel gebruiksmiddel)
	{
		this.gebruiksmiddel = gebruiksmiddel;
	}

	public Integer getAantal()
	{
		return aantal;
	}

	public void setAantal(Integer aantal)
	{
		this.aantal = aantal;
	}
}
