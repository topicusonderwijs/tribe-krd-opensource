package nl.topicus.eduarte.krd.bron.schakeltest.bve.aanlevering.bo;

import static nl.topicus.cobra.types.personalia.Geslacht.*;
import static nl.topicus.onderwijs.duo.bron.bve.waardelijsten.Intensiteit.*;
import static nl.topicus.onderwijs.duo.bron.bve.waardelijsten.Leerweg.*;
import static nl.topicus.onderwijs.duo.bron.bve.waardelijsten.SoortMutatie.*;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import nl.topicus.eduarte.entities.bpv.BPVInschrijving.BPVStatus;
import nl.topicus.eduarte.entities.examen.Examendeelname;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis.VerbintenisStatus;
import nl.topicus.eduarte.krd.entities.bron.meldingen.aanlevering.BronAanleverMelding;
import nl.topicus.onderwijs.duo.bron.BRONConstants;
import nl.topicus.onderwijs.duo.bron.bve.batches.pve_9_9.aanlevering.PersoonsgegevensRecord;
import nl.topicus.onderwijs.duo.bron.bve.batches.pve_9_9.aanlevering.bo.BpvGegevensRecord;
import nl.topicus.onderwijs.duo.bron.bve.batches.pve_9_9.aanlevering.bo.ExamengegevensRecord;
import nl.topicus.onderwijs.duo.bron.bve.batches.pve_9_9.aanlevering.bo.InschrijvingsgegevensRecord;
import nl.topicus.onderwijs.duo.bron.bve.batches.pve_9_9.aanlevering.bo.PeriodegegevensInschrijvingRecord;
import nl.topicus.onderwijs.duo.bron.data.types.Datum;

import org.junit.Test;

public class ProefgevallenBveBOBatch2Test extends ProefgevallenBveBO
{
	@Test
	public void proefgeval14() throws Exception
	{
		createGeval14();
		controller.save();

		BronAanleverMelding melding = getEersteMelding();
		assertThat(melding.getLeerlingnummer(), is("1002"));
		assertThat(melding.getSofinummer(), is("210000697"));
		assertThat(melding.getGeboortedatum(), is(Datum.valueOf("19910716")));
		assertThat(melding.getPostcodeVolgensInstelling(), is("9722TB"));
		assertThat(melding.getLand(), is((String) null));

		assertThat(melding.getMeldingen().size(), is(3));

		InschrijvingsgegevensRecord inschrijving = melding.getMeldingen().get(0);

		assertThat(inschrijving.getSoortMutatie(), is(Toevoeging));
		assertThat(inschrijving.getGevolgdeOpleiding(), is("10342"));
		assertThat(inschrijving.getDatumInschrijving(), is(asDate(20080801)));
		assertThat(inschrijving.getLeerweg(), is(Beroepsopleidend));
		assertThat(inschrijving.getIntensiteit(), is(Voltijd));

		PeriodegegevensInschrijvingRecord periode1 = melding.getMeldingen().get(1);
		assertThat(periode1.getRecordType(), is(BRONConstants.BVE_AANLEVERING_BO_PERIODEGEGEVENS));
		assertThat(periode1.getDatumIngangPeriodegegevensInschrijving(), is(asDate(20080801)));
		assertThat(periode1.getIndicatieBekostigingInschrijving(), is(false));
		assertThat(periode1.getSoortMutatie(), is(Toevoeging));

		PeriodegegevensInschrijvingRecord periode2 = melding.getMeldingen().get(2);
		assertThat(periode2.getRecordType(), is(BRONConstants.BVE_AANLEVERING_BO_PERIODEGEGEVENS));
		assertThat(periode2.getDatumIngangPeriodegegevensInschrijving(), is(asDate(20091101)));
		assertThat(periode2.getIndicatieBekostigingInschrijving(), is(false));
		assertThat(periode2.getSoortMutatie(), is(Toevoeging));
	}

	private void createGeval14() throws Exception
	{
		getDeelnemer1002();

		addChange(verbintenis, "status", VerbintenisStatus.Voorlopig, VerbintenisStatus.Definitief);
	}

	@Test
	public void proefgeval15() throws Exception
	{
		createGeval15();
		controller.save();

		BronAanleverMelding melding = getEersteMelding();
		assertThat(melding.getLeerlingnummer(), is("1003"));
		assertThat(melding.getSofinummer(), is("210000028"));
		assertThat(melding.getGeboortedatum(), is(Datum.valueOf("19910503")));
		assertThat(melding.getPostcodeVolgensInstelling(), is("9722TB"));
		assertThat(melding.getLand(), is((String) null));

		assertThat(melding.getMeldingen().size(), is(2));

		InschrijvingsgegevensRecord inschrijving = melding.getMeldingen().get(0);

		assertThat(inschrijving.getDatumInschrijving(), is(asDate(20080801)));
		assertThat(inschrijving.getGevolgdeOpleiding(), is("10342"));
		assertThat(inschrijving.getLeerweg(), is(Beroepsopleidend));
		assertThat(inschrijving.getIntensiteit(), is(Voltijd));
		assertThat(inschrijving.getIndicatieGehandicapt(), is(false));
		assertThat(inschrijving.getGeplandeDatumUitschrijving(), is(asDate(20110101)));
		assertThat(inschrijving.getSoortMutatie(), is(Toevoeging));

		PeriodegegevensInschrijvingRecord periode1 = melding.getMeldingen().get(1);
		assertThat(periode1.getRecordType(), is(BRONConstants.BVE_AANLEVERING_BO_PERIODEGEGEVENS));
		assertThat(periode1.getDatumIngangPeriodegegevensInschrijving(), is(asDate(20080801)));
		assertThat(periode1.getIndicatieBekostigingInschrijving(), is(true));
		assertThat(periode1.getSoortMutatie(), is(Toevoeging));
	}

	private void createGeval15() throws Exception
	{
		getDeelnemer1003();
		addChange(verbintenis, "status", VerbintenisStatus.Voorlopig, VerbintenisStatus.Definitief);
	}

	@Test
	public void proefgeval16() throws Exception
	{
		createGeval16();
		controller.save();

		BronAanleverMelding melding = getEersteMelding();

		assertThat(melding.getLeerlingnummer(), is("1004"));
		assertThat(melding.getSofinummer(), is((String) null));
		assertThat(melding.getOnderwijsnummer(), is("100320254"));
		assertThat(melding.getGeboortedatum(), is(Datum.valueOf("19910701")));
		assertThat(melding.getGeslacht(), is(Man));
		assertThat(melding.getPostcodeVolgensInstelling(), is((String) null));
		assertThat(melding.getLand(), is("6030"));

		assertThat(melding.getMeldingen().size(), is(2));

		InschrijvingsgegevensRecord inschrijving = melding.getMeldingen().get(0);

		assertThat(inschrijving.getSoortMutatie(), is(Toevoeging));
		assertThat(inschrijving.getGevolgdeOpleiding(), is("10342"));
		assertThat(inschrijving.getDatumInschrijving(), is(asDate(20080801)));
		assertThat(inschrijving.getIndicatieGehandicapt(), is(false));
		assertThat(inschrijving.getLeerweg(), is(Beroepsopleidend));
		assertThat(inschrijving.getIntensiteit(), is(Voltijd));

		PeriodegegevensInschrijvingRecord periode = melding.getMeldingen().get(1);
		assertThat(periode.getRecordType(), is(BRONConstants.BVE_AANLEVERING_BO_PERIODEGEGEVENS));
		assertThat(periode.getDatumIngangPeriodegegevensInschrijving(), is(asDate(20080801)));
		assertThat(periode.getIndicatieBekostigingInschrijving(), is(true));
		assertThat(periode.getSoortMutatie(), is(Toevoeging));
	}

	private void createGeval16() throws Exception
	{
		getDeelnemer1004();
		addChange(verbintenis, "status", VerbintenisStatus.Voorlopig, VerbintenisStatus.Definitief);
	}

	@Test
	public void proefgeval17() throws Exception
	{
		createGeval17();
		controller.save();

		BronAanleverMelding melding = getEersteMelding();

		assertThat(melding.getLeerlingnummer(), is("1005"));
		assertThat(melding.getSofinummer(), is((String) null));
		assertThat(melding.getOnderwijsnummer(), is((String) null));
		assertThat(melding.getGeboortedatum(), is(Datum.valueOf("19910717")));
		assertThat(melding.getGeslacht(), is(Man));
		assertThat(melding.getPostcodeVolgensInstelling(), is("9722TB"));
		assertThat(melding.getLand(), is((String) null));

		assertThat(melding.getMeldingen().size(), is(3));

		PersoonsgegevensRecord personalia = melding.getMeldingen().get(0);
		assertThat(personalia.getRecordType(), is(BRONConstants.BVE_AANLEVERING_PERSOONSGEGEVENS));

		assertThat(personalia.getAchternaam(), is("ACHTERNAAMZEVENENZESTIG"));
		assertThat(personalia.getAlleVoornamen(), is("VOORNAAMZEVENENZESTIG"));
		assertThat(personalia.getStraatnaam(), is("Kemkensberg"));
		assertThat(personalia.getPlaatsnaam(), is("Groningen"));

		InschrijvingsgegevensRecord inschrijving = melding.getMeldingen().get(1);

		assertThat(inschrijving.getRecordType(),
			is(BRONConstants.BVE_AANLEVERING_BO_INSCHRIJFGEGEVENS));
		assertThat(inschrijving.getSoortMutatie(), is(Toevoeging));
		assertThat(inschrijving.getGevolgdeOpleiding(), is("10342"));
		assertThat(inschrijving.getDatumInschrijving(), is(asDate(20080801)));
		assertThat(inschrijving.getIndicatieGehandicapt(), is(false));
		assertThat(inschrijving.getLeerweg(), is(Beroepsopleidend));
		assertThat(inschrijving.getIntensiteit(), is(Voltijd));

		PeriodegegevensInschrijvingRecord periode = melding.getMeldingen().get(2);
		assertThat(periode.getRecordType(), is(BRONConstants.BVE_AANLEVERING_BO_PERIODEGEGEVENS));
		assertThat(periode.getDatumIngangPeriodegegevensInschrijving(), is(asDate(20080801)));
		assertThat(periode.getIndicatieBekostigingInschrijving(), is(true));
		assertThat(periode.getSoortMutatie(), is(Toevoeging));
	}

	private void createGeval17() throws Exception
	{
		getDeelnemer1005();
		addChange(verbintenis, "status", VerbintenisStatus.Voorlopig, VerbintenisStatus.Definitief);
	}

	@Test
	public void proefgeval18() throws Exception
	{
		createGeval18();
		controller.save();

		BronAanleverMelding melding = getEersteMelding();

		assertThat(melding.getLeerlingnummer(), is("1006"));
		assertThat(melding.getSofinummer(), is((String) null));
		assertThat(melding.getOnderwijsnummer(), is((String) null));
		assertThat(melding.getGeboortedatum(), is(Datum.valueOf("19910702")));
		assertThat(melding.getGeslacht(), is(Man));
		assertThat(melding.getPostcodeVolgensInstelling(), is((String) null));
		assertThat(melding.getLand(), is("6030"));

		assertThat(melding.getMeldingen().size(), is(3));

		PersoonsgegevensRecord personalia = melding.getMeldingen().get(0);
		assertThat(personalia.getAchternaam(), is("ACHTERNAAMTWEEENVIJFTIG"));
		assertThat(personalia.getAlleVoornamen(), is("VOORNAAMTWEEENVIJFTIG"));

		InschrijvingsgegevensRecord inschrijving = melding.getMeldingen().get(1);

		assertThat(inschrijving.getSoortMutatie(), is(Toevoeging));
		assertThat(inschrijving.getGevolgdeOpleiding(), is("10342"));
		assertThat(inschrijving.getDatumInschrijving(), is(asDate(20080801)));
		assertThat(inschrijving.getIndicatieGehandicapt(), is(false));
		assertThat(inschrijving.getLeerweg(), is(Beroepsopleidend));
		assertThat(inschrijving.getIntensiteit(), is(Voltijd));

		PeriodegegevensInschrijvingRecord periode = melding.getMeldingen().get(2);
		assertThat(periode.getRecordType(), is(BRONConstants.BVE_AANLEVERING_BO_PERIODEGEGEVENS));
		assertThat(periode.getDatumIngangPeriodegegevensInschrijving(), is(asDate(20080801)));
		assertThat(periode.getIndicatieBekostigingInschrijving(), is(true));
		assertThat(periode.getSoortMutatie(), is(Toevoeging));
	}

	private void createGeval18() throws Exception
	{
		getDeelnemer1006();
		addChange(verbintenis, "status", VerbintenisStatus.Voorlopig, VerbintenisStatus.Definitief);
	}

	@Test
	public void proefgeval19() throws Exception
	{
		createGeval19();

		controller.save();

		BronAanleverMelding melding = getEersteMelding();

		assertThat(melding.getLeerlingnummer(), is("1007"));
		assertThat(melding.getSofinummer(), is("800000122"));
		assertThat(melding.getOnderwijsnummer(), is((String) null));
		assertThat(melding.getGeboortedatum(), is(Datum.valueOf("19900202")));
		assertThat(melding.getGeslacht(), is(Vrouw));
		assertThat(melding.getPostcodeVolgensInstelling(), is("9711LK"));
		assertThat(melding.getLand(), is((String) null));

		assertThat(melding.getMeldingen().size(), is(2));

		InschrijvingsgegevensRecord inschrijving = melding.getMeldingen().get(0);

		assertThat(inschrijving.getSoortMutatie(), is(Toevoeging));
		assertThat(inschrijving.getGevolgdeOpleiding(), is("10021"));
		assertThat(inschrijving.getDatumInschrijving(), is(asDate(20080801)));
		assertThat(inschrijving.getIndicatieGehandicapt(), is(true));
		assertThat(inschrijving.getLeerweg(), is(Beroepsbegeleidend));
		assertThat(inschrijving.getIntensiteit(), is(Voltijd));

		PeriodegegevensInschrijvingRecord periode = melding.getMeldingen().get(1);
		assertThat(periode.getRecordType(), is(BRONConstants.BVE_AANLEVERING_BO_PERIODEGEGEVENS));
		assertThat(periode.getDatumIngangPeriodegegevensInschrijving(), is(asDate(20080801)));
		assertThat(periode.getIndicatieBekostigingInschrijving(), is(true));
		assertThat(periode.getSoortMutatie(), is(Toevoeging));
	}

	private void createGeval19() throws Exception
	{
		getDeelnemer1007();
		addChange(verbintenis, "status", VerbintenisStatus.Voorlopig, VerbintenisStatus.Definitief);
	}

	@Test
	public void proefgeval20() throws Exception
	{
		createGeval20();

		controller.save();

		BronAanleverMelding melding = getEersteMelding();

		assertThat(melding.getLeerlingnummer(), is("1008"));
		assertThat(melding.getSofinummer(), is("210000727"));
		assertThat(melding.getOnderwijsnummer(), is((String) null));
		assertThat(melding.getGeboortedatum(), is(Datum.valueOf("19910719")));
		assertThat(melding.getGeslacht(), is(Man));
		assertThat(melding.getPostcodeVolgensInstelling(), is("9722TB"));
		assertThat(melding.getLand(), is((String) null));

		assertThat(melding.getMeldingen().size(), is(2));

		InschrijvingsgegevensRecord inschrijving = melding.getMeldingen().get(0);

		assertThat(inschrijving.getSoortMutatie(), is(Toevoeging));
		assertThat(inschrijving.getGevolgdeOpleiding(), is("10342"));
		assertThat(inschrijving.getDatumInschrijving(), is(asDate(20080801)));
		assertThat(inschrijving.getIndicatieGehandicapt(), is(false));
		assertNull(inschrijving.getLeerweg());
		assertThat(inschrijving.getIntensiteit(), is(Examendeelnemer));

		PeriodegegevensInschrijvingRecord periode = melding.getMeldingen().get(1);
		assertThat(periode.getRecordType(), is(BRONConstants.BVE_AANLEVERING_BO_PERIODEGEGEVENS));
		assertThat(periode.getDatumIngangPeriodegegevensInschrijving(), is(asDate(20080801)));
		assertThat(periode.getIndicatieBekostigingInschrijving(), is(false));
		assertThat(periode.getSoortMutatie(), is(Toevoeging));
	}

	private void createGeval20() throws Exception
	{
		getDeelnemer1008();
		addChange(verbintenis, "status", VerbintenisStatus.Voorlopig, VerbintenisStatus.Definitief);
	}

	@Test
	public void proefgeval21() throws Exception
	{
		createGeval21();
		controller.save();

		BronAanleverMelding melding = getEersteMelding();
		assertThat(melding.getLeerlingnummer(), is("1016"));
		assertThat(melding.getSofinummer(), is("210000181"));
		assertThat(melding.getOnderwijsnummer(), is((String) null));
		assertThat(melding.getGeboortedatum(), is(Datum.valueOf("19910516")));
		assertThat(melding.getGeslacht(), is(Man));
		assertThat(melding.getPostcodeVolgensInstelling(), is("9722TB"));
		assertThat(melding.getLand(), is((String) null));

		assertThat(melding.getMeldingen().size(), is(3));

		InschrijvingsgegevensRecord inschrijving = melding.getMeldingen().get(0);
		assertThat(inschrijving.getDatumInschrijving(), is(asDate(20080801)));
		assertThat(inschrijving.getGevolgdeOpleiding(), is("12003"));
		assertThat(inschrijving.getSoortMutatie(), is(Toevoeging));

		PeriodegegevensInschrijvingRecord bekostiging = melding.getMeldingen().get(1);
		assertThat(bekostiging.getRecordType(),
			is(BRONConstants.BVE_AANLEVERING_BO_PERIODEGEGEVENS));
		assertThat(bekostiging.getSoortMutatie(), is(Toevoeging));
		assertThat(bekostiging.getDatumIngangPeriodegegevensInschrijving(), is(asDate(20080801)));
		assertThat(bekostiging.getIndicatieBekostigingInschrijving(), is(true));

		ExamengegevensRecord examenmelding = melding.getMeldingen().get(2);
		assertThat(examenmelding.getRecordType(),
			is(BRONConstants.BVE_AANLEVERING_BO_EXAMENGEGEVENS));
		assertThat(examenmelding.getSoortMutatie(), is(Toevoeging));
		assertThat(examenmelding.getBehaaldeDeelKwalificatie(), is("12003"));
		assertThat(examenmelding.getDatumBehaald(), is(asDate(20090211)));
	}

	private void createGeval21() throws Exception
	{
		getDeelnemer1016();

		// examendeelname bevat de deelkwalificatie deelname na deze twee calls, aangezien
		// deze als laatste gezet is. Deze afhankelijkheid is noodzakelijk voor
		// proefgeval34(). Hierdoor moeten we nu bij de 'officiele verbintenis' de
		// examendeelname voor geval21 alsnog ophalen.

		Examendeelname examen = verbintenis.getExamendeelnames().get(0);
		addChange(examen, "examenstatus", null, examen.getExamenstatus());
		addChange(verbintenis, "status", VerbintenisStatus.Voorlopig, VerbintenisStatus.Definitief);
	}

	@Test
	public void proefgeval22() throws Exception
	{
		createGeval22();
		controller.save();

		BronAanleverMelding melding = getEersteMelding();
		assertThat(melding.getLeerlingnummer(), is("1017"));
		assertThat(melding.getSofinummer(), is("210000193"));
		assertThat(melding.getOnderwijsnummer(), is((String) null));
		assertThat(melding.getGeboortedatum(), is(Datum.valueOf("19910517")));
		assertThat(melding.getGeslacht(), is(Man));
		assertThat(melding.getPostcodeVolgensInstelling(), is("9722TB"));
		assertThat(melding.getLand(), is((String) null));
		assertThat(melding.getMeldingen().size(), is(3));

		InschrijvingsgegevensRecord inschrijving = melding.getMeldingen().get(0);
		assertThat(inschrijving.getDatumInschrijving(), is(asDate(20081002)));
		assertThat(inschrijving.getGevolgdeOpleiding(), is("12003"));
		assertThat(inschrijving.getIndicatieGehandicapt(), is(false));
		assertThat(inschrijving.getIntensiteit(), is(Voltijd));
		assertThat(inschrijving.getLeerweg(), is(Beroepsopleidend));
		assertThat(inschrijving.getSoortMutatie(), is(Toevoeging));

		PeriodegegevensInschrijvingRecord bekostiging = melding.getMeldingen().get(1);
		assertThat(bekostiging.getRecordType(),
			is(BRONConstants.BVE_AANLEVERING_BO_PERIODEGEGEVENS));
		assertThat(bekostiging.getDatumIngangPeriodegegevensInschrijving(), is(asDate(20081002)));
		assertThat(bekostiging.getIndicatieBekostigingInschrijving(), is(true));
		assertThat(bekostiging.getSoortMutatie(), is(Toevoeging));

		ExamengegevensRecord examenmelding = melding.getMeldingen().get(2);
		assertThat(examenmelding.getRecordType(),
			is(BRONConstants.BVE_AANLEVERING_BO_EXAMENGEGEVENS));
		assertThat(examenmelding.getSoortMutatie(), is(Toevoeging));
		assertThat(examenmelding.getBehaaldeDeelKwalificatie(), is("12003"));
		assertThat(examenmelding.getDatumBehaald(), is(asDate(20090211)));
	}

	private void createGeval22() throws Exception
	{
		getDeelnemer1017();

		addChange(verbintenis, "status", VerbintenisStatus.Voorlopig, VerbintenisStatus.Definitief);

		Examendeelname examendeelname = verbintenis.getExamendeelnames().get(0);
		addChange(examendeelname, "examenstatus", null, examendeelname.getExamenstatus());
	}

	@Test
	public void proefgeval23() throws Exception
	{
		createGeval23();
		controller.save();

		BronAanleverMelding melding = getEersteMelding();
		assertThat(melding.getLeerlingnummer(), is("1018"));
		assertThat(melding.getSofinummer(), is("210000211"));
		assertThat(melding.getOnderwijsnummer(), is((String) null));
		assertThat(melding.getGeboortedatum(), is(Datum.valueOf("19910518")));
		assertThat(melding.getGeslacht(), is(Man));
		assertThat(melding.getPostcodeVolgensInstelling(), is("9722TB"));
		assertThat(melding.getLand(), is((String) null));
		assertThat(melding.getMeldingen().size(), is(3));

		InschrijvingsgegevensRecord inschrijving = melding.getMeldingen().get(0);
		assertThat(inschrijving.getDatumInschrijving(), is(asDate(20080801)));
		assertThat(inschrijving.getGevolgdeOpleiding(), is("12003"));
		assertThat(inschrijving.getIndicatieGehandicapt(), is(false));
		assertThat(inschrijving.getIntensiteit(), is(Voltijd));
		assertThat(inschrijving.getLeerweg(), is(Beroepsopleidend));
		assertThat(inschrijving.getSoortMutatie(), is(Toevoeging));

		PeriodegegevensInschrijvingRecord bekostiging = melding.getMeldingen().get(1);
		assertThat(bekostiging.getRecordType(),
			is(BRONConstants.BVE_AANLEVERING_BO_PERIODEGEGEVENS));
		assertThat(bekostiging.getDatumIngangPeriodegegevensInschrijving(), is(asDate(20080801)));
		assertThat(bekostiging.getIndicatieBekostigingInschrijving(), is(true));
		assertThat(bekostiging.getSoortMutatie(), is(Toevoeging));

		ExamengegevensRecord examenmelding = melding.getMeldingen().get(2);
		assertThat(examenmelding.getRecordType(),
			is(BRONConstants.BVE_AANLEVERING_BO_EXAMENGEGEVENS));
		assertThat(examenmelding.getSoortMutatie(), is(Toevoeging));
		assertThat(examenmelding.getBehaaldeDeelKwalificatie(), is("12003"));
		assertThat(examenmelding.getDatumBehaald(), is(asDate(20090211)));
	}

	private void createGeval23() throws Exception
	{
		getDeelnemer1018();
		addChange(verbintenis, "status", VerbintenisStatus.Voorlopig, VerbintenisStatus.Definitief);

		Examendeelname examendeelname = verbintenis.getExamendeelnames().get(0);
		addChange(examendeelname, "examenstatus", null, examendeelname.getExamenstatus());
	}

	@Test
	public void proefgeval24() throws Exception
	{
		createGeval24();

		controller.save();

		BronAanleverMelding melding = getEersteMelding();
		assertThat(melding.getLeerlingnummer(), is("1019"));
		assertThat(melding.getSofinummer(), is("210000223"));
		assertThat(melding.getOnderwijsnummer(), is((String) null));
		assertThat(melding.getGeboortedatum(), is(Datum.valueOf("19910519")));
		assertThat(melding.getGeslacht(), is(Man));
		assertThat(melding.getPostcodeVolgensInstelling(), is("9722TB"));
		assertThat(melding.getLand(), is((String) null));
		assertThat(melding.getMeldingen().size(), is(3));

		InschrijvingsgegevensRecord inschrijving = melding.getMeldingen().get(0);
		assertThat(inschrijving.getDatumInschrijving(), is(asDate(20080801)));
		assertThat(inschrijving.getGevolgdeOpleiding(), is("12003"));
		assertThat(inschrijving.getIndicatieGehandicapt(), is(false));
		assertThat(inschrijving.getIntensiteit(), is(Voltijd));
		assertThat(inschrijving.getLeerweg(), is(Beroepsopleidend));
		assertThat(inschrijving.getSoortMutatie(), is(Toevoeging));

		PeriodegegevensInschrijvingRecord bekostiging = melding.getMeldingen().get(1);
		assertThat(bekostiging.getRecordType(),
			is(BRONConstants.BVE_AANLEVERING_BO_PERIODEGEGEVENS));
		assertThat(bekostiging.getDatumIngangPeriodegegevensInschrijving(), is(asDate(20080801)));
		assertThat(bekostiging.getIndicatieBekostigingInschrijving(), is(true));
		assertThat(bekostiging.getSoortMutatie(), is(Toevoeging));

		ExamengegevensRecord examenmelding = melding.getMeldingen().get(2);
		assertThat(examenmelding.getRecordType(),
			is(BRONConstants.BVE_AANLEVERING_BO_EXAMENGEGEVENS));
		assertThat(examenmelding.getSoortMutatie(), is(Toevoeging));
		assertThat(examenmelding.getBehaaldeDeelKwalificatie(), is("12003"));
		assertThat(examenmelding.getDatumBehaald(), is(asDate(20090211)));
	}

	private void createGeval24() throws Exception
	{
		getDeelnemer1019();

		addChange(verbintenis, "status", VerbintenisStatus.Voorlopig, VerbintenisStatus.Definitief);

		Examendeelname examendeelname = verbintenis.getExamendeelnames().get(0);
		addChange(examendeelname, "examenstatus", null, examendeelname.getExamenstatus());
	}

	@Test
	public void proefgeval25() throws Exception
	{
		createGeval25();
		controller.save();

		BronAanleverMelding melding = getEersteMelding();
		assertThat(melding.getLeerlingnummer(), is("1021"));
		assertThat(melding.getSofinummer(), is("210000259"));
		assertThat(melding.getOnderwijsnummer(), is((String) null));
		assertThat(melding.getGeboortedatum(), is(Datum.valueOf("19910521")));
		assertThat(melding.getGeslacht(), is(Man));
		assertThat(melding.getPostcodeVolgensInstelling(), is("9722TB"));
		assertThat(melding.getLand(), is((String) null));
		assertThat(melding.getMeldingen().size(), is(3));

		InschrijvingsgegevensRecord inschrijving = melding.getMeldingen().get(0);
		assertThat(inschrijving.getDatumInschrijving(), is(asDate(20080801)));
		assertThat(inschrijving.getGevolgdeOpleiding(), is("12003"));
		assertThat(inschrijving.getIndicatieGehandicapt(), is(false));
		assertThat(inschrijving.getIntensiteit(), is(Voltijd));
		assertThat(inschrijving.getLeerweg(), is(Beroepsopleidend));
		assertThat(inschrijving.getSoortMutatie(), is(Toevoeging));

		PeriodegegevensInschrijvingRecord bekostiging = melding.getMeldingen().get(1);
		assertThat(bekostiging.getRecordType(),
			is(BRONConstants.BVE_AANLEVERING_BO_PERIODEGEGEVENS));
		assertThat(bekostiging.getDatumIngangPeriodegegevensInschrijving(), is(asDate(20080801)));
		assertThat(bekostiging.getIndicatieBekostigingInschrijving(), is(true));
		assertThat(bekostiging.getSoortMutatie(), is(Toevoeging));

		BpvGegevensRecord bpv = melding.getMeldingen().get(2);
		assertThat(bpv.getRecordType(), is(BRONConstants.BVE_AANLEVERING_BO_BPVGEGEVENS));
		assertThat(bpv.getBpvVolgnummer(), is(1));
		assertThat(bpv.getAfsluitdatumBpv(), is(asDate(20080811)));
		assertThat(bpv.getDatumBeginBpv(), is(asDate(20080901)));
		assertThat(bpv.getGeplandeDatumEindeBpv(), is(asDate(20100901)));
		assertThat(bpv.getSoortMutatie(), is(Toevoeging));
		assertThat(bpv.getLeerbedrijf(), is("02PR9725JJ0001601"));
		assertThat(bpv.getOmvangBpv(), is(1000));
	}

	private void createGeval25() throws Exception
	{
		getDeelnemer1021();

		addChange(verbintenis, "status", VerbintenisStatus.Voorlopig, VerbintenisStatus.Definitief);
		addChange(bpvInschrijving, "status", BPVStatus.Voorlopig, BPVStatus.Definitief);
	}

	@Test
	public void proefgeval26() throws Exception
	{
		createGeval26();
		controller.save();

		BronAanleverMelding melding = getEersteMelding();
		assertThat(melding.getLeerlingnummer(), is("1022"));
		assertThat(melding.getSofinummer(), is("210000260"));
		assertThat(melding.getOnderwijsnummer(), is((String) null));
		assertThat(melding.getGeboortedatum(), is(Datum.valueOf("19910522")));
		assertThat(melding.getGeslacht(), is(Man));
		assertThat(melding.getPostcodeVolgensInstelling(), is("9722TB"));
		assertThat(melding.getLand(), is((String) null));
		assertThat(melding.getMeldingen().size(), is(3));

		InschrijvingsgegevensRecord inschrijving = melding.getMeldingen().get(0);
		assertThat(inschrijving.getDatumInschrijving(), is(asDate(20080801)));
		assertThat(inschrijving.getGevolgdeOpleiding(), is("12003"));
		assertThat(inschrijving.getIndicatieGehandicapt(), is(false));
		assertThat(inschrijving.getIntensiteit(), is(Voltijd));
		assertThat(inschrijving.getLeerweg(), is(Beroepsopleidend));
		assertThat(inschrijving.getSoortMutatie(), is(Toevoeging));

		PeriodegegevensInschrijvingRecord bekostiging = melding.getMeldingen().get(1);
		assertThat(bekostiging.getRecordType(),
			is(BRONConstants.BVE_AANLEVERING_BO_PERIODEGEGEVENS));
		assertThat(bekostiging.getDatumIngangPeriodegegevensInschrijving(), is(asDate(20080801)));
		assertThat(bekostiging.getIndicatieBekostigingInschrijving(), is(true));
		assertThat(bekostiging.getSoortMutatie(), is(Toevoeging));

		BpvGegevensRecord bpv = melding.getMeldingen().get(2);
		assertThat(bpv.getRecordType(), is(BRONConstants.BVE_AANLEVERING_BO_BPVGEGEVENS));
		assertThat(bpv.getBpvVolgnummer(), is(1));
		assertThat(bpv.getAfsluitdatumBpv(), is(asDate(20080811)));
		assertThat(bpv.getDatumBeginBpv(), is(asDate(20080901)));
		assertThat(bpv.getGeplandeDatumEindeBpv(), is(asDate(20100901)));
		assertThat(bpv.getSoortMutatie(), is(Toevoeging));
		assertThat(bpv.getLeerbedrijf(), is("02PR9725JJ0001601"));
		assertThat(bpv.getOmvangBpv(), is(1000));
	}

	private void createGeval26() throws Exception
	{
		getDeelnemer1022();
		addChange(verbintenis, "status", VerbintenisStatus.Voorlopig, VerbintenisStatus.Definitief);
		addChange(bpvInschrijving, "status", BPVStatus.Voorlopig, BPVStatus.Definitief);
	}

	@Test
	public void proefgeval27() throws Exception
	{
		createGeval27();
		controller.save();

		BronAanleverMelding melding = getEersteMelding();
		assertThat(melding.getLeerlingnummer(), is("1023"));
		assertThat(melding.getSofinummer(), is("210000272"));
		assertThat(melding.getOnderwijsnummer(), is((String) null));
		assertThat(melding.getGeboortedatum(), is(Datum.valueOf("19910523")));
		assertThat(melding.getGeslacht(), is(Man));
		assertThat(melding.getPostcodeVolgensInstelling(), is("9722TB"));
		assertThat(melding.getLand(), is((String) null));
		assertThat(melding.getMeldingen().size(), is(3));

		InschrijvingsgegevensRecord inschrijving = melding.getMeldingen().get(0);
		assertThat(inschrijving.getDatumInschrijving(), is(asDate(20080801)));
		assertThat(inschrijving.getGevolgdeOpleiding(), is("12003"));
		assertThat(inschrijving.getIndicatieGehandicapt(), is(false));
		assertThat(inschrijving.getIntensiteit(), is(Voltijd));
		assertThat(inschrijving.getLeerweg(), is(Beroepsopleidend));
		assertThat(inschrijving.getSoortMutatie(), is(Toevoeging));

		PeriodegegevensInschrijvingRecord bekostiging = melding.getMeldingen().get(1);
		assertThat(bekostiging.getRecordType(),
			is(BRONConstants.BVE_AANLEVERING_BO_PERIODEGEGEVENS));
		assertThat(bekostiging.getDatumIngangPeriodegegevensInschrijving(), is(asDate(20080801)));
		assertThat(bekostiging.getIndicatieBekostigingInschrijving(), is(true));
		assertThat(bekostiging.getSoortMutatie(), is(Toevoeging));

		BpvGegevensRecord bpv = melding.getMeldingen().get(2);
		assertThat(bpv.getRecordType(), is(BRONConstants.BVE_AANLEVERING_BO_BPVGEGEVENS));
		assertThat(bpv.getBpvVolgnummer(), is(1));
		assertThat(bpv.getAfsluitdatumBpv(), is(asDate(20080811)));
		assertThat(bpv.getDatumBeginBpv(), is(asDate(20080901)));
		assertThat(bpv.getGeplandeDatumEindeBpv(), is(asDate(20100901)));
		assertThat(bpv.getSoortMutatie(), is(Toevoeging));
		assertThat(bpv.getLeerbedrijf(), is("02PR9725JJ0001601"));
		assertThat(bpv.getOmvangBpv(), is(1000));
	}

	private void createGeval27() throws Exception
	{
		getDeelnemer1023();
		addChange(verbintenis, "status", VerbintenisStatus.Voorlopig, VerbintenisStatus.Definitief);
		addChange(bpvInschrijving, "status", null, BPVStatus.Definitief);
	}

	@Test
	public void bveBO1Batch2() throws Exception
	{
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

		batchBVE = maakBronBOBatch();
		batchBVE.setBatchNummer(2);
		batchBVE.getAanleverpunt().setNummer(1);

		writeBronBatch(batchBVE);

		assertThat(batchBVE.getAantalMeldingen(), is(14));
		assertThat(batchBVE.getAantalRecords(), is(53));
	}
}
