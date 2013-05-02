package nl.topicus.eduarte.krd.bron.events.vo;

import static nl.topicus.eduarte.krd.entities.bron.BronMeldingStatus.*;

import java.util.Date;
import java.util.List;

import nl.topicus.cobra.util.TimeUtil;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.krd.bron.BronEvent;
import nl.topicus.eduarte.krd.entities.bron.meldingen.aanlevering.BronInschrijvingsgegevensVOMelding;
import nl.topicus.eduarte.krd.entities.bron.meldingen.aanlevering.BronInschrijvingsgegevensVOMelding.VoMeldingSoort;

/**
 * Basisklasse voor BRON wijzigingsevents voor het VO. Houdt een lijst van meldingen bij
 * die in de wachtrij staan of erin gezet worden voor het verdichten van de meldingen.
 */
public abstract class AbstractVoEvent implements BronEvent<BronInschrijvingsgegevensVOMelding>
{
	private final List<BronInschrijvingsgegevensVOMelding> wachtrij;

	public AbstractVoEvent(List<BronInschrijvingsgegevensVOMelding> wachtrij)
	{
		this.wachtrij = wachtrij;
	}

	protected abstract Verbintenis getVerbintenis();

	protected List<BronInschrijvingsgegevensVOMelding> getWachtrij()
	{
		return wachtrij;
	}

	protected void verwijderUitWachtrij(BronInschrijvingsgegevensVOMelding melding)
	{
		wachtrij.remove(melding);
	}

	protected BronInschrijvingsgegevensVOMelding findOrNew(VoMeldingSoort soort, Date begindatum)
	{
		BronInschrijvingsgegevensVOMelding melding = find(soort, begindatum);
		if (melding == null)
		{
			melding = create(soort, begindatum);
		}
		return melding;
	}

	protected BronInschrijvingsgegevensVOMelding find(VoMeldingSoort soort, Date begindatum)
	{
		for (BronInschrijvingsgegevensVOMelding melding : wachtrij)
		{
			if (melding.getSoort() == soort
				&& TimeUtil.getInstance().datesEqual(melding.getIngangsDatum(), begindatum))
			{
				return melding;
			}
		}
		return null;
	}

	private BronInschrijvingsgegevensVOMelding create(VoMeldingSoort soort, Date begindatum)
	{
		Verbintenis verbintenis = getVerbintenis();

		BronInschrijvingsgegevensVOMelding melding = new BronInschrijvingsgegevensVOMelding();
		melding.setSoort(soort);
		melding.setBronMeldingStatus(WACHTRIJ);
		melding.setIngangsDatum(begindatum);
		melding.setVerbintenis(verbintenis);
		melding.setDeelnemer(verbintenis.getDeelnemer());
		melding.setMeldingNummer(1);
		melding.vulSleutelgegevens();

		// voegt de melding toe aan de wachtrij zodat er meerdere aanpassingen gedaan
		// kunnen worden aan dezelfde melding.
		wachtrij.add(melding);
		return melding;
	}
}
