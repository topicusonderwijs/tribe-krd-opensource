package nl.topicus.eduarte.entities.signalering.events;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;

import nl.topicus.cobra.dao.BatchDataAccessHelper;
import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.eduarte.entities.personen.Deelnemer;
import nl.topicus.eduarte.entities.signalering.Event;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
public abstract class AbstractDeelnemerEvent extends Event
{
	private static final long serialVersionUID = 1L;

	@Basic(optional = false)
	@Column(name = "deelnemerId", nullable = true)
	private Long deelnemerId;

	public AbstractDeelnemerEvent()
	{
	}

	public Long getDeelnemerId()
	{
		return deelnemerId;
	}

	public void setDeelnemerId(Long deelnemerId)
	{
		this.deelnemerId = deelnemerId;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Deelnemer getDeelnemer()
	{
		BatchDataAccessHelper<Deelnemer> helper =
			DataAccessRegistry.getHelper(BatchDataAccessHelper.class);
		return helper.get(Deelnemer.class, getDeelnemerId());
	}
}
