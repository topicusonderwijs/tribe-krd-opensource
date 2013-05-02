package nl.topicus.eduarte.web.components.quicksearch.deelnemermedewerkergroep;

import nl.topicus.cobra.util.TimeUtil;
import nl.topicus.cobra.web.components.quicksearch.AbstractSearchEditor;
import nl.topicus.cobra.web.components.wiquery.auto.QuickSearchField;
import nl.topicus.cobra.zoekfilters.ZoekFilterCopyManager;
import nl.topicus.eduarte.entities.participatie.DeelnemerMedewerkerGroep;
import nl.topicus.eduarte.web.components.modalwindow.AbstractZoekenModalWindow;
import nl.topicus.eduarte.web.components.modalwindow.deelnemermedewerkergroep.DeelnemerMedewerkerGroepModalWindow;
import nl.topicus.eduarte.zoekfilters.DeelnemerMedewerkerGroepZoekFilter;
import nl.topicus.eduarte.zoekfilters.OrganisatieEenheidLocatieAuthorizationContext;

import org.apache.wicket.model.IModel;

public class DeelnemerMedewerkerGroepSearchEditor extends
		AbstractSearchEditor<DeelnemerMedewerkerGroep>
{
	private static final long serialVersionUID = 1L;

	private static final DeelnemerMedewerkerGroepZoekFilter getDefaultFilter()
	{
		DeelnemerMedewerkerGroepZoekFilter filter = new DeelnemerMedewerkerGroepZoekFilter();
		filter.addOrderByProperty("omschrijving");
		filter.setPeildatum(TimeUtil.getInstance().currentDate());
		return filter;
	}

	private DeelnemerMedewerkerGroepZoekFilter filter;

	public DeelnemerMedewerkerGroepSearchEditor(String id, IModel<DeelnemerMedewerkerGroep> model)
	{
		this(id, model, getDefaultFilter());
	}

	public DeelnemerMedewerkerGroepSearchEditor(String id, IModel<DeelnemerMedewerkerGroep> model,
			DeelnemerMedewerkerGroepZoekFilter filter)
	{
		super(id, model);
		this.filter = filter;
		filter.setAuthorizationContext(new OrganisatieEenheidLocatieAuthorizationContext(this));
	}

	@Override
	public AbstractZoekenModalWindow<DeelnemerMedewerkerGroep> createModelWindow(String id,
			IModel<DeelnemerMedewerkerGroep> model)
	{
		return new DeelnemerMedewerkerGroepModalWindow(id, model, filter);
	}

	@Override
	public QuickSearchField<DeelnemerMedewerkerGroep> createSearchField(String id,
			IModel<DeelnemerMedewerkerGroep> model)
	{
		return new DeelnemerMedewerkerGroepQuicksearchField(id, model, new ZoekFilterCopyManager()
			.copyObject(filter));
	}
}