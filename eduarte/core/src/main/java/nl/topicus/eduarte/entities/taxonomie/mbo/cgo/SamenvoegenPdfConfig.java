package nl.topicus.eduarte.entities.taxonomie.mbo.cgo;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import nl.topicus.eduarte.entities.begineinddatum.BeginEinddatumInstellingEntiteit;

import org.hibernate.annotations.Table;

/**
 * @author vandenbrink
 */
@Entity
@Table(appliesTo = "SamenvoegenPdfConfig")
public class SamenvoegenPdfConfig extends BeginEinddatumInstellingEntiteit
{
	private static final long serialVersionUID = 1L;

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private GraphType graphType;

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private CategoryProperty categoryProperty;

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private CategoryAggregation categoryAggregation;

	@Temporal(TemporalType.DATE)
	@Column(nullable = true)
	private Date samenvoegenVanaf;

	@Temporal(TemporalType.DATE)
	@Column(nullable = true)
	private Date samenvoegenTot;

	public SamenvoegenPdfConfig()
	{
	}

	public Date getSamenvoegenVanaf()
	{
		return samenvoegenVanaf;
	}

	public void setSamenvoegenVanaf(Date samenvoegenVanaf)
	{
		this.samenvoegenVanaf = samenvoegenVanaf;
	}

	public Date getSamenvoegenTot()
	{
		return samenvoegenTot;
	}

	public void setSamenvoegenTot(Date samenvoegenTot)
	{
		this.samenvoegenTot = samenvoegenTot;
	}

	public GraphType getGraphType()
	{
		return graphType;
	}

	public void setGraphType(GraphType graphType)
	{
		this.graphType = graphType;
	}

	public CategoryProperty getCategoryProperty()
	{
		return categoryProperty;
	}

	public void setCategoryProperty(CategoryProperty categoryProperty)
	{
		this.categoryProperty = categoryProperty;
	}

	public CategoryAggregation getCategoryAggregation()
	{
		return categoryAggregation;
	}

	public void setCategoryAggregation(CategoryAggregation categoryAggregation)
	{
		this.categoryAggregation = categoryAggregation;
	}

}
