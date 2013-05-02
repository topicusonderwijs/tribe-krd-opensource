package nl.topicus.eduarte.entities.dbs.trajecten;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import nl.topicus.cobra.dao.helpers.IgnoreInGebruik;
import nl.topicus.eduarte.entities.organisatie.InstellingEntiteit;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.ForeignKey;
import org.hibernate.annotations.Index;

@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
public class ToegestaneStatusSoort extends InstellingEntiteit
{
	private static final long serialVersionUID = 1L;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "trajectsoort", nullable = false)
	@ForeignKey(name = "FK_ToegStatSrt_trajSrt")
	@Index(name = "idx_ToegStatSrt_trajSrt")
	@IgnoreInGebruik
	private TrajectSoort trajectsoort;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "trajectStatusSoort", nullable = false)
	@ForeignKey(name = "FK_ToegSS_trajStSrt")
	@Index(name = "idx_ToegSS_trajStSrt")
	private TrajectStatusSoort trajectStatusSoort;

	@Column(nullable = false)
	private boolean defaultStatus;

	public ToegestaneStatusSoort()
	{
	}

	public ToegestaneStatusSoort(TrajectSoort trajectSoort, TrajectStatusSoort statussoort)
	{
		setTrajectsoort(trajectSoort);
		setTrajectStatusSoort(statussoort);
	}

	public TrajectSoort getTrajectsoort()
	{
		return trajectsoort;
	}

	public void setTrajectsoort(TrajectSoort trajectsoort)
	{
		this.trajectsoort = trajectsoort;
	}

	public TrajectStatusSoort getTrajectStatusSoort()
	{
		return trajectStatusSoort;
	}

	public void setTrajectStatusSoort(TrajectStatusSoort trajectStatusSoort)
	{
		this.trajectStatusSoort = trajectStatusSoort;
	}

	public boolean isDefaultStatus()
	{
		return defaultStatus;
	}

	public void setDefaultStatus(boolean defaultStatus)
	{
		this.defaultStatus = defaultStatus;
	}
}
