package nl.topicus.eduarte.krd.web.pages.intake.stap4;

import java.util.ArrayList;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.web.components.choice.AbstractAjaxDropDownChoice;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.cobra.web.components.panels.bottomrow.VorigeButton;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis.VerbintenisStatus;
import nl.topicus.eduarte.entities.vrijevelden.VrijVeldCategorie;
import nl.topicus.eduarte.krd.web.components.panels.VrijVeldEntiteitEditPanel;
import nl.topicus.eduarte.krd.web.pages.deelnemer.verbintenis.BlokkadedatumValidatorMode;
import nl.topicus.eduarte.krd.web.pages.deelnemer.verbintenis.BlokkadedatumVerbintenisValidatingFormComponent;
import nl.topicus.eduarte.krd.web.pages.intake.IntakeWizardModel;
import nl.topicus.eduarte.krd.web.pages.intake.IntakeWizardPage;
import nl.topicus.eduarte.krd.web.pages.intake.VerbintenisKeuze;
import nl.topicus.eduarte.krd.web.pages.intake.VerbintenisUndo;
import nl.topicus.eduarte.krd.web.pages.intake.stap2.IntakeStap2Achtergrond;
import nl.topicus.eduarte.krd.web.pages.intake.stap3.IntakeStap3Intakegesprekken;
import nl.topicus.eduarte.providers.VerbintenisProvider;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.PropertyModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@PageInfo(title = "Intake stap 4", menu = {"Deelnemer > intake"})
public class IntakeStap4Verbintenis extends IntakeWizardPage implements VerbintenisProvider
{
	private static final long serialVersionUID = 1L;

	private static final Logger log = LoggerFactory.getLogger(IntakeStap4Verbintenis.class);

	private IModel<Verbintenis> verbintenisModel;

	private Form<Void> form;

	private VerbintenisPanel verbintenisPanel;

	public IntakeStap4Verbintenis(IntakeWizardModel model)
	{
		setDefaultModel(model);
		createComponents();
	}

	@Override
	protected void createComponents()
	{
		add(form = new Form<Void>("verbintenisForm"));
		form.setOutputMarkupId(true);

		verbintenisModel = new LoadableDetachableModel<Verbintenis>()
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected Verbintenis load()
			{
				VerbintenisKeuze keuze = getWizard().getStap4keuze();
				return keuze == null ? null : keuze.getVerbintenis(getWizard());
			}
		};

		ArrayList<VerbintenisKeuze> dropDownOpties = new ArrayList<VerbintenisKeuze>();
		for (Verbintenis curIntake : getWizard().getIntakes())
		{
			dropDownOpties.add(new VerbintenisKeuze(curIntake));
		}
		dropDownOpties.add(new VerbintenisKeuze());

		form.add(new AbstractAjaxDropDownChoice<VerbintenisKeuze>("selectVerbintenisStap4",
			new PropertyModel<VerbintenisKeuze>(this, "wizard.stap4keuze"), dropDownOpties,
			new IChoiceRenderer<VerbintenisKeuze>()
			{

				private static final long serialVersionUID = 1L;

				@Override
				public Object getDisplayValue(VerbintenisKeuze object)
				{
					VerbintenisKeuze verbintenis = object;
					return verbintenis.toString(getWizard());
				}

				@Override
				public String getIdValue(VerbintenisKeuze object, int index)
				{
					VerbintenisKeuze verbintenis = object;
					return verbintenis.getIdValue();
				}
			})
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onUpdate(AjaxRequestTarget target, VerbintenisKeuze newSelection)
			{
				VerbintenisUndo vorigeUndo = getWizard().getStap4Undo();
				if (vorigeUndo != null)
					vorigeUndo.undo(getWizard());

				VerbintenisUndo nieuweUndo;
				VerbintenisKeuze keuze = newSelection;
				if (keuze == null)
					nieuweUndo = null;
				else
				{
					if (keuze.isIntakeVerbintenis())
						nieuweUndo = new VerbintenisUndo(getWizard(), keuze);
					else
					{
						Verbintenis nieuweVerbintenis = getWizard().createDefaultVerbintenis();
						nieuweUndo = new VerbintenisUndo(nieuweVerbintenis);
						getWizard()
							.setStap4idVanNieuweVerbintenis(nieuweVerbintenis.getCurrentId());
					}
					keuze.getVerbintenis(getWizard()).setStatus(VerbintenisStatus.Voorlopig);
				}

				getWizard().setStap4Undo(nieuweUndo);
				// make sure the model is refreshed
				verbintenisModel.detach();
				target.addComponent(form);
			}

			@Override
			protected void onError(AjaxRequestTarget target, RuntimeException e)
			{
				log.error(e.getMessage(), e);
			}
		}.setNullValid(true));

		form.add(new BlokkadedatumVerbintenisValidatingFormComponent("blokkadedatumValidator",
			this, BlokkadedatumValidatorMode.Aanmaken));

		verbintenisPanel =
			new VerbintenisPanel("verbintenisStap4", new CompoundPropertyModel<Verbintenis>(
				verbintenisModel), this, form)
			{
				private static final long serialVersionUID = 1L;

				@Override
				public boolean isVisible()
				{
					return getDefaultModelObject() != null;
				}

			};
		verbintenisPanel.setOutputMarkupId(true).setOutputMarkupPlaceholderTag(true);
		form.add(verbintenisPanel);

		voegVrijVeldVeldenToeAanForm();

		super.createComponents();
	}

	private void voegVrijVeldVeldenToeAanForm()
	{
		VrijVeldEntiteitEditPanel<Verbintenis> VVEEPanel =
			new VrijVeldEntiteitEditPanel<Verbintenis>("vrijVelden", verbintenisModel)
			{
				private static final long serialVersionUID = 1L;

				@Override
				public boolean isVisible()
				{
					return getModelObject() != null;
				}
			};
		VVEEPanel.getVrijVeldZoekFilter().setCategorie(VrijVeldCategorie.VERBINTENIS);
		VVEEPanel.getVrijVeldZoekFilter().setIntakeScherm(true);
		form.add(VVEEPanel);
	}

	private void voegVorigeKnopToe(BottomRowPanel panel)
	{
		panel.addButton(new VorigeButton(panel, form)
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onVorige()
			{
				if (getWizard().getDefaultOrganisatieEenheid().isIntakeWizardStap3Overslaan())
					setResponsePage(new IntakeStap2Achtergrond(getWizard()));
				else
					setResponsePage(new IntakeStap3Intakegesprekken(getWizard()));
			}
		});
	}

	@Override
	protected void fillBottomRow(BottomRowPanel panel)
	{
		voegVorigeKnopToe(panel);
		voegVoltooienKnopToe(panel, form);
		voegAnnulerenKnopToe(panel);
	}

	@Override
	protected int getStapNummer()
	{
		return 4;
	}

	@Override
	protected String getStapTitel()
	{
		return "Verbintenis";
	}

	@Override
	protected void onDetach()
	{
		super.onDetach();
		verbintenisModel.detach();
	}

	@Override
	public Verbintenis getVerbintenis()
	{
		return verbintenisModel.getObject();
	}
}
