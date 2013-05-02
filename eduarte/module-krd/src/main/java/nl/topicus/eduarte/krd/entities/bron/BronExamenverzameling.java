package nl.topicus.eduarte.krd.entities.bron;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;

import nl.topicus.eduarte.entities.landelijk.Schooljaar;
import nl.topicus.eduarte.entities.opleiding.SoortOnderwijsTax;
import nl.topicus.eduarte.entities.organisatie.InstellingEntiteit;
import nl.topicus.eduarte.krd.entities.bron.meldingen.BronBatchBVE;
import nl.topicus.eduarte.krd.entities.bron.meldingen.BronBatchVOExamengegevens;
import nl.topicus.eduarte.krd.entities.bron.meldingen.aanlevering.BronAanleverMelding;
import nl.topicus.eduarte.krd.entities.bron.meldingen.aanlevering.BronExamenresultaatVOMelding;
import nl.topicus.onderwijs.duo.bron.BronOnderwijssoort;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Index;
import org.hibernate.annotations.Type;

@Entity
@Table(name = "BRON_EXAMENVERZAMELINGEN")
public class BronExamenverzameling extends InstellingEntiteit
{
	private static final long serialVersionUID = 1L;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false, name = "aanleverpunt")
	@Index(name = "idx_BRON_EXVERZ_aanleverpunt")
	private BronAanleverpunt aanleverpunt;

	@Type(type = "nl.topicus.eduarte.hibernate.usertypes.SchooljaarUserType")
	@Column(nullable = false, length = 9)
	private Schooljaar schooljaar;

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private BronOnderwijssoort bronOnderwijssoort;

	@Column(nullable = true)
	@Enumerated(EnumType.STRING)
	private SoortOnderwijsTax soortOnderwijs;

	@Column(nullable = false)
	private int aantalMeldingen;

	@BatchSize(size = 20)
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "examenverzameling")
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
	private List<BronExamenresultaatVOMelding> voExamenMeldingen =
		new ArrayList<BronExamenresultaatVOMelding>();

	@BatchSize(size = 20)
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "examenverzameling")
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
	private List<BronAanleverMelding> vavoExamenMeldingen = new ArrayList<BronAanleverMelding>();

	@BatchSize(size = 20)
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "bveBatch", nullable = true)
	@Index(name = "idx_BRON_EXVERZ_bveBatch")
	private BronBatchBVE bveBatch;

	@BatchSize(size = 20)
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "voBatch", nullable = true)
	@Index(name = "idx_BRON_EXVERZ_voBatch")
	private BronBatchVOExamengegevens voBatch;

	public BronExamenverzameling()
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

	public Schooljaar getSchooljaar()
	{
		return schooljaar;
	}

	public void setSchooljaar(Schooljaar schooljaar)
	{
		this.schooljaar = schooljaar;
	}

	public BronOnderwijssoort getBronOnderwijssoort()
	{
		return bronOnderwijssoort;
	}

	public void setBronOnderwijssoort(BronOnderwijssoort bronOnderwijssoort)
	{
		this.bronOnderwijssoort = bronOnderwijssoort;
	}

	public List<BronExamenresultaatVOMelding> getVoExamenMeldingen()
	{
		return voExamenMeldingen;
	}

	public void setVoExamenMeldingen(List<BronExamenresultaatVOMelding> voExamenMeldingen)
	{
		this.voExamenMeldingen = voExamenMeldingen;
	}

	public List<BronAanleverMelding> getVavoExamenMeldingen()
	{
		return vavoExamenMeldingen;
	}

	public void setVavoExamenMeldingen(List<BronAanleverMelding> vavoExamenMeldingen)
	{
		this.vavoExamenMeldingen = vavoExamenMeldingen;
	}

	public void setAantalMeldingen(int aantalMeldingen)
	{
		this.aantalMeldingen = aantalMeldingen;
	}

	public int getAantalMeldingen()
	{
		return aantalMeldingen;
	}

	public void setBveBatch(BronBatchBVE bveBatch)
	{
		this.bveBatch = bveBatch;
	}

	public BronBatchBVE getBveBatch()
	{
		return bveBatch;
	}

	public void setVoBatch(BronBatchVOExamengegevens voBatch)
	{
		this.voBatch = voBatch;
	}

	public BronBatchVOExamengegevens getVoBatch()
	{
		return voBatch;
	}

	public String getBatchOmschr()
	{
		if (getBveBatch() != null)
			return "Ja, " + getBveBatch().getBatchNummer();
		if (getVoBatch() != null)
			return "Ja, " + getVoBatch().getBatchNummer();
		return "Nee";
	}

	public void setSoortOnderwijs(SoortOnderwijsTax soortOnderwijs)
	{
		this.soortOnderwijs = soortOnderwijs;
	}

	public SoortOnderwijsTax getSoortOnderwijs()
	{
		return soortOnderwijs;
	}
}
