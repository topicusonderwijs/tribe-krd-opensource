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
 * Koppeling tussen een Competentie en een Werkproces. Correspondeert met een cel uit een
 * competentiematrix.
 */
@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Landelijk")
@BatchSize(size = 1000)
public class Leerpunt extends Deelgebied
{
	private static final long serialVersionUID = 1L;

	@Lob
	@Column(nullable = true)
	private String indicator;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "werkproces", nullable = true)
	@Index(name = "idx_leerpunt_werkproces")
	private Werkproces werkproces;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "competentie", nullable = true)
	@Index(name = "idx_leerpunt_competentie")
	private Competentie competentie;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "leerpunt")
	private List<LeerpuntVaardigheid> leerpuntVaardigheden = new ArrayList<LeerpuntVaardigheid>();

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "leerpunt")
	private List<LeerpuntComponent> leerpuntComponenten = new ArrayList<LeerpuntComponent>();

	public Leerpunt()
	{
		super();
	}

	public Leerpunt(EntiteitContext context)
	{
		super(context);
	}

	public String getIndicator()
	{
		return indicator;
	}

	public void setIndicator(String indicator)
	{
		this.indicator = indicator;
	}

	public Werkproces getWerkproces()
	{
		return werkproces;
	}

	public void setWerkproces(Werkproces werkproces)
	{
		this.werkproces = werkproces;
	}

	public void setCompetentie(Competentie competentie)
	{
		this.competentie = competentie;
	}

	public Competentie getCompetentie()
	{
		return competentie;
	}

	public List<LeerpuntVaardigheid> getLeerpuntVaardigheden()
	{
		return leerpuntVaardigheden;
	}

	public void setLeerpuntVaardigheden(List<LeerpuntVaardigheid> leerpuntVaardigheden)
	{
		this.leerpuntVaardigheden = leerpuntVaardigheden;
	}

	public List<LeerpuntComponent> getLeerpuntComponenten()
	{
		return leerpuntComponenten;
	}

	public void setLeerpuntComponenten(List<LeerpuntComponent> leerpuntComponenten)
	{
		this.leerpuntComponenten = leerpuntComponenten;
	}

	public void addCompetentieComponent(CompetentieComponent component)
	{
		LeerpuntComponent lpc = new LeerpuntComponent();
		lpc.setCompetentieComponent(component);
		lpc.setLeerpunt(this);
		getLeerpuntComponenten().add(lpc);
	}

	public void addVaardigheid(Vaardigheid vaardigheid)
	{
		LeerpuntVaardigheid lpv = new LeerpuntVaardigheid();
		lpv.setLeerpunt(this);
		lpv.setVaardigheid(vaardigheid);
		getLeerpuntVaardigheden().add(lpv);
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
