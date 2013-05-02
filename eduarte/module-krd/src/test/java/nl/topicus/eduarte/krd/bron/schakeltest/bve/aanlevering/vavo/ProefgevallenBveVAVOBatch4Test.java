package nl.topicus.eduarte.krd.bron.schakeltest.bve.aanlevering.vavo;

import static nl.topicus.cobra.types.personalia.Geslacht.*;
import static nl.topicus.onderwijs.duo.bron.bve.waardelijsten.HoogsteVooropleiding.*;
import static nl.topicus.onderwijs.duo.bron.bve.waardelijsten.SoortMutatie.*;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.util.Date;

import nl.topicus.eduarte.krd.entities.bron.meldingen.aanlevering.BronAanleverMelding;
import nl.topicus.onderwijs.duo.bron.BRONConstants;
import nl.topicus.onderwijs.duo.bron.bve.batches.pve_9_9.aanlevering.PersoonsgegevensRecord;
import nl.topicus.onderwijs.duo.bron.bve.batches.pve_9_9.aanlevering.vavo.InschrijvingsgegevensRecord;
import nl.topicus.onderwijs.duo.bron.bve.waardelijsten.Sectordeel;
import nl.topicus.onderwijs.duo.bron.bve.waardelijsten.VerantwoordelijkeAanleverbestand;
import nl.topicus.onderwijs.duo.bron.data.types.Datum;
import nl.topicus.onderwijs.duo.bron.data.waardelijsten.BestandSoort;

import org.junit.Test;

public class ProefgevallenBveVAVOBatch4Test extends ProefgevallenBveVAVO
{
	@Test
	public void proefgeval26() throws Exception
	{
		createGeval26();
		controller.save();

		BronAanleverMelding melding = getEersteMelding();
		assertThat(melding.getLeerlingnummer(), is("2017"));
		assertThat(melding.getSofinummer(), is((String) null));
		assertThat(melding.getOnderwijsnummer(), is((String) null));
		assertThat(melding.getGeboortedatum(), is(Datum.valueOf("19910517")));
		assertThat(melding.getGeslacht(), is(Man));
		assertThat(melding.getPostcodeVolgensInstelling(), is("9722TB"));
		assertThat(melding.getLand(), is((String) null));
		assertThat(melding.getMeldingen().size(), is(2));

		PersoonsgegevensRecord persoonsgegevensMelding = melding.getMeldingen().get(0);
		assertThat(persoonsgegevensMelding.getRecordType(),
			is(BRONConstants.BVE_AANLEVERING_PERSOONSGEGEVENS));
		assertThat(persoonsgegevensMelding.getAchternaam(), is("ACHTERNAAMZEVENTIEN"));
		assertThat(persoonsgegevensMelding.getVoorvoegsel(), is(""));
		assertThat(persoonsgegevensMelding.getAlleVoornamen(), is("Voornaam17"));
		assertThat(persoonsgegevensMelding.getStraatnaam(), is("Kemkensberg"));
		assertThat(persoonsgegevensMelding.getHuisnummer(), is(2));
		assertThat(persoonsgegevensMelding.getPlaatsnaam(), is("Groningen"));

		InschrijvingsgegevensRecord inschrijving = melding.getMeldingen().get(1);
		assertThat(inschrijving.getRecordType(),
			is(BRONConstants.BVE_AANLEVERING_VAVO_INSCHRIJFGEGEVENS));
		assertThat(inschrijving.getSoortMutatie(), is(Toevoeging));
		assertThat(inschrijving.getInschrijvingsvolgnummer(), is("1"));
		assertThat(inschrijving.getGevolgdeOpleiding(), is("5150"));
		assertThat(inschrijving.getDatumInschrijving(), is(asDate(20080101)));
		assertThat(inschrijving.getGeplandeDatumUitschrijving(), is(asDate(20100101)));
		assertThat(inschrijving.getWerkelijkeDatumUitschrijving(), is((Date) null));
		assertThat(inschrijving.getHoogsteVooropleiding(), is(HAVO));
		assertThat(inschrijving.getIndicatieNieuwkomer(), is(false));
		assertThat(inschrijving.getContacturenPerWeek(), is(10));
	}

	private void createGeval26() throws Exception
	{
		getDeelnemer2017();
		addChange(verbintenis, "status", null, verbintenis.getStatus());
	}

	@Test
	public void proefgeval27() throws Exception
	{
		createGeval27();
		controller.save();

		BronAanleverMelding melding = getEersteMelding();
		assertThat(melding.getLeerlingnummer(), is("1018"));
		assertThat(melding.getSofinummer(), is("210000211"));
		assertThat(melding.getOnderwijsnummer(), is((String) null));
		assertThat(melding.getGeboortedatum(), is(Datum.valueOf("19910518")));
		assertThat(melding.getGeslacht(), is(Man));
		assertThat(melding.getPostcodeVolgensInstelling(), is("9722TB"));
		assertThat(melding.getLand(), is((String) null));
		assertThat(melding.getMeldingen().size(), is(1));

		InschrijvingsgegevensRecord inschrijving = melding.getMeldingen().get(0);
		assertThat(inschrijving.getRecordType(),
			is(BRONConstants.BVE_AANLEVERING_VAVO_INSCHRIJFGEGEVENS));
		assertThat(inschrijving.getSoortMutatie(), is(Toevoeging));
		assertThat(inschrijving.getInschrijvingsvolgnummer(), is("1"));
		assertThat(inschrijving.getGevolgdeOpleiding(), is("5105"));
		assertThat(inschrijving.getDatumInschrijving(), is(asDate(20080101)));
		assertThat(inschrijving.getGeplandeDatumUitschrijving(), is(asDate(20100101)));
		assertThat(inschrijving.getWerkelijkeDatumUitschrijving(), is((Date) null));
		assertThat(inschrijving.getHoogsteVooropleiding(), is(HAVO));
		assertThat(inschrijving.getIndicatieNieuwkomer(), is(false));
		assertThat(inschrijving.getContacturenPerWeek(), is(10));
	}

	private void createGeval27() throws Exception
	{
		getDeelnemer1018();
		addChange(verbintenis, "status", null, verbintenis.getStatus());
	}

	@Test
	public void bveVAVOBatch4() throws Exception
	{
		createGeval26();
		createGeval27();

		batchBVE = maakBronVAVOBatch();
		batchBVE.setBatchNummer(4);
		batchBVE.getAanleverpunt().setNummer(1);
		batchBVE.getOrganisatie().getBrincode().setCode("25PZ");

		assertThat(batchBVE.getBrinNummer(), is("25PZ"));
		assertThat(batchBVE.getAanleverPuntNummer(), is(1));
		assertThat(batchBVE.getSectordeel(), is(Sectordeel.VAVO));
		assertThat(batchBVE.getInternOrganisatieNummer(), is((String) null));
		assertThat(batchBVE.getBatchNummer(), is(4));
		assertThat(batchBVE.getBestandSoort(), is(BestandSoort.AANLEVERING));
		// assertThat(batchBVE.getVersieProgrammaVanEisen(), is("1.1"));

		assertThat(batchBVE.getAantalMeldingen(), is(2));
		assertThat(batchBVE.getAantalRecords(), is(6));
		assertThat(batchBVE.getVerantwoordelijkeAanlevering(),
			is(VerantwoordelijkeAanleverbestand.Instelling));
		assertThat(batchBVE.getLaatsteAanlevering(), is(false));

		writeBronBatch(batchBVE);
	}
}
