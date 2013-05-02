package nl.topicus.eduarte.entities.taxonomie.mbo.cgo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
 * Kerntaak voor het CGO.
 * 
 * @author loite
 */
@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Landelijk")
public class Kerntaak extends Deelgebied
{
	private static final long serialVersionUID = 1L;

	@Column(nullable = true)
	private int nummer;

	@Column(nullable = true)
	private Integer hoofdstuk;

	@Lob
	private String omschrijving;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "competentieMatrix", nullable = true)
	@Index(name = "idx_kerntaak_competentieMatrix")
	private CompetentieMatrix competentieMatrix;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "kerntaak")
	@BatchSize(size = 100)
	private List<Werkproces> werkprocessen = new ArrayList<Werkproces>();

	public Kerntaak()
	{
		super();
	}

	public Kerntaak(EntiteitContext context)
	{
		super(context);
	}

	public int getNummer()
	{
		return nummer;
	}

	public Integer getHoofdstuk()
	{
		return hoofdstuk;
	}

	public List<Werkproces> getWerkprocessen()
	{
		return werkprocessen;
	}

	public void setWerkprocessen(List<Werkproces> werkprocessen)
	{
		this.werkprocessen = werkprocessen;
	}

	public void addWerkproces(Werkproces werkproces)
	{
		getWerkprocessen().add(werkproces);
	}

	public void setNummer(int nummer)
	{
		this.nummer = nummer;
	}

	public void setHoofdstuk(Integer hoofdstuk)
	{
		this.hoofdstuk = hoofdstuk;
	}

	@Override
	public void setTaxonomiecode(String code)
	{
		super.setTaxonomiecode(code);
	}

	public List<Werkproces> getSortedWerkprocessen()
	{
		List<Werkproces> ret = new ArrayList<Werkproces>(getWerkprocessen());
		Collections.sort(ret, new Comparator<Werkproces>()
		{
			@Override
			public int compare(Werkproces o1, Werkproces o2)
			{
				if (o1.getVolgnummer() != o2.getVolgnummer())
				{
					return o1.getVolgnummer() - o2.getVolgnummer();
				}
				return o1.getNaam().compareTo(o2.getNaam());
			}
		});
		return ret;
	}

	public String getStrippedOmschrijving()
	{
		String[] lines = omschrijving.split("\n");
		if (lines.length > 9)
		{
			int linesNr = lines.length;
			if (lines[0].trim().startsWith("<h2 ") && lines[1].trim().startsWith("<a ")
				&& lines[2].trim().startsWith("<table ") && lines[3].trim().startsWith("<tr>")
				&& lines[4].trim().startsWith("<tr>") && lines[5].trim().startsWith("<td>")
				&& lines[linesNr - 1].trim().startsWith("</table>")
				&& lines[linesNr - 2].trim().startsWith("</tr>")
				&& lines[linesNr - 3].trim().startsWith("<td>")
				&& lines[linesNr - 4].trim().endsWith("</td>"))
			{
				StringBuilder ret = new StringBuilder();
				ret.append(lines[5].trim().substring(4));
				for (int lineCount = 6; lineCount < linesNr - 4; lineCount++)
				{
					ret.append('\n');
					ret.append(lines[lineCount]);
				}
				ret.append('\n');
				String lastLine = lines[linesNr - 4];
				ret.append(lastLine.substring(0, lastLine.length() - 5));
				return ret.toString();
			}
		}
		return omschrijving;
	}

	public String getOmschrijving()
	{
		return omschrijving;
	}

	public void setOmschrijving(String omschrijving)
	{
		this.omschrijving = omschrijving;
	}

	public String getDisplayName()
	{
		return getVolgnummer() + " " + getNaam();
	}

	public CompetentieMatrix getCompetentieMatrix()
	{
		return competentieMatrix;
	}

	public void setCompetentieMatrix(CompetentieMatrix competentieMatrix)
	{
		this.competentieMatrix = competentieMatrix;
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
		if (obj instanceof Kerntaak)
		{
			Kerntaak other = (Kerntaak) obj;
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

}
