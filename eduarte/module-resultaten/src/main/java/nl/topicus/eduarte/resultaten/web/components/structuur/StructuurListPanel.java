package nl.topicus.eduarte.resultaten.web.components.structuur;

import java.util.List;

import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.web.behaviors.AppendingAttributeModifier;
import nl.topicus.cobra.web.components.panels.TypedPanel;
import nl.topicus.eduarte.dao.helpers.ResultaatstructuurDataAccessHelper;
import nl.topicus.eduarte.entities.resultaatstructuur.Resultaatstructuur;
import nl.topicus.eduarte.zoekfilters.ResultaatstructuurZoekFilter;

import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.navigation.paging.IPageable;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;

public abstract class StructuurListPanel extends TypedPanel<Resultaatstructuur> implements
		IPageable
{
	private class ListModel extends LoadableDetachableModel<List<Resultaatstructuur>>
	{
		private static final long serialVersionUID = 1L;

		private ResultaatstructuurZoekFilter filter;

		public ListModel(ResultaatstructuurZoekFilter filter)
		{
			this.filter = filter;
		}

		@Override
		protected List<Resultaatstructuur> load()
		{
			return DataAccessRegistry.getHelper(ResultaatstructuurDataAccessHelper.class).list(
				filter);
		}

		@Override
		protected void onDetach()
		{
			filter.detach();
		}
	}

	private static final long serialVersionUID = 1L;

	public StructuurListPanel(String id, IModel<Resultaatstructuur> selected,
			ResultaatstructuurZoekFilter filter)
	{
		super(id, selected);
		add(new ListView<Resultaatstructuur>("structuren", new ListModel(filter))
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(final ListItem<Resultaatstructuur> item)
			{
				Resultaatstructuur structuur = item.getModelObject();
				item.add(new Label("label", structuur.getCode() + " - " + structuur.getNaam()));
				if (item.getModelObject().equals(StructuurListPanel.this.getModelObject()))
					item.add(new AppendingAttributeModifier("class", "selected"));
				item.add(new AjaxEventBehavior("onclick")
				{
					private static final long serialVersionUID = 1L;

					@Override
					protected void onEvent(AjaxRequestTarget target)
					{
						StructuurListPanel.this.setModelObject(item.getModelObject());
						target.addComponent(StructuurListPanel.this);
						onClick(target, item.getModelObject());
					}

				});
			}
		});
		setOutputMarkupId(true);
	}

	protected abstract void onClick(AjaxRequestTarget target, Resultaatstructuur structuur);

	@Override
	public int getCurrentPage()
	{
		return 0;
	}

	@Override
	public int getPageCount()
	{
		return 1;
	}

	@Override
	public void setCurrentPage(int page)
	{
	}
}
