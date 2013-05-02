package nl.topicus.eduarte.krd.web.pages.deelnemer.onderwijs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.dao.BatchDataAccessHelper;
import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.modelsv2.DefaultModelManager;
import nl.topicus.cobra.modelsv2.IChangeRecordingModel;
import nl.topicus.cobra.modelsv2.ModelFactory;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.util.ComponentUtil;
import nl.topicus.cobra.web.components.panels.bottomrow.AjaxActieButton;
import nl.topicus.cobra.web.components.panels.bottomrow.AnnulerenButton;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.cobra.web.components.panels.bottomrow.ButtonAlignment;
import nl.topicus.cobra.web.components.panels.bottomrow.OpslaanButton;
import nl.topicus.cobra.web.components.panels.bottomrow.VerwijderButton;
import nl.topicus.cobra.web.components.text.DatumField;
import nl.topicus.cobra.web.components.text.RequiredDatumField;
import nl.topicus.cobra.web.validators.BegindatumVoorEinddatumValidator;
import nl.topicus.eduarte.app.EduArteApp;
import nl.topicus.eduarte.app.EduArteModuleKey;
import nl.topicus.eduarte.dao.helpers.OnderwijsproductAfnameDataAccessHelper;
import nl.topicus.eduarte.dao.helpers.ProductregelDataAccessHelper;
import nl.topicus.eduarte.entities.inschrijving.OnderwijsproductAfname;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.entities.onderwijsproduct.Onderwijsproduct;
import nl.topicus.eduarte.entities.onderwijsproduct.OnderwijsproductAfnameContext;
import nl.topicus.eduarte.entities.organisatie.ExterneOrganisatie;
import nl.topicus.eduarte.entities.personen.Deelnemer;
import nl.topicus.eduarte.entities.productregel.Productregel;
import nl.topicus.eduarte.entities.productregel.Productregel.TypeProductregel;
import nl.topicus.eduarte.krd.dao.helpers.BronDataAccessHelper;
import nl.topicus.eduarte.krd.entities.bron.meldingen.aanlevering.BronBveAanleverRecord;
import nl.topicus.eduarte.krd.principals.deelnemer.onderwijsproduct.DeelnemerOnderwijsproductenWrite;
import nl.topicus.eduarte.krd.web.components.modalwindow.productregel.SelecteerProductregelModalWindow;
import nl.topicus.eduarte.web.components.choice.CohortCombobox;
import nl.topicus.eduarte.web.components.choice.VrijstellingTypeCombobox;
import nl.topicus.eduarte.web.components.menu.DeelnemerMenuItem;
import nl.topicus.eduarte.web.components.quicksearch.externeorganisatie.ExterneOrganisatieSearchEditor;
import nl.topicus.eduarte.web.pages.IModuleEditPage;
import nl.topicus.eduarte.web.pages.deelnemer.AbstractDeelnemerPage;
import nl.topicus.eduarte.web.pages.deelnemer.onderwijs.DeelnemerAfgenomenOnderwijsproductenPage;
import nl.topicus.eduarte.zoekfilters.ExterneOrganisatieZoekFilter;
import nl.topicus.eduarte.zoekfilters.OnderwijsproductAfnameZoekFilter;
import nl.topicus.eduarte.zoekfilters.ProductregelZoekFilter;

import org.apache.wicket.Page;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.form.validation.IFormValidator;
import org.apache.wicket.markup.html.link.IPageLink;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.validation.validator.RangeValidator;

/*
 * @author vandekamp
 */
@PageInfo(title = "Onderwijsproductafname bewerken", menu = {
	"Deelnemer > [deelnemer] > Afg. onderwijsproducten > Bewerken",
	"Groep > [groep] > [deelnemer] >  Afg. onderwijsproducten > Bewerken"})
@InPrincipal(DeelnemerOnderwijsproductenWrite.class)
public class OnderwijsproductAfnameEditPage extends AbstractDeelnemerPage implements
		IModuleEditPage<Verbintenis>
{

	private SelecteerProductregelModalWindow modalWindow;

	private final Form<OnderwijsproductAfname> form;

	private final DatumField begindatumField;

	private final DatumField einddatumField;

	private final CohortCombobox cohortCombo;

	private IChangeRecordingModel<OnderwijsproductAfname> afnameModel;

	private class ProductregelListModel extends LoadableDetachableModel<List<Productregel>>
	{
		private static final long serialVersionUID = 1L;

		@Override
		protected List<Productregel> load()
		{
			ProductregelDataAccessHelper helper =
				DataAccessRegistry.getHelper(ProductregelDataAccessHelper.class);
			if (getContextVerbintenis().getOpleiding() == null)
				return Collections.emptyList();
			ProductregelZoekFilter filter =
				new ProductregelZoekFilter(getContextVerbintenis().getOpleiding(),
					getContextVerbintenis().getCohort());
			filter.addOrderByProperty("soortProductregel");
			List<Productregel> ret = new ArrayList<Productregel>();
			for (Productregel regel : helper.list(filter))
			{
				if (regel.getTypeProductregel() == TypeProductregel.Productregel)
				{
					if (!getContextVerbintenis().heeftKeuzeVoorProductregel(regel))
					{
						if (regel.getOnderwijsproducten(getContextVerbintenis().getOpleiding())
							.contains(afnameModel.getObject().getOnderwijsproduct()))
						{
							ret.add(regel);
						}
					}
				}
			}
			return ret;
		}
	}

	private final class OnderwijsproductAfnameUniqueValidator implements IFormValidator
	{
		private static final long serialVersionUID = 1L;

		@Override
		public FormComponent< ? >[] getDependentFormComponents()
		{
			return new FormComponent[] {begindatumField, einddatumField, cohortCombo};
		}

		@Override
		public void validate(Form< ? > validateForm)
		{
			// Controleer dat er niet al een andere onderwijsproductafname voor deze
			// deelnemer bestaat met het geselecteerde onderwijsproduct en cohort.
			OnderwijsproductAfname afname = (OnderwijsproductAfname) validateForm.getModelObject();
			OnderwijsproductAfnameZoekFilter filter =
				new OnderwijsproductAfnameZoekFilter(getContextDeelnemer());
			filter.setOnderwijsproduct(afname.getOnderwijsproduct());
			filter.setCohort(afname.getCohort());
			List<OnderwijsproductAfname> afnames =
				DataAccessRegistry.getHelper(OnderwijsproductAfnameDataAccessHelper.class).list(
					filter);
			if (afnames.size() > 0)
			{
				// Door unique constraint kan het max 1 zijn.
				if (!afnames.get(0).getId().equals(afname.getId()))
				{
					validateForm.error("Het onderwijsproduct "
						+ afname.getOnderwijsproduct().getTitel()
						+ " wordt al afgenomen in het cohort " + afname.getCohort().getNaam());
				}
			}
		}

	}

	public OnderwijsproductAfnameEditPage(Verbintenis inschrijving, OnderwijsproductAfname afname)
	{
		super(DeelnemerMenuItem.AfgOnderwijsproducten, inschrijving.getDeelnemer(), inschrijving);
		afnameModel =
			ModelFactory.getCompoundChangeRecordingModel(afname, new DefaultModelManager(
				OnderwijsproductAfnameContext.class, OnderwijsproductAfname.class));
		form = new Form<OnderwijsproductAfname>("afnameForm", afnameModel);
		form.add(new OnderwijsproductAfnameUniqueValidator());
		form.add(new Label("onderwijsproduct.titel"));
		form.add(cohortCombo = new CohortCombobox("cohort"));
		begindatumField = new RequiredDatumField("begindatum");
		form.add(begindatumField);
		einddatumField = new DatumField("einddatum");
		form.add(einddatumField);
		form.add(new BegindatumVoorEinddatumValidator(begindatumField, einddatumField));
		WebMarkupContainer werkstuktitelContainer =
			new WebMarkupContainer("werkstuktitelContainer");
		werkstuktitelContainer.setVisible(afname.getOnderwijsproduct().isHeeftWerkstuktitel());
		form.add(werkstuktitelContainer);
		werkstuktitelContainer.add(ComponentUtil.fixLength(new TextField<String>("werkstuktitel"),
			OnderwijsproductAfname.class));
		form.add(new VrijstellingTypeCombobox("vrijstellingType"));

		WebMarkupContainer wmc = new WebMarkupContainer("creditsContainer");
		FormComponent<Integer> credits =
			ComponentUtil.fixLength(new TextField<Integer>("credits", Integer.class)
				.add(new RangeValidator<Integer>(0, 300)), Onderwijsproduct.class);
		wmc.add(credits);
		form.add(wmc.setVisible(EduArteApp.get().isModuleActive(EduArteModuleKey.HOGER_ONDERWIJS)));

		form.add(new ExterneOrganisatieSearchEditor("externeOrganisatie",
			new PropertyModel<ExterneOrganisatie>(afnameModel, "externeOrganisatie"),
			new ExterneOrganisatieZoekFilter()));

		add(form);
		createComponents();
		modalWindow =
			new SelecteerProductregelModalWindow("modalwindow", new ProductregelListModel());
		modalWindow.setWindowClosedCallback(new ModalWindow.WindowClosedCallback()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void onClose(AjaxRequestTarget target)
			{
				if (modalWindow.getDefaultModelObject() == null)
					return;
				Productregel productregel = (Productregel) modalWindow.getDefaultModelObject();
				OnderwijsproductAfnameContext context = new OnderwijsproductAfnameContext();
				context.setProductregel(productregel);
				context.setOnderwijsproductAfname(afnameModel.getObject());
				context.setVerbintenis(getContextVerbintenis());
				getContextVerbintenis().getAfnameContexten().add(context);
				afnameModel.getObject().getAfnameContexten().add(context);
				context.save();
				context.commit();
				afnameModel.recalculate();
				info("Onderwijsproduct gekoppeld aan productregel " + productregel.getNaam());
				refreshFeedback(target);
				refreshBottomRow(target);
			}
		});
		form.add(modalWindow);
	}

	@Override
	protected void fillBottomRow(BottomRowPanel panel)
	{
		panel.addButton(new AjaxActieButton(panel, "Koppelen aan productregel")
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onClick(AjaxRequestTarget target)
			{
				modalWindow.show(target);
			}

			@Override
			public boolean isVisible()
			{
				OnderwijsproductAfname afname = afnameModel.getObject();
				return !getContextVerbintenis().heeftOnderwijsproductGekozenBinnenVerbintenis(
					afname.getOnderwijsproduct());
			}
		});
		panel.addButton(new AjaxActieButton(panel, "Loskoppelen van productregel")
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onClick(AjaxRequestTarget target)
			{
				OnderwijsproductAfname afname = afnameModel.getObject();
				OnderwijsproductAfnameContext context =
					getContextVerbintenis().getAfnameContext(afname);
				if (context != null)
				{
					List<BronBveAanleverRecord> records =
						DataAccessRegistry.getHelper(BronDataAccessHelper.class)
							.getAanleverRecords(context);
					for (BronBveAanleverRecord record : records)
					{
						record.setAfnameContext(null);
						record.update();
					}
					getContextVerbintenis().getAfnameContexten().remove(context);
					afname.getAfnameContexten().remove(context);
					context.delete();
					getContextVerbintenis().update();
					afname.update();
					context.commit();
					afnameModel.recalculate();
					info("Onderwijsproductafname losgekoppeld van productregel "
						+ context.getProductregel().getNaam());
					refreshBottomRow(target);
					refreshFeedback(target);
				}
			}

			@Override
			public boolean isVisible()
			{
				OnderwijsproductAfname afname = afnameModel.getObject();
				return getContextVerbintenis().getAfnameContext(afname) != null;
			}
		}.setAlignment(ButtonAlignment.LEFT));
		panel.addButton(new VerwijderButton(panel)
		{
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isVisible()
			{
				return true;
			}

			@Override
			protected void onClick()
			{
				OnderwijsproductAfname afname = afnameModel.getObject();
				for (OnderwijsproductAfnameContext context : afname.getAfnameContexten())
				{
					context.getVerbintenis().getAfnameContexten().remove(context);
				}
				Deelnemer deelnemer = afnameModel.getObject().getDeelnemer();
				afnameModel.deleteObject();
				DataAccessRegistry.getHelper(BatchDataAccessHelper.class).batchExecute();
				setResponsePage(new DeelnemerAfgenomenOnderwijsproductenPage(deelnemer,
					getContextVerbintenis()));
			}
		});
		panel.addButton(new OpslaanButton(panel, form)
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void onSubmit()
			{
				afnameModel.saveObject();
				DataAccessRegistry.getHelper(BatchDataAccessHelper.class).batchExecute();
				setResponsePage(new DeelnemerAfgenomenOnderwijsproductenPage(getContextDeelnemer(),
					getContextVerbintenis()));
			}

		});

		panel.addButton(new AnnulerenButton(panel, new IPageLink()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public Page getPage()
			{
				return new DeelnemerAfgenomenOnderwijsproductenPage(getContextDeelnemer(),
					getContextVerbintenis());
			}

			@Override
			public Class< ? extends Page> getPageIdentity()
			{
				return DeelnemerAfgenomenOnderwijsproductenPage.class;
			}

		}));
		super.fillBottomRow(panel);
	}

	@Override
	public void detachModels()
	{
		ComponentUtil.detachQuietly(afnameModel);
		super.detachModels();
	}
}