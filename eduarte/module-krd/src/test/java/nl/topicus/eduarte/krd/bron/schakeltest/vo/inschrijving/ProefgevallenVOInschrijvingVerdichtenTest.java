package nl.topicus.eduarte.krd.bron.schakeltest.vo.inschrijving;

import static java.util.Arrays.*;
import static nl.topicus.eduarte.entities.BronEntiteitStatus.*;
import static nl.topicus.eduarte.entities.BronMeldingOnderdeel.*;
import static nl.topicus.eduarte.entities.inschrijving.Verbintenis.VerbintenisStatus.*;
import static nl.topicus.eduarte.krd.entities.bron.BronMeldingStatus.*;
import static nl.topicus.eduarte.tester.hibernate.DatabaseAction.*;
import static nl.topicus.onderwijs.duo.bron.vo.waardelijsten.SoortMutatie.*;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.util.Date;

import nl.topicus.eduarte.entities.inschrijving.Plaatsing;
import nl.topicus.eduarte.krd.bron.BronController;
import nl.topicus.eduarte.krd.entities.bron.meldingen.aanlevering.BronInschrijvingsgegevensVOMelding;
import nl.topicus.eduarte.krd.entities.bron.meldingen.aanlevering.BronInschrijvingsgegevensVOMelding.VoMeldingSoort;

import org.junit.Ignore;
import org.junit.Test;

public class ProefgevallenVOInschrijvingVerdichtenTest extends ProefgevallenVOInschrijving
{
	/**
	 * Test of het verdichten van de wijziging van de begindatum van een verbintenis
	 * waarbij een Toevoeging in de wachtrij staat resulteert in het aanpassen van de
	 * Toevoeging, in plaats van het aanmaken van een Aanpassing (die dan fout gaat,
	 * aangezien je de begindatum niet mag aanpassen)
	 */
	@Test
	public void verdichtenVanBegindatumWijzigingMetToevoegingInWachtrijPastDatumAan()
			throws Exception
	{
		getDeelnemer1001();
		addChange(verbintenis, "status", Voorlopig, Definitief);

		controller.save();

		BronInschrijvingsgegevensVOMelding melding = getEersteBronInschrijvingsgegevensVOMelding();

		assertThat(melding.getBronMeldingStatus(), is(WACHTRIJ));
		assertThat(melding.getSoortMutatie(), is(Toevoeging));
		assertThat(melding.getSoort(), is(VoMeldingSoort.I));

		assertThat(melding.getBronMeldingOnderdelen(), is(asList(VOInschrijving)));
		assertThat(melding.getIngangsDatum(), is(verbintenis.getBegindatum()));

		// start een nieuw request
		controller = new BronController();

		Date begindatum = verbintenis.getBegindatum();
		verbintenis.setBegindatum(asDate(20090801));
		addChange(verbintenis, "begindatum", begindatum, verbintenis.getBegindatum());

		controller.save();

		assertThat(tester.getTransactionLog(), is(asList(insert(melding), update(melding))));
		assertThat(melding.getIngangsDatum(), is(verbintenis.getBegindatum()));

		assertThat(melding.getSoortMutatie(), is(Toevoeging));
		assertThat(melding.getSoort(), is(VoMeldingSoort.I));

		assertThat(verbintenis.getBronStatus(), is(Wachtrij));
	}

	/**
	 * Controleert of het aanpassen van de begindatum van een verbintenis met een
	 * Aanpassing in de wachtrij resulteert in het veranderen van de Aanpassing in een
	 * Verwijdering en een Toevoeging van de verbintenis: de bestaande verbintenis moet
	 * uit BRON verwijderd worden, en een nieuwe toegevoegd met de aangepaste
	 * ingangsdatum.
	 */
	@Test
	public void verdichtenVanBegindatumWijzigingMetAanpassingInWachtrijVerwijdertEnVoegtToe()
			throws Exception
	{
		getDeelnemer1001();
		verbintenis.setStatus(Definitief);
		verbintenis.setBronStatus(Goedgekeurd);

		plaatsing.setLeerjaar(2);
		addChange(plaatsing, "leerjaar", 3, 2);

		controller.save();

		BronInschrijvingsgegevensVOMelding melding = getEersteBronInschrijvingsgegevensVOMelding();

		assertThat(melding.getBronMeldingStatus(), is(equalTo(WACHTRIJ)));
		assertThat(melding.getSoort(), is(equalTo(VoMeldingSoort.I)));

		assertThat(melding.getBronMeldingOnderdelen(), is(asList(VOInschrijving)));
		assertThat(melding.getSoortMutatie(), is(Aanpassing));
		assertThat(melding.getIngangsDatum(), is(verbintenis.getBegindatum()));

		// creeer een nieuwe controller
		controller = new BronController();

		Date begindatum = verbintenis.getBegindatum();
		verbintenis.setBegindatum(asDate(20090801));
		addChange(verbintenis, "begindatum", begindatum, verbintenis.getBegindatum());

		controller.save();

		BronInschrijvingsgegevensVOMelding melding2 = getTweedeBronInschrijvingsgegevensVOMelding();

		assertThat(tester.getTransactionLog(), is(asList(insert(melding), update(melding),
			insert(melding2))));
		assertThat(melding.getSoortMutatie(), is(Verwijdering));
		assertThat(melding.getIngangsDatum(), is(asDate(20080730)));
		assertThat(melding.getILTCode(), is(nullValue()));
		assertThat(melding.getLeerjaar(), is(nullValue()));
		assertThat(melding.getJarenPraktijkOnderwijs(), is(nullValue()));

		assertThat(melding2.getSoortMutatie(), is(Toevoeging));
		assertThat(melding2.getIngangsDatum(), is(verbintenis.getBegindatum()));
		assertThat(melding2.getLeerjaar(), is(2));
		assertThat(melding2.getILTCode(), is(2781));

		assertThat(verbintenis.getBronStatus(), is(WachtrijWelInBron));
	}

	/**
	 * Controleert of het aanpassen van de begindatum van een verbintenis die al aangemeld
	 * is bij BRON zonder dat er een melding in de wachtrij staat resulteert in twee
	 * meldingen: een verwijdering van de bestaande verbintenis uit BRON en een toevoeging
	 * van de verbintenis met de nieuwe ingangsdatum.
	 */
	@Test
	public void verdichtenVanBegindatumZonderMeldingInWachtrijVerwijdertEnVoegtToe()
			throws Exception
	{
		getDeelnemer1001();
		verbintenis.setStatus(Definitief);
		verbintenis.setBronStatus(Goedgekeurd);
		controller.save();

		Date begindatum = verbintenis.getBegindatum();
		verbintenis.setBegindatum(asDate(20090801));
		addChange(verbintenis, "begindatum", begindatum, verbintenis.getBegindatum());

		controller.save();

		BronInschrijvingsgegevensVOMelding melding1 = getEersteBronInschrijvingsgegevensVOMelding();
		BronInschrijvingsgegevensVOMelding melding2 = getTweedeBronInschrijvingsgegevensVOMelding();

		assertThat(tester.getTransactionLog(), is(asList(insert(melding1), insert(melding2))));
		assertThat(melding1.getIngangsDatum(), is(asDate(20080730)));
		assertThat(melding1.getSoortMutatie(), is(Verwijdering));
		assertThat(melding2.getIngangsDatum(), is(asDate(20090801)));
		assertThat(melding2.getSoortMutatie(), is(Toevoeging));

		assertThat(verbintenis.getBronStatus(), is(WachtrijWelInBron));
	}

	/**
	 * Controleert of het aanpassen van de begindatum van een verbintenis die al aangemeld
	 * is bij BRON waarbij er een verwijder melding in de wachtrij staat niets doet: de
	 * ingangsdatum van de verwijdering van de melding zou de inschrijving moeten
	 * identificeren in BRON.
	 */
	@Test
	public void verdichtenVanBegindatumMetVerwijderingInWachtrijDoetNiets() throws Exception
	{
		getDeelnemer1001();
		verbintenis.setStatus(Afgemeld);
		verbintenis.setBronStatus(Goedgekeurd);
		addChange(verbintenis, "status", Definitief, Afgemeld);

		// trigger de verwijder melding
		controller.save();

		BronInschrijvingsgegevensVOMelding melding = getEersteBronInschrijvingsgegevensVOMelding();

		assertThat(melding.getBronMeldingStatus(), is(WACHTRIJ));
		assertThat(melding.getSoort(), is(VoMeldingSoort.I));

		assertThat(melding.getBronMeldingOnderdelen(), is(asList(VOInschrijving)));
		assertThat(melding.getSoortMutatie(), is(Verwijdering));
		assertThat(melding.getIngangsDatum(), is(verbintenis.getBegindatum()));

		// start een nieuw request
		controller = new BronController();

		Date begindatum = verbintenis.getBegindatum();
		verbintenis.setBegindatum(asDate(20090801));
		addChange(verbintenis, "begindatum", begindatum, verbintenis.getBegindatum());

		controller.save();

		assertThat(tester.getTransactionLog(), is(asList(insert(melding))));
		assertThat(melding.getIngangsDatum(), is(asDate(20080730)));
		assertThat(verbintenis.getBronStatus(), is(WachtrijWelInBron));
	}

	/**
	 * Test of het verdichten van de wijziging van de achternaam van een deelnemer waarbij
	 * een Toevoeging in de wachtrij staat resulteert in het aanpassen van de Toevoeging,
	 * in plaats van het aanmaken van een Aanpassing.
	 */
	@Test
	public void verdichtenVanAchternaamWijzigingMetToevoegingInWachtrijGebruiktNieuweNaam()
			throws Exception
	{
		getDeelnemer1001();
		verbintenis.setBronStatus(Geen);
		deelnemer.setOnderwijsnummer(null);
		deelnemer.getPersoon().setBsn(null);

		addChange(verbintenis, "status", Voorlopig, Definitief);

		controller.save();

		BronInschrijvingsgegevensVOMelding melding = getEersteBronInschrijvingsgegevensVOMelding();

		assertThat(verbintenis.getBronStatus(), is(Wachtrij));
		assertThat(melding.getBronMeldingStatus(), is(WACHTRIJ));
		assertThat(melding.getSoortMutatie(), is(Toevoeging));
		assertThat(melding.getSoort(), is(VoMeldingSoort.I));

		assertThat(melding.getBronMeldingOnderdelen(), is(asList(VOInschrijving)));
		assertThat(melding.getIngangsDatum(), is(verbintenis.getBegindatum()));
		assertThat(melding.getAchternaam(), is(""));

		// start een nieuw request
		controller = new BronController();

		deelnemer.getPersoon().setOfficieleAchternaam("Gifkikker");
		addChange(deelnemer.getPersoon(), "officieleAchternaam", "", "Gifkikker");

		controller.save();

		assertThat(tester.getTransactionLog(), is(asList(insert(melding), update(melding))));
		assertThat(melding.getIngangsDatum(), is(verbintenis.getBegindatum()));
		assertThat(melding.getAchternaam(), is("Gifkikker"));
		assertThat(verbintenis.getBronStatus(), is(Wachtrij));
	}

	@Test
	public void wijzigingBegindatumPlaatsingVerwijdertEnPastAan() throws Exception
	{
		getDeelnemer1001();
		verbintenis.setStatus(Definitief);
		verbintenis.setBronStatus(Goedgekeurd);

		Plaatsing leerjaar4 = getPlaatsing(4, verbintenis);
		leerjaar4.setBegindatum(asDate(20081130));
		verbintenis.getPlaatsingen().add(0, leerjaar4);

		Plaatsing leerjaar5 = getPlaatsing(5, verbintenis);
		leerjaar5.setBegindatum(asDate(20100801));
		verbintenis.getPlaatsingen().add(0, leerjaar5);

		addChange(leerjaar4, "begindatum", null, leerjaar4.getBegindatum());
		addChange(leerjaar5, "begindatum", null, leerjaar5.getBegindatum());

		controller.save();

		BronInschrijvingsgegevensVOMelding meldingLeerjaar4 =
			getEersteBronInschrijvingsgegevensVOMelding();
		BronInschrijvingsgegevensVOMelding meldingLeerjaar5 =
			getTweedeBronInschrijvingsgegevensVOMelding();

		assertThat(tester.getTransactionLog(), is(asList(insert(meldingLeerjaar4),
			insert(meldingLeerjaar5))));
		assertThat(meldingLeerjaar4.getSoortMutatie(), is(Aanpassing));
		assertThat(meldingLeerjaar5.getSoortMutatie(), is(Aanpassing));

		controller = new BronController();

		leerjaar4.setBegindatum(asDate(20090801));
		addChange(leerjaar4, "begindatum", asDate(20081130), leerjaar4.getBegindatum());

		controller.save();

		BronInschrijvingsgegevensVOMelding meldingLeerjaar4b =
			getDerdeBronInschrijvingsgegevensVOMelding();
		assertThat(tester.getTransactionLog(), is(asList(insert(meldingLeerjaar4),
			insert(meldingLeerjaar5), update(meldingLeerjaar4), insert(meldingLeerjaar4b))));

		assertThat(meldingLeerjaar4.getSoortMutatie(), is(Verwijdering));
		assertThat(meldingLeerjaar5.getSoortMutatie(), is(Aanpassing));
		assertThat(meldingLeerjaar4b.getSoortMutatie(), is(Aanpassing));

	}

	@Test
	public void verwijderenVerbintenisNogNietNaarBronMaaktWachtrijLeeg() throws Exception
	{
		getDeelnemer1001();
		verbintenis.setStatus(Definitief);

		addChange(verbintenis, "status", Voorlopig, Definitief);

		controller.save();
		controller = new BronController();

		BronInschrijvingsgegevensVOMelding melding1 = getEersteBronInschrijvingsgegevensVOMelding();

		assertThat(tester.getTransactionLog(), is(asList(insert(melding1))));

		Plaatsing leerjaar4 = getPlaatsing(4, verbintenis);
		leerjaar4.setBegindatum(asDate(20081130));
		verbintenis.getPlaatsingen().add(0, leerjaar4);

		addChange(leerjaar4, "begindatum", null, leerjaar4.getBegindatum());

		controller.save();
		controller = new BronController();

		BronInschrijvingsgegevensVOMelding melding2 = getTweedeBronInschrijvingsgegevensVOMelding();

		assertThat(tester.getTransactionLog(), is(asList(insert(melding1), insert(melding2))));

		verbintenis.setStatus(Voorlopig);
		addChange(verbintenis, "status", Definitief, Voorlopig);

		controller.save();

		assertThat(tester.getTransactionLog(), is(asList(insert(melding1), insert(melding2),
			delete(melding1), delete(melding2))));
	}

	@Ignore("Aanpassen wachtrij nog correct krijgen")
	@Test
	public void wijzigingOnderwijsnummerPastWachtrijAan() throws Exception
	{
		getDeelnemer1001();
		deelnemer.setOnderwijsnummer(1000000001L);
		deelnemer.getPersoon().setBsn(null);
		verbintenis.setStatus(Definitief);
		verbintenis.setBronStatus(Goedgekeurd);
		controller.save();

		Date begindatum = verbintenis.getBegindatum();
		verbintenis.setBegindatum(asDate(20090801));
		addChange(verbintenis, "begindatum", begindatum, verbintenis.getBegindatum());

		controller.save();

		BronInschrijvingsgegevensVOMelding melding1 = getEersteBronInschrijvingsgegevensVOMelding();
		BronInschrijvingsgegevensVOMelding melding2 = getTweedeBronInschrijvingsgegevensVOMelding();

		assertThat(verbintenis.getBronStatus(), is(WachtrijWelInBron));

		assertThat(melding1.getBronMeldingStatus(), is(WACHTRIJ));
		assertThat(melding1.getSoortMutatie(), is(Verwijdering));
		assertThat(melding1.getSoort(), is(VoMeldingSoort.I));
		assertThat(melding1.getOnderwijsNummer(), is(1000000001));

		assertThat(melding2.getBronMeldingStatus(), is(WACHTRIJ));
		assertThat(melding2.getSoortMutatie(), is(Toevoeging));
		assertThat(melding2.getOnderwijsNummer(), is(1000000001));
		assertThat(melding2.getSoort(), is(VoMeldingSoort.I));

		assertThat(tester.getTransactionLog(), is(asList(insert(melding1), insert(melding2))));

		// start een nieuw request
		controller = new BronController();
		deelnemer.setOnderwijsnummer(1000000002L);
		addChange(deelnemer, "onderwijsnummer", 1000000001L, 1000000002L);
		controller.save();
		assertThat(tester.getTransactionLog(), is(asList(insert(melding1), insert(melding2),
			update(melding1), update(melding2))));

		assertThat(melding1.getOnderwijsNummer(), is(1000000002));
		assertThat(melding2.getOnderwijsNummer(), is(1000000002));
	}

	@Ignore("Aanpassen wachtrij nog correct krijgen")
	@Test
	public void wijzigingSofinummerPastWachtrijAan() throws Exception
	{
		getDeelnemer1001();
		deelnemer.getPersoon().setBsn(1000000001L);
		verbintenis.setStatus(Definitief);
		verbintenis.setBronStatus(Goedgekeurd);
		controller.save();

		Date begindatum = verbintenis.getBegindatum();
		verbintenis.setBegindatum(asDate(20090801));
		addChange(verbintenis, "begindatum", begindatum, verbintenis.getBegindatum());

		controller.save();

		BronInschrijvingsgegevensVOMelding melding1 = getEersteBronInschrijvingsgegevensVOMelding();
		BronInschrijvingsgegevensVOMelding melding2 = getTweedeBronInschrijvingsgegevensVOMelding();

		assertThat(verbintenis.getBronStatus(), is(WachtrijWelInBron));

		assertThat(melding1.getBronMeldingStatus(), is(WACHTRIJ));
		assertThat(melding1.getSoortMutatie(), is(Verwijdering));
		assertThat(melding1.getSoort(), is(VoMeldingSoort.I));
		assertThat(melding1.getSofiNummer(), is(1000000001));

		assertThat(melding2.getBronMeldingStatus(), is(WACHTRIJ));
		assertThat(melding2.getSoortMutatie(), is(Toevoeging));
		assertThat(melding2.getSofiNummer(), is(1000000001));
		assertThat(melding2.getSoort(), is(VoMeldingSoort.I));

		assertThat(tester.getTransactionLog(), is(asList(insert(melding1), insert(melding2))));

		// start een nieuw request
		controller = new BronController();
		deelnemer.getPersoon().setBsn(1000000002L);
		addChange(deelnemer.getPersoon(), "bsn", 1000000001L, 1000000002L);
		controller.save();
		assertThat(tester.getTransactionLog(), is(asList(insert(melding1), insert(melding2),
			update(melding2), update(melding1))));

		assertThat(melding1.getSofiNummer(), is(1000000002));
		assertThat(melding2.getSofiNummer(), is(1000000002));
	}

	@Test
	public void mantis61370() throws Exception
	{
		getDeelnemer1001();

		addChange(verbintenis, "status", Voorlopig, Volledig);

		controller.save();

		BronInschrijvingsgegevensVOMelding melding = getEersteBronInschrijvingsgegevensVOMelding();

		assertThat(melding.getSoortMutatie(), is(Toevoeging));
		assertThat(melding.getLeerjaar(), is(3));

		controller = new BronController();

		plaatsing.setLeerjaar(2);
		addChange(plaatsing, "leerjaar", 3, 2);
		controller.save();

		assertThat(melding.getSoortMutatie(), is(Toevoeging));
		assertThat(melding.getLeerjaar(), is(2));
	}

	@Test
	public void toevoegingGevolgdDoorVerwijderingLevertGeenMeldingOp() throws Exception
	{
		getDeelnemer1001();

		addChange(verbintenis, "status", Voorlopig, Volledig);

		controller.save();

		BronInschrijvingsgegevensVOMelding melding = getEersteBronInschrijvingsgegevensVOMelding();

		assertThat(tester.getTransactionLog(), is(asList(insert(melding))));
		assertThat(melding.getSoortMutatie(), is(Toevoeging));

		controller = new BronController();

		addChange(verbintenis, "status", Volledig, Voorlopig);
		verbintenis.setStatus(Voorlopig);

		controller.save();

		assertThat(melding.isVerwijderd(), is(true));
		assertThat(tester.getTransactionLog(), is(asList(insert(melding), delete(melding))));
	}
}
