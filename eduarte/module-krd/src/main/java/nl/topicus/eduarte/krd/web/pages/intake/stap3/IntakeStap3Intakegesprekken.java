package nl.topicus.eduarte.krd.web.pages.intake.stap3;

import java.util.ArrayList;
import java.util.List;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.dataproviders.ListModelDataProvider;
import nl.topicus.cobra.modelsv2.ReadOnlyListPropertyModel;
import nl.topicus.cobra.util.ComponentUtil;
import nl.topicus.cobra.web.components.datapanel.CollapsableRowFactoryDecorator;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanel;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanelAjaxFormSubmittingRowFactory;
import nl.topicus.cobra.web.components.panels.bottomrow.AbstractBottomRowButton;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.cobra.web.components.panels.bottomrow.ButtonAlignment;
import nl.topicus.cobra.web.components.panels.bottomrow.VerwijderButton;
import nl.topicus.cobra.web.components.panels.bottomrow.VolgendeButton;
import nl.topicus.cobra.web.components.panels.bottomrow.VorigeButton;
import nl.topicus.cobra.web.components.shortcut.CobraKeyAction;
import nl.topicus.eduarte.app.security.actions.Instelling;
import nl.topicus.eduarte.entities.inschrijving.Intakegesprek;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis.VerbintenisStatus;
import nl.topicus.eduarte.entities.organisatie.InstellingEntiteit;
import nl.topicus.eduarte.krd.web.components.panels.intake.IntakegesprekEditPanel;
import nl.topicus.eduarte.krd.web.pages.beheer.bron.BronValidatingFormComponent;
import nl.topicus.eduarte.krd.web.pages.intake.IntakeWizardModel;
import nl.topicus.eduarte.krd.web.pages.intake.IntakeWizardPage;
import nl.topicus.eduarte.krd.web.pages.intake.stap2.IntakeStap2Achtergrond;
import nl.topicus.eduarte.krd.web.pages.intake.stap4.IntakeStap4Verbintenis;
import nl.topicus.eduarte.providers.VerbintenisProvider;
import nl.topicus.eduarte.util.OrganisatieEenheidLocatieUtil;
import nl.topicus.eduarte.web.components.datapanel.HighlightVerbintenisRowFactoryDecorator;
import nl.topicus.eduarte.web.components.panels.EduArteDataPanel;
import nl.topicus.eduarte.web.components.panels.datapanel.table.IntakegesprekTable;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.SubmitLink;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.LoadableDetachableModel;

@PageInfo(title = "Intake stap 3", menu = {"Deelnemer > intake"})
public class IntakeStap3Intakegesprekken extends IntakeWizardPage
{
	private static final long serialVersionUID = 1L;

	private ReadOnlyListPropertyModel<VerbintenisProvider> intakeModel;

	private IntakegesprekEditPanel detailPanel;

	private CustomDataPanel<VerbintenisProvider> datapanel;

	private Form<Void> form;

	private ListModel items;

	public IntakeStap3Intakegesprekken(IntakeWizardModel model)
	{
		setWizard(model);

		items = new ListModel();
		intakeModel = new ReadOnlyListPropertyModel<VerbintenisProvider>(items)
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void setObject(VerbintenisProvider object)
			{
				if (object instanceof Verbintenis)
				{
					Verbintenis selectedVerbintenis = (Verbintenis) object;
					if (selectedVerbintenis.getIntakegesprekken().size() == 1)
						object = selectedVerbintenis.getIntakegesprekken().get(0);
				}
				super.setObject(object);
			}
		};
		selectEersteGesprek();

		form = new Form<Void>("gesprekForm");
		add(form);

		CollapsableRowFactoryDecorator<VerbintenisProvider> rowFactory =
			new CollapsableRowFactoryDecorator<VerbintenisProvider>(
				new HighlightVerbintenisRowFactoryDecorator<VerbintenisProvider>(
					new CustomDataPanelAjaxFormSubmittingRowFactory<VerbintenisProvider>(form,
						intakeModel)
					{
						private static final long serialVersionUID = 1L;

						@Override
						protected void onClick(AjaxRequestTarget target,
								Item<VerbintenisProvider> item)
						{
							target.addComponent(datapanel);
							target.addComponent(detailPanel);
							refreshBottomRow(target);
						}

						@Override
						protected void onError(AjaxRequestTarget target)
						{
							refreshFeedback(target);
						}
					}));

		datapanel =
			new EduArteDataPanel<VerbintenisProvider>("intakes",
				new ListModelDataProvider<VerbintenisProvider>(items),
				new IntakegesprekTable<VerbintenisProvider>(rowFactory, false));
		datapanel.setSelecteerKolommenButtonVisible(false);
		datapanel.setGroeperenButtonVisible(false);
		datapanel.setRowFactory(rowFactory);
		add(datapanel);

		detailPanel =
			new IntakegesprekEditPanel("gesprekDetails", new AbstractReadOnlyModel<Intakegesprek>()
			{
				private static final long serialVersionUID = 1L;

				@Override
				public Intakegesprek getObject()
				{
					Object ret = intakeModel.getObject();
					return ret instanceof Verbintenis ? null : (Intakegesprek) ret;
				}
			})
			{
				private static final long serialVersionUID = 1L;

				@Override
				protected boolean getIsIntakeScherm()
				{
					return true;
				}
			};
		detailPanel.setOutputMarkupPlaceholderTag(true);
		detailPanel.setOutputMarkupId(true);
		form.add(detailPanel);
		form.add(new BronValidatingFormComponent("bronvalidator", this, form)
			.setRenderBodyOnly(true));

		createComponents();
	}

	@Override
	protected void fillBottomRow(BottomRowPanel panel)
	{
		voegVorigeKnopToe(panel);
		voegVolgendeKnopToe(panel);
		voegVoltooienKnopToe(panel, form);
		voegAnnulerenKnopToe(panel);
		voegVerwijderKnopToe(panel);
		voegToevoegenKnopToe(panel);
	}

	private void voegVolgendeKnopToe(BottomRowPanel panel)
	{
		panel.addButton(new VolgendeButton(panel, form)
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onVolgende()
			{
				setResponsePage(new IntakeStap4Verbintenis(getWizard()));
			}
		});
	}

	private void voegVorigeKnopToe(BottomRowPanel panel)
	{
		panel.addButton(new VorigeButton(panel, form)
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onVorige()
			{
				setResponsePage(new IntakeStap2Achtergrond(getWizard()));
			}
		});
	}

	private void voegToevoegenKnopToe(BottomRowPanel panel)
	{
		AbstractBottomRowButton toevoegen =
			new AbstractBottomRowButton(panel, "Nieuw gesprek", CobraKeyAction.TOEVOEGEN,
				ButtonAlignment.LEFT)
			{
				private static final long serialVersionUID = 1L;

				@Override
				protected WebMarkupContainer getLink(String linkId)
				{
					return new SubmitLink(linkId, form)
					{
						private static final long serialVersionUID = 1L;

						@Override
						public void onSubmit()
						{
							Verbintenis intake;
							if (intakeModel.getObject() instanceof Verbintenis)
								intake = (Verbintenis) intakeModel.getObject();
							else
								intake = getWizard().createDefaultVerbintenis();

							Intakegesprek gesprek = getWizard().createDefaultIntakegesprek(intake);
							intakeModel.detach();
							intakeModel.setObject(gesprek);
						}
					};
				}
			};
		panel.addButton(toevoegen);
	}

	private void voegVerwijderKnopToe(BottomRowPanel panel)
	{
		panel.addButton(new VerwijderButton(panel)
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onClick()
			{
				Intakegesprek curSelectedGesprek = (Intakegesprek) intakeModel.getObject();
				Verbintenis intake = curSelectedGesprek.getVerbintenis();

				if (intake.isSaved())
					intake.getIntakegesprekken().remove(curSelectedGesprek);
				else
					getWizard().getDeelnemer().getVerbintenissen().remove(intake);
				items.detach();
				selectEersteGesprek();
			}

			@Override
			public boolean isVisible()
			{
				InstellingEntiteit curEntiteit = (InstellingEntiteit) intakeModel.getObject();
				if (curEntiteit instanceof Intakegesprek
					&& !VerbintenisStatus.Intake.equals(((Intakegesprek) curEntiteit)
						.getVerbintenis().getStatus()))
					return false;

				return curEntiteit != null && !curEntiteit.isSaved();
			}
		});
	}

	private void selectEersteGesprek()
	{
		List<VerbintenisProvider> availableItems = items.getObject();
		if (!availableItems.isEmpty())
			intakeModel.setObject(availableItems.get(0));
	}

	@Override
	public void detachModels()
	{
		super.detachModels();
		ComponentUtil.detachFields(this);
	}

	public class ListModel extends LoadableDetachableModel<List<VerbintenisProvider>>
	{
		private static final long serialVersionUID = 1L;

		@Override
		protected List<VerbintenisProvider> load()
		{
			List<VerbintenisProvider> ret = new ArrayList<VerbintenisProvider>();
			boolean instellingRechten =
				getSecurityCheck().isActionAuthorized(getAction(Instelling.class));
			for (Verbintenis intake : getWizard().getIntakes())
			{
				List<Intakegesprek> gesprekken = intake.getIntakegesprekken();
				ret.add(intake);
				if (!instellingRechten)
					gesprekken =
						OrganisatieEenheidLocatieUtil.filterOrganisatieEenheidLocatie(gesprekken,
							getIngelogdeMedewerker().getOrganisatieMedewerkers());
				ret.addAll(gesprekken);
			}
			return ret;
		}
	}

	@Override
	protected int getStapNummer()
	{
		return 3;
	}

	@Override
	protected String getStapTitel()
	{
		return "Intakegesprekken";
	}
}
