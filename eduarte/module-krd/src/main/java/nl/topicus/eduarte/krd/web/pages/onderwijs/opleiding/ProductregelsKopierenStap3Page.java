package nl.topicus.eduarte.krd.web.pages.onderwijs.opleiding;

import java.util.HashSet;
import java.util.List;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.modelsv2.ModelFactory;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.util.ComponentUtil;
import nl.topicus.cobra.web.components.form.AutoFieldSet;
import nl.topicus.cobra.web.components.form.RenderMode;
import nl.topicus.cobra.web.components.menu.AbstractMenuBar;
import nl.topicus.cobra.web.components.panels.bottomrow.AnnulerenButton;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.cobra.web.components.panels.bottomrow.VolgendeButton;
import nl.topicus.cobra.web.components.panels.bottomrow.VorigeButton;
import nl.topicus.eduarte.entities.landelijk.Cohort;
import nl.topicus.eduarte.entities.opleiding.Opleiding;
import nl.topicus.eduarte.entities.productregel.Productregel;
import nl.topicus.eduarte.krd.jobs.ProductregelsKopierenJobDataMap;
import nl.topicus.eduarte.krd.principals.onderwijs.OpleidingProductregelsKopieren;
import nl.topicus.eduarte.web.components.menu.OnderwijsCollectiefMenu;
import nl.topicus.eduarte.web.components.menu.OnderwijsCollectiefMenuItem;
import nl.topicus.eduarte.web.components.menu.main.CoreMainMenuItem;
import nl.topicus.eduarte.web.pages.IModuleEditPage;
import nl.topicus.eduarte.web.pages.SecurePage;
import nl.topicus.eduarte.web.pages.shared.KopieerSettings;

import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

/**
 * @author loite
 */
@PageInfo(title = "Productregels kopieren stap 3", menu = "Onderwijs > Kopiëren productregels")
@InPrincipal(OpleidingProductregelsKopieren.class)
public class ProductregelsKopierenStap3Page extends SecurePage implements
		IModuleEditPage<List<Productregel>>
{
	private IModel<List<Opleiding>> selection;

	private SecurePage returnPage;

	private ProductregelsKopierenStap2Page stap2Page;

	private Form<Void> form;

	private KopieerSettings settings;

	public ProductregelsKopierenStap3Page(SecurePage returnPage,
			ProductregelsKopierenStap2Page stap2Page, List<Opleiding> selection, Cohort cohort)
	{
		super(CoreMainMenuItem.Onderwijs);
		this.returnPage = returnPage;
		this.stap2Page = stap2Page;
		this.selection = ModelFactory.getListModel(selection);
		settings = new KopieerSettings(cohort);

		form = new Form<Void>("form");
		add(form);

		AutoFieldSet<KopieerSettings> inputFields =
			new AutoFieldSet<KopieerSettings>("settings", Model.of(settings),
				"Instellingen voor kopiëren");
		inputFields.setPropertyNames("actieBijBestaandeStructuur", "bronCohort", "doelCohort",
			"kopieerOnderwijsproducten");
		inputFields.setRenderMode(RenderMode.EDIT);
		form.add(inputFields);

		createComponents();
	}

	@Override
	protected void fillBottomRow(BottomRowPanel panel)
	{
		panel.addButton(new VorigeButton(panel, form)
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onVorige()
			{
				setResponsePage(stap2Page);
			}
		});
		panel.addButton(new VolgendeButton(panel, form)
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onVolgende()
			{
				ProductregelsKopierenOverzichtPage overzichtPage =
					new ProductregelsKopierenOverzichtPage();
				overzichtPage.startJob(new ProductregelsKopierenJobDataMap(settings,
					new HashSet<Opleiding>(selection.getObject()), stap2Page.getProductregels()));
				setResponsePage(overzichtPage);
			}
		}.setLabel("Kopiëren"));
		panel.addButton(new AnnulerenButton(panel, returnPage));
	}

	@Override
	protected void onDetach()
	{
		super.onDetach();
		ComponentUtil.detachQuietly(selection);
		ComponentUtil.detachQuietly(stap2Page);
	}

	@Override
	public AbstractMenuBar createMenu(String id)
	{
		return new OnderwijsCollectiefMenu(id, OnderwijsCollectiefMenuItem.ProductregelsKopieren);
	}
}
