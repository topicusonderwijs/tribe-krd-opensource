package nl.topicus.cobra.web.components.datapanel;

import java.util.HashMap;
import java.util.Map;

import nl.topicus.cobra.util.ModelObjectKey;
import nl.topicus.cobra.web.behaviors.ClientSideCallable;
import nl.topicus.cobra.web.behaviors.ServerCallAjaxBehaviour;
import nl.topicus.cobra.web.components.wiquery.resources.ResourceRefUtil;

import org.apache.wicket.ResourceReference;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.behavior.HeaderContributor;
import org.apache.wicket.markup.html.IHeaderContributor;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.resources.JavascriptResourceReference;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.odlabs.wiquery.core.javascript.JsQuery;
import org.odlabs.wiquery.core.options.Options;

public class CollapsableRowFactoryDecorator<T> extends AbstractRowFactoryDecorator<T>
{
	private static final long serialVersionUID = 1L;

	Map<ModelObjectKey, ExpandCollapseClassModel> classModels =
		new HashMap<ModelObjectKey, ExpandCollapseClassModel>();

	// Bepaalt of de groupheaders standaard uitgeklapt zijn
	private boolean defaultExpanded = true;

	public CollapsableRowFactoryDecorator(CustomDataPanelRowFactory<T> inner)
	{
		super(inner);
	}

	@Override
	public void bind(CustomDataPanel<T> panel)
	{
		super.bind(panel);
		panel.add(new HeaderContributor(new IHeaderContributor()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void renderHead(IHeaderResponse response)
			{
				response.renderJavascriptReference(new JavascriptResourceReference(
					ResourceRefUtil.class, "treetable/javascripts/jquery.treeTable.js"));
				response.renderCSSReference(new ResourceReference(ResourceRefUtil.class,
					"treetable/stylesheets/jquery.treeTable.css"));
			}
		}));
		JsQuery jsq = new JsQuery(panel.getTable());
		Options treeTableOptions = new Options();
		treeTableOptions.put("indent", 19);
		treeTableOptions.put("callback",
			"function(node, val){node.serverCall('setExpanded', val);}");
		jsq.$().chain("treeTable", treeTableOptions.getJavaScriptOptions());
		jsq.contribute(panel.getTable());
	}

	public void makeParent(WebMarkupContainer parent, IModel< ? > rowModel)
	{
		ExpandCollapseClassModel classModel = classModels.get(new ModelObjectKey(rowModel));
		if (classModel == null)
		{
			classModel = new ExpandCollapseClassModel();
		}
		classModel.setExpanded(getDefaultExpanded());
		// always overwrite, prevents holding on to old models
		classModels.put(new ModelObjectKey(rowModel), classModel);
		parent.add(new ServerCallAjaxBehaviour(classModel));
		parent.add(new AttributeAppender("class", classModel, " "));
		parent.setOutputMarkupId(true);
	}

	public void makeChild(final WebMarkupContainer parent, WebMarkupContainer child)
	{
		child.add(new AttributeAppender("class", new AbstractReadOnlyModel<String>()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public String getObject()
			{
				return parent != null && parent.isVisible() ? "child-of-" + parent.getMarkupId()
					: null;
			}
		}, " "));
	}

	@Override
	public void detach()
	{
		super.detach();
		for (ModelObjectKey curKey : classModels.keySet())
			curKey.detach();
	}

	public boolean getDefaultExpanded()
	{
		return defaultExpanded;
	}

	public void setDefaultExpanded(boolean expanded)
	{
		this.defaultExpanded = expanded;
	}

	public static class ExpandCollapseClassModel extends AbstractReadOnlyModel<String>
	{
		private static final long serialVersionUID = 1L;

		private boolean expanded = true;

		@ClientSideCallable
		public void setExpanded(boolean expanded)
		{
			this.expanded = expanded;
		}

		@Override
		public String getObject()
		{
			return expanded ? "expanded" : "collapsed";
		}
	}
}
