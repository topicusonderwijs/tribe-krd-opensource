package nl.topicus.eduarte.app.functies;

import java.math.BigDecimal;

import net.sourceforge.jeval.Evaluator;
import net.sourceforge.jeval.function.Function;
import net.sourceforge.jeval.function.FunctionConstants;
import net.sourceforge.jeval.function.FunctionException;
import net.sourceforge.jeval.function.FunctionResult;

/**
 * Een functie die het aantal input parameters dat lager dan een gegeven waarde is,
 * bepaalt. De input parameters moeten allemaal numeriek zijn.
 * 
 * @author loite
 */
public class AantalGroterDan implements Function
{
	public static final AantalGroterDan INSTANCE = new AantalGroterDan();

	/**
	 * Private constructor, er is maar 1 instance.
	 */
	private AantalGroterDan()
	{
	}

	/**
	 * @see net.sourceforge.jeval.function.Function#execute(net.sourceforge.jeval.Evaluator,
	 *      java.lang.String)
	 */
	@Override
	public FunctionResult execute(Evaluator evaluator, String arguments) throws FunctionException
	{
		String args[] = arguments.split(",", -1);
		if (args.length < 2)
		{
			throw new FunctionException(
				"Verkeerd aantal parameters meegegeven voor functie aantalGroterDan (minimaal 2 waarden nodig).");
		}
		BigDecimal minimaleWaarde = new BigDecimal(args[0]);
		int aantal = 0;
		for (int i = 1; i < args.length; i++)
		{
			BigDecimal value = null;
			try
			{
				value = new BigDecimal(args[i]);
			}
			catch (NumberFormatException e)
			{
				// ignore, blijkbaar geen cijfer.
			}
			if (value != null && !value.equals(FunctieUtil.GEEN_CIJFER)
				&& value.compareTo(minimaleWaarde) > 0)
			{
				aantal++;
			}
		}

		String res = String.valueOf(aantal);
		FunctionResult result =
			new FunctionResult(res, FunctionConstants.FUNCTION_RESULT_TYPE_NUMERIC);
		return result;
	}

	@Override
	public String getName()
	{
		return "aantalGroterDan";
	}

}
