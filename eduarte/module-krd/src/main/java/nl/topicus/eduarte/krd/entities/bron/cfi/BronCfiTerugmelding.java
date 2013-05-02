package nl.topicus.eduarte.krd.entities.bron.cfi;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.*;

import nl.topicus.cobra.util.StringUtil;
import nl.topicus.eduarte.entities.organisatie.InstellingEntiteit;
import nl.topicus.eduarte.entities.personen.Medewerker;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Index;

/**
 * Verzamelentiteit voor alle cfi terugmeldingen, van BVE.
 * 
 * @author vandekamp
 */
@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
public class BronCfiTerugmelding extends InstellingEntiteit
{
	private static final long serialVersionUID = 1L;

	public static enum BronCFIStatus
	{
		InBehandeling,
		Verwerkt,
		Afgekeurd;

		@Override
		public String toString()
		{
			return StringUtil.convertCamelCase(name());
		}
	}

	@Column(nullable = false, length = 200)
	private String bestandsnaam;

	@Column(nullable = false, length = 3)
	private String sector;

	@Enumerated(value = EnumType.STRING)
	@Column(nullable = false)
	private BronCFIStatus status;

	@Temporal(value = TemporalType.DATE)
	@Column(nullable = true)
	private Date peildatum;

	@Temporal(value = TemporalType.TIMESTAMP)
	@Column(nullable = false)
	private Date inleesdatum;

	@Temporal(value = TemporalType.TIMESTAMP)
	@Column(nullable = true)
	private Date aanmaakdatum;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false, name = "ingelezenDoor")
	@Index(name = "idx_BronCfiTerug_ingelezenDoo")
	private Medewerker ingelezenDoor;

	@Column(nullable = false)
	private int controleTotaal;

	@Column(nullable = false)
	private int aantalSignalen;

	@Column(nullable = false)
	private int aantalConflicten;

	@Column(nullable = false)
	private int aantalBEK;

	@Column(nullable = false)
	private int aantalSAG;

	@Column(nullable = false)
	private int aantalSIN;

	@Column(nullable = false)
	private int aantalSBH;

	@Column(nullable = false)
	private int aantalSBL;

	@Column(nullable = false)
	private int aantalEXP;

	@BatchSize(size = 20)
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "cfiTerugmelding")
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
	private List<BronCfiTerugmeldingRegel> regels = new ArrayList<BronCfiTerugmeldingRegel>();

	public BronCfiTerugmelding()
	{
	}

	public String getBestandsnaam()
	{
		return bestandsnaam;
	}

	public void setBestandsnaam(String bestandsnaam)
	{
		this.bestandsnaam = bestandsnaam;
	}

	public Date getPeildatum()
	{
		return peildatum;
	}

	public void setPeildatum(Date peildatum)
	{
		this.peildatum = peildatum;
	}

	public Date getInleesdatum()
	{
		return inleesdatum;
	}

	public void setInleesdatum(Date inleesdatum)
	{
		this.inleesdatum = inleesdatum;
	}

	public Medewerker getIngelezenDoor()
	{
		return ingelezenDoor;
	}

	public void setIngelezenDoor(Medewerker ingelezenDoor)
	{
		this.ingelezenDoor = ingelezenDoor;
	}

	public void setStatus(BronCFIStatus status)
	{
		this.status = status;
	}

	public BronCFIStatus getStatus()
	{
		return status;
	}

	public void setAantalSignalen(int aantalSignalen)
	{
		this.aantalSignalen = aantalSignalen;
	}

	public int getAantalSignalen()
	{
		return aantalSignalen;
	}

	public void setAantalConflicten(int aantalConflicten)
	{
		this.aantalConflicten = aantalConflicten;
	}

	public int getAantalConflicten()
	{
		return aantalConflicten;
	}

	public int getAantalBEK()
	{
		return aantalBEK;
	}

	public void setAantalBEK(int aantalBEK)
	{
		this.aantalBEK = aantalBEK;
	}

	public int getAantalSAG()
	{
		return aantalSAG;
	}

	public void setAantalSAG(int aantalSAG)
	{
		this.aantalSAG = aantalSAG;
	}

	public int getAantalSIN()
	{
		return aantalSIN;
	}

	public void setAantalSIN(int aantalSIN)
	{
		this.aantalSIN = aantalSIN;
	}

	public int getAantalSBH()
	{
		return aantalSBH;
	}

	public void setAantalSBH(int aantalSBH)
	{
		this.aantalSBH = aantalSBH;
	}

	public int getAantalSBL()
	{
		return aantalSBL;
	}

	public void setAantalSBL(int aantalSBL)
	{
		this.aantalSBL = aantalSBL;
	}

	public void setRegels(List<BronCfiTerugmeldingRegel> regels)
	{
		this.regels = regels;
	}

	public List<BronCfiTerugmeldingRegel> getRegels()
	{
		return regels;
	}

	public void setSector(String sector)
	{
		this.sector = sector;
	}

	public String getSector()
	{
		return sector;
	}

	public void setAanmaakdatum(Date aanmaakdatum)
	{
		this.aanmaakdatum = aanmaakdatum;
	}

	public Date getAanmaakdatum()
	{
		return aanmaakdatum;
	}

	public void setControleTotaal(int controleTotaal)
	{
		this.controleTotaal = controleTotaal;
	}

	public int getControleTotaal()
	{
		return controleTotaal;
	}

	public void setAantalEXP(int aantalEXP)
	{
		this.aantalEXP = aantalEXP;
	}

	public int getAantalEXP()
	{
		return aantalEXP;
	}

}
