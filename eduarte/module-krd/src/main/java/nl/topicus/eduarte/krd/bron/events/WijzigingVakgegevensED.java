package nl.topicus.eduarte.krd.bron.events;

import static nl.topicus.cobra.util.TimeUtil.*;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.entities.onderwijsproduct.OnderwijsproductAfnameContext;
import nl.topicus.eduarte.krd.bron.BronEntiteitChanges;
import nl.topicus.eduarte.krd.bron.BronEvent;
import nl.topicus.eduarte.krd.entities.bron.meldingen.aanlevering.BronAanleverMelding;
import nl.topicus.eduarte.krd.entities.bron.meldingen.aanlevering.BronBveAanleverRecord;
import nl.topicus.onderwijs.duo.bron.BronOnderwijssoort;
import nl.topicus.onderwijs.duo.bron.bve.batches.pve_9_9.aanlevering.ed.VakgegevensRecord;
import nl.topicus.onderwijs.duo.bron.bve.waardelijsten.SoortMutatie;

public class WijzigingVakgegevensED implements BronEvent<VakgegevensRecord>
{
	private final BronAanleverMelding aanleverMelding;

	private final OnderwijsproductAfnameContext context;

	private final BronEntiteitChanges<Verbintenis> changes;

	private final Verbintenis verbintenis;

	public WijzigingVakgegevensED(BronAanleverMelding aanleverMelding,
			BronEntiteitChanges<Verbintenis> changes, OnderwijsproductAfnameContext context)
	{
		this.aanleverMelding = aanleverMelding;
		this.context = context;
		this.changes = changes;
		this.verbintenis = context.getVerbintenis();
	}

	@Override
	public boolean isToepasselijk()
	{
		if (!verbintenis.isEducatieVerbintenis())
		{
			return false;
		}
		if (!verbintenis.isOpleidingBronCommuniceerbaar())
			return false;
		// Alleen gegevens naar BRON versturen als de verbintenis naar BRON moet
		if (!changes.moetNaarBron(verbintenis))
		{
			return false;
		}
		// Alleen onderwijsproducten meesturen die aan de taxonomie zijn
		// gekoppeld (mantis 0044759)
		boolean isGekoppeldAanTaxonomieElement =
			context.getOnderwijsproductAfname().getOnderwijsproduct() != null
				&& !context.getOnderwijsproductAfname().getOnderwijsproduct()
					.getOnderwijsproductTaxonomieList().isEmpty();
		if (!isGekoppeldAanTaxonomieElement)
		{
			return false;
		}
		boolean handmatig = context.isHandmatigVersturenNaarBron();
		boolean nieuw = changes.isNieuwVoorBron(context);
		boolean verwijderd = changes.moetUitBronVerwijderdWorden(context);
		boolean alleenVolgnummerWijziging = changes.heeftAlleenVolgnummerWijzigingen(context);
		// Geen melding aanmaken als er geen externe code bekend is.
		boolean vakHeeftExterneCode =
			(context.getOnderwijsproductAfname().getOnderwijsproduct().getExterneCode() != null);
		boolean heeftOnderwijsproductAfnameWijziging =
			changes.heeftOnderwijsproductAfnameWijziging(context);
		boolean heeftOnderwijsproductAfnameContextWijziging =
			changes.heeftWijzigingenVoor(OnderwijsproductAfnameContext.class);
		return (handmatig || ((nieuw || verwijderd || heeftOnderwijsproductAfnameWijziging || heeftOnderwijsproductAfnameContextWijziging)
			&& !alleenVolgnummerWijziging && vakHeeftExterneCode));
	}

	@Override
	public VakgegevensRecord createMelding()
	{
		if (!isToepasselijk())
			return null;

		aanleverMelding.setBronOnderwijssoort(BronOnderwijssoort.EDUCATIE);

		context.bepaalVolgnummer();
		BronBveAanleverRecord record = (BronBveAanleverRecord) findOrNew(context.getVolgnummer());

		record.vulEdVakgegevensRecord();

		if (record.getSoortMutatie() == null)
		{
			if (context.isHandmatigVersturenNaarBron())
				record.setSoortMutatie(context.getHandmatigeBronBveSoortMutatie());
			else if (changes.isNieuwVoorBron(context))
				record.setSoortMutatie(SoortMutatie.Toevoeging);
			else if (changes.moetUitBronVerwijderdWorden(context))
			{
				record.setSoortMutatie(SoortMutatie.Verwijdering);
				record.setAfnameContext(null);
			}
			else
				record.setSoortMutatie(SoortMutatie.Aanpassing);
		}

		if (aanleverMelding.getIngangsDatum() == null)
		{
			aanleverMelding.setIngangsDatum(max(vandaag(), verbintenis.getBegindatum()));
		}
		return record;
	}

	private VakgegevensRecord findOrNew(int volgnummer)
	{
		VakgegevensRecord record = aanleverMelding.getVakgegevensRecordED(volgnummer);
		if (record != null)
			return record;
		return BronBveAanleverRecord.newEdVakgegevensRecord(aanleverMelding, verbintenis, context);
	}
}