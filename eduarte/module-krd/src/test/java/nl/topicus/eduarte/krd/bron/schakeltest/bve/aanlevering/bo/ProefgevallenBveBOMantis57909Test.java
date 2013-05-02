package nl.topicus.eduarte.krd.bron.schakeltest.bve.aanlevering.bo;

import static nl.topicus.eduarte.entities.inschrijving.Verbintenis.VerbintenisStatus.*;
import static nl.topicus.eduarte.krd.bron.BronBpvWijzigingToegestaanCheck.WijzigingToegestaanResultaat.*;
import static nl.topicus.eduarte.krd.entities.bron.BronStatus.*;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.util.Date;

import nl.topicus.eduarte.entities.begineinddatum.IBeginEinddatumEntiteit;
import nl.topicus.eduarte.entities.bpv.BPVBedrijfsgegeven;
import nl.topicus.eduarte.entities.bpv.BPVInschrijving.BPVStatus;
import nl.topicus.eduarte.entities.landelijk.Cohort;
import nl.topicus.eduarte.entities.landelijk.Schooljaar;
import nl.topicus.eduarte.entities.organisatie.ExterneOrganisatie;
import nl.topicus.eduarte.entities.taxonomie.MBOLeerweg;
import nl.topicus.eduarte.krd.bron.BronBpvWijzigingToegestaanCheck;
import nl.topicus.eduarte.krd.entities.bron.meldingen.aanlevering.BronAanleverMelding;
import nl.topicus.eduarte.krd.principals.deelnemer.bron.BronEditNaMutatieBeperking;
import nl.topicus.onderwijs.duo.bron.BronException;

import org.junit.Before;
import org.junit.Test;

public class ProefgevallenBveBOMantis57909Test extends ProefgevallenBveBO
{
	private Date vandaag;

	private Schooljaar huidigSchooljaar;

	private Date eenJanuari;

	private BPVBedrijfsgegeven oudeBedrijfsgegeven;

	private BPVBedrijfsgegeven nieuweBedrijfsgegeven;

	private void zetBedrijfsgegeven(BPVBedrijfsgegeven bedrijfsgegeven)
	{
		oudeBedrijfsgegeven = bpvInschrijving.getBedrijfsgegeven();
		bpvInschrijving.setBedrijfsgegeven(bedrijfsgegeven);
		bpvInschrijving.setBpvBedrijf(bedrijfsgegeven.getExterneOrganisatie());
	}

	@Before
	public void setupInschrijving()
	{
		ExterneOrganisatie bpvOrganisatie;
		bpvOrganisatie = new ExterneOrganisatie();
		bpvOrganisatie.setBegindatum(IBeginEinddatumEntiteit.MIN_DATE);
		bpvOrganisatie.setBpvBedrijf(true);
		bpvOrganisatie.saveOrUpdate();

		nieuweBedrijfsgegeven = new BPVBedrijfsgegeven(bpvOrganisatie);
		nieuweBedrijfsgegeven.setCodeLeerbedrijf("99ZZ9999JJ0001602");
		nieuweBedrijfsgegeven.saveOrUpdate();

		getDeelnemer1022();

		vandaag = timeUtil.currentDate();
		huidigSchooljaar = Schooljaar.valueOf(vandaag);
		eenJanuari = timeUtil.asDate(huidigSchooljaar.getEindJaar(), 0, 1);

		verbintenis.setBegindatum(huidigSchooljaar.getBegindatum());

		Schooljaar.valueOf(verbintenis.getBegindatum());

		verbintenis.setStatus(Definitief);
		verbintenis.getOpleiding().setLeerweg(MBOLeerweg.BBL);

		bpvInschrijving.getBpvBedrijf().saveOrUpdate();
		bpvInschrijving.setStatus(BPVStatus.Definitief);
		bpvInschrijving.setBegindatum(verbintenis.getBegindatum());
		bpvInschrijving.setAfsluitdatum(verbintenis.getBegindatum());
		bpvInschrijving.setVerwachteEinddatum(huidigSchooljaar.getEinddatum());
		bpvInschrijving.setEinddatum(huidigSchooljaar.getEinddatum());
	}

	@Test
	public void toevoegingBpvBolTijdensMutatiebeperkingToegestaan() throws Exception
	{
		tester.voerTestUitMetMedewerker();

		verbintenis.getOpleiding().setLeerweg(MBOLeerweg.BOL);

		zetSchooljaarStatus(MutatiebeperkingIngesteld);

		BronBpvWijzigingToegestaanCheck check =
			new BronBpvWijzigingToegestaanCheck(null, null, null, null, bpvInschrijving,
				verbintenis);

		assertThat(check.getResultaat(), is(LeerwegIsNiet_BBL_of_CBL));

		addChange(bpvInschrijving, "status", BPVStatus.Voorlopig, BPVStatus.Definitief);
		controller.save();

		BronAanleverMelding melding = getEersteMelding();
		assertThat(melding.isBekostigingsRelevant(), is(false));
	}

	@Test
	public void verwijderingBpvBolTijdensMutatiebeperkingToegestaan() throws Exception
	{
		tester.voerTestUitMetMedewerker();

		verbintenis.getOpleiding().setLeerweg(MBOLeerweg.BOL);

		zetSchooljaarStatus(MutatiebeperkingIngesteld);

		bpvInschrijving.setStatus(BPVStatus.Afgemeld);

		BronBpvWijzigingToegestaanCheck check =
			new BronBpvWijzigingToegestaanCheck(bpvInschrijving.getBegindatum(), bpvInschrijving
				.getAfsluitdatum(), 321L, BPVStatus.Volledig, bpvInschrijving, verbintenis);

		assertThat(check.getResultaat(), is(LeerwegIsNiet_BBL_of_CBL));

		addChange(bpvInschrijving, "status", BPVStatus.Definitief, BPVStatus.Afgemeld);
		controller.save();

		BronAanleverMelding melding = getEersteMelding();
		assertThat(melding.isBekostigingsRelevant(), is(false));
	}

	@Test
	public void aanpassingBpvBolTijdensMutatiebeperkingToegestaan() throws Exception
	{
		tester.voerTestUitMetMedewerker();

		verbintenis.getOpleiding().setLeerweg(MBOLeerweg.BOL);

		zetSchooljaarStatus(MutatiebeperkingIngesteld);

		bpvInschrijving.setStatus(BPVStatus.Definitief);

		Date oudeAfsluitdatum = bpvInschrijving.getAfsluitdatum();
		// pak een datum tussen start schooljaar en 1 januari
		bpvInschrijving.setAfsluitdatum(huidigSchooljaar.getEenOktober());

		BronBpvWijzigingToegestaanCheck check =
			new BronBpvWijzigingToegestaanCheck(bpvInschrijving.getBegindatum(), oudeAfsluitdatum,
				321L, BPVStatus.Volledig, bpvInschrijving, verbintenis);

		assertThat(check.getResultaat(), is(LeerwegIsNiet_BBL_of_CBL));

		addChange(bpvInschrijving, "afsluitdatum", oudeAfsluitdatum, bpvInschrijving
			.getAfsluitdatum());
		controller.save();

		BronAanleverMelding melding = getEersteMelding();
		assertThat(melding.isBekostigingsRelevant(), is(false));
	}

	@Test
	public void toevoegingBpvBolTijdensMutatiestopToegestaan() throws Exception
	{
		tester.voerTestUitMetMedewerker();

		verbintenis.getOpleiding().setLeerweg(MBOLeerweg.BOL);

		zetSchooljaarStatus(MutatiestopIngesteld);

		BronBpvWijzigingToegestaanCheck check =
			new BronBpvWijzigingToegestaanCheck(null, null, null, null, bpvInschrijving,
				verbintenis);

		assertThat(check.getResultaat(), is(LeerwegIsNiet_BBL_of_CBL));

		addChange(bpvInschrijving, "status", BPVStatus.Voorlopig, BPVStatus.Definitief);
		controller.save();

		BronAanleverMelding melding = getEersteMelding();
		assertThat(melding.isBekostigingsRelevant(), is(false));
	}

	@Test
	public void verwijderingBpvBolTijdensMutatiestopToegestaan() throws Exception
	{
		tester.voerTestUitMetMedewerker();

		verbintenis.getOpleiding().setLeerweg(MBOLeerweg.BOL);

		zetSchooljaarStatus(MutatiestopIngesteld);

		bpvInschrijving.setStatus(BPVStatus.Afgemeld);

		BronBpvWijzigingToegestaanCheck check =
			new BronBpvWijzigingToegestaanCheck(bpvInschrijving.getBegindatum(), bpvInschrijving
				.getAfsluitdatum(), 321L, BPVStatus.Volledig, bpvInschrijving, verbintenis);

		assertThat(check.getResultaat(), is(LeerwegIsNiet_BBL_of_CBL));

		addChange(bpvInschrijving, "status", BPVStatus.Definitief, BPVStatus.Afgemeld);
		controller.save();

		BronAanleverMelding melding = getEersteMelding();
		assertThat(melding.isBekostigingsRelevant(), is(false));
	}

	@Test
	public void aanpassingBpvBolTijdensMutatiestopToegestaan() throws Exception
	{
		tester.voerTestUitMetMedewerker();

		verbintenis.getOpleiding().setLeerweg(MBOLeerweg.BOL);

		zetSchooljaarStatus(MutatiestopIngesteld);

		bpvInschrijving.setStatus(BPVStatus.Definitief);

		Date oudeAfsluitdatum = bpvInschrijving.getAfsluitdatum();
		// pak een datum tussen start schooljaar en 1 januari
		bpvInschrijving.setAfsluitdatum(huidigSchooljaar.getEenOktober());

		BronBpvWijzigingToegestaanCheck check =
			new BronBpvWijzigingToegestaanCheck(bpvInschrijving.getBegindatum(), oudeAfsluitdatum,
				321L, BPVStatus.Volledig, bpvInschrijving, verbintenis);

		assertThat(check.getResultaat(), is(LeerwegIsNiet_BBL_of_CBL));

		addChange(bpvInschrijving, "afsluitdatum", oudeAfsluitdatum, bpvInschrijving
			.getAfsluitdatum());
		controller.save();

		BronAanleverMelding melding = getEersteMelding();
		assertThat(melding.isBekostigingsRelevant(), is(false));
	}

	@Test
	public void toevoegingBpvColTijdensMutatiebeperkingToegestaan() throws Exception
	{
		tester.voerTestUitMetMedewerker();

		verbintenis.getOpleiding().setLeerweg(MBOLeerweg.COL);

		zetSchooljaarStatus(MutatiebeperkingIngesteld);

		BronBpvWijzigingToegestaanCheck check =
			new BronBpvWijzigingToegestaanCheck(null, null, null, null, bpvInschrijving,
				verbintenis);

		assertThat(check.getResultaat(), is(LeerwegIsNiet_BBL_of_CBL));

		addChange(bpvInschrijving, "status", BPVStatus.Voorlopig, BPVStatus.Definitief);
		controller.save();

		BronAanleverMelding melding = getEersteMelding();
		assertThat(melding.isBekostigingsRelevant(), is(false));
	}

	@Test
	public void verwijderingBpvColTijdensMutatiebeperkingToegestaan() throws Exception
	{
		tester.voerTestUitMetMedewerker();

		verbintenis.getOpleiding().setLeerweg(MBOLeerweg.COL);

		zetSchooljaarStatus(MutatiebeperkingIngesteld);

		bpvInschrijving.setStatus(BPVStatus.Afgemeld);

		BronBpvWijzigingToegestaanCheck check =
			new BronBpvWijzigingToegestaanCheck(bpvInschrijving.getBegindatum(), bpvInschrijving
				.getAfsluitdatum(), 321L, BPVStatus.Volledig, bpvInschrijving, verbintenis);

		assertThat(check.getResultaat(), is(LeerwegIsNiet_BBL_of_CBL));

		addChange(bpvInschrijving, "status", BPVStatus.Definitief, BPVStatus.Afgemeld);
		controller.save();

		BronAanleverMelding melding = getEersteMelding();
		assertThat(melding.isBekostigingsRelevant(), is(false));
	}

	@Test
	public void aanpassingBpvColTijdensMutatiebeperkingToegestaan() throws Exception
	{
		tester.voerTestUitMetMedewerker();

		verbintenis.getOpleiding().setLeerweg(MBOLeerweg.COL);

		zetSchooljaarStatus(MutatiebeperkingIngesteld);

		bpvInschrijving.setStatus(BPVStatus.Definitief);

		Date oudeAfsluitdatum = bpvInschrijving.getAfsluitdatum();
		// pak een datum tussen start schooljaar en 1 januari
		bpvInschrijving.setAfsluitdatum(huidigSchooljaar.getEenOktober());

		BronBpvWijzigingToegestaanCheck check =
			new BronBpvWijzigingToegestaanCheck(bpvInschrijving.getBegindatum(), oudeAfsluitdatum,
				321L, BPVStatus.Volledig, bpvInschrijving, verbintenis);

		assertThat(check.getResultaat(), is(LeerwegIsNiet_BBL_of_CBL));

		addChange(bpvInschrijving, "afsluitdatum", oudeAfsluitdatum, bpvInschrijving
			.getAfsluitdatum());
		controller.save();

		BronAanleverMelding melding = getEersteMelding();
		assertThat(melding.isBekostigingsRelevant(), is(false));
	}

	@Test
	public void toevoegingBpvColTijdensMutatiestopToegestaan() throws Exception
	{
		tester.voerTestUitMetMedewerker();

		verbintenis.getOpleiding().setLeerweg(MBOLeerweg.COL);

		zetSchooljaarStatus(MutatiestopIngesteld);

		BronBpvWijzigingToegestaanCheck check =
			new BronBpvWijzigingToegestaanCheck(null, null, null, null, bpvInschrijving,
				verbintenis);

		assertThat(check.getResultaat(), is(LeerwegIsNiet_BBL_of_CBL));

		addChange(bpvInschrijving, "status", BPVStatus.Voorlopig, BPVStatus.Definitief);
		controller.save();

		BronAanleverMelding melding = getEersteMelding();
		assertThat(melding.isBekostigingsRelevant(), is(false));
	}

	@Test
	public void verwijderingBpvColTijdensMutatiestopToegestaan() throws Exception
	{
		tester.voerTestUitMetMedewerker();

		verbintenis.getOpleiding().setLeerweg(MBOLeerweg.COL);

		zetSchooljaarStatus(MutatiestopIngesteld);

		bpvInschrijving.setStatus(BPVStatus.Afgemeld);

		BronBpvWijzigingToegestaanCheck check =
			new BronBpvWijzigingToegestaanCheck(bpvInschrijving.getBegindatum(), bpvInschrijving
				.getAfsluitdatum(), 321L, BPVStatus.Volledig, bpvInschrijving, verbintenis);

		assertThat(check.getResultaat(), is(LeerwegIsNiet_BBL_of_CBL));

		addChange(bpvInschrijving, "status", BPVStatus.Definitief, bpvInschrijving.getStatus());
		controller.save();

		BronAanleverMelding melding = getEersteMelding();
		assertThat(melding.isBekostigingsRelevant(), is(false));
	}

	@Test
	public void aanpassingBpvColTijdensMutatiestopToegestaan() throws Exception
	{
		tester.voerTestUitMetMedewerker();

		verbintenis.getOpleiding().setLeerweg(MBOLeerweg.COL);

		zetSchooljaarStatus(MutatiestopIngesteld);

		bpvInschrijving.setStatus(BPVStatus.Definitief);

		Date oudeAfsluitdatum = bpvInschrijving.getAfsluitdatum();
		// pak een datum tussen start schooljaar en 1 januari
		bpvInschrijving.setAfsluitdatum(huidigSchooljaar.getEenOktober());

		BronBpvWijzigingToegestaanCheck check =
			new BronBpvWijzigingToegestaanCheck(bpvInschrijving.getBegindatum(), oudeAfsluitdatum,
				321L, BPVStatus.Volledig, bpvInschrijving, verbintenis);

		assertThat(check.getResultaat(), is(LeerwegIsNiet_BBL_of_CBL));

		addChange(bpvInschrijving, "afsluitdatum", oudeAfsluitdatum, bpvInschrijving
			.getAfsluitdatum());
		controller.save();

		BronAanleverMelding melding = getEersteMelding();
		assertThat(melding.isBekostigingsRelevant(), is(false));
	}

	@Test(expected = BronException.class)
	public void toevoegingBpvBblTijdensMutatiebeperkingNietToegestaan() throws Exception
	{
		tester.voerTestUitMetMedewerker();

		zetSchooljaarStatus(MutatiebeperkingIngesteld);

		BronBpvWijzigingToegestaanCheck check =
			new BronBpvWijzigingToegestaanCheck(null, null, null, null, bpvInschrijving,
				verbintenis);

		assertThat(check.getResultaat(),
			is(MedewerkerHeeftGeenToestemmingOmWijzigingDoorTeVoerenBijEenMutatiebeperkingOfStop));

		addChange(bpvInschrijving, "status", BPVStatus.Voorlopig, bpvInschrijving.getStatus());

		// gooit exception
		controller.save();
	}

	@Test(expected = BronException.class)
	public void verwijderingBpvBblTijdensMutatiebeperkingNietToegestaan() throws Exception
	{
		tester.voerTestUitMetMedewerker();

		zetSchooljaarStatus(MutatiebeperkingIngesteld);

		bpvInschrijving.setStatus(BPVStatus.Afgemeld);

		BronBpvWijzigingToegestaanCheck check =
			new BronBpvWijzigingToegestaanCheck(bpvInschrijving.getBegindatum(), bpvInschrijving
				.getAfsluitdatum(), 321L, BPVStatus.Volledig, bpvInschrijving, verbintenis);

		assertThat(check.getResultaat(),
			is(MedewerkerHeeftGeenToestemmingOmWijzigingDoorTeVoerenBijEenMutatiebeperkingOfStop));

		addChange(bpvInschrijving, "status", BPVStatus.Volledig, bpvInschrijving.getStatus());

		// gooit exception
		controller.save();
	}

	@Test
	public void aanpassingOverigeKenmerkenBpvBblTijdensMutatiebeperkingToegestaan()
			throws BronException
	{
		tester.voerTestUitMetMedewerker();

		zetSchooljaarStatus(MutatiebeperkingIngesteld);

		bpvInschrijving.setStatus(BPVStatus.Definitief);

		bpvInschrijving.setAfsluitdatum(huidigSchooljaar.getEenOktober());
		bpvInschrijving.setVerwachteEinddatum(huidigSchooljaar.getEinddatum());
		bpvInschrijving.setEinddatum(huidigSchooljaar.getEinddatum());
		BronBpvWijzigingToegestaanCheck check =
			new BronBpvWijzigingToegestaanCheck(bpvInschrijving.getBegindatum(), bpvInschrijving
				.getAfsluitdatum(), bpvInschrijving.getBedrijfsgegeven().getId(),
				BPVStatus.Volledig, bpvInschrijving, verbintenis);

		assertThat(check.getResultaat(), is(GeenBelangrijkeVeldenAangepast));

		addChange(bpvInschrijving, "status", BPVStatus.Volledig, bpvInschrijving.getStatus());
		addChange(bpvInschrijving, "verwachteEinddatum", null, bpvInschrijving
			.getVerwachteEinddatum());
		addChange(bpvInschrijving, "einddatum", null, bpvInschrijving.getEinddatum());

		controller.save();

		BronAanleverMelding melding = getEersteMelding();
		assertThat(melding.isBekostigingsRelevant(), is(false));
	}

	@Test(expected = BronException.class)
	public void toevoegingBpvCblTijdensMutatiebeperkingNietToegestaan() throws BronException
	{
		tester.voerTestUitMetMedewerker();

		verbintenis.getOpleiding().setLeerweg(MBOLeerweg.CBL);
		zetSchooljaarStatus(MutatiebeperkingIngesteld);

		BronBpvWijzigingToegestaanCheck check =
			new BronBpvWijzigingToegestaanCheck(null, null, null, null, bpvInschrijving,
				verbintenis);

		assertThat(check.getResultaat(),
			is(MedewerkerHeeftGeenToestemmingOmWijzigingDoorTeVoerenBijEenMutatiebeperkingOfStop));

		addChange(bpvInschrijving, "status", BPVStatus.Voorlopig, bpvInschrijving.getStatus());
		// gooit exception
		controller.save();
	}

	@Test(expected = BronException.class)
	public void verwijderingBpvCblTijdensMutatiebeperkingNietToegestaan() throws BronException
	{
		tester.voerTestUitMetMedewerker();

		zetSchooljaarStatus(MutatiebeperkingIngesteld);

		verbintenis.getOpleiding().setLeerweg(MBOLeerweg.CBL);
		bpvInschrijving.setStatus(BPVStatus.Afgemeld);

		BronBpvWijzigingToegestaanCheck check =
			new BronBpvWijzigingToegestaanCheck(bpvInschrijving.getBegindatum(), bpvInschrijving
				.getAfsluitdatum(), 321L, BPVStatus.Volledig, bpvInschrijving, verbintenis);

		assertThat(check.getResultaat(),
			is(MedewerkerHeeftGeenToestemmingOmWijzigingDoorTeVoerenBijEenMutatiebeperkingOfStop));

		addChange(bpvInschrijving, "status", BPVStatus.Volledig, bpvInschrijving.getStatus());
		// gooit exception
		controller.save();
	}

	@Test
	public void aanpassingOverigeKenmerkenBpvCblTijdensMutatiebeperkingToegestaan()
			throws BronException
	{
		tester.voerTestUitMetMedewerker();

		zetSchooljaarStatus(MutatiebeperkingIngesteld);

		verbintenis.getOpleiding().setLeerweg(MBOLeerweg.CBL);
		bpvInschrijving.setStatus(BPVStatus.Definitief);

		// pak een datum tussen start schooljaar en 1 januari
		bpvInschrijving.setAfsluitdatum(huidigSchooljaar.getEenOktober());
		bpvInschrijving.setVerwachteEinddatum(huidigSchooljaar.getEinddatum());

		BronBpvWijzigingToegestaanCheck check =
			new BronBpvWijzigingToegestaanCheck(bpvInschrijving.getBegindatum(), bpvInschrijving
				.getAfsluitdatum(), bpvInschrijving.getBedrijfsgegeven().getId(),
				BPVStatus.Volledig, bpvInschrijving, verbintenis);

		assertThat(check.getResultaat(), is(GeenBelangrijkeVeldenAangepast));

		addChange(bpvInschrijving, "status", BPVStatus.Volledig, bpvInschrijving.getStatus());
		addChange(bpvInschrijving, "verwachteEinddatum", null, bpvInschrijving
			.getVerwachteEinddatum());
		controller.save();

		BronAanleverMelding melding = getEersteMelding();
		assertThat(melding.isBekostigingsRelevant(), is(false));
	}

	@Test
	public void toevoegingBpvBblWelToegestaanTijdensMutatiebeperkingMetPermissie()
			throws BronException
	{
		tester.voerTestUitMetMedewerker(new BronEditNaMutatieBeperking());

		zetSchooljaarStatus(MutatiebeperkingIngesteld);

		BronBpvWijzigingToegestaanCheck check =
			new BronBpvWijzigingToegestaanCheck(null, null, null, null, bpvInschrijving,
				verbintenis);

		assertThat(check.getResultaat(),
			is(MedewerkerHeeftToestemmingOmWijzigingDoorTeVoerenBijEenMutatiebeperking));

		addChange(bpvInschrijving, "status", BPVStatus.Voorlopig, bpvInschrijving.getStatus());
		controller.save();

		BronAanleverMelding melding = getEersteMelding();
		assertThat(melding.isBekostigingsRelevant(), is(false));
	}

	@Test
	public void verwijderingBpvBblWelToegestaanTijdensMutatiebeperkingMetPermissie()
			throws BronException
	{
		tester.voerTestUitMetMedewerker(new BronEditNaMutatieBeperking());
		zetSchooljaarStatus(MutatiebeperkingIngesteld);

		bpvInschrijving.setStatus(BPVStatus.Afgemeld);

		BronBpvWijzigingToegestaanCheck check =
			new BronBpvWijzigingToegestaanCheck(bpvInschrijving.getBegindatum(), bpvInschrijving
				.getAfsluitdatum(), 321L, BPVStatus.Volledig, bpvInschrijving, verbintenis);

		assertThat(check.getResultaat(),
			is(MedewerkerHeeftToestemmingOmWijzigingDoorTeVoerenBijEenMutatiebeperking));

		addChange(bpvInschrijving, "status", BPVStatus.Volledig, bpvInschrijving.getStatus());
		controller.save();

		BronAanleverMelding melding = getEersteMelding();
		assertThat(melding.isBekostigingsRelevant(), is(false));
	}

	@Test
	public void aanpassingBpvBblWelToegestaanTijdensMutatiebeperkingMetPermissie()
			throws BronException
	{
		tester.voerTestUitMetMedewerker(new BronEditNaMutatieBeperking());
		zetSchooljaarStatus(MutatiebeperkingIngesteld);

		bpvInschrijving.setStatus(BPVStatus.Definitief);
		bpvInschrijving.setAfsluitdatum(huidigSchooljaar.getBegindatum());

		Date oudeAfsluitdatum = bpvInschrijving.getAfsluitdatum();
		// pak een datum tussen start schooljaar en 1 januari
		bpvInschrijving.setAfsluitdatum(huidigSchooljaar.getEenOktober());

		BronBpvWijzigingToegestaanCheck check =
			new BronBpvWijzigingToegestaanCheck(bpvInschrijving.getBegindatum(), oudeAfsluitdatum,
				bpvInschrijving.getBedrijfsgegeven().getId(), BPVStatus.Volledig, bpvInschrijving,
				verbintenis);

		assertThat(check.getResultaat(), is(AfsluitdatumsLiggenVoorPeildatum));

		addChange(bpvInschrijving, "afsluitdatum", oudeAfsluitdatum, bpvInschrijving
			.getAfsluitdatum());
		addChange(bpvInschrijving, "status", BPVStatus.Volledig, bpvInschrijving.getStatus());
		controller.save();

		BronAanleverMelding melding = getEersteMelding();
		assertThat(melding.isBekostigingsRelevant(), is(false));
	}

	@Test
	public void toevoegingBpvBblWelToegestaanTijdensMutatiestopMetPermissie() throws BronException
	{
		tester.voerTestUitMetMedewerker(new BronEditNaMutatieBeperking());

		zetSchooljaarStatus(MutatiestopIngesteld);

		BronBpvWijzigingToegestaanCheck check =
			new BronBpvWijzigingToegestaanCheck(null, null, null, null, bpvInschrijving,
				verbintenis);

		assertThat(check.getResultaat(),
			is(MedewerkerHeeftToestemmingOmWijzigingDoorTeVoerenBijEenMutatiestop));

		addChange(bpvInschrijving, "afsluitdatum", null, bpvInschrijving.getAfsluitdatum());
		addChange(bpvInschrijving, "begindatum", null, bpvInschrijving.getBegindatum());
		addChange(bpvInschrijving, "status", null, bpvInschrijving.getStatus());

		controller.save();

		BronAanleverMelding melding = getEersteMelding();
		assertThat(melding.isBekostigingsRelevant(), is(true));
	}

	@Test
	public void verwijderingBpvBblWelToegestaanTijdensMutatiestopMetPermissie()
			throws BronException
	{
		tester.voerTestUitMetMedewerker(new BronEditNaMutatieBeperking());

		zetSchooljaarStatus(MutatiestopIngesteld);

		bpvInschrijving.setStatus(BPVStatus.Afgemeld);

		BronBpvWijzigingToegestaanCheck check =
			new BronBpvWijzigingToegestaanCheck(bpvInschrijving.getBegindatum(), bpvInschrijving
				.getAfsluitdatum(), bpvInschrijving.getBpvBedrijf().getId(), BPVStatus.Volledig,
				bpvInschrijving, verbintenis);

		assertThat(check.getResultaat(),
			is(MedewerkerHeeftToestemmingOmWijzigingDoorTeVoerenBijEenMutatiestop));

		addChange(bpvInschrijving, "einddatum", null, bpvInschrijving.getEinddatum());
		addChange(bpvInschrijving, "status", BPVStatus.Volledig, bpvInschrijving.getStatus());
		controller.save();

		BronAanleverMelding melding = getEersteMelding();
		assertThat(melding.isBekostigingsRelevant(), is(true));
	}

	@Test
	public void aanpassingBpvBblWelToegestaanTijdensMutatiestopMetPermissie() throws BronException
	{
		tester.voerTestUitMetMedewerker(new BronEditNaMutatieBeperking());

		zetSchooljaarStatus(MutatiestopIngesteld);

		bpvInschrijving.setStatus(BPVStatus.Definitief);

		Date oudeAfsluitdatum = bpvInschrijving.getAfsluitdatum();
		// pak een datum tussen start schooljaar en 1 januari
		bpvInschrijving.setAfsluitdatum(huidigSchooljaar.getEenOktober());

		BronBpvWijzigingToegestaanCheck check =
			new BronBpvWijzigingToegestaanCheck(bpvInschrijving.getBegindatum(), oudeAfsluitdatum,
				bpvInschrijving.getBedrijfsgegeven().getId(), BPVStatus.Volledig, bpvInschrijving,
				verbintenis);

		assertThat(check.getResultaat(), is(AfsluitdatumsLiggenVoorPeildatum));

		addChange(bpvInschrijving, "afsluitdatum", oudeAfsluitdatum, bpvInschrijving
			.getAfsluitdatum());
		addChange(bpvInschrijving, "status", BPVStatus.Volledig, bpvInschrijving.getStatus());
		controller.save();

		BronAanleverMelding melding = getEersteMelding();
		assertThat(melding.isBekostigingsRelevant(), is(false));
	}

	/**
	 * Indien soort mutatie = "T" of "V" en de leerweg van de inschrijving is BBL of CBL:
	 * de mutatie is alleen toegestaan als de afsluitdatum BPV op of na 1 januari van het
	 * studiejaar waarvoor de mutatiestop van kracht is ligt, tenzij het 300-record
	 * aangeeft dat de accountant verantwoordelijk is.
	 */
	@Test
	public void toevoegingBpvBblWelToegestaanTijdensMutatiestopNa1Januari() throws BronException
	{
		tester.voerTestUitMetMedewerker();

		zetSchooljaarStatus(MutatiestopIngesteld);
		bpvInschrijving.setAfsluitdatum(eenJanuari);
		BronBpvWijzigingToegestaanCheck check =
			new BronBpvWijzigingToegestaanCheck(null, null, null, null, bpvInschrijving,
				verbintenis);

		assertThat(check.getResultaat(), is(AfsluitdatumLigtNaPeildatum));

		addChange(bpvInschrijving, "afsluitdatum", null, bpvInschrijving.getAfsluitdatum());
		addChange(bpvInschrijving, "begindatum", null, bpvInschrijving.getBegindatum());
		addChange(bpvInschrijving, "status", null, bpvInschrijving.getStatus());

		controller.save();

		BronAanleverMelding melding = getEersteMelding();
		assertThat(melding.isBekostigingsRelevant(), is(false));
	}

	@Test
	public void verwijderingBpvBblWelToegestaanTijdensMutatiestopNa1Januari() throws BronException
	{
		tester.voerTestUitMetMedewerker();

		zetSchooljaarStatus(MutatiestopIngesteld);

		bpvInschrijving.setAfsluitdatum(eenJanuari);
		bpvInschrijving.setStatus(BPVStatus.Afgemeld);

		BronBpvWijzigingToegestaanCheck check =
			new BronBpvWijzigingToegestaanCheck(bpvInschrijving.getBegindatum(), bpvInschrijving
				.getAfsluitdatum(), bpvInschrijving.getBpvBedrijf().getId(), BPVStatus.Volledig,
				bpvInschrijving, verbintenis);

		assertThat(check.getResultaat(), is(AfsluitdatumLigtNaPeildatum));

		addChange(bpvInschrijving, "status", BPVStatus.Definitief, bpvInschrijving.getStatus());
		controller.save();

		BronAanleverMelding melding = getEersteMelding();
		assertThat(melding.isBekostigingsRelevant(), is(false));
	}

	/**
	 * De afsluitdatum mag alleen worden aangepast als de Afsluitdatum BPV in Bron en de
	 * Afsluitdatum BPV in de melding beiden voor, of beiden na 1 januari van het
	 * studiejaar waarvoor de mutatiestop van kracht is liggen, tenzij de accountant
	 * verantwoordelijk is. Signaal 952
	 */
	@Test
	public void aanpassingAfsluitdatumBpvBblWelToegestaanTijdensVrijeInvoer() throws BronException
	{
		tester.voerTestUitMetMedewerker();

		bpvInschrijving.setAfsluitdatum(huidigSchooljaar.getBegindatum());
		Date oudeAfsluitdatum = bpvInschrijving.getAfsluitdatum();
		bpvInschrijving.setAfsluitdatum(huidigSchooljaar.getEinddatum());

		BronBpvWijzigingToegestaanCheck check =
			new BronBpvWijzigingToegestaanCheck(bpvInschrijving.getBegindatum(), oudeAfsluitdatum,
				bpvInschrijving.getBedrijfsgegeven().getId(), BPVStatus.Volledig, bpvInschrijving,
				verbintenis);

		assertThat(check.getResultaat(), is(AlleMutatiesZijnNogToegestaan));

		addChange(bpvInschrijving, "afsluitdatum", oudeAfsluitdatum, bpvInschrijving
			.getAfsluitdatum());
		addChange(bpvInschrijving, "status", BPVStatus.Volledig, bpvInschrijving.getStatus());
		controller.save();

		BronAanleverMelding melding = getEersteMelding();
		assertThat(melding.isBekostigingsRelevant(), is(false));
	}

	/**
	 * De afsluitdatum mag alleen worden aangepast als de Afsluitdatum BPV in Bron en de
	 * Afsluitdatum BPV in de melding beiden voor, of beiden na 1 januari van het
	 * studiejaar waarvoor de mutatiestop van kracht is liggen, tenzij de accountant
	 * verantwoordelijk is. Signaal 952
	 * 
	 * @throws BronException
	 */
	@Test
	public void aanpassingAfsluitdatumBpvBblWelToegestaanTijdensMutatiestopVoorJanuari()
			throws BronException
	{
		tester.voerTestUitMetMedewerker();

		zetSchooljaarStatus(MutatiestopIngesteld);

		bpvInschrijving.setAfsluitdatum(huidigSchooljaar.getBegindatum());
		Date oudeAfsluitdatum = bpvInschrijving.getAfsluitdatum();
		bpvInschrijving.setAfsluitdatum(huidigSchooljaar.getEenOktober());

		BronBpvWijzigingToegestaanCheck check =
			new BronBpvWijzigingToegestaanCheck(bpvInschrijving.getBegindatum(), oudeAfsluitdatum,
				bpvInschrijving.getBedrijfsgegeven().getId(), BPVStatus.Volledig, bpvInschrijving,
				verbintenis);

		assertThat(check.getResultaat(), is(AfsluitdatumsLiggenVoorPeildatum));

		addChange(bpvInschrijving, "afsluitdatum", oudeAfsluitdatum, bpvInschrijving
			.getAfsluitdatum());
		addChange(bpvInschrijving, "status", BPVStatus.Volledig, bpvInschrijving.getStatus());
		controller.save();

		BronAanleverMelding melding = getEersteMelding();
		assertThat(melding.isBekostigingsRelevant(), is(false));
	}

	/**
	 * De afsluitdatum mag alleen worden aangepast als de Afsluitdatum BPV in Bron en de
	 * Afsluitdatum BPV in de melding beiden voor, of beiden na 1 januari van het
	 * studiejaar waarvoor de mutatiestop van kracht is liggen, tenzij de accountant
	 * verantwoordelijk is. Signaal 952
	 * 
	 * @throws BronException
	 */
	@Test
	public void aanpassingAfsluitdatumBpvBblWelToegestaanTijdensMutatiestopNa1Januari()
			throws BronException
	{
		tester.voerTestUitMetMedewerker();

		zetSchooljaarStatus(MutatiestopIngesteld);

		bpvInschrijving.setAfsluitdatum(eenJanuari);

		Date oudeAfsluitdatum = bpvInschrijving.getAfsluitdatum();
		bpvInschrijving.setAfsluitdatum(huidigSchooljaar.getEinddatum());

		BronBpvWijzigingToegestaanCheck check =
			new BronBpvWijzigingToegestaanCheck(bpvInschrijving.getBegindatum(), oudeAfsluitdatum,
				bpvInschrijving.getBedrijfsgegeven().getId(), BPVStatus.Volledig, bpvInschrijving,
				verbintenis);

		assertThat(check.getResultaat(), is(AfsluitdatumsLiggenNaPeildatum));

		addChange(bpvInschrijving, "afsluitdatum", oudeAfsluitdatum, bpvInschrijving
			.getAfsluitdatum());
		addChange(bpvInschrijving, "status", BPVStatus.Volledig, bpvInschrijving.getStatus());
		controller.save();

		BronAanleverMelding melding = getEersteMelding();
		assertThat(melding.isBekostigingsRelevant(), is(false));
	}

	/**
	 * De afsluitdatum mag alleen worden aangepast als de Afsluitdatum BPV in Bron en de
	 * Afsluitdatum BPV in de melding beiden voor, of beiden na 1 januari van het
	 * studiejaar waarvoor de mutatiestop van kracht is liggen, tenzij de accountant
	 * verantwoordelijk is. Signaal 952
	 * 
	 * @throws BronException
	 */
	@Test(expected = BronException.class)
	public void aanpassingAfsluitdatumBpvBblNietToegestaanTijdensMutatiestopVanVoorNaarNa1Januari()
			throws BronException
	{
		tester.voerTestUitMetMedewerker();

		zetSchooljaarStatus(MutatiestopIngesteld);

		bpvInschrijving.setAfsluitdatum(huidigSchooljaar.getBegindatum());

		Date oudeAfsluitdatum = bpvInschrijving.getAfsluitdatum();
		bpvInschrijving.setAfsluitdatum(huidigSchooljaar.getEinddatum());

		BronBpvWijzigingToegestaanCheck check =
			new BronBpvWijzigingToegestaanCheck(bpvInschrijving.getBegindatum(), oudeAfsluitdatum,
				bpvInschrijving.getBpvBedrijf().getId(), BPVStatus.Volledig, bpvInschrijving,
				verbintenis);

		assertThat(check.getResultaat(),
			is(MedewerkerHeeftGeenToestemmingOmWijzigingDoorTeVoerenBijEenMutatiebeperkingOfStop));

		addChange(bpvInschrijving, "afsluitdatum", oudeAfsluitdatum, bpvInschrijving
			.getAfsluitdatum());
		addChange(bpvInschrijving, "status", BPVStatus.Volledig, bpvInschrijving.getStatus());
		controller.save();
	}

	/**
	 * De afsluitdatum mag alleen worden aangepast als de Afsluitdatum BPV in Bron en de
	 * Afsluitdatum BPV in de melding beiden voor, of beiden na 1 januari van het
	 * studiejaar waarvoor de mutatiestop van kracht is liggen, tenzij de accountant
	 * verantwoordelijk is. Signaal 952
	 * 
	 * @throws BronException
	 */
	@Test(expected = BronException.class)
	public void aanpassingAfsluitdatumBpvBblNietToegestaanTijdensMutatiestopVanNaNaarVoor1Januari()
			throws BronException
	{
		tester.voerTestUitMetMedewerker();

		zetSchooljaarStatus(MutatiestopIngesteld);

		bpvInschrijving.setAfsluitdatum(huidigSchooljaar.getEinddatum());

		Date oudeAfsluitdatum = bpvInschrijving.getAfsluitdatum();

		bpvInschrijving.setAfsluitdatum(huidigSchooljaar.getBegindatum());

		BronBpvWijzigingToegestaanCheck check =
			new BronBpvWijzigingToegestaanCheck(bpvInschrijving.getBegindatum(), oudeAfsluitdatum,
				bpvInschrijving.getBpvBedrijf().getId(), BPVStatus.Volledig, bpvInschrijving,
				verbintenis);

		assertThat(check.getResultaat(),
			is(MedewerkerHeeftGeenToestemmingOmWijzigingDoorTeVoerenBijEenMutatiebeperkingOfStop));

		addChange(bpvInschrijving, "afsluitdatum", oudeAfsluitdatum, bpvInschrijving
			.getAfsluitdatum());
		addChange(bpvInschrijving, "status", BPVStatus.Volledig, bpvInschrijving.getStatus());
		controller.save();

		BronAanleverMelding melding = getEersteMelding();
		assertThat(melding.isBekostigingsRelevant(), is(false));
	}

	/**
	 * De afsluitdatum mag alleen worden aangepast als de Afsluitdatum BPV in Bron en de
	 * Afsluitdatum BPV in de melding beiden voor, of beiden na 1 januari van het
	 * studiejaar waarvoor de mutatiestop van kracht is liggen, tenzij de accountant
	 * verantwoordelijk is. Signaal 952
	 * 
	 * @throws BronException
	 */
	@Test
	public void aanpassingAfsluitdatumBpvBblToegestaanTijdensMutatiestopVanVoorNaarNa1JanuariMetPermissie()
			throws BronException
	{
		tester.voerTestUitMetMedewerker(new BronEditNaMutatieBeperking());

		zetSchooljaarStatus(MutatiestopIngesteld);

		bpvInschrijving.setAfsluitdatum(huidigSchooljaar.getBegindatum());

		Date oudeAfsluitdatum = bpvInschrijving.getAfsluitdatum();
		bpvInschrijving.setAfsluitdatum(huidigSchooljaar.getEinddatum());

		BronBpvWijzigingToegestaanCheck check =
			new BronBpvWijzigingToegestaanCheck(bpvInschrijving.getBegindatum(), oudeAfsluitdatum,
				bpvInschrijving.getBedrijfsgegeven().getId(), BPVStatus.Volledig, bpvInschrijving,
				verbintenis);

		assertThat(check.getResultaat(),
			is(MedewerkerHeeftToestemmingOmWijzigingDoorTeVoerenBijEenMutatiestop));

		addChange(bpvInschrijving, "afsluitdatum", oudeAfsluitdatum, bpvInschrijving
			.getAfsluitdatum());
		addChange(bpvInschrijving, "status", BPVStatus.Volledig, bpvInschrijving.getStatus());
		controller.save();

		BronAanleverMelding melding = getEersteMelding();
		assertThat(melding.isBekostigingsRelevant(), is(true));
	}

	/**
	 * De afsluitdatum mag alleen worden aangepast als de Afsluitdatum BPV in Bron en de
	 * Afsluitdatum BPV in de melding beiden voor, of beiden na 1 januari van het
	 * studiejaar waarvoor de mutatiestop van kracht is liggen, tenzij de accountant
	 * verantwoordelijk is. Signaal 952
	 * 
	 * @throws BronException
	 */
	@Test
	public void aanpassingAfsluitdatumBpvBblToegestaanTijdensMutatiestopVanNaNaarVoor1JanuariMetPermissie()
			throws BronException
	{
		tester.voerTestUitMetMedewerker(new BronEditNaMutatieBeperking());

		zetSchooljaarStatus(MutatiestopIngesteld);

		bpvInschrijving.setAfsluitdatum(huidigSchooljaar.getEinddatum());

		Date oudeAfsluitdatum = bpvInschrijving.getAfsluitdatum();

		bpvInschrijving.setAfsluitdatum(huidigSchooljaar.getBegindatum());

		BronBpvWijzigingToegestaanCheck check =
			new BronBpvWijzigingToegestaanCheck(bpvInschrijving.getBegindatum(), oudeAfsluitdatum,
				bpvInschrijving.getBpvBedrijf().getId(), BPVStatus.Volledig, bpvInschrijving,
				verbintenis);

		assertThat(check.getResultaat(),
			is(MedewerkerHeeftToestemmingOmWijzigingDoorTeVoerenBijEenMutatiestop));

		addChange(bpvInschrijving, "afsluitdatum", oudeAfsluitdatum, bpvInschrijving
			.getAfsluitdatum());
		addChange(bpvInschrijving, "status", BPVStatus.Volledig, bpvInschrijving.getStatus());
		controller.save();

		BronAanleverMelding melding = getEersteMelding();
		assertThat(melding.isBekostigingsRelevant(), is(true));
	}

	/**
	 * De datum begin BPV mag niet worden aangepast als de Afsluitdatum BPV in Bron voor 1
	 * januari van het studiejaar waarvoor de mutatiestop van kracht is ligt, tenzij de
	 * accountant hiervoor verantwoordelijk is. Signaal 952
	 * 
	 * @throws BronException
	 */
	@Test
	public void aanpassingBegindatumBpvBblToegestaanAfsluitdatumVoor1JanuariZonderPermissie()
			throws BronException
	{
		tester.voerTestUitMetMedewerker();

		bpvInschrijving.setAfsluitdatum(huidigSchooljaar.getBegindatum());

		bpvInschrijving.setBegindatum(huidigSchooljaar.getBegindatum());
		Date oudeBegindatum = bpvInschrijving.getBegindatum();
		bpvInschrijving.setBegindatum(huidigSchooljaar.getEenOktober());

		BronBpvWijzigingToegestaanCheck check =
			new BronBpvWijzigingToegestaanCheck(oudeBegindatum, bpvInschrijving.getAfsluitdatum(),
				bpvInschrijving.getBpvBedrijf().getId(), BPVStatus.Volledig, bpvInschrijving,
				verbintenis);

		assertThat(check.getResultaat(), is(AlleMutatiesZijnNogToegestaan));

		addChange(bpvInschrijving, "begindatum", oudeBegindatum, bpvInschrijving.getBegindatum());
		controller.save();

		BronAanleverMelding melding = getEersteMelding();
		assertThat(melding.isBekostigingsRelevant(), is(false));
	}

	/**
	 * De datum begin BPV mag niet worden aangepast als de Afsluitdatum BPV in Bron voor 1
	 * januari van het studiejaar waarvoor de mutatiestop van kracht is ligt, tenzij de
	 * accountant hiervoor verantwoordelijk is. Signaal 952
	 * 
	 * @throws BronException
	 */
	@Test
	public void aanpassingBegindatumBpvBblToegestaanAfsluitdatumNa1JanuariZonderPermissie()
			throws BronException
	{
		tester.voerTestUitMetMedewerker();

		bpvInschrijving.setAfsluitdatum(eenJanuari);

		bpvInschrijving.setBegindatum(huidigSchooljaar.getBegindatum());
		Date oudeBegindatum = bpvInschrijving.getBegindatum();
		bpvInschrijving.setBegindatum(huidigSchooljaar.getEenOktober());

		BronBpvWijzigingToegestaanCheck check =
			new BronBpvWijzigingToegestaanCheck(oudeBegindatum, bpvInschrijving.getAfsluitdatum(),
				bpvInschrijving.getBpvBedrijf().getId(), BPVStatus.Volledig, bpvInschrijving,
				verbintenis);

		assertThat(check.getResultaat(), is(AfsluitdatumLigtNaPeildatum));

		addChange(bpvInschrijving, "begindatum", oudeBegindatum, bpvInschrijving.getBegindatum());
		controller.save();

		BronAanleverMelding melding = getEersteMelding();
		assertThat(melding.isBekostigingsRelevant(), is(false));
	}

	/**
	 * De datum begin BPV mag niet worden aangepast als de Afsluitdatum BPV in Bron voor 1
	 * januari van het studiejaar waarvoor de mutatiestop van kracht is ligt, tenzij de
	 * accountant hiervoor verantwoordelijk is. Signaal 952
	 * 
	 * @throws BronException
	 */
	@Test(expected = BronException.class)
	public void aanpassingBegindatumBpvBblNietToegestaanTijdensMutatiestopAfsluitdatumVoor1JanuariZonderPermissie()
			throws BronException
	{
		tester.voerTestUitMetMedewerker();

		zetSchooljaarStatus(MutatiestopIngesteld);

		bpvInschrijving.setAfsluitdatum(huidigSchooljaar.getBegindatum());

		bpvInschrijving.setBegindatum(huidigSchooljaar.getBegindatum());
		Date oudeBegindatum = bpvInschrijving.getBegindatum();
		bpvInschrijving.setBegindatum(huidigSchooljaar.getEenOktober());

		BronBpvWijzigingToegestaanCheck check =
			new BronBpvWijzigingToegestaanCheck(oudeBegindatum, bpvInschrijving.getAfsluitdatum(),
				bpvInschrijving.getBpvBedrijf().getId(), BPVStatus.Volledig, bpvInschrijving,
				verbintenis);

		assertThat(check.getResultaat(),
			is(MedewerkerHeeftGeenToestemmingOmWijzigingDoorTeVoerenBijEenMutatiebeperkingOfStop));

		addChange(bpvInschrijving, "begindatum", oudeBegindatum, bpvInschrijving.getBegindatum());
		controller.save();
	}

	/**
	 * De datum begin BPV mag niet worden aangepast als de Afsluitdatum BPV in Bron voor 1
	 * januari van het studiejaar waarvoor de mutatiestop van kracht is ligt, tenzij de
	 * accountant hiervoor verantwoordelijk is. Signaal 952
	 * 
	 * @throws BronException
	 */
	@Test
	public void aanpassingBegindatumBpvBblToegestaanTijdensMutatiestopAfsluitdatumVoor1JanuariMetPermissie()
			throws BronException
	{
		tester.voerTestUitMetMedewerker(new BronEditNaMutatieBeperking());

		zetSchooljaarStatus(MutatiestopIngesteld);

		bpvInschrijving.setAfsluitdatum(huidigSchooljaar.getBegindatum());

		bpvInschrijving.setBegindatum(huidigSchooljaar.getBegindatum());
		Date oudeBegindatum = bpvInschrijving.getBegindatum();
		bpvInschrijving.setBegindatum(huidigSchooljaar.getEenOktober());

		BronBpvWijzigingToegestaanCheck check =
			new BronBpvWijzigingToegestaanCheck(oudeBegindatum, bpvInschrijving.getAfsluitdatum(),
				bpvInschrijving.getBpvBedrijf().getId(), BPVStatus.Volledig, bpvInschrijving,
				verbintenis);

		assertThat(check.getResultaat(),
			is(MedewerkerHeeftToestemmingOmWijzigingDoorTeVoerenBijEenMutatiestop));

		addChange(bpvInschrijving, "begindatum", oudeBegindatum, bpvInschrijving.getBegindatum());
		controller.save();

		BronAanleverMelding melding = getEersteMelding();
		assertThat(melding.isBekostigingsRelevant(), is(true));
	}

	/**
	 * De datum begin BPV mag niet worden aangepast als de Afsluitdatum BPV in Bron voor 1
	 * januari van het studiejaar waarvoor de mutatiestop van kracht is ligt, tenzij de
	 * accountant hiervoor verantwoordelijk is. Signaal 952
	 * 
	 * @throws BronException
	 */
	@Test
	public void aanpassingBegindatumBpvBblToegestaanTijdensMutatiestopAfsluitdatumNa1Januari()
			throws BronException
	{
		tester.voerTestUitMetMedewerker();

		zetSchooljaarStatus(MutatiestopIngesteld);

		bpvInschrijving.setAfsluitdatum(eenJanuari);

		bpvInschrijving.setBegindatum(huidigSchooljaar.getBegindatum());
		Date oudeBegindatum = bpvInschrijving.getBegindatum();
		bpvInschrijving.setBegindatum(huidigSchooljaar.getEenOktober());

		BronBpvWijzigingToegestaanCheck check =
			new BronBpvWijzigingToegestaanCheck(oudeBegindatum, bpvInschrijving.getAfsluitdatum(),
				bpvInschrijving.getBpvBedrijf().getId(), BPVStatus.Volledig, bpvInschrijving,
				verbintenis);

		assertThat(check.getResultaat(), is(AfsluitdatumLigtNaPeildatum));

		addChange(bpvInschrijving, "begindatum", oudeBegindatum, bpvInschrijving.getBegindatum());
		controller.save();

		BronAanleverMelding melding = getEersteMelding();
		assertThat(melding.isBekostigingsRelevant(), is(false));
	}

	@Test
	public void toevoegingBpvBblWelToegestaanNaOpgesteldAssurancerapportNa1Januari()
			throws BronException
	{
		tester.voerTestUitMetMedewerker();

		zetSchooljaarStatus(AssurancerapportOpgesteld);

		bpvInschrijving.setAfsluitdatum(eenJanuari);

		BronBpvWijzigingToegestaanCheck check =
			new BronBpvWijzigingToegestaanCheck(null, null, null, null, bpvInschrijving,
				verbintenis);

		assertThat(check.getResultaat(), is(AfsluitdatumLigtNaPeildatum));

		addChange(bpvInschrijving, "begindatum", null, bpvInschrijving.getBegindatum());
		addChange(bpvInschrijving, "afsluitdatum", null, bpvInschrijving.getAfsluitdatum());
		addChange(bpvInschrijving, "status", null, bpvInschrijving.getStatus());
		controller.save();

		BronAanleverMelding melding = getEersteMelding();
		assertThat(melding.isBekostigingsRelevant(), is(false));
	}

	@Test
	public void verwijderingBpvBblWelToegestaanNaOpgesteldAssurancerapportNa1Januari()
			throws BronException
	{
		tester.voerTestUitMetMedewerker();

		zetSchooljaarStatus(AssurancerapportOpgesteld);

		bpvInschrijving.setAfsluitdatum(eenJanuari);
		bpvInschrijving.setStatus(BPVStatus.Afgemeld);

		BronBpvWijzigingToegestaanCheck check =
			new BronBpvWijzigingToegestaanCheck(bpvInschrijving.getBegindatum(), bpvInschrijving
				.getAfsluitdatum(), bpvInschrijving.getBpvBedrijf().getId(), BPVStatus.Volledig,
				bpvInschrijving, verbintenis);

		assertThat(check.getResultaat(), is(AfsluitdatumLigtNaPeildatum));

		addChange(bpvInschrijving, "status", BPVStatus.Volledig, bpvInschrijving.getStatus());
		controller.save();

		BronAanleverMelding melding = getEersteMelding();
		assertThat(melding.isBekostigingsRelevant(), is(false));
	}

	@Test(expected = BronException.class)
	public void toevoegingBpvBblNietToegestaanNaOpgesteldAssurancerapportVoor1Januari()
			throws BronException
	{
		tester.voerTestUitMetMedewerker();

		zetSchooljaarStatus(AssurancerapportOpgesteld);
		BronBpvWijzigingToegestaanCheck check =
			new BronBpvWijzigingToegestaanCheck(null, null, null, null, bpvInschrijving,
				verbintenis);

		assertThat(check.getResultaat(), is(SchooljaarIsAfgeslotenVoorMutaties));

		addChange(bpvInschrijving, "begindatum", null, bpvInschrijving.getBegindatum());
		addChange(bpvInschrijving, "afsluitdatum", null, bpvInschrijving.getAfsluitdatum());
		addChange(bpvInschrijving, "status", null, bpvInschrijving.getStatus());
		controller.save();
	}

	@Test(expected = BronException.class)
	public void verwijderingBpvBblNietToegestaanNaOpgesteldAssurancerapportVoor1Januari()
			throws BronException
	{
		tester.voerTestUitMetMedewerker();

		bpvInschrijving.setStatus(BPVStatus.Afgewezen);
		zetSchooljaarStatus(AssurancerapportOpgesteld);
		BronBpvWijzigingToegestaanCheck check =
			new BronBpvWijzigingToegestaanCheck(bpvInschrijving.getBegindatum(), bpvInschrijving
				.getAfsluitdatum(), bpvInschrijving.getBpvBedrijf().getId(), BPVStatus.Volledig,
				bpvInschrijving, verbintenis);

		assertThat(check.getResultaat(), is(SchooljaarIsAfgeslotenVoorMutaties));

		addChange(bpvInschrijving, "status", BPVStatus.Volledig, bpvInschrijving.getStatus());
		controller.save();
	}

	@Test(expected = BronException.class)
	public void toevoegingBpvBblNietToegestaanNaOpgesteldAssurancerapportVoor1JanuariMetPermissie()
			throws BronException
	{
		tester.voerTestUitMetMedewerker(new BronEditNaMutatieBeperking());

		zetSchooljaarStatus(AssurancerapportOpgesteld);
		BronBpvWijzigingToegestaanCheck check =
			new BronBpvWijzigingToegestaanCheck(null, null, null, null, bpvInschrijving,
				verbintenis);

		assertThat(check.getResultaat(), is(SchooljaarIsAfgeslotenVoorMutaties));

		addChange(bpvInschrijving, "begindatum", null, bpvInschrijving.getBegindatum());
		addChange(bpvInschrijving, "afsluitdatum", null, bpvInschrijving.getAfsluitdatum());
		addChange(bpvInschrijving, "status", null, bpvInschrijving.getStatus());
		controller.save();
	}

	@Test(expected = BronException.class)
	public void verwijderingBpvBblNietToegestaanNaOpgesteldAssurancerapportVoor1JanuariMetPermissie()
			throws BronException
	{
		tester.voerTestUitMetMedewerker(new BronEditNaMutatieBeperking());

		bpvInschrijving.setStatus(BPVStatus.Afgewezen);
		zetSchooljaarStatus(AssurancerapportOpgesteld);

		BronBpvWijzigingToegestaanCheck check =
			new BronBpvWijzigingToegestaanCheck(bpvInschrijving.getBegindatum(), bpvInschrijving
				.getBegindatum(), bpvInschrijving.getBpvBedrijf().getId(), BPVStatus.Volledig,
				bpvInschrijving, verbintenis);

		assertThat(check.getResultaat(), is(SchooljaarIsAfgeslotenVoorMutaties));

		addChange(bpvInschrijving, "status", BPVStatus.Volledig, bpvInschrijving.getStatus());
		controller.save();
	}

	/**
	 * Het Leerbedrijf mag niet worden aangepast als de Afsluitdatum BPV in Bron voor 1
	 * januari van het studiejaar waarvoor de mutatiestop van kracht is ligt, tenzij een
	 * van de volgende situaties van kracht is:
	 * <ul>
	 * <li>de accountant is verantwoordelijk</li>
	 * <li>het Leerbedrijf in Bron is leeg</li>
	 * </ul>
	 * 
	 * @throws BronException
	 */
	@Test
	public void aanpassingLeerbedrijfIsToegestaanTijdensVrijeInvoer() throws BronException
	{
		tester.voerTestUitMetMedewerker();

		bpvInschrijving.setAfsluitdatum(huidigSchooljaar.getBegindatum());
		bpvInschrijving.setBegindatum(huidigSchooljaar.getBegindatum());

		zetBedrijfsgegeven(nieuweBedrijfsgegeven);

		BronBpvWijzigingToegestaanCheck check =
			new BronBpvWijzigingToegestaanCheck(bpvInschrijving.getBegindatum(), bpvInschrijving
				.getAfsluitdatum(), oudeBedrijfsgegeven.getId(), BPVStatus.Volledig,
				bpvInschrijving, verbintenis);

		assertThat(check.getResultaat(), is(AlleMutatiesZijnNogToegestaan));

		addChange(bpvInschrijving, "bedrijfsgegeven", oudeBedrijfsgegeven, bpvInschrijving
			.getBedrijfsgegeven());
		controller.save();
		BronAanleverMelding melding = getEersteMelding();

		assertThat(melding.isBekostigingsRelevant(), is(false));
	}

	/**
	 * Het Leerbedrijf mag niet worden aangepast als de Afsluitdatum BPV in Bron voor 1
	 * januari van het studiejaar waarvoor de mutatiestop van kracht is ligt, tenzij een
	 * van de volgende situaties van kracht is:
	 * <ul>
	 * <li>de accountant is verantwoordelijk</li>
	 * <li>het Leerbedrijf in Bron is leeg</li>
	 * </ul>
	 * 
	 * @throws BronException
	 */
	@Test
	public void aanpassingLeerbedrijfVanNullNaarWaardeIsToegestaanTijdensVrijeInvoer()
			throws BronException
	{
		tester.voerTestUitMetMedewerker();

		bpvInschrijving.setAfsluitdatum(huidigSchooljaar.getBegindatum());

		zetBedrijfsgegeven(nieuweBedrijfsgegeven);

		BronBpvWijzigingToegestaanCheck check =
			new BronBpvWijzigingToegestaanCheck(bpvInschrijving.getBegindatum(), bpvInschrijving
				.getAfsluitdatum(), null, BPVStatus.Volledig, bpvInschrijving, verbintenis);

		assertThat(check.getResultaat(), is(AanvullingBpvBedrijfsgegeven));

		addChange(bpvInschrijving, "bedrijfsgegeven", null, bpvInschrijving.getBedrijfsgegeven());
		controller.save();
		BronAanleverMelding melding = getEersteMelding();

		assertThat(melding.isBekostigingsRelevant(), is(false));
	}

	/**
	 * Het Leerbedrijf mag niet worden aangepast als de Afsluitdatum BPV in Bron voor 1
	 * januari van het studiejaar waarvoor de mutatiestop van kracht is ligt, tenzij een
	 * van de volgende situaties van kracht is:
	 * <ul>
	 * <li>de accountant is verantwoordelijk</li>
	 * <li>het Leerbedrijf in Bron is leeg</li>
	 * </ul>
	 * 
	 * @throws BronException
	 */
	@Test
	public void aanpassingLeerbedrijfIsToegestaanTijdensMutatieStopNa1Januari()
			throws BronException
	{
		tester.voerTestUitMetMedewerker();

		zetSchooljaarStatus(MutatiestopIngesteld);
		bpvInschrijving.setAfsluitdatum(eenJanuari);

		zetBedrijfsgegeven(nieuweBedrijfsgegeven);

		BronBpvWijzigingToegestaanCheck check =
			new BronBpvWijzigingToegestaanCheck(bpvInschrijving.getBegindatum(), bpvInschrijving
				.getAfsluitdatum(), oudeBedrijfsgegeven.getId(), BPVStatus.Volledig,
				bpvInschrijving, verbintenis);

		assertThat(check.getResultaat(), is(AfsluitdatumLigtNaPeildatum));

		addChange(bpvInschrijving, "bedrijfsgegeven", oudeBedrijfsgegeven, bpvInschrijving
			.getBedrijfsgegeven());
		controller.save();
		BronAanleverMelding melding = getEersteMelding();

		assertThat(melding.isBekostigingsRelevant(), is(false));
	}

	/**
	 * Het Leerbedrijf mag niet worden aangepast als de Afsluitdatum BPV in Bron voor 1
	 * januari van het studiejaar waarvoor de mutatiestop van kracht is ligt, tenzij een
	 * van de volgende situaties van kracht is:
	 * <ul>
	 * <li>de accountant is verantwoordelijk</li>
	 * <li>het Leerbedrijf in Bron is leeg</li>
	 * </ul>
	 * 
	 * @throws BronException
	 */
	@Test
	public void aanpassingLeerbedrijfVanNullNaarWaardeIsToegestaanTijdensMutatieStopNa1Januari()
			throws BronException
	{
		tester.voerTestUitMetMedewerker();

		zetSchooljaarStatus(MutatiestopIngesteld);
		bpvInschrijving.setAfsluitdatum(eenJanuari);

		zetBedrijfsgegeven(nieuweBedrijfsgegeven);

		BronBpvWijzigingToegestaanCheck check =
			new BronBpvWijzigingToegestaanCheck(bpvInschrijving.getBegindatum(), bpvInschrijving
				.getAfsluitdatum(), null, BPVStatus.Volledig, bpvInschrijving, verbintenis);

		assertThat(check.getResultaat(), is(AanvullingBpvBedrijfsgegeven));

		addChange(bpvInschrijving, "bedrijfsgegeven", null, bpvInschrijving.getBedrijfsgegeven());
		controller.save();
		BronAanleverMelding melding = getEersteMelding();

		assertThat(melding.isBekostigingsRelevant(), is(false));
	}

	/**
	 * Het Leerbedrijf mag niet worden aangepast als de Afsluitdatum BPV in Bron voor 1
	 * januari van het studiejaar waarvoor de mutatiestop van kracht is ligt, tenzij een
	 * van de volgende situaties van kracht is:
	 * <ul>
	 * <li>de accountant is verantwoordelijk</li>
	 * <li>het Leerbedrijf in Bron is leeg</li>
	 * </ul>
	 * 
	 * @throws BronException
	 */
	@Test(expected = BronException.class)
	public void aanpassingLeerbedrijfIsNietToegestaanTijdensMutatieStopVoor1Januari()
			throws BronException
	{
		tester.voerTestUitMetMedewerker();

		zetSchooljaarStatus(MutatiestopIngesteld);
		bpvInschrijving.setAfsluitdatum(huidigSchooljaar.getEenOktober());

		zetBedrijfsgegeven(nieuweBedrijfsgegeven);

		BronBpvWijzigingToegestaanCheck check =
			new BronBpvWijzigingToegestaanCheck(bpvInschrijving.getBegindatum(), bpvInschrijving
				.getAfsluitdatum(), oudeBedrijfsgegeven.getId(), BPVStatus.Volledig,
				bpvInschrijving, verbintenis);

		assertThat(check.getResultaat(),
			is(MedewerkerHeeftGeenToestemmingOmWijzigingDoorTeVoerenBijEenMutatiebeperkingOfStop));

		addChange(bpvInschrijving, "bedrijfsgegeven", oudeBedrijfsgegeven, bpvInschrijving
			.getBedrijfsgegeven());
		controller.save();
	}

	/**
	 * Het Leerbedrijf mag niet worden aangepast als de Afsluitdatum BPV in Bron voor 1
	 * januari van het studiejaar waarvoor de mutatiestop van kracht is ligt, tenzij een
	 * van de volgende situaties van kracht is:
	 * <ul>
	 * <li>de accountant is verantwoordelijk</li>
	 * <li>het Leerbedrijf in Bron is leeg</li>
	 * </ul>
	 * 
	 * @throws BronException
	 */
	@Test
	public void aanpassingLeerbedrijfIsToegestaanTijdensMutatieStopVoor1JanuariMetPermissie()
			throws BronException
	{
		tester.voerTestUitMetMedewerker(new BronEditNaMutatieBeperking());

		zetSchooljaarStatus(MutatiestopIngesteld);
		bpvInschrijving.setAfsluitdatum(huidigSchooljaar.getEenOktober());

		zetBedrijfsgegeven(nieuweBedrijfsgegeven);

		BronBpvWijzigingToegestaanCheck check =
			new BronBpvWijzigingToegestaanCheck(bpvInschrijving.getBegindatum(), bpvInschrijving
				.getAfsluitdatum(), oudeBedrijfsgegeven.getId(), BPVStatus.Volledig,
				bpvInschrijving, verbintenis);

		assertThat(check.getResultaat(),
			is(MedewerkerHeeftToestemmingOmWijzigingDoorTeVoerenBijEenMutatiestop));

		addChange(bpvInschrijving, "bedrijfsgegeven", oudeBedrijfsgegeven, bpvInschrijving
			.getBedrijfsgegeven());
		controller.save();
		BronAanleverMelding melding = getEersteMelding();

		assertThat(melding.isBekostigingsRelevant(), is(true));
	}

	/**
	 * Het Leerbedrijf mag niet worden aangepast als de Afsluitdatum BPV in Bron voor 1
	 * januari van het studiejaar waarvoor de mutatiestop van kracht is ligt, tenzij een
	 * van de volgende situaties van kracht is:
	 * <ul>
	 * <li>de accountant is verantwoordelijk</li>
	 * <li>het Leerbedrijf in Bron is leeg</li>
	 * </ul>
	 * 
	 * @throws BronException
	 */
	@Test
	public void aanpassingLeerbedrijfVanNullNaarWaardeIsToegestaanTijdensMutatieStopVoor1Januari()
			throws BronException
	{
		tester.voerTestUitMetMedewerker();

		zetSchooljaarStatus(MutatiestopIngesteld);
		bpvInschrijving.setAfsluitdatum(huidigSchooljaar.getEenOktober());

		zetBedrijfsgegeven(nieuweBedrijfsgegeven);

		BronBpvWijzigingToegestaanCheck check =
			new BronBpvWijzigingToegestaanCheck(bpvInschrijving.getBegindatum(), bpvInschrijving
				.getAfsluitdatum(), null, BPVStatus.Volledig, bpvInschrijving, verbintenis);

		assertThat(check.getResultaat(), is(AanvullingBpvBedrijfsgegeven));

		addChange(bpvInschrijving, "bedrijfsgegeven", null, bpvInschrijving.getBedrijfsgegeven());
		controller.save();
		BronAanleverMelding melding = getEersteMelding();

		assertThat(melding.isBekostigingsRelevant(), is(false));
	}

	@Test(expected = BronException.class)
	public void aanpassingLeerbedrijfBpvBblNietToegestaanNaOpgesteldAssurancerapportVoor1JanuariMetPermissie()
			throws BronException
	{
		tester.voerTestUitMetMedewerker(new BronEditNaMutatieBeperking());

		bpvInschrijving.setStatus(BPVStatus.Definitief);
		zetSchooljaarStatus(AssurancerapportOpgesteld);
		zetBedrijfsgegeven(nieuweBedrijfsgegeven);

		BronBpvWijzigingToegestaanCheck check =
			new BronBpvWijzigingToegestaanCheck(bpvInschrijving.getBegindatum(), bpvInschrijving
				.getAfsluitdatum(), 321L, BPVStatus.Volledig, bpvInschrijving, verbintenis);

		assertThat(check.getResultaat(), is(SchooljaarIsAfgeslotenVoorMutaties));

		addChange(bpvInschrijving, "bedrijfsgegeven", oudeBedrijfsgegeven, bpvInschrijving
			.getBedrijfsgegeven());
		controller.save();
	}

	@Test(expected = BronException.class)
	public void aanpassingLeerbedrijfBpvBblVanNullNaarWaardeNietToegestaanNaOpgesteldAssurancerapportVoor1JanuariMetPermissie()
			throws BronException
	{
		tester.voerTestUitMetMedewerker(new BronEditNaMutatieBeperking());

		bpvInschrijving.setStatus(BPVStatus.Definitief);
		zetSchooljaarStatus(AssurancerapportOpgesteld);

		zetBedrijfsgegeven(nieuweBedrijfsgegeven);

		BronBpvWijzigingToegestaanCheck check =
			new BronBpvWijzigingToegestaanCheck(bpvInschrijving.getBegindatum(), bpvInschrijving
				.getAfsluitdatum(), null, BPVStatus.Volledig, bpvInschrijving, verbintenis);

		assertThat(check.getResultaat(), is(SchooljaarIsAfgeslotenVoorMutaties));

		addChange(bpvInschrijving, "bedrijfsgegeven", null, bpvInschrijving.getBedrijfsgegeven());
		controller.save();
	}

	@Test(expected = BronException.class)
	public void multiAanpassingGeldigeAfsluitdatumOngeldigeBegindatumNietToegestaan()
			throws BronException
	{
		tester.voerTestUitMetMedewerker();

		zetSchooljaarStatus(MutatiestopIngesteld);

		bpvInschrijving.setAfsluitdatum(huidigSchooljaar.getBegindatum());
		Date oudeAfsluitdatum = bpvInschrijving.getAfsluitdatum();
		bpvInschrijving.setAfsluitdatum(huidigSchooljaar.getEenOktober());

		bpvInschrijving.setBegindatum(huidigSchooljaar.getBegindatum());
		Date oudeBegindatum = bpvInschrijving.getBegindatum();
		bpvInschrijving.setBegindatum(huidigSchooljaar.getEenOktober());

		BronBpvWijzigingToegestaanCheck check =
			new BronBpvWijzigingToegestaanCheck(oudeBegindatum, oudeAfsluitdatum, 321L,
				BPVStatus.Volledig, bpvInschrijving, verbintenis);

		assertThat(check.getResultaat(),
			is(MedewerkerHeeftGeenToestemmingOmWijzigingDoorTeVoerenBijEenMutatiebeperkingOfStop));

		addChange(bpvInschrijving, "afsluitdatum", oudeAfsluitdatum, bpvInschrijving
			.getAfsluitdatum());
		addChange(bpvInschrijving, "begindatum", oudeBegindatum, bpvInschrijving.getBegindatum());
		controller.save();
	}

	@Test
	public void multiAanpassingGeldigeAfsluitdatumOngeldigeBegindatumWelToegestaanMetPermissie()
			throws BronException
	{
		// de test uitvoeren met mutaties toegestaan permissie
		tester.voerTestUitMetMedewerker(new BronEditNaMutatieBeperking());

		zetSchooljaarStatus(MutatiestopIngesteld);

		bpvInschrijving.setAfsluitdatum(huidigSchooljaar.getBegindatum());
		Date oudeAfsluitdatum = bpvInschrijving.getAfsluitdatum();
		bpvInschrijving.setAfsluitdatum(huidigSchooljaar.getEenOktober());

		bpvInschrijving.setBegindatum(huidigSchooljaar.getBegindatum());
		Date oudeBegindatum = bpvInschrijving.getBegindatum();
		bpvInschrijving.setBegindatum(huidigSchooljaar.getEenOktober());

		BronBpvWijzigingToegestaanCheck check =
			new BronBpvWijzigingToegestaanCheck(oudeBegindatum, oudeAfsluitdatum, 321L,
				BPVStatus.Volledig, bpvInschrijving, verbintenis);

		assertThat(check.getResultaat(),
			is(MedewerkerHeeftToestemmingOmWijzigingDoorTeVoerenBijEenMutatiestop));

		addChange(bpvInschrijving, "afsluitdatum", oudeAfsluitdatum, bpvInschrijving
			.getAfsluitdatum());
		addChange(bpvInschrijving, "begindatum", oudeBegindatum, bpvInschrijving.getBegindatum());
		controller.save();

		BronAanleverMelding melding = getEersteMelding();

		assertThat(melding.isBekostigingsRelevant(), is(true));
	}

	@Test(expected = BronException.class)
	public void multiAanpassingGeldigeAfsluitdatumOngeldigLeerbedrijfNietToegestaan()
			throws BronException
	{
		tester.voerTestUitMetMedewerker();

		zetSchooljaarStatus(MutatiestopIngesteld);

		bpvInschrijving.setAfsluitdatum(huidigSchooljaar.getBegindatum());
		Date oudeAfsluitdatum = bpvInschrijving.getAfsluitdatum();
		bpvInschrijving.setAfsluitdatum(huidigSchooljaar.getEenOktober());

		zetBedrijfsgegeven(nieuweBedrijfsgegeven);

		BronBpvWijzigingToegestaanCheck check =
			new BronBpvWijzigingToegestaanCheck(bpvInschrijving.getBegindatum(), oudeAfsluitdatum,
				321L, BPVStatus.Volledig, bpvInschrijving, verbintenis);

		assertThat(check.getResultaat(),
			is(MedewerkerHeeftGeenToestemmingOmWijzigingDoorTeVoerenBijEenMutatiebeperkingOfStop));

		addChange(bpvInschrijving, "afsluitdatum", oudeAfsluitdatum, bpvInschrijving
			.getAfsluitdatum());
		addChange(bpvInschrijving, "bedrijfsgegeven", oudeBedrijfsgegeven, bpvInschrijving
			.getBedrijfsgegeven());

		controller.save();
	}

	@Test
	public void multiAanpassingGeldigeAfsluitdatumOngeldigLeerbedrijfWelToegestaanMetPermissie()
			throws BronException
	{
		tester.voerTestUitMetMedewerker(new BronEditNaMutatieBeperking());

		zetSchooljaarStatus(MutatiestopIngesteld);

		bpvInschrijving.setAfsluitdatum(huidigSchooljaar.getBegindatum());
		Date oudeAfsluitdatum = bpvInschrijving.getAfsluitdatum();
		bpvInschrijving.setAfsluitdatum(huidigSchooljaar.getEenOktober());

		zetBedrijfsgegeven(nieuweBedrijfsgegeven);

		BronBpvWijzigingToegestaanCheck check =
			new BronBpvWijzigingToegestaanCheck(bpvInschrijving.getBegindatum(), oudeAfsluitdatum,
				oudeBedrijfsgegeven.getId(), BPVStatus.Volledig, bpvInschrijving, verbintenis);

		assertThat(check.getResultaat(),
			is(MedewerkerHeeftToestemmingOmWijzigingDoorTeVoerenBijEenMutatiestop));

		addChange(bpvInschrijving, "afsluitdatum", oudeAfsluitdatum, bpvInschrijving
			.getAfsluitdatum());
		addChange(bpvInschrijving, "bedrijfsgegeven", oudeBedrijfsgegeven, bpvInschrijving
			.getBedrijfsgegeven());

		controller.save();

		BronAanleverMelding melding = getEersteMelding();
		assertThat(melding.isBekostigingsRelevant(), is(true));
	}

	/**
	 * Ik heb nu een BBL-POK verwijderd met een ingangsdatum 01-08-2009 en een
	 * afsluitdatum 27-10-2009. Dit zou dus een accountantsmutatie moeten opleveren, want
	 * afsluitdatum ligt vóór 1 januari 2010 en mutatie wordt doorgevoerd na 1 januari
	 * 2010. Wordt echter als reguliere mutatie (Verwijdering) klaargezet voor BRON.
	 * Betreft deelnemer 39246 in de acceptatie-omgeving.
	 */
	@Test
	public void mutatieUitMantisMelding() throws Exception
	{
		tester.voerTestUitMetMedewerker(new BronEditNaMutatieBeperking());

		int beginJaar = Integer.parseInt(Cohort.getHuidigCohort().getBeginjaar()) * 10000;
		verbintenis.setBegindatum(asDate(beginJaar + 801));
		bpvInschrijving.setBegindatum(asDate(beginJaar + 801));
		bpvInschrijving.setAfsluitdatum(asDate(beginJaar + 1027));
		bpvInschrijving.setStatus(BPVStatus.Afgemeld);
		zetSchooljaarStatus(MutatiestopIngesteld);

		BronBpvWijzigingToegestaanCheck check =
			new BronBpvWijzigingToegestaanCheck(bpvInschrijving.getBegindatum(), bpvInschrijving
				.getAfsluitdatum(), bpvInschrijving.getBpvBedrijf().getId(), BPVStatus.Volledig,
				bpvInschrijving, verbintenis);

		assertThat(check.getResultaat(),
			is(MedewerkerHeeftToestemmingOmWijzigingDoorTeVoerenBijEenMutatiestop));

		addChange(bpvInschrijving, "status", BPVStatus.Definitief, BPVStatus.Afgemeld);
		controller.save();

		BronAanleverMelding melding = getEersteMelding();
		assertThat(melding.isBekostigingsRelevant(), is(true));
	}
}
