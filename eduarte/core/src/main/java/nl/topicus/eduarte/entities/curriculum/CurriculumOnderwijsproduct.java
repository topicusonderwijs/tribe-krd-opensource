package nl.topicus.eduarte.entities.curriculum;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import nl.topicus.cobra.web.components.form.AutoForm;
import nl.topicus.eduarte.entities.onderwijsproduct.Onderwijsproduct;
import nl.topicus.eduarte.entities.organisatie.InstellingEntiteit;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Index;

@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
public class CurriculumOnderwijsproduct extends InstellingEntiteit
{
	private static final long serialVersionUID = 1L;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false, name = "curriculum")
	@Index(name = "idx_CurriculumOnw_cur")
	private Curriculum curriculum;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false, name = "onderwijsproduct")
	@Index(name = "idx_CurriculumOnw_onw")
	@AutoForm(htmlClasses = "unit_max")
	private Onderwijsproduct onderwijsproduct;

	@Column(nullable = false)
	private Integer leerjaar;

	@Column(nullable = false)
	private Integer periode;

	@Column(nullable = true, scale = 10, precision = 20)
	private BigDecimal onderwijstijd;

	public CurriculumOnderwijsproduct()
	{
	}

	public CurriculumOnderwijsproduct(Curriculum curriculum)
	{
		this.curriculum = curriculum;
	}

	public Curriculum getCurriculum()
	{
		return curriculum;
	}

	public void setCurriculum(Curriculum curriculum)
	{
		this.curriculum = curriculum;
	}

	public Onderwijsproduct getOnderwijsproduct()
	{
		return onderwijsproduct;
	}

	public void setOnderwijsproduct(Onderwijsproduct onderwijsproduct)
	{
		this.onderwijsproduct = onderwijsproduct;
	}

	public Integer getLeerjaar()
	{
		return leerjaar;
	}

	public void setLeerjaar(Integer leerjaar)
	{
		this.leerjaar = leerjaar;
	}

	public Integer getPeriode()
	{
		return periode;
	}

	public void setPeriode(Integer periode)
	{
		this.periode = periode;
	}

	public BigDecimal getOnderwijstijd()
	{
		return onderwijstijd;
	}

	public void setOnderwijstijd(BigDecimal onderwijstijd)
	{
		this.onderwijstijd = onderwijstijd;
	}

	@AutoForm(label = "Totale onderwijstijd")
	public BigDecimal getTotaleOnderwijstijdOnderwijsproduct()
	{
		if (getOnderwijsproduct() != null)
			return getOnderwijsproduct().getOnderwijstijd();
		return null;
	}

	@AutoForm(label = "Belasting")
	public BigDecimal getBelastingOnderwijsproduct()
	{
		if (getOnderwijsproduct() != null)
			return getOnderwijsproduct().getBelasting();
		return null;
	}

	@AutoForm(label = "Afgenomen onderwijstijd")
	public BigDecimal getAfgenomenOnderwijstijdOnderwijsproduct()
	{
		return getCurriculum().getAfgenomenOnderwijstijdOnderwijsproduct(getOnderwijsproduct());
	}

	public boolean isVolledigAfgenomen()
	{
		if (getTotaleOnderwijstijdOnderwijsproduct() == null
			|| getAfgenomenOnderwijstijdOnderwijsproduct() == null)
			return true;

		return (getAfgenomenOnderwijstijdOnderwijsproduct().compareTo(
			getTotaleOnderwijstijdOnderwijsproduct()) == 0);
	}

	@Override
	public String toString()
	{
		StringBuilder builder = new StringBuilder();
		builder.append(getCurriculum().toString());
		builder.append(" - ");
		builder.append(getOnderwijsproduct().toString());
		return builder.toString();
	}
}