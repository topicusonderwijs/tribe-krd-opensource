package nl.topicus.eduarte.entities.participatie;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.entities.IActiefEntiteit;
import nl.topicus.eduarte.dao.participatie.helpers.MaatregelDataAccessHelper;
import nl.topicus.eduarte.entities.organisatie.InstellingEntiteit;
import nl.topicus.eduarte.entities.organisatie.Locatie;
import nl.topicus.eduarte.entities.organisatie.OrganisatieEenheid;
import nl.topicus.eduarte.providers.OrganisatieEenheidLocatieProvider;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Index;

/**
 * @author vandekamp
 */
@Entity
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
public class Maatregel extends InstellingEntiteit implements OrganisatieEenheidLocatieProvider,
		IActiefEntiteit
{
	private static final long serialVersionUID = 1L;

	@Column(length = 50, nullable = false)
	private String omschrijving;

	@Column(nullable = false)
	private boolean actief;

	/**
	 * Boolean die aangeeft of automatisch aangemaakte maatregelen getoond moeten worden
	 * na het aanmaken. Dit betekent dat er een scherm 'tussenkomt' op het moment dat een
	 * gebruiker een absentiemelding invoert die een maatregel veroorzaakt.
	 */
	@Column(nullable = false)
	private boolean automatischeMaatregelTonen;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "organisatieEenheid", nullable = false)
	@Index(name = "idx_Maatregel_organEenheid")
	private OrganisatieEenheid organisatieEenheid;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "locatie", nullable = true)
	@Index(name = "idx_Maatregel_locatie")
	private Locatie locatie;

	@BatchSize(size = 20)
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "maatregel")
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
	private List<MaatregelToekenningsRegel> maatregelToekenningsRegels;

	public Maatregel()
	{
	}

	public String getOmschrijving()
	{
		return omschrijving;
	}

	public void setOmschrijving(String omschrijving)
	{
		this.omschrijving = omschrijving;
	}

	public boolean isActief()
	{
		return actief;
	}

	public void setActief(boolean actief)
	{
		this.actief = actief;
	}

	public boolean isAutomatischeMaatregelTonen()
	{
		return automatischeMaatregelTonen;
	}

	public void setAutomatischeMaatregelTonen(boolean automatischeMaatregelTonen)
	{
		this.automatischeMaatregelTonen = automatischeMaatregelTonen;
	}

	public OrganisatieEenheid getOrganisatieEenheid()
	{
		return organisatieEenheid;
	}

	public void setOrganisatieEenheid(OrganisatieEenheid organisatieEenheid)
	{
		this.organisatieEenheid = organisatieEenheid;
	}

	public List<MaatregelToekenningsRegel> getMaatregelToekenningsRegels()
	{
		if (maatregelToekenningsRegels != null)
			return maatregelToekenningsRegels;
		return new ArrayList<MaatregelToekenningsRegel>();

	}

	public void setMaatregelToekenningsRegels(
			List<MaatregelToekenningsRegel> maatregelToekenningsRegels)
	{
		this.maatregelToekenningsRegels = maatregelToekenningsRegels;
	}

	/**
	 * @return true als de omschrijving al bestaat en actief is bij de meegegeven of
	 *         hogere of lagere of organisatieeenheid. anders false
	 */
	public boolean bestaatEnActief()
	{
		MaatregelDataAccessHelper helper =
			DataAccessRegistry.getHelper(MaatregelDataAccessHelper.class);
		return helper.bestaatEnActief(this);
	}

	/**
	 * @return true als de organisatie-eenheid is toegestaan, anders false
	 */
	public boolean isOrganisatieEenheidToegestaan()
	{
		for (MaatregelToekenningsRegel maatregelTR : getMaatregelToekenningsRegels())
		{
			if (maatregelTR.getOrganisatieEenheid() != null
				&& !getOrganisatieEenheid().isParentOf(maatregelTR.getOrganisatieEenheid()))
				return false;
		}
		return true;
	}

	public void setLocatie(Locatie locatie)
	{
		this.locatie = locatie;
	}

	public Locatie getLocatie()
	{
		return locatie;
	}
}
