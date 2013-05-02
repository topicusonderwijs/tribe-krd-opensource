package nl.topicus.eduarte.krd.bron;

import static org.junit.Assert.*;
import nl.topicus.eduarte.entities.personen.Deelnemer;
import nl.topicus.eduarte.entities.personen.Persoon;
import nl.topicus.eduarte.tester.EduArteTestCase;

import org.junit.Before;
import org.junit.Test;

public class BronWatchListTest extends EduArteTestCase
{
	private BronWatchList watches;

	@Before
	public void setup()
	{
		watches = new BronWatchList();
	}

	@Test
	public void controleerDeelnemerProperties()
	{
		Deelnemer deelnemer = new Deelnemer();
		assertTrue(watches.isWatched(deelnemer));
		assertTrue(watches.isWatched(deelnemer, "onderwijsnummer"));
		assertFalse(watches.isWatched(deelnemer, "id"));
	}

	@Test
	public void controleerPersoonProperties()
	{
		Persoon persoon = new Persoon();
		assertTrue(watches.isWatched(persoon));
		assertTrue(watches.isWatched(persoon, "bsn"));
		assertTrue(watches.isWatched(persoon, "officieleAchternaam"));
		assertFalse(watches.isWatched(persoon, "id"));
	}

	@Test
	public void isSleutelgegeven()
	{
		assertTrue(BronWatchList.isSleutelWaarde(new Persoon(), "bsn"));
		assertTrue(BronWatchList.isSleutelWaarde(new Deelnemer(), "onderwijsnummer"));
		assertFalse(BronWatchList.isSleutelWaarde(new Deelnemer(), "indicatieGehandicapt"));
		assertFalse(BronWatchList.isSleutelWaarde(new Deelnemer(), "xxx"));
		assertFalse(BronWatchList.isSleutelWaarde(new Deelnemer(), ""));
		assertFalse(BronWatchList.isSleutelWaarde(new Deelnemer(), null));
	}
}
