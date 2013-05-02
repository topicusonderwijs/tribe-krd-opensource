package nl.topicus.eduarte.entities.dbs.trajecten.templates;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import nl.topicus.eduarte.entities.bijlage.BijlageEntiteit;
import nl.topicus.eduarte.entities.bijlage.IBijlageKoppelEntiteit;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Index;

@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
public class TrajectTemplateBijlage extends BijlageEntiteit
{

	private static final long serialVersionUID = 1L;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = true, name = "trajectTemplate")
	@Index(name = "idx_TrajectTemplateBijlage_template")
	private TrajectTemplate trajectTemplate;

	public TrajectTemplateBijlage()
	{
	}

	public TrajectTemplate getTrajectTemplate()
	{
		return trajectTemplate;
	}

	public void setTrajectTemplate(TrajectTemplate trajectTemplate)
	{
		this.trajectTemplate = trajectTemplate;
	}

	@Override
	public IBijlageKoppelEntiteit< ? extends BijlageEntiteit> getEntiteit()
	{
		return getTrajectTemplate();
	}

}
