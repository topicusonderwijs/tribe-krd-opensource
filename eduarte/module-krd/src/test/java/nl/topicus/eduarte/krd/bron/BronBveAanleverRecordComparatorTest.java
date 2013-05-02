package nl.topicus.eduarte.krd.bron;

import static nl.topicus.eduarte.krd.entities.bron.meldingen.aanlevering.BronBveAanleverRecord.*;
import static org.junit.Assert.*;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collections;

import nl.topicus.eduarte.entities.Entiteit;
import nl.topicus.eduarte.entities.examen.Examendeelname;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.entities.onderwijsproduct.OnderwijsproductAfnameContext;
import nl.topicus.eduarte.entities.personen.Deelnemer;
import nl.topicus.eduarte.krd.entities.bron.meldingen.aanlevering.BronAanleverMelding;
import nl.topicus.onderwijs.duo.bron.bve.batches.pve_9_9.aanlevering.ed.InschrijvingsgegevensRecord;
import nl.topicus.onderwijs.duo.bron.bve.batches.pve_9_9.aanlevering.ed.NT2Vaardigheden;
import nl.topicus.onderwijs.duo.bron.bve.batches.pve_9_9.aanlevering.ed.ResultaatgegevensRecord;
import nl.topicus.onderwijs.duo.bron.bve.batches.pve_9_9.aanlevering.ed.VakgegevensRecord;
import nl.topicus.onderwijs.duo.bron.bve.waardelijsten.NT2Vaardigheid;

import org.junit.Before;
import org.junit.Test;

public class BronBveAanleverRecordComparatorTest
{
	private Deelnemer deelnemer;

	private Verbintenis verbintenis;

	private Examendeelname examen;

	private OnderwijsproductAfnameContext context;

	@Before
	public void setup()
	{
		deelnemer = new Deelnemer();
		verbintenis = new Verbintenis(deelnemer);
		context = new OnderwijsproductAfnameContext();
		context.setVerbintenis(verbintenis);
		examen = new Examendeelname(verbintenis);
	}

	@Test
	public void test327328327() throws Exception
	{
		BronAanleverMelding melding = new BronAanleverMelding();
		VakgegevensRecord record1 = newEdVakgegevensRecord(melding, verbintenis, context);
		record1.setVakvolgnummer(1);
		setId(record1, 1);

		NT2Vaardigheden record2 = newEdNt2Vaardigheden(melding, verbintenis);
		record2.setVakvolgnummer(1);
		record2.setNT2Vaardigheid(NT2Vaardigheid.Luisteren);
		setId(record2, 2);

		VakgegevensRecord record3 = newEdVakgegevensRecord(melding, verbintenis, context);
		record3.setVakvolgnummer(2);
		setId(record3, 3);

		Collections.sort(melding.getMeldingen(), new BronBveAanleverRecordComparator());

		assertEquals(Arrays.asList(record1, record2, record3), melding.getMeldingen());
	}

	@Test
	public void test327327328() throws Exception
	{
		BronAanleverMelding melding = new BronAanleverMelding();
		VakgegevensRecord record1 = newEdVakgegevensRecord(melding, verbintenis, context);
		record1.setVakvolgnummer(2);
		setId(record1, 1);

		NT2Vaardigheden record2 = newEdNt2Vaardigheden(melding, verbintenis);
		record2.setVakvolgnummer(1);
		record2.setNT2Vaardigheid(NT2Vaardigheid.Luisteren);
		setId(record2, 2);

		VakgegevensRecord record3 = newEdVakgegevensRecord(melding, verbintenis, context);
		record3.setVakvolgnummer(1);
		setId(record3, 3);

		Collections.sort(melding.getMeldingen(), new BronBveAanleverRecordComparator());

		assertEquals(Arrays.asList(record3, record2, record1), melding.getMeldingen());
	}

	/**
	 * Controleert of de records binnen een melding juist gesorteerd worden.
	 */
	@Test
	public void test325_326_327_328_327() throws Exception
	{
		BronAanleverMelding melding = new BronAanleverMelding();

		VakgegevensRecord record1 = newEdVakgegevensRecord(melding, verbintenis, context);
		record1.setVakvolgnummer(1);
		setId(record1, 1);

		NT2Vaardigheden record2 = newEdNt2Vaardigheden(melding, verbintenis);
		record2.setVakvolgnummer(1);
		record2.setNT2Vaardigheid(NT2Vaardigheid.Luisteren);
		setId(record2, 2);

		VakgegevensRecord record3 = newEdVakgegevensRecord(melding, verbintenis, context);
		record3.setVakvolgnummer(2);
		setId(record3, 3);

		ResultaatgegevensRecord record4 =
			newEdResultaatgegevensRecord(melding, verbintenis, examen);
		setId(record4, 4);

		InschrijvingsgegevensRecord record5 =
			newEdInschrijvingsgegevensRecord(melding, verbintenis);
		setId(record5, 5);

		Collections.sort(melding.getMeldingen(), new BronBveAanleverRecordComparator());

		assertEquals(Arrays.asList(record5, record4, record1, record2, record3), melding
			.getMeldingen());
	}

	private void setId(Object object, int nr) throws Exception
	{
		Field field = Entiteit.class.getDeclaredField("id");
		field.setAccessible(true);
		field.set(object, Long.valueOf(nr));
	}
}
