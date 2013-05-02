package nl.topicus.eduarte.entities.onderwijsproduct;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import nl.topicus.eduarte.entities.begineinddatum.BeginEinddatumInstellingEntiteit;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Index;

/**
 * Periode waarin een onderwijsaanbod geldt. Een onderwijsproduct kan meerdere malen per
 * jaar worden aangeboden.
 * 
 * @author vanharen
 */
@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
public class OnderwijsproductAanbodPeriode extends BeginEinddatumInstellingEntiteit
{

	private static final long serialVersionUID = 1L;

	private Date begindatumInschrijving;

	private Date begindatumLesperiode;

	private Date einddatumInschrijving;

	private Date einddatumLesperiode;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false, name = "onderwijsproductaanbod")
	@Index(name = "idx_ProdAanbodPer_aanbod")
	private OnderwijsproductAanbod aanbod;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false, name = "aanbodperiode")
	@Index(name = "idx_ProdAanbodPer_aanbodper")
	private AanbodPeriode aanbodperiode;

	private int minimaalAantalInschrijvingen;

	public OnderwijsproductAanbodPeriode()
	{

	}

	public Date getBegindatumInschrijving()
	{
		return begindatumInschrijving;
	}

	public void setBegindatumInschrijving(Date begindatumInschrijving)
	{
		this.begindatumInschrijving = begindatumInschrijving;
	}

	public Date getBegindatumLesperiode()
	{
		return begindatumLesperiode;
	}

	public void setBegindatumLesperiode(Date begindatumLesperiode)
	{
		this.begindatumLesperiode = begindatumLesperiode;
	}

	public Date getEinddatumInschrijving()
	{
		return einddatumInschrijving;
	}

	public void setEinddatumInschrijving(Date einddatumInschrijving)
	{
		this.einddatumInschrijving = einddatumInschrijving;
	}

	public Date getEinddatumLesperiode()
	{
		return einddatumLesperiode;
	}

	public void setEinddatumLesperiode(Date einddatumLesperiode)
	{
		this.einddatumLesperiode = einddatumLesperiode;
	}

	public OnderwijsproductAanbod getAanbod()
	{
		return aanbod;
	}

	public void setAanbod(OnderwijsproductAanbod aanbod)
	{
		this.aanbod = aanbod;
	}

	public int getMinimaalAantalInschrijvingen()
	{
		return minimaalAantalInschrijvingen;
	}

	public void setMinimaalAantalInschrijvingen(int minimaalAantalInschrijvingen)
	{
		this.minimaalAantalInschrijvingen = minimaalAantalInschrijvingen;
	}

	public AanbodPeriode getAanbodperiode()
	{
		return aanbodperiode;
	}

	public void setAanbodperiode(AanbodPeriode aanbodperiode)
	{
		this.aanbodperiode = aanbodperiode;
	}

}
