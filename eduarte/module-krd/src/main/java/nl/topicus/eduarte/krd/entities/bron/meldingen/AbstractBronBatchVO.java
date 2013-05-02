package nl.topicus.eduarte.krd.entities.bron.meldingen;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.*;

import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.entities.RestrictedAccess;
import nl.topicus.cobra.util.StringUtil;
import nl.topicus.eduarte.entities.landelijk.Schooljaar;
import nl.topicus.eduarte.entities.organisatie.InstellingEntiteit;
import nl.topicus.eduarte.krd.dao.helpers.BronDataAccessHelper;
import nl.topicus.eduarte.krd.dao.helpers.BronExamenverzamenlingDataAccessHelper;
import nl.topicus.eduarte.krd.entities.bron.BronAanleverpunt;
import nl.topicus.eduarte.krd.entities.bron.BronExamenverzameling;
import nl.topicus.eduarte.krd.entities.bron.BronMeldingStatus;
import nl.topicus.eduarte.krd.entities.bron.meldingen.aanlevering.AbstractBronVOMelding;
import nl.topicus.onderwijs.duo.bron.BronOnderwijssoort;
import nl.topicus.onderwijs.duo.bron.IBronBatch;
import nl.topicus.onderwijs.duo.bron.data.waardelijsten.BestandSoort;
import nl.topicus.onderwijs.duo.bron.vo.batches.VOVoorloopRecordDecentraal;
import nl.topicus.onderwijs.duo.bron.vo.waardelijsten.SoortMelding;
import nl.topicus.onderwijs.duo.bron.vo.waardelijsten.VerantwoordelijkeAanlevering;

import org.hibernate.annotations.Index;
import org.hibernate.annotations.Type;

@Entity
@DiscriminatorColumn(discriminatorType = DiscriminatorType.STRING, length = 2)
@Table(name = "BRON_VO_BATCHES")
public abstract class AbstractBronBatchVO<T extends AbstractBronVOMelding> extends
		InstellingEntiteit implements VOVoorloopRecordDecentraal, IBronBatch
{
	private static final long serialVersionUID = 1L;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false, name = "aanleverpunt")
	@Index(name = "idx_BRON_VO_BATC_aanleverpunt")
	private BronAanleverpunt aanleverpunt;

	@RestrictedAccess(hasSetter = false)
	private int aantalMeldingen = 0;

	@RestrictedAccess(hasSetter = false)
	private int aantalAanpassingen = 0;

	@RestrictedAccess(hasSetter = false)
	private int aantalToevoegingen = 0;

	@RestrictedAccess(hasSetter = false)
	private int aantalUitschrijvingen = 0;

	@RestrictedAccess(hasSetter = false)
	private int aantalVerwijderingen = 0;

	@RestrictedAccess(hasSetter = false)
	private int aantalTerugkoppelMeldingen = 0;

	@Column(nullable = false, length = 4)
	private String BRINNummer;

	@Column(nullable = false)
	private Integer batchNummer;

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private BestandSoort bestandSoort;

	@Column(nullable = true)
	private Date datumVerzending;

	@Column(nullable = true, name = "organisatienummer")
	private String internOrganisatieNummer;

	/**
	 * Kan null zijn voor examenbatches.
	 */
	@Column(nullable = true)
	private Boolean laatsteAanlevering = false;

	@Column(nullable = false, length = 80, name = "pakket_naam")
	private String naamSchoolAdministratiePakket;

	@Column(nullable = false, length = 10, name = "pakket_versie")
	public String versieSchoolAdministratiePakket;

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private SoortMelding soortMelding;

	@Column(nullable = false, name = "verantwoordelijke")
	@Enumerated(EnumType.STRING)
	private VerantwoordelijkeAanlevering verantwoordelijkeAanlevering;

	@OneToMany(mappedBy = "batch", targetEntity = AbstractBronVOMelding.class, fetch = FetchType.LAZY)
	@OrderBy("meldingNummer asc")
	private List< ? extends AbstractBronVOMelding> meldingen =
		new ArrayList<AbstractBronVOMelding>();

	@Column(length = 20, nullable = true)
	private String bestandsnaam;

	@Type(type = "nl.topicus.eduarte.hibernate.usertypes.SchooljaarUserType")
	@Column(nullable = false, length = 9)
	private Schooljaar schooljaar;

	@Lob()
	private byte[] bestand;

	public AbstractBronBatchVO()
	{
	}

	public BronAanleverpunt getAanleverpunt()
	{
		return aanleverpunt;
	}

	public void setAanleverpunt(BronAanleverpunt aanleverpunt)
	{
		this.aanleverpunt = aanleverpunt;
	}

	public Integer getAanleverPuntNummer()
	{
		if (getAanleverpunt() == null)
			return null;
		return getAanleverpunt().getNummer();
	}

	public int getAantalAanpassingen()
	{
		return aantalAanpassingen;
	}

	public int getAantalToevoegingen()
	{
		return aantalToevoegingen;
	}

	public int getAantalUitschrijvingen()
	{
		return aantalUitschrijvingen;
	}

	public int getAantalVerwijderingen()
	{
		return aantalVerwijderingen;
	}

	public void setBRINNummer(String bRINNummer)
	{
		BRINNummer = bRINNummer;
	}

	public String getBRINNummer()
	{
		return BRINNummer;
	}

	public void setBatchNummer(Integer batchNummer)
	{
		this.batchNummer = batchNummer;
	}

	public Integer getBatchNummer()
	{
		return batchNummer;
	}

	public void setBestandSoort(BestandSoort bestandSoort)
	{
		this.bestandSoort = bestandSoort;
	}

	public BestandSoort getBestandSoort()
	{
		return bestandSoort;
	}

	public Date getDatumVerzending()
	{
		return datumVerzending;
	}

	public void setDatumVerzending(Date datumVerzending)
	{
		this.datumVerzending = datumVerzending;
	}

	public String getInternOrganisatieNummer()
	{
		return internOrganisatieNummer;
	}

	public void setInternOrganisatieNummer(String internOrganisatieNummer)
	{
		this.internOrganisatieNummer = internOrganisatieNummer;
	}

	public Boolean getLaatsteAanlevering()
	{
		return laatsteAanlevering;
	}

	public void setLaatsteAanlevering(Boolean laatsteAanlevering)
	{
		this.laatsteAanlevering = laatsteAanlevering;
	}

	public String getNaamSchoolAdministratiePakket()
	{
		return naamSchoolAdministratiePakket;
	}

	public void setNaamSchoolAdministratiePakket(String naamSchoolAdministratiePakket)
	{
		this.naamSchoolAdministratiePakket = naamSchoolAdministratiePakket;
	}

	public SoortMelding getSoortMelding()
	{
		return soortMelding;
	}

	public void setSoortMelding(SoortMelding soortMelding)
	{
		this.soortMelding = soortMelding;
	}

	public VerantwoordelijkeAanlevering getVerantwoordelijkeAanlevering()
	{
		return verantwoordelijkeAanlevering;
	}

	public void setVerantwoordelijkeAanlevering(
			nl.topicus.onderwijs.duo.bron.vo.waardelijsten.VerantwoordelijkeAanlevering verantwoordelijkeAanlevering)
	{
		this.verantwoordelijkeAanlevering = verantwoordelijkeAanlevering;
	}

	public String getVersieSchoolAdministratiePakket()
	{
		return versieSchoolAdministratiePakket;
	}

	public void setVersieSchoolAdministratiePakket(String versieSchoolAdministratiePakket)
	{
		this.versieSchoolAdministratiePakket =
			StringUtil.truncate(versieSchoolAdministratiePakket, 10, "");
	}

	public int getAantalRecords()
	{
		return aantalMeldingen;
	}

	@Override
	public void berekenControleTotalen()
	{
		aantalMeldingen = getMeldingen().size();
		aantalToevoegingen = 0;
		aantalAanpassingen = 0;
		aantalUitschrijvingen = 0;
		aantalVerwijderingen = 0;
		aantalTerugkoppelMeldingen = 0;

		for (AbstractBronVOMelding melding : getMeldingen())
		{
			switch (melding.getSoortMutatie())
			{
				case Toevoeging:
					aantalToevoegingen++;
					break;
				case Aanpassing:
					aantalAanpassingen++;
					break;
				case Uitschrijving:
					aantalUitschrijvingen++;
					break;
				case Verwijdering:
					aantalVerwijderingen++;
					break;
			}
			if (melding.getTerugkoppelmelding() != null)
			{
				aantalTerugkoppelMeldingen++;
			}
		}
	}

	@SuppressWarnings("unchecked")
	public void addMelding(T melding)
	{
		melding.setMeldingNummer(getMeldingen().size() + 1);
		((List) getMeldingen()).add(melding);
	}

	@Override
	public List< ? extends AbstractBronVOMelding> getMeldingen()
	{
		return meldingen;
	}

	/**
	 * DIT IS GEEN ONDERDEEL VAN DE API, GEBRUIK DEZE METHODE NIET! Met grote tegenzin
	 * toegevoegd tbv het Enhanced-yeah right-HibernateModel.
	 */
	public void setMeldingen(List< ? extends AbstractBronVOMelding> meldingen)
	{
		this.meldingen = meldingen;
	}

	@Override
	public int getAantalMeldingen()
	{
		return aantalMeldingen;
	}

	@Override
	public int getAantalTerugkoppelMeldingen()
	{
		return aantalTerugkoppelMeldingen;
	}

	@Override
	public int getAantalMeldingenInBehandeling()
	{
		return getAantalMeldingen() - getAantalTerugkoppelMeldingen();
	}

	@Override
	public BronOnderwijssoort getOnderwijssoort()
	{
		return BronOnderwijssoort.VOORTGEZETONDERWIJS;
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
		return String.format("%03d", getBatchNummer());
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
		if (getBatchNummer() == getAanleverpunt().getLaatsteBatchNrVO())
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
				bronExamenverzameling.setVoBatch(null);
				bronExamenverzameling.saveOrUpdate();
			}
			for (AbstractBronVOMelding melding : getMeldingen())
			{
				melding.setBatch(null);
				melding.setBronMeldingStatus(BronMeldingStatus.WACHTRIJ);
				melding.setMeldingNummer(0);
				melding.saveOrUpdate();
			}
			BronAanleverpunt bronAanleverpunt = getAanleverpunt();
			bronAanleverpunt.decBatchNrVO();
			bronAanleverpunt.saveOrUpdate();
			this.delete();
			this.commit();
		}
	}
}
