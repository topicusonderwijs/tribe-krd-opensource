package nl.topicus.eduarte.web.pages.beheer.rapportage;

import java.io.StringWriter;

import nl.topicus.cobra.templates.ExtractPropertyUtil;
import nl.topicus.eduarte.entities.rapportage.DocumentTemplateContext;

public class ContextMergeFieldResolver implements MergeFieldResolver
{
	private static final long serialVersionUID = 1L;

	private DocumentTemplateContext context;

	public ContextMergeFieldResolver(DocumentTemplateContext context)
	{
		this.context = context;
	}

	@Override
	public void resolveMergeFields(ExtractPropertyUtil propertyExtractor, StringWriter writer)
	{
		propertyExtractor.extractClass(context.getContextClass(), context.getModelName(), writer);
	}
}
