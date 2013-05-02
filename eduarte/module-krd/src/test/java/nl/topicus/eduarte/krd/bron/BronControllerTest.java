package nl.topicus.eduarte.krd.bron;

import static org.hamcrest.core.Is.*;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import nl.topicus.cobra.entities.IdObject;
import nl.topicus.cobra.reflection.Property;
import nl.topicus.cobra.reflection.ReflectionUtil;
import nl.topicus.eduarte.entities.landelijk.Nationaliteit;
import nl.topicus.eduarte.entities.personen.Deelnemer;
import nl.topicus.eduarte.entities.personen.Persoon;
import nl.topicus.eduarte.tester.EduArteTestCase;

import org.junit.Before;
import org.junit.Test;

public class BronControllerTest extends EduArteTestCase
{
	private BronController controller;

	@Before
	public void setup()
	{
		controller = new BronController();
	}

	@Test
	public void geenChangesInZelfdeObject() throws Exception
	{
		Persoon persoon = new Persoon();
		persoon.setAchternaam("Vries");
		persoon.setBankrekeningnummer("123456789");
		persoon.setDatumInNederland(new Date());
		persoon.setVoorletters("V.E.");
		persoon.setVoornamen("Victor Eduart");
		persoon.setRoepnaam("Victor");
		persoon.setNationaliteit1(new Nationaliteit());

		State state = convertEntityToState(persoon);

		controller.controleerOpWijzigingenOpBronVelden(persoon, state.values, state.values,
			state.propertyNames);

		assertTrue(controller.getRecordedChanges().getGewijzigdeDeelnemers().isEmpty());
	}

	@Test
	public void ontdektBankrekeningnummerWijzigingNiet() throws Exception
	{
		Persoon persoon = new Persoon();
		persoon.setBankrekeningnummer("123456789");

		State origineel = convertEntityToState(persoon);

		persoon.setBankrekeningnummer("987654321");
		State nieuw = convertEntityToState(persoon);

		controller.controleerOpWijzigingenOpBronVelden(persoon, nieuw.values, origineel.values,
			origineel.propertyNames);

		assertTrue(controller.getRecordedChanges().getGewijzigdeDeelnemers().isEmpty());
	}

	@Test
	public void ontdektEenWijzigingBijAttribuutWijzigingen() throws Exception
	{
		Deelnemer deelnemer = createDeelnemerMetPersoon();
		Persoon persoon = deelnemer.getPersoon();

		persoon.setId(1234L);
		persoon.setBankrekeningnummer("123456789");
		persoon.setBsn(123456789L);
		persoon.setOfficieleAchternaam("Jansen");

		State origineel = convertEntityToState(persoon);

		persoon.setBankrekeningnummer("987654321");
		persoon.setBsn(987654321L);
		persoon.setOfficieleAchternaam("Vries");
		State nieuw = convertEntityToState(persoon);

		controller.controleerOpWijzigingenOpBronVelden(persoon, nieuw.values, origineel.values,
			origineel.propertyNames);

		assertThat(controller.getRecordedChanges().getGewijzigdeDeelnemers().size(), is(1));

		List<BronStateChange> listOfChangesToPersoon =
			controller.getRecordedChanges().getWijzigingen(deelnemer).getChanges();

		assertThat(listOfChangesToPersoon.size(), is(2));

		printChanges(listOfChangesToPersoon);

		BronStateChange change1 = listOfChangesToPersoon.get(0);
		assertEquals("officieleAchternaam", change1.getPropertyName());
		assertEquals("Jansen", change1.getPreviousValue());
		assertEquals("Vries", change1.getCurrentValue());

		BronStateChange change2 = listOfChangesToPersoon.get(1);
		assertEquals("bsn", change2.getPropertyName());
		assertEquals(123456789L, change2.getPreviousValue());
		assertEquals(987654321L, change2.getCurrentValue());
	}

	private void printChanges(List<BronStateChange> changes)
	{
		IdObject entiteit = null;
		for (BronStateChange change : changes)
		{
			if (change.getEntity() != entiteit)
			{
				entiteit = change.getEntity();
				System.out.println(entiteit.getClass().getSimpleName() + " "
					+ entiteit.getIdAsSerializable());
			}
			System.out.printf("    %s: %s -> %s\n", change.getPropertyName(), change
				.getPreviousValue(), change.getCurrentValue());
		}
	}

	private Deelnemer createDeelnemerMetPersoon()
	{
		Deelnemer deelnemer = new Deelnemer();
		Persoon persoon = new Persoon();
		deelnemer.setPersoon(persoon);
		persoon.setDeelnemer(deelnemer);
		return deelnemer;
	}

	private class State
	{
		Object[] values;

		String[] propertyNames;

	}

	@SuppressWarnings("unchecked")
	private State convertEntityToState(Object entity)
	{
		State state = new State();

		List<Property<Object, ? , ? >> properties =
			ReflectionUtil.findProperties((Class<Object>) entity.getClass());

		List<Object> values = new ArrayList<Object>();
		List<String> propertyNames = new ArrayList<String>();

		for (Property<Object, ? , ? > property : properties)
		{
			if (property.getField() == null)
				continue;
			propertyNames.add(property.getName());
			Object value = property.getValue(entity);
			values.add(value);
		}
		state.propertyNames = propertyNames.toArray(new String[] {});
		state.values = values.toArray();

		return state;
	}
}
