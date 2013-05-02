package nl.topicus.eduarte.krd.bron;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import nl.topicus.cobra.converters.PostcodeConverter;
import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.types.personalia.Geslacht;
import nl.topicus.cobra.util.StringUtil;
import nl.topicus.cobra.util.TimeUtil;
import nl.topicus.eduarte.dao.helpers.VerbintenisDataAccessHelper;
import nl.topicus.eduarte.entities.adres.Adres;
import nl.topicus.eduarte.entities.bpv.BPVBedrijfsgegeven;
import nl.topicus.eduarte.entities.bpv.BPVInschrijving;
import nl.topicus.eduarte.entities.bpv.BPVInschrijving.BPVStatus;
import nl.topicus.eduarte.entities.examen.Examendeelname;
import nl.topicus.eduarte.entities.inschrijving.Bekostigingsperiode;
import nl.topicus.eduarte.entities.inschrijving.IVooropleiding;
import nl.topicus.eduarte.entities.inschrijving.OnderwijsproductAfname;
import nl.topicus.eduarte.entities.inschrijving.Plaatsing;
import nl.topicus.eduarte.entities.inschrijving.RedenUitschrijving;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.entities.inschrijving.Vooropleiding;
import nl.topicus.eduarte.entities.inschrijving.SoortVooropleiding.SoortOnderwijs;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis.VerbintenisStatus;
import nl.topicus.eduarte.entities.landelijk.Land;
import nl.topicus.eduarte.entities.landelijk.Nationaliteit;
import nl.topicus.eduarte.entities.onderwijsproduct.Onderwijsproduct;
import nl.topicus.eduarte.entities.onderwijsproduct.OnderwijsproductAfnameContext;
import nl.topicus.eduarte.entities.organisatie.Brin;
import nl.topicus.eduarte.entities.organisatie.Locatie;
import nl.topicus.eduarte.entities.organisatie.LocatieAdres;
import nl.topicus.eduarte.entities.personen.Deelnemer;
import nl.topicus.eduarte.entities.personen.Persoon;
import nl.topicus.eduarte.entities.personen.PersoonAdres;
import nl.topicus.eduarte.entities.personen.Persoon.ToepassingGeboortedatum;
import nl.topicus.eduarte.entities.resultaatstructuur.Resultaat;
import nl.topicus.eduarte.entities.resultaatstructuur.Resultaatstructuur;
import nl.topicus.eduarte.entities.resultaatstructuur.Schaalwaarde;
import nl.topicus.eduarte.entities.resultaatstructuur.Toets;
import nl.topicus.eduarte.entities.resultaatstructuur.Toets.SoortToets;
import nl.topicus.eduarte.entities.taxonomie.MBOLeerweg;
import nl.topicus.eduarte.zoekfilters.OrganisatieEenheidLocatieAuthorizationContext;
import nl.topicus.eduarte.zoekfilters.VerbintenisZoekFilter;
import nl.topicus.onderwijs.duo.bron.BronOnderwijssoort;
import nl.topicus.onderwijs.duo.bron.bve.waardelijsten.HoogsteVooropleiding;
import nl.topicus.onderwijs.duo.bron.bve.waardelijsten.Intensiteit;
import nl.topicus.onderwijs.duo.bron.bve.waardelijsten.Leerweg;
import nl.topicus.onderwijs.duo.bron.data.types.Datum;
import nl.topicus.onderwijs.duo.bron.data.waardelijsten.HuisnummerAanduiding;
import nl.topicus.onderwijs.duo.bron.data.waardelijsten.examen.BeoordelingSchoolExamen;
import nl.topicus.onderwijs.duo.bron.data.waardelijsten.examen.BeoordelingWerkstuk;
import nl.topicus.onderwijs.duo.bron.vo.waardelijsten.CumiCategorie;
import nl.topicus.onderwijs.duo.bron.vo.waardelijsten.CumiRatio;

import org.apache.wicket.security.checks.AlwaysGrantedSecurityCheck;

/**
 * Model voor BRON om over het EduArte domein te kunnen redeneren, zoals het bepalen of
 * een <tt>adres</tt> wel bij een <tt>Deelnemer</tt> hoort (in plaats van een
 * <tt>Medewerker</tt>).
 * 
 * @author dashorst
 */
public class BronEduArteModel
{
	public Collection<Verbintenis> getVerbintenissen(Object entity)
	{
		if (entity instanceof Verbintenis)
		{
			return Arrays.asList((Verbintenis) entity);
		}
		if (entity instanceof Plaatsing)
		{
			return Arrays.asList(((Plaatsing) entity).getVerbintenis());
		}
		if (entity instanceof Bekostigingsperiode)
		{
			return Arrays.asList(((Bekostigingsperiode) entity).getVerbintenis());
		}
		if (entity instanceof BPVBedrijfsgegeven)
		{
			BPVBedrijfsgegeven bedrijf = (BPVBedrijfsgegeven) entity;
			VerbintenisDataAccessHelper verbintenisHelper =
				DataAccessRegistry.getHelper(VerbintenisDataAccessHelper.class);

			VerbintenisZoekFilter filter = new VerbintenisZoekFilter();

			List<BPVStatus> statussen = BPVStatus.getNietBronCommuniceerbareStatussen();
			statussen.add(BPVStatus.Beëindigd);

			filter.setBpvStatusOngelijkAan(statussen);
			filter.setBpvBedrijf(bedrijf.getExterneOrganisatie());

			List<VerbintenisStatus> nietBronCommuniceerbareStatussen =
				VerbintenisStatus.getNietBronCommuniceerbareStatussen();

			// beeindigde verbintenissen moeten niet aangepast worden
			nietBronCommuniceerbareStatussen.add(VerbintenisStatus.Beeindigd);

			filter.setVerbintenisStatusOngelijkAan(nietBronCommuniceerbareStatussen);
			filter.setAuthorizationContext(new OrganisatieEenheidLocatieAuthorizationContext(
				new AlwaysGrantedSecurityCheck()));
			List<Verbintenis> verbintenissen = verbintenisHelper.list(filter);
			return verbintenissen;
		}
		if (entity instanceof BPVInschrijving)
		{
			return Arrays.asList(((BPVInschrijving) entity).getVerbintenis());
		}
		if (entity instanceof Examendeelname)
		{
			return Arrays.asList(((Examendeelname) entity).getVerbintenis());
		}
		if (entity instanceof OnderwijsproductAfname)
		{
			return getVerbintenissen((OnderwijsproductAfname) entity);
		}
		if (entity instanceof OnderwijsproductAfnameContext)
		{
			return Arrays.asList(((OnderwijsproductAfnameContext) entity).getVerbintenis());
		}
		if (entity instanceof Vooropleiding)
		{
			return getVerbintenissen((Vooropleiding) entity);
		}
		if (entity instanceof Resultaat)
		{
			return getVerbintenissen((Resultaat) entity);
		}
		if (entity instanceof Collection< ? >)
		{
			return getVerbintenissen((Collection< ? >) entity);
		}
		return Collections.emptyList();
	}

	private Collection<Verbintenis> getVerbintenissen(OnderwijsproductAfname onderwijsproductAfname)
	{
		HashSet<Verbintenis> verbintenissen = new HashSet<Verbintenis>();
		for (OnderwijsproductAfnameContext context : onderwijsproductAfname.getAfnameContexten())
		{
			verbintenissen.add(context.getVerbintenis());
		}
		return verbintenissen;
	}

	private Collection<Verbintenis> getVerbintenissen(Vooropleiding vooropleiding)
	{
		HashSet<Verbintenis> verbintenissen = new HashSet<Verbintenis>();
		for (Verbintenis verbintenis : vooropleiding.getDeelnemer().getVerbintenissen())
		{
			if (vooropleiding.equals(verbintenis.getRelevanteVooropleiding()))
			{
				verbintenissen.add(verbintenis);
			}
		}
		return verbintenissen;
	}

	private Collection<Verbintenis> getVerbintenissen(Resultaat resultaat)
	{
		HashSet<Verbintenis> verbintenissen = new HashSet<Verbintenis>();
		Deelnemer deelnemer = resultaat.getDeelnemer();
		Toets toets = resultaat.getToets();
		if (toets.getSoort().isNT2VaardigheidToets()
			|| (toets.getParent() != null && toets.getParent().getSoort().isNT2VaardigheidToets()))
		{
			Resultaatstructuur structuur = toets.getResultaatstructuur();
			Onderwijsproduct onderwijsproduct = structuur.getOnderwijsproduct();
			for (Verbintenis verbintenis : deelnemer.getVerbintenissen())
			{
				for (OnderwijsproductAfname afname : verbintenis.getOnderwijsproductAfnames())
				{
					if (afname.getOnderwijsproduct().equals(onderwijsproduct))
						verbintenissen.add(verbintenis);
				}
			}
		}
		return verbintenissen;
	}

	private Collection<Verbintenis> getVerbintenissen(Collection< ? > collection)
	{
		HashSet<Verbintenis> verbintenissen = new HashSet<Verbintenis>();
		for (Object object : collection)
		{
			verbintenissen.addAll(getVerbintenissen(object));
		}
		return verbintenissen;
	}

	public boolean isInBronScope(Adres adres)
	{
		if (adres == null)
			return false;

		List<PersoonAdres> persoonAdressen = adres.getPersoonAdressen();
		if (persoonAdressen == null || persoonAdressen.isEmpty())
		{
			return false;
		}

		for (PersoonAdres persoonAdres : persoonAdressen)
		{
			if (isInBronScope(persoonAdres.getPersoon()))
				return true;
		}
		return false;
	}

	public boolean isInBronScope(Persoon persoon)
	{
		if (persoon == null)
		{
			return false;
		}

		Deelnemer deelnemer = persoon.getDeelnemer();
		if (deelnemer == null)
		{
			return false;
		}
		return isInBronScope(deelnemer);
	}

	public boolean isInBronScope(Deelnemer deelnemer)
	{
		List<Verbintenis> verbintenissen = deelnemer.getVerbintenissen();
		if (verbintenissen == null)
		{
			return false;
		}

		if (verbintenissen.isEmpty())
		{
			return false;
		}

		for (Verbintenis verbintenis : verbintenissen)
		{
			if (verbintenis.isBronCommuniceerbaar())
			{
				return true;
			}
		}
		return false;
	}

	public boolean isInBronScopeEnBVE(Deelnemer deelnemer)
	{
		List<Verbintenis> verbintenissen = deelnemer.getVerbintenissen();
		if (verbintenissen == null)
		{
			return false;
		}

		if (verbintenissen.isEmpty())
		{
			return false;
		}

		for (Verbintenis verbintenis : verbintenissen)
		{
			if (verbintenis.isBronCommuniceerbaar()
				&& (verbintenis.isVAVOVerbintenis() || verbintenis.isEducatieVerbintenis() || verbintenis
					.isBOVerbintenis()))
			{
				return true;
			}
		}
		return false;
	}

	public boolean isInBronScopeEnVO(Deelnemer deelnemer)
	{
		List<Verbintenis> verbintenissen = deelnemer.getVerbintenissen();
		if (verbintenissen == null)
		{
			return false;
		}

		if (verbintenissen.isEmpty())
		{
			return false;
		}

		for (Verbintenis verbintenis : verbintenissen)
		{
			if (verbintenis.isBronCommuniceerbaar() && verbintenis.isVOVerbintenis())
			{
				return true;
			}
		}
		return false;
	}

	public boolean heeftSofinummer(Deelnemer deelnemer)
	{
		return deelnemer.getPersoon().getBsn() != null;
	}

	public String getSofinummer(Deelnemer deelnemer)
	{
		Long bsn = deelnemer.getPersoon().getBsn();
		if (bsn == null)
			return null;

		return String.format("%09d", bsn);
	}

	public String getOnderwijsnummer(Deelnemer deelnemer)
	{
		Long onderwijsnummer = deelnemer.getOnderwijsnummer();
		if (onderwijsnummer == null)
			return null;
		return String.format("%09d", onderwijsnummer);
	}

	public String getPostcode(Deelnemer deelnemer)
	{
		Adres woonadres = getWoonadres(deelnemer);
		if (woonadres != null)
			return getPostcode(woonadres);
		else
			return null;
	}

	public String getLand(Deelnemer deelnemer)
	{
		Adres woonAdres = getWoonadres(deelnemer);
		if (woonAdres != null && woonAdres.getLand() != null)
		{
			return woonAdres.getLand().getCode();
		}
		return null;
	}

	public boolean woontInNederland(Deelnemer deelnemer)
	{
		Adres woonadres = getWoonadres(deelnemer);
		if (woonadres == null)
			return false;
		return !woonadres.isBuitenlandsAdres();
	}

	public String getAchternaam(Deelnemer deelnemer)
	{
		return deelnemer.getPersoon().getOfficieleAchternaam();
	}

	public String getAdresregelBuitenland1(Deelnemer deelnemer)
	{
		if (woontInNederland(deelnemer))
		{
			return null;
		}
		return getWoonadres(deelnemer).getStraatHuisnummer();
	}

	public String getAdresregelBuitenland2(Deelnemer deelnemer)
	{
		if (woontInNederland(deelnemer))
		{
			return null;
		}
		return getWoonadres(deelnemer).getPostcodePlaats();
	}

	public String getAdresregelBuitenland3(Deelnemer deelnemer)
	{
		if (woontInNederland(deelnemer))
		{
			return null;
		}
		// KRD heeft geen 3e adresregel, wij hebben nl. straat + huisnr, postcode +
		// woonplaats
		return null;
	}

	public Adres getWoonadres(Deelnemer deelnemer)
	{
		TimeUtil timeUtil = TimeUtil.getInstance();
		Date peildatum = timeUtil.currentDate();
		PersoonAdres woonadres = deelnemer.getPersoon().getFysiekAdres(peildatum);
		if (woonadres == null)
		{
			woonadres = deelnemer.getPersoon().getLaatsteAdres();
			if (woonadres == null)
			{
				return null;
			}
		}
		return woonadres.getAdres();
	}

	public boolean heeftWoonadres(Deelnemer deelnemer)
	{
		return getWoonadres(deelnemer) != null;
	}

	public String getAlleVoornamen(Deelnemer deelnemer)
	{
		return deelnemer.getPersoon().getVoornamen();
	}

	public Integer getHuisnummer(Deelnemer deelnemer)
	{
		if (!woontInNederland(deelnemer))
		{
			return null;
		}

		return getHuisnummer(getWoonadres(deelnemer));
	}

	public HuisnummerAanduiding getHuisnummerAanduiding(
			@SuppressWarnings("unused") Deelnemer deelnemer)
	{
		return null;
	}

	public String getHuisnummerToevoeging(Deelnemer deelnemer)
	{
		if (!woontInNederland(deelnemer))
		{
			return null;
		}
		String toevoeging = getWoonadres(deelnemer).getHuisnummerToevoeging();
		if (StringUtil.isEmpty(toevoeging))
		{
			return null;
		}
		return toevoeging.trim();
	}

	public String getLocatieOmschrijving(@SuppressWarnings("unused") Deelnemer deelnemer)
	{
		return null;
	}

	public String getGeboorteland(Deelnemer deelnemer)
	{
		Land land = deelnemer.getPersoon().getGeboorteland();
		if (land != null)
		{
			return land.getCode();
		}
		return null;
	}

	public String getNationaliteit1(Deelnemer deelnemer)
	{
		Nationaliteit nationaliteit1 = deelnemer.getPersoon().getNationaliteit1();
		if (nationaliteit1 != null)
		{
			return nationaliteit1.getCode();
		}
		return null;
	}

	public String getNationaliteit2(Deelnemer deelnemer)
	{
		Nationaliteit nationaliteit2 = deelnemer.getPersoon().getNationaliteit2();
		if (nationaliteit2 != null)
		{
			return nationaliteit2.getCode();
		}
		return null;
	}

	public CumiCategorie getCumiCategorie(Deelnemer deelnemer)
	{
		return deelnemer.getPersoon().getCumiCategorie();
	}

	public CumiRatio getCumiRatio(Deelnemer deelnemer)
	{
		return deelnemer.getPersoon().getCumiRatio();
	}

	public String getPlaatsnaam(Deelnemer deelnemer)
	{
		if (!woontInNederland(deelnemer))
		{
			return null;
		}
		return getWoonadres(deelnemer).getPlaats();
	}

	public String getStraatnaam(Deelnemer deelnemer)
	{
		if (!woontInNederland(deelnemer))
		{
			return null;
		}
		return getWoonadres(deelnemer).getStraat();
	}

	public String getVoorvoegsel(Deelnemer deelnemer)
	{
		return deelnemer.getPersoon().getOfficieleVoorvoegsel();
	}

	public Datum getGeboortedatum(Deelnemer deelnemer)
	{
		Datum geboortedatum = null;

		if (deelnemer.getPersoon().getToepassingGeboortedatum() != null)
		{
			geboortedatum =
				getDatumMetToepassing(deelnemer.getPersoon().getGeboortedatum(), deelnemer
					.getPersoon().getToepassingGeboortedatum());
		}
		else
		{
			geboortedatum = Datum.valueOf(deelnemer.getPersoon().getGeboortedatum());
		}

		return geboortedatum;
	}

	public Datum getVorigeGeboorteDatum(BronEntiteitChanges< ? > changes, Persoon persoon)
	{
		Date vorigedate = null;
		Datum vorigedatum = null;

		if (changes.getPreviousValue(persoon, "geboortedatum") != null)
		{
			vorigedate = changes.getPreviousValue(persoon, "geboortedatum");
		}
		else
		{
			vorigedate = persoon.getGeboortedatum();
		}

		if (changes.getPreviousValue(persoon, "toepassingGeboortedatum") != null)
		{
			vorigedatum =
				getDatumMetToepassing(vorigedate, (ToepassingGeboortedatum) changes
					.getPreviousValue(persoon, "toepassingGeboortedatum"));
		}
		else
		{
			vorigedatum = Datum.valueOf(vorigedate);
		}
		return vorigedatum;
	}

	private Datum getDatumMetToepassing(Date date, ToepassingGeboortedatum toepassing)
	{
		Datum geboortedatum = null;

		int jaar = TimeUtil.getInstance().getYear(date);

		switch (toepassing)
		{
			case Geboortejaar:
				geboortedatum = Datum.valueOf(jaar, 0, 0);
				break;
			case GeboortemaandEnJaar:
				int maand = TimeUtil.getInstance().getMonth(date) + 1;
				geboortedatum = Datum.valueOf(jaar, maand, 0);
				break;
		}

		return geboortedatum;
	}

	public Geslacht getGeslacht(Deelnemer deelnemer)
	{
		return deelnemer.getPersoon().getGeslacht();
	}

	public String getLeerlingnummer(Deelnemer deelnemer)
	{
		return String.valueOf(deelnemer.getDeelnemernummer());
	}

	/**
	 * @param verbintenis
	 * @return de hoogste vooropleiding in relatie tot de gegeven verbintenis. Dit is de
	 *         relevante vooropleiding.
	 */
	public HoogsteVooropleiding getHoogsteVooropleiding(Verbintenis verbintenis)
	{
		HoogsteVooropleiding hoogste = null;
		IVooropleiding vooropleiding = verbintenis.getRelevanteVooropleiding();
		if (vooropleiding != null && vooropleiding.getSoortOnderwijs() != null)
		{
			SoortOnderwijs soort = vooropleiding.getSoortOnderwijs();

			for (HoogsteVooropleiding bronOpleiding : HoogsteVooropleiding.values())
			{
				if (bronOpleiding.getBRONString().equals(soort.getCode()))
				{
					hoogste = bronOpleiding;
					break;
				}
			}
		}
		return hoogste;
	}

	public Intensiteit getIntensiteit(Verbintenis verbintenis)
	{
		if (verbintenis.getIntensiteit() == null)
		{
			return null;
		}
		String name = verbintenis.getIntensiteit().name();
		return Intensiteit.valueOf(name);
	}

	public String getLaatsteVooropleiding(Deelnemer deelnemer)
	{
		List<Vooropleiding> vooropleidingen = deelnemer.getVooropleidingen();
		if (!vooropleidingen.isEmpty())
		{
			Vooropleiding laatste =
				Collections.max(vooropleidingen, new Comparator<Vooropleiding>()
				{
					@Override
					public int compare(Vooropleiding o1, Vooropleiding o2)
					{
						return o1.getEinddatumNotNull().compareTo(o2.getEinddatumNotNull());
					}
				});
			return laatste == null ? null : laatste.getSoortVooropleiding().getNaam();
		}
		return null;
	}

	public Leerweg getLeerweg(Verbintenis verbintenis)
	{
		MBOLeerweg mboLeerweg = verbintenis.getOpleiding().getLeerweg();
		if (mboLeerweg != null)
		{
			for (Leerweg leerweg : Leerweg.values())
			{
				if (leerweg.getBRONString().equalsIgnoreCase(mboLeerweg.name()))
				{
					return leerweg;
				}
			}
		}
		return null;
	}

	public Integer getLocatie(Verbintenis verbintenis)
	{
		Locatie locatie = verbintenis.getLocatie();
		if (locatie == null)
			return null;
		LocatieAdres fysiekadres = locatie.getFysiekAdres();
		if (fysiekadres == null)
			return null;
		Adres adres = fysiekadres.getAdres();
		if (adres == null)
			return null;

		String postcode = getPostcode(adres);
		if (StringUtil.checkMatchesRegExp("postcode", postcode, "\\d{4}[a-zA-Z]{2}"))
		{
			return Integer.valueOf(postcode.substring(0, 4));
		}
		return null;
	}

	public String getOpleidingscode(Verbintenis verbintenis)
	{
		return verbintenis.getExterneCode();
	}

	public String getRedenUitstroom(Verbintenis verbintenis)
	{
		RedenUitschrijving redenUitschrijving = verbintenis.getRedenUitschrijving();
		return redenUitschrijving == null ? null : redenUitschrijving.getCode();
	}

	public boolean heeftOnderwijsnummer(Deelnemer deelnemer)
	{
		return deelnemer.getOnderwijsnummer() != null;
	}

	public boolean isNietIdentificeerbaar(Deelnemer deelnemer)
	{
		return !isIdentificeerbaar(deelnemer);
	}

	public boolean isIdentificeerbaar(Deelnemer deelnemer)
	{
		return (heeftSofinummer(deelnemer) || heeftOnderwijsnummer(deelnemer));
	}

	public Boolean getIndicatieNieuwkomer(Deelnemer deelnemer)
	{
		return deelnemer.getPersoon().isNieuwkomer();
	}

	public Integer getVestigingsVolgNummer(Verbintenis verbintenis)
	{
		if (verbintenis.getLocatie() != null && verbintenis.getLocatie().getBrincode() != null)
			return verbintenis.getLocatie().getBrincode().getVestigingsVolgnummer();
		return 0;
	}

	public Integer getLeerjaar(Deelnemer deelnemer)
	{
		if (deelnemer.getVerbintenissen().size() > 0)
		{
			Verbintenis verbintenis = deelnemer.getVerbintenissen().get(0);
			if (verbintenis.getPlaatsingen().size() > 0)
				return verbintenis.getPlaatsingen().get(0).getLeerjaar();

		}
		return null;
	}

	public Integer getCijfer(Resultaat resultaat)
	{
		if (resultaat == null || resultaat.getCijfer() == null)
			return null;
		if (resultaat.getToets().getSoort() == SoortToets.Schoolexamen
			|| resultaat.getToets().getSoort() == SoortToets.CentraalExamen)
			// SE en CE altijd met 1 decimaal
			return (resultaat.getCijfer().multiply(BigDecimal.TEN)).intValue();
		else if (resultaat.getToets().getSchaal().getAantalDecimalen() > 0)
			return (resultaat.getCijfer().multiply(BigDecimal.TEN)).intValue();
		else
			return resultaat.getCijfer().intValue();
	}

	public BeoordelingSchoolExamen getBeoordeling(Schaalwaarde waarde)
	{
		if (waarde == null || StringUtil.isEmpty(waarde.getExterneWaarde()))
			return null;
		return BeoordelingSchoolExamen.valueOf(waarde.getExterneWaarde().charAt(0));
	}

	public BeoordelingWerkstuk getBeoordelingWerkstuk(Resultaat resultaat)
	{
		if (resultaat == null || resultaat.getWaarde() == null
			|| StringUtil.isEmpty(resultaat.getWaarde().getExterneWaarde()))
			return null;
		String waarde = resultaat.getWaarde().getExterneWaarde();
		return BeoordelingWerkstuk.valueOf(waarde.charAt(0));
	}

	public String getLeerbedrijf(BPVInschrijving inschrijving)
	{
		if (inschrijving != null && inschrijving.getBedrijfsgegeven() != null)
		{
			return inschrijving.getBedrijfsgegeven().getCodeLeerbedrijf();
		}
		return null;
	}

	public BronOnderwijssoort getBronOnderwijssoort(Deelnemer deelnemer)
	{
		for (Verbintenis verbintenis : deelnemer.getVerbintenissen())
		{
			if (verbintenis.isBronCommuniceerbaar())
			{
				if (verbintenis.isVAVOVerbintenis())
					return BronOnderwijssoort.VAVO;
				if (verbintenis.isEducatieVerbintenis())
					return BronOnderwijssoort.EDUCATIE;
				if (verbintenis.isBOVerbintenis())
					return BronOnderwijssoort.BEROEPSONDERWIJS;
				if (verbintenis.isVOVerbintenis())
					return BronOnderwijssoort.VOORTGEZETONDERWIJS;
			}
		}
		return null;
	}

	public BronOnderwijssoort getBronOnderwijssoort(Verbintenis verbintenis)
	{
		if (verbintenis.isBronCommuniceerbaar())
		{
			if (verbintenis.isVAVOVerbintenis())
				return BronOnderwijssoort.VAVO;
			if (verbintenis.isEducatieVerbintenis())
				return BronOnderwijssoort.EDUCATIE;
			if (verbintenis.isBOVerbintenis())
				return BronOnderwijssoort.BEROEPSONDERWIJS;
			if (verbintenis.isVOVerbintenis())
				return BronOnderwijssoort.VOORTGEZETONDERWIJS;
		}
		return null;
	}

	public Verbintenis getBVEVerbintenis(Deelnemer deelnemer)
	{
		for (Verbintenis verbintenis : deelnemer.getVerbintenissen())
		{
			if (verbintenis.isBronCommuniceerbaar()
				&& (verbintenis.isVAVOVerbintenis() || verbintenis.isEducatieVerbintenis() || verbintenis
					.isBOVerbintenis()))
				return verbintenis;
		}
		return null;
	}

	public String getBrinCodeKbb(BPVInschrijving inschrijving)
	{
		BPVBedrijfsgegeven bedrijfsgegeven = inschrijving.getBedrijfsgegeven();
		if (bedrijfsgegeven != null)
		{
			Brin kenniscentrum = bedrijfsgegeven.getKenniscentrum();
			if (kenniscentrum != null)
				return kenniscentrum.getCode();

		}
		return null;
	}

	public String getPostcode(Adres adres)
	{
		if (adres == null || StringUtil.isEmpty(adres.getPostcode()))
			return null;
		PostcodeConverter converter = new PostcodeConverter();
		String postcode = converter.convertToString(adres.getPostcode(), null);
		if (StringUtil.isEmpty(postcode))
			return null;
		if (postcode.contains(" "))
			postcode = postcode.replace(" ", "");

		if (adres.isBuitenlandsAdres())
			// Buitenlandse postcode wil nog wel eens langer zijn
			return StringUtil.truncate(postcode, 6, null);
		else
			return postcode;
	}

	public Integer getHuisnummer(Adres adres)
	{
		if (adres == null || StringUtil.isEmpty(adres.getHuisnummer()))
			return null;

		if (!StringUtil.isNumeric(adres.getHuisnummer()))
			return StringUtil.getFirstNumberSequence(adres.getHuisnummer());
		else
			return Integer.valueOf(adres.getHuisnummer());
	}
}
