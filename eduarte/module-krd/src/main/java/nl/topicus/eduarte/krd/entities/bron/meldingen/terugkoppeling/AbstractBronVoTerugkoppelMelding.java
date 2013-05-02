package nl.topicus.eduarte.krd.entities.bron.meldingen.terugkoppeling;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.*;

import nl.topicus.cobra.entities.RestrictedAccess;
import nl.topicus.cobra.types.personalia.Geslacht;
import nl.topicus.eduarte.entities.organisatie.InstellingEntiteit;
import nl.topicus.eduarte.krd.entities.bron.meldingen.aanlevering.AbstractBronVOMelding;
import nl.topicus.onderwijs.duo.bron.vo.waardelijsten.SoortMutatie;
import nl.topicus.onderwijs.duo.bron.vo.waardelijsten.StatusMelding;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Index;

@Entity
@Table(name = "BRON_VO_TERUGKOPPELMELDINGEN")
@DiscriminatorColumn(discriminatorType = DiscriminatorType.INTEGER)
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
@BatchSize(size = 20)
public abstract class AbstractBronVoTerugkoppelMelding extends InstellingEntiteit implements
		IBronTerugkMelding
{
	private static final long serialVersionUID = 1L;

	@SuppressWarnings("unused")
	@Column(nullable = false, insertable = false, updatable = false)
	@RestrictedAccess(hasGetter = false, hasSetter = false)
	private Integer DTYPE;

	@Column(nullable = false)
	private int vestigingsVolgnummer;

	@Column(nullable = false)
	private int meldingNummer;

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private SoortMutatie soortMutatie;

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private StatusMelding statusMelding;

	@Column
	private Integer sofiNummer;

	@Column
	private Integer onderwijsNummer;

	@Column(nullable = false, length = 10)
	private String leerlingNummerInstelling;

	@Column(nullable = true, name = "datumGeboorte")
	@Temporal(TemporalType.DATE)
	private Date geboorteDatum;

	@Column(nullable = true)
	@Enumerated(EnumType.STRING)
	private Geslacht geslacht;

	@Column(length = 6)
	private String postcode;

	@Column(nullable = true)
	private Integer iLTCode;

	@ManyToOne
	@JoinColumn(nullable = true, name = "batchgegevens")
	@Index(name = "idx_BRON_VO_TERUGK_batch")
	private BronVoBatchgegevens batchgegevens;

	@OneToOne
	@JoinColumn(nullable = true, name = "aanlevermelding")
	@Index(name = "idx_BRON_VO_TERUGK_aanleverm")
	private AbstractBronVOMelding aanlevermelding;

	@BatchSize(size = 20)
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "melding")
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
	private List<BronVoSignaal> signalen = new ArrayList<BronVoSignaal>();

	public int getVestigingsVolgnummer()
	{
		return vestigingsVolgnummer;
	}

	public void setVestigingsVolgnummer(int vestigingsVolgnummer)
	{
		this.vestigingsVolgnummer = vestigingsVolgnummer;
	}

	public SoortMutatie getSoortMutatie()
	{
		return soortMutatie;
	}

	public void setSoortMutatie(SoortMutatie soortMutatie)
	{
		this.soortMutatie = soortMutatie;
	}

	public StatusMelding getStatusMelding()
	{
		return statusMelding;
	}

	public void setStatusMelding(StatusMelding statusMelding)
	{
		this.statusMelding = statusMelding;
	}

	public Integer getSofiNummer()
	{
		return sofiNummer;
	}

	public void setSofiNummer(Integer sofiNummer)
	{
		this.sofiNummer = sofiNummer;
	}

	public Integer getOnderwijsNummer()
	{
		return onderwijsNummer;
	}

	public void setOnderwijsNummer(Integer onderwijsNummer)
	{
		this.onderwijsNummer = onderwijsNummer;
	}

	public String getLeerlingNummerInstelling()
	{
		return leerlingNummerInstelling;
	}

	public void setLeerlingNummerInstelling(String leerlingNummerInstelling)
	{
		this.leerlingNummerInstelling = leerlingNummerInstelling;
	}

	public Geslacht getGeslacht()
	{
		return geslacht;
	}

	public void setGeslacht(Geslacht geslacht)
	{
		this.geslacht = geslacht;
	}

	public Integer getILTCode()
	{
		return iLTCode;
	}

	public void setILTCode(Integer code)
	{
		iLTCode = code;
	}

	public void setBatchgegevens(BronVoBatchgegevens batchgegevens)
	{
		this.batchgegevens = batchgegevens;
	}

	public BronVoBatchgegevens getBatchgegevens()
	{
		return batchgegevens;
	}

	public void setSignalen(List<BronVoSignaal> signalen)
	{
		this.signalen = signalen;
	}

	public List<BronVoSignaal> getSignalen()
	{
		return signalen;
	}

	public void setAanlevermelding(AbstractBronVOMelding aanlevermelding)
	{
		this.aanlevermelding = aanlevermelding;
	}

	public AbstractBronVOMelding getAanlevermelding()
	{
		return aanlevermelding;
	}

	public void setGeboorteDatum(Date geboorteDatum)
	{
		this.geboorteDatum = geboorteDatum;
	}

	public Date getGeboorteDatum()
	{
		return geboorteDatum;
	}

	public void setPostcode(String postcode)
	{
		this.postcode = postcode;
	}

	public String getPostcode()
	{
		return postcode;
	}

	public void setMeldingNummer(int meldingNummer)
	{
		this.meldingNummer = meldingNummer;
	}

	public int getMeldingNummer()
	{
		return meldingNummer;
	}

	public Integer getTerugkoppelNummer()
	{
		return getMeldingNummer();
	}

	@Override
	public Integer getBatchNummer()
	{
		if (getBatchgegevens() != null)
			return getBatchgegevens().getBatchNummer();
		return null;
	}

	@Override
	public String getBestandsnaam()
	{
		if (getBatchgegevens() != null && getBatchgegevens().getTerugkoppelbestand() != null)
			return getBatchgegevens().getTerugkoppelbestand().getBestandsnaam();
		return null;
	}
}