/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.krd.web.pages.groep;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.dao.helpers.DatabaseSelection;
import nl.topicus.cobra.dao.hibernate.HibernateSelection;
import nl.topicus.cobra.dao.hibernate.ProjectedSelection;
import nl.topicus.cobra.dataproviders.ListModelDataProvider;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.transformers.HoofdletterMode;
import nl.topicus.cobra.util.ComponentUtil;
import nl.topicus.cobra.util.TimeUtil;
import nl.topicus.cobra.web.behaviors.HoofdletterAjaxHandler;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanel;
import nl.topicus.cobra.web.components.datapanel.selection.ISelectionComponent;
import nl.topicus.cobra.web.components.form.AutoFieldSet;
import nl.topicus.cobra.web.components.form.RenderMode;
import nl.topicus.cobra.web.components.form.modifier.ValidateModifier;
import nl.topicus.cobra.web.components.panels.bottomrow.AnnulerenButton;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.cobra.web.components.panels.bottomrow.OpslaanButton;
import nl.topicus.cobra.web.components.text.DatumField;
import nl.topicus.cobra.web.validators.BegindatumVoorEinddatumValidator;
import nl.topicus.eduarte.app.security.checks.OrganisatieEenheidLocatieSecurityCheck;
import nl.topicus.eduarte.dao.helpers.GroepDataAccessHelper;
import nl.topicus.eduarte.entities.groep.Groep;
import nl.topicus.eduarte.entities.groep.GroepDocent;
import nl.topicus.eduarte.entities.groep.GroepMentor;
import nl.topicus.eduarte.entities.groep.Groepsdeelname;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.entities.personen.Deelnemer;
import nl.topicus.eduarte.entities.personen.Medewerker;
import nl.topicus.eduarte.entities.vrijevelden.VrijVeldCategorie;
import nl.topicus.eduarte.krd.principals.groep.GroepVerwijderen;
import nl.topicus.eduarte.krd.principals.groep.GroepWrite;
import nl.topicus.eduarte.krd.principals.groep.GroepsdeelnameVerwijderen;
import nl.topicus.eduarte.krd.principals.groep.GroepsdeelnameWrite;
import nl.topicus.eduarte.krd.web.components.panels.VrijVeldEntiteitEditPanel;
import nl.topicus.eduarte.web.components.autoform.OrganisatieEenheidLocatieFieldModifier;
import nl.topicus.eduarte.web.components.menu.GroepMenuItem;
import nl.topicus.eduarte.web.components.panels.EduArteDataPanel;
import nl.topicus.eduarte.web.components.panels.bottomrow.ProbeerTeVerwijderenButton;
import nl.topicus.eduarte.web.components.panels.datapanel.table.GroepDocentTable;
import nl.topicus.eduarte.web.components.panels.datapanel.table.GroepMentorTable;
import nl.topicus.eduarte.web.components.panels.datapanel.table.GroepsdeelnameTable;
import nl.topicus.eduarte.web.components.panels.filter.GroepsdeelnameZoekFilterPanel;
import nl.topicus.eduarte.web.pages.IModuleEditPage;
import nl.topicus.eduarte.web.pages.SecurePage;
import nl.topicus.eduarte.web.pages.groep.AbstractGroepPage;
import nl.topicus.eduarte.web.pages.groep.GroepZoekenPage;
import nl.topicus.eduarte.web.pages.shared.AbstractSelectieTarget;
import nl.topicus.eduarte.zoekfilters.GroepZoekFilter;
import nl.topicus.eduarte.zoekfilters.GroepsdeelnameZoekFilter;
import nl.topicus.eduarte.zoekfilters.MedewerkerZoekFilter;
import nl.topicus.eduarte.zoekfilters.OrganisatieEenheidLocatieAuthorizationContext;
import nl.topicus.eduarte.zoekfilters.VerbintenisZoekFilter;

import org.apache.wicket.Component;
import org.apache.wicket.Page;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.IFormSubmittingComponent;
import org.apache.wicket.markup.html.form.SubmitLink;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.IPageLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.security.actions.Enable;
import org.apache.wicket.security.checks.ClassSecurityCheck;
import org.apache.wicket.security.swarm.checks.DataSecurityCheck;
import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.ValidationError;
import org.apache.wicket.validation.validator.AbstractValidator;

/**
 * @author hoeve
 */
@PageInfo(title = "Groepkaart bewerken", menu = {"Groep > [groep] > Bewerken", "Groep > Toevoegen"})
@InPrincipal( {GroepsdeelnameWrite.class, GroepsdeelnameVerwijderen.class, GroepWrite.class})
public class GroepEditPage extends AbstractGroepPage implements IModuleEditPage<Groep>
{
	@InPrincipal(GroepVerwijderen.class)
	private static class GroepVerwijderenButton extends ProbeerTeVerwijderenButton
	{
		private static final long serialVersionUID = 1L;

		public GroepVerwijderenButton(BottomRowPanel bottomRow, IModel<Groep> objectToDelete)
		{
			super(bottomRow, objectToDelete, "deze groep (inclusief alle deelnames)",
				GroepZoekenPage.class);
			ComponentUtil.setSecurityCheck(this, new ClassSecurityCheck(
				GroepVerwijderenButton.class));
		}
	}

	private static final long serialVersionUID = 1L;

	private SecurePage returnPage;

	private final Form<Groep> form;

	private GroepModel groepModel;

	private AutoFieldSet<Groep> fieldSet;

	private boolean editRecht;

	private boolean deelnameRemoveRecht;

	public GroepEditPage(Groep groep, SecurePage returnPage)
	{
		super(GroepMenuItem.Groepkaart, groep);
		this.returnPage = returnPage;
		groepModel = new GroepModel(groep);

		form = new Form<Groep>("form", groepModel.getEntiteitModel());
		form.add(new GroepsdeelnameValidator(groepModel));
		add(form);

		editRecht =
			new OrganisatieEenheidLocatieSecurityCheck(new DataSecurityCheck(Groep.GROEP_WRITE),
				groep).isActionAuthorized(Enable.class);
		deelnameRemoveRecht =
			new OrganisatieEenheidLocatieSecurityCheck(
				new DataSecurityCheck(Groep.DEELNAME_REMOVE), groep)
				.isActionAuthorized(Enable.class);

		fieldSet = new AutoFieldSet<Groep>("fieldSet", groepModel.getEntiteitModel());
		if (editRecht)
		{
			fieldSet.setRenderMode(RenderMode.EDIT);
			final OrganisatieEenheidLocatieFieldModifier orgEhdLocModifier =
				new OrganisatieEenheidLocatieFieldModifier();
			fieldSet.addFieldModifier(orgEhdLocModifier);
			fieldSet.addModifier("code", new HoofdletterAjaxHandler(HoofdletterMode.Alles));
			fieldSet.addFieldModifier(new ValidateModifier(new AbstractValidator<String>()
			{
				private static final long serialVersionUID = 1L;

				@Override
				protected void onValidate(IValidatable<String> validatable)
				{
					Date begindatum =
						((DatumField) GroepEditPage.this.fieldSet.findFieldComponent("begindatum"))
							.getDatum();
					Date einddatum =
						((DatumField) GroepEditPage.this.fieldSet.findFieldComponent("einddatum"))
							.getDatum();
					String code = validatable.getValue();

					GroepZoekFilter filter = new GroepZoekFilter();
					filter.setExactCaseInsensitiveMatch(code);
					filter
						.setAuthorizationContext(new OrganisatieEenheidLocatieAuthorizationContext(
							GroepEditPage.this));

					List<Groep> list =
						DataAccessRegistry.getHelper(GroepDataAccessHelper.class).list(filter);

					boolean overlappendeGroepBestaatAl = false;
					for (Groep gr : list)
					{
						if ((!getGroep().isSaved() || getGroep().getId().compareTo(gr.getId()) != 0)
							&& TimeUtil.getInstance().isOverlapping(gr.getBegindatum(),
								gr.getEinddatum(), begindatum, einddatum))
						{
							overlappendeGroepBestaatAl = true;
							break;
						}
					}
					if (overlappendeGroepBestaatAl)
					{
						ValidationError error =
							new ValidationError()
								.setMessage("Er bestaat al een groep met deze code en een overlappende actief-periode.");
						validatable.error(error);
					}
				}
			}, "code"));

		}
		fieldSet.setPropertyNames("code", "groepstype", "naam", "leerjaar", "begindatum",
			"einddatum", "organisatieEenheid", "locatie");
		fieldSet.setSortAccordingToPropertyNames(true);

		form.add(fieldSet);

		VrijVeldEntiteitEditPanel<Groep> vrijVeldenPanel =
			new VrijVeldEntiteitEditPanel<Groep>("vrijvelden", groepModel.getEntiteitModel())
			{
				private static final long serialVersionUID = 1L;

				@Override
				public void updateModel()
				{
					// Als je niet begrijpt wat dit doet, dan moet je het ook niet
					// aanpassen
					IFormSubmittingComponent submitButton = form.findSubmittingButton();
					if (((Component) submitButton).findParent(BottomRowPanel.class) != null)
						super.updateModel();
				}
			};
		vrijVeldenPanel.getVrijVeldZoekFilter().setDossierScherm(true);
		vrijVeldenPanel.getVrijVeldZoekFilter().setCategorie(VrijVeldCategorie.GROEP);
		form.add(vrijVeldenPanel);

		createFieldsetDocenten();
		createFieldsetMentoren();
		createFieldsetDeelnemers();

		createComponents();
	}

	private void createFieldsetDocenten()
	{
		final WebMarkupContainer datapanelContainer = new WebMarkupContainer("panelDocenten");
		datapanelContainer.setOutputMarkupId(true);
		CustomDataPanel<GroepDocent> datapanel =
			new EduArteDataPanel<GroepDocent>("datapanelDocenten",
				new ListModelDataProvider<GroepDocent>(groepModel.getGroepDocentenListModel())
				{
					private static final long serialVersionUID = 1L;

					@Override
					public IModel<GroepDocent> model(GroepDocent object)
					{
						return groepModel.getEntiteitManager().getModel(object, null);
					}
				}, new GroepDocentTable(editRecht)
				{
					private static final long serialVersionUID = 1L;

					@Override
					public void deleteGroepDocent(GroepDocent groepDocent, AjaxRequestTarget target)
					{
						GroepEditPage.this.groepModel.deleteGroepDocent(groepDocent);
						target.addComponent(datapanelContainer);
					}
				});
		datapanel.setReuseItems(true);
		datapanelContainer.setOutputMarkupId(true);
		datapanelContainer.add(datapanel);
		datapanel.setSelecteerKolommenButtonVisible(false);

		datapanelContainer.add(new SubmitLink("docentToevoegen", form)
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void onSubmit()
			{
				final MedewerkerZoekFilter filter = new MedewerkerZoekFilter();
				final HibernateSelection<Medewerker> selection =
					new HibernateSelection<Medewerker>(Medewerker.class);

				setResponsePage(new GroepMedewerkerSelectiePage(GroepEditPage.this, filter,
					selection, new AbstractSelectieTarget<Medewerker, Medewerker>(
						GroepEditPage.class, "Toevoegen")
					{
						private static final long serialVersionUID = 1L;

						@Override
						public Link<Void> createLink(String linkId,
								final ISelectionComponent<Medewerker, Medewerker> base)
						{
							return new Link<Void>(linkId)
							{
								private static final long serialVersionUID = 1L;

								@Override
								public void onClick()
								{
									updateDocenten(base.getSelectedElements());
									setResponsePage(GroepEditPage.this);
								}
							};
						}
					}));
			}
		}.setVisible(editRecht));

		add(datapanelContainer);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void onBeforeRender()
	{
		super.onBeforeRender();
		Component begindatum = fieldSet.findFieldComponent("begindatum");
		Component einddatum = fieldSet.findFieldComponent("einddatum");
		if (editRecht)
			form.add(new BegindatumVoorEinddatumValidator((TextField< ? extends Date>) begindatum,
				(TextField< ? extends Date>) einddatum));
	}

	private Groep getGroep()
	{
		return groepModel.getObject();
	}

	private void createFieldsetMentoren()
	{
		final WebMarkupContainer datapanelContainer = new WebMarkupContainer("panelMentoren");
		datapanelContainer.setOutputMarkupId(true);
		CustomDataPanel<GroepMentor> datapanel =
			new EduArteDataPanel<GroepMentor>("datapanelMentoren",
				new ListModelDataProvider<GroepMentor>(groepModel.getGroepMentorenListModel())
				{
					private static final long serialVersionUID = 1L;

					@Override
					public IModel<GroepMentor> model(GroepMentor object)
					{
						return groepModel.getEntiteitManager().getModel(object, null);
					}
				}, new GroepMentorTable(editRecht)
				{
					private static final long serialVersionUID = 1L;

					@Override
					public void deleteGroepMentor(GroepMentor groepMentor, AjaxRequestTarget target)
					{
						GroepEditPage.this.groepModel.deleteGroepMentor(groepMentor);
						target.addComponent(datapanelContainer);
					}
				});
		datapanel.setReuseItems(true);
		datapanelContainer.setOutputMarkupId(true);
		datapanelContainer.add(datapanel);
		datapanel.setSelecteerKolommenButtonVisible(false);

		datapanelContainer.add(new SubmitLink("mentorToevoegen", form)
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void onSubmit()
			{
				final MedewerkerZoekFilter filter = new MedewerkerZoekFilter();
				final HibernateSelection<Medewerker> selection =
					new HibernateSelection<Medewerker>(Medewerker.class);

				setResponsePage(new GroepMedewerkerSelectiePage(GroepEditPage.this, filter,
					selection, new AbstractSelectieTarget<Medewerker, Medewerker>(
						GroepEditPage.class, "Toevoegen")
					{
						private static final long serialVersionUID = 1L;

						@Override
						public Link<Void> createLink(String linkId,
								final ISelectionComponent<Medewerker, Medewerker> base)
						{
							return new Link<Void>(linkId)
							{
								private static final long serialVersionUID = 1L;

								@Override
								public void onClick()
								{
									updateMentoren(base.getSelectedElements());
									setResponsePage(GroepEditPage.this);
								}
							};
						}
					}));
			}
		}.setVisible(editRecht));

		add(datapanelContainer);
	}

	private void createFieldsetDeelnemers()
	{
		boolean allowEdit =
			getContextGroep().getGroepstype() == null
				|| !getContextGroep().getGroepstype().isPlaatsingsgroep();
		GroepsdeelnameZoekFilter deelnameFilter = new GroepsdeelnameZoekFilter();
		deelnameFilter.getDeelnemerFilter().addOrderByProperty("persoon.achternaam");
		final WebMarkupContainer datapanelContainer = new WebMarkupContainer("panelDeelnemers");
		datapanelContainer.setOutputMarkupId(true);
		final GroepsdeelnameDataProvider dataProvider =
			new GroepsdeelnameDataProvider(deelnameFilter, groepModel);
		final CustomDataPanel<Groepsdeelname> datapanel =
			new EduArteDataPanel<Groepsdeelname>("datapanelDeelnemers", dataProvider,
				new GroepsdeelnameTable(allowEdit, allowEdit && deelnameRemoveRecht, false)
				{
					private static final long serialVersionUID = 1L;

					@Override
					public void deleteGroepsdeelname(Groepsdeelname groepsdeelname,
							AjaxRequestTarget target)
					{
						GroepEditPage.this.groepModel.deleteGroepsdeelname(groepsdeelname);
						target.addComponent(datapanelContainer);
					}
				});
		datapanel.setItemsPerPage(35);
		datapanel.setReuseItems(true);
		datapanelContainer.setOutputMarkupId(true);
		datapanelContainer.add(datapanel);
		datapanel.setSelecteerKolommenButtonVisible(false);
		add(new GroepsdeelnameZoekFilterPanel("filter", deelnameFilter, datapanel));

		WebMarkupContainer editContainer = new WebMarkupContainer("editContainer");
		editContainer.setRenderBodyOnly(true);
		editContainer.setVisibilityAllowed(allowEdit);
		datapanelContainer.add(editContainer);

		editContainer.add(new SubmitLink("deelnemerToevoegen", form)
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void onSubmit()
			{
				VerbintenisZoekFilter filter = VerbintenisZoekFilter.getDefaultFilter();
				DatabaseSelection<Deelnemer, Verbintenis> selection =
					new ProjectedSelection<Deelnemer, Verbintenis>(Verbintenis.class, "deelnemer");

				setResponsePage(new GroepDeelnemerSelectiePage(GroepEditPage.this, filter,
					selection, new AbstractSelectieTarget<Deelnemer, Verbintenis>(
						GroepEditPage.class, "Toevoegen")
					{
						private static final long serialVersionUID = 1L;

						@Override
						public Link<Void> createLink(String linkId,
								final ISelectionComponent<Deelnemer, Verbintenis> base)
						{
							return new Link<Void>(linkId)
							{
								private static final long serialVersionUID = 1L;

								@Override
								public void onClick()
								{
									updateDeelnemers(base.getSelectedElements());
									setResponsePage(GroepEditPage.this);
								}
							};
						}
					}));
			}
		});
		add(datapanelContainer);

		final DatumField massDatum =
			new DatumField("massDatum", Model.of(TimeUtil.getInstance().currentDate()));
		editContainer.add(massDatum);
		editContainer.add(new AjaxLink<Void>("massBegin")
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick(AjaxRequestTarget target)
			{
				Iterator<Groepsdeelname> it = dataProvider.iterator(0, Integer.MAX_VALUE);
				while (it.hasNext())
					it.next().setBegindatum(massDatum.getModelObject());
				target.addComponent(datapanel);
			}
		});
		editContainer.add(new AjaxLink<Void>("massEind")
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick(AjaxRequestTarget target)
			{
				Iterator<Groepsdeelname> it = dataProvider.iterator(0, Integer.MAX_VALUE);
				while (it.hasNext())
				{
					Groepsdeelname deelname = it.next();
					if (deelname.getEinddatum() == null)
						deelname.setEinddatum(massDatum.getModelObject());
				}
				target.addComponent(datapanel);
			}
		});
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
				GroepEditPage.this.groepModel.save();
				setResponsePage(GroepEditPage.this.getReturnPage());
			}
		});
		panel.addButton(new AnnulerenButton(panel, new IPageLink()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public Page getPage()
			{
				return GroepEditPage.this.getReturnPage();
			}

			@Override
			public Class< ? extends SecurePage> getPageIdentity()
			{
				return GroepEditPage.class;
			}

		}));
		panel.addButton(new GroepVerwijderenButton(panel, groepModel.getEntiteitModel()));
	}

	public SecurePage getReturnPage()
	{
		return returnPage;
	}

	@Override
	protected void onDetach()
	{
		super.onDetach();

		ComponentUtil.detachQuietly(groepModel);
	}

	private void updateDeelnemers(List<Deelnemer> deelnemers)
	{
		for (Deelnemer deelnemer : deelnemers)
		{
			Groepsdeelname deelname = new Groepsdeelname();
			deelname.setDeelnemer(deelnemer);
			groepModel.addGroepsdeelname(deelname);
		}
	}

	private void updateMentoren(List<Medewerker> medewerkers)
	{
		for (Medewerker mentor : medewerkers)
		{
			GroepMentor groepMentor = new GroepMentor();
			groepMentor.setMedewerker(mentor);
			groepModel.addGroepMentor(groepMentor);
		}
	}

	private void updateDocenten(List<Medewerker> medewerkers)
	{
		for (Medewerker docent : medewerkers)
		{
			GroepDocent groepDocent = new GroepDocent();
			groepDocent.setMedewerker(docent);
			groepModel.addGroepDocent(groepDocent);
		}
	}
}
