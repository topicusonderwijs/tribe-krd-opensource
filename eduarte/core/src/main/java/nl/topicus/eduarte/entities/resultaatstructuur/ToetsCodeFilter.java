package nl.topicus.eduarte.entities.resultaatstructuur;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import nl.topicus.cobra.templates.annotations.Exportable;
import nl.topicus.cobra.web.components.form.AutoForm;
import nl.topicus.eduarte.entities.organisatie.IOrganisatieEenheidLocatieKoppelbaarEntiteit;
import nl.topicus.eduarte.entities.organisatie.InstellingEntiteit;
import nl.topicus.eduarte.entities.personen.Medewerker;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Index;

/**
 * Een filter op 1 of meer toetscodes
 * 
 * @author papegaaij
 */
@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
@Exportable
public class ToetsCodeFilter extends InstellingEntiteit implements
		IOrganisatieEenheidLocatieKoppelbaarEntiteit<ToetsCodeFilterOrganisatieEenheidLocatie>
{
	private static final long serialVersionUID = 1L;

	@Column(nullable = false, length = 100)
	@AutoForm(htmlClasses = "unit_max")
	private String naam;

	@Column(nullable = false, length = 1000)
	@AutoForm(label = "Codes", description = "Voer een lijst van toetscodes in, gescheiden door"
		+ " komma's. De ingevoerde codes zijn hoofdletter gevoelig.", htmlClasses = "unit_max")
	private String toetsCodes;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "toetsCodeFilter")
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
	private List<ToetsCodeFilterOrganisatieEenheidLocatie> organisatieEenhedenLocaties =
		new ArrayList<ToetsCodeFilterOrganisatieEenheidLocatie>();

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "toetsCodeFilter")
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
	private List<StandaardToetsCodeFilter> standaardSelelecties =
		new ArrayList<StandaardToetsCodeFilter>();

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "medewerker", nullable = true)
	@Index(name = "idx_ToetsCodeFilter_medewerker")
	private Medewerker medewerker;

	public ToetsCodeFilter()
	{
	}

	public void setNaam(String naam)
	{
		this.naam = naam;
	}

	@Exportable
	public String getNaam()
	{
		return naam;
	}

	public void setToetsCodes(String toetsCodes)
	{
		this.toetsCodes = toetsCodes;
	}

	@Exportable
	public String getToetsCodes()
	{
		return toetsCodes;
	}

	@Exportable
	public SortedSet<String> getToetsCodesAsSet()
	{
		SortedSet<String> ret = new TreeSet<String>();
		if (getToetsCodes() == null)
			return ret;

		for (String curCode : getToetsCodes().split(","))
		{
			ret.add(curCode.trim());
		}
		return ret;
	}

	public void setToetsCodesAsSet(SortedSet<String> toetsCodesAsSet)
	{
		StringBuilder newValue = new StringBuilder();
		boolean first = true;
		for (String curCode : toetsCodesAsSet)
		{
			if (!first)
				newValue.append(',');
			first = false;
			newValue.append(curCode);
		}
		setToetsCodes(newValue.toString());
	}

	public void setMedewerker(Medewerker medewerker)
	{
		this.medewerker = medewerker;
	}

	public Medewerker getMedewerker()
	{
		return medewerker;
	}

	public void setOrganisatieEenhedenLocaties(
			List<ToetsCodeFilterOrganisatieEenheidLocatie> organisatieEenhedenLocaties)
	{
		this.organisatieEenhedenLocaties = organisatieEenhedenLocaties;
	}

	public List<ToetsCodeFilterOrganisatieEenheidLocatie> getOrganisatieEenhedenLocaties()
	{
		return organisatieEenhedenLocaties;
	}

	public void setStandaardSelelecties(List<StandaardToetsCodeFilter> standaardSelelecties)
	{
		this.standaardSelelecties = standaardSelelecties;
	}

	public List<StandaardToetsCodeFilter> getStandaardSelelecties()
	{
		return standaardSelelecties;
	}

	@Override
	public String toString()
	{
		StringBuilder ret = new StringBuilder(getNaam());
		if (getMedewerker() != null)
			ret.append(" (pers.)");
		return ret.toString();
	}

	@Override
	public List<ToetsCodeFilterOrganisatieEenheidLocatie> getOrganisatieEenheidLocatieKoppelingen()
	{
		return getOrganisatieEenhedenLocaties();
	}
}
