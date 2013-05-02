package nl.topicus.eduarte.krd.web.components.panels.intake;

import java.util.Collections;
import java.util.Date;

import nl.topicus.cobra.comparators.MultiFieldComparator;
import nl.topicus.cobra.util.TimeUtil;
import nl.topicus.cobra.web.components.form.AutoFieldSet;
import nl.topicus.cobra.web.components.form.FieldProperties;
import nl.topicus.cobra.web.components.form.RenderMode;
import nl.topicus.cobra.web.components.form.modifier.ConstructorArgModifier;
import nl.topicus.cobra.web.components.form.modifier.EnableModifier;
import nl.topicus.cobra.web.components.form.modifier.PostProcessModifier;
import nl.topicus.cobra.web.components.form.modifier.ValidateModifier;
import nl.topicus.cobra.web.components.panels.TypedPanel;
import nl.topicus.cobra.web.components.text.DatumTijdField;
import nl.topicus.eduarte.entities.inschrijving.Intakegesprek;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.entities.inschrijving.Intakegesprek.IntakegesprekStatus;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis.VerbintenisStatus;
import nl.topicus.eduarte.entities.vrijevelden.VrijVeldCategorie;
import nl.topicus.eduarte.krd.web.components.panels.VrijVeldEntiteitEditPanel;
import nl.topicus.eduarte.web.components.autoform.EduArteAjaxRefreshModifier;
import nl.topicus.eduarte.web.components.autoform.OpleidingOrganisatieEenheidLocatieFieldModifier;
import nl.topicus.eduarte.zoekfilters.ExterneOrganisatieZoekFilter;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.ValidationError;
import org.apache.wicket.validation.validator.AbstractValidator;

public abstract class IntakegesprekEditPanel extends TypedPanel<Intakegesprek>
{
	private static final long serialVersionUID = 1L;

	public IntakegesprekEditPanel(String id, IModel<Intakegesprek> model)
	{
		super(id, model);

		createGesprekDetails("gesprekDetails", model);
		createVoorkeuren("voorkeuren", model);
		createVrijeVelden("vrijevelden", model);

		add(new FormComponent<Void>("submitter")
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void updateModel()
			{
				Intakegesprek gesprek = getGesprek();
				if (gesprek == null)
					return;

				Verbintenis intake = gesprek.getVerbintenis();
				if (!VerbintenisStatus.Intake.equals(intake.getStatus()))
					return;

				if (!intake.getIntakegesprekken().contains(gesprek))
					intake.getIntakegesprekken().add(gesprek);

				Collections.sort(intake.getIntakegesprekken(), new MultiFieldComparator(true,
					"datumTijd"));

				// update alleen als dit het meest recente gesprek is
				if (intake.getIntakegesprekken().indexOf(gesprek) != 0)
					return;

				intake.setOrganisatieEenheid(gesprek.getOrganisatieEenheid());
				if (gesprek.getGewensteOpleiding() != null)
					intake.setOpleiding(gesprek.getGewensteOpleiding());
				if (gesprek.getGewensteLocatie() != null)
					intake.setLocatie(gesprek.getGewensteLocatie());
			}
		});
	}

	protected abstract boolean getIsIntakeScherm();

	private void createGesprekDetails(String id, IModel<Intakegesprek> model)
	{
		final AutoFieldSet<Intakegesprek> gesprekDetails =
			new AutoFieldSet<Intakegesprek>(id, model);
		gesprekDetails.setPropertyNames("datumTijd", "intaker", "intakerOverig", "locatie",
			"kamer", "status", "uitkomst");
		gesprekDetails.setSortAccordingToPropertyNames(true);
		gesprekDetails.setRenderMode(RenderMode.EDIT);
		add(gesprekDetails);

		gesprekDetails.addFieldModifier(new EduArteAjaxRefreshModifier("status", "uitkomst",
			"datumTijd")
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onUpdate(AjaxRequestTarget target)
			{
				IntakegesprekStatus status = getGesprek().getStatus();
				if (!IntakegesprekStatus.Uitgevoerd.equals(status))
					getGesprek().setUitkomst(null);
			}
		});
		gesprekDetails.addFieldModifier(new EnableModifier("uitkomst",
			new AbstractReadOnlyModel<Boolean>()
			{
				private static final long serialVersionUID = 1L;

				@Override
				public Boolean getObject()
				{
					return IntakegesprekStatus.Uitgevoerd.equals(getGesprek().getStatus());
				}
			}));
		gesprekDetails.addFieldModifier(new PostProcessModifier("datumTijd")
		{
			private static final long serialVersionUID = 1L;

			@Override
			public <T> void postProcess(AutoFieldSet<T> fieldSet, Component field,
					FieldProperties<T, ? , ? > fieldProperties)
			{
				((DatumTijdField) field).setRequiredModel(new AbstractReadOnlyModel<Boolean>()
				{
					private static final long serialVersionUID = 1L;

					@Override
					public Boolean getObject()
					{
						IntakegesprekStatus status = getGesprek().getStatus();
						return IntakegesprekStatus.Uitgevoerd.equals(status)
							|| IntakegesprekStatus.Uitvoeren.equals(status);
					}
				});
			}
		});
	}

	private void createVoorkeuren(String id, IModel<Intakegesprek> model)
	{
		final AutoFieldSet<Intakegesprek> voorkeuren = new AutoFieldSet<Intakegesprek>(id, model);
		voorkeuren.setPropertyNames("organisatieEenheid", "gewensteLocatie", "gewensteOpleiding",
			"gewensteBegindatum", "gewensteEinddatum", "gewensteBPV", "opmerking");
		voorkeuren.setSortAccordingToPropertyNames(true);
		voorkeuren.setRenderMode(RenderMode.EDIT);
		add(voorkeuren);

		voorkeuren.addFieldModifier(new ValidateModifier(new AbstractValidator<Date>()
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onValidate(IValidatable<Date> validatable)
			{

				if (getGesprek() != null && getGesprek().getGewensteBegindatum() != null
					&& getGesprek().getGewensteOpleiding() != null
					&& getGesprek().getGewensteOpleiding().getDatumLaatsteInschrijving() != null)
				{
					if (validatable.getValue().after(
						getGesprek().getGewensteOpleiding().getDatumLaatsteInschrijving()))
					{
						ValidationError error =
							new ValidationError()
								.setMessage("Gewenste begindatum moet voor de datum laatste inschrijving van de opleiding ("
									+ TimeUtil.getInstance().formatDate(
										getGesprek().getGewensteOpleiding()
											.getDatumLaatsteInschrijving()) + ") liggen");
						validatable.error(error);
					}
				}
			}
		}, "gewensteBegindatum"));

		ExterneOrganisatieZoekFilter bpvZoekFilter = new ExterneOrganisatieZoekFilter();
		bpvZoekFilter.setBpvBedrijf(true);
		voorkeuren.addFieldModifier(new ConstructorArgModifier("gewensteBPV", bpvZoekFilter));

		OpleidingOrganisatieEenheidLocatieFieldModifier orgEhdLocatieModifier =
			new OpleidingOrganisatieEenheidLocatieFieldModifier();
		orgEhdLocatieModifier.setOrganisatieEenheidPropertyName("organisatieEenheid");
		orgEhdLocatieModifier.setLocatiePropertyName("gewensteLocatie");
		orgEhdLocatieModifier.setOpleidingPropertyName("gewensteOpleiding");
		voorkeuren.addFieldModifier(orgEhdLocatieModifier);

		voorkeuren.addFieldModifier(new EnableModifier(new AbstractReadOnlyModel<Boolean>()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public Boolean getObject()
			{
				return VerbintenisStatus.Intake.equals(getGesprek().getVerbintenis().getStatus());
			}
		}, "organisatieEenheid", "gewensteLocatie", "gewensteOpleiding"));
	}

	private void createVrijeVelden(String id, IModel<Intakegesprek> model)
	{
		VrijVeldEntiteitEditPanel<Intakegesprek> VVEEPanel =
			new VrijVeldEntiteitEditPanel<Intakegesprek>(id, model)
			{
				private static final long serialVersionUID = 1L;

				@Override
				public boolean isVisible()
				{
					return getGesprek() != null && super.isVisible();
				}
			};
		VVEEPanel.getVrijVeldZoekFilter().setCategorie(VrijVeldCategorie.INTAKE);
		VVEEPanel.getVrijVeldZoekFilter().setDossierScherm(!getIsIntakeScherm() ? true : null);
		VVEEPanel.getVrijVeldZoekFilter().setIntakeScherm(getIsIntakeScherm() ? true : null);
		add(VVEEPanel);
	}

	private Intakegesprek getGesprek()
	{
		return getModelObject();
	}

	@Override
	public boolean isVisible()
	{
		return getGesprek() != null && super.isVisible();
	}
}
