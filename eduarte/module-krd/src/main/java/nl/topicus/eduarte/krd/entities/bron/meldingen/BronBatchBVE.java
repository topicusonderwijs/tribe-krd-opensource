package nl.topicus.eduarte.krd.entities.bron.meldingen;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.persistence.*;

import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.entities.AdministratiePakket;
import nl.topicus.cobra.entities.RestrictedAccess;
import nl.topicus.eduarte.entities.landelijk.Schooljaar;
import nl.topicus.eduarte.entities.organisatie.InstellingEntiteit;
import nl.topicus.eduarte.krd.bron.BronBveAanleverRecordComparator;
import nl.topicus.eduarte.krd.dao.helpers.BronDataAccessHelper;
import nl.topicus.eduarte.krd.dao.helpers.BronExamenverzamenlingDataAccessHelper;
import nl.topicus.eduarte.krd.entities.bron.BronAanleverpunt;
import nl.topicus.eduarte.krd.entities.bron.BronExamenverzameling;
import nl.topicus.eduarte.krd.entities.bron.BronMeldingStatus;
import nl.topicus.eduarte.krd.entities.bron.meldingen.aanlevering.BronAanleverMelding;
import nl.topicus.eduarte.krd.entities.bron.meldingen.aanlevering.BronBveAanleverRecord;
import nl.topicus.onderwijs.duo.bron.BronOnderwijssoort;
import nl.topicus.onderwijs.duo.bron.IBronBatch;
import nl.topicus.onderwijs.duo.bron.annotation.RecordSet;
import nl.topicus.onderwijs.duo.bron.bve.batches.pve_9_9.aanlevering.VoorlooprecordAanleverbestand;
import nl.topicus.onderwijs.duo.bron.bve.waardelijsten.Sectordeel;
import nl.topicus.onderwijs.duo.bron.bve.waardelijsten.SoortMutatie;
import nl.topicus.onderwijs.duo.bron.bve.waardelijsten.VerantwoordelijkeAanleverbestand;
import nl.topicus.onderwijs.duo.bron.data.waardelijsten.BestandSoort;

import org.hibernate.annotations.Index;
import org.hibernate.annotations.Type;

@Entity
@Table(name = "BRON_BVE_BATCHES")
public class BronBatchBVE extends InstellingEntiteit implements VoorlooprecordAanleverbestand,
		IBronBatch
{
	private static final long serialVersionUID = 1L;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false, name = "aanleverpunt")
	@Index(name = "idx_BRON_BVE_BAT_aanleverpunt")
	private BronAanleverpunt aanleverpunt;

	@Column(name = "aanmaakdatum")
	private Date aanmaakdatumAanleverbestand;

	private int aantalMeldingen = 0;

	private int aantalRecords = 1;

	private Integer batchNummer = null;

	@Enumerated(EnumType.STRING)
	private BestandSoort bestandSoort;

	@Column(name = "organisatienummer")
	private String internOrganisatieNummer;

	private Boolean laatsteAanlevering = false;

	@Enumerated(EnumType.STRING)
	private Sectordeel sectordeel = Sectordeel.Beroepsonderwijs;

	@Enumerated(EnumType.STRING)
	@Column(name = "verantwoordelijke")
	private VerantwoordelijkeAanleverbestand verantwoordelijkeAanlevering =
		VerantwoordelijkeAanleverbestand.Instelling;

	@OneToMany(mappedBy = "batch", fetch = FetchType.LAZY)
	@OrderBy("meldingnummer asc, createdAt")
	private List<BronAanleverMelding> meldingen = new ArrayList<BronAanleverMelding>();

	@Column(length = 200, nullable = true)
	private String bestandsnaam;

	@Lob()
	private byte[] bestand;

	@RestrictedAccess(hasSetter = false)
	private int aantalVerwijderingen;

	@RestrictedAccess(hasSetter = false)
	private int aantalToevoegingen;

	@RestrictedAccess(hasSetter = false)
	private int aantalAanpassingen;

	@RestrictedAccess(hasSetter = false)
	private int aantalTerugkoppelMeldingen;

	@Type(type = "nl.topicus.eduarte.hibernate.usertypes.SchooljaarUserType")
	@Column(nullable = false, length = 9)
	private Schooljaar schooljaar;

	public BronBatchBVE()
	{
		// voor Hibernate
	}

	@Override
	public void berekenControleTotalen()
	{
		aantalMeldingen = getMeldingen().size();
		aantalRecords = 1;

		aantalVerwijderingen = 0;
		aantalToevoegingen = 0;
		aantalAanpassingen = 0;

		for (BronAanleverMelding melding : getMeldingen())
		{
			Collections.sort(melding.getMeldingen(), new BronBveAanleverRecordComparator());
			for (int i = 0; i < melding.getMeldingen().size(); i++)
			{
				BronBveAanleverRecord record = melding.getMeldingen().get(i);
				record.setRecordNummer(i + 1);
				if (record.getSoortMutatie() == SoortMutatie.Toevoeging)
					aantalToevoegingen++;
				else if (record.getSoortMutatie() == SoortMutatie.Aanpassing)
					aantalAanpassingen++;
				else if (record.getSoortMutatie() == SoortMutatie.Verwijdering)
					aantalVerwijderingen++;
			}
			melding.saveOrUpdate();
			aantalRecords = aantalRecords + 1 + melding.getMeldingen().size();
		}
	}

	@RecordSet
	public List<BronAanleverMelding> getMeldingen()
	{
		return meldingen;
	}

	/**
	 * DIT IS GEEN ONDERDEEL VAN DE API, GEBRUIK DEZE METHODE NIET! Enkel met tegenzin
	 * toegevoegd voor EnhancedHibernateModel.
	 */
	public void setMeldingen(List<BronAanleverMelding> meldingen)
	{
		this.meldingen = meldingen;
	}

	public BronAanleverpunt getAanleverpunt()
	{
		return aanleverpunt;
	}

	public void setAanleverpunt(BronAanleverpunt aanleverpunt)
	{
		this.aanleverpunt = aanleverpunt;
	}

	@Override
	public Integer getAanleverPuntNummer()
	{
		return aanleverpunt.getNummer();
	}

	@Override
	public Date getAanmaakdatumAanleverbestand()
	{
		return aanmaakdatumAanleverbestand;
	}

	public void setAanmaakdatumAanleverbestand(Date aanmaakdatumAanleverbestand)
	{
		this.aanmaakdatumAanleverbestand = aanmaakdatumAanleverbestand;
	}

	@Override
	public int getAantalMeldingen()
	{
		return aantalMeldingen;
	}

	public void setAantalMeldingen(int aantalMeldingen)
	{
		this.aantalMeldingen = aantalMeldingen;
	}

	@Override
	public int getAantalRecords()
	{
		return aantalRecords;
	}

	public void setAantalRecords(int aantalRecords)
	{
		this.aantalRecords = aantalRecords;
	}

	@Override
	public Integer getBatchNummer()
	{
		return batchNummer;
	}

	public void setBatchNummer(Integer batchNummer)
	{
		this.batchNummer = batchNummer;
	}

	@Override
	public BestandSoort getBestandSoort()
	{
		return bestandSoort;
	}

	public void setBestandSoort(BestandSoort bestandSoort)
	{
		this.bestandSoort = bestandSoort;
	}

	@Override
	public String getBrinNummer()
	{
		return getOrganisatie().getBrincode().getCode();
	}

	@Override
	public String getInternOrganisatieNummer()
	{
		// FIXME ophalen bij Organisatie?
		return internOrganisatieNummer;
	}

	public void setInternOrganisatieNummer(String internOrganisatieNummer)
	{
		// FIXME als ophalen bij Organisatie, dan niet setten?
		this.internOrganisatieNummer = internOrganisatieNummer;
	}

	@Override
	public Boolean getLaatsteAanlevering()
	{
		return laatsteAanlevering;
	}

	public void setLaatsteAanlevering(Boolean laatsteAanlevering)
	{
		this.laatsteAanlevering = laatsteAanlevering;
	}

	@Override
	public String getNaamSchoolAdministratiePakket()
	{
		return AdministratiePakket.getPakket().getNaam();
	}

	@Override
	public Sectordeel getSectordeel()
	{
		return sectordeel;
	}

	public void setSectordeel(Sectordeel sectordeel)
	{
		this.sectordeel = sectordeel;
	}

	@Override
	public VerantwoordelijkeAanleverbestand getVerantwoordelijkeAanlevering()
	{
		return verantwoordelijkeAanlevering;
	}

	public void setVerantwoordelijkeAanlevering(VerantwoordelijkeAanleverbestand verantwoordelijke)
	{
		this.verantwoordelijkeAanlevering = verantwoordelijke;
	}

	@Override
	public String getVersieProgrammaVanEisen()
	{
		return "1.1";
	}

	@Override
	public String getVersieSchoolAdministratiePakket()
	{
		return AdministratiePakket.getPakket().getVersie();
	}

	public void addMelding(BronAanleverMelding melding)
	{
		getMeldingen().add(melding);
		melding.setMeldingnummer(getMeldingen().size());
		setAantalMeldingen(getMeldingen().size());
	}

	@Override
	public int getAantalTerugkoppelMeldingen()
	{
		return aantalTerugkoppelMeldingen;
	}

	@Override
	public int getAantalMeldingenInBehandeling()
	{
		int totaalAantal = getAantalMeldingen();
		int teruggekoppeldeMeldingen = getAantalTerugkoppelMeldingen();
		return totaalAantal - teruggekoppeldeMeldingen;
	}

	@Override
	public BronOnderwijssoort getOnderwijssoort()
	{
		return BronOnderwijssoort.valueOf(getSectordeel());
	}

	@Override
	public int getAantalAanpassingen()
	{
		return aantalAanpassingen;
	}

	@Override
	public int getAantalToevoegingen()
	{
		return aantalToevoegingen;
	}

	@Override
	public int getAantalUitschrijvingen()
	{
		return 0;
	}

	@Override
	public int getAantalVerwijderingen()
	{
		return aantalVerwijderingen;
	}

	public List<BronBveAanleverRecord> getRecords()
	{
		List<BronBveAanleverRecord> records = new ArrayList<BronBveAanleverRecord>();
		for (BronAanleverMelding melding : getMeldingen())
		{
			records.addAll(melding.getMeldingen());
		}
		return records;
	}

	public void setBestandsnaam(String bestandsnaam)
	{
		this.bestandsnaam = bestandsnaam;
	}

	public String getBestandsnaam()
	{
		return bestandsnaam;
	}

	public void setBestand(byte[] bestand)
	{
		this.bestand = bestand;
	}

	public byte[] getBestand()
	{
		return bestand;
	}

	public String getBatchNummerAsString()
	{
		Integer nr = getBatchNummer();
		if (nr != null)
		{
			return String.format("%03d", nr);
		}
		return "n/a";
	}

	public void setSchooljaar(Schooljaar schooljaar)
	{
		this.schooljaar = schooljaar;
	}

	public Schooljaar getSchooljaar()
	{
		return schooljaar;
	}

	@Override
	public boolean isVerwijderBatchMogelijk()
	{
		if (getBatchNummer() == getAanleverpunt().getLaatseBatchNrSector(getSectordeel()))
		{
			BronDataAccessHelper helper = DataAccessRegistry.getHelper(BronDataAccessHelper.class);
			return !helper.isTerugkoppelingIngelezenVoorBatch(this);
		}
		return false;
	}

	@Override
	public void verwijderBatch()
	{
		if (isVerwijderBatchMogelijk())
		{
			BronExamenverzamenlingDataAccessHelper helper =
				DataAccessRegistry.getHelper(BronExamenverzamenlingDataAccessHelper.class);
			List<BronExamenverzameling> verzamelingen = helper.getExVerzGekoppeldAanBatch(this);
			for (BronExamenverzameling bronExamenverzameling : verzamelingen)
			{
				bronExamenverzameling.setBveBatch(null);
				bronExamenverzameling.saveOrUpdate();
			}
			for (BronAanleverMelding melding : getMeldingen())
			{
				melding.setBatch(null);
				melding.setBronMeldingStatus(BronMeldingStatus.WACHTRIJ);
				melding.setMeldingnummer(0);
				melding.saveOrUpdate();
			}
			BronAanleverpunt bronAanleverpunt = getAanleverpunt();
			bronAanleverpunt.decLaatseBatchNrSector(getSectordeel());
			bronAanleverpunt.saveOrUpdate();
			this.delete();
			this.commit();
		}
	}
}
