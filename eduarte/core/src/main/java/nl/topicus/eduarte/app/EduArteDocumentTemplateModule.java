package nl.topicus.eduarte.app;

import java.util.List;

import nl.topicus.cobra.modules.CobraModule;
import nl.topicus.eduarte.entities.rapportage.DocumentTemplate;

public interface EduArteDocumentTemplateModule extends CobraModule
{
	public List<DocumentTemplate> getRegisteredDocumentTemplates();
}
