/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.entities.signalering.settings;

import javax.persistence.*;

import nl.topicus.eduarte.app.signalering.EventAbonnementInstelling;
import nl.topicus.eduarte.app.signalering.EventAbonnementType;
import nl.topicus.eduarte.entities.organisatie.InstellingEntiteit;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Index;

/**
 * @author loite
 */
@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public abstract class EventAbonnementSetting extends InstellingEntiteit
{
	private static final long serialVersionUID = 1L;

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private EventAbonnementInstelling waarde;

	@Column(nullable = false, length = 200)
	private String eventClassname;

	@Column(nullable = false, length = 200)
	private String transportClassname;

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private EventAbonnementType type;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "configuratie", nullable = true)
	@Index(name = "idx_EventAbbSet_configuratie")
	private AbstractEventAbonnementConfiguration< ? > configuratie;

	public EventAbonnementSetting()
	{
	}

	public EventAbonnementInstelling getWaarde()
	{
		return waarde;
	}

	public void setWaarde(EventAbonnementInstelling waarde)
	{
		this.waarde = waarde;
	}

	public EventAbonnementType getType()
	{
		return type;
	}

	public void setType(EventAbonnementType type)
	{
		this.type = type;
	}

	public void setEventClassname(String eventClassname)
	{
		this.eventClassname = eventClassname;
	}

	public String getEventClassname()
	{
		return eventClassname;
	}

	public void setTransportClassname(String transportClassname)
	{
		this.transportClassname = transportClassname;
	}

	public String getTransportClassname()
	{
		return transportClassname;
	}

	public AbstractEventAbonnementConfiguration< ? > getConfiguratie()
	{
		return configuratie;
	}

	public void setConfiguratie(AbstractEventAbonnementConfiguration< ? > configuratie)
	{
		this.configuratie = configuratie;
	}
}
