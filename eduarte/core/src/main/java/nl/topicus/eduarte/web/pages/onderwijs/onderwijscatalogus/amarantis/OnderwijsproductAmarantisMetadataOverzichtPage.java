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

@PageInfo(title = "Onderwijscatalogus Amarantis - Metadata overzicht", menu = {"Onderwijs > Onderwijsproducten > [onderwijsproduct] > Curriculum > Metadata > Bewerken"})
@InPrincipal(OnderwijscatalogusAmarantisInzien.class)
public class OnderwijsproductAmarantisMetadataOverzichtPage extends AbstractOnderwijsproductPage
{
	private static final long serialVersionUID = 1L;

	public OnderwijsproductAmarantisMetadataOverzichtPage(Onderwijsproduct onderwijsproduct)
	{
		super(OnderwijsproductMenuItem.AmarantisMetadata, ModelFactory.getModel(onderwijsproduct));

		createAutoFieldSetLeft();
		createAutoFieldSetRight();

		createComponents();
	}

	private void createAutoFieldSetLeft()
	{
		AutoFieldSet<Onderwijsproduct> autoFieldSetLeft =
			new AutoFieldSet<Onderwijsproduct>("autoFieldSetLeft",
				getContextOnderwijsproductModel(), "Metadata overzicht");
		autoFieldSetLeft.setPropertyNames("individueel", "onafhankelijk", "begeleid",
			"niveauaanduiding", "leerstofdrager", "vereisteVoorkennis");
		autoFieldSetLeft.setSortAccordingToPropertyNames(true);

		add(autoFieldSetLeft);
	}

	private void createAutoFieldSetRight()
	{
		AutoFieldSet<Onderwijsproduct> autoFieldSetRight =
			new AutoFieldSet<Onderwijsproduct>("autoFieldSetRight",
				getContextOnderwijsproductModel(), "Metadata overzicht");
		autoFieldSetRight.setPropertyNames("literatuur", "hulpmiddelen", "docentactiviteiten");
		autoFieldSetRight.setSortAccordingToPropertyNames(true);

		add(autoFieldSetRight);
	}

	@Override
	protected void fillBottomRow(BottomRowPanel panel)
	{
		super.fillBottomRow(panel);
		panel.addButton(new BewerkenButton<Onderwijsproduct>(panel,
			OnderwijsproductAmarantisMetadataEditPage.class, getContextOnderwijsproductModel(),
			OnderwijsproductAmarantisMetadataOverzichtPage.this));
	}
}
