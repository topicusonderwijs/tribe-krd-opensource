package nl.topicus.eduarte.resultaten.web.components.quicksearch;

import java.util.List;

import nl.topicus.cobra.web.components.panels.AbstractQuickSearchToevoegenPanel;
import nl.topicus.cobra.web.components.quicksearch.AbstractSearchEditor;
import nl.topicus.eduarte.entities.resultaatstructuur.Toets;
import nl.topicus.eduarte.entities.resultaatstructuur.ToetsVerwijzing;
import nl.topicus.eduarte.zoekfilters.ToetsZoekFilter;

import org.apache.wicket.model.IModel;

public class ToetsVerwijzingQuickSearchToevoegenPanel extends
		AbstractQuickSearchToevoegenPanel<Toets, ToetsVerwijzing>
{
	private static final long serialVersionUID = 1L;

	private IModel<Toets> toetsModel;

	public ToetsVerwijzingQuickSearchToevoegenPanel(String id, IModel<List<ToetsVerwijzing>> model,
			IModel<Toets> toetsModel)
	{
		super(id, model, "Toetsverwijzingen");
		this.toetsModel = toetsModel;
	}

	@Override
	protected AbstractSearchEditor<Toets> createSearchEditor(String id, IModel<Toets> model)
	{
		ToetsZoekFilter filter = new ToetsZoekFilter();
		filter.setVerwijsbaar(true);
		return new ToetsSearchEditor(id, model, filter);
	}

	@Override
	protected Toets convertKtoT(ToetsVerwijzing object)
	{
		return object.getSchrijvenIn();
	}

	@Override
	protected ToetsVerwijzing createNewK(Toets newObject)
	{
		Toets lezenUit = getToets();
		ToetsVerwijzing verwijzing = new ToetsVerwijzing();
		verwijzing.setLezenUit(lezenUit);
		verwijzing.setSchrijvenIn(newObject);
		return verwijzing;
	}

	@Override
	protected String getWidth()
	{
		return "";
	}

	private Toets getToets()
	{
		return toetsModel.getObject();
	}

	@Override
	protected void setTonK(Toets objectT, ToetsVerwijzing objectK)
	{
		objectK.setSchrijvenIn(objectT);
	}

	@Override
	protected void onDetach()
	{
		super.onDetach();
		toetsModel.detach();
	}
}
