package nl.topicus.eduarte.krd.bron.schakeltest.bve.aanlevering.bo;

import static nl.topicus.eduarte.entities.inschrijving.Verbintenis.Bekostigd.*;
import static nl.topicus.eduarte.entities.inschrijving.Verbintenis.VerbintenisStatus.*;
import static nl.topicus.eduarte.krd.bron.BronVerbintenisWijzigingToegestaanCheck.WijzigingToegestaanResultaat.*;
import static nl.topicus.eduarte.krd.entities.bron.BronStatus.*;
import static nl.topicus.onderwijs.duo.bron.bve.waardelijsten.Intensiteit.*;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import nl.topicus.cobra.util.TimeUtil;
import nl.topicus.eduarte.entities.inschrijving.Bekostigingsperiode;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis.Bekostigd;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis.VerbintenisStatus;
import nl.topicus.eduarte.entities.landelijk.Schooljaar;
import nl.topicus.eduarte.entities.opleiding.Opleiding;
import nl.topicus.eduarte.entities.taxonomie.MBOLeerweg;
import nl.topicus.eduarte.krd.bron.BronVerbintenisWijzigingToegestaanCheck;
import nl.topicus.eduarte.krd.bron.BronVerbintenisWijzigingToegestaanCheck.WijzigingToegestaanResultaat;
import nl.topicus.eduarte.krd.bron.schakeltest.BronOpleidingBuilder;
import nl.topicus.eduarte.krd.entities.bron.meldingen.aanlevering.BronAanleverMelding;
import nl.topicus.eduarte.krd.principals.deelnemer.bron.BronEditNaMutatieBeperking;
import nl.topicus.onderwijs.duo.bron.BronException;
import nl.topicus.onderwijs.duo.bron.bve.waardelijsten.Intensiteit;

import org.junit.Before;
import org.junit.Test;

/**
 * Voor BPV is een hele exercitie uitgevoerd om de mutatiestop/accountantsmutaties correct
 * te krijgen, dit dient ook te gebeuren voor de verbintenis mutaties aangezien hier
 * mogelijk nogal wat fouten in zitten. Bijvoorbeeld gedeeltelijk bekostigde
 * verbintenissen worden niet goed gecontroleerd, er wordt enkel op peildatum 1 februari
 * gekeken (1 oktober is ook relevant) en de beslisboom is dusdanig complex dat er nogal
 * wat fouten ingeslopen kunnen zijn.
 */
public class ProefgevallenBveBOMantis59386Test extends ProefgevallenBveBO
{
	private Date oudeBegindatum;

	private Date oudeEinddatum;

	private Bekostigd oudeBekostigd;

	private Intensiteit oudeIntensiteit;

	private VerbintenisStatus oudeStatus;

	private Opleiding oudeOpleiding;

	private final Set<String> gewijzigdeKenmerken = new HashSet<String>();

	private Schooljaar vorigSchooljaar;

	private Schooljaar volgendSchooljaar;

	private Schooljaar huidigSchooljaar;

	private Opleiding nieuweOpleiding;

	private Date eenSeptember;

	private Bekostigingsperiode periode1;

	private Bekostigingsperiode periode2;

	@Override
	@Before
	public void setup()
	{
		gewijzigdeKenmerken.clear();

		BronOpleidingBuilder opleidingBuilder = BronOpleidingBuilder.kwalificatieBuilder();
		opleidingBuilder.setCode(12003);
		opleidingBuilder.setLeerweg(MBOLeerweg.BOL);
		nieuweOpleiding = opleidingBuilder.build();

		huidigSchooljaar = Schooljaar.huidigSchooljaar();
		vorigSchooljaar = huidigSchooljaar.getVorigSchooljaar();
		volgendSchooljaar = huidigSchooljaar.getVolgendSchooljaar();

		getDeelnemer1001();

		verbintenis.setBegindatum(huidigSchooljaar.getBegindatum());
		verbintenis.setGeplandeEinddatum(huidigSchooljaar.getVolgendSchooljaar().getEinddatum());
		verbintenis.setBekostigd(Ja);
		verbintenis.setIntensiteit(Voltijd);
		verbintenis.setStatus(Definitief);

		eenSeptember = TimeUtil.getInstance().addMonths(huidigSchooljaar.getBegindatum(), 1);
	}

	@Test
	public void toevoegingMagTijdensVrijeInvoer() throws BronException
	{
		tester.voerTestUitMetMedewerker();

		zetSchooljaarStatus(GegevensWordenIngevoerd);

		wijzigBegindatum(null, verbintenis.getBegindatum());
		wijzigStatus(null, verbintenis.getStatus());
		wijzigIntensiteit(null, verbintenis.getIntensiteit());
		wijzigBekostigd(null, verbintenis.getBekostigd());
		wijzigOpleiding(null, verbintenis.getOpleiding());

		assertThat(mutatieCheck(), is(AlleMutatiesZijnNogToegestaan));

		controller.save();

		BronAanleverMelding melding = getEersteMelding();
		assertThat(melding.isBekostigingsRelevant(), is(false));
	}

	@Test(expected = BronException.class)
	public void toevoegingMagNietTijdensMutatiebeperking() throws BronException
	{
		tester.voerTestUitMetMedewerker();

		zetSchooljaarStatus(MutatiebeperkingIngesteld);

		wijzigBegindatum(null, verbintenis.getBegindatum());
		wijzigStatus(null, verbintenis.getStatus());
		wijzigIntensiteit(null, verbintenis.getIntensiteit());
		wijzigBekostigd(null, verbintenis.getBekostigd());
		wijzigOpleiding(null, verbintenis.getOpleiding());

		assertThat(mutatieCheck(),
			is(MedewerkerHeeftGeenToestemmingOmWijzigingDoorTeVoerenBijEenMutatiebeperkingOfStop));

		controller.save();
	}

	@Test
	public void toevoegingMagWelTijdensMutatiebeperkingMetPermissie() throws BronException
	{
		tester.voerTestUitMetMedewerker(new BronEditNaMutatieBeperking());

		zetSchooljaarStatus(MutatiebeperkingIngesteld);

		wijzigBegindatum(null, verbintenis.getBegindatum());
		wijzigStatus(null, verbintenis.getStatus());
		wijzigIntensiteit(null, verbintenis.getIntensiteit());
		wijzigBekostigd(null, verbintenis.getBekostigd());
		wijzigOpleiding(null, verbintenis.getOpleiding());

		assertThat(mutatieCheck(),
			is(MedewerkerHeeftToestemmingOmWijzigingDoorTeVoerenBijEenMutatiebeperking));

		controller.save();

		BronAanleverMelding melding = getEersteMelding();

		// tijdens een mutatiebeperking geldt dat de wijzigingen nog steeds geen
		// accountantsmutatie zijn.
		assertThat(melding.isBekostigingsRelevant(), is(false));
	}

	@Test(expected = BronException.class)
	public void toevoegingMagNietTijdensMutatiestop() throws BronException
	{
		tester.voerTestUitMetMedewerker();

		zetSchooljaarStatus(MutatiestopIngesteld);

		wijzigBegindatum(null, verbintenis.getBegindatum());
		wijzigStatus(null, verbintenis.getStatus());
		wijzigIntensiteit(null, verbintenis.getIntensiteit());
		wijzigBekostigd(null, verbintenis.getBekostigd());
		wijzigOpleiding(null, verbintenis.getOpleiding());

		assertThat(mutatieCheck(),
			is(MedewerkerHeeftGeenToestemmingOmWijzigingDoorTeVoerenBijEenMutatiebeperkingOfStop));

		controller.save();
	}

	@Test
	public void toevoegingMagWelTijdensMutatiestopMetPermissie() throws BronException
	{
		tester.voerTestUitMetMedewerker(new BronEditNaMutatieBeperking());

		zetSchooljaarStatus(MutatiestopIngesteld);

		wijzigBegindatum(null, verbintenis.getBegindatum());
		wijzigStatus(null, verbintenis.getStatus());
		wijzigIntensiteit(null, verbintenis.getIntensiteit());
		wijzigBekostigd(null, verbintenis.getBekostigd());
		wijzigOpleiding(null, verbintenis.getOpleiding());

		assertThat(mutatieCheck(),
			is(MedewerkerHeeftToestemmingOmWijzigingDoorTeVoerenBijEenMutatiestop));

		controller.save();

		BronAanleverMelding melding = getEersteMelding();

		// tijdens een mutatiestop geldt dat de wijzigingen wel accountantsmutatie zijn.
		assertThat(melding.isBekostigingsRelevant(), is(true));
	}

	@Test(expected = BronException.class)
	public void toevoegingMagNietNaAssuranceRapport() throws BronException
	{
		tester.voerTestUitMetMedewerker();

		zetSchooljaarStatus(AssurancerapportOpgesteld);

		wijzigBegindatum(null, verbintenis.getBegindatum());
		wijzigStatus(null, verbintenis.getStatus());
		wijzigIntensiteit(null, verbintenis.getIntensiteit());
		wijzigBekostigd(null, verbintenis.getBekostigd());
		wijzigOpleiding(null, verbintenis.getOpleiding());

		assertThat(mutatieCheck(), is(SchooljaarIsAfgeslotenVoorMutaties));

		controller.save();
	}

	@Test(expected = BronException.class)
	public void toevoegingMagNietNaAssuranceRapportMetPermissie() throws BronException
	{
		tester.voerTestUitMetMedewerker(new BronEditNaMutatieBeperking());

		zetSchooljaarStatus(AssurancerapportOpgesteld);

		wijzigBegindatum(null, verbintenis.getBegindatum());
		wijzigStatus(null, verbintenis.getStatus());
		wijzigIntensiteit(null, verbintenis.getIntensiteit());
		wijzigBekostigd(null, verbintenis.getBekostigd());
		wijzigOpleiding(null, verbintenis.getOpleiding());

		assertThat(mutatieCheck(), is(SchooljaarIsAfgeslotenVoorMutaties));

		controller.save();
	}

	@Test(expected = BronException.class)
	public void toevoegingZonderPermissieInVorigJaarMagNietAlsVorigJaarMutatiestopHeeft()
			throws BronException
	{
		tester.voerTestUitMetMedewerker();

		verbintenis.setBegindatum(vorigSchooljaar.getBegindatum());
		zetSchooljaarStatus(vorigSchooljaar, MutatiestopIngesteld);
		zetSchooljaarStatus(huidigSchooljaar, GegevensWordenIngevoerd);

		wijzigBegindatum(null, verbintenis.getBegindatum());
		wijzigStatus(null, verbintenis.getStatus());
		wijzigIntensiteit(null, verbintenis.getIntensiteit());
		wijzigBekostigd(null, verbintenis.getBekostigd());
		wijzigOpleiding(null, verbintenis.getOpleiding());

		assertThat(mutatieCheck(),
			is(MedewerkerHeeftGeenToestemmingOmWijzigingDoorTeVoerenBijEenMutatiebeperkingOfStop));

		controller.save();
	}

	@Test
	public void toevoegingInVorigJaarMagWelAlsVorigJaarMutatiestopHeeftMetPermissie()
			throws BronException
	{
		tester.voerTestUitMetMedewerker(new BronEditNaMutatieBeperking());

		verbintenis.setBegindatum(vorigSchooljaar.getBegindatum());
		zetSchooljaarStatus(vorigSchooljaar, MutatiestopIngesteld);
		zetSchooljaarStatus(huidigSchooljaar, GegevensWordenIngevoerd);

		wijzigBegindatum(null, verbintenis.getBegindatum());
		wijzigStatus(null, verbintenis.getStatus());
		wijzigIntensiteit(null, verbintenis.getIntensiteit());
		wijzigBekostigd(null, verbintenis.getBekostigd());
		wijzigOpleiding(null, verbintenis.getOpleiding());

		assertThat(mutatieCheck(),
			is(MedewerkerHeeftToestemmingOmWijzigingDoorTeVoerenBijEenMutatiestop));

		controller.save();

		BronAanleverMelding melding = getEersteMelding();

		// tijdens een mutatiestop geldt dat de wijzigingen wel accountantsmutatie zijn.
		assertThat(melding.isBekostigingsRelevant(), is(true));
	}

	@Test(expected = BronException.class)
	public void toevoegingInVorigJaarMagNietAlsVorigJaarAssuranceRapportOpgesteldHeeft()
			throws BronException
	{
		tester.voerTestUitMetMedewerker(new BronEditNaMutatieBeperking());

		verbintenis.setBegindatum(vorigSchooljaar.getBegindatum());
		zetSchooljaarStatus(vorigSchooljaar, AssurancerapportOpgesteld);
		zetSchooljaarStatus(huidigSchooljaar, GegevensWordenIngevoerd);

		wijzigBegindatum(null, verbintenis.getBegindatum());
		wijzigStatus(null, verbintenis.getStatus());
		wijzigIntensiteit(null, verbintenis.getIntensiteit());
		wijzigBekostigd(null, verbintenis.getBekostigd());
		wijzigOpleiding(null, verbintenis.getOpleiding());

		assertThat(mutatieCheck(), is(SchooljaarIsAfgeslotenVoorMutaties));

		controller.save();
	}

	@Test(expected = BronException.class)
	public void toevoegingInVorigJaarMagNietAlsVorigJaarHistorischIs() throws BronException
	{
		tester.voerTestUitMetMedewerker(new BronEditNaMutatieBeperking());

		verbintenis.setBegindatum(vorigSchooljaar.getBegindatum());
		zetSchooljaarStatus(vorigSchooljaar, Historisch);
		zetSchooljaarStatus(huidigSchooljaar, AssurancerapportOpgesteld);

		wijzigBegindatum(null, verbintenis.getBegindatum());
		wijzigStatus(null, verbintenis.getStatus());
		wijzigIntensiteit(null, verbintenis.getIntensiteit());
		wijzigBekostigd(null, verbintenis.getBekostigd());
		wijzigOpleiding(null, verbintenis.getOpleiding());

		assertThat(mutatieCheck(), is(SchooljaarIsAfgeslotenVoorMutaties));

		controller.save();
	}

	@Test
	public void toevoegingNietBekostigdInVorigJaarMagAlsVorigJaarAssuranceRapportOpgesteldHeeft()
			throws BronException
	{
		tester.voerTestUitMetMedewerker();

		verbintenis.setBegindatum(vorigSchooljaar.getBegindatum());
		verbintenis.setBekostigd(Nee);

		zetSchooljaarStatus(vorigSchooljaar, AssurancerapportOpgesteld);
		zetSchooljaarStatus(huidigSchooljaar, GegevensWordenIngevoerd);

		wijzigBegindatum(null, verbintenis.getBegindatum());
		wijzigStatus(null, verbintenis.getStatus());
		wijzigIntensiteit(null, verbintenis.getIntensiteit());
		wijzigBekostigd(null, verbintenis.getBekostigd());
		wijzigOpleiding(null, verbintenis.getOpleiding());

		assertThat(mutatieCheck(), is(NietBekostigdOpRecentePeildatum));

		controller.save();

		BronAanleverMelding melding = getEersteMelding();

		assertThat(melding.isBekostigingsRelevant(), is(false));
	}

	@Test
	public void toevoegingNietBekostigdInVorigJaarMagAlsVorigJaarHistorischIs()
			throws BronException
	{
		tester.voerTestUitMetMedewerker();

		verbintenis.setBegindatum(vorigSchooljaar.getBegindatum());
		verbintenis.setBekostigd(Nee);

		zetSchooljaarStatus(vorigSchooljaar, Historisch);
		zetSchooljaarStatus(huidigSchooljaar, AssurancerapportOpgesteld);

		wijzigBegindatum(null, verbintenis.getBegindatum());
		wijzigStatus(null, verbintenis.getStatus());
		wijzigIntensiteit(null, verbintenis.getIntensiteit());
		wijzigBekostigd(null, verbintenis.getBekostigd());
		wijzigOpleiding(null, verbintenis.getOpleiding());

		assertThat(mutatieCheck(), is(NietBekostigdOpRecentePeildatum));

		controller.save();

		BronAanleverMelding melding = getEersteMelding();

		assertThat(melding.isBekostigingsRelevant(), is(false));
	}

	@Test
	public void verwijderingMagTijdensVrijeInvoer() throws BronException
	{
		tester.voerTestUitMetMedewerker();

		zetSchooljaarStatus(GegevensWordenIngevoerd);

		wijzigStatus(verbintenis.getStatus(), Afgemeld);

		assertThat(mutatieCheck(), is(AlleMutatiesZijnNogToegestaan));

		controller.save();

		BronAanleverMelding melding = getEersteMelding();
		assertThat(melding.isBekostigingsRelevant(), is(false));
	}

	@Test(expected = BronException.class)
	public void verwijderingMagNietTijdensMutatiebeperking() throws BronException
	{
		tester.voerTestUitMetMedewerker();

		zetSchooljaarStatus(MutatiebeperkingIngesteld);

		wijzigStatus(verbintenis.getStatus(), Afgemeld);

		assertThat(mutatieCheck(),
			is(MedewerkerHeeftGeenToestemmingOmWijzigingDoorTeVoerenBijEenMutatiebeperkingOfStop));

		controller.save();
	}

	@Test
	public void verwijderingMagWelTijdensMutatiebeperkingMetPermissie() throws BronException
	{
		tester.voerTestUitMetMedewerker(new BronEditNaMutatieBeperking());

		zetSchooljaarStatus(MutatiebeperkingIngesteld);

		wijzigStatus(verbintenis.getStatus(), Afgemeld);

		assertThat(mutatieCheck(),
			is(MedewerkerHeeftToestemmingOmWijzigingDoorTeVoerenBijEenMutatiebeperking));

		controller.save();

		BronAanleverMelding melding = getEersteMelding();

		// tijdens een mutatiebeperking geldt dat de wijzigingen nog steeds geen
		// accountantsmutatie zijn.
		assertThat(melding.isBekostigingsRelevant(), is(false));
	}

	@Test(expected = BronException.class)
	public void verwijderingMagNietTijdensMutatiestop() throws BronException
	{
		tester.voerTestUitMetMedewerker();

		zetSchooljaarStatus(MutatiestopIngesteld);

		wijzigStatus(verbintenis.getStatus(), Afgemeld);

		assertThat(mutatieCheck(),
			is(MedewerkerHeeftGeenToestemmingOmWijzigingDoorTeVoerenBijEenMutatiebeperkingOfStop));

		controller.save();
	}

	@Test
	public void verwijderingMagWelTijdensMutatiestopMetPermissie() throws BronException
	{
		tester.voerTestUitMetMedewerker(new BronEditNaMutatieBeperking());

		zetSchooljaarStatus(MutatiestopIngesteld);

		wijzigStatus(verbintenis.getStatus(), Afgemeld);

		assertThat(mutatieCheck(),
			is(MedewerkerHeeftToestemmingOmWijzigingDoorTeVoerenBijEenMutatiestop));

		controller.save();

		BronAanleverMelding melding = getEersteMelding();

		// tijdens een mutatiestop geldt dat de wijzigingen wel accountantsmutatie zijn.
		assertThat(melding.isBekostigingsRelevant(), is(true));
	}

	@Test(expected = BronException.class)
	public void verwijderingMagNietNaAssuranceRapportMetPermissie() throws BronException
	{
		tester.voerTestUitMetMedewerker(new BronEditNaMutatieBeperking());

		zetSchooljaarStatus(AssurancerapportOpgesteld);

		wijzigStatus(verbintenis.getStatus(), Afgemeld);

		assertThat(mutatieCheck(), is(SchooljaarIsAfgeslotenVoorMutaties));

		controller.save();
	}

	@Test
	public void verwijderingNietBekostigdMagWelNaAssuranceRapport() throws BronException
	{
		tester.voerTestUitMetMedewerker();

		verbintenis.setBekostigd(Nee);
		zetSchooljaarStatus(AssurancerapportOpgesteld);

		wijzigStatus(verbintenis.getStatus(), Afgemeld);

		assertThat(mutatieCheck(), is(NietBekostigdOpRecentePeildatum));

		controller.save();

		BronAanleverMelding melding = getEersteMelding();

		assertThat(melding.isBekostigingsRelevant(), is(false));
	}

	@Test(expected = BronException.class)
	public void verwijderingZonderPermissieInVorigJaarMagNietAlsVorigJaarMutatiestopHeeft()
			throws BronException
	{
		tester.voerTestUitMetMedewerker();

		verbintenis.setBegindatum(vorigSchooljaar.getBegindatum());
		zetSchooljaarStatus(vorigSchooljaar, MutatiestopIngesteld);
		zetSchooljaarStatus(huidigSchooljaar, GegevensWordenIngevoerd);

		wijzigStatus(verbintenis.getStatus(), Afgemeld);

		assertThat(mutatieCheck(),
			is(MedewerkerHeeftGeenToestemmingOmWijzigingDoorTeVoerenBijEenMutatiebeperkingOfStop));

		controller.save();
	}

	@Test
	public void verwijderingInVorigJaarMagWelAlsVorigJaarMutatiestopHeeftMetPermissie()
			throws BronException
	{
		tester.voerTestUitMetMedewerker(new BronEditNaMutatieBeperking());

		verbintenis.setBegindatum(vorigSchooljaar.getBegindatum());
		zetSchooljaarStatus(vorigSchooljaar, MutatiestopIngesteld);
		zetSchooljaarStatus(huidigSchooljaar, GegevensWordenIngevoerd);

		wijzigStatus(verbintenis.getStatus(), Afgemeld);

		assertThat(mutatieCheck(),
			is(MedewerkerHeeftToestemmingOmWijzigingDoorTeVoerenBijEenMutatiestop));

		controller.save();

		BronAanleverMelding melding = getEersteMelding();

		// tijdens een mutatiestop geldt dat de wijzigingen wel accountantsmutatie zijn.
		assertThat(melding.isBekostigingsRelevant(), is(true));
	}

	@Test(expected = BronException.class)
	public void verwijderingInVorigJaarMagNietAlsVorigJaarNaAssuranceRapportOpgesteldHeeft()
			throws BronException
	{
		tester.voerTestUitMetMedewerker(new BronEditNaMutatieBeperking());

		verbintenis.setBegindatum(vorigSchooljaar.getBegindatum());
		zetSchooljaarStatus(vorigSchooljaar, AssurancerapportOpgesteld);
		zetSchooljaarStatus(huidigSchooljaar, GegevensWordenIngevoerd);

		wijzigStatus(verbintenis.getStatus(), Afgemeld);

		assertThat(mutatieCheck(), is(SchooljaarIsAfgeslotenVoorMutaties));

		controller.save();
	}

	@Test
	public void verwijderingNietBekostigdVorigJaarMagWelNaAssuranceRapport() throws BronException
	{
		tester.voerTestUitMetMedewerker();

		verbintenis.setBegindatum(vorigSchooljaar.getBegindatum());
		verbintenis.setBekostigd(Nee);

		zetSchooljaarStatus(vorigSchooljaar, AssurancerapportOpgesteld);
		zetSchooljaarStatus(huidigSchooljaar, GegevensWordenIngevoerd);

		wijzigStatus(verbintenis.getStatus(), Afgemeld);

		assertThat(mutatieCheck(), is(NietBekostigdOpRecentePeildatum));

		controller.save();

		BronAanleverMelding melding = getEersteMelding();

		assertThat(melding.isBekostigingsRelevant(), is(false));
	}

	@Test
	public void verwijderingNietBekostigdVorigJaarMagWelIndienHistorisch() throws BronException
	{
		tester.voerTestUitMetMedewerker();

		verbintenis.setBegindatum(vorigSchooljaar.getBegindatum());
		verbintenis.setBekostigd(Nee);

		zetSchooljaarStatus(vorigSchooljaar, Historisch);
		zetSchooljaarStatus(huidigSchooljaar, AssurancerapportOpgesteld);

		wijzigStatus(verbintenis.getStatus(), Afgemeld);

		assertThat(mutatieCheck(), is(NietBekostigdOpRecentePeildatum));

		controller.save();

		BronAanleverMelding melding = getEersteMelding();

		assertThat(melding.isBekostigingsRelevant(), is(false));
	}

	@Test
	public void aanpassingOpleidingMagTijdensVrijeInvoer() throws Exception
	{
		tester.voerTestUitMetMedewerker();

		zetSchooljaarStatus(huidigSchooljaar, GegevensWordenIngevoerd);

		wijzigOpleiding(verbintenis.getOpleiding(), nieuweOpleiding);

		assertThat(mutatieCheck(), is(AlleMutatiesZijnNogToegestaan));

		controller.save();

		BronAanleverMelding melding = getEersteMelding();

		assertThat(melding.isBekostigingsRelevant(), is(false));
	}

	@Test(expected = BronException.class)
	public void aanpassingOpleidingMagNietTijdensMutatieBeperking() throws Exception
	{
		tester.voerTestUitMetMedewerker();

		zetSchooljaarStatus(huidigSchooljaar, MutatiebeperkingIngesteld);

		wijzigOpleiding(verbintenis.getOpleiding(), nieuweOpleiding);

		assertThat(mutatieCheck(),
			is(MedewerkerHeeftGeenToestemmingOmWijzigingDoorTeVoerenBijEenMutatiebeperkingOfStop));

		controller.save();
	}

	@Test
	public void aanpassingOpleidingMagTijdensMutatieBeperkingMetPermissie() throws Exception
	{
		tester.voerTestUitMetMedewerker(new BronEditNaMutatieBeperking());

		zetSchooljaarStatus(huidigSchooljaar, MutatiebeperkingIngesteld);

		wijzigOpleiding(verbintenis.getOpleiding(), nieuweOpleiding);

		assertThat(mutatieCheck(),
			is(MedewerkerHeeftToestemmingOmWijzigingDoorTeVoerenBijEenMutatiebeperking));

		controller.save();

		BronAanleverMelding melding = getEersteMelding();

		assertThat(melding.isBekostigingsRelevant(), is(false));
	}

	@Test(expected = BronException.class)
	public void aanpassingOpleidingMagNietTijdensMutatiestop() throws Exception
	{
		tester.voerTestUitMetMedewerker();

		zetSchooljaarStatus(huidigSchooljaar, MutatiestopIngesteld);

		wijzigOpleiding(verbintenis.getOpleiding(), nieuweOpleiding);

		assertThat(mutatieCheck(),
			is(MedewerkerHeeftGeenToestemmingOmWijzigingDoorTeVoerenBijEenMutatiebeperkingOfStop));

		controller.save();
	}

	@Test
	public void aanpassingOpleidingMagTijdensMutatiestopMetPermissie() throws Exception
	{
		tester.voerTestUitMetMedewerker(new BronEditNaMutatieBeperking());

		zetSchooljaarStatus(huidigSchooljaar, MutatiestopIngesteld);

		wijzigOpleiding(verbintenis.getOpleiding(), nieuweOpleiding);

		assertThat(mutatieCheck(),
			is(MedewerkerHeeftToestemmingOmWijzigingDoorTeVoerenBijEenMutatiestop));

		controller.save();

		BronAanleverMelding melding = getEersteMelding();

		assertThat(melding.isBekostigingsRelevant(), is(true));
	}

	@Test(expected = BronException.class)
	public void aanpassingOpleidingMagNietNaOpgesteldAssuranceRapport() throws Exception
	{
		tester.voerTestUitMetMedewerker();

		zetSchooljaarStatus(huidigSchooljaar, AssurancerapportOpgesteld);

		wijzigOpleiding(verbintenis.getOpleiding(), nieuweOpleiding);

		assertThat(mutatieCheck(), is(SchooljaarIsAfgeslotenVoorMutaties));

		controller.save();
	}

	@Test(expected = BronException.class)
	public void aanpassinOpleidingMagNietNaOpgesetldAssuranceRapportMetPermissie() throws Exception
	{
		tester.voerTestUitMetMedewerker(new BronEditNaMutatieBeperking());

		zetSchooljaarStatus(huidigSchooljaar, AssurancerapportOpgesteld);

		wijzigOpleiding(verbintenis.getOpleiding(), nieuweOpleiding);

		assertThat(mutatieCheck(), is(SchooljaarIsAfgeslotenVoorMutaties));

		controller.save();
	}

	@Test
	public void aanpassingNietBekostigdeOpleidingMagTijdensVrijeInvoer() throws Exception
	{
		tester.voerTestUitMetMedewerker();
		verbintenis.setBekostigd(Nee);

		zetSchooljaarStatus(huidigSchooljaar, GegevensWordenIngevoerd);

		wijzigOpleiding(verbintenis.getOpleiding(), nieuweOpleiding);

		assertThat(mutatieCheck(), is(AlleMutatiesZijnNogToegestaan));

		controller.save();

		BronAanleverMelding melding = getEersteMelding();

		assertThat(melding.isBekostigingsRelevant(), is(false));
	}

	@Test
	public void aanpassingNietBekostigdeOpleidingMagTijdensMutatieBeperking() throws Exception
	{
		tester.voerTestUitMetMedewerker();
		verbintenis.setBekostigd(Nee);

		zetSchooljaarStatus(huidigSchooljaar, MutatiebeperkingIngesteld);

		wijzigOpleiding(verbintenis.getOpleiding(), nieuweOpleiding);

		assertThat(mutatieCheck(), is(NietBekostigdOpRecentePeildatum));

		controller.save();

		BronAanleverMelding melding = getEersteMelding();

		assertThat(melding.isBekostigingsRelevant(), is(false));
	}

	@Test
	public void aanpassingNietBekostigdeOpleidingMagTijdensMutatiestop() throws Exception
	{
		tester.voerTestUitMetMedewerker();
		verbintenis.setBekostigd(Nee);

		zetSchooljaarStatus(huidigSchooljaar, MutatiestopIngesteld);

		wijzigOpleiding(verbintenis.getOpleiding(), nieuweOpleiding);

		assertThat(mutatieCheck(), is(NietBekostigdOpRecentePeildatum));

		controller.save();

		BronAanleverMelding melding = getEersteMelding();

		assertThat(melding.isBekostigingsRelevant(), is(false));
	}

	@Test
	public void aanpassingNietBekostigdeOpleidingMagNaOpgesteldAssurancerapport() throws Exception
	{
		tester.voerTestUitMetMedewerker();
		verbintenis.setBekostigd(Nee);

		zetSchooljaarStatus(huidigSchooljaar, AssurancerapportOpgesteld);

		wijzigOpleiding(verbintenis.getOpleiding(), nieuweOpleiding);

		assertThat(mutatieCheck(), is(NietBekostigdOpRecentePeildatum));

		controller.save();

		BronAanleverMelding melding = getEersteMelding();

		assertThat(melding.isBekostigingsRelevant(), is(false));
	}

	@Test
	public void aanpassingIntensiteitMagTijdensVrijeInvoer() throws Exception
	{
		tester.voerTestUitMetMedewerker();

		zetSchooljaarStatus(huidigSchooljaar, GegevensWordenIngevoerd);

		wijzigIntensiteit(verbintenis.getIntensiteit(), Deeltijd);

		assertThat(mutatieCheck(), is(AlleMutatiesZijnNogToegestaan));

		controller.save();

		BronAanleverMelding melding = getEersteMelding();

		assertThat(melding.isBekostigingsRelevant(), is(false));
	}

	@Test(expected = BronException.class)
	public void aanpassingIntensiteitMagNietTijdensMutatieBeperking() throws Exception
	{
		tester.voerTestUitMetMedewerker();

		zetSchooljaarStatus(huidigSchooljaar, MutatiebeperkingIngesteld);

		wijzigIntensiteit(verbintenis.getIntensiteit(), Deeltijd);

		assertThat(mutatieCheck(),
			is(MedewerkerHeeftGeenToestemmingOmWijzigingDoorTeVoerenBijEenMutatiebeperkingOfStop));

		controller.save();
	}

	@Test
	public void aanpassingIntensiteitMagTijdensMutatieBeperkingMetPermissie() throws Exception
	{
		tester.voerTestUitMetMedewerker(new BronEditNaMutatieBeperking());

		zetSchooljaarStatus(huidigSchooljaar, MutatiebeperkingIngesteld);

		wijzigIntensiteit(verbintenis.getIntensiteit(), Deeltijd);

		assertThat(mutatieCheck(),
			is(MedewerkerHeeftToestemmingOmWijzigingDoorTeVoerenBijEenMutatiebeperking));

		controller.save();

		BronAanleverMelding melding = getEersteMelding();

		assertThat(melding.isBekostigingsRelevant(), is(false));
	}

	@Test(expected = BronException.class)
	public void aanpassingIntensiteitMagNietTijdensMutatiestop() throws Exception
	{
		tester.voerTestUitMetMedewerker();

		zetSchooljaarStatus(huidigSchooljaar, MutatiestopIngesteld);

		wijzigIntensiteit(verbintenis.getIntensiteit(), Deeltijd);

		assertThat(mutatieCheck(),
			is(MedewerkerHeeftGeenToestemmingOmWijzigingDoorTeVoerenBijEenMutatiebeperkingOfStop));

		controller.save();
	}

	@Test
	public void aanpassingIntensiteitMagTijdensMutatiestopMetPermissie() throws Exception
	{
		tester.voerTestUitMetMedewerker(new BronEditNaMutatieBeperking());

		zetSchooljaarStatus(huidigSchooljaar, MutatiestopIngesteld);

		wijzigIntensiteit(verbintenis.getIntensiteit(), Deeltijd);

		assertThat(mutatieCheck(),
			is(MedewerkerHeeftToestemmingOmWijzigingDoorTeVoerenBijEenMutatiestop));

		controller.save();

		BronAanleverMelding melding = getEersteMelding();

		assertThat(melding.isBekostigingsRelevant(), is(true));
	}

	@Test(expected = BronException.class)
	public void aanpassingIntensiteitMagNietNaOpgesteldAssuranceRapport() throws Exception
	{
		tester.voerTestUitMetMedewerker();

		zetSchooljaarStatus(huidigSchooljaar, AssurancerapportOpgesteld);

		wijzigIntensiteit(verbintenis.getIntensiteit(), Deeltijd);

		assertThat(mutatieCheck(), is(SchooljaarIsAfgeslotenVoorMutaties));

		controller.save();
	}

	@Test(expected = BronException.class)
	public void aanpassinIntensiteitMagNietNaOpgesetldAssuranceRapportMetPermissie()
			throws Exception
	{
		tester.voerTestUitMetMedewerker(new BronEditNaMutatieBeperking());

		zetSchooljaarStatus(huidigSchooljaar, AssurancerapportOpgesteld);

		wijzigIntensiteit(verbintenis.getIntensiteit(), Deeltijd);

		assertThat(mutatieCheck(), is(SchooljaarIsAfgeslotenVoorMutaties));

		controller.save();
	}

	@Test
	public void aanpassingNietBekostigdeIntensiteitMagTijdensVrijeInvoer() throws Exception
	{
		tester.voerTestUitMetMedewerker();
		verbintenis.setBekostigd(Nee);

		zetSchooljaarStatus(huidigSchooljaar, GegevensWordenIngevoerd);

		wijzigIntensiteit(verbintenis.getIntensiteit(), Deeltijd);

		assertThat(mutatieCheck(), is(AlleMutatiesZijnNogToegestaan));

		controller.save();

		BronAanleverMelding melding = getEersteMelding();

		assertThat(melding.isBekostigingsRelevant(), is(false));
	}

	@Test
	public void aanpassingNietBekostigdeIntensiteitMagTijdensMutatieBeperking() throws Exception
	{
		tester.voerTestUitMetMedewerker();
		verbintenis.setBekostigd(Nee);

		zetSchooljaarStatus(huidigSchooljaar, MutatiebeperkingIngesteld);

		wijzigIntensiteit(verbintenis.getIntensiteit(), Deeltijd);

		assertThat(mutatieCheck(), is(NietBekostigdOpRecentePeildatum));

		controller.save();

		BronAanleverMelding melding = getEersteMelding();

		assertThat(melding.isBekostigingsRelevant(), is(false));
	}

	@Test
	public void aanpassingNietBekostigdeIntensiteitMagTijdensMutatiestop() throws Exception
	{
		tester.voerTestUitMetMedewerker();
		verbintenis.setBekostigd(Nee);

		zetSchooljaarStatus(huidigSchooljaar, MutatiestopIngesteld);

		wijzigIntensiteit(verbintenis.getIntensiteit(), Deeltijd);

		assertThat(mutatieCheck(), is(NietBekostigdOpRecentePeildatum));

		controller.save();

		BronAanleverMelding melding = getEersteMelding();

		assertThat(melding.isBekostigingsRelevant(), is(false));
	}

	@Test
	public void aanpassingNietBekostigdeIntensiteitMagNaOpgesteldAssurancerapport()
			throws Exception
	{
		tester.voerTestUitMetMedewerker();
		verbintenis.setBekostigd(Nee);

		zetSchooljaarStatus(huidigSchooljaar, AssurancerapportOpgesteld);

		wijzigIntensiteit(verbintenis.getIntensiteit(), Deeltijd);

		assertThat(mutatieCheck(), is(NietBekostigdOpRecentePeildatum));

		controller.save();

		BronAanleverMelding melding = getEersteMelding();

		assertThat(melding.isBekostigingsRelevant(), is(false));
	}

	@Test
	public void aanpassingBegindatumNaarNa1OktoberMagTijdensVrijeInvoer() throws Exception
	{
		tester.voerTestUitMetMedewerker();
		verbintenis.setBekostigd(Ja);

		zetSchooljaarStatus(huidigSchooljaar, GegevensWordenIngevoerd);

		wijzigBegindatum(verbintenis.getBegindatum(), huidigSchooljaar.getEenJanuari());

		assertThat(mutatieCheck(), is(AlleMutatiesZijnNogToegestaan));

		controller.save();

		BronAanleverMelding melding = getEersteMelding();

		assertThat(melding.isBekostigingsRelevant(), is(false));
	}

	@Test(expected = BronException.class)
	public void aanpassingBegindatumNaarNa1OktoberMagNietTijdensMutatiebeperking() throws Exception
	{
		tester.voerTestUitMetMedewerker();
		verbintenis.setBekostigd(Ja);

		zetSchooljaarStatus(huidigSchooljaar, MutatiebeperkingIngesteld);

		wijzigBegindatum(verbintenis.getBegindatum(), huidigSchooljaar.getEenJanuari());

		assertThat(mutatieCheck(),
			is(MedewerkerHeeftGeenToestemmingOmWijzigingDoorTeVoerenBijEenMutatiebeperkingOfStop));

		controller.save();
	}

	@Test
	public void aanpassingBegindatumNaarNa1OktoberMagTijdensMutatiebeperkingMetPermissie()
			throws Exception
	{
		tester.voerTestUitMetMedewerker(new BronEditNaMutatieBeperking());
		verbintenis.setBekostigd(Ja);

		zetSchooljaarStatus(huidigSchooljaar, MutatiebeperkingIngesteld);

		wijzigBegindatum(verbintenis.getBegindatum(), huidigSchooljaar.getEenJanuari());

		assertThat(mutatieCheck(),
			is(MedewerkerHeeftToestemmingOmWijzigingDoorTeVoerenBijEenMutatiebeperking));

		controller.save();

		BronAanleverMelding melding = getEersteMelding();

		assertThat(melding.isBekostigingsRelevant(), is(false));
	}

	@Test(expected = BronException.class)
	public void aanpassingBegindatumNaarNa1OktoberMagNietTijdensMutatiestop() throws Exception
	{
		tester.voerTestUitMetMedewerker();
		verbintenis.setBekostigd(Ja);

		zetSchooljaarStatus(huidigSchooljaar, MutatiestopIngesteld);

		wijzigBegindatum(verbintenis.getBegindatum(), huidigSchooljaar.getEenJanuari());

		assertThat(mutatieCheck(),
			is(MedewerkerHeeftGeenToestemmingOmWijzigingDoorTeVoerenBijEenMutatiebeperkingOfStop));

		controller.save();
	}

	@Test
	public void aanpassingBegindatumNaarNa1OktoberMagTijdensMutatiestopMetPermissie()
			throws Exception
	{
		tester.voerTestUitMetMedewerker(new BronEditNaMutatieBeperking());
		verbintenis.setBekostigd(Ja);

		zetSchooljaarStatus(huidigSchooljaar, MutatiestopIngesteld);

		wijzigBegindatum(verbintenis.getBegindatum(), huidigSchooljaar.getEenJanuari());

		assertThat(mutatieCheck(),
			is(MedewerkerHeeftToestemmingOmWijzigingDoorTeVoerenBijEenMutatiestop));

		controller.save();

		BronAanleverMelding melding = getEersteMelding();

		assertThat(melding.isBekostigingsRelevant(), is(true));
	}

	@Test(expected = BronException.class)
	public void aanpassingBegindatumNaarNa1OktoberMagNietNaOpgesteldAssurancerapport()
			throws Exception
	{
		tester.voerTestUitMetMedewerker();
		verbintenis.setBekostigd(Ja);

		zetSchooljaarStatus(huidigSchooljaar, AssurancerapportOpgesteld);

		wijzigBegindatum(verbintenis.getBegindatum(), huidigSchooljaar.getEenJanuari());

		assertThat(mutatieCheck(), is(SchooljaarIsAfgeslotenVoorMutaties));

		controller.save();
	}

	@Test(expected = BronException.class)
	public void aanpassingBegindatumNaarNa1OktoberMagNietNaOpgesteldAssurancerapportMetPermissie()
			throws Exception
	{
		tester.voerTestUitMetMedewerker(new BronEditNaMutatieBeperking());
		verbintenis.setBekostigd(Ja);

		zetSchooljaarStatus(huidigSchooljaar, AssurancerapportOpgesteld);

		wijzigBegindatum(verbintenis.getBegindatum(), huidigSchooljaar.getEenJanuari());

		assertThat(mutatieCheck(), is(SchooljaarIsAfgeslotenVoorMutaties));

		controller.save();

		BronAanleverMelding melding = getEersteMelding();

		assertThat(melding.isBekostigingsRelevant(), is(false));
	}

	@Test
	public void aanpassingBegindatumNaar1SeptemberMagTijdensVrijeInvoer() throws Exception
	{
		tester.voerTestUitMetMedewerker();
		verbintenis.setBekostigd(Ja);

		zetSchooljaarStatus(huidigSchooljaar, GegevensWordenIngevoerd);

		wijzigBegindatum(verbintenis.getBegindatum(), eenSeptember);

		assertThat(mutatieCheck(), is(AlleMutatiesZijnNogToegestaan));

		controller.save();

		BronAanleverMelding melding = getEersteMelding();

		assertThat(melding.isBekostigingsRelevant(), is(false));
	}

	@Test
	public void aanpassingBegindatumNaar1SeptemberMagTijdensMutatiebeperking() throws Exception
	{
		tester.voerTestUitMetMedewerker();
		verbintenis.setBekostigd(Ja);

		zetSchooljaarStatus(huidigSchooljaar, MutatiebeperkingIngesteld);

		wijzigBegindatum(verbintenis.getBegindatum(), eenSeptember);

		assertThat(mutatieCheck(), is(NietBekostigdOpRecentePeildatum));

		controller.save();

		BronAanleverMelding melding = getEersteMelding();

		assertThat(melding.isBekostigingsRelevant(), is(false));
	}

	@Test
	public void aanpassingBegindatumNaar1SeptemberMagTijdensMutatiebeperkingMetPermissie()
			throws Exception
	{
		tester.voerTestUitMetMedewerker(new BronEditNaMutatieBeperking());
		verbintenis.setBekostigd(Ja);

		zetSchooljaarStatus(huidigSchooljaar, MutatiebeperkingIngesteld);

		wijzigBegindatum(verbintenis.getBegindatum(), eenSeptember);

		assertThat(mutatieCheck(), is(NietBekostigdOpRecentePeildatum));

		controller.save();

		BronAanleverMelding melding = getEersteMelding();

		assertThat(melding.isBekostigingsRelevant(), is(false));
	}

	@Test
	public void aanpassingBegindatumNaar1SeptemberMagTijdensMutatiestop() throws Exception
	{
		tester.voerTestUitMetMedewerker();
		verbintenis.setBekostigd(Ja);

		zetSchooljaarStatus(huidigSchooljaar, MutatiestopIngesteld);

		wijzigBegindatum(verbintenis.getBegindatum(), eenSeptember);

		assertThat(mutatieCheck(), is(NietBekostigdOpRecentePeildatum));

		controller.save();

		BronAanleverMelding melding = getEersteMelding();

		assertThat(melding.isBekostigingsRelevant(), is(false));
	}

	@Test
	public void aanpassingBegindatumNaar1SeptemberMagTijdensMutatiestopMetPermissie()
			throws Exception
	{
		tester.voerTestUitMetMedewerker(new BronEditNaMutatieBeperking());
		verbintenis.setBekostigd(Ja);

		zetSchooljaarStatus(huidigSchooljaar, MutatiestopIngesteld);

		wijzigBegindatum(verbintenis.getBegindatum(), eenSeptember);

		assertThat(mutatieCheck(), is(NietBekostigdOpRecentePeildatum));

		controller.save();

		BronAanleverMelding melding = getEersteMelding();

		assertThat(melding.isBekostigingsRelevant(), is(false));
	}

	@Test
	public void aanpassingBegindatumNaar1SeptemberMagNaOpgesteldAssurancerapport() throws Exception
	{
		tester.voerTestUitMetMedewerker();
		verbintenis.setBekostigd(Ja);

		zetSchooljaarStatus(huidigSchooljaar, AssurancerapportOpgesteld);

		wijzigBegindatum(verbintenis.getBegindatum(), eenSeptember);

		assertThat(mutatieCheck(), is(NietBekostigdOpRecentePeildatum));

		controller.save();

		BronAanleverMelding melding = getEersteMelding();

		assertThat(melding.isBekostigingsRelevant(), is(false));
	}

	@Test
	public void aanpassingBegindatumNaar1SeptemberMagNaOpgesteldAssurancerapportMetPermissie()
			throws Exception
	{
		tester.voerTestUitMetMedewerker(new BronEditNaMutatieBeperking());
		verbintenis.setBekostigd(Ja);

		zetSchooljaarStatus(huidigSchooljaar, AssurancerapportOpgesteld);

		wijzigBegindatum(verbintenis.getBegindatum(), eenSeptember);

		assertThat(mutatieCheck(), is(NietBekostigdOpRecentePeildatum));

		controller.save();

		BronAanleverMelding melding = getEersteMelding();

		assertThat(melding.isBekostigingsRelevant(), is(false));
	}

	@Test
	public void aanpassingEinddatumNaarVoor1FebruariMagTijdensVrijeInvoer() throws Exception
	{
		tester.voerTestUitMetMedewerker();
		verbintenis.setBekostigd(Ja);

		zetSchooljaarStatus(huidigSchooljaar, GegevensWordenIngevoerd);

		wijzigStatus(Beeindigd, verbintenis.getStatus());
		wijzigEinddatum(verbintenis.getEinddatum(), huidigSchooljaar.getEenJanuari());

		assertThat(mutatieCheck(), is(AlleMutatiesZijnNogToegestaan));

		controller.save();

		BronAanleverMelding melding = getEersteMelding();

		assertThat(melding.isBekostigingsRelevant(), is(false));
	}

	@Test(expected = BronException.class)
	public void aanpassingEinddatumNaarVoor1FebruariMagNietTijdensMutatiebeperking()
			throws Exception
	{
		tester.voerTestUitMetMedewerker();
		verbintenis.setBekostigd(Ja);

		zetSchooljaarStatus(huidigSchooljaar, MutatiebeperkingIngesteld);

		wijzigStatus(Beeindigd, verbintenis.getStatus());
		wijzigEinddatum(verbintenis.getEinddatum(), huidigSchooljaar.getEenJanuari());

		assertThat(mutatieCheck(),
			is(MedewerkerHeeftGeenToestemmingOmWijzigingDoorTeVoerenBijEenMutatiebeperkingOfStop));

		controller.save();
	}

	@Test
	public void aanpassingEinddatumNaarVoor1FebruariMagTijdensMutatiebeperkingMetPermissie()
			throws Exception
	{
		tester.voerTestUitMetMedewerker(new BronEditNaMutatieBeperking());
		verbintenis.setBekostigd(Ja);

		zetSchooljaarStatus(huidigSchooljaar, MutatiebeperkingIngesteld);

		wijzigStatus(Beeindigd, verbintenis.getStatus());
		wijzigEinddatum(verbintenis.getEinddatum(), huidigSchooljaar.getEenJanuari());

		assertThat(mutatieCheck(),
			is(MedewerkerHeeftToestemmingOmWijzigingDoorTeVoerenBijEenMutatiebeperking));

		controller.save();

		BronAanleverMelding melding = getEersteMelding();

		assertThat(melding.isBekostigingsRelevant(), is(false));
	}

	@Test(expected = BronException.class)
	public void aanpassingEinddatumNaarVoor1FebruariMagNietTijdensMutatiestop() throws Exception
	{
		tester.voerTestUitMetMedewerker();
		verbintenis.setBekostigd(Ja);

		zetSchooljaarStatus(huidigSchooljaar, MutatiestopIngesteld);

		wijzigStatus(Beeindigd, verbintenis.getStatus());
		wijzigEinddatum(verbintenis.getEinddatum(), huidigSchooljaar.getEenJanuari());

		assertThat(mutatieCheck(),
			is(MedewerkerHeeftGeenToestemmingOmWijzigingDoorTeVoerenBijEenMutatiebeperkingOfStop));

		controller.save();
	}

	@Test
	public void aanpassingEinddatumNaarVoor1FebruariMagTijdensMutatiestopMetPermissie()
			throws Exception
	{
		tester.voerTestUitMetMedewerker(new BronEditNaMutatieBeperking());
		verbintenis.setBekostigd(Ja);

		zetSchooljaarStatus(huidigSchooljaar, MutatiestopIngesteld);

		wijzigStatus(Beeindigd, verbintenis.getStatus());
		wijzigEinddatum(verbintenis.getEinddatum(), huidigSchooljaar.getEenJanuari());

		assertThat(mutatieCheck(),
			is(MedewerkerHeeftToestemmingOmWijzigingDoorTeVoerenBijEenMutatiestop));

		controller.save();

		BronAanleverMelding melding = getEersteMelding();

		assertThat(melding.isBekostigingsRelevant(), is(true));
	}

	@Test(expected = BronException.class)
	public void aanpassingEinddatumNaarVoor1FebruariMagNietNaOpgesteldAssurancerapport()
			throws Exception
	{
		tester.voerTestUitMetMedewerker();
		verbintenis.setBekostigd(Ja);

		zetSchooljaarStatus(huidigSchooljaar, AssurancerapportOpgesteld);

		wijzigStatus(Beeindigd, verbintenis.getStatus());
		wijzigEinddatum(verbintenis.getEinddatum(), huidigSchooljaar.getEenJanuari());

		assertThat(mutatieCheck(), is(SchooljaarIsAfgeslotenVoorMutaties));

		controller.save();
	}

	@Test(expected = BronException.class)
	public void aanpassingEinddatumNaarVoor1FebruariMagNietNaOpgesteldAssurancerapportMetPermissie()
			throws Exception
	{
		tester.voerTestUitMetMedewerker(new BronEditNaMutatieBeperking());
		verbintenis.setBekostigd(Ja);

		zetSchooljaarStatus(huidigSchooljaar, AssurancerapportOpgesteld);

		wijzigEinddatum(verbintenis.getEinddatum(), huidigSchooljaar.getEenJanuari());

		wijzigStatus(Beeindigd, verbintenis.getStatus());
		assertThat(mutatieCheck(), is(SchooljaarIsAfgeslotenVoorMutaties));

		controller.save();

		BronAanleverMelding melding = getEersteMelding();

		assertThat(melding.isBekostigingsRelevant(), is(false));
	}

	@Test
	public void aanpassingEinddatumNaar31JuliMagTijdensVrijeInvoer() throws Exception
	{
		tester.voerTestUitMetMedewerker();
		verbintenis.setBekostigd(Ja);

		zetSchooljaarStatus(huidigSchooljaar, GegevensWordenIngevoerd);

		wijzigStatus(verbintenis.getStatus(), Beeindigd);
		wijzigEinddatum(verbintenis.getEinddatum(), huidigSchooljaar.getEinddatum());

		assertThat(mutatieCheck(), is(AlleMutatiesZijnNogToegestaan));

		controller.save();

		BronAanleverMelding melding = getEersteMelding();

		assertThat(melding.isBekostigingsRelevant(), is(false));
	}

	@Test
	public void aanpassingEinddatumNaar31JuliMagTijdensMutatiebeperking() throws Exception
	{
		tester.voerTestUitMetMedewerker();
		verbintenis.setBekostigd(Ja);

		zetSchooljaarStatus(huidigSchooljaar, MutatiebeperkingIngesteld);
		zetSchooljaarStatus(volgendSchooljaar, GegevensWordenIngevoerd);

		wijzigStatus(verbintenis.getStatus(), Beeindigd);
		wijzigEinddatum(verbintenis.getEinddatum(), huidigSchooljaar.getEinddatum());

		assertThat(mutatieCheck(), is(GeenMutatiebeperkingOfStopGevondenVoorDezeMutatie));

		controller.save();

		BronAanleverMelding melding = getEersteMelding();

		assertThat(melding.isBekostigingsRelevant(), is(false));
	}

	@Test
	public void aanpassingEinddatumNaar31JuliMagTijdensMutatiebeperkingMetPermissie()
			throws Exception
	{
		tester.voerTestUitMetMedewerker(new BronEditNaMutatieBeperking());
		verbintenis.setBekostigd(Ja);

		zetSchooljaarStatus(huidigSchooljaar, MutatiebeperkingIngesteld);
		zetSchooljaarStatus(volgendSchooljaar, GegevensWordenIngevoerd);

		wijzigStatus(verbintenis.getStatus(), Beeindigd);
		wijzigEinddatum(verbintenis.getEinddatum(), huidigSchooljaar.getEinddatum());

		assertThat(mutatieCheck(), is(GeenMutatiebeperkingOfStopGevondenVoorDezeMutatie));

		controller.save();

		BronAanleverMelding melding = getEersteMelding();

		assertThat(melding.isBekostigingsRelevant(), is(false));
	}

	@Test
	public void aanpassingEinddatumNaar31JuliMagTijdensMutatiestop() throws Exception
	{
		tester.voerTestUitMetMedewerker();
		verbintenis.setBekostigd(Ja);

		zetSchooljaarStatus(huidigSchooljaar, MutatiestopIngesteld);
		zetSchooljaarStatus(volgendSchooljaar, GegevensWordenIngevoerd);

		wijzigStatus(verbintenis.getStatus(), Beeindigd);
		wijzigEinddatum(verbintenis.getEinddatum(), huidigSchooljaar.getEinddatum());

		assertThat(mutatieCheck(), is(GeenMutatiebeperkingOfStopGevondenVoorDezeMutatie));

		controller.save();

		BronAanleverMelding melding = getEersteMelding();

		assertThat(melding.isBekostigingsRelevant(), is(false));
	}

	@Test
	public void aanpassingEinddatumNaar31JuliMagTijdensMutatiestopMetPermissie() throws Exception
	{
		tester.voerTestUitMetMedewerker(new BronEditNaMutatieBeperking());
		verbintenis.setBekostigd(Ja);

		zetSchooljaarStatus(huidigSchooljaar, MutatiestopIngesteld);
		zetSchooljaarStatus(volgendSchooljaar, GegevensWordenIngevoerd);

		wijzigStatus(verbintenis.getStatus(), Beeindigd);
		wijzigEinddatum(verbintenis.getEinddatum(), huidigSchooljaar.getEinddatum());

		assertThat(mutatieCheck(), is(GeenMutatiebeperkingOfStopGevondenVoorDezeMutatie));

		controller.save();

		BronAanleverMelding melding = getEersteMelding();

		assertThat(melding.isBekostigingsRelevant(), is(false));
	}

	@Test
	public void aanpassingEinddatumNaar31JuliMagNaOpgesteldAssurancerapport() throws Exception
	{
		tester.voerTestUitMetMedewerker();
		verbintenis.setBekostigd(Ja);

		zetSchooljaarStatus(huidigSchooljaar, AssurancerapportOpgesteld);
		zetSchooljaarStatus(volgendSchooljaar, GegevensWordenIngevoerd);

		wijzigStatus(verbintenis.getStatus(), Beeindigd);
		wijzigEinddatum(verbintenis.getEinddatum(), huidigSchooljaar.getEinddatum());

		assertThat(mutatieCheck(), is(GeenMutatiebeperkingOfStopGevondenVoorDezeMutatie));

		controller.save();

		BronAanleverMelding melding = getEersteMelding();

		assertThat(melding.isBekostigingsRelevant(), is(false));
	}

	@Test
	public void aanpassingEinddatumNaar31JuliMagNaOpgesteldAssurancerapportMetPermissie()
			throws Exception
	{
		tester.voerTestUitMetMedewerker(new BronEditNaMutatieBeperking());
		verbintenis.setBekostigd(Ja);

		zetSchooljaarStatus(huidigSchooljaar, AssurancerapportOpgesteld);
		zetSchooljaarStatus(volgendSchooljaar, GegevensWordenIngevoerd);

		wijzigStatus(verbintenis.getStatus(), Beeindigd);
		wijzigEinddatum(verbintenis.getEinddatum(), huidigSchooljaar.getEinddatum());

		assertThat(mutatieCheck(), is(GeenMutatiebeperkingOfStopGevondenVoorDezeMutatie));

		controller.save();

		BronAanleverMelding melding = getEersteMelding();

		assertThat(melding.isBekostigingsRelevant(), is(false));
	}

	@Test(expected = BronException.class)
	public void aanpassingEinddatumVan1JanuariNaar31JuliVolgendSchooljaarMagNietBijOpgesteldAssurancerapportMetPermissie()
			throws Exception
	{
		tester.voerTestUitMetMedewerker(new BronEditNaMutatieBeperking());
		verbintenis.setBekostigd(Ja);

		zetSchooljaarStatus(huidigSchooljaar, AssurancerapportOpgesteld);
		zetSchooljaarStatus(volgendSchooljaar, GegevensWordenIngevoerd);

		wijzigStatus(verbintenis.getStatus(), Beeindigd);
		wijzigEinddatum(huidigSchooljaar.getEenJanuari(), huidigSchooljaar.getVolgendSchooljaar()
			.getEinddatum());

		assertThat(mutatieCheck(), is(SchooljaarIsAfgeslotenVoorMutaties));

		controller.save();
	}

	@Test(expected = BronException.class)
	public void aanpassingEinddatumVan31JuliHuidigSchooljaarNaar31JuliVolgendSchooljaarMagNietBijMutatiestop()
			throws Exception
	{
		tester.voerTestUitMetMedewerker();
		verbintenis.setBekostigd(Ja);
		verbintenis.setBegindatum(vorigSchooljaar.getBegindatum());
		verbintenis.setEinddatum(vorigSchooljaar.getEinddatum());

		zetSchooljaarStatus(vorigSchooljaar, AssurancerapportOpgesteld);
		zetSchooljaarStatus(huidigSchooljaar, MutatiestopIngesteld);

		wijzigStatus(verbintenis.getStatus(), Beeindigd);
		wijzigEinddatum(vorigSchooljaar.getEinddatum(), huidigSchooljaar.getEinddatum());

		assertThat(mutatieCheck(),
			is(MedewerkerHeeftGeenToestemmingOmWijzigingDoorTeVoerenBijEenMutatiebeperkingOfStop));

		controller.save();
	}

	@Test
	public void aanpassingEinddatumVan31JuliHuidigSchooljaarNaar31JuliVolgendSchooljaarMagBijMutatiestopMetPermissie()
			throws Exception
	{
		tester.voerTestUitMetMedewerker(new BronEditNaMutatieBeperking());
		verbintenis.setBekostigd(Ja);
		verbintenis.setBegindatum(vorigSchooljaar.getBegindatum());
		verbintenis.setEinddatum(vorigSchooljaar.getEinddatum());

		zetSchooljaarStatus(vorigSchooljaar, AssurancerapportOpgesteld);
		zetSchooljaarStatus(huidigSchooljaar, MutatiestopIngesteld);

		wijzigStatus(verbintenis.getStatus(), Beeindigd);
		wijzigEinddatum(vorigSchooljaar.getEinddatum(), huidigSchooljaar.getEinddatum());

		assertThat(mutatieCheck(),
			is(MedewerkerHeeftToestemmingOmWijzigingDoorTeVoerenBijEenMutatiestop));

		controller.save();

		BronAanleverMelding melding = getEersteMelding();

		assertThat(melding.isBekostigingsRelevant(), is(true));
	}

	@Test
	public void undoBeeindigingVerbintenisMagTijdensVrijeInvoer() throws Exception
	{
		tester.voerTestUitMetMedewerker();
		verbintenis.setBekostigd(Ja);
		verbintenis.setBegindatum(huidigSchooljaar.getBegindatum());
		verbintenis.setEinddatum(huidigSchooljaar.getEenJanuari());
		verbintenis.setStatus(Beeindigd);

		zetSchooljaarStatus(vorigSchooljaar, AssurancerapportOpgesteld);
		zetSchooljaarStatus(huidigSchooljaar, GegevensWordenIngevoerd);

		wijzigStatus(verbintenis.getStatus(), Definitief);
		wijzigEinddatum(verbintenis.getEinddatum(), null);

		assertThat(mutatieCheck(), is(AlleMutatiesZijnNogToegestaan));

		controller.save();

		BronAanleverMelding melding = getEersteMelding();

		assertThat(melding.isBekostigingsRelevant(), is(false));
	}

	@Test(expected = BronException.class)
	public void undoBeeindigingVerbintenisMagNietTijdensMutatiebeperking() throws Exception
	{
		tester.voerTestUitMetMedewerker();
		verbintenis.setBekostigd(Ja);
		verbintenis.setBegindatum(huidigSchooljaar.getBegindatum());
		verbintenis.setEinddatum(huidigSchooljaar.getEenJanuari());
		verbintenis.setStatus(Beeindigd);

		zetSchooljaarStatus(huidigSchooljaar, MutatiebeperkingIngesteld);

		wijzigStatus(verbintenis.getStatus(), Definitief);
		wijzigEinddatum(verbintenis.getEinddatum(), null);

		assertThat(mutatieCheck(),
			is(MedewerkerHeeftGeenToestemmingOmWijzigingDoorTeVoerenBijEenMutatiebeperkingOfStop));

		controller.save();
	}

	@Test
	public void undoBeeindigingVerbintenisMagTijdensMutatiebeperkingMetPermissie() throws Exception
	{
		tester.voerTestUitMetMedewerker(new BronEditNaMutatieBeperking());
		verbintenis.setBekostigd(Ja);
		verbintenis.setBegindatum(huidigSchooljaar.getBegindatum());
		verbintenis.setEinddatum(huidigSchooljaar.getEenJanuari());
		verbintenis.setStatus(Beeindigd);

		zetSchooljaarStatus(huidigSchooljaar, MutatiebeperkingIngesteld);

		wijzigStatus(verbintenis.getStatus(), Definitief);
		wijzigEinddatum(verbintenis.getEinddatum(), null);

		assertThat(mutatieCheck(),
			is(MedewerkerHeeftToestemmingOmWijzigingDoorTeVoerenBijEenMutatiebeperking));

		controller.save();

		BronAanleverMelding melding = getEersteMelding();

		assertThat(melding.isBekostigingsRelevant(), is(false));
	}

	@Test(expected = BronException.class)
	public void undoBeeindigingVerbintenisMagNietTijdensMutatiestop() throws Exception
	{
		tester.voerTestUitMetMedewerker();
		verbintenis.setBekostigd(Ja);
		verbintenis.setBegindatum(huidigSchooljaar.getBegindatum());
		verbintenis.setEinddatum(huidigSchooljaar.getEenJanuari());
		verbintenis.setStatus(Beeindigd);

		zetSchooljaarStatus(huidigSchooljaar, MutatiestopIngesteld);

		wijzigStatus(verbintenis.getStatus(), Definitief);
		wijzigEinddatum(verbintenis.getEinddatum(), null);

		assertThat(mutatieCheck(),
			is(MedewerkerHeeftGeenToestemmingOmWijzigingDoorTeVoerenBijEenMutatiebeperkingOfStop));

		controller.save();
	}

	@Test
	public void undoBeeindigingVerbintenisMagTijdensMutatiestopMetPermissie() throws Exception
	{
		tester.voerTestUitMetMedewerker(new BronEditNaMutatieBeperking());
		verbintenis.setBekostigd(Ja);
		verbintenis.setBegindatum(huidigSchooljaar.getBegindatum());
		verbintenis.setEinddatum(huidigSchooljaar.getEenJanuari());
		verbintenis.setStatus(Beeindigd);

		zetSchooljaarStatus(huidigSchooljaar, MutatiestopIngesteld);

		wijzigStatus(verbintenis.getStatus(), Definitief);
		wijzigEinddatum(verbintenis.getEinddatum(), null);

		assertThat(mutatieCheck(),
			is(MedewerkerHeeftToestemmingOmWijzigingDoorTeVoerenBijEenMutatiestop));

		controller.save();

		BronAanleverMelding melding = getEersteMelding();

		assertThat(melding.isBekostigingsRelevant(), is(true));
	}

	@Test(expected = BronException.class)
	public void undoBeeindigingVerbintenisMagNietNaOpgesteldAssurancerapportMetPermissie()
			throws Exception
	{
		tester.voerTestUitMetMedewerker(new BronEditNaMutatieBeperking());
		verbintenis.setBekostigd(Ja);
		verbintenis.setBegindatum(huidigSchooljaar.getBegindatum());
		verbintenis.setEinddatum(huidigSchooljaar.getEenJanuari());
		verbintenis.setStatus(Beeindigd);

		zetSchooljaarStatus(huidigSchooljaar, AssurancerapportOpgesteld);

		wijzigStatus(verbintenis.getStatus(), Definitief);
		wijzigEinddatum(verbintenis.getEinddatum(), null);

		assertThat(mutatieCheck(), is(SchooljaarIsAfgeslotenVoorMutaties));

		controller.save();
	}

	@Test
	public void wijzigingBekostigingsperiodeMagTijdensVrijeInvoer() throws BronException
	{
		tester.voerTestUitMetMedewerker();
		createGevalMetMeervoudigeBekostigingsperiodes();

		zetSchooljaarStatus(huidigSchooljaar, GegevensWordenIngevoerd);
		zetSchooljaarStatus(volgendSchooljaar, GegevensWordenIngevoerd);

		addChange(periode1, "bekostigd", true, false);

		assertThat(mutatieCheck(), is(AlleMutatiesZijnNogToegestaan));
		controller.save();

		BronAanleverMelding melding = getEersteMelding();

		assertThat(melding.isBekostigingsRelevant(), is(false));
	}

	@Test
	public void wijzigingBekostigingsperiodeMagTijdensVrijeInvoerMetPermissie()
			throws BronException
	{
		tester.voerTestUitMetMedewerker(new BronEditNaMutatieBeperking());
		createGevalMetMeervoudigeBekostigingsperiodes();

		zetSchooljaarStatus(huidigSchooljaar, GegevensWordenIngevoerd);
		zetSchooljaarStatus(volgendSchooljaar, GegevensWordenIngevoerd);

		addChange(periode1, "bekostigd", true, false);

		assertThat(mutatieCheck(), is(AlleMutatiesZijnNogToegestaan));
		controller.save();

		BronAanleverMelding melding = getEersteMelding();

		assertThat(melding.isBekostigingsRelevant(), is(false));
	}

	@Test(expected = BronException.class)
	public void wijzigingBekostigingsperiodeMagNietTijdensMutatiebeperking() throws BronException
	{
		tester.voerTestUitMetMedewerker();
		createGevalMetMeervoudigeBekostigingsperiodes();

		zetSchooljaarStatus(huidigSchooljaar, MutatiebeperkingIngesteld);
		zetSchooljaarStatus(volgendSchooljaar, GegevensWordenIngevoerd);

		assertThat(mutatieCheck(periode1, true, false),
			is(MedewerkerHeeftGeenToestemmingOmWijzigingDoorTeVoerenBijEenMutatiebeperkingOfStop));
		controller.save();
	}

	@Test
	public void wijzigingBekostigingsperiodeMagWelTijdensMutatiebeperkingMetPermissie()
			throws BronException
	{
		tester.voerTestUitMetMedewerker(new BronEditNaMutatieBeperking());
		createGevalMetMeervoudigeBekostigingsperiodes();

		zetSchooljaarStatus(huidigSchooljaar, MutatiebeperkingIngesteld);
		zetSchooljaarStatus(volgendSchooljaar, GegevensWordenIngevoerd);

		assertThat(mutatieCheck(periode1, true, false),
			is(MedewerkerHeeftToestemmingOmWijzigingDoorTeVoerenBijEenMutatiebeperking));
		controller.save();

		BronAanleverMelding melding = getEersteMelding();

		assertThat(melding.isBekostigingsRelevant(), is(false));
	}

	@Test(expected = BronException.class)
	public void wijzigingBekostigingsperiodeMagNietTijdensMutatiestop() throws BronException
	{
		tester.voerTestUitMetMedewerker();
		createGevalMetMeervoudigeBekostigingsperiodes();

		zetSchooljaarStatus(huidigSchooljaar, MutatiestopIngesteld);
		zetSchooljaarStatus(volgendSchooljaar, GegevensWordenIngevoerd);

		assertThat(mutatieCheck(periode1, true, false),
			is(MedewerkerHeeftGeenToestemmingOmWijzigingDoorTeVoerenBijEenMutatiebeperkingOfStop));
		controller.save();
	}

	@Test
	public void wijzigingBekostigingsperiodeMagWelTijdensMutatiestopMetPermissie()
			throws BronException
	{
		tester.voerTestUitMetMedewerker(new BronEditNaMutatieBeperking());
		createGevalMetMeervoudigeBekostigingsperiodes();

		zetSchooljaarStatus(huidigSchooljaar, MutatiestopIngesteld);
		zetSchooljaarStatus(volgendSchooljaar, GegevensWordenIngevoerd);

		assertThat(mutatieCheck(periode1, true, false),
			is(MedewerkerHeeftToestemmingOmWijzigingDoorTeVoerenBijEenMutatiestop));
		controller.save();

		BronAanleverMelding melding = getEersteMelding();

		assertThat(melding.isBekostigingsRelevant(), is(true));
	}

	@Test(expected = BronException.class)
	public void wijzigingBekostigingsperiodeMagNietNaOpgesteldAssurancerapport()
			throws BronException
	{
		tester.voerTestUitMetMedewerker();
		createGevalMetMeervoudigeBekostigingsperiodes();

		zetSchooljaarStatus(huidigSchooljaar, AssurancerapportOpgesteld);
		zetSchooljaarStatus(volgendSchooljaar, GegevensWordenIngevoerd);

		assertThat(mutatieCheck(periode1, true, false), is(SchooljaarIsAfgeslotenVoorMutaties));
		controller.save();
	}

	@Test(expected = BronException.class)
	public void wijzigingBekostigingsperiodeMagNietNaOpgesteldAssurancerapportMetPermissie()
			throws BronException
	{
		tester.voerTestUitMetMedewerker(new BronEditNaMutatieBeperking());
		createGevalMetMeervoudigeBekostigingsperiodes();

		zetSchooljaarStatus(huidigSchooljaar, AssurancerapportOpgesteld);
		zetSchooljaarStatus(volgendSchooljaar, GegevensWordenIngevoerd);

		assertThat(mutatieCheck(periode1, true, false), is(SchooljaarIsAfgeslotenVoorMutaties));
		controller.save();
	}

	private void createGevalMetMeervoudigeBekostigingsperiodes()
	{
		getDeelnemer1001();

		verbintenis.setBegindatum(huidigSchooljaar.getBegindatum());
		verbintenis.setEinddatum(null);
		verbintenis.setStatus(Definitief);
		verbintenis.setBekostigd(Gedeeltelijk);

		periode1 = new Bekostigingsperiode();
		periode1.setVerbintenis(verbintenis);
		periode1.setBegindatum(huidigSchooljaar.getBegindatum());
		periode1.setEinddatum(huidigSchooljaar.getEinddatum());
		periode1.setBekostigd(true);
		verbintenis.getBekostigingsperiodes().add(periode1);

		periode2 = new Bekostigingsperiode();
		periode2.setVerbintenis(verbintenis);
		periode2.setBegindatum(huidigSchooljaar.getVolgendSchooljaar().getBegindatum());
		periode2.setBekostigd(true);
		verbintenis.getBekostigingsperiodes().add(periode2);
	}

	@Test
	public void wijzigingBekostigingsperiodeDieGeenTeldatumRaaktMagWelTijdensMutatiestop()
			throws BronException
	{
		tester.voerTestUitMetMedewerker();

		getDeelnemer1001();

		verbintenis.setBegindatum(huidigSchooljaar.getBegindatum());
		verbintenis.setEinddatum(null);
		verbintenis.setStatus(Definitief);
		verbintenis.setBekostigd(Gedeeltelijk);

		periode1 = new Bekostigingsperiode();
		periode1.setVerbintenis(verbintenis);
		periode1.setBegindatum(huidigSchooljaar.getBegindatum());
		periode1.setEinddatum(TimeUtil.getInstance().addDays(huidigSchooljaar.getEenOktober(), -1));
		periode1.setBekostigd(false);
		verbintenis.getBekostigingsperiodes().add(periode1);

		periode2 = new Bekostigingsperiode();
		periode2.setVerbintenis(verbintenis);
		periode2.setBegindatum(huidigSchooljaar.getEenOktober());
		periode2.setBekostigd(true);
		verbintenis.getBekostigingsperiodes().add(periode2);

		zetSchooljaarStatus(huidigSchooljaar, MutatiestopIngesteld);
		zetSchooljaarStatus(volgendSchooljaar, GegevensWordenIngevoerd);

		assertThat(mutatieCheck(periode1, false, true),
			is(GeenMutatiebeperkingOfStopGevondenVoorDezeMutatie));
		controller.save();

		BronAanleverMelding melding = getEersteMelding();

		assertThat(melding.isBekostigingsRelevant(), is(false));
	}

	@Test
	public void wijzigingTweedeBekostigingsperiodeDieGeenTeldatumRaaktMagWelTijdensMutatiestop()
			throws BronException
	{
		tester.voerTestUitMetMedewerker();

		getDeelnemer1001();

		verbintenis.setBegindatum(huidigSchooljaar.getBegindatum());
		verbintenis.setEinddatum(null);
		verbintenis.setStatus(Definitief);
		verbintenis.setBekostigd(Gedeeltelijk);

		periode1 = new Bekostigingsperiode();
		periode1.setVerbintenis(verbintenis);
		periode1.setBegindatum(huidigSchooljaar.getBegindatum());
		periode1.setEinddatum(huidigSchooljaar.getEenOktober());
		periode1.setBekostigd(true);
		verbintenis.getBekostigingsperiodes().add(periode1);

		periode2 = new Bekostigingsperiode();
		periode2.setVerbintenis(verbintenis);

		TimeUtil timeutil = TimeUtil.getInstance();
		periode2.setBegindatum(timeutil.addDays(huidigSchooljaar.getEenOktober(), 1));
		periode2.setEinddatum(timeutil.addDays(huidigSchooljaar.getEenFebruari(), -1));
		periode2.setBekostigd(true);
		verbintenis.getBekostigingsperiodes().add(periode2);

		Bekostigingsperiode periode3 = new Bekostigingsperiode();
		periode3.setVerbintenis(verbintenis);
		periode3.setBegindatum(huidigSchooljaar.getEenFebruari());
		periode3.setBekostigd(true);
		verbintenis.getBekostigingsperiodes().add(periode3);

		zetSchooljaarStatus(huidigSchooljaar, MutatiestopIngesteld);
		zetSchooljaarStatus(volgendSchooljaar, GegevensWordenIngevoerd);

		assertThat(mutatieCheck(periode2, true, false), is(NietBekostigdOpRecentePeildatum));
		controller.save();

		BronAanleverMelding melding = getEersteMelding();

		assertThat(melding.isBekostigingsRelevant(), is(false));
	}

	@Test
	public void verwijderingMagMetBekostigdeExamendeelnamesTijdensVrijeInvoer()
			throws BronException
	{
		tester.voerTestUitMetMedewerker();

		getDeelnemer1016();
		verbintenis.setBegindatum(huidigSchooljaar.getBegindatum());
		verbintenis.setEinddatum(huidigSchooljaar.getEinddatum());
		deelname1.setDatumUitslag(eenSeptember);

		zetSchooljaarStatus(huidigSchooljaar, GegevensWordenIngevoerd);
		wijzigStatus(Definitief, Voorlopig);

		assertThat(mutatieCheck(), is(AlleMutatiesZijnNogToegestaan));

		controller.save();

		BronAanleverMelding melding = getEersteMelding();
		assertThat(melding.isBekostigingsRelevant(), is(false));
	}

	@Test(expected = BronException.class)
	public void verwijderingMagNietMetBekostigdeExamendeelnamesTijdensMutatiebeperking()
			throws BronException
	{
		tester.voerTestUitMetMedewerker();

		getDeelnemer1016();
		verbintenis.setBegindatum(huidigSchooljaar.getBegindatum());
		verbintenis.setEinddatum(huidigSchooljaar.getEinddatum());
		deelname1.setDatumUitslag(eenSeptember);

		zetSchooljaarStatus(huidigSchooljaar, MutatiebeperkingIngesteld);
		wijzigStatus(Definitief, Voorlopig);

		assertThat(mutatieCheck(),
			is(MedewerkerHeeftGeenToestemmingOmWijzigingDoorTeVoerenBijEenMutatiebeperkingOfStop));

		controller.save();
	}

	@Test
	public void verwijderingMagWelMetBekostigdeExamendeelnamesTijdensMutatiebeperkingEnPermissie()
			throws BronException
	{
		tester.voerTestUitMetMedewerker(new BronEditNaMutatieBeperking());

		getDeelnemer1016();
		verbintenis.setBegindatum(huidigSchooljaar.getBegindatum());
		verbintenis.setEinddatum(huidigSchooljaar.getEinddatum());
		deelname1.setDatumUitslag(eenSeptember);

		zetSchooljaarStatus(huidigSchooljaar, MutatiebeperkingIngesteld);
		wijzigStatus(Definitief, Voorlopig);

		assertThat(mutatieCheck(),
			is(MedewerkerHeeftToestemmingOmWijzigingDoorTeVoerenBijEenMutatiebeperking));

		controller.save();

		BronAanleverMelding melding = getEersteMelding();
		assertThat(melding.isBekostigingsRelevant(), is(false));
	}

	@Test(expected = BronException.class)
	public void verwijderingMagNietMetBekostigdeExamendeelnamesTijdensMutatiestop()
			throws BronException
	{
		tester.voerTestUitMetMedewerker();

		getDeelnemer1016();
		verbintenis.setBegindatum(huidigSchooljaar.getBegindatum());
		verbintenis.setEinddatum(huidigSchooljaar.getEinddatum());
		deelname1.setDatumUitslag(eenSeptember);

		zetSchooljaarStatus(huidigSchooljaar, MutatiestopIngesteld);
		wijzigStatus(Definitief, Voorlopig);

		assertThat(mutatieCheck(),
			is(MedewerkerHeeftGeenToestemmingOmWijzigingDoorTeVoerenBijEenMutatiebeperkingOfStop));

		controller.save();
	}

	@Test
	public void verwijderingMagWelMetBekostigdeExamendeelnamesTijdensMutatiestopEnPermissie()
			throws BronException
	{
		tester.voerTestUitMetMedewerker(new BronEditNaMutatieBeperking());

		getDeelnemer1016();
		verbintenis.setBegindatum(huidigSchooljaar.getBegindatum());
		verbintenis.setEinddatum(huidigSchooljaar.getEinddatum());
		deelname1.setDatumUitslag(eenSeptember);

		zetSchooljaarStatus(huidigSchooljaar, MutatiestopIngesteld);
		wijzigStatus(Definitief, Voorlopig);

		assertThat(mutatieCheck(),
			is(MedewerkerHeeftToestemmingOmWijzigingDoorTeVoerenBijEenMutatiestop));

		controller.save();

		BronAanleverMelding melding = getEersteMelding();
		assertThat(melding.isBekostigingsRelevant(), is(true));
	}

	@Test(expected = BronException.class)
	public void verwijderingMagNietMetBekostigdeExamendeelnamesNaOpgesteldAssurancerapport()
			throws BronException
	{
		tester.voerTestUitMetMedewerker();

		getDeelnemer1016();
		verbintenis.setBegindatum(huidigSchooljaar.getBegindatum());
		verbintenis.setEinddatum(huidigSchooljaar.getEinddatum());
		deelname1.setDatumUitslag(eenSeptember);

		zetSchooljaarStatus(huidigSchooljaar, AssurancerapportOpgesteld);
		wijzigStatus(Definitief, Voorlopig);

		assertThat(mutatieCheck(), is(SchooljaarIsAfgeslotenVoorMutaties));

		controller.save();
	}

	@Test(expected = BronException.class)
	public void verwijderingMagNietMetBekostigdeExamendeelnamesNaOpgesteldAssurancerapportEnPermissie()
			throws BronException
	{
		tester.voerTestUitMetMedewerker(new BronEditNaMutatieBeperking());

		getDeelnemer1016();
		verbintenis.setBegindatum(huidigSchooljaar.getBegindatum());
		verbintenis.setEinddatum(huidigSchooljaar.getEinddatum());
		deelname1.setDatumUitslag(eenSeptember);

		zetSchooljaarStatus(huidigSchooljaar, AssurancerapportOpgesteld);
		wijzigStatus(Definitief, Voorlopig);

		assertThat(mutatieCheck(), is(SchooljaarIsAfgeslotenVoorMutaties));

		controller.save();
	}

	@Test
	public void verwijderingMagMetNietBekostigdeExamendeelnamesTijdensVrijeInvoer()
			throws BronException
	{
		tester.voerTestUitMetMedewerker();

		getDeelnemer1016();
		verbintenis.setBegindatum(huidigSchooljaar.getBegindatum());
		verbintenis.setEinddatum(huidigSchooljaar.getEinddatum());
		deelname1.setBekostigd(false);
		deelname1.setDatumUitslag(eenSeptember);

		zetSchooljaarStatus(huidigSchooljaar, GegevensWordenIngevoerd);
		wijzigStatus(Definitief, Voorlopig);

		assertThat(mutatieCheck(), is(AlleMutatiesZijnNogToegestaan));

		controller.save();

		BronAanleverMelding melding = getEersteMelding();
		assertThat(melding.isBekostigingsRelevant(), is(false));
	}

	@Test(expected = BronException.class)
	public void verwijderingMagNietMetNietBekostigdeExamendeelnamesTijdensMutatiebeperking()
			throws BronException
	{
		tester.voerTestUitMetMedewerker();

		getDeelnemer1016();
		verbintenis.setBegindatum(huidigSchooljaar.getBegindatum());
		verbintenis.setEinddatum(huidigSchooljaar.getEinddatum());
		deelname1.setBekostigd(false);
		deelname1.setDatumUitslag(eenSeptember);

		zetSchooljaarStatus(huidigSchooljaar, MutatiebeperkingIngesteld);
		wijzigStatus(Definitief, Voorlopig);

		assertThat(mutatieCheck(),
			is(MedewerkerHeeftGeenToestemmingOmWijzigingDoorTeVoerenBijEenMutatiebeperkingOfStop));

		controller.save();
	}

	@Test
	public void verwijderingMagWelMetNietBekostigdeExamendeelnamesTijdensMutatiebeperkingEnPermissie()
			throws BronException
	{
		tester.voerTestUitMetMedewerker(new BronEditNaMutatieBeperking());

		getDeelnemer1016();
		verbintenis.setBegindatum(huidigSchooljaar.getBegindatum());
		verbintenis.setEinddatum(huidigSchooljaar.getEinddatum());
		deelname1.setDatumUitslag(eenSeptember);
		deelname1.setBekostigd(false);

		zetSchooljaarStatus(huidigSchooljaar, MutatiebeperkingIngesteld);
		wijzigStatus(Definitief, Voorlopig);

		assertThat(mutatieCheck(),
			is(MedewerkerHeeftToestemmingOmWijzigingDoorTeVoerenBijEenMutatiebeperking));

		controller.save();

		BronAanleverMelding melding = getEersteMelding();
		assertThat(melding.isBekostigingsRelevant(), is(false));
	}

	@Test(expected = BronException.class)
	public void verwijderingMagNietMetNietBekostigdeExamendeelnamesTijdensMutatiestop()
			throws BronException
	{
		tester.voerTestUitMetMedewerker();

		getDeelnemer1016();
		verbintenis.setBegindatum(huidigSchooljaar.getBegindatum());
		verbintenis.setEinddatum(huidigSchooljaar.getEinddatum());
		deelname1.setDatumUitslag(eenSeptember);

		zetSchooljaarStatus(huidigSchooljaar, MutatiestopIngesteld);
		wijzigStatus(Definitief, Voorlopig);

		assertThat(mutatieCheck(),
			is(MedewerkerHeeftGeenToestemmingOmWijzigingDoorTeVoerenBijEenMutatiebeperkingOfStop));

		controller.save();
	}

	@Test
	public void verwijderingMagWelMetNietBekostigdeExamendeelnamesTijdensMutatiestopEnPermissie()
			throws BronException
	{
		tester.voerTestUitMetMedewerker(new BronEditNaMutatieBeperking());

		getDeelnemer1016();
		verbintenis.setBegindatum(huidigSchooljaar.getBegindatum());
		verbintenis.setEinddatum(huidigSchooljaar.getEinddatum());
		deelname1.setDatumUitslag(eenSeptember);
		deelname1.setBekostigd(false);

		zetSchooljaarStatus(huidigSchooljaar, MutatiestopIngesteld);
		wijzigStatus(Definitief, Voorlopig);

		assertThat(mutatieCheck(),
			is(MedewerkerHeeftToestemmingOmWijzigingDoorTeVoerenBijEenMutatiestop));

		controller.save();

		BronAanleverMelding melding = getEersteMelding();
		assertThat(melding.isBekostigingsRelevant(), is(true));
	}

	@Test
	public void verwijderingNietBekostigdeVerbintenisMagMetBekostigdeExamendeelnamesTijdensVrijeInvoer()
			throws BronException
	{
		tester.voerTestUitMetMedewerker();

		getDeelnemer1016();
		verbintenis.setBekostigd(Nee);
		verbintenis.setBegindatum(huidigSchooljaar.getBegindatum());
		verbintenis.setEinddatum(huidigSchooljaar.getEinddatum());
		deelname1.setDatumUitslag(eenSeptember);

		zetSchooljaarStatus(huidigSchooljaar, GegevensWordenIngevoerd);
		wijzigStatus(Definitief, Voorlopig);

		assertThat(mutatieCheck(), is(AlleMutatiesZijnNogToegestaan));

		controller.save();

		BronAanleverMelding melding = getEersteMelding();
		assertThat(melding.isBekostigingsRelevant(), is(false));
	}

	@Test(expected = BronException.class)
	public void verwijderingNietBekostigdeVerbintenisMagNietMetBekostigdeExamendeelnamesTijdensMutatiebeperking()
			throws BronException
	{
		tester.voerTestUitMetMedewerker();

		getDeelnemer1016();
		verbintenis.setBekostigd(Nee);
		verbintenis.setBegindatum(huidigSchooljaar.getBegindatum());
		verbintenis.setEinddatum(huidigSchooljaar.getEinddatum());
		deelname1.setDatumUitslag(eenSeptember);

		zetSchooljaarStatus(huidigSchooljaar, MutatiebeperkingIngesteld);
		wijzigStatus(Definitief, Voorlopig);

		assertThat(mutatieCheck(),
			is(MedewerkerHeeftGeenToestemmingOmWijzigingDoorTeVoerenBijEenMutatiebeperkingOfStop));

		controller.save();
	}

	@Test
	public void verwijderingNietBekostigdeVerbintenisMagWelMetBekostigdeExamendeelnamesTijdensMutatiebeperkingEnPermissie()
			throws BronException
	{
		tester.voerTestUitMetMedewerker(new BronEditNaMutatieBeperking());

		getDeelnemer1016();
		verbintenis.setBekostigd(Nee);
		verbintenis.setBegindatum(huidigSchooljaar.getBegindatum());
		verbintenis.setEinddatum(huidigSchooljaar.getEinddatum());
		deelname1.setDatumUitslag(eenSeptember);

		zetSchooljaarStatus(huidigSchooljaar, MutatiebeperkingIngesteld);
		wijzigStatus(Definitief, Voorlopig);

		assertThat(mutatieCheck(),
			is(MedewerkerHeeftToestemmingOmWijzigingDoorTeVoerenBijEenMutatiebeperking));

		controller.save();

		BronAanleverMelding melding = getEersteMelding();
		assertThat(melding.isBekostigingsRelevant(), is(false));
	}

	@Test(expected = BronException.class)
	public void verwijderingNietBekostigdeVerbintenisMagNietMetBekostigdeExamendeelnamesTijdensMutatiestop()
			throws BronException
	{
		tester.voerTestUitMetMedewerker();

		getDeelnemer1016();
		verbintenis.setBekostigd(Nee);
		verbintenis.setBegindatum(huidigSchooljaar.getBegindatum());
		verbintenis.setEinddatum(huidigSchooljaar.getEinddatum());
		deelname1.setDatumUitslag(eenSeptember);

		zetSchooljaarStatus(huidigSchooljaar, MutatiestopIngesteld);
		wijzigStatus(Definitief, Voorlopig);

		assertThat(mutatieCheck(),
			is(MedewerkerHeeftGeenToestemmingOmWijzigingDoorTeVoerenBijEenMutatiebeperkingOfStop));

		controller.save();
	}

	@Test
	public void verwijderingNietBekostigdeVerbintenisMagWelMetBekostigdeExamendeelnamesTijdensMutatiestopEnPermissie()
			throws BronException
	{
		tester.voerTestUitMetMedewerker(new BronEditNaMutatieBeperking());

		getDeelnemer1016();
		verbintenis.setBekostigd(Nee);
		verbintenis.setBegindatum(huidigSchooljaar.getBegindatum());
		verbintenis.setEinddatum(huidigSchooljaar.getEinddatum());
		deelname1.setDatumUitslag(eenSeptember);

		zetSchooljaarStatus(huidigSchooljaar, MutatiestopIngesteld);
		wijzigStatus(Definitief, Voorlopig);

		assertThat(mutatieCheck(),
			is(MedewerkerHeeftToestemmingOmWijzigingDoorTeVoerenBijEenMutatiestop));

		controller.save();

		BronAanleverMelding melding = getEersteMelding();
		assertThat(melding.isBekostigingsRelevant(), is(true));
	}

	@Test(expected = BronException.class)
	public void verwijderingNietBekostigdeVerbintenisMagNietMetBekostigdeExamendeelnamesNaOpgesteldAssurancerapport()
			throws BronException
	{
		tester.voerTestUitMetMedewerker();

		getDeelnemer1016();
		verbintenis.setBekostigd(Nee);
		verbintenis.setBegindatum(huidigSchooljaar.getBegindatum());
		verbintenis.setEinddatum(huidigSchooljaar.getEinddatum());
		deelname1.setDatumUitslag(eenSeptember);

		zetSchooljaarStatus(huidigSchooljaar, AssurancerapportOpgesteld);
		wijzigStatus(Definitief, Voorlopig);

		assertThat(mutatieCheck(), is(SchooljaarIsAfgeslotenVoorMutaties));

		controller.save();
	}

	@Test(expected = BronException.class)
	public void verwijderingNietBekostigdeVerbintenisMagNietMetBekostigdeExamendeelnamesNaOpgesteldAssurancerapportEnPermissie()
			throws BronException
	{
		tester.voerTestUitMetMedewerker(new BronEditNaMutatieBeperking());

		getDeelnemer1016();
		verbintenis.setBekostigd(Nee);
		verbintenis.setBegindatum(huidigSchooljaar.getBegindatum());
		verbintenis.setEinddatum(huidigSchooljaar.getEinddatum());
		deelname1.setDatumUitslag(eenSeptember);

		zetSchooljaarStatus(huidigSchooljaar, AssurancerapportOpgesteld);
		wijzigStatus(Definitief, Voorlopig);

		assertThat(mutatieCheck(), is(SchooljaarIsAfgeslotenVoorMutaties));

		controller.save();
	}

	@Test
	public void verwijderingNietBekostigdeVerbintenisMagMetNietBekostigdeExamendeelnamesTijdensVrijeInvoer()
			throws BronException
	{
		tester.voerTestUitMetMedewerker();

		getDeelnemer1016();
		verbintenis.setBekostigd(Nee);
		verbintenis.setBegindatum(huidigSchooljaar.getBegindatum());
		verbintenis.setEinddatum(huidigSchooljaar.getEinddatum());
		deelname1.setBekostigd(false);
		deelname1.setDatumUitslag(eenSeptember);

		zetSchooljaarStatus(huidigSchooljaar, GegevensWordenIngevoerd);
		wijzigStatus(Definitief, Voorlopig);

		assertThat(mutatieCheck(), is(AlleMutatiesZijnNogToegestaan));

		controller.save();

		BronAanleverMelding melding = getEersteMelding();
		assertThat(melding.isBekostigingsRelevant(), is(false));
	}

	@Test
	public void verwijderingNietBekostigdeVerbintenisMagMetNietBekostigdeExamendeelnamesTijdensMutatiebeperking()
			throws BronException
	{
		tester.voerTestUitMetMedewerker();

		getDeelnemer1016();
		verbintenis.setBekostigd(Nee);
		verbintenis.setBegindatum(huidigSchooljaar.getBegindatum());
		verbintenis.setEinddatum(huidigSchooljaar.getEinddatum());
		deelname1.setDatumUitslag(eenSeptember);
		deelname1.setBekostigd(false);

		zetSchooljaarStatus(huidigSchooljaar, MutatiebeperkingIngesteld);
		wijzigStatus(Definitief, Voorlopig);

		assertThat(mutatieCheck(), is(NietBekostigdOpRecentePeildatum));

		controller.save();

		BronAanleverMelding melding = getEersteMelding();
		assertThat(melding.isBekostigingsRelevant(), is(false));
	}

	@Test
	public void verwijderingNietBekostigdeVerbintenisMagMetNietBekostigdeExamendeelnamesTijdensMutatiebeperkingEnPermissie()
			throws BronException
	{
		tester.voerTestUitMetMedewerker(new BronEditNaMutatieBeperking());

		getDeelnemer1016();
		verbintenis.setBekostigd(Nee);
		verbintenis.setBegindatum(huidigSchooljaar.getBegindatum());
		verbintenis.setEinddatum(huidigSchooljaar.getEinddatum());
		deelname1.setDatumUitslag(eenSeptember);
		deelname1.setBekostigd(false);

		zetSchooljaarStatus(huidigSchooljaar, MutatiebeperkingIngesteld);
		wijzigStatus(Definitief, Voorlopig);

		assertThat(mutatieCheck(), is(NietBekostigdOpRecentePeildatum));

		controller.save();

		BronAanleverMelding melding = getEersteMelding();
		assertThat(melding.isBekostigingsRelevant(), is(false));
	}

	@Test
	public void verwijderingNietBekostigdeVerbintenisMagMetNietBekostigdeExamendeelnamesTijdensMutatiestop()
			throws BronException
	{
		tester.voerTestUitMetMedewerker();

		getDeelnemer1016();
		verbintenis.setBekostigd(Nee);
		verbintenis.setBegindatum(huidigSchooljaar.getBegindatum());
		verbintenis.setEinddatum(huidigSchooljaar.getEinddatum());
		deelname1.setBekostigd(false);
		deelname1.setDatumUitslag(eenSeptember);

		zetSchooljaarStatus(huidigSchooljaar, MutatiestopIngesteld);
		wijzigStatus(Definitief, Voorlopig);

		assertThat(mutatieCheck(), is(NietBekostigdOpRecentePeildatum));

		controller.save();

		BronAanleverMelding melding = getEersteMelding();
		assertThat(melding.isBekostigingsRelevant(), is(false));
	}

	@Test
	public void verwijderingNietBekostigdeVerbintenisMagWelMetNietBekostigdeExamendeelnamesTijdensMutatiestopEnPermissie()
			throws BronException
	{
		tester.voerTestUitMetMedewerker(new BronEditNaMutatieBeperking());

		getDeelnemer1016();
		verbintenis.setBekostigd(Nee);
		verbintenis.setBegindatum(huidigSchooljaar.getBegindatum());
		verbintenis.setEinddatum(huidigSchooljaar.getEinddatum());
		deelname1.setBekostigd(false);
		deelname1.setDatumUitslag(eenSeptember);

		zetSchooljaarStatus(huidigSchooljaar, MutatiestopIngesteld);
		wijzigStatus(Definitief, Voorlopig);

		assertThat(mutatieCheck(), is(NietBekostigdOpRecentePeildatum));

		controller.save();

		BronAanleverMelding melding = getEersteMelding();
		assertThat(melding.isBekostigingsRelevant(), is(false));
	}

	@Test
	public void verwijderingNietBekostigdeVerbintenisMagMetNietBekostigdeExamendeelnamesNaOpgesteldAssurancerapport()
			throws BronException
	{
		tester.voerTestUitMetMedewerker();

		getDeelnemer1016();
		verbintenis.setBekostigd(Nee);
		verbintenis.setBegindatum(huidigSchooljaar.getBegindatum());
		verbintenis.setEinddatum(huidigSchooljaar.getEinddatum());
		deelname1.setBekostigd(false);
		deelname1.setDatumUitslag(eenSeptember);

		zetSchooljaarStatus(huidigSchooljaar, AssurancerapportOpgesteld);
		wijzigStatus(Definitief, Voorlopig);

		assertThat(mutatieCheck(), is(NietBekostigdOpRecentePeildatum));

		controller.save();

		BronAanleverMelding melding = getEersteMelding();
		assertThat(melding.isBekostigingsRelevant(), is(false));
	}

	@Test
	public void verwijderingNietBekostigdeVerbintenisMagMetNietBekostigdeExamendeelnamesNaOpgesteldAssurancerapportEnPermissie()
			throws BronException
	{
		tester.voerTestUitMetMedewerker(new BronEditNaMutatieBeperking());

		getDeelnemer1016();
		verbintenis.setBekostigd(Nee);
		verbintenis.setBegindatum(huidigSchooljaar.getBegindatum());
		verbintenis.setEinddatum(huidigSchooljaar.getEinddatum());
		deelname1.setBekostigd(false);
		deelname1.setDatumUitslag(eenSeptember);

		zetSchooljaarStatus(huidigSchooljaar, AssurancerapportOpgesteld);
		wijzigStatus(Definitief, Voorlopig);

		assertThat(mutatieCheck(), is(NietBekostigdOpRecentePeildatum));

		controller.save();

		BronAanleverMelding melding = getEersteMelding();
		assertThat(melding.isBekostigingsRelevant(), is(false));
	}

	/**
	 * Zonder permissie tegelijkertijd de intensiteit en de bekostiging wijzigen van een
	 * verbintenis die begint op 1 februari van het lopende schooljaar tijdens een
	 * mutatiestop. Dit is een accountantsmutatie indien de verbintenis bekostigd was.
	 */
	@Test(expected = BronException.class)
	public void mantis58602_magNietZonderPermissie() throws Exception
	{
		tester.voerTestUitMetMedewerker();

		createGevalMantis58602();

		assertThat(mutatieCheck(),
			is(MedewerkerHeeftGeenToestemmingOmWijzigingDoorTeVoerenBijEenMutatiebeperkingOfStop));

		controller.save();
	}

	private void createGevalMantis58602()
	{
		getDeelnemer1001();

		verbintenis.setBegindatum(huidigSchooljaar.getEenFebruari());

		periode1 = new Bekostigingsperiode();
		periode1.setVerbintenis(verbintenis);
		periode1.setBegindatum(huidigSchooljaar.getBegindatum());
		periode1.setEinddatum(huidigSchooljaar.getEinddatum());
		periode1.setBekostigd(true);
		verbintenis.getBekostigingsperiodes().add(periode1);

		periode2 = new Bekostigingsperiode();
		periode2.setVerbintenis(verbintenis);
		periode2.setBegindatum(huidigSchooljaar.getVolgendSchooljaar().getBegindatum());
		periode2.setBekostigd(true);
		verbintenis.getBekostigingsperiodes().add(periode2);

		zetSchooljaarStatus(MutatiestopIngesteld);

		wijzigIntensiteit(Deeltijd, Examendeelnemer);
		wijzigBekostigd(Gedeeltelijk, Bekostigd.Nee);
	}

	@Test
	public void mantis58602_magWelMetPermissie() throws Exception
	{
		tester.voerTestUitMetMedewerker(new BronEditNaMutatieBeperking());

		createGevalMantis58602();

		controller.save();

		BronAanleverMelding melding = getEersteMelding();

		assertThat(melding.isBekostigingsRelevant(), is(true));
	}

	/**
	 * In versie 1.05.3 heb ik nog een mutatie doorgevoerd die toch ook weer ten onrechte
	 * niet als accountantsmutatie wordt meegenomen. Dat betreft een deelnemer die is
	 * ingeschreven per 01-02-2010 en waarvan ik nu de intensiteit heb aangepast van
	 * deeltijd in examendeelnemer. Deze zou als accountantsmutatie moeten worden
	 * klaargezet maar wordt ten onrechte als reguliere mutatie klaargezet.
	 */
	@Test
	public void mantis59680() throws Exception
	{
		tester.voerTestUitMetMedewerker(new BronEditNaMutatieBeperking());
		getDeelnemer1001();

		verbintenis.setBegindatum(huidigSchooljaar.getEenFebruari());
		verbintenis.setStatus(Definitief);
		verbintenis.setIntensiteit(Deeltijd);

		zetSchooljaarStatus(huidigSchooljaar, MutatiestopIngesteld);
		wijzigIntensiteit(Deeltijd, Examendeelnemer);

		assertThat(mutatieCheck(),
			is(MedewerkerHeeftToestemmingOmWijzigingDoorTeVoerenBijEenMutatiestop));

		controller.save();

		BronAanleverMelding melding = getEersteMelding();

		assertThat(melding.isBekostigingsRelevant(), is(true));
	}

	/**
	 * Een deelnemer had een inschrijving van 04-09-2009 tot 07-09-2009. Is destijds al
	 * helemaal uitgewisseld naar BRON en goedgekeurd. Nu blijkt achteraf dat de
	 * inschrijfdatum 01-09-2009 had moeten zijn. Dat heb ik aangepast, alleen wordt deze
	 * aanpassing ten onrechte als accountantsmutatie opgenomen. Dat klopt niet, want
	 * uitschrijfdatum blijft 07-09-2009 en het betreft dus geen bekostigingsrelevante
	 * gegevens. Deze moet gewoon als reguliere mutatie naar BRON worden gestuurd. In het
	 * verleden heb ik dit soort mutaties vaker regulier naar BRON gestuurd en dat levert
	 * in BRON in ieder geval geen probleem op. Betreft deelnemer 74907.
	 */
	@Test
	public void mantis58672() throws Exception
	{
		tester.voerTestUitMetMedewerker(new BronEditNaMutatieBeperking());
		getDeelnemer1001();

		TimeUtil util = TimeUtil.getInstance();

		int jaar = util.getYear(huidigSchooljaar.getBegindatum());
		Date begindatum = util.asDate(jaar, Calendar.SEPTEMBER, 4);

		Date einddatum = util.asDate(jaar, Calendar.SEPTEMBER, 7);

		verbintenis.setBegindatum(begindatum);
		verbintenis.setEinddatum(einddatum);
		verbintenis.setStatus(Beeindigd);

		zetSchooljaarStatus(huidigSchooljaar, MutatiestopIngesteld);
		wijzigBegindatum(begindatum, util.addDays(begindatum, -3));

		assertThat(mutatieCheck(), is(GeenMutatiebeperkingOfStopGevondenVoorDezeMutatie));

		controller.save();

		BronAanleverMelding melding = getEersteMelding();

		assertThat(melding.isBekostigingsRelevant(), is(false));
	}

	/**
	 * Heb bij deelnemer 99961 de uitschrijfdatum 01-09-2009 aangepast in 25-09-2009, dit
	 * wordt ten onrechte als accountantsmutatie klaargezet. Aanpassing raakt immers geen
	 * teldatum.
	 */
	@Test
	public void mantis58672Deel2() throws Exception
	{
		tester.voerTestUitMetMedewerker(new BronEditNaMutatieBeperking());
		getDeelnemer1001();

		TimeUtil util = TimeUtil.getInstance();

		int jaar = util.getYear(huidigSchooljaar.getBegindatum());
		Date begindatum = huidigSchooljaar.getBegindatum();

		Date einddatum = util.asDate(jaar, Calendar.SEPTEMBER, 1);

		verbintenis.setBegindatum(begindatum);
		verbintenis.setEinddatum(einddatum);
		verbintenis.setStatus(Beeindigd);

		zetSchooljaarStatus(huidigSchooljaar, MutatiestopIngesteld);
		wijzigEinddatum(einddatum, util.addDays(einddatum, 24));

		assertThat(mutatieCheck(), is(GeenMutatiebeperkingOfStopGevondenVoorDezeMutatie));

		controller.save();

		BronAanleverMelding melding = getEersteMelding();

		assertThat(melding.isBekostigingsRelevant(), is(false));
	}

	/**
	 * Ik heb bij deelnemer 98673 een beëindiging per 29-01-2010 ongedaan gemaakt, dat
	 * moet een accountantsmutatie opleveren maar dat levert een 'normale' BRON-mutatie
	 * op. Die heb ik nu handmatig als accountantsmutatie aangevinkt, maar moet
	 * structureel worden opgelost. Hierdoor wordt immers alsnog de inschrijving op
	 * peildatum 01-02-2010 weer actief en is dus een correctie op de telling.
	 */
	@Test
	public void mantis60230() throws Exception
	{
		tester.voerTestUitMetMedewerker(new BronEditNaMutatieBeperking());
		getDeelnemer1001();

		TimeUtil util = TimeUtil.getInstance();

		int jaar = util.getYear(huidigSchooljaar.getBegindatum());

		Date begindatum = huidigSchooljaar.getBegindatum();
		Date einddatum = util.asDate(jaar + 1, Calendar.JANUARY, 29);

		verbintenis.setBegindatum(begindatum);
		verbintenis.setEinddatum(einddatum);
		verbintenis.setStatus(Beeindigd);

		zetSchooljaarStatus(huidigSchooljaar, MutatiestopIngesteld);
		wijzigEinddatum(einddatum, null);
		wijzigStatus(Beeindigd, Definitief);

		assertThat(mutatieCheck(),
			is(MedewerkerHeeftToestemmingOmWijzigingDoorTeVoerenBijEenMutatiestop));

		controller.save();

		BronAanleverMelding melding = getEersteMelding();

		assertThat(melding.isBekostigingsRelevant(), is(true));
	}

	private WijzigingToegestaanResultaat mutatieCheck()
	{
		vulOudeKenmerken();

		BronVerbintenisWijzigingToegestaanCheck check =
			new BronVerbintenisWijzigingToegestaanCheck(oudeBegindatum, oudeEinddatum, oudeStatus,
				oudeOpleiding, oudeIntensiteit, oudeBekostigd, verbintenis);
		return check.getResultaat();
	}

	private void vulOudeKenmerken()
	{
		if (!gewijzigdeKenmerken.contains("begindatum"))
			oudeBegindatum = verbintenis.getBegindatum();
		if (!gewijzigdeKenmerken.contains("einddatum"))
			oudeEinddatum = verbintenis.getEinddatum();
		if (!gewijzigdeKenmerken.contains("status"))
			oudeStatus = verbintenis.getStatus();
		if (!gewijzigdeKenmerken.contains("bekostigd"))
			oudeBekostigd = verbintenis.getBekostigd();
		if (!gewijzigdeKenmerken.contains("opleiding"))
			oudeOpleiding = verbintenis.getOpleiding();
		if (!gewijzigdeKenmerken.contains("intensiteit"))
			oudeIntensiteit = verbintenis.getIntensiteit();
	}

	private WijzigingToegestaanResultaat mutatieCheck(Bekostigingsperiode periode, boolean oud,
			boolean nieuw) throws BronException
	{
		vulOudeKenmerken();
		addChange(periode, "bekostigd", oud, nieuw);

		periode.setBekostigd(nieuw);

		Bekostigingsperiode oudePeriode = new Bekostigingsperiode();
		oudePeriode.setBekostigd(oud);
		oudePeriode.setBegindatum(periode.getBegindatum());
		oudePeriode.setEinddatum(periode.getEinddatum());

		BronVerbintenisWijzigingToegestaanCheck check =
			new BronVerbintenisWijzigingToegestaanCheck(oudeBekostigd, oudeIntensiteit,
				oudePeriode, periode);
		return check.getResultaat();
	}

	private void wijzigBegindatum(Date oudeWaarde, Date nieuweWaarde)
	{
		try
		{
			this.oudeBegindatum = oudeWaarde;
			verbintenis.setBegindatum(nieuweWaarde);

			gewijzigdeKenmerken.add("begindatum");
			addChange(verbintenis, "begindatum", oudeWaarde, nieuweWaarde);
		}
		catch (BronException e)
		{
			fail(e.getMessage());
		}
	}

	private void wijzigEinddatum(Date oudeWaarde, Date nieuweWaarde)
	{
		try
		{
			this.oudeEinddatum = oudeWaarde;
			verbintenis.setEinddatum(nieuweWaarde);

			gewijzigdeKenmerken.add("einddatum");
			addChange(verbintenis, "einddatum", oudeWaarde, nieuweWaarde);
		}
		catch (BronException e)
		{
			fail(e.getMessage());
		}
	}

	private void wijzigStatus(VerbintenisStatus oudeWaarde, VerbintenisStatus nieuweWaarde)
	{
		try
		{
			this.oudeStatus = oudeWaarde;
			verbintenis.setStatus(nieuweWaarde);

			gewijzigdeKenmerken.add("status");
			addChange(verbintenis, "status", oudeWaarde, nieuweWaarde);
		}
		catch (BronException e)
		{
			fail(e.getMessage());
		}
	}

	private void wijzigIntensiteit(Intensiteit oudeWaarde, Intensiteit nieuweWaarde)
	{
		try
		{
			this.oudeIntensiteit = oudeWaarde;
			verbintenis.setIntensiteit(nieuweWaarde);

			gewijzigdeKenmerken.add("intensiteit");
			addChange(verbintenis, "intensiteit", oudeWaarde, nieuweWaarde);
		}
		catch (BronException e)
		{
			fail(e.getMessage());
		}
	}

	private void wijzigBekostigd(Bekostigd oudeWaarde, Bekostigd nieuweWaarde)
	{
		try
		{
			this.oudeBekostigd = oudeWaarde;
			verbintenis.setBekostigd(nieuweWaarde);

			gewijzigdeKenmerken.add("bekostigd");
			addChange(verbintenis, "bekostigd", oudeWaarde, nieuweWaarde);
		}
		catch (BronException e)
		{
			fail(e.getMessage());
		}
	}

	private void wijzigOpleiding(Opleiding oudeWaarde, Opleiding nieuweWaarde)
	{
		try
		{
			this.oudeOpleiding = oudeWaarde;
			verbintenis.setOpleiding(nieuweWaarde);

			gewijzigdeKenmerken.add("opleiding");
			addChange(verbintenis, "opleiding", oudeWaarde, nieuweWaarde);
		}
		catch (BronException e)
		{
			fail(e.getMessage());
		}
	}
}
