package nl.topicus.eduarte.krd.web.pages.groep;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nl.topicus.eduarte.entities.groep.Groep;
import nl.topicus.eduarte.entities.groep.GroepDocent;
import nl.topicus.eduarte.entities.groep.GroepMentor;
import nl.topicus.eduarte.entities.groep.Groepsdeelname;
import nl.topicus.eduarte.entities.personen.Deelnemer;

import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.validation.AbstractFormValidator;

public class GroepsdeelnameValidator extends AbstractFormValidator
{
	private static final long serialVersionUID = 1L;

	private GroepModel groepModel;

	public GroepsdeelnameValidator(GroepModel groepModel)
	{
		this.groepModel = groepModel;
	}

	@Override
	public FormComponent< ? >[] getDependentFormComponents()
	{
		return null;
	}

	@Override
	public void validate(Form< ? > form)
	{
		Groep groep = groepModel.getGroep();
		Map<Deelnemer, List<Groepsdeelname>> deelnames =
			new HashMap<Deelnemer, List<Groepsdeelname>>();
		for (Groepsdeelname curDeelname : groep.getDeelnemers())
		{
			if (curDeelname.getBegindatum().after(curDeelname.getEinddatumNotNull()))
			{
				form.error("De begindatum dient voor de einddatum te liggen voor de deelname van "
					+ curDeelname.getDeelnemer() + ".");
			}
			List<Groepsdeelname> curDeelnames = deelnames.get(curDeelname.getDeelnemer());
			if (curDeelnames == null)
			{
				curDeelnames = new ArrayList<Groepsdeelname>();
				deelnames.put(curDeelname.getDeelnemer(), curDeelnames);
			}
			for (Groepsdeelname curCheckDeelname : curDeelnames)
			{
				if (curCheckDeelname.getBegindatum().before(curDeelname.getEinddatumNotNull())
					&& curDeelname.getBegindatum().before(curCheckDeelname.getEinddatumNotNull()))
				{
					form.error("Deelnames van " + curDeelname.getDeelnemer() + " overlappen.");
				}
			}
			curDeelnames.add(curDeelname);

			if (curDeelname.getBegindatum().before(groep.getBegindatum())
				|| (curDeelname.getEinddatum() != null && curDeelname.getEinddatum().after(
					groep.getEinddatumNotNull())))
			{
				form.error("De begin- en/of einddatum voor " + curDeelname.getDeelnemer()
					+ " vallen niet geheel binnen de geldigheidsperiode van de groep.");
			}
		}
		for (GroepDocent curDocent : groep.getGroepDocenten())
		{
			if (curDocent.getBegindatum().after(curDocent.getEinddatumNotNull()))
			{
				form.error("De begindatum dient voor de einddatum te liggen voor de docent "
					+ curDocent.getMedewerker() + ".");
			}

			if (curDocent.getBegindatum().before(groep.getBegindatum())
				|| (curDocent.getEinddatum() != null && curDocent.getEinddatum().after(
					groep.getEinddatumNotNull())))
			{
				form.error("De begin- en/of einddatum voor de docent " + curDocent.getMedewerker()
					+ " vallen niet geheel binnen de geldigheidsperiode van de groep.");
			}
		}
		for (GroepMentor curMentor : groep.getGroepMentoren())
		{
			if (curMentor.getBegindatum().after(curMentor.getEinddatumNotNull()))
			{
				form.error("De begindatum dient voor de einddatum te liggen voor de mentor "
					+ curMentor.getMedewerker() + ".");
			}

			if (curMentor.getBegindatum().before(groep.getBegindatum())
				|| (curMentor.getEinddatum() != null && curMentor.getEinddatum().after(
					groep.getEinddatumNotNull())))
			{
				form.error("De begin- en/of einddatum voor de mentor " + curMentor.getMedewerker()
					+ " vallen niet geheel binnen de geldigheidsperiode van de groep.");
			}
		}
	}
}
