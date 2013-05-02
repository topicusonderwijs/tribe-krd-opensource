package nl.topicus.eduarte.krd.bron.schakeltest;

import static nl.topicus.eduarte.entities.organisatie.EntiteitContext.*;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import nl.topicus.cobra.util.TimeUtil;
import nl.topicus.eduarte.entities.opleiding.Opleiding;
import nl.topicus.eduarte.entities.organisatie.EntiteitContext;
import nl.topicus.eduarte.entities.taxonomie.MBOLeerweg;
import nl.topicus.eduarte.entities.taxonomie.Taxonomie;
import nl.topicus.eduarte.entities.taxonomie.Verbintenisgebied;
import nl.topicus.eduarte.entities.taxonomie.educatie.BasiseducatieElementcode;
import nl.topicus.eduarte.entities.taxonomie.mbo.Kwalificatie;
import nl.topicus.eduarte.entities.taxonomie.vo.Elementcode;

public abstract class BronOpleidingBuilder
{
	public static BronOpleidingBuilder kwalificatieBuilder()
	{
		return new BronOpleidingBuilderKwalificatie();
	}

	public static BronOpleidingBuilder educatieBuilder()
	{
		return new BronOpleidingBuilderEducatie();
	}

	public static BronOpleidingBuilder voBuilder()
	{
		return new BronOpleidingBuilderVO();
	}

	private static Map<Integer, String> namen = new HashMap<Integer, String>();

	static
	{
		namen.put(5105, "VAVO tiepvaut in schakeltest");
		namen.put(5150, "VAVO ATH Prf Nat/Tech");
		namen.put(5170, "VAVO ATH NW Prf Nat/Tech");
		namen.put(5370, "VAVO HAVO NW Prf Nat/Tech");
		namen.put(10021, "Magazijnmedewerker");
		namen.put(10395, "Zelfstandig Werkend Kok");
		namen.put(10630, "Zelfstandig beroepsbeoefenaar potplantenteelt");
		namen.put(10670, "Particulier Begeleider Exceptioneel Transport");
		namen.put(11003, "Hoefsmid");
		namen.put(11048, "Medewerker veehouderij");
		namen.put(11051, "Medewerker dierverzorging");
		namen.put(11052, "Medewerker paardenhouderij en paardensport");
		namen.put(12003, "Assistent medewerker voedsel en groen (bloemen- en tuincentrumbranche)");
		namen.put(10342, "Grafisch Vormgever");
		namen.put(56176, "Installeren en configureren van netwerken 4");
		namen.put(57010, "SOM: Booglassen MBE 1");
		namen.put(57016, "Teeltvoorbereiding A");
	}

	protected int code;

	protected MBOLeerweg leerweg;

	private Taxonomie taxonomie;

	protected BronOpleidingBuilder(String taxonomieNr, String taxonomieAfkorting,
			String taxonomieNaam)
	{
		this.taxonomie = getTaxonomie(taxonomieNr, taxonomieAfkorting, taxonomieNaam);
	}

	public Opleiding build()
	{
		Opleiding opleiding = createOpleiding();

		Verbintenisgebied verbintenisgebied = getTaxonomieElement();
		opleiding.setVerbintenisgebied(verbintenisgebied);

		return opleiding;
	}

	public BronOpleidingBuilder setCode(int code)
	{
		this.code = code;
		return this;
	}

	public BronOpleidingBuilder setLeerweg(MBOLeerweg leerweg)
	{
		this.leerweg = leerweg;
		return this;
	}

	private Opleiding createOpleiding()
	{
		Opleiding opleiding = new Opleiding();

		Date begindatum = TimeUtil.getInstance().parseDateString("20000801");
		opleiding.setBegindatum(begindatum);
		opleiding.setNaam(namen.get(code));
		opleiding.setLeerweg(leerweg);
		return opleiding;
	}

	private Taxonomie getTaxonomie(String nr, String afkorting, String omschrijving)
	{
		taxonomie = new Taxonomie(LANDELIJK);
		taxonomie.setCode(nr);
		taxonomie.setAfkorting(afkorting);
		taxonomie.setNaam(omschrijving);
		return taxonomie;
	}

	private Verbintenisgebied getTaxonomieElement()
	{
		Verbintenisgebied gebied = newVerbintenisgebied(code);
		gebied.setAfkorting(String.format("%04d", code));
		gebied.setNaam(namen.get(code));
		gebied.setExterneCode(String.format("%04d", code));
		gebied.setTaxonomie(taxonomie);
		taxonomie.getChildren().add(gebied);
		return gebied;
	}

	abstract Verbintenisgebied newVerbintenisgebied(int opleidingsCode);
}

class BronOpleidingBuilderKwalificatie extends BronOpleidingBuilder
{
	BronOpleidingBuilderKwalificatie()
	{
		super("2", "MBO", "MBO Kwalificatiestructuur");
	}

	@Override
	protected Verbintenisgebied newVerbintenisgebied(int opleidingsCode)
	{
		return new Kwalificatie(EntiteitContext.LANDELIJK);
	}
}

class BronOpleidingBuilderEducatie extends BronOpleidingBuilder
{
	BronOpleidingBuilderEducatie()
	{
		super("4", "Educatie", "Educatie");
	}

	@Override
	protected Verbintenisgebied newVerbintenisgebied(int opleidingsCode)
	{
		return new BasiseducatieElementcode(EntiteitContext.LANDELIJK);
	}
}

class BronOpleidingBuilderVO extends BronOpleidingBuilder
{
	private static Map<Integer, Elementcode> opleidingen = new HashMap<Integer, Elementcode>();

	static
	{
		Elementcode ec2481 = new Elementcode(LANDELIJK);
		ec2481.setCode("2481");
		ec2481.setExterneCode("2481");
		ec2481.setLwoo(true);
		opleidingen.put(2481, ec2481);

		Elementcode ec2881 = new Elementcode(LANDELIJK);
		ec2881.setCode("2881");
		ec2881.setExterneCode("2881");
		ec2881.setLwoo(false);
		ec2881.setLwooTaxonomieElement(ec2481);
		opleidingen.put(2881, ec2881);
	}

	BronOpleidingBuilderVO()
	{
		super("3", "VO", "Voortgezet onderwijs");
	}

	@Override
	protected Verbintenisgebied newVerbintenisgebied(int opleidingsCode)
	{
		Elementcode elementcode = opleidingen.get(opleidingsCode);
		if (elementcode != null)
			return elementcode;
		return new Elementcode(EntiteitContext.LANDELIJK);
	}
}