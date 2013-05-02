/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.krd.web.pages.medewerker;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.dao.BatchDataAccessHelper;
import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.modelsv2.DefaultModelManager;
import nl.topicus.cobra.modelsv2.IChangeRecordingModel;
import nl.topicus.cobra.modelsv2.ModelFactory;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.web.components.panels.bottomrow.AnnulerenButton;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.cobra.web.components.panels.bottomrow.OpslaanButton;
import nl.topicus.cobra.web.components.panels.bottomrow.VerwijderButton;
import nl.topicus.cobra.web.security.RequiredSecurityCheck;
import nl.topicus.eduarte.app.security.checks.NietOverledenSecurityCheck;
import nl.topicus.eduarte.dao.helpers.KenmerkDataAccessHelper;
import nl.topicus.eduarte.entities.kenmerk.MedewerkerKenmerk;
import nl.topicus.eduarte.krd.principals.medewerker.MedewerkerKenmerkenWrite;
import nl.topicus.eduarte.krd.web.components.panels.EditKenmerkPanel;
import nl.topicus.eduarte.web.components.menu.MedewerkerMenuItem;
import nl.topicus.eduarte.web.pages.IModuleEditPage;
import nl.topicus.eduarte.web.pages.medewerker.AbstractMedewerkerPage;
import nl.topicus.eduarte.web.pages.medewerker.MedewerkerKenmerkenPage;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.spring.injection.annot.SpringBean;

@PageInfo(title = "Kenmerk bewerken", menu = {"Medewerker > [medewerker] > Verbintenis > Kenmerken > [kenmerk] > Bewerken"})
@InPrincipal(MedewerkerKenmerkenWrite.class)
@RequiredSecurityCheck(NietOverledenSecurityCheck.class)
public class MedewerkerKenmerkEditPage extends AbstractMedewerkerPage implements
		IModuleEditPage<MedewerkerKenmerk>
{
	private static final long serialVersionUID = 1L;

	private Form<MedewerkerKenmerk> form;

	@SpringBean
	private KenmerkDataAccessHelper helper;

	private AbstractMedewerkerPage returnToPage;

	public MedewerkerKenmerkEditPage(MedewerkerKenmerk kenmerk,
			final AbstractMedewerkerPage returnToPage)
	{
		super(MedewerkerMenuItem.Kenmerken, returnToPage.getContextMedewerker());
		this.returnToPage = returnToPage;

		add(form =
			new Form<MedewerkerKenmerk>("form", ModelFactory.getCompoundChangeRecordingModel(
				kenmerk, new DefaultModelManager(MedewerkerKenmerk.class))));
		form.add(new EditKenmerkPanel<MedewerkerKenmerk>("panel", form.getModel(), form));
		add(new Label("titel", kenmerk.isSaved() ? "Kenmerk bewerken" : "Nieuw kenmerk"));
		createComponents();
	}

	public IChangeRecordingModel<MedewerkerKenmerk> getChangeRecordingModel()
	{
		return (IChangeRecordingModel<MedewerkerKenmerk>) form.getModel();
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
				MedewerkerKenmerk kenmerk = getChangeRecordingModel().getObject();
				if (!kenmerk.isSaved())
				{
					kenmerk.getMedewerker().getKenmerken().add(kenmerk);
				}
				getChangeRecordingModel().saveObject();
				DataAccessRegistry.getHelper(BatchDataAccessHelper.class).batchExecute();
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
				IChangeRecordingModel<MedewerkerKenmerk> model = getChangeRecordingModel();
				model.deleteObject();
				helper.batchExecute();
				getContextMedewerker().refresh();
				setResponsePage(new MedewerkerKenmerkenPage(getContextMedewerker()));
			}

			@Override
			public boolean isVisible()
			{
				return getMedewerkerKenmerk().isSaved();
			}
		});
	}

	private MedewerkerKenmerk getMedewerkerKenmerk()
	{
		return form.getModelObject();
	}
}