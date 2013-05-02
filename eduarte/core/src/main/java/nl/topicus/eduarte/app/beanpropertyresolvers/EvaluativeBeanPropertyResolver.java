package nl.topicus.eduarte.app.beanpropertyresolvers;

import java.math.BigDecimal;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sourceforge.jeval.EvaluationException;
import net.sourceforge.jeval.Evaluator;
import net.sourceforge.jeval.VariableResolver;
import net.sourceforge.jeval.function.FunctionException;
import nl.topicus.cobra.templates.FieldInfo;
import nl.topicus.cobra.templates.resolvers.BeanPropertyResolver;
import nl.topicus.eduarte.app.functies.AantalGroterDan;
import nl.topicus.eduarte.app.functies.AantalKleinerDan;
import nl.topicus.eduarte.app.functies.Als;
import nl.topicus.eduarte.app.functies.FunctieUtil;
import nl.topicus.eduarte.app.functies.Gemiddeld;
import nl.topicus.eduarte.app.functies.IsLeeg;

/**
 * Propertyresolver die veldnamen waarin #{...} voorkomt, evalueert met JEval. De
 * #{...}-variabelen die hierin voorkomen worden als bean property geresolvet. Dates
 * worden geconverteerd naar timestamp, booleans naar 0/1.
 * 
 * @author hop
 */
public class EvaluativeBeanPropertyResolver extends BeanPropertyResolver
{
	private class ResolverVariableResolver implements VariableResolver
	{
		private Evaluator eval;

		protected BigDecimal geenCijfer;

		public ResolverVariableResolver(Evaluator eval)
		{
			this.eval = eval;
			this.geenCijfer = FunctieUtil.GEEN_CIJFER;
		}

		protected Object doResolve(String variableName)
		{
			return EvaluativeBeanPropertyResolver.super.resolve(variableName);
		}

		@Override
		public String resolveVariable(String variableName) throws FunctionException
		{
			Object resolved = doResolve(variableName);

			if (resolved == null)
			{
				FieldInfo info = EvaluativeBeanPropertyResolver.super.getInfo(variableName);
				if (!info.isValid())
					throw new FunctionException(info.getMessage());
				resolved = getNull(info);
			}

			if (resolved instanceof Date)
				resolved = ((Date) resolved).getTime();
			else if (resolved instanceof Boolean)
				resolved = ((Boolean) resolved) ? 1 : 0;

			if (!(resolved instanceof Number))
				resolved =
					eval.getQuoteCharacter() + resolved.toString() + eval.getQuoteCharacter();

			return resolved.toString();
		}

		private Object getNull(FieldInfo info)
		{
			Object resolved;
			if (info != null && isOneOf(info.getClass(), Number.class, Date.class, Boolean.class))
				resolved = geenCijfer;
			resolved = "";
			return resolved;
		}
	}

	private class DummyVariableResolver extends ResolverVariableResolver
	{
		public DummyVariableResolver(Evaluator eval)
		{
			super(eval);
			geenCijfer = BigDecimal.ONE;
		}

		@Override
		protected Object doResolve(String variableName)
		{
			return null;
		}
	}

	private final static Pattern REGEX_VARIABLE = Pattern.compile("#\\{(.*)\\}");

	private static boolean isOneOf(Class< ? > typeToTest, Class< ? >... theTypes)
	{
		for (Class< ? > one : theTypes)
			if (one.isAssignableFrom(typeToTest))
				return true;

		return false;
	}

	public EvaluativeBeanPropertyResolver(Object context)
	{
		super(context);

	}

	@Override
	public Object resolve(String name)
	{
		Matcher matcher = REGEX_VARIABLE.matcher(name);

		if (!matcher.find())
			return super.resolve(name);

		Evaluator eval = prepareEvaluator();
		eval.setVariableResolver(new ResolverVariableResolver(eval));

		String result = null;
		try
		{
			result = eval.evaluate(name, false, true);
		}
		catch (EvaluationException e)
		{
			// doe niets
		}
		return result;
	}

	private Evaluator prepareEvaluator()
	{
		Evaluator eval = new Evaluator();
		eval.putFunction(Als.INSTANCE);
		eval.putFunction(Gemiddeld.INSTANCE);
		eval.putFunction(AantalGroterDan.INSTANCE);
		eval.putFunction(AantalKleinerDan.INSTANCE);
		eval.putFunction(IsLeeg.INSTANCE);
		return eval;
	}

	@Override
	public FieldInfo getInfo(String name)
	{
		Matcher matcher = REGEX_VARIABLE.matcher(name);

		if (!matcher.find())
			return super.getInfo(name);

		Evaluator eval = prepareEvaluator();
		eval.setVariableResolver(new DummyVariableResolver(eval));

		try
		{
			eval.evaluate(name, false, true);
		}
		catch (EvaluationException e)
		{
			return new FieldInfo(name, null, false, e.getMessage() + ": " + name);
		}

		return new FieldInfo(name, String.class, true, null);
	}

}
