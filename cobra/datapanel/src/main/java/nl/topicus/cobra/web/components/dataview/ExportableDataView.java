/*
 * $Id: ExportableDataView.java,v 1.10 2008-06-16 16:14:34 loite Exp $
 * $Revision: 1.10 $
 * $Date: 2008-06-16 16:14:34 $
 *
 * ====================================================================
 * Copyright (c) 2005, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.cobra.web.components.dataview;

import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import nl.topicus.cobra.dataproviders.CachingDataProvider;
import nl.topicus.cobra.fileresources.AbstractFileResourceStream;
import nl.topicus.cobra.fileresources.CsvFileResourceStream;
import nl.topicus.cobra.io.Utf8BufferedWriter;
import nl.topicus.cobra.util.ComponentUtil;
import nl.topicus.cobra.util.StringUtil;
import nl.topicus.cobra.util.TimeUtil;
import nl.topicus.cobra.web.components.datapanel.CustomColumn;
import nl.topicus.cobra.web.components.datapanel.columns.HideableColumn;
import nl.topicus.cobra.web.components.labels.NewlinePreservingLabel;
import nl.topicus.cobra.web.components.link.IProgressCallback;

import org.apache.wicket.Component;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.RequestCycle;
import org.apache.wicket.Session;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.HiddenField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.ExternalLink;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.repeater.AbstractRepeater;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.RefreshingView;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.IModel;
import org.apache.wicket.util.convert.IConverter;
import org.apache.wicket.util.lang.PropertyResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Een listview van exportable data view componenten
 * 
 * @author loite
 */
public abstract class ExportableDataView<T> extends DataView<T> implements IExportableView
{
	private static final long serialVersionUID = 1L;

	// lijst met kolom id die worden genegeerd door de writeExport
	private List<String> excludeIds = new ArrayList<String>();

	private boolean isExportingToCsv = false;

	/**
	 * @author loite
	 */
	public abstract static class ExportableListView<T> extends ListView<T>
	{
		private static final long serialVersionUID = 1L;

		public ExportableListView(String id, List<T> list)
		{
			super(id, list);
		}

		/**
		 * methode public gemaakt.
		 * 
		 * @param item
		 */
		@Override
		public abstract void populateItem(ListItem<T> item);
	}

	public static final Logger LOG = LoggerFactory.getLogger(ExportableDataView.class);

	private static final int BATCH_SIZE = 200;

	public ExportableDataView(String id, IDataProvider<T> dataProvider)
	{
		super(id, dataProvider);
	}

	public CsvFileResourceStream generateCsvFileFromView(String bestandsnaam)
	{
		return generateCsvFileFromView(bestandsnaam, null);
	}

	public CsvFileResourceStream generateCsvFileFromView(String bestandsnaam, String header)
	{
		return generateCsvFileFromView(bestandsnaam, header, true, null);
	}

	public CsvFileResourceStream generateCsvFileFromView(String bestandsnaam, String header,
			boolean exportToCsv, IProgressCallback progressCallback)
	{
		isExportingToCsv = exportToCsv;
		File file = null;
		Utf8BufferedWriter writer = null;
		try
		{
			file = File.createTempFile(bestandsnaam + "_", ".csv");
			writer = new Utf8BufferedWriter(file);
			writer.writeByteOrderMark();

			if (StringUtil.isNotEmpty(header))
			{
				writer.write(header);
				writer.write("\r\n");
			}
			writeExport(writer, null, null, progressCallback);
			CsvFileResourceStream resourceStream =
				new CsvFileResourceStream(file, TimeUtil.getInstance().currentDateTime(), "");
			resourceStream.setExportCharsetFormat(AbstractFileResourceStream.UTF_8);
			return resourceStream;
		}
		catch (IOException e)
		{
			LOG.error(e.getMessage(), e);
			return null;
		}
		catch (InterruptedException e)
		{
			LOG.error(e.getMessage(), e);
			return null;
		}
		finally
		{
			isExportingToCsv = false;
			try
			{
				if (writer != null)
					writer.close();
			}
			catch (IOException e)
			{
				LOG.error(e.getMessage(), e);
			}
		}
	}

	/**
	 * @see #writeExport(Writer, String, String, IProgressCallback)
	 * @param writer
	 * @throws IOException
	 */
	public void writeExport(Writer writer) throws IOException, InterruptedException
	{
		writeExport(writer, null, null, null);
	}

	/**
	 * Schrijft de data (geen headers) via de writer. Elke kolom wordt gescheiden door een
	 * ; elke rij door een \r\n. Als de dataview grouping ondersteunt, kan de groepering
	 * op twee verschillende manieren meegenomen worden in de export; door een nieuwe
	 * regel met daarop alleen de nieuwe groepwaarde (writeGroupingComponents = true), of
	 * door op alle regels een extra kolom mee te nemen waarin de waarde van een property
	 * van het model object (groupingProperty != null).
	 * 
	 * @param writer
	 * @param groupingProperty
	 * @param groupPrefix
	 *            Prefix voor alle nieuwe groepwaarden.
	 * @param progressCallback
	 *            callback waarop de progress gezet kan worden. Mag null zijn.
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public void writeExport(Writer writer, String groupingProperty, String groupPrefix,
			IProgressCallback progressCallback) throws InterruptedException, IOException
	{
		int index = 0;
		int size = getDataProvider().size();

		for (int counter = 0; counter < size; counter = counter + BATCH_SIZE)
		{
			IDataProvider<T> dataProvider = getDataProvider();
			Iterator< ? extends T> iterator = dataProvider.iterator(counter, BATCH_SIZE);
			while (iterator.hasNext())
			{
				T object = iterator.next();
				IModel<T> model = getDataProvider().model(object);
				Item<T> item = new Item<T>("id", index, model);
				if (findPage() != null && RequestCycle.get() != null)
					getPage().add(item);
				populateItem(item);
				// Haal alle labels van dit item op.
				Iterator< ? extends Component> components = item.iterator();
				while (components.hasNext())
				{
					Component component = components.next();
					if (component.isVisible())
					{
						// Controleer of dit een groeperingsrij is.
						if (isGroupingComponent(component))
						{
							if (isWriteGroupingComponents())
							{
								exportComponent(component, writer);
								writer.write("\r\n");
							}
						}
						else
						{
							exportComponent(component, writer);
						}
					}
				}
				if (groupingProperty != null)
				{
					Object objectValue = PropertyResolver.getValue(groupingProperty, object);
					String value = objectValue == null ? null : objectValue.toString();
					if (value == null)
					{
						value = groupPrefix + "<Geen waarde>";
					}
					else
					{
						value =
							groupPrefix + value.replaceAll("\r\n|\n|\r|\u0085|\u2028|\u2029", " ");
					}
					writer.write(value);
					writer.write(";");
				}
				writer.write("\r\n");
				if (findPage() != null && RequestCycle.get() != null)
					getPage().remove(item);
				index++;
				if (progressCallback != null)
				{
					progressCallback.setProgress((index * 100 / size));
				}
			}
			if (dataProvider instanceof CachingDataProvider< ? >)
				((CachingDataProvider<T>) dataProvider).clearCache();
		}
	}

	@SuppressWarnings("unchecked")
	private <Y extends CustomColumn< ? >> Y findColumn(Component component, Class<Y> columnClass)
	{
		Component testComponent = component;
		while (testComponent != null)
		{
			try
			{
				Object curModelObject = testComponent.getDefaultModelObject();
				if (columnClass.isInstance(curModelObject))
				{
					return (Y) curModelObject;
				}
			}
			catch (Exception e)
			{
				// ignore all exceptions
			}
			testComponent = testComponent.getParent();
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	private final void exportComponent(Component component, Writer writer) throws IOException
	{
		if (excludeIds.contains(component.getId()))
		{
			return;
		}
		if (RequestCycle.get() != null && !component.isActionAuthorized(RENDER))
		{
			writer.write(";");
			return;
		}
		if (!component.isVisible())
		{
			writer.write(";");
			return;
		}
		HideableColumn< ? > parentCol = findColumn(component, HideableColumn.class);
		if (parentCol != null
			&& (!parentCol.isColumnVisible() || !parentCol.isColumnVisibleInExport()))
		{
			return;
		}
		// Controleer of dit een tekstcomponent is en we hacken er maar wat op los, want
		// wie weet willen we de NewlinePreservingLabel ook wel niet tonen of toch wel?
		if (component instanceof HiddenField || component instanceof NewlinePreservingLabel)
		{
			// do nothing
		}
		else if (component instanceof Label || component instanceof TextField
			|| component instanceof ExternalLink)
		{
			// A newline (line feed) character ('\n'),
			// A carriage-return character followed immediately by a newline character
			// ("\r\n"),
			// A standalone carriage-return character ('\r'),
			// A next-line character ('\u0085'),
			// A line-separator character ('\u2028'), or
			// A paragraph-separator character ('\u2029), or
			// A email link ("MAILTO:").
			String value = getModelObjectAsString(component);
			if (value != null)
			{
				value = value.replaceAll("MAILTO:", "");
				value = value.replaceAll("\r\n|\n|\r|\u0085|\u2028|\u2029", " ");
				value = value.replaceAll("&nbsp;|&nbsp", " ");
				value = value.replaceAll("<br/>", "\n");
			}
			writer.write(value);
			writer.write(";");
		}
		else if (component instanceof ListView)
		{
			Iterator<ListItem> it = (Iterator<ListItem>) ((MarkupContainer) component).iterator();
			if (!it.hasNext())
			{
				ComponentUtil.invokePopulate((AbstractRepeater) component);
			}
			if (!it.hasNext())
			{
				writer.write(";");
				return;
			}
			ListItem temp;
			while (it.hasNext())
			{
				temp = it.next();
				if (temp.isVisible())
				{
					Iterator<Component> it2 = (Iterator<Component>) temp.iterator();
					while (it2.hasNext())
						exportComponent(it2.next(), writer);
				}
			}
		}
		else if (component instanceof DataView)
		{
			Iterator<Item> it = ((RefreshingView) component).getItems();
			if (!it.hasNext())
			{
				ComponentUtil.invokePopulate((AbstractRepeater) component);
			}
			if (!it.hasNext())
			{
				writer.write(";");
				return;
			}
			Item temp;
			while (it.hasNext())
			{
				temp = it.next();
				if (temp.isVisible())
				{
					Iterator<Component> it2 = (Iterator<Component>) temp.iterator();
					while (it2.hasNext())
						exportComponent(it2.next(), writer);
				}
			}
		}
		else if (component instanceof MarkupContainer)
		{
			Iterator<Component> it = (Iterator<Component>) ((MarkupContainer) component).iterator();
			if (it.hasNext())
			{
				while (it.hasNext())
				{
					exportComponent(it.next(), writer);
				}
			}
			else
			{
				writer.write(";");
			}
		}
		else
		{
			writer.write(";");
			LOG.warn("skipping unknown component type " + component.getClass().getName() + " at "
				+ component.getClassRelativePath());
		}
	}

	private static final String getModelObjectAsString(Component component)
	{
		final Object modelObject = component.getDefaultModelObject();
		if (modelObject != null)
		{
			// Get converter
			final IConverter converter = component.getConverter(modelObject.getClass());

			// Model string from property
			final String modelString =
				converter.convertToString(modelObject, Session.get().getLocale());

			if (modelString != null)
			{
				return modelString.replace(";", "");
			}
		}
		return "";

	}

	/**
	 * Methode public gemaakt.
	 */
	@Override
	public int getViewOffset()
	{
		return super.getViewOffset();
	}

	/**
	 * Methode public gemaakt
	 */
	@Override
	public int getViewSize()
	{
		return super.getViewSize();
	}

	/**
	 * @param component
	 *            Het component dat gecontroleerd moet worden.
	 * @return true als het gegeven component een grouping component is, en daarom niet
	 *         als een standaard component in de export meegenoemn moet worden. De
	 *         standaard implementatie geeft altijd false. Subclasses moeten zelf logica
	 *         implementeren die de juiste waarde teruggeeft als deze grouping
	 *         ondersteunt.
	 */
	protected boolean isGroupingComponent(Component component)
	{
		return false;
	}

	/**
	 * @return true als grouping components moeten worden geprint, default geeft dit
	 *         false.
	 */
	protected boolean isWriteGroupingComponents()
	{
		return false;
	}

	/**
	 * Voeg ids doe die niet ge-exporteerd mogen worden
	 * 
	 * @param ids
	 */
	public void addExcludeIds(String... ids)
	{
		excludeIds.addAll(Arrays.asList(ids));
	}

	/**
	 * @return excludeIdList
	 */
	public List<String> getExcludeIds()
	{
		return excludeIds;
	}

	public boolean allowSkipColumns()
	{
		return !isExportingToCsv;
	}
}
