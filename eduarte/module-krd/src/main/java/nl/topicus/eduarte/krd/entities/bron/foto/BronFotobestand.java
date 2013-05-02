package nl.topicus.eduarte.krd.entities.bron.foto;

import java.util.Date;

import javax.persistence.*;

import nl.topicus.cobra.util.StringUtil;
import nl.topicus.eduarte.entities.organisatie.InstellingEntiteit;
import nl.topicus.eduarte.entities.personen.Medewerker;
import nl.topicus.onderwijs.duo.bron.bve.foto.pve_9_9.BronFotoType;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Index;

/**
 * Verzamelentiteit voor alle bron fotobestanden, zowel van BVE als VO.
 * 
 * @author loite
 */
@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
public class BronFotobestand extends InstellingEntiteit
{
	private static final long serialVersionUID = 1L;

	public static enum BronFotoVerwerkingsstatus
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

	public static enum BronFotoStatus
	{
		Foto1("1")
		{
			@Override
			public String toString()
			{
				return "1ste foto";
			}
		},
		Foto2("2")
		{
			@Override
			public String toString()
			{
				return "2de foto";
			}
		},
		Voorlopig("V"),
		Definitief("D"),
		// TODO: Opzoeken wat deze status precies inhoudt.
		A("A");

		private String code;

		private BronFotoStatus(String code)
		{
			this.code = code;
		}

		public String getCode()
		{
			return code;
		}

		public static BronFotoStatus parse(String zoekcode)
		{
			for (BronFotoStatus waarde : BronFotoStatus.values())
			{
				if (waarde.getCode().equalsIgnoreCase(zoekcode))
					return waarde;
			}
			return null;
		}
	}

	@Column(nullable = false, length = 200)
	private String bestandsnaam;

	@Enumerated(value = EnumType.STRING)
	@Column(nullable = true)
	private BronFotoVerwerkingsstatus verwerkingsstatus;

	@Enumerated(value = EnumType.STRING)
	@Column(nullable = true)
	private BronFotoStatus status;

	@Enumerated(value = EnumType.STRING)
	@Column(nullable = true)
	private BronFotoType type;

	@Temporal(value = TemporalType.DATE)
	@Column(nullable = true)
	private Date peildatum;

	@Temporal(value = TemporalType.DATE)
	@Column(nullable = true)
	private Date aanmaakdatum;

	@Temporal(value = TemporalType.TIMESTAMP)
	@Column(nullable = false)
	private Date inleesdatum;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false, name = "ingelezenDoor")
	@Index(name = "idx_BronFotobest_ingelezenDoo")
	private Medewerker ingelezenDoor;

	@Column(nullable = true)
	private Integer aantalVerschillen;

	@Column(nullable = true)
	private Long controletotaalAccountant;

	public BronFotobestand()
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

	public BronFotoVerwerkingsstatus getVerwerkingsstatus()
	{
		return verwerkingsstatus;
	}

	public void setVerwerkingsstatus(BronFotoVerwerkingsstatus verwerkingsstatus)
	{
		this.verwerkingsstatus = verwerkingsstatus;
	}

	public BronFotoStatus getStatus()
	{
		return status;
	}

	public void setStatus(BronFotoStatus status)
	{
		this.status = status;
	}

	public BronFotoType getType()
	{
		return type;
	}

	public void setType(BronFotoType type)
	{
		this.type = type;
	}

	public Date getPeildatum()
	{
		return peildatum;
	}

	public void setPeildatum(Date peildatum)
	{
		this.peildatum = peildatum;
	}

	public Date getAanmaakdatum()
	{
		return aanmaakdatum;
	}

	public void setAanmaakdatum(Date aanmaakdatum)
	{
		this.aanmaakdatum = aanmaakdatum;
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

	public Integer getAantalVerschillen()
	{
		return aantalVerschillen;
	}

	public void setAantalVerschillen(Integer aantalVerschillen)
	{
		this.aantalVerschillen = aantalVerschillen;
	}

	public void setControletotaalAccountant(Long controletotaalAccountant)
	{
		this.controletotaalAccountant = controletotaalAccountant;
	}

	public Long getControletotaalAccountant()
	{
		return controletotaalAccountant;
	}

}
