package nl.topicus.eduarte.zoekfilters;

import java.util.Date;

import nl.topicus.cobra.web.components.choice.JaNeeCombobox;
import nl.topicus.cobra.web.components.form.AutoForm;
import nl.topicus.eduarte.entities.inschrijving.OnderwijsproductAfname;
import nl.topicus.eduarte.entities.inschrijving.VrijstellingType;
import nl.topicus.eduarte.entities.landelijk.Cohort;
import nl.topicus.eduarte.entities.onderwijsproduct.Onderwijsproduct;
import nl.topicus.eduarte.entities.onderwijsproduct.SoortOnderwijsproduct;
import nl.topicus.eduarte.entities.personen.Deelnemer;
import nl.topicus.eduarte.web.components.choice.SoortOnderwijsproductCombobox;

import org.apache.wicket.model.IModel;

/**
 * Zoekfilter voor OnderwijsproductAfname
 * 
 * @author vandekamp
 */
public class OnderwijsproductAfnameZoekFilter extends AbstractZoekFilter<OnderwijsproductAfname>
{
	private static final long serialVersionUID = 1L;

	private String code;

	private String titel;

	@AutoForm(editorClass = JaNeeCombobox.class)
	private Boolean summatief;

	private VrijstellingType vrijstellingType;

	@AutoForm(editorClass = SoortOnderwijsproductCombobox.class)
	private IModel<SoortOnderwijsproduct> soortOnderwijsproduct;

	private IModel<Cohort> cohort;

	private IModel<Deelnemer> deelnemer;

	private IModel<Onderwijsproduct> onderwijsproduct;

	private DeelnemerZoekFilter deelnemerFilter = new DeelnemerZoekFilter();

	private Boolean beeindigdeProductafnames;

	private Date peilEindDatum;

	private Boolean actief;

	public OnderwijsproductAfnameZoekFilter()
	{
	}

	public OnderwijsproductAfnameZoekFilter(Deelnemer deelnemer)
	{
		setDeelnemer(deelnemer);
	}

	public String getCode()
	{
		return code;
	}

	public void setCode(String code)
	{
		this.code = code;
	}

	public String getTitel()
	{
		return titel;
	}

	public void setTitel(String titel)
	{
		this.titel = titel;
	}

	/**
	 * @return Returns the soortOnderwijsproduct.
	 */
	public SoortOnderwijsproduct getSoortOnderwijsproduct()
	{
		return getModelObject(soortOnderwijsproduct);
	}

	/**
	 * @param soortOnderwijsproduct
	 *            The soortOnderwijsproduct to set.
	 */
	public void setSoortOnderwijsproduct(SoortOnderwijsproduct soortOnderwijsproduct)
	{
		this.soortOnderwijsproduct = makeModelFor(soortOnderwijsproduct);
	}

	/**
	 * @return Returns the cohort.
	 */
	public Cohort getCohort()
	{
		return getModelObject(cohort);
	}

	/**
	 * @param cohort
	 *            The cohort to set.
	 */
	public void setCohort(Cohort cohort)
	{
		this.cohort = makeModelFor(cohort);
	}

	/**
	 * @return Returns the deelnemer.
	 */
	public Deelnemer getDeelnemer()
	{
		return getModelObject(deelnemer);
	}

	/**
	 * @param deelnemer
	 *            The deelnemer to set.
	 */
	public void setDeelnemer(Deelnemer deelnemer)
	{
		this.deelnemer = makeModelFor(deelnemer);
	}

	/**
	 * @return Returns the summatief.
	 */
	public Boolean getSummatief()
	{
		return summatief;
	}

	/**
	 * @param summatief
	 *            The summatief to set.
	 */
	public void setSummatief(Boolean summatief)
	{
		this.summatief = summatief;
	}

	public void setOnderwijsproduct(Onderwijsproduct onderwijsproduct)
	{
		this.onderwijsproduct = makeModelFor(onderwijsproduct);
	}

	public Onderwijsproduct getOnderwijsproduct()
	{
		return getModelObject(onderwijsproduct);
	}

	public DeelnemerZoekFilter getDeelnemerFilter()
	{
		return deelnemerFilter;
	}

	public void setDeelnemerFilter(DeelnemerZoekFilter deelnemerFilter)
	{
		this.deelnemerFilter = deelnemerFilter;
	}

	public void setBeeindigdeProductafnames(Boolean beeindigdeProductafnames)
	{
		this.beeindigdeProductafnames = beeindigdeProductafnames;
	}

	public Boolean getBeeindigdeProductafnames()
	{
		return beeindigdeProductafnames;
	}

	public Date getPeilEindDatum()
	{
		if (peilEindDatum == null)
			return getPeildatum();
		return peilEindDatum;
	}

	public void setPeilEindDatum(Date peilEindDatum)
	{
		this.peilEindDatum = peilEindDatum;
		if (deelnemerFilter != null)
			deelnemerFilter.setPeilEindDatum(peilEindDatum);
	}

	public void setActief(Boolean actief)
	{
		this.actief = actief;
	}

	public Boolean getActief()
	{
		return actief;
	}

	public void setVrijstellingType(VrijstellingType vrijstellingType)
	{
		this.vrijstellingType = vrijstellingType;
	}

	public VrijstellingType getVrijstellingType()
	{
		return vrijstellingType;
	}

}
