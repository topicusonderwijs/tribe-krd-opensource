package nl.topicus.eduarte.entities.curriculum;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.UniqueConstraint;

import nl.topicus.eduarte.entities.landelijk.Cohort;
import nl.topicus.eduarte.entities.onderwijsproduct.Onderwijsproduct;
import nl.topicus.eduarte.entities.opleiding.Opleiding;
import nl.topicus.eduarte.entities.organisatie.InstellingEntiteit;
import nl.topicus.eduarte.entities.organisatie.Locatie;
import nl.topicus.eduarte.entities.organisatie.OrganisatieEenheid;
import nl.topicus.eduarte.providers.OpleidingProvider;
import nl.topicus.eduarte.providers.OrganisatieEenheidLocatieProvider;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Index;

@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
@javax.persistence.Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"opleiding",
	"cohort", "organisatieEenheid", "locatie"})})
public class Curriculum extends InstellingEntiteit implements OrganisatieEenheidLocatieProvider,
		OpleidingProvider
{
	private static final long serialVersionUID = 1L;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false, name = "opleiding")
	@Index(name = "idx_curriculum_opleiding")
	private Opleiding opleiding;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false, name = "cohort")
	@Index(name = "idx_curriculum_cohort")
	private Cohort cohort;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false, name = "organisatieEenheid")
	@Index(name = "idx_curriculum_orgenh")
	private OrganisatieEenheid organisatieEenheid;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = true, name = "locatie")
	@Index(name = "idx_curriculum_locatie")
	private Locatie locatie;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "curriculum")
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
	@OrderBy("leerjaar ASC, periode ASC")
	private List<CurriculumOnderwijsproduct> curriculumOnderwijsproducten =
		new ArrayList<CurriculumOnderwijsproduct>();

	public Curriculum()
	{
	}

	public Curriculum(Opleiding opleiding)
	{
		setOpleiding(opleiding);
	}

	public Opleiding getOpleiding()
	{
		return opleiding;
	}

	public void setOpleiding(Opleiding opleiding)
	{
		this.opleiding = opleiding;
	}

	public Cohort getCohort()
	{
		return cohort;
	}

	public void setCohort(Cohort cohort)
	{
		this.cohort = cohort;
	}

	public OrganisatieEenheid getOrganisatieEenheid()
	{
		return organisatieEenheid;
	}

	public void setOrganisatieEenheid(OrganisatieEenheid organisatieEenheid)
	{
		this.organisatieEenheid = organisatieEenheid;
	}

	public Locatie getLocatie()
	{
		return locatie;
	}

	public void setLocatie(Locatie locatie)
	{
		this.locatie = locatie;
	}

	public List<CurriculumOnderwijsproduct> getCurriculumOnderwijsproducten()
	{
		return curriculumOnderwijsproducten;
	}

	public void setCurriculumOnderwijsproducten(
			List<CurriculumOnderwijsproduct> curriculumOnderwijsproducten)
	{
		this.curriculumOnderwijsproducten = curriculumOnderwijsproducten;
	}

	public int getAantalGekoppeldeOnderwijsproducten()
	{
		return getCurriculumOnderwijsproducten().size();
	}

	@Override
	public String toString()
	{
		StringBuilder builder = new StringBuilder();
		builder.append(getOpleiding().getNaam());
		builder.append(" - Cohort ");
		builder.append(getCohort().toString());
		return builder.toString();
	}

	public BigDecimal getAfgenomenOnderwijstijdOnderwijsproduct(Onderwijsproduct onderwijsproduct)
	{
		if (onderwijsproduct != null && !getCurriculumOnderwijsproducten().isEmpty())
		{
			BigDecimal ret = new BigDecimal(0);

			for (CurriculumOnderwijsproduct curOndpr : getCurriculumOnderwijsproducten())
			{
				if (onderwijsproduct.equals(curOndpr.getOnderwijsproduct()))
				{
					if (curOndpr.getOnderwijstijd() != null)
						ret = ret.add(curOndpr.getOnderwijstijd());
				}
			}
			return ret;
		}

		return null;
	}
}
