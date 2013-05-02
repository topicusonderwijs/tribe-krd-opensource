package nl.topicus.eduarte.entities.resultaatstructuur;

import java.math.BigDecimal;
import java.util.Map;

import net.sourceforge.jeval.EvaluationException;
import net.sourceforge.jeval.Evaluator;
import nl.topicus.eduarte.app.functies.AantalGroterDan;
import nl.topicus.eduarte.app.functies.AantalKleinerDan;
import nl.topicus.eduarte.app.functies.Als;
import nl.topicus.eduarte.app.functies.Gemiddeld;
import nl.topicus.eduarte.entities.resultaatstructuur.Schaal.Schaaltype;

public final class FormuleBerekening
{
	public static BigDecimal bereken(Map<Toets, Resultaat> deeltoetsen, Toets samengesteldeToets)
			throws EvaluationException
	{
		Evaluator eval = new Evaluator();
		for (Map.Entry<Toets, Resultaat> curEntry : deeltoetsen.entrySet())
		{
			Toets toets = curEntry.getKey();
			Resultaat resultaat = curEntry.getValue();
			String afkorting = toets.getCode();
			try
			{
				eval.isValidName(afkorting);
				if (resultaat == null || resultaat.isNullResultaat())
				{
					if (toets.getSchaal().getSchaaltype() == Schaaltype.Tekstueel)
						eval.putVariable(afkorting, "''");
					else
						eval.putVariable(afkorting, "0");
					eval.putVariable(afkorting + "_cijfer", "0");
					eval.putVariable(afkorting + "_behaald", "0");
				}
				else
				{
					if (toets.getSchaal().getSchaaltype() == Schaaltype.Tekstueel)
						eval.putVariable(afkorting, "'" + resultaat.getFormattedDisplayCijfer()
							+ "'");
					else
						eval.putVariable(afkorting, resultaat.getCijfer().toPlainString());
					eval.putVariable(afkorting + "_cijfer", resultaat.getCijfer().toPlainString());
					eval.putVariable(afkorting + "_behaald", resultaat.isBehaald() ? "1" : "0");
					eval.putVariable(afkorting + "_weging", Integer.toString(resultaat
						.getWegingVoorBerekening()));
				}
				eval
					.putVariable(afkorting + "_volgnummer", Integer.toString(toets.getVolgnummer()));
			}
			catch (IllegalArgumentException e)
			{
			}
		}
		eval.putFunction(AantalKleinerDan.INSTANCE);
		eval.putFunction(AantalGroterDan.INSTANCE);
		eval.putFunction(Gemiddeld.INSTANCE);
		eval.putFunction(Als.INSTANCE);

		try
		{
			eval.parse(samengesteldeToets.getFormule());
			return new BigDecimal(eval.evaluate());
		}
		catch (NumberFormatException e)
		{
			throw new EvaluationException(e);
		}
	}
}
