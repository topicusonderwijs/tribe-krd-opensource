package nl.topicus.eduarte.zoekfilters;

import java.util.List;

import nl.topicus.eduarte.entities.taxonomie.mbo.cgo.ModerneTaal;
import nl.topicus.eduarte.entities.taxonomie.mbo.cgo.TaalType;

import org.apache.wicket.model.IModel;

/**
 * @author vandenbrink
 */
public class TaalZoekFilter extends AbstractZoekFilter<ModerneTaal>
{

	private static final long serialVersionUID = 1L;

	private String naam;

	private String omschrijving;

	private String afkorting;

	private String code;

	private IModel<TaalType> type;

	private List<Long> talenUitgezonderd;

	private Boolean voorgedefinieerd;

	public Boolean getVoorgedefinieerd()
	{
		return voorgedefinieerd;
	}

	public void setVoorgedefinieerd(Boolean voorgedefinieerd)
	{
		this.voorgedefinieerd = voorgedefinieerd;
	}

	public String getNaam()
	{
		return naam;
	}

	public void setNaam(String naam)
	{
		this.naam = naam;
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

	public TaalType getType()
	{
		return getModelObject(type);
	}

	public void setType(TaalType type)
	{
		this.type = makeModelFor(type);
	}

	public List<Long> getTalenUitgezonderd()
	{
		return talenUitgezonderd;
	}

	public void setTalenUitgezonderd(List<Long> talenUitgezonderd)
	{
		this.talenUitgezonderd = talenUitgezonderd;
	}

}
