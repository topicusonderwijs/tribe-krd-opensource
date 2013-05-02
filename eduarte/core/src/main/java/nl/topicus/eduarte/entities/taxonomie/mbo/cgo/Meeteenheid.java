package nl.topicus.eduarte.entities.taxonomie.mbo.cgo;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;

import nl.topicus.cobra.util.Asserts;
import nl.topicus.eduarte.entities.organisatie.EntiteitContext;
import nl.topicus.eduarte.entities.organisatie.LandelijkOfInstellingEntiteit;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * Meeteenheid omschrijft hoe competenties gemeten kunnen worden
 * 
 * @author vandenbrink
 */
@Entity
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
public class Meeteenheid extends LandelijkOfInstellingEntiteit
{
	private static final long serialVersionUID = 1L;

	@Column(length = 128, nullable = false)
	private String naam;

	@Column(length = 128, nullable = false)
	private String omschrijving;

	@OneToMany(mappedBy = "meeteenheid")
	@OrderBy("waarde asc")
	private List<MeeteenheidWaarde> meeteenheidWaardes = new ArrayList<MeeteenheidWaarde>();

	protected Meeteenheid()
	{
	}

	public Meeteenheid(EntiteitContext context)
	{
		super(context);
	}

	/**
	 * @return naam
	 */
	public String getNaam()
	{
		return naam;
	}

	/**
	 * @param naam
	 */
	public void setNaam(String naam)
	{
		this.naam = naam;
	}

	/**
	 * @return omschrijving
	 */
	public String getOmschrijving()
	{
		return omschrijving;
	}

	/**
	 * @param omschrijving
	 */
	public void setOmschrijving(String omschrijving)
	{
		this.omschrijving = omschrijving;
	}

	/**
	 * @return De toegestane meeteenheidwaardes
	 */
	public List<MeeteenheidWaarde> getMeeteenheidWaardes()
	{
		return meeteenheidWaardes;
	}

	/**
	 * @param meeteenheidWaardes
	 */
	public void setMeeteenheidWaardes(List<MeeteenheidWaarde> meeteenheidWaardes)
	{
		this.meeteenheidWaardes = meeteenheidWaardes;
	}

	/**
	 * @param meeteenheidWaarde
	 */
	public void addMeeteenheidWaarde(MeeteenheidWaarde meeteenheidWaarde)
	{
		Asserts.assertNotNull("meeteenheidWaarde", meeteenheidWaarde);
		getMeeteenheidWaardes().add(meeteenheidWaarde);
	}

	/**
	 * @param meeteenheidWaarde
	 */
	public void removeMeeteenheidWaarde(MeeteenheidWaarde meeteenheidWaarde)
	{
		Asserts.assertNotNull("meeteenheidWaarde", meeteenheidWaarde);
		getMeeteenheidWaardes().remove(meeteenheidWaarde);
	}

	public MeeteenheidWaarde getMeeteenheidWaarde(int waarde)
	{
		MeeteenheidWaarde ret = getMeeteenheidWaardeOrNull(waarde);
		if (ret != null)
			return ret;
		throw new IllegalArgumentException("Gegeven waarde bestaat niet: " + waarde);
	}

	public MeeteenheidWaarde getMeeteenheidWaardeOrNull(int waarde)
	{
		for (MeeteenheidWaarde curWaarde : getMeeteenheidWaardes())
		{
			if (curWaarde.getWaarde() == waarde)
			{
				return curWaarde;
			}
		}
		return null;
	}

	public MeeteenheidWaarde getMaxWaarde()
	{
		MeeteenheidWaarde maxWaarde = null;

		for (MeeteenheidWaarde curWaarde : getMeeteenheidWaardes())
		{
			if (maxWaarde == null || curWaarde.getWaarde() > maxWaarde.getWaarde())
			{
				maxWaarde = curWaarde;
			}
		}
		return maxWaarde;
	}

	public MeeteenheidWaarde getMinWaarde()
	{
		MeeteenheidWaarde minWaarde = null;

		for (MeeteenheidWaarde curWaarde : getMeeteenheidWaardes())
		{
			if (minWaarde == null || curWaarde.getWaarde() < minWaarde.getWaarde())
			{
				minWaarde = curWaarde;
			}
		}
		return minWaarde;
	}

}
