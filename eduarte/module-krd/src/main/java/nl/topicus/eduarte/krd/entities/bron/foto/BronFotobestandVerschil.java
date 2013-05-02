package nl.topicus.eduarte.krd.entities.bron.foto;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import nl.topicus.cobra.types.personalia.Geslacht;
import nl.topicus.cobra.util.TimeUtil;
import nl.topicus.eduarte.entities.adres.Adres;
import nl.topicus.eduarte.entities.bpv.BPVInschrijving;
import nl.topicus.eduarte.entities.examen.Examendeelname;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.entities.landelijk.Land;
import nl.topicus.eduarte.entities.organisatie.InstellingEntiteit;
import nl.topicus.eduarte.entities.personen.AbstractRelatie;
import nl.topicus.eduarte.entities.personen.Deelnemer;
import nl.topicus.eduarte.entities.personen.Persoon;
import nl.topicus.eduarte.entities.personen.Relatie;
import nl.topicus.eduarte.entities.personen.RelatieSoort;
import nl.topicus.eduarte.krd.bron.BronEduArteModel;
import nl.topicus.eduarte.krd.entities.bron.foto.bve.BronFotoBOBPVRecord;
import nl.topicus.eduarte.krd.entities.bron.foto.bve.BronFotoBODiplomaKwalificatieRecord;
import nl.topicus.eduarte.krd.entities.bron.foto.bve.BronFotoBOOnderwijsontvangendeRecord;
import nl.topicus.eduarte.krd.entities.bron.foto.bve.BronFotoRecord;
import nl.topicus.eduarte.krd.entities.bron.foto.bve.IBronFotoInschrijvingRecord;
import nl.topicus.eduarte.krd.entities.bron.foto.bve.IBronFotoOnderwijsontvangendeRecord;
import nl.topicus.eduarte.krd.entities.bron.foto.bve.IBronFotoRecord;

import org.apache.wicket.util.lang.PropertyResolver;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Index;

/**
 * Entiteit die een verschil tussen een fotobestand en de EduArte database aangeeft.
 * 
 * @author loite
 */
@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
public class BronFotobestandVerschil extends InstellingEntiteit
{
	private static final long serialVersionUID = 1L;

	public static enum FotoVerschil
	{
		DeelnemerOnderwijsnummerNietGevonden("Onbekend onderwijsnummer",
				"Deelnemer met het aangegeven onderwijsnummer niet gevonden in de database",
				"record.pgn", null),
		DeelnemerBSNNietGevonden("Onbekend BSN",
				"Deelnemer met het aangegeven BSN niet gevonden in de database", "record.pgn", null),
		VerbintenisNietGevonden("Verbintenis niet gevonden",
				"Verbintenis met het aangegeven volgnummer niet gevonden in de database",
				"record.inschrijvingsvolgnummer", null),
		BegindataKomenNietOvereen("Begindatum", "Begindata in foto en database komen niet overeen",
				"record.datumInschrijving", "verbintenis.begindatum"),
		WerkelijkeEinddataKomenNietOvereen("Einddatum",
				"Werkelijke einddata in foto en database komen niet overeen",
				"record.werkelijkeUitschrijfdatum", "verbintenis.einddatum"),
		VerwachteEinddataKomenNietOvereen("Verwachte einddatum",
				"Verwachte einddata in foto en database komen niet overeen",
				"record.geplandeUitschrijfdatum", "verbintenis.geplandeEinddatum"),
		OpleidingenKomenNietOvereen("Opleiding",
				"Opleidingen in foto en database komen niet overeen", "record.codeOpleiding",
				"verbintenis.externeCode"),
		BekostigingOpTeldatumKomenNietOvereen("Bekostiging",
				"Bekostiging op teldatum in foto en database komen niet overeen",
				"record.indicatieBekostigingInschrijvingOpTeldatumInRecordOmschrijving",
				"verbintenisBekostigdOpTeldatumOmschrijving"),
		BekostigingOp1FebKomenNietOvereen("Bekostiging op 1 feb",
				"Bekostiging op 1 februari in foto en database komen niet overeen",
				"record.indicatieBekostigingInschrijvingOp1FebruariOmschrijving",
				"verbintenisBekostigdOp1FebOmschrijving"),
		IndicatieGehandicaptKomenNietOvereen("Indicatie gehandicapt",
				"Indicatie gehandicapt in foto en database komen niet overeen",
				"record.indicatieGehandicaptOmschrijving",
				"verbintenis.indicatieGehandicaptOmschrijving"),
		HoogsteVooropleidingKomenNietOvereen("Hoogste vooropleiding",
				"Hoogste vooropleiding in foto en database komen niet overeen",
				"record.hoogsteVooropleiding", "verbintenis.relevanteVooropleiding.soortOnderwijs"),
		LeerwegKomenNietOvereen("Leerweg", "Leerweg in foto en database komen niet overeen",
				"record.leerweg", "verbintenis.opleiding.leerweg"),
		IntensiteitKomenNietOvereen("Intensiteit",
				"Intensiteit in foto en database komen niet overeen", "record.intensiteit",
				"verbintenis.intensiteit"),
		RedenUitstroomKomenNietOvereen("Reden uitstroom",
				"Reden uitstroom in foto en database komen niet overeen", "record.redenUitstroom",
				"verbintenis.redenUitschrijving.code"),
		BpvNietGevonden("BPV niet gevonden",
				"BPV met het aangegeven volgnummer niet gevonden in de database",
				"bpvRecord.bpvVolgnummer", null),
		BpvNietGevondenInBron("BPV komt niet voor in foto",
				"BPV met het aangegeven volgnummer komt niet voor in de foto", null,
				"bpvInschrijving.volgnummer"),
		BpvAfsluitdatum("BPV afsluitdatum",
				"Afsluitdatum BPV in foto en database komen niet overeen",
				"bpvRecord.afsluitdatumBPV", "bpvInschrijving.afsluitdatum"),
		BpvBegindatum("BPV begindatum", "Begindatum BPV in foto en database komen niet overeen",
				"bpvRecord.begindatumBPV", "bpvInschrijving.begindatum"),
		BpvEinddatum("BPV einddatum",
				"Werkelijke einddatum BPV in foto en database komen niet overeen",
				"bpvRecord.werkelijkeEinddatumBPV", "bpvInschrijving.einddatum"),
		BpvGeplandeEinddatum("BPV geplande einddatum",
				"Geplande einddatum BPV in foto en database komen niet overeen",
				"bpvRecord.geplandeEinddatumBPV", "bpvInschrijving.verwachteEinddatum"),
		BpvOmvang("BPV omvang", "BPV omvang in foto en database komen niet overeen",
				"bpvRecord.omvangBPV", "bpvInschrijving.totaleOmvang"),
		BpvCodeLeerbedrijf("BPV code leerbedrijf",
				"Code leerbedrijf van BPV-inschrijving in foto en database komen niet overeen",
				"bpvRecord.codeLeerbedrijf", "bpvInschrijving.bedrijfsgegeven.codeLeerbedrijf"),
		DiplomaNietGevonden("Diploma niet gevonden", "Diploma niet gevonden in de database",
				"examenRecord.codeDeelkwalificatieBehaald", null),
		DiplomaNietGevondenInBron("Diploma komt niet voor in foto",
				"Diploma komt niet voor in foto", null, "examendeelname.verbintenis.externeCode"),
		DiplomaKwalificatieBehaald("Behaalde kwalificatie",
				"Behaalde kwalificatie in foto en database komen niet overeen",
				"examenRecord.codeDeelkwalificatieBehaald",
				"examendeelname.verbintenis.externeCode"),
		DiplomaDatumBehaald("Datum behaald",
				"Datum behaald van diploma in foto en database komen niet overeen",
				"examenRecord.datumKwalificatieBehaald", "examendeelname.datumUitslag"),
		DiplomaBekostiging("Bekostiging diploma",
				"Bekostiging van diploma in foto en database komen niet overeen",
				"examenRecord.indicatieBekostigingDiplomaOmschrijving",
				"examendeelname.bekostigdOmschrijving"),
		OnderwijsontvangendeRecordNietGevondenInFoto("Onderwijsontvangende niet gevonden",
				"Onderwijsontvangende record niet gevonden in foto", "pgn", null),
		Geslacht("Geslacht", "Geslacht in foto en database komen niet overeen",
				"onderwijsontvangendeRecord.geslacht", "verbintenis.deelnemer.persoon.geslacht"),
		Geboortedatum("Geboortedatum", "Geboortedatum in foto en database komen niet overeen",
				"onderwijsontvangendeRecord.geboortedatum",
				"verbintenis.deelnemer.persoon.geboortedatum"),
		GeboorteJaarEnMaand("Geboortejaar en -maand",
				"Geboortejaar en -maand in foto en database komen niet overeen",
				"onderwijsontvangendeRecord.geboorteJaarEnMaand",
				"verbintenis.deelnemer.persoon.geboortedatum"),
		Postcodecijfers("Postcode", "Postcode in foto en database komen niet overeen",
				"onderwijsontvangendeRecord.postcodecijfers", "verbintenisPostcodecijfers"),
		Overlijdensdatum("Overlijdensdatum",
				"Overlijdensdatum in foto en database komen niet overeen",
				"onderwijsontvangendeRecord.overlijdensdatum",
				"verbintenis.deelnemer.persoon.datumOverlijden"),
		DatumVestigingInNederland("Datum in NL",
				"Datum vestiging in Nederland in foto en database komen niet overeen",
				"onderwijsontvangendeRecord.datumVestigingInNederland",
				"verbintenis.deelnemer.persoon.datumInNederland"),
		Geboorteland("Geboorteland", "Geboorteland in foto en database komen niet overeen",
				"onderwijsontvangendeRecord.codeGeboorteland",
				"verbintenis.deelnemer.persoon.geboorteland.code"),
		GeboortelandOuder1("Geboorteland ouder 1",
				"Geboorteland ouder 1 in foto en database komen niet overeen",
				"onderwijsontvangendeRecord.codeGeboortelandOuder1",
				"verbintenisCodeGeboortelandOuder1"),
		GeslachtOuder1("Geslacht ouder 1",
				"Geslacht ouder 1 in foto en database komen niet overeen",
				"onderwijsontvangendeRecord.geslachtOuder1", "verbintenisGeslachtOuder1"),
		GeboortelandOuder2("Geboorteland ouder 2",
				"Geboorteland ouder 2 in foto en database komen niet overeen",
				"onderwijsontvangendeRecord.codeGeboortelandOuder2",
				"verbintenisCodeGeboortelandOuder2"),
		GeslachtOuder2("Geslacht ouder 2",
				"Geslacht ouder 2 in foto en database komen niet overeen",
				"onderwijsontvangendeRecord.geslachtOuder2", "verbintenisGeslachtOuder2"),
		Nationaliteit1("Nationaliteit 1", "Nationaliteit 1 in foto en database komen niet overeen",
				"onderwijsontvangendeRecord.codeNationaliteit1",
				"verbintenis.deelnemer.persoon.nationaliteit1.code"),
		Nationaliteit2("Nationaliteit 2", "Nationaliteit 2 in foto en database komen niet overeen",
				"onderwijsontvangendeRecord.codeNationaliteit2",
				"verbintenis.deelnemer.persoon.nationaliteit2.code"),
		LeeftijdOpMeetdatum1("Leeftijd op meetdatum 1",
				"Leeftijd op meetdatum 1 in foto en database komen niet overeen",
				"onderwijsontvangendeRecord.leeftijdOpMeetdatum1",
				"verbintenisLeeftijdOpMeetdatum1"),
		LeeftijdOpMeetdatum2("Leeftijd op meetdatum 2",
				"Leeftijd op meetdatum 2 in foto en database komen niet overeen",
				"onderwijsontvangendeRecord.leeftijdOpMeetdatum2",
				"verbintenisLeeftijdOpMeetdatum2"),
		LeerjaarKomtNietOvereen("Leerjaar", "Leerjaar in foto en database komen niet overeen",
				"record.opleidingrecord.leerjaar", "verbintenis.plaatsing.leerjaar"),
		PraktijkjaarKomtNietOvereen("Praktijkjaar",
				"Praktijkjaar in foto en database komen niet overeen",
				"record.opleidingrecord.aantalJarenPraktijkonderwijs",
				"verbintenis.plaatsing.jarenPraktijkonderwijs");

		private final String korteOmschrijving;

		private final String omschrijving;

		private final String bronProperty;

		private final String dbProperty;

		private FotoVerschil(String korteOmschrijving, String omschrijving, String bronProperty,
				String dbProperty)
		{
			this.korteOmschrijving = korteOmschrijving;
			this.omschrijving = omschrijving;
			this.bronProperty = bronProperty;
			this.dbProperty = dbProperty;
		}

		public boolean isPersoonsgegevensVerschil()
		{
			switch (this)
			{
				case DatumVestigingInNederland:
				case DeelnemerBSNNietGevonden:
				case DeelnemerOnderwijsnummerNietGevonden:
				case Geboortedatum:
				case GeboorteJaarEnMaand:
				case Geboorteland:
				case GeboortelandOuder1:
				case GeboortelandOuder2:
				case Geslacht:
				case GeslachtOuder1:
				case GeslachtOuder2:
				case LeeftijdOpMeetdatum1:
				case LeeftijdOpMeetdatum2:
				case Nationaliteit1:
				case Nationaliteit2:
				case OnderwijsontvangendeRecordNietGevondenInFoto:
				case Overlijdensdatum:
				case Postcodecijfers:
					return true;
				default:
					return false;
			}
		}

		public boolean isInschrijvingsVerschil()
		{
			switch (this)
			{
				case BegindataKomenNietOvereen:
				case BekostigingOp1FebKomenNietOvereen:
				case BekostigingOpTeldatumKomenNietOvereen:
				case HoogsteVooropleidingKomenNietOvereen:
				case IndicatieGehandicaptKomenNietOvereen:
				case IntensiteitKomenNietOvereen:
				case LeerwegKomenNietOvereen:
				case OpleidingenKomenNietOvereen:
				case RedenUitstroomKomenNietOvereen:
				case VerbintenisNietGevonden:
				case VerwachteEinddataKomenNietOvereen:
				case LeerjaarKomtNietOvereen:
				case WerkelijkeEinddataKomenNietOvereen:
					return true;
				default:
					return false;
			}
		}

		public boolean isBpvVerschil()
		{
			return name().startsWith("Bpv");
		}

		public boolean isDiplomaVerschil()
		{
			return name().startsWith("Diploma");
		}

		@Override
		public String toString()
		{
			return korteOmschrijving;
		}

		public String getKorteOmschrijving()
		{
			return korteOmschrijving;
		}

		public String getOmschrijving()
		{
			return omschrijving;
		}

		public String getBronProperty()
		{
			return bronProperty;
		}

		public String getDbProperty()
		{
			return dbProperty;
		}

	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false, name = "bestand")
	@Index(name = "idx_BronFotobest_bestand")
	private BronFotobestand bestand;

	@Enumerated(value = EnumType.STRING)
	@Column(nullable = false)
	private FotoVerschil verschil;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = true, name = "fotoRecord")
	@Index(name = "idx_BronFotobest_fotoRecord")
	private BronFotoRecord fotoRecord;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = true, name = "deelnemer")
	@Index(name = "idx_BronFotobest_deelnemer")
	private Deelnemer deelnemer;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = true, name = "verbintenis")
	@Index(name = "idx_BronFotobest_verbintenis")
	private Verbintenis verbintenis;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = true, name = "examendeelname")
	@Index(name = "idx_BronFotobest_examendeelna")
	private Examendeelname examendeelname;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = true, name = "bpvInschrijving")
	@Index(name = "idx_BronFotobest_bpvInschrijv")
	private BPVInschrijving bpvInschrijving;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = true, name = "bpvRecord")
	@Index(name = "idx_BronFotobest_bpvRecord")
	private BronFotoBOBPVRecord bpvRecord;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = true, name = "examenRecord")
	@Index(name = "idx_BronFotobest_examenRecord")
	private BronFotoBODiplomaKwalificatieRecord examenRecord;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = true, name = "boOnderwijsontvangendeRecord")
	@Index(name = "idx_BronFotobest_boOoRecord")
	private BronFotoBOOnderwijsontvangendeRecord boOnderwijsontvangendeRecord;

	public BronFotobestandVerschil()
	{
	}

	public BronFotobestandVerschil(BronFotobestand bestand, FotoVerschil verschil)
	{
		setBestand(bestand);
		setVerschil(verschil);
	}

	public BronFotobestandVerschil(IBronFotoRecord record, FotoVerschil verschil,
			Verbintenis verbintenis)
	{
		setBestand(record.getBestand());
		setFotoRecord((BronFotoRecord) record);
		setVerschil(verschil);
		setVerbintenis(verbintenis);
		setDeelnemer(verbintenis == null ? null : verbintenis.getDeelnemer());
	}

	public IBronFotoInschrijvingRecord getRecord()
	{
		return (IBronFotoInschrijvingRecord) getFotoRecord().doUnproxy();
	}

	public BronFotobestand getBestand()
	{
		return bestand;
	}

	public void setBestand(BronFotobestand bestand)
	{
		this.bestand = bestand;
	}

	public FotoVerschil getVerschil()
	{
		return verschil;
	}

	public void setVerschil(FotoVerschil verschil)
	{
		this.verschil = verschil;
	}

	public BronFotoRecord getFotoRecord()
	{
		return fotoRecord;
	}

	public void setFotoRecord(BronFotoRecord fotoRecord)
	{
		this.fotoRecord = fotoRecord;
	}

	public Deelnemer getDeelnemer()
	{
		return deelnemer;
	}

	public void setDeelnemer(Deelnemer deelnemer)
	{
		this.deelnemer = deelnemer;
	}

	public Verbintenis getVerbintenis()
	{
		return verbintenis;
	}

	public void setVerbintenis(Verbintenis verbintenis)
	{
		this.verbintenis = verbintenis;
	}

	public Examendeelname getExamendeelname()
	{
		return examendeelname;
	}

	public void setExamendeelname(Examendeelname examendeelname)
	{
		this.examendeelname = examendeelname;
	}

	public boolean isVerbintenisBekostigdOpTeldatum()
	{
		if (getVerbintenis() != null && getFotoRecord() != null)
		{
			return getVerbintenis().isBekostigdOpDatum(getFotoRecord().getTeldatum());
		}
		return false;
	}

	public String getVerbintenisBekostigdOpTeldatumOmschrijving()
	{
		return isVerbintenisBekostigdOpTeldatum() ? "Ja" : "Nee";
	}

	public boolean isVerbintenisBekostigdOp1Feb()
	{
		if (getVerbintenis() != null && getFotoRecord() != null)
		{
			Date feb1 =
				TimeUtil.getInstance().getNextOccurrenceOfDate(getFotoRecord().getTeldatum(), 1, 1);
			return getVerbintenis().isBekostigdOpDatum(feb1);
		}
		return false;
	}

	public String getVerbintenisBekostigdOp1FebOmschrijving()
	{
		return isVerbintenisBekostigdOp1Feb() ? "Ja" : "Nee";
	}

	public Object getWaardeInBron()
	{
		if (getVerschil().getBronProperty() == null)
			return null;
		return PropertyResolver.getValue(getVerschil().getBronProperty(), this);
	}

	public Object getWaardeInDb()
	{
		if (getVerschil().getDbProperty() == null)
			return null;
		return PropertyResolver.getValue(getVerschil().getDbProperty(), this);
	}

	public BPVInschrijving getBpvInschrijving()
	{
		return bpvInschrijving;
	}

	public void setBpvInschrijving(BPVInschrijving bpvInschrijving)
	{
		this.bpvInschrijving = bpvInschrijving;
	}

	public BronFotoBOBPVRecord getBpvRecord()
	{
		return bpvRecord;
	}

	public void setBpvRecord(BronFotoBOBPVRecord bpvRecord)
	{
		this.bpvRecord = bpvRecord;
	}

	public BronFotoBODiplomaKwalificatieRecord getExamenRecord()
	{
		return examenRecord;
	}

	public void setExamenRecord(BronFotoBODiplomaKwalificatieRecord examenRecord)
	{
		this.examenRecord = examenRecord;
	}

	public String getVerbintenisPostcodecijfers()
	{
		BronEduArteModel model = new BronEduArteModel();
		Adres woonadres = model.getWoonadres(verbintenis.getDeelnemer());
		if (woonadres == null)
			return "0040";
		if (woonadres.getLand().getCode().equals("5010"))
			return "0010"; // belgie
		if (woonadres.getLand().getCode().equals("9089"))
			return "0020"; // duitsland
		if (woonadres.getLand().equals(Land.getNederland()))
		{
			return woonadres.getPostcode().substring(0, 4);
		}
		return "0030";
	}

	public String getVerbintenisCodeGeboortelandOuder1()
	{
		Persoon ouder = getVerbintenisOuder(1);
		return ouder == null || ouder.getGeboorteland() == null ? null : ouder.getGeboorteland()
			.getCode();
	}

	public String getVerbintenisCodeGeboortelandOuder2()
	{
		Persoon ouder = getVerbintenisOuder(2);
		return ouder == null || ouder.getGeboorteland() == null ? null : ouder.getGeboorteland()
			.getCode();
	}

	public Geslacht getVerbintenisGeslachtOuder1()
	{
		Persoon ouder = getVerbintenisOuder(1);
		return ouder == null ? null : ouder.getGeslacht();
	}

	public Geslacht getVerbintenisGeslachtOuder2()
	{
		Persoon ouder = getVerbintenisOuder(2);
		return ouder == null ? null : ouder.getGeslacht();
	}

	public Persoon getVerbintenisOuder(int nummer)
	{
		int counter = 0;
		List<AbstractRelatie> relaties = getVerbintenis().getDeelnemer().getPersoon().getRelaties();
		for (AbstractRelatie r : relaties)
		{
			AbstractRelatie relatie = (AbstractRelatie) r.doUnproxy();
			if (relatie instanceof Relatie)
			{
				Relatie ouder = (Relatie) relatie;
				RelatieSoort relatieSoort = ouder.getRelatieSoort();
				if (relatieSoort.getNaam().equals("Ouder"))
				{
					counter++;
					if (counter == nummer)
					{
						return ouder.getPersoon();
					}
				}
			}
		}
		return null;
	}

	public BronFotoBOOnderwijsontvangendeRecord getBoOnderwijsontvangendeRecord()
	{
		return boOnderwijsontvangendeRecord;
	}

	public void setBoOnderwijsontvangendeRecord(
			BronFotoBOOnderwijsontvangendeRecord boOnderwijsontvangendeRecord)
	{
		this.boOnderwijsontvangendeRecord = boOnderwijsontvangendeRecord;
	}

	public IBronFotoOnderwijsontvangendeRecord getOnderwijsontvangendeRecord()
	{
		if (getBoOnderwijsontvangendeRecord() != null)
			return getBoOnderwijsontvangendeRecord();
		return null;
	}

	public void setOnderwijsontvangendeRecord(IBronFotoOnderwijsontvangendeRecord ooRecord)
	{
		if (ooRecord instanceof BronFotoBOOnderwijsontvangendeRecord)
			setBoOnderwijsontvangendeRecord((BronFotoBOOnderwijsontvangendeRecord) ooRecord);
	}

	public int getVerbintenisLeeftijdOpMeetdatum1()
	{
		if (getOnderwijsontvangendeRecord() == null)
			return 0;
		return getVerbintenis().getDeelnemer().getPersoon().getLeeftijdOpDatum(
			getOnderwijsontvangendeRecord().getLeeftijdmeetdatum1());
	}

	public int getVerbintenisLeeftijdOpMeetdatum2()
	{
		if (getOnderwijsontvangendeRecord() == null)
			return 0;
		return getVerbintenis().getDeelnemer().getPersoon().getLeeftijdOpDatum(
			getOnderwijsontvangendeRecord().getLeeftijdmeetdatum2());
	}
}
