package nl.topicus.eduarte.krd.web.pages.deelnemer.collectief.aanmaken;

import nl.topicus.cobra.util.TimeUtil;
import nl.topicus.eduarte.entities.begineinddatum.BeginEinddatumUtil;
import nl.topicus.eduarte.entities.inschrijving.Plaatsing;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis;

public class NieuwePlaatsingUtil
{
	Verbintenis verbintenis;

	public NieuwePlaatsingUtil(Verbintenis verbintenis)
	{
		this.verbintenis = verbintenis;
	}

	public void addNieuwePlaatsing(Plaatsing nieuwePlaatsing)
	{
		beeindigActievePlaatsingen(nieuwePlaatsing);

		nieuwePlaatsing.setVerbintenis(verbintenis);
		nieuwePlaatsing.setDeelnemer(verbintenis.getDeelnemer());

		verbintenis.getPlaatsingen().add(nieuwePlaatsing);

		nieuwePlaatsing.save();
	}

	private void beeindigActievePlaatsingen(Plaatsing nieuwePlaatsing)
	{
		beeindigActievePlaatsingOpBegindatum(nieuwePlaatsing);
		beeindigActievePlaatsingOpEinddatum(nieuwePlaatsing);
	}

	private void beeindigActievePlaatsingOpBegindatum(Plaatsing nieuwePlaatsing)
	{
		Plaatsing actievePlaatsingOpBegindatum =
			BeginEinddatumUtil.getElementOpPeildatum(verbintenis.getPlaatsingen(), nieuwePlaatsing
				.getBegindatum());

		if (actievePlaatsingOpBegindatum != null)
		{
			actievePlaatsingOpBegindatum.setEinddatum(TimeUtil.getInstance().addDays(
				nieuwePlaatsing.getBegindatum(), -1));
			actievePlaatsingOpBegindatum.saveOrUpdate();
		}
	}

	private void beeindigActievePlaatsingOpEinddatum(Plaatsing nieuwePlaatsing)
	{
		if (nieuwePlaatsing.getEinddatum() != null)
		{
			Plaatsing actievePlaatsingOpEinddatum =
				BeginEinddatumUtil.getElementOpPeildatum(verbintenis.getPlaatsingen(),
					nieuwePlaatsing.getEinddatum());

			if (actievePlaatsingOpEinddatum != null)
			{
				actievePlaatsingOpEinddatum.setBegindatum(TimeUtil.getInstance().addDays(
					nieuwePlaatsing.getEinddatum(), 1));
				actievePlaatsingOpEinddatum.saveOrUpdate();
			}
		}
	}

}