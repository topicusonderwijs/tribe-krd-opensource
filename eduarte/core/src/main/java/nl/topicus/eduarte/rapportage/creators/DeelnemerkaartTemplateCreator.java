package nl.topicus.eduarte.rapportage.creators;

import java.io.IOException;

import nl.topicus.cobra.templates.documents.DocumentTemplateType;
import nl.topicus.cobra.util.ResourceUtil;
import nl.topicus.eduarte.app.DocumentTemplateCreator;
import nl.topicus.eduarte.entities.rapportage.DocumentTemplate;
import nl.topicus.eduarte.entities.rapportage.DocumentTemplateCategorie;
import nl.topicus.eduarte.entities.rapportage.DocumentTemplateContext;
import nl.topicus.eduarte.web.pages.deelnemer.deelnemerkaart.DeelnemerkaartPage;

import org.apache.wicket.security.checks.ClassSecurityCheck;
import org.apache.wicket.security.checks.ISecurityCheck;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DeelnemerkaartTemplateCreator implements DocumentTemplateCreator
{
	protected static Logger log = LoggerFactory.getLogger(DeelnemerkaartTemplateCreator.class);

	private static class DeelnemerkaartRapportage extends DocumentTemplate
	{
		private static final long serialVersionUID = 1L;

		public DeelnemerkaartRapportage()
		{
			setOmschrijving("Deelnemerkaart");
			setBestandsnaam("deelnemerkaart.jrxml");
			setValid(true);
			setActief(true);
			setKopieBijContext(false);
			setContext(DocumentTemplateContext.Verbintenis);
			setCategorie(DocumentTemplateCategorie.Verbintenis);
			setType(DocumentTemplateType.JRXML);
			setValid(true);
			try
			{
				setZzzBestand(ResourceUtil.readClassPathFileAsBytes(DeelnemerkaartPage.class,
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
			return new ClassSecurityCheck(DeelnemerkaartPage.class);
		}
	}

	@Override
	public DocumentTemplate createDocumentTemplate()
	{
		return new DeelnemerkaartRapportage();
	}
}
