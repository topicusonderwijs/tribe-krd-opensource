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
import nl.topicus.eduarte.entities.criteriumbank.Criterium;
import nl.topicus.eduarte.entities.landelijk.Cohort;
import nl.topicus.eduarte.entities.opleiding.Opleiding;
import nl.topicus.eduarte.krd.jobs.CriteriaKopierenJobDataMap;
import nl.topicus.eduarte.krd.principals.onderwijs.OpleidingCriteriaKopieren;
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
@PageInfo(title = "Criteria kopiëren", menu = "Onderwijs > Kopiëren criteria")
@InPrincipal(OpleidingCriteriaKopieren.class)
public class CriteriaKopierenStap3Page extends SecurePage implements
		IModuleEditPage<List<Criterium>>
{
	private IModel<List<Opleiding>> selection;

	private SecurePage returnPage;

	private CriteriaKopierenStap2Page stap2Page;

	private Form<Void> form;

	private KopieerSettings settings;

	public CriteriaKopierenStap3Page(SecurePage returnPage, CriteriaKopierenStap2Page stap2Page,
			List<Opleiding> selection, Cohort cohort)
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
				CriteriaKopierenOverzichtPage overzichtPage = new CriteriaKopierenOverzichtPage();
				overzichtPage.startJob(new CriteriaKopierenJobDataMap(settings,
					new HashSet<Opleiding>(selection.getObject()), stap2Page.getCriteria()));
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
		return new OnderwijsCollectiefMenu(id, OnderwijsCollectiefMenuItem.CriteriaKopieren);
	}
}
