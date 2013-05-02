package nl.topicus.eduarte.zoekfilters;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.entities.landelijk.Cohort;
import nl.topicus.eduarte.entities.personen.Deelnemer;
import nl.topicus.eduarte.entities.taxonomie.mbo.cgo.CompetentieMatrix;
import nl.topicus.eduarte.entities.taxonomie.mbo.cgo.CompetentieNiveauVerzameling;

import org.apache.wicket.model.IModel;

/**
 * @author vanharen
 * @param <T>
 */
public abstract class NiveauVerzamelingZoekFilter<T extends CompetentieNiveauVerzameling> extends
		AbstractZoekFilter<T>

{
	private static final long serialVersionUID = 1L;

	private IModel<Deelnemer> deelnemer;

	private IModel<CompetentieMatrix> matrix;

	private IModel<Verbintenis> verbintenis;

	private String naam;

	private Date begindatum;

	private Date einddatum;

	private boolean matrixBewustNull = false;

	private boolean mustIncludeIds = false;

	private boolean mustExcludeIds = false;

	private List<Serializable> ids;

	private IModel<Cohort> cohort;

	public NiveauVerzamelingZoekFilter()
	{
	}

	public Cohort getCohort()
	{
		return getModelObject(cohort);
	}

	public void setCohort(Cohort cohort)
	{
		this.cohort = makeModelFor(cohort);
	}

	public Deelnemer getDeelnemer()
	{
		return getModelObject(deelnemer);
	}

	public void setDeelnemer(Deelnemer deelnemer)
	{
		this.deelnemer = makeModelFor(deelnemer);
	}

	public String getNaam()
	{
		return naam;
	}

	public void setNaam(String naam)
	{
		this.naam = naam;
	}

	public CompetentieMatrix getMatrix()
	{
		return getModelObject(matrix);
	}

	public void setMatrix(CompetentieMatrix matrix)
	{
		this.matrix = makeModelFor(matrix);
	}

	public void clearIds()
	{
		mustExcludeIds = false;
		mustIncludeIds = false;
		ids = null;
	}

	/**
	 * Lijst met id's waarop gefiltered moet worden.
	 * 
	 * @return Returns the ids.
	 * @see #isMustExcludeIds()
	 * @see #isMustIncludeIds()
	 */
	public final List<Serializable> getIds()
	{
		return ids;
	}

	/**
	 * @return Returns the mustIncludeIds.
	 */
	public final boolean isMustIncludeIds()
	{
		return mustIncludeIds;
	}

	/**
	 * @return Returns the mustExcludeIds.
	 */
	public final boolean isMustExcludeIds()
	{
		return mustExcludeIds;
	}

	public void resultsMustExclude(Collection< ? extends Serializable> myIds)
	{
		this.ids = new ArrayList<Serializable>(myIds);
		mustExcludeIds = true;
		mustIncludeIds = false;
	}

	public void resultsMustMatch(Collection< ? extends Serializable> myIds)
	{
		this.ids = new ArrayList<Serializable>(myIds);
		mustIncludeIds = true;
		mustExcludeIds = false;
	}

	public Verbintenis getVerbintenis()
	{
		return getModelObject(verbintenis);
	}

	public void setVerbintenis(Verbintenis verbintenis)
	{
		this.verbintenis = makeModelFor(verbintenis);
	}

	public boolean isMatrixBewustNull()
	{
		return matrixBewustNull;
	}

	public void setMatrixBewustNull(boolean matrixBewustNull)
	{
		this.matrixBewustNull = matrixBewustNull;
	}

	public Date getBeginDatum()
	{
		return begindatum;
	}

	public void setBeginDatum(Date begindatum)
	{
		this.begindatum = begindatum;
	}

	public Date getEindDatum()
	{
		return einddatum;
	}

	public void setEindDatum(Date einddatum)
	{
		this.einddatum = einddatum;
	}

}
