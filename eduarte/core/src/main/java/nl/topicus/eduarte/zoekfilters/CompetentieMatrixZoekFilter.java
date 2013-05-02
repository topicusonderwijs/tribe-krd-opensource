package nl.topicus.eduarte.zoekfilters;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import nl.topicus.cobra.web.components.form.AutoForm;
import nl.topicus.eduarte.entities.landelijk.Cohort;
import nl.topicus.eduarte.entities.personen.Deelnemer;
import nl.topicus.eduarte.entities.taxonomie.mbo.cgo.CompetentieMatrix;

import org.apache.wicket.model.IModel;

/**
 * @author vandenbrink
 */
public class CompetentieMatrixZoekFilter extends AbstractZoekFilter<CompetentieMatrix>
{
	private static final long serialVersionUID = 1L;

	private Integer hoofdstuk;

	private Integer niveau;

	private String titel;

	@AutoForm(htmlClasses = "unit_max")
	private String naam;

	private Cohort cohort;

	private String titelDossier;

	private Class< ? extends CompetentieMatrix> matrixType;

	private IModel<Deelnemer> exclusiefDeelnemer;

	private IModel<Deelnemer> koppelDeelnemer;

	private boolean excludeKoppelDeelnemer;

	private boolean mustIncludeIds = false;

	private boolean mustExcludeIds = false;

	private List<Serializable> ids;

	public Class< ? extends CompetentieMatrix> getMatrixType()
	{
		return matrixType;
	}

	public void setMatrixType(Class< ? extends CompetentieMatrix> matrixType)
	{
		this.matrixType = matrixType;
	}

	public Integer getHoofdstuk()
	{
		return hoofdstuk;
	}

	public void setHoofdstuk(Integer hoofdstuk)
	{
		this.hoofdstuk = hoofdstuk;
	}

	public Integer getNiveau()
	{
		return niveau;
	}

	public void setNiveau(Integer niveau)
	{
		this.niveau = niveau;
	}

	public String getTitel()
	{
		return titel;
	}

	public void setTitel(String titel)
	{
		this.titel = titel;
	}

	public String getTitelDossier()
	{
		return titelDossier;
	}

	public void setTitelDossier(String titelDossier)
	{
		this.titelDossier = titelDossier;
	}

	public Deelnemer getExclusiefDeelnemer()
	{
		return getModelObject(exclusiefDeelnemer);
	}

	public void setExclusiefDeelnemer(Deelnemer exclusiefDeelnemer)
	{
		this.exclusiefDeelnemer = makeModelFor(exclusiefDeelnemer);
	}

	public Deelnemer getKoppelDeelnemer()
	{
		return getModelObject(koppelDeelnemer);
	}

	public void setKoppelDeelnemer(Deelnemer koppelDeelnemer)
	{
		this.koppelDeelnemer = makeModelFor(koppelDeelnemer);
	}

	public boolean isExcludeKoppelDeelnemer()
	{
		return excludeKoppelDeelnemer;
	}

	public void setExcludeKoppelDeelnemer(boolean excludeKoppelDeelnemer)
	{
		this.excludeKoppelDeelnemer = excludeKoppelDeelnemer;
	}

	public void clearIds()
	{
		mustExcludeIds = false;
		mustIncludeIds = false;
		ids = null;
	}

	public List<Serializable> getIds()
	{
		return ids;
	}

	public boolean isMustExcludeIds()
	{
		return mustExcludeIds;
	}

	public boolean isMustIncludeIds()
	{
		return mustIncludeIds;
	}

	@SuppressWarnings("hiding")
	public void resultsMustExclude(Collection< ? extends Serializable> ids)
	{
		this.ids = new ArrayList<Serializable>(ids);
		mustExcludeIds = true;
		mustIncludeIds = false;
	}

	@SuppressWarnings("hiding")
	public void resultsMustMatch(Collection< ? extends Serializable> ids)
	{
		this.ids = new ArrayList<Serializable>(ids);
		mustIncludeIds = true;
		mustExcludeIds = false;
	}

	public Cohort getCohort()
	{
		return cohort;
	}

	public void setCohort(Cohort cohort)
	{
		this.cohort = cohort;
	}

	public String getNaam()
	{
		return naam;
	}

	public void setNaam(String naam)
	{
		this.naam = naam;
	}

}
