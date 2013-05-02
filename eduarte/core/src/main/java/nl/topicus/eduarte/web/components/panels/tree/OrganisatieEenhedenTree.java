package nl.topicus.eduarte.web.components.panels.tree;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;

import nl.topicus.cobra.modelsv2.ModelFactory;
import nl.topicus.cobra.util.StringUtil;
import nl.topicus.eduarte.app.EduArteContext;
import nl.topicus.eduarte.web.pages.SecurePage;
import nl.topicus.eduarte.web.pages.beheer.organisatie.OrganisatieEenheidKaartPage;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.markup.html.tree.table.ColumnLocation;
import org.apache.wicket.extensions.markup.html.tree.table.IColumn;
import org.apache.wicket.extensions.markup.html.tree.table.PropertyRenderableColumn;
import org.apache.wicket.extensions.markup.html.tree.table.PropertyTreeColumn;
import org.apache.wicket.extensions.markup.html.tree.table.TreeTable;
import org.apache.wicket.extensions.markup.html.tree.table.ColumnLocation.Alignment;
import org.apache.wicket.extensions.markup.html.tree.table.ColumnLocation.Unit;
import org.apache.wicket.markup.html.tree.ITreeState;
import org.apache.wicket.model.IModel;

/**
 * Boom die de structuur van de organisatie-eenheden van een onderwijinstelling weergeeft.
 * 
 * @author loite
 */
public class OrganisatieEenhedenTree extends TreeTable
{
	private static final long serialVersionUID = 1L;

	private static final IColumn[] getColumns()
	{
		List<IColumn> cols = new ArrayList<IColumn>(2);
		cols.add(new PropertyTreeColumn(new ColumnLocation(Alignment.LEFT, 50, Unit.PERCENT),
			"Organisatie-eenheid", "organisatieEenheid.naam")
		{
			private static final long serialVersionUID = 1L;

			@Override
			public String renderNode(TreeNode node)
			{
				String text = super.renderNode(node);
				return StringUtil.truncate(text, 55, "...");
			}
		});
		cols.add(new PropertyRenderableColumn(new ColumnLocation(Alignment.LEFT, 50, Unit.PERCENT),
			"Locaties", "organisatieEenheid.locatieNamen"));
		IColumn[] colsArray = new IColumn[cols.size()];
		cols.toArray(colsArray);
		return colsArray;
	}

	public OrganisatieEenhedenTree(String id, IModel<Date> peildatumModel)
	{
		super(id, new DefaultTreeModel(new OrganisatieEenheidTreeNode(null, EduArteContext.get()
			.getInstelling().getRootOrganisatieEenheid(), peildatumModel)), getColumns());
	}

	@Override
	protected ITreeState newTreeState()
	{
		ITreeState treeState = super.newTreeState();
		treeState.expandAll();
		return treeState;
	}

	@Override
	protected void onNodeLinkClicked(AjaxRequestTarget target, TreeNode node)
	{
		OrganisatieEenheidKaartPage page =
			new OrganisatieEenheidKaartPage(ModelFactory
				.getModel(((OrganisatieEenheidTreeNode) node).getOrganisatieEenheid()),
				(SecurePage) getPage());
		setResponsePage(page);
	}
}
