/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.web.pages.shared;

import java.util.List;
import java.util.Set;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.dao.BatchDataAccessHelper;
import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.dao.hibernate.HibernateSelection;
import nl.topicus.cobra.modelsv2.DefaultModelManager;
import nl.topicus.cobra.modelsv2.IChangeRecordingModel;
import nl.topicus.cobra.modelsv2.ModelFactory;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.security.RechtenSoort;
import nl.topicus.cobra.security.RechtenSoorten;
import nl.topicus.cobra.util.ComponentUtil;
import nl.topicus.cobra.web.components.datapanel.selection.ISelectionComponent;
import nl.topicus.cobra.web.components.form.AutoFieldSet;
import nl.topicus.cobra.web.components.form.RenderMode;
import nl.topicus.cobra.web.components.form.SavableForm;
import nl.topicus.cobra.web.components.link.ErrorProcessingSubmitLink;
import nl.topicus.cobra.web.components.panels.bottomrow.AbstractBottomRowButton;
import nl.topicus.cobra.web.components.panels.bottomrow.AnnulerenButton;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.cobra.web.components.panels.bottomrow.ButtonAlignment;
import nl.topicus.cobra.web.components.panels.bottomrow.OpslaanButton;
import nl.topicus.cobra.web.components.shortcut.CobraKeyAction;
import nl.topicus.cobra.web.pages.IEditPage;
import nl.topicus.eduarte.core.principals.Always;
import nl.topicus.eduarte.entities.onderwijsproduct.Onderwijsproduct;
import nl.topicus.eduarte.entities.resultaatstructuur.StandaardToetsCodeFilter;
import nl.topicus.eduarte.entities.resultaatstructuur.ToetsCodeFilter;
import nl.topicus.eduarte.entities.resultaatstructuur.ToetsCodeFilterOrganisatieEenheidLocatie;
import nl.topicus.eduarte.web.components.panels.datapanel.table.OrganisatieLocatieTable;
import nl.topicus.eduarte.web.components.panels.organisatielocatie.OrganisatieEenheidLocatieEntiteitEditPanel;
import nl.topicus.eduarte.web.pages.AbstractDynamicContextPage;
import nl.topicus.eduarte.web.pages.SecurePage;
import nl.topicus.eduarte.web.pages.SubpageContext;
import nl.topicus.eduarte.zoekfilters.OnderwijsproductZoekFilter;

import org.apache.wicket.Session;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.PropertyModel;

@PageInfo(title = "Toetscodefilters bewerken", menu = "Meerdere paden")
@InPrincipal(Always.class)
@RechtenSoorten(RechtenSoort.INSTELLING)
public class ToetsFilterEditPage extends AbstractDynamicContextPage<ToetsCodeFilter> implements
		IEditPage
{
	private static final long serialVersionUID = 1L;

	private SavableForm<ToetsCodeFilter> form;

	private SecurePage returnPage;

	public ToetsFilterEditPage(ToetsCodeFilter filter, SecurePage returnPage)
	{
		super(ModelFactory.getCompoundChangeRecordingModel(filter, new DefaultModelManager(
			ToetsCodeFilterOrganisatieEenheidLocatie.class, StandaardToetsCodeFilter.class,
			ToetsCodeFilter.class)), new SubpageContext(returnPage));

		this.returnPage = returnPage;
		form = new SavableForm<ToetsCodeFilter>("form", getContextModel());
		form.setUpdateModelsOnError(true);
		add(form);
		AutoFieldSet<ToetsCodeFilter> inputFields =
			new AutoFieldSet<ToetsCodeFilter>("inputFields", getContextModel(), "Toetsfilter");
		inputFields.setRenderMode(RenderMode.EDIT);
		inputFields.setPropertyNames("naam", "toetsCodes");
		form.add(inputFields);

		form
			.add(new OrganisatieEenheidLocatieEntiteitEditPanel<ToetsCodeFilterOrganisatieEenheidLocatie>(
				"orgEhdLocatieKoppel",
				new PropertyModel<List<ToetsCodeFilterOrganisatieEenheidLocatie>>(
					getContextModel(), "organisatieEenhedenLocaties"), getFilterModel()
					.getManager(),
				new OrganisatieLocatieTable<ToetsCodeFilterOrganisatieEenheidLocatie>(false), true)
			{
				private static final long serialVersionUID = 1L;

				@Override
				public ToetsCodeFilterOrganisatieEenheidLocatie createNewT()
				{
					return new ToetsCodeFilterOrganisatieEenheidLocatie(getFilter());
				}

				@Override
				public boolean isVisible()
				{
					return super.isVisible() && getFilter().getMedewerker() == null;
				}
			});

		createComponents();
	}

	@Override
	protected void onBeforeRender()
	{
		super.onBeforeRender();
		form.clearInput();
	}

	@SuppressWarnings("unchecked")
	private IChangeRecordingModel<ToetsCodeFilter> getFilterModel()
	{
		return (IChangeRecordingModel<ToetsCodeFilter>) getDefaultModel();
	}

	private ToetsCodeFilter getFilter()
	{
		return getFilterModel().getObject();
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
				getFilterModel().saveObject();
				DataAccessRegistry.getHelper(BatchDataAccessHelper.class).batchExecute();
				setResponsePage(returnPage);
			}
		});
		panel.addButton(new AnnulerenButton(panel, returnPage));
		panel.addButton(new AbstractBottomRowButton(panel, "Toetscodes selecteren",
			CobraKeyAction.LINKKNOP1, ButtonAlignment.LEFT)
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected WebMarkupContainer getLink(String linkId)
			{
				return new ErrorProcessingSubmitLink(linkId, form)
				{
					private static final long serialVersionUID = 1L;

					@Override
					public void onSubmit()
					{
						startToetscodeWizard();
					}

					@Override
					public void onError()
					{
						Session.get().getFeedbackMessages().clear();
						startToetscodeWizard();
					}
				};
			}
		});
	}

	private void startToetscodeWizard()
	{
		setResponsePage(new ToetsFilterOnderwijsproductSelectiePage(ToetsFilterEditPage.this,
			OnderwijsproductZoekFilter.createDefaultFilter(),
			new HibernateSelection<Onderwijsproduct>(Onderwijsproduct.class),
			new AbstractSelectieTarget<Onderwijsproduct, Onderwijsproduct>(
				ToetscodeSelectiePage.class, "Volgende")
			{
				private static final long serialVersionUID = 1L;

				@Override
				public Link<Void> createLink(String linkId,
						final ISelectionComponent<Onderwijsproduct, Onderwijsproduct> base)
				{
					return new Link<Void>(linkId)
					{
						private static final long serialVersionUID = 1L;

						@Override
						public void onClick()
						{
							setResponsePage(new ToetscodeSelectiePage(
								new PropertyModel<Set<String>>(getFilterModel(), "toetsCodesAsSet"),
								((OnderwijsproductZoekFilter) base.getFilter()).getCohort(), base
									.getSelectedElements(), ToetsFilterEditPage.this,
								(SecurePage) getPage()));
						}
					};
				}
			}));
	}

	@Override
	protected void onDetach()
	{
		super.onDetach();
		ComponentUtil.detachFields(this);
	}
}
