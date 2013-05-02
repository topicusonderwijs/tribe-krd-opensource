package nl.topicus.eduarte.web.components.modalwindow.documenttemplate;

import nl.topicus.cobra.web.components.datapanel.CustomDataPanel;
import nl.topicus.cobra.web.components.modal.CobraModalWindow;
import nl.topicus.eduarte.dao.helpers.DocumentTemplateDataAccessHelper;
import nl.topicus.eduarte.entities.rapportage.DocumentTemplate;
import nl.topicus.eduarte.web.components.modalwindow.AbstractZoekenPanel;
import nl.topicus.eduarte.web.components.panels.datapanel.table.DocumentTemplateTable;
import nl.topicus.eduarte.web.components.panels.filter.DocumentTemplateZoekFilterPanel;
import nl.topicus.eduarte.zoekfilters.DocumentTemplateZoekFilter;

import org.apache.wicket.Component;

public class DocumentTemplateSelectiePanel extends
		AbstractZoekenPanel<DocumentTemplate, DocumentTemplateZoekFilter>
{
	private static final long serialVersionUID = 1L;

	public DocumentTemplateSelectiePanel(String id, CobraModalWindow<DocumentTemplate> window,
			DocumentTemplateZoekFilter filter)
	{
		super(id, window, filter, DocumentTemplateDataAccessHelper.class,
			new DocumentTemplateTable());
	}

	@Override
	protected Component createFilterPanel(String id, DocumentTemplateZoekFilter filter,
			CustomDataPanel<DocumentTemplate> datapanel)
	{
		return new DocumentTemplateZoekFilterPanel(id, filter, datapanel);
	}
}
