/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.web.components.datapanel;

import nl.topicus.cobra.modelsv2.ModelFactory;
import nl.topicus.cobra.util.ComponentUtil;
import nl.topicus.cobra.web.components.datapanel.columns.AbstractCustomColumn;
import nl.topicus.eduarte.entities.bijlage.DocumentCategorie;
import nl.topicus.eduarte.providers.DeelnemerProvider;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;

/**
 * Column die aangeeft of een deelnemer minimaal een document van een bepaalde categorie
 * heeft.
 * 
 * @author loite
 */
public class DocumentCategorieAanwezigColumn<T extends DeelnemerProvider> extends
		AbstractCustomColumn<T>
{
	private static final long serialVersionUID = 1L;

	private final IModel<DocumentCategorie> categorieModel;

	public DocumentCategorieAanwezigColumn(String id, String header, DocumentCategorie categorie)
	{
		super(id, header);
		this.categorieModel = ModelFactory.getModel(categorie);
	}

	private DocumentCategorie getCategorie()
	{
		return categorieModel.getObject();
	}

	@Override
	public void populateItem(WebMarkupContainer cell, String componentId, WebMarkupContainer row,
			IModel<T> rowModel, int span)
	{
		DeelnemerProvider provider = rowModel.getObject();
		boolean heeftBijlage = provider.getDeelnemer().heeftBijlageVanCategorie(getCategorie());
		cell.add(new Label(componentId, heeftBijlage ? "Ja" : "Nee"));
	}

	@Override
	public void detach()
	{
		super.detach();
		ComponentUtil.detachQuietly(categorieModel);
	}

}
