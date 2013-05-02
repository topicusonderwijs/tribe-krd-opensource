/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.entities.organisatie;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;

import nl.topicus.cobra.entities.RestrictedAccess;
import nl.topicus.cobra.util.Asserts;
import nl.topicus.cobra.web.components.form.AutoForm;
import nl.topicus.eduarte.app.EduArteContext;
import nl.topicus.eduarte.entities.Entiteit;

import org.hibernate.annotations.Index;

/**
 * @author loite
 */
@MappedSuperclass()
public abstract class LandelijkOfInstellingEntiteit extends Entiteit
{
	private static final long serialVersionUID = 1L;

	/**
	 * De instelling waarbij deze entiteit hoort.
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "organisatie", nullable = true)
	@AutoForm(include = false)
	@RestrictedAccess(hasSetter = false)
	@Index(name = "GENERATED_NAME")
	private Instelling organisatie;

	/**
	 * Het id van deze entiteit in het oude pakket (bijvoorbeeld nOISe). Wordt vooral
	 * gebruikt tijdens de conversie.
	 */
	@Column(nullable = true)
	@AutoForm(include = false)
	private Long idInOudPakket;

	protected LandelijkOfInstellingEntiteit()
	{
		organisatie = EduArteContext.get().getInstelling();
	}

	protected LandelijkOfInstellingEntiteit(boolean landelijk)
	{
		setLandelijk(landelijk);
	}

	public LandelijkOfInstellingEntiteit(EntiteitContext context)
	{
		setLandelijk(context.isLandelijk());
	}

	/**
	 * Geeft aan of deze entiteit een landelijke of een instellingsspecifieke entiteit is.
	 * 
	 * @return true als getOrganisatie() null teruggeeft, en anders false.
	 */
	@Override
	public final boolean isLandelijk()
	{
		return getOrganisatie() == null;
	}

	private void setLandelijk(boolean landelijk)
	{
		if (!landelijk && EduArteContext.get().getInstelling() == null)
			throw new IllegalArgumentException(
				"Kan geen instelling-specifieke entiteit maken als de instelling niet gezet is.");
		setOrganisatie(landelijk ? null : EduArteContext.get().getInstelling());
	}

	/**
	 * <strong>Let op:</strong> Geeft een INSTELLING terug
	 * 
	 * @return Returns the instelling.
	 */
	public Instelling getOrganisatie()
	{
		return organisatie;
	}

	void setOrganisatie(Instelling organisatie)
	{
		if (organisatie != null)
			Asserts.assertEquals("organisatie", organisatie, EduArteContext.get().getInstelling());
		this.organisatie = organisatie;
	}

	public void setIdInOudPakket(Long idInOudPakket)
	{
		this.idInOudPakket = idInOudPakket;
	}

	public Long getIdInOudPakket()
	{
		return idInOudPakket;
	}
}
