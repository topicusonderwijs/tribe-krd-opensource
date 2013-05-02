package nl.topicus.eduarte.web.components.choice;

import java.util.ArrayList;
import java.util.List;

import nl.topicus.cobra.web.components.choice.EnumCombobox;
import nl.topicus.eduarte.entities.rapportage.DocumentTemplateCategorie;
import nl.topicus.eduarte.entities.rapportage.DocumentTemplateContext;

import org.apache.wicket.model.AbstractReadOnlyModel;

public class DocumentTemplateCategorieListModel extends
		AbstractReadOnlyModel<List<DocumentTemplateCategorie>>
{
	private static final long serialVersionUID = 1L;

	private EnumCombobox<DocumentTemplateContext> typeCombobox;

	private DocumentTemplateCategorie[] basis;

	public DocumentTemplateCategorieListModel(EnumCombobox<DocumentTemplateContext> typeCombobox,
			DocumentTemplateCategorie[] basis)
	{
		this.typeCombobox = typeCombobox;
		this.basis = basis;
	}

	@Override
	public List<DocumentTemplateCategorie> getObject()
	{
		List<DocumentTemplateCategorie> ret = new ArrayList<DocumentTemplateCategorie>();
		for (DocumentTemplateCategorie curCategorie : basis)
		{
			if (curCategorie.getBeperkteContext() == null
				|| curCategorie.getBeperkteContext() == typeCombobox.getModelObject())
				ret.add(curCategorie);
		}
		return ret;
	}
}
