package nl.topicus.eduarte.entities.participatie;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import nl.topicus.cobra.entities.IdObject;
import nl.topicus.cobra.util.TimeUtil;
import nl.topicus.cobra.web.components.form.AutoForm;
import nl.topicus.eduarte.entities.organisatie.InstellingEntiteit;
import nl.topicus.eduarte.entities.personen.Deelnemer;
import nl.topicus.eduarte.entities.personen.Medewerker;
import nl.topicus.eduarte.participatie.web.components.choice.combobox.MaatregelComboBox;
import nl.topicus.eduarte.providers.DeelnemerProvider;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Index;

/**
 * @author vandekamp
 */
@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
public class MaatregelToekenning extends InstellingEntiteit implements DeelnemerProvider
{
	private static final long serialVersionUID = 1L;

	/**
	 * De datum waarop de maatregel nagekomen/uitgevoerd moet worden.
	 */
	@Temporal(value = TemporalType.TIMESTAMP)
	@Column(nullable = false)
	private Date maatregelDatum;

	@Column(nullable = true, length = 1024)
	private String opmerkingen;

	@Column(nullable = false)
	private boolean nagekomen;

	/**
	 * Een maatregel kan handmatig of automatisch toegekend worden. Handmatig is altijd
	 * een expliciete actie van een gebruiker. Automatisch is op basis van maatregel
	 * toekenningsregels die in het systeem ingevoerd kunnen worden.
	 */
	@Column(nullable = false)
	private boolean automatischToegekend;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "deelnemer", nullable = false)
	@Index(name = "idx_MaatregelToek_deelnemer")
	private Deelnemer deelnemer;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "maatregel", nullable = false)
	@Index(name = "idx_MaatregelToek_Maatregel")
	@AutoForm(editorClass = MaatregelComboBox.class)
	private Maatregel maatregel;

	/**
	 * De medewerker die de maatregel heeft toegekend.
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "eigenaarmedewerker", nullable = true)
	@Index(name = "idx_MaatregelToek_EignrMedwrkr")
	private Medewerker eigenaarMedewerker;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "eigenaardeelnemer", nullable = true)
	@Index(name = "idx_MaatregelToek_EignrDeelnr")
	private Deelnemer eigenaarDeelnemer;

	/**
	 * De absentiemelding die ertoe leidde dat deze maatregel werd toegekend.
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "veroorzaaktDoor", nullable = true)
	@Index(name = "idx_MaatregelToek_AbsentMeld")
	private AbsentieMelding veroorzaaktDoor;

	/**
	 * Default constructor voor Hibernate.
	 */
	public MaatregelToekenning()
	{
	}

	/**
	 * @return Ja indien nagekomen, en anders Nee.
	 */
	public String getNagekomenOmschrijving()
	{
		return isNagekomen() ? "Ja" : "Nee";
	}

	/**
	 * @return Ja indien automatischToegekend, en anders Nee.
	 */
	public String getAutomatischToegekendOmschrijving()
	{
		return isAutomatischToegekend() ? "Ja" : "Nee";
	}

	/**
	 * @return Toekomstig, Vandaag, Gisteren, Deze maand, Dit schooljaar, 2006/2007 etc.
	 */
	public String getGroepeerDatumOmschrijving()
	{
		return TimeUtil.getInstance().getDateGroup(getMaatregelDatum());
	}

	/**
	 * @return Returns the maatregelDatum.
	 */
	public Date getMaatregelDatum()
	{
		return maatregelDatum;
	}

	/**
	 * @param maatregelDatum
	 *            The maatregelDatum to set.
	 */
	public void setMaatregelDatum(Date maatregelDatum)
	{
		this.maatregelDatum = maatregelDatum;
	}

	/**
	 * @return Returns the opmerkingen.
	 */
	public String getOpmerkingen()
	{
		return opmerkingen;
	}

	/**
	 * @param opmerkingen
	 *            The opmerkingen to set.
	 */
	public void setOpmerkingen(String opmerkingen)
	{
		this.opmerkingen = opmerkingen;
	}

	/**
	 * @return Returns the nagekomen.
	 */
	public boolean isNagekomen()
	{
		return nagekomen;
	}

	/**
	 * @param nagekomen
	 *            The nagekomen to set.
	 */
	public void setNagekomen(boolean nagekomen)
	{
		this.nagekomen = nagekomen;
	}

	/**
	 * @return Returns the automatischToegekend.
	 */
	public boolean isAutomatischToegekend()
	{
		return automatischToegekend;
	}

	/**
	 * @param automatischToegekend
	 *            The automatischToegekend to set.
	 */
	public void setAutomatischToegekend(boolean automatischToegekend)
	{
		this.automatischToegekend = automatischToegekend;
	}

	/**
	 * @return Returns the deelnemer.
	 */
	public Deelnemer getDeelnemer()
	{
		return deelnemer;
	}

	/**
	 * @param deelnemer
	 *            The deelnemer to set.
	 */
	public void setDeelnemer(Deelnemer deelnemer)
	{
		this.deelnemer = deelnemer;
	}

	/**
	 * @return Returns the maatregel.
	 */
	public Maatregel getMaatregel()
	{
		return maatregel;
	}

	/**
	 * @param maatregel
	 *            The maatregel to set.
	 */
	public void setMaatregel(Maatregel maatregel)
	{
		this.maatregel = maatregel;
	}

	/**
	 * @return Returns the medewerker.
	 */
	public Medewerker getEigenaarMedewerker()
	{
		return eigenaarMedewerker;
	}

	/**
	 * @param medewerker
	 *            The medewerker to set.
	 */
	public void setEigenaarMedewerker(Medewerker medewerker)
	{
		this.eigenaarMedewerker = medewerker;
	}

	/**
	 * @return De entiteit (deelnemer, noiseGroep, persoonlijke groep) die aan deze
	 *         PersoonlijkeGroepDeelnemer is gekoppeld.
	 */
	public IdObject getEigenaar()
	{
		if (getEigenaarMedewerker() != null)
			return getEigenaarMedewerker();
		return getEigenaarDeelnemer();
	}

	/**
	 * @param entiteit
	 *            De entiteit (deelnemer, noiseGroep, persoonlijke groep) die gekoppeld
	 *            moet worden aan deze PersoonlijkeGroepDeelnemer.
	 */
	public void setEigenaar(IdObject entiteit)
	{
		if (entiteit instanceof Deelnemer)
			setEigenaarDeelnemer((Deelnemer) entiteit);
		else if (entiteit instanceof Medewerker)
			setEigenaarMedewerker((Medewerker) entiteit);
		else
			throw new IllegalArgumentException("Cannot set deelnemer of medewerker entiteit to "
				+ entiteit);
	}

	/**
	 * @return Returns the veroorzaaktDoor.
	 */
	public AbsentieMelding getVeroorzaaktDoor()
	{
		return veroorzaaktDoor;
	}

	/**
	 * @param veroorzaaktDoor
	 *            The veroorzaaktDoor to set.
	 */
	public void setVeroorzaaktDoor(AbsentieMelding veroorzaaktDoor)
	{
		this.veroorzaaktDoor = veroorzaaktDoor;
	}

	/**
	 * @param eigenaarDeelnemer
	 *            The eigenaarDeelnemer to set.
	 */
	public void setEigenaarDeelnemer(Deelnemer eigenaarDeelnemer)
	{
		this.eigenaarDeelnemer = eigenaarDeelnemer;
	}

	/**
	 * @return Returns the eigenaarDeelnemer.
	 */
	public Deelnemer getEigenaarDeelnemer()
	{
		return eigenaarDeelnemer;
	}

}
