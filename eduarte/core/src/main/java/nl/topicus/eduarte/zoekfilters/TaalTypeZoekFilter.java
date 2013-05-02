package nl.topicus.eduarte.zoekfilters;

import java.util.List;

import nl.topicus.eduarte.entities.taxonomie.mbo.cgo.TaalType;

public class TaalTypeZoekFilter extends AbstractZoekFilter<TaalType>
{

	private static final long serialVersionUID = 1L;

	private String titel;

	private List<Long> taalTypesUitgezonderd;

	public List<Long> getTaalTypesUitgezonderd()
	{
		return taalTypesUitgezonderd;
	}

	public void setTaalTypesUitgezonderd(List<Long> taalTypesUitgezonderd)
	{
		this.taalTypesUitgezonderd = taalTypesUitgezonderd;
	}

	public String getTitel()
	{
		return titel;
	}

	public void setTitel(String titel)
	{
		this.titel = titel;

	}

}
