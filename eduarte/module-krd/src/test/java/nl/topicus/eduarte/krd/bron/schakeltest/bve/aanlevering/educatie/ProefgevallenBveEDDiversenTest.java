package nl.topicus.eduarte.krd.bron.schakeltest.bve.aanlevering.educatie;

import static java.util.Arrays.*;
import static nl.topicus.eduarte.entities.inschrijving.Verbintenis.VerbintenisStatus.*;
import static nl.topicus.eduarte.krd.entities.bron.BronStatus.*;
import static nl.topicus.eduarte.tester.hibernate.DatabaseAction.*;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import nl.topicus.cobra.util.TimeUtil;
import nl.topicus.eduarte.entities.landelijk.Schooljaar;
import nl.topicus.eduarte.krd.entities.bron.BronMeldingStatus;
import nl.topicus.eduarte.krd.entities.bron.meldingen.aanlevering.BronAanleverMelding;

import org.junit.Before;
import org.junit.Test;

public class ProefgevallenBveEDDiversenTest extends ProefgevallenBveED
{
	private Schooljaar huidigSchooljaar;

	@Before
	public void setup()
	{
		huidigSchooljaar = Schooljaar.huidigSchooljaar();
	}

	@Test
	public void mantis56804() throws Exception
	{
		getDeelnemer3001();
		verbintenis.setEinddatum(asDate(20100226));
		verbintenis.setStatus(Beeindigd);

		addChange(verbintenis, "status", Definitief, Beeindigd);
		addChange(verbintenis, "einddatum", null, verbintenis.getEinddatum());

		controller.save();

		BronAanleverMelding melding = getEersteMelding();
		assertThat(tester.getTransactionLog(), is(asList(insert(melding))));
		assertThat(melding.getBronMeldingStatus(), is(equalTo(BronMeldingStatus.WACHTRIJ)));

		assertThat(melding.getIngangsDatum(), is(TimeUtil.vandaag()));
	}

	/**
	 * Educatie meldingen zijn nooit bekostigingsrelevant, dus daarop testen.
	 */
	@Test
	public void mantis58540_1() throws Exception
	{
		getDeelnemer3001();

		verbintenis.setBegindatum(huidigSchooljaar.getBegindatum());
		verbintenis.setEinddatum(null);
		verbintenis.setStatus(Definitief);

		addChange(verbintenis, "status", Voorlopig, Definitief);

		zetSchooljaarStatus(GegevensWordenIngevoerd);

		controller.save();

		BronAanleverMelding melding = getEersteMelding();
		assertThat(melding.isBekostigingsRelevant(), is(false));
		assertThat(melding.getBronMeldingStatus(), is(equalTo(BronMeldingStatus.WACHTRIJ)));

		assertThat(melding.getIngangsDatum(), is(TimeUtil.vandaag()));
	}

	/**
	 * Educatie meldingen zijn nooit bekostigingsrelevant, dus daarop testen.
	 */
	@Test
	public void mantis58540_2() throws Exception
	{
		getDeelnemer3001();

		verbintenis.setBegindatum(huidigSchooljaar.getBegindatum());
		verbintenis.setEinddatum(null);
		verbintenis.setStatus(Definitief);

		addChange(verbintenis, "status", Voorlopig, Definitief);

		zetSchooljaarStatus(MutatiestopIngesteld);

		controller.save();

		BronAanleverMelding melding = getEersteMelding();
		assertThat(melding.isBekostigingsRelevant(), is(false));
		assertThat(melding.getBronMeldingStatus(), is(equalTo(BronMeldingStatus.WACHTRIJ)));

		assertThat(melding.getIngangsDatum(), is(TimeUtil.vandaag()));
	}
}
