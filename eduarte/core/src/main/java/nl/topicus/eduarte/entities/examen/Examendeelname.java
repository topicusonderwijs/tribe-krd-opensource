package nl.topicus.eduarte.entities.examen;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.persistence.*;

import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.templates.annotations.Exportable;
import nl.topicus.cobra.util.StringUtil;
import nl.topicus.cobra.util.TimeUtil;
import nl.topicus.eduarte.dao.helpers.OnderwijsproductAfnameContextDataAccessHelper;
import nl.topicus.eduarte.entities.BronEntiteitStatus;
import nl.topicus.eduarte.entities.IBronStatusEntiteit;
import nl.topicus.eduarte.entities.IsViewWhenOnNoise;
import nl.topicus.eduarte.entities.bijlage.Bijlage;
import nl.topicus.eduarte.entities.bijlage.IBijlageKoppelEntiteit;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.entities.landelijk.Schooljaar;
import nl.topicus.eduarte.entities.onderwijsproduct.OnderwijsproductAfnameContext;
import nl.topicus.eduarte.entities.organisatie.InstellingEntiteit;
import nl.topicus.onderwijs.duo.bron.Bron;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Index;

/**
 * Deelname aan het examenproces voor een deelnemer. Een Examendeelname is gekoppeld aan
 * een verbintenis en heeft als uiteindelijke doel het uitdelen van een diploma aan de
 * deelnemer voor de opleiding waarop hij/zij is ingeschreven volgens de verbintenis
 * waaraan de examendeelname is gekoppeld.
 * 
 * @author loite
 */
@Entity()
@Exportable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
@IsViewWhenOnNoise
public class Examendeelname extends InstellingEntiteit implements Comparable<Examendeelname>,
		IBijlageKoppelEntiteit<ExamendeelnameBijlage>, IBronStatusEntiteit
{
	private static final long serialVersionUID = 1L;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "verbintenis", nullable = false)
	@Index(name = "idx_exdeelname_verbintenis")
	private Verbintenis verbintenis;

	@Bron
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "examenstatus", nullable = false)
	@Index(name = "idx_exdeelname_status")
	private Examenstatus examenstatus;

	@Column(nullable = true)
	@Index(name = "idx_exdeelname_nummer")
	private Integer examennummer;

	@Bron
	@Column(nullable = true)
	private Integer examenjaar;

	@Column(nullable = true)
	@Index(name = "idx_exdeelname_tijdvak")
	private Integer tijdvak;

	@Column(nullable = true, length = 20)
	@Index(name = "idx_exdeelname_prefix")
	private String examennummerPrefix;

	@Bron
	@Temporal(value = TemporalType.DATE)
	@Column(nullable = true)
	private Date datumUitslag;

	@Temporal(value = TemporalType.TIMESTAMP)
	@Column(nullable = false)
	private Date datumLaatsteStatusovergang;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "examendeelname")
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
	@OrderBy(value = "datumTijd")
	private List<ExamenstatusOvergang> statusovergangen;

	/**
	 * De bijlages van deze examendeelname
	 */
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "examendeelname")
	private List<ExamendeelnameBijlage> bijlagen;

	@Column(nullable = false)
	private boolean meenemenInVolgendeBronBatch;

	/**
	 * Dit is een property die automatisch gezet wordt door de broncontroller als een
	 * wijzing van de resultaten of iets dergelijks resulteert in het (opnieuw)verzenden
	 * van deze examendeelname naar BRON
	 */
	@Column(nullable = false)
	private boolean gewijzigd;

	@Bron
	private boolean bekostigd;

	@Column(nullable = true)
	@Enumerated(EnumType.STRING)
	private BronEntiteitStatus bronStatus = BronEntiteitStatus.Geen;

	public BronEntiteitStatus getBronStatus()
	{
		return bronStatus;
	}

	public void setBronStatus(BronEntiteitStatus bronStatus)
	{
		this.bronStatus = bronStatus;
	}

	public Date getBronDatum()
	{
		return bronDatum;
	}

	public void setBronDatum(Date bronDatum)
	{
		this.bronDatum = bronDatum;
	}

	@Temporal(value = TemporalType.TIMESTAMP)
	@Column(nullable = true)
	private Date bronDatum;

	/**
	 * Default constructor voor Hibernate. NIET GEBRUIKEN! Gebruik
	 * Examendeelname(Verbintenis)
	 */
	public Examendeelname()
	{
	}

	public Examendeelname(Verbintenis verbintenis)
	{
		setVerbintenis(verbintenis);
		examenjaar =
			verbintenis.getGeplandeEinddatum() == null ? null : TimeUtil.getInstance().getYear(
				verbintenis.getGeplandeEinddatum());
		if (examenjaar != null && verbintenis.getGeplandeEinddatum() != null)
		{
			bekostigd =
				!verbintenis.heeftBekostigdeExamendeelnameInKalenderjaar(examenjaar.intValue());
		}
		setGewijzigd(false);
		setMeenemenInVolgendeBronBatch(true);
	}

	/**
	 * @return Returns the verbintenis.
	 */
	@Exportable
	public Verbintenis getVerbintenis()
	{
		return verbintenis;
	}

	/**
	 * @param verbintenis
	 *            The verbintenis to set.
	 */
	public void setVerbintenis(Verbintenis verbintenis)
	{
		this.verbintenis = verbintenis;
	}

	/**
	 * @return Returns the examenstatus.
	 */
	@Exportable
	public Examenstatus getExamenstatus()
	{
		return examenstatus;
	}

	/**
	 * @param examenstatus
	 *            The examenstatus to set.
	 */
	public void setExamenstatus(Examenstatus examenstatus)
	{
		this.examenstatus = examenstatus;
		if (examenstatus != null && getVerbintenis() != null
			&& getVerbintenis().getDeelnemer() != null && examenstatus.isGeslaagd())
		{
			getVerbintenis().getDeelnemer().berekenEnSetStartkwalificatieplichtigTot(null);
		}
	}

	/**
	 * @return Returns the examennummer.
	 */
	@Exportable
	public Integer getExamennummer()
	{
		return examennummer;
	}

	/**
	 * @param examennummer
	 *            The examennummer to set.
	 */
	public void setExamennummer(Integer examennummer)
	{
		this.examennummer = examennummer;
	}

	public Integer getExamenjaar()
	{
		return examenjaar;
	}

	public void setExamenjaar(Integer examenjaar)
	{
		this.examenjaar = examenjaar;
	}

	/**
	 * @return Returns the examennummerPrefix.
	 */
	public String getExamennummerPrefix()
	{
		return examennummerPrefix;
	}

	/**
	 * @param examennummerPrefix
	 *            The examennummerPrefix to set.
	 */
	public void setExamennummerPrefix(String examennummerPrefix)
	{
		this.examennummerPrefix = examennummerPrefix;
	}

	/**
	 * @return Returns the statusovergangen.
	 */
	public List<ExamenstatusOvergang> getStatusovergangen()
	{
		if (statusovergangen == null)
			statusovergangen = new ArrayList<ExamenstatusOvergang>();
		return statusovergangen;
	}

	/**
	 * @param statusovergangen
	 *            The statusovergangen to set.
	 */
	public void setStatusovergangen(List<ExamenstatusOvergang> statusovergangen)
	{
		this.statusovergangen = statusovergangen;
	}

	/**
	 * @return Returns the tijdvak.
	 */
	public Integer getTijdvak()
	{
		return tijdvak;
	}

	/**
	 * @param tijdvak
	 *            The tijdvak to set.
	 */
	public void setTijdvak(Integer tijdvak)
	{
		this.tijdvak = tijdvak;
	}

	public String getExamennummerMetPrefix()
	{
		if (StringUtil.isNotEmpty(getExamennummerPrefix()))
		{
			return getExamennummerPrefix() + StringUtil.valueOrEmptyIfNull(getExamennummer());
		}
		return StringUtil.valueOrEmptyIfNull(getExamennummer());
	}

	@Override
	public int compareTo(Examendeelname o)
	{
		return getDatumLaatsteStatusovergang().compareTo(o.getDatumLaatsteStatusovergang());
	}

	public boolean isBekostigd()
	{
		return bekostigd;
	}

	public void setBekostigd(boolean bekostigd)
	{
		this.bekostigd = bekostigd;
	}

	public String getBekostigdOmschrijving()
	{
		if (isVO())
			return "N.v.t.";
		return isBekostigd() ? "Ja" : "Nee";
	}

	@Exportable
	public Date getDatumUitslag()
	{
		return datumUitslag;
	}

	@Exportable
	public String getDatumUitslagOfficieel()
	{
		if (getDatumUitslag() != null)
		{
			return new SimpleDateFormat("d MMMMM yyyy", new Locale("nl", "NL"))
				.format(getDatumUitslag());
		}
		return "";
	}

	public void setDatumUitslag(Date datumUitslag)
	{
		this.datumUitslag = datumUitslag;
		if (datumUitslag != null && getExamenstatus() != null && getVerbintenis() != null
			&& getVerbintenis().getDeelnemer() != null && getExamenstatus().isGeslaagd())
		{
			getVerbintenis().getDeelnemer().berekenEnSetStartkwalificatieplichtigTot(null);
		}
	}

	public Date getDatumLaatsteStatusovergang()
	{
		return datumLaatsteStatusovergang;
	}

	public void setDatumLaatsteStatusovergang(Date datumLaatsteStatusovergang)
	{
		this.datumLaatsteStatusovergang = datumLaatsteStatusovergang;
	}

	@Override
	public ExamendeelnameBijlage addBijlage(Bijlage bijlage)
	{
		ExamendeelnameBijlage newBijlage = new ExamendeelnameBijlage();
		newBijlage.setBijlage(bijlage);
		newBijlage.setExamendeelname(this);
		newBijlage.setDeelnemer(getVerbintenis().getDeelnemer());

		getBijlagen().add(newBijlage);

		return newBijlage;
	}

	@Override
	public boolean bestaatBijlage(Bijlage bijlage)
	{
		for (ExamendeelnameBijlage deelbijlage : getBijlagen())
		{
			if (deelbijlage.getBijlage().equals(bijlage))
				return true;
		}
		return false;
	}

	@Override
	public List<ExamendeelnameBijlage> getBijlagen()
	{
		if (bijlagen == null)
			bijlagen = new ArrayList<ExamendeelnameBijlage>();
		return bijlagen;
	}

	@Override
	public void setBijlagen(List<ExamendeelnameBijlage> bijlagen)
	{
		this.bijlagen = bijlagen;
	}

	public boolean isUitslagBekend()
	{
		Examenstatus uitslag = getExamenstatus();

		if (uitslag == null || !uitslag.isEindstatus())
		{
			return false;
		}
		return true;
	}

	public boolean isVAVO()
	{
		return getVerbintenis().isVAVOVerbintenis();
	}

	public boolean isEducatie()
	{
		return getVerbintenis().isEducatieVerbintenis();
	}

	public boolean isBO()
	{
		return getVerbintenis().isBOVerbintenis();
	}

	public boolean isVO()
	{
		return getVerbintenis().isVOVerbintenis();
	}

	public void setMeenemenInVolgendeBronBatch(boolean meenemenInVolgendeBronBatch)
	{
		this.meenemenInVolgendeBronBatch = meenemenInVolgendeBronBatch;
	}

	public boolean isMeenemenInVolgendeBronBatch()
	{
		return meenemenInVolgendeBronBatch;
	}

	public String getMeenemenInVolgendeBronBatchOmsch()
	{
		return isMeenemenInVolgendeBronBatch() ? "Ja" : "Nee";
	}

	/**
	 * @return Het schooljaar van de datum uitslag.
	 */
	@Exportable()
	public Schooljaar getSchooljaar()
	{
		if (getDatumUitslag() == null)
			return null;
		return Schooljaar.valueOf(getDatumUitslag());
	}

	public void setGewijzigd(boolean gewijzigd)
	{
		this.gewijzigd = gewijzigd;
	}

	public boolean isGewijzigd()
	{
		return gewijzigd;
	}

	/**
	 * Aanpassing nav 0062320
	 */
	public void pasAfnameContextenAan(Examenstatus naarStatus)
	{
		if (naarStatus.isEindstatus())
		{
			OnderwijsproductAfnameContextDataAccessHelper helper =
				DataAccessRegistry.getHelper(OnderwijsproductAfnameContextDataAccessHelper.class);
			for (OnderwijsproductAfnameContext context : helper.listContexten(getVerbintenis()))
			{
				if (context.isVerwezenNaarVolgendTijdvak())
				{
					context.setVerwezenNaarVolgendTijdvak(false);
					context.save();
				}
			}
		}
	}
}
