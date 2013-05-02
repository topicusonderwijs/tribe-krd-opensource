/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.entities.vrijevelden;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import nl.topicus.eduarte.entities.VrijVeldable;
import nl.topicus.eduarte.entities.organisatie.ExterneOrganisatie;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Index;

/**
 * @author vandekamp
 */
@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
public class ExterneOrganisatieVrijVeld extends VrijVeldEntiteit
{
	private static final long serialVersionUID = 1L;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "externeOrganisatie", nullable = true)
	@Index(name = "idx_EOVV_externeOrg")
	private ExterneOrganisatie externeOrganisatie;

	public ExterneOrganisatieVrijVeld()
	{
	}

	@Override
	public VrijVeldable< ? extends VrijVeldEntiteit> getEntiteit()
	{
		return getExterneOrganisatie();
	}

	@Override
	public void setEntiteit(VrijVeldable< ? extends VrijVeldEntiteit> entiteit)
	{
		setExterneOrganisatie((ExterneOrganisatie) entiteit);
	}

	public void setExterneOrganisatie(ExterneOrganisatie externeOrganisatie)
	{
		this.externeOrganisatie = externeOrganisatie;
	}

	public ExterneOrganisatie getExterneOrganisatie()
	{
		return externeOrganisatie;
	}
}
