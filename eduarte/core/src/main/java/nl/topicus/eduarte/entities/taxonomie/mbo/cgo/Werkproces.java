package nl.topicus.eduarte.entities.taxonomie.mbo.cgo;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import nl.topicus.eduarte.entities.organisatie.EntiteitContext;
import nl.topicus.eduarte.entities.taxonomie.Deelgebied;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Index;

/**
 *
 */
@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Landelijk")
public class Werkproces extends Deelgebied
{
	private static final long serialVersionUID = 1L;

	@Column(nullable = true)
	private Integer nummer;

	@Lob
	@Column(nullable = true)
	private String omschrijving;

	@Lob
	@Column(nullable = true)
	private String resultaat;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "kerntaak", nullable = true)
	@Index(name = "idx_werkproces_kerntaak")
	private Kerntaak kerntaak;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "werkproces")
	@BatchSize(size = 100)
	private List<Leerpunt> leerpunten = new ArrayList<Leerpunt>();

	public Werkproces()
	{
		super();
	}

	public Werkproces(EntiteitContext context)
	{
		super(context);
	}

	public Integer getNummer()
	{
		return nummer;
	}

	public void setNummer(Integer nummer)
	{
		this.nummer = nummer;
	}

	public String getDisplayName()
	{
		return getParent().getVolgnummer() + "." + getVolgnummer() + " " + getNaam();
	}

	public List<Leerpunt> getLeerpunten()
	{
		return leerpunten;
	}

	public void setLeerpunten(List<Leerpunt> leerpunten)
	{
		this.leerpunten = leerpunten;
	}

	public Kerntaak getKerntaak()
	{
		return kerntaak;
	}

	public void setKerntaak(Kerntaak kerntaak)
	{
		this.kerntaak = kerntaak;
	}

	public String getOmschrijving()
	{
		return omschrijving;
	}

	public void setOmschrijving(String omschrijving)
	{
		this.omschrijving = omschrijving;
	}

	public String getResultaat()
	{
		return resultaat;
	}

	public void setResultaat(String resultaat)
	{
		this.resultaat = resultaat;
	}

	@Override
	public int hashCode()
	{
		if (isUitzonderlijk() && getNaam() != null)
		{
			return getNaam().hashCode();
		}
		else
		{
			return super.hashCode();
		}
	}

	@Override
	public boolean equals(Object obj)
	{
		if (super.equals(obj))
			return true;
		if (obj instanceof Werkproces)
		{
			Werkproces other = (Werkproces) obj;
			if (other.isUitzonderlijk() && getNaam() != null)
				return getNaam().equals(other.getNaam());
			if (getId() == null && other.getId() == null)
				return false;
		}
		return false;
	}

	@Override
	public void setCode(String code)
	{
		if (isUitzonderlijk())
		{
			this.setCodeZonderTaxonomie(code);
		}
		else
		{
			super.setCode(code);
		}
	}

	public Leerpunt getLeerpunt(Competentie competentie)
	{
		for (Leerpunt lp : getLeerpunten())
		{
			if (lp.getCompetentie().equals(competentie))
			{
				return lp;
			}
		}
		return null;
	}
}
