/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.krd.web.pages.deelnemer.relatie;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.dao.BatchDataAccessHelper;
import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.modelsv2.DefaultModelManager;
import nl.topicus.cobra.modelsv2.IChangeRecordingModel;
import nl.topicus.cobra.modelsv2.ModelFactory;
import nl.topicus.cobra.modelsv2.ModelManager;
import nl.topicus.cobra.reflection.copy.CopyManager;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.transformers.HoofdletterMode;
import nl.topicus.cobra.util.HibernateObjectCopyManager;
import nl.topicus.cobra.web.behaviors.AjaxFormComponentValidatingBehavior;
import nl.topicus.cobra.web.behaviors.HoofdletterAjaxHandler;
import nl.topicus.cobra.web.components.form.AutoFieldSet;
import nl.topicus.cobra.web.components.form.RenderMode;
import nl.topicus.cobra.web.components.form.modifier.ConstructorArgModifier;
import nl.topicus.cobra.web.components.form.modifier.ValidateModifier;
import nl.topicus.cobra.web.components.panels.bottomrow.AbstractBottomRowButton;
import nl.topicus.cobra.web.components.panels.bottomrow.AnnulerenButton;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.cobra.web.components.panels.bottomrow.ButtonAlignment;
import nl.topicus.cobra.web.components.panels.bottomrow.OpslaanButton;
import nl.topicus.cobra.web.components.shortcut.CobraKeyAction;
import nl.topicus.cobra.web.validators.BankrekeningElfProefValidator;
import nl.topicus.cobra.web.validators.BsnValidator;
import nl.topicus.cobra.web.validators.UniqueConstraintValidator;
import nl.topicus.eduarte.entities.adres.Adres;
import nl.topicus.eduarte.entities.personen.AbstractRelatie;
import nl.topicus.eduarte.entities.personen.Persoon;
import nl.topicus.eduarte.entities.personen.PersoonAdres;
import nl.topicus.eduarte.entities.personen.PersoonContactgegeven;
import nl.topicus.eduarte.entities.personen.Relatie;
import nl.topicus.eduarte.entities.vrijevelden.RelatieVrijVeld;
import nl.topicus.eduarte.entities.vrijevelden.VrijVeldCategorie;
import nl.topicus.eduarte.entities.vrijevelden.VrijVeldOptieKeuze;
import nl.topicus.eduarte.krd.principals.deelnemer.relatie.DeelnemerRelatiePersoonWrite;
import nl.topicus.eduarte.krd.web.components.panels.VrijVeldEntiteitEditPanel;
import nl.topicus.eduarte.web.components.menu.DeelnemerMenuItem;
import nl.topicus.eduarte.web.components.modalwindow.adres.AdressenEditPanel;
import nl.topicus.eduarte.web.components.panels.ContactgegevenEntiteitEditPanel;
import nl.topicus.eduarte.web.pages.IModuleEditPage;
import nl.topicus.eduarte.web.pages.SecurePage;
import nl.topicus.eduarte.web.pages.deelnemer.AbstractDeelnemerPage;
import nl.topicus.eduarte.web.pages.deelnemer.personalia.DeelnemerRelatiesOverzichtPage;
import nl.topicus.eduarte.zoekfilters.RelatieSoortZoekFilter;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.PropertyModel;

/**
 * @author idserda
 */
@PageInfo(title = "Personalia Bewerken", menu = {"Deelnemer > [deelnemer]",
	"Groep > [groep] > [deelnemer]"})
@InPrincipal(DeelnemerRelatiePersoonWrite.class)
public class EditRelatiePage extends AbstractDeelnemerPage implements IModuleEditPage<Relatie>
{
	private boolean adressenGelijkMakenAanDeelnemer;

	private Form<Void> form;

	private AbstractDeelnemerPage returnToPage;

	private AdressenEditPanel<PersoonAdres, Persoon> adressenpanel;

	public EditRelatiePage(Relatie relatie, AbstractDeelnemerPage returnToPage)
	{
		super(DeelnemerMenuItem.Relaties, returnToPage.getContextDeelnemer(), returnToPage
			.getContextVerbintenis());
		setDefaultModel(ModelFactory.getCompoundChangeRecordingModel(relatie,
			new DefaultModelManager(PersoonAdres.class, Adres.class, PersoonContactgegeven.class,
				VrijVeldOptieKeuze.class, RelatieVrijVeld.class, Relatie.class, Persoon.class)));
		this.returnToPage = returnToPage;
		Persoon verzorger = getRelatie().getRelatie();
		Persoon deelnemer = getRelatie().getPersoon();
		adressenGelijkMakenAanDeelnemer =
			!verzorger.isSaved() || zijnAdressenGelijk(verzorger, deelnemer);
		verzorger.checkAndCreateDebiteurNummer();

		adressenpanel =
			new AdressenEditPanel<PersoonAdres, Persoon>("adresEditTabs",
				new PropertyModel<Persoon>(getDefaultModel(), "relatie"), getManager())
			{
				private static final long serialVersionUID = 1L;

				@Override
				public boolean isVisible()
				{
					return !adressenGelijkMakenAanDeelnemer;
				}

			};
		adressenpanel.setOutputMarkupPlaceholderTag(true);

		form = new Form<Void>("personaliaForm");

		form.add(createPersonaliaAutoFieldset("inputFieldsPersonalia", relatie.getDeelnemer()
			.isMeerderjarig()));

		form.add(adressenpanel);
		form.add(new CheckBox("adressenGelijkDeelnemer", new PropertyModel<Boolean>(this,
			"adressenGelijkMakenAanDeelnemer"))
			.add(new AjaxFormComponentUpdatingBehavior("onclick")
			{
				private static final long serialVersionUID = 1L;

				@Override
				protected void onUpdate(AjaxRequestTarget target)
				{
					Persoon updateVerzorger = getRelatie().getRelatie();
					updateVerzorger.getAdressen().clear();
					if (adressenGelijkMakenAanDeelnemer)
					{
						kopieerAdressen(updateVerzorger);
					}
					target.addComponent(getComponent());
					target.addComponent(adressenpanel);
				}

			}));
		form.add(new ContactgegevenEntiteitEditPanel<PersoonContactgegeven, Persoon>(
			"contactgegevens", new PropertyModel<Persoon>(getDefaultModel(), "relatie")));

		VrijVeldEntiteitEditPanel<Relatie> VVEEPanel =
			new VrijVeldEntiteitEditPanel<Relatie>("vrijVelden", getChangeRecordingModel());
		VVEEPanel.getVrijVeldZoekFilter().setDossierScherm(true);
		VVEEPanel.getVrijVeldZoekFilter().setCategorie(VrijVeldCategorie.RELATIE);
		form.add(VVEEPanel);

		add(form);

		createComponents();
	}

	@SuppressWarnings("unchecked")
	private IChangeRecordingModel<Relatie> getChangeRecordingModel()
	{
		return (IChangeRecordingModel<Relatie>) getDefaultModel();
	}

	private ModelManager getManager()
	{
		return getChangeRecordingModel().getManager();
	}

	private boolean zijnAdressenGelijk(Persoon verzorger, Persoon deelnemer)
	{
		List<Adres> adressen = new ArrayList<Adres>();
		for (PersoonAdres curAdres : verzorger.getAdressen())
		{
			adressen.add(curAdres.getAdres());
		}
		for (PersoonAdres curAdres : deelnemer.getAdressen())
		{
			if (!adressen.remove(curAdres.getAdres()))
				return false;
		}
		return adressen.isEmpty();
	}

	private void kopieerAdressen(Persoon verzorger)
	{
		Persoon deelnemer = getRelatie().getPersoon();
		if (adressenGelijkMakenAanDeelnemer)
		{
			CopyManager copyManager = new HibernateObjectCopyManager(PersoonAdres.class);
			for (PersoonAdres persoonAdres : deelnemer.getAdressenOpPeildatum())
			{
				PersoonAdres copyPersoonAdres = copyManager.copyObject(persoonAdres);
				copyPersoonAdres.getAdres().getAdresEntiteiten().add(copyPersoonAdres);
				copyPersoonAdres.getAdres().getPersoonAdressen().add(copyPersoonAdres);
				copyPersoonAdres.setPersoon(verzorger);
				verzorger.getAdressen().add(copyPersoonAdres);
			}
		}
	}

	private AutoFieldSet<Relatie> createPersonaliaAutoFieldset(String id, boolean meerderjarig)
	{
		List<String> fields = new ArrayList<String>();
		fields.addAll(Arrays.asList("relatie.achternaam", "relatie.voorvoegsel",
			"relatie.voorletters", "relatie.roepnaam", "relatie.voornamen", "relatie.geslacht",
			"relatie.bsn", "relatie.geboorteland", "relatie.nationaliteit1", "relatieSoort"));

		if (!meerderjarig)
		{
			fields.addAll(Arrays.asList("wettelijkeVertegenwoordiger"));
		}
		fields.addAll(Arrays.asList("betalingsplichtige", "relatie.debiteurennummer",
			"relatie.bankrekeningnummer"));

		AutoFieldSet<Relatie> personalia =
			new AutoFieldSet<Relatie>(id, getChangeRecordingModel(), "Personalia");
		personalia.setPropertyNames(fields);
		personalia.setSortAccordingToPropertyNames(true);
		personalia.setRenderMode(RenderMode.EDIT);

		RelatieSoortZoekFilter filter = new RelatieSoortZoekFilter();
		filter.setPersoonOpname(true);

		personalia.addFieldModifier(new ConstructorArgModifier("relatieSoort", filter));
		personalia.addFieldModifier(new ValidateModifier(new BankrekeningElfProefValidator(),
			"relatie.bankrekeningnummer"));
		personalia.addModifier("relatie.voorletters", new HoofdletterAjaxHandler(
			HoofdletterMode.PuntSeperated));
		personalia.addModifier("relatie.achternaam", new HoofdletterAjaxHandler(
			HoofdletterMode.EersteLetterEenWoord));

		personalia.addModifier("relatie.bsn", new AjaxFormComponentValidatingBehavior("onchange"));
		personalia.addModifier("relatie.bsn", new BsnValidator());
		personalia.addModifier("relatie.bsn", new UniqueConstraintValidator<Long>(personalia,
			"relatie", "relatie.bsn", "organisatie"));

		return personalia;
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

				Persoon verzorger = getRelatie().getRelatie();
				if (verzorger.getOfficieleAchternaam() == null)
				{
					verzorger.setOfficieleAchternaam(verzorger.getAchternaam());
				}
				if (!getRelatie().isSaved())
					getRelatie().getPersoon().getRelaties().add(getRelatie());
				kopieerAdressen(verzorger);
				checkBetalingsplichtige(getRelatie());
				getChangeRecordingModel().saveObject();
				DataAccessRegistry.getHelper(BatchDataAccessHelper.class).batchExecute();
				setResponsePage(returnToPage);
			}

		});

		if (getRelatie().isSaved())
		{
			AbstractBottomRowButton ontkoppelen =
				new AbstractBottomRowButton(panel, "Relatie ontkoppelen",
					CobraKeyAction.VERWIJDEREN, ButtonAlignment.RIGHT)
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
								Relatie relatie = getRelatie();

								relatie.getPersoon().getRelaties().remove(relatie);
								relatie.delete();
								relatie.commit();

								setResponsePage(new DeelnemerRelatiesOverzichtPage(returnToPage
									.getContextDeelnemerModel()));
							}
						};
					}
				};

			panel.addButton(ontkoppelen);
		}

		panel.addButton(new AnnulerenButton(panel, returnToPage));
		panel.setDefaultModel(returnToPage.getDefaultModel());
	}

	private void checkBetalingsplichtige(Relatie nieuweRelatie)
	{
		if (nieuweRelatie.isBetalingsplichtige())
		{
			for (AbstractRelatie relatie : nieuweRelatie.getDeelnemer().getRelaties())
			{
				if (relatie.isBetalingsplichtige() && !relatie.equals(nieuweRelatie))
				{
					relatie.setBetalingsplichtige(false);
					relatie.save();
				}
			}
		}
	}

	@Override
	public void onDetach()
	{
		super.onDetach();
		returnToPage.detach();
	}

	public Relatie getRelatie()
	{
		return (Relatie) getDefaultModelObject();
	}

	public SecurePage getReturnTopage()
	{
		return this.returnToPage;
	}
}
