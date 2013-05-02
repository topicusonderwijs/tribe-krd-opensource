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
 * Koppeltabel tussen onderwijsproduct en verbruiksmiddel
 * 
 * @author loite
 */
@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
@javax.persistence.Table(name = "OndProdVerbruiksmiddel", uniqueConstraints = {@UniqueConstraint(columnNames = {
	"onderwijsproduct", "verbruiksmiddel"})})
public class OnderwijsproductVerbruiksmiddel extends InstellingEntiteit
{
	private static final long serialVersionUID = 1L;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false, name = "onderwijsproduct")
	@Index(name = "idx_ProdVer_product")
	private Onderwijsproduct onderwijsproduct;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false, name = "verbruiksmiddel")
	@Index(name = "idx_ProdVer_verbruiksmiddel")
	private Verbruiksmiddel verbruiksmiddel;

	/**
	 * Optioneel het aantal van het gebruiksmiddel dat benodigd is.
	 */
	@Column(nullable = true)
	private Integer aantal;

	public OnderwijsproductVerbruiksmiddel()
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

	public Integer getAantal()
	{
		return aantal;
	}

	public void setAantal(Integer aantal)
	{
		this.aantal = aantal;
	}

	public Verbruiksmiddel getVerbruiksmiddel()
	{
		return verbruiksmiddel;
	}

	public void setVerbruiksmiddel(Verbruiksmiddel verbruiksmiddel)
	{
		this.verbruiksmiddel = verbruiksmiddel;
	}

}
