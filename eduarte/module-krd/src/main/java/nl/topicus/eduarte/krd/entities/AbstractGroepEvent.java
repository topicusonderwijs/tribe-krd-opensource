package nl.topicus.eduarte.krd.entities;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;

import nl.topicus.eduarte.entities.signalering.Event;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
public abstract class AbstractGroepEvent extends Event
{
	private static final long serialVersionUID = 1L;

	@Basic(optional = false)
	@Column(nullable = true)
	private Long groepId;

	public AbstractGroepEvent()
	{
	}

	public Long getGroepId()
	{
		return groepId;
	}

	public void setGroepId(Long groepId)
	{
		this.groepId = groepId;
	}
}
