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
import nl.topicus.eduarte.krdparticipatie.jobs.WeekoverzichtRapportageJob;
import nl.topicus.eduarte.krdparticipatie.web.components.panels.rapportage.WeekoverzichtRapportagePanel;
import nl.topicus.eduarte.krdparticipatie.web.pages.deelnemer.dossier.ParticipatieWeekOverzichtPage;
import nl.topicus.eduarte.web.components.panels.templates.RapportageConfiguratiePanel;

import org.apache.wicket.security.checks.ClassSecurityCheck;
import org.apache.wicket.security.checks.ISecurityCheck;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WeekoverzichtTemplateCreator implements DocumentTemplateCreator
{
	protected static Logger log = LoggerFactory.getLogger(WeekoverzichtTemplateCreator.class);

	private static class WeekoverzichtRapportage extends DocumentTemplate
	{
		private static final long serialVersionUID = 1L;

		public WeekoverzichtRapportage()
		{
			setOmschrijving("Weekoverzicht");
			setBestandsnaam("ParticipatieLandscapeWeekView.jrxml");
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
					ParticipatieWeekOverzichtPage.class, getBestandsnaam()));
			}
			catch (IOException e)
			{
				log.error(e.toString(), e);
			}
		}

		@Override
		public ISecurityCheck getCustomSecurityCheck()
		{
			return new ClassSecurityCheck(ParticipatieWeekOverzichtPage.class);
		}

		@Override
		public Class< ? extends RapportageConfiguratiePanel<Verbintenis>> getConfiguratiePanel()
		{
			return WeekoverzichtRapportagePanel.class;
		}

		@Override
		public Class< ? extends RapportageJob> getJobClass()
		{
			return WeekoverzichtRapportageJob.class;
		}
	}

	@Override
	public DocumentTemplate createDocumentTemplate()
	{
		return new WeekoverzichtRapportage();
	}
}
