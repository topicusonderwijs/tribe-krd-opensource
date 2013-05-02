package nl.topicus.eduarte.resultaten.web.pages.beheer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.TreeSet;

import nl.topicus.eduarte.entities.resultaatstructuur.Schaal;
import nl.topicus.eduarte.entities.resultaatstructuur.Schaalwaarde;
import nl.topicus.eduarte.entities.resultaatstructuur.Schaal.Schaaltype;

import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.validation.AbstractFormValidator;

public class SchaalwaardenValidator extends AbstractFormValidator
{
	private static final long serialVersionUID = 1L;

	private String prevErrorKey = null;

	public SchaalwaardenValidator()
	{
	}

	@Override
	public FormComponent< ? >[] getDependentFormComponents()
	{
		return null;
	}

	@Override
	public void validate(Form< ? > form)
	{
		Schaal schaal = (Schaal) form.getModelObject();
		if (schaal.getSchaaltype() == Schaaltype.Cijfer)
			return;

		String errorKey = schaal.computeHash();
		if (!errorKey.equals(prevErrorKey) && !schaalwaardenValid(schaal, form))
		{
			form.error("Er zijn mogelijke problemen gevonden in de schaalwaarden van deze schaal. "
				+ "Corrigeer de waarden of druk nogmaal op opslaan om toch op te slaan.");
			prevErrorKey = errorKey;
		}
	}

	private boolean schaalwaardenValid(Schaal schaal, Form< ? > form)
	{
		List<Schaalwaarde> sortedByVanaf = new ArrayList<Schaalwaarde>();
		TreeSet<Schaalwaarde> sortedByVolgnummer =
			new TreeSet<Schaalwaarde>(new Comparator<Schaalwaarde>()
			{
				@Override
				public int compare(Schaalwaarde o1, Schaalwaarde o2)
				{
					return o1.getVolgnummer() - o2.getVolgnummer();
				}
			});
		for (Schaalwaarde curWaarde : schaal.getSchaalwaarden())
		{
			if (curWaarde.getVanafCijfer() != null)
				sortedByVanaf.add(curWaarde);
			sortedByVolgnummer.add(curWaarde);
		}
		Collections.sort(sortedByVanaf, new Comparator<Schaalwaarde>()
		{
			@Override
			public int compare(Schaalwaarde o1, Schaalwaarde o2)
			{
				return o1.getVanafCijfer().compareTo(o2.getVanafCijfer());
			}
		});

		boolean valid = true;
		Schaalwaarde lastWaarde = null;
		for (Schaalwaarde curWaarde : sortedByVanaf)
		{
			if (lastWaarde != null)
			{
				int diff = curWaarde.getVanafCijfer().compareTo(lastWaarde.getTotCijfer());
				if (diff > 0)
				{
					form.error("Er zit een gat in de schaal tussen '" + lastWaarde + "' en '"
						+ curWaarde + "'.");
					valid = false;
				}
				else if (diff < 0)
				{
					form.error("De schaalwaarden '" + lastWaarde + "' en '" + curWaarde
						+ "' overlappen.");
					valid = false;
				}
			}
			lastWaarde = curWaarde;
		}

		lastWaarde = null;
		for (Schaalwaarde curWaarde : sortedByVolgnummer)
		{
			if (lastWaarde != null)
			{
				int diff = curWaarde.getNominaleWaarde().compareTo(lastWaarde.getNominaleWaarde());
				if (diff < 0)
				{
					form
						.error("De nominale waarde van '" + lastWaarde + "' is hoger dan die van '"
							+ curWaarde + "' terwijl het volgnummer van '" + lastWaarde
							+ "' lager is.");
					valid = false;
				}
			}
			lastWaarde = curWaarde;
		}

		return valid;
	}
}
