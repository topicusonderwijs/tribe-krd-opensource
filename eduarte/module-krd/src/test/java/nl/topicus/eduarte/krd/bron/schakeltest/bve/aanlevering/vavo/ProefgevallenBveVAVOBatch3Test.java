package nl.topicus.eduarte.krd.bron.schakeltest.bve.aanlevering.vavo;

import static nl.topicus.cobra.types.personalia.Geslacht.*;
import static nl.topicus.eduarte.entities.inschrijving.Verbintenis.VerbintenisStatus.*;
import static nl.topicus.onderwijs.duo.bron.bve.waardelijsten.HoogsteVooropleiding.*;
import static nl.topicus.onderwijs.duo.bron.bve.waardelijsten.SoortMutatie.*;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;

import nl.topicus.eduarte.entities.examen.Examendeelname;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis.VerbintenisStatus;
import nl.topicus.eduarte.krd.entities.bron.meldingen.aanlevering.BronAanleverMelding;
import nl.topicus.onderwijs.duo.bron.BRONConstants;
import nl.topicus.onderwijs.duo.bron.bve.batches.pve_9_9.aanlevering.vavo.InschrijvingsgegevensRecord;
import nl.topicus.onderwijs.duo.bron.bve.waardelijsten.Sectordeel;
import nl.topicus.onderwijs.duo.bron.bve.waardelijsten.VerantwoordelijkeAanleverbestand;
import nl.topicus.onderwijs.duo.bron.data.types.Datum;
import nl.topicus.onderwijs.duo.bron.data.waardelijsten.BestandSoort;

import org.junit.Test;

public class ProefgevallenBveVAVOBatch3Test extends ProefgevallenBveVAVO
{
	@Test
	public void proefgeval17() throws Exception
	{
		createGeval17();
		controller.save();

		BronAanleverMelding melding = getEersteMelding();
		assertThat(melding.getLeerlingnummer(), is("2008"));
		assertThat(melding.getSofinummer(), is("210000090"));
		assertThat(melding.getOnderwijsnummer(), is((String) null));
		assertThat(melding.getGeboortedatum(), is(Datum.valueOf("19910508")));
		assertThat(melding.getGeslacht(), is(Man));
		assertThat(melding.getPostcodeVolgensInstelling(), is("9722TB"));
		assertThat(melding.getLand(), is((String) null));
		assertThat(melding.getMeldingen().size(), is(1));

		InschrijvingsgegevensRecord inschrijving = melding.getMeldingen().get(0);
		assertThat(inschrijving.getRecordType(),
			is(BRONConstants.BVE_AANLEVERING_VAVO_INSCHRIJFGEGEVENS));
		assertThat(inschrijving.getSoortMutatie(), is(Aanpassing));
		assertThat(inschrijving.getInschrijvingsvolgnummer(), is("1"));
		assertThat(inschrijving.getGevolgdeOpleiding(), is("5150"));
		assertThat(inschrijving.getDatumInschrijving(), is(asDate(20080101)));
		assertThat(inschrijving.getGeplandeDatumUitschrijving(), is(asDate(20100101)));
		assertThat(inschrijving.getWerkelijkeDatumUitschrijving(), is(asDate(20090101)));
		assertThat(inschrijving.getHoogsteVooropleiding(), is(HAVO));
		assertThat(inschrijving.getIndicatieNieuwkomer(), is(false));
		assertThat(inschrijving.getContacturenPerWeek(), is(10));
	}

	private void createGeval17() throws Exception
	{
		getDeelnemer2008();

		verbintenis.setEinddatum(asDate(20090101));
		verbintenis.setStatus(Beeindigd);

		addChange(verbintenis, "einddatum", null, verbintenis.getEinddatum());
		addChange(verbintenis, "status", Definitief, Beeindigd);
	}

	@Test
	public void proefgeval18() throws Exception
	{
		createGeval18();
		controller.save();

		BronAanleverMelding melding = getEersteMelding();
		assertThat(melding.getLeerlingnummer(), is("2009"));
		assertThat(melding.getSofinummer(), is("210000107"));
		assertThat(melding.getOnderwijsnummer(), is((String) null));
		assertThat(melding.getGeboortedatum(), is(Datum.valueOf("19910509")));
		assertThat(melding.getGeslacht(), is(Man));
		assertThat(melding.getPostcodeVolgensInstelling(), is("9722TB"));
		assertThat(melding.getLand(), is((String) null));

		InschrijvingsgegevensRecord inschrijving = melding.getMeldingen().get(0);
		assertThat(inschrijving.getRecordType(),
			is(BRONConstants.BVE_AANLEVERING_VAVO_INSCHRIJFGEGEVENS));
		assertThat(inschrijving.getSoortMutatie(), is(Aanpassing));
		assertThat(inschrijving.getInschrijvingsvolgnummer(), is("1"));
		assertThat(inschrijving.getGevolgdeOpleiding(), is("5150"));
		assertThat(inschrijving.getDatumInschrijving(), is(asDate(20080101)));
		assertThat(inschrijving.getGeplandeDatumUitschrijving(), is(asDate(20090731)));
		assertNull(inschrijving.getWerkelijkeDatumUitschrijving());
		assertThat(inschrijving.getHoogsteVooropleiding(), is(HAVO));
		assertThat(inschrijving.getIndicatieNieuwkomer(), is(false));
		assertThat(inschrijving.getContacturenPerWeek(), is(10));

		assertThat(melding.getMeldingen().size(), is(1));
	}

	private void createGeval18() throws Exception
	{
		getDeelnemer2009();
		Date vorige = verbintenis.getGeplandeEinddatum();
		Date nieuwe = asDate(20090731);
		verbintenis.setGeplandeEinddatum(nieuwe);

		addChange(verbintenis, "geplandeEinddatum", vorige, nieuwe);
	}

	@Test
	public void proefgeval19() throws Exception
	{
		createGeval19();
		controller.save();

		BronAanleverMelding melding = getEersteMelding();
		assertThat(melding.getLeerlingnummer(), is("2010"));
		assertThat(melding.getSofinummer(), is("210000119"));
		assertThat(melding.getOnderwijsnummer(), is((String) null));
		assertThat(melding.getGeboortedatum(), is(Datum.valueOf("19910510")));
		assertThat(melding.getGeslacht(), is(Man));
		assertThat(melding.getPostcodeVolgensInstelling(), is("9722TB"));
		assertThat(melding.getLand(), is((String) null));

		InschrijvingsgegevensRecord inschrijving = melding.getMeldingen().get(0);
		assertThat(inschrijving.getRecordType(),
			is(BRONConstants.BVE_AANLEVERING_VAVO_INSCHRIJFGEGEVENS));
		assertThat(inschrijving.getSoortMutatie(), is(Aanpassing));
		assertThat(inschrijving.getInschrijvingsvolgnummer(), is("1"));
		assertThat(inschrijving.getGevolgdeOpleiding(), is("5150"));
		assertThat(inschrijving.getDatumInschrijving(), is(asDate(20080801)));
		assertThat(inschrijving.getGeplandeDatumUitschrijving(), is(asDate(20100101)));
		assertNull(inschrijving.getWerkelijkeDatumUitschrijving());
		assertThat(inschrijving.getHoogsteVooropleiding(), is(HAVO));
		assertThat(inschrijving.getIndicatieNieuwkomer(), is(false));
		assertThat(inschrijving.getContacturenPerWeek(), is(10));

		assertThat(melding.getMeldingen().size(), is(1));
	}

	private void createGeval19() throws Exception
	{
		getDeelnemer2010();
		Date vorige = verbintenis.getBegindatum();
		Date nieuwe = asDate(20080801);
		verbintenis.setBegindatum(nieuwe);

		addChange(verbintenis, "begindatum", vorige, nieuwe);
	}

	@Test
	public void proefgeval20() throws Exception
	{
		createGeval20();
		controller.save();

		BronAanleverMelding melding = getEersteMelding();
		assertThat(melding.getLeerlingnummer(), is("2011"));
		assertThat(melding.getSofinummer(), is("210000120"));
		assertThat(melding.getOnderwijsnummer(), is((String) null));
		assertThat(melding.getGeboortedatum(), is(Datum.valueOf("19910511")));
		assertThat(melding.getGeslacht(), is(Man));
		assertThat(melding.getPostcodeVolgensInstelling(), is("9722TB"));
		assertThat(melding.getLand(), is((String) null));

		InschrijvingsgegevensRecord inschrijving = melding.getMeldingen().get(0);
		assertThat(inschrijving.getRecordType(),
			is(BRONConstants.BVE_AANLEVERING_VAVO_INSCHRIJFGEGEVENS));
		assertThat(inschrijving.getSoortMutatie(), is(Aanpassing));
		assertThat(inschrijving.getInschrijvingsvolgnummer(), is("1"));
		assertThat(inschrijving.getGevolgdeOpleiding(), is("5150"));
		assertThat(inschrijving.getDatumInschrijving(), is(asDate(20080101)));
		assertThat(inschrijving.getGeplandeDatumUitschrijving(), is(asDate(20100101)));
		assertNull(inschrijving.getWerkelijkeDatumUitschrijving());
		assertThat(inschrijving.getHoogsteVooropleiding(), is(HAVO));
		assertThat(inschrijving.getIndicatieNieuwkomer(), is(false));
		assertThat(inschrijving.getContacturenPerWeek(), is(20));

		assertThat(melding.getMeldingen().size(), is(1));
	}

	private void createGeval20() throws Exception
	{
		getDeelnemer2011();
		int vorige =
			verbintenis.getContacturenPerWeek().setScale(0, RoundingMode.HALF_UP).intValue();
		int nieuwe = 20;
		verbintenis.setContacturenPerWeek(BigDecimal.valueOf(nieuwe));

		addChange(verbintenis, "contacturenPerWeek", vorige, nieuwe);
	}

	@Test
	public void proefgeval21() throws Exception
	{
		createGeval21();
		controller.save();

		BronAanleverMelding melding = getEersteMelding();
		assertThat(melding.getLeerlingnummer(), is("2012"));
		assertThat(melding.getSofinummer(), is("210000132"));
		assertThat(melding.getOnderwijsnummer(), is((String) null));
		assertThat(melding.getGeboortedatum(), is(Datum.valueOf("19910512")));
		assertThat(melding.getGeslacht(), is(Man));
		assertThat(melding.getPostcodeVolgensInstelling(), is("9722TB"));
		assertThat(melding.getLand(), is((String) null));

		InschrijvingsgegevensRecord inschrijving = melding.getMeldingen().get(0);
		assertThat(inschrijving.getRecordType(),
			is(BRONConstants.BVE_AANLEVERING_VAVO_INSCHRIJFGEGEVENS));
		assertThat(inschrijving.getSoortMutatie(), is(Verwijdering));
		assertThat(inschrijving.getInschrijvingsvolgnummer(), is("1"));

		assertThat(melding.getMeldingen().size(), is(1));
	}

	private void createGeval21() throws Exception
	{
		getDeelnemer2012();

		VerbintenisStatus vorige = verbintenis.getStatus();
		VerbintenisStatus nieuwe = VerbintenisStatus.Afgemeld;
		verbintenis.setStatus(nieuwe);

		addChange(verbintenis, "status", vorige, nieuwe);
	}

	@Test
	public void proefgeval22() throws Exception
	{
		createGeval22();
		Examendeelname examen = verbintenis.getExamendeelnames().get(0);
		assertThat(examen.isGewijzigd(), is(false));
		controller.save();
		assertThat(getEersteMelding(), is(nullValue()));
		assertThat(examen.isGewijzigd(), is(true));
	}

	private void createGeval22() throws Exception
	{
		getDeelnemer2013();

		Examendeelname examen = addExamendeelname(verbintenis, 20090110);
		examen.setExamenstatus(getCertificaten());
		addChange(examen, "examenstatus", null, examen.getExamenstatus());
	}

	@Test
	public void proefgeval23() throws Exception
	{
		createGeval23();

		Examendeelname examen1 = verbintenis.getExamendeelnames().get(0);
		Examendeelname examen2 = verbintenis.getExamendeelnames().get(1);
		assertThat(examen1.isGewijzigd(), is(false));
		assertThat(examen2.isGewijzigd(), is(false));
		controller.save();
		assertThat(getEersteMelding(), is(nullValue()));
		assertThat(examen1.isGewijzigd(), is(false));
		assertThat(examen2.isGewijzigd(), is(true));
	}

	private void createGeval23() throws Exception
	{
		getDeelnemer2014();

		// voor vakmelding hack
		deelnemer.setId(new Long(2014));

		Examendeelname examen = addExamendeelname(verbintenis, 20090110);
		examen.setExamenstatus(getCertificaten());
		addChange(examen, "examenstatus", null, examen.getExamenstatus());
	}

	@Test
	public void proefgeval24() throws Exception
	{
		createGeval24();

		Examendeelname examen1 = verbintenis.getExamendeelnames().get(0);
		Examendeelname examen2 = verbintenis.getExamendeelnames().get(1);
		assertThat(examen1.isGewijzigd(), is(false));
		assertThat(examen2.isGewijzigd(), is(false));
		controller.save();
		assertThat(getEersteMelding(), is(nullValue()));
		assertThat(examen1.isGewijzigd(), is(false));
		assertThat(examen2.isGewijzigd(), is(true));
	}

	private void createGeval24() throws Exception
	{
		getDeelnemer2015();

		// voor vakmelding hack
		deelnemer.setId(new Long(2015));

		Examendeelname examen = addExamendeelname(verbintenis, 20090110);
		examen.setExamenstatus(getCertificaten());
		addChange(examen, "examenstatus", null, examen.getExamenstatus());
	}

	@Test
	public void proefgeval25() throws Exception
	{
		createGeval25();

		Examendeelname examen = verbintenis.getExamendeelnames().get(0);
		assertThat(examen.isGewijzigd(), is(false));
		controller.save();
		assertThat(getEersteMelding(), is(nullValue()));
		assertThat(examen.isGewijzigd(), is(true));
	}

	private void createGeval25() throws Exception
	{
		getDeelnemer2016_testgeval25();

		// voor vakmelding hack
		deelnemer.setId(new Long(2016));

		Examendeelname examen = addExamendeelname(verbintenis, 20090110);
		examen.setExamenstatus(getCertificaten());
		addChange(examen, "examenstatus", examen.getExamenstatus(), null);
	}

	@Test
	public void bveVAVOBatch3() throws Exception
	{
		createGeval17();
		createGeval18();
		createGeval19();
		createGeval20();
		createGeval21();
		createGeval22();
		createGeval23();
		createGeval24();
		createGeval25();

		batchBVE = maakBronVAVOBatch();
		batchBVE.setBatchNummer(5);
		batchBVE.getAanleverpunt().setNummer(1);
		batchBVE.getOrganisatie().getBrincode().setCode("25PZ");

		// assertThat(batchBVE.getBrinNummer(), is("25PZ"));
		assertThat(batchBVE.getAanleverPuntNummer(), is(1));
		assertThat(batchBVE.getSectordeel(), is(Sectordeel.VAVO));
		assertThat(batchBVE.getInternOrganisatieNummer(), is((String) null));
		assertThat(batchBVE.getBatchNummer(), is(5));
		assertThat(batchBVE.getBestandSoort(), is(BestandSoort.AANLEVERING));
		// assertThat(batchBVE.getVersieProgrammaVanEisen(), is("1.1"));

		assertThat(batchBVE.getAantalMeldingen(), is(5));
		assertThat(batchBVE.getAantalRecords(), is(11));
		assertThat(batchBVE.getVerantwoordelijkeAanlevering(),
			is(VerantwoordelijkeAanleverbestand.Instelling));
		assertThat(batchBVE.getLaatsteAanlevering(), is(false));

		writeBronBatch(batchBVE);
	}

}
