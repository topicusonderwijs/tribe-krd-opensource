package nl.topicus.eduarte.app.functies;

import java.math.BigDecimal;

import net.sourceforge.jeval.Evaluator;
import net.sourceforge.jeval.function.Function;
import net.sourceforge.jeval.function.FunctionConstants;
import net.sourceforge.jeval.function.FunctionException;
import net.sourceforge.jeval.function.FunctionResult;

/**
 * Retourneert 1 (true) als de operand een lege string is, of {@link FunctieUtil}
 * .GEEN_CIJFER; anders 0 (false).
 * 
 * @author hop
 */
public class IsLeeg implements Function
{
	public static final IsLeeg INSTANCE = new IsLeeg();

	/**
	 * Private constructor, er is maar 1 instance.
	 */
	private IsLeeg()
	{
	}

	@Override
	public FunctionResult execute(Evaluator evaluator, String arguments) throws FunctionException
	{
		String args[] = arguments.split(",", -1);
		if (args.length != 1)
		{
			throw new FunctionException(
				"Verkeerd aantal parameters meegegeven voor functie isLeeg (precies 1 waarde nodig).");
		}

		String arg = args[0];
		try
		{
			boolean result = false;
			String quote = String.valueOf(evaluator.getQuoteCharacter());
			if (arg.startsWith(quote))
				result = arg.equals(quote + quote);
			else
				result = new BigDecimal(arg).equals(FunctieUtil.GEEN_CIJFER);

			if (result)
				return new FunctionResult("1", FunctionConstants.FUNCTION_RESULT_TYPE_NUMERIC);
			else
				return new FunctionResult("0", FunctionConstants.FUNCTION_RESULT_TYPE_NUMERIC);
		}
		catch (NumberFormatException e)
		{
			throw new FunctionException(arg + " is geen geldige string of getal.");
		}
	}

	@Override
	public String getName()
	{
		return "isLeeg";
	}
}
