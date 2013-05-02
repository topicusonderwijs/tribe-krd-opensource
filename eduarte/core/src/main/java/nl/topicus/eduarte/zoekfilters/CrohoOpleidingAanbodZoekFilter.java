package nl.topicus.eduarte.zoekfilters;

import nl.topicus.eduarte.entities.hogeronderwijs.OpleidingsVorm;
import nl.topicus.eduarte.entities.organisatie.Brin;
import nl.topicus.eduarte.entities.taxonomie.ho.CrohoOpleiding;
import nl.topicus.eduarte.entities.taxonomie.ho.CrohoOpleidingAanbod;

public class CrohoOpleidingAanbodZoekFilter extends AbstractZoekFilter<CrohoOpleidingAanbod>
{
	private static final long serialVersionUID = 1L;

	private CrohoOpleiding crohoOpleiding;

	private Brin brin;

	private OpleidingsVorm opleidingsvorm;

	public CrohoOpleiding getCrohoOpleiding()
	{
		return crohoOpleiding;
	}

	public void setCrohoOpleiding(CrohoOpleiding crohoOpleiding)
	{
		this.crohoOpleiding = crohoOpleiding;
	}

	public Brin getBrin()
	{
		return brin;
	}

	public void setBrin(Brin brin)
	{
		this.brin = brin;
	}

	public OpleidingsVorm getOpleidingsvorm()
	{
		return opleidingsvorm;
	}

	public void setOpleidingsvorm(OpleidingsVorm opleidingsvorm)
	{
		this.opleidingsvorm = opleidingsvorm;
	}
}
