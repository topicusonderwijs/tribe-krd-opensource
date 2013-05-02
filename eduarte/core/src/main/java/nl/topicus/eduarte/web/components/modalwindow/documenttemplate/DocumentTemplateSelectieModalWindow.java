package nl.topicus.eduarte.web.components.modalwindow.documenttemplate;

import nl.topicus.cobra.util.ComponentUtil;
import nl.topicus.cobra.web.components.modal.CobraModalWindowBasePanel;
import nl.topicus.eduarte.entities.rapportage.DocumentTemplate;
import nl.topicus.eduarte.web.components.modalwindow.AbstractZoekenModalWindow;
import nl.topicus.eduarte.zoekfilters.DocumentTemplateZoekFilter;

import org.apache.wicket.model.IModel;

public class DocumentTemplateSelectieModalWindow extends
		AbstractZoekenModalWindow<DocumentTemplate>
{
	private static final long serialVersionUID = 1L;

	private DocumentTemplateZoekFilter filter;

	public DocumentTemplateZoekFilter getFilter()
	{
		return filter;
	}

	public DocumentTemplateSelectieModalWindow(String id, DocumentTemplateZoekFilter filter)
	{
		this(id, null, filter);
	}

	public DocumentTemplateSelectieModalWindow(String id, IModel<DocumentTemplate> model,
			DocumentTemplateZoekFilter filter)
	{
		super(id, model, filter);
		this.filter = filter;
		setTitle("Samenvoegdocument selecteren");
	}

	@Override
	protected CobraModalWindowBasePanel<DocumentTemplate> createContents(String id)
	{
		return new DocumentTemplateSelectiePanel(id, this, filter);
	}

	@Override
	protected void onDetach()
	{
		super.onDetach();
		ComponentUtil.detachQuietly(filter);
	}
}
