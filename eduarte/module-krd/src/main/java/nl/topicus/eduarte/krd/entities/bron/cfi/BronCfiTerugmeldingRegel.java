package nl.topicus.eduarte.krd.entities.bron.cfi;

import javax.persistence.*;

import nl.topicus.eduarte.entities.organisatie.InstellingEntiteit;
import nl.topicus.eduarte.entities.taxonomie.MBOLeerweg;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Index;

/**
 * Abstract base class voor alle CFI-terugmelding regels
 * 
 * @author vandekamp
 */
@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
@DiscriminatorColumn(length = 60)
public abstract class BronCfiTerugmeldingRegel extends InstellingEntiteit
{
	private static final long serialVersionUID = 1L;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false, name = "bestand")
	@Index(name = "idx_BronCfiRegel_bestand")
	private BronCfiTerugmelding cfiTerugmelding;

	@Enumerated(value = EnumType.STRING)
	@Column(nullable = false)
	private BronCfiRegelType regelType;

	@Column(nullable = true)
	private Long bsn;

	@Column(nullable = true)
	private String volgnummerInschrijving;

	@Column(nullable = false)
	private int regelnummer;

	@Column(nullable = false)
	private String creboCode;

	@Enumerated(value = EnumType.STRING)
	@Column(nullable = true)
	private MBOLeerweg leerweg;

	@Column(nullable = true)
	private String opmerking;

	@Column(nullable = true)
	private String opleiding;

	public BronCfiTerugmeldingRegel()
	{
	}

	public BronCfiTerugmeldingRegel(BronCfiRegelType regelType, String[] velden)
	{
		if (velden.length < 6)
			throw new IllegalArgumentException("Niet genoeg velden");
		this.regelType = regelType;
		regelnummer = Integer.valueOf(velden[1]);
		creboCode = velden[2];
		leerweg = MBOLeerweg.parse(velden[3]);
		if (BronCfiRegelType.SIN == regelType || BronCfiRegelType.SBL == regelType)
		{
			try
			{
				bsn = Long.valueOf(velden[4]);
			}
			catch (NumberFormatException e)
			{
				// Hier hoeft niets te gebeuren, het kan zijn dat de bsn niet geparsed kan
				// worden, en dan moet het veld gewoon null blijven
			}
			volgnummerInschrijving = velden[5];
		}
	}

	public BronCfiTerugmelding getCfiTerugmelding()
	{
		return cfiTerugmelding;
	}

	public void setCfiTerugmelding(BronCfiTerugmelding cfiTerugmelding)
	{
		this.cfiTerugmelding = cfiTerugmelding;
	}

	public BronCfiRegelType getRegelType()
	{
		return regelType;
	}

	public void setRegelType(BronCfiRegelType regelType)
	{
		this.regelType = regelType;
	}

	public int getRegelnummer()
	{
		return regelnummer;
	}

	public void setRegelnummer(int regelnummer)
	{
		this.regelnummer = regelnummer;
	}

	public String getCreboCode()
	{
		return creboCode;
	}

	public void setCreboCode(String creboCode)
	{
		this.creboCode = creboCode;
	}

	public MBOLeerweg getLeerweg()
	{
		return leerweg;
	}

	public void setLeerweg(MBOLeerweg leerweg)
	{
		this.leerweg = leerweg;
	}

	public Long getBsn()
	{
		return bsn;
	}

	public void setBsn(Long bsn)
	{
		this.bsn = bsn;
	}

	public String getVolgnummerInschrijving()
	{
		return volgnummerInschrijving;
	}

	public void setVolgnummerInschrijving(String volgnummerInschrijving)
	{
		this.volgnummerInschrijving = volgnummerInschrijving;
	}

	public void setOpmerking(String opmerking)
	{
		this.opmerking = opmerking;
	}

	public String getOpmerking()
	{
		return opmerking;
	}

	public void setOpleiding(String opleiding)
	{
		this.opleiding = opleiding;
	}

	public String getOpleiding()
	{
		return opleiding;
	}

}
