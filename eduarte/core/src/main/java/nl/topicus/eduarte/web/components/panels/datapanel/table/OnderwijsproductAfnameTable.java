package nl.topicus.eduarte.web.components.panels.datapanel.table;

import nl.topicus.cobra.web.components.datapanel.CustomDataPanelContentDescription;
import nl.topicus.cobra.web.components.datapanel.GroupProperty;
import nl.topicus.cobra.web.components.datapanel.columns.AbstractCustomColumn;
import nl.topicus.cobra.web.components.datapanel.columns.CustomPropertyColumn;
import nl.topicus.cobra.web.components.datapanel.columns.CustomPropertyDateFieldColumn;
import nl.topicus.eduarte.app.EduArteApp;
import nl.topicus.eduarte.app.EduArteModuleKey;
import nl.topicus.eduarte.entities.inschrijving.OnderwijsproductAfname;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.entities.onderwijsproduct.OnderwijsproductAfnameContext;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;

/**
 * @author vandekamp
 */
public class OnderwijsproductAfnameTable extends
		CustomDataPanelContentDescription<OnderwijsproductAfname>
{
	private static final long serialVersionUID = 1L;

	private static final class ProductregelColumn extends
			AbstractCustomColumn<OnderwijsproductAfname>
	{
		private static final long serialVersionUID = 1L;

		private final IModel<Verbintenis> verbintenisModel;

		public ProductregelColumn(String id, String header, IModel<Verbintenis> verbintenisModel)
		{
			super(id, header);
			this.verbintenisModel = verbintenisModel;
		}

		@Override
		public void populateItem(WebMarkupContainer cell, String componentId,
				WebMarkupContainer row, IModel<OnderwijsproductAfname> rowModel, int span)
		{
			OnderwijsproductAfname afname = rowModel.getObject();

			String label = "";
			if (getVerbintenis() != null)
			{
				OnderwijsproductAfnameContext context = getVerbintenis().getAfnameContext(afname);
				if (context != null)
					label = context.getProductregel().getNaam();
			}
			cell.add(new Label(componentId, label).setRenderBodyOnly(true));

		}

		private Verbintenis getVerbintenis()
		{
			return verbintenisModel.getObject();
		}

	}

	public OnderwijsproductAfnameTable(boolean edit, IModel<Verbintenis> verbintenisModel)
	{
		super("Afgenomen onderwijsproducten");
		addAllColumns(edit, verbintenisModel);
		addGroupProperties();
	}

	private void addGroupProperties()
	{
		addGroupProperty(new GroupProperty<OnderwijsproductAfname>(
			"onderwijsproduct.soortProduct.naam", "Soort product", "soortProduct.naam"));
	}

	private void addAllColumns(boolean edit, IModel<Verbintenis> verbintenisModel)
	{
		addColumn(new CustomPropertyColumn<OnderwijsproductAfname>("Code", "Code",
			"onderwijsproduct.code", "onderwijsproduct.code"));
		addColumn(new CustomPropertyColumn<OnderwijsproductAfname>("Titel", "Titel",
			"onderwijsproduct.titel", "onderwijsproduct.titel"));
		if (EduArteApp.get().isModuleActive(EduArteModuleKey.HOGER_ONDERWIJS))
			addColumn(new CustomPropertyColumn<OnderwijsproductAfname>("Credits", "Credits",
				"credits"));
		if (edit)
		{
			addColumn(new CustomPropertyDateFieldColumn<OnderwijsproductAfname>("Begindatum",
				"Begindatum", "begindatum", "begindatum"));
			addColumn(new CustomPropertyDateFieldColumn<OnderwijsproductAfname>("Einddatum",
				"Einddatum", "einddatum", "einddatum"));
		}
		else
		{
			addColumn(new CustomPropertyColumn<OnderwijsproductAfname>("Begindatum", "Begindatum",
				"begindatum", "begindatum"));
			addColumn(new CustomPropertyColumn<OnderwijsproductAfname>("Einddatum", "Einddatum",
				"einddatum", "einddatum"));
		}
		addColumn(new CustomPropertyColumn<OnderwijsproductAfname>("Werkstuktitel",
			"Werkstuktitel", "werkstuktitel", "werkstuktitel"));
		addColumn(new ProductregelColumn("Productregel", "Productregel", verbintenisModel));
		addColumn(new CustomPropertyColumn<OnderwijsproductAfname>("Soort product", "Soort",
			"soortProduct.naam", "onderwijsproduct.soortProduct.naam").setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<OnderwijsproductAfname>("Cohort", "Cohort", "cohort",
			"cohort").setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<OnderwijsproductAfname>("Vrijstelling", "Vrijstelling", "vrijstellingType",
			"vrijstellingType").setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<OnderwijsproductAfname>("Externe Organisatie",
			"Externe Organisatie", "externeOrganisatie", "externeOrganisatie")
			.setDefaultVisible(false));

	}
}
