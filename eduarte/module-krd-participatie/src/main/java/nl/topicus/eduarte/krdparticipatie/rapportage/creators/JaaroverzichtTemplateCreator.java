package nl.topicus.eduarte.krdparticipatie.rapportage.creators;

import java.io.IOException;

import nl.topicus.cobra.templates.documents.DocumentTemplateType;
import nl.topicus.cobra.util.ResourceUtil;
import nl.topicus.eduarte.app.DocumentTemplateCreator;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.entities.rapportage.DocumentTemplate;
import nl.topicus.eduarte.entities.rapportage.DocumentTemplateCategorie;
import nl.topicus.eduarte.entities.rapportage.DocumentTemplateContext;
import nl.topicus.eduarte.jobs.rapportage.RapportageJob;
import nl.topicus.eduarte.krdparticipatie.jobs.JaaroverzichtRapportageJob;
import nl.topicus.eduarte.krdparticipatie.web.components.panels.rapportage.JaaroverzichtRapportagePanel;
import nl.topicus.eduarte.krdparticipatie.web.pages.deelnemer.dossier.DeelnemerJaarOverzichtPage;
import nl.topicus.eduarte.web.components.panels.templates.RapportageConfiguratiePanel;

import org.apache.wicket.security.checks.ClassSecurityCheck;
import org.apache.wicket.security.checks.ISecurityCheck;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JaaroverzichtTemplateCreator implements DocumentTemplateCreator
{
	protected static Logger log = LoggerFactory.getLogger(JaaroverzichtTemplateCreator.class);

	private static class JaaroverzichtRapportage extends DocumentTemplate
	{
		private static final long serialVersionUID = 1L;

		public JaaroverzichtRapportage()
		{
			setOmschrijving("Jaaroverzicht");
			setBestandsnaam("DeelnemerWaarnemingenOverzicht.jrxml");
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
					DeelnemerJaarOverzichtPage.class, getBestandsnaam()));
			}
			catch (IOException e)
			{
				log.error(e.toString(), e);
			}
		}

		@Override
		public ISecurityCheck getCustomSecurityCheck()
		{
			return new ClassSecurityCheck(DeelnemerJaarOverzichtPage.class);
		}

		@Override
		public Class< ? extends RapportageConfiguratiePanel<Verbintenis>> getConfiguratiePanel()
		{
			return JaaroverzichtRapportagePanel.class;
		}

		@Override
		public Class< ? extends RapportageJob> getJobClass()
		{
			return JaaroverzichtRapportageJob.class;
		}
	}

	@Override
	public DocumentTemplate createDocumentTemplate()
	{
		return new JaaroverzichtRapportage();
	}
}
