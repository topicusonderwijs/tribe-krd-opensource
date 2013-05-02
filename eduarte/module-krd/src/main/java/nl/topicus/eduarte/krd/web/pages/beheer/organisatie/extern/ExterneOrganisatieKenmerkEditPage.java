/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.krd.web.pages.beheer.organisatie.extern;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.modelsv2.DefaultModelManager;
import nl.topicus.cobra.modelsv2.IChangeRecordingModel;
import nl.topicus.cobra.modelsv2.ModelFactory;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.web.components.panels.bottomrow.AnnulerenButton;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.cobra.web.components.panels.bottomrow.OpslaanButton;
import nl.topicus.cobra.web.components.panels.bottomrow.VerwijderButton;
import nl.topicus.eduarte.dao.helpers.KenmerkDataAccessHelper;
import nl.topicus.eduarte.entities.kenmerk.ExterneOrganisatieKenmerk;
import nl.topicus.eduarte.krd.principals.organisatie.ExterneOrganisatieKenmerkenWrite;
import nl.topicus.eduarte.krd.web.components.panels.EditKenmerkPanel;
import nl.topicus.eduarte.web.components.menu.ExterneOrganisatieMenuItem;
import nl.topicus.eduarte.web.pages.IModuleEditPage;
import nl.topicus.eduarte.web.pages.beheer.organisatie.AbstractExterneOrganisatiePage;
import nl.topicus.eduarte.web.pages.beheer.organisatie.ExterneOrganisatieKenmerkenPage;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.spring.injection.annot.SpringBean;

@PageInfo(title = "Kenmerk bewerken", menu = {"Relatie > Externe organisaties > [Externe organisatie] > Kenmerken > [kenmerk] > Bewerken"})
@InPrincipal(ExterneOrganisatieKenmerkenWrite.class)
public class ExterneOrganisatieKenmerkEditPage extends AbstractExterneOrganisatiePage implements
		IModuleEditPage<ExterneOrganisatieKenmerk>
{
	private static final long serialVersionUID = 1L;

	private Form<ExterneOrganisatieKenmerk> form;

	@SpringBean
	private KenmerkDataAccessHelper helper;

	private AbstractExterneOrganisatiePage returnToPage;

	public ExterneOrganisatieKenmerkEditPage(ExterneOrganisatieKenmerk kenmerk,
			final AbstractExterneOrganisatiePage returnToPage)
	{
		super(ExterneOrganisatieMenuItem.Kenmerken, returnToPage.getContextExterneOrganisatie());
		this.returnToPage = returnToPage;

		add(form =
			new Form<ExterneOrganisatieKenmerk>("form", ModelFactory
				.getCompoundChangeRecordingModel(kenmerk, new DefaultModelManager(
					ExterneOrganisatieKenmerk.class))));
		form.add(new EditKenmerkPanel<ExterneOrganisatieKenmerk>("panel", form.getModel(), form));
		add(new Label("titel", kenmerk.isSaved() ? "Kenmerk bewerken" : "Nieuw kenmerk"));
		createComponents();
	}

	public IChangeRecordingModel<ExterneOrganisatieKenmerk> getChangeRecordingModel()
	{
		return (IChangeRecordingModel<ExterneOrganisatieKenmerk>) form.getModel();
	}

	@Override
	protected void fillBottomRow(BottomRowPanel panel)
	{

		panel.addButton(new OpslaanButton(panel, form)
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void onSubmit()
			{
				ExterneOrganisatieKenmerk kenmerk = getExterneOrganisatieKenmerk();
				if (!kenmerk.isSaved())
				{
					kenmerk.getExterneOrganisatie().getKenmerken().add(kenmerk);
				}
				getChangeRecordingModel().saveObject();
				helper.batchExecute();
				setResponsePage(returnToPage);
			}

		});
		panel.addButton(new AnnulerenButton(panel, returnToPage));

		panel.addButton(new VerwijderButton(panel)
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onClick()
			{
				IChangeRecordingModel<ExterneOrganisatieKenmerk> model = getChangeRecordingModel();
				model.deleteObject();
				helper.batchExecute();
				getContextExterneOrganisatie().refresh();
				setResponsePage(new ExterneOrganisatieKenmerkenPage(getContextExterneOrganisatie()));
			}

			@Override
			public boolean isVisible()
			{
				return getExterneOrganisatieKenmerk().isSaved();
			}
		});
	}

	private ExterneOrganisatieKenmerk getExterneOrganisatieKenmerk()
	{
		return form.getModelObject();
	}
}