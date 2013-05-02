package nl.topicus.eduarte.web.pages.onderwijs.onderwijscatalogus.amarantis;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.modelsv2.ModelFactory;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.web.components.form.AutoFieldSet;
import nl.topicus.cobra.web.components.panels.bottomrow.BewerkenButton;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.eduarte.core.principals.onderwijs.OnderwijscatalogusAmarantisInzien;
import nl.topicus.eduarte.entities.onderwijsproduct.Onderwijsproduct;
import nl.topicus.eduarte.web.components.menu.OnderwijsproductMenuItem;
import nl.topicus.eduarte.web.pages.onderwijs.onderwijscatalogus.AbstractOnderwijsproductPage;

import org.apache.wicket.markup.html.WebMarkupContainer;

@PageInfo(title = "Onderwijscatalogus Amarantis - Belasting overzicht", menu = {"Onderwijs > Onderwijsproducten > [onderwijsproduct] > Curriculum > Belasting"})
@InPrincipal(OnderwijscatalogusAmarantisInzien.class)
public class OnderwijsproductAmarantisBelastingOverzichtPage extends AbstractOnderwijsproductPage
{
	private static final long serialVersionUID = 1L;

	public OnderwijsproductAmarantisBelastingOverzichtPage(Onderwijsproduct onderwijsproduct)
	{
		super(OnderwijsproductMenuItem.AmarantisBelasting, ModelFactory.getModel(onderwijsproduct));

		createAutoFieldSetLeft();
		createAutoFieldSetRight();

		createComponents();
	}

	private void createAutoFieldSetLeft()
	{
		AutoFieldSet<Onderwijsproduct> autoFieldSetLeft =
			new AutoFieldSet<Onderwijsproduct>("autoFieldSetLeft",
				getContextOnderwijsproductModel(), "Belasting overzicht");
		autoFieldSetLeft.setPropertyNames("belasting", "belastingEC", "aantalWeken",
			"frequentiePerWeek", "tijdPerEenheid", "onderwijstijd");
		autoFieldSetLeft.setSortAccordingToPropertyNames(true);

		add(autoFieldSetLeft);
	}

	private void createAutoFieldSetRight()
	{
		add(new WebMarkupContainer("autoFieldSetRight"));
	}

	@Override
	protected void fillBottomRow(BottomRowPanel panel)
	{
		super.fillBottomRow(panel);
		panel.addButton(new BewerkenButton<Onderwijsproduct>(panel,
			OnderwijsproductAmarantisBelastingEditPage.class, getContextOnderwijsproductModel(),
			OnderwijsproductAmarantisBelastingOverzichtPage.this));
	}
}
