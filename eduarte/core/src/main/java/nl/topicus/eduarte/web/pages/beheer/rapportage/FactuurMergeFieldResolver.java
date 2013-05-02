package nl.topicus.eduarte.web.pages.beheer.rapportage;

import java.io.StringWriter;

import nl.topicus.cobra.templates.ExtractPropertyUtil;
import nl.topicus.eduarte.entities.rapportage.DocumentTemplateContext;

public class FactuurMergeFieldResolver implements MergeFieldResolver
{
	private static final long serialVersionUID = 1L;

	public FactuurMergeFieldResolver()
	{
	}

	@Override
	public void resolveMergeFields(ExtractPropertyUtil propertyExtractor, StringWriter writer)
	{
		try
		{
			propertyExtractor.extractClass(Class
				.forName("nl.topicus.eduarte.financieel.entities.Factuur"),
				DocumentTemplateContext.Factuur.getModelName(), writer);
		}
		catch (ClassNotFoundException e)
		{
			// doe niets
		}
	}
}
