package nl.topicus.eduarte.entities.resultaatstructuur;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import nl.topicus.eduarte.entities.groep.Groep;
import nl.topicus.eduarte.entities.landelijk.Cohort;
import nl.topicus.eduarte.entities.onderwijsproduct.Onderwijsproduct;
import nl.topicus.eduarte.entities.organisatie.InstellingEntiteit;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Index;

@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
@Table(name = "GroepResultaatFilterInst")
public class GroepResultaatZoekFilterInstelling extends InstellingEntiteit
{
	private static final long serialVersionUID = 1L;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false, name = "filterInstelling")
	@Index(name = "idx_GRZFI_instelling")
	private ResultaatZoekFilterInstelling filterInstelling;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false, name = "groep")
	@Index(name = "idx_GRZFI_groep")
	private Groep groep;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = true, name = "onderwijsproduct")
	@Index(name = "idx_GRZFI_onderwijsproduct")
	private Onderwijsproduct onderwijsproduct;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false, name = "cohort")
	@Index(name = "idx_GRZFI_cohort")
	private Cohort cohort;

	public GroepResultaatZoekFilterInstelling()
	{
	}

	public ResultaatZoekFilterInstelling getFilterInstelling()
	{
		return filterInstelling;
	}

	public void setFilterInstelling(ResultaatZoekFilterInstelling filterInstelling)
	{
		this.filterInstelling = filterInstelling;
	}

	public Groep getGroep()
	{
		return groep;
	}

	public void setGroep(Groep groep)
	{
		this.groep = groep;
	}

	public Onderwijsproduct getOnderwijsproduct()
	{
		return onderwijsproduct;
	}

	public void setOnderwijsproduct(Onderwijsproduct onderwijsproduct)
	{
		this.onderwijsproduct = onderwijsproduct;
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
