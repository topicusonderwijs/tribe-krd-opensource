package nl.topicus.eduarte.krd.bron.schakeltest.bve.aanlevering.educatie;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import nl.topicus.cobra.types.personalia.Geslacht;
import nl.topicus.eduarte.entities.examen.Examendeelname;
import nl.topicus.eduarte.entities.inschrijving.OnderwijsproductAfname;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis.VerbintenisStatus;
import nl.topicus.eduarte.entities.onderwijsproduct.Onderwijsproduct;
import nl.topicus.eduarte.entities.onderwijsproduct.OnderwijsproductAfnameContext;
import nl.topicus.eduarte.entities.resultaatstructuur.Resultaat;
import nl.topicus.eduarte.entities.resultaatstructuur.Schaalwaarde;
import nl.topicus.eduarte.entities.resultaatstructuur.Toets.SoortToets;
import nl.topicus.eduarte.krd.entities.bron.meldingen.aanlevering.BronAanleverMelding;
import nl.topicus.onderwijs.duo.bron.BRONConstants;
import nl.topicus.onderwijs.duo.bron.bve.batches.pve_9_9.aanlevering.ed.InschrijvingsgegevensRecord;
import nl.topicus.onderwijs.duo.bron.bve.batches.pve_9_9.aanlevering.ed.NT2Vaardigheden;
import nl.topicus.onderwijs.duo.bron.bve.batches.pve_9_9.aanlevering.ed.ResultaatgegevensRecord;
import nl.topicus.onderwijs.duo.bron.bve.batches.pve_9_9.aanlevering.ed.VakgegevensRecord;
import nl.topicus.onderwijs.duo.bron.bve.waardelijsten.HoogsteVooropleiding;
import nl.topicus.onderwijs.duo.bron.bve.waardelijsten.NT2Niveau;
import nl.topicus.onderwijs.duo.bron.bve.waardelijsten.NT2Vaardigheid;
import nl.topicus.onderwijs.duo.bron.bve.waardelijsten.Sectordeel;
import nl.topicus.onderwijs.duo.bron.bve.waardelijsten.SoortMutatie;
import nl.topicus.onderwijs.duo.bron.bve.waardelijsten.VerantwoordelijkeAanleverbestand;
import nl.topicus.onderwijs.duo.bron.data.types.Datum;
import nl.topicus.onderwijs.duo.bron.data.waardelijsten.BestandSoort;

import org.junit.Before;
import org.junit.Test;

public class ProefgevallenBveEDBatch3Test extends ProefgevallenBveED
{
	@Before
	public void setUp()
	{
		tester.setupRequestAndResponse();
	}

	@Test
	public void proefgeval28() throws Exception
	{
		createGeval28();
		controller.save();

		BronAanleverMelding melding = getEersteMelding();

		assertThat(melding.getLeerlingnummer(), is("3009"));
		assertThat(melding.getSofinummer(), is("210000107"));
		assertThat(melding.getOnderwijsnummer(), is((String) null));
		assertThat(melding.getGeboortedatum(), is(Datum.valueOf("19910509")));
		assertThat(melding.getPostcodeVolgensInstelling(), is("9722TB"));
		assertThat(melding.getGeslacht(), is(Geslacht.Man));
		assertThat(melding.getLand(), is((String) null));

		InschrijvingsgegevensRecord inschrijving = melding.getMeldingen().get(0);
		assertThat(inschrijving.getRecordType(),
			is(BRONConstants.BVE_AANLEVERING_ED_INSCHRIJFGEGEVENS));
		assertThat(inschrijving.getSoortMutatie(), is(SoortMutatie.Aanpassing));
		assertThat(inschrijving.getInschrijvingsvolgnummer(), is("1"));
		assertThat(inschrijving.getGevolgdeOpleiding(), is("5511"));
		assertThat(inschrijving.getDatumInschrijving(), is(asDate(20080101)));
		assertThat(inschrijving.getGeplandeDatumUitschrijving(), is(asDate(20100101)));
		assertThat(inschrijving.getWerkelijkeDatumUitschrijving(), is(asDate(20090101)));
		assertThat(inschrijving.getHoogsteVooropleiding(), is(HoogsteVooropleiding.HAVO));
		assertThat(inschrijving.getIndicatieNieuwkomer(), is(false));
		assertThat(inschrijving.getContacturenPerWeek(), is(10));

		assertThat(melding.getMeldingen().size(), is(1));
	}

	private void createGeval28() throws Exception
	{
		getDeelnemer3009();
		verbintenis.setEinddatum(asDate(20090101));
		addChange(verbintenis, "einddatum", null, asDate(20090101));
	}

	@Test
	public void proefgeval29() throws Exception
	{
		createGeval29();
		controller.save();

		BronAanleverMelding melding = getEersteMelding();

		assertThat(melding.getLeerlingnummer(), is("3010"));
		assertThat(melding.getSofinummer(), is("210000119"));
		assertThat(melding.getOnderwijsnummer(), is((String) null));
		assertThat(melding.getGeboortedatum(), is(Datum.valueOf("19910510")));
		assertThat(melding.getPostcodeVolgensInstelling(), is("9722TB"));
		assertThat(melding.getGeslacht(), is(Geslacht.Man));
		assertThat(melding.getLand(), is((String) null));

		InschrijvingsgegevensRecord inschrijving = melding.getMeldingen().get(0);
		assertThat(inschrijving.getRecordType(),
			is(BRONConstants.BVE_AANLEVERING_ED_INSCHRIJFGEGEVENS));
		assertThat(inschrijving.getSoortMutatie(), is(SoortMutatie.Aanpassing));
		assertThat(inschrijving.getInschrijvingsvolgnummer(), is("1"));
		assertThat(inschrijving.getGevolgdeOpleiding(), is("5511"));
		assertThat(inschrijving.getDatumInschrijving(), is(asDate(20080101)));
		assertThat(inschrijving.getGeplandeDatumUitschrijving(), is(asDate(20090731)));
		assertThat(inschrijving.getWerkelijkeDatumUitschrijving(), is((Date) null));
		assertThat(inschrijving.getHoogsteVooropleiding(), is(HoogsteVooropleiding.HAVO));
		assertThat(inschrijving.getIndicatieNieuwkomer(), is(false));
		assertThat(inschrijving.getContacturenPerWeek(), is(10));

		assertThat(melding.getMeldingen().size(), is(1));
	}

	private void createGeval29() throws Exception
	{
		getDeelnemer3010();
		verbintenis.setGeplandeEinddatum(asDate(20090731));
		addChange(verbintenis, "geplandeEinddatum", asDate(20100101), asDate(20090731));
	}

	@Test
	public void proefgeval30() throws Exception
	{
		createGeval30();
		controller.save();

		BronAanleverMelding melding = getEersteMelding();

		assertThat(melding.getLeerlingnummer(), is("3011"));
		assertThat(melding.getSofinummer(), is("210000120"));
		assertThat(melding.getOnderwijsnummer(), is((String) null));
		assertThat(melding.getGeboortedatum(), is(Datum.valueOf("19910511")));
		assertThat(melding.getPostcodeVolgensInstelling(), is("9722TB"));
		assertThat(melding.getGeslacht(), is(Geslacht.Man));
		assertThat(melding.getLand(), is((String) null));

		InschrijvingsgegevensRecord inschrijving = melding.getMeldingen().get(0);
		assertThat(inschrijving.getRecordType(),
			is(BRONConstants.BVE_AANLEVERING_ED_INSCHRIJFGEGEVENS));
		assertThat(inschrijving.getSoortMutatie(), is(SoortMutatie.Aanpassing));
		assertThat(inschrijving.getInschrijvingsvolgnummer(), is("1"));
		assertThat(inschrijving.getGevolgdeOpleiding(), is("5511"));
		assertThat(inschrijving.getDatumInschrijving(), is(asDate(20080801)));
		assertThat(inschrijving.getGeplandeDatumUitschrijving(), is(asDate(20100101)));
		assertThat(inschrijving.getWerkelijkeDatumUitschrijving(), is((Date) null));
		assertThat(inschrijving.getHoogsteVooropleiding(), is(HoogsteVooropleiding.HAVO));
		assertThat(inschrijving.getIndicatieNieuwkomer(), is(false));
		assertThat(inschrijving.getContacturenPerWeek(), is(10));

		assertThat(melding.getMeldingen().size(), is(1));
	}

	private void createGeval30() throws Exception
	{
		getDeelnemer3011();
		verbintenis.setBegindatum(asDate(20080801));
		addChange(verbintenis, "begindatum", asDate(20080101), asDate(20080801));
	}

	@Test
	public void proefgeval31() throws Exception
	{
		createGeval31();
		controller.save();

		BronAanleverMelding melding = getEersteMelding();

		assertThat(melding.getLeerlingnummer(), is("3012"));
		assertThat(melding.getSofinummer(), is("210000132"));
		assertThat(melding.getOnderwijsnummer(), is((String) null));
		assertThat(melding.getGeboortedatum(), is(Datum.valueOf("19910512")));
		assertThat(melding.getPostcodeVolgensInstelling(), is("9722TB"));
		assertThat(melding.getGeslacht(), is(Geslacht.Man));
		assertThat(melding.getLand(), is((String) null));

		InschrijvingsgegevensRecord inschrijving = melding.getMeldingen().get(0);
		assertThat(inschrijving.getRecordType(),
			is(BRONConstants.BVE_AANLEVERING_ED_INSCHRIJFGEGEVENS));
		assertThat(inschrijving.getSoortMutatie(), is(SoortMutatie.Aanpassing));
		assertThat(inschrijving.getInschrijvingsvolgnummer(), is("1"));
		assertThat(inschrijving.getGevolgdeOpleiding(), is("5511"));
		assertThat(inschrijving.getDatumInschrijving(), is(asDate(20080101)));
		assertThat(inschrijving.getGeplandeDatumUitschrijving(), is(asDate(20100101)));
		assertThat(inschrijving.getWerkelijkeDatumUitschrijving(), is((Date) null));
		assertThat(inschrijving.getHoogsteVooropleiding(), is(HoogsteVooropleiding.HAVO));
		assertThat(inschrijving.getIndicatieNieuwkomer(), is(false));
		assertThat(inschrijving.getContacturenPerWeek(), is(20));

		assertThat(melding.getMeldingen().size(), is(1));
	}

	private void createGeval31() throws Exception
	{
		getDeelnemer3012();
		verbintenis.setContacturenPerWeek(BigDecimal.valueOf(20));
		addChange(verbintenis, "contacturenPerWeek", 10, 20);
	}

	@Test
	public void proefgeval32() throws Exception
	{
		createGeval32();
		controller.save();

		BronAanleverMelding melding = getEersteMelding();

		assertThat(melding.getLeerlingnummer(), is("3013"));
		assertThat(melding.getSofinummer(), is("210000144"));
		assertThat(melding.getOnderwijsnummer(), is((String) null));
		assertThat(melding.getGeboortedatum(), is(Datum.valueOf("19910513")));
		assertThat(melding.getPostcodeVolgensInstelling(), is("9722TB"));
		assertThat(melding.getGeslacht(), is(Geslacht.Man));
		assertThat(melding.getLand(), is((String) null));

		InschrijvingsgegevensRecord inschrijving = melding.getMeldingen().get(0);
		assertThat(inschrijving.getRecordType(),
			is(BRONConstants.BVE_AANLEVERING_ED_INSCHRIJFGEGEVENS));
		assertThat(inschrijving.getSoortMutatie(), is(SoortMutatie.Verwijdering));
		assertThat(inschrijving.getInschrijvingsvolgnummer(), is("1"));
		// Onderstaande velden zouden bij SoortMutatie = Verwijdering null moeten zijn,
		// echter laat de huidige implementatie dit niet toe.
		//
		// assertThat(inschrijving.getGevolgdeOpleiding(), is((String) null));
		// assertThat(inschrijving.getDatumInschrijving(), is((Date) null));
		// assertThat(inschrijving.getGeplandeDatumUitschrijving(), is((Date) null));
		// assertThat(inschrijving.getWerkelijkeDatumUitschrijving(), is((Date) null));
		// assertThat(inschrijving.getHoogsteVooropleiding(), is((HoogsteVooropleiding)
		// null));
		// assertThat(inschrijving.getIndicatieNieuwkomer(), is((Boolean) null));
		// assertThat(inschrijving.getContacturenPerWeek(), is((Integer) null));

		assertThat(melding.getMeldingen().size(), is(1));
	}

	private void createGeval32() throws Exception
	{
		getDeelnemer3013();
		verbintenis.setStatus(VerbintenisStatus.Afgemeld);
		addChange(verbintenis, "status", VerbintenisStatus.Definitief, VerbintenisStatus.Afgemeld);
	}

	@Test
	public void proefgeval33() throws Exception
	{
		createGeval33();
		controller.save();

		BronAanleverMelding melding = getEersteMelding();

		assertThat(melding.getLeerlingnummer(), is("3014"));
		assertThat(melding.getSofinummer(), is("210000156"));
		assertThat(melding.getOnderwijsnummer(), is((String) null));
		assertThat(melding.getGeboortedatum(), is(Datum.valueOf("19910514")));
		assertThat(melding.getPostcodeVolgensInstelling(), is("9722TB"));
		assertThat(melding.getGeslacht(), is(Geslacht.Man));
		assertThat(melding.getLand(), is((String) null));

		ResultaatgegevensRecord resultaatmelding = melding.getMeldingen().get(0);
		assertThat(resultaatmelding.getRecordType(),
			is(BRONConstants.BVE_AANLEVERING_ED_RESULTAATGEGEVENS));
		assertThat(resultaatmelding.getSoortMutatie(), is(SoortMutatie.Toevoeging));
		assertThat(resultaatmelding.getInschrijvingsvolgnummer(), is("1"));
		assertThat(resultaatmelding.getVoltooideOpleiding(), is("5511"));
		assertThat(resultaatmelding.getDatumVoltooid(), is(asDate(20081201)));

		assertThat(melding.getMeldingen().size(), is(1));
	}

	private void createGeval33() throws Exception
	{
		getDeelnemer3014();

		Examendeelname deelname = addExamendeelname(verbintenis, 20081201);
		deelname.setExamenstatus(getGeslaagd());
		addChange(deelname, "examenstatus", null, deelname.getExamenstatus());

	}

	@Test
	public void proefgeval34() throws Exception
	{
		createGeval34();
		controller.save();

		BronAanleverMelding melding = getEersteMelding();

		assertThat(melding.getLeerlingnummer(), is("3015"));
		assertThat(melding.getSofinummer(), is("210000168"));
		assertThat(melding.getOnderwijsnummer(), is((String) null));
		assertThat(melding.getGeboortedatum(), is(Datum.valueOf("19910515")));
		assertThat(melding.getPostcodeVolgensInstelling(), is("9722TB"));
		assertThat(melding.getGeslacht(), is(Geslacht.Man));
		assertThat(melding.getLand(), is((String) null));

		ResultaatgegevensRecord resultaatmelding = melding.getMeldingen().get(0);
		assertThat(resultaatmelding.getRecordType(),
			is(BRONConstants.BVE_AANLEVERING_ED_RESULTAATGEGEVENS));
		assertThat(resultaatmelding.getSoortMutatie(), is(SoortMutatie.Aanpassing));
		assertThat(resultaatmelding.getInschrijvingsvolgnummer(), is("1"));
		assertThat(resultaatmelding.getVoltooideOpleiding(), is("5511"));
		assertThat(resultaatmelding.getDatumVoltooid(), is(asDate(20081101)));

		assertThat(melding.getMeldingen().size(), is(1));
	}

	private void createGeval34() throws Exception
	{
		getDeelnemer3015();

		Examendeelname deelname = addExamendeelname(verbintenis, 20081101);
		deelname.setExamenstatus(getGeslaagd());

		addChange(deelname, "datumUitslag", asDate(20081001), asDate(20081101));
	}

	@Test
	public void proefgeval35() throws Exception
	{
		createGeval35();
		controller.save();

		BronAanleverMelding melding = getEersteMelding();

		assertThat(melding.getLeerlingnummer(), is("3016"));
		assertThat(melding.getSofinummer(), is("210000181"));
		assertThat(melding.getOnderwijsnummer(), is((String) null));
		assertThat(melding.getGeboortedatum(), is(Datum.valueOf("19910516")));
		assertThat(melding.getPostcodeVolgensInstelling(), is("9722TB"));
		assertThat(melding.getGeslacht(), is(Geslacht.Man));
		assertThat(melding.getLand(), is((String) null));

		ResultaatgegevensRecord resultaatmelding = melding.getMeldingen().get(0);
		assertThat(resultaatmelding.getRecordType(),
			is(BRONConstants.BVE_AANLEVERING_ED_RESULTAATGEGEVENS));
		assertThat(resultaatmelding.getSoortMutatie(), is(SoortMutatie.Verwijdering));
		assertThat(resultaatmelding.getInschrijvingsvolgnummer(), is("1"));
		assertThat(resultaatmelding.getVoltooideOpleiding(), is("5501"));

		// veld is verplicht, kan dus niet null zijn
		// assertThat(resultaatmelding.getDatumVoltooid(), is((Date) null));

		assertThat(melding.getMeldingen().size(), is(1));

	}

	private void createGeval35() throws Exception
	{
		getDeelnemer3016();

		Examendeelname deelname = addExamendeelname(verbintenis, 20081001);
		deelname.setExamenstatus(getGeslaagd());

		addChange(deelname, "examenstatus", deelname.getExamenstatus(), null);
	}

	@Test
	public void proefgeval36() throws Exception
	{
		createGeval36();
		controller.save();

		BronAanleverMelding melding = getEersteMelding();

		assertThat(melding.getLeerlingnummer(), is("3017"));
		assertThat(melding.getSofinummer(), is("210000193"));
		assertThat(melding.getOnderwijsnummer(), is((String) null));
		assertThat(melding.getGeboortedatum(), is(Datum.valueOf("19910517")));
		assertThat(melding.getPostcodeVolgensInstelling(), is("9722TB"));
		assertThat(melding.getGeslacht(), is(Geslacht.Man));
		assertThat(melding.getLand(), is((String) null));

		VakgegevensRecord vakgegevens = melding.getMeldingen().get(0);
		assertThat(vakgegevens.getRecordType(), is(BRONConstants.BVE_AANLEVERING_ED_VAKGEGEVENS));
		assertThat(vakgegevens.getSoortMutatie(), is(SoortMutatie.Toevoeging));
		assertThat(vakgegevens.getInschrijvingsvolgnummer(), is("1"));
		assertThat(vakgegevens.getVakvolgnummer(), is(2));
		assertThat(vakgegevens.getVak(), is("0011"));
		assertThat(vakgegevens.getDatumVoltooid(), is((Date) null));

		assertThat(melding.getMeldingen().size(), is(1));

	}

	private void createGeval36() throws Exception
	{
		getDeelnemer3017();
		addEDVak("0011", 2);

		List<OnderwijsproductAfnameContext> huidigeAfname =
			verbintenis.getAfnameContexten().subList(0, 1);

		addChange(verbintenis, "afnameContexten", huidigeAfname, verbintenis.getAfnameContexten());
	}

	@Test
	public void proefgeval37() throws Exception
	{
		createGeval37();
		controller.save();

		BronAanleverMelding melding = getEersteMelding();

		assertThat(melding.getLeerlingnummer(), is("3018"));
		assertThat(melding.getSofinummer(), is("210000211"));
		assertThat(melding.getOnderwijsnummer(), is((String) null));
		assertThat(melding.getGeboortedatum(), is(Datum.valueOf("19910518")));
		assertThat(melding.getPostcodeVolgensInstelling(), is("9722TB"));
		assertThat(melding.getGeslacht(), is(Geslacht.Man));
		assertThat(melding.getLand(), is((String) null));

		VakgegevensRecord vakgegevensmelding = melding.getMeldingen().get(0);
		assertThat(vakgegevensmelding.getRecordType(),
			is(BRONConstants.BVE_AANLEVERING_ED_VAKGEGEVENS));
		assertThat(vakgegevensmelding.getSoortMutatie(), is(SoortMutatie.Toevoeging));
		assertThat(vakgegevensmelding.getInschrijvingsvolgnummer(), is("1"));
		assertThat(vakgegevensmelding.getVakvolgnummer(), is(2));
		assertThat(vakgegevensmelding.getVak(), is("0990"));
		assertThat(vakgegevensmelding.getDatumVoltooid(), is((Date) null));

		NT2Vaardigheden vaardigheidmelding = melding.getMeldingen().get(1);
		assertThat(vaardigheidmelding.getRecordType(),
			is(BRONConstants.BVE_AANLEVERING_ED_NT2VAARDIGHEDEN));
		assertThat(vaardigheidmelding.getSoortMutatie(), is(SoortMutatie.Toevoeging));
		assertThat(vaardigheidmelding.getInschrijvingsvolgnummer(), is("1"));
		assertThat(vaardigheidmelding.getVakvolgnummer(), is(2));
		assertThat(vaardigheidmelding.getNT2Vaardigheid(), is(NT2Vaardigheid.Spreken));
		assertThat(vaardigheidmelding.getStartniveau(), is(NT2Niveau.A1BasicUserBreakthrough));
		assertThat(vaardigheidmelding.getBehaaldNiveau(), is((NT2Niveau) null));

		assertThat(melding.getMeldingen().size(), is(2));

	}

	private void createGeval37() throws Exception
	{
		getDeelnemer3018();

		List<OnderwijsproductAfnameContext> vorigeContext =
			new ArrayList<OnderwijsproductAfnameContext>();
		for (OnderwijsproductAfnameContext context : verbintenis.getAfnameContexten())
		{
			vorigeContext.add(context);
		}

		addEDVak("0990", 2);
		addChange(verbintenis, "afnameContexten", vorigeContext, verbintenis.getAfnameContexten());
	}

	@Test
	public void proefgeval38() throws Exception
	{
		createGeval38();
		controller.save();

		BronAanleverMelding melding = getEersteMelding();

		assertThat(melding.getLeerlingnummer(), is("3019"));
		assertThat(melding.getSofinummer(), is("210000223"));
		assertThat(melding.getOnderwijsnummer(), is((String) null));
		assertThat(melding.getGeboortedatum(), is(Datum.valueOf("19910519")));
		assertThat(melding.getPostcodeVolgensInstelling(), is("9722TB"));
		assertThat(melding.getGeslacht(), is(Geslacht.Man));
		assertThat(melding.getLand(), is((String) null));

		NT2Vaardigheden vaardigheidmelding = melding.getMeldingen().get(0);
		assertThat(vaardigheidmelding.getRecordType(),
			is(BRONConstants.BVE_AANLEVERING_ED_NT2VAARDIGHEDEN));
		assertThat(vaardigheidmelding.getSoortMutatie(), is(SoortMutatie.Toevoeging));
		assertThat(vaardigheidmelding.getInschrijvingsvolgnummer(), is("1"));
		assertThat(vaardigheidmelding.getVakvolgnummer(), is(1));
		assertThat(vaardigheidmelding.getNT2Vaardigheid(), is(NT2Vaardigheid.Luisteren));
		assertThat(vaardigheidmelding.getStartniveau(), is(NT2Niveau.A1BasicUserBreakthrough));
		assertThat(vaardigheidmelding.getBehaaldNiveau(), is((NT2Niveau) null));

		assertThat(melding.getMeldingen().size(), is(1));
	}

	private void createGeval38() throws Exception
	{
		getDeelnemer3019();
		deelnemer.setId(3019L);

		Resultaat resultaat = new Resultaat();
		Schaalwaarde schaalwaarde = getNT2Schaalwaarde(resultaat, SoortToets.Luisteren, "1", null);

		addChange(resultaat, "waarde", null, schaalwaarde);
	}

	@Test
	public void proefgeval39() throws Exception
	{
		createGeval39();
		controller.save();

		BronAanleverMelding melding = getEersteMelding();

		assertThat(melding.getLeerlingnummer(), is("3020"));
		assertThat(melding.getSofinummer(), is("210000235"));
		assertThat(melding.getOnderwijsnummer(), is((String) null));
		assertThat(melding.getGeboortedatum(), is(Datum.valueOf("19910520")));
		assertThat(melding.getPostcodeVolgensInstelling(), is("9722TB"));
		assertThat(melding.getGeslacht(), is(Geslacht.Man));
		assertThat(melding.getLand(), is((String) null));

		VakgegevensRecord vakgegevensmelding = melding.getMeldingen().get(0);
		assertThat(vakgegevensmelding.getRecordType(),
			is(BRONConstants.BVE_AANLEVERING_ED_VAKGEGEVENS));
		assertThat(vakgegevensmelding.getSoortMutatie(), is(SoortMutatie.Aanpassing));
		assertThat(vakgegevensmelding.getInschrijvingsvolgnummer(), is("1"));
		assertThat(vakgegevensmelding.getVakvolgnummer(), is(1));
		assertThat(vakgegevensmelding.getVak(), is("0310"));
		assertThat(vakgegevensmelding.getDatumVoltooid(), is(asDate(20081201)));

		assertThat(melding.getMeldingen().size(), is(1));
	}

	private void createGeval39() throws Exception
	{
		getDeelnemer3020();
		verbintenis.getOnderwijsproductAfnames().get(0).setEinddatum(asDate(20081201));

		addChange(verbintenis.getOnderwijsproductAfnames().get(0), "einddatum", null,
			asDate(20081201));
	}

	@Test
	public void proefgeval40() throws Exception
	{
		createGeval40();
		controller.save();

		BronAanleverMelding melding = getEersteMelding();

		assertThat(melding.getLeerlingnummer(), is("3021"));
		assertThat(melding.getSofinummer(), is("210000259"));
		assertThat(melding.getOnderwijsnummer(), is((String) null));
		assertThat(melding.getGeboortedatum(), is(Datum.valueOf("19910521")));
		assertThat(melding.getPostcodeVolgensInstelling(), is("9722TB"));
		assertThat(melding.getGeslacht(), is(Geslacht.Man));
		assertThat(melding.getLand(), is((String) null));

		VakgegevensRecord vakgegevensmelding = melding.getMeldingen().get(0);
		assertThat(vakgegevensmelding.getRecordType(),
			is(BRONConstants.BVE_AANLEVERING_ED_VAKGEGEVENS));
		assertThat(vakgegevensmelding.getSoortMutatie(), is(SoortMutatie.Aanpassing));
		assertThat(vakgegevensmelding.getInschrijvingsvolgnummer(), is("1"));
		assertThat(vakgegevensmelding.getVakvolgnummer(), is(1));
		assertThat(vakgegevensmelding.getVak(), is("0011"));
		assertThat(vakgegevensmelding.getDatumVoltooid(), is(((Date) null)));

		assertThat(melding.getMeldingen().size(), is(1));
	}

	private void createGeval40() throws Exception
	{
		getDeelnemer3021();

		OnderwijsproductAfnameContext context = verbintenis.getAfnameContexten().get(0);

		OnderwijsproductAfname vorigeAfname = context.getOnderwijsproductAfname();

		Onderwijsproduct vak = getEDVak("0011", 1);
		OnderwijsproductAfname nieuweAfname = new OnderwijsproductAfname();
		nieuweAfname.setBegindatum(new Date());
		nieuweAfname.setOnderwijsproduct(vak);
		nieuweAfname.setDeelnemer(deelnemer);
		context.setOnderwijsproductAfname(nieuweAfname);

		addChange(verbintenis.getAfnameContexten().get(0), "onderwijsproductAfname", vorigeAfname,
			nieuweAfname);
	}

	@Test
	public void proefgeval41() throws Exception
	{
		createGeval41();
		controller.save();

		BronAanleverMelding melding = getEersteMelding();

		assertThat(melding.getLeerlingnummer(), is("3022"));
		assertThat(melding.getSofinummer(), is("210000260"));
		assertThat(melding.getOnderwijsnummer(), is((String) null));
		assertThat(melding.getGeboortedatum(), is(Datum.valueOf("19910522")));
		assertThat(melding.getPostcodeVolgensInstelling(), is("9722TB"));
		assertThat(melding.getGeslacht(), is(Geslacht.Man));
		assertThat(melding.getLand(), is((String) null));

		NT2Vaardigheden vaardigheidmelding = melding.getMeldingen().get(0);
		assertThat(vaardigheidmelding.getRecordType(),
			is(BRONConstants.BVE_AANLEVERING_ED_NT2VAARDIGHEDEN));
		assertThat(vaardigheidmelding.getSoortMutatie(), is(SoortMutatie.Aanpassing));
		assertThat(vaardigheidmelding.getInschrijvingsvolgnummer(), is("1"));
		assertThat(vaardigheidmelding.getVakvolgnummer(), is(1));
		assertThat(vaardigheidmelding.getNT2Vaardigheid(), is(NT2Vaardigheid.Spreken));
		assertThat(vaardigheidmelding.getStartniveau(), is(NT2Niveau.A1BasicUserBreakthrough));
		assertThat(vaardigheidmelding.getBehaaldNiveau(), is(NT2Niveau.A2BasicUserWaystage));

		assertThat(melding.getMeldingen().size(), is(1));
	}

	private void createGeval41() throws Exception
	{
		getDeelnemer3022();
		deelnemer.setId(3022L);

		Resultaat resultaat = new Resultaat();
		resultaat.setOverschrijft(new Resultaat());
		Schaalwaarde schaalwaarde = getNT2Schaalwaarde(resultaat, SoortToets.Spreken, "1", "2");

		addChange(resultaat, "waarde", null, schaalwaarde);
	}

	@Test
	public void proefgeval42() throws Exception
	{
		createGeval42();
		controller.save();

		BronAanleverMelding melding = getEersteMelding();

		assertThat(melding.getLeerlingnummer(), is("3023"));
		assertThat(melding.getSofinummer(), is("210000272"));
		assertThat(melding.getOnderwijsnummer(), is((String) null));
		assertThat(melding.getGeboortedatum(), is(Datum.valueOf("19910523")));
		assertThat(melding.getPostcodeVolgensInstelling(), is("9722TB"));
		assertThat(melding.getGeslacht(), is(Geslacht.Man));
		assertThat(melding.getLand(), is((String) null));

		NT2Vaardigheden vaardigheidmelding = melding.getMeldingen().get(0);
		assertThat(vaardigheidmelding.getRecordType(),
			is(BRONConstants.BVE_AANLEVERING_ED_NT2VAARDIGHEDEN));
		assertThat(vaardigheidmelding.getSoortMutatie(), is(SoortMutatie.Aanpassing));
		assertThat(vaardigheidmelding.getInschrijvingsvolgnummer(), is("1"));
		assertThat(vaardigheidmelding.getVakvolgnummer(), is(1));
		assertThat(vaardigheidmelding.getNT2Vaardigheid(), is(NT2Vaardigheid.Spreken));
		assertThat(vaardigheidmelding.getStartniveau(), is(NT2Niveau.A2BasicUserWaystage));
		assertThat(vaardigheidmelding.getBehaaldNiveau(), is((NT2Niveau) null));

		assertThat(melding.getMeldingen().size(), is(1));

	}

	private void createGeval42() throws Exception
	{
		getDeelnemer3023();
		deelnemer.setId(3023L);

		Resultaat oudeResultaat = new Resultaat();
		getNT2Schaalwaarde(oudeResultaat, SoortToets.Spreken, "1", null);

		Resultaat resultaat = new Resultaat();
		Schaalwaarde nieuweSchaalwaarde =
			getNT2Schaalwaarde(resultaat, SoortToets.Spreken, "2", null);

		resultaat.setOverschrijft(oudeResultaat);

		addChange(resultaat, "waarde", null, nieuweSchaalwaarde);
	}

	@Test
	public void proefgeval43() throws Exception
	{
		createGeval43();
		controller.save();

		BronAanleverMelding melding = getEersteMelding();

		assertThat(melding, is(not(nullValue())));
		assertThat(melding.getLeerlingnummer(), is("3024"));
		assertThat(melding.getSofinummer(), is("210000296"));
		assertThat(melding.getOnderwijsnummer(), is((String) null));
		assertThat(melding.getGeboortedatum(), is(Datum.valueOf("19910524")));
		assertThat(melding.getPostcodeVolgensInstelling(), is("9722TB"));
		assertThat(melding.getGeslacht(), is(Geslacht.Man));
		assertThat(melding.getLand(), is((String) null));

		NT2Vaardigheden vaardigheidmelding = melding.getMeldingen().get(0);
		assertThat(vaardigheidmelding.getRecordType(),
			is(BRONConstants.BVE_AANLEVERING_ED_NT2VAARDIGHEDEN));
		assertThat(vaardigheidmelding.getSoortMutatie(), is(SoortMutatie.Aanpassing));
		assertThat(vaardigheidmelding.getInschrijvingsvolgnummer(), is("1"));
		assertThat(vaardigheidmelding.getVakvolgnummer(), is(1));
		assertThat(vaardigheidmelding.getNT2Vaardigheid(), is(NT2Vaardigheid.Spreken));
		assertThat(vaardigheidmelding.getStartniveau(), is(NT2Niveau.A1BasicUserBreakthrough));
		assertThat(vaardigheidmelding.getBehaaldNiveau(), is(NT2Niveau.B1IndependentUserThreshold));

		assertThat(melding.getMeldingen().size(), is(1));
	}

	private void createGeval43() throws Exception
	{
		getDeelnemer3024();
		deelnemer.setId(3024L);

		Resultaat oudeResultaat = new Resultaat();
		getNT2Schaalwaarde(oudeResultaat, SoortToets.Spreken, "1", "2");

		Resultaat resultaat = new Resultaat();
		Schaalwaarde nieuweSchaalwaarde =
			getNT2Schaalwaarde(resultaat, SoortToets.Spreken, "1", "3");

		resultaat.setOverschrijft(oudeResultaat);

		addChange(resultaat, "waarde", null, nieuweSchaalwaarde);
	}

	@Test
	public void proefgeval44() throws Exception
	{
		createGeval44();
		controller.save();

		BronAanleverMelding melding = getEersteMelding();

		assertThat(melding.getLeerlingnummer(), is("3025"));
		assertThat(melding.getSofinummer(), is("210000302"));
		assertThat(melding.getOnderwijsnummer(), is((String) null));
		assertThat(melding.getGeboortedatum(), is(Datum.valueOf("19910525")));
		assertThat(melding.getPostcodeVolgensInstelling(), is("9722TB"));
		assertThat(melding.getGeslacht(), is(Geslacht.Man));
		assertThat(melding.getLand(), is((String) null));

		VakgegevensRecord vakgegevensmelding = melding.getMeldingen().get(0);
		assertThat(vakgegevensmelding.getRecordType(),
			is(BRONConstants.BVE_AANLEVERING_ED_VAKGEGEVENS));
		assertThat(vakgegevensmelding.getSoortMutatie(), is(SoortMutatie.Verwijdering));
		assertThat(vakgegevensmelding.getInschrijvingsvolgnummer(), is("1"));
		assertThat(vakgegevensmelding.getVakvolgnummer(), is(1));
		// assertThat(vakgegevensmelding.getVak(), is((String) null));
		// assertThat(vakgegevensmelding.getDatumVoltooid(), is(((Date) null)));

		assertThat(melding.getMeldingen().size(), is(1));

	}

	private void createGeval44() throws Exception
	{
		getDeelnemer3025();

		List<OnderwijsproductAfnameContext> vorigeContext =
			new ArrayList<OnderwijsproductAfnameContext>();
		for (OnderwijsproductAfnameContext context : verbintenis.getAfnameContexten())
		{
			vorigeContext.add(context);
		}

		verbintenis.getAfnameContexten().remove(0);

		addChange(verbintenis, "afnameContexten", vorigeContext, verbintenis.getAfnameContexten());
	}

	@Test
	public void proefgeval45() throws Exception
	{
		createGeval45();
		controller.save();

		BronAanleverMelding melding = getEersteMelding();

		assertThat(melding.getLeerlingnummer(), is("3026"));
		assertThat(melding.getSofinummer(), is("210000326"));
		assertThat(melding.getOnderwijsnummer(), is((String) null));
		assertThat(melding.getGeboortedatum(), is(Datum.valueOf("19910526")));
		assertThat(melding.getPostcodeVolgensInstelling(), is("9722TB"));
		assertThat(melding.getGeslacht(), is(Geslacht.Man));
		assertThat(melding.getLand(), is((String) null));

		VakgegevensRecord vakgegevensmelding = melding.getMeldingen().get(0);
		assertThat(vakgegevensmelding.getRecordType(),
			is(BRONConstants.BVE_AANLEVERING_ED_VAKGEGEVENS));
		assertThat(vakgegevensmelding.getSoortMutatie(), is(SoortMutatie.Verwijdering));
		assertThat(vakgegevensmelding.getInschrijvingsvolgnummer(), is("1"));
		assertThat(vakgegevensmelding.getVakvolgnummer(), is(1));
		// assertThat(vakgegevensmelding.getVak(), is((String) null));
		// assertThat(vakgegevensmelding.getDatumVoltooid(), is(((Date) null)));

		assertThat(melding.getMeldingen().size(), is(1));
	}

	private void createGeval45() throws Exception
	{
		getDeelnemer3026();

		List<OnderwijsproductAfnameContext> vorigeContexten =
			new ArrayList<OnderwijsproductAfnameContext>();
		for (OnderwijsproductAfnameContext huidigeContext : verbintenis.getAfnameContexten())
		{
			vorigeContexten.add(huidigeContext);
		}

		verbintenis.getAfnameContexten().remove(0);

		addChange(verbintenis, "afnameContexten", vorigeContexten, verbintenis.getAfnameContexten());
	}

	@Test
	public void proefgeval46() throws Exception
	{
		createGeval46();
		controller.save();

		BronAanleverMelding melding = getEersteMelding();

		assertThat(melding.getLeerlingnummer(), is("3027"));
		assertThat(melding.getSofinummer(), is("210000338"));
		assertThat(melding.getOnderwijsnummer(), is((String) null));
		assertThat(melding.getGeboortedatum(), is(Datum.valueOf("19910527")));
		assertThat(melding.getPostcodeVolgensInstelling(), is("9722TB"));
		assertThat(melding.getGeslacht(), is(Geslacht.Man));
		assertThat(melding.getLand(), is((String) null));

		NT2Vaardigheden vaardigheidmelding = melding.getMeldingen().get(0);
		assertThat(vaardigheidmelding.getRecordType(),
			is(BRONConstants.BVE_AANLEVERING_ED_NT2VAARDIGHEDEN));
		assertThat(vaardigheidmelding.getSoortMutatie(), is(SoortMutatie.Verwijdering));
		assertThat(vaardigheidmelding.getInschrijvingsvolgnummer(), is("1"));
		assertThat(vaardigheidmelding.getVakvolgnummer(), is(1));
		assertThat(vaardigheidmelding.getNT2Vaardigheid(), is(NT2Vaardigheid.Spreken));
		// assertThat(vaardigheidmelding.getStartniveau(), is((NT2Niveau) null));
		// assertThat(vaardigheidmelding.getBehaaldNiveau(), is((NT2Niveau) null));

		assertThat(melding.getMeldingen().size(), is(1));
	}

	private void createGeval46() throws Exception
	{
		getDeelnemer3027();

		Resultaat resultaat = new Resultaat();
		Resultaat oudResultaat = new Resultaat();
		oudResultaat.setCijfer(BigDecimal.ONE);
		resultaat.setOverschrijft(oudResultaat);
		Schaalwaarde schaalwaarde = getNT2Schaalwaarde(resultaat, SoortToets.Spreken, "1", null);

		addChange(resultaat, "waarde", schaalwaarde, null);
	}

	@Test
	public void bveBO1Batch2() throws Exception
	{
		createGeval28();
		createGeval29();
		createGeval30();
		createGeval31();
		createGeval32();
		createGeval33();
		createGeval34();
		createGeval35();
		createGeval36();
		createGeval37();
		createGeval38();
		createGeval39();
		createGeval40();
		createGeval41();
		createGeval42();
		createGeval43();
		createGeval44();
		createGeval45();
		createGeval46();

		batchBVE = maakBronEDBatch();
		batchBVE.setBatchNummer(3);
		batchBVE.getAanleverpunt().setNummer(1);
		batchBVE.getOrganisatie().getBrincode().setCode("04CY");

		assertThat(batchBVE.getBrinNummer(), is("04CY"));
		assertThat(batchBVE.getAanleverPuntNummer(), is(1));
		assertThat(batchBVE.getSectordeel(), is(Sectordeel.Basiseducatie));
		assertThat(batchBVE.getInternOrganisatieNummer(), is((String) null));
		assertThat(batchBVE.getBatchNummer(), is(3));
		assertThat(batchBVE.getBestandSoort(), is(BestandSoort.AANLEVERING));
		assertThat(batchBVE.getVersieProgrammaVanEisen(), is("1.1"));
		assertThat(batchBVE.getAantalMeldingen(), is(19));
		assertThat(batchBVE.getAantalRecords(), is(40));
		assertThat(batchBVE.getVerantwoordelijkeAanlevering(),
			is(VerantwoordelijkeAanleverbestand.Instelling));
		assertThat(batchBVE.getLaatsteAanlevering(), is(false));

		writeBronBatch(batchBVE);
	}
}