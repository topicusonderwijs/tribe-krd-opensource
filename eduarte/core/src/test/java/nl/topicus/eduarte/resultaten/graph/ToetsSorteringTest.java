package nl.topicus.eduarte.resultaten.graph;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import nl.topicus.eduarte.entities.resultaatstructuur.Resultaatstructuur;
import nl.topicus.eduarte.entities.resultaatstructuur.Toets;
import nl.topicus.eduarte.entities.resultaatstructuur.Resultaatstructuur.Type;
import nl.topicus.eduarte.entities.resultaatstructuur.Toets.ToetsCodePathMode;
import nl.topicus.eduarte.web.components.resultaat.ToetsDirectedWeightedGraph;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ToetsSorteringTest
{
	private List<Resultaatstructuur> toetsen;

	private static String eindcode = "EIND";

	private static String toets1code = "toets1";

	private static String toets1acode = "toets1a";

	private static String toets1bcode = "toets1b";

	private static String toets2code = "toets2";

	private static String toets2acode = "toets2a";

	private static String toets2bcode = "toets2b";

	@Before
	public void setupToetsen()
	{
		toetsen = new ArrayList<Resultaatstructuur>();

		/**
		 * Structuur moet worden: - struct 1 - eind 1 - toets1 1 - toets1a 1 - toets1b 2 -
		 * toets2 2 - toets2a 1 - toets2b 2
		 * 
		 *- struct 2 - eind 1 - toets1 1 - toets1a 2 - toets1b 1 - toets2 2 - toets2a 2 -
		 * toets2b 1
		 * 
		 *- struct 3 - eind 1 - toets1 2 - toets1a 1 - toets1b 2 - toets2 1 - toets2a 1 -
		 * toets2b 2
		 * 
		 * Waardoor het eind resultaat dus net zo moet worden als struct 1.
		 * 
		 */

		addStruct1();
		addStruct2();
		addStruct3();
	}

	private void addStruct1()
	{
		Resultaatstructuur struct = new Resultaatstructuur();
		struct.setNaam("Summatief");
		struct.setCode("SUM");
		struct.setType(Type.SUMMATIEF);
		toetsen.add(struct);

		Toets eind = new Toets(struct);
		eind.setCode(eindcode);
		eind.setVolgnummer(1);
		struct.setEindresultaat(eind);

		Toets toets1 = new Toets(eind);
		eind.getChildren().add(toets1);
		toets1.setCode(toets1code);
		toets1.setVolgnummer(1);

		Toets toets1a = new Toets(toets1);
		toets1.getChildren().add(toets1a);
		toets1a.setCode(toets1acode);
		toets1a.setVolgnummer(1);

		Toets toets1b = new Toets(toets1);
		toets1.getChildren().add(toets1b);
		toets1b.setCode(toets1bcode);
		toets1b.setVolgnummer(2);

		Toets toets2 = new Toets(eind);
		eind.getChildren().add(toets2);
		toets2.setCode(toets2code);
		toets2.setVolgnummer(1);

		Toets toets2a = new Toets(toets2);
		toets2.getChildren().add(toets2a);
		toets2a.setCode(toets2acode);
		toets2a.setVolgnummer(1);

		Toets toets2b = new Toets(toets2);
		toets2.getChildren().add(toets2b);
		toets2b.setCode(toets2bcode);
		toets2b.setVolgnummer(2);
	}

	private void addStruct2()
	{
		Resultaatstructuur struct = new Resultaatstructuur();
		struct.setNaam("Summatief");
		struct.setCode("SUM");
		struct.setType(Type.SUMMATIEF);
		toetsen.add(struct);

		Toets eind = new Toets(struct);
		eind.setCode(eindcode);
		eind.setVolgnummer(1);
		struct.setEindresultaat(eind);

		Toets toets1 = new Toets(eind);
		eind.getChildren().add(toets1);
		toets1.setCode(toets1code);
		toets1.setVolgnummer(1);

		Toets toets1b = new Toets(toets1);
		toets1.getChildren().add(toets1b);
		toets1b.setCode(toets1bcode);
		toets1b.setVolgnummer(1);

		Toets toets1a = new Toets(toets1);
		toets1.getChildren().add(toets1a);
		toets1a.setCode(toets1acode);
		toets1a.setVolgnummer(2);

		Toets toets2 = new Toets(eind);
		eind.getChildren().add(toets2);
		toets2.setCode(toets2code);
		toets2.setVolgnummer(1);

		Toets toets2b = new Toets(toets2);
		toets2.getChildren().add(toets2b);
		toets2b.setCode(toets2bcode);
		toets2b.setVolgnummer(1);

		Toets toets2a = new Toets(toets2);
		toets2.getChildren().add(toets2a);
		toets2a.setCode(toets2acode);
		toets2a.setVolgnummer(2);
	}

	private void addStruct3()
	{
		Resultaatstructuur struct = new Resultaatstructuur();
		struct.setNaam("Summatief");
		struct.setCode("SUM");
		struct.setType(Type.SUMMATIEF);
		toetsen.add(struct);

		Toets eind = new Toets(struct);
		eind.setCode(eindcode);
		eind.setVolgnummer(1);
		struct.setEindresultaat(eind);

		Toets toets2 = new Toets(eind);
		eind.getChildren().add(toets2);
		toets2.setCode(toets2code);
		toets2.setVolgnummer(1);

		Toets toets2a = new Toets(toets2);
		toets2.getChildren().add(toets2a);
		toets2a.setCode(toets2acode);
		toets2a.setVolgnummer(1);

		Toets toets2b = new Toets(toets2);
		toets2.getChildren().add(toets2b);
		toets2b.setCode(toets2bcode);
		toets2b.setVolgnummer(2);

		Toets toets1 = new Toets(eind);
		eind.getChildren().add(toets1);
		toets1.setCode(toets1code);
		toets1.setVolgnummer(2);

		Toets toets1a = new Toets(toets1);
		toets1.getChildren().add(toets1a);
		toets1a.setCode(toets1acode);
		toets1a.setVolgnummer(1);

		Toets toets1b = new Toets(toets1);
		toets1.getChildren().add(toets1b);
		toets1b.setCode(toets1bcode);
		toets1b.setVolgnummer(2);
	}

	@After
	public void breakDownToetsen()
	{
		toetsen.clear();
		toetsen = null;
	}

	@Test
	public void testMatrixCodeVolgnummer()
	{
		List<String> sorted =
			ToetsDirectedWeightedGraph.sortToetsen(toetsen, ToetsCodePathMode.STANDAARD);
		Assert.assertEquals(Arrays
			.asList("EIND/toets1/toets1a/", "EIND/toets1/toets1b/", "EIND/toets1/",
				"EIND/toets2/toets2a/", "EIND/toets2/toets2b/", "EIND/toets2/", "EIND/"), sorted);
	}
}
