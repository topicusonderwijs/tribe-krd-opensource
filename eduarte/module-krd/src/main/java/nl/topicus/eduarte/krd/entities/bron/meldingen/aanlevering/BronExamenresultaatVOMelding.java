package nl.topicus.eduarte.krd.entities.bron.meldingen.aanlevering;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.*;

import nl.topicus.cobra.util.Asserts;
import nl.topicus.eduarte.entities.BronMeldingOnderdeel;
import nl.topicus.eduarte.entities.examen.Examendeelname;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.krd.entities.bron.BronExamenverzameling;
import nl.topicus.eduarte.krd.entities.bron.meldingen.BronBatchVOExamengegevens;
import nl.topicus.onderwijs.duo.bron.data.waardelijsten.examen.BeoordelingWerkstuk;
import nl.topicus.onderwijs.duo.bron.data.waardelijsten.examen.ExamenUitslag;
import nl.topicus.onderwijs.duo.bron.data.waardelijsten.examen.ToepassingResultaat;
import nl.topicus.onderwijs.duo.bron.vo.batches.examen.VOExamenResultaat;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Index;

@Entity
@DiscriminatorValue(value = "120")
public class BronExamenresultaatVOMelding extends AbstractBronVOMelding implements
		VOExamenResultaat, IBronExamenMelding
{
	private static final long serialVersionUID = 1L;

	@Column(length = 6)
	private String postcode;

	@Column(length = 4)
	private int ILTCode;

	@Column
	private int examenJaar;

	@Column
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
	private ToepassingResultaat toepassingBeoordelingWerkstuk;

	@ManyToOne
	@JoinColumn(name = "examenverzameling")
	@Index(name = "idx_BRON_VO_EXRES_MELD_exverz")
	private BronExamenverzameling examenverzameling;

	@Column
	private Integer cijferWerkstuk;

	@BatchSize(size = 20)
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "examenresultaat", cascade = CascadeType.ALL)
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
	private List<BronVakGegegevensVOMelding> vakgegevens =
		new ArrayList<BronVakGegegevensVOMelding>();

	@Transient
	private boolean verwijderd;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = true, name = "examendeelname")
	@Index(name = "idx_BronExMeld_exDeeln")
	private Examendeelname examendeelname;

	public BronExamenresultaatVOMelding()
	{
	}

	public BronExamenresultaatVOMelding(BronBatchVOExamengegevens bronBatchVO)
	{
		super(bronBatchVO);
		setBekostigingsRelevant(false);
	}

	public String getPostcode()
	{
		return postcode;
	}

	public void setPostcode(String postcode)
	{
		String postcodeZonderSpaties = (postcode != null) ? postcode.replaceAll(" ", "") : postcode;
		Asserts.assertMaxLength("postcode", postcodeZonderSpaties, 6);
		this.postcode = postcodeZonderSpaties;
	}

	public int getILTCode()
	{
		return ILTCode;
	}

	public void setILTCode(int code)
	{
		ILTCode = code;
	}

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

	public ToepassingResultaat getToepassingBeoordelingWerkstuk()
	{
		return toepassingBeoordelingWerkstuk;
	}

	public void setToepassingBeoordelingWerkstuk(ToepassingResultaat toepassingBeoordelingWerkstuk)
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

	public List<BronVakGegegevensVOMelding> getVakgegevens()
	{
		return vakgegevens;
	}

	public void setVakgegevens(List<BronVakGegegevensVOMelding> bronVakGegegevensVOMeldingen)
	{
		this.vakgegevens = bronVakGegegevensVOMeldingen;
	}

	@Override
	public List< ? extends Object> getVakGegevens()
	{
		return vakgegevens;
	}

	@Override
	public BronBatchVOExamengegevens getBatch()
	{
		return (BronBatchVOExamengegevens) super.getBatch();
	}

	@Override
	public List<BronMeldingOnderdeel> getBronMeldingOnderdelen()
	{
		List<BronMeldingOnderdeel> onderdelen = new ArrayList<BronMeldingOnderdeel>();
		onderdelen.add(BronMeldingOnderdeel.VOExamen);
		return onderdelen;
	}

	public BronVakGegegevensVOMelding getExamenvak(int examenVak)
	{
		for (BronVakGegegevensVOMelding vakGegeven : getVakgegevens())
		{
			if (vakGegeven.getExamenVak() == examenVak)
				return vakGegeven;
		}
		return null;
	}

	public void setExamenverzameling(BronExamenverzameling examenverzameling)
	{
		this.examenverzameling = examenverzameling;
	}

	public BronExamenverzameling getExamenverzameling()
	{
		return examenverzameling;
	}

	@Override
	public int getAantalVakken()
	{
		return getVakgegevens().size();
	}

	@Override
	public String getExamenCode()
	{
		return String.valueOf(getILTCode());
	}

	@Override
	public Date getIngangsDatum()
	{
		return null;
	}

	@Override
	public List<IVakMelding> getVakMeldingen()
	{
		List<IVakMelding> vakMeldingen = new ArrayList<IVakMelding>();
		for (BronVakGegegevensVOMelding vakMelding : getVakgegevens())
		{
			vakMeldingen.add(vakMelding);
		}
		return vakMeldingen;
	}

	@Override
	public boolean isVerwijderd()
	{
		return verwijderd;
	}

	@Override
	public void setVerwijderd(boolean verwijderd)
	{
		this.verwijderd = verwijderd;
	}

	@Override
	public void voegOpnieuwToeAanWachtrij()
	{
		// Examenmeldingen worden niet direct in de wachtrij gezet, maar aangemerkt om
		// naar BRON verstuurd te worden. Hierdoor worden ze meegenomen in de volgende
		// batch.
		Verbintenis verbintenis = getVerbintenis();
		String examenCode = getExamenCode();
		int jaar = getExamenJaar();
		for (Examendeelname examen : verbintenis.getExamendeelnames())
		{
			if (examen.getExamenjaar() == jaar && verbintenis.getExterneCode().equals(examenCode))
			{
				examen.setGewijzigd(true);
				examen.saveOrUpdate();
			}
		}
	}

	public void setExamendeelname(Examendeelname examendeelname)
	{
		this.examendeelname = examendeelname;
	}

	public Examendeelname getExamendeelname()
	{
		return examendeelname;
	}

	@Override
	public Integer getCijferCombinatieVak()
	{
		// TODO Auto-generated method stub
		return null;
	}
}
