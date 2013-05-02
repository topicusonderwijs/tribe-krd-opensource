package nl.topicus.eduarte.entities.dbs.trajecten.templates;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import nl.topicus.cobra.dao.helpers.IgnoreInGebruik;
import nl.topicus.eduarte.entities.organisatie.InstellingEntiteit;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.ForeignKey;
import org.hibernate.annotations.Index;

/**
 * @author maatman
 */
@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
@Table(name = "TrajectBegHandelingTemplate")
public class TrajectBegeleidingsHandelingTemplate extends InstellingEntiteit
{
	private static final long serialVersionUID = 1L;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "trajectTemplate", nullable = false)
	@ForeignKey(name = "FK_trajectBegHT_traject")
	@Index(name = "idx_trajectBegHT_traject")
	@IgnoreInGebruik
	private TrajectTemplate trajectTemplate;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "begeleidingsHandeling", nullable = false)
	@ForeignKey(name = "FK_trajectBegHT_handeling")
	@Index(name = "idx_trajectBegHT_handeling")
	private BegeleidingsHandelingTemplate begeleidingsHandeling;

	public TrajectBegeleidingsHandelingTemplate()
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

	public BegeleidingsHandelingTemplate getBegeleidingsHandeling()
	{
		return begeleidingsHandeling;
	}

	public void setBegeleidingsHandeling(BegeleidingsHandelingTemplate begeleidingsHandeling)
	{
		this.begeleidingsHandeling = begeleidingsHandeling;
	}
}
