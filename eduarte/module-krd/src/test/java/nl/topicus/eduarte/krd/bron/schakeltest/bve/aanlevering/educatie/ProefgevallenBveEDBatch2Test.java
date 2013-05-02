package nl.topicus.eduarte.krd.bron.schakeltest.bve.aanlevering.educatie;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.util.Date;

import nl.topicus.cobra.types.personalia.Geslacht;
import nl.topicus.eduarte.entities.examen.Examendeelname;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis.VerbintenisStatus;
import nl.topicus.eduarte.krd.entities.bron.meldingen.aanlevering.BronAanleverMelding;
import nl.topicus.onderwijs.duo.bron.BRONConstants;
import nl.topicus.onderwijs.duo.bron.bve.batches.pve_9_9.aanlevering.PersoonsgegevensRecord;
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

import org.junit.Test;

public class ProefgevallenBveEDBatch2Test extends ProefgevallenBveED
{
	@Test
	public void proefgeval13() throws Exception
	{
		createGeval13();
		controller.save();

		BronAanleverMelding melding = getEersteMelding();

		assertThat(melding.getLeerlingnummer(), is("3002"));
		assertThat(melding.getSofinummer(), is("210000697"));
		assertThat(melding.getOnderwijsnummer(), is((String) null));
		assertThat(melding.getGeboortedatum(), is(Datum.valueOf("19910716")));
		assertThat(melding.getPostcodeVolgensInstelling(), is("9722TB"));
		assertThat(melding.getGeslacht(), is(Geslacht.Man));
		assertThat(melding.getLand(), is((String) null));

		InschrijvingsgegevensRecord inschrijving = melding.getMeldingen().get(0);
		assertThat(inschrijving.getRecordType(),
			is(BRONConstants.BVE_AANLEVERING_ED_INSCHRIJFGEGEVENS));
		assertThat(inschrijving.getSoortMutatie(), is(SoortMutatie.Toevoeging));
		assertThat(inschrijving.getInschrijvingsvolgnummer(), is("1"));
		assertThat(inschrijving.getGevolgdeOpleiding(), is("5511"));
		assertThat(inschrijving.getDatumInschrijving(), is(asDate(20080101)));
		assertThat(inschrijving.getGeplandeDatumUitschrijving(), is(asDate(20100101)));
		assertThat(inschrijving.getWerkelijkeDatumUitschrijving(), is((Date) null));
		assertThat(inschrijving.getHoogsteVooropleiding(), is(HoogsteVooropleiding.HAVO));
		assertThat(inschrijving.getIndicatieNieuwkomer(), is(false));
		assertThat(inschrijving.getContacturenPerWeek(), is(10));

		VakgegevensRecord vakgegevens = melding.getMeldingen().get(1);
		assertThat(vakgegevens.getRecordType(), is(BRONConstants.BVE_AANLEVERING_ED_VAKGEGEVENS));
		assertThat(vakgegevens.getSoortMutatie(), is(SoortMutatie.Toevoeging));
		assertThat(vakgegevens.getInschrijvingsvolgnummer(), is("1"));
		assertThat(vakgegevens.getVakvolgnummer(), is(1));
		assertThat(vakgegevens.getVak(), is("0310"));

		assertThat(melding.getMeldingen().size(), is(2));
	}

	private void createGeval13() throws Exception
	{
		getDeelnemer3002();
		addChange(verbintenis, "status", VerbintenisStatus.Voorlopig, VerbintenisStatus.Definitief);
	}

	@Test
	public void proefgeval14() throws Exception
	{
		createGeval14();
		controller.save();

		BronAanleverMelding melding = getEersteMelding();

		assertThat(melding.getLeerlingnummer(), is("3003"));
		assertThat(melding.getSofinummer(), is("210000028"));
		assertThat(melding.getOnderwijsnummer(), is((String) null));
		assertThat(melding.getGeboortedatum(), is(Datum.valueOf("19910503")));
		assertThat(melding.getPostcodeVolgensInstelling(), is("9722TB"));
		assertThat(melding.getGeslacht(), is(Geslacht.Man));
		assertThat(melding.getLand(), is((String) null));

		InschrijvingsgegevensRecord inschrijving = melding.getMeldingen().get(0);
		assertThat(inschrijving.getRecordType(),
			is(BRONConstants.BVE_AANLEVERING_ED_INSCHRIJFGEGEVENS));
		assertThat(inschrijving.getSoortMutatie(), is(SoortMutatie.Toevoeging));
		assertThat(inschrijving.getInschrijvingsvolgnummer(), is("1"));
		assertThat(inschrijving.getGevolgdeOpleiding(), is("5511"));
		assertThat(inschrijving.getDatumInschrijving(), is(asDate(20080101)));
		assertThat(inschrijving.getGeplandeDatumUitschrijving(), is(asDate(20100101)));
		assertThat(inschrijving.getWerkelijkeDatumUitschrijving(), is((Date) null));
		assertThat(inschrijving.getHoogsteVooropleiding(), is(HoogsteVooropleiding.HAVO));
		assertThat(inschrijving.getIndicatieNieuwkomer(), is(false));
		assertThat(inschrijving.getContacturenPerWeek(), is(10));

		VakgegevensRecord vakgegevens = melding.getMeldingen().get(1);
		assertThat(vakgegevens.getRecordType(), is(BRONConstants.BVE_AANLEVERING_ED_VAKGEGEVENS));
		assertThat(vakgegevens.getSoortMutatie(), is(SoortMutatie.Toevoeging));
		assertThat(vakgegevens.getInschrijvingsvolgnummer(), is("1"));
		assertThat(vakgegevens.getVakvolgnummer(), is(1));
		assertThat(vakgegevens.getVak(), is("0310"));

		assertThat(melding.getMeldingen().size(), is(2));

	}

	private void createGeval14() throws Exception
	{
		getDeelnemer3003();
		addChange(verbintenis, "status", VerbintenisStatus.Voorlopig, VerbintenisStatus.Definitief);
	}

	@Test
	public void proefgeval15() throws Exception
	{
		createGeval15();
		controller.save();

		BronAanleverMelding melding = getEersteMelding();

		assertThat(melding.getLeerlingnummer(), is("3004"));
		assertThat(melding.getSofinummer(), is((String) null));
		assertThat(melding.getOnderwijsnummer(), is("100320254"));
		assertThat(melding.getGeboortedatum(), is(Datum.valueOf("19910701")));
		assertThat(melding.getPostcodeVolgensInstelling(), is("9722TB"));
		assertThat(melding.getGeslacht(), is(Geslacht.Man));
		assertThat(melding.getLand(), is((String) null));

		InschrijvingsgegevensRecord inschrijving = melding.getMeldingen().get(0);
		assertThat(inschrijving.getRecordType(),
			is(BRONConstants.BVE_AANLEVERING_ED_INSCHRIJFGEGEVENS));
		assertThat(inschrijving.getSoortMutatie(), is(SoortMutatie.Toevoeging));
		assertThat(inschrijving.getInschrijvingsvolgnummer(), is("1"));
		assertThat(inschrijving.getGevolgdeOpleiding(), is("5511"));
		assertThat(inschrijving.getDatumInschrijving(), is(asDate(20080101)));
		assertThat(inschrijving.getGeplandeDatumUitschrijving(), is(asDate(20100101)));
		assertThat(inschrijving.getWerkelijkeDatumUitschrijving(), is((Date) null));
		assertThat(inschrijving.getHoogsteVooropleiding(), is(HoogsteVooropleiding.HAVO));
		assertThat(inschrijving.getIndicatieNieuwkomer(), is(false));
		assertThat(inschrijving.getContacturenPerWeek(), is(10));

		VakgegevensRecord vakgegevens = melding.getMeldingen().get(1);
		assertThat(vakgegevens.getRecordType(), is(BRONConstants.BVE_AANLEVERING_ED_VAKGEGEVENS));
		assertThat(vakgegevens.getSoortMutatie(), is(SoortMutatie.Toevoeging));
		assertThat(vakgegevens.getInschrijvingsvolgnummer(), is("1"));
		assertThat(vakgegevens.getVakvolgnummer(), is(1));
		assertThat(vakgegevens.getVak(), is("0310"));

		assertThat(melding.getMeldingen().size(), is(2));

	}

	private void createGeval15() throws Exception
	{
		getDeelnemer3004();
		addChange(verbintenis, "status", VerbintenisStatus.Voorlopig, VerbintenisStatus.Definitief);
	}

	@Test
	public void proefgeval16() throws Exception
	{
		createGeval16();
		controller.save();

		BronAanleverMelding melding = getEersteMelding();

		assertThat(melding.getLeerlingnummer(), is("3005"));
		assertThat(melding.getSofinummer(), is((String) null));
		assertThat(melding.getOnderwijsnummer(), is((String) null));
		assertThat(melding.getGeboortedatum(), is(Datum.valueOf("19910717")));
		assertThat(melding.getPostcodeVolgensInstelling(), is("9722TB"));
		assertThat(melding.getGeslacht(), is(Geslacht.Man));
		assertThat(melding.getLand(), is((String) null));

		PersoonsgegevensRecord persoonsgegevens = melding.getMeldingen().get(0);
		assertThat(persoonsgegevens.getRecordType(),
			is(BRONConstants.BVE_AANLEVERING_PERSOONSGEGEVENS));
		assertThat(persoonsgegevens.getAchternaam(), is("ACHTERNAAMZEVENENZESTIG"));
		assertThat(persoonsgegevens.getAlleVoornamen(), is("VOORNAAMZEVENENZESTIG"));
		assertThat(persoonsgegevens.getStraatnaam(), is("Kemkensberg"));
		assertThat(persoonsgegevens.getHuisnummer(), is(2));
		assertThat(persoonsgegevens.getPlaatsnaam(), is("Groningen"));

		InschrijvingsgegevensRecord inschrijving = melding.getMeldingen().get(1);
		assertThat(inschrijving.getRecordType(),
			is(BRONConstants.BVE_AANLEVERING_ED_INSCHRIJFGEGEVENS));
		assertThat(inschrijving.getSoortMutatie(), is(SoortMutatie.Toevoeging));
		assertThat(inschrijving.getInschrijvingsvolgnummer(), is("1"));
		assertThat(inschrijving.getGevolgdeOpleiding(), is("5511"));
		assertThat(inschrijving.getDatumInschrijving(), is(asDate(20080101)));
		assertThat(inschrijving.getGeplandeDatumUitschrijving(), is(asDate(20100101)));
		assertThat(inschrijving.getWerkelijkeDatumUitschrijving(), is((Date) null));
		assertThat(inschrijving.getHoogsteVooropleiding(), is(HoogsteVooropleiding.HAVO));
		assertThat(inschrijving.getIndicatieNieuwkomer(), is(false));
		assertThat(inschrijving.getContacturenPerWeek(), is(10));

		VakgegevensRecord vakgegevens = melding.getMeldingen().get(2);
		assertThat(vakgegevens.getRecordType(), is(BRONConstants.BVE_AANLEVERING_ED_VAKGEGEVENS));
		assertThat(vakgegevens.getSoortMutatie(), is(SoortMutatie.Toevoeging));
		assertThat(vakgegevens.getInschrijvingsvolgnummer(), is("1"));
		assertThat(vakgegevens.getVakvolgnummer(), is(1));
		assertThat(vakgegevens.getVak(), is("0310"));

		assertThat(melding.getMeldingen().size(), is(3));

	}

	private void createGeval16() throws Exception
	{
		getDeelnemer3005();
		addChange(verbintenis, "status", VerbintenisStatus.Voorlopig, VerbintenisStatus.Definitief);
	}

	@Test
	public void proefgeval17() throws Exception
	{
		createGeval17();
		controller.save();

		BronAanleverMelding melding = getEersteMelding();

		assertThat(melding.getLeerlingnummer(), is("3006"));
		assertThat(melding.getSofinummer(), is((String) null));
		assertThat(melding.getOnderwijsnummer(), is((String) null));
		assertThat(melding.getGeboortedatum(), is(Datum.valueOf("19910704")));
		assertThat(melding.getPostcodeVolgensInstelling(), is((String) null));
		assertThat(melding.getGeslacht(), is(Geslacht.Man));
		assertThat(melding.getLand(), is("6030"));

		PersoonsgegevensRecord persoonsgegevens = melding.getMeldingen().get(0);
		assertThat(persoonsgegevens.getRecordType(),
			is(BRONConstants.BVE_AANLEVERING_PERSOONSGEGEVENS));

		assertThat(persoonsgegevens.getAchternaam(), is("ACHTERNAAMVIERENVIJFTIG"));
		assertThat(persoonsgegevens.getAlleVoornamen(), is("VOORNAAMVIERENVIJFTIG"));
		assertThat(persoonsgegevens.getStraatnaam(), is(""));
		assertThat(persoonsgegevens.getHuisnummer(), is((Integer) null));
		assertThat(persoonsgegevens.getPlaatsnaam(), is((String) null));

		InschrijvingsgegevensRecord inschrijving = melding.getMeldingen().get(1);
		assertThat(inschrijving.getRecordType(),
			is(BRONConstants.BVE_AANLEVERING_ED_INSCHRIJFGEGEVENS));
		assertThat(inschrijving.getSoortMutatie(), is(SoortMutatie.Toevoeging));
		assertThat(inschrijving.getInschrijvingsvolgnummer(), is("1"));
		assertThat(inschrijving.getGevolgdeOpleiding(), is("5511"));
		assertThat(inschrijving.getDatumInschrijving(), is(asDate(20080101)));
		assertThat(inschrijving.getGeplandeDatumUitschrijving(), is(asDate(20100101)));
		assertThat(inschrijving.getWerkelijkeDatumUitschrijving(), is((Date) null));
		assertThat(inschrijving.getHoogsteVooropleiding(), is(HoogsteVooropleiding.HAVO));
		assertThat(inschrijving.getIndicatieNieuwkomer(), is(false));
		assertThat(inschrijving.getContacturenPerWeek(), is(10));

		VakgegevensRecord vakgegevens = melding.getMeldingen().get(2);
		assertThat(vakgegevens.getRecordType(), is(BRONConstants.BVE_AANLEVERING_ED_VAKGEGEVENS));
		assertThat(vakgegevens.getSoortMutatie(), is(SoortMutatie.Toevoeging));
		assertThat(vakgegevens.getInschrijvingsvolgnummer(), is("1"));
		assertThat(vakgegevens.getVakvolgnummer(), is(1));
		assertThat(vakgegevens.getVak(), is("0310"));

		assertThat(melding.getMeldingen().size(), is(3));

	}

	private void createGeval17() throws Exception
	{
		getDeelnemer3006();
		addChange(verbintenis, "status", VerbintenisStatus.Voorlopig, VerbintenisStatus.Definitief);
	}

	@Test
	public void proefgeval18() throws Exception
	{
		createGeval18();
		controller.save();

		BronAanleverMelding melding = getEersteMelding();

		assertThat(melding.getLeerlingnummer(), is("3007"));
		assertThat(melding.getSofinummer(), is("210000089"));
		assertThat(melding.getOnderwijsnummer(), is((String) null));
		assertThat(melding.getGeboortedatum(), is(Datum.valueOf("19910507")));
		assertThat(melding.getPostcodeVolgensInstelling(), is("9722TB"));
		assertThat(melding.getGeslacht(), is(Geslacht.Man));
		assertThat(melding.getLand(), is((String) null));

		InschrijvingsgegevensRecord inschrijving = melding.getMeldingen().get(0);
		assertThat(inschrijving.getRecordType(),
			is(BRONConstants.BVE_AANLEVERING_ED_INSCHRIJFGEGEVENS));
		assertThat(inschrijving.getSoortMutatie(), is(SoortMutatie.Toevoeging));
		assertThat(inschrijving.getInschrijvingsvolgnummer(), is("1"));
		assertThat(inschrijving.getGevolgdeOpleiding(), is("5511"));
		assertThat(inschrijving.getDatumInschrijving(), is(asDate(20080101)));
		assertThat(inschrijving.getGeplandeDatumUitschrijving(), is(asDate(20100101)));
		assertThat(inschrijving.getWerkelijkeDatumUitschrijving(), is((Date) null));
		assertThat(inschrijving.getHoogsteVooropleiding(), is(HoogsteVooropleiding.HAVO));
		assertThat(inschrijving.getIndicatieNieuwkomer(), is(false));
		assertThat(inschrijving.getContacturenPerWeek(), is(10));

		VakgegevensRecord vakgegevens = melding.getMeldingen().get(1);
		assertThat(vakgegevens.getRecordType(), is(BRONConstants.BVE_AANLEVERING_ED_VAKGEGEVENS));
		assertThat(vakgegevens.getSoortMutatie(), is(SoortMutatie.Toevoeging));
		assertThat(vakgegevens.getInschrijvingsvolgnummer(), is("1"));
		assertThat(vakgegevens.getVakvolgnummer(), is(1));
		assertThat(vakgegevens.getVak(), is("0011"));

		vakgegevens = melding.getMeldingen().get(2);
		assertThat(vakgegevens.getRecordType(), is(BRONConstants.BVE_AANLEVERING_ED_VAKGEGEVENS));
		assertThat(vakgegevens.getSoortMutatie(), is(SoortMutatie.Toevoeging));
		assertThat(vakgegevens.getInschrijvingsvolgnummer(), is("1"));
		assertThat(vakgegevens.getVakvolgnummer(), is(2));
		assertThat(vakgegevens.getVak(), is("0310"));

		assertThat(melding.getMeldingen().size(), is(3));

	}

	private void createGeval18() throws Exception
	{
		getDeelnemer3007();
		addChange(verbintenis, "status", VerbintenisStatus.Voorlopig, VerbintenisStatus.Definitief);
	}

	@Test
	public void proefgeval19() throws Exception
	{
		createGeval19();
		controller.save();

		BronAanleverMelding melding = getEersteMelding();

		assertThat(melding.getLeerlingnummer(), is("3008"));
		assertThat(melding.getSofinummer(), is("210000090"));
		assertThat(melding.getOnderwijsnummer(), is((String) null));
		assertThat(melding.getGeboortedatum(), is(Datum.valueOf("19910508")));
		assertThat(melding.getPostcodeVolgensInstelling(), is("9722TB"));
		assertThat(melding.getGeslacht(), is(Geslacht.Man));
		assertThat(melding.getLand(), is((String) null));

		InschrijvingsgegevensRecord inschrijving = melding.getMeldingen().get(0);
		assertThat(inschrijving.getRecordType(),
			is(BRONConstants.BVE_AANLEVERING_ED_INSCHRIJFGEGEVENS));
		assertThat(inschrijving.getSoortMutatie(), is(SoortMutatie.Toevoeging));
		assertThat(inschrijving.getInschrijvingsvolgnummer(), is("1"));
		assertThat(inschrijving.getGevolgdeOpleiding(), is("5511"));
		assertThat(inschrijving.getDatumInschrijving(), is(asDate(20080101)));
		assertThat(inschrijving.getGeplandeDatumUitschrijving(), is(asDate(20100101)));
		assertThat(inschrijving.getWerkelijkeDatumUitschrijving(), is((Date) null));
		assertThat(inschrijving.getHoogsteVooropleiding(), is(HoogsteVooropleiding.HAVO));
		assertThat(inschrijving.getIndicatieNieuwkomer(), is(false));
		assertThat(inschrijving.getContacturenPerWeek(), is(10));

		VakgegevensRecord vakgegevens = melding.getMeldingen().get(1);
		assertThat(vakgegevens.getRecordType(), is(BRONConstants.BVE_AANLEVERING_ED_VAKGEGEVENS));
		assertThat(vakgegevens.getSoortMutatie(), is(SoortMutatie.Toevoeging));
		assertThat(vakgegevens.getInschrijvingsvolgnummer(), is("1"));
		assertThat(vakgegevens.getVakvolgnummer(), is(1));
		assertThat(vakgegevens.getVak(), is("0310"));

		vakgegevens = melding.getMeldingen().get(2);
		assertThat(vakgegevens.getRecordType(), is(BRONConstants.BVE_AANLEVERING_ED_VAKGEGEVENS));
		assertThat(vakgegevens.getSoortMutatie(), is(SoortMutatie.Toevoeging));
		assertThat(vakgegevens.getInschrijvingsvolgnummer(), is("1"));
		assertThat(vakgegevens.getVakvolgnummer(), is(2));
		assertThat(vakgegevens.getVak(), is("0990"));

		NT2Vaardigheden nt2vaardigheid = melding.getMeldingen().get(3);
		assertThat(nt2vaardigheid.getRecordType(),
			is(BRONConstants.BVE_AANLEVERING_ED_NT2VAARDIGHEDEN));
		assertThat(nt2vaardigheid.getSoortMutatie(), is(SoortMutatie.Toevoeging));
		assertThat(nt2vaardigheid.getInschrijvingsvolgnummer(), is("1"));
		assertThat(nt2vaardigheid.getVakvolgnummer(), is(2));
		assertThat(nt2vaardigheid.getNT2Vaardigheid(), is(NT2Vaardigheid.Luisteren));
		assertThat(nt2vaardigheid.getStartniveau(), is(NT2Niveau.A1BasicUserBreakthrough));
		assertThat(nt2vaardigheid.getBehaaldNiveau(), is((NT2Niveau) null));

		assertThat(melding.getMeldingen().size(), is(4));

	}

	private void createGeval19() throws Exception
	{
		getDeelnemer3008();
		addChange(verbintenis, "status", VerbintenisStatus.Voorlopig, VerbintenisStatus.Definitief);
	}

	@Test
	public void proefgeval20() throws Exception
	{
		createGeval20();
		controller.save();

		BronAanleverMelding melding = getEersteMelding();

		assertThat(melding.getLeerlingnummer(), is("3015"));
		assertThat(melding.getSofinummer(), is("210000168"));
		assertThat(melding.getOnderwijsnummer(), is((String) null));
		assertThat(melding.getGeboortedatum(), is(Datum.valueOf("19910515")));
		assertThat(melding.getPostcodeVolgensInstelling(), is("9722TB"));
		assertThat(melding.getGeslacht(), is(Geslacht.Man));
		assertThat(melding.getLand(), is((String) null));

		InschrijvingsgegevensRecord inschrijving = melding.getMeldingen().get(0);
		assertThat(inschrijving.getRecordType(),
			is(BRONConstants.BVE_AANLEVERING_ED_INSCHRIJFGEGEVENS));
		assertThat(inschrijving.getSoortMutatie(), is(SoortMutatie.Toevoeging));
		assertThat(inschrijving.getInschrijvingsvolgnummer(), is("1"));
		assertThat(inschrijving.getGevolgdeOpleiding(), is("5511"));
		assertThat(inschrijving.getDatumInschrijving(), is(asDate(20080101)));
		assertThat(inschrijving.getGeplandeDatumUitschrijving(), is(asDate(20100101)));
		assertThat(inschrijving.getWerkelijkeDatumUitschrijving(), is((Date) null));
		assertThat(inschrijving.getHoogsteVooropleiding(), is(HoogsteVooropleiding.HAVO));
		assertThat(inschrijving.getIndicatieNieuwkomer(), is(false));
		assertThat(inschrijving.getContacturenPerWeek(), is(10));

		ResultaatgegevensRecord resultaatgegevens = melding.getMeldingen().get(1);
		assertThat(resultaatgegevens.getRecordType(),
			is(BRONConstants.BVE_AANLEVERING_ED_RESULTAATGEGEVENS));
		assertThat(resultaatgegevens.getSoortMutatie(), is(SoortMutatie.Toevoeging));
		assertThat(resultaatgegevens.getInschrijvingsvolgnummer(), is("1"));
		assertThat(resultaatgegevens.getVoltooideOpleiding(), is("5511"));
		assertThat(resultaatgegevens.getDatumVoltooid(), is(asDate(20081001)));

		VakgegevensRecord vakgegevens = melding.getMeldingen().get(2);
		assertThat(vakgegevens.getRecordType(), is(BRONConstants.BVE_AANLEVERING_ED_VAKGEGEVENS));
		assertThat(vakgegevens.getSoortMutatie(), is(SoortMutatie.Toevoeging));
		assertThat(vakgegevens.getInschrijvingsvolgnummer(), is("1"));
		assertThat(vakgegevens.getVakvolgnummer(), is(1));
		assertThat(vakgegevens.getVak(), is("0310"));

		assertThat(melding.getMeldingen().size(), is(3));

	}

	private void createGeval20() throws Exception
	{
		getDeelnemer3015();

		Examendeelname deelname = addExamendeelname(verbintenis, 20081001);
		deelname.setExamenstatus(getGeslaagd());

		addChange(deelname, "examenstatus", null, deelname.getExamenstatus());
		addChange(verbintenis, "status", VerbintenisStatus.Voorlopig, VerbintenisStatus.Definitief);
	}

	@Test
	public void proefgeval21() throws Exception
	{
		createGeval21();
		controller.save();

		BronAanleverMelding melding = getEersteMelding();

		assertThat(melding.getLeerlingnummer(), is("3016"));
		assertThat(melding.getSofinummer(), is("210000181"));
		assertThat(melding.getOnderwijsnummer(), is((String) null));
		assertThat(melding.getGeboortedatum(), is(Datum.valueOf("19910516")));
		assertThat(melding.getPostcodeVolgensInstelling(), is("9722TB"));
		assertThat(melding.getGeslacht(), is(Geslacht.Man));
		assertThat(melding.getLand(), is((String) null));

		InschrijvingsgegevensRecord inschrijving = melding.getMeldingen().get(0);
		assertThat(inschrijving.getRecordType(),
			is(BRONConstants.BVE_AANLEVERING_ED_INSCHRIJFGEGEVENS));
		assertThat(inschrijving.getSoortMutatie(), is(SoortMutatie.Toevoeging));
		assertThat(inschrijving.getInschrijvingsvolgnummer(), is("1"));
		assertThat(inschrijving.getGevolgdeOpleiding(), is("5501"));
		assertThat(inschrijving.getDatumInschrijving(), is(asDate(20080101)));
		assertThat(inschrijving.getGeplandeDatumUitschrijving(), is(asDate(20100101)));
		assertThat(inschrijving.getWerkelijkeDatumUitschrijving(), is((Date) null));
		assertThat(inschrijving.getHoogsteVooropleiding(), is(HoogsteVooropleiding.HAVO));
		assertThat(inschrijving.getIndicatieNieuwkomer(), is(false));
		assertThat(inschrijving.getContacturenPerWeek(), is(10));

		ResultaatgegevensRecord resultaatgegevens = melding.getMeldingen().get(1);
		assertThat(resultaatgegevens.getRecordType(),
			is(BRONConstants.BVE_AANLEVERING_ED_RESULTAATGEGEVENS));
		assertThat(resultaatgegevens.getSoortMutatie(), is(SoortMutatie.Toevoeging));
		assertThat(resultaatgegevens.getInschrijvingsvolgnummer(), is("1"));
		assertThat(resultaatgegevens.getVoltooideOpleiding(), is("5501"));
		assertThat(resultaatgegevens.getDatumVoltooid(), is(asDate(20081001)));

		VakgegevensRecord vakgegevens = melding.getMeldingen().get(2);
		assertThat(vakgegevens.getRecordType(), is(BRONConstants.BVE_AANLEVERING_ED_VAKGEGEVENS));
		assertThat(vakgegevens.getSoortMutatie(), is(SoortMutatie.Toevoeging));
		assertThat(vakgegevens.getInschrijvingsvolgnummer(), is("1"));
		assertThat(vakgegevens.getVakvolgnummer(), is(1));
		assertThat(vakgegevens.getVak(), is("0310"));

		assertThat(melding.getMeldingen().size(), is(3));

	}

	private void createGeval21() throws Exception
	{
		getDeelnemer3016();

		Examendeelname deelname = addExamendeelname(verbintenis, 20081001);
		deelname.setExamenstatus(getGeslaagd());

		addChange(deelname, "examenstatus", null, deelname.getExamenstatus());

		addChange(verbintenis, "status", VerbintenisStatus.Voorlopig, VerbintenisStatus.Definitief);
	}

	@Test
	public void proefgeval22() throws Exception
	{
		createGeval22();
		controller.save();

		BronAanleverMelding melding = getEersteMelding();

		assertThat(melding.getLeerlingnummer(), is("3019"));
		assertThat(melding.getSofinummer(), is("210000223"));
		assertThat(melding.getOnderwijsnummer(), is((String) null));
		assertThat(melding.getGeboortedatum(), is(Datum.valueOf("19910519")));
		assertThat(melding.getPostcodeVolgensInstelling(), is("9722TB"));
		assertThat(melding.getGeslacht(), is(Geslacht.Man));
		assertThat(melding.getLand(), is((String) null));

		InschrijvingsgegevensRecord inschrijving = melding.getMeldingen().get(0);
		assertThat(inschrijving.getRecordType(),
			is(BRONConstants.BVE_AANLEVERING_ED_INSCHRIJFGEGEVENS));
		assertThat(inschrijving.getSoortMutatie(), is(SoortMutatie.Toevoeging));
		assertThat(inschrijving.getInschrijvingsvolgnummer(), is("1"));
		assertThat(inschrijving.getGevolgdeOpleiding(), is("5511"));
		assertThat(inschrijving.getDatumInschrijving(), is(asDate(20080101)));
		assertThat(inschrijving.getGeplandeDatumUitschrijving(), is(asDate(20100101)));
		assertThat(inschrijving.getWerkelijkeDatumUitschrijving(), is((Date) null));
		assertThat(inschrijving.getHoogsteVooropleiding(), is(HoogsteVooropleiding.HAVO));
		assertThat(inschrijving.getIndicatieNieuwkomer(), is(false));
		assertThat(inschrijving.getContacturenPerWeek(), is(10));

		VakgegevensRecord vakgegevens = melding.getMeldingen().get(1);
		assertThat(vakgegevens.getRecordType(), is(BRONConstants.BVE_AANLEVERING_ED_VAKGEGEVENS));
		assertThat(vakgegevens.getSoortMutatie(), is(SoortMutatie.Toevoeging));
		assertThat(vakgegevens.getInschrijvingsvolgnummer(), is("1"));
		assertThat(vakgegevens.getVakvolgnummer(), is(1));
		assertThat(vakgegevens.getVak(), is("0990"));

		NT2Vaardigheden nt2vaardigheid = melding.getMeldingen().get(2);
		assertThat(nt2vaardigheid.getRecordType(),
			is(BRONConstants.BVE_AANLEVERING_ED_NT2VAARDIGHEDEN));
		assertThat(nt2vaardigheid.getSoortMutatie(), is(SoortMutatie.Toevoeging));
		assertThat(nt2vaardigheid.getInschrijvingsvolgnummer(), is("1"));
		assertThat(nt2vaardigheid.getVakvolgnummer(), is(1));
		assertThat(nt2vaardigheid.getNT2Vaardigheid(), is(NT2Vaardigheid.Spreken));
		assertThat(nt2vaardigheid.getStartniveau(), is(NT2Niveau.A1BasicUserBreakthrough));
		assertThat(nt2vaardigheid.getBehaaldNiveau(), is((NT2Niveau) null));

		assertThat(melding.getMeldingen().size(), is(3));

	}

	private void createGeval22() throws Exception
	{
		getDeelnemer3019();
		addChange(verbintenis, "status", VerbintenisStatus.Voorlopig, VerbintenisStatus.Definitief);
	}

	@Test
	public void proefgeval23() throws Exception
	{
		createGeval23();
		controller.save();

		BronAanleverMelding melding = getEersteMelding();

		assertThat(melding.getLeerlingnummer(), is("3022"));
		assertThat(melding.getSofinummer(), is("210000260"));
		assertThat(melding.getOnderwijsnummer(), is((String) null));
		assertThat(melding.getGeboortedatum(), is(Datum.valueOf("19910522")));
		assertThat(melding.getPostcodeVolgensInstelling(), is("9722TB"));
		assertThat(melding.getGeslacht(), is(Geslacht.Man));
		assertThat(melding.getLand(), is((String) null));

		InschrijvingsgegevensRecord inschrijving = melding.getMeldingen().get(0);
		assertThat(inschrijving.getRecordType(),
			is(BRONConstants.BVE_AANLEVERING_ED_INSCHRIJFGEGEVENS));
		assertThat(inschrijving.getSoortMutatie(), is(SoortMutatie.Toevoeging));
		assertThat(inschrijving.getInschrijvingsvolgnummer(), is("1"));
		assertThat(inschrijving.getGevolgdeOpleiding(), is("5511"));
		assertThat(inschrijving.getDatumInschrijving(), is(asDate(20080101)));
		assertThat(inschrijving.getGeplandeDatumUitschrijving(), is(asDate(20100101)));
		assertThat(inschrijving.getWerkelijkeDatumUitschrijving(), is((Date) null));
		assertThat(inschrijving.getHoogsteVooropleiding(), is(HoogsteVooropleiding.HAVO));
		assertThat(inschrijving.getIndicatieNieuwkomer(), is(false));
		assertThat(inschrijving.getContacturenPerWeek(), is(10));

		VakgegevensRecord vakgegevens = melding.getMeldingen().get(1);
		assertThat(vakgegevens.getRecordType(), is(BRONConstants.BVE_AANLEVERING_ED_VAKGEGEVENS));
		assertThat(vakgegevens.getSoortMutatie(), is(SoortMutatie.Toevoeging));
		assertThat(vakgegevens.getInschrijvingsvolgnummer(), is("1"));
		assertThat(vakgegevens.getVakvolgnummer(), is(1));
		assertThat(vakgegevens.getVak(), is("0990"));

		NT2Vaardigheden nt2vaardigheid = melding.getMeldingen().get(2);
		assertThat(nt2vaardigheid.getRecordType(),
			is(BRONConstants.BVE_AANLEVERING_ED_NT2VAARDIGHEDEN));
		assertThat(nt2vaardigheid.getSoortMutatie(), is(SoortMutatie.Toevoeging));
		assertThat(nt2vaardigheid.getInschrijvingsvolgnummer(), is("1"));
		assertThat(nt2vaardigheid.getVakvolgnummer(), is(1));
		assertThat(nt2vaardigheid.getNT2Vaardigheid(), is(NT2Vaardigheid.Spreken));
		assertThat(nt2vaardigheid.getStartniveau(), is(NT2Niveau.A1BasicUserBreakthrough));
		assertThat(nt2vaardigheid.getBehaaldNiveau(), is((NT2Niveau) null));

		assertThat(melding.getMeldingen().size(), is(3));

	}

	private void createGeval23() throws Exception
	{
		getDeelnemer3022();
		addChange(verbintenis, "status", VerbintenisStatus.Voorlopig, VerbintenisStatus.Definitief);
	}

	@Test
	public void proefgeval24() throws Exception
	{
		createGeval24();
		controller.save();

		BronAanleverMelding melding = getEersteMelding();

		assertThat(melding.getLeerlingnummer(), is("3023"));
		assertThat(melding.getSofinummer(), is("210000272"));
		assertThat(melding.getOnderwijsnummer(), is((String) null));
		assertThat(melding.getGeboortedatum(), is(Datum.valueOf("19910523")));
		assertThat(melding.getPostcodeVolgensInstelling(), is("9722TB"));
		assertThat(melding.getGeslacht(), is(Geslacht.Man));
		assertThat(melding.getLand(), is((String) null));

		InschrijvingsgegevensRecord inschrijving = melding.getMeldingen().get(0);
		assertThat(inschrijving.getRecordType(),
			is(BRONConstants.BVE_AANLEVERING_ED_INSCHRIJFGEGEVENS));
		assertThat(inschrijving.getSoortMutatie(), is(SoortMutatie.Toevoeging));
		assertThat(inschrijving.getInschrijvingsvolgnummer(), is("1"));
		assertThat(inschrijving.getGevolgdeOpleiding(), is("5511"));
		assertThat(inschrijving.getDatumInschrijving(), is(asDate(20080101)));
		assertThat(inschrijving.getGeplandeDatumUitschrijving(), is(asDate(20100101)));
		assertThat(inschrijving.getWerkelijkeDatumUitschrijving(), is((Date) null));
		assertThat(inschrijving.getHoogsteVooropleiding(), is(HoogsteVooropleiding.HAVO));
		assertThat(inschrijving.getIndicatieNieuwkomer(), is(false));
		assertThat(inschrijving.getContacturenPerWeek(), is(10));

		VakgegevensRecord vakgegevens = melding.getMeldingen().get(1);
		assertThat(vakgegevens.getRecordType(), is(BRONConstants.BVE_AANLEVERING_ED_VAKGEGEVENS));
		assertThat(vakgegevens.getSoortMutatie(), is(SoortMutatie.Toevoeging));
		assertThat(vakgegevens.getInschrijvingsvolgnummer(), is("1"));
		assertThat(vakgegevens.getVakvolgnummer(), is(1));
		assertThat(vakgegevens.getVak(), is("0990"));

		NT2Vaardigheden nt2vaardigheid = melding.getMeldingen().get(2);
		assertThat(nt2vaardigheid.getRecordType(),
			is(BRONConstants.BVE_AANLEVERING_ED_NT2VAARDIGHEDEN));
		assertThat(nt2vaardigheid.getSoortMutatie(), is(SoortMutatie.Toevoeging));
		assertThat(nt2vaardigheid.getInschrijvingsvolgnummer(), is("1"));
		assertThat(nt2vaardigheid.getVakvolgnummer(), is(1));
		assertThat(nt2vaardigheid.getNT2Vaardigheid(), is(NT2Vaardigheid.Spreken));
		assertThat(nt2vaardigheid.getStartniveau(), is(NT2Niveau.A1BasicUserBreakthrough));
		assertThat(nt2vaardigheid.getBehaaldNiveau(), is((NT2Niveau) null));

		assertThat(melding.getMeldingen().size(), is(3));

	}

	private void createGeval24() throws Exception
	{
		getDeelnemer3023();
		addChange(verbintenis, "status", VerbintenisStatus.Voorlopig, VerbintenisStatus.Definitief);
	}

	@Test
	public void proefgeval25() throws Exception
	{
		createGeval25();
		controller.save();

		BronAanleverMelding melding = getEersteMelding();

		assertThat(melding.getLeerlingnummer(), is("3024"));
		assertThat(melding.getSofinummer(), is("210000296"));
		assertThat(melding.getOnderwijsnummer(), is((String) null));
		assertThat(melding.getGeboortedatum(), is(Datum.valueOf("19910524")));
		assertThat(melding.getPostcodeVolgensInstelling(), is("9722TB"));
		assertThat(melding.getGeslacht(), is(Geslacht.Man));
		assertThat(melding.getLand(), is((String) null));

		InschrijvingsgegevensRecord inschrijving = melding.getMeldingen().get(0);
		assertThat(inschrijving.getRecordType(),
			is(BRONConstants.BVE_AANLEVERING_ED_INSCHRIJFGEGEVENS));
		assertThat(inschrijving.getSoortMutatie(), is(SoortMutatie.Toevoeging));
		assertThat(inschrijving.getInschrijvingsvolgnummer(), is("1"));
		assertThat(inschrijving.getGevolgdeOpleiding(), is("5511"));
		assertThat(inschrijving.getDatumInschrijving(), is(asDate(20080101)));
		assertThat(inschrijving.getGeplandeDatumUitschrijving(), is(asDate(20100101)));
		assertThat(inschrijving.getWerkelijkeDatumUitschrijving(), is((Date) null));
		assertThat(inschrijving.getHoogsteVooropleiding(), is(HoogsteVooropleiding.HAVO));
		assertThat(inschrijving.getIndicatieNieuwkomer(), is(false));
		assertThat(inschrijving.getContacturenPerWeek(), is(10));

		VakgegevensRecord vakgegevens = melding.getMeldingen().get(1);
		assertThat(vakgegevens.getRecordType(), is(BRONConstants.BVE_AANLEVERING_ED_VAKGEGEVENS));
		assertThat(vakgegevens.getSoortMutatie(), is(SoortMutatie.Toevoeging));
		assertThat(vakgegevens.getInschrijvingsvolgnummer(), is("1"));
		assertThat(vakgegevens.getVakvolgnummer(), is(1));
		assertThat(vakgegevens.getVak(), is("0990"));

		NT2Vaardigheden nt2vaardigheid = melding.getMeldingen().get(2);
		assertThat(nt2vaardigheid.getRecordType(),
			is(BRONConstants.BVE_AANLEVERING_ED_NT2VAARDIGHEDEN));
		assertThat(nt2vaardigheid.getSoortMutatie(), is(SoortMutatie.Toevoeging));
		assertThat(nt2vaardigheid.getInschrijvingsvolgnummer(), is("1"));
		assertThat(nt2vaardigheid.getVakvolgnummer(), is(1));
		assertThat(nt2vaardigheid.getNT2Vaardigheid(), is(NT2Vaardigheid.Spreken));
		assertThat(nt2vaardigheid.getStartniveau(), is(NT2Niveau.A1BasicUserBreakthrough));
		assertThat(nt2vaardigheid.getBehaaldNiveau(), is(NT2Niveau.A2BasicUserWaystage.parse("2")));

		assertThat(melding.getMeldingen().size(), is(3));

	}

	private void createGeval25() throws Exception
	{
		getDeelnemer3024();
		addChange(verbintenis, "status", VerbintenisStatus.Voorlopig, VerbintenisStatus.Definitief);
	}

	@Test
	public void proefgeval26() throws Exception
	{
		createGeval26();
		controller.save();

		BronAanleverMelding melding = getEersteMelding();

		assertThat(melding.getLeerlingnummer(), is("3026"));
		assertThat(melding.getSofinummer(), is("210000326"));
		assertThat(melding.getOnderwijsnummer(), is((String) null));
		assertThat(melding.getGeboortedatum(), is(Datum.valueOf("19910526")));
		assertThat(melding.getPostcodeVolgensInstelling(), is("9722TB"));
		assertThat(melding.getGeslacht(), is(Geslacht.Man));
		assertThat(melding.getLand(), is((String) null));

		InschrijvingsgegevensRecord inschrijving = melding.getMeldingen().get(0);
		assertThat(inschrijving.getRecordType(),
			is(BRONConstants.BVE_AANLEVERING_ED_INSCHRIJFGEGEVENS));
		assertThat(inschrijving.getSoortMutatie(), is(SoortMutatie.Toevoeging));
		assertThat(inschrijving.getInschrijvingsvolgnummer(), is("1"));
		assertThat(inschrijving.getGevolgdeOpleiding(), is("5511"));
		assertThat(inschrijving.getDatumInschrijving(), is(asDate(20080101)));
		assertThat(inschrijving.getGeplandeDatumUitschrijving(), is(asDate(20100101)));
		assertThat(inschrijving.getWerkelijkeDatumUitschrijving(), is((Date) null));
		assertThat(inschrijving.getHoogsteVooropleiding(), is(HoogsteVooropleiding.HAVO));
		assertThat(inschrijving.getIndicatieNieuwkomer(), is(false));
		assertThat(inschrijving.getContacturenPerWeek(), is(10));

		VakgegevensRecord vakgegevens = melding.getMeldingen().get(1);
		assertThat(vakgegevens.getRecordType(), is(BRONConstants.BVE_AANLEVERING_ED_VAKGEGEVENS));
		assertThat(vakgegevens.getSoortMutatie(), is(SoortMutatie.Toevoeging));
		assertThat(vakgegevens.getInschrijvingsvolgnummer(), is("1"));
		assertThat(vakgegevens.getVakvolgnummer(), is(1));
		assertThat(vakgegevens.getVak(), is("0990"));

		NT2Vaardigheden nt2vaardigheid = melding.getMeldingen().get(2);
		assertThat(nt2vaardigheid.getRecordType(),
			is(BRONConstants.BVE_AANLEVERING_ED_NT2VAARDIGHEDEN));
		assertThat(nt2vaardigheid.getSoortMutatie(), is(SoortMutatie.Toevoeging));
		assertThat(nt2vaardigheid.getInschrijvingsvolgnummer(), is("1"));
		assertThat(nt2vaardigheid.getVakvolgnummer(), is(1));
		assertThat(nt2vaardigheid.getNT2Vaardigheid(), is(NT2Vaardigheid.Spreken));
		assertThat(nt2vaardigheid.getStartniveau(), is(NT2Niveau.A1BasicUserBreakthrough));
		assertThat(nt2vaardigheid.getBehaaldNiveau(), is((NT2Niveau) null));

		vakgegevens = melding.getMeldingen().get(3);
		assertThat(vakgegevens.getRecordType(), is(BRONConstants.BVE_AANLEVERING_ED_VAKGEGEVENS));
		assertThat(vakgegevens.getSoortMutatie(), is(SoortMutatie.Toevoeging));
		assertThat(vakgegevens.getInschrijvingsvolgnummer(), is("1"));
		assertThat(vakgegevens.getVakvolgnummer(), is(2));
		assertThat(vakgegevens.getVak(), is("0011"));

		assertThat(melding.getMeldingen().size(), is(4));

	}

	private void createGeval26() throws Exception
	{
		getDeelnemer3026();
		addChange(verbintenis, "status", VerbintenisStatus.Voorlopig, VerbintenisStatus.Definitief);
	}

	@Test
	public void proefgeval27() throws Exception
	{
		createGeval27();
		controller.save();

		BronAanleverMelding melding = getEersteMelding();

		assertThat(melding.getLeerlingnummer(), is("3027"));
		assertThat(melding.getSofinummer(), is("210000338"));
		assertThat(melding.getOnderwijsnummer(), is((String) null));
		assertThat(melding.getGeboortedatum(), is(Datum.valueOf("19910527")));
		assertThat(melding.getPostcodeVolgensInstelling(), is("9722TB"));
		assertThat(melding.getGeslacht(), is(Geslacht.Man));
		assertThat(melding.getLand(), is((String) null));

		InschrijvingsgegevensRecord inschrijving = melding.getMeldingen().get(0);
		assertThat(inschrijving.getRecordType(),
			is(BRONConstants.BVE_AANLEVERING_ED_INSCHRIJFGEGEVENS));
		assertThat(inschrijving.getSoortMutatie(), is(SoortMutatie.Toevoeging));
		assertThat(inschrijving.getInschrijvingsvolgnummer(), is("1"));
		assertThat(inschrijving.getGevolgdeOpleiding(), is("5511"));
		assertThat(inschrijving.getDatumInschrijving(), is(asDate(20080101)));
		assertThat(inschrijving.getGeplandeDatumUitschrijving(), is(asDate(20100101)));
		assertThat(inschrijving.getWerkelijkeDatumUitschrijving(), is((Date) null));
		assertThat(inschrijving.getHoogsteVooropleiding(), is(HoogsteVooropleiding.HAVO));
		assertThat(inschrijving.getIndicatieNieuwkomer(), is(false));
		assertThat(inschrijving.getContacturenPerWeek(), is(10));

		VakgegevensRecord vakgegevens = melding.getMeldingen().get(1);
		assertThat(vakgegevens.getRecordType(), is(BRONConstants.BVE_AANLEVERING_ED_VAKGEGEVENS));
		assertThat(vakgegevens.getSoortMutatie(), is(SoortMutatie.Toevoeging));
		assertThat(vakgegevens.getInschrijvingsvolgnummer(), is("1"));
		assertThat(vakgegevens.getVakvolgnummer(), is(1));
		assertThat(vakgegevens.getVak(), is("0990"));

		NT2Vaardigheden nt2vaardigheid = melding.getMeldingen().get(2);
		assertThat(nt2vaardigheid.getRecordType(),
			is(BRONConstants.BVE_AANLEVERING_ED_NT2VAARDIGHEDEN));
		assertThat(nt2vaardigheid.getSoortMutatie(), is(SoortMutatie.Toevoeging));
		assertThat(nt2vaardigheid.getInschrijvingsvolgnummer(), is("1"));
		assertThat(nt2vaardigheid.getVakvolgnummer(), is(1));
		assertThat(nt2vaardigheid.getNT2Vaardigheid(), is(NT2Vaardigheid.Spreken));
		assertThat(nt2vaardigheid.getStartniveau(), is(NT2Niveau.A1BasicUserBreakthrough));
		assertThat(nt2vaardigheid.getBehaaldNiveau(), is((NT2Niveau) null));

		nt2vaardigheid = melding.getMeldingen().get(3);
		assertThat(nt2vaardigheid.getRecordType(),
			is(BRONConstants.BVE_AANLEVERING_ED_NT2VAARDIGHEDEN));
		assertThat(nt2vaardigheid.getSoortMutatie(), is(SoortMutatie.Toevoeging));
		assertThat(nt2vaardigheid.getInschrijvingsvolgnummer(), is("1"));
		assertThat(nt2vaardigheid.getVakvolgnummer(), is(1));
		assertThat(nt2vaardigheid.getNT2Vaardigheid(), is(NT2Vaardigheid.Luisteren));
		assertThat(nt2vaardigheid.getStartniveau(), is(NT2Niveau.A1BasicUserBreakthrough));
		assertThat(nt2vaardigheid.getBehaaldNiveau(), is((NT2Niveau) null));

		assertThat(melding.getMeldingen().size(), is(4));

	}

	private void createGeval27() throws Exception
	{
		getDeelnemer3027();
		addChange(verbintenis, "status", VerbintenisStatus.Voorlopig, VerbintenisStatus.Definitief);
	}

	@Test
	public void bveBO1Batch2() throws Exception
	{
		createGeval13();
		createGeval14();
		createGeval15();
		createGeval16();
		createGeval17();
		createGeval18();
		createGeval19();
		createGeval20();
		createGeval21();
		createGeval22();
		createGeval23();
		createGeval24();
		createGeval25();
		createGeval26();
		createGeval27();

		batchBVE = maakBronEDBatch();
		batchBVE.setBatchNummer(2);
		batchBVE.getAanleverpunt().setNummer(1);
		batchBVE.getOrganisatie().getBrincode().setCode("04CY");

		assertThat(batchBVE.getBrinNummer(), is("04CY"));
		assertThat(batchBVE.getAanleverPuntNummer(), is(1));
		assertThat(batchBVE.getSectordeel(), is(Sectordeel.Basiseducatie));
		assertThat(batchBVE.getInternOrganisatieNummer(), is((String) null));
		assertThat(batchBVE.getBatchNummer(), is(2));
		assertThat(batchBVE.getBestandSoort(), is(BestandSoort.AANLEVERING));
		assertThat(batchBVE.getVersieProgrammaVanEisen(), is("1.1"));
		assertThat(batchBVE.getAantalMeldingen(), is(15));
		assertThat(batchBVE.getAantalRecords(), is(61));
		assertThat(batchBVE.getVerantwoordelijkeAanlevering(),
			is(VerantwoordelijkeAanleverbestand.Instelling));
		assertThat(batchBVE.getLaatsteAanlevering(), is(false));

		writeBronBatch(batchBVE);
	}
}
