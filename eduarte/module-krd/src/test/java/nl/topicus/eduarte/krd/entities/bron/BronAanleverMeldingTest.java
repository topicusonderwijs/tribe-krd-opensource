package nl.topicus.eduarte.krd.entities.bron;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import nl.topicus.cobra.types.personalia.Geslacht;
import nl.topicus.eduarte.entities.landelijk.Land;
import nl.topicus.eduarte.entities.personen.Deelnemer;
import nl.topicus.eduarte.entities.personen.Persoon.ToepassingGeboortedatum;
import nl.topicus.eduarte.krd.entities.bron.meldingen.aanlevering.BronAanleverMelding;
import nl.topicus.eduarte.tester.DeelnemerBuilder;
import nl.topicus.eduarte.tester.EduArteTestCase;
import nl.topicus.onderwijs.duo.bron.data.types.Datum;

import org.junit.Test;

public class BronAanleverMeldingTest extends EduArteTestCase
{
	@Test
	public void mantis63672()
	{
		DeelnemerBuilder db = new DeelnemerBuilder();
		db.setGeboortedatum("19910701");
		db.setBsn("225907720");
		db.setAdresPostcode("7777AM");
		db.setAdresLand(Land.getNederland());
		db.setGeslacht(Geslacht.Man);

		Deelnemer deelnemer = db.build();
		deelnemer.getPersoon().setToepassingGeboortedatum(
			ToepassingGeboortedatum.GeboortemaandEnJaar);
		BronAanleverMelding melding = new BronAanleverMelding();
		melding.setDeelnemer(deelnemer);
		melding.vul();

		assertThat(melding.getGeboortedatum(), is(Datum.valueOf(1991, 7)));
	}

	@Test
	public void redenKorterDan4000Karakters()
	{
		String string900 = generateString(900);

		BronAanleverMelding melding = new BronAanleverMelding();
		melding.addReden(string900);
		melding.addReden(string900);
		melding.addReden(string900);
		melding.addReden(string900);

		StringBuilder controle = new StringBuilder();
		controle.append(string900);
		controle.append('\n');
		controle.append(string900);
		controle.append('\n');
		controle.append(string900);
		controle.append('\n');
		controle.append(string900);

		assertThat(melding.getReden(), is(equalTo(controle.toString())));
	}

	@Test
	public void redenNietLangerDan4000Karakters()
	{
		String string1000 = generateString(1000);

		BronAanleverMelding melding = new BronAanleverMelding();
		melding.addReden(string1000);
		melding.addReden(string1000);
		melding.addReden(string1000);
		melding.addReden(string1000);

		StringBuilder controle = new StringBuilder();
		controle.append(string1000);
		controle.append('\n');
		controle.append(string1000);
		controle.append('\n');
		controle.append(string1000);

		assertThat(melding.getReden(), is(equalTo(controle.toString())));
	}

	@Test
	public void redenNietLangerDan4000KaraktersBijEersteReden()
	{
		String string4001 = generateString(4001);

		BronAanleverMelding melding = new BronAanleverMelding();
		melding.addReden(string4001);

		String controle = string4001.substring(0, 4000);

		assertThat(melding.getReden(), is(equalTo(controle)));
	}

	private String generateString(int n)
	{
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < n; i++)
		{
			sb.append(Character.forDigit(i % 10, 10));
		}
		String string1000 = sb.toString();
		return string1000;
	}
}
