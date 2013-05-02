package nl.topicus.eduarte.krd.bron.schakeltest.bve.aanlevering.bo;

import static nl.topicus.eduarte.entities.inschrijving.Verbintenis.Bekostigd.*;
import static nl.topicus.eduarte.entities.inschrijving.Verbintenis.VerbintenisStatus.*;
import static nl.topicus.eduarte.krd.bron.BronExamendeelnameWijzigingToegestaanCheck.WijzigingToegestaanResultaat.*;
import static nl.topicus.eduarte.krd.entities.bron.BronStatus.*;
import static nl.topicus.onderwijs.duo.bron.bve.waardelijsten.Intensiteit.*;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import nl.topicus.eduarte.entities.examen.Examenstatus;
import nl.topicus.eduarte.krd.bron.BronExamendeelnameWijzigingToegestaanCheck;
import nl.topicus.eduarte.krd.bron.BronExamendeelnameWijzigingToegestaanCheck.WijzigingToegestaanResultaat;
import nl.topicus.eduarte.krd.entities.bron.meldingen.aanlevering.BronAanleverMelding;
import nl.topicus.eduarte.krd.principals.deelnemer.bron.BronEditNaMutatieBeperking;
import nl.topicus.onderwijs.duo.bron.BronException;

import org.junit.Before;
import org.junit.Test;

public class ProefgevallenBveBOMantis60229Test extends ProefgevallenBveBO
{
	private Date oudeDatumUitslag;

	private Examenstatus oudeExamenstatus;

	private final Set<String> gewijzigdeKenmerken = new HashSet<String>();

	private boolean oudeBekostigd;

	@Override
	@Before
	public void setup()
	{
		gewijzigdeKenmerken.clear();

		getDeelnemer1016();

		verbintenis.setBegindatum(huidigSchooljaar.getBegindatum());
		verbintenis.setGeplandeEinddatum(huidigSchooljaar.getVolgendSchooljaar().getEinddatum());
		verbintenis.setBekostigd(Ja);
		verbintenis.setIntensiteit(Voltijd);
		verbintenis.setStatus(Definitief);

		deelname1.setDatumUitslag(huidigSchooljaar.getEenOktober());
	}

	@Test
	public void toevoegingBekostigdExamendeelnameMagTijdensVrijeInvoerVoorUitslagOp1OktoberHuidigSchooljaar()
			throws Exception
	{
		tester.voerTestUitMetMedewerker();

		wijzigDatumUitslag(null, huidigSchooljaar.getEenOktober());
		wijzigExamenstatus(null, getGeslaagd());

		zetSchooljaarStatus(vorigSchooljaar, AssurancerapportOpgesteld);
		zetSchooljaarStatus(huidigSchooljaar, GegevensWordenIngevoerd);
		zetSchooljaarStatus(volgendSchooljaar, GegevensWordenIngevoerd);

		assertThat(mutatieCheck(), is(AlleMutatiesZijnNogToegestaan));

		controller.save();

		BronAanleverMelding melding = getEersteMelding();
		assertThat(melding.isBekostigingsRelevant(), is(false));
	}

	@Test
	public void toevoegingBekostigdExamendeelnameMagTijdensVrijeInvoerUitslagOp1JanuariVanKalenderjaar1Oktober()
			throws Exception
	{
		tester.voerTestUitMetMedewerker();

		wijzigDatumUitslag(null, vorigSchooljaar.getEenJanuari());
		wijzigExamenstatus(null, getGeslaagd());

		zetSchooljaarStatus(vorigSchooljaar, AssurancerapportOpgesteld);
		zetSchooljaarStatus(huidigSchooljaar, GegevensWordenIngevoerd);
		zetSchooljaarStatus(volgendSchooljaar, GegevensWordenIngevoerd);

		assertThat(mutatieCheck(), is(AlleMutatiesZijnNogToegestaan));

		controller.save();

		BronAanleverMelding melding = getEersteMelding();
		assertThat(melding.isBekostigingsRelevant(), is(false));
	}

	@Test
	public void toevoegingBekostigdExamendeelnameMagTijdensMutatiebeperkingOp1JanuariVanVolgendKalenderJaar()
			throws Exception
	{
		tester.voerTestUitMetMedewerker();

		wijzigDatumUitslag(null, huidigSchooljaar.getEenJanuari());
		wijzigExamenstatus(null, getGeslaagd());

		zetSchooljaarStatus(vorigSchooljaar, AssurancerapportOpgesteld);
		zetSchooljaarStatus(huidigSchooljaar, MutatiebeperkingIngesteld);
		zetSchooljaarStatus(volgendSchooljaar, GegevensWordenIngevoerd);

		controller.save();

		assertThat(mutatieCheck(), is(AlleMutatiesZijnNogToegestaan));

		BronAanleverMelding melding = getEersteMelding();
		assertThat(melding.isBekostigingsRelevant(), is(false));
	}

	@Test(expected = BronException.class)
	public void toevoegingBekostigdExamendeelnameMagNietTijdensMutatiebeperkingOp1OktoberVanHuidigKalenderJaar()
			throws Exception
	{
		tester.voerTestUitMetMedewerker();

		wijzigDatumUitslag(null, huidigSchooljaar.getEenOktober());
		wijzigExamenstatus(null, getGeslaagd());

		zetSchooljaarStatus(vorigSchooljaar, AssurancerapportOpgesteld);
		zetSchooljaarStatus(huidigSchooljaar, MutatiebeperkingIngesteld);
		zetSchooljaarStatus(volgendSchooljaar, GegevensWordenIngevoerd);

		controller.save();

		assertThat(mutatieCheck(),
			is(MedewerkerHeeftGeenToestemmingOmWijzigingDoorTeVoerenBijEenMutatiebeperkingOfStop));
	}

	@Test
	public void toevoegingBekostigdExamendeelnameMagTijdensMutatiebeperkingOp1OktoberVanHuidigKalenderJaarMetPermissie()
			throws Exception
	{
		tester.voerTestUitMetMedewerker(new BronEditNaMutatieBeperking());

		wijzigDatumUitslag(null, huidigSchooljaar.getEenOktober());
		wijzigExamenstatus(null, getGeslaagd());

		zetSchooljaarStatus(vorigSchooljaar, AssurancerapportOpgesteld);
		zetSchooljaarStatus(huidigSchooljaar, MutatiebeperkingIngesteld);
		zetSchooljaarStatus(volgendSchooljaar, GegevensWordenIngevoerd);

		controller.save();

		assertThat(mutatieCheck(),
			is(MedewerkerHeeftToestemmingOmWijzigingDoorTeVoerenBijEenMutatiebeperking));

		BronAanleverMelding melding = getEersteMelding();
		assertThat(melding.isBekostigingsRelevant(), is(false));
	}

	@Test
	public void toevoegingBekostigdExamendeelnameMagTijdensMutatiestopOp1JanuariVanVolgendKalenderJaar()
			throws Exception
	{
		tester.voerTestUitMetMedewerker();

		wijzigDatumUitslag(null, huidigSchooljaar.getEenJanuari());
		wijzigExamenstatus(null, getGeslaagd());

		zetSchooljaarStatus(vorigSchooljaar, AssurancerapportOpgesteld);
		zetSchooljaarStatus(huidigSchooljaar, MutatiestopIngesteld);
		zetSchooljaarStatus(volgendSchooljaar, GegevensWordenIngevoerd);

		assertThat(mutatieCheck(), is(AlleMutatiesZijnNogToegestaan));

		controller.save();

		BronAanleverMelding melding = getEersteMelding();
		assertThat(melding.isBekostigingsRelevant(), is(false));
	}

	@Test(expected = BronException.class)
	public void toevoegingBekostigdExamendeelnameMagNietTijdensMutatiestopOp1OktoberVanHuidigKalenderJaar()
			throws Exception
	{
		tester.voerTestUitMetMedewerker();

		wijzigDatumUitslag(null, huidigSchooljaar.getEenOktober());
		wijzigExamenstatus(null, getGeslaagd());

		zetSchooljaarStatus(vorigSchooljaar, AssurancerapportOpgesteld);
		zetSchooljaarStatus(huidigSchooljaar, MutatiestopIngesteld);
		zetSchooljaarStatus(volgendSchooljaar, GegevensWordenIngevoerd);

		assertThat(mutatieCheck(),
			is(MedewerkerHeeftGeenToestemmingOmWijzigingDoorTeVoerenBijEenMutatiebeperkingOfStop));

		controller.save();
	}

	@Test
	public void toevoegingBekostigdExamendeelnameMagTijdensMutatiestopOp1OktoberVanHuidigKalenderJaarMetPermissie()
			throws Exception
	{
		tester.voerTestUitMetMedewerker(new BronEditNaMutatieBeperking());

		wijzigDatumUitslag(null, huidigSchooljaar.getEenOktober());
		wijzigExamenstatus(null, getGeslaagd());

		zetSchooljaarStatus(vorigSchooljaar, AssurancerapportOpgesteld);
		zetSchooljaarStatus(huidigSchooljaar, MutatiestopIngesteld);
		zetSchooljaarStatus(volgendSchooljaar, GegevensWordenIngevoerd);

		assertThat(mutatieCheck(),
			is(MedewerkerHeeftToestemmingOmWijzigingDoorTeVoerenBijEenMutatiestop));

		controller.save();

		BronAanleverMelding melding = getEersteMelding();
		assertThat(melding.isBekostigingsRelevant(), is(true));
	}

	@Test
	public void toevoegingBekostigdExamendeelnameMagNaOpgesteldAssurancerapportOp1JanuariVanVolgendKalenderJaar()
			throws Exception
	{
		tester.voerTestUitMetMedewerker();

		wijzigDatumUitslag(null, huidigSchooljaar.getEenJanuari());
		wijzigExamenstatus(null, getGeslaagd());

		zetSchooljaarStatus(vorigSchooljaar, AssurancerapportOpgesteld);
		zetSchooljaarStatus(huidigSchooljaar, AssurancerapportOpgesteld);
		zetSchooljaarStatus(volgendSchooljaar, GegevensWordenIngevoerd);

		assertThat(mutatieCheck(), is(AlleMutatiesZijnNogToegestaan));
		controller.save();

		BronAanleverMelding melding = getEersteMelding();
		assertThat(melding.isBekostigingsRelevant(), is(false));
	}

	@Test(expected = BronException.class)
	public void toevoegingBekostigdExamendeelnameMagNietNaOpgesteldAssurancerapportOp1OktoberVanHuidigKalenderJaar()
			throws Exception
	{
		tester.voerTestUitMetMedewerker();

		wijzigDatumUitslag(null, huidigSchooljaar.getEenOktober());
		wijzigExamenstatus(null, getGeslaagd());

		zetSchooljaarStatus(vorigSchooljaar, AssurancerapportOpgesteld);
		zetSchooljaarStatus(huidigSchooljaar, AssurancerapportOpgesteld);
		zetSchooljaarStatus(volgendSchooljaar, GegevensWordenIngevoerd);

		assertThat(mutatieCheck(), is(SchooljaarIsAfgeslotenVoorMutaties));
		controller.save();
	}

	@Test
	public void toevoegingNietBekostigdExamendeelnameMagTijdensVrijeInvoerVoorUitslagOp1OktoberHuidigSchooljaar()
			throws Exception
	{
		tester.voerTestUitMetMedewerker();

		deelname1.setBekostigd(false);

		wijzigDatumUitslag(null, huidigSchooljaar.getEenOktober());
		wijzigExamenstatus(null, getGeslaagd());

		zetSchooljaarStatus(vorigSchooljaar, AssurancerapportOpgesteld);
		zetSchooljaarStatus(huidigSchooljaar, GegevensWordenIngevoerd);
		zetSchooljaarStatus(volgendSchooljaar, GegevensWordenIngevoerd);

		assertThat(mutatieCheck(), is(ExamendeelnameIsNietBekostigd));
		controller.save();

		BronAanleverMelding melding = getEersteMelding();
		assertThat(melding.isBekostigingsRelevant(), is(false));
	}

	@Test
	public void toevoegingNietBekostigdExamendeelnameMagTijdensVrijeInvoerUitslagOp1JanuariVanKalenderjaar1Oktober()
			throws Exception
	{
		tester.voerTestUitMetMedewerker();

		deelname1.setBekostigd(false);

		wijzigDatumUitslag(null, vorigSchooljaar.getEenJanuari());
		wijzigExamenstatus(null, getGeslaagd());

		zetSchooljaarStatus(vorigSchooljaar, AssurancerapportOpgesteld);
		zetSchooljaarStatus(huidigSchooljaar, GegevensWordenIngevoerd);
		zetSchooljaarStatus(volgendSchooljaar, GegevensWordenIngevoerd);

		assertThat(mutatieCheck(), is(ExamendeelnameIsNietBekostigd));
		controller.save();

		BronAanleverMelding melding = getEersteMelding();
		assertThat(melding.isBekostigingsRelevant(), is(false));
	}

	@Test
	public void toevoegingNietBekostigdExamendeelnameMagTijdensMutatiebeperkingOp1JanuariVanVolgendKalenderJaar()
			throws Exception
	{
		tester.voerTestUitMetMedewerker();

		deelname1.setBekostigd(false);

		wijzigDatumUitslag(null, huidigSchooljaar.getEenJanuari());
		wijzigExamenstatus(null, getGeslaagd());

		zetSchooljaarStatus(vorigSchooljaar, AssurancerapportOpgesteld);
		zetSchooljaarStatus(huidigSchooljaar, MutatiebeperkingIngesteld);
		zetSchooljaarStatus(volgendSchooljaar, GegevensWordenIngevoerd);

		assertThat(mutatieCheck(), is(ExamendeelnameIsNietBekostigd));
		controller.save();

		BronAanleverMelding melding = getEersteMelding();
		assertThat(melding.isBekostigingsRelevant(), is(false));
	}

	@Test
	public void toevoegingNietBekostigdExamendeelnameMagTijdensMutatiestopOp1JanuariVanVolgendKalenderJaar()
			throws Exception
	{
		tester.voerTestUitMetMedewerker();

		deelname1.setBekostigd(false);

		wijzigDatumUitslag(null, huidigSchooljaar.getEenJanuari());
		wijzigExamenstatus(null, getGeslaagd());

		zetSchooljaarStatus(vorigSchooljaar, AssurancerapportOpgesteld);
		zetSchooljaarStatus(huidigSchooljaar, MutatiestopIngesteld);
		zetSchooljaarStatus(volgendSchooljaar, GegevensWordenIngevoerd);

		assertThat(mutatieCheck(), is(ExamendeelnameIsNietBekostigd));
		controller.save();

		BronAanleverMelding melding = getEersteMelding();
		assertThat(melding.isBekostigingsRelevant(), is(false));
	}

	@Test
	public void toevoegingNietBekostigdExamendeelnameMagNaOpgesteldAssurancerapportOp1JanuariVanVolgendKalenderJaar()
			throws Exception
	{
		tester.voerTestUitMetMedewerker();

		deelname1.setBekostigd(false);

		wijzigDatumUitslag(null, huidigSchooljaar.getEenJanuari());
		wijzigExamenstatus(null, getGeslaagd());

		zetSchooljaarStatus(vorigSchooljaar, AssurancerapportOpgesteld);
		zetSchooljaarStatus(huidigSchooljaar, AssurancerapportOpgesteld);
		zetSchooljaarStatus(volgendSchooljaar, GegevensWordenIngevoerd);

		assertThat(mutatieCheck(), is(ExamendeelnameIsNietBekostigd));
		controller.save();

		BronAanleverMelding melding = getEersteMelding();
		assertThat(melding.isBekostigingsRelevant(), is(false));
	}

	@Test
	public void verwijderingBekostigdExamendeelnameMagTijdensVrijeInvoerVoorUitslagOp1OktoberHuidigSchooljaar()
			throws Exception
	{
		tester.voerTestUitMetMedewerker();

		deelname1.setDatumUitslag(huidigSchooljaar.getEenOktober());

		wijzigExamenstatus(getGeslaagd(), getVerwijderd());

		zetSchooljaarStatus(vorigSchooljaar, AssurancerapportOpgesteld);
		zetSchooljaarStatus(huidigSchooljaar, GegevensWordenIngevoerd);
		zetSchooljaarStatus(volgendSchooljaar, GegevensWordenIngevoerd);

		assertThat(mutatieCheck(), is(AlleMutatiesZijnNogToegestaan));

		controller.save();

		BronAanleverMelding melding = getEersteMelding();
		assertThat(melding.isBekostigingsRelevant(), is(false));
	}

	@Test
	public void verwijderingBekostigdExamendeelnameMagTijdensVrijeInvoerUitslagOp1JanuariVanKalenderjaar1Oktober()
			throws Exception
	{
		tester.voerTestUitMetMedewerker();

		deelname1.setDatumUitslag(vorigSchooljaar.getEenJanuari());

		wijzigExamenstatus(getGeslaagd(), getVerwijderd());

		zetSchooljaarStatus(vorigSchooljaar, AssurancerapportOpgesteld);
		zetSchooljaarStatus(huidigSchooljaar, GegevensWordenIngevoerd);
		zetSchooljaarStatus(volgendSchooljaar, GegevensWordenIngevoerd);

		assertThat(mutatieCheck(), is(AlleMutatiesZijnNogToegestaan));

		controller.save();

		BronAanleverMelding melding = getEersteMelding();
		assertThat(melding.isBekostigingsRelevant(), is(false));
	}

	@Test
	public void verwijderingBekostigdExamendeelnameMagTijdensMutatiebeperkingOp1JanuariVanVolgendKalenderJaar()
			throws Exception
	{
		tester.voerTestUitMetMedewerker();

		deelname1.setDatumUitslag(huidigSchooljaar.getEenJanuari());

		wijzigExamenstatus(getGeslaagd(), getVerwijderd());

		zetSchooljaarStatus(vorigSchooljaar, AssurancerapportOpgesteld);
		zetSchooljaarStatus(huidigSchooljaar, MutatiebeperkingIngesteld);
		zetSchooljaarStatus(volgendSchooljaar, GegevensWordenIngevoerd);

		assertThat(mutatieCheck(), is(AlleMutatiesZijnNogToegestaan));

		controller.save();

		BronAanleverMelding melding = getEersteMelding();
		assertThat(melding.isBekostigingsRelevant(), is(false));
	}

	@Test(expected = BronException.class)
	public void verwijderingBekostigdExamendeelnameMagNietTijdensMutatiebeperkingOp1OktoberVanHuidigKalenderJaar()
			throws Exception
	{
		tester.voerTestUitMetMedewerker();

		deelname1.setDatumUitslag(huidigSchooljaar.getEenOktober());

		wijzigExamenstatus(getGeslaagd(), getVerwijderd());

		zetSchooljaarStatus(vorigSchooljaar, AssurancerapportOpgesteld);
		zetSchooljaarStatus(huidigSchooljaar, MutatiebeperkingIngesteld);
		zetSchooljaarStatus(volgendSchooljaar, GegevensWordenIngevoerd);

		assertThat(mutatieCheck(),
			is(MedewerkerHeeftGeenToestemmingOmWijzigingDoorTeVoerenBijEenMutatiebeperkingOfStop));

		controller.save();
	}

	@Test
	public void verwijderingBekostigdExamendeelnameMagTijdensMutatiebeperkingOp1OktoberVanHuidigKalenderJaarMetPermissie()
			throws Exception
	{
		tester.voerTestUitMetMedewerker(new BronEditNaMutatieBeperking());

		deelname1.setDatumUitslag(huidigSchooljaar.getEenOktober());

		wijzigExamenstatus(getGeslaagd(), getVerwijderd());

		zetSchooljaarStatus(vorigSchooljaar, AssurancerapportOpgesteld);
		zetSchooljaarStatus(huidigSchooljaar, MutatiebeperkingIngesteld);
		zetSchooljaarStatus(volgendSchooljaar, GegevensWordenIngevoerd);

		assertThat(mutatieCheck(),
			is(MedewerkerHeeftToestemmingOmWijzigingDoorTeVoerenBijEenMutatiebeperking));

		controller.save();

		BronAanleverMelding melding = getEersteMelding();
		assertThat(melding.isBekostigingsRelevant(), is(false));
	}

	@Test
	public void verwijderingBekostigdExamendeelnameMagTijdensMutatiestopOp1JanuariVanVolgendKalenderJaar()
			throws Exception
	{
		tester.voerTestUitMetMedewerker();

		deelname1.setDatumUitslag(huidigSchooljaar.getEenJanuari());

		wijzigExamenstatus(getGeslaagd(), getVerwijderd());

		zetSchooljaarStatus(vorigSchooljaar, AssurancerapportOpgesteld);
		zetSchooljaarStatus(huidigSchooljaar, MutatiestopIngesteld);
		zetSchooljaarStatus(volgendSchooljaar, GegevensWordenIngevoerd);

		assertThat(mutatieCheck(), is(AlleMutatiesZijnNogToegestaan));

		controller.save();

		BronAanleverMelding melding = getEersteMelding();
		assertThat(melding.isBekostigingsRelevant(), is(false));
	}

	@Test(expected = BronException.class)
	public void verwijderingBekostigdExamendeelnameMagNietTijdensMutatiestopOp1OktoberVanHuidigKalenderJaar()
			throws Exception
	{
		tester.voerTestUitMetMedewerker();

		deelname1.setDatumUitslag(huidigSchooljaar.getEenOktober());

		wijzigExamenstatus(getGeslaagd(), getVerwijderd());

		zetSchooljaarStatus(vorigSchooljaar, AssurancerapportOpgesteld);
		zetSchooljaarStatus(huidigSchooljaar, MutatiestopIngesteld);
		zetSchooljaarStatus(volgendSchooljaar, GegevensWordenIngevoerd);

		assertThat(mutatieCheck(),
			is(MedewerkerHeeftGeenToestemmingOmWijzigingDoorTeVoerenBijEenMutatiebeperkingOfStop));

		controller.save();
	}

	@Test
	public void verwijderingBekostigdExamendeelnameMagTijdensMutatiestopOp1OktoberVanHuidigKalenderJaarMetPermissie()
			throws Exception
	{
		tester.voerTestUitMetMedewerker(new BronEditNaMutatieBeperking());

		deelname1.setDatumUitslag(huidigSchooljaar.getEenOktober());

		wijzigExamenstatus(getGeslaagd(), getVerwijderd());

		zetSchooljaarStatus(vorigSchooljaar, AssurancerapportOpgesteld);
		zetSchooljaarStatus(huidigSchooljaar, MutatiestopIngesteld);
		zetSchooljaarStatus(volgendSchooljaar, GegevensWordenIngevoerd);

		assertThat(mutatieCheck(),
			is(MedewerkerHeeftToestemmingOmWijzigingDoorTeVoerenBijEenMutatiestop));

		controller.save();

		BronAanleverMelding melding = getEersteMelding();
		assertThat(melding.isBekostigingsRelevant(), is(true));
	}

	@Test
	public void verwijderingBekostigdExamendeelnameMagNaOpgesteldAssurancerapportOp1JanuariVanVolgendKalenderJaar()
			throws Exception
	{
		tester.voerTestUitMetMedewerker();

		deelname1.setDatumUitslag(huidigSchooljaar.getEenJanuari());

		wijzigExamenstatus(getGeslaagd(), getVerwijderd());

		zetSchooljaarStatus(vorigSchooljaar, AssurancerapportOpgesteld);
		zetSchooljaarStatus(huidigSchooljaar, AssurancerapportOpgesteld);
		zetSchooljaarStatus(volgendSchooljaar, GegevensWordenIngevoerd);

		assertThat(mutatieCheck(), is(AlleMutatiesZijnNogToegestaan));

		controller.save();

		BronAanleverMelding melding = getEersteMelding();
		assertThat(melding.isBekostigingsRelevant(), is(false));
	}

	@Test(expected = BronException.class)
	public void verwijderingBekostigdExamendeelnameMagNaOpgesteldAssurancerapportOp1OktoberVanHuidigKalenderJaar()
			throws Exception
	{
		tester.voerTestUitMetMedewerker();

		deelname1.setDatumUitslag(huidigSchooljaar.getEenOktober());

		wijzigExamenstatus(getGeslaagd(), getVerwijderd());

		zetSchooljaarStatus(vorigSchooljaar, AssurancerapportOpgesteld);
		zetSchooljaarStatus(huidigSchooljaar, AssurancerapportOpgesteld);
		zetSchooljaarStatus(volgendSchooljaar, GegevensWordenIngevoerd);

		assertThat(mutatieCheck(), is(SchooljaarIsAfgeslotenVoorMutaties));

		controller.save();
	}

	@Test
	public void verwijderingNietBekostigdExamendeelnameMagTijdensVrijeInvoerVoorUitslagOp1OktoberHuidigSchooljaar()
			throws Exception
	{
		tester.voerTestUitMetMedewerker();

		deelname1.setDatumUitslag(huidigSchooljaar.getEenOktober());
		deelname1.setBekostigd(false);

		wijzigExamenstatus(getGeslaagd(), getVerwijderd());

		zetSchooljaarStatus(vorigSchooljaar, AssurancerapportOpgesteld);
		zetSchooljaarStatus(huidigSchooljaar, GegevensWordenIngevoerd);
		zetSchooljaarStatus(volgendSchooljaar, GegevensWordenIngevoerd);

		assertThat(mutatieCheck(), is(ExamendeelnameIsNietBekostigd));

		controller.save();

		BronAanleverMelding melding = getEersteMelding();
		assertThat(melding.isBekostigingsRelevant(), is(false));
	}

	@Test
	public void verwijderingNietBekostigdExamendeelnameMagTijdensVrijeInvoerUitslagOp1JanuariVanKalenderjaar1Oktober()
			throws Exception
	{
		tester.voerTestUitMetMedewerker();

		deelname1.setDatumUitslag(vorigSchooljaar.getEenJanuari());
		deelname1.setBekostigd(false);

		wijzigExamenstatus(getGeslaagd(), getVerwijderd());

		zetSchooljaarStatus(vorigSchooljaar, AssurancerapportOpgesteld);
		zetSchooljaarStatus(huidigSchooljaar, GegevensWordenIngevoerd);
		zetSchooljaarStatus(volgendSchooljaar, GegevensWordenIngevoerd);

		assertThat(mutatieCheck(), is(ExamendeelnameIsNietBekostigd));

		controller.save();

		BronAanleverMelding melding = getEersteMelding();
		assertThat(melding.isBekostigingsRelevant(), is(false));
	}

	@Test
	public void verwijderingNietBekostigdExamendeelnameMagTijdensMutatiebeperkingOp1JanuariVanVolgendKalenderJaar()
			throws Exception
	{
		tester.voerTestUitMetMedewerker();

		deelname1.setDatumUitslag(huidigSchooljaar.getEenJanuari());
		deelname1.setBekostigd(false);

		wijzigExamenstatus(getGeslaagd(), getVerwijderd());

		zetSchooljaarStatus(vorigSchooljaar, AssurancerapportOpgesteld);
		zetSchooljaarStatus(huidigSchooljaar, MutatiebeperkingIngesteld);
		zetSchooljaarStatus(volgendSchooljaar, GegevensWordenIngevoerd);

		assertThat(mutatieCheck(), is(ExamendeelnameIsNietBekostigd));

		controller.save();

		BronAanleverMelding melding = getEersteMelding();
		assertThat(melding.isBekostigingsRelevant(), is(false));
	}

	@Test
	public void verwijderingNietBekostigdExamendeelnameMagTijdensMutatiestopOp1JanuariVanVolgendKalenderJaar()
			throws Exception
	{
		tester.voerTestUitMetMedewerker();

		deelname1.setDatumUitslag(huidigSchooljaar.getEenJanuari());
		deelname1.setBekostigd(false);

		wijzigExamenstatus(getGeslaagd(), getVerwijderd());

		zetSchooljaarStatus(vorigSchooljaar, AssurancerapportOpgesteld);
		zetSchooljaarStatus(huidigSchooljaar, MutatiestopIngesteld);
		zetSchooljaarStatus(volgendSchooljaar, GegevensWordenIngevoerd);

		assertThat(mutatieCheck(), is(ExamendeelnameIsNietBekostigd));

		controller.save();

		BronAanleverMelding melding = getEersteMelding();
		assertThat(melding.isBekostigingsRelevant(), is(false));
	}

	@Test
	public void verwijderingNietBekostigdExamendeelnameMagNaOpgesteldAssurancerapportOp1JanuariVanVolgendKalenderJaar()
			throws Exception
	{
		tester.voerTestUitMetMedewerker();

		deelname1.setDatumUitslag(huidigSchooljaar.getEenJanuari());
		deelname1.setBekostigd(false);

		wijzigExamenstatus(getGeslaagd(), getVerwijderd());

		zetSchooljaarStatus(vorigSchooljaar, AssurancerapportOpgesteld);
		zetSchooljaarStatus(huidigSchooljaar, AssurancerapportOpgesteld);
		zetSchooljaarStatus(volgendSchooljaar, GegevensWordenIngevoerd);

		assertThat(mutatieCheck(), is(ExamendeelnameIsNietBekostigd));

		controller.save();

		BronAanleverMelding melding = getEersteMelding();
		assertThat(melding.isBekostigingsRelevant(), is(false));
	}

	@Test
	public void aanpassingKalenderjaarDatumUitslagVooruitBekostigdExamendeelnameMagTijdensVrijeInvoer()
			throws Exception
	{
		tester.voerTestUitMetMedewerker();

		wijzigDatumUitslag(huidigSchooljaar.getEenOktober(), huidigSchooljaar.getEenFebruari());

		zetSchooljaarStatus(vorigSchooljaar, AssurancerapportOpgesteld);
		zetSchooljaarStatus(huidigSchooljaar, GegevensWordenIngevoerd);
		zetSchooljaarStatus(volgendSchooljaar, GegevensWordenIngevoerd);

		assertThat(mutatieCheck(), is(AlleMutatiesZijnNogToegestaan));

		controller.save();

		BronAanleverMelding melding = getEersteMelding();
		assertThat(melding.isBekostigingsRelevant(), is(false));
	}

	@Test
	public void aanpassingKalenderjaarDatumUitslagAchteruitBekostigdExamendeelnameMagTijdensVrijeInvoer()
			throws Exception
	{
		tester.voerTestUitMetMedewerker();

		wijzigDatumUitslag(huidigSchooljaar.getEenFebruari(), huidigSchooljaar.getEenOktober());

		zetSchooljaarStatus(vorigSchooljaar, AssurancerapportOpgesteld);
		zetSchooljaarStatus(huidigSchooljaar, GegevensWordenIngevoerd);
		zetSchooljaarStatus(volgendSchooljaar, GegevensWordenIngevoerd);

		assertThat(mutatieCheck(), is(AlleMutatiesZijnNogToegestaan));

		controller.save();

		BronAanleverMelding melding = getEersteMelding();
		assertThat(melding.isBekostigingsRelevant(), is(false));
	}

	@Test(expected = BronException.class)
	public void aanpassingKalenderjaarDatumUitslagVooruitBekostigdExamendeelnameMagNietTijdensMutatiebeperking()
			throws Exception
	{
		tester.voerTestUitMetMedewerker();

		wijzigDatumUitslag(huidigSchooljaar.getEenOktober(), huidigSchooljaar.getEenFebruari());

		zetSchooljaarStatus(vorigSchooljaar, AssurancerapportOpgesteld);
		zetSchooljaarStatus(huidigSchooljaar, MutatiebeperkingIngesteld);
		zetSchooljaarStatus(volgendSchooljaar, GegevensWordenIngevoerd);

		assertThat(mutatieCheck(),
			is(MedewerkerHeeftGeenToestemmingOmWijzigingDoorTeVoerenBijEenMutatiebeperkingOfStop));

		controller.save();
	}

	@Test(expected = BronException.class)
	public void aanpassingKalenderjaarDatumUitslagAchteruitBekostigdExamendeelnameMagNietTijdensMutatiebeperking()
			throws Exception
	{
		tester.voerTestUitMetMedewerker();

		wijzigDatumUitslag(huidigSchooljaar.getEenFebruari(), huidigSchooljaar.getEenOktober());

		zetSchooljaarStatus(vorigSchooljaar, AssurancerapportOpgesteld);
		zetSchooljaarStatus(huidigSchooljaar, MutatiebeperkingIngesteld);
		zetSchooljaarStatus(volgendSchooljaar, GegevensWordenIngevoerd);

		assertThat(mutatieCheck(),
			is(MedewerkerHeeftGeenToestemmingOmWijzigingDoorTeVoerenBijEenMutatiebeperkingOfStop));

		controller.save();
	}

	@Test
	public void aanpassingKalenderjaarDatumUitslagVooruitBekostigdExamendeelnameMagTijdensMutatiebeperkingMetPermissie()
			throws Exception
	{
		tester.voerTestUitMetMedewerker(new BronEditNaMutatieBeperking());

		wijzigDatumUitslag(huidigSchooljaar.getEenOktober(), huidigSchooljaar.getEenFebruari());

		zetSchooljaarStatus(vorigSchooljaar, AssurancerapportOpgesteld);
		zetSchooljaarStatus(huidigSchooljaar, MutatiebeperkingIngesteld);
		zetSchooljaarStatus(volgendSchooljaar, GegevensWordenIngevoerd);

		assertThat(mutatieCheck(),
			is(MedewerkerHeeftToestemmingOmWijzigingDoorTeVoerenBijEenMutatiebeperking));
		controller.save();

		BronAanleverMelding melding = getEersteMelding();
		assertThat(melding.isBekostigingsRelevant(), is(false));
	}

	@Test
	public void aanpassingKalenderjaarDatumUitslagAchteruitBekostigdExamendeelnameMagTijdensMutatiebeperkingMetPermissie()
			throws Exception
	{
		tester.voerTestUitMetMedewerker(new BronEditNaMutatieBeperking());

		wijzigDatumUitslag(huidigSchooljaar.getEenFebruari(), huidigSchooljaar.getEenOktober());

		zetSchooljaarStatus(vorigSchooljaar, AssurancerapportOpgesteld);
		zetSchooljaarStatus(huidigSchooljaar, MutatiebeperkingIngesteld);
		zetSchooljaarStatus(volgendSchooljaar, GegevensWordenIngevoerd);

		assertThat(mutatieCheck(),
			is(MedewerkerHeeftToestemmingOmWijzigingDoorTeVoerenBijEenMutatiebeperking));
		controller.save();

		BronAanleverMelding melding = getEersteMelding();
		assertThat(melding.isBekostigingsRelevant(), is(false));
	}

	@Test
	public void aanpassingKalenderjaarDatumUitslagVooruitBekostigdExamendeelnameMagTijdensMutatiestopMetPermissie()
			throws Exception
	{
		tester.voerTestUitMetMedewerker(new BronEditNaMutatieBeperking());

		wijzigDatumUitslag(huidigSchooljaar.getEenOktober(), huidigSchooljaar.getEenFebruari());

		zetSchooljaarStatus(vorigSchooljaar, AssurancerapportOpgesteld);
		zetSchooljaarStatus(huidigSchooljaar, MutatiestopIngesteld);
		zetSchooljaarStatus(volgendSchooljaar, GegevensWordenIngevoerd);

		assertThat(mutatieCheck(),
			is(MedewerkerHeeftToestemmingOmWijzigingDoorTeVoerenBijEenMutatiestop));
		controller.save();

		BronAanleverMelding melding = getEersteMelding();
		assertThat(melding.isBekostigingsRelevant(), is(true));
	}

	@Test
	public void aanpassingKalenderjaarDatumUitslagAchteruitBekostigdExamendeelnameMagTijdensMutatiestopMetPermissie()
			throws Exception
	{
		tester.voerTestUitMetMedewerker(new BronEditNaMutatieBeperking());

		wijzigDatumUitslag(huidigSchooljaar.getEenFebruari(), huidigSchooljaar.getEenOktober());

		zetSchooljaarStatus(vorigSchooljaar, AssurancerapportOpgesteld);
		zetSchooljaarStatus(huidigSchooljaar, MutatiestopIngesteld);
		zetSchooljaarStatus(volgendSchooljaar, GegevensWordenIngevoerd);

		assertThat(mutatieCheck(),
			is(MedewerkerHeeftToestemmingOmWijzigingDoorTeVoerenBijEenMutatiestop));
		controller.save();

		BronAanleverMelding melding = getEersteMelding();
		assertThat(melding.isBekostigingsRelevant(), is(true));
	}

	@Test(expected = BronException.class)
	public void aanpassingKalenderjaarDatumUitslagVooruitBekostigdExamendeelnameMagNietNaOpgesteldAssurancerapport()
			throws Exception
	{
		tester.voerTestUitMetMedewerker();

		wijzigDatumUitslag(huidigSchooljaar.getEenOktober(), huidigSchooljaar.getEenFebruari());

		zetSchooljaarStatus(vorigSchooljaar, AssurancerapportOpgesteld);
		zetSchooljaarStatus(huidigSchooljaar, AssurancerapportOpgesteld);
		zetSchooljaarStatus(volgendSchooljaar, GegevensWordenIngevoerd);

		assertThat(mutatieCheck(), is(SchooljaarIsAfgeslotenVoorMutaties));

		controller.save();
	}

	@Test(expected = BronException.class)
	public void aanpassingKalenderjaarDatumUitslagAchteruitBekostigdExamendeelnameMagNietNaOpgesteldAssurancerapport()
			throws Exception
	{
		tester.voerTestUitMetMedewerker();

		wijzigDatumUitslag(huidigSchooljaar.getEenFebruari(), huidigSchooljaar.getEenOktober());

		zetSchooljaarStatus(vorigSchooljaar, AssurancerapportOpgesteld);
		zetSchooljaarStatus(huidigSchooljaar, AssurancerapportOpgesteld);
		zetSchooljaarStatus(volgendSchooljaar, GegevensWordenIngevoerd);

		assertThat(mutatieCheck(), is(SchooljaarIsAfgeslotenVoorMutaties));

		controller.save();
	}

	@Test(expected = BronException.class)
	public void aanpassingKalenderjaarDatumUitslagVooruitBekostigdExamendeelnameMagNietNaOpgesteldAssurancerapportMetPermissie()
			throws Exception
	{
		tester.voerTestUitMetMedewerker(new BronEditNaMutatieBeperking());

		wijzigDatumUitslag(huidigSchooljaar.getEenOktober(), huidigSchooljaar.getEenFebruari());

		zetSchooljaarStatus(vorigSchooljaar, AssurancerapportOpgesteld);
		zetSchooljaarStatus(huidigSchooljaar, AssurancerapportOpgesteld);
		zetSchooljaarStatus(volgendSchooljaar, GegevensWordenIngevoerd);

		assertThat(mutatieCheck(), is(SchooljaarIsAfgeslotenVoorMutaties));

		controller.save();
	}

	@Test(expected = BronException.class)
	public void aanpassingKalenderjaarDatumUitslagAchteruitBekostigdExamendeelnameMagNietNaOpgesteldAssurancerapportMetPermissie()
			throws Exception
	{
		tester.voerTestUitMetMedewerker(new BronEditNaMutatieBeperking());

		wijzigDatumUitslag(huidigSchooljaar.getEenFebruari(), huidigSchooljaar.getEenOktober());

		zetSchooljaarStatus(vorigSchooljaar, AssurancerapportOpgesteld);
		zetSchooljaarStatus(huidigSchooljaar, AssurancerapportOpgesteld);
		zetSchooljaarStatus(volgendSchooljaar, GegevensWordenIngevoerd);

		assertThat(mutatieCheck(), is(SchooljaarIsAfgeslotenVoorMutaties));
		controller.save();
	}

	@Test(expected = BronException.class)
	public void aanpassingBekostigingExamendeelnameNaarNietBekostigdMagNietTijdensMutatiebeperking()
			throws Exception
	{
		tester.voerTestUitMetMedewerker();

		wijzigBekostigd(true, false);

		zetSchooljaarStatus(vorigSchooljaar, AssurancerapportOpgesteld);
		zetSchooljaarStatus(huidigSchooljaar, MutatiebeperkingIngesteld);
		zetSchooljaarStatus(volgendSchooljaar, GegevensWordenIngevoerd);

		assertThat(mutatieCheck(),
			is(MedewerkerHeeftGeenToestemmingOmWijzigingDoorTeVoerenBijEenMutatiebeperkingOfStop));
		controller.save();
	}

	@Test(expected = BronException.class)
	public void aanpassingBekostigingExamendeelnameNaarBekostigdMagNietTijdensMutatiebeperking()
			throws Exception
	{
		tester.voerTestUitMetMedewerker();

		wijzigBekostigd(false, true);

		zetSchooljaarStatus(vorigSchooljaar, AssurancerapportOpgesteld);
		zetSchooljaarStatus(huidigSchooljaar, MutatiebeperkingIngesteld);
		zetSchooljaarStatus(volgendSchooljaar, GegevensWordenIngevoerd);

		assertThat(mutatieCheck(),
			is(MedewerkerHeeftGeenToestemmingOmWijzigingDoorTeVoerenBijEenMutatiebeperkingOfStop));
		controller.save();
	}

	@Test
	public void aanpassingBekostigingExamendeelnameNaarNietBekostigdMagTijdensMutatiebeperkingMetPermissie()
			throws Exception
	{
		tester.voerTestUitMetMedewerker(new BronEditNaMutatieBeperking());

		wijzigBekostigd(true, false);

		zetSchooljaarStatus(vorigSchooljaar, AssurancerapportOpgesteld);
		zetSchooljaarStatus(huidigSchooljaar, MutatiebeperkingIngesteld);
		zetSchooljaarStatus(volgendSchooljaar, GegevensWordenIngevoerd);

		assertThat(mutatieCheck(),
			is(MedewerkerHeeftToestemmingOmWijzigingDoorTeVoerenBijEenMutatiebeperking));
		controller.save();

		BronAanleverMelding melding = getEersteMelding();
		assertThat(melding.isBekostigingsRelevant(), is(false));
	}

	@Test
	public void aanpassingBekostigingExamendeelnameNaarBekostigdMagTijdensMutatiebeperkingMetPermissie()
			throws Exception
	{
		tester.voerTestUitMetMedewerker(new BronEditNaMutatieBeperking());

		wijzigBekostigd(false, true);

		zetSchooljaarStatus(vorigSchooljaar, AssurancerapportOpgesteld);
		zetSchooljaarStatus(huidigSchooljaar, MutatiebeperkingIngesteld);
		zetSchooljaarStatus(volgendSchooljaar, GegevensWordenIngevoerd);

		assertThat(mutatieCheck(),
			is(MedewerkerHeeftToestemmingOmWijzigingDoorTeVoerenBijEenMutatiebeperking));
		controller.save();

		BronAanleverMelding melding = getEersteMelding();
		assertThat(melding.isBekostigingsRelevant(), is(false));
	}

	@Test(expected = BronException.class)
	public void aanpassingBekostigingExamendeelnameNaarNietBekostigdMagNietTijdensMutatiestop()
			throws Exception
	{
		tester.voerTestUitMetMedewerker();

		wijzigBekostigd(true, false);

		zetSchooljaarStatus(vorigSchooljaar, AssurancerapportOpgesteld);
		zetSchooljaarStatus(huidigSchooljaar, MutatiestopIngesteld);
		zetSchooljaarStatus(volgendSchooljaar, GegevensWordenIngevoerd);

		assertThat(mutatieCheck(),
			is(MedewerkerHeeftGeenToestemmingOmWijzigingDoorTeVoerenBijEenMutatiebeperkingOfStop));
		controller.save();
	}

	@Test(expected = BronException.class)
	public void aanpassingBekostigingExamendeelnameNaarBekostigdMagNietTijdensMutatiestop()
			throws Exception
	{
		tester.voerTestUitMetMedewerker();

		wijzigBekostigd(false, true);

		zetSchooljaarStatus(vorigSchooljaar, AssurancerapportOpgesteld);
		zetSchooljaarStatus(huidigSchooljaar, MutatiestopIngesteld);
		zetSchooljaarStatus(volgendSchooljaar, GegevensWordenIngevoerd);

		assertThat(mutatieCheck(),
			is(MedewerkerHeeftGeenToestemmingOmWijzigingDoorTeVoerenBijEenMutatiebeperkingOfStop));
		controller.save();
	}

	@Test
	public void aanpassingBekostigingExamendeelnameNaarNietBekostigdMagTijdensMutatiestopMetPermissie()
			throws Exception
	{
		tester.voerTestUitMetMedewerker(new BronEditNaMutatieBeperking());

		wijzigBekostigd(true, false);

		zetSchooljaarStatus(vorigSchooljaar, AssurancerapportOpgesteld);
		zetSchooljaarStatus(huidigSchooljaar, MutatiestopIngesteld);
		zetSchooljaarStatus(volgendSchooljaar, GegevensWordenIngevoerd);

		assertThat(mutatieCheck(),
			is(MedewerkerHeeftToestemmingOmWijzigingDoorTeVoerenBijEenMutatiestop));
		controller.save();

		BronAanleverMelding melding = getEersteMelding();
		assertThat(melding.isBekostigingsRelevant(), is(true));
	}

	@Test
	public void aanpassingBekostigingExamendeelnameNaarBekostigdMagTijdensMutatiestopMetPermissie()
			throws Exception
	{
		tester.voerTestUitMetMedewerker(new BronEditNaMutatieBeperking());

		wijzigBekostigd(false, true);

		zetSchooljaarStatus(vorigSchooljaar, AssurancerapportOpgesteld);
		zetSchooljaarStatus(huidigSchooljaar, MutatiestopIngesteld);
		zetSchooljaarStatus(volgendSchooljaar, GegevensWordenIngevoerd);

		assertThat(mutatieCheck(),
			is(MedewerkerHeeftToestemmingOmWijzigingDoorTeVoerenBijEenMutatiestop));
		controller.save();

		BronAanleverMelding melding = getEersteMelding();
		assertThat(melding.isBekostigingsRelevant(), is(true));
	}

	@Test(expected = BronException.class)
	public void aanpassingBekostigingExamendeelnameNaarNietBekostigdMagNietNaOpgesteldAssurancerapport()
			throws Exception
	{
		tester.voerTestUitMetMedewerker();

		wijzigBekostigd(true, false);

		zetSchooljaarStatus(vorigSchooljaar, AssurancerapportOpgesteld);
		zetSchooljaarStatus(huidigSchooljaar, AssurancerapportOpgesteld);
		zetSchooljaarStatus(volgendSchooljaar, GegevensWordenIngevoerd);

		assertThat(mutatieCheck(), is(SchooljaarIsAfgeslotenVoorMutaties));
		controller.save();
	}

	@Test(expected = BronException.class)
	public void aanpassingBekostigingExamendeelnameNaarBekostigdMagNietNaOpgesteldAssurancerapport()
			throws Exception
	{
		tester.voerTestUitMetMedewerker();

		wijzigBekostigd(false, true);

		zetSchooljaarStatus(vorigSchooljaar, AssurancerapportOpgesteld);
		zetSchooljaarStatus(huidigSchooljaar, AssurancerapportOpgesteld);
		zetSchooljaarStatus(volgendSchooljaar, GegevensWordenIngevoerd);

		assertThat(mutatieCheck(), is(SchooljaarIsAfgeslotenVoorMutaties));
		controller.save();
	}

	@Test(expected = BronException.class)
	public void aanpassingBekostigingExamendeelnameNaarNietBekostigdMagNietNaOpgesteldAssurancerapportMetPermissie()
			throws Exception
	{
		tester.voerTestUitMetMedewerker(new BronEditNaMutatieBeperking());

		wijzigBekostigd(true, false);

		zetSchooljaarStatus(vorigSchooljaar, AssurancerapportOpgesteld);
		zetSchooljaarStatus(huidigSchooljaar, AssurancerapportOpgesteld);
		zetSchooljaarStatus(volgendSchooljaar, GegevensWordenIngevoerd);

		assertThat(mutatieCheck(), is(SchooljaarIsAfgeslotenVoorMutaties));
		controller.save();
	}

	@Test(expected = BronException.class)
	public void aanpassingBekostigingExamendeelnameNaarBekostigdMagNietNaOpgesteldAssurancerapportMetPermissie()
			throws Exception
	{
		tester.voerTestUitMetMedewerker(new BronEditNaMutatieBeperking());

		wijzigBekostigd(false, true);

		zetSchooljaarStatus(vorigSchooljaar, AssurancerapportOpgesteld);
		zetSchooljaarStatus(huidigSchooljaar, AssurancerapportOpgesteld);
		zetSchooljaarStatus(volgendSchooljaar, GegevensWordenIngevoerd);

		assertThat(mutatieCheck(), is(SchooljaarIsAfgeslotenVoorMutaties));
		controller.save();
	}

	private WijzigingToegestaanResultaat mutatieCheck()
	{
		if (!gewijzigdeKenmerken.contains("bekostigd"))
			oudeBekostigd = deelname1.isBekostigd();
		if (!gewijzigdeKenmerken.contains("datumUitslag"))
			oudeDatumUitslag = deelname1.getDatumUitslag();
		if (!gewijzigdeKenmerken.contains("examenstatus"))
			oudeExamenstatus = deelname1.getExamenstatus();

		BronExamendeelnameWijzigingToegestaanCheck check =
			new BronExamendeelnameWijzigingToegestaanCheck(oudeDatumUitslag, oudeExamenstatus,
				oudeBekostigd, deelname1);
		return check.getResultaat();
	}

	private void wijzigDatumUitslag(Date oudeDatum, Date nieuweDatum) throws BronException
	{
		gewijzigdeKenmerken.add("datumUitslag");
		addChange(deelname1, "datumUitslag", oudeDatum, nieuweDatum);
		deelname1.setDatumUitslag(nieuweDatum);
		this.oudeDatumUitslag = oudeDatum;
	}

	private void wijzigExamenstatus(Examenstatus oudeStatus, Examenstatus nieuweStatus)
			throws BronException
	{
		gewijzigdeKenmerken.add("examenstatus");
		addChange(deelname1, "examenstatus", oudeStatus, nieuweStatus);
		deelname1.setExamenstatus(nieuweStatus);
		this.oudeExamenstatus = oudeStatus;
	}

	private void wijzigBekostigd(boolean oudeWaarde, boolean nieuweBekostigd) throws BronException
	{
		gewijzigdeKenmerken.add("bekostigd");
		addChange(deelname1, "bekostigd", oudeWaarde, nieuweBekostigd);
		deelname1.setBekostigd(nieuweBekostigd);
		this.oudeBekostigd = oudeWaarde;
	}
}
