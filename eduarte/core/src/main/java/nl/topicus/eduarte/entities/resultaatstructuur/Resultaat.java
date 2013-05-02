package nl.topicus.eduarte.entities.resultaatstructuur;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.persistence.*;

import nl.topicus.cobra.converters.BigDecimalConverter;
import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.templates.annotations.Exportable;
import nl.topicus.cobra.util.DecimalUtil;
import nl.topicus.cobra.util.HibernateObjectCopyManager;
import nl.topicus.cobra.util.StringUtil;
import nl.topicus.cobra.util.TimeUtil;
import nl.topicus.cobra.util.inwords.Dutch;
import nl.topicus.eduarte.dao.helpers.ResultaatDataAccessHelper;
import nl.topicus.eduarte.entities.organisatie.InstellingEntiteit;
import nl.topicus.eduarte.entities.personen.Deelnemer;
import nl.topicus.eduarte.entities.personen.Medewerker;
import nl.topicus.eduarte.entities.resultaatstructuur.Schaal.Schaaltype;
import nl.topicus.eduarte.entities.resultaatstructuur.Toets.SoortToets;
import nl.topicus.onderwijs.duo.bron.Bron;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Index;

@Exportable()
@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
public class Resultaat extends InstellingEntiteit
{
	private static final long serialVersionUID = 1L;

	public static enum Resultaatsoort
	{
		/**
		 * Door een docent ingevoerd
		 */
		Ingevoerd,
		/**
		 * Handmatig overschreven
		 */
		Overschreven,
		/**
		 * Het resultaat is via een toetsverwijzing geplaatst
		 */
		Verwezen,
		/**
		 * Automatisch berekend uit onderliggende resultaten
		 */
		Berekend,
		/**
		 * Geen geldig resultaat, omdat niet voldaan is aan de eisen van onderliggende
		 * resultaten
		 */
		Tijdelijk,
		/**
		 * Alternatief resultaat
		 */
		Alternatief;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false, name = "deelnemer")
	@Index(name = "idx_Resultaat_deelnemer")
	private Deelnemer deelnemer;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false, name = "toets")
	@Index(name = "idx_Resultaat_toets")
	private Toets toets;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = true, name = "waarde")
	@Index(name = "idx_Resultaat_waarde")
	@Bron
	private Schaalwaarde waarde;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = true, name = "overschrijft")
	@Index(name = "idx_Resultaat_overschrijft")
	private Resultaat overschrijft;

	@Column(nullable = true)
	private Integer score;

	@Column(nullable = true, scale = 10, precision = 20)
	@Bron
	private BigDecimal cijfer;

	@Column(nullable = true, scale = 10, precision = 20)
	private BigDecimal onafgerondCijfer;

	@Column(nullable = false)
	private int studiepunten;

	/**
	 * Het geldende resultaat is het resultaat dat voor de deelnemer voor deze toets
	 * geldt, oftewel na alle berekeningen met herkansingen, alternatieve resultaten etc.
	 * is dit het resultaat dat eruit komt. Een deelnemer heeft per toets max 1 geldend
	 * resultaat.
	 */
	@Column(nullable = false)
	private boolean geldend;

	@Column(nullable = false)
	private int herkansingsnummer;

	/**
	 * Een deelnemer kan per resultaatsoort 1 actueel resultaat hebben. Dit is het laatst
	 * ingevoerde/berekende resultaat voor de resultaatsoort bij deze toets. Dit komt
	 * *NIET* altijd overeen met het geldende resultaat. Daarvan kan een deelnemer per
	 * toets max 1 hebben, en is het resultaat dat gebruikt moet worden in verdere
	 * berekeningen. De indicatie 'actueel' heeft dus alleen betekenis binnen de toets
	 * zelf.
	 */
	@Column(nullable = false)
	private boolean actueel;

	@Column(nullable = false)
	private boolean inSamengesteld = false;

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private Resultaatsoort soort;

	@Column(nullable = true)
	@Temporal(TemporalType.DATE)
	private Date datumBehaald;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = true, name = "ingevoerdDoor")
	@Index(name = "idx_Resultaat_ingevoerdDoor")
	private Medewerker ingevoerdDoor;

	@Column(nullable = true)
	@Lob
	private String notitie;

	@Column(nullable = true)
	@Lob
	private String berekening;

	@Column(nullable = true)
	private Integer weging;

	@SuppressWarnings("unchecked")
	@Bron
	@Transient
	private Enum handmatigVersturenNaarBronMutatie = null;

	public Resultaat()
	{
	}

	public Deelnemer getDeelnemer()
	{
		return deelnemer;
	}

	public void setDeelnemer(Deelnemer deelnemer)
	{
		this.deelnemer = deelnemer;
	}

	@Exportable
	public Toets getToets()
	{
		return toets;
	}

	public void setToets(Toets toets)
	{
		this.toets = toets;
	}

	public Schaalwaarde getWaarde()
	{
		return waarde;
	}

	public void setWaarde(Schaalwaarde waarde)
	{
		this.waarde = waarde;
		setCijfer(waarde == null ? null : waarde.getNominaleWaarde());
	}

	public Resultaat getOverschrijft()
	{
		return overschrijft;
	}

	public void setOverschrijft(Resultaat overschrijft)
	{
		this.overschrijft = overschrijft;
	}

	public BigDecimal getCijfer()
	{
		return cijfer;
	}

	public void setCijfer(BigDecimal cijfer)
	{
		this.cijfer = cijfer;
	}

	public BigDecimal getOnafgerondCijfer()
	{
		return onafgerondCijfer == null ? getCijfer() : onafgerondCijfer;
	}

	public void setOnafgerondCijfer(BigDecimal onafgerondCijfer)
	{
		this.onafgerondCijfer = onafgerondCijfer;
	}

	public int getStudiepunten()
	{
		return studiepunten;
	}

	public void setStudiepunten(int studiepunten)
	{
		this.studiepunten = studiepunten;
	}

	public void setScore(Integer score)
	{
		this.score = score;
	}

	public Integer getScore()
	{
		return score;
	}

	public boolean isGeldend()
	{
		return geldend;
	}

	public void setGeldend(boolean geldend)
	{
		this.geldend = geldend;
	}

	public int getHerkansingsnummer()
	{
		return herkansingsnummer;
	}

	public void setHerkansingsnummer(int herkansingsnummer)
	{
		this.herkansingsnummer = herkansingsnummer;
	}

	public boolean isActueel()
	{
		return actueel;
	}

	public void setActueel(boolean actueel)
	{
		this.actueel = actueel;
	}

	public boolean isInSamengesteld()
	{
		return inSamengesteld;
	}

	public void setInSamengesteld(boolean inSamengesteld)
	{
		this.inSamengesteld = inSamengesteld;
	}

	public void setSoort(Resultaatsoort soort)
	{
		this.soort = soort;
	}

	public Resultaatsoort getSoort()
	{
		return soort;
	}

	public void setDatumBehaald(Date datumBehaald)
	{
		this.datumBehaald = datumBehaald;
	}

	@Exportable()
	public Date getDatumBehaald()
	{
		return datumBehaald;
	}

	public void setIngevoerdDoor(Medewerker ingevoerdDoor)
	{
		this.ingevoerdDoor = ingevoerdDoor;
	}

	@Exportable()
	public Medewerker getIngevoerdDoor()
	{
		return ingevoerdDoor;
	}

	public void setNotitie(String notitie)
	{
		this.notitie = notitie;
	}

	@Exportable()
	public String getNotitie()
	{
		return notitie;
	}

	public void setBerekening(String berekening)
	{
		this.berekening = berekening;
	}

	public String getBerekening()
	{
		return berekening;
	}

	public void setWeging(Integer weging)
	{
		this.weging = weging;
	}

	public Integer getWeging()
	{
		return weging;
	}

	public Integer getWegingVoorBerekening()
	{
		return getWeging() == null ? getToets().getWeging() : getWeging();
	}

	public void appendBerekening(String addendum)
	{
		if (getBerekening() == null)
			setBerekening(addendum);
		else
			setBerekening(getBerekening() + "\n" + addendum);
	}

	public boolean isCijferUpdateRequired()
	{
		return isZonderCijferOfWaarde()
			&& getToets().isCijferBerekenbaar(getScore(), getHerkansingsnummer() + 1);
	}

	public void setCijferOfWaarde(Object object)
	{
		if (object == null)
		{
			setWaarde(null);
			setCijfer(null);
			return;
		}

		if (getToets().getSchaal().getSchaaltype().equals(Schaaltype.Cijfer))
		{
			BigDecimal newCijfer = (BigDecimal) object;
			setCijfer(newCijfer.setScale(getToets().getSchaal().getAantalDecimalen(),
				RoundingMode.HALF_UP));
		}
		else
		{
			for (Schaalwaarde curWaarde : getToets().getSchaal().getSchaalwaarden())
			{
				if (curWaarde.equals(object)
					|| curWaarde.getInterneWaarde().equalsIgnoreCase(object.toString()))
				{
					setWaarde(curWaarde);
					break;
				}
			}
		}
	}

	public void setCijferOfWaardeUitScore(int score)
	{
		if (getToets().getSchaal().getSchaaltype().equals(Schaaltype.Cijfer))
		{
			setCijfer(getToets().berekenCijfer(score, getHerkansingsnummer() + 1));
		}
		else
		{
			setWaarde(getToets().berekenWaarde(score));
		}
	}

	public boolean isHogerDan(Resultaat other)
	{
		if (isNullResultaat())
			return false;
		if (other.isNullResultaat())
			return true;

		if (isZonderCijferOfWaarde())
			return false;
		if (other.isZonderCijferOfWaarde())
			return true;

		boolean thisBehaald = isBehaald();
		boolean otherBehaald = other.isBehaald();
		if (thisBehaald != otherBehaald)
			return thisBehaald;

		boolean thisTijdelijk = getSoort().equals(Resultaatsoort.Tijdelijk);
		boolean otherTijdelijk = other.getSoort().equals(Resultaatsoort.Tijdelijk);
		if (thisTijdelijk != otherTijdelijk)
			return otherTijdelijk;

		if (getToets().getSchaal().getSchaaltype().equals(Schaaltype.Cijfer))
		{
			if (getCijfer().compareTo(other.getCijfer()) == 0)
			{
				if (getOnafgerondCijfer().compareTo(other.getOnafgerondCijfer()) == 0)
					return isLaterDan(other);
				return getOnafgerondCijfer().compareTo(other.getOnafgerondCijfer()) > 0;
			}
			return getCijfer().compareTo(other.getCijfer()) > 0;
		}
		else
		{
			if (getWaarde().equals(other.getWaarde()))
				return isLaterDan(other);
			return getWaarde().getVolgnummer() > other.getWaarde().getVolgnummer();
		}
	}

	public boolean isLaterDan(Resultaat other)
	{
		return getHerkansingsnummer() > other.getHerkansingsnummer();
	}

	public boolean isNullResultaat()
	{
		return getCijfer() == null && getWaarde() == null && getScore() == null;
	}

	public boolean isZonderCijferOfWaarde()
	{
		return getCijfer() == null && getWaarde() == null && getScore() != null;
	}

	public boolean isNullOfZonderCijferOfWaarde()
	{
		return isNullResultaat() || isZonderCijferOfWaarde();
	}

	public boolean isOverschreven()
	{
		return getSoort() == Resultaatsoort.Overschreven;
	}

	public boolean isGefixeerd()
	{
		return getSoort() == Resultaatsoort.Overschreven || getSoort() == Resultaatsoort.Verwezen;
	}

	public boolean isIngevuld()
	{
		return !isNullResultaat() && !getSoort().equals(Resultaatsoort.Tijdelijk);
	}

	public boolean isBehaald()
	{
		if (isNullResultaat() || isZonderCijferOfWaarde() || isCijferUpdateRequired())
			return false;
		if (isTijdelijk())
			return false;

		if (getToets() == null && getCijfer() != null)
		{
			// Geen toets maar wel cijfer --> afgeleide productregel
			return getCijfer().compareTo(DecimalUtil.FIVE_POINT_FIVE) >= 0;
		}

		if (getToets().getSchaal().getSchaaltype().equals(Schaaltype.Cijfer))
		{
			if (getCijfer() == null)
				return false;
			return getCijfer().compareTo(getToets().getSchaal().getMinimumVoorBehaald()) >= 0;
		}
		if (getWaarde() == null)
			return false;
		return getWaarde().isBehaald();
	}

	public boolean isCompenseerbaar()
	{
		if (getToets() == null || getToets().getCompenseerbaarVanaf() == null)
			return true;
		if (isNullResultaat() || isZonderCijferOfWaarde() || isCijferUpdateRequired())
			return false;
		if (isTijdelijk())
			return false;
		if (getCijfer() == null)
			return false;
		return getCijfer().compareTo(getToets().getCompenseerbaarVanaf()) >= 0;
	}

	public boolean isTijdelijk()
	{
		return getSoort().equals(Resultaatsoort.Tijdelijk);
	}

	/**
	 * 
	 * @return Het volgnummer van de schaalwaarde van dit resultaat indien het resultaat
	 *         een tabel gebruikt, en anders -1.
	 */
	public int getVolgnummer()
	{
		if (getWaarde() != null)
		{
			return getWaarde().getVolgnummer();
		}
		return -1;
	}

	public void setHandmatigVersturenNaarBron(Enum< ? > soortMutatie)
	{
		this.handmatigVersturenNaarBronMutatie = soortMutatie;
	}

	public nl.topicus.onderwijs.duo.bron.bve.waardelijsten.SoortMutatie getHandmatigeBronBveSoortMutatie()
	{
		if (handmatigVersturenNaarBronMutatie instanceof nl.topicus.onderwijs.duo.bron.bve.waardelijsten.SoortMutatie)
		{
			return (nl.topicus.onderwijs.duo.bron.bve.waardelijsten.SoortMutatie) handmatigVersturenNaarBronMutatie;
		}
		return null;
	}

	public nl.topicus.onderwijs.duo.bron.vo.waardelijsten.SoortMutatie getHandmatigeBronVoSoortMutatie()
	{
		if (handmatigVersturenNaarBronMutatie instanceof nl.topicus.onderwijs.duo.bron.vo.waardelijsten.SoortMutatie)
		{
			return (nl.topicus.onderwijs.duo.bron.vo.waardelijsten.SoortMutatie) handmatigVersturenNaarBronMutatie;
		}
		return null;
	}

	public boolean isHandmatigVersturenNaarBron()
	{
		return handmatigVersturenNaarBronMutatie != null;
	}

	/**
	 * 
	 * @return true indien dit resultaat gekoppeld is aan een tekstuele schaal.
	 */
	public boolean isTekstueel()
	{
		return getToets() != null && getToets().getSchaal().getSchaaltype() == Schaaltype.Tekstueel;
	}

	public String toJSON(boolean altijdCijfer)
	{
		StringBuilder sb = new StringBuilder();
		sb.append('{');
		sb.append("ingevoerd:\"").append(
			StringUtil.escapeForJavascriptString(getIngevoerdDoor() == null ? "onbekend"
				: getIngevoerdDoor().getPersoon().getFormeleNaam())).append("\",");
		sb.append("cijfer:\"").append(
			StringUtil.escapeForJavascriptString(getFormattedDisplayWaarde(altijdCijfer))).append(
			"\",");
		sb.append("datum:\"").append(TimeUtil.getInstance().formatDate(getDatumBehaald())).append(
			'\"');
		sb.append('}');
		return sb.toString();
	}

	public static String resultatenToJSON(List<Resultaat> resultaten, boolean altijdCijfer)
	{
		StringBuilder sb = new StringBuilder();
		sb.append('[');
		for (Resultaat curResultaat : resultaten)
		{
			if (sb.length() > 1)
				sb.append(',');
			sb.append(curResultaat.toJSON(altijdCijfer));
		}
		sb.append(']');
		return sb.toString();
	}

	public Object getDisplayWaarde(boolean altijdCijfer)
	{
		if (altijdCijfer || getScore() == null)
		{
			if (getWaarde() != null)
				return getWaarde().toString();
			return getCijfer();
		}
		return getScore();
	}

	@Exportable
	public String getCijferAlsWoord()
	{
		if (getWaarde() != null)
			return getWaarde().getNaam();
		if (getCijfer() != null)
		{
			Dutch dutch = new Dutch();
			return dutch.toWords(getCijfer().longValue());
		}
		return null;
	}

	@Exportable
	public String getCijferOfSchaalwaarde()
	{
		if (getWaarde() != null)
			return getWaarde().getNaam();
		return getFormattedDisplayWaarde(true);
	}

	@Exportable
	public String getFormattedDisplayWaarde()
	{
		return getFormattedDisplayWaarde(false);
	}

	@Exportable
	public String getFormattedDisplayCijfer()
	{
		return getFormattedDisplayWaarde(true);
	}

	@Exportable
	public String getBehaaldInWoorden()
	{
		if (isBehaald())
			return "Behaald";
		return "Niet behaald";
	}

	public String getFormattedDisplayWaarde(boolean altijdCijfer)
	{
		Object ret = getDisplayWaarde(altijdCijfer);
		if (ret == null)
			return "";
		if (ret instanceof BigDecimal)
		{
			return formatCijfer((BigDecimal) ret);
		}
		return ret.toString();
	}

	public String getFormattedNominaleWaarde()
	{
		return formatCijfer(getCijfer());
	}

	private String formatCijfer(BigDecimal ret)
	{
		if (getToets() != null && getToets().getSchaal().getAantalDecimalen() != null)
		{
			return new BigDecimalConverter(getToets().getSchaal().getAantalDecimalen())
				.convertToString(ret, Locale.getDefault());
		}
		else
		{
			return new BigDecimalConverter(0).convertToString(ret, Locale.getDefault());
		}
	}

	public boolean overschrijft(Resultaat other)
	{
		if (getOverschrijft() == null)
			return false;
		if (other.equals(getOverschrijft()))
			return true;
		return getOverschrijft().overschrijft(other);
	}

	public Resultaat copy()
	{
		HibernateObjectCopyManager copyManager = new HibernateObjectCopyManager(Resultaat.class)
		{
			@Override
			public boolean getMustCopyField(Field field)
			{
				if (field.getName().equals("overschrijft"))
					return false;
				return super.getMustCopyField(field);
			}
		};
		Resultaat ret = copyManager.copyObject(this);
		ret.setOverschrijft(null);
		return ret;
	}

	@Override
	public String toString()
	{
		StringBuilder resultaatFlags = new StringBuilder();
		resultaatFlags.append(formatFlag(isActueel(), 'a'));
		resultaatFlags.append(formatFlag(isBehaald(), 'b'));
		resultaatFlags.append(formatFlag(isCompenseerbaar(), 'c'));
		resultaatFlags.append(formatFlag(isGefixeerd(), 'f'));
		resultaatFlags.append(formatFlag(isGeldend(), 'g'));
		resultaatFlags.append(formatFlag(isNullResultaat(), 'n'));
		resultaatFlags.append(formatFlag(isOverschreven(), 'o'));
		resultaatFlags.append(formatFlag(isInSamengesteld(), 's'));
		resultaatFlags.append(formatFlag(isCijferUpdateRequired(), 'u'));
		resultaatFlags.append(formatFlag(isZonderCijferOfWaarde(), 'z'));
		String herkansingNr;
		if (getSoort() == Resultaatsoort.Alternatief)
			herkansingNr = "A";
		else
			herkansingNr = Integer.toString(getHerkansingsnummer() + 1);
		return "D(" + getDeelnemer().getDeelnemernummer() + ") R(" + getId() + ")["
			+ resultaatFlags + "] "
			+ getToets().getResultaatstructuur().getOnderwijsproduct().getCode() + "-"
			+ getToets().getCode() + " P" + herkansingNr + "("
			+ (getSoort() == null ? '-' : getSoort().toString().charAt(0)) + ")="
			+ (isNullResultaat() ? "-" : "'" + getFormattedDisplayWaarde(false) + "'");
	}

	public static char formatFlag(boolean value, char flag)
	{
		return value ? Character.toUpperCase(flag) : flag;
	}

	@Exportable()
	public Date getDatumLaatsteWijziging()
	{
		return getLastModifiedAt();
	}

	public String getResultaatVoorSoortToets(SoortToets soortToets)
	{
		for (Toets to : getToets().getChildren())
		{
			if (to.getSoort().equals(soortToets))
			{
				List<Resultaat> resultaten =
					DataAccessRegistry.getHelper(ResultaatDataAccessHelper.class)
						.getActueleResultaten(to, deelnemer);
				for (Resultaat resultaat : resultaten)
				{
					if (resultaat.isGeldend())
					{
						return resultaat.getWaarde().getInterneWaarde();
					}
				}
			}
		}
		return "";
	}

	@Exportable
	public String getLuisteren()
	{
		return getResultaatVoorSoortToets(SoortToets.Luisteren);
	}

	@Exportable
	public String getLezen()
	{
		return getResultaatVoorSoortToets(SoortToets.Lezen);
	}

	@Exportable
	public String getGesprekVoeren()
	{
		return getResultaatVoorSoortToets(SoortToets.Gesprekken);
	}

	@Exportable
	public String getSpreken()
	{
		return getResultaatVoorSoortToets(SoortToets.Spreken);
	}

	@Exportable
	public String getSchrijven()
	{
		return getResultaatVoorSoortToets(SoortToets.Schrijven);
	}

	@Exportable
	public String getGetallen()
	{
		return getResultaatVoorSoortToets(SoortToets.Getallen);
	}

	@Exportable
	public String getRuimteVorm()
	{
		return getResultaatVoorSoortToets(SoortToets.RuimteVorm);
	}

	@Exportable
	public String getGegevensverwerking()
	{
		return getResultaatVoorSoortToets(SoortToets.Gegevensverwerking);
	}

	@Exportable
	public String getVerbanden()
	{
		return getResultaatVoorSoortToets(SoortToets.Verbanden);
	}

}
