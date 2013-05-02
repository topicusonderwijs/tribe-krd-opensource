package nl.topicus.eduarte.app.functies;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

import net.sourceforge.jeval.Evaluator;
import net.sourceforge.jeval.function.Function;
import net.sourceforge.jeval.function.FunctionConstants;
import net.sourceforge.jeval.function.FunctionException;
import net.sourceforge.jeval.function.FunctionResult;

/**
 * Functie voor het berekenen het gemiddelde van een lijst van waarden.
 * 
 * @author loite
 * 
 */
public class Gemiddeld implements Function
{
	public static final Gemiddeld INSTANCE = new Gemiddeld();

	private static final MathContext MC = new MathContext(34, RoundingMode.HALF_UP);

	/**
	 * Private constructor, er is maar 1 instance.
	 */
	private Gemiddeld()
	{
	}

	@Override
	public FunctionResult execute(Evaluator evaluator, String arguments) throws FunctionException
	{
		String args[] = arguments.split(",", -1);
		if (args.length < 1)
		{
			throw new FunctionException(
				"Verkeerd aantal parameters meegegeven voor functie gemiddeld (minimaal 1 waarde nodig).");
		}
		BigDecimal sum = new BigDecimal(0);
		int aantal = 0;
		for (int i = 0; i < args.length; i++)
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
			if (value != null && !FunctieUtil.GEEN_CIJFER.equals(value))
			{
				sum = sum.add(value);
				aantal++;
			}
		}
		BigDecimal gemiddeld = sum.divide(BigDecimal.valueOf(aantal), MC);

		String res = gemiddeld.toPlainString();
		FunctionResult result =
			new FunctionResult(res, FunctionConstants.FUNCTION_RESULT_TYPE_NUMERIC);
		return result;
	}

	@Override
	public String getName()
	{
		return "gemiddeld";
	}

}
