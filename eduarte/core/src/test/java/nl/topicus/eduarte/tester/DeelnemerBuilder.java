package nl.topicus.eduarte.tester;

import java.util.Date;

import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.types.personalia.Geslacht;
import nl.topicus.cobra.util.StringUtil;
import nl.topicus.cobra.util.TimeUtil;
import nl.topicus.eduarte.dao.helpers.SoortVooropleidingDataAccessHelper;
import nl.topicus.eduarte.entities.inschrijving.Vooropleiding;
import nl.topicus.eduarte.entities.inschrijving.SoortVooropleiding.SoortOnderwijs;
import nl.topicus.eduarte.entities.landelijk.Land;
import nl.topicus.eduarte.entities.landelijk.Nationaliteit;
import nl.topicus.eduarte.entities.personen.Deelnemer;
import nl.topicus.eduarte.entities.personen.Persoon;
import nl.topicus.eduarte.entities.personen.PersoonAdres;

public class DeelnemerBuilder
{
	private Deelnemer deelnemer = null;

	private final TimeUtil timeUtil = TimeUtil.getInstance();

	private Persoon persoon;

	private PersoonAdres fysiekAdres;

	public DeelnemerBuilder()
	{
		constructDefaultDeelnemer();
	}

	private void constructDefaultDeelnemer()
	{
		deelnemer = new Deelnemer();
		persoon = new Persoon();

		deelnemer.setPersoon(persoon);
		persoon.setDeelnemer(deelnemer);

		fysiekAdres = persoon.newAdres();
		fysiekAdres.setFysiekadres(true);
		persoon.getAdressen().add(fysiekAdres);

		setDeelnemernummer(100000);
		setBsn("123456789");
		setGeslacht(Geslacht.Man);
		setOfficieleAchternaam("");
		setOfficieleVoorvoegsel("");
		setVoornamen("");
		setRoepnaam("");
		setGeboortedatum(19670427);
		setNationaliteit1(Nationaliteit.getNederlands());
		setWoonadres("", "", "", null, Land.getNederland());
		addVooropleiding("20050801", "20090731", SoortOnderwijs.VMBOTL);

		fysiekAdres.setBegindatum(persoon.getGeboortedatum());
	}

	public void setAllochtoon(boolean allochtoon)
	{
		deelnemer.setAllochtoon(allochtoon);
	}

	public void setDeelnemernummer(int deelnemernummer)
	{
		deelnemer.setDeelnemernummer(deelnemernummer);
	}

	public void setOnderwijsnummer(String onderwijsnummer)
	{
		if (StringUtil.isEmpty(onderwijsnummer))
			deelnemer.setOnderwijsnummer(null);
		else
			deelnemer.setOnderwijsnummer(Long.valueOf(onderwijsnummer));
	}

	public void setBsn(String bsn)
	{
		if (StringUtil.isEmpty(bsn))
			persoon.setBsn(null);
		else
			persoon.setBsn(Long.valueOf(bsn));
	}

	public void setGeboortedatum(String geboortedatum)
	{
		persoon.setGeboortedatum(timeUtil.parseDateString(geboortedatum));
	}

	public void setGeboortedatum(Integer geboortedatum)
	{
		persoon.setGeboortedatum(timeUtil.parseDateString(geboortedatum.toString()));
	}

	public void setOfficieleAchternaam(String naam)
	{
		persoon.setOfficieleAchternaam(naam);
	}

	public void setAchternaam(String achternaam)
	{
		persoon.setAchternaam(achternaam);
	}

	public void setGeslacht(Geslacht geslacht)
	{
		persoon.setGeslacht(geslacht);
	}

	public void setNationaliteit1(Nationaliteit nationaliteit1)
	{
		persoon.setNationaliteit1(nationaliteit1);
	}

	public void setNationaliteit2(Nationaliteit nationaliteit2)
	{
		persoon.setNationaliteit2(nationaliteit2);
	}

	public void setOfficieleVoorvoegsel(String officieleVoorvoegsel)
	{
		persoon.setOfficieleVoorvoegsel(officieleVoorvoegsel);
	}

	public void setRoepnaam(String roepnaam)
	{
		persoon.setRoepnaam(roepnaam);
	}

	public void setVoorletters(String voorletters)
	{
		persoon.setVoorletters(voorletters);
	}

	public void setVoornamen(String voornamen)
	{
		persoon.setVoornamen(voornamen);
	}

	public void setVoorvoegsel(String voorvoegsel)
	{
		persoon.setVoorvoegsel(voorvoegsel);
	}

	public void setAdresLand(Land land)
	{
		fysiekAdres.getAdres().setLand(land);
	}

	public void setAdresPostcode(String postcode)
	{
		if (postcode != null)
		{
			fysiekAdres.getAdres().setPostcode(postcode.trim());
		}
		else
		{
			fysiekAdres.getAdres().setPostcode(null);
		}
	}

	public void setAdresBegindatum(Date begindatum)
	{
		fysiekAdres.setBegindatum(begindatum);
	}

	public void setWoonadres(String straat, String huisnummer, String postcode, String plaats,
			Land land)
	{
		fysiekAdres.getAdres().setStraat(straat);
		fysiekAdres.getAdres().setHuisnummer(huisnummer);
		fysiekAdres.getAdres().setPostcode(postcode);
		fysiekAdres.getAdres().setPlaats(plaats);
		fysiekAdres.getAdres().setLand(land);
	}

	public void addVooropleiding(String begindatum, String einddatum, SoortOnderwijs soort)
	{
		Vooropleiding vooropleiding = new Vooropleiding();
		vooropleiding.setDeelnemer(deelnemer);
		deelnemer.getVooropleidingen().add(vooropleiding);

		vooropleiding.setBegindatum(timeUtil.parseDateString(begindatum));
		vooropleiding.setEinddatum(timeUtil.parseDateString(einddatum));
		SoortVooropleidingDataAccessHelper helper =
			DataAccessRegistry.getHelper(SoortVooropleidingDataAccessHelper.class);
		vooropleiding.setSoortVooropleiding(helper.get(soort));
	}

	public Deelnemer build()
	{
		Deelnemer result = deelnemer;
		constructDefaultDeelnemer();
		return result;
	}
}
