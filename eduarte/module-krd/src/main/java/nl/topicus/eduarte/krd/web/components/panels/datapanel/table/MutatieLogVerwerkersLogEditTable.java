package nl.topicus.eduarte.krd.web.components.panels.datapanel.table;

import nl.topicus.cobra.web.components.datapanel.CustomDataPanelContentDescription;
import nl.topicus.cobra.web.components.datapanel.columns.CheckboxColumn;
import nl.topicus.cobra.web.components.datapanel.columns.CustomPropertyColumn;
import nl.topicus.cobra.web.components.datapanel.columns.CustomPropertyTextFieldColumn;
import nl.topicus.eduarte.krd.entities.mutatielog.MutatieLogVerwerkersLog;

import org.apache.wicket.behavior.SimpleAttributeModifier;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

public class MutatieLogVerwerkersLogEditTable extends
		CustomDataPanelContentDescription<MutatieLogVerwerkersLog>
{
	private static final long serialVersionUID = 1L;

	public MutatieLogVerwerkersLogEditTable()
	{
		super("Mutatielog verwerkerslog bewerken");
		createColumns();
	}

	private void createColumns()
	{
		addColumn(new CustomPropertyColumn<MutatieLogVerwerkersLog>("Verwerker", "Verwerker",
			"verwerker"));
		addColumn(new CheckboxColumn<MutatieLogVerwerkersLog>("Actief", "Actief")
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected IModel<Boolean> getCheckBoxModel(IModel<MutatieLogVerwerkersLog> rowModel)
			{
				return new PropertyModel<Boolean>(rowModel, "actief");
			}
		});
		addColumn(new CustomPropertyTextFieldColumn<MutatieLogVerwerkersLog>("TestEndpointAddress",
			"Test Endpoint Address", "testEndpointAddress")
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected TextField< ? > createTextField(String id, IModel< ? > propertyModel,
					IModel<MutatieLogVerwerkersLog> rowModel)
			{
				TextField< ? > textfield = super.createTextField(id, propertyModel, rowModel);
				textfield.add(new SimpleAttributeModifier("class", "unit_200"));

				return textfield;
			}
		});
		addColumn(new CustomPropertyTextFieldColumn<MutatieLogVerwerkersLog>(
			"TestServiceNameSpaceURI", "Test Service Namespace URI", "testServiceNameSpaceURI")
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected TextField< ? > createTextField(String id, IModel< ? > propertyModel,
					IModel<MutatieLogVerwerkersLog> rowModel)
			{
				TextField< ? > textfield = super.createTextField(id, propertyModel, rowModel);
				textfield.add(new SimpleAttributeModifier("class", "unit_200"));

				return textfield;
			}
		});
		addColumn(new CustomPropertyTextFieldColumn<MutatieLogVerwerkersLog>("TestServiceName",
			"Test Service Name", "testServiceName"));
		addColumn(new CustomPropertyTextFieldColumn<MutatieLogVerwerkersLog>("ProdEndpointAddress",
			"Productie Endpoint Address", "prodEndpointAddress")
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected TextField< ? > createTextField(String id, IModel< ? > propertyModel,
					IModel<MutatieLogVerwerkersLog> rowModel)
			{
				TextField< ? > textfield = super.createTextField(id, propertyModel, rowModel);
				textfield.add(new SimpleAttributeModifier("class", "unit_200"));

				return textfield;
			}
		});
		addColumn(new CustomPropertyTextFieldColumn<MutatieLogVerwerkersLog>(
			"ProdServiceNameSpaceURI", "Productie Service Namespace URI", "prodServiceNameSpaceURI")
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected TextField< ? > createTextField(String id, IModel< ? > propertyModel,
					IModel<MutatieLogVerwerkersLog> rowModel)
			{
				TextField< ? > textfield = super.createTextField(id, propertyModel, rowModel);
				textfield.add(new SimpleAttributeModifier("class", "unit_200"));

				return textfield;
			}
		});
		addColumn(new CustomPropertyTextFieldColumn<MutatieLogVerwerkersLog>("ProdServiceName",
			"Productie Service Name", "prodServiceName"));
	}
}
