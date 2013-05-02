package nl.topicus.eduarte.krd.entities.bron.meldingen.terugkoppeling;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.persistence.*;

import nl.topicus.cobra.entities.RestrictedAccess;
import nl.topicus.eduarte.entities.organisatie.InstellingEntiteit;
import nl.topicus.onderwijs.duo.bron.BronOnderwijssoort;
import nl.topicus.onderwijs.duo.bron.data.waardelijsten.BestandSoort;
import nl.topicus.onderwijs.duo.bron.vo.batches.terugkoppeling.VOTerugkoppelingExamenDecentraal;
import nl.topicus.onderwijs.duo.bron.vo.batches.terugkoppeling.VOTerugkoppelingInschrijvingDecentraal;
import nl.topicus.onderwijs.duo.bron.vo.waardelijsten.Ernst;
import nl.topicus.onderwijs.duo.bron.vo.waardelijsten.SoortMelding;
import nl.topicus.onderwijs.duo.bron.vo.waardelijsten.StatusMelding;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity
@Table(name = "BRON_VO_TERUGKOPPELBATCHES")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
@BatchSize(size = 20)
public class BronVoTerugkoppelbestand extends InstellingEntiteit implements
		VOTerugkoppelingExamenDecentraal, VOTerugkoppelingInschrijvingDecentraal,
		IBronTerugkoppeling
{
	private static final long serialVersionUID = 1L;

	@Column(nullable = false, length = 4)
	private String brinNummer;

	@Column(nullable = false)
	private int aanleverpuntNummer;

	@Column(nullable = false)
	private int bRONBatchNummer;

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private BestandSoort bestandSoort;

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private SoortMelding soortMelding;

	@Column(nullable = false)
	@Temporal(TemporalType.DATE)
	private Date datumTerugkoppeling;

	@Column(nullable = false)
	private int aantalGeleverdeRecords;

	@Column(nullable = true)
	private String bestandsnaam;

	@RestrictedAccess(hasSetter = false)
	@Column(nullable = false)
	private int aantalSignalen;

	@RestrictedAccess(hasSetter = false)
	@Column(nullable = false)
	private int aantalAfkeurSignalen;

	@RestrictedAccess(hasSetter = false)
	@Column(nullable = false)
	private int aantalMeldingen;

	@RestrictedAccess(hasSetter = false)
	@Column(nullable = false)
	private int aantalAfgekeurdeMeldingen;

	@RestrictedAccess(hasSetter = false)
	@Column(nullable = false)
	private int aantalGoedgekeurdeMeldingen;

	@RestrictedAccess(hasSetter = false)
	@Column(length = 4000)
	private String batchesInBestand;

	@BatchSize(size = 20)
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "terugkoppelbestand", cascade = CascadeType.ALL)
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
	@OrderBy("batchNummer")
	private List<BronVoBatchgegevens> batches = new ArrayList<BronVoBatchgegevens>();

	public void berekenControleGetallen()
	{
		aantalMeldingen = 0;
		aantalSignalen = 0;
		aantalAfkeurSignalen = 0;
		aantalAfgekeurdeMeldingen = 0;
		aantalGoedgekeurdeMeldingen = 0;

		List<Integer> batchnummers = new ArrayList<Integer>();
		for (BronVoBatchgegevens batchgegeven : batches)
		{
			batchnummers.add(batchgegeven.getBatchNummer());

			List<AbstractBronVoTerugkoppelMelding> meldingen = batchgegeven.getTerugkoppelingen();
			aantalMeldingen += meldingen.size();

			for (AbstractBronVoTerugkoppelMelding melding : meldingen)
			{
				if (melding.getSignalen() != null)
					aantalSignalen += melding.getSignalen().size();
				if (melding.getStatusMelding() == StatusMelding.AFGEKEURD)
					aantalAfgekeurdeMeldingen++;
				else
					aantalGoedgekeurdeMeldingen++;
				for (BronVoSignaal signaal : melding.getSignalen())
				{
					if (signaal.getErnst() == Ernst.AFKEURING)
						aantalAfkeurSignalen++;
				}
				if (melding instanceof BronVoExamenTerugkoppelMelding)
				{
					BronVoExamenTerugkoppelMelding examenmelding =
						(BronVoExamenTerugkoppelMelding) melding;
					List<BronVoVakTerugkoppelMelding> vakMeldingen =
						examenmelding.getVakMeldingen();
					for (BronVoVakTerugkoppelMelding vakmelding : vakMeldingen)
					{
						if (vakmelding.getSignalen() != null)
						{
							aantalSignalen += vakmelding.getSignalen().size();
							for (BronVoSignaal signaal : vakmelding.getSignalen())
							{
								if (signaal.getErnst() == Ernst.AFKEURING)
									aantalAfkeurSignalen++;
							}
						}
					}
				}
			}
		}
		Collections.sort(batchnummers);
		String komma = "";
		batchesInBestand = "";
		for (Integer batchnummer : batchnummers)
		{
			batchesInBestand = String.format("%s%s%03d", batchesInBestand, komma, batchnummer);
			komma = ", ";
		}
	}

	public String getBrinNummer()
	{
		return brinNummer;
	}

	public void setBrinNummer(String brinNummer)
	{
		this.brinNummer = brinNummer;
	}

	public int getAanleverpuntNummer()
	{
		return aanleverpuntNummer;
	}

	public void setAanleverpuntNummer(int aanleverpuntNummer)
	{
		this.aanleverpuntNummer = aanleverpuntNummer;
	}

	public int getBRONBatchNummer()
	{
		return bRONBatchNummer;
	}

	public void setBRONBatchNummer(int batchNummer)
	{
		bRONBatchNummer = batchNummer;
	}

	public String getBRONBatchNummerAsString()
	{
		return String.format("%03d", getBRONBatchNummer());
	}

	public BestandSoort getBestandSoort()
	{
		return bestandSoort;
	}

	public void setBestandSoort(BestandSoort bestandSoort)
	{
		this.bestandSoort = bestandSoort;
	}

	public SoortMelding getSoortMelding()
	{
		return soortMelding;
	}

	public void setSoortMelding(SoortMelding soortMelding)
	{
		this.soortMelding = soortMelding;
	}

	public Date getDatumTerugkoppeling()
	{
		return datumTerugkoppeling;
	}

	public void setDatumTerugkoppeling(Date datumTerugkoppeling)
	{
		this.datumTerugkoppeling = datumTerugkoppeling;
	}

	public int getAantalGeleverdeRecords()
	{
		return aantalGeleverdeRecords;
	}

	public void setAantalGeleverdeRecords(int aantalGeleverdeRecords)
	{
		this.aantalGeleverdeRecords = aantalGeleverdeRecords;
	}

	public List<BronVoBatchgegevens> getBatches()
	{
		return batches;
	}

	public void setBatches(List<BronVoBatchgegevens> batches)
	{
		this.batches = batches;
	}

	@Override
	public BronOnderwijssoort getBronOnderwijssoort()
	{
		return BronOnderwijssoort.VOORTGEZETONDERWIJS;
	}

	@Override
	public int getAantalSignalen()
	{
		return aantalSignalen;
	}

	@Override
	public String getBatchesInBestand()
	{
		return batchesInBestand;
	}

	@Override
	public int getAantalAfkeurSignalen()
	{
		return aantalAfkeurSignalen;
	}

	@Override
	public int getAantalAfgekeurdeMeldingen()
	{
		return aantalAfgekeurdeMeldingen;
	}

	@Override
	public int getAantalGoedgekeurdeMeldingen()
	{
		return aantalGoedgekeurdeMeldingen;
	}

	@Override
	public int getAantalMeldingen()
	{
		return aantalMeldingen;
	}

	public void setBestandsnaam(String bestandsnaam)
	{
		this.bestandsnaam = bestandsnaam;
	}

	public String getBestandsnaam()
	{
		return bestandsnaam;
	}
}
