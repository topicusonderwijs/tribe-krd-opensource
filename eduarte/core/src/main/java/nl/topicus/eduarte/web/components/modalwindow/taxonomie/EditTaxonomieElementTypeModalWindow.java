package nl.topicus.eduarte.web.components.modalwindow.taxonomie;

import nl.topicus.cobra.web.components.modal.CobraModalWindow;
import nl.topicus.cobra.web.components.modal.CobraModalWindowBasePanel;
import nl.topicus.cobra.web.components.modal.CobraModalWindowEmptyPanel;
import nl.topicus.eduarte.entities.taxonomie.TaxonomieElementType;

import org.apache.wicket.ajax.AjaxRequestTarget;

/**
 * Modal window voor het bewerken van een taxonomie-elementtype.
 * 
 * @author loite
 */
public class EditTaxonomieElementTypeModalWindow extends CobraModalWindow<TaxonomieElementType>
{
	private static final long serialVersionUID = 1L;

	public EditTaxonomieElementTypeModalWindow(String id)
	{
		super(id);
		setInitialWidth(600);
		setInitialHeight(400);
		setTitle("Taxonomie-elementtype bewerken");
	}

	public void show(AjaxRequestTarget target, TaxonomieElementType type)
	{
		getModalWindow().setContent(createContents(getModalWindow().getContentId(), type));
		getModalWindow().show(target);
		target.appendJavascript(getContents().onShowStatement().render().toString());
	}

	@Override
	protected CobraModalWindowBasePanel<TaxonomieElementType> createContents(String id)
	{
		return createContents(id, null);
	}

	protected CobraModalWindowBasePanel<TaxonomieElementType> createContents(String id,
			TaxonomieElementType type)
	{
		if (type == null)
			return new CobraModalWindowEmptyPanel<TaxonomieElementType>(id, this);
		return new EditTaxonomieElementTypeBasePanel(id, type, this);
	}

}
