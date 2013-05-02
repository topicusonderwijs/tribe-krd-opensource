package nl.topicus.eduarte.rapportage.creators;

import java.io.IOException;

import nl.topicus.cobra.templates.documents.DocumentTemplateType;
import nl.topicus.cobra.util.ResourceUtil;
import nl.topicus.eduarte.app.DocumentTemplateCreator;
import nl.topicus.eduarte.entities.rapportage.DocumentTemplate;
import nl.topicus.eduarte.entities.rapportage.DocumentTemplateCategorie;
import nl.topicus.eduarte.entities.rapportage.DocumentTemplateContext;
import nl.topicus.eduarte.web.pages.onderwijs.opleiding.OpleidingkaartPage;

import org.apache.wicket.security.checks.ClassSecurityCheck;
import org.apache.wicket.security.checks.ISecurityCheck;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OpleidingkaartTemplateCreator implements DocumentTemplateCreator
{
	protected static Logger log = LoggerFactory.getLogger(OpleidingkaartTemplateCreator.class);

	private static class OpleidingkaartRapportage extends DocumentTemplate
	{
		private static final long serialVersionUID = 1L;

		public OpleidingkaartRapportage()
		{
			setOmschrijving("Opleidingkaart");
			setBestandsnaam("opleidingkaart.jrxml");
			setValid(true);
			setActief(true);
			setKopieBijContext(false);
			setContext(DocumentTemplateContext.Opleiding);
			setCategorie(DocumentTemplateCategorie.Identiteit);
			setType(DocumentTemplateType.JRXML);
			try
			{
				setZzzBestand(ResourceUtil.readClassPathFileAsBytes(OpleidingkaartPage.class,
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
			return new ClassSecurityCheck(OpleidingkaartPage.class);
		}
	}

	@Override
	public DocumentTemplate createDocumentTemplate()
	{
		return new OpleidingkaartRapportage();
	}
}
