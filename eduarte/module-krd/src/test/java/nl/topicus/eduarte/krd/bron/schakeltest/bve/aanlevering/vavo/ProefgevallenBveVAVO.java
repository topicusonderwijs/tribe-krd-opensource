package nl.topicus.eduarte.krd.bron.schakeltest.bve.aanlevering.vavo;

import static nl.topicus.cobra.types.personalia.Geslacht.*;
import static nl.topicus.eduarte.entities.inschrijving.Verbintenis.VerbintenisStatus.*;

import java.math.BigDecimal;

import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.types.personalia.Geslacht;
import nl.topicus.eduarte.dao.helpers.SoortVooropleidingDataAccessHelper;
import nl.topicus.eduarte.entities.examen.Examendeelname;
import nl.topicus.eduarte.entities.inschrijving.OnderwijsproductAfname;
import nl.topicus.eduarte.entities.inschrijving.SoortVooropleiding;
import nl.topicus.eduarte.entities.inschrijving.Vooropleiding;
import nl.topicus.eduarte.entities.inschrijving.SoortVooropleiding.SoortOnderwijs;
import nl.topicus.eduarte.entities.onderwijsproduct.Onderwijsproduct;
import nl.topicus.eduarte.entities.onderwijsproduct.OnderwijsproductAfnameContext;
import nl.topicus.eduarte.entities.onderwijsproduct.OnderwijsproductTaxonomie;
import nl.topicus.eduarte.entities.organisatie.EntiteitContext;
import nl.topicus.eduarte.entities.personen.Deelnemer;
import nl.topicus.eduarte.entities.taxonomie.vo.LandelijkVak;
import nl.topicus.eduarte.krd.bron.schakeltest.BronBuilder;
import nl.topicus.eduarte.krd.bron.schakeltest.BronSchakelTestCase;
import nl.topicus.onderwijs.duo.bron.bve.waardelijsten.ToepassingResultaatExamenvak;

public abstract class ProefgevallenBveVAVO extends BronSchakelTestCase
{
	private BronBuilder builder = new BronBuilder();

	protected void getDeelnemer2001()
	{
		deelnemer = newDeelnemer(2001, 210000004, null, 19910501, Man, "9722TB", "6030");
		addVerbintenis(5150, 20080101, 20100101);
	}

	private Deelnemer newDeelnemer(int deelnemernummer, Integer sofinummer,
			Integer onderwijsnummer, int geboortedatum, Geslacht geslacht, String postcode,
			String land)
	{
		builder.buildDeelnemer(deelnemernummer, sofinummer, onderwijsnummer, geboortedatum,
			postcode, land);
		deelnemer = builder.getDeelnemer();
		deelnemer.getPersoon().setGeslacht(geslacht);

		Vooropleiding e = new Vooropleiding();
		e.setDeelnemer(deelnemer);
		e.setBegindatum(asDate(20010801));
		e.setEinddatum(asDate(20071231));
		e.setDiplomaBehaald(true);

		deelnemer.getVooropleidingen().clear();
		deelnemer.getVooropleidingen().add(e);

		SoortVooropleiding havo =
			DataAccessRegistry.getHelper(SoortVooropleidingDataAccessHelper.class).get(
				SoortOnderwijs.HAVO);
		e.setSoortVooropleiding(havo);

		return deelnemer;
	}

	private void addVerbintenis(int opleiding, int ingangsdatum, int geplandedatumUitschrijving)
	{
		builder.addVerbintenisVAVO(ingangsdatum, opleiding, geplandedatumUitschrijving);
		verbintenis = builder.getVerbintenis();
		verbintenis.setStatus(Definitief);
		verbintenis.setContacturenPerWeek(BigDecimal.valueOf(10));
	}

	protected void getDeelnemer2002()
	{
		deelnemer = newDeelnemer(2002, 210000697, null, 19910716, Man, "9722TB", "6030");
		addVerbintenis(5150, 20080101, 20100101);
	}

	protected void getDeelnemer2003()
	{
		deelnemer = newDeelnemer(2003, 210000028, null, 19910503, Man, "9722TB", "6030");
		addVerbintenis(5150, 20080101, 20100101);
	}

	protected void getDeelnemer2004()
	{
		deelnemer = newDeelnemer(2004, null, 100320254, 19910701, Man, "9722TB", "6030");
		addVerbintenis(5150, 20080101, 20100101);
	}

	protected void getDeelnemer2005()
	{
		deelnemer = newDeelnemer(2005, null, null, 19910505, Man, "9722TB", "6030");
		deelnemer.getPersoon().setOfficieleAchternaam("ACHTERNAAMVIJF");
		deelnemer.getPersoon().setVoornamen("VOORNAAMVIJF");
		deelnemer.getPersoon().getFysiekAdres().getAdres().setStraat("kemkensberg");
		deelnemer.getPersoon().getFysiekAdres().getAdres().setHuisnummer("2");
		deelnemer.getPersoon().getFysiekAdres().getAdres().setPlaats("Groningen");

		addVerbintenis(5150, 20080101, 20100101);
	}

	protected void getDeelnemer2006()
	{
		deelnemer = newDeelnemer(2006, null, null, 19910702, Man, null, "6030");
		deelnemer.getPersoon().setOfficieleAchternaam("ACHTERNAAMTWEEENVIJFTIG");
		deelnemer.getPersoon().setVoornamen("VOORNAAMTWEEENVIJFTIG");

		addVerbintenis(5150, 20080101, 20100101);
	}

	protected void getDeelnemer2007()
	{
		deelnemer = newDeelnemer(2007, 210000089, null, 19910507, Man, "9722TB", "6030");
		deelnemer.getPersoon().setNieuwkomer(true);
		addVerbintenis(5150, 20080101, 20100101);
	}

	protected void getDeelnemer2008()
	{
		deelnemer = newDeelnemer(2008, 210000090, null, 19910508, Man, "9722TB", "6030");
		addVerbintenis(5150, 20080101, 20100101);
	}

	protected void getDeelnemer2009()
	{
		deelnemer = newDeelnemer(2009, 210000107, null, 19910509, Man, "9722TB", "6030");
		addVerbintenis(5150, 20080101, 20100101);
	}

	protected void getDeelnemer2010()
	{
		deelnemer = newDeelnemer(2010, 210000119, null, 19910510, Man, "9722TB", "6030");
		addVerbintenis(5150, 20080101, 20100101);
	}

	protected void getDeelnemer2011()
	{
		deelnemer = newDeelnemer(2011, 210000120, null, 19910511, Man, "9722TB", "6030");
		addVerbintenis(5150, 20080101, 20100101);
	}

	protected void getDeelnemer2012()
	{
		deelnemer = newDeelnemer(2012, 210000132, null, 19910512, Man, "9722TB", "6030");
		addVerbintenis(5150, 20080101, 20100101);
	}

	protected void getDeelnemer2013()
	{
		deelnemer = newDeelnemer(2013, 210000144, null, 19910513, Man, "9722TB", "6030");
		addVerbintenis(5150, 20080101, 20100101);
	}

	protected void getDeelnemer2014()
	{
		deelnemer = newDeelnemer(2014, 210000156, null, 19910514, Man, "9722TB", "6030");
		addVerbintenis(5370, 20080101, 20100101);

		Examendeelname examen = addExamendeelname(verbintenis, 20090110);
		examen.setExamenstatus(getCertificaten());

		Onderwijsproduct vak = getVak("0011");

		OnderwijsproductAfname vak0011 = new OnderwijsproductAfname();
		vak0011.setOnderwijsproduct(vak);

		OnderwijsproductAfnameContext afnameContext = new OnderwijsproductAfnameContext();
		afnameContext.setVerbintenis(verbintenis);
		afnameContext.setOnderwijsproductAfname(vak0011);
		afnameContext.setDiplomavak(true);
		afnameContext
			.setToepassingResultaatExamenvak(ToepassingResultaatExamenvak.GeexamineerdInJaarMelding);
		afnameContext.setWerkstukHoortBijProduct(false);
		afnameContext.setCertificaatBehaald(true);
		afnameContext.setVerwezenNaarVolgendTijdvak(true);

		verbintenis.getAfnameContexten().add(afnameContext);
	}

	protected void getDeelnemer2015()
	{
		deelnemer = newDeelnemer(2015, 210000168, null, 19910515, Man, "9722TB", "6030");
		addVerbintenis(5150, 20080101, 20100101);

		Examendeelname examen = addExamendeelname(verbintenis, 20090110);
		examen.setExamenstatus(getCertificaten());

		Onderwijsproduct vak = getVak("0011");

		OnderwijsproductAfname vak0011 = new OnderwijsproductAfname();
		vak0011.setOnderwijsproduct(vak);

		OnderwijsproductAfnameContext afnameContext = new OnderwijsproductAfnameContext();
		afnameContext.setVerbintenis(verbintenis);
		afnameContext.setOnderwijsproductAfname(vak0011);
		afnameContext.setDiplomavak(true);
		afnameContext
			.setToepassingResultaatExamenvak(ToepassingResultaatExamenvak.GeexamineerdInJaarMelding);
		afnameContext.setWerkstukHoortBijProduct(false);
		afnameContext.setCertificaatBehaald(true);
		afnameContext.setVerwezenNaarVolgendTijdvak(true);

		verbintenis.getAfnameContexten().add(afnameContext);
	}

	protected void getDeelnemer2016()
	{
		deelnemer = newDeelnemer(2016, 210000181, null, 19910516, Man, "9722TB", "6030");
		addVerbintenis(5150, 20080101, 20100101);

		Examendeelname examen = addExamendeelname(verbintenis, 20090110);
		examen.setExamenstatus(getCertificaten());

		Onderwijsproduct vak = getVak("0011");

		OnderwijsproductAfname vak0011 = new OnderwijsproductAfname();
		vak0011.setOnderwijsproduct(vak);

		OnderwijsproductAfnameContext afnameContext = new OnderwijsproductAfnameContext();
		afnameContext.setVerbintenis(verbintenis);
		afnameContext.setOnderwijsproductAfname(vak0011);
		afnameContext.setDiplomavak(true);
		afnameContext
			.setToepassingResultaatExamenvak(ToepassingResultaatExamenvak.GeexamineerdInJaarMelding);
		afnameContext.setWerkstukHoortBijProduct(false);
		afnameContext.setCertificaatBehaald(true);
		afnameContext.setVerwezenNaarVolgendTijdvak(true);

		verbintenis.getAfnameContexten().add(afnameContext);
	}

	protected void getDeelnemer2016_testgeval25()
	{
		deelnemer = newDeelnemer(2016, 210000181, null, 19910516, Man, "9722TB", "6030");
		addVerbintenis(5150, 20080101, 20100101);
	}

	private Onderwijsproduct getVak(String vakcode)
	{
		LandelijkVak vak = new LandelijkVak(EntiteitContext.LANDELIJK);
		vak.setExterneCode(vakcode);
		Onderwijsproduct product = new Onderwijsproduct();
		OnderwijsproductTaxonomie taxonomie = new OnderwijsproductTaxonomie();
		taxonomie.setOnderwijsproduct(product);
		taxonomie.setTaxonomieElement(vak);
		product.getOnderwijsproductTaxonomieList().add(taxonomie);
		return product;
	}

	protected OnderwijsproductAfname createOnderwijsproductAfname(String vakcode,
			boolean certificaatBehaald)
	{
		Onderwijsproduct vak = getVak(vakcode);

		OnderwijsproductAfname onderwijsproductAfname = new OnderwijsproductAfname();
		onderwijsproductAfname.setOnderwijsproduct(vak);

		OnderwijsproductAfnameContext afnameContext = new OnderwijsproductAfnameContext();
		afnameContext.setVerbintenis(verbintenis);
		afnameContext.setOnderwijsproductAfname(onderwijsproductAfname);
		afnameContext.setDiplomavak(true);
		afnameContext
			.setToepassingResultaatExamenvak(ToepassingResultaatExamenvak.GeexamineerdInJaarMelding);
		afnameContext.setWerkstukHoortBijProduct(false);
		afnameContext.setCertificaatBehaald(certificaatBehaald);
		afnameContext.setVerwezenNaarVolgendTijdvak(true);

		return onderwijsproductAfname;
	}

	protected void getDeelnemer2017()
	{
		deelnemer = newDeelnemer(2017, null, null, 19910517, Man, "9722TB", "6030");

		deelnemer.getPersoon().setOfficieleAchternaam("ACHTERNAAMZEVENTIEN");
		deelnemer.getPersoon().setVoornamen("Voornaam17");
		deelnemer.getPersoon().getFysiekAdres().getAdres().setStraat("Kemkensberg");
		deelnemer.getPersoon().getFysiekAdres().getAdres().setHuisnummer("2");
		deelnemer.getPersoon().getFysiekAdres().getAdres().setPlaats("Groningen");

		addVerbintenis(5150, 20080101, 20100101);
	}

	protected void getDeelnemer1018()
	{
		deelnemer = newDeelnemer(1018, 210000211, null, 19910518, Man, "9722TB", "6030");
		addVerbintenis(5105, 20080101, 20100101);
	}
}
