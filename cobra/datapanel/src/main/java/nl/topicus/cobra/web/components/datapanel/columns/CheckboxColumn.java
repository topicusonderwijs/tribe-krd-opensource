/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.cobra.web.components.datapanel.columns;

import nl.topicus.cobra.web.components.panels.TypedPanel;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxCheckBox;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

/**
 * Column die default een (niet-aangevinkte) checkbox toont. Override methodes
 * {@link #onCheckboxSelectionChanged(IModel, Object)} en
 * {@link #getCheckBoxModel(IModel)} om eigen models en eigen acties uit te voeren.
 * 
 * @author bos
 */
public class CheckboxColumn<T> extends AbstractCustomColumn<T>
{
	private static final long serialVersionUID = 1L;

	/**
	 * Geeft aan of er per geklikte checkbox een seintje naar de server moet.
	 */
	private final boolean wantOnSelectionChangedNotifications;

	private boolean ajax;

	public CheckboxColumn(String id, String header)
	{
		this(id, header, false);
	}

	public CheckboxColumn(String id, String header, boolean wantOnSelectionChangedNotifications)
	{
		super(id, header);
		this.wantOnSelectionChangedNotifications = wantOnSelectionChangedNotifications;
	}

	public CheckboxColumn(String id, String header, boolean wantOnSelectionChangedNotifications,
			boolean ajax)
	{
		super(id, header);
		this.wantOnSelectionChangedNotifications = wantOnSelectionChangedNotifications;
		this.ajax = ajax;
	}

	@Override
	public void populateItem(WebMarkupContainer cell, String componentId, WebMarkupContainer row,
			IModel<T> rowModel, int span)
	{
		cell.add(new CheckboxPanel(componentId, "checkbox", rowModel)
			.setVisible(isContentsVisible(rowModel)));
	}

	private final class CheckboxPanel extends TypedPanel<T>
	{
		private static final long serialVersionUID = 1L;

		public CheckboxPanel(String id, String checkboxId, final IModel<T> rowModel)
		{
			super(id, rowModel);
			CheckBox checkBox;

			if (ajax)
			{
				checkBox = new AjaxCheckBox(checkboxId, getCheckBoxModel(rowModel))
				{
					private static final long serialVersionUID = 1L;

					@Override
					protected void onUpdate(AjaxRequestTarget target)
					{
						onCheckboxSelectionChanged(getModel(), rowModel, target);
					}

					@Override
					public boolean isEnabled()
					{
						return super.isEnabled() && CheckboxColumn.this.isContentsEnabled(rowModel);
					}

					@Override
					public boolean isVisible()
					{
						return super.isVisible() && CheckboxColumn.this.isContentsVisible(rowModel);
					}
				};
				add(checkBox);
			}
			else
			{
				checkBox = new CheckBox(checkboxId, getCheckBoxModel(rowModel))
				{
					private static final long serialVersionUID = 1L;

					@Override
					protected void onSelectionChanged(Object newSelection)
					{
						onCheckboxSelectionChanged(rowModel, newSelection);
					}

					@Override
					protected boolean wantOnSelectionChangedNotifications()
					{
						return wantOnSelectionChangedNotifications;
					}

					@Override
					public boolean isEnabled()
					{
						return super.isEnabled() && CheckboxColumn.this.isContentsEnabled(rowModel);
					}

					@Override
					public boolean isVisible()
					{
						return super.isVisible() && CheckboxColumn.this.isContentsVisible(rowModel);
					}
				};
				add(checkBox);
			}
			setCheckBoxTitleText(checkBox, rowModel);
		}
	}

	@SuppressWarnings("unused")
	protected void onCheckboxSelectionChanged(IModel<Boolean> checkboxModel, IModel<T> rowModel,
			AjaxRequestTarget target)
	{
	}

	@SuppressWarnings("unused")
	protected void onCheckboxSelectionChanged(IModel<T> rowModel, Object newSelection)
	{
	}

	@SuppressWarnings("unused")
	protected IModel<Boolean> getCheckBoxModel(IModel<T> rowModel)
	{
		return new Model<Boolean>(Boolean.FALSE);
	}

	@SuppressWarnings("unused")
	protected void setCheckBoxTitleText(CheckBox checkbox, IModel<T> rowModel)
	{
	}
}
