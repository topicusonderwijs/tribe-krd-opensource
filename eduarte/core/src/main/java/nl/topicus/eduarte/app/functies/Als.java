package nl.topicus.eduarte.app.functies;

import java.math.BigDecimal;

import net.sourceforge.jeval.Evaluator;
import net.sourceforge.jeval.function.Function;
import net.sourceforge.jeval.function.FunctionConstants;
import net.sourceforge.jeval.function.FunctionException;
import net.sourceforge.jeval.function.FunctionResult;
import nl.topicus.cobra.util.DecimalUtil;

/**
 * Functie voor het berekenen het gemiddelde van een lijst van waarden.
 * 
 * @author loite
 * 
 */
public class Als implements Function
{
	public static final Als INSTANCE = new Als();

	/**
	 * Private constructor, er is maar 1 instance.
	 */
	private Als()
	{
	}

	@Override
	public FunctionResult execute(Evaluator evaluator, String arguments) throws FunctionException
	{
		String args[] = arguments.split(",", -1);
		if (args.length != 3)
		{
			throw new FunctionException(
				"Verkeerd aantal parameters meegegeven voor functie als (precies 3 waarden nodig).");
		}

		try
		{
			BigDecimal condition = new BigDecimal(args[0]);
			if (condition.compareTo(DecimalUtil.ONE) == 0)
				return getFunctionResult(evaluator, args[1]);
			else
				return getFunctionResult(evaluator, args[2]);
		}
		catch (NumberFormatException e)
		{
			throw new FunctionException(args[0] + " is geen geldig getal.");
		}
	}

	private FunctionResult getFunctionResult(Evaluator evaluator, final String result)
			throws FunctionException
	{
		int type = FunctionConstants.FUNCTION_RESULT_TYPE_NUMERIC;
		if (result.trim().startsWith(Character.toString(evaluator.getQuoteCharacter())))
			type = FunctionConstants.FUNCTION_RESULT_TYPE_STRING;
		return new FunctionResult(result, type);
	}

	@Override
	public String getName()
	{
		return "als";
	}

}
