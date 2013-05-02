package nl.topicus.eduarte.entities.resultaatstructuur;

import nl.topicus.cobra.templates.annotations.Exportable;
import nl.topicus.eduarte.entities.onderwijsproduct.Onderwijsproduct;

/**
 * Resultaat voor een vervallen OnderwijsproductAfname. Soort 'dummy' resultaat. Alleen te
 * gebruiken in rapportages.
 * 
 * @author hop
 */
@Exportable()
public class ResultaatVervallen extends Resultaat
{
	private static final long serialVersionUID = 1L;

	private Onderwijsproduct onderwijsproduct;

	public ResultaatVervallen(Onderwijsproduct onderwijsproduct)
	{
		this.onderwijsproduct = onderwijsproduct;
	}

	@Override
	public String getCijferAlsWoord()
	{
		return "vervallen";
	}

	@Override
	public String getCijferOfSchaalwaarde()
	{
		return "--";
	}

	@Override
	public String getFormattedDisplayCijfer()
	{
		return "--";
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
