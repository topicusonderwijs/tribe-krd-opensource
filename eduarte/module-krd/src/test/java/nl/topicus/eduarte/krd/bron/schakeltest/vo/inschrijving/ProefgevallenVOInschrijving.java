package nl.topicus.eduarte.krd.bron.schakeltest.vo.inschrijving;

import static nl.topicus.eduarte.entities.organisatie.EntiteitContext.*;

import java.util.Date;

import nl.topicus.cobra.types.personalia.Geslacht;
import nl.topicus.eduarte.entities.landelijk.Nationaliteit;
import nl.topicus.eduarte.entities.opleiding.Opleiding;
import nl.topicus.eduarte.entities.organisatie.EntiteitContext;
import nl.topicus.eduarte.entities.personen.Deelnemer;
import nl.topicus.eduarte.entities.personen.Persoon;
import nl.topicus.eduarte.entities.taxonomie.Taxonomie;
import nl.topicus.eduarte.entities.taxonomie.Verbintenisgebied;
import nl.topicus.eduarte.entities.taxonomie.vo.Elementcode;
import nl.topicus.eduarte.krd.bron.schakeltest.BronSchakelTestCase;

@SuppressWarnings("hiding")
public abstract class ProefgevallenVOInschrijving extends BronSchakelTestCase
{
	protected Deelnemer getDeelnemer1001()
	{
		add(1000000001, 800000481, null, 19890101, "9722TB", "6030", 20080730, 2781,
			Geslacht.Onbekend);
		verbintenis.setLocatie(getLocatie("01OE21"));
		plaatsing = getPlaatsing(3, verbintenis);
		verbintenis.getPlaatsingen().add(plaatsing);
		return deelnemer;
	}

	protected Deelnemer getDeelnemer1002()
	{
		add(1000000002, 800000122, null, 19940202, "9711LK", "", 20080707, 2881, Geslacht.Vrouw);
		verbintenis.setLocatie(getLocatie("01OE21"));
		plaatsing = getPlaatsing(3, verbintenis);
		verbintenis.getPlaatsingen().add(plaatsing);

		// Voor CUMI
		Nationaliteit nationaliteit = new Nationaliteit();
		nationaliteit.setCode("0113");
		nationaliteit.setNaam("Equatoriaalguinese");
		nationaliteit.setBegindatum(new Date());
		deelnemer.getPersoon().setNationaliteit1(nationaliteit);

		// Minder dan 1 jaar in NL
		deelnemer.getPersoon().setDatumInNederland(new Date());

		return deelnemer;
	}

	protected Deelnemer getDeelnemer1003()
	{
		add(1000000003, null, null, 19940303, "9711LB", "", 20081002, 2981, Geslacht.Vrouw);

		verbintenis.setLocatie(getLocatie("01OE21"));
		plaatsing = getPlaatsing(3, verbintenis);
		verbintenis.getPlaatsingen().add(plaatsing);

		Persoon persoon = deelnemer.getPersoon();
		persoon.setOfficieleAchternaam("Proefgevaldrievv");
		persoon.setVoornamen("Voornaamdrievv");
		persoon.getFysiekAdres().getAdres().setStraat("Herenstraat");
		persoon.getFysiekAdres().getAdres().setHuisnummer("00033");
		persoon.getFysiekAdres().getAdres().setPlaats("Groningen");

		return deelnemer;
	}

	protected Deelnemer getDeelnemer1004()
	{
		add(1000000004, null, 100511515, 19910707, "", "6039", 20080531, 2981, Geslacht.Man);
		verbintenis.setLocatie(getLocatie("01OE22"));
		plaatsing = getPlaatsing(4, verbintenis);
		verbintenis.getPlaatsingen().add(plaatsing);
		return deelnemer;
	}

	protected Deelnemer getDeelnemer1005()
	{
		add(1000000005, null, null, 19910711, "9722TB", "", 20080601, 2781, Geslacht.Man);
		verbintenis.setLocatie(getLocatie("01OE22"));
		plaatsing = getPlaatsing(3, verbintenis);
		verbintenis.getPlaatsingen().add(plaatsing);

		Persoon persoon = deelnemer.getPersoon();
		persoon.setOfficieleAchternaam("Achternaameenenzestig");
		persoon.setVoornamen("Voornaameenenzestig");
		persoon.getFysiekAdres().getAdres().setStraat("Kempkensberg");
		persoon.getFysiekAdres().getAdres().setHuisnummer("00002");
		persoon.getFysiekAdres().getAdres().setPlaats("Groningen");
		return deelnemer;
	}

	protected Deelnemer getDeelnemer1006()
	{
		add(1000000006, null, null, 19910708, "9722TB", "", 20080828, 3081, Geslacht.Man);
		verbintenis.setLocatie(getLocatie("01OE21"));
		plaatsing = getPlaatsing(3, verbintenis);
		verbintenis.getPlaatsingen().add(plaatsing);

		Persoon persoon = deelnemer.getPersoon();
		persoon.setOfficieleAchternaam("Achternaamachtenvijftig");
		persoon.setVoornamen("Voornaamachtenvijftig");
		persoon.getFysiekAdres().getAdres().setStraat("Kempkensberg");
		persoon.getFysiekAdres().getAdres().setHuisnummer("00002");
		persoon.getFysiekAdres().getAdres().setPlaats("Groningen");
		return deelnemer;
	}

	protected Deelnemer getDeelnemer1007()
	{
		add(1000000007, 800000407, null, 19940707, "9711LB", "", 20080229, 2781, Geslacht.Vrouw);
		verbintenis.setLocatie(getLocatie("01OE21"));
		plaatsing = getPlaatsing(4, verbintenis);
		verbintenis.getPlaatsingen().add(plaatsing);
		return deelnemer;
	}

	protected Deelnemer getDeelnemer1008()
	{
		add(1000000008, 800000328, null, 19940818, "9711LK", "", 20081212, 2981, Geslacht.Vrouw);
		verbintenis.setLocatie(getLocatie("01OE21"));
		plaatsing = getPlaatsing(4, verbintenis);
		verbintenis.getPlaatsingen().add(plaatsing);
		return deelnemer;
	}

	protected Deelnemer getDeelnemer1009()
	{
		add(1000000009, 800000419, null, 19940909, "9711LB", "", 20080707, 2881, Geslacht.Vrouw);
		verbintenis.setLocatie(getLocatie("01OE21"));
		plaatsing = getPlaatsing(3, verbintenis);
		verbintenis.getPlaatsingen().add(plaatsing);
		return deelnemer;
	}

	protected Deelnemer getDeelnemer1010()
	{
		add(1000000010, null, 100509703, 19910706, "9711LK", "", 20080707, 3081, Geslacht.Man);
		verbintenis.setLocatie(getLocatie("01OE21"));
		plaatsing = getPlaatsing(4, verbintenis);
		verbintenis.getPlaatsingen().add(plaatsing);
		return deelnemer;
	}

	protected Deelnemer getDeelnemer1011()
	{
		add(1000000011, 800000511, null, 19941111, "9711LK", "", 20080831, 36, Geslacht.Vrouw);
		verbintenis.setLocatie(getLocatie("01OE21"));
		plaatsing = getPlaatsing(4, verbintenis);
		verbintenis.getPlaatsingen().add(plaatsing);
		return deelnemer;
	}

	protected Opleiding newOpleiding(String code, String naam)
	{
		Taxonomie taxonomie = new Taxonomie(LANDELIJK);
		taxonomie.setCode("3");
		taxonomie.setAfkorting("VO");
		taxonomie.setNaam("Voortgezet onderwijs");

		Opleiding opleiding = new Opleiding();
		opleiding.setBegindatum(timeUtil.parseDateString("20000801"));
		opleiding.setNaam(naam);
		Verbintenisgebied verbintenisgebied = new Elementcode(EntiteitContext.LANDELIJK);
		verbintenisgebied.setExterneCode(code);
		verbintenisgebied.setTaxonomie(taxonomie);
		opleiding.setVerbintenisgebied(verbintenisgebied);
		return opleiding;
	}
}
