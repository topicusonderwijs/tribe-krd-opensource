package nl.topicus.eduarte.entities.resultaatstructuur;

import nl.topicus.cobra.templates.annotations.Exportable;
import nl.topicus.eduarte.entities.inschrijving.OnderwijsproductAfname;
import nl.topicus.eduarte.entities.inschrijving.VrijstellingType;
import nl.topicus.eduarte.entities.onderwijsproduct.Onderwijsproduct;

/**
 * Resultaat voor een EVC OnderwijsproductAfname. Soort 'dummy' resultaat. Alleen te
 * gebruiken in rapportages.
 * 
 * @author idserda
 */
@Exportable()
public class ResultaatVrijstelling extends Resultaat
{
	private static final long serialVersionUID = 1L;

	private Onderwijsproduct onderwijsproduct;

	private VrijstellingType vrijstellingType;

	public ResultaatVrijstelling(OnderwijsproductAfname afname)
	{
		this.onderwijsproduct = afname.getOnderwijsproduct();
		this.vrijstellingType = afname.getVrijstellingType();
	}

	@Override
	public String getCijferAlsWoord()
	{
		return vrijstellingType.getOmschrijving();
	}

	@Override
	public String getCijferOfSchaalwaarde()
	{
		return vrijstellingType.getAfkorting();
	}

	@Override
	public String getFormattedDisplayCijfer()
	{
		return vrijstellingType.getAfkorting();
	}

	@Override
	public Toets getToets()
	{
		Toets toets = new Toets()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public Resultaatstructuur getResultaatstructuur()
			{
				Resultaatstructuur resultaatStructuur = new Resultaatstructuur()
				{
					private static final long serialVersionUID = 1L;

					@Override
					public Onderwijsproduct getOnderwijsproduct()
					{
						return onderwijsproduct;
					}
				};
				return resultaatStructuur;
			}
		};
		return toets;
	}
}
