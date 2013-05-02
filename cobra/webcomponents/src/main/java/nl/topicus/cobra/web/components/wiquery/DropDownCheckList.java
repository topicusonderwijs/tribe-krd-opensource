package nl.topicus.cobra.web.components.wiquery;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import nl.topicus.cobra.web.components.wiquery.resources.ResourceRefUtil;

import org.apache.wicket.markup.html.form.AbstractSingleSelectChoice;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.markup.html.form.ListMultipleChoice;
import org.apache.wicket.model.IModel;
import org.apache.wicket.security.swarm.models.SwarmModel;
import org.odlabs.wiquery.core.commons.IWiQueryPlugin;
import org.odlabs.wiquery.core.commons.WiQueryResourceManager;
import org.odlabs.wiquery.core.javascript.JsQuery;
import org.odlabs.wiquery.core.javascript.JsStatement;
import org.odlabs.wiquery.core.options.Options;
import org.odlabs.wiquery.ui.commons.WiQueryUIPlugin;

/**
 * @author vandekamp TODO MARK14 de allItemsName wordt niet meer gebruikt
 */
@WiQueryUIPlugin
public class DropDownCheckList<T> extends ListMultipleChoice<T> implements IWiQueryPlugin
{
	private static final long serialVersionUID = 1L;

	private Options options = new Options();

	private boolean addCheckAll;

	private String allItemsName;

	public DropDownCheckList(String id, IModel<Collection<T>> model,
			IModel< ? extends List< ? extends T>> choiceModel)
	{
		this(id, model, choiceModel, false);
	}

	public DropDownCheckList(String id, IModel<Collection<T>> model, List< ? extends T> choices)
	{
		super(id, model, choices);
	}

	public DropDownCheckList(String id, IModel<Collection<T>> model,
			IModel< ? extends List< ? extends T>> choiceModel, boolean addCheckAll)
	{
		this(id, model, choiceModel, addCheckAll, "(Alle soorten)");
	}

	public DropDownCheckList(String id, IModel<Collection<T>> model,
			IModel< ? extends List< ? extends T>> choiceModel, boolean addCheckAll,
			String allItemsName)
	{
		super(id, model, choiceModel);

		this.addCheckAll = addCheckAll;
		this.allItemsName = allItemsName;
	}

	public DropDownCheckList(String id, IModel<Collection<T>> model,
			IModel< ? extends List< ? extends T>> choiceModel, IChoiceRenderer< ? super T> renderer)
	{
		this(id, model, choiceModel, renderer, false);
	}

	public DropDownCheckList(String id, IModel<Collection<T>> model,
			IModel< ? extends List< ? extends T>> choiceModel,
			IChoiceRenderer< ? super T> renderer, boolean addCheckAll)
	{
		super(id, model, choiceModel, renderer);
		this.addCheckAll = addCheckAll;

	}

	@Override
	protected List<T> convertChoiceIdsToChoices(String[] ids)
	{
		List<T> ret = new ArrayList<T>(super.convertChoiceIdsToChoices(ids));
		if (addCheckAll)
			ret.remove(allItemsName);
		return ret;
	}

	private void initSecureChoicesModel()
	{
		try
		{
			Field choicesField =
				AbstractSingleSelectChoice.class.getSuperclass().getDeclaredField("choices");
			choicesField.setAccessible(true);
			Object choicesModel = choicesField.get(this);
			if (choicesModel instanceof SwarmModel< ? >)
				((SwarmModel< ? >) choicesModel).getSecurityId(this);
		}
		catch (SecurityException e)
		{
			throw new RuntimeException(e);
		}
		catch (NoSuchFieldException e)
		{
			throw new RuntimeException(e);
		}
		catch (IllegalArgumentException e)
		{
			throw new RuntimeException(e);
		}
		catch (IllegalAccessException e)
		{
			throw new RuntimeException(e);
		}
	}

	@Override
	public List<T> getChoices()
	{
		initSecureChoicesModel();
		List<T> ret = new ArrayList<T>(super.getChoices());
		if (addCheckAll)
			ret.add(0, null);
		return ret;
	}

	@Override
	public void contribute(WiQueryResourceManager resourceManager)
	{
		ResourceRefUtil.addDropDownCheckList(resourceManager);
	}

	public JsStatement statement()
	{
		options.put("firstItemChecksAll", addCheckAll);
		return new JsQuery(this).$().chain("dropdownchecklist", options.getJavaScriptOptions());
	}

	public Options getOptions()
	{
		return options;
	}
}
