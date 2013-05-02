/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.entities.organisatie;

import java.awt.Image;
import java.io.ByteArrayInputStream;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.security.RechtenSoort;
import nl.topicus.cobra.templates.annotations.Exportable;
import nl.topicus.eduarte.dao.helpers.InstellingsLogoDataAccessHelper;
import nl.topicus.eduarte.dao.helpers.OrganisatieEenheidDataAccessHelper;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Index;

/**
 * Instelling = BVE instelling. Hieraan worden deelnemers en medewerkers gekoppeld.
 * 
 * @author loite
 */
@Exportable
@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Inrichting")
public class Instelling extends Organisatie
{
	private static final long serialVersionUID = 1L;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "code", nullable = true)
	@Index(name = "idx_Instelling_brincode")
	private Brin brincode;

	@Column(nullable = true, length = 300)
	private String wikiUser;

	@Column(nullable = true, length = 300)
	private String wikiPassword;

	/**
	 * Default constructor voor Hibernate
	 */
	public Instelling()
	{
	}

	/**
	 * @see nl.topicus.eduarte.entities.organisatie.Organisatie#getRechtenSoort()
	 */
	@Override
	public RechtenSoort getRechtenSoort()
	{
		return RechtenSoort.INSTELLING;
	}

	/**
	 * @return De organisatie-eenheid die helemaal bovenaan staat in de boom van
	 *         organisatie-eenheden.
	 */
	public OrganisatieEenheid getRootOrganisatieEenheid()
	{
		return DataAccessRegistry.getHelper(OrganisatieEenheidDataAccessHelper.class)
			.getRootOrganisatieEenheid();
	}

	@Exportable
	public Brin getBrincode()
	{
		return brincode;
	}

	public void setBrincode(Brin brincode)
	{
		this.brincode = brincode;
	}

	public String getWikiUser()
	{
		return wikiUser;
	}

	public void setWikiUser(String wikiUser)
	{
		this.wikiUser = wikiUser;
	}

	public String getWikiPassword()
	{
		return wikiPassword;
	}

	public void setWikiPassword(String wikiPassword)
	{
		this.wikiPassword = wikiPassword;
	}

	public Image getInstellingslogo()
	{
		Image curFoto = null;
		try
		{
			InstellingsLogo logo =
				DataAccessRegistry.getHelper(InstellingsLogoDataAccessHelper.class)
					.getInstellingsLogo();
			if (logo != null)
			{
				byte[] afbeelding = logo.getLogo().getBestand();
				if (afbeelding != null && afbeelding.length > 0)
				{
					curFoto = ImageIO.read(new ByteArrayInputStream(afbeelding));
				}
			}
		}
		catch (IOException e)
		{
			// ignore, we kunnen hier toch niets doen.
		}
		return curFoto;
	}

}
