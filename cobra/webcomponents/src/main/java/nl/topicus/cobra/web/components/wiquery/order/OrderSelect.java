package nl.topicus.cobra.web.components.wiquery.order;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nl.topicus.cobra.web.components.wiquery.resources.ResourceRefUtil;

import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.FormComponentPanel;
import org.apache.wicket.markup.html.form.HiddenField;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.odlabs.wiquery.core.commons.IWiQueryPlugin;
import org.odlabs.wiquery.core.commons.WiQueryResourceManager;
import org.odlabs.wiquery.core.javascript.JsQuery;
import org.odlabs.wiquery.core.javascript.JsStatement;
import org.odlabs.wiquery.core.options.Options;
import org.odlabs.wiquery.ui.commons.WiQueryUIPlugin;

@WiQueryUIPlugin
public class OrderSelect<T> extends FormComponentPanel<List<T>> implements
		IWiQueryPlugin {
	private class ItemListView extends ListView<T> {
		private static final long serialVersionUID = 1L;

		public ItemListView(String id, IModel<List<T>> model) {
			super(id, model);
			setReuseItems(false);
		}

		@Override
		protected void populateItem(ListItem<T> item) {
			item.add(new Label("name", renderItem(item.getModelObject())));

			item.setOutputMarkupId(true);
			int index = getChoices().indexOf(item.getModelObject());
			markupToChoice.put(item.getMarkupId(), renderer.getIdValue(item
					.getModelObject(), index));
		}

		private String renderItem(T object) {
			Object objectValue = renderer.getDisplayValue(object);
			Class<?> objectClass = objectValue == null ? null : objectValue
					.getClass();
			if (objectClass != null && objectClass != String.class) {
				return getConverter(objectClass).convertToString(objectValue,
						getLocale());
			} else if (objectValue != null) {
				return objectValue.toString();
			}
			return null;
		}
	}

	private static final long serialVersionUID = 1L;

	private static final String NO_CHANGE = "NO_CHANGE";

	private IModel<List<T>> choices;

	private IChoiceRenderer<T> renderer;

	private HiddenField<String> selectionValue;

	private ListView<T> availableList;

	private ListView<T> selectionList;

	private List<String> choiceIds = new ArrayList<String>();

	private Map<String, String> markupToChoice = new HashMap<String, String>();

	private WebMarkupContainer selection;

	private WebMarkupContainer available;

	// label voor kolom met *beschikbare* velden
	private String beschikbaarLabel = "Beschikbaar";

	// label voor kolom met *geselecteerde* velden
	private String geselecteerdLabel = "Geselecteerd";

	public OrderSelect(String id, IModel<List<T>> model,
			IModel<List<T>> choices, IChoiceRenderer<T> renderer) {
		super(id, model);
		setOutputMarkupId(true);
		this.choices = choices;
		this.renderer = renderer;

		selectionValue = new HiddenField<String>("selectionValue",
				new Model<String>(NO_CHANGE));
		add(selectionValue);

		add(new Label("BeschikbaarLabel", beschikbaarLabel));
		add(new Label("GeselecteerdLabel", geselecteerdLabel));

		availableList = new ItemListView("availableItems",
				new LoadableDetachableModel<List<T>>() {
					private static final long serialVersionUID = 1L;

					@Override
					protected List<T> load() {
						ArrayList<T> ret = new ArrayList<T>(getChoices());
						ret.removeAll(getSelection());
						return ret;
					}
				});
		available = new WebMarkupContainer("availableList");
		available.setOutputMarkupId(true);
		add(available);
		available.add(availableList);

		selectionList = new ItemListView("selectionItems", model);
		selection = new WebMarkupContainer("selectionList");
		selection.setOutputMarkupId(true);
		add(selection);
		selection.add(selectionList);
	}

	public List<T> getChoices() {
		return choices.getObject();
	}

	public List<T> getSelection() {
		return getModelObject();
	}

	@Override
	protected void convertInput() {
		String value = selectionValue.getConvertedInput() == null ? ""
				: selectionValue.getConvertedInput();
		if (NO_CHANGE.equals(value)) {
			setConvertedInput(getSelection());
		} else {
			ArrayList<T> newValue = new ArrayList<T>();
			for (String curMarkupId : value.split(",")) {
				String itemId = markupToChoice.get(curMarkupId);
				if (itemId != null) {
					for (int count = 0; count < getChoices().size(); count++) {
						T curObject = getChoices().get(count);
						if (itemId
								.equals(renderer.getIdValue(curObject, count))) {
							newValue.add(curObject);
							break;
						}
					}
				}
			}
			selectionValue.setConvertedInput(NO_CHANGE);
			setConvertedInput(newValue);
		}
	}

	@Override
	protected void onBeforeRender() {
		markupToChoice.clear();
		int index = 0;
		for (T curSelected : getChoices())
			choiceIds.add(renderer.getIdValue(curSelected, index++));

		super.onBeforeRender();
	}

	@Override
	public void contribute(WiQueryResourceManager resourceManager) {
		ResourceRefUtil.addOrderSelect(resourceManager);
	}

	@Override
	public JsStatement statement() {
		Options options = new Options();
		options.putLiteral("availableId", available.getMarkupId());
		options.putLiteral("selectedId", selection.getMarkupId());
		options.putLiteral("containerId", getMarkupId());
		return new JsQuery(this).$().chain("orderSelect",
				options.getJavaScriptOptions());
	}

	@Override
	protected void onDetach() {
		super.onDetach();
		choices.detach();
	}

	@Override
	protected void onComponentTag(ComponentTag tag) {
		super.onComponentTag(tag);
		tag.remove("name");
	}

	@Override
	protected void onDisabled(ComponentTag tag) {
	}

	public void setBeschikbaarLabel(String beschikbaarLabel) {
		this.beschikbaarLabel = beschikbaarLabel;
		addOrReplace(new Label("BeschikbaarLabel", beschikbaarLabel));
	}

	public void setGeselecteerdLabel(String geselecteerdLabel) {
		this.geselecteerdLabel = geselecteerdLabel;
		addOrReplace(new Label("GeselecteerdLabel", geselecteerdLabel));
	}
}
