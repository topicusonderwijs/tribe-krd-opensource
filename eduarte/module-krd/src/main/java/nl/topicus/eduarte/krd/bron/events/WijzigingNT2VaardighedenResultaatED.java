package nl.topicus.eduarte.krd.bron.events;

import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.util.TimeUtil;
import nl.topicus.eduarte.dao.helpers.ResultaatDataAccessHelper;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.entities.onderwijsproduct.Onderwijsproduct;
import nl.topicus.eduarte.entities.onderwijsproduct.OnderwijsproductAfnameContext;
import nl.topicus.eduarte.entities.personen.Deelnemer;
import nl.topicus.eduarte.entities.resultaatstructuur.Resultaat;
import nl.topicus.eduarte.entities.resultaatstructuur.Toets;
import nl.topicus.eduarte.entities.resultaatstructuur.Toets.SoortToets;
import nl.topicus.eduarte.krd.bron.BronEntiteitChanges;
import nl.topicus.eduarte.krd.bron.BronEvent;
import nl.topicus.eduarte.krd.entities.bron.meldingen.aanlevering.BronAanleverMelding;
import nl.topicus.eduarte.krd.entities.bron.meldingen.aanlevering.BronBveAanleverRecord;
import nl.topicus.onderwijs.duo.bron.BronOnderwijssoort;
import nl.topicus.onderwijs.duo.bron.bve.batches.pve_9_9.aanlevering.ed.NT2Vaardigheden;
import nl.topicus.onderwijs.duo.bron.bve.waardelijsten.NT2Niveau;
import nl.topicus.onderwijs.duo.bron.bve.waardelijsten.NT2Vaardigheid;
import nl.topicus.onderwijs.duo.bron.bve.waardelijsten.SoortMutatie;

/**
 * NT2 vaardigheden worden in de volgende resultaatstructuur opgeslagen:
 * 
 * Hoofdtoets - Deeltoets 1, weging = 0 : Startniveau - Deeltoets 2, weging = 1 : Behaald
 * niveau
 * 
 * Vervolgens wordt de werkelijke waarde in de 'externe waarde' van de bijbehorende
 * schaalwaarde opgeslagen.
 * 
 * @author idserda
 */
public class WijzigingNT2VaardighedenResultaatED implements BronEvent<NT2Vaardigheden>
{
	private final Resultaat resultaat;

	private final BronAanleverMelding aanlevermelding;

	private final BronEntiteitChanges<Verbintenis> changes;

	private final Verbintenis verbintenis;

	public WijzigingNT2VaardighedenResultaatED(BronAanleverMelding aanlevermelding,
			BronEntiteitChanges<Verbintenis> changes, Resultaat resultaat)
	{
		this.aanlevermelding = aanlevermelding;
		this.changes = changes;
		this.resultaat = resultaat;
		this.verbintenis = changes.getEntiteit();
	}

	@Override
	public boolean isToepasselijk()
	{
		if (verbintenis != null)
		{
			if (!verbintenis.isBronCommuniceerbaar() || !verbintenis.isEducatieVerbintenis())
				return false;

			boolean isNT2Toets =
				resultaat.getToets().getParent() != null
					&& resultaat.getToets().getParent().getSoort().isNT2VaardigheidToets();

			return isNT2Toets;
		}
		// Als er geen ED verbintenis is, dan zowiezo niet toepasselijk.
		return false;
	}

	@Override
	public NT2Vaardigheden createMelding()
	{
		if (!isToepasselijk())
			return null;

		Toets toets = resultaat.getToets();

		if (verbintenis != null)
		{
			OnderwijsproductAfnameContext context =
				getAfnamecontext(toets.getResultaatstructuur().getOnderwijsproduct());

			if (context != null)
			{
				// @mantis 0047437: Als startniveau van NT2 vaardigheid niet
				// ingevuld is, niet naar BRON
				NT2Niveau startniveau = getNT2Niveau(toets, SoortToets.Instroomniveau);

				if (startniveau != null)
				{
					context.bepaalVolgnummer();
					NT2Vaardigheid vaardigheid =
						NT2Vaardigheid.valueOf(toets.getParent().getSoort().name());

					aanlevermelding.setBronOnderwijssoort(BronOnderwijssoort.EDUCATIE);
					if (aanlevermelding.getIngangsDatum() == null)
					{
						aanlevermelding.setIngangsDatum(TimeUtil.max(TimeUtil.vandaag(),
							verbintenis.getBegindatum()));
					}

					NT2Vaardigheden record = findOrNew(context.getVolgnummer(), vaardigheid);

					if (record.getSoortMutatie() == null)
					{
						record.setSoortMutatie(getSoortMutatie());
					}

					record.setStartniveau(startniveau);
					record.setNT2Vaardigheid(vaardigheid);

					record.setVakvolgnummer(context.getVolgnummer());

					NT2Niveau behaaldNiveau = getNT2Niveau(toets, SoortToets.BehaaldNiveau);
					if (behaaldNiveau != null)
						record.setBehaaldNiveau(behaaldNiveau);

					return record;

				}
			}
		}

		return null;
	}

	private NT2Vaardigheden findOrNew(int volgnummer, NT2Vaardigheid vaardigheid)
	{
		NT2Vaardigheden record = aanlevermelding.getNt2VaardigheidRecord(volgnummer, vaardigheid);
		if (record != null)
			return record;
		return BronBveAanleverRecord.newEdNt2Vaardigheden(aanlevermelding, verbintenis);
	}

	private NT2Niveau getNT2Niveau(Toets toets, SoortToets soortToets)
	{
		NT2Niveau startniveau = null;
		if (toets.getSoort().equals(soortToets))
		{
			startniveau =
				NT2Niveau.A1BasicUserBreakthrough.parse(resultaat.getWaarde().getExterneWaarde());
		}
		else
		{
			startniveau = getNT2NiveauUitDataBase(toets, resultaat.getDeelnemer(), soortToets);
		}
		return startniveau;
	}

	private SoortMutatie getSoortMutatie()
	{
		if (resultaat.isHandmatigVersturenNaarBron())
		{
			return resultaat.getHandmatigeBronBveSoortMutatie();
		}
		else if (changes.isNieuwVoorBron(resultaat))
		{
			return SoortMutatie.Toevoeging;
		}
		else if (changes.moetUitBronVerwijderdWorden(resultaat))
		{
			return SoortMutatie.Verwijdering;
		}
		else
		{
			return SoortMutatie.Aanpassing;
		}
	}

	private OnderwijsproductAfnameContext getAfnamecontext(Onderwijsproduct onderwijsproduct)
	{
		OnderwijsproductAfnameContext context = null;

		for (OnderwijsproductAfnameContext contextUitLijst : verbintenis.getAfnameContexten())
		{
			if (contextUitLijst.getOnderwijsproductAfname().getOnderwijsproduct().equals(
				onderwijsproduct))
				context = contextUitLijst;
		}

		return context;
	}

	private NT2Niveau getNT2NiveauUitDataBase(Toets toets, Deelnemer deelnemer,
			SoortToets soortToets)
	{
		Toets niveauToets = getSubToets(toets, soortToets);

		if (niveauToets != null)
		{
			ResultaatDataAccessHelper resultaatHelper =
				DataAccessRegistry.getHelper(ResultaatDataAccessHelper.class);

			Resultaat beginwaarde = resultaatHelper.getGeldendeResultaat(niveauToets, deelnemer);

			if (beginwaarde != null && beginwaarde.getWaarde() != null)
				return NT2Niveau.A1BasicUserBreakthrough.parse(beginwaarde.getWaarde()
					.getExterneWaarde());
		}
		return null;
	}

	private Toets getSubToets(Toets toets, SoortToets soort)
	{
		if (toets.getParent() != null)
		{
			for (Toets subtoets : toets.getParent().getChildren())
			{
				if (subtoets.getSoort().equals(soort))
					return subtoets;
			}
		}

		return null;
	}
}