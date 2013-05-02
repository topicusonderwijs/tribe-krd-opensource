package nl.topicus.eduarte.web.components.quicksearch.deelnemer;

import nl.topicus.cobra.web.components.quicksearch.AbstractBaseSearchEditor;
import nl.topicus.cobra.web.components.wiquery.auto.QuickSearchField;
import nl.topicus.cobra.zoekfilters.ZoekFilterCopyManager;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.entities.personen.Deelnemer;
import nl.topicus.eduarte.web.components.modalwindow.AbstractZoekenModalWindow;
import nl.topicus.eduarte.web.components.modalwindow.deelnemer.DeelnemerSelectieModalWindow;
import nl.topicus.eduarte.zoekfilters.OrganisatieEenheidLocatieAuthorizationContext;
import nl.topicus.eduarte.zoekfilters.VerbintenisZoekFilter;

import org.apache.wicket.model.IModel;

public class DeelnemerSearchEditor extends AbstractBaseSearchEditor<Verbintenis, Deelnemer>
{
	private static final long serialVersionUID = 1L;

	private static final VerbintenisZoekFilter getDefaultFilter()
	{
		VerbintenisZoekFilter filter = new VerbintenisZoekFilter();
		filter.addOrderByProperty("persoon.achternaam");
		return filter;
	}

	private VerbintenisZoekFilter filter;

	public DeelnemerSearchEditor(String id, IModel<Deelnemer> model)
	{
		this(id, model, getDefaultFilter());
	}

	public DeelnemerSearchEditor(String id, IModel<Deelnemer> model, VerbintenisZoekFilter filter)
	{
		super(id, model);
		filter.setAuthorizationContext(new OrganisatieEenheidLocatieAuthorizationContext(this));
		this.filter = filter;
	}

	@Override
	public AbstractZoekenModalWindow<Verbintenis> createModelWindow(String id,
			IModel<Deelnemer> model)
	{
		return new DeelnemerSelectieModalWindow(id, new ReturnDeelnemerModel(model), filter);
	}

	@Override
	public QuickSearchField<Verbintenis> createSearchField(String id, IModel<Deelnemer> model)
	{
		return new VerbintenisQuickSearchField(id, new ReturnDeelnemerModel(model),
			new ZoekFilterCopyManager().copyObject(filter));
	}

	@Override
	protected Deelnemer convertTToS(Verbintenis search)
	{
		return search == null ? null : search.getDeelnemer();
	}
}
