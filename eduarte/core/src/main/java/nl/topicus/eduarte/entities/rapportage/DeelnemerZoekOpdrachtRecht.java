/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.entities.rapportage;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import nl.topicus.eduarte.entities.organisatie.InstellingEntiteit;
import nl.topicus.eduarte.entities.security.authorization.Rol;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Index;

@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
public class DeelnemerZoekOpdrachtRecht extends InstellingEntiteit
{
	private static final long serialVersionUID = 1L;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false, name = "rol")
	@Index(name = "idx_DZORecht_rol")
	private Rol rol;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false, name = "zoekOpdracht")
	@Index(name = "idx_DZORecht_zoekOpdracht")
	private DeelnemerZoekOpdracht zoekOpdracht;

	public DeelnemerZoekOpdrachtRecht()
	{
	}

	public DeelnemerZoekOpdrachtRecht(DeelnemerZoekOpdracht zoekOpdracht)
	{
		setZoekOpdracht(zoekOpdracht);
	}

	public Rol getRol()
	{
		return rol;
	}

	public void setRol(Rol rol)
	{
		this.rol = rol;
	}

	public DeelnemerZoekOpdracht getZoekOpdracht()
	{
		return zoekOpdracht;
	}

	public void setZoekOpdracht(DeelnemerZoekOpdracht zoekOpdracht)
	{
		this.zoekOpdracht = zoekOpdracht;
	}
}
