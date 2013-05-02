package nl.topicus.eduarte.entities.participatie;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.*;

import nl.topicus.eduarte.entities.organisatie.InstellingEntiteit;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Index;

@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
public class CacheRegion extends InstellingEntiteit
{
	private static final long serialVersionUID = 1L;

	@Temporal(value = TemporalType.TIMESTAMP)
	@Column(nullable = false)
	private Date lastUpdate;

	@Temporal(value = TemporalType.DATE)
	@Column(nullable = false)
	private Date regionStartDate;

	@Temporal(value = TemporalType.DATE)
	@Column(nullable = false)
	private Date regionEndDate;

	@Column(nullable = false)
	private boolean dirty;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "cacheRegion")
	private List<Afspraak> afspraken = new ArrayList<Afspraak>();

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "externeAgenda", nullable = false)
	@Index(name = "idx_CacheRegion_externeAgenda")
	private ExterneAgenda externeAgenda;

	public CacheRegion()
	{
	}

	public void setAfspraken(List<Afspraak> afspraken)
	{
		this.afspraken = afspraken;
	}

	public List<Afspraak> getAfspraken()
	{
		return afspraken;
	}

	public void setExterneAgenda(ExterneAgenda externeAgenda)
	{
		this.externeAgenda = externeAgenda;
	}

	public ExterneAgenda getExterneAgenda()
	{
		return externeAgenda;
	}

	public void setLastUpdate(Date lastUpdate)
	{
		this.lastUpdate = lastUpdate;
	}

	public Date getLastUpdate()
	{
		return lastUpdate;
	}

	public void setDirty(boolean dirty)
	{
		this.dirty = dirty;
	}

	public boolean isDirty()
	{
		return dirty;
	}

	public Date getRegionStartDate()
	{
		return regionStartDate;
	}

	public void setRegionStartDate(Date regionStartDate)
	{
		this.regionStartDate = regionStartDate;
	}

	public Date getRegionEndDate()
	{
		return regionEndDate;
	}

	public void setRegionEndDate(Date regionEndDate)
	{
		this.regionEndDate = regionEndDate;
	}

	public boolean isValid()
	{
		return !dirty
			&& System.currentTimeMillis() - getExterneAgenda().getKoppeling().getGeldigheidsduur()
				* 60000 < getLastUpdate().getTime();
	}
}
