/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.krd.web.pages.deelnemer.verbintenis;

import java.util.Date;
import java.util.List;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.dao.BatchDataAccessHelper;
import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.modelsv2.DefaultModelManager;
import nl.topicus.cobra.modelsv2.IChangeRecordingModel;
import nl.topicus.cobra.modelsv2.ModelFactory;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.util.ComponentUtil;
import nl.topicus.cobra.util.TimeUtil;
import nl.topicus.cobra.web.components.choice.IntegerRangeCombobox;
import nl.topicus.cobra.web.components.form.AutoFieldSet;
import nl.topicus.cobra.web.components.form.RenderMode;
import nl.topicus.cobra.web.components.form.modifier.ConstructorArgModifier;
import nl.topicus.cobra.web.components.form.modifier.EnableModifier;
import nl.topicus.cobra.web.components.form.modifier.VisibilityModifier;
import nl.topicus.cobra.web.components.panels.bottomrow.AnnulerenButton;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.cobra.web.components.panels.bottomrow.OpslaanButton;
import nl.topicus.cobra.web.components.panels.bottomrow.VerwijderButton;
import nl.topicus.cobra.web.components.text.DatumField;
import nl.topicus.cobra.web.security.RequiredSecurityCheck;
import nl.topicus.cobra.web.security.RequiredSecurityChecks;
import nl.topicus.cobra.web.validators.BegindatumVoorEinddatumValidator;
import nl.topicus.cobra.web.validators.DatumGroterOfGelijkDatumValidator;
import nl.topicus.cobra.web.validators.DatumKleinerOfGelijkDatumValidator;
import nl.topicus.eduarte.app.security.checks.DeelnemerSecurityCheck;
import nl.topicus.eduarte.app.security.checks.NietOverledenSecurityCheck;
import nl.topicus.eduarte.app.security.checks.OrganisatieEenheidLocatieSecurityCheck;
import nl.topicus.eduarte.entities.inschrijving.Plaatsing;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.entities.opleiding.Opleiding;
import nl.topicus.eduarte.entities.vrijevelden.PlaatsingVrijVeld;
import nl.topicus.eduarte.entities.vrijevelden.VrijVeldCategorie;
import nl.topicus.eduarte.entities.vrijevelden.VrijVeldOptieKeuze;
import nl.topicus.eduarte.krd.bron.BronVerbintenisWijzigingToegestaanCheck;
import nl.topicus.eduarte.krd.principals.deelnemer.verbintenis.DeelnemerVerbintenissenWrite;
import nl.topicus.eduarte.krd.principals.deelnemer.verbintenis.PlaatsingVerwijderen;
import nl.topicus.eduarte.krd.web.components.panels.VrijVeldEntiteitEditPanel;
import nl.topicus.eduarte.krd.web.validators.BronPlaatsingValidator;
import nl.topicus.eduarte.web.components.autoform.EduArteAjaxRefreshModifier;
import nl.topicus.eduarte.web.components.menu.DeelnemerMenuItem;
import nl.topicus.eduarte.web.pages.IModuleEditPage;
import nl.topicus.eduarte.web.pages.SecurePage;
import nl.topicus.eduarte.web.pages.deelnemer.AbstractDeelnemerPage;
import nl.topicus.eduarte.web.pages.deelnemer.verbintenis.DeelnemerVerbintenisPage;
import nl.topicus.eduarte.zoekfilters.GroepZoekFilter;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.security.checks.ClassSecurityCheck;

/**
 * @author idserda
 */
@PageInfo(title = "Plaatsing Bewerken", menu = {"Deelnemer > [deelnemer] > Verbintenis > [plaatsing] > Bewerken"})
@InPrincipal(DeelnemerVerbintenissenWrite.class)
@RequiredSecurityChecks( {@RequiredSecurityCheck(NietOverledenSecurityCheck.class),
	@RequiredSecurityCheck(OrganisatieEenheidLocatieSecurityCheck.class)})
public class EditPlaatsingPage extends AbstractDeelnemerPage implements IModuleEditPage<Plaatsing>
{
	@InPrincipal(PlaatsingVerwijderen.class)
	private class PlaastingVerwijderenButton extends VerwijderButton
	{
		private static final long serialVersionUID = 1L;

		private PlaastingVerwijderenButton(BottomRowPanel bottomRow)
		{
			super(bottomRow, "Verwijderen", "Weet u zeker dat u deze plaatsing wilt verwijderen?");
			ComponentUtil.setSecurityCheck(this, new DeelnemerSecurityCheck(new ClassSecurityCheck(
				PlaastingVerwijderenButton.class), getContextDeelnemer()));
		}

		@Override
		public boolean isVisible()
		{
			return getPlaatsing().isSaved();
		}

		@Override
		protected void onClick()
		{
			Verbintenis verbintenis = getPlaatsing().getVerbintenis();
			verbintenis.getPlaatsingen().remove(getPlaatsing());

			getChangeRecordingModel().deleteObject();
			DataAccessRegistry.getHelper(BatchDataAccessHelper.class).batchExecute();

			setResponsePage(new DeelnemerVerbintenisPage(verbintenis));
		}
	}

	private static final long serialVersionUID = 1L;

	private Form<Void> form;

	private AutoFieldSet<Plaatsing> formFields;

	private SecurePage returnToPage;

	public EditPlaatsingPage(Plaatsing plaatsing, final SecurePage returnToPage)
	{
		super(DeelnemerMenuItem.Verbintenis, plaatsing.getDeelnemer(), plaatsing.getVerbintenis());
		this.returnToPage = returnToPage;

		setDefaultModel(ModelFactory.getCompoundChangeRecordingModel(plaatsing,
			new DefaultModelManager(VrijVeldOptieKeuze.class, PlaatsingVrijVeld.class,
				Plaatsing.class)));

		form = new Form<Void>("plaatsingForm");

		formFields = new AutoFieldSet<Plaatsing>("inputFields", getPlaatsingModel());
		formFields.setPropertyNames("groep", "begindatum", "einddatum", "lwoo",
			"inschrijvingsVorm", "opleidingsVorm");

		GroepZoekFilter filter = GroepZoekFilter.createDefaultFilter();
		filter.setPlaatsingsgroep(Boolean.TRUE);
		filter.setOrganisatieEenheid(getPlaatsing().getVerbintenis().getOrganisatieEenheid());
		formFields.addFieldModifier(new ConstructorArgModifier("groep", filter));
		formFields.setSortAccordingToPropertyNames(true);
		formFields.setRenderMode(RenderMode.EDIT);
		formFields.addFieldModifier(new EduArteAjaxRefreshModifier("begindatum", "lwoo"));

		formFields.addFieldModifier(new EnableModifier(new LoadableDetachableModel<Boolean>()
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected Boolean load()
			{
				BronVerbintenisWijzigingToegestaanCheck check =
					new BronVerbintenisWijzigingToegestaanCheck(getPlaatsing().getBegindatum(),
						getPlaatsing().getVerbintenis());

				return check.isWijzigingToegestaan();
			}

		}, "lwoo"));

		formFields.addFieldModifier(new VisibilityModifier(new LoadableDetachableModel<Boolean>()
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected Boolean load()
			{
				return heeftLwoo();
			}

		}, "lwoo"));

		formFields.addFieldModifier(new VisibilityModifier(isHOOpleiding(), "inschrijvingsVorm",
			"opleidingsVorm"));

		form.add(formFields);

		form.add(new Label("verbintenis", getVerbintenisNaam(plaatsing)));

		addJarenPraktijkonderwijs(plaatsing);

		form.add(voegVrijVeldVeldenToeAanForm());

		addLeerjaar(plaatsing);

		// addLWOOVeld();

		add(form);

		createComponents();
	}

	private boolean isHOOpleiding()
	{
		Opleiding opleiding = getPlaatsing().getVerbintenis().getOpleiding();
		return opleiding != null && opleiding.isHogerOnderwijs();
	}

	private boolean heeftLwoo()
	{
		if (getPlaatsing().getVerbintenis().getOpleiding() != null)
		{
			return getPlaatsing().getVerbintenis().getOpleiding().heeftLwoo();
		}
		else
		{
			return false;
		}
	}

	private IChangeRecordingModel<Plaatsing> getChangeRecordingModel()
	{
		return (IChangeRecordingModel<Plaatsing>) getPlaatsingModel();
	}

	private void addLeerjaar(Plaatsing plaatsing)
	{
		WebMarkupContainer leerjaarRow = new WebMarkupContainer("leerjaarRow");

		boolean verplicht = false;

		Opleiding opleiding = plaatsing.getVerbintenis().getOpleiding();

		if (opleiding != null)
		{
			verplicht =
				opleiding.getVerbintenisgebied().getTaxonomie().isVO()
					&& !"0090".equals(opleiding.getVerbintenisgebied().getExterneCode());
		}

		leerjaarRow.add(new IntegerRangeCombobox("leerjaar", getBeginLeerjaar(plaatsing),
			getEindLeerjaar(plaatsing)).setRequired(verplicht));

		form.add(leerjaarRow);
	}

	private void addJarenPraktijkonderwijs(Plaatsing plaatsing)
	{
		WebMarkupContainer jarenPraktijkonderwijsRow =
			new WebMarkupContainer("jarenPraktijkonderwijsRow");
		boolean praktijkonderwijs = false;
		Opleiding opleiding = plaatsing.getVerbintenis().getOpleiding();
		if (opleiding != null)
			praktijkonderwijs =
				opleiding.getVerbintenisgebied().getTaxonomie().isVO()
					&& "0090".equals(opleiding.getVerbintenisgebied().getExterneCode());
		jarenPraktijkonderwijsRow.add(new IntegerRangeCombobox("jarenPraktijkonderwijs", 1, 9)
			.setRequired(praktijkonderwijs));
		jarenPraktijkonderwijsRow.setVisible(praktijkonderwijs);
		form.add(jarenPraktijkonderwijsRow);
	}

	private VrijVeldEntiteitEditPanel<Plaatsing> voegVrijVeldVeldenToeAanForm()
	{
		VrijVeldEntiteitEditPanel<Plaatsing> VVEEPanel =
			new VrijVeldEntiteitEditPanel<Plaatsing>("vrijVelden", getPlaatsingModel());
		VVEEPanel.getVrijVeldZoekFilter().setCategorie(VrijVeldCategorie.PLAATSING);
		VVEEPanel.getVrijVeldZoekFilter().setDossierScherm(true);
		return VVEEPanel;
	}

	@Override
	protected void onBeforeRender()
	{
		super.onBeforeRender();

		DatumField begindatumVeld = (DatumField) formFields.findFieldComponent("begindatum");
		DatumField einddatumVeld = (DatumField) formFields.findFieldComponent("einddatum");

		Date verbintenisBegindatum = getPlaatsing().getVerbintenis().getBegindatum();
		Date verbintenisEinddatum = getPlaatsing().getVerbintenis().getEinddatum();

		form.add(new BegindatumVoorEinddatumValidator(begindatumVeld, einddatumVeld));
		form.add(new DatumGroterOfGelijkDatumValidator("Begindatum", begindatumVeld,
			verbintenisBegindatum));
		form.add(new BronPlaatsingValidator(begindatumVeld, getPlaatsingModel()));

		if (verbintenisEinddatum != null)
		{
			form.add(new DatumKleinerOfGelijkDatumValidator("Begindatum", begindatumVeld,
				verbintenisEinddatum));
			form.add(new DatumKleinerOfGelijkDatumValidator("Einddatum", einddatumVeld,
				verbintenisEinddatum));
		}
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
				processOnSubmit();
			}

		});
		panel.addButton(new AnnulerenButton(panel, returnToPage));

		panel.addButton(new PlaastingVerwijderenButton(panel));
	}

	private String getVerbintenisNaam(Plaatsing plaatsing)
	{
		if (plaatsing.getVerbintenis() != null && plaatsing.getVerbintenis().getOpleiding() != null)
			return plaatsing.getVerbintenis().getOpleiding().toString();
		return plaatsing.getVerbintenis().getOrganisatieEenheid().getNaam();
	}

	private int getEindLeerjaar(Plaatsing plaatsing)
	{
		if (plaatsing.getVerbintenis().getOpleiding() != null)
		{
			Integer ret = plaatsing.getVerbintenis().getOpleiding().getEindLeerjaar();

			if (ret != null)
			{
				return ret;
			}
		}

		return 6;
	}

	private int getBeginLeerjaar(Plaatsing plaatsing)
	{
		if (plaatsing.getVerbintenis().getOpleiding() != null)
		{
			Integer ret = plaatsing.getVerbintenis().getOpleiding().getBeginLeerjaar();

			if (ret != null)
			{
				return ret;
			}
		}
		return 1;
	}

	private Plaatsing getPlaatsing()
	{
		return (Plaatsing) getDefaultModelObject();
	}

	@SuppressWarnings("unchecked")
	public IModel<Plaatsing> getPlaatsingModel()
	{
		return (IModel<Plaatsing>) getDefaultModel();
	}

	private void processOnSubmit()
	{
		getChangeRecordingModel().saveObject();
		DataAccessRegistry.getHelper(BatchDataAccessHelper.class).batchExecute();

		List<Plaatsing> plaatsingen = getPlaatsing().getVerbintenis().getPlaatsingen();
		Plaatsing huidigePlaatsing = getPlaatsing();

		int currentIndex = plaatsingen.indexOf(huidigePlaatsing);
		if ((currentIndex + 1) < plaatsingen.size())
		{
			Plaatsing vorigePlaatsing = plaatsingen.get(currentIndex + 1);
			if (vorigePlaatsing.getEinddatum() == null)
				vorigePlaatsing.setEinddatum(TimeUtil.getInstance().addDays(
					huidigePlaatsing.getBegindatum(), -1));
		}

		// nieuwe plaatsing direct tonen op het response scherm
		huidigePlaatsing.getVerbintenis().commit();
		huidigePlaatsing.getVerbintenis().refresh();
		setResponsePage(returnToPage);
	}
}