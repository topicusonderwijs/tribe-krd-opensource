/*
 * $Id: ExportableViewExportLink.java,v 1.4 2008-03-14 09:41:29 harmsen Exp $
 * $Revision: 1.4 $
 * $Date: 2008-03-14 09:41:29 $
 *
 * ====================================================================
 * Copyright (c) 2005, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.cobra.web.components.link;

import nl.topicus.cobra.fileresources.CsvFileResourceStream;
import nl.topicus.cobra.web.components.dataview.IExportableView;

import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.protocol.http.WebResponse;
import org.apache.wicket.request.target.resource.ResourceStreamRequestTarget;

/**
 * @author loite
 */
public class ExportableViewExportLink<T> extends Link<T>
{
	private static final long serialVersionUID = 1L;

	private final IExportableView exportableView;

	private final String bestandsnaam;

	private final IModel<String> header;

	public ExportableViewExportLink(String id, IExportableView exportableView)
	{
		this(id, exportableView, "csvexport");
	}

	public ExportableViewExportLink(String id, IExportableView exportableView, String bestandsnaam)
	{
		this(id, exportableView, bestandsnaam, (String) null);
	}

	public ExportableViewExportLink(String id, IExportableView exportableView, String bestandsnaam,
			String header)
	{
		this(id, exportableView, bestandsnaam, new Model<String>(header));
	}

	public ExportableViewExportLink(String id, IExportableView exportableView, String bestandsnaam,
			IModel<String> header)
	{
		super(id);
		this.exportableView = exportableView;
		this.bestandsnaam = bestandsnaam;
		this.header = header;
	}

	@Override
	public void onClick()
	{
		CsvFileResourceStream stream =
			exportableView.generateCsvFileFromView(bestandsnaam, header.getObject());
		((WebResponse) getRequestCycle().getResponse()).setHeader("Content-Disposition",
			"attachment; filename=\"" + stream.getFileName() + "\"");
		getRequestCycle().setRequestTarget(new ResourceStreamRequestTarget(stream));
		getRequestCycle().setRedirect(false);
	}

}
