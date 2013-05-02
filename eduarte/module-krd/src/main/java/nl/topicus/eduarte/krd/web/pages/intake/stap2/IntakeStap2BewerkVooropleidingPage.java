/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.krd.web.pages.intake.stap2;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.web.components.panels.bottomrow.AbstractBottomRowButton;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.cobra.web.components.panels.bottomrow.ButtonAlignment;
import nl.topicus.cobra.web.components.panels.bottomrow.OpslaanButton;
import nl.topicus.eduarte.entities.inschrijving.Vooropleiding;
import nl.topicus.eduarte.entities.vrijevelden.VrijVeldCategorie;
import nl.topicus.eduarte.krd.web.components.panels.VrijVeldEntiteitEditPanel;
import nl.topicus.eduarte.krd.web.components.panels.verbintenis.EditVooropleidingPanel;
import nl.topicus.eduarte.krd.web.pages.intake.IntakeWizardModel;
import nl.topicus.eduarte.krd.web.pages.intake.IntakeWizardPage;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

/**
 * @author hop
 */
@PageInfo(title = "Intake stap 2 vooropleiding", menu = {"Deelnemer > intake"})
public class IntakeStap2BewerkVooropleidingPage extends IntakeWizardPage
{
	private static final long serialVersionUID = 1L;

	private EditVooropleidingPanel panel;

	private boolean isNieuw;

	private Form<Void> form;

	private IModel<Vooropleiding> vooropleidingModel;

	public IntakeStap2BewerkVooropleidingPage(IntakeWizardModel wizard,
			Vooropleiding vooropleiding, boolean isNieuw)
	{
		setWizard(wizard);
		vooropleidingModel = wizard.getModel(vooropleiding);
		this.isNieuw = isNieuw;

		createComponents();
	}

	@Override
	protected void createComponents()
	{
		form = new Form<Void>("vooropleidingForm")
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onSubmit()
			{
				setResponsePage(new IntakeStap2Achtergrond(getWizard()));
			}
		};
		super.createComponents();
		createVelden();

		add(form);
	}

	public void createVelden()
	{
		createTitel();

		panel =
			new EditVooropleidingPanel("vooropleidingPanel",
				new CompoundPropertyModel<Vooropleiding>(vooropleidingModel), false, getWizard()
					.getManager());
		form.add(panel);

		VrijVeldEntiteitEditPanel<Vooropleiding> VVEEPanel =
			new VrijVeldEntiteitEditPanel<Vooropleiding>("vrijVelden", vooropleidingModel);
		VVEEPanel.getVrijVeldZoekFilter().setIntakeScherm(true);
		VVEEPanel.getVrijVeldZoekFilter().setCategorie(VrijVeldCategorie.VOOROPLEIDING);
		form.add(VVEEPanel);
	}

	private void createTitel()
	{
		add(new Label("titel", (isNieuw ? "Vooropleiding bewerken" : "Nieuwe vooropleiding")));
	}

	@Override
	protected void fillBottomRow(BottomRowPanel bottom)
	{
		bottom.addButton(new OpslaanButton(bottom, form));
		bottom.addButton(new AbstractBottomRowButton(bottom, "Annuleren", null,
			ButtonAlignment.RIGHT)
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected WebMarkupContainer getLink(String linkId)
			{
				return new Link<Void>(linkId)
				{
					private static final long serialVersionUID = 1L;

					@Override
					public void onClick()
					{
						IntakeWizardModel wizard = getWizard();
						if (isNieuw)
						{
							Vooropleiding vooropleiding = getVooropleiding();
							// model op null zetten voorkomt probleem bij dubbelklik
							// (mantis 43834)
							vooropleidingModel = new Model<Vooropleiding>(null);
							wizard.removeVooropleiding(vooropleiding);
						}
						setResponsePage(new IntakeStap2Achtergrond(wizard));
					}
				};
			}
		});
	}

	private Vooropleiding getVooropleiding()
	{
		return vooropleidingModel.getObject();
	}

	@Override
	protected int getStapNummer()
	{
		return 2;
	}

	@Override
	protected String getStapTitel()
	{
		return "Vooropleiding";
	}
}