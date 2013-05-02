/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.cobra.web.components.datapanel.columns;

import java.util.List;

import nl.topicus.cobra.web.components.panels.TypedPanel;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.RadioChoice;
import org.apache.wicket.model.IModel;

public abstract class RadioChoiceColumn<T> extends AbstractCustomColumn<T>
{
	private static final long serialVersionUID = 1L;

	public RadioChoiceColumn(String id, String header)
	{
		super(id, header);
	}

	@Override
	public void populateItem(WebMarkupContainer cell, String componentId, WebMarkupContainer row,
			IModel<T> rowModel, int span)
	{
		cell.add(new RadioChoicePanel(componentId, rowModel));
	}

	private final class RadioChoicePanel extends TypedPanel<T>
	{
		private static final long serialVersionUID = 1L;

		public RadioChoicePanel(String id, final IModel<T> rowModel)
		{
			super(id, rowModel);
			setRenderBodyOnly(true);
			RadioChoice<T> choice =
				new RadioChoice<T>("radio", createRadioModel(rowModel), getChoices(rowModel))
				{
					private static final long serialVersionUID = 1L;

					@Override
					public boolean isEnabled()
					{
						return super.isEnabled()
							&& RadioChoiceColumn.this.isContentsEnabled(rowModel);
					}

					@Override
					public boolean isVisible()
					{
						return super.isVisible()
							&& RadioChoiceColumn.this.isContentsVisible(rowModel);
					}
				};
			choice.setSuffix(" ");
			add(choice);
		}
	}

	protected abstract IModel<T> createRadioModel(IModel<T> rowModel);

	protected abstract IModel<List<T>> getChoices(IModel<T> rowModel);
}
