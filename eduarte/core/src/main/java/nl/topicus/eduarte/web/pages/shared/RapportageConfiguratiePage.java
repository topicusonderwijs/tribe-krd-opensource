package nl.topicus.eduarte.web.pages.shared;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.entities.IdObject;
import nl.topicus.cobra.reflection.ReflectionUtil;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.templates.documents.DocumentTemplateType;
import nl.topicus.cobra.util.ComponentUtil;
import nl.topicus.cobra.web.components.panels.bottomrow.AnnulerenButton;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.cobra.web.components.panels.bottomrow.OpslaanButton;
import nl.topicus.cobra.web.components.panels.datapanel.selection.IDatabaseSelectionComponent;
import nl.topicus.cobra.web.pages.IEditPage;
import nl.topicus.cobra.zoekfilters.DetachableZoekFilter;
import nl.topicus.eduarte.core.principals.Always;
import nl.topicus.eduarte.entities.bijlage.BijlageEntiteit;
import nl.topicus.eduarte.entities.bijlage.IBijlageKoppelEntiteit;
import nl.topicus.eduarte.entities.rapportage.DocumentTemplate;
import nl.topicus.eduarte.jobs.rapportage.RapportageJobDataMap;
import nl.topicus.eduarte.web.components.link.MultiEntityRapportageGenereerLink;
import nl.topicus.eduarte.web.components.panels.templates.RapportageConfiguratiePanel;
import nl.topicus.eduarte.web.pages.AbstractDynamicContextPage;
import nl.topicus.eduarte.web.pages.SecurePage;
import nl.topicus.eduarte.web.pages.SubpageContext;

import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

@InPrincipal(Always.class)
@PageInfo(title = "Rapportage instellingen", menu = "Meerdere paden")
public class RapportageConfiguratiePage<R extends IBijlageKoppelEntiteit< ? extends BijlageEntiteit>, S extends IdObject, ZF extends DetachableZoekFilter<S>>
		extends AbstractDynamicContextPage<Void> implements IEditPage
{
	private IModel<DocumentTemplate> templateModel;

	private SecurePage returnPage;

	private DocumentTemplateType eindType;

	private IDatabaseSelectionComponent<R, S, ZF> selection;

	private RapportageConfiguratiePanel<R> configPanel;

	private Form<Void> form;

	@SuppressWarnings("unchecked")
	public RapportageConfiguratiePage(IModel<DocumentTemplate> templateModel,
			SecurePage returnPage, DocumentTemplateType eindType,
			IDatabaseSelectionComponent<R, S, ZF> selection)
	{
		super(new SubpageContext(returnPage));
		this.templateModel = templateModel;
		this.returnPage = returnPage;
		this.eindType = eindType;
		this.selection = selection;

		form = new Form<Void>("form");
		add(form);
		configPanel =
			(RapportageConfiguratiePanel<R>) ReflectionUtil.invokeConstructor(getDocumentTemplate()
				.getConfiguratiePanel(), "configPanel", this);
		form.add((Panel) configPanel);
		createComponents();
	}

	public IDatabaseSelectionComponent<R, S, ZF> getSelection()
	{
		return selection;
	}

	private DocumentTemplate getDocumentTemplate()
	{
		return templateModel.getObject();
	}

	@Override
	protected void fillBottomRow(BottomRowPanel panel)
	{
		panel.addButton(new OpslaanButton(panel, form)
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onSubmit()
			{
				MultiEntityRapportageGenereerLink<R, S, ZF> ret =
					new MultiEntityRapportageGenereerLink<R, S, ZF>("link", templateModel)
					{
						private static final long serialVersionUID = 1L;

						@Override
						protected void updateDatamap(RapportageJobDataMap<R, S> datamap)
						{
							datamap.setConfiguration(configPanel.getConfiguratie());
						}
					};
				ret.setDahClass(selection.getDahClass());
				ret.setSelectionComponent(selection);
				ret.setReturnPage(returnPage);
				ret.setEindType(eindType);
				ret.onClick();
			}
		});
		panel.addButton(new AnnulerenButton(panel, returnPage));
	}

	@Override
	protected void onDetach()
	{
		super.onDetach();
		ComponentUtil.detachQuietly(templateModel);
		ComponentUtil.detachQuietly(returnPage);
		ComponentUtil.detachQuietly(selection);
	}
}
