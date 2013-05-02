package nl.topicus.eduarte.krd.bron.schakeltest;

import java.util.Date;

import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.types.personalia.Geslacht;
import nl.topicus.cobra.util.TimeUtil;
import nl.topicus.eduarte.dao.helpers.SoortVooropleidingDataAccessHelper;
import nl.topicus.eduarte.entities.inschrijving.Vooropleiding;
import nl.topicus.eduarte.entities.inschrijving.SoortVooropleiding.SoortOnderwijs;
import nl.topicus.eduarte.entities.landelijk.Land;
import nl.topicus.eduarte.entities.landelijk.Nationaliteit;
import nl.topicus.eduarte.entities.personen.Deelnemer;
import nl.topicus.eduarte.entities.personen.Persoon;
import nl.topicus.eduarte.entities.personen.PersoonAdres;

public class BronDeelnemerBuilder
{
	private final TimeUtil timeUtil = TimeUtil.getInstance();

	private final BronBuilder parent;

	private final Deelnemer deelnemer = new Deelnemer();

	private final Persoon persoon = new Persoon();

	private final PersoonAdres fysiekAdres;

	BronDeelnemerBuilder(BronBuilder parent)
	{
		this.parent = parent;
		deelnemer.setPersoon(persoon);
		persoon.setDeelnemer(deelnemer);
		fysiekAdres = persoon.newAdres();
		fysiekAdres.setFysiekadres(true);
		persoon.getAdressen().add(fysiekAdres);

		constructDefaultDeelnemer();
	}

	public BronBuilder build()
	{
		parent.setDeelnemer(deelnemer);
		return parent;
	}

	private void constructDefaultDeelnemer()
	{
		setDeelnemernummer(100000);
		setGeslacht(Geslacht.Man);
		setOfficieleAchternaam("");
		setOfficieleVoorvoegsel("");
		setVoornamen("");
		setRoepnaam("");
		setGeboortedatum(19670427);
		setNationaliteit1(Nationaliteit.getNederlands());
		setWoonadres("", "", "", null, Land.getNederland());
		addVooropleiding("20050801", "20090731", SoortOnderwijs.HAVO);

		fysiekAdres.setBegindatum(persoon.getGeboortedatum());
	}

	public BronDeelnemerBuilder setAllochtoon(boolean allochtoon)
	{
		deelnemer.setAllochtoon(allochtoon);
		return this;
	}

	public BronDeelnemerBuilder setDeelnemernummer(int deelnemernummer)
	{
		deelnemer.setDeelnemernummer(deelnemernummer);
		return this;
	}

	public BronDeelnemerBuilder setOnderwijsnummer(Integer onderwijsnummer)
	{
		if (onderwijsnummer != null)
		{
			deelnemer.setOnderwijsnummer(Long.valueOf(onderwijsnummer));
		}
		return this;
	}

	public BronDeelnemerBuilder setBsn(Integer bsn)
	{
		if (bsn != null)
		{
			persoon.setBsn(Long.valueOf(bsn));
		}
		return this;
	}

	public BronDeelnemerBuilder setGeboortedatum(String geboortedatum)
	{
		persoon.setGeboortedatum(timeUtil.parseDateString(geboortedatum));
		return this;
	}

	public BronDeelnemerBuilder setGeboortedatum(Integer geboortedatum)
	{
		persoon.setGeboortedatum(timeUtil.parseDateString(geboortedatum.toString()));
		return this;
	}

	public BronDeelnemerBuilder setOfficieleAchternaam(String naam)
	{
		persoon.setOfficieleAchternaam(naam);
		return this;
	}

	public BronDeelnemerBuilder setAchternaam(String achternaam)
	{
		persoon.setAchternaam(achternaam);
		return this;
	}

	public BronDeelnemerBuilder setGeslacht(Geslacht geslacht)
	{
		persoon.setGeslacht(geslacht);
		return this;
	}

	public BronDeelnemerBuilder setNationaliteit1(Nationaliteit nationaliteit1)
	{
		persoon.setNationaliteit1(nationaliteit1);
		return this;
	}

	public BronDeelnemerBuilder setNationaliteit2(Nationaliteit nationaliteit2)
	{
		persoon.setNationaliteit2(nationaliteit2);
		return this;
	}

	public BronDeelnemerBuilder setOfficieleVoorvoegsel(String officieleVoorvoegsel)
	{
		persoon.setOfficieleVoorvoegsel(officieleVoorvoegsel);
		return this;
	}

	public BronDeelnemerBuilder setRoepnaam(String roepnaam)
	{
		persoon.setRoepnaam(roepnaam);
		return this;
	}

	public BronDeelnemerBuilder setVoorletters(String voorletters)
	{
		persoon.setVoorletters(voorletters);
		return this;
	}

	public BronDeelnemerBuilder setVoornamen(String voornamen)
	{
		persoon.setVoornamen(voornamen);
		return this;
	}

	public BronDeelnemerBuilder setVoorvoegsel(String voorvoegsel)
	{
		persoon.setVoorvoegsel(voorvoegsel);
		return this;
	}

	public BronDeelnemerBuilder setAdresLand(Land land)
	{
		fysiekAdres.getAdres().setLand(land);
		return this;
	}

	public BronDeelnemerBuilder setAdresPostcode(String postcode)
	{
		if (postcode != null)
		{
			fysiekAdres.getAdres().setPostcode(postcode.trim());
		}
		else
		{
			fysiekAdres.getAdres().setPostcode(null);
		}
		return this;
	}

	public BronDeelnemerBuilder setAdresBegindatum(Date begindatum)
	{
		fysiekAdres.setBegindatum(begindatum);
		return this;

	}

	public BronDeelnemerBuilder setWoonadres(String straat, String huisnummer, String postcode,
			String plaats, Land land)
	{
		fysiekAdres.getAdres().setStraat(straat);
		fysiekAdres.getAdres().setHuisnummer(huisnummer);
		fysiekAdres.getAdres().setPostcode(postcode);
		fysiekAdres.getAdres().setPlaats(plaats);
		fysiekAdres.getAdres().setLand(land);
		return this;

	}

	public BronDeelnemerBuilder addVooropleiding(String begindatum, String einddatum,
			SoortOnderwijs soort)
	{
		Vooropleiding vooropleiding = new Vooropleiding();
		vooropleiding.setDeelnemer(deelnemer);
		deelnemer.getVooropleidingen().add(vooropleiding);

		vooropleiding.setDiplomaBehaald(true);
		vooropleiding.setBegindatum(timeUtil.parseDateString(begindatum));
		vooropleiding.setEinddatum(timeUtil.parseDateString(einddatum));
		SoortVooropleidingDataAccessHelper helper =
			DataAccessRegistry.getHelper(SoortVooropleidingDataAccessHelper.class);
		vooropleiding.setSoortVooropleiding(helper.get(soort));
		return this;

	}
}
