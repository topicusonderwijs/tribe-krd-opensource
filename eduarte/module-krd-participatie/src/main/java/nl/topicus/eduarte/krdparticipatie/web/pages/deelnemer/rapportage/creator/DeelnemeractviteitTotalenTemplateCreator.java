package nl.topicus.eduarte.krdparticipatie.web.pages.deelnemer.rapportage.creator;

import java.io.IOException;

import nl.topicus.cobra.templates.documents.DocumentTemplateType;
import nl.topicus.cobra.util.ResourceUtil;
import nl.topicus.eduarte.app.DocumentTemplateCreator;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.entities.rapportage.DocumentTemplate;
import nl.topicus.eduarte.entities.rapportage.DocumentTemplateCategorie;
import nl.topicus.eduarte.entities.rapportage.DocumentTemplateContext;
import nl.topicus.eduarte.jobs.rapportage.RapportageJob;
import nl.topicus.eduarte.krdparticipatie.web.pages.deelnemer.rapportage.DeelnemerActiviteitTotalenRapportageJob;
import nl.topicus.eduarte.krdparticipatie.web.pages.deelnemer.rapportage.DeelnemerActiviteitTotalenRapportagePromptPanel;
import nl.topicus.eduarte.krdparticipatie.web.pages.deelnemer.rapportage.DeelnemerActiviteitWaarnemingTotalenPage;
import nl.topicus.eduarte.web.components.panels.templates.RapportageConfiguratiePanel;

import org.apache.wicket.security.checks.ClassSecurityCheck;
import org.apache.wicket.security.checks.ISecurityCheck;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DeelnemeractviteitTotalenTemplateCreator implements DocumentTemplateCreator
{
	protected static Logger log =
		LoggerFactory.getLogger(DeelnemeractviteitTotalenTemplateCreator.class);

	private static class DeelnemerActiviteitRapportage extends DocumentTemplate
	{
		private static final long serialVersionUID = 1L;

		public DeelnemerActiviteitRapportage()
		{
			setOmschrijving("Deelnemer Onderwijsproduct totalen");
			setBestandsnaam("Deelnemertotalen.jrxml");
			setValid(true);
			setActief(true);
			setKopieBijContext(false);
			setContext(DocumentTemplateContext.Verbintenis);
			setCategorie(DocumentTemplateCategorie.Verbintenis);
			setType(DocumentTemplateType.JRXML);
			setValid(true);
			try
			{
				setZzzBestand(ResourceUtil.readClassPathFileAsBytes(
					DeelnemeractviteitTotalenTemplateCreator.class, getBestandsnaam()));
			}
			catch (IOException e)
			{
				log.error(e.toString(), e);
			}
		}

		@Override
		public ISecurityCheck getCustomSecurityCheck()
		{
			return new ClassSecurityCheck(DeelnemerActiviteitWaarnemingTotalenPage.class);
		}

		@Override
		public Class< ? extends RapportageConfiguratiePanel<Verbintenis>> getConfiguratiePanel()
		{
			return DeelnemerActiviteitTotalenRapportagePromptPanel.class;
		}

		@Override
		public Class< ? extends RapportageJob> getJobClass()
		{
			return DeelnemerActiviteitTotalenRapportageJob.class;
		}
	}

	@Override
	public DocumentTemplate createDocumentTemplate()
	{
		return new DeelnemerActiviteitRapportage();
	}
}
