package nl.topicus.eduarte.entities.examen;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import nl.topicus.eduarte.entities.organisatie.EntiteitContext;
import nl.topicus.eduarte.entities.organisatie.LandelijkOfInstellingEntiteit;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Index;

/**
 * Een toegestane examenstatusovergang kan meerdere geldige beginstatussen. Zo kan de
 * statusovergang 'Uitslag bepalen' opgestart worden vanuit Aangemeld en Gespreid examen.
 * 
 * @author loite
 */
@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Landelijk")
public class ToegestaneBeginstatus extends LandelijkOfInstellingEntiteit
{
	private static final long serialVersionUID = 1L;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false, name = "examenstatus")
	@Index(name = "idx_toegBegin_exStatus")
	private Examenstatus examenstatus;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false, name = "toegestaneExamenstatusOvergang")
	@Index(name = "idx_toegBegin_toegOvergang")
	private ToegestaneExamenstatusOvergang toegestaneExamenstatusOvergang;

	/**
	 * Default constructor voor Hibernate.
	 */
	public ToegestaneBeginstatus()
	{
	}

	public ToegestaneBeginstatus(EntiteitContext context)
	{
		super(context);
	}

	/**
	 * Constructor
	 * 
	 * @param examenstatus
	 * @param toegestaneExamenstatusOvergang
	 */
	public ToegestaneBeginstatus(Examenstatus examenstatus,
			ToegestaneExamenstatusOvergang toegestaneExamenstatusOvergang)
	{
		super(examenstatus.isLandelijk());
		setExamenstatus(examenstatus);
		setToegestaneExamenstatusOvergang(toegestaneExamenstatusOvergang);
	}

	/**
	 * @return Returns the examenstatus.
	 */
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
	}

	/**
	 * @return Returns the toegestaneExamenstatusOvergang.
	 */
	public ToegestaneExamenstatusOvergang getToegestaneExamenstatusOvergang()
	{
		return toegestaneExamenstatusOvergang;
	}

	/**
	 * @param toegestaneExamenstatusOvergang
	 *            The toegestaneExamenstatusOvergang to set.
	 */
	public void setToegestaneExamenstatusOvergang(
			ToegestaneExamenstatusOvergang toegestaneExamenstatusOvergang)
	{
		this.toegestaneExamenstatusOvergang = toegestaneExamenstatusOvergang;
	}

}
