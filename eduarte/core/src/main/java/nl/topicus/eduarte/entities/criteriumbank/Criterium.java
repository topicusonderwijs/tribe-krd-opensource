package nl.topicus.eduarte.entities.criteriumbank;

import javax.persistence.*;

import nl.topicus.cobra.web.components.form.AutoForm;
import nl.topicus.eduarte.entities.landelijk.Cohort;
import nl.topicus.eduarte.entities.opleiding.Opleiding;
import nl.topicus.eduarte.entities.organisatie.EntiteitContext;
import nl.topicus.eduarte.entities.organisatie.LandelijkOfInstellingEntiteit;
import nl.topicus.eduarte.entities.taxonomie.Verbintenisgebied;

import org.apache.wicket.markup.html.form.TextField;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Index;

/**
 * Een criterium is een regel uit de criteriumbank die bepaalt of een deelnemer voldoet
 * aan de eisen van zijn/haar opleiding. Een criterium kan zowel landelijk als lokaal
 * gedefinieerd worden. Een criterium bestaat uit een formule die een integer moet
 * opleveren. 0 betekent dat de regel niet met succes is voltooid, en alle andere waarden
 * geven aan dat de deelnemer wel voldoet aan de regel.
 * 
 * @author loite
 */
@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
@javax.persistence.Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"opleiding",
	"verbintenisgebied", "cohort", "volgnummer"})})
public class Criterium extends LandelijkOfInstellingEntiteit
{
	private static final long serialVersionUID = 1L;

	/**
	 * Een criterium is gekoppeld aan of een opleiding (lokaal) of een verbintenisgebied
	 * (landelijk)
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = true, name = "opleiding")
	@Index(name = "idx_criterium_opleiding")
	private Opleiding opleiding;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = true, name = "verbintenisgebied")
	@Index(name = "idx_criterium_verbgeb")
	private Verbintenisgebied verbintenisgebied;

	/**
	 * Een criterium is altijd cohortspecifiek
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false, name = "cohort")
	@Index(name = "idx_criterium_cohort")
	@AutoForm(editorClass = TextField.class)
	private Cohort cohort;

	@Column(nullable = false)
	private int volgnummer;

	/**
	 * De naam van deze regel, zoals 'Alle cijfers moeten 4 of hoger zijn'
	 */
	@Column(nullable = false, length = 500)
	private String naam;

	/**
	 * De melding die gegeven moet worden als een deelnemer niet voldoet aan de regel,
	 * zoals 'De deelnemer heeft een cijfer die lager is dan 4'.
	 */
	@Column(nullable = false, length = 500)
	private String melding;

	/**
	 * De formule die geevalueerd moet worden.
	 */
	@Lob()
	@Basic(optional = false)
	private String formule;

	/**
	 * Default constructor voor Hibernate.
	 */
	public Criterium()
	{
	}

	public Criterium(EntiteitContext context)
	{
		super(context);
	}

	public Criterium(Opleiding opleiding)
	{
		super(opleiding.isLandelijk());
		setOpleiding(opleiding);
	}

	public Criterium(Verbintenisgebied verbintenisgebied)
	{
		super(verbintenisgebied.isLandelijk());
		setVerbintenisgebied(verbintenisgebied);
	}

	public Opleiding getOpleiding()
	{
		return opleiding;
	}

	public void setOpleiding(Opleiding opleiding)
	{
		this.opleiding = opleiding;
	}

	public Verbintenisgebied getVerbintenisgebied()
	{
		return verbintenisgebied;
	}

	public void setVerbintenisgebied(Verbintenisgebied verbintenisgebied)
	{
		this.verbintenisgebied = verbintenisgebied;
	}

	public String getNaam()
	{
		return naam;
	}

	public void setNaam(String naam)
	{
		this.naam = naam;
	}

	public String getMelding()
	{
		return melding;
	}

	public void setMelding(String melding)
	{
		this.melding = melding;
	}

	public String getFormule()
	{
		return formule;
	}

	public void setFormule(String formule)
	{
		this.formule = formule;
	}

	public Cohort getCohort()
	{
		return cohort;
	}

	public void setCohort(Cohort cohort)
	{
		this.cohort = cohort;
	}

	public int getVolgnummer()
	{
		return volgnummer;
	}

	public void setVolgnummer(int volgnummer)
	{
		this.volgnummer = volgnummer;
	}

	@Override
	public String toString()
	{
		StringBuilder ret = new StringBuilder();
		if (getVerbintenisgebied() != null)
			ret.append(getVerbintenisgebied().getTaxonomiecode()).append(" - ");
		if (getOpleiding() != null)
			ret.append(getOpleiding().getCode()).append(" - ");
		ret.append('(').append(getVolgnummer()).append(')').append(getNaam());
		return ret.toString();
	}
}
