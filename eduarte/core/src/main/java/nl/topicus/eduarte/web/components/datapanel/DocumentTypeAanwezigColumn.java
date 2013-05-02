/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.web.components.datapanel;

import nl.topicus.cobra.modelsv2.ModelFactory;
import nl.topicus.cobra.util.ComponentUtil;
import nl.topicus.cobra.web.components.datapanel.columns.AbstractCustomColumn;
import nl.topicus.eduarte.entities.bijlage.DocumentType;
import nl.topicus.eduarte.providers.DeelnemerProvider;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;

/**
 * Column die aangeeft of een deelnemer minimaal een document van een bepaald type heeft.
 * 
 * @author loite
 */
public class DocumentTypeAanwezigColumn extends AbstractCustomColumn<DeelnemerProvider>
{
	private static final long serialVersionUID = 1L;

	private final IModel<DocumentType> typeModel;

	public DocumentTypeAanwezigColumn(String id, String header, DocumentType type)
	{
		super(id, header);
		this.typeModel = ModelFactory.getModel(type);
	}

	private DocumentType getType()
	{
		return typeModel.getObject();
	}

	@Override
	public void populateItem(WebMarkupContainer cell, String componentId, WebMarkupContainer row,
			IModel<DeelnemerProvider> rowModel, int span)
	{
		DeelnemerProvider provider = rowModel.getObject();
		boolean heeftBijlage = provider.getDeelnemer().heeftBijlageVanType(getType());
		cell.add(new Label(componentId, heeftBijlage ? "Ja" : "Nee"));
	}

	@Override
	public void detach()
	{
		super.detach();
		ComponentUtil.detachQuietly(typeModel);
	}

}
