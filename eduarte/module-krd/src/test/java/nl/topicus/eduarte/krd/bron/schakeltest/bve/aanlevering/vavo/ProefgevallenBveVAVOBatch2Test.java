package nl.topicus.eduarte.krd.bron.schakeltest.bve.aanlevering.vavo;

import static nl.topicus.cobra.types.personalia.Geslacht.*;
import static nl.topicus.eduarte.entities.inschrijving.Verbintenis.VerbintenisStatus.*;
import static nl.topicus.onderwijs.duo.bron.bve.waardelijsten.HoogsteVooropleiding.*;
import static nl.topicus.onderwijs.duo.bron.bve.waardelijsten.SoortMutatie.*;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.util.Arrays;

import nl.topicus.eduarte.entities.examen.Examendeelname;
import nl.topicus.eduarte.krd.entities.bron.meldingen.aanlevering.BronAanleverMelding;
import nl.topicus.onderwijs.duo.bron.BRONConstants;
import nl.topicus.onderwijs.duo.bron.bve.batches.pve_9_9.aanlevering.PersoonsgegevensRecord;
import nl.topicus.onderwijs.duo.bron.bve.batches.pve_9_9.aanlevering.vavo.InschrijvingsgegevensRecord;
import nl.topicus.onderwijs.duo.bron.data.types.Datum;

import org.junit.Test;

public class ProefgevallenBveVAVOBatch2Test extends ProefgevallenBveVAVO
{
	@Test
	public void proefgeval8() throws Exception
	{
		createGeval8();
		controller.save();

		BronAanleverMelding melding = getEersteMelding();
		assertThat(melding.getLeerlingnummer(), is("2002"));
		assertThat(melding.getSofinummer(), is("210000697"));
		assertThat(melding.getOnderwijsnummer(), is((String) null));
		assertThat(melding.getGeboortedatum(), is(Datum.valueOf("19910716")));
		assertThat(melding.getGeslacht(), is(Man));
		assertThat(melding.getPostcodeVolgensInstelling(), is("9722TB"));
		assertThat(melding.getLand(), is((String) null));
		assertThat(melding.getMeldingen().size(), is(1));

		InschrijvingsgegevensRecord inschrijving = melding.getMeldingen().get(0);
		assertThat(inschrijving.getRecordType(),
			is(BRONConstants.BVE_AANLEVERING_VAVO_INSCHRIJFGEGEVENS));
		assertThat(inschrijving.getSoortMutatie(), is(Toevoeging));
		assertThat(inschrijving.getInschrijvingsvolgnummer(), is("1"));
		assertThat(inschrijving.getGevolgdeOpleiding(), is("5150"));
		assertThat(inschrijving.getDatumInschrijving(), is(asDate(20080101)));
		assertThat(inschrijving.getGeplandeDatumUitschrijving(), is(asDate(20100101)));
		assertThat(inschrijving.getHoogsteVooropleiding(), is(HAVO));
		assertThat(inschrijving.getIndicatieNieuwkomer(), is(false));
		assertThat(inschrijving.getContacturenPerWeek(), is(10));
	}

	private void createGeval8() throws Exception
	{
		getDeelnemer2002();

		addChange(verbintenis, "status", null, Definitief);
	}

	@Test
	public void proefgeval9() throws Exception
	{
		createGeval9();
		controller.save();

		BronAanleverMelding melding = getEersteMelding();
		assertThat(melding.getLeerlingnummer(), is("2003"));
		assertThat(melding.getSofinummer(), is("210000028"));
		assertThat(melding.getOnderwijsnummer(), is((String) null));
		assertThat(melding.getGeboortedatum(), is(Datum.valueOf("19910503")));
		assertThat(melding.getGeslacht(), is(Man));
		assertThat(melding.getPostcodeVolgensInstelling(), is("9722TB"));
		assertThat(melding.getLand(), is((String) null));
		assertThat(melding.getMeldingen().size(), is(1));

		InschrijvingsgegevensRecord inschrijving = melding.getMeldingen().get(0);
		assertThat(inschrijving.getRecordType(),
			is(BRONConstants.BVE_AANLEVERING_VAVO_INSCHRIJFGEGEVENS));
		assertThat(inschrijving.getSoortMutatie(), is(Toevoeging));
		assertThat(inschrijving.getInschrijvingsvolgnummer(), is("1"));
		assertThat(inschrijving.getGevolgdeOpleiding(), is("5150"));
		assertThat(inschrijving.getDatumInschrijving(), is(asDate(20080101)));
		assertThat(inschrijving.getGeplandeDatumUitschrijving(), is(asDate(20100101)));
		assertThat(inschrijving.getHoogsteVooropleiding(), is(HAVO));
		assertThat(inschrijving.getIndicatieNieuwkomer(), is(false));
		assertThat(inschrijving.getContacturenPerWeek(), is(10));
	}

	private void createGeval9() throws Exception
	{
		getDeelnemer2003();

		addChange(verbintenis, "status", null, Definitief);
	}

	@Test
	public void proefgeval10() throws Exception
	{
		createGeval10();
		controller.save();

		BronAanleverMelding melding = getEersteMelding();
		assertThat(melding.getLeerlingnummer(), is("2004"));
		assertThat(melding.getSofinummer(), is((String) null));
		assertThat(melding.getOnderwijsnummer(), is("100320254"));
		assertThat(melding.getGeboortedatum(), is(Datum.valueOf("19910701")));
		assertThat(melding.getGeslacht(), is(Man));
		assertThat(melding.getPostcodeVolgensInstelling(), is("9722TB"));
		assertThat(melding.getLand(), is((String) null));
		assertThat(melding.getMeldingen().size(), is(1));

		InschrijvingsgegevensRecord inschrijving = melding.getMeldingen().get(0);
		assertThat(inschrijving.getRecordType(),
			is(BRONConstants.BVE_AANLEVERING_VAVO_INSCHRIJFGEGEVENS));
		assertThat(inschrijving.getSoortMutatie(), is(Toevoeging));
		assertThat(inschrijving.getInschrijvingsvolgnummer(), is("1"));
		assertThat(inschrijving.getGevolgdeOpleiding(), is("5150"));
		assertThat(inschrijving.getDatumInschrijving(), is(asDate(20080101)));
		assertThat(inschrijving.getGeplandeDatumUitschrijving(), is(asDate(20100101)));
		assertThat(inschrijving.getHoogsteVooropleiding(), is(HAVO));
		assertThat(inschrijving.getIndicatieNieuwkomer(), is(false));
		assertThat(inschrijving.getContacturenPerWeek(), is(10));
	}

	private void createGeval10() throws Exception
	{
		getDeelnemer2004();

		addChange(verbintenis, "status", null, Definitief);
	}

	@Test
	public void proefgeval11() throws Exception
	{
		createGeval11();
		controller.save();

		BronAanleverMelding melding = getEersteMelding();
		assertThat(melding.getLeerlingnummer(), is("2005"));
		assertThat(melding.getSofinummer(), is((String) null));
		assertThat(melding.getOnderwijsnummer(), is((String) null));
		assertThat(melding.getGeboortedatum(), is(Datum.valueOf("19910505")));
		assertThat(melding.getGeslacht(), is(Man));
		assertThat(melding.getPostcodeVolgensInstelling(), is("9722TB"));
		assertThat(melding.getLand(), is((String) null));
		assertThat(melding.getMeldingen().size(), is(2));

		PersoonsgegevensRecord personalia = melding.getMeldingen().get(0);
		assertThat(personalia.getRecordType(), is(BRONConstants.BVE_AANLEVERING_PERSOONSGEGEVENS));
		assertThat(personalia.getAchternaam(), is("ACHTERNAAMVIJF"));
		assertThat(personalia.getAlleVoornamen(), is("VOORNAAMVIJF"));
		assertThat(personalia.getStraatnaam(), is("kemkensberg"));
		assertThat(personalia.getHuisnummer(), is(2));
		assertThat(personalia.getPlaatsnaam(), is("Groningen"));

		InschrijvingsgegevensRecord inschrijving = melding.getMeldingen().get(1);
		assertThat(inschrijving.getRecordType(),
			is(BRONConstants.BVE_AANLEVERING_VAVO_INSCHRIJFGEGEVENS));
		assertThat(inschrijving.getSoortMutatie(), is(Toevoeging));
		assertThat(inschrijving.getInschrijvingsvolgnummer(), is("1"));
		assertThat(inschrijving.getGevolgdeOpleiding(), is("5150"));
		assertThat(inschrijving.getDatumInschrijving(), is(asDate(20080101)));
		assertThat(inschrijving.getGeplandeDatumUitschrijving(), is(asDate(20100101)));
		assertThat(inschrijving.getHoogsteVooropleiding(), is(HAVO));
		assertThat(inschrijving.getIndicatieNieuwkomer(), is(false));
		assertThat(inschrijving.getContacturenPerWeek(), is(10));
	}

	private void createGeval11() throws Exception
	{
		getDeelnemer2005();
		addChange(verbintenis, "status", null, Definitief);
	}

	@Test
	public void proefgeval12() throws Exception
	{
		createGeval12();
		controller.save();

		BronAanleverMelding melding = getEersteMelding();
		assertThat(melding.getLeerlingnummer(), is("2006"));
		assertThat(melding.getSofinummer(), is((String) null));
		assertThat(melding.getOnderwijsnummer(), is((String) null));
		assertThat(melding.getGeboortedatum(), is(Datum.valueOf("19910702")));
		assertThat(melding.getGeslacht(), is(Man));
		assertThat(melding.getPostcodeVolgensInstelling(), is((String) null));
		assertThat(melding.getLand(), is("6030"));
		assertThat(melding.getMeldingen().size(), is(2));

		PersoonsgegevensRecord personalia = melding.getMeldingen().get(0);
		assertThat(personalia.getRecordType(), is(BRONConstants.BVE_AANLEVERING_PERSOONSGEGEVENS));
		assertThat(personalia.getAchternaam(), is("ACHTERNAAMTWEEENVIJFTIG"));
		assertThat(personalia.getAlleVoornamen(), is("VOORNAAMTWEEENVIJFTIG"));
		assertThat(personalia.getStraatnaam(), is(""));
		assertThat(personalia.getPlaatsnaam(), is((String) null));

		InschrijvingsgegevensRecord inschrijving = melding.getMeldingen().get(1);
		assertThat(inschrijving.getRecordType(),
			is(BRONConstants.BVE_AANLEVERING_VAVO_INSCHRIJFGEGEVENS));
		assertThat(inschrijving.getSoortMutatie(), is(Toevoeging));
		assertThat(inschrijving.getInschrijvingsvolgnummer(), is("1"));
		assertThat(inschrijving.getGevolgdeOpleiding(), is("5150"));
		assertThat(inschrijving.getDatumInschrijving(), is(asDate(20080101)));
		assertThat(inschrijving.getGeplandeDatumUitschrijving(), is(asDate(20100101)));
		assertThat(inschrijving.getHoogsteVooropleiding(), is(HAVO));
		assertThat(inschrijving.getIndicatieNieuwkomer(), is(false));
		assertThat(inschrijving.getContacturenPerWeek(), is(10));
	}

	private void createGeval12() throws Exception
	{
		getDeelnemer2006();
		addChange(verbintenis, "status", null, Definitief);
	}

	@Test
	public void proefgeval13() throws Exception
	{
		createGeval13();
		controller.save();

		BronAanleverMelding melding = getEersteMelding();
		assertThat(melding.getLeerlingnummer(), is("2007"));
		assertThat(melding.getSofinummer(), is("210000089"));
		assertThat(melding.getOnderwijsnummer(), is((String) null));
		assertThat(melding.getGeboortedatum(), is(Datum.valueOf("19910507")));
		assertThat(melding.getGeslacht(), is(Man));
		assertThat(melding.getPostcodeVolgensInstelling(), is("9722TB"));
		assertThat(melding.getLand(), is((String) null));
		assertThat(melding.getMeldingen().size(), is(1));

		InschrijvingsgegevensRecord inschrijving = melding.getMeldingen().get(0);
		assertThat(inschrijving.getRecordType(),
			is(BRONConstants.BVE_AANLEVERING_VAVO_INSCHRIJFGEGEVENS));
		assertThat(inschrijving.getSoortMutatie(), is(Toevoeging));
		assertThat(inschrijving.getInschrijvingsvolgnummer(), is("1"));
		assertThat(inschrijving.getGevolgdeOpleiding(), is("5150"));
		assertThat(inschrijving.getDatumInschrijving(), is(asDate(20080101)));
		assertThat(inschrijving.getGeplandeDatumUitschrijving(), is(asDate(20100101)));
		assertThat(inschrijving.getHoogsteVooropleiding(), is(HAVO));
		assertThat(inschrijving.getIndicatieNieuwkomer(), is(true));
		assertThat(inschrijving.getContacturenPerWeek(), is(10));
	}

	private void createGeval13() throws Exception
	{
		getDeelnemer2007();
		addChange(verbintenis, "status", null, Definitief);
	}

	@Test
	public void proefgeval14() throws Exception
	{
		createGeval14();
		controller.save();

		BronAanleverMelding melding = getEersteMelding();
		assertThat(melding.getLeerlingnummer(), is("2014"));
		assertThat(melding.getSofinummer(), is("210000156"));
		assertThat(melding.getOnderwijsnummer(), is((String) null));
		assertThat(melding.getGeboortedatum(), is(Datum.valueOf("19910514")));
		assertThat(melding.getGeslacht(), is(Man));
		assertThat(melding.getPostcodeVolgensInstelling(), is("9722TB"));
		assertThat(melding.getLand(), is((String) null));

		// je zou 305, 330, 331, 332 verwachten, maar we hebben besloten om de
		// examengegevens pas aan te maken op batch generatie moment, niet op
		// wijzigingsmoment.

		assertThat(getRecordTypes(melding), is(equalTo(Arrays.asList(305, 330))));

		InschrijvingsgegevensRecord inschrijving = melding.getMeldingen().get(0);
		assertThat(inschrijving.getRecordType(),
			is(BRONConstants.BVE_AANLEVERING_VAVO_INSCHRIJFGEGEVENS));
		assertThat(inschrijving.getSoortMutatie(), is(Toevoeging));
		assertThat(inschrijving.getInschrijvingsvolgnummer(), is("1"));
		assertThat(inschrijving.getGevolgdeOpleiding(), is("5370"));
		assertThat(inschrijving.getDatumInschrijving(), is(asDate(20080101)));
		assertThat(inschrijving.getGeplandeDatumUitschrijving(), is(asDate(20100101)));
		assertThat(inschrijving.getHoogsteVooropleiding(), is(HAVO));
		assertThat(inschrijving.getIndicatieNieuwkomer(), is(false));
		assertThat(inschrijving.getContacturenPerWeek(), is(10));

		Examendeelname examen = verbintenis.getExamendeelnames().get(0);
		assertThat(examen.isGewijzigd(), is(true));
	}

	private void createGeval14() throws Exception
	{
		getDeelnemer2014();

		Examendeelname examen = verbintenis.getExamendeelnames().get(0);
		addChange(verbintenis, "status", null, Definitief);
		addChange(examen, "examenstatus", null, examen.getExamenstatus());
	}

	@Test
	public void proefgeval15() throws Exception
	{
		createGeval15();
		controller.save();

		BronAanleverMelding melding = getEersteMelding();
		assertThat(melding.getLeerlingnummer(), is("2015"));
		assertThat(melding.getSofinummer(), is("210000168"));
		assertThat(melding.getOnderwijsnummer(), is((String) null));
		assertThat(melding.getGeboortedatum(), is(Datum.valueOf("19910515")));
		assertThat(melding.getGeslacht(), is(Man));
		assertThat(melding.getPostcodeVolgensInstelling(), is("9722TB"));
		assertThat(melding.getLand(), is((String) null));

		assertThat(getRecordTypes(melding), is(Arrays.asList(305, 330)));

		InschrijvingsgegevensRecord inschrijving = melding.getMeldingen().get(0);
		assertThat(inschrijving.getRecordType(),
			is(BRONConstants.BVE_AANLEVERING_VAVO_INSCHRIJFGEGEVENS));
		assertThat(inschrijving.getSoortMutatie(), is(Toevoeging));
		assertThat(inschrijving.getInschrijvingsvolgnummer(), is("1"));
		assertThat(inschrijving.getGevolgdeOpleiding(), is("5150"));
		assertThat(inschrijving.getDatumInschrijving(), is(asDate(20080101)));
		assertThat(inschrijving.getGeplandeDatumUitschrijving(), is(asDate(20100101)));
		assertThat(inschrijving.getHoogsteVooropleiding(), is(HAVO));
		assertThat(inschrijving.getIndicatieNieuwkomer(), is(false));
		assertThat(inschrijving.getContacturenPerWeek(), is(10));

		Examendeelname examen = verbintenis.getExamendeelnames().get(0);
		assertThat(examen.isGewijzigd(), is(true));
	}

	private void createGeval15() throws Exception
	{
		getDeelnemer2015();
		Examendeelname examen = verbintenis.getExamendeelnames().get(0);
		addChange(verbintenis, "status", null, Definitief);
		addChange(examen, "examenstatus", null, examen.getExamenstatus());
	}

	@Test
	public void proefgeval16() throws Exception
	{
		createGeval16();
		controller.save();

		BronAanleverMelding melding = getEersteMelding();
		assertThat(melding.getLeerlingnummer(), is("2016"));
		assertThat(melding.getSofinummer(), is("210000181"));
		assertThat(melding.getOnderwijsnummer(), is((String) null));
		assertThat(melding.getGeboortedatum(), is(Datum.valueOf("19910516")));
		assertThat(melding.getGeslacht(), is(Man));
		assertThat(melding.getPostcodeVolgensInstelling(), is("9722TB"));
		assertThat(melding.getLand(), is((String) null));

		assertThat(getRecordTypes(melding), is(equalTo(Arrays.asList(305, 330))));

		InschrijvingsgegevensRecord inschrijving = melding.getMeldingen().get(0);
		assertThat(inschrijving.getRecordType(),
			is(BRONConstants.BVE_AANLEVERING_VAVO_INSCHRIJFGEGEVENS));
		assertThat(inschrijving.getSoortMutatie(), is(Toevoeging));
		assertThat(inschrijving.getInschrijvingsvolgnummer(), is("1"));
		assertThat(inschrijving.getGevolgdeOpleiding(), is("5150"));
		assertThat(inschrijving.getDatumInschrijving(), is(asDate(20080101)));
		assertThat(inschrijving.getGeplandeDatumUitschrijving(), is(asDate(20100101)));
		assertThat(inschrijving.getHoogsteVooropleiding(), is(HAVO));
		assertThat(inschrijving.getIndicatieNieuwkomer(), is(false));
		assertThat(inschrijving.getContacturenPerWeek(), is(10));

		Examendeelname examen = verbintenis.getExamendeelnames().get(0);
		assertThat(examen.isGewijzigd(), is(true));
	}

	private void createGeval16() throws Exception
	{
		getDeelnemer2016();
		Examendeelname examen = verbintenis.getExamendeelnames().get(0);
		addChange(verbintenis, "status", null, Definitief);
		addChange(examen, "examenstatus", null, examen.getExamenstatus());
	}

	@Test
	public void bveVAVOBatch2() throws Exception
	{
		// deze batch komt niet meer overeen met de schakeltest batch, aangezien de
		// examenmeldingen niet door de BRON controller worden aangemaakt.
		createGeval8();
		createGeval9();
		createGeval10();
		createGeval11();
		createGeval12();
		createGeval13();
		createGeval14();
		createGeval15();
		createGeval16();

		batchBVE = maakBronVAVOBatch();
		batchBVE.setBatchNummer(2);
		batchBVE.getAanleverpunt().setNummer(1);

		assertThat(batchBVE.getAantalMeldingen(), is(9));
		assertThat(getRecordTypesFromBatch(), is(equalTo(Arrays.asList(305, 330, 305, 330, 305,
			330, 305, 310, 330, 305, 310, 330, 305, 330, 305, 330, 305, 330, 305, 330))));
		assertThat(batchBVE.getAantalRecords(), is(21));

		writeBronBatch(batchBVE);
	}
}
