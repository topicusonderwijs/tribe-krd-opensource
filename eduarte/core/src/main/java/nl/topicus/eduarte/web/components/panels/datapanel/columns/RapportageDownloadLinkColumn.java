/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.web.components.panels.datapanel.columns;

import nl.topicus.cobra.web.components.datapanel.columns.AbstractCustomColumn;
import nl.topicus.eduarte.entities.jobs.logging.RapportageJobRun;
import nl.topicus.eduarte.jobs.rapportage.RapportageDownloadLink;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

/**
 * Column om een gegenereerde rapportage te downloaden.
 * 
 * @author hoeve
 */
public class RapportageDownloadLinkColumn extends AbstractCustomColumn<RapportageJobRun>
{
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor
	 * 
	 * @param id
	 *            Id van de column (niet het wicket id)
	 * @param header
	 *            De tekst van de kolomheader.
	 */
	public RapportageDownloadLinkColumn(String id, String header)
	{
		super(id, header);
	}

	@Override
	public void populateItem(WebMarkupContainer cellItem, String componentId,
			WebMarkupContainer row, IModel<RapportageJobRun> rowModel, int span)
	{
		RapportageJobRun jobrun = rowModel.getObject();
		if (jobrun != null && jobrun.heeftResultaat())
		{
			LinkPanel panel = new LinkPanel(componentId);
			cellItem.add(panel);

			panel.add(new RapportageDownloadLink("link", rowModel));
		}
		else
			cellItem.add(new WebMarkupContainer(componentId));
	}

	private static final class LinkPanel extends Panel
	{
		private static final long serialVersionUID = 1L;

		/**
		 * @param id
		 */
		public LinkPanel(String id)
		{
			super(id);
		}
	}

}
