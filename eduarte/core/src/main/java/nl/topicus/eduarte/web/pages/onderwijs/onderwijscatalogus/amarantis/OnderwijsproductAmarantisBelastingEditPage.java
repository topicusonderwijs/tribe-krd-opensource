package nl.topicus.eduarte.web.pages.onderwijs.onderwijscatalogus.amarantis;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.modelsv2.DefaultModelManager;
import nl.topicus.cobra.modelsv2.ModelFactory;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.web.components.form.AutoFieldSet;
import nl.topicus.cobra.web.components.form.RenderMode;
import nl.topicus.cobra.web.components.form.modifier.RequiredModifier;
import nl.topicus.cobra.web.components.panels.bottomrow.AnnulerenButton;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.cobra.web.components.panels.bottomrow.OpslaanButton;
import nl.topicus.cobra.web.pages.IEditPage;
import nl.topicus.eduarte.core.principals.onderwijs.OnderwijscatalogusAmarantisWijzigen;
import nl.topicus.eduarte.entities.onderwijsproduct.Onderwijsproduct;
import nl.topicus.eduarte.web.components.autoform.EduArteAjaxRefreshModifier;
import nl.topicus.eduarte.web.components.menu.OnderwijsproductMenuItem;
import nl.topicus.eduarte.web.pages.SecurePage;
import nl.topicus.eduarte.web.pages.onderwijs.onderwijscatalogus.AbstractOnderwijsproductPage;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.IModel;

@PageInfo(title = "Onderwijscatalogus Amarantis - Belasting bewerken", menu = {"Onderwijs > Onderwijsproducten > [onderwijsproduct] > Curriculum > Belasting > Bewerken"})
@InPrincipal(OnderwijscatalogusAmarantisWijzigen.class)
public class OnderwijsproductAmarantisBelastingEditPage extends AbstractOnderwijsproductPage
		implements IEditPage
{
	private static final long serialVersionUID = 1L;

	private Form<Void> form;

	private SecurePage returnToPage;

	public OnderwijsproductAmarantisBelastingEditPage(
			IModel<Onderwijsproduct> onderwijsproductModel, SecurePage returnToPage)
	{
		this(onderwijsproductModel.getObject(), returnToPage);
	}

	public OnderwijsproductAmarantisBelastingEditPage(Onderwijsproduct onderwijsproduct,
			SecurePage returnToPage)
	{
		super(OnderwijsproductMenuItem.AmarantisBelasting, ModelFactory
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
				getContextOnderwijsproductModel(), "Belasting bewerken");
		autoFieldSetLeft.setPropertyNames("belasting", "belastingEC", "aantalWeken",
			"frequentiePerWeek", "tijdPerEenheid", "onderwijstijd");
		autoFieldSetLeft.setSortAccordingToPropertyNames(true);
		autoFieldSetLeft.setRenderMode(RenderMode.EDIT);

		autoFieldSetLeft.addFieldModifier(new RequiredModifier(true, "belasting", "aantalWeken",
			"frequentiePerWeek", "tijdPerEenheid"));

		autoFieldSetLeft.addFieldModifier(new EduArteAjaxRefreshModifier("aantalWeken",
			"onderwijstijd"));
		autoFieldSetLeft.addFieldModifier(new EduArteAjaxRefreshModifier("frequentiePerWeek",
			"onderwijstijd"));
		autoFieldSetLeft.addFieldModifier(new EduArteAjaxRefreshModifier("tijdPerEenheid",
			"onderwijstijd"));

		form.add(autoFieldSetLeft);
	}

	private void createAutoFieldSetRight()
	{
		form.add(new WebMarkupContainer("autoFieldSetRight"));
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
				getContextOnderwijsproduct().setOmvang(
					getContextOnderwijsproduct().getOnderwijstijd());

				getContextOnderwijsproduct().saveOrUpdate();
				getContextOnderwijsproduct().commit();

				setResponsePage(new OnderwijsproductAmarantisBelastingOverzichtPage(
					getContextOnderwijsproduct()));
			}
		});
		panel.addButton(new AnnulerenButton(panel, returnToPage));
	}
}
