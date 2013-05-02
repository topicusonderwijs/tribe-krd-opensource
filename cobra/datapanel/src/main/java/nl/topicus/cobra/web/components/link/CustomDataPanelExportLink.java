package nl.topicus.cobra.web.components.link;

import java.util.Map;

import nl.topicus.cobra.fileresources.CsvFileResourceStream;
import nl.topicus.cobra.util.StringUtil;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanel;
import nl.topicus.cobra.web.components.dataview.ExportableDataView;

import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.protocol.http.WebResponse;
import org.apache.wicket.request.target.resource.ResourceStreamRequestTarget;

/**
 * Exportlink specifiek voor custom datapanel.
 * 
 * @author loite
 * 
 */
public class CustomDataPanelExportLink<T> extends Link<T>
{
	private static final long serialVersionUID = 1L;

	private static final int MAX_ROWS_IN_REQUEST = 2000;

	private final CustomDataPanel<T> datapanel;

	private final ExportableDataView<T> rows;

	private String bestandsnaam;

	private final IModel<String> header;

	public CustomDataPanelExportLink(String id, CustomDataPanel<T> datapanel,
			ExportableDataView<T> rows)
	{
		this(id, datapanel, rows, "csvexport");
	}

	public CustomDataPanelExportLink(String id, CustomDataPanel<T> datapanel,
			ExportableDataView<T> rows, String bestandsnaam)
	{
		this(id, datapanel, rows, bestandsnaam, (String) null);
	}

	public CustomDataPanelExportLink(String id, CustomDataPanel<T> datapanel,
			ExportableDataView<T> rows, String bestandsnaam, String header)
	{
		this(id, datapanel, rows, bestandsnaam, new Model<String>(header));
	}

	public CustomDataPanelExportLink(String id, CustomDataPanel<T> datapanel,
			ExportableDataView<T> rows, String bestandsnaam, IModel<String> headerModel)
	{
		super(id);
		this.datapanel = datapanel;
		this.rows = rows;
		this.bestandsnaam = bestandsnaam;
		this.header = headerModel;
	}

	@Override
	public void onClick()
	{
		Map<String, Object> parameters = datapanel.getAfdrukParameters();
		String context = (String) parameters.get("context");
		if (StringUtil.isNotEmpty(context))
		{
			bestandsnaam = StringUtil.createValidFileName(context);
		}
		int aantal = rows.getRowCount();
		if (aantal > MAX_ROWS_IN_REQUEST)
		{
			if (datapanel.supportsExportJobs())
			{
				datapanel.triggerExportJob(this);
				info("De export is op de achtergrond opgestart. U kunt het bestand terugvinden onder Home | Rapportage.");
			}
			else
			{
				error("De zoeklijst bevat meer dan " + MAX_ROWS_IN_REQUEST
					+ " elementen. Verklein de selectie om deze te kunnen exporteren");
			}
		}
		else
		{
			CsvFileResourceStream stream =
				rows.generateCsvFileFromView(bestandsnaam, header.getObject());
			((WebResponse) getRequestCycle().getResponse()).setHeader("Content-Disposition",
				"attachment; filename=\"" + stream.getFileName() + "\"");
			getRequestCycle().setRequestTarget(new ResourceStreamRequestTarget(stream));
			getRequestCycle().setRedirect(false);
		}
	}

	public ExportableDataView<T> getRows()
	{
		return rows;
	}

	public String getBestandsnaam()
	{
		return bestandsnaam;
	}

	public IModel<String> getHeader()
	{
		return header;
	}

}
