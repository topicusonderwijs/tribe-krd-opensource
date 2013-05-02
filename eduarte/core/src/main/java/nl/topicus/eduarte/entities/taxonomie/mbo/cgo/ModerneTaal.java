package nl.topicus.eduarte.entities.taxonomie.mbo.cgo;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;

import nl.topicus.eduarte.entities.organisatie.EntiteitContext;
import nl.topicus.eduarte.entities.organisatie.LandelijkOfInstellingEntiteit;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * @author vandenbrink
 */
@Entity
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
public class ModerneTaal extends LandelijkOfInstellingEntiteit
{
	private static final long serialVersionUID = 1L;

	private String omschrijving;

	private String afkorting;

	private String code;

	@Column(nullable = false)
	private Boolean voorgedefinieerd = false;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "taal")
	private List<TaalTypeKoppel> gekoppeldeTaalTypes = new ArrayList<TaalTypeKoppel>();

	protected ModerneTaal()
	{
	}

	public ModerneTaal(EntiteitContext context)
	{
		super(context);
	}

	public String getOmschrijving()
	{
		return omschrijving;
	}

	public void setOmschrijving(String omschrijving)
	{
		this.omschrijving = omschrijving;
	}

	public String getAfkorting()
	{
		return afkorting;
	}

	public void setAfkorting(String afkorting)
	{
		this.afkorting = afkorting;
	}

	public String getCode()
	{
		return code;
	}

	public void setCode(String code)
	{
		this.code = code;
	}

	public List<TaalTypeKoppel> getGekoppeldeTaalTypes()
	{
		return gekoppeldeTaalTypes;
	}

	public void setGekoppeldeTaalTypes(List<TaalTypeKoppel> gekoppeldeTaalTypes)
	{
		this.gekoppeldeTaalTypes = gekoppeldeTaalTypes;
	}

	public void addTaalTypeKoppel(TaalTypeKoppel type)
	{
		gekoppeldeTaalTypes.add(type);
	}

	public Boolean getVoorgedefinieerd()
	{
		return voorgedefinieerd;
	}

	public void setVoorgedefinieerd(Boolean voorgedefinieerd)
	{
		this.voorgedefinieerd = voorgedefinieerd;
	}

}
