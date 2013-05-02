package nl.topicus.eduarte.krd.bron.schakeltest.bve.aanlevering.vavo;

import static java.util.Arrays.*;
import static nl.topicus.eduarte.tester.hibernate.DatabaseAction.*;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import nl.topicus.cobra.util.TimeUtil;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis.VerbintenisStatus;
import nl.topicus.eduarte.krd.entities.bron.BronMeldingStatus;
import nl.topicus.eduarte.krd.entities.bron.meldingen.aanlevering.BronAanleverMelding;

import org.junit.Test;

public class ProefgevallenBveVAVODiversenTest extends ProefgevallenBveVAVO
{
	@Test
	public void mantis56804() throws Exception
	{
		getDeelnemer2001();
		verbintenis.setStatus(VerbintenisStatus.Beeindigd);
		verbintenis.setEinddatum(asDate(20100226));
		addChange(verbintenis, "status", VerbintenisStatus.Definitief, VerbintenisStatus.Beeindigd);
		addChange(verbintenis, "einddatum", null, verbintenis.getEinddatum());

		controller.save();

		BronAanleverMelding melding = getEersteMelding();
		assertThat(tester.getTransactionLog(), is(asList(insert(melding))));
		assertThat(melding.getBronMeldingStatus(), is(equalTo(BronMeldingStatus.WACHTRIJ)));

		assertThat(melding.getIngangsDatum(), is(TimeUtil.vandaag()));
	}
}
