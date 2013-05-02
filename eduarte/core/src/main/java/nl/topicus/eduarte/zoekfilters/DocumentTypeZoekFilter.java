package nl.topicus.eduarte.zoekfilters;

import nl.topicus.eduarte.entities.bijlage.DocumentCategorie;
import nl.topicus.eduarte.entities.bijlage.DocumentType;

import org.apache.wicket.model.IModel;

/**
 * 
 * 
 * @author loite
 */
public class DocumentTypeZoekFilter extends CodeNaamActiefZoekFilter<DocumentType>
{
	private static final long serialVersionUID = 1L;

	private IModel<DocumentCategorie> categorie;

	public DocumentTypeZoekFilter()
	{
		super(DocumentType.class);
	}

	public DocumentCategorie getCategorie()
	{
		return getModelObject(categorie);
	}

	public void setCategorie(DocumentCategorie categorie)
	{
		this.categorie = makeModelFor(categorie);
	}

}
