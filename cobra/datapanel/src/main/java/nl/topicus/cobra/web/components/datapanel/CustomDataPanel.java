/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.cobra.web.components.datapanel;

import java.awt.Image;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import nl.topicus.cobra.dataproviders.IndexedDataProvider;
import nl.topicus.cobra.util.ComponentUtil;
import nl.topicus.cobra.util.StringUtil;
import nl.topicus.cobra.web.behaviors.AppendingAttributeModifier;
import nl.topicus.cobra.web.components.datapanel.GroupProperty.GroupPropertyList;
import nl.topicus.cobra.web.components.datapanel.columns.HideableColumn;
import nl.topicus.cobra.web.components.datapanel.columns.ISpanningColumn;
import nl.topicus.cobra.web.components.datapanel.settings.SelecteerGroeperingModalWindow;
import nl.topicus.cobra.web.components.datapanel.settings.SelecteerKolommenModalWindow;
import nl.topicus.cobra.web.components.dataview.ExportableDataView;
import nl.topicus.cobra.web.components.link.CustomDataPanelAfdrukkenLink;
import nl.topicus.cobra.web.components.link.CustomDataPanelExportLink;
import nl.topicus.cobra.web.components.modal.CobraModalWindow;
import nl.topicus.cobra.web.components.navigation.paging.CustomPagingNavigator;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.behavior.IBehavior;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow.WindowClosedCallback;
import org.apache.wicket.extensions.markup.html.repeater.data.sort.ISortState;
import org.apache.wicket.extensions.markup.html.repeater.data.sort.ISortStateLocator;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IStyledColumn;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.navigation.paging.IPageable;
import org.apache.wicket.markup.html.navigation.paging.PagingNavigator;
import org.apache.wicket.markup.html.panel.EmptyPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.AbstractPageableView;
import org.apache.wicket.markup.repeater.IItemReuseStrategy;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.ReuseIfModelsEqualStrategy;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.lang.PropertyResolver;

/**
 * Panel om ExtendableDataView te vervangen. Net als daar geeft de programmeur op welke
 * kolommen beschikbaar zijn en kan de gebruiker hier een "view" over leggen om zelf te
 * bepalen welke kolommen uiteindelijk getoond worden en waar. Om te kunnen sorteren dient
 * de {@link IDataProvider} tevens een {@link ISortStateLocator} te zijn.
 * 
 * @author marrink
 */
public class CustomDataPanel<T> extends Panel implements IPageable, CustomDataPanelIdSource
{
	private static final int BATCH_SIZE = 200;

	private static final int MAX_COUNT_IN_GROUP = 1000;

	private static final String GROUPING_ROW_ID = "groepHeaderRow";

	private static final long serialVersionUID = 1L;

	private IDataProvider< ? extends T> provider;

	private CustomDataPanelContentDescription<T> contentDescription;

	private CustomDataPanelId panelId;

	private CustomDataPanelRowFactory<T> rowFactory = new CustomDataPanelRowFactory<T>();

	private IModel<GroupProperty<T>> selectedGroupProperty;

	private List<CustomDataPanelToolbar<T>> toolbars = new ArrayList<CustomDataPanelToolbar<T>>();

	private List<CustomDataPanelToolbar<T>> footerbars = new ArrayList<CustomDataPanelToolbar<T>>();

	@SpringBean
	private CustomDataPanelService<T> service;

	private ExportableDataView<T> data;

	private WebMarkupContainer table;

	private boolean reuseItems = false;

	private Label titleLabel;

	private boolean isWriteGroupingComponents = false;

	/**
	 * Wordt gebruikt tijdens rendering. Houdt de huidige groepwaarde vast om zo te kunnen
	 * bepalen of een nieuwe groep moet beginnen.
	 */
	private transient Object currentGroupValue;

	/**
	 * Wordt gebruikt tijdens rendering. Houdt de waarde van de regels vast zodat bepaalde
	 * properties niet op elke regel herhaald hoeven te worden als de waarde gelijk is aan
	 * die van het vorige object.
	 */
	private transient Map<Integer, T> rowValues;

	@Deprecated
	public CustomDataPanel(String id, String title, final IDataProvider<T> provider,
			GroupPropertyList<T> groupProperties, CustomColumn<T>... columns)
	{
		this(id, title, provider, null, groupProperties, columns);
	}

	@Deprecated
	public CustomDataPanel(String id, String title, final IDataProvider<T> provider,
			CustomColumn<T>... columns)
	{
		this(id, title, provider, null, columns);
	}

	@Deprecated
	public CustomDataPanel(String id, String title, final IDataProvider<T> provider,
			GroupProperty<T> defaultGroupProperty, GroupPropertyList<T> groupProperties,
			CustomColumn<T>... columns)
	{
		this(id, provider, new CustomDataPanelContentDescription<T>(title, groupProperties,
			defaultGroupProperty, Arrays.asList(columns)));
	}

	public CustomDataPanel(String id, CustomDataPanelId panelId, final IDataProvider<T> provider,
			CustomDataPanelContentDescription<T> contentDescription)
	{
		this(id, provider, contentDescription);
		this.panelId = panelId;
	}

	public CustomDataPanel(String id, final IDataProvider<T> provider,
			CustomDataPanelContentDescription<T> contentDescription)
	{
		super(id);
		this.provider = provider;
		this.contentDescription = contentDescription;
		setDefaultModel(new ColumnModel<T>(this, service));
		setOutputMarkupId(true);
		titleLabel = new Label("title", createTitleModel(contentDescription.getTitle()));
		titleLabel.setOutputMarkupId(true);
		add(titleLabel);
		add(table = new WebMarkupContainer("table")
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onBeforeRender()
			{
				currentGroupValue = null;
				super.onBeforeRender();
			}
		});
		table.setOutputMarkupId(true);
		addCustomAttributeModifier();
		final ConcatModel exportHeaderModel = new ConcatModel(25);
		table.add(createHeaderToolbars());
		table.add(createFooterToolbars());
		addHeaderToolbar(new LabelsHeaderToolbar<T>(this, exportHeaderModel));

		data = createRowDataView("data", provider);
		data.setItemsPerPage(20);
		data.setVersioned(false);
		table.add(data);
		table.add(createGeenGegevensGevondenMarker());

		selectedGroupProperty = new GroupPropertySettingModel<T>(this, service);

		createButtons(exportHeaderModel);

		table.add(createNavigator());
	}

	public final void addBehaviorToTable(IBehavior behavior)
	{
		getTable().add(behavior);
	}

	public void ensureVisible(T object)
	{
		IDataProvider< ? extends T> prov = getDataProvider();
		if (prov instanceof IndexedDataProvider< ? >)
		{
			int index = ((IndexedDataProvider< ? extends T>) prov).getIndex(object);
			int page = index / getItemsPerPage();
			setCurrentPage(page);
		}
	}

	/**
	 * Geeft het aantal zichtbare kolommen in dit datapanel.
	 * 
	 * @return Het aantal zichtbare kolommen
	 */
	public int getAantalZichtbareKolommen()
	{
		return getModel().getObject().size();
	}

	/**
	 * 
	 * @return De contextprovider voor dit datapanel. Dit kan de pagina zijn waarop het
	 *         panel staat, of een datamap bij een achtergrondtaak.
	 */
	protected IContextProvider getContextProvider()
	{
		return null;
	}

	/**
	 * Methode die aangeroepen wordt om zaken zoals de titel van het rapport op te vragen.
	 * 
	 * @return de titel
	 */
	public Map<String, Object> getAfdrukParameters()
	{
		IContextProvider contextprovider = getContextProvider();
		if (contextprovider == null)
		{
			if (getPage() != null && getPage() instanceof IContextProvider)
			{
				contextprovider = (IContextProvider) getPage();
			}
		}
		String context = "";
		String user = "";
		String organisatieNaam = "";
		List<CriteriaBean> zoekCriteria = new ArrayList<CriteriaBean>();
		Image logo = null;
		if (contextprovider != null)
		{
			context = contextprovider.getContextOmschrijving();
			user = contextprovider.getIngelogdeGebruikersNaam();
			organisatieNaam = contextprovider.getOrganisatieNaam();
			zoekCriteria = contextprovider.getZoekCriteria();
			logo = contextprovider.getInstellingsLogo();
		}

		Map<String, Object> result = new HashMap<String, Object>();
		result.put("titel", contentDescription.getTitle());
		if (context != null)
			result.put("context", context);
		if (user != null)
			result.put("user", user);
		if (organisatieNaam != null)
			result.put("organisatie", organisatieNaam);
		if (zoekCriteria != null && !zoekCriteria.isEmpty())
		{
			result.put("criteria", zoekCriteria);
			result.put("zoekcriteria", "Zoekcriteria:");
		}
		if (logo != null)
			result.put("logo", logo);
		return result;
	}

	public CustomDataPanelContentDescription<T> getContentDescription()
	{
		return contentDescription;
	}

	/**
	 * @see org.apache.wicket.markup.html.navigation.paging.IPageable#getCurrentPage()
	 */
	@Override
	public final int getCurrentPage()
	{
		return data.getCurrentPage();
	}

	/**
	 * De data.
	 * 
	 * @return data
	 */
	public IDataProvider< ? extends T> getDataProvider()
	{
		return provider;
	}

	/**
	 * @return Prefix voor alle groepwaarden (bijvoorbeeld 'Opleiding: ').
	 */
	public String getGroupingPrefix()
	{
		if (getGroupProperties() == null)
			return null;
		GroupProperty<T> setting = selectedGroupProperty.getObject();
		String prop = setting == null ? null : setting.getProperty();
		String omschrijving = getGroupProperties().get(prop).getOmschrijving();
		return omschrijving != null ? omschrijving + ": " : "";
	}

	/**
	 * @return aantal rijen op 1 pagina
	 * @see DataView#getItemsPerPage()
	 */
	public final int getItemsPerPage()
	{
		return data.getItemsPerPage();
	}

	/**
	 * @see org.apache.wicket.markup.html.navigation.paging.IPageable#getPageCount()
	 */
	@Override
	public final int getPageCount()
	{
		return data.getPageCount();
	}

	public CustomDataPanelId getPanelId()
	{
		if (panelId == null)
			panelId = new DefaultCustomDataPanelId(getPage().getClass(), getId());
		return panelId;
	}

	/**
	 * @see AbstractPageableView#getRowCount()
	 * @return totale aantal rijen op alle pagina's
	 */
	public final int getRowCount()
	{
		return data.getRowCount();
	}

	/**
	 * @return Het property waarop gegroepeerd zou moeten worden.
	 */
	public GroupProperty<T> getSelectedGroupingProperty()
	{
		return selectedGroupProperty.getObject();
	}

	/**
	 * SortStateLocator, indien aanwezig.
	 * 
	 * @return sortering
	 */
	public ISortStateLocator getSortStateLocator()
	{
		IDataProvider< ? > dataprovider = getDataProvider();
		if (dataprovider instanceof ISortStateLocator)
			return (ISortStateLocator) dataprovider;
		return null;
	}

	/**
	 * @return index van de eerste rij van deze pagina
	 */
	public final int getViewOffset()
	{
		return data.getViewOffset();
	}

	/**
	 * @return aantal rijen op deze pagina
	 */
	public final int getViewSize()
	{
		return data.getViewSize();
	}

	/**
	 * Indicator of er een {@link ISortStateLocator} gevonden is als {@link IDataProvider}
	 * . Dit bepaald nl of er gesorteerd kan worden.
	 * 
	 * @return true als er gesorteerd kan worden, anders false.
	 */
	public boolean isSortable()
	{
		return getDataProvider() instanceof ISortStateLocator;
	}

	/**
	 * Laat de buttons voor exporteren, printen en settings al dan niet zien.
	 * 
	 * @param visible
	 */
	public void setButtonsVisible(boolean visible)
	{
		setExportButtonVisible(visible);
		setPrintButtonVisible(visible);
		setSelecteerKolommenButtonVisible(visible);
		setGroeperenButtonVisible(visible);
	}

	public void setGroeperenButtonVisible(boolean visible)
	{
		get("groeperen").setVisible(visible);
		get("selecteerGroeperingModalWindow").setVisible(visible);
	}

	public void setSelecteerKolommenButtonVisible(boolean visible)
	{
		get("settings").setVisible(visible);
		get("selecteerKolommenModalWindow").setVisible(visible);
	}

	public void setPrintButtonVisible(boolean visible)
	{
		get("print").setVisible(visible);
	}

	public void setExportButtonVisible(boolean visible)
	{
		get("export").setVisible(visible);
	}

	public void setNavigatorVisible(boolean visible)
	{
		get("table:navigatorContainer").setVisible(visible);
	}

	public void setTitleVisible(boolean visible)
	{
		titleLabel.setVisible(visible);
	}

	public void setTitleModel(IModel<String> titleModel)
	{
		titleLabel.setDefaultModel(titleModel);
	}

	public void updateTitle(AjaxRequestTarget target)
	{
		target.addComponent(titleLabel);
	}

	/**
	 * @see org.apache.wicket.markup.html.navigation.paging.IPageable#setCurrentPage(int)
	 */
	@Override
	public final void setCurrentPage(int page)
	{
		data.setCurrentPage(page);
		table.addOrReplace(createNavigator());
		onNavigate();
	}

	/**
	 * Set aantal rijen dat op 1 pagina getoond wordt.
	 * 
	 * @param amount
	 * @see DataView#setItemsPerPage(int)
	 */
	public final void setItemsPerPage(int amount)
	{
		data.setItemsPerPage(amount);
	}

	@SuppressWarnings("unchecked")
	public void setRowFactory(CustomDataPanelRowFactory<T> rowFactory)
	{
		if (this.rowFactory instanceof IRowFactoryDecorator< ? >)
			((IRowFactoryDecorator<T>) this.rowFactory).setInnerRowFactory(rowFactory);
		else
			this.rowFactory = rowFactory;
	}

	/**
	 * Callback om de table {@link #getTable()} van attribute modifiers te voorzien.
	 */
	protected void addCustomAttributeModifier()
	{

	}

	/**
	 * @return Container component om de hele tabel bij te werken, bijvoorbeeld bij Ajax.
	 */
	public final WebMarkupContainer getTable()
	{
		return table;
	}

	/**
	 * @return override deze methode om een title mee te geven aan het de csv export
	 */
	protected String getCSVTitle()
	{
		return null;
	}

	/**
	 * @return override deze methode om de standaard tekst 'Er zijn geen gegevens
	 *         gevonden' te veranderen.
	 */
	protected String getGeengegevensTekst()
	{
		return "Er zijn geen gegevens gevonden";
	}

	public boolean getReuseItems()
	{
		return reuseItems;
	}

	/**
	 * Hiermee kun je voorkomen dat bij elke render alle cellen opnieuw gemaakt worden.
	 * Dit vereist echter wel dat de models de equals doorlussen op het object. Dit is te
	 * checken met een breakpoint in {@link ReuseIfModelsEqualStrategy}.
	 */
	public void setReuseItems(boolean reuseItems)
	{
		this.reuseItems = reuseItems;
	}

	public void addHeaderToolbar(final CustomDataPanelToolbar<T> toolbar)
	{
		toolbars.add(toolbar);
	}

	public List<CustomDataPanelToolbar<T>> getHeaderToolbars()
	{
		return toolbars;
	}

	public void addFooterToolbar(final CustomDataPanelToolbar<T> toolbar)
	{
		footerbars.add(toolbar);
	}

	public List<CustomDataPanelToolbar<T>> getFooterToolbars()
	{
		return footerbars;
	}

	protected GroupPropertyList<T> getGroupProperties()
	{
		return contentDescription.getGroupProperties();
	}

	/**
	 * @param id
	 * @param item
	 * @return De markup container waarbinnen de row wordt gedefinieerd.
	 */
	protected WebMarkupContainer getRow(String id, final Item<T> item)
	{
		return rowFactory.createRow(id, this, item);
	}

	/**
	 * @param dataprovider
	 * @return De dataview die de daadwerkelijke rows van het datapanel bevat.
	 */
	protected ExportableDataView<T> createRowDataView(String id, final IDataProvider<T> dataprovider)
	{
		return new ExportableDataView<T>(id, dataprovider)
		{
			private static final long serialVersionUID = 1L;

			/**
			 * @see nl.topicus.cobra.web.components.dataview.ExportableDataView#isGroupingComponent(org.apache.wicket.Component)
			 */
			@Override
			protected boolean isGroupingComponent(Component component)
			{
				return GROUPING_ROW_ID.equals(component.getId());
			}

			@Override
			protected boolean isWriteGroupingComponents()
			{
				return CustomDataPanel.this.isWriteGroupingComponents();
			}

			@Override
			protected void populateItem(Item<T> item)
			{
				populateRow("cols", "column", "content", "colsHeader", "columnHeader",
					"contentHeader", item);
			}

			@Override
			public IItemReuseStrategy getItemReuseStrategy()
			{
				if (reuseItems)
					return ReuseIfModelsEqualStrategy.getInstance();
				else
					return super.getItemReuseStrategy();
			}
		};
	}

	/**
	 * @return true als grouping components moeten worden geprint, default geeft dit
	 *         false.
	 */
	public boolean isWriteGroupingComponents()
	{
		return isWriteGroupingComponents;
	}

	public void setWriteGroupingComponents(boolean isWriteGroupingComponents)
	{
		this.isWriteGroupingComponents = isWriteGroupingComponents;
	}

	/**
	 * @return De waarde van een regel zoals de gebruiker dit ervaart. In een methode
	 *         gestopt zodat subclasses het gedrag hiervan kan overriden. Het object dat
	 *         voor de gebruiker op een regel staat kan verschillen van het daadwerkelijke
	 *         object op een regel. Zo voelt het voor een gebruiker alsof een deelnemer op
	 *         een regel staat, terwijl het eigenlijk om een inschrijving gaat. Deze
	 *         datapanels kunnen in die gevallen de deelnemer teruggeven als de waarde,
	 *         waardoor bijvoorbeeld de naam van de deelnemer niet op elke regel herhaald
	 *         wordt.
	 */
	protected Object getRowValue(T rowValue)
	{
		return rowValue;
	}

	/**
	 * De component die de html table voorsteld, hiermee kunnen attributemodifiers gezet
	 * worden ed.
	 * 
	 * @return table
	 * @deprecated gebruik {@link #getTable()}.
	 */
	@Deprecated
	protected final Component getTableComponent()
	{
		return table;
	}

	/**
	 * Methode die aangeroepen wordt na het populaten van elke row in de dataview.
	 * 
	 * @param item
	 *            Het item dat gepopulated wordt.
	 */
	protected void onAfterPopulateItem(Item<T> item)
	{
	}

	/**
	 * @see org.apache.wicket.Component#onBeforeRender()
	 */
	@Override
	protected void onBeforeRender()
	{
		if (!hasBeenRendered())
		{
			if (isSortable())
			{
				GroupProperty<T> setting = selectedGroupProperty.getObject();
				if (setting != null && StringUtil.isNotEmpty(setting.getProperty()))
				{
					getSortStateLocator().getSortState().setPropertySortOrder(
						setting.getSorteerProperty(),
						setting.isAscending() ? ISortState.ASCENDING : ISortState.DESCENDING);
				}
			}
			rowFactory.bind(this);
		}
		rowFactory.onBeforeRender();
		currentGroupValue = null;
		super.onBeforeRender();
	}

	@Override
	protected void onDetach()
	{
		super.onDetach();
		IDataProvider< ? > prov = getDataProvider();
		if (prov != null)
			prov.detach(); // detach handmatig als er geen rows opgeleverd worden
		contentDescription.detach();
		selectedGroupProperty.detach();
		rowFactory.detach();
		rowValues = null;
		ComponentUtil.detachQuietly(toolbars);
		ComponentUtil.detachQuietly(footerbars);
	}

	/**
	 * Methode die aangeroepen wordt voor het populaten van een column in een row.
	 * 
	 * @param item
	 *            De kolom die gepopulated wordt.
	 * @param rowModel
	 * @param rowIndex
	 */
	protected void onPopulateColumn(Item<CustomColumn<T>> item, IModel<T> rowModel, int rowIndex)
	{
	}

	/**
	 * Methode die aangeroepen wordt voor het populaten van elke row in de dataview.
	 * Override deze methode indien je iets met bijvoorbeeld het dataobject (het model van
	 * de row) moet doen voordat deze gebruikt wordt om de rij te populaten.
	 * 
	 * @param item
	 *            Het item dat gepopulated wordt.
	 */
	protected void onPopulateItem(Item<T> item)
	{
	}

	private void addGroupHeader(String headerRowId, String headerColumnsId, String headerContentId,
			Item<T> item, final IModel<T> rowModel)
	{
		// Toon een eventuele groep header.
		String property = null;
		GroupProperty<T> setting = selectedGroupProperty.getObject();
		if (setting != null)
			property = setting.getProperty();
		WebMarkupContainer groepHeaderRow;
		if (StringUtil.isNotEmpty(property))
		{
			Serializable value =
				(Serializable) PropertyResolver.getValue(property, rowModel.getObject());
			if ((value == null && currentGroupValue == null)
				|| (value != null && currentGroupValue != null && value.equals(currentGroupValue)))
			{
				// Groeperingswaarde van deze row is gelijk aan groeperingswaarde van
				// vorige row.
				groepHeaderRow = new WebMarkupContainer(GROUPING_ROW_ID);
				groepHeaderRow.setVisible(false);
			}
			else
			{
				groepHeaderRow = rowFactory.createHeaderRow(GROUPING_ROW_ID, this, item);
				currentGroupValue = value;

				DataView<CustomColumn<T>> headerColumns =
					getColumnDataView(headerRowId, headerColumnsId, headerContentId,
						new ColumnProvider<T>(new PropertyModel<List<CustomColumn<T>>>(setting,
							"columns")), groepHeaderRow, rowModel, item.getIndex());
				groepHeaderRow.add(headerColumns);

				GroupProperty<T> prop = contentDescription.getGroupProperties().get(property);

				String header = prop.getOmschrijving();
				if (value == null)
				{
					header = header + ": <Geen waarde>";
				}
				else
				{
					if (hasGroupColumns() || header == null)
					{
						header = value.toString();
					}
					else
					{
						header = header + ": " + value.toString();
					}
				}
				// Tel het aantal met deze waarde.
				int count = 0;
				int size = provider.size();
				boolean found = false;
				boolean stop = false;
				boolean forcedStop = false;
				int countFrom = Math.max(getViewOffset() - MAX_COUNT_IN_GROUP, 0);
				for (int counter = countFrom; counter < size; counter = counter + BATCH_SIZE)
				{
					IDataProvider< ? extends T> dataProvider = getDataProvider();
					Iterator< ? extends T> it = dataProvider.iterator(counter, BATCH_SIZE);
					while (it.hasNext())
					{
						T o = it.next();
						Serializable objectValue =
							(Serializable) PropertyResolver.getValue(property, o);
						if ((objectValue == null && currentGroupValue == null)
							|| (objectValue != null && currentGroupValue != null && objectValue
								.equals(currentGroupValue)))
						{
							found = true;
							count++;
						}
						else
						{
							if (found)
							{
								stop = true;
								break;
							}
						}
					}
					if (stop)
					{
						break;
					}
					if (count >= MAX_COUNT_IN_GROUP)
					{
						forcedStop = true;
						break;
					}
				}
				if (forcedStop)
				{
					header = header + " (meer dan " + MAX_COUNT_IN_GROUP + ")";
				}
				else
				{
					header = header + " (" + count + ")";
				}

				Label groepHeader = new Label("columnHeaderCaption", new Model<String>(header));
				groepHeader.add(new AttributeModifier("colspan", new Model<Integer>(
					getColspanVoorGroupHeaderCaption())));

				groepHeaderRow.add(groepHeader);

				// andere style class gebruiken voor group header met meerdere columns
				if (hasGroupColumns())
				{
					groepHeaderRow.add(new AppendingAttributeModifier("class", "withColumns"));
				}
			}
		}
		else
		{
			groepHeaderRow = new WebMarkupContainer(GROUPING_ROW_ID);
			groepHeaderRow.setVisible(false);
		}
		item.add(groepHeaderRow);
	}

	/**
	 * Factory methode voor de titel. Standaard wordt een model gebruikt dat het volgende
	 * format gebruikt: ${title} - ${x} tot ${y} van ${z}. Waarbij x en y staan voor de
	 * range en z vor het totale aantal items.
	 * 
	 * @param title
	 * @return model om titel te renderen
	 */
	protected IModel<String> createTitleModel(String title)
	{
		return new TitleModel(title, this);
	}

	private int calculateColspans(List<CustomColumn<T>> columns)
	{
		int ret = 0;
		for (CustomColumn<T> curColumn : columns)
		{
			if (curColumn instanceof ISpanningColumn< ? >)
				ret += ((ISpanningColumn<T>) curColumn).getSpan();
			else
				ret += 1;
		}
		return ret;
	}

	private ArrayList<Integer> collectSpans(final CustomColumn<T> customColumn)
	{
		ArrayList<Integer> spans = new ArrayList<Integer>();
		if (customColumn instanceof ISpanningColumn< ? >)
		{
			for (int count = 0; count < ((ISpanningColumn<T>) customColumn).getSpan(); count++)
			{
				spans.add(count);
			}
		}
		else
		{
			spans.add(0);
		}
		return spans;
	}

	private void createButtons(final ConcatModel exportHeaderModel)
	{
		add(createExtraButtons("extraButtons").setRenderBodyOnly(true));
		add(new CustomDataPanelExportLink<T>("export", this, data, "csvexport", exportHeaderModel));
		add(createAfdrukkenLink("print", getModel(), data));

		SelecteerKolommenModalWindow<T> selecteerKolommenModalWindow =
			new SelecteerKolommenModalWindow<T>("selecteerKolommenModalWindow",
				getContentDescription(), getModel());
		selecteerKolommenModalWindow.setWindowClosedCallback(new WindowClosedCallback()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void onClose(AjaxRequestTarget target)
			{
				target.addComponent(CustomDataPanel.this);
			}
		});

		add(selecteerKolommenModalWindow);
		add(createSelecteerKolommenLink("settings", selecteerKolommenModalWindow));

		SelecteerGroeperingModalWindow<T> selecteerGroeperingModalWindow =
			new SelecteerGroeperingModalWindow<T>("selecteerGroeperingModalWindow", this,
				selectedGroupProperty);
		add(selecteerGroeperingModalWindow);
		add(createGroeperenLink("groeperen", selecteerGroeperingModalWindow));
	}

	/**
	 * Factory methode om een link te maken naar een popup voor het kiezen van de
	 * kolommen.
	 * 
	 * @param id
	 *            id van de component
	 * @return component al dan niet zichtbaar
	 */
	@SuppressWarnings("unchecked")
	protected Component createSelecteerKolommenLink(String id,
			final CobraModalWindow selecteerKolommenModalWindow)
	{
		return new AjaxLink(id)
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick(AjaxRequestTarget target)
			{
				selecteerKolommenModalWindow.show(target);
			}
		};
	}

	/**
	 * Factory methode om een link te maken waarmee de gebruiker kan aangeven of hij de
	 * view wil laten groeperen.
	 * 
	 * @param id
	 *            id van het component
	 * @return component al dan niet zichtbaar
	 */
	@SuppressWarnings("unchecked")
	protected Component createGroeperenLink(String id,
			final CobraModalWindow selecteerGroeperingModalWindow)
	{
		return new AjaxLink(id)
		{
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isVisible()
			{
				return super.isVisible() && contentDescription.isGroupable();
			}

			@Override
			public void onClick(AjaxRequestTarget target)
			{
				selecteerGroeperingModalWindow.show(target);
			}
		};
	}

	/**
	 * Factory methode om een link te maken die de getoonde kolommen direct kan afdrukken.
	 * 
	 * @param id
	 *            id van de component
	 * @param columns
	 *            model dat de getoonde kolommen bevat
	 * @param dataz
	 *            de view waar het over gaat
	 * @return component al dan niet zichtbaar.
	 */
	protected Component createAfdrukkenLink(String id, ColumnModel<T> columns,
			ExportableDataView<T> dataz)
	{
		return new CustomDataPanelAfdrukkenLink<T>(id, this, dataz);
	}

	protected Panel createExtraButtons(String id)
	{
		return new EmptyPanel(id);
	}

	private WebMarkupContainer createGeenGegevensGevondenMarker()
	{
		WebMarkupContainer geenGegevensGevonden = new WebMarkupContainer("geenGegevensGevonden")
		{
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isVisible()
			{
				return super.isVisible() && data.getDataProvider().size() == 0;
			}
		};
		geenGegevensGevonden.setRenderBodyOnly(true);
		Label geenGegevensMsg = new Label("geenGegevensMsg", getGeengegevensTekst());
		geenGegevensMsg.add(new AttributeModifier("colspan", new LoadableDetachableModel<Integer>()
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected Integer load()
			{
				return getColspanVoorZichtbarekolommen();
			}
		}));
		geenGegevensGevonden.add(geenGegevensMsg);
		return geenGegevensGevonden;
	}

	private WebMarkupContainer createHeaderToolbars()
	{
		return new ListView<CustomDataPanelToolbar<List<CustomColumn<T>>>>(
			"headerbars",
			new PropertyModel<List<CustomDataPanelToolbar<List<CustomColumn<T>>>>>(this, "toolbars"))
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(ListItem<CustomDataPanelToolbar<List<CustomColumn<T>>>> item)
			{
				item.add(item.getModelObject());
			}
		};
	}

	private WebMarkupContainer createFooterToolbars()
	{
		return new ListView<CustomDataPanelToolbar<List<CustomColumn<T>>>>("footerbars",
			new PropertyModel<List<CustomDataPanelToolbar<List<CustomColumn<T>>>>>(this,
				"footerbars"))
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(ListItem<CustomDataPanelToolbar<List<CustomColumn<T>>>> item)
			{
				item.add(item.getModelObject());
			}

			@Override
			public boolean isVisible()
			{
				return super.isVisible() && !footerbars.isEmpty();
			}
		};
	}

	private WebMarkupContainer createNavigator()
	{
		WebMarkupContainer navigatorContainer = new WebMarkupContainer("navigatorContainer")
		{
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isVisible()
			{
				return super.isVisible() && getPageCount() > 1 && getRowCount() > 0
					&& getItemsPerPage() < Integer.MAX_VALUE;
			}
		};

		PagingNavigator navigator = new CustomPagingNavigator("navigator", data, titleLabel)
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onNavigate()
			{
				super.onNavigate();
				CustomDataPanel.this.onNavigate();
			}
		};
		navigatorContainer.add(navigator);

		IModel<Integer> aantalKolommenModel = new AbstractReadOnlyModel<Integer>()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public Integer getObject()
			{
				return getColspanVoorZichtbarekolommen();
			}
		};
		navigatorContainer.add(new AttributeModifier("colspan", aantalKolommenModel));
		return navigatorContainer;
	}

	public void onNavigate()
	{

	}

	/**
	 * @return Colspan voor de 'header' (caption) van de group header
	 */
	private int getColspanVoorGroupHeaderCaption()
	{
		return getColspanVoorZichtbarekolommen() - calculateColspans(getGroupColumns());
	}

	public int getColspanVoorZichtbarekolommen()
	{
		return calculateColspans(getModel().getObject());
	}

	private DataView<CustomColumn<T>> getColumnDataView(String rowId, final String columnId,
			final String contentId, IDataProvider<CustomColumn<T>> dataprovider,
			final WebMarkupContainer row, final IModel<T> rowModel, final int rowIndex)
	{
		return new DataView<CustomColumn<T>>(rowId, dataprovider)
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(Item<CustomColumn<T>> item)
			{
				populateColumn(columnId, contentId, item, row, rowModel, rowIndex);
			}

			@Override
			public IItemReuseStrategy getItemReuseStrategy()
			{
				if (reuseItems)
					return ReuseIfModelsEqualStrategy.getInstance();
				else
					return super.getItemReuseStrategy();
			}
		};
	}

	/**
	 * @return Een lijst van alle kolommen van de group header
	 */
	private List<CustomColumn<T>> getGroupColumns()
	{
		return contentDescription.getDefaultGroupProperty().getColumns();
	}

	/**
	 * @return True als in dit datapanel extra columns in de group header getoond moeten
	 *         worden.
	 */
	private boolean hasGroupColumns()
	{
		return (contentDescription.getDefaultGroupProperty() != null && contentDescription
			.getDefaultGroupProperty().getColumns().size() > 0);
	}

	private boolean mustHideColumn(IModel<T> rowModel, int rowIndex, CustomColumn<T> customColumn)
	{
		boolean skip = false;
		if (data.allowSkipColumns())
		{
			if (!customColumn.isRepeatWhenEqualToPrevRow() && rowIndex > 0)
			{
				Object prevValue = getRowValue(rowValues.get(rowIndex - 1));
				if (prevValue != null)
				{
					Object currentValue = getRowValue(rowModel.getObject());
					if (currentValue.equals(prevValue))
					{
						skip = true;
					}
				}
			}
		}
		return skip;
	}

	/**
	 * Gebruikt de {@link CustomColumn} uit het item om de kolom te renderen.
	 */
	private void populateColumn(String columnId, final String contentId,
			Item<CustomColumn<T>> item, final WebMarkupContainer row, final IModel<T> rowModel,
			int rowIndex)
	{
		onPopulateColumn(item, rowModel, rowIndex);
		final CustomColumn<T> customColumn = item.getModelObject();
		final boolean skip = mustHideColumn(rowModel, rowIndex, customColumn);

		ListView<Integer> spansView = new ListView<Integer>(columnId, collectSpans(customColumn))
		{
			private static final long serialVersionUID = 1L;

			@SuppressWarnings("unchecked")
			@Override
			protected void populateItem(ListItem<Integer> cell)
			{
				if (customColumn instanceof IStyledColumn< ? >)
				{
					IStyledColumn<T> styledColumn = (IStyledColumn<T>) customColumn;
					if (StringUtil.isNotEmpty(styledColumn.getCssClass()))
						cell
							.add(new AppendingAttributeModifier("class", styledColumn.getCssClass()));
				}

				if (skip)
					cell.add(new WebMarkupContainer(contentId));
				else
				{
					int span = cell.getModelObject();
					customColumn.populateItem(cell, contentId, row, rowModel, span);
				}
			}

			@Override
			public boolean getReuseItems()
			{
				return reuseItems;
			}

			@Override
			protected ListItem<Integer> newItem(int index)
			{
				return new ListItem<Integer>(index, getListItemModel(getModel(), index))
				{
					private static final long serialVersionUID = 1L;

					@Override
					public boolean isVisible()
					{
						if (customColumn instanceof HideableColumn< ? >)
							return super.isVisible()
								&& ((HideableColumn<T>) customColumn).isColumnVisible();
						return super.isVisible();
					}
				};
			}
		};
		item.add(spansView);
	}

	private final void populateRow(String rowId, String columnsId, String contentId,
			String headerRowId, String headerColumnsId, String headerContentId, Item<T> item)
	{
		if (rowValues == null)
		{
			rowValues = new HashMap<Integer, T>();
		}
		final IModel<T> rowModel = item.getModel();
		rowValues.put(item.getIndex(), rowModel.getObject());
		onPopulateItem(item);
		item.setRenderBodyOnly(true);

		addGroupHeader(headerRowId, headerColumnsId, headerContentId, item, rowModel);

		WebMarkupContainer row = getRow("row", item);
		row.setOutputMarkupId(true);
		DataView<CustomColumn<T>> columns =
			getColumnDataView(rowId, columnsId, contentId, new ColumnProvider<T>(getModel()), row,
				rowModel, item.getIndex());

		row.add(columns);
		item.add(row);
		// Geef subclasses een kans om zelf nog iets toe te xvoegen of te veranderen.
		onAfterPopulateItem(item);
	}

	/**
	 * 
	 * @return true als dit datapanel export in de achtergrond kan uitvoeren.
	 */
	public boolean supportsExportJobs()
	{
		return false;
	}

	/**
	 * Triggert een pdf-print van dit datapanel in een achtergrondtaak. De default
	 * implementatie doet niets, verschillende projecten, met verschillende soorten
	 * achtergrondtaken moeten dit zelf implementeren.
	 * 
	 * @param link
	 */
	public void triggerExportJob(CustomDataPanelAfdrukkenLink<T> link)
	{
		throw new UnsupportedOperationException(
			"Dit datapanel ondersteunt het exporteren in een achtergrondtaak niet");
	}

	/**
	 * Triggert een csv-export van dit datapanel in een achtergrondtaak. De default
	 * implementatie doet niets, verschillende projecten, met verschillende soorten
	 * achtergrondtaken moeten dit zelf implementeren.
	 * 
	 * @param link
	 */
	public void triggerExportJob(CustomDataPanelExportLink<T> link)
	{
		throw new UnsupportedOperationException(
			"Dit datapanel ondersteunt het exporteren in een achtergrondtaak niet");
	}

	public void createContextProvider()
	{
	}

	@SuppressWarnings("unchecked")
	public ColumnModel<T> getModel()
	{
		return (ColumnModel<T>) getDefaultModel();
	}
}
