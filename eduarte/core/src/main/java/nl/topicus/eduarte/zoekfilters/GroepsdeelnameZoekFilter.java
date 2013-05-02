package nl.topicus.eduarte.zoekfilters;

import java.util.Date;

import nl.topicus.cobra.web.components.form.AutoForm;
import nl.topicus.cobra.web.components.form.AutoFormEmbedded;
import nl.topicus.eduarte.entities.groep.Groep;
import nl.topicus.eduarte.entities.groep.Groepsdeelname;
import nl.topicus.eduarte.entities.groep.Groepstype;
import nl.topicus.eduarte.entities.personen.Deelnemer;
import nl.topicus.eduarte.web.components.choice.GroepstypeCombobox;
import nl.topicus.eduarte.web.components.quicksearch.groep.GroepQuickSearchField;

import org.apache.wicket.model.IModel;

/**
 * 
 * @author loite
 * 
 */
public class GroepsdeelnameZoekFilter extends
		AbstractOrganisatieEenheidLocatieZoekFilter<Groepsdeelname>
{
	private static final long serialVersionUID = 1L;

	@AutoFormEmbedded
	private DeelnemerZoekFilter deelnemerFilter = new DeelnemerZoekFilter();

	@AutoFormEmbedded
	private GroepZoekFilter groepFilter = new GroepZoekFilter();

	private IModel<Deelnemer> deelnemer;

	@AutoForm(editorClass = GroepQuickSearchField.class, htmlClasses = "unit_160")
	private IModel<Groep> groep;

	@AutoForm(editorClass = GroepstypeCombobox.class)
	private IModel<Groepstype> groepstype;

	@AutoForm(label = "Toon toekomstige")
	private boolean toonToekomstigeDeelnames = true;

	private Date peilEindDatum;

	private Boolean beeindigdeGroepsdeelnames;

	public GroepsdeelnameZoekFilter()
	{
	}

	public GroepsdeelnameZoekFilter(Deelnemer deelnemer)
	{
		setDeelnemer(deelnemer);
	}

	public GroepsdeelnameZoekFilter(Groep groep)
	{
		setGroep(groep);
	}

	@Override
	public void setAuthorizationContext(
			OrganisatieEenheidLocatieAuthorizationContext authorizationContext)
	{
		super.setAuthorizationContext(authorizationContext);
		groepFilter.setAuthorizationContext(authorizationContext);
	}

	public Deelnemer getDeelnemer()
	{
		return getModelObject(deelnemer);
	}

	public void setDeelnemer(Deelnemer deelnemer)
	{
		this.deelnemer = makeModelFor(deelnemer);
	}

	public Groep getGroep()
	{
		return getModelObject(groep);
	}

	public void setGroep(Groep groep)
	{
		this.groep = makeModelFor(groep);
	}

	public boolean isToonToekomstigeDeelnames()
	{
		return toonToekomstigeDeelnames;
	}

	public void setToonToekomstigeDeelnames(boolean toonToekomstigeDeelnames)
	{
		this.toonToekomstigeDeelnames = toonToekomstigeDeelnames;
	}

	public void setDeelnemerFilter(DeelnemerZoekFilter deelnemerFilter)
	{
		this.deelnemerFilter = deelnemerFilter;
	}

	public DeelnemerZoekFilter getDeelnemerFilter()
	{
		return deelnemerFilter;
	}

	public void setGroepFilter(GroepZoekFilter groepFilter)
	{
		this.groepFilter = groepFilter;
	}

	public GroepZoekFilter getGroepFilter()
	{
		return groepFilter;
	}

	public void setGroepstype(Groepstype type)
	{
		this.groepstype = makeModelFor(type);
	}

	public Groepstype getGroepstype()
	{
		return getModelObject(groepstype);
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

	/**
	 * @param beeindigdeGroepsdeelnames
	 *            indien true, selecteer alleen in de peilperiode beeindigde
	 *            groepsdeelnames
	 */
	public void setBeeindigdeGroepsdeelnames(Boolean beeindigdeGroepsdeelnames)
	{
		this.beeindigdeGroepsdeelnames = beeindigdeGroepsdeelnames;
	}

	/**
	 * @return indien true, selecteer alleen in de peilperiode beeindigde groepsdeelnames
	 */
	public Boolean getBeeindigdeGroepsdeelnames()
	{
		return beeindigdeGroepsdeelnames;
	}
}
