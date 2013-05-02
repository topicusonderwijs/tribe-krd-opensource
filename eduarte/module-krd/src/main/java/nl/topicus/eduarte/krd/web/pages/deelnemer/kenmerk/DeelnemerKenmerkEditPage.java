/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.krd.web.pages.deelnemer.kenmerk;

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
import nl.topicus.eduarte.entities.kenmerk.DeelnemerKenmerk;
import nl.topicus.eduarte.krd.principals.deelnemer.verbintenis.DeelnemerKenmerkenWrite;
import nl.topicus.eduarte.krd.web.components.panels.EditKenmerkPanel;
import nl.topicus.eduarte.web.components.menu.DeelnemerMenuItem;
import nl.topicus.eduarte.web.pages.IModuleEditPage;
import nl.topicus.eduarte.web.pages.deelnemer.AbstractDeelnemerPage;
import nl.topicus.eduarte.web.pages.deelnemer.kenmerk.DeelnemerKenmerkenPage;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.spring.injection.annot.SpringBean;

@PageInfo(title = "Kenmerk bewerken", menu = {"Deelnemer > [deelnemer] > Verbintenis > Kenmerken > [kenmerk] > Bewerken"})
@InPrincipal(DeelnemerKenmerkenWrite.class)
@RequiredSecurityCheck(NietOverledenSecurityCheck.class)
public class DeelnemerKenmerkEditPage extends AbstractDeelnemerPage implements
		IModuleEditPage<DeelnemerKenmerk>
{
	private static final long serialVersionUID = 1L;

	private Form<Void> form;

	@SpringBean
	private KenmerkDataAccessHelper helper;

	private AbstractDeelnemerPage returnToPage;

	public DeelnemerKenmerkEditPage(DeelnemerKenmerk kenmerk,
			final AbstractDeelnemerPage returnToPage)
	{
		super(DeelnemerMenuItem.Kenmerken, returnToPage.getContextDeelnemer(), returnToPage
			.getContextVerbintenis());
		this.returnToPage = returnToPage;
		setDefaultModel(ModelFactory.getCompoundChangeRecordingModel(kenmerk,
			new DefaultModelManager(DeelnemerKenmerk.class)));

		add(form = new Form<Void>("form"));
		form.add(new EditKenmerkPanel<DeelnemerKenmerk>("panel", getChangeRecordingModel(), form));
		add(new Label("titel", kenmerk.isSaved() ? "Kenmerk bewerken" : "Nieuw kenmerk"));
		createComponents();
	}

	@SuppressWarnings("unchecked")
	public IChangeRecordingModel<DeelnemerKenmerk> getChangeRecordingModel()
	{
		return (IChangeRecordingModel<DeelnemerKenmerk>) getDefaultModel();
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
				DeelnemerKenmerk kenmerk = getChangeRecordingModel().getObject();
				if (!kenmerk.isSaved())
				{
					kenmerk.getDeelnemer().getKenmerken().add(kenmerk);
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
				IChangeRecordingModel<DeelnemerKenmerk> model = getChangeRecordingModel();
				model.deleteObject();
				helper.batchExecute();
				getContextDeelnemer().refresh();
				setResponsePage(new DeelnemerKenmerkenPage(getContextDeelnemer()));
			}

			@Override
			public boolean isVisible()
			{
				return getDeelnemerKenmerk().isSaved();
			}
		});
	}

	private DeelnemerKenmerk getDeelnemerKenmerk()
	{
		return (DeelnemerKenmerk) getDefaultModelObject();
	}
}