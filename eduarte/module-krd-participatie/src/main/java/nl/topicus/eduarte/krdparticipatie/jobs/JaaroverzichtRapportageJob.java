package nl.topicus.eduarte.krdparticipatie.jobs;

import java.io.ByteArrayOutputStream;
import java.util.List;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperPrint;
import nl.topicus.cobra.quartz.JobInfo;
import nl.topicus.cobra.templates.exceptions.TemplateException;
import nl.topicus.eduarte.entities.bijlage.BijlageEntiteit;
import nl.topicus.eduarte.entities.bijlage.IBijlageKoppelEntiteit;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.jobs.rapportage.RapportageJob;
import nl.topicus.eduarte.krdparticipatie.rapportage.JaaroverzichtRapportageUtil;
import nl.topicus.eduarte.participatie.zoekfilters.WaarnemingOverzichtZoekFilter;

@JobInfo(name = JaaroverzichtRapportageJob.JOB_NAAM)
public class JaaroverzichtRapportageJob extends RapportageJob
{
	public static final String JOB_NAAM = "Jaaroverzicht Rapportage";

	private WaarnemingOverzichtZoekFilter filter;

	@Override
	protected <T extends IBijlageKoppelEntiteit< ? extends BijlageEntiteit>> void generateDocuments(
			Class<T> contextClass) throws TemplateException
	{
		if (!contextClass.equals(Verbintenis.class))
			throw new RuntimeException("JaaroverzichtRapportageJob werkt alleen met Verbintenis");
		try
		{
			initTemplateObjects();
			filter =
				(WaarnemingOverzichtZoekFilter) getJobDataMap(Verbintenis.class).getConfiguration();
			List<Verbintenis> verbintenissen = getSelection(Verbintenis.class);
			JaaroverzichtRapportageUtil rapport = new JaaroverzichtRapportageUtil(filter);
			JasperPrint print =
				rapport.generateDocuments(verbintenissen, documentTemplate.getZzzBestand());
			ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
			JasperExportManager.exportReportToPdfStream(print, byteStream);
			currentDocument.setOutputStream(byteStream);
		}
		catch (JRException e)
		{
			log.error(e.toString(), e);
			rollbackJob(e);
		}
	}
}