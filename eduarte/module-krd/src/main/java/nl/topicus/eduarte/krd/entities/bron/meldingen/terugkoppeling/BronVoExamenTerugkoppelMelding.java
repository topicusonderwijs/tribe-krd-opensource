package nl.topicus.eduarte.krd.entities.bron.meldingen.terugkoppeling;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.*;

import nl.topicus.onderwijs.duo.bron.bve.waardelijsten.ToepassingBeoordelingWerkstuk;
import nl.topicus.onderwijs.duo.bron.data.waardelijsten.examen.BeoordelingWerkstuk;
import nl.topicus.onderwijs.duo.bron.data.waardelijsten.examen.ExamenUitslag;
import nl.topicus.onderwijs.duo.bron.vo.batches.terugkoppeling.VOTerugkoppelingResultaat;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity
@DiscriminatorValue(value = "220")
public class BronVoExamenTerugkoppelMelding extends AbstractBronVoTerugkoppelMelding implements
		VOTerugkoppelingResultaat
{
	private static final long serialVersionUID = 1L;

	@Column(length = 4)
	private int examenJaar;

	@Column
	@Temporal(TemporalType.DATE)
	private Date datumUitslagExamen;

	@Column
	@Enumerated(EnumType.STRING)
	private ExamenUitslag uitslagExamen;

	@Column(length = 150)
	private String titelOfThemaWerkstuk;

	@Column
	@Enumerated(EnumType.STRING)
	private BeoordelingWerkstuk beoordelingWerkstuk;

	@Column
	@Enumerated(EnumType.STRING)
	private ToepassingBeoordelingWerkstuk toepassingBeoordelingWerkstuk;

	@Column(length = 3)
	private Integer cijferWerkstuk;

	@Transient
	private transient Integer combinatieCijfer;

	@BatchSize(size = 20)
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "examenMelding")
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
	private List<BronVoVakTerugkoppelMelding> vakMeldingen =
		new ArrayList<BronVoVakTerugkoppelMelding>();

	public int getExamenJaar()
	{
		return examenJaar;
	}

	public void setExamenJaar(int examenJaar)
	{
		this.examenJaar = examenJaar;
	}

	public Date getDatumUitslagExamen()
	{
		return datumUitslagExamen;
	}

	public void setDatumUitslagExamen(Date datumUitslagExamen)
	{
		this.datumUitslagExamen = datumUitslagExamen;
	}

	public ExamenUitslag getUitslagExamen()
	{
		return uitslagExamen;
	}

	public void setUitslagExamen(ExamenUitslag uitslagExamen)
	{
		this.uitslagExamen = uitslagExamen;
	}

	public String getTitelOfThemaWerkstuk()
	{
		return titelOfThemaWerkstuk;
	}

	public void setTitelOfThemaWerkstuk(String titelOfThemaWerkstuk)
	{
		this.titelOfThemaWerkstuk = titelOfThemaWerkstuk;
	}

	public BeoordelingWerkstuk getBeoordelingWerkstuk()
	{
		return beoordelingWerkstuk;
	}

	public void setBeoordelingWerkstuk(BeoordelingWerkstuk beoordelingWerkstuk)
	{
		this.beoordelingWerkstuk = beoordelingWerkstuk;
	}

	public ToepassingBeoordelingWerkstuk getToepassingBeoordelingWerkstuk()
	{
		return toepassingBeoordelingWerkstuk;
	}

	public void setToepassingBeoordelingWerkstuk(
			ToepassingBeoordelingWerkstuk toepassingBeoordelingWerkstuk)
	{
		this.toepassingBeoordelingWerkstuk = toepassingBeoordelingWerkstuk;
	}

	public Integer getCijferWerkstuk()
	{
		return cijferWerkstuk;
	}

	public void setCijferWerkstuk(Integer cijferWerkstuk)
	{
		this.cijferWerkstuk = cijferWerkstuk;
	}

	@Override
	public List<BronVoVakTerugkoppelMelding> getVakMeldingen()
	{
		return vakMeldingen;
	}

	public void setVakMeldingen(List<BronVoVakTerugkoppelMelding> vakMeldingen)
	{
		this.vakMeldingen = vakMeldingen;
	}

	@Override
	public Integer getCombinatieCijfer()
	{
		// Placeholder, moet vanaf 1 sept. 2010 gevuld worden
		return combinatieCijfer;
	}

	public void setCombinatieCijfer(Integer cijfer)
	{
		this.combinatieCijfer = cijfer;
	}

	@Override
	public List< ? extends Object> getSubRecords()
	{
		List<Object> subRecords = new ArrayList<Object>();
		subRecords.addAll(getSignalen());
		subRecords.addAll(getVakMeldingen());
		return subRecords;
	}
}