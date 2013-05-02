package nl.topicus.eduarte.web.pages.onderwijs.onderwijscatalogus.amarantis;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.modelsv2.DefaultModelManager;
import nl.topicus.cobra.modelsv2.ModelFactory;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.web.components.form.AutoFieldSet;
import nl.topicus.cobra.web.components.form.RenderMode;
import nl.topicus.cobra.web.components.panels.bottomrow.AnnulerenButton;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.cobra.web.components.panels.bottomrow.OpslaanButton;
import nl.topicus.cobra.web.pages.IEditPage;
import nl.topicus.eduarte.core.principals.onderwijs.OnderwijscatalogusAmarantisWijzigen;
import nl.topicus.eduarte.entities.onderwijsproduct.Onderwijsproduct;
import nl.topicus.eduarte.web.components.menu.OnderwijsproductMenuItem;
import nl.topicus.eduarte.web.pages.SecurePage;
import nl.topicus.eduarte.web.pages.onderwijs.onderwijscatalogus.AbstractOnderwijsproductPage;

import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.IModel;

@PageInfo(title = "Onderwijscatalogus Amarantis - Metadata bewerken", menu = {"Onderwijs > Onderwijsproducten > [onderwijsproduct] > Curriculum > Metadata > Bewerken"})
@InPrincipal(OnderwijscatalogusAmarantisWijzigen.class)
public class OnderwijsproductAmarantisMetadataEditPage extends AbstractOnderwijsproductPage
		implements IEditPage
{
	private static final long serialVersionUID = 1L;

	private Form<Void> form;

	private SecurePage returnToPage;

	public OnderwijsproductAmarantisMetadataEditPage(
			IModel<Onderwijsproduct> onderwijsproductModel, SecurePage returnToPage)
	{
		this(onderwijsproductModel.getObject(), returnToPage);
	}

	public OnderwijsproductAmarantisMetadataEditPage(Onderwijsproduct onderwijsproduct,
			SecurePage returnToPage)
	{
		super(OnderwijsproductMenuItem.AmarantisMetadata, ModelFactory
			.getCompoundChangeRecordingModel(onderwijsproduct, new DefaultModelManager(
				Onderwijsproduct.class)));
		this.returnToPage = returnToPage;

		form = new Form<Void>("form");

		createAutoFieldSetLeft();
		createAutoFieldSetRight();

		add(form);

		createComponents();
	}

	private void createAutoFieldSetLeft()
	{
		AutoFieldSet<Onderwijsproduct> autoFieldSetLeft =
			new AutoFieldSet<Onderwijsproduct>("autoFieldSetLeft",
				getContextOnderwijsproductModel(), "Metadata bewerken");
		autoFieldSetLeft.setPropertyNames("individueel", "onafhankelijk", "begeleid",
			"niveauaanduiding", "leerstofdrager", "vereisteVoorkennis");
		autoFieldSetLeft.setSortAccordingToPropertyNames(true);
		autoFieldSetLeft.setRenderMode(RenderMode.EDIT);

		form.add(autoFieldSetLeft);
	}

	private void createAutoFieldSetRight()
	{
		AutoFieldSet<Onderwijsproduct> autoFieldSetRight =
			new AutoFieldSet<Onderwijsproduct>("autoFieldSetRight",
				getContextOnderwijsproductModel(), "Metadata bewerken");
		autoFieldSetRight.setPropertyNames("literatuur", "hulpmiddelen", "docentactiviteiten");
		autoFieldSetRight.setSortAccordingToPropertyNames(true);
		autoFieldSetRight.setRenderMode(RenderMode.EDIT);

		form.add(autoFieldSetRight);
	}

	@Override
	protected void fillBottomRow(BottomRowPanel panel)
	{
		super.fillBottomRow(panel);
		panel.addButton(new OpslaanButton(panel, form)
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onSubmit()
			{
				getContextOnderwijsproduct().saveOrUpdate();
				getContextOnderwijsproduct().commit();

				setResponsePage(new OnderwijsproductAmarantisMetadataOverzichtPage(
					getContextOnderwijsproduct()));
			}
		});
		panel.addButton(new AnnulerenButton(panel, returnToPage));
	}
}
