package nl.topicus.cobra.templates.monitors;

import net.sf.jasperreports.engine.export.JRExportProgressMonitor;

/**
 * Class om een status update te kunnen doen. Dit is voornamelijk voor JasperReports waar
 * men niet kan opvragen hoe ver de generatie is.
 * 
 * @author hoeve
 */
public class DocumentTemplateProgressMonitor implements JRExportProgressMonitor
{
	protected MonitorListener listener;

	/**
	 * Functie welke de rapportage generatie tool kan aanroepen na elke Sectie. Dit bv om
	 * de progress aan te passen.
	 */
	public void afterWriteSection()
	{
		listener.sectionWritten();
	}

	public void registerMonitorListener(MonitorListener newListener)
	{
		this.listener = newListener;
	}

	/**
	 * @see net.sf.jasperreports.engine.export.JRExportProgressMonitor#afterPageExport()
	 */
	@Override
	public void afterPageExport()
	{
		afterWriteSection();
	}
}
