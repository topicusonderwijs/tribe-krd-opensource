package nl.topicus.eduarte.krd.bron.schakeltest.bve.aanlevering.bo;

import static java.util.Arrays.*;
import static nl.topicus.cobra.types.personalia.Geslacht.*;
import static nl.topicus.eduarte.entities.inschrijving.Verbintenis.Bekostigd.*;
import static nl.topicus.eduarte.entities.inschrijving.Verbintenis.VerbintenisStatus.*;
import static nl.topicus.eduarte.tester.hibernate.DatabaseAction.*;
import static nl.topicus.onderwijs.duo.bron.bve.waardelijsten.Intensiteit.*;
import static nl.topicus.onderwijs.duo.bron.bve.waardelijsten.SoortMutatie.*;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.util.Arrays;

import nl.topicus.eduarte.entities.BronEntiteitStatus;
import nl.topicus.eduarte.entities.bpv.BPVInschrijving.BPVStatus;
import nl.topicus.eduarte.entities.examen.Examendeelname;
import nl.topicus.eduarte.entities.examen.Examenstatus;
import nl.topicus.eduarte.entities.inschrijving.Bekostigingsperiode;
import nl.topicus.eduarte.entities.inschrijving.Vooropleiding;
import nl.topicus.eduarte.entities.landelijk.Schooljaar;
import nl.topicus.eduarte.krd.bron.BronController;
import nl.topicus.eduarte.krd.entities.bron.BronMeldingStatus;
import nl.topicus.eduarte.krd.entities.bron.BronStatus;
import nl.topicus.eduarte.krd.entities.bron.meldingen.aanlevering.BronAanleverMelding;
import nl.topicus.onderwijs.duo.bron.bve.batches.pve_9_9.aanlevering.PersoonsgegevensRecord;
import nl.topicus.onderwijs.duo.bron.bve.batches.pve_9_9.aanlevering.bo.BpvGegevensRecord;
import nl.topicus.onderwijs.duo.bron.bve.batches.pve_9_9.aanlevering.bo.ExamengegevensRecord;
import nl.topicus.onderwijs.duo.bron.bve.batches.pve_9_9.aanlevering.bo.InschrijvingsgegevensRecord;
import nl.topicus.onderwijs.duo.bron.bve.waardelijsten.HoogsteVooropleiding;
import nl.topicus.onderwijs.duo.bron.data.types.Datum;

import org.junit.Test;

public class ProefgevallenBveBOVerdichtenTest extends ProefgevallenBveBO
{
	private static final long bsn = 210000004L;

	private static final long bsn2 = 210000016L;

	@Test
	public void testVerdichtenVanBoInschrijvingsWijzigingen() throws Exception
	{
		getDeelnemer1001();
		addChange(verbintenis, "status", Voorlopig, Definitief);

		controller.save();

		BronAanleverMelding melding = getEersteMelding();
		assertThat(tester.getTransactionLog(), is(asList(insert(melding))));
		assertThat(melding.getBronMeldingStatus(), is(equalTo(BronMeldingStatus.WACHTRIJ)));

		assertThat(getRecordTypes(melding), is(equalTo(asList(305, 320, 321))));
		assertThat(getMutaties(melding), is(equalTo(asList(Toevoeging, Toevoeging))));

		// creeer een nieuwe controller
		controller = new BronController();

		verbintenis.setIntensiteit(Deeltijd);
		addChange(verbintenis, "intensiteit", Voltijd, Deeltijd);

		controller.save();

		assertThat(tester.getTransactionLog(), is(asList(insert(melding), update(melding))));

		assertThat(getRecordTypes(melding), is(equalTo(asList(305, 320, 321))));
		assertThat(getMutaties(melding), is(equalTo(asList(Toevoeging, Toevoeging))));
	}

	@Test
	public void testVerdichtenVanBoInschrijvingsWijzigingEnBekostigingsWijziging() throws Exception
	{
		getDeelnemer1001();
		addChange(verbintenis, "status", Voorlopig, Definitief);

		controller.save();

		int aantalMeldingen = aantalInTransactie(BronAanleverMelding.class);
		assertThat(aantalMeldingen, is(1));

		BronAanleverMelding melding = getEersteMelding();

		assertThat(melding.getBronMeldingStatus(), is(equalTo(BronMeldingStatus.WACHTRIJ)));

		assertThat(getRecordTypes(melding), is(equalTo(asList(305, 320, 321))));
		assertThat(getMutaties(melding), is(equalTo(asList(Toevoeging, Toevoeging))));

		// creeer een nieuwe controller
		controller = new BronController();

		verbintenis.setBekostigd(Nee);
		addChange(verbintenis, "bekostigd", Ja, Nee);

		controller.save();

		assertThat(tester.getTransactionLog(), is(asList(insert(melding), update(melding))));

		assertThat(getRecordTypes(melding), is(equalTo(asList(305, 320, 321))));
		assertThat(getMutaties(melding), is(equalTo(asList(Toevoeging, Toevoeging))));
	}

	@Test
	public void testVerdichtenVanBoInschrijvingsWijzigingEnBekostigingsToevoeging()
			throws Exception
	{
		getDeelnemer1001();
		addChange(verbintenis, "status", Voorlopig, Definitief);

		controller.save();

		int aantalMeldingen = aantalInTransactie(BronAanleverMelding.class);
		assertThat(aantalMeldingen, is(1));

		BronAanleverMelding melding = getEersteMelding();

		assertThat(melding.getBronMeldingStatus(), is(equalTo(BronMeldingStatus.WACHTRIJ)));
		assertThat(getRecordTypes(melding), is(equalTo(asList(305, 320, 321))));

		// creeer een nieuwe controller
		controller = new BronController();

		verbintenis.setBekostigd(Gedeeltelijk);
		addChange(verbintenis, "bekostigd", Ja, Gedeeltelijk);

		Bekostigingsperiode periode1 = new Bekostigingsperiode();
		periode1.setBegindatum(verbintenis.getBegindatum());
		periode1.setBekostigd(true);
		periode1.setEinddatum(asDate(20091231));
		periode1.setVerbintenis(verbintenis);
		verbintenis.getBekostigingsperiodes().add(periode1);

		Bekostigingsperiode periode2 = new Bekostigingsperiode();
		periode2.setBegindatum(asDate(20100101));
		periode2.setBekostigd(false);
		periode2.setEinddatum(verbintenis.getGeplandeEinddatum());
		periode2.setVerbintenis(verbintenis);
		verbintenis.getBekostigingsperiodes().add(periode2);

		addChange(periode1, "begindatum", null, periode1.getBegindatum());
		addChange(periode1, "bekostigd", null, true);
		addChange(periode1, "einddatum", null, periode1.getEinddatum());

		addChange(periode2, "begindatum", null, periode2.getBegindatum());
		addChange(periode2, "bekostigd", null, false);
		addChange(periode2, "einddatum", null, periode2.getEinddatum());

		controller.save();

		assertThat(tester.getTransactionLog(), is(asList(insert(melding), update(melding))));

		assertThat(getRecordTypes(melding), is(equalTo(asList(305, 320, 321, 321))));
		assertThat(getMutaties(melding), is(equalTo(asList(Toevoeging, Aanpassing, Toevoeging))));
	}

	@Test
	public void testVerdichtenVanBoInschrijvingsWijzigingEnVooropleidingWijziging()
			throws Exception
	{
		getDeelnemer1001();
		addChange(verbintenis, "status", Voorlopig, Definitief);

		controller.save();

		int aantalMeldingen = aantalInTransactie(BronAanleverMelding.class);
		assertThat(aantalMeldingen, is(1));

		BronAanleverMelding melding = getEersteMelding();
		assertThat(melding.getBronMeldingStatus(), is(equalTo(BronMeldingStatus.WACHTRIJ)));
		assertThat(getRecordTypes(melding), is(equalTo(asList(305, 320, 321))));
		assertThat(getEersteMelding().getRecord(InschrijvingsgegevensRecord.class)
			.getHoogsteVooropleiding(), is(HoogsteVooropleiding.HAVO));

		// creeer een nieuwe controller
		controller = new BronController();

		Vooropleiding vooropleiding = deelnemer.getVooropleidingen().get(0);
		vooropleiding.setDiplomaBehaald(false);

		addChange(vooropleiding, "diplomaBehaald", true, false);

		controller.save();

		assertThat(tester.getTransactionLog(), is(asList(insert(melding), update(melding))));

		assertThat(getRecordTypes(melding), is(equalTo(asList(305, 320, 321))));
		assertThat(getMutaties(melding), is(equalTo(asList(Toevoeging, Toevoeging))));
		assertThat(getEersteMelding().getRecord(InschrijvingsgegevensRecord.class)
			.getHoogsteVooropleiding(), is(HoogsteVooropleiding.Basisvorming));
	}

	/**
	 * Controleert of de broncontroller constateert dat een melding in de wachtrij voor
	 * een verbintenis die toevoegt, verwijderd wordt als er een melding 'VERWIJDER'
	 * getriggerd wordt. Hierdoor worden er geen zinloze meldingen naar BRON gestuurd
	 * (toevoeging, verwijdering).
	 */
	@Test
	public void boInschrijvingToevoegingEnVerwijderingVerdichtNaarGeenMelding() throws Exception
	{
		getDeelnemer1001();

		addChange(verbintenis, "status", Voorlopig, Definitief);

		controller.save();

		int aantalMeldingen = aantalInTransactie(BronAanleverMelding.class);
		assertThat(aantalMeldingen, is(1));

		BronAanleverMelding melding = getEersteMelding();

		assertThat(tester.getTransactionLog(), is(asList(insert(melding))));
		assertThat(deelnemer.getBronStatus(), is(BronEntiteitStatus.Wachtrij));
		assertThat(verbintenis.getBronStatus(), is(BronEntiteitStatus.Wachtrij));

		assertThat(melding.getBronMeldingStatus(), is(equalTo(BronMeldingStatus.WACHTRIJ)));
		assertThat(getRecordTypes(melding), is(equalTo(asList(305, 320, 321))));

		// creeer een nieuwe controller
		controller = new BronController();

		verbintenis.setStatus(Voorlopig);
		addChange(verbintenis, "status", Definitief, Voorlopig);

		controller.save();

		assertThat(tester.getTransactionLog(), is(asList(insert(melding), delete(melding))));

		assertThat(deelnemer.getBronStatus(), is(BronEntiteitStatus.Geen));
		assertThat(verbintenis.getBronStatus(), is(BronEntiteitStatus.Geen));
	}

	/**
	 * Controleert of een BPV inschrijvingsrecord verwijderd wordt als hier een toevoeging
	 * *en* een verwijdering mutatie op geregistreerd wordt.
	 */
	@Test
	public void bpvInschrijvingToevoegingEnVerwijderingVerdichtNaarGeenRecord() throws Exception
	{
		getDeelnemer1021();

		zetSchooljaarStatus(BronStatus.GegevensWordenIngevoerd);

		addChange(verbintenis, "status", null, Definitief);
		addChange(bpvInschrijving, "status", null, BPVStatus.Definitief);

		controller.save();

		int aantalMeldingen = aantalInTransactie(BronAanleverMelding.class);
		assertThat(aantalMeldingen, is(1));

		BronAanleverMelding melding = getEersteMelding();

		assertThat(tester.getTransactionLog(), is(asList(insert(melding))));
		assertThat(deelnemer.getBronStatus(), is(BronEntiteitStatus.Wachtrij));
		assertThat(verbintenis.getBronStatus(), is(BronEntiteitStatus.Wachtrij));

		assertThat(melding.getBronMeldingStatus(), is(equalTo(BronMeldingStatus.WACHTRIJ)));
		assertThat(getRecordTypes(melding), is(equalTo(asList(305, 320, 321, 322))));

		BpvGegevensRecord record = melding.getRecord(BpvGegevensRecord.class);

		// creeer een nieuwe controller
		controller = new BronController();

		bpvInschrijving.setStatus(BPVStatus.Voorlopig);
		addChange(bpvInschrijving, "status", BPVStatus.Definitief, BPVStatus.Voorlopig);

		controller.save();

		assertThat(tester.getTransactionLog(), is(asList(insert(melding), delete(record),
			update(melding))));

		assertThat(getRecordTypes(melding), is(equalTo(asList(305, 320, 321))));
		assertThat(deelnemer.getBronStatus(), is(BronEntiteitStatus.Wachtrij));
		assertThat(verbintenis.getBronStatus(), is(BronEntiteitStatus.Wachtrij));
	}

	/**
	 * Controleert of de broncontroller constateert dat een lege melding in de wachtrij
	 * verwijderd wordt.
	 */
	@Test
	public void meldingMetEnkelBpvInschrijvingToevoegingEnVerwijderingVerdichtNaarGeenMelding()
			throws Exception
	{
		getDeelnemer1021();

		deelnemer.rollback();
		deelnemer.setBronStatus(BronEntiteitStatus.Goedgekeurd);
		verbintenis.setBronStatus(BronEntiteitStatus.Goedgekeurd);

		addChange(bpvInschrijving, "status", null, BPVStatus.Definitief);

		controller.save();

		int aantalMeldingen = aantalInTransactie(BronAanleverMelding.class);
		assertThat(aantalMeldingen, is(1));

		BronAanleverMelding melding = getEersteMelding();

		assertThat(getRecordTypes(melding), is(equalTo(asList(305, 322))));
		assertThat(tester.getTransactionLog(), is(asList(insert(melding))));

		assertThat(melding.getBronMeldingStatus(), is(equalTo(BronMeldingStatus.WACHTRIJ)));

		BpvGegevensRecord record = melding.getRecord(BpvGegevensRecord.class);

		// creeer een nieuwe controller
		controller = new BronController();

		bpvInschrijving.setStatus(BPVStatus.Voorlopig);
		addChange(bpvInschrijving, "status", BPVStatus.Definitief, BPVStatus.Voorlopig);

		controller.save();

		assertThat(tester.getTransactionLog(), is(asList(insert(melding), delete(record),
			delete(melding))));

		assertThat(deelnemer.getBronStatus(), is(BronEntiteitStatus.Goedgekeurd));
		assertThat(verbintenis.getBronStatus(), is(BronEntiteitStatus.Goedgekeurd));
	}

	@Test
	public void boPersoonsgegevensEnInschrijvingToevoegingVerdichtNaarGeenMeldingBijVerwijderingInschrijving()
			throws Exception
	{
		getDeelnemer1005();

		addChange(verbintenis, "status", null, Volledig);

		controller.save();

		assertThat(deelnemer.getBronStatus(), is(BronEntiteitStatus.Wachtrij));
		assertThat(verbintenis.getBronStatus(), is(BronEntiteitStatus.Wachtrij));

		BronAanleverMelding melding = getEersteMelding();
		PersoonsgegevensRecord record = melding.getRecord(PersoonsgegevensRecord.class);

		assertThat(getRecordTypes(melding), is(equalTo(asList(305, 310, 320, 321))));
		assertThat(tester.getTransactionLog(), is(asList(insert(melding), update(record))));

		// creeer een nieuwe controller
		controller = new BronController();

		addChange(verbintenis, "status", Volledig, Voorlopig);

		controller.save();
		assertThat(tester.getTransactionLog(), is(asList(insert(melding), update(record),
			delete(melding))));

		assertThat(deelnemer.getBronStatus(), is(BronEntiteitStatus.Geen));
		assertThat(verbintenis.getBronStatus(), is(BronEntiteitStatus.Geen));
	}

	@Test
	public void boGeenPersoonsgegevensRecordBijBpvInschrijvingVoorVolledigeBoInschrijving()
			throws Exception
	{
		getDeelnemer1022();

		deelnemer.setOnderwijsnummer(null);
		deelnemer.getPersoon().setBsn(null);
		deelnemer.rollback();

		verbintenis.setStatus(Volledig);

		addChange(bpvInschrijving, "status", null, BPVStatus.Volledig);
		controller.save();

		int aantalMeldingen = aantalInTransactie(BronAanleverMelding.class);
		assertThat(aantalMeldingen, is(1));

		BronAanleverMelding melding = getEersteMelding();

		assertThat(getRecordTypes(melding), is(equalTo(asList(305, 322))));
		assertThat(tester.getTransactionLog(), is(asList(insert(melding))));
		assertThat(melding.getSofinummer(), is(nullValue()));
		assertThat(melding.getOnderwijsnummer(), is(nullValue()));

		BpvGegevensRecord record = melding.getRecord(BpvGegevensRecord.class);

		// creeer een nieuwe controller
		controller = new BronController();

		addChange(bpvInschrijving, "status", BPVStatus.Volledig, BPVStatus.Voorlopig);

		controller.save();
		assertThat(tester.getTransactionLog(), is(asList(insert(melding), delete(record),
			delete(melding))));
	}

	@Test
	public void boBpvInschrijvingZonderPgnBijVolledigeBoInschrijvingKrijgtBsnNaVerdichting()
			throws Exception
	{
		getDeelnemer1022();

		deelnemer.setOnderwijsnummer(null);
		deelnemer.getPersoon().setBsn(null);
		deelnemer.rollback();

		verbintenis.setStatus(Volledig);

		addChange(bpvInschrijving, "status", null, BPVStatus.Volledig);
		controller.save();

		BronAanleverMelding melding = getEersteMelding();

		assertThat(getRecordTypes(melding), is(equalTo(asList(305, 322))));
		assertThat(tester.getTransactionLog(), is(asList(insert(melding))));
		assertThat(melding.getSofinummer(), is(nullValue()));
		assertThat(melding.getOnderwijsnummer(), is(nullValue()));

		assertThat(tester.getTransactionLog(), is(asList(insert(melding))));

		// creeer een nieuwe controller
		controller = new BronController();

		deelnemer.setOnderwijsnummer(bsn);
		addChange(deelnemer, "onderwijsnummer", null, bsn);

		controller.save();

		assertThat(tester.getTransactionLog(), is(asList(insert(melding), update(melding))));
		assertThat(melding.getOnderwijsnummer(), is(String.valueOf(bsn)));
	}

	/**
	 * Controleert of het opslaan van een verbintenis van een deelnemer zonder PGN
	 * resulteert in een sequentie van 305, 310, 320, 321, waarbij het 305 record geen PGN
	 * ingevuld heeft. Als dan de deelnemer wel wordt voorzien van een BSN, dient het 310
	 * record verwijderd te worden en het 305 record voorzien te worden van het BSN.
	 */
	@Test
	public void boInschrijvingZonderBsnWijzigingen() throws Exception
	{
		getDeelnemer1001();
		deelnemer.getPersoon().setBsn(null);
		addChange(verbintenis, "status", Voorlopig, Definitief);

		controller.save();

		BronAanleverMelding melding = getEersteMelding();
		PersoonsgegevensRecord record = melding.getRecord(PersoonsgegevensRecord.class);

		assertThat(tester.getTransactionLog(), is(asList(insert(melding), update(record))));

		assertThat(melding.getBronMeldingStatus(), is(equalTo(BronMeldingStatus.WACHTRIJ)));

		assertThat(getRecordTypes(melding), is(equalTo(asList(305, 310, 320, 321))));
		assertThat(getMutaties(melding), is(equalTo(asList(null, Toevoeging, Toevoeging))));
		assertThat(tester.getTransactionLog(), is(asList(insert(melding), update(record))));
		assertThat(melding.getSofinummer(), is(nullValue()));

		// creeer een nieuwe controller
		controller = new BronController();

		deelnemer.getPersoon().setBsn(bsn);
		addChange(deelnemer.getPersoon(), "bsn", null, bsn);

		controller.save();

		assertThat(tester.getTransactionLog(), is(asList(insert(melding), update(record),
			update(record), delete(record), update(melding))));

		assertThat(getRecordTypes(melding), is(equalTo(asList(305, 320, 321))));
		assertThat(getMutaties(melding), is(equalTo(asList(Toevoeging, Toevoeging))));
		assertThat(melding.getSofinummer(), is(equalTo(String.valueOf(bsn))));
	}

	/**
	 * Controleert of een personalia wijziging ook wordt doorgevoerd in bestaande
	 * persoonsgegevens records in de wachtrij.
	 */
	@Test
	public void boInschrijvingZonderBsnKrijgtGewijzigdeNaam() throws Exception
	{
		getDeelnemer1001();
		deelnemer.getPersoon().setBsn(null);
		addChange(verbintenis, "status", Voorlopig, Definitief);

		controller.save();

		BronAanleverMelding melding = getEersteMelding();
		PersoonsgegevensRecord record = melding.getRecord(PersoonsgegevensRecord.class);

		assertThat(tester.getTransactionLog(), is(asList(insert(melding), update(record))));

		assertThat(melding.getBronMeldingStatus(), is(equalTo(BronMeldingStatus.WACHTRIJ)));

		assertThat(getRecordTypes(melding), is(equalTo(asList(305, 310, 320, 321))));
		assertThat(getMutaties(melding), is(equalTo(asList(null, Toevoeging, Toevoeging))));
		assertThat(melding.getSofinummer(), is(nullValue()));

		// creeer een nieuwe controller
		controller = new BronController();

		String vorigeAchternaam = deelnemer.getPersoon().getOfficieleAchternaam();
		assertThat(record.getAchternaam(), is(vorigeAchternaam));

		String nieuweAchternaam = "AchternaamOfficieel";
		deelnemer.getPersoon().setOfficieleAchternaam(nieuweAchternaam);
		addChange(deelnemer.getPersoon(), "officieleAchternaam", vorigeAchternaam, nieuweAchternaam);

		controller.save();

		assertThat(tester.getTransactionLog(), is(asList(insert(melding), update(record),
			update(record))));

		assertThat(getRecordTypes(melding), is(equalTo(asList(305, 310, 320, 321))));
		assertThat(getMutaties(melding), is(equalTo(asList(null, Toevoeging, Toevoeging))));
		assertThat(record.getAchternaam(), is(nieuweAchternaam));
	}

	/**
	 * Als er onverhoopt met een verkeerd BSN gecommuniceerd is, of er een fout tijdens de
	 * invoer gemaakt is met het BSN, dan dient de wijziging BSN->BSN2 alle meldingen in
	 * de wachtrij voor die deelnemer aan te passen en te voorzien van het nieuwe BSN.
	 */
	@Test
	public void bsnWijzigingPastAlleMeldingenInWachtrijAan() throws Exception
	{
		getDeelnemer1001();
		deelnemer.getPersoon().setBsn(bsn);
		addChange(verbintenis, "status", Voorlopig, Definitief);

		controller.save();

		BronAanleverMelding melding = getEersteMelding();

		assertThat(tester.getTransactionLog(), is(asList(insert(melding))));
		assertThat(melding.getBronMeldingStatus(), is(equalTo(BronMeldingStatus.WACHTRIJ)));

		assertThat(getRecordTypes(melding), is(equalTo(asList(305, 320, 321))));
		assertThat(getMutaties(melding), is(equalTo(asList(Toevoeging, Toevoeging))));

		assertThat(tester.getTransactionLog(), is(asList(insert(melding))));
		assertThat(melding.getSofinummer(), is(String.valueOf(bsn)));

		// creeer een nieuwe controller
		controller = new BronController();

		deelnemer.getPersoon().setBsn(bsn2);
		addChange(deelnemer.getPersoon(), "bsn", bsn, bsn2);

		controller.save();

		assertThat(tester.getTransactionLog(), is(asList(insert(melding), update(melding))));
		assertThat(melding.getSofinummer(), is(equalTo(String.valueOf(bsn2))));
	}

	@Test
	public void toevoegingEnVerwijderingExamenmeldingVerdichtNaarDelete() throws Exception
	{
		getDeelnemer1001();

		Examendeelname examendeelname = addExamendeelname(verbintenis, 20091231);
		addChange(examendeelname, "examenstatus", null, examendeelname.getExamenstatus());
		addChange(examendeelname, "examenjaar", null, examendeelname.getExamenjaar());

		controller.save();

		BronAanleverMelding melding = getEersteMelding();

		ExamengegevensRecord record = melding.getRecord(ExamengegevensRecord.class);

		assertThat(tester.getTransactionLog(), is(asList(insert(melding))));
		assertThat(melding.getBronMeldingStatus(), is(equalTo(BronMeldingStatus.WACHTRIJ)));

		assertThat(getRecordTypes(melding), is(equalTo(asList(305, 323))));
		assertThat(getMutaties(melding), is(equalTo(asList(Toevoeging))));

		// creeer een nieuwe controller
		controller = new BronController();
		Examenstatus vorige = examendeelname.getExamenstatus();
		Examenstatus nieuwe = getVerwijderd();
		examendeelname.setExamenstatus(nieuwe);

		addChange(examendeelname, "examenstatus", vorige, nieuwe);
		controller.save();

		assertThat(tester.getTransactionLog(), is(asList(insert(melding), delete(record),
			delete(melding))));
	}

	@Test
	public void verwijderingEnToevoegingExamenmeldingVerdichtNaarAanpassing() throws Exception
	{
		getDeelnemer1001();

		Examendeelname examendeelname = addExamendeelname(verbintenis, 20091231);
		Examenstatus vorige = examendeelname.getExamenstatus();
		examendeelname.setExamenstatus(getVerwijderd());
		addChange(examendeelname, "examenstatus", vorige, examendeelname.getExamenstatus());

		controller.save();

		BronAanleverMelding melding = getEersteMelding();

		assertThat(tester.getTransactionLog(), is(asList(insert(melding))));
		assertThat(melding.getBronMeldingStatus(), is(equalTo(BronMeldingStatus.WACHTRIJ)));

		assertThat(getRecordTypes(melding), is(equalTo(asList(305, 323))));
		assertThat(getMutaties(melding), is(equalTo(asList(Verwijdering))));

		// creeer een nieuwe controller
		controller = new BronController();

		vorige = examendeelname.getExamenstatus();
		Examenstatus nieuwe = getGeslaagd();
		examendeelname.setExamenstatus(nieuwe);

		addChange(examendeelname, "examenstatus", vorige, nieuwe);
		controller.save();

		assertThat(tester.getTransactionLog(), is(asList(insert(melding), update(melding))));
		assertThat(getMutaties(melding), is(equalTo(asList(Aanpassing))));
	}

	/**
	 * Deelnemer 41314 heeft een verbintenis die ik niet meteen kan beëindigen omdat het
	 * bedrijf en de code leerbedrijf niet gevuld zijn in de conversie (i.v.m. melding
	 * 3300). Ik zet dus eerst de bpv naar voorlopig, zorg dat het bedrijf gevuld wordt,
	 * daarna ga ik door naar definitief en beëindig dan. In de BRON melding zie ik dat
	 * een verwijdering opgestuurd gaat worden. In de log bovenin zie ik wel alles keurig
	 * staan. Ik hoop dat de weergave niet goed is en dat uiteindelijk wel de beëindiging
	 * netjes opgestuurd wordt, maar het lijkt nu in elk geval dat een verwijdering
	 * gestuurd gaat worden en dat is niet de bedoeling! Ook zonder fout 3300 zou het voor
	 * kunnen komen dat je een bpv terug zet naar voorlopig, iets wijzigt en dan doorzet
	 * naar definitief of beëindigd.
	 * 
	 * Ondanks het fiet dat er later mutaties zijn op een verbintenis BPV, wordt de
	 * verwijdering klaargezet voor BRON. De andere mutaties zijn in het BRON-tabblad niet
	 * terug te vinden.
	 */
	@Test
	public void mantis60009() throws Exception
	{
		getDeelnemer1021();

		deelnemer.rollback();

		bpvInschrijving.setStatus(BPVStatus.Definitief);

		addChange(bpvInschrijving, "status", BPVStatus.Definitief, BPVStatus.Voorlopig);

		controller.save();

		int aantalMeldingen = aantalInTransactie(BronAanleverMelding.class);
		assertThat(aantalMeldingen, is(1));

		BronAanleverMelding melding = getEersteMelding();

		assertThat(tester.getTransactionLog(), is(asList(insert(melding))));
		assertThat(bpvInschrijving.getBronStatus(), is(BronEntiteitStatus.Wachtrij));

		assertThat(melding.getBronMeldingStatus(), is(equalTo(BronMeldingStatus.WACHTRIJ)));
		assertThat(getRecordTypes(melding), is(equalTo(asList(305, 322))));
		assertThat(getMutaties(melding), is(asList(Verwijdering)));

		// creeer een nieuwe controller
		controller = new BronController();

		bpvInschrijving.setStatus(BPVStatus.Volledig);
		addChange(bpvInschrijving, "status", BPVStatus.Voorlopig, BPVStatus.Volledig);

		controller.save();

		assertThat(tester.getTransactionLog(), is(asList(insert(melding), update(melding))));

		assertThat(getRecordTypes(melding), is(equalTo(asList(305, 322))));

		assertThat(getMutaties(melding), is(asList(Aanpassing)));

		assertThat(bpvInschrijving.getBronStatus(), is(BronEntiteitStatus.Wachtrij));
	}

	/**
	 * Controleert of een wijziging van een personalia kenmerk in de wachtrij doorgevoerd
	 * wordt voor toevoegingen.
	 */
	@Test
	public void mantis60647wijzigingGeslacht() throws Exception
	{
		getDeelnemer1001();

		verbintenis.setStatus(Volledig);
		addChange(verbintenis, "status", Voorlopig, Volledig);

		controller.save();

		assertThat(getAantalInTransactie(BronAanleverMelding.class), is(1));
		BronAanleverMelding melding = getEersteMelding();

		assertThat(melding.getGeboortedatum(), is(equalTo(Datum.valueOf(19910501))));
		assertThat(melding.getGeslacht(), is(equalTo(Man)));

		controller = new BronController();
		deelnemer.getPersoon().setGeslacht(Vrouw);
		addChange(deelnemer.getPersoon(), "geslacht", Man, Vrouw);

		controller.save();

		assertThat(getAantalInTransactie(BronAanleverMelding.class), is(1));

		assertThat(melding.getGeboortedatum(), is(equalTo(Datum.valueOf(19910501))));
		assertThat(melding.getGeslacht(), is(equalTo(Vrouw)));
	}

	/**
	 * Controleert of een wijziging van een personalia kenmerk in de wachtrij doorgevoerd
	 * wordt voor toevoegingen.
	 */
	@Test
	public void mantis60647wijzigingGeboortedatum() throws Exception
	{
		getDeelnemer1001();

		verbintenis.setStatus(Volledig);
		addChange(verbintenis, "status", Voorlopig, Volledig);

		controller.save();

		assertThat(getAantalInTransactie(BronAanleverMelding.class), is(1));
		BronAanleverMelding melding = getEersteMelding();

		assertThat(melding.getGeboortedatum(), is(equalTo(Datum.valueOf(19910501))));
		assertThat(melding.getGeslacht(), is(equalTo(Man)));

		controller = new BronController();
		deelnemer.getPersoon().setGeboortedatum(asDate(19900101));
		addChange(deelnemer.getPersoon(), "geboortedatum", asDate(19910501), deelnemer.getPersoon()
			.getGeboortedatum());

		controller.save();

		assertThat(getAantalInTransactie(BronAanleverMelding.class), is(1));

		assertThat(melding.getGeboortedatum(), is(equalTo(Datum.valueOf(19900101))));
		assertThat(melding.getGeslacht(), is(equalTo(Man)));
	}

	/**
	 * Controleert of een wijziging van een personalia kenmerk in de wachtrij doorgevoerd
	 * wordt voor toevoegingen.
	 */
	@Test
	public void mantis60647wijzigingGeslachtDeelnemerBekendBijBronLevert306Op() throws Exception
	{
		getDeelnemer1001();

		deelnemer.getPersoon().setBsn(null);
		deelnemer.setDeelnemernummer(123456789);
		verbintenis.setStatus(Volledig);
		deelnemer.setBronStatus(BronEntiteitStatus.Goedgekeurd);

		addChange(verbintenis, "status", Volledig, Definitief);
		addChange(verbintenis, "begindatum", Schooljaar.huidigSchooljaar().getBegindatum(),
			Schooljaar.huidigSchooljaar().getEenOktober());

		controller.save();

		assertThat(getAantalInTransactie(BronAanleverMelding.class), is(1));
		BronAanleverMelding melding = getEersteMelding();

		assertThat(getRecordTypes(melding), is(Arrays.asList(305, 320, 321, 321)));
		assertThat(melding.getGeboortedatum(), is(equalTo(Datum.valueOf(19910501))));
		assertThat(melding.getGeslacht(), is(equalTo(Man)));

		controller = new BronController();

		deelnemer.getPersoon().setGeslacht(Vrouw);
		deelnemer.getPersoon().setGeboortedatum(asDate(19900101));

		addChange(deelnemer.getPersoon(), "geslacht", Man, Vrouw);
		addChange(deelnemer.getPersoon(), "geboortedatum", asDate(19910501), asDate(19900101));

		controller.save();

		assertThat(getAantalInTransactie(BronAanleverMelding.class), is(2));

		assertThat(melding.getGeboortedatum(), is(equalTo(Datum.valueOf(19910501))));
		assertThat(melding.getGeslacht(), is(equalTo(Man)));

		BronAanleverMelding tweedeMelding = getTweedeMelding();
		assertThat(getRecordTypes(tweedeMelding), is(Arrays.asList(305, 306)));
		assertThat(tweedeMelding.getGeboortedatum(), is(equalTo(Datum.valueOf(19910501))));
		assertThat(tweedeMelding.getGeslacht(), is(equalTo(Man)));
	}

	/**
	 * Controleer of een verwijdering gevolgd door een toevoeging niet verdicht wordt, en
	 * dat een daar opvolgende verwijdering de toevoeging ongedaan maakt in de wachtrij.
	 * 
	 * @see <a href="http://bugs.topicus.nl/view.php?id=61737">#0061737</a>
	 */
	@Test
	public void mantis61737verwijderingenNietVerdichten() throws Exception
	{
		getDeelnemer1001();

		verbintenis.setStatus(Voorlopig);
		addChange(verbintenis, "status", Volledig, verbintenis.getStatus());

		controller.save();

		assertThat(getAantalInTransactie(BronAanleverMelding.class), is(1));
		BronAanleverMelding melding = getEersteMelding();

		assertThat(getRecordTypes(melding), is(Arrays.asList(305, 320)));
		assertThat(getMutaties(melding), is(Arrays.asList(Verwijdering)));

		controller = new BronController();

		verbintenis.setStatus(Volledig);

		addChange(verbintenis, "status", Voorlopig, verbintenis.getStatus());
		controller.save();

		assertThat(getAantalInTransactie(BronAanleverMelding.class), is(2));

		assertThat(melding, is(equalTo(getEersteMelding())));

		assertThat(getRecordTypes(melding), is(Arrays.asList(305, 320)));
		assertThat(getMutaties(melding), is(Arrays.asList(Verwijdering)));

		BronAanleverMelding melding2 = getTweedeMelding();

		assertThat(getRecordTypes(melding2), is(Arrays.asList(305, 320, 321)));
		assertThat(getMutaties(melding2), is(Arrays.asList(Toevoeging, Toevoeging)));

		controller = new BronController();

		verbintenis.setStatus(Voorlopig);
		addChange(verbintenis, "status", Volledig, verbintenis.getStatus());

		controller.save();

		assertThat(getAantalInTransactie(BronAanleverMelding.class), is(1));

		assertThat(melding, is(equalTo(getEersteMelding())));

		assertThat(getRecordTypes(melding), is(Arrays.asList(305, 320)));
		assertThat(getMutaties(melding), is(Arrays.asList(Verwijdering)));
	}

	/**
	 * Wanneer ik een verbintenis van definitief naar voorlopig zet, wordt er een
	 * Verwijdering van de verbintenis en een aanpassing van de bekostigingsperiode
	 * richting BRON.
	 * 
	 * Dit klopt niet: Want wanneer de verbintenis is verwijderd, wordt de aanpassing van
	 * de bekostiging niet geaccepteerd door BRON.
	 * 
	 * Wanneer ik daarna de verbinteris weer volledig maak, wordt er geen melding gemaakt
	 * richting BRON. Dit is een ZEER GROOT PROBLEEM, want dan klopt de KRD database, en
	 * de BRON database niet meer met elkaar. Dit is iets waardoor de overgang naar het
	 * KRD voor mij niet door kan gaan!!!!
	 * 
	 * @see <a href="http://bugs.topicus.nl/view.php?id=52488">#0052488</a>
	 */
	@Test
	public void mantis52488() throws Exception
	{
		getDeelnemer1001();
		verbintenis.setBekostigd(Nee);

		addChange(verbintenis, "bekostigd", Ja, Nee);

		controller.save();

		BronAanleverMelding melding = getEersteMelding();
		assertThat(tester.getTransactionLog(), is(asList(insert(melding))));
		assertThat(melding.getBronMeldingStatus(), is(equalTo(BronMeldingStatus.WACHTRIJ)));

		assertThat(getRecordTypes(melding), is(equalTo(asList(305, 321))));
		assertThat(getMutaties(melding), is(equalTo(asList(Aanpassing))));

		controller = new BronController();

		verbintenis.setStatus(Voorlopig);
		addChange(verbintenis, "status", Definitief, Voorlopig);

		controller.save();

		assertThat(getRecordTypes(melding), is(equalTo(asList(305, 320))));
		assertThat(getMutaties(melding), is(equalTo(asList(Verwijdering))));

		controller = new BronController();

		verbintenis.setStatus(Volledig);
		addChange(verbintenis, "status", Voorlopig, Volledig);

		controller.save();

		assertThat(getRecordTypes(melding), is(equalTo(asList(305, 320))));
		assertThat(getMutaties(melding), is(equalTo(asList(Verwijdering))));

		BronAanleverMelding melding2 = getTweedeMelding();

		assertThat(getRecordTypes(melding2), is(equalTo(asList(305, 320, 321))));
		assertThat(getMutaties(melding2), is(equalTo(asList(Toevoeging, Toevoeging))));
	}

	/**
	 * 
	 */
	@Test
	public void mantis62492_verbintenis2() throws Exception
	{
		getDeelnemer1002();
		addChange(verbintenis, "status", Voorlopig, Definitief);
		controller.save();

		BronAanleverMelding melding = getEersteMelding();
		assertThat(tester.getTransactionLog(), is(asList(insert(melding))));
		assertThat(melding.getBronMeldingStatus(), is(equalTo(BronMeldingStatus.WACHTRIJ)));

		assertThat(getRecordTypes(melding), is(equalTo(asList(305, 320, 321, 321))));
		assertThat(getMutaties(melding), is(equalTo(asList(Toevoeging, Toevoeging, Toevoeging))));
	}
}
