package nl.topicus.eduarte.krd.bron.schakeltest.bve.aanlevering.bo;

import static nl.topicus.cobra.types.personalia.Geslacht.*;
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

public abstract class ProefgevallenBveBO extends BronSchakelTestCase
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
		return add(1001, 210000004, null, 19910501, "9722TB", null, 20080801, 10342, BBL, Ja);
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

		return data;
	}

	protected BronTestData getDeelnemer1003()
	{
		return add(1003, 210000028, null, 19910503, "9722TB", null, 20080801, 10342, BOL, Ja);
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
		return add(1010, 210000119, null, 19910510, "9722TB", null, 20080801, 10342, BBL, Ja);
	}

	protected BronTestData getDeelnemer1011()
	{
		return add(1011, 210000120, null, 19910511, "9722TB", null, 20080801, 10342, BBL, Ja);
	}

	protected BronTestData getDeelnemer1012()
	{
		return add(1012, 210000132, null, 19910512, "9722TB", null, 20080801, 10342, BBL, Ja);
	}

	protected BronTestData getDeelnemer1013()
	{
		return add(1013, 210000144, null, 19910513, "9722TB", null, 20080801, 10342, BBL, Nee);
	}

	protected BronTestData getDeelnemer1014()
	{
		return add(1014, 210000156, null, 19910514, "9722TB", null, 20080801, 10342, BBL, Ja);
	}

	protected BronTestData getDeelnemer1015()
	{
		return add(1015, 210000168, null, 19910515, "9722TB", null, 20080801, 10342, BBL, Ja);
	}

	protected BronTestData getDeelnemer1016()
	{
		BronTestData data =
			add(1016, 210000181, null, 19910516, "9722TB", null, 20080801, 12003, BOL, Ja);

		addExamendeelname(verbintenis, 20090211);

		return data;
	}

	protected BronTestData getDeelnemer1017()
	{
		BronTestData data =
			add(1017, 210000193, null, 19910517, "9722TB", null, 20081002, 12003, BOL, Ja);
		addExamendeelname(verbintenis, 20090211);

		return data;
	}

	protected BronTestData getDeelnemer1018()
	{
		BronTestData data =
			add(1018, 210000211, null, 19910518, "9722TB", null, 20080801, 12003, BOL, Ja);
		addExamendeelname(verbintenis, 20090211);
		return data;
	}

	protected BronTestData getDeelnemer1019()
	{
		BronTestData data =
			add(1019, 210000223, null, 19910519, "9722TB", null, 20080801, 12003, BOL, Ja);
		addExamendeelname(verbintenis, 20090211);
		return data;
	}

	protected BronTestData getDeelnemer1020()
	{
		return add(1020, 210000235, null, 19910520, "9722TB", null, 20080801, 10342, BBL, Ja);
	}

	protected BronTestData getDeelnemer1021()
	{
		BronTestData data =
			add(1021, 210000259, null, 19910521, "9722TB", null, 20080801, 12003, BOL, Ja);
		addBpvInschrijving(20080811, 20080901, 20100901, "02PR9725JJ0001601", 1000);
		return data;
	}

	protected BronTestData getDeelnemer1022()
	{
		BronTestData data =
			add(1022, 210000260, null, 19910522, "9722TB", null, 20080801, 12003, BOL, Ja);
		addBpvInschrijving(20080811, 20080901, 20100901, "02PR9725JJ0001601", 1000);
		return data;
	}

	protected BronTestData getDeelnemer1023()
	{
		BronTestData data =
			add(1023, 210000272, null, 19910523, "9722TB", null, 20080801, 12003, BOL, Ja);
		addBpvInschrijving(20080811, 20080901, 20100901, "02PR9725JJ0001601", 1000);
		return data;
	}

	protected BronTestData getDeelnemer1024()
	{
		BronTestData data =
			add(1024, 210000296, null, 19910524, "9722TB", null, 20080801, 11052, BOL, Nee);
		deelnemer.getPersoon().setGeslacht(Vrouw);
		return data;
	}

	protected BronTestData getDeelnemer1025()
	{
		return add(1025, 210000302, null, 19910525, "9722TB", null, 20080801, 10342, BBL, Ja);
	}

	protected BronTestData getDeelnemer1026()
	{
		return add(1026, 210000326, null, 19910526, "9722TB", null, 20080801, 10342, BBL, Ja);
	}

	protected BronTestData getDeelnemer1027()
	{
		return add(1027, 210000351, null, 19910528, "9722TB", null, 20080801, 10342, BBL, Ja);
	}

	protected BronTestData getDeelnemer1028()
	{
		return add(1028, 210000351, null, 19910528, "9722TB", null, 20080801, 10342, BBL, Ja);
	}

	protected BronTestData getDeelnemer1029()
	{
		return add(1029, 210000375, null, 19910529, "9722TB", null, 20080801, 10342, BBL, Nee);
	}

	protected BronTestData getDeelnemer1030()
	{
		return add(1030, 210000387, null, 19910530, "9722TB", null, 20080801, 10342, BBL, Ja);
	}
}
