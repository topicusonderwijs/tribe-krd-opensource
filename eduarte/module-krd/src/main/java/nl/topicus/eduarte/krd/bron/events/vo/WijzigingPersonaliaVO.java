package nl.topicus.eduarte.krd.bron.events.vo;

import static nl.topicus.onderwijs.duo.bron.vo.waardelijsten.SoortMutatie.*;

import java.util.List;

import nl.topicus.eduarte.entities.adres.Adres;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.entities.personen.Persoon;
import nl.topicus.eduarte.entities.personen.PersoonAdres;
import nl.topicus.eduarte.krd.bron.BronDeelnemerChanges;
import nl.topicus.eduarte.krd.entities.bron.meldingen.aanlevering.BronInschrijvingsgegevensVOMelding;
import nl.topicus.eduarte.krd.entities.bron.meldingen.aanlevering.BronInschrijvingsgegevensVOMelding.VoMeldingSoort;

public class WijzigingPersonaliaVO extends AbstractVoEvent
{
	private BronDeelnemerChanges changes;

	private Verbintenis verbintenis;

	public WijzigingPersonaliaVO(BronDeelnemerChanges changes,
			List<BronInschrijvingsgegevensVOMelding> wachtrij, Verbintenis verbintenis)
	{
		super(wachtrij);
		this.changes = changes;
		this.verbintenis = verbintenis;
	}

	@Override
	public BronInschrijvingsgegevensVOMelding createMelding()
	{
		if (!isToepasselijk())
			return null;
		BronInschrijvingsgegevensVOMelding melding =
			findOrNew(VoMeldingSoort.I, verbintenis.getBegindatum());

		// alleen toevoegingen hebben additionele personalia die mogelijk ververst moeten
		// worden
		if (melding.getSoortMutatie() == Toevoeging || changes.isWijzigingCumiVO(verbintenis))
		{
			if (melding.getSoortMutatie() == null)
			{
				melding.setSoortMutatie(Aanpassing);
			}
			melding.ververs(null);
			melding.addReden(changes.toString());
			melding.saveOrUpdate();
			return melding;
		}
		return null;
	}

	@Override
	public boolean isToepasselijk()
	{
		if (!verbintenis.isOpleidingBronCommuniceerbaar())
			return false;
		return verbintenis.isVOVerbintenis() && changes.moetNaarBron(verbintenis)
			&& changes.heeftWijzigingenVoor(Persoon.class, PersoonAdres.class, Adres.class);
	}

	@Override
	protected Verbintenis getVerbintenis()
	{
		return verbintenis;
	}
}
