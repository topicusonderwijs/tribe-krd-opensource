package nl.topicus.eduarte.rapportage.creators;

import java.io.IOException;

import nl.topicus.cobra.templates.documents.DocumentTemplateType;
import nl.topicus.cobra.util.ResourceUtil;
import nl.topicus.eduarte.app.DocumentTemplateCreator;
import nl.topicus.eduarte.entities.rapportage.DocumentTemplate;
import nl.topicus.eduarte.entities.rapportage.DocumentTemplateCategorie;
import nl.topicus.eduarte.entities.rapportage.DocumentTemplateContext;
import nl.topicus.eduarte.web.pages.groep.GroepPasfotoPage;
import nl.topicus.eduarte.web.pages.groep.GroepRapportagesPage;

import org.apache.wicket.security.checks.ClassSecurityCheck;
import org.apache.wicket.security.checks.ISecurityCheck;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GroepPasfotosTemplateCreator implements DocumentTemplateCreator
{
	protected static Logger log = LoggerFactory.getLogger(GroepPasfotosTemplateCreator.class);

	private static class GroepPasfotosRapportage extends DocumentTemplate
	{
		private static final long serialVersionUID = 1L;

		public GroepPasfotosRapportage()
		{
			setOmschrijving("Groeppasfoto's");
			setBestandsnaam("groeppasfotos.jrxml");
			setValid(true);
			setActief(true);
			setKopieBijContext(false);
			setContext(DocumentTemplateContext.Groep);
			setCategorie(DocumentTemplateCategorie.Identiteit);
			setType(DocumentTemplateType.JRXML);
			try
			{
				setZzzBestand(ResourceUtil.readClassPathFileAsBytes(GroepRapportagesPage.class,
					getBestandsnaam()));
			}
			catch (IOException e)
			{
				log.error(e.toString(), e);
			}
		}

		@Override
		public ISecurityCheck getCustomSecurityCheck()
		{
			return new ClassSecurityCheck(GroepPasfotoPage.class);
		}
	}

	@Override
	public DocumentTemplate createDocumentTemplate()
	{
		return new GroepPasfotosRapportage();
	}
}
