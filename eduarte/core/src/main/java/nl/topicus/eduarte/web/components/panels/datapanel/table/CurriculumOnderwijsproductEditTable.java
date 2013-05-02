package nl.topicus.eduarte.web.components.panels.datapanel.table;

import nl.topicus.cobra.web.behaviors.AjaxFormComponentSaveBehaviour;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanelContentDescription;
import nl.topicus.cobra.web.components.datapanel.columns.AjaxButtonColumn;
import nl.topicus.cobra.web.components.datapanel.columns.CustomPropertyColumn;
import nl.topicus.cobra.web.components.datapanel.columns.CustomPropertyTextFieldColumn;
import nl.topicus.eduarte.entities.curriculum.Curriculum;
import nl.topicus.eduarte.entities.curriculum.CurriculumOnderwijsproduct;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.IModel;

public abstract class CurriculumOnderwijsproductEditTable extends
		CustomDataPanelContentDescription<CurriculumOnderwijsproduct>
{
	private static final long serialVersionUID = 1L;

	public CurriculumOnderwijsproductEditTable(Curriculum curriculum)
	{
		super("Curriculum voor cohort " + curriculum.getCohort().toString());
		createColumns();
	}

	private void createColumns()
	{
		addColumn(new CustomPropertyColumn<CurriculumOnderwijsproduct>("Onderwijsproduct",
			"Onderwijsproduct", "onderwijsproduct", "onderwijsproduct"));

		addColumn(new CustomPropertyRequiredTextFieldColumn<CurriculumOnderwijsproduct>("Leerjaar",
			"Leerjaar", "leerjaar", "leerjaar"));
		addColumn(new CustomPropertyRequiredTextFieldColumn<CurriculumOnderwijsproduct>("Periode",
			"Periode", "periode", "periode"));
		addColumn(new CustomPropertyRequiredTextFieldColumn<CurriculumOnderwijsproduct>(
			"Onderwijstijd", "Onderwijstijd", "onderwijstijd", "onderwijstijd"));

		addColumn(new AjaxButtonColumn<CurriculumOnderwijsproduct>("kopieeren", "Kopieeren")
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected String getCssDisabled()
			{
				return "newItem_grey";
			}

			@Override
			protected String getCssEnabled()
			{
				return "newItem_grey";
			}

			@Override
			public void onClick(WebMarkupContainer item,
					IModel<CurriculumOnderwijsproduct> rowModel, AjaxRequestTarget target)
			{
				onCopy(rowModel, target);
			}
		});

		addColumn(new AjaxButtonColumn<CurriculumOnderwijsproduct>("verwijderen", "Verwijderen")
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected String getCssDisabled()
			{
				return "deleteItem_grey";
			}

			@Override
			protected String getCssEnabled()
			{
				return "deleteItem";
			}

			@Override
			public void onClick(WebMarkupContainer item,
					IModel<CurriculumOnderwijsproduct> rowModel, AjaxRequestTarget target)
			{
				onDelete(rowModel, target);
			}
		});
	}

	abstract protected void onDelete(IModel<CurriculumOnderwijsproduct> rowModel,
			AjaxRequestTarget target);

	abstract protected void onCopy(IModel<CurriculumOnderwijsproduct> rowModel,
			AjaxRequestTarget target);

	private class CustomPropertyRequiredTextFieldColumn<T> extends CustomPropertyTextFieldColumn<T>
	{
		private static final long serialVersionUID = 1L;

		public CustomPropertyRequiredTextFieldColumn(String id, String header, String sortProperty,
				String propertyExpression)
		{
			super(id, header, sortProperty, propertyExpression);
		}

		@SuppressWarnings("unchecked")
		@Override
		protected TextField< ? > createTextField(String id, IModel< ? > propertyModel,
				final IModel<T> rowModel)
		{
			TextField ret = new TextField(id, propertyModel)
			{
				private static final long serialVersionUID = 1L;

				@Override
				public boolean isEnabled()
				{
					return super.isEnabled()
						&& CustomPropertyRequiredTextFieldColumn.this.isContentsEnabled(rowModel);
				}

				@Override
				public boolean isVisible()
				{
					return super.isVisible()
						&& CustomPropertyRequiredTextFieldColumn.this.isContentsVisible(rowModel);
				}
			};
			ret.add(new AjaxFormComponentSaveBehaviour());
			ret.setRequired(true);
			return ret;
		}
	}
}
