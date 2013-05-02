package nl.topicus.eduarte.entities.participatie;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;

import nl.topicus.eduarte.entities.organisatie.InstellingEntiteit;
import nl.topicus.eduarte.entities.organisatie.OrganisatieEenheid;
import nl.topicus.eduarte.entities.sidebar.IContextInfoObject;
import nl.topicus.eduarte.providers.OrganisatieEenheidProvider;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Index;

/**
 * Een basisrooster geeft aan wat de globale planning voor het gehele schooljaar of voor
 * een periode is.
 * 
 * @author loite
 */
@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
public class Basisrooster extends InstellingEntiteit implements OrganisatieEenheidProvider,
		IContextInfoObject
{
	private static final long serialVersionUID = 1L;

	@Column(length = 60, nullable = false)
	private String naam;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "organisatieEenheid", nullable = false)
	@Index(name = "idx_Basisrooster_organisatieE")
	private OrganisatieEenheid organisatieEenheid;

	/**
	 * Het externe systeem waar dit rooster gedefinieerd is.
	 */
	@Column(nullable = true)
	@Enumerated(EnumType.STRING)
	private ExternSysteem externSysteem;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "basisrooster")
	private List<Vakantie> vakanties;

	public Basisrooster()
	{
	}

	public Basisrooster(OrganisatieEenheid organisatieEenheid, String naam)
	{
		setOrganisatieEenheid(organisatieEenheid);
		setNaam(naam);
	}

	public String getNaam()
	{
		return naam;
	}

	public void setNaam(String naam)
	{
		this.naam = naam;
	}

	public OrganisatieEenheid getOrganisatieEenheid()
	{
		return organisatieEenheid;
	}

	public void setOrganisatieEenheid(OrganisatieEenheid organisatieEenheid)
	{
		this.organisatieEenheid = organisatieEenheid;
	}

	public ExternSysteem getExternSysteem()
	{
		return externSysteem;
	}

	public void setExternSysteem(ExternSysteem externSysteem)
	{
		this.externSysteem = externSysteem;
	}

	public List<Vakantie> getVakanties()
	{
		if (vakanties == null)
			vakanties = new ArrayList<Vakantie>();
		return vakanties;
	}

	public void setVakanties(List<Vakantie> vakanties)
	{
		this.vakanties = vakanties;
	}

	/**
	 * @param vakantieNaam
	 * @return De vakantie met de gegeven naam.
	 */
	public Vakantie getVakantie(String vakantieNaam)
	{
		for (Vakantie vakantie : getVakanties())
		{
			if (vakantie.getNaam().equalsIgnoreCase(vakantieNaam))
			{
				return vakantie;
			}
		}
		return null;
	}

	@Override
	public String toString()
	{
		return getNaam();
	}

	private void batchDeleteVakanties()
	{
		for (Vakantie vakantie : getVakanties())
		{
			vakantie.delete();
		}
	}

	@Override
	public void delete()
	{
		batchDeleteVakanties();
		super.delete();
	}

	@Override
	public String getContextInfoOmschrijving()
	{
		return "Basisrooster Afspraken";
	}

}
