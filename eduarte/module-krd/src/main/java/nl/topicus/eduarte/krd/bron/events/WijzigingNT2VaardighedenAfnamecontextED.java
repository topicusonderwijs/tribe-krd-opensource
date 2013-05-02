package nl.topicus.eduarte.krd.bron.events;

import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.util.TimeUtil;
import nl.topicus.eduarte.dao.helpers.ResultaatDataAccessHelper;
import nl.topicus.eduarte.dao.helpers.ToetsDataAccessHelper;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.entities.onderwijsproduct.Onderwijsproduct;
import nl.topicus.eduarte.entities.onderwijsproduct.OnderwijsproductAfnameContext;
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

public class WijzigingNT2VaardighedenAfnamecontextED implements BronEvent<NT2Vaardigheden>
{
	private final OnderwijsproductAfnameContext context;

	private final BronAanleverMelding aanlevermelding;

	private final BronEntiteitChanges<Verbintenis> changes;

	private final Verbintenis verbintenis;

	private final Onderwijsproduct onderwijsproduct;

	public WijzigingNT2VaardighedenAfnamecontextED(BronAanleverMelding aanlevermelding,
			BronEntiteitChanges<Verbintenis> changes, OnderwijsproductAfnameContext context)
	{
		this.aanlevermelding = aanlevermelding;
		this.changes = changes;
		this.context = context;
		this.verbintenis = context.getVerbintenis();
		this.onderwijsproduct = context.getOnderwijsproductAfname().getOnderwijsproduct();
	}

	@Override
	public boolean isToepasselijk()
	{
		if (!changes.moetNaarBron(verbintenis))
		{
			return false;
		}
		if (!verbintenis.isOpleidingBronCommuniceerbaar())
			return false;
		boolean isEducatie = verbintenis.isEducatieVerbintenis();
		boolean isNederlandsAlsTweedeTaal =
			onderwijsproduct != null && onderwijsproduct.getExterneCode() != null
				&& onderwijsproduct.getExterneCode().equals("0990");
		return isEducatie && isNederlandsAlsTweedeTaal
			&& !changes.moetUitBronVerwijderdWorden(context);
	}

	@Override
	public NT2Vaardigheden createMelding()
	{
		if (!isToepasselijk())
			return null;

		aanlevermelding.setBronOnderwijssoort(BronOnderwijssoort.EDUCATIE);

		ToetsDataAccessHelper toetsHelper =
			DataAccessRegistry.getHelper(ToetsDataAccessHelper.class);

		ResultaatDataAccessHelper resultaatHelper =
			DataAccessRegistry.getHelper(ResultaatDataAccessHelper.class);

		for (SoortToets soortToets : SoortToets.nt2vaardigheidToetsen())
		{
			// Standaard toets voor deze NT2 vaardigheid zoeken
			Toets toets =
				toetsHelper.getToets(onderwijsproduct, context.getOnderwijsproductAfname()
					.getCohort(), soortToets);

			if (toets != null && toets.getChildren().size() == 2)
			{
				Resultaat beginwaarde =
					resultaatHelper.getGeldendeResultaat(toets.getChildren().get(0), verbintenis
						.getDeelnemer());

				if (beginwaarde != null)
				{
					context.bepaalVolgnummer();
					NT2Vaardigheid vaardigheid = NT2Vaardigheid.valueOf(soortToets.name());

					NT2Vaardigheden nt2melding = findOrNew(context.getVolgnummer(), vaardigheid);

					nt2melding.setSoortMutatie(SoortMutatie.Toevoeging);
					nt2melding.setVakvolgnummer(context.getVolgnummer());

					nt2melding.setNT2Vaardigheid(vaardigheid);
					nt2melding.setStartniveau(NT2Niveau.A1BasicUserBreakthrough.parse(beginwaarde
						.getWaarde().getExterneWaarde()));

					Resultaat behaald =
						resultaatHelper.getGeldendeResultaat(toets.getChildren().get(1),
							verbintenis.getDeelnemer());

					if (behaald != null && behaald.getWaarde() != null)
						nt2melding.setBehaaldNiveau(NT2Niveau.A1BasicUserBreakthrough.parse(behaald
							.getWaarde().getExterneWaarde()));
					if (aanlevermelding.getIngangsDatum() == null)
					{
						aanlevermelding.setIngangsDatum(TimeUtil.max(TimeUtil.vandaag(),
							verbintenis.getBegindatum()));
					}
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
}