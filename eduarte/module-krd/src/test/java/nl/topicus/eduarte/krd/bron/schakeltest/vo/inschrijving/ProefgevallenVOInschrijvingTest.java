package nl.topicus.eduarte.krd.bron.schakeltest.vo.inschrijving;

import static nl.topicus.eduarte.entities.inschrijving.Verbintenis.VerbintenisStatus.*;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.util.Collections;
import java.util.List;

import nl.topicus.eduarte.tester.hibernate.DatabaseAction;

import org.junit.Before;
import org.junit.Test;

public class ProefgevallenVOInschrijvingTest extends ProefgevallenVOInschrijving
{
	@Before
	public void setup()
	{
		getDeelnemer1001();
		verbintenis.setStatus(Voorlopig);
	}

	@Test
	public void voorlopigeVerbintenisLevertGeenMeldingenOpBijWijzigingBegindatumVerbintenis()
			throws Exception
	{
		addChange(verbintenis, "begindatum", null, verbintenis.getBegindatum());
		controller.save();

		List<DatabaseAction> empty = Collections.emptyList();
		assertThat(tester.getTransactionLog(), is(empty));
	}

	@Test
	public void voorlopigeVerbintenisLevertGeenMeldingenOpBijWijzigingBegindatumPlaatsing()
			throws Exception
	{
		addChange(plaatsing, "begindatum", null, plaatsing.getBegindatum());
		controller.save();

		List<DatabaseAction> empty = Collections.emptyList();
		assertThat(tester.getTransactionLog(), is(empty));
	}

	@Test
	public void voorlopigeVerbintenisLevertGeenMeldingenOpBijWijzigingBsn() throws Exception
	{
		deelnemer.setOnderwijsnummer(2100000004L);
		deelnemer.getPersoon().setBsn(2100000120L);

		addChange(deelnemer.getPersoon(), "bsn", null, deelnemer.getPersoon().getBsn());

		controller.save();

		List<DatabaseAction> empty = Collections.emptyList();
		assertThat(tester.getTransactionLog(), is(empty));
	}

	@Test
	public void voorlopigeVerbintenisLevertGeenMeldingenOpBijWijzigingGeboortedatum()
			throws Exception
	{
		deelnemer.setOnderwijsnummer(2100000004L);
		deelnemer.getPersoon().setBsn(null);

		addChange(deelnemer.getPersoon(), "geboortedatum", null, deelnemer.getPersoon()
			.getGeboortedatum());

		controller.save();

		List<DatabaseAction> empty = Collections.emptyList();
		assertThat(tester.getTransactionLog(), is(empty));
	}
}
