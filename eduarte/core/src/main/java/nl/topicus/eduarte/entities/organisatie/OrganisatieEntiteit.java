/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.entities.organisatie;

import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;

import nl.topicus.cobra.entities.RestrictedAccess;
import nl.topicus.cobra.web.components.form.AutoForm;
import nl.topicus.eduarte.app.EduArteContext;
import nl.topicus.eduarte.entities.Entiteit;

import org.hibernate.annotations.Index;

/**
 * Base class voor alle entiteiten die bij een organisatie horen. In de database wordt
 * automatisch een verwijzing naar de juiste organisatie opgenomen.
 * 
 * @author loite
 */
@MappedSuperclass()
public abstract class OrganisatieEntiteit extends Entiteit implements IOrganisatieEntiteit
{
	private static final long serialVersionUID = 1L;

	/**
	 * De organisatie waarbij deze entiteit hoort.
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "organisatie", nullable = false)
	@AutoForm(include = false, readOnly = true)
	@RestrictedAccess(hasSetter = false)
	@Index(name = "GENERATED_NAME")
	private Organisatie organisatie;

	public OrganisatieEntiteit()
	{
		organisatie = EduArteContext.get().getOrganisatie();
	}

	public Organisatie getOrganisatie()
	{
		return organisatie;
	}

	@Override
	public final boolean isLandelijk()
	{
		return false;
	}
}
