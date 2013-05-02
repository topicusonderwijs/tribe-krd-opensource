package nl.topicus.eduarte.krd.bron.schakeltest;

import static nl.topicus.eduarte.entities.inschrijving.Verbintenis.Bekostigd.*;
import static nl.topicus.eduarte.entities.taxonomie.MBOLeerweg.*;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.util.Date;

import nl.topicus.cobra.util.TimeUtil;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.entities.personen.Deelnemer;
import nl.topicus.eduarte.krd.bron.BronEduArteModel;
import nl.topicus.eduarte.tester.EduArteTestCase;
import nl.topicus.eduarte.util.EduArteUtil;
import nl.topicus.onderwijs.duo.bron.data.types.Datum;

import org.junit.Before;
import org.junit.Test;

/**
 * Testcase voor de BronBuilder.
 */
public class BronBuilderTest extends EduArteTestCase
{
	private BronEduArteModel model;

	@Before
	public void setup()
	{
		model = new BronEduArteModel();
	}

	@Test
	public void proefgevalBveBOBatch1Deelnemer1001()
	{
		BronBuilder builder = new BronBuilder();

		builder.buildDeelnemer(1001, 21000004, null, 19910501, "9722TB", null);
		builder.addVerbintenisMBO(20080801, 10342, BBL, Ja);

		BronTestData data = builder.build();

		Deelnemer deelnemer = data.getDeelnemer();
		assertThat(deelnemer, notNullValue());
		assertThat(model.getLeerlingnummer(deelnemer), is(String.valueOf(1001)));
		assertThat(model.getSofinummer(deelnemer), is("0" + String.valueOf(21000004)));
		assertThat(model.getGeboortedatum(deelnemer), is(Datum.valueOf(19910501)));

		Verbintenis verbintenis = data.getVerbintenis();
		assertThat(verbintenis, notNullValue());
		assertThat(verbintenis.getBegindatum(), is(asDate(20080801)));
		assertThat(EduArteUtil.getDeelnemer(verbintenis), is(deelnemer));
		assertThat(model.getOpleidingscode(verbintenis), is(String.valueOf(10342)));
		assertThat(verbintenis.isBOVerbintenis(), is(true));
	}

	private Date asDate(int i)
	{
		return TimeUtil.getInstance().parseDateString(String.valueOf(i));
	}
}
