package nl.topicus.eduarte.krd.web.pages.deelnemer.examens.individueel;

import nl.topicus.cobra.web.components.datapanel.CustomDataPanelContentDescription;
import nl.topicus.cobra.web.components.datapanel.columns.CheckboxColumn;
import nl.topicus.cobra.web.components.datapanel.columns.CustomPropertyColumn;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.entities.onderwijsproduct.OnderwijsproductAfnameContext;

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

/**
 * Columns voor tabellen van OnderwijsproductAfnameContexten
 * 
 * @author loite
 */
public class ExamenOnderwijsproductAfnameContextTable extends
		CustomDataPanelContentDescription<OnderwijsproductAfnameContext>
{
	private static final long serialVersionUID = 1L;

	public ExamenOnderwijsproductAfnameContextTable(final IModel<Verbintenis> verbintenisModel)
	{
		super("Onderwijsproducten");
		addColumn(new CustomPropertyColumn<OnderwijsproductAfnameContext>("Soort", "Soort",
			"productregel.soortProductregel"));
		addColumn(new CustomPropertyColumn<OnderwijsproductAfnameContext>("Afkorting", "Afkorting",
			"productregel.afkorting").setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<OnderwijsproductAfnameContext>("Naam", "Naam",
			"productregel.naam"));
		addColumn(new CustomPropertyColumn<OnderwijsproductAfnameContext>("Keuze", "Keuze",
			"onderwijsproductAfname.onderwijsproduct.titel"));

		Verbintenis verbintenis = verbintenisModel.getObject();
		if (verbintenis.isVAVOVerbintenis() || verbintenis.isVOVerbintenis())
		{
			addColumn(new CheckboxColumn<OnderwijsproductAfnameContext>("volgendeTijdvak",
				"Volg. tijdvak")
			{
				private static final long serialVersionUID = 1L;

				@Override
				protected IModel<Boolean> getCheckBoxModel(
						IModel<OnderwijsproductAfnameContext> rowModel)
				{
					return new PropertyModel<Boolean>(rowModel, "verwezenNaarVolgendTijdvak");
				}
			});
		}

	}
}
