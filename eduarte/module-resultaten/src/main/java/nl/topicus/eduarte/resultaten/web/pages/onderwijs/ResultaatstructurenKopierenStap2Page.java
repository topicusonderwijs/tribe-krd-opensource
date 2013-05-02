package nl.topicus.eduarte.resultaten.web.pages.onderwijs;

import java.util.HashSet;
import java.util.List;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.modelsv2.ModelFactory;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.util.ComponentUtil;
import nl.topicus.cobra.web.components.form.AutoFieldSet;
import nl.topicus.cobra.web.components.form.RenderMode;
import nl.topicus.cobra.web.components.form.modifier.EnableModifier;
import nl.topicus.cobra.web.components.form.modifier.VisibilityModifier;
import nl.topicus.cobra.web.components.menu.AbstractMenuBar;
import nl.topicus.cobra.web.components.panels.bottomrow.AnnulerenButton;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.cobra.web.components.panels.bottomrow.VolgendeButton;
import nl.topicus.cobra.web.components.panels.bottomrow.VorigeButton;
import nl.topicus.eduarte.app.EduArteApp;
import nl.topicus.eduarte.app.EduArteModuleKey;
import nl.topicus.eduarte.entities.landelijk.Cohort;
import nl.topicus.eduarte.entities.onderwijsproduct.Onderwijsproduct;
import nl.topicus.eduarte.entities.resultaatstructuur.Resultaatstructuur;
import nl.topicus.eduarte.entities.resultaatstructuur.Resultaatstructuur.Type;
import nl.topicus.eduarte.resultaten.jobs.SchrijfSelectie;
import nl.topicus.eduarte.resultaten.jobs.ResultaatstructurenKopierenJobDataMap;
import nl.topicus.eduarte.resultaten.jobs.ResultaatstructuurKopieerSettings;
import nl.topicus.eduarte.resultaten.principals.onderwijs.OnderwijsproductResultaatstructuurKopieren;
import nl.topicus.eduarte.web.components.autoform.EduArteAjaxRefreshModifier;
import nl.topicus.eduarte.web.components.menu.OnderwijsCollectiefMenu;
import nl.topicus.eduarte.web.components.menu.OnderwijsCollectiefMenuItem;
import nl.topicus.eduarte.web.components.menu.main.CoreMainMenuItem;
import nl.topicus.eduarte.web.pages.IModuleEditPage;
import nl.topicus.eduarte.web.pages.SecurePage;

import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

/**
 * @author papegaaij
 */
@PageInfo(title = "Resultaatstructuren kopieren stap 2", menu = "Onderwijs > Onderwijsproducten > Kopiëren resultaatstructuren")
@InPrincipal(OnderwijsproductResultaatstructuurKopieren.class)
public class ResultaatstructurenKopierenStap2Page extends SecurePage implements
		IModuleEditPage<List<Resultaatstructuur>>
{
	private IModel<List<Onderwijsproduct>> selection;

	private SecurePage returnPage;

	private ResultaatstructurenKopierenStap1Page stap1Page;

	private Form<Void> form;

	private ResultaatstructuurKopieerSettings settings;

	public ResultaatstructurenKopierenStap2Page(SecurePage returnPage,
			ResultaatstructurenKopierenStap1Page stap1Page, List<Onderwijsproduct> selection,
			Cohort cohort)
	{
		super(CoreMainMenuItem.Onderwijs);
		this.returnPage = returnPage;
		this.stap1Page = stap1Page;
		this.selection = ModelFactory.getListModel(selection);
		settings =
			new ResultaatstructuurKopieerSettings(cohort, stap1Page.getBronStructuur() == null
				? cohort.getVolgende() : cohort);

		form = new Form<Void>("form");
		add(form);

		boolean formatiefEnabled =
			EduArteApp.get().isModuleActive(EduArteModuleKey.FORMATIEVE_RESULTATEN);
		if (!formatiefEnabled)
		{
			settings.setKopieerActie(SchrijfSelectie.Resultaatstructuren);
			settings.setType(Type.SUMMATIEF);
		}

		AutoFieldSet<ResultaatstructuurKopieerSettings> inputFields =
			new AutoFieldSet<ResultaatstructuurKopieerSettings>("settings",
				new Model<ResultaatstructuurKopieerSettings>(settings),
				"Instellingen voor kopiëren");
		inputFields.setRenderMode(RenderMode.EDIT);
		inputFields.addFieldModifier(new VisibilityModifier(stap1Page.getBronStructuur() == null
			&& formatiefEnabled, "type", "code"));
		inputFields.addFieldModifier(new VisibilityModifier(formatiefEnabled, "kopieerActie",
			"actieBijBestaandeVerwijzingen"));
		inputFields.addFieldModifier(new EduArteAjaxRefreshModifier("kopieerActie",
			"actieBijBestaandeStructuur", "actieBijBestaandeVerwijzingen"));
		inputFields.addFieldModifier(new EduArteAjaxRefreshModifier("type", "code"));
		inputFields.addFieldModifier(new EnableModifier("code",
			new AbstractReadOnlyModel<Boolean>()
			{
				private static final long serialVersionUID = 1L;

				@Override
				public Boolean getObject()
				{
					return settings.getType() == Type.FORMATIEF;
				}
			}));
		inputFields.addFieldModifier(new EnableModifier("actieBijBestaandeStructuur",
			new AbstractReadOnlyModel<Boolean>()
			{
				private static final long serialVersionUID = 1L;

				@Override
				public Boolean getObject()
				{
					return settings.isStructurenKopieren();
				}
			}));
		inputFields.addFieldModifier(new EnableModifier("actieBijBestaandeVerwijzingen",
			new AbstractReadOnlyModel<Boolean>()
			{
				private static final long serialVersionUID = 1L;

				@Override
				public Boolean getObject()
				{
					return settings.isVerwijzingenKopieren();
				}
			}));

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
				setResponsePage(stap1Page);
			}
		});
		panel.addButton(new VolgendeButton(panel, form)
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onVolgende()
			{
				ResultaatstructurenKopierenOverzichtPage overzichtPage =
					new ResultaatstructurenKopierenOverzichtPage();
				overzichtPage.startJob(new ResultaatstructurenKopierenJobDataMap(settings,
					new HashSet<Onderwijsproduct>(selection.getObject()), stap1Page
						.getBronStructuur()));
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
		ComponentUtil.detachQuietly(stap1Page);
	}

	@Override
	public AbstractMenuBar createMenu(String id)
	{
		return new OnderwijsCollectiefMenu(id,
			OnderwijsCollectiefMenuItem.ResultaatstructurenKopieren);
	}
}
