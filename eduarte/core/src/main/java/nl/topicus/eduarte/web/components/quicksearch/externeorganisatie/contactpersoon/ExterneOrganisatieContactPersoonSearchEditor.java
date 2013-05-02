package nl.topicus.eduarte.web.components.quicksearch.externeorganisatie.contactpersoon;

import nl.topicus.cobra.util.ComponentUtil;
import nl.topicus.cobra.web.components.quicksearch.AbstractSearchEditor;
import nl.topicus.cobra.web.components.wiquery.auto.QuickSearchField;
import nl.topicus.cobra.zoekfilters.ZoekFilterCopyManager;
import nl.topicus.eduarte.entities.organisatie.ExterneOrganisatie;
import nl.topicus.eduarte.entities.personen.ExterneOrganisatieContactPersoon;
import nl.topicus.eduarte.web.components.modalwindow.AbstractZoekenModalWindow;
import nl.topicus.eduarte.web.components.modalwindow.externeorganisatie.contactpersoon.ExterneOrganisatieContactPersoonSelectieModalWindow;
import nl.topicus.eduarte.zoekfilters.ExterneOrganisatieContactPersoonZoekFilter;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.model.IModel;

public class ExterneOrganisatieContactPersoonSearchEditor extends
		AbstractSearchEditor<ExterneOrganisatieContactPersoon>
{
	private static final long serialVersionUID = 1L;

	private ExterneOrganisatieContactPersoonZoekFilter filter;

	private ExterneOrganisatieContactPersoonQuickSearchField quickSearchField;

	public ExterneOrganisatieContactPersoonSearchEditor(String id,
			IModel<ExterneOrganisatieContactPersoon> model,
			ExterneOrganisatieContactPersoonZoekFilter filter)
	{
		super(id, model);

		if (filter == null)
			throw new IllegalArgumentException("filter is null");

		this.filter = filter;
	}

	public ExterneOrganisatieContactPersoonSearchEditor(String id,
			IModel<ExterneOrganisatieContactPersoon> model,
			IModel<ExterneOrganisatie> externeOrganisatieModel)
	{
		super(id, model);
		this.filter = new ExterneOrganisatieContactPersoonZoekFilter(externeOrganisatieModel);
	}

	@Override
	public AbstractZoekenModalWindow<ExterneOrganisatieContactPersoon> createModelWindow(String id,
			IModel<ExterneOrganisatieContactPersoon> model)
	{
		return new ExterneOrganisatieContactPersoonSelectieModalWindow(id, model, filter);
	}

	@Override
	public QuickSearchField<ExterneOrganisatieContactPersoon> createSearchField(String id,
			IModel<ExterneOrganisatieContactPersoon> model)
	{
		ExterneOrganisatieContactPersoonZoekFilter filterCopy =
			new ZoekFilterCopyManager().copyObject(filter);
		filterCopy.setExterneOrganisatie(filter.getExterneOrganisatieModel());
		quickSearchField =
			new ExterneOrganisatieContactPersoonQuickSearchField(id, model, filterCopy);
		return quickSearchField;
	}

	@Override
	protected void onDetach()
	{
		ComponentUtil.detachQuietly(quickSearchField);
		super.onDetach();
	}

	/**
	 * Zorgt ervoor dat als er maar een waarde mogelijk is dat die automatisch
	 * geselecteerd wordt.
	 */
	public void updateQuickSearchField(AjaxRequestTarget target)
	{
		quickSearchField.convertInputFromRawInput("");
		quickSearchField.updateModel();
		target.addComponent(quickSearchField);
	}

}
