package nl.topicus.cobra.transformers;

import nl.topicus.cobra.util.StringUtil;

/**
 * Transformeert een string waarde naar een string met (of zonder) hoofdletters,
 * afhankelijk van de opgegeven {@link HoofdletterMode}.
 */
public class HoofdletterTransformer implements Transformer<String>
{
	private static final long serialVersionUID = 1L;

	private HoofdletterMode mode;

	public HoofdletterTransformer(HoofdletterMode mode)
	{
		this.mode = mode;
	}

	@Override
	public String transform(String value)
	{
		String res = value;
		switch (mode)
		{
			// TODO uitzonderingssituaties als:
			// ijsbrand -> IJsbrand
			// anne-marie -> Anne-Marie
			// etc
			case EersteLetter:
				res = StringUtil.firstCharUppercase(value);
				break;
			case EersteLetterEenWoord:
				res = StringUtil.firstCharUppercaseOnlyIfOneWordAndStartWithLowercase(value);
				break;
			case ElkWoord:
				res = StringUtil.firstCharUppercaseOfEachWord(value);
				break;
			case PuntSeperated:
				res = StringUtil.puntSeperated(value);
				break;
			case Alles:
				res = value.toUpperCase();
				break;
			case Geen:
				res = value.toLowerCase();
				break;
		}
		return res;
	}
}
