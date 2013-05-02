/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.entities.adres;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.persistence.*;

import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.entities.FieldPersistance;
import nl.topicus.cobra.entities.FieldPersistenceMode;
import nl.topicus.cobra.entities.RestrictedAccess;
import nl.topicus.cobra.modelsv2.CheckIncomingReferences;
import nl.topicus.cobra.templates.annotations.Exportable;
import nl.topicus.cobra.util.StringUtil;
import nl.topicus.cobra.web.components.form.AutoForm;
import nl.topicus.eduarte.dao.helpers.PlaatsDataAccessHelper;
import nl.topicus.eduarte.entities.IsViewWhenOnNoise;
import nl.topicus.eduarte.entities.landelijk.Gemeente;
import nl.topicus.eduarte.entities.landelijk.Land;
import nl.topicus.eduarte.entities.landelijk.Plaats;
import nl.topicus.eduarte.entities.landelijk.Provincie;
import nl.topicus.eduarte.entities.organisatie.LandelijkOfInstellingEntiteit;
import nl.topicus.eduarte.entities.personen.PersoonAdres;
import nl.topicus.eduarte.web.components.label.PostcodeWoonplaatsLabel;
import nl.topicus.onderwijs.duo.bron.Bron;

import org.apache.commons.lang.StringUtils;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Index;

/**
 * Entiteit voor adresgegevens voor zowel Nederlandse als buitenlandse adressen. Er zijn
 * geen aparte velden voor adresgegevens van buitenlandse adressen (zoals buitenland1,
 * buitenland2, buitenland3). In plaats daarvan maken buitenlandse adressen ook gewoon
 * gebruik van de straat, huisnummer en postcodevelden.
 * 
 * @author loite
 */
@Entity()
@Exportable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
@IsViewWhenOnNoise
public class Adres extends LandelijkOfInstellingEntiteit
{
	private static final long serialVersionUID = 1L;

	@Column(length = 60, nullable = true)
	@Bron
	private String straat;

	@Column(length = 60, nullable = false)
	@Index(name = "idx_adres_plaats")
	@Bron
	private String plaats;

	@Column(length = 12)
	@Index(name = "idx_adres_postcode")
	@Bron(sleutel = true)
	private String postcode;

	@Column(length = 15)
	@Bron
	private String huisnummer;

	@Column(length = 5)
	@Bron
	private String huisnummerToevoeging;

	@Column(length = 35)
	@Bron
	private String locatie;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "gemeente", nullable = true)
	@Index(name = "idx_adres_gemeente")
	private Gemeente gemeente;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "provincie", nullable = true)
	@Index(name = "idx_adres_provincie")
	private Provincie provincie;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "land", nullable = false)
	@Index(name = "idx_adres_land")
	@Bron(sleutel = true, verplicht = true)
	private Land land;

	@Column(nullable = false)
	private boolean geheim;

	/**
	 * Optioneel veld, alleen gevuld voor personen die bij een deelnemer horen. Wordt
	 * gebruikt om wijzigingen in personalia terug te kunnen voeren op de deelnemer voor
	 */
	@OneToMany(mappedBy = "adres", fetch = FetchType.LAZY, targetEntity = AdresEntiteit.class)
	@RestrictedAccess(hasGetter = true, hasSetter = false)
	@FieldPersistance(FieldPersistenceMode.SKIP)
	private List<PersoonAdres> persoonAdressen = new ArrayList<PersoonAdres>();

	/**
	 * Optioneel veld, alleen gevuld voor personen die bij een deelnemer horen. Wordt
	 * gebruikt om wijzigingen in personalia terug te kunnen voeren op de deelnemer voor
	 */
	@OneToMany(mappedBy = "adres", fetch = FetchType.LAZY)
	@RestrictedAccess(hasGetter = true, hasSetter = false)
	@FieldPersistance(FieldPersistenceMode.SKIP)
	@CheckIncomingReferences
	private List<AdresEntiteit< ? >> adresEntiteiten = new ArrayList<AdresEntiteit< ? >>();

	@Enumerated(EnumType.STRING)
	@Column(length = 30)
	private DuitseDeelstaat duitseDeelstaat;

	public Adres()
	{
		try
		{
			// standaard als land = nederland instellen, maar alleen als de
			// DataAccessRegistry beschikbaar is, want anders krijgen we een exception
			// tijdens het opstarten
			setLand(Land.getNederland());
		}
		catch (Exception e)
		{
			// vang alle nare exceptions af, zoals tijdens opstarten of sommige testcases
		}
	}

	public Adres(AdresEntiteit< ? > adresEntiteit)
	{
		this();
		getAdresEntiteiten().add(adresEntiteit);
	}

	@Exportable
	public String getStraat()
	{
		return straat;
	}

	public void setStraat(String straat)
	{
		this.straat = straat;
	}

	@Exportable
	public String getPlaats()
	{
		return plaats;
	}

	/**
	 * Check of de plaatsnaam voorkomt in de Plaats table, zo ja, return die plaatsnaam
	 * (omdat die de correcte hoofdletters zal gebruiken). Zo niet, maak iedere eerste
	 * letter van de woorden in de plaatsnaam een hoofdletter.
	 */
	@Exportable
	public String getPlaatsBeginMetHoofdletter()
	{
		Plaats p =
			DataAccessRegistry.getHelper(PlaatsDataAccessHelper.class)
				.getPlaatsVanCaseInsensitiveNaam(plaats);
		if (p != null)
			return p.getNaam();
		else
			return StringUtil.firstCharUppercaseOfEachWord(plaats.toLowerCase());
	}

	public void setPlaats(String plaats)
	{
		this.plaats = plaats;
	}

	@Exportable
	public Land getLand()
	{
		return land;
	}

	public void setLand(Land land)
	{
		this.land = land;
		if (land == null || !land.isDuitsland())
			duitseDeelstaat = null;
	}

	@Exportable
	public String getPostcodeMetSpatie()
	{
		if (getLand() != null && getPostcode() != null)
		{
			if (getLand().isNederland() && getPostcode().length() == 6)
				return getPostcode().substring(0, 4) + " " + getPostcode().substring(4);
		}
		return getPostcode();
	}

	@Exportable
	public String getPostcode()
	{
		return postcode;
	}

	public void setPostcode(String postcode)
	{
		this.postcode = postcode;
	}

	public Provincie getProvincie()
	{
		return provincie;
	}

	public void setProvincie(Provincie provincie)
	{
		this.provincie = provincie;
	}

	@Exportable
	public String getHuisnummer()
	{
		return huisnummer;
	}

	public void setHuisnummer(String huisnummer)
	{
		this.huisnummer = huisnummer;
	}

	@Exportable
	public String getHuisnummerToevoeging()
	{
		return huisnummerToevoeging;
	}

	public void setHuisnummerToevoeging(String huisnummerToevoeging)
	{
		this.huisnummerToevoeging = huisnummerToevoeging;
	}

	public String getLocatie()
	{
		return locatie;
	}

	public void setLocatie(String locatie)
	{
		this.locatie = locatie;
	}

	/**
	 * Geeft de straatnaam en het huisnummer als een String.
	 * 
	 * @return Straatnaam + huisnummer + huisnummertoevoeging + locatie
	 */
	@AutoForm(label = "Adres")
	@Exportable
	public String getStraatHuisnummer()
	{
		String str = getStraat();
		if (str == null)
			return null;

		StringBuilder ret = new StringBuilder(str);
		if (StringUtil.isNotEmpty(getHuisnummer()))
		{
			ret.append(' ');
			ret.append(getHuisnummer());
		}
		if (StringUtil.isNotEmpty(getHuisnummerToevoeging()))
		{
			ret.append(' ');
			ret.append(getHuisnummerToevoeging());
		}
		if (StringUtil.isNotEmpty(getLocatie()))
		{
			ret.append(' ');
			ret.append(getLocatie());
		}
		return ret.toString();
	}

	/**
	 * Geeft de straatnaam en het huisnummer als een String, of ******** als het een
	 * geheim adres is.
	 * 
	 * @return Straatnaam + huisnummer + huisnummertoevoeging + locatie, of ******** als
	 *         het een geheim adres is.
	 */
	@AutoForm(label = "Adres")
	@Exportable
	public String getStraatHuisnummerFormatted()
	{
		if (isGeheim())
			return "***Geheim***";
		return getStraatHuisnummer();
	}

	/**
	 * @return true als dit een buitenlands adres is.
	 */
	@Exportable
	public boolean isBuitenlandsAdres()
	{
		return getLand() != null && !getLand().isNederland();
	}

	/**
	 * Geeft postcode + plaatsnaam.
	 * 
	 * @return Postcode + plaatsnaam
	 */
	@AutoForm(label = "Plaats", displayClass = PostcodeWoonplaatsLabel.class)
	@Exportable
	public String getPostcodePlaats()
	{
		String plts = getPlaats();
		if (plts == null)
			return null;

		return StringUtil.valueOrEmptyIfNull(getPostcodeMetSpatie()) + "  "
			+ StringUtil.valueOrEmptyIfNull(plts);
	}

	/**
	 * Geeft postcode + plaatsnaam, of ******** als het een geheim adres is.
	 * 
	 * @return Postcode + plaatsnaam, of ******* als het een geheim adres is.
	 */
	@Exportable
	public String getPostcodePlaatsFormatted()
	{
		if (isGeheim())
			return "***Geheim***";
		return getPostcodePlaats();
	}

	public String getLandnaamFormatted()
	{
		if (isGeheim())
			return "***Geheim***";
		return getLand() == null ? null : getLand().getNaam();
	}

	public String getGemeenteFormatted()
	{
		if (isGeheim())
			return "***Geheim***";
		return getGemeente() == null ? null : getGemeente().getNaam();
	}

	public String getProvincieFormatted()
	{
		if (isGeheim())
			return "***Geheim***";
		return getProvincie() == null ? null : getProvincie().getNaam();
	}

	@Exportable
	public String getVolledigAdres()
	{
		StringBuilder sb = new StringBuilder();
		sb.append(StringUtil.valueOrEmptyIfNull(getStraatHuisnummer()));
		if (sb.length() != 0)
			sb.append("\n");
		sb.append(StringUtil.valueOrEmptyIfNull(getPostcodePlaats()));
		if (isBuitenlandsAdres())
			sb.append("\n").append(getLand().getNaam());
		return sb.toString();
	}

	@Exportable
	public String getVolledigAdresOp1Regel()
	{
		StringBuilder sb = new StringBuilder();
		sb.append(StringUtil.valueOrEmptyIfNull(getStraatHuisnummer()));
		if (sb.length() != 0)
			sb.append(", ");
		sb.append(StringUtil.valueOrEmptyIfNull(getPostcodePlaats()));
		if (isBuitenlandsAdres())
			sb.append(", ").append(getLand().getNaam());
		return sb.toString();
	}

	public Gemeente getGemeente()
	{
		return gemeente;
	}

	public void setGemeente(Gemeente gemeente)
	{
		this.gemeente = gemeente;
	}

	@Exportable
	public boolean isGeheim()
	{
		return geheim;
	}

	public void setGeheim(boolean geheim)
	{
		this.geheim = geheim;
	}

	@AutoForm(label = "Geheim", include = true)
	@Exportable
	public String getGeheimOmschrijving()
	{
		return isGeheim() ? "Ja" : "Nee";
	}

	public List<PersoonAdres> getPersoonAdressen()
	{
		return persoonAdressen;
	}

	public List<AdresEntiteit< ? >> getAdresEntiteiten()
	{
		return adresEntiteiten;
	}

	@Override
	public String toString()
	{
		return getVolledigAdres();
	}

	/*
	 * Static methode die een array teruggeeft met in het eerste element het huisnummer
	 * (bv. '100') en in het tweede element de toevoeging (bv. 'A').
	 */
	public static String[] parseHuisnummerToevoeging(String text)
	{
		Pattern pattern = Pattern.compile("([0-9]+)(.*)");
		Matcher matcher = pattern.matcher(text);
		if (!matcher.find())
		{
			return StringUtils.stripAll(new String[] {text, ""});
		}
		else
		{
			return StringUtils.stripAll(new String[] {matcher.group(1), matcher.group(2)});
		}
	}

	public void setDuitseDeelstaat(DuitseDeelstaat duitseDeelstaat)
	{
		this.duitseDeelstaat = duitseDeelstaat;
	}

	public DuitseDeelstaat getDuitseDeelstaat()
	{
		return duitseDeelstaat;
	}

	/**
	 * @return true als dit (woon)adres in Nederland, Belgie, Luxemburg of de grensstreek
	 *         van Duitsland ligt
	 * 
	 */
	public boolean isHOBekostigbaar()
	{
		if (land == null)
			return false;

		return land.isBenelux()
			|| (land.isDuitsland() && duitseDeelstaat != null && duitseDeelstaat.isHOBekostigbaar());
	}
}
