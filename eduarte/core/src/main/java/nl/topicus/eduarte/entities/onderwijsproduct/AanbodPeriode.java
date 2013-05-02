package nl.topicus.eduarte.entities.onderwijsproduct;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import nl.topicus.cobra.web.components.form.AutoForm;
import nl.topicus.eduarte.entities.begineinddatum.BeginEinddatumInstellingEntiteit;
import nl.topicus.eduarte.entities.landelijk.Cohort;
import nl.topicus.eduarte.web.components.choice.CohortCombobox;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Index;

/**
 * Daadwerkelijke periode van een onderwijsproductaanbodperiode
 * 
 * @author vanharen
 */
@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
public class AanbodPeriode extends BeginEinddatumInstellingEntiteit
{
	private static final long serialVersionUID = 1L;

	private String naam;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cohort", nullable = true)
	@Index(name = "idx_Aanbodper_cohort")
	@AutoForm(editorClass = CohortCombobox.class, htmlClasses = "unit_120")
	private Cohort cohort;

	public AanbodPeriode()
	{

	}

	public String getNaam()
	{
		return naam;
	}

	public void setNaam(String naam)
	{
		this.naam = naam;
	}

	public Cohort getCohort()
	{
		return cohort;
	}

	public void setCohort(Cohort cohort)
	{
		this.cohort = cohort;
	}

}
