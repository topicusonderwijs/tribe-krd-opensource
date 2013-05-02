package nl.topicus.eduarte.krd.bron.schakeltest.bve.aanlevering.bo;

import static java.util.Arrays.*;
import static nl.topicus.cobra.util.TimeUtil.*;
import static nl.topicus.eduarte.entities.inschrijving.Verbintenis.VerbintenisStatus.*;
import static nl.topicus.eduarte.krd.entities.bron.BronStatus.*;
import static nl.topicus.eduarte.tester.hibernate.DatabaseAction.*;
import static nl.topicus.onderwijs.duo.bron.bve.waardelijsten.Intensiteit.*;
import static nl.topicus.onderwijs.duo.bron.bve.waardelijsten.SoortMutatie.*;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Date;

import nl.topicus.cobra.util.TimeUtil;
import nl.topicus.eduarte.entities.BronEntiteitStatus;
import nl.topicus.eduarte.entities.adres.Adres;
import nl.topicus.eduarte.entities.bpv.BPVInschrijving.BPVStatus;
import nl.topicus.eduarte.entities.inschrijving.Bekostigingsperiode;
import nl.topicus.eduarte.entities.inschrijving.RedenUitschrijving;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.entities.inschrijving.RedenUitschrijving.UitstroomredenWI;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis.Bekostigd;
import nl.topicus.eduarte.entities.landelijk.Land;
import nl.topicus.eduarte.entities.personen.Persoon;
import nl.topicus.eduarte.entities.personen.PersoonAdres;
import nl.topicus.eduarte.entities.taxonomie.MBOLeerweg;
import nl.topicus.eduarte.krd.bron.BronController;
import nl.topicus.eduarte.krd.bron.BronVerbintenisWijzigingToegestaanCheck;
import nl.topicus.eduarte.krd.bron.BronVerbintenisWijzigingToegestaanCheck.WijzigingToegestaanResultaat;
import nl.topicus.eduarte.krd.bron.schakeltest.BronVerbintenisBuilder;
import nl.topicus.eduarte.krd.entities.bron.BronMeldingStatus;
import nl.topicus.eduarte.krd.entities.bron.meldingen.aanlevering.BronAanleverMelding;
import nl.topicus.eduarte.krd.entities.bron.meldingen.aanlevering.BronBveAanleverRecord;
import nl.topicus.onderwijs.duo.bron.BronException;
import nl.topicus.onderwijs.duo.bron.bve.batches.pve_9_9.aanlevering.WijzigingSleutelgegevensRecord;
import nl.topicus.onderwijs.duo.bron.bve.batches.pve_9_9.aanlevering.bo.BpvGegevensRecord;
import nl.topicus.onderwijs.duo.bron.bve.batches.pve_9_9.aanlevering.bo.InschrijvingsgegevensRecord;
import nl.topicus.onderwijs.duo.bron.bve.waardelijsten.Intensiteit;
import nl.topicus.onderwijs.duo.bron.bve.waardelijsten.Leerweg;
import nl.topicus.onderwijs.duo.bron.bve.waardelijsten.RedenUitval;
import nl.topicus.onderwijs.duo.bron.data.types.Datum;

import org.junit.Test;

@SuppressWarnings("hiding")
public class ProefgevallenBveBODiversenTest extends ProefgevallenBveBO
{
	@Test
	public void mantis54689() throws Exception
	{
		getDeelnemer1001();

		Persoon persoon = deelnemer.getPersoon();
		PersoonAdres persoonAdres = persoon.getFysiekAdres();
		persoonAdres.setBegindatum(asDate(20000101));
		persoonAdres.setEinddatum(null);
		persoonAdres.setPostadres(true);
		persoonAdres.setFysiekadres(true);
		persoonAdres.setFactuuradres(true);

		Adres adres = persoonAdres.getAdres();
		adres.setStraat("Melmesfeld");
		adres.setHuisnummer("14");
		adres.setPostcode("D-47647");
		adres.setLand(Land.getLand("5010"));
		adres.setPlaats("KERKEN");

		verbintenis.setStatus(Definitief);
		addChange(verbintenis, "status", null, Definitief);

		controller.save();

		BronAanleverMelding melding = getEersteMelding();

		assertThat(melding.getPostcodeVolgensInstelling(), is(nullValue()));
		assertThat(melding.getLand(), is("5010"));
	}

	@Test
	public void mantis54941() throws Exception
	{
		getDeelnemer1022();

		Date vandaag = TimeUtil.getInstance().currentDate();

		verbintenis.setStatus(Definitief);
		bpvInschrijving.setOpnemenInBron(false);
		bpvInschrijving.setStatus(BPVStatus.Definitief);
		bpvInschrijving.setBegindatum(vandaag);

		addChange(bpvInschrijving, "status", null, BPVStatus.Definitief);
		addChange(bpvInschrijving, "begindatum", null, bpvInschrijving.getBegindatum());

		assertThat(bpvInschrijving.getBronStatus(), is(BronEntiteitStatus.Geen));

		controller.save();

		BronAanleverMelding melding = getEersteMelding();

		assertThat(melding, is(nullValue()));
		assertThat(bpvInschrijving.getBronStatus(), is(BronEntiteitStatus.Geen));
	}

	@Test
	public void mantis57209() throws Exception
	{
		getDeelnemer1022();

		Date vandaag = TimeUtil.getInstance().currentDate();

		verbintenis.setStatus(Definitief);
		bpvInschrijving.setEinddatum(vandaag);

		addChange(bpvInschrijving, "status", BPVStatus.Definitief, BPVStatus.Beëindigd);
		addChange(bpvInschrijving, "einddatum", null, bpvInschrijving.getEinddatum());

		controller.save();

		BronAanleverMelding melding = getEersteMelding();

		assertThat(getRecordTypes(melding), is(equalTo(asList(305, 322))));
		assertThat(tester.getTransactionLog(), is(asList(insert(melding))));
		assertThat(melding.getIngangsDatum(), is(vandaag));

		assertThat(tester.getTransactionLog(), is(asList(insert(melding))));
	}

	@Test
	public void mantis58679_test1() throws Exception
	{
		getDeelnemer1001();

		Persoon persoon = deelnemer.getPersoon();
		PersoonAdres persoonAdres = persoon.getFysiekAdres();
		persoonAdres.setBegindatum(asDate(20000101));
		persoonAdres.setEinddatum(null);
		persoonAdres.setPostadres(true);
		persoonAdres.setFysiekadres(true);
		persoonAdres.setFactuuradres(true);

		Adres adres = persoonAdres.getAdres();
		adres.setStraat("Melmesfeld");
		adres.setHuisnummer("14");
		adres.setPostcode("D-47647");
		adres.setLand(Land.getLand("5010"));
		adres.setPlaats("KERKEN");

		verbintenis.setStatus(Definitief);

		addChange(adres, "postcode", "D-47647", "6708KH");
		addChange(adres, "land", Land.getLand("5010"), Land.getNederland());

		adres.setLand(Land.getNederland());
		adres.setPostcode("6708KH");

		controller.save();

		BronAanleverMelding melding = getEersteMelding();

		assertThat(getRecordTypes(melding), is(Arrays.asList(305, 306)));

		assertThat(melding.getPostcodeVolgensInstelling(), is(nullValue()));
		assertThat(melding.getLand(), is("5010"));

		WijzigingSleutelgegevensRecord record =
			melding.getRecord(WijzigingSleutelgegevensRecord.class);
		assertThat(record.getPostcodeVolgensInstellingGewijzigd(), is("6708KH"));
		assertThat(record.getLandGewijzigd(), is(nullValue()));
	}

	@Test
	public void mantis58679_test2() throws Exception
	{
		getDeelnemer1001();

		Persoon persoon = deelnemer.getPersoon();
		PersoonAdres persoonAdres = persoon.getFysiekAdres();
		persoonAdres.setBegindatum(asDate(20000101));
		persoonAdres.setEinddatum(null);
		persoonAdres.setPostadres(true);
		persoonAdres.setFysiekadres(true);
		persoonAdres.setFactuuradres(true);

		Adres adres = persoonAdres.getAdres();
		adres.setStraat("Kolderpolder");
		adres.setHuisnummer("13");
		adres.setPostcode("6708KH");
		adres.setPlaats("BERGEN AAN ZEE");
		adres.setLand(Land.getNederland());

		verbintenis.setStatus(Definitief);

		String nieuweStraat = "Welvesvelt";
		String nieuweHuisnummer = "14";
		String nieuwePostcode = "D-47647";
		String nieuwePlaats = "KERKEN";
		Land nieuweLand = Land.getLand("5010");

		addChange(adres, "straat", adres.getStraat(), nieuweStraat);
		addChange(adres, "huisnummer", adres.getHuisnummer(), nieuweHuisnummer);
		addChange(adres, "postcode", adres.getPostcode(), nieuwePostcode);
		addChange(adres, "plaats", adres.getPlaats(), nieuwePlaats);
		addChange(adres, "land", adres.getLand(), nieuweLand);

		adres.setStraat(nieuweStraat);
		adres.setHuisnummer(nieuweHuisnummer);
		adres.setPostcode(nieuwePostcode);
		adres.setPlaats(nieuwePlaats);
		adres.setLand(nieuweLand);

		controller.save();

		BronAanleverMelding melding = getEersteMelding();

		assertThat(getRecordTypes(melding), is(Arrays.asList(305, 306)));

		assertThat(melding.getPostcodeVolgensInstelling(), is("6708KH"));
		assertThat(melding.getLand(), is(nullValue()));

		WijzigingSleutelgegevensRecord record =
			melding.getRecord(WijzigingSleutelgegevensRecord.class);
		assertThat(record.getPostcodeVolgensInstellingGewijzigd(), is(nullValue()));
		assertThat(record.getLandGewijzigd(), is(nieuweLand.getCode()));
	}

	@Test
	public void mantis62496() throws Exception
	{
		getDeelnemer1001();

		addChange(verbintenis, "status", Voorlopig, Definitief);
		controller.save();

		BronAanleverMelding melding = getEersteMelding();
		BronBveAanleverRecord record =
			(BronBveAanleverRecord) melding.getRecord(InschrijvingsgegevensRecord.class);

		assertThat(record.getIntensiteit(), is(equalTo(Intensiteit.Voltijd)));
		assertThat(record.getLeerweg(), is(equalTo(Leerweg.Beroepsbegeleidend)));

		verbintenis.setIntensiteit(Intensiteit.Examendeelnemer);

		record.vulBoInschrijfgegevens();

		assertThat(record.getIntensiteit(), is(equalTo(Intensiteit.Examendeelnemer)));
		assertThat(record.getLeerweg(), is(nullValue()));
	}

	@Test
	public void mantis62846() throws Exception
	{
		getDeelnemer1001();

		addChange(verbintenis, "intensiteit", Deeltijd, Voltijd);

		controller.save();

		BronAanleverMelding melding = getEersteMelding();
		BronBveAanleverRecord record =
			(BronBveAanleverRecord) melding.getRecord(InschrijvingsgegevensRecord.class);
		assertThat(record.getSoortMutatie(), is(Aanpassing));

		controller = new BronController();

		addChange(verbintenis, "status", Definitief, Afgemeld);

		controller.save();

		assertThat(record.getSoortMutatie(), is(Verwijdering));
	}

	@Test
	public void mantis64270() throws Exception
	{
		getDeelnemer1022();

		Date vandaag = TimeUtil.getInstance().currentDate();

		verbintenis.setStatus(Definitief);
		bpvInschrijving.setOpnemenInBron(true);
		bpvInschrijving.setStatus(BPVStatus.Definitief);
		bpvInschrijving.setBegindatum(vandaag);

		verbintenis.save();
		tester.clearTransactionLog();

		addChange(bpvInschrijving.getBedrijfsgegeven(), "codeLeerbedrijf", null,
			"02PR9725JJ0001601");

		controller.save();

		BronAanleverMelding melding = getEersteMelding();

		BpvGegevensRecord record = melding.getBpvRecord(bpvInschrijving.getVolgnummer());

		assertThat(record.getLeerbedrijf(), is("02PR9725JJ0001601"));

		controller = new BronController();

		bpvInschrijving.getBedrijfsgegeven().setCodeLeerbedrijf(null);

		addChange(bpvInschrijving.getBedrijfsgegeven(), "codeLeerbedrijf", "02PR9725JJ0001601",
			null);

		controller.save();

		assertThat(record.getLeerbedrijf(), is(nullValue()));
	}

	@Test
	public void mantis64078() throws Exception
	{
		getDeelnemer1001();
		Date oudeBegindatum = verbintenis.getBegindatum();
		verbintenis.setBegindatum(asDate(20100106));
		addChange(verbintenis, "begindatum", oudeBegindatum, verbintenis.getBegindatum());
		addChange(verbintenis, "status", Voorlopig, Definitief);
		zetSchooljaarStatus(huidigSchooljaar, VolledigheidsverklaringGeregistreerd);
		controller.save();
		controller = new BronController();

		Bekostigingsperiode periode = new Bekostigingsperiode();
		periode.setVerbintenis(verbintenis);
		periode.setBegindatum(verbintenis.getBegindatum());
		periode.setBekostigd(true);

		verbintenis.getBekostigingsperiodes().add(periode);

		addChange(periode, "begindatum", null, periode.getBegindatum());

		controller.save();

		assertThat(verbintenis.getBekostigingsperiodes().size(), is(1));

	}

	@Test
	public void mantis65054() throws Exception
	{
		getDeelnemer1001();
		deelnemer.getPersoon().setBsn(null);

		verbintenis.setBegindatum(asDate(20100401));
		verbintenis.setStatus(Volledig);

		addChange(verbintenis, "status", Voorlopig, Volledig);
		addChange(verbintenis, "begindatum", asDate(20090801), asDate(20100401));

		controller.save();

		BronAanleverMelding melding = getEersteMelding();
		assertThat(getRecordTypes(melding), is(asList(305, 310, 320, 321)));
		assertThat(getMutaties(melding), is(asList(null, Toevoeging, Toevoeging)));
	}

	@Test
	public void mantis65328() throws Exception
	{
		tester.voerTestUitMetMedewerker();

		zetSchooljaarStatus(vorigSchooljaar, AssurancerapportOpgesteld);
		zetSchooljaarStatus(huidigSchooljaar, MutatiestopIngesteld);
		zetSchooljaarStatus(volgendSchooljaar, GegevensWordenIngevoerd);

		getDeelnemer1002();

		verbintenis.setStatus(Definitief);

		RedenUitschrijving reden = new RedenUitschrijving();
		reden.setCode("");
		reden.setNaam("");
		reden.setRedenUitval(RedenUitval.StudieBeroepsgebonden);
		reden.setUitstroomredenWI(UitstroomredenWI.AndereRoute);

		verbintenis.setEinddatum(vandaag());
		verbintenis.setRedenUitschrijving(reden);

		Verbintenis origineel = verbintenis;

		addChange(origineel, "einddatum", null, origineel.getEinddatum());
		addChange(origineel, "redenUitschrijving", null, origineel.getRedenUitschrijving());

		BronVerbintenisBuilder verbintenisBuilder = builder.addVerbintenisMBO();
		verbintenisBuilder.setBekostigd(Bekostigd.Nee);
		verbintenisBuilder.setGeplandeEinddatum(asInt(verbintenis.getGeplandeEinddatum()));
		verbintenisBuilder.setIndicatieGehandicapt(verbintenis.getIndicatieGehandicapt());
		verbintenisBuilder.setIngangsdatum(asInt(morgen()));
		verbintenisBuilder.setIntensiteit(verbintenis.getIntensiteit());
		verbintenisBuilder.setOpleidingMBO(10342, MBOLeerweg.BOL);
		verbintenisBuilder.build();

		Verbintenis nieuwe = builder.getVerbintenis();

		assertThat(origineel, is(not(equalTo(nieuwe))));

		addChange(origineel, "einddatum", null, origineel.getEinddatum());
		addChange(origineel, "redenUitschrijving", null, origineel.getRedenUitschrijving());

		addChange(nieuwe, "begindatum", null, nieuwe.getBegindatum());
		addChange(nieuwe, "bekostigd", null, nieuwe.getBekostigd());
		addChange(nieuwe, "geplandeEinddatum", null, nieuwe.getGeplandeEinddatum());
		addChange(nieuwe, "indicatieGehandicapt", null, nieuwe.getIndicatieGehandicapt());
		addChange(nieuwe, "intensiteit", null, nieuwe.getIntensiteit());
		addChange(nieuwe, "opleiding", null, nieuwe.getOpleiding());
		addChange(nieuwe, "relevanteVooropleiding", null, nieuwe.getRelevanteVooropleiding());
		addChange(nieuwe, "status", null, nieuwe.getStatus());
		addChange(nieuwe, "volgnummer", null, nieuwe.getVolgnummer());

		controller.save();

		BronAanleverMelding melding1 = getEersteMelding();
		assertThat(getRecordTypes(melding1), is(asList(305, 320)));
		assertThat(getMutaties(melding1), is(asList(Aanpassing)));

		BronAanleverMelding melding2 = getTweedeMelding();
		assertThat(getRecordTypes(melding2), is(asList(305, 320, 321)));
		assertThat(getMutaties(melding2), is(asList(Toevoeging, Toevoeging)));
	}

	int asInt(Date date)
	{
		return (int) Datum.valueOf(date).getWaarde();
	}

	@Test(expected = BronException.class)
	public void mantis65376() throws Exception
	{
		tester.voerTestUitMetMedewerker();

		zetSchooljaarStatus(vorigSchooljaar, AssurancerapportOpgesteld);
		zetSchooljaarStatus(huidigSchooljaar, MutatiestopIngesteld);
		zetSchooljaarStatus(volgendSchooljaar, GegevensWordenIngevoerd);

		getDeelnemer1001();

		verbintenis.setBegindatum(vorigSchooljaar.getBegindatum());
		verbintenis.setStatus(Voorlopig);
		verbintenis.setBekostigd(Bekostigd.Nee);

		addChange(verbintenis, "status", null, verbintenis.getStatus());
		addChange(verbintenis, "begindatum", null, verbintenis.getBegindatum());
		addChange(verbintenis, "bekostigd", null, verbintenis.getBekostigd());

		controller.save();

		assertThat(getEersteMelding(), is(nullValue()));

		controller = new BronController();

		verbintenis.setStatus(Volledig);
		verbintenis.setBekostigd(Bekostigd.Ja);

		addChange(verbintenis, "status", Voorlopig, Volledig);
		addChange(verbintenis, "bekostigd", Bekostigd.Nee, Bekostigd.Ja);

		BronVerbintenisWijzigingToegestaanCheck check =
			new BronVerbintenisWijzigingToegestaanCheck(verbintenis.getBegindatum(), null,
				Voorlopig, verbintenis.getOpleiding(), verbintenis.getIntensiteit(), Bekostigd.Nee,
				verbintenis);
		assertThat(check.getResultaat(),
			is(WijzigingToegestaanResultaat.SchooljaarIsAfgeslotenVoorMutaties));

		controller.save();

		// hier kom je niet meer...
	}

	@Test
	public void mantis64573_1() throws Exception
	{
		tester.voerTestUitMetMedewerker();

		zetSchooljaarStatus(huidigSchooljaar, GegevensWordenIngevoerd);

		getDeelnemer1001();

		verbintenis.setBegindatum(huidigSchooljaar.getBegindatum());
		verbintenis.setStatus(Volledig);

		addChange(verbintenis, "status", Voorlopig, Volledig);
		addChange(verbintenis, "begindatum", null, verbintenis.getBegindatum());

		controller.save();

		BronAanleverMelding melding = getEersteMelding();
		assertThat(getRecordTypes(melding), is(asList(305, 320, 321)));

		controller = new BronController();

		addChange(persoon, "bsn", persoon.getBsn(), null);
		persoon.setBsn(null);

		controller.save();

		assertThat(getRecordTypes(melding), is(asList(305, 310, 320, 321)));
	}

	@Test
	public void mantis64573_2() throws Exception
	{
		tester.voerTestUitMetMedewerker();

		zetSchooljaarStatus(huidigSchooljaar, GegevensWordenIngevoerd);

		getDeelnemer1001();

		verbintenis.setBegindatum(huidigSchooljaar.getBegindatum());
		verbintenis.setStatus(Volledig);

		addChange(persoon, "bsn", persoon.getBsn(), null);
		persoon.setBsn(null);

		controller.save();

		assertThat(getEersteMelding(), is(nullValue()));
	}

	@Test
	public void mantis64573_3() throws Exception
	{
		tester.voerTestUitMetMedewerker();

		zetSchooljaarStatus(huidigSchooljaar, GegevensWordenIngevoerd);

		getDeelnemer1001();

		verbintenis.setBegindatum(huidigSchooljaar.getBegindatum());
		verbintenis.setStatus(Volledig);

		addChange(verbintenis, "status", Voorlopig, Volledig);
		addChange(verbintenis, "begindatum", null, verbintenis.getBegindatum());

		controller.save();

		BronAanleverMelding melding = getEersteMelding();
		assertThat(getRecordTypes(melding), is(asList(305, 320, 321)));

		BronAanleverMelding melding2 = melding.voegOpnieuwToeAanWachtrij();

		assertThat(getRecordTypes(melding2), is(asList(305, 320, 321)));
	}

	@Test
	public void mantis64573_4() throws Exception
	{
		tester.voerTestUitMetMedewerker();

		zetSchooljaarStatus(huidigSchooljaar, GegevensWordenIngevoerd);

		getDeelnemer1001();

		verbintenis.setBegindatum(huidigSchooljaar.getBegindatum());
		verbintenis.setStatus(Volledig);

		addChange(verbintenis, "status", Voorlopig, Volledig);
		addChange(verbintenis, "begindatum", null, verbintenis.getBegindatum());

		controller.save();

		BronAanleverMelding melding = getEersteMelding();
		assertThat(getRecordTypes(melding), is(asList(305, 320, 321)));

		persoon.setBsn(null);

		BronAanleverMelding melding2 = melding.voegOpnieuwToeAanWachtrij();

		assertThat(melding, is(sameInstance(melding2)));
		assertThat(getRecordTypes(melding2), is(asList(305, 310, 320, 321)));
	}

	@Test
	public void mantis64573_5() throws Exception
	{
		tester.voerTestUitMetMedewerker();

		zetSchooljaarStatus(huidigSchooljaar, GegevensWordenIngevoerd);

		getDeelnemer1001();

		verbintenis.setBegindatum(huidigSchooljaar.getBegindatum());
		verbintenis.setStatus(Volledig);

		addChange(verbintenis, "status", Voorlopig, Volledig);
		addChange(verbintenis, "begindatum", null, verbintenis.getBegindatum());

		controller.save();

		BronAanleverMelding melding = getEersteMelding();
		assertThat(getRecordTypes(melding), is(asList(305, 320, 321)));

		melding.setBronMeldingStatus(BronMeldingStatus.AFGEKEURD);
		persoon.setBsn(null);

		BronAanleverMelding melding2 = melding.voegOpnieuwToeAanWachtrij();

		assertThat(melding, is(not(sameInstance(melding2))));
		assertThat(getRecordTypes(melding2), is(asList(305, 310, 320, 321)));
	}
}
