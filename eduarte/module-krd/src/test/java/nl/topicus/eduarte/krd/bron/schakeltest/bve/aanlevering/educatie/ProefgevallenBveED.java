package nl.topicus.eduarte.krd.bron.schakeltest.bve.aanlevering.educatie;

import static nl.topicus.cobra.types.personalia.Geslacht.*;
import static nl.topicus.eduarte.entities.inschrijving.Verbintenis.VerbintenisStatus.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import nl.topicus.cobra.types.personalia.Geslacht;
import nl.topicus.eduarte.entities.inschrijving.OnderwijsproductAfname;
import nl.topicus.eduarte.entities.inschrijving.SoortVooropleiding.SoortOnderwijs;
import nl.topicus.eduarte.entities.landelijk.Land;
import nl.topicus.eduarte.entities.onderwijsproduct.Onderwijsproduct;
import nl.topicus.eduarte.entities.onderwijsproduct.OnderwijsproductAfnameContext;
import nl.topicus.eduarte.entities.onderwijsproduct.OnderwijsproductTaxonomie;
import nl.topicus.eduarte.entities.organisatie.EntiteitContext;
import nl.topicus.eduarte.entities.personen.Deelnemer;
import nl.topicus.eduarte.entities.resultaatstructuur.Resultaat;
import nl.topicus.eduarte.entities.resultaatstructuur.Resultaatstructuur;
import nl.topicus.eduarte.entities.resultaatstructuur.Schaalwaarde;
import nl.topicus.eduarte.entities.resultaatstructuur.Toets;
import nl.topicus.eduarte.entities.resultaatstructuur.Resultaatstructuur.Type;
import nl.topicus.eduarte.entities.resultaatstructuur.Toets.SoortToets;
import nl.topicus.eduarte.entities.taxonomie.educatie.BasiseducatieVak;
import nl.topicus.eduarte.krd.bron.schakeltest.BronBuilder;
import nl.topicus.eduarte.krd.bron.schakeltest.BronSchakelTestCase;

public abstract class ProefgevallenBveED extends BronSchakelTestCase
{
	private BronBuilder builder = new BronBuilder();

	private Deelnemer newDeelnemer(int deelnemernummer, Integer sofinummer,
			Integer onderwijsnummer, int geboortedatum, Geslacht geslacht, String postcode,
			Land land)
	{
		builder.buildDeelnemer().setDeelnemernummer(deelnemernummer).setBsn(sofinummer)
			.setOnderwijsnummer(onderwijsnummer).setGeboortedatum(geboortedatum).setGeslacht(
				geslacht).setAdresPostcode(postcode).setAdresLand(land).addVooropleiding(
				"20010801", "20071231", SoortOnderwijs.HAVO).build();
		deelnemer = builder.getDeelnemer();
		return deelnemer;
	}

	private void addVerbintenis(int opleiding, int ingangsdatum, int geplandedatumUitschrijving)
	{
		builder.addVerbintenisMBO().setOpleidingEducatie(opleiding).setIngangsdatum(ingangsdatum)
			.setGeplandeEinddatum(geplandedatumUitschrijving).build();
		verbintenis = builder.getVerbintenis();
		verbintenis.setStatus(Definitief);
		verbintenis.setContacturenPerWeek(BigDecimal.valueOf(10));
	}

	protected void getDeelnemer3001()
	{
		deelnemer =
			newDeelnemer(3001, 210000004, null, 19910501, Man, "9722TB", Land.getNederland());
		addVerbintenisEnVakEDBatch1();
	}

	protected void getDeelnemer3002()
	{
		deelnemer =
			newDeelnemer(3002, 210000697, null, 19910716, Man, "9722TB", Land.getNederland());
		addVerbintenisEnVakEDBatch1();
	}

	protected void getDeelnemer3003()
	{
		deelnemer =
			newDeelnemer(3003, 210000028, null, 19910503, Man, "9722TB", Land.getNederland());
		addVerbintenisEnVakEDBatch1();
	}

	protected void getDeelnemer3004()
	{
		deelnemer =
			newDeelnemer(3004, null, 100320254, 19910701, Man, "9722TB", Land.getNederland());
		addVerbintenisEnVakEDBatch1();
	}

	protected void getDeelnemer3005()
	{
		deelnemer = newDeelnemer(3005, null, null, 19910717, Man, "9722TB", Land.getNederland());

		deelnemer.getPersoon().setOfficieleAchternaam("ACHTERNAAMZEVENENZESTIG");
		deelnemer.getPersoon().setVoornamen("VOORNAAMZEVENENZESTIG");
		deelnemer.getPersoon().getFysiekAdres().getAdres().setStraat("Kemkensberg");
		deelnemer.getPersoon().getFysiekAdres().getAdres().setHuisnummer("2");
		deelnemer.getPersoon().getFysiekAdres().getAdres().setPlaats("Groningen");

		addVerbintenisEnVakEDBatch1();
	}

	protected void getDeelnemer3006()
	{
		deelnemer = newDeelnemer(3006, null, null, 19910704, Man, null, Land.getNederland());

		deelnemer.getPersoon().setOfficieleAchternaam("ACHTERNAAMVIERENVIJFTIG");
		deelnemer.getPersoon().setVoornamen("VOORNAAMVIERENVIJFTIG");

		addVerbintenisEnVakEDBatch1();
	}

	protected void getDeelnemer3007()
	{
		deelnemer =
			newDeelnemer(3007, 210000089, null, 19910507, Man, "9722TB", Land.getNederland());

		addVerbintenis(5511, 20080101, 20100101);
		addEDVak("0011", 1);
		addEDVak("0310", 2);
	}

	protected void getDeelnemer3008()
	{
		deelnemer =
			newDeelnemer(3008, 210000090, null, 19910508, Man, "9722TB", Land.getNederland());
		addVerbintenis(5511, 20080101, 20100101);
		addEDVak("0310", 1);
		addEDVak("0990", 2);
	}

	protected void getDeelnemer3009()
	{
		deelnemer =
			newDeelnemer(3009, 210000107, null, 19910509, Man, "9722TB", Land.getNederland());
		addVerbintenisEnVakEDBatch1();
	}

	protected void getDeelnemer3010()
	{
		deelnemer =
			newDeelnemer(3010, 210000119, null, 19910510, Man, "9722TB", Land.getNederland());
		addVerbintenisEnVakEDBatch1();
	}

	protected void getDeelnemer3011()
	{
		deelnemer =
			newDeelnemer(3011, 210000120, null, 19910511, Man, "9722TB", Land.getNederland());
		addVerbintenisEnVakEDBatch1();
	}

	protected void getDeelnemer3012()
	{
		deelnemer =
			newDeelnemer(3012, 210000132, null, 19910512, Man, "9722TB", Land.getNederland());
		addVerbintenisEnVakEDBatch1();
	}

	protected void getDeelnemer3013()
	{
		deelnemer =
			newDeelnemer(3013, 210000144, null, 19910513, Man, "9722TB", Land.getNederland());
		addVerbintenisEnVakEDBatch1();
	}

	protected void getDeelnemer3014()
	{
		deelnemer =
			newDeelnemer(3014, 210000156, null, 19910514, Man, "9722TB", Land.getNederland());
		addVerbintenisEnVakEDBatch1();
	}

	protected void getDeelnemer3015()
	{
		deelnemer =
			newDeelnemer(3015, 210000168, null, 19910515, Man, "9722TB", Land.getNederland());
		addVerbintenisEnVakEDBatch1();
	}

	protected void getDeelnemer3016()
	{
		deelnemer =
			newDeelnemer(3016, 210000181, null, 19910516, Man, "9722TB", Land.getNederland());
		addVerbintenis(5501, 20080101, 20100101);
		addEDVak("0310", 1);
	}

	protected void getDeelnemer3017()
	{
		deelnemer =
			newDeelnemer(3017, 210000193, null, 19910517, Man, "9722TB", Land.getNederland());
		addVerbintenisEnVakEDBatch1();
	}

	protected void getDeelnemer3018()
	{
		deelnemer =
			newDeelnemer(3018, 210000211, null, 19910518, Man, "9722TB", Land.getNederland());
		addVerbintenisEnVakEDBatch1();
	}

	protected void getDeelnemer3019()
	{
		deelnemer =
			newDeelnemer(3019, 210000223, null, 19910519, Man, "9722TB", Land.getNederland());
		addVerbintenis(5511, 20080101, 20100101);
		addEDVak("0990", 1);
	}

	protected void getDeelnemer3020()
	{
		deelnemer =
			newDeelnemer(3020, 210000235, null, 19910520, Man, "9722TB", Land.getNederland());
		addVerbintenisEnVakEDBatch1();
	}

	protected void getDeelnemer3021()
	{
		deelnemer =
			newDeelnemer(3021, 210000259, null, 19910521, Man, "9722TB", Land.getNederland());
		addVerbintenisEnVakEDBatch1();
	}

	protected void getDeelnemer3022()
	{
		deelnemer =
			newDeelnemer(3022, 210000260, null, 19910522, Man, "9722TB", Land.getNederland());
		addVerbintenis(5511, 20080101, 20100101);
		addEDVak("0990", 1);
	}

	protected void getDeelnemer3023()
	{
		deelnemer =
			newDeelnemer(3023, 210000272, null, 19910523, Man, "9722TB", Land.getNederland());
		addVerbintenis(5511, 20080101, 20100101);
		addEDVak("0990", 1);
	}

	protected void getDeelnemer3024()
	{
		deelnemer =
			newDeelnemer(3024, 210000296, null, 19910524, Man, "9722TB", Land.getNederland());
		addVerbintenis(5511, 20080101, 20100101);
		addEDVak("0990", 1);
	}

	protected void getDeelnemer3025()
	{
		deelnemer =
			newDeelnemer(3025, 210000302, null, 19910525, Man, "9722TB", Land.getNederland());
		addVerbintenisEnVakEDBatch1();
		addEDVak("0011", 2);
	}

	protected void getDeelnemer3026()
	{
		deelnemer =
			newDeelnemer(3026, 210000326, null, 19910526, Man, "9722TB", Land.getNederland());
		addVerbintenis(5511, 20080101, 20100101);
		addEDVak("0990", 1);
		addEDVak("0011", 2);
	}

	protected void getDeelnemer3027()
	{
		deelnemer =
			newDeelnemer(3027, 210000338, null, 19910527, Man, "9722TB", Land.getNederland());
		addVerbintenis(5511, 20080101, 20100101);
		addEDVak("0990", 1);
	}

	private void addVerbintenisEnVakEDBatch1()
	{
		addVerbintenis(5511, 20080101, 20100101);
		addEDVak("0310", 1);
	}

	protected void addEDVak(String vakcode, int volgnummer)
	{
		Onderwijsproduct vak = getEDVak(vakcode, volgnummer);

		OnderwijsproductAfname vakAfname = new OnderwijsproductAfname();
		vakAfname.setBegindatum(new Date());
		vakAfname.setOnderwijsproduct(vak);
		vakAfname.setDeelnemer(deelnemer);
		OnderwijsproductAfnameContext afnameContext = new OnderwijsproductAfnameContext();
		afnameContext.setVolgnummer(volgnummer);
		afnameContext.setOnderwijsproductAfname(vakAfname);
		afnameContext.setVerbintenis(verbintenis);

		List<OnderwijsproductAfnameContext> listOnderwijsproductAfnameContext =
			new ArrayList<OnderwijsproductAfnameContext>();
		listOnderwijsproductAfnameContext.add(afnameContext);
		vakAfname.setAfnameContexten(listOnderwijsproductAfnameContext);

		verbintenis.getAfnameContexten().add(afnameContext);
	}

	protected Onderwijsproduct getEDVak(String vakcode, int volgnummer)
	{
		BasiseducatieVak vak = new BasiseducatieVak(EntiteitContext.LANDELIJK);
		vak.setExterneCode(vakcode);
		vak.setVolgnummer(volgnummer);
		Onderwijsproduct product = new Onderwijsproduct();
		OnderwijsproductTaxonomie taxonomie = new OnderwijsproductTaxonomie();
		taxonomie.setOnderwijsproduct(product);
		taxonomie.setTaxonomieElement(vak);
		product.getOnderwijsproductTaxonomieList().add(taxonomie);
		return product;
	}

	protected Schaalwaarde getNT2Schaalwaarde(Resultaat resultaat, SoortToets soortToetst,
			String startniveau, String behaaldNiveau)
	{
		resultaat.setDeelnemer(deelnemer);

		Resultaatstructuur resultaatstructuur = new Resultaatstructuur();
		resultaatstructuur.setType(Type.SUMMATIEF);
		resultaatstructuur.setNaam("Summatief");
		resultaatstructuur.setCode("SUM");
		resultaatstructuur.setOnderwijsproduct(verbintenis.getOnderwijsproductAfnames().get(0)
			.getOnderwijsproduct());

		Toets hoofdtoets = new Toets();
		hoofdtoets.setSoort(soortToetst);

		Toets toetsStartniveau = new Toets();
		toetsStartniveau.setSoort(SoortToets.Instroomniveau);
		toetsStartniveau.setWeging(0);
		toetsStartniveau.setResultaatstructuur(resultaatstructuur);
		toetsStartniveau.setParent(hoofdtoets);

		Toets toetsBehaaldniveau = new Toets();
		toetsBehaaldniveau.setSoort(SoortToets.BehaaldNiveau);
		toetsBehaaldniveau.setWeging(1);
		toetsBehaaldniveau.setResultaatstructuur(resultaatstructuur);
		toetsBehaaldniveau.setParent(hoofdtoets);

		Schaalwaarde schaalwaardeStartNiveau = new Schaalwaarde();
		schaalwaardeStartNiveau.setExterneWaarde(startniveau);
		schaalwaardeStartNiveau.setNominaleWaarde(BigDecimal.ONE);

		Schaalwaarde schaalwaardeBehaaldNiveau = new Schaalwaarde();
		schaalwaardeBehaaldNiveau.setExterneWaarde(behaaldNiveau);
		schaalwaardeStartNiveau.setNominaleWaarde(BigDecimal.ONE);

		List<Toets> toetsen = new ArrayList<Toets>();
		toetsen.add(toetsStartniveau);
		toetsen.add(toetsBehaaldniveau);
		hoofdtoets.setChildren(toetsen);

		toetsBehaaldniveau.setParent(hoofdtoets);
		toetsBehaaldniveau.setParent(hoofdtoets);

		if (behaaldNiveau == null)
		{
			resultaat.setCijfer(schaalwaardeStartNiveau.getNominaleWaarde());
			resultaat.setWaarde(schaalwaardeStartNiveau);
			resultaat.setToets(toetsStartniveau);
			return schaalwaardeStartNiveau;
		}
		else
		{
			resultaat.setToets(toetsBehaaldniveau);
			resultaat.setCijfer(schaalwaardeBehaaldNiveau.getNominaleWaarde());
			resultaat.setWaarde(schaalwaardeBehaaldNiveau);
			return schaalwaardeBehaaldNiveau;
		}
	}
}
