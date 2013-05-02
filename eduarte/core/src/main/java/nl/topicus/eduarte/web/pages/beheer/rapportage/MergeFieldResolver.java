package nl.topicus.eduarte.web.pages.beheer.rapportage;

import java.io.Serializable;
import java.io.StringWriter;

import nl.topicus.cobra.templates.ExtractPropertyUtil;

public interface MergeFieldResolver extends Serializable
{
	public void resolveMergeFields(ExtractPropertyUtil propertyExtractor, StringWriter writer);
}
