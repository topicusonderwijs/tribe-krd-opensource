package nl.topicus.eduarte.entities.taxonomie.mbo.cgo;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.OneToMany;

import nl.topicus.eduarte.entities.begineinddatum.BeginEinddatumInstellingEntiteit;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.Table;

@Entity
@Table(appliesTo = "VoortgangHtmlConfig")
public class VoortgangHtmlConfig extends BeginEinddatumInstellingEntiteit implements
		IRapportageTemplateIJkpuntenProvider
{
	private static final long serialVersionUID = 1L;

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private GraphType graphType;

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private CategoryProperty categoryProperty;

	@Column(nullable = false)
	private boolean includeEvcEvk;

	@Column(nullable = false)
	private boolean includeInvidueleIJkpunten;

	@Column(nullable = false)
	private boolean includeLokaalMaximum;

	@Column(nullable = false)
	private int aantalBeoordelingen;

	@OneToMany(mappedBy = "config", cascade = javax.persistence.CascadeType.REMOVE)
	@Cascade( {CascadeType.REMOVE})
	private List<RapportageTemplateIJkpunt> ijkpunten = new ArrayList<RapportageTemplateIJkpunt>();

	public VoortgangHtmlConfig()
	{
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

	public boolean isIncludeEvcEvk()
	{
		return includeEvcEvk;
	}

	public void setIncludeEvcEvk(boolean includeEvcEvk)
	{
		this.includeEvcEvk = includeEvcEvk;
	}

	public boolean isIncludeInvidueleIJkpunten()
	{
		return includeInvidueleIJkpunten;
	}

	public void setIncludeInvidueleIJkpunten(boolean includeInvidueleIJkpunten)
	{
		this.includeInvidueleIJkpunten = includeInvidueleIJkpunten;
	}

	public int getAantalBeoordelingen()
	{
		return aantalBeoordelingen;
	}

	public void setAantalBeoordelingen(int aantalBeoordelingen)
	{
		this.aantalBeoordelingen = aantalBeoordelingen;
	}

	public List<RapportageTemplateIJkpunt> getIjkpunten()
	{
		return ijkpunten;
	}

	public void setIjkpunten(List<RapportageTemplateIJkpunt> ijkpunten)
	{
		this.ijkpunten = ijkpunten;
	}

	public boolean isIncludeLokaalMaximum()
	{
		return includeLokaalMaximum;
	}

	public void setIncludeLokaalMaximum(boolean includeLokaalMaximum)
	{
		this.includeLokaalMaximum = includeLokaalMaximum;
	}
}
