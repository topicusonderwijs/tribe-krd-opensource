package nl.topicus.eduarte.zoekfilters;

import nl.topicus.cobra.web.components.form.AutoForm;
import nl.topicus.cobra.web.components.form.AutoFormEmbedded;
import nl.topicus.eduarte.entities.resultaatstructuur.Resultaatstructuur;
import nl.topicus.eduarte.entities.resultaatstructuur.Toets;
import nl.topicus.eduarte.entities.resultaatstructuur.ToetsCodeFilter;
import nl.topicus.eduarte.entities.resultaatstructuur.Toets.Scoreschaal;
import nl.topicus.eduarte.web.components.choice.SamengesteldeToetsCombobox;

import org.apache.wicket.model.IModel;

public class ToetsZoekFilter extends AbstractZoekFilter<Toets>
{
	private static final long serialVersionUID = 1L;

	@AutoFormEmbedded
	private ResultaatstructuurZoekFilter resultaatstructuurFilter;

	private IModel<Resultaatstructuur> resultaatstructuur;

	private String code;

	@AutoForm(label = "Toets", editorClass = SamengesteldeToetsCombobox.class, htmlClasses = "unit_100")
	private String codePath;

	private String naam;

	private IModel<ToetsCodeFilter> toetsCodeFilter;

	private boolean ignoreToetsCodeFilter = true;

	private Scoreschaal scoreschaal;

	private Boolean verwijsbaar;

	private Boolean eindResultaat;

	private Boolean samengesteldeToets;

	public ToetsZoekFilter()
	{
	}

	public ToetsZoekFilter(ResultaatstructuurZoekFilter resultaatstructuurFilter)
	{
		setResultaatstructuurFilter(resultaatstructuurFilter);
	}

	public ResultaatstructuurZoekFilter getResultaatstructuurFilter()
	{
		return resultaatstructuurFilter;
	}

	public void setResultaatstructuurFilter(ResultaatstructuurZoekFilter resultaatstructuurFilter)
	{
		this.resultaatstructuurFilter = resultaatstructuurFilter;
	}

	public Resultaatstructuur getResultaatstructuur()
	{
		return getModelObject(resultaatstructuur);
	}

	public void setResultaatstructuur(Resultaatstructuur resultaatstructuur)
	{
		this.resultaatstructuur = makeModelFor(resultaatstructuur);
	}

	public void setCode(String code)
	{
		this.code = code;
	}

	public String getCode()
	{
		return code;
	}

	public String getCodePath()
	{
		return codePath;
	}

	public void setCodePath(String codePath)
	{
		this.codePath = codePath;
	}

	public void setNaam(String naam)
	{
		this.naam = naam;
	}

	public String getNaam()
	{
		return naam;
	}

	public void setToetsCodeFilter(ToetsCodeFilter toetsCodeFilter)
	{
		this.toetsCodeFilter = makeModelFor(toetsCodeFilter);
	}

	public ToetsCodeFilter getToetsCodeFilter()
	{
		return getModelObject(toetsCodeFilter);
	}

	public boolean getIgnoreToetsCodeFilter()
	{
		return ignoreToetsCodeFilter;
	}

	public void setIgnoreToetsCodeFilter(boolean ignoreToetsCodeFilter)
	{
		this.ignoreToetsCodeFilter = ignoreToetsCodeFilter;
	}

	public void setScoreschaal(Scoreschaal scoreschaal)
	{
		this.scoreschaal = scoreschaal;
	}

	public Scoreschaal getScoreschaal()
	{
		return scoreschaal;
	}

	public Boolean getVerwijsbaar()
	{
		return verwijsbaar;
	}

	public void setVerwijsbaar(Boolean verwijsbaar)
	{
		this.verwijsbaar = verwijsbaar;
	}

	public boolean isBeperktInResultaatstructuur()
	{
		return getResultaatstructuur() != null || getResultaatstructuurFilter() != null;
	}

	public Boolean isEindResultaat()
	{
		return eindResultaat;
	}

	public void setEindResultaat(Boolean eindResultaat)
	{
		this.eindResultaat = eindResultaat;
	}

	public Boolean isSamengesteldeToets()
	{
		return samengesteldeToets;
	}

	public void setSamengesteldeToets(Boolean samengesteldeToets)
	{
		this.samengesteldeToets = samengesteldeToets;
	}
}
