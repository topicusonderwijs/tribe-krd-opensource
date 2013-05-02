package nl.topicus.eduarte.krd.entities.bron.meldingen.terugkoppeling;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.persistence.*;

import nl.topicus.cobra.entities.RestrictedAccess;
import nl.topicus.eduarte.entities.organisatie.InstellingEntiteit;
import nl.topicus.onderwijs.duo.bron.BronOnderwijssoort;
import nl.topicus.onderwijs.duo.bron.bve.batches.pve_9_9.terugkoppeling.VoorlooprecordTerugkoppeling;
import nl.topicus.onderwijs.duo.bron.bve.waardelijsten.Sectordeel;
import nl.topicus.onderwijs.duo.bron.data.waardelijsten.BestandSoort;
import nl.topicus.onderwijs.duo.bron.vo.waardelijsten.Ernst;
import nl.topicus.onderwijs.duo.bron.vo.waardelijsten.StatusMelding;

@Entity
@Table(name = "BRON_BVE_TERUGKOPPELBATCHES")
public class BronBveTerugkoppelbestand extends InstellingEntiteit implements
		VoorlooprecordTerugkoppeling, IBronTerugkoppeling
{
	private static final long serialVersionUID = 1L;

	@Column(name = "aantalbestanden")
	@RestrictedAccess(hasGetter = true, hasSetter = false)
	private Integer aantalTeruggekoppeldeAanleverbestanden;

	@OneToMany(mappedBy = "terugkoppelbestand", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@OrderBy("batchnummerAanleverbestand")
	private List<BronBveBatchgegevens> batchgegevens = new ArrayList<BronBveBatchgegevens>();

	@Enumerated(EnumType.STRING)
	@RestrictedAccess(hasGetter = true, hasSetter = false)
	private BestandSoort bestandSoort;

	@Column(name = "brinnummer")
	@RestrictedAccess(hasGetter = true, hasSetter = false)
	private String brinNummer;

	@Column(name = "batchnummer")
	@RestrictedAccess(hasGetter = true, hasSetter = false)
	private int bRONBatchNummer;

	@Temporal(TemporalType.DATE)
	@RestrictedAccess(hasGetter = true, hasSetter = false)
	private Date datumTerugkoppeling;

	@Enumerated(EnumType.STRING)
	@RestrictedAccess(hasGetter = true, hasSetter = false)
	private Sectordeel sectordeel;

	@RestrictedAccess(hasGetter = true, hasSetter = false)
	private int aanleverpuntNummer;

	@Column(nullable = true)
	private String bestandsnaam;

	@RestrictedAccess(hasSetter = false)
	@Column(nullable = false)
	private int aantalMeldingen;

	@RestrictedAccess(hasSetter = false)
	@Column(nullable = false)
	private int aantalGoedgekeurdeMeldingen;

	@RestrictedAccess(hasSetter = false)
	@Column(nullable = false)
	private int aantalAfgekeurdeMeldingen;

	@RestrictedAccess(hasSetter = false)
	@Column(nullable = false)
	private int aantalAfkeurSignalen;

	@RestrictedAccess(hasSetter = false)
	@Column(length = 4000)
	private String batchesInBestand;

	@RestrictedAccess(hasSetter = false)
	@Column(nullable = false)
	private int aantalSignalen;

	@RestrictedAccess(hasSetter = false)
	private int aantalGeleverdeRecords;

	public void berekenControleTotalen()
	{
		List<Integer> batchnummers = new ArrayList<Integer>();

		aantalMeldingen = 0;
		aantalSignalen = 0;
		aantalAfkeurSignalen = 0;
		aantalGoedgekeurdeMeldingen = 0;
		aantalAfgekeurdeMeldingen = 0;
		aantalGeleverdeRecords = 0;

		for (BronBveBatchgegevens batchgegeven : batchgegevens)
		{
			aantalMeldingen += batchgegeven.getMeldingen().size();
			for (BronBveTerugkoppelMelding terugkoppelmelding : batchgegeven.getMeldingen())
			{
				if (terugkoppelmelding.getStatusMelding() == StatusMelding.GOEDGEKEURD)
					aantalGoedgekeurdeMeldingen++;
				else
					aantalAfgekeurdeMeldingen++;

				// De signalen lijst wordt dynamisch bepaalt bij het inlezen vanuit de
				// database (zie de hibernate annotaties voor signalen), dus zelf door de
				// net geconstrueerde lijst van records itereren.
				for (BronBveTerugkoppelRecord record : terugkoppelmelding.getRecords())
				{
					if (record.getRecordType() == 499)
						aantalSignalen++;
					if (record.getErnst() == Ernst.AFKEURING)
						aantalAfkeurSignalen++;
				}
				aantalGeleverdeRecords += terugkoppelmelding.getRecords().size();
			}
			batchnummers.add(batchgegeven.getBatchnummerAanleverbestand());
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

	@Override
	public Integer getAantalTeruggekoppeldeAanleverbestanden()
	{
		return aantalTeruggekoppeldeAanleverbestanden;
	}

	@Override
	public List<BronBveBatchgegevens> getBatchgegevens()
	{
		return batchgegevens;
	}

	public void setBatchgegevens(List<BronBveBatchgegevens> batchgegevens)
	{
		this.batchgegevens = batchgegevens;
	}

	@Override
	public String getBrinNummer()
	{
		return brinNummer;
	}

	@Override
	public Date getDatumTerugkoppeling()
	{
		return datumTerugkoppeling;
	}

	@Override
	public Sectordeel getSectordeel()
	{
		return sectordeel;
	}

	public BestandSoort getBestandSoort()
	{
		return bestandSoort;
	}

	public int getBRONBatchNummer()
	{
		return bRONBatchNummer;
	}

	public String getBRONBatchNummerAsString()
	{
		return String.format("%03d", getBRONBatchNummer());
	}

	public int getAanleverpuntNummer()
	{
		return aanleverpuntNummer;
	}

	@Override
	public int getAantalGeleverdeRecords()
	{
		return aantalGeleverdeRecords;
	}

	@Override
	public BronOnderwijssoort getBronOnderwijssoort()
	{
		return BronOnderwijssoort.valueOf(getSectordeel());
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
	public int getAantalMeldingen()
	{
		return aantalMeldingen;
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

	public void setBestandsnaam(String bestandsnaam)
	{
		this.bestandsnaam = bestandsnaam;
	}

	public String getBestandsnaam()
	{
		return bestandsnaam;
	}
}
