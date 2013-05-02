package nl.topicus.eduarte.entities.signalering;

import java.util.Date;

import javax.persistence.*;

import nl.topicus.eduarte.entities.organisatie.InstellingEntiteit;
import nl.topicus.eduarte.entities.personen.Persoon;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Index;

@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class Signaal extends InstellingEntiteit
{
	private static final long serialVersionUID = 1L;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "event", nullable = false)
	@Index(name = "idx_Signaal_event")
	private Event event;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ontvanger", nullable = false)
	@Index(name = "idx_Signaal_ontvanger")
	private Persoon ontvanger;

	@Temporal(value = TemporalType.TIMESTAMP)
	@Column(nullable = true)
	private Date datumGelezen;

	public Signaal()
	{
	}

	public Signaal(Event event, Persoon ontvanger)
	{
		setEvent(event);
		setOntvanger(ontvanger);
	}

	public Date getDatumGelezen()
	{
		return datumGelezen;
	}

	public void setDatumGelezen(Date datumGelezen)
	{
		this.datumGelezen = datumGelezen;
	}

	public Event getEvent()
	{
		return event;
	}

	public void setEvent(Event event)
	{
		this.event = event;
	}

	public Persoon getOntvanger()
	{
		return ontvanger;
	}

	public void setOntvanger(Persoon ontvanger)
	{
		this.ontvanger = ontvanger;
	}
}
