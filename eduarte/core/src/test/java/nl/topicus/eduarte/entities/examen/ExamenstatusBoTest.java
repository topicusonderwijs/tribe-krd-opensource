package nl.topicus.eduarte.entities.examen;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class ExamenstatusBoTest
{
	private Examenstatus status1;

	private Examenstatus status2;

	private boolean nieuw;

	private boolean verwijderd;

	public ExamenstatusBoTest(Examenstatus status1, Examenstatus status2, boolean nieuw,
			boolean verwijderd)
	{
		this.status1 = status1;
		this.status2 = status2;
		this.nieuw = nieuw;
		this.verwijderd = verwijderd;
	}

	@Parameters
	public static Collection<Object[]> getParameters()
	{
		Examenstatus verwijderd = status("Verwijderd", false);
		Examenstatus afgewezen = status("Afgewezen", false);
		Examenstatus geslaagd = status("geslaagd", true);
		Examenstatus certificaten = status("Certificaten", true);

		ArrayList<Object[]> result = new ArrayList<Object[]>();
		result.add(testcase(null, null, false, false));

		result.add(testcase(null, verwijderd, false, false));
		result.add(testcase(null, afgewezen, false, false));
		result.add(testcase(null, geslaagd, true, false));
		result.add(testcase(null, certificaten, true, false));

		result.add(testcase(verwijderd, null, false, false));
		result.add(testcase(afgewezen, null, false, false));
		result.add(testcase(geslaagd, null, false, true));
		result.add(testcase(certificaten, null, false, true));

		result.add(testcase(verwijderd, verwijderd, false, false));
		result.add(testcase(verwijderd, afgewezen, false, false));
		result.add(testcase(verwijderd, geslaagd, true, false));
		result.add(testcase(verwijderd, certificaten, true, false));

		result.add(testcase(afgewezen, verwijderd, false, false));
		result.add(testcase(afgewezen, afgewezen, false, false));
		result.add(testcase(afgewezen, geslaagd, true, false));
		result.add(testcase(afgewezen, certificaten, true, false));

		result.add(testcase(geslaagd, verwijderd, false, true));
		result.add(testcase(geslaagd, afgewezen, false, true));
		result.add(testcase(geslaagd, geslaagd, false, false));
		result.add(testcase(geslaagd, certificaten, false, false));

		result.add(testcase(certificaten, verwijderd, false, true));
		result.add(testcase(certificaten, afgewezen, false, true));
		result.add(testcase(certificaten, geslaagd, false, false));
		result.add(testcase(certificaten, certificaten, false, false));

		return result;
	}

	private static Object[] testcase(Examenstatus oud, Examenstatus nieuw, boolean nieuwVoorBron,
			boolean verwijderdUitBron)
	{
		return new Object[] {oud, nieuw, nieuwVoorBron, verwijderdUitBron};
	}

	private static Examenstatus status(String naam, boolean geslaagd)
	{
		Examenstatus status = new Examenstatus();
		status.setNaam(naam);
		status.setGeslaagd(geslaagd);
		return status;
	}

	@Test
	public void assertsNieuwVoorBron()
	{
		assertThat(Examenstatus.isNieuwVoorBronBo(status1, status2), is(nieuw));
	}

	@Test
	public void assertsVerwijderdUitBron()
	{
		assertThat(Examenstatus.moetUitBronVerwijderdWordenBo(status1, status2), is(verwijderd));
	}
}
