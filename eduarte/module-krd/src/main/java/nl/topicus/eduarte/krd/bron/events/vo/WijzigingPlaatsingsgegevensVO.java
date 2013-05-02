package nl.topicus.eduarte.krd.bron.events.vo;

import static nl.topicus.onderwijs.duo.bron.vo.waardelijsten.SoortMutatie.*;

import java.util.Date;
import java.util.List;

import nl.topicus.eduarte.entities.inschrijving.Plaatsing;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.krd.bron.BronVerbintenisChanges;
import nl.topicus.eduarte.krd.entities.bron.meldingen.aanlevering.BronInschrijvingsgegevensVOMelding;
import nl.topicus.eduarte.krd.entities.bron.meldingen.aanlevering.BronInschrijvingsgegevensVOMelding.VoMeldingSoort;
import nl.topicus.onderwijs.duo.bron.vo.waardelijsten.SoortMutatie;

public class WijzigingPlaatsingsgegevensVO extends AbstractVoEvent
{
	private BronVerbintenisChanges changes;

	private Plaatsing plaatsing;

	public WijzigingPlaatsingsgegevensVO(BronVerbintenisChanges changes,
			List<BronInschrijvingsgegevensVOMelding> wachtrij, Plaatsing plaatsing)
	{
		super(wachtrij);
		this.changes = changes;
		this.plaatsing = plaatsing;
	}

	@Override
	public boolean isToepasselijk()
	{
		Verbintenis verbintenis = getVerbintenis();
		if (!verbintenis.isOpleidingBronCommuniceerbaar())
			return false;
		// controle of het leerjaar wel veranderd, anders hoeft de melding namelijk niet
		// naar bron
		Plaatsing pl = verbintenis.getPlaatsingOpDatum(plaatsing.getBegindatum());
		if (pl != null && pl.getLeerjaar() != null && plaatsing.getLeerjaar() != null
			&& pl.getLeerjaar().equals(plaatsing.getLeerjaar()) && !pl.equals(plaatsing))
			return false;
		return verbintenis.isVOVerbintenis()
			&& changes.moetNaarBron(verbintenis)
			&& (plaatsing.isHandmatigVersturenNaarBron() || !changes
				.heeftAlleenEinddatumWijzigingen(plaatsing));
	}

	@Override
	public BronInschrijvingsgegevensVOMelding createMelding()
	{
		if (!isToepasselijk())
			return null;

		if (changes.heeftWijziging(plaatsing, "begindatum"))
		{
			verwerkAanpassingBegindatum();
		}

		SoortMutatie mutatie = changes.getVOSoortMutatie(plaatsing.getVerbintenis());
		switch (mutatie)
		{
			case Toevoeging:
				return verwerkAanpassing(plaatsing.getBegindatum());
			case Aanpassing:
				return verwerkAanpassing(plaatsing.getBegindatum());
			case Uitschrijving:
				break;
			case Verwijdering:
				return verwerkVerwijdering(plaatsing.getBegindatum());
		}
		return null;
	}

	private void verwerkAanpassingBegindatum()
	{
		Date peildatum = changes.getPreviousValue(plaatsing, "begindatum");
		if (peildatum != null)
		{
			verwerkVerwijdering(peildatum);
		}
	}

	private BronInschrijvingsgegevensVOMelding verwerkAanpassing(Date peildatum)
	{
		BronInschrijvingsgegevensVOMelding melding = findOrNew(VoMeldingSoort.I, peildatum);
		// pas alleen de soort mutatie aan als deze nog niet gezet is.
		if (melding.getSoortMutatie() == null)
		{
			melding.setSoortMutatie(Aanpassing);
		}
		melding.addReden(changes.toString());
		melding.ververs(plaatsing);
		melding.saveOrUpdate();
		return melding;
	}

	private BronInschrijvingsgegevensVOMelding verwerkVerwijdering(Date peildatum)
	{
		BronInschrijvingsgegevensVOMelding melding = findOrNew(VoMeldingSoort.I, peildatum);
		melding.setSoortMutatie(Verwijdering);
		melding.addReden(changes.toString());
		melding.ververs(plaatsing);
		melding.saveOrUpdate();
		return melding;
	}

	@Override
	protected Verbintenis getVerbintenis()
	{
		return changes.getEntiteit();
	}
}
