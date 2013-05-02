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
import nl.topicus.eduarte.krdparticipatie.rapportage.WeekoverzichtRapportageUtil;
import nl.topicus.eduarte.participatie.zoekfilters.AanwezigheidsWeekFilter;

@JobInfo(name = WeekoverzichtRapportageJob.JOB_NAAM)
public class WeekoverzichtRapportageJob extends RapportageJob
{
	public static final String JOB_NAAM = "Weekoverzicht Rapportage";

	private AanwezigheidsWeekFilter filter;

	@Override
	protected <T extends IBijlageKoppelEntiteit< ? extends BijlageEntiteit>> void generateDocuments(
			Class<T> contextClass) throws TemplateException
	{
		if (!contextClass.equals(Verbintenis.class))
			throw new RuntimeException("WeekoverzichtRapportageJob werkt alleen met Verbintenis");
		try
		{
			initTemplateObjects();
			filter = (AanwezigheidsWeekFilter) getJobDataMap(Verbintenis.class).getConfiguration();
			List<Verbintenis> verbintenissen = getSelection(Verbintenis.class);
			WeekoverzichtRapportageUtil rapport = new WeekoverzichtRapportageUtil(filter);
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