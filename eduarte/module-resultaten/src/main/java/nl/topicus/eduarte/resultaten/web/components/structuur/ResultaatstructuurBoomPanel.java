package nl.topicus.eduarte.resultaten.web.components.structuur;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import nl.topicus.cobra.web.components.panels.TypedPanel;
import nl.topicus.eduarte.entities.resultaatstructuur.Resultaatstructuur;
import nl.topicus.eduarte.entities.resultaatstructuur.Toets;

import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.behavior.SimpleAttributeModifier;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.PropertyModel;

public class ResultaatstructuurBoomPanel extends TypedPanel<Resultaatstructuur>
{
	private static final long serialVersionUID = 1L;

	private class BoomInformation
	{
		private List<List<Toets>> toetsen = new ArrayList<List<Toets>>();

		private int height = 0;

		private int width = 0;

		private BoomInformation()
		{
			if (getModelObject() == null)
				return;

			List<List<Toets>> toetsenPerColumn = new ArrayList<List<Toets>>();
			LinkedList<Toets> todo = new LinkedList<Toets>();
			todo.add(getModelObject().getEindresultaat());
			int depth = -1;
			int remainingAtDepth = 0;
			while (!todo.isEmpty())
			{
				if (remainingAtDepth == 0)
				{
					depth++;
					remainingAtDepth = todo.size();
				}
				Toets curToets = todo.removeFirst();
				List<Toets> children = getChildren(curToets);
				todo.addAll(children);
				if (toetsenPerColumn.size() == depth)
					toetsenPerColumn.add(new ArrayList<Toets>());
				toetsenPerColumn.get(depth).add(curToets);
				if (children.isEmpty())
					height++;
				remainingAtDepth--;
			}

			width = toetsenPerColumn.size();
			List<Integer> filled = new ArrayList<Integer>();
			for (int colNr = 0; colNr < width; colNr++)
				filled.add(-1);
			List<Integer> toetsIndex = new ArrayList<Integer>();
			for (int colNr = 0; colNr < width; colNr++)
				toetsIndex.add(-1);

			for (int rowNr = 0; rowNr < height; rowNr++)
			{
				List<Toets> curRow = new ArrayList<Toets>();
				toetsen.add(curRow);
				for (int colNr = 0; colNr < width; colNr++)
				{
					if (rowNr > filled.get(colNr))
					{
						int curToetsIndex = toetsIndex.get(colNr) + 1;
						toetsIndex.set(colNr, curToetsIndex);
						Toets curToets = toetsenPerColumn.get(colNr).get(curToetsIndex);
						int toetsSize = countLeafToetsen(curToets);
						filled.set(colNr, rowNr + toetsSize - 1);
						curRow.add(curToets);
						if (getChildren(curToets).isEmpty())
							for (int incColNr = colNr + 1; incColNr < width; incColNr++)
								filled.set(incColNr, filled.get(incColNr) + 1);
					}
					else
						curRow.add(null);
				}
			}
		}

		@SuppressWarnings("unused")
		public List<List<Toets>> getToetsen()
		{
			return toetsen;
		}

		public int getWidth()
		{
			return width;
		}
	}

	private class BoomModel extends LoadableDetachableModel<BoomInformation>
	{
		private static final long serialVersionUID = 1L;

		@Override
		protected BoomInformation load()
		{
			return new BoomInformation();
		}
	}

	private BoomModel boomModel;

	public ResultaatstructuurBoomPanel(String id, IModel<Resultaatstructuur> model)
	{
		super(id, model);

		boomModel = new BoomModel();
		add(new ListView<List<Toets>>("rows", new PropertyModel<List<List<Toets>>>(boomModel,
			"toetsen"))
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(final ListItem<List<Toets>> rowItem)
			{
				rowItem.add(new ListView<Toets>("cols", rowItem.getModel())
				{
					private static final long serialVersionUID = 1L;

					@Override
					protected void populateItem(final ListItem<Toets> colItem)
					{
						if (colItem.getModelObject() == null)
							colItem.setVisible(false);
						else
						{
							colItem.add(createToetsPanel("toets", colItem.getModel()));
							colItem.add(new SimpleAttributeModifier("rowspan", Integer
								.toString(countLeafToetsen(colItem.getModelObject()))));
							if (getChildren(colItem.getModelObject()).isEmpty())
							{
								colItem.add(new SimpleAttributeModifier("colspan",
									Integer.toString(boomModel.getObject().getWidth()
										- colItem.getIndex())));
							}
							colItem.add(new AttributeAppender("class",
								new AbstractReadOnlyModel<String>()
								{
									private static final long serialVersionUID = 1L;

									@Override
									public String getObject()
									{
										return getClassesFor(colItem.getModelObject(), rowItem
											.getModelObject().indexOf(colItem.getModelObject()));
									}
								}, " "));
						}
					}
				});
			}
		});
	}

	private String getClassesFor(Toets toets, int depth)
	{
		StringBuilder sb = new StringBuilder();
		if (toets.getParent() == null)
			sb.append("root");
		else
		{
			boolean eerste = toets.isEersteChild();
			boolean laatste = toets.isLaatsteChild();
			if (eerste)
				sb.append("first");
			if (laatste)
			{
				if (eerste)
					sb.append(' ');
				sb.append("last");
			}
			if (!eerste && !laatste)
				sb.append("middle");
		}
		if (!getChildren(toets).isEmpty())
			sb.append(" samengesteld");
		if (toets.isVerwijsbaar())
			sb.append(" verwijsbaar");
		sb.append(" depth-" + Math.min(depth, 5));
		addExtraClasses(sb, toets);

		return sb.toString();
	}

	private int countLeafToetsen(Toets toets)
	{
		List<Toets> children = getChildren(toets);
		if (!children.isEmpty())
		{
			int total = 0;
			for (Toets curToets : children)
				total += countLeafToetsen(curToets);
			return total;
		}
		return 1;
	}

	protected List<Toets> getChildren(Toets toets)
	{
		return toets.getChildren();
	}

	@SuppressWarnings("unused")
	protected void addExtraClasses(StringBuilder sb, Toets toets)
	{
	}

	@Override
	protected void onDetach()
	{
		super.onDetach();
		boomModel.detach();
	}

	protected ToetsBoomPanel createToetsPanel(String id, IModel<Toets> model)
	{
		return new ToetsBoomPanel(id, model);
	}
}
