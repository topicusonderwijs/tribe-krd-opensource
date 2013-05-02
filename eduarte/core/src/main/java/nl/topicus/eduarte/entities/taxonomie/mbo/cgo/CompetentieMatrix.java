package nl.topicus.eduarte.entities.taxonomie.mbo.cgo;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;

import nl.topicus.eduarte.entities.organisatie.EntiteitContext;
import nl.topicus.eduarte.entities.taxonomie.mbo.AbstractMBOVerbintenisgebied;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Landelijk")
public abstract class CompetentieMatrix extends AbstractMBOVerbintenisgebied
{
	private static final long serialVersionUID = 1L;

	@Column(nullable = true)
	private String titel;

	@OneToMany(mappedBy = "competentieMatrix", fetch = FetchType.LAZY)
	@BatchSize(size = 20)
	@OrderBy("volgnummer ASC")
	private List<Kerntaak> kerntaken = new ArrayList<Kerntaak>();

	public CompetentieMatrix()
	{
	}

	public CompetentieMatrix(EntiteitContext context)
	{
		super(context);
	}

	public List<Kerntaak> getKerntaken()
	{
		return kerntaken;
	}

	public void setKerntaken(List<Kerntaak> kerntaken)
	{
		this.kerntaken = kerntaken;
	}

	public void addKerntaak(Kerntaak kerntaak)
	{
		getKerntaken().add(kerntaak);
	}

	public SortedSet<Competentie> getCompetenties()
	{
		SortedSet<Competentie> ret = new TreeSet<Competentie>(new Comparator<Competentie>()
		{
			@Override
			public int compare(Competentie o1, Competentie o2)
			{
				return o1.equals(o2) ? 0 : o1.getCode().compareTo(o2.getCode());
			}
		});

		for (Kerntaak kt : getKerntaken())
		{
			for (Werkproces wp : kt.getWerkprocessen())
			{
				for (Leerpunt curLp : wp.getLeerpunten())
				{
					ret.add(curLp.getCompetentie());
				}
			}
		}
		return ret;
	}

	public List<Competentie> getCompetentiesAsList()
	{
		return new ArrayList<Competentie>(getCompetenties());
	}

	public List<Leerpunt> getLeerpunten(Kerntaak kerntaak)
	{
		List<Leerpunt> ret = new ArrayList<Leerpunt>();
		for (Kerntaak kt : getKerntaken())
		{
			if (kt.equals(kerntaak))
			{
				for (Werkproces wp : kt.getWerkprocessen())
				{
					ret.addAll(wp.getLeerpunten());
				}
			}
		}
		return ret;
	}

	public List<Leerpunt> getLeerpunten(Kerntaak kerntaak, Competentie competentie)
	{
		List<Leerpunt> ret = new ArrayList<Leerpunt>();
		for (Kerntaak kt : getKerntaken())
		{
			if (kt.equals(kerntaak))
			{
				for (Werkproces wp : kt.getWerkprocessen())
				{
					for (Leerpunt lp : wp.getLeerpunten())
					{
						if (lp.getCompetentie().equals(competentie))
						{
							ret.add(lp);
						}
					}
				}
			}
		}
		return ret;
	}

	public Map<Competentie, Leerpunt> getLeerpunten(Werkproces werkproces)
	{
		Map<Competentie, Leerpunt> ret = new HashMap<Competentie, Leerpunt>();
		for (Kerntaak kt : getKerntaken())
		{
			for (Werkproces wp : kt.getWerkprocessen())
			{
				if (wp.equals(werkproces))
				{
					for (Leerpunt lp : wp.getLeerpunten())
					{
						ret.put(lp.getCompetentie(), lp);
					}
				}
			}
		}
		return ret;
	}

	abstract public String getType();

	public String getTitel()
	{
		return titel;
	}

	public void setTitel(String titel)
	{
		this.titel = titel;
	}
}
