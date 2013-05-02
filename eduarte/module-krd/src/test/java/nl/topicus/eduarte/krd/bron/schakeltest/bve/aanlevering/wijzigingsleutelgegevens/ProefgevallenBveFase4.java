package nl.topicus.eduarte.krd.bron.schakeltest.bve.aanlevering.wijzigingsleutelgegevens;

import static nl.topicus.eduarte.entities.inschrijving.Verbintenis.Bekostigd.*;
import static nl.topicus.eduarte.entities.taxonomie.MBOLeerweg.*;
import static nl.topicus.eduarte.krd.entities.bron.BronStatus.*;
import nl.topicus.cobra.types.personalia.Geslacht;
import nl.topicus.eduarte.entities.adres.Adres;
import nl.topicus.eduarte.entities.inschrijving.Bekostigingsperiode;
import nl.topicus.eduarte.entities.landelijk.Land;
import nl.topicus.eduarte.krd.bron.schakeltest.BronSchakelTestCase;
import nl.topicus.eduarte.krd.bron.schakeltest.BronTestData;
import nl.topicus.onderwijs.duo.bron.bve.waardelijsten.Intensiteit;

import org.junit.Before;

public abstract class ProefgevallenBveFase4 extends BronSchakelTestCase
{
	@Before
	public void setup()
	{
		zetSchooljaarStatus(schooljaar0809, GegevensWordenIngevoerd);
		zetSchooljaarStatus(schooljaar0910, GegevensWordenIngevoerd);
		zetSchooljaarStatus(schooljaar1011, GegevensWordenIngevoerd);
	}

	protected BronTestData getDeelnemer1001()
	{
		BronTestData data =
			add(1001, 210000004, null, 19910501, "9722TB", null, 20080801, 11014, BOL, Ja);
		verbintenis.setGeplandeEinddatum(asDate(20100101));
		return data;
	}

	protected BronTestData getDeelnemer1002()
	{
		BronTestData data =
			add(1002, 210000697, null, 19910716, "9722TB", null, 20080801, 10342, BOL, Gedeeltelijk);

		Bekostigingsperiode periode1 = new Bekostigingsperiode();
		periode1.setVerbintenis(verbintenis);
		periode1.setBegindatum(verbintenis.getBegindatum());
		periode1.setBekostigd(false);
		periode1.setEinddatum(asDate(20091030));

		Bekostigingsperiode periode2 = new Bekostigingsperiode();
		periode2.setVerbintenis(verbintenis);
		periode2.setBegindatum(asDate(20091101));
		periode2.setBekostigd(false);

		verbintenis.getBekostigingsperiodes().add(periode1);
		verbintenis.getBekostigingsperiodes().add(periode2);
		verbintenis.setGeplandeEinddatum(asDate(20100101));

		return data;
	}

	protected BronTestData getDeelnemer1003()
	{
		BronTestData data =
			add(1003, 210000028, null, 19910503, "9722TB", null, 20080801, 10342, BOL, Ja);
		verbintenis.setGeplandeEinddatum(asDate(20081231));
		return data;
	}

	protected BronTestData getDeelnemer1004()
	{
		return add(1004, null, 100320254, 19910701, null, "6030", 20080801, 10342, BOL, Ja);
	}

	protected BronTestData getDeelnemer1005()
	{
		BronTestData data =
			add(1005, null, null, 19910717, "9722TB", null, 20080801, 10342, BOL, Ja);

		deelnemer.getPersoon().setOfficieleAchternaam("ACHTERNAAMZEVENENZESTIG");
		deelnemer.getPersoon().setVoornamen("VOORNAAMZEVENENZESTIG");

		Adres adres = deelnemer.getPersoon().getFysiekAdres().getAdres();
		adres.setStraat("Kemkensberg");
		adres.setHuisnummer("2");
		adres.setPostcode("9722TB");
		adres.setPlaats("Groningen");
		adres.setLand(Land.getNederland());

		return data;
	}

	protected BronTestData getDeelnemer1006()
	{
		BronTestData data = add(1006, null, null, 19910702, null, "6030", 20080801, 10342, BOL, Ja);

		deelnemer.getPersoon().setOfficieleAchternaam("ACHTERNAAMTWEEENVIJFTIG");
		deelnemer.getPersoon().setVoornamen("VOORNAAMTWEEENVIJFTIG");

		return data;
	}

	protected BronTestData getDeelnemer1007()
	{
		BronTestData data =
			add(1007, 800000122, null, 19900202, "9711LK", null, 20080801, 10021, BBL, Ja);
		deelnemer.getPersoon().setGeslacht(Geslacht.Vrouw);
		verbintenis.setIndicatieGehandicapt(true);
		return data;
	}

	protected BronTestData getDeelnemer1008()
	{
		BronTestData data =
			add(1008, 210000727, null, 19910719, "9722TB", null, 20080801, 10342, null, Nee);
		verbintenis.setIntensiteit(Intensiteit.Examendeelnemer);
		return data;
	}

	protected BronTestData getDeelnemer1009()
	{
		return add(1009, 210000107, null, 19910509, "9722TB", null, 20080801, 10342, BBL, Ja);
	}

	protected BronTestData getDeelnemer1010()
	{
		return add(1010, 210000119, null, 19910510, "9722TB", null, 20081101, 11048, BBL, Ja);
	}

	protected BronTestData getDeelnemer1011()
	{
		return add(1011, 210000120, null, 19910511, "9722TB", null, 20080801, 11048, BBL, Ja);
	}

	protected BronTestData getDeelnemer1012()
	{
		return add(1012, 210000132, null, 19910512, "9722TB", null, 20080801, 10342, BBL, Ja);
	}

	protected BronTestData getDeelnemer4001()
	{
		BronTestData data =
			add(4001, null, 100320254, 19910701, "9722TB", null, 20090331, 11048, BBL, Ja);
		verbintenis.setGeplandeEinddatum(asDate(20100401));
		return data;
	}

	protected BronTestData getDeelnemer4001_proefgeval17()
	{
		BronTestData data =
			add(4001, null, 100320254, 19910701, null, "5010", 20090331, 11048, BBL, Ja);
		verbintenis.setGeplandeEinddatum(asDate(20100401));
		return data;
	}

	protected BronTestData getDeelnemer4002()
	{
		BronTestData data =
			add(4002, null, 100144750, 19910702, "9722TB", null, 20090331, 11048, BBL, Ja);
		verbintenis.setGeplandeEinddatum(asDate(20100401));
		return data;
	}

	protected BronTestData getDeelnemer4003()
	{
		BronTestData data =
			add(4003, null, 100069880, 19910703, null, "6030", 20090331, 11048, BBL, Ja);
		verbintenis.setGeplandeEinddatum(asDate(20100401));
		return data;
	}

	protected BronTestData getDeelnemer4004()
	{
		BronTestData data =
			add(4004, 210000041, null, 19910504, "9722TB", null, 20090331, 11048, BBL, Ja);
		verbintenis.setGeplandeEinddatum(asDate(20100401));
		return data;
	}

	protected BronTestData getDeelnemer4005()
	{
		BronTestData data =
			add(4005, null, 100509521, 19910705, null, "6030", 20090331, 11048, BBL, Ja);
		verbintenis.setGeplandeEinddatum(asDate(20100401));
		return data;
	}
}
