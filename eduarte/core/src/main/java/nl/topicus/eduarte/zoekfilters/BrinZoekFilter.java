package nl.topicus.eduarte.zoekfilters;

import nl.topicus.cobra.web.components.form.AutoForm;
import nl.topicus.eduarte.entities.organisatie.Brin;
import nl.topicus.eduarte.entities.organisatie.Brin.Onderwijssector;

/**
 * @author loite
 */
public class BrinZoekFilter extends AbstractZoekFilter<Brin>
{
	private static final long serialVersionUID = 1L;

	private String organisatie;

	private String code;

	private String plaats;

	@AutoForm(htmlClasses = "unit_240")
	private Onderwijssector onderwijssector;

	private final boolean gebruikLandelijkeExterneOrganisaties;

	public BrinZoekFilter()
	{
		this.gebruikLandelijkeExterneOrganisaties =
			ExterneOrganisatieZoekFilter.getGebruikLandelijkeSetting();
	}

	public BrinZoekFilter(boolean gebruikLandelijkeExterneOrganisaties)
	{
		this.gebruikLandelijkeExterneOrganisaties = gebruikLandelijkeExterneOrganisaties;
	}

	public BrinZoekFilter(Onderwijssector onderwijssector)
	{
		setOnderwijssector(onderwijssector);
		this.gebruikLandelijkeExterneOrganisaties =
			ExterneOrganisatieZoekFilter.getGebruikLandelijkeSetting();
	}

	public void setOrganisatie(String organisatie)
	{
		this.organisatie = organisatie;
	}

	public String getOrganisatie()
	{
		return organisatie;
	}

	public void setCode(String code)
	{
		this.code = code;
	}

	public String getCode()
	{
		return code;
	}

	public void setPlaats(String plaats)
	{
		this.plaats = plaats;
	}

	public String getPlaats()
	{
		return plaats;
	}

	public void setOnderwijssector(Onderwijssector onderwijssector)
	{
		this.onderwijssector = onderwijssector;
	}

	public Onderwijssector getOnderwijssector()
	{
		return onderwijssector;
	}

	public boolean isGebruikLandelijkeExterneOrganisaties()
	{
		return gebruikLandelijkeExterneOrganisaties;
	}
}
