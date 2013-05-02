package nl.topicus.eduarte.krd.entities.bron.meldingen.terugkoppeling;

import javax.persistence.*;

import nl.topicus.eduarte.entities.organisatie.InstellingEntiteit;
import nl.topicus.eduarte.krd.entities.bron.meldingen.aanlevering.IBronMelding;
import nl.topicus.onderwijs.duo.bron.vo.batches.terugkoppeling.VOSignaal;
import nl.topicus.onderwijs.duo.bron.vo.waardelijsten.Ernst;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Index;

@Entity
@Table(name = "BRON_VO_SIGNAAL")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
@BatchSize(size = 20)
public class BronVoSignaal extends InstellingEntiteit implements VOSignaal, IBronSignaal
{
	private static final long serialVersionUID = 1L;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "vakMelding")
	@Index(name = "idx_BRON_VO_SIGN_vakMelding")
	@BatchSize(size = 20)
	private BronVoVakTerugkoppelMelding vakMelding;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "melding")
	@Index(name = "idx_BRON_VO_SIGN_melding")
	@BatchSize(size = 20)
	private AbstractBronVoTerugkoppelMelding melding;

	@Column(nullable = false)
	private Integer signaalcode;

	@Column(nullable = false, length = 100)
	private String omschrijvingSignaal;

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private Ernst ernst;

	@Column(nullable = false)
	private Boolean geaccordeerd;

	@Lob
	@Column(nullable = true)
	private String opmerking;

	public String getOmschrijvingSignaal()
	{
		return omschrijvingSignaal;
	}

	public void setOmschrijvingSignaal(String omschrijvingSignaal)
	{
		this.omschrijvingSignaal = omschrijvingSignaal;
	}

	public Ernst getErnst()
	{
		return ernst;
	}

	public void setErnst(Ernst ernst)
	{
		this.ernst = ernst;
	}

	public void setVakMelding(BronVoVakTerugkoppelMelding vakMelding)
	{
		this.vakMelding = vakMelding;
	}

	public BronVoVakTerugkoppelMelding getVakMelding()
	{
		return vakMelding;
	}

	public void setMelding(AbstractBronVoTerugkoppelMelding melding)
	{
		this.melding = melding;
	}

	public AbstractBronVoTerugkoppelMelding getMelding()
	{
		return melding;
	}

	public void setSignaalcode(Integer signaalcode)
	{
		this.signaalcode = signaalcode;
	}

	public Integer getSignaalcode()
	{
		return signaalcode;
	}

	public void setGeaccordeerd(Boolean geaccordeerd)
	{
		this.geaccordeerd = geaccordeerd;
	}

	public Boolean getGeaccordeerd()
	{
		return geaccordeerd;
	}

	public void setOpmerking(String opmerking)
	{
		this.opmerking = opmerking;
	}

	public String getOpmerking()
	{
		return opmerking;
	}

	@Override
	public IBronMelding getAanleverMelding()
	{
		return getMelding().getAanlevermelding();
	}

	@Override
	public IBronTerugkMelding getTerugkMelding()
	{
		return getMelding();
	}

}