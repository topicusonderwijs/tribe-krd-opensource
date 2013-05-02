package nl.topicus.eduarte.krd.entities.bron.meldingen.terugkoppeling;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.persistence.*;

import nl.topicus.eduarte.entities.organisatie.InstellingEntiteit;
import nl.topicus.eduarte.krd.entities.bron.meldingen.BronBatchBVE;
import nl.topicus.onderwijs.duo.bron.annotation.Record;
import nl.topicus.onderwijs.duo.bron.bve.batches.pve_9_9.terugkoppeling.Batchgegevens;

import org.hibernate.annotations.Index;

@Entity
@Table(name = "BRON_BVE_BATCHGEGEVENS")
public class BronBveBatchgegevens extends InstellingEntiteit implements Batchgegevens
{
	private static final long serialVersionUID = 1L;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "terugkoppelbestand")
	@Index(name = "idx_BRON_BVE_BAT_terugkoppelb")
	private BronBveTerugkoppelbestand terugkoppelbestand;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "batch")
	@Index(name = "idx_BRON_BVE_BAT_batch_id")
	private BronBatchBVE batch;

	@Column(name = "batchnummer")
	private Integer batchnummerAanleverbestand;

	@Column(name = "aantalMeldingenFout")
	private Integer cumulatiefAantalFouteMeldingen;

	@Column(name = "aantalMeldingenGoed")
	private Integer cumulatiefAantalGoedeMeldingen;

	@Column(name = "datumOntvangstAanleverbestand")
	@Temporal(TemporalType.DATE)
	private Date datumOntvangstAanleverbestand;

	@Column(length = 3)
	private String internOrganisatienummer;

	@Column(name = "aantalMeldingenAanlevering")
	private Integer meldingenInAanleverbestand;

	@Column(name = "aantalMeldingenInBehandeling")
	private Integer resterendAantalMeldingenInBehandeling;

	@OneToMany(mappedBy = "batchgegevens", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<BronBveTerugkoppelMelding> meldingen = new ArrayList<BronBveTerugkoppelMelding>();

	public BronBveTerugkoppelbestand getTerugkoppelbestand()
	{
		return terugkoppelbestand;
	}

	public void setTerugkoppelbestand(BronBveTerugkoppelbestand terugkoppelbestand)
	{
		this.terugkoppelbestand = terugkoppelbestand;
	}

	public BronBatchBVE getBatch()
	{
		return batch;
	}

	public void setBatch(BronBatchBVE batch)
	{
		this.batch = batch;
	}

	@Override
	public Integer getBatchnummerAanleverbestand()
	{
		return batchnummerAanleverbestand;
	}

	public void setBatchnummerAanleverbestand(Integer batchnummerAanleverbestand)
	{
		this.batchnummerAanleverbestand = batchnummerAanleverbestand;
	}

	@Override
	public Integer getCumulatiefAantalFouteMeldingen()
	{
		return cumulatiefAantalFouteMeldingen;
	}

	public void setCumulatiefAantalFouteMeldingen(Integer cumulatiefAantalFouteMeldingen)
	{
		this.cumulatiefAantalFouteMeldingen = cumulatiefAantalFouteMeldingen;
	}

	@Override
	public Integer getCumulatiefAantalGoedeMeldingen()
	{
		return cumulatiefAantalGoedeMeldingen;
	}

	public void setCumulatiefAantalGoedeMeldingen(Integer cumulatiefAantalGoedeMeldingen)
	{
		this.cumulatiefAantalGoedeMeldingen = cumulatiefAantalGoedeMeldingen;
	}

	@Override
	public Date getDatumOntvangstAanleverbestand()
	{
		return datumOntvangstAanleverbestand;
	}

	public void setDatumOntvangstAanleverbestand(Date datumOntvangstAanleverbestand)
	{
		this.datumOntvangstAanleverbestand = datumOntvangstAanleverbestand;
	}

	@Override
	public String getInternOrganisatienummer()
	{
		return internOrganisatienummer;
	}

	public void setInternOrganisatienummer(String internOrganisatienummer)
	{
		this.internOrganisatienummer = internOrganisatienummer;
	}

	@Override
	public List<BronBveTerugkoppelMelding> getMeldingen()
	{
		if (meldingen == null)
			return Collections.emptyList();
		return meldingen;
	}

	public void setMeldingen(List<BronBveTerugkoppelMelding> meldingen)
	{
		this.meldingen = meldingen;
	}

	@Override
	public Integer getMeldingenInAanleverbestand()
	{
		return meldingenInAanleverbestand;
	}

	public void setMeldingenInAanleverbestand(Integer meldingenInAanleverbestand)
	{
		this.meldingenInAanleverbestand = meldingenInAanleverbestand;
	}

	@Override
	public int getRecordType()
	{
		return Batchgegevens.class.getAnnotation(Record.class).type();
	}

	@Override
	public Integer getResterendAantalMeldingenInBehandeling()
	{
		return resterendAantalMeldingenInBehandeling;
	}

	public void setResterendAantalMeldingenInBehandeling(
			Integer resterendAantalMeldingenInBehandeling)
	{
		this.resterendAantalMeldingenInBehandeling = resterendAantalMeldingenInBehandeling;
	}
}
