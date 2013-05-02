package nl.topicus.eduarte.entities.taxonomie.mbo.cgo;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;

import nl.topicus.cobra.util.Asserts;
import nl.topicus.eduarte.entities.organisatie.EntiteitContext;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * Kwalificatiedossiers voor het CGO.
 * 
 * @author loite
 */
@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Landelijk")
public class Kwalificatiedossier extends CompetentieMatrix
{
	private static final long serialVersionUID = 1L;

	@Column(nullable = true)
	private Integer nummer;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "dossier")
	private Set<Vaardigheid> vaardigheden = new HashSet<Vaardigheid>();

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "dossier")
	private List<Uitstroom> uitstromen = new ArrayList<Uitstroom>();

	public Kwalificatiedossier()
	{
	}

	public Kwalificatiedossier(EntiteitContext context)
	{
		super(context);
	}

	public Integer getNummer()
	{
		return nummer;
	}

	public Set<Vaardigheid> getVaardigheden()
	{
		return vaardigheden;
	}

	public void setNummer(Integer nummer)
	{
		Asserts.assertNotNull("nummer", nummer);
		this.nummer = nummer;
	}

	public void addVaardigheid(Vaardigheid vaardigheid)
	{
		getVaardigheden().add(vaardigheid);
	}

	public void addUitstroom(Uitstroom uitstroom)
	{
		Asserts.assertNotNull("uitstroom", uitstroom);
		getUitstromen().add(uitstroom);
	}

	public List<Uitstroom> getUitstromen()
	{
		return uitstromen;
	}

	public void setVaardigheden(Set<Vaardigheid> vaardigheden)
	{
		this.vaardigheden = vaardigheden;
	}

	public void setUitstromen(List<Uitstroom> uitstromen)
	{
		this.uitstromen = uitstromen;
	}

	/**
	 * @return Een string die aangeeft welke kerntaken en werkprocessen binnen dit dossier
	 *         gedefinieerd zijn. De string ziet er als volgt uit:
	 *         90000.1.1-90000.1.2-90000.1
	 *         .3-90000.2.1-90000.2.2-90000.2.3-90000.3.1-90000.3.2
	 */
	public String getKerntakenWerkprocessenKey()
	{
		SortedSet<String> werkprocessen = new TreeSet<String>();
		for (Uitstroom uitstroom : getUitstromen())
		{
			for (Kerntaak kerntaak : uitstroom.getKerntaken())
			{
				for (Werkproces wp : kerntaak.getWerkprocessen())
				{
					werkprocessen.add(uitstroom.getExterneCode() + "." + kerntaak.getCode() + "."
						+ wp.getCode());
				}
			}
		}
		StringBuilder res = new StringBuilder();
		for (String wp : werkprocessen)
		{
			if (res.length() > 0)
				res.append("-");
			res.append(wp);
		}
		return res.toString();
	}

	@Override
	public String getType()
	{
		return "Kwalificatiedossier";
	}

}
