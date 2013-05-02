package nl.topicus.eduarte.krd.entities.bron.meldingen.terugkoppeling;

import java.util.Date;
import java.util.List;

import javax.persistence.*;

import nl.topicus.eduarte.entities.organisatie.InstellingEntiteit;
import nl.topicus.eduarte.krd.entities.bron.meldingen.AbstractBronBatchVO;
import nl.topicus.onderwijs.duo.bron.vo.batches.terugkoppeling.VOVoorloopRecordBatch;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Index;

@Entity
@Table(name = "BRON_VO_BATCHGEGEVENS")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
@BatchSize(size = 20)
public class BronVoBatchgegevens extends InstellingEntiteit implements VOVoorloopRecordBatch
{
	private static final long serialVersionUID = 1L;

	@Column(nullable = false)
	private int batchNummer;

	@Column(nullable = true, length = 3)
	private String internOrganisatieNummer;

	@Column(nullable = false)
	private int aantalMeldingenOntvangen;

	@Column(nullable = false)
	private int aantalMeldingenGoed;

	@Column(nullable = false)
	private int aantalMeldingenFout;

	@Column(nullable = false, name = "aantalMeldingenInBehand")
	private int aantalMeldingenNogInBehandeling;

	@BatchSize(size = 20)
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "batchgegevens", cascade = CascadeType.ALL)
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
	@OrderBy("meldingNummer asc")
	List<AbstractBronVoTerugkoppelMelding> terugkoppelingen;

	@ManyToOne
	@JoinColumn(name = "terugkoppelbestand")
	@Index(name = "idx_BRON_VO_BAT_terugkoppelb")
	private BronVoTerugkoppelbestand terugkoppelbestand;

	@ManyToOne
	@JoinColumn(name = "batch")
	@Index(name = "idx_BRON_VO_BAT_batch_id")
	private AbstractBronBatchVO< ? > batch;

	@Column(name = "datumOntvangstAanleverbestand")
	@Temporal(TemporalType.DATE)
	private Date datumOntvangstAanleverbestand;

	public int getBatchNummer()
	{
		return batchNummer;
	}

	public void setBatchNummer(int batchNummer)
	{
		this.batchNummer = batchNummer;
	}

	public String getInternOrganisatieNummer()
	{
		return internOrganisatieNummer;
	}

	public void setInternOrganisatieNummer(String internOrganisatieNummer)
	{
		this.internOrganisatieNummer = internOrganisatieNummer;
	}

	public int getAantalMeldingenOntvangen()
	{
		return aantalMeldingenOntvangen;
	}

	public void setAantalMeldingenOntvangen(int aantalMeldingenOntvangen)
	{
		this.aantalMeldingenOntvangen = aantalMeldingenOntvangen;
	}

	public int getAantalMeldingenGoed()
	{
		return aantalMeldingenGoed;
	}

	public void setAantalMeldingenGoed(int aantalMeldingenGoed)
	{
		this.aantalMeldingenGoed = aantalMeldingenGoed;
	}

	public int getAantalMeldingenFout()
	{
		return aantalMeldingenFout;
	}

	public void setAantalMeldingenFout(int aantalMeldingenFout)
	{
		this.aantalMeldingenFout = aantalMeldingenFout;
	}

	public int getAantalMeldingenNogInBehandeling()
	{
		return aantalMeldingenNogInBehandeling;
	}

	public void setAantalMeldingenNogInBehandeling(int aantalMeldingenNogInBehandeling)
	{
		this.aantalMeldingenNogInBehandeling = aantalMeldingenNogInBehandeling;
	}

	public List<AbstractBronVoTerugkoppelMelding> getTerugkoppelingen()
	{
		return terugkoppelingen;
	}

	public void setTerugkoppelingen(List<AbstractBronVoTerugkoppelMelding> terugkoppelingen)
	{
		this.terugkoppelingen = terugkoppelingen;
	}

	public BronVoTerugkoppelbestand getTerugkoppelbestand()
	{
		return terugkoppelbestand;
	}

	public void setTerugkoppelbestand(BronVoTerugkoppelbestand terugkoppelbestand)
	{
		this.terugkoppelbestand = terugkoppelbestand;
	}

	public AbstractBronBatchVO< ? > getBatch()
	{
		return batch;
	}

	public void setBatch(AbstractBronBatchVO< ? > batch)
	{
		this.batch = batch;
	}

	public Date getDatumOntvangstAanleverbestand()
	{
		return datumOntvangstAanleverbestand;
	}

	public void setDatumOntvangstAanleverbestand(Date datumOntvangstAanleverbestand)
	{
		this.datumOntvangstAanleverbestand = datumOntvangstAanleverbestand;
	}

}
