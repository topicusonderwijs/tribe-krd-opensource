package nl.topicus.eduarte.krd.entities.bron.meldingen.aanlevering;

import static nl.topicus.eduarte.entities.inschrijving.Verbintenis.VerbintenisStatus.*;
import static nl.topicus.onderwijs.duo.bron.vo.waardelijsten.SoortMutatie.*;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Transient;

import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.types.personalia.Geslacht;
import nl.topicus.cobra.util.Asserts;
import nl.topicus.cobra.util.HibernateObjectCopyManager;
import nl.topicus.cobra.util.JavaUtil;
import nl.topicus.cobra.util.StringUtil;
import nl.topicus.cobra.util.TimeUtil;
import nl.topicus.eduarte.entities.BronMeldingOnderdeel;
import nl.topicus.eduarte.entities.inschrijving.Plaatsing;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.entities.landelijk.Land;
import nl.topicus.eduarte.entities.landelijk.Schooljaar;
import nl.topicus.eduarte.entities.personen.Deelnemer;
import nl.topicus.eduarte.krd.bron.BronEduArteModel;
import nl.topicus.eduarte.krd.dao.helpers.BronDataAccessHelper;
import nl.topicus.eduarte.krd.entities.bron.BronMeldingStatus;
import nl.topicus.eduarte.krd.entities.bron.meldingen.BronBatchVOInschrijvingen;
import nl.topicus.eduarte.krd.zoekfilters.BronMeldingZoekFilter;
import nl.topicus.eduarte.zoekfilters.OrganisatieEenheidLocatieAuthorizationContext;
import nl.topicus.onderwijs.duo.bron.data.types.Datum;
import nl.topicus.onderwijs.duo.bron.data.waardelijsten.HuisnummerAanduiding;
import nl.topicus.onderwijs.duo.bron.vo.batches.VOInschrijvingMeldingRecordDecentraal;
import nl.topicus.onderwijs.duo.bron.vo.waardelijsten.CumiCategorie;
import nl.topicus.onderwijs.duo.bron.vo.waardelijsten.CumiRatio;

import org.apache.wicket.security.checks.AlwaysGrantedSecurityCheck;
import org.hibernate.annotations.Type;

@Entity
@DiscriminatorValue(value = "110")
public class BronInschrijvingsgegevensVOMelding extends AbstractBronVOMelding implements
		VOInschrijvingMeldingRecordDecentraal
{
	private static final long serialVersionUID = 1L;

	public enum VoMeldingSoort
	{
		W("Wijziging persoonsgegevens"),
		I("Inschrijfgegevens");

		private final String omschrijving;

		private VoMeldingSoort(String omschrijving)
		{
			this.omschrijving = omschrijving;
		}

		public String getOmschrijving()
		{
			return omschrijving;
		}

		@Override
		public String toString()
		{
			return omschrijving;
		}
	}

	@Enumerated(EnumType.STRING)
	@Column(length = 1, name = "soort")
	private VoMeldingSoort soort;

	@Column(length = 200)
	private String achternaam;

	@Column(length = 35)
	private String adresregelBuitenland1;

	@Column(length = 35)
	private String adresregelBuitenland2;

	@Column(length = 35)
	private String adresregelBuitenland3;

	@Column(length = 200)
	private String alleVoornamen;

	@Column(length = 4)
	private String codeLandAdres;

	@Column
	@Enumerated(EnumType.STRING)
	private CumiCategorie cumiCategorie;

	@Column
	@Enumerated(EnumType.STRING)
	private CumiRatio cumiRatio;

	@Column
	private Date datumIngangAdresWijziging;

	@Column
	private Date eindDatum;

	@Column
	@Type(type = "nl.topicus.eduarte.krd.hibernate.usertypes.DatumUsertype")
	private Datum geboorteDatumGewijzigd;

	@Column
	@Enumerated(EnumType.STRING)
	private Geslacht geslachtGewijzigd;

	@Column
	private Integer huisNummer;

	@Column
	@Enumerated(EnumType.STRING)
	private HuisnummerAanduiding huisnummerAanduiding;

	@Column(length = 5)
	private String huisnummerToevoeging;

	@Column
	private Integer ILTCode;

	@Column
	private Date ingangsDatum;

	@Column
	private Integer jarenPraktijkOnderwijs;

	@Column(length = 4, name = "landGewijzigd")
	private String landCodeVolgensInstellingGewijzigd;

	@Column
	private Integer leerjaar;

	@Column(length = 35)
	private String locatieOmschrijving;

	@Column(length = 24)
	private String plaatsnaam;

	@Column(length = 6)
	private String postcode;

	@Column(length = 6, name = "postcodeGewijzigd")
	private String postcodeVolgensInstellingGewijzigd;

	@Column(length = 9, name = "sofinummerGewijzigd")
	private String sofinummerAchterhaald;

	@Column(length = 24)
	private String straatNaam;

	@Column(length = 10)
	private String voorvoegsel;

	@Transient
	private boolean verwijderd;

	public BronInschrijvingsgegevensVOMelding()
	{
	}

	public BronInschrijvingsgegevensVOMelding(BronBatchVOInschrijvingen batch)
	{
		super(batch);
	}

	/**
	 * Bepaalt of er gegevens ingevuld zijn die naar BRON gestuurd moeten worden. Als de
	 * melding leeg is, dan moet deze uit de wachtrij gegooid worden.
	 */
	public boolean isBronCommuniceerbaar()
	{
		boolean geldigeInschrijving = isInschrijvingGevuld() && !isWijzigingSleutelGevuld();
		boolean geldigeWijziging = !isInschrijvingGevuld() && isWijzigingSleutelGevuld();
		return isSleutelwijzigingGevuld() && (geldigeInschrijving || geldigeWijziging);
	}

	public boolean isSleutelwijzigingGevuld()
	{
		return JavaUtil.areNotNullOrEmpty(getSofiNummer(), getOnderwijsNummer(),
			getLeerlingNummerInstelling(), getGeboorteDatum(), getGeslacht(), getPostcode(),
			getCodeLandAdres());
	}

	public boolean isInschrijvingGevuld()
	{
		return JavaUtil.areNotNullOrEmpty(getIngangsDatum(), getEindDatum(), getILTCode(),
			getJarenPraktijkOnderwijs(), getLeerjaar(), getCumiCategorie(), getCumiRatio());
	}

	public boolean isWijzigingSleutelGevuld()
	{
		return JavaUtil.areNotNullOrEmpty(getSofinummerAchterhaald(), getGeboorteDatumGewijzigd(),
			getGeslachtGewijzigd(), getPostcodeVolgensInstellingGewijzigd(),
			getLandCodeVolgensInstellingGewijzigd(), getDatumIngangAdresWijziging());
	}

	public boolean isPersonaliaGevuld()
	{
		return JavaUtil.areNotNullOrEmpty(getAchternaam(), getVoorvoegsel(), getAlleVoornamen(),
			getStraatNaam(), getHuisNummer(), getHuisnummerToevoeging(), getLocatieOmschrijving(),
			getHuisnummerAanduiding(), getPlaatsnaam(), getAdresregelBuitenland1(),
			getAdresregelBuitenland2(), getAdresregelBuitenland3());
	}

	public VoMeldingSoort getSoort()
	{
		return soort;
	}

	public void setSoort(VoMeldingSoort soort)
	{
		this.soort = soort;
	}

	public String getAchternaam()
	{
		return achternaam;
	}

	public void setAchternaam(String achternaam)
	{
		this.achternaam = StringUtil.truncate(achternaam, 200, "");
	}

	public String getAdresregelBuitenland1()
	{
		return adresregelBuitenland1;
	}

	public void setAdresregelBuitenland1(String adresregelBuitenland1)
	{
		this.adresregelBuitenland1 = StringUtil.truncate(adresregelBuitenland1, 35, "");
	}

	public String getAdresregelBuitenland2()
	{
		return adresregelBuitenland2;
	}

	public void setAdresregelBuitenland2(String adresregelBuitenland2)
	{
		this.adresregelBuitenland2 = StringUtil.truncate(adresregelBuitenland2, 35, "");
	}

	public String getAdresregelBuitenland3()
	{
		return adresregelBuitenland3;
	}

	public void setAdresregelBuitenland3(String adresregelBuitenland3)
	{
		this.adresregelBuitenland3 = StringUtil.truncate(adresregelBuitenland3, 35, "");
	}

	public String getAlleVoornamen()
	{
		return alleVoornamen;
	}

	public void setAlleVoornamen(String alleVoornamen)
	{
		this.alleVoornamen = StringUtil.truncate(alleVoornamen, 200, "");
	}

	public String getCodeLandAdres()
	{
		return codeLandAdres;
	}

	public void setCodeLandAdres(String codeLandAdres)
	{
		this.codeLandAdres = codeLandAdres;
	}

	public CumiCategorie getCumiCategorie()
	{
		return cumiCategorie;
	}

	public void setCumiCategorie(CumiCategorie cumiCategorie)
	{
		this.cumiCategorie = cumiCategorie;
	}

	public CumiRatio getCumiRatio()
	{
		return cumiRatio;
	}

	public void setCumiRatio(CumiRatio cumiRatio)
	{
		this.cumiRatio = cumiRatio;
	}

	public Date getDatumIngangAdresWijziging()
	{
		return datumIngangAdresWijziging;
	}

	public void setDatumIngangAdresWijziging(Date datumIngangAdresWijziging)
	{
		this.datumIngangAdresWijziging = datumIngangAdresWijziging;
	}

	public Date getEindDatum()
	{
		return eindDatum;
	}

	public void setEindDatum(Date eindDatum)
	{
		this.eindDatum = eindDatum;
	}

	public Datum getGeboorteDatumGewijzigd()
	{
		return geboorteDatumGewijzigd;
	}

	public void setGeboorteDatumGewijzigd(Datum geboorteDatumGewijzigd)
	{
		this.geboorteDatumGewijzigd = geboorteDatumGewijzigd;
	}

	public Geslacht getGeslachtGewijzigd()
	{
		return geslachtGewijzigd;
	}

	public void setGeslachtGewijzigd(Geslacht geslachtGewijzigd)
	{
		this.geslachtGewijzigd = geslachtGewijzigd;
	}

	public Integer getHuisNummer()
	{
		return huisNummer;
	}

	public void setHuisNummer(Integer huisNummer)
	{
		this.huisNummer = huisNummer;
	}

	public HuisnummerAanduiding getHuisnummerAanduiding()
	{
		return huisnummerAanduiding;
	}

	public void setHuisnummerAanduiding(HuisnummerAanduiding huisnummerAanduiding)
	{
		this.huisnummerAanduiding = huisnummerAanduiding;
	}

	public String getHuisnummerToevoeging()
	{
		return huisnummerToevoeging;
	}

	public void setHuisnummerToevoeging(String huisnummerToevoeging)
	{
		this.huisnummerToevoeging = StringUtil.truncate(huisnummerToevoeging, 5, "");
	}

	public Integer getILTCode()
	{
		return ILTCode;
	}

	public void setILTCode(Integer code)
	{
		ILTCode = code;
	}

	public Date getIngangsDatum()
	{
		return ingangsDatum;
	}

	public void setIngangsDatum(Date ingangsDatum)
	{
		this.ingangsDatum = ingangsDatum;
	}

	public Integer getJarenPraktijkOnderwijs()
	{
		return jarenPraktijkOnderwijs;
	}

	public void setJarenPraktijkOnderwijs(Integer jarenPraktijkOnderwijs)
	{
		this.jarenPraktijkOnderwijs = jarenPraktijkOnderwijs;
	}

	public String getLandCodeVolgensInstellingGewijzigd()
	{
		return landCodeVolgensInstellingGewijzigd;
	}

	public void setLandCodeVolgensInstellingGewijzigd(String landCodeVolgensInstellingGewijzigd)
	{
		this.landCodeVolgensInstellingGewijzigd = landCodeVolgensInstellingGewijzigd;
	}

	public Integer getLeerjaar()
	{
		return leerjaar;
	}

	public void setLeerjaar(Integer leerjaar)
	{
		this.leerjaar = leerjaar;
	}

	public String getLocatieOmschrijving()
	{
		return locatieOmschrijving;
	}

	public void setLocatieOmschrijving(String locatieOmschrijving)
	{
		this.locatieOmschrijving = StringUtil.truncate(locatieOmschrijving, 35, "");
	}

	public String getPlaatsnaam()
	{
		return plaatsnaam;
	}

	public void setPlaatsnaam(String plaatsnaam)
	{
		this.plaatsnaam = StringUtil.truncate(plaatsnaam, 24, "");
	}

	public String getPostcode()
	{
		return postcode;
	}

	public void setPostcode(String postcode)
	{
		String postcodeZonderSpaties = (postcode != null) ? postcode.replaceAll(" ", "") : postcode;
		Asserts.assertMaxLength("postcode", postcodeZonderSpaties, 6);
		this.postcode = postcodeZonderSpaties;
	}

	public String getPostcodeVolgensInstellingGewijzigd()
	{
		return postcodeVolgensInstellingGewijzigd;
	}

	public void setPostcodeVolgensInstellingGewijzigd(String postcode)
	{
		String postcodeZonderSpaties = (postcode != null) ? postcode.replaceAll(" ", "") : postcode;
		Asserts.assertMaxLength("postcode", postcodeZonderSpaties, 6);
		this.postcodeVolgensInstellingGewijzigd = postcodeZonderSpaties;
	}

	public String getSofinummerAchterhaald()
	{
		return sofinummerAchterhaald;
	}

	public void setSofinummerAchterhaald(String sofinummerAchterhaald)
	{
		this.sofinummerAchterhaald = sofinummerAchterhaald;
	}

	public String getStraatNaam()
	{
		return straatNaam;
	}

	public void setStraatNaam(String straatNaam)
	{
		this.straatNaam = StringUtil.truncate(straatNaam, 24, "");
	}

	public String getVoorvoegsel()
	{
		return voorvoegsel;
	}

	public void setVoorvoegsel(String voorvoegsel)
	{
		this.voorvoegsel = StringUtil.truncate(voorvoegsel, 10, "");
	}

	@Override
	public BronBatchVOInschrijvingen getBatch()
	{
		return (BronBatchVOInschrijvingen) super.getBatch();
	}

	@Override
	public List<BronMeldingOnderdeel> getBronMeldingOnderdelen()
	{
		List<BronMeldingOnderdeel> onderdelen = new ArrayList<BronMeldingOnderdeel>();
		onderdelen.add(BronMeldingOnderdeel.VOInschrijving);
		return onderdelen;
	}

	@Override
	public boolean isVerwijderd()
	{
		return verwijderd;
	}

	@Override
	public void setVerwijderd(boolean verwijderd)
	{
		this.verwijderd = verwijderd;
	}

	@Override
	public void voegOpnieuwToeAanWachtrij()
	{
		BronInschrijvingsgegevensVOMelding meldingInWachtrij = findMeldingInWachtrij();
		if (meldingInWachtrij == null)
		{
			// er staat niets in de wachtrij te wachten, dus kan de foutieve melding
			// gewoon kopieren.
			kopieerMelding();
		}
		else
		{
			// er staat wel iets in de wachtrij, dus we moeten de afgekeurde melding
			// consolideren met de gegevens in de wachtrij.
			consolideerMeldingMet(meldingInWachtrij);
		}
	}

	private BronInschrijvingsgegevensVOMelding findMeldingInWachtrij()
	{
		TimeUtil timeutil = TimeUtil.getInstance();
		BronMeldingZoekFilter filter = new BronMeldingZoekFilter(getVerbintenis());
		filter.setAuthorizationContext(new OrganisatieEenheidLocatieAuthorizationContext(
			new AlwaysGrantedSecurityCheck()));
		filter.setDeelnemer(getDeelnemer());
		filter.setMeldingStatus(BronMeldingStatus.WACHTRIJ);
		filter.setVoMeldingSoort(getSoort());

		BronDataAccessHelper wachtrij = DataAccessRegistry.getHelper(BronDataAccessHelper.class);
		List<BronInschrijvingsgegevensVOMelding> meldingenInWachtrij =
			wachtrij.getBronVoMeldingen(filter, BronInschrijvingsgegevensVOMelding.class);
		for (BronInschrijvingsgegevensVOMelding meldingInWachtrij : meldingenInWachtrij)
		{
			if (timeutil.datesEqual(getIngangsDatum(), meldingInWachtrij.getIngangsDatum()))
			{
				return meldingInWachtrij;
			}
		}
		return null;
	}

	private void kopieerMelding()
	{
		HibernateObjectCopyManager copymanager = new HibernateObjectCopyManager(getClass());

		BronInschrijvingsgegevensVOMelding copy = copymanager.copyObject(this);
		copy.setId(null);
		copy.setBatch(null);
		copy.setBronMeldingStatus(BronMeldingStatus.WACHTRIJ);
		copy.setTerugkoppelmelding(null);

		String reden = getRedenAfkeur();
		copy.addReden(reden);
		copy.ververs(null);
		copy.save();
	}

	private void consolideerMeldingMet(BronInschrijvingsgegevensVOMelding meldingInWachtrij)
	{
		switch (getSoortMutatie())
		{
			case Toevoeging:
				bepaalMutatieSoortNaAfkeuringToevoeging(meldingInWachtrij);
				break;
			case Aanpassing:
				// een afgekeurde aanpassing neemt de bestaande mutatie in de wachtrij
				// over
				break;
			case Uitschrijving:
				bepaalMutatieSoortNaAfkeuringUitschrijving(meldingInWachtrij);
				break;
			case Verwijdering:
				bepaalMutatieSoortNaAfkeuringVerwijdering(meldingInWachtrij);
				break;
		}
		if (!meldingInWachtrij.isVerwijderd())
		{
			meldingInWachtrij.addReden(getRedenAfkeur());
			meldingInWachtrij.ververs(null);
			meldingInWachtrij.saveOrUpdate();
		}
	}

	private void bepaalMutatieSoortNaAfkeuringToevoeging(
			BronInschrijvingsgegevensVOMelding meldingInWachtrij)
	{
		switch (meldingInWachtrij.getSoortMutatie())
		{
			case Toevoeging:
				// aan de melding in de wachtrij hoeft niets meer veranderd te worden
				break;
			case Aanpassing:
				// Als de melding in de wachtrij een aanpassing is,
				// terwijl de toevoeging afgekeurd is, dan is de melding
				// nog steeds niet bekend binnen BRON, en moet de mutatie
				// een toevoeging worden.
				meldingInWachtrij.setSoortMutatie(Toevoeging);
				break;
			case Uitschrijving:
				// Als de melding in de wachtrij een uitschrijving is,
				// moet de afgekeurde toevoeging er naast aangemaakt worden
				kopieerMelding();
				break;
			case Verwijdering:
				// als de melding uit bron verwijderd moet worden terwijl
				// de toevoeging afgekeurd is, stuur dan de melding
				// helemaal niet meer naar BRON.
				meldingInWachtrij.setVerwijderd(true);
				meldingInWachtrij.delete();
				break;
		}
	}

	private void bepaalMutatieSoortNaAfkeuringUitschrijving(
			BronInschrijvingsgegevensVOMelding meldingInWachtrij)
	{
		switch (meldingInWachtrij.getSoortMutatie())
		{
			case Toevoeging:
				// als de verbintenis nog steeds beeindigd is, de uitschrijving opnieuw
				// sturen, en laat de bestaande melding in de wachtrij staan
				if (getVerbintenis().getStatus() == Beeindigd)
				{
					kopieerMelding();
				}
				break;
			case Aanpassing:
				// als de verbintenis nog steeds beeindigd is, de uitschrijving opnieuw
				// sturen, en laat de bestaande melding in de wachtrij staan
				if (getVerbintenis().getStatus() == Beeindigd)
				{
					kopieerMelding();
				}
				break;
			case Uitschrijving:
				// aan de melding in de wachtrij hoeft niets meer veranderd te worden
				break;
			case Verwijdering:
				// aan de melding in de wachtrij hoeft niets meer veranderd te worden
				break;
		}
	}

	private void bepaalMutatieSoortNaAfkeuringVerwijdering(
			BronInschrijvingsgegevensVOMelding meldingInWachtrij)
	{
		// Als de verbintenis nog steeds niet BRON communiceerbaar is, nogmaals proberen
		// te verwijderen, in alle andere gevallen de bestaande mutatie gebruiken
		if (!getVerbintenis().isBronCommuniceerbaar())
		{
			meldingInWachtrij.setSoortMutatie(Verwijdering);
		}
	}

	private String getRedenAfkeur()
	{
		Integer batch = getBatch().getBatchNummer();
		Integer melding = getMeldingnummer();
		String reden =
			String.format("Afkeuring door BRON van %s melding %d in batch %03d", getSoortMutatie(),
				melding, batch);
		return reden;
	}

	@Override
	public void vulSleutelgegevens()
	{
		super.vulSleutelgegevens();

		BronEduArteModel model = new BronEduArteModel();
		Deelnemer deelnemer = getDeelnemer();
		if (model.woontInNederland(deelnemer))
		{
			setPostcode(model.getPostcode(deelnemer));
		}
		else if (!Land.CODE_NEDERLAND.equals(model.getLand(deelnemer)))
		{
			setCodeLandAdres(model.getLand(deelnemer));
		}
	}

	public void ververs(Plaatsing plaatsing)
	{
		if (getSoort() == VoMeldingSoort.I)
		{
			vulPersonalia();
			vulInschrijvingsgegevens(plaatsing);
			bepaalRelevantieVoorBekostiging();
		}
	}

	private void bepaalRelevantieVoorBekostiging()
	{
		int startJaar = Schooljaar.huidigSchooljaar().getStartJaar();
		Date peildatumVO = TimeUtil.getInstance().getFirstDayOfMonth(startJaar, Calendar.OCTOBER);
		setBekostigingsRelevant(getIngangsDatum() != null && getIngangsDatum().before(peildatumVO));
	}

	private void vulPersonalia()
	{
		// De extra persoonsgegevens mogen alleen maar gevuld zijn als bijde
		// persoonsgebondennummers leeg zijn
		if (getSofiNummer() == null && getOnderwijsNummer() == null
			&& getSoortMutatie() == Toevoeging)
		{
			Deelnemer deelnemer = getDeelnemer();
			BronEduArteModel model = new BronEduArteModel();

			setAchternaam(model.getAchternaam(deelnemer));
			setVoorvoegsel(model.getVoorvoegsel(deelnemer));
			setAlleVoornamen(model.getAlleVoornamen(deelnemer));
			if (model.woontInNederland(deelnemer))
			{
				setStraatNaam(model.getStraatnaam(deelnemer));
				setHuisNummer(model.getHuisnummer(deelnemer));
				setHuisnummerToevoeging(model.getHuisnummerToevoeging(deelnemer));
				setPlaatsnaam(model.getPlaatsnaam(deelnemer));
			}
			else if (model.getLand(deelnemer) != null
				&& !model.getLand(deelnemer).equals(Land.CODE_NEDERLAND))
			{
				setAdresregelBuitenland1(model.getAdresregelBuitenland1(deelnemer));
				setAdresregelBuitenland2(model.getAdresregelBuitenland2(deelnemer));
				setAdresregelBuitenland3(model.getAdresregelBuitenland3(deelnemer));
			}
		}
		else
		{
			// maak de gegevens leeg wanneer er wel een PGN is
			setAchternaam(null);
			setVoorvoegsel(null);
			setAlleVoornamen(null);
			setStraatNaam(null);
			setHuisNummer(null);
			setHuisnummerToevoeging(null);
			setPlaatsnaam(null);
			setAdresregelBuitenland1(null);
			setAdresregelBuitenland2(null);
			setAdresregelBuitenland3(null);
		}
	}

	private void vulInschrijvingsgegevens(Plaatsing plaatsing)
	{
		BronEduArteModel model = new BronEduArteModel();
		Verbintenis verbintenis = getVerbintenis();
		switch (getSoortMutatie())
		{
			case Toevoeging:
				setILTCode(Integer.valueOf(verbintenis.getExterneCode()));
				break;
			case Aanpassing:
				setILTCode(Integer.valueOf(verbintenis.getExterneCode()));
				break;
			case Uitschrijving:
			case Verwijdering:
				setEindDatum(null);
				setILTCode(null);
				setCumiCategorie(null);
				setCumiRatio(null);
				setLeerjaar(null);
				setJarenPraktijkOnderwijs(null);
				return;
		}
		if (plaatsing == null)
			plaatsing = getPlaatsingBijIngangsdatum();
		if (plaatsing != null)
		{
			if (isPraktijkOnderwijs())
				setJarenPraktijkOnderwijs(plaatsing.getJarenPraktijkonderwijs());
			else
				setLeerjaar(plaatsing.getLeerjaar());
		}
		setCumiCategorie(model.getCumiCategorie(verbintenis.getDeelnemer()));
		setCumiRatio(model.getCumiRatio(verbintenis.getDeelnemer()));
	}

	private Plaatsing getPlaatsingBijIngangsdatum()
	{
		Verbintenis verbintenis = getVerbintenis();
		List<Plaatsing> plaatsingen = verbintenis.getPlaatsingen();
		for (Plaatsing plaatsing : plaatsingen)
		{
			if (plaatsing.isActief(getIngangsDatum()))
			{
				return plaatsing;
			}
		}
		return null;
	}

	/**
	 * Bepaalt of de verbintenis gekoppeld is aan een praktijkonderwijs opleiding.
	 */
	protected boolean isPraktijkOnderwijs()
	{
		return "0090".equals(getVerbintenis().getExterneCode());
	}

	@Override
	public String toString()
	{
		return String.format("%s %s", super.toString(), getSoortMutatie() != null
			? getSoortMutatie().getBRONString() : "-");
	}
}
