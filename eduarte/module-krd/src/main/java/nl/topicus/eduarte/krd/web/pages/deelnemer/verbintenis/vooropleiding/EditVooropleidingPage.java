/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.krd.web.pages.deelnemer.verbintenis.vooropleiding;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.dao.BatchDataAccessHelper;
import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.modelsv2.DefaultModelManager;
import nl.topicus.cobra.modelsv2.IChangeRecordingModel;
import nl.topicus.cobra.modelsv2.ModelFactory;
import nl.topicus.cobra.modelsv2.ModelManager;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.web.components.panels.bottomrow.AnnulerenButton;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.cobra.web.components.panels.bottomrow.ButtonAlignment;
import nl.topicus.cobra.web.components.panels.bottomrow.OpslaanButton;
import nl.topicus.cobra.web.security.RequiredSecurityCheck;
import nl.topicus.eduarte.app.security.checks.NietOverledenSecurityCheck;
import nl.topicus.eduarte.entities.hogeronderwijs.VooropleidingVakResultaat;
import nl.topicus.eduarte.entities.inschrijving.Vooropleiding;
import nl.topicus.eduarte.entities.inschrijving.VooropleidingVak;
import nl.topicus.eduarte.entities.vrijevelden.VooropleidingVrijVeld;
import nl.topicus.eduarte.entities.vrijevelden.VrijVeldOptieKeuze;
import nl.topicus.eduarte.krd.principals.deelnemer.verbintenis.DeelnemerVooropleidingenWrite;
import nl.topicus.eduarte.krd.web.components.panels.verbintenis.EditVooropleidingPanel;
import nl.topicus.eduarte.krd.web.pages.deelnemer.verbintenis.vooropleiding.buttons.VooropleidingVerwijderenButton;
import nl.topicus.eduarte.web.components.menu.DeelnemerMenuItem;
import nl.topicus.eduarte.web.pages.IModuleEditPage;
import nl.topicus.eduarte.web.pages.SecurePage;
import nl.topicus.eduarte.web.pages.deelnemer.AbstractDeelnemerPage;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.IModel;

/**
 * @author idserda
 */
@PageInfo(title = "Vooropleiding bewerken", menu = {"Deelnemer > [deelnemer] > Personalia > Vooropleidingen > [vooropleiding] > Bewerken"})
@InPrincipal(DeelnemerVooropleidingenWrite.class)
@RequiredSecurityCheck(NietOverledenSecurityCheck.class)
public class EditVooropleidingPage extends AbstractDeelnemerPage implements
		IModuleEditPage<Vooropleiding>
{
	private static final long serialVersionUID = 1L;

	private Form<Void> form;

	private SecurePage returnToPage;

	private ModelManager manager;

	public EditVooropleidingPage(Vooropleiding vooropleiding,
			final AbstractDeelnemerPage returnToPage)
	{
		super(DeelnemerMenuItem.Vooropleidingen, returnToPage.getContextDeelnemer(), returnToPage
			.getContextVerbintenis());

		this.manager =
			new DefaultModelManager(VooropleidingVakResultaat.class, VooropleidingVak.class,
				VrijVeldOptieKeuze.class, VooropleidingVrijVeld.class, Vooropleiding.class);

		setDefaultModel(ModelFactory.getCompoundChangeRecordingModel(vooropleiding, manager));

		this.returnToPage = returnToPage;
		add(form = new Form<Void>("vooropleidingForm"));
		createVelden();

		createComponents();
	}

	public void createVelden()
	{
		add(new Label("titel", (getVooropleiding().isSaved() ? "Vooropleiding bewerken"
			: "Nieuwe vooropleiding")));
		form.add(new EditVooropleidingPanel("vooropleidingPanel", getVooropleidingModel(), false,
			manager));

	}

	private Vooropleiding getVooropleiding()
	{
		return (Vooropleiding) getDefaultModelObject();
	}

	@SuppressWarnings("unchecked")
	public IModel<Vooropleiding> getVooropleidingModel()
	{
		return (IModel<Vooropleiding>) getDefaultModel();
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
				if (!getVooropleiding().isSaved())
				{
					getVooropleiding().getDeelnemer().getVooropleidingen().add(getVooropleiding());
				}
				if (!getVooropleiding().isAantalJarenZelfInvullen())
				{
					Integer aantalJarenOnderwijs = getVooropleiding().berekenAantalJarenOnderwijs();
					if (aantalJarenOnderwijs != null)
					{
						getVooropleiding().setAantalJarenOnderwijs(aantalJarenOnderwijs);
					}
				}
				((IChangeRecordingModel< ? >) EditVooropleidingPage.this.getDefaultModel())
					.saveObject();
				DataAccessRegistry.getHelper(BatchDataAccessHelper.class).batchExecute();
				setResponsePage(returnToPage);
			}

		});
		panel.addButton(new AnnulerenButton(panel, returnToPage));
		VooropleidingVerwijderenButton verwijderButton =
			new VooropleidingVerwijderenButton(panel, getVooropleidingModel(), returnToPage);
		verwijderButton.setOutputMarkupPlaceholderTag(true);
		panel.addButton(verwijderButton.setAlignment(ButtonAlignment.LEFT));
	}

}