package nl.topicus.eduarte.web.components.resultaat;

import java.util.List;

import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.templates.annotations.Exportable;
import nl.topicus.cobra.util.ComponentUtil;
import nl.topicus.cobra.web.components.form.AutoForm;
import nl.topicus.cobra.zoekfilters.ZoekFilterCopyManager;
import nl.topicus.eduarte.dao.helpers.ResultaatstructuurDataAccessHelper;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.entities.landelijk.Cohort;
import nl.topicus.eduarte.entities.resultaatstructuur.Resultaatstructuur;
import nl.topicus.eduarte.entities.resultaatstructuur.ResultaatstructuurCategorie;
import nl.topicus.eduarte.entities.resultaatstructuur.ToetsCodeFilter;
import nl.topicus.eduarte.entities.resultaatstructuur.Resultaatstructuur.Type;
import nl.topicus.eduarte.web.components.panels.templates.RapportageConfiguratieFactory;
import nl.topicus.eduarte.zoekfilters.ResultaatstructuurZoekFilter;
import nl.topicus.eduarte.zoekfilters.ToetsZoekFilter;

@Exportable
public class ResultaatRapportageConfiguratieFactory implements
		RapportageConfiguratieFactory<Verbintenis>
{
	private static final long serialVersionUID = 1L;

	private ToetsZoekFilter filter;

	public ResultaatRapportageConfiguratieFactory()
	{
	}

	public ResultaatRapportageConfiguratieFactory(ToetsZoekFilter filter)
	{
		this.filter = filter;
	}

	public ResultaatRapportageConfiguratieFactory prepareForJob()
	{
		filter = new ZoekFilterCopyManager().copyObject(filter);
		return this;
	}

	public ToetsZoekFilter getToetsFilter()
	{
		return filter;
	}

	public ResultaatstructuurZoekFilter getStructuurFilter()
	{
		return filter.getResultaatstructuurFilter();
	}

	@Exportable
	public Type getType()
	{
		return getStructuurFilter().getType();
	}

	public void setType(Type type)
	{
		getStructuurFilter().setType(type);
	}

	@Exportable
	public ResultaatstructuurCategorie getCategorie()
	{
		return getStructuurFilter().getCategorie();
	}

	public void setCategorie(ResultaatstructuurCategorie categorie)
	{
		getStructuurFilter().setCategorie(categorie);
	}

	@Exportable
	public Cohort getCohort()
	{
		return getStructuurFilter().getCohort();
	}

	public void setCohort(Cohort cohort)
	{
		getStructuurFilter().setCohort(cohort);
	}

	@AutoForm(label = "Gekoppeld aan verbintenis")
	@Exportable
	public boolean isAlleenGekoppeldAanVerbintenis()
	{
		return getStructuurFilter().isAlleenGekoppeldAanVerbintenis();
	}

	public void setAlleenGekoppeldAanVerbintenis(boolean alleenGekoppeldAanVerbintenis)
	{
		getStructuurFilter().setAlleenGekoppeldAanVerbintenis(alleenGekoppeldAanVerbintenis);
	}

	@Exportable
	public ToetsCodeFilter getToetsCodeFilter()
	{
		return filter.getToetsCodeFilter();
	}

	public void setToetsCodeFilter(ToetsCodeFilter toetsCodeFilter)
	{
		filter.setToetsCodeFilter(toetsCodeFilter);
	}

	@Exportable
	public List<Resultaatstructuur> getResultaatstructuren()
	{
		return DataAccessRegistry.getHelper(ResultaatstructuurDataAccessHelper.class).list(
			getStructuurFilter());
	}

	@Override
	public Object createConfiguratie(Verbintenis contextObject)
	{
		return new RapportageResultaten(this, contextObject);
	}

	@Override
	public void detach()
	{
		ComponentUtil.detachQuietly(filter);
	}
}
