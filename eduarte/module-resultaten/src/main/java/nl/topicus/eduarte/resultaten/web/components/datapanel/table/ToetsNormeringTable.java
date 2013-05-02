package nl.topicus.eduarte.resultaten.web.components.datapanel.table;

import java.math.BigDecimal;

import nl.topicus.cobra.web.behaviors.AppendingAttributeModifier;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanel;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanelContentDescription;
import nl.topicus.cobra.web.components.datapanel.columns.CustomPropertyColumn;
import nl.topicus.cobra.web.components.datapanel.columns.CustomPropertyTextFieldColumn;
import nl.topicus.eduarte.entities.resultaatstructuur.Toets;
import nl.topicus.eduarte.resultaten.web.components.resultaat.ToetsSchaallengteValidator;

import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.validation.IValidator;
import org.apache.wicket.validation.validator.RangeValidator;

/**
 * Definieert de content voor het tonen van de BRON Batches in een {@link CustomDataPanel}
 * .
 */
public class ToetsNormeringTable extends CustomDataPanelContentDescription<Toets>
{
	private static final long serialVersionUID = 1L;

	public ToetsNormeringTable(boolean edit)
	{
		super("Toetsen");
		createColumns(edit);
	}

	private void createColumns(boolean edit)
	{
		addColumn(new CustomPropertyColumn<Toets>("Onderwijsproductcode", "Onderwijsproductcode",
			"resultaatstructuur.onderwijsproduct.code"));
		addColumn(new CustomPropertyColumn<Toets>("Onderwijsproducttitel", "Onderwijsproducttitel",
			"resultaatstructuur.onderwijsproduct.titel"));
		addColumn(new CustomPropertyColumn<Toets>("Toetscode", "Toetscode", "code"));
		for (int tijdvak = 1; tijdvak <= 3; tijdvak++)
			addLengteEnNormering(edit, tijdvak);
	}

	private void addLengteEnNormering(boolean edit, int tijdvak)
	{
		createColumn("Scoreschaallengte tijdvak " + tijdvak, "Lengte T" + tijdvak,
			"scoreschaalLengteTijdvak" + tijdvak, Integer.class, tijdvak, edit);
		createColumn("Normering tijdvak " + tijdvak, "Normering T" + tijdvak,
			"scoreschaalNormeringTijdvak" + tijdvak, BigDecimal.class, tijdvak, edit);
	}

	private <T> void createColumn(final String title, String header, String property,
			final Class<T> type, final int tijdvak, boolean edit)
	{
		if (edit)
			addColumn(new CustomPropertyTextFieldColumn<Toets>(title, header, property)
			{
				private static final long serialVersionUID = 1L;

				@SuppressWarnings("unchecked")
				@Override
				protected TextField<T> createTextField(String id, IModel< ? > propertyModel,
						final IModel<Toets> rowModel)
				{
					TextField<T> ret = new TextField<T>(id, (IModel<T>) propertyModel)
					{
						private static final long serialVersionUID = 1L;

						@Override
						protected void onModelChanged()
						{
							toetsUpdated(rowModel.getObject());
						}
					};
					ret.setType(type);
					ret.add(new AppendingAttributeModifier("class", "unit_60"));
					if (type.equals(Integer.class))
					{
						ret.add((IValidator<T>) new ToetsSchaallengteValidator(rowModel,
							tijdvak - 1));
					}
					else
						ret.add((IValidator<T>) new RangeValidator<BigDecimal>(new BigDecimal(
							"-2.0"), new BigDecimal("4.0")));
					ret.setLabel(new Model<String>(title));
					return ret;
				}
			});
		else
			addColumn(new CustomPropertyColumn<Toets>(title, header, property));
	}

	@SuppressWarnings("unused")
	protected void toetsUpdated(Toets toets)
	{
	}
}
