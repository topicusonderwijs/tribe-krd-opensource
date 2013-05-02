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
 * Een zoekterm voor een onderwijsproduct.
 * 
 * @author loite
 */
@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
@javax.persistence.Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"onderwijsproduct",
	"zoekterm"})})
public class OnderwijsproductZoekterm extends InstellingEntiteit
{
	private static final long serialVersionUID = 1L;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false, name = "onderwijsproduct")
	@Index(name = "idx_ProdZoekterm_prod")
	private Onderwijsproduct onderwijsproduct;

	@Column(nullable = false, length = 100)
	@Index(name = "idx_ProdZoekterm_term")
	private String zoekterm;

	public OnderwijsproductZoekterm()
	{
	}

	public OnderwijsproductZoekterm(Onderwijsproduct onderwijsproduct)
	{
		setOnderwijsproduct(onderwijsproduct);
	}

	public Onderwijsproduct getOnderwijsproduct()
	{
		return onderwijsproduct;
	}

	public void setOnderwijsproduct(Onderwijsproduct onderwijsproduct)
	{
		this.onderwijsproduct = onderwijsproduct;
	}

	public String getZoekterm()
	{
		return zoekterm;
	}

	public void setZoekterm(String zoekterm)
	{
		this.zoekterm = zoekterm;
	}

}
