package nl.topicus.eduarte.web.components.quicksearch.medewerker;

import nl.topicus.cobra.web.components.quicksearch.AbstractSearchEditor;
import nl.topicus.cobra.web.components.wiquery.auto.QuickSearchField;
import nl.topicus.cobra.zoekfilters.ZoekFilterCopyManager;
import nl.topicus.eduarte.entities.personen.Medewerker;
import nl.topicus.eduarte.web.components.modalwindow.AbstractZoekenModalWindow;
import nl.topicus.eduarte.web.components.modalwindow.medewerker.MedewerkerSelectieModalWindow;
import nl.topicus.eduarte.zoekfilters.MedewerkerZoekFilter;
import nl.topicus.eduarte.zoekfilters.OrganisatieEenheidLocatieAuthorizationContext;

import org.apache.wicket.model.IModel;

public class MedewerkerSearchEditor extends AbstractSearchEditor<Medewerker>
{
	private static final long serialVersionUID = 1L;

	private static final MedewerkerZoekFilter getDefaultFilter()
	{
		MedewerkerZoekFilter filter = new MedewerkerZoekFilter();
		filter.addOrderByProperty("persoon.achternaam");
		return filter;
	}

	private MedewerkerZoekFilter filter;

	private boolean lockHeeftAccount;

	public MedewerkerSearchEditor(String id, IModel<Medewerker> model)
	{
		this(id, model, getDefaultFilter());
	}

	public MedewerkerSearchEditor(String id, IModel<Medewerker> model, MedewerkerZoekFilter filter)
	{
		this(id, model, filter, false);
	}

	public MedewerkerSearchEditor(String id, IModel<Medewerker> model, MedewerkerZoekFilter filter,
			boolean lockHeeftAccount)
	{
		super(id, model);
		this.filter = filter;
		filter.setAuthorizationContext(new OrganisatieEenheidLocatieAuthorizationContext(this));
		this.lockHeeftAccount = lockHeeftAccount;
	}

	@Override
	public AbstractZoekenModalWindow<Medewerker> createModelWindow(String id,
			IModel<Medewerker> model)
	{
		return new MedewerkerSelectieModalWindow(id, model, filter, lockHeeftAccount);
	}

	@Override
	public QuickSearchField<Medewerker> createSearchField(String id, IModel<Medewerker> model)
	{
		return new MedewerkerQuickSearchField(id, model, new ZoekFilterCopyManager()
			.copyObject(filter));
	}
}
