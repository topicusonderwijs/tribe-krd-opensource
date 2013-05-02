package nl.topicus.eduarte.krd.web.pages.beheer;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.modelsv2.DefaultModelManager;
import nl.topicus.cobra.modelsv2.ModelFactory;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.transformers.HoofdletterMode;
import nl.topicus.cobra.web.behaviors.HoofdletterAjaxHandler;
import nl.topicus.cobra.web.components.form.AutoFieldSet;
import nl.topicus.cobra.web.components.form.RenderMode;
import nl.topicus.cobra.web.components.form.modifier.EnableModifier;
import nl.topicus.cobra.web.components.panels.bottomrow.AnnulerenButton;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.cobra.web.components.panels.bottomrow.OpslaanButton;
import nl.topicus.cobra.web.validators.UniqueConstraintFormValidator;
import nl.topicus.eduarte.app.EduArteRequestCycle;
import nl.topicus.eduarte.dao.helpers.BPVInschrijvingDataAccessHelper;
import nl.topicus.eduarte.dao.helpers.VerbintenisDataAccessHelper;
import nl.topicus.eduarte.entities.inschrijving.RedenUitschrijving;
import nl.topicus.eduarte.krd.principals.beheer.systeemtabellen.RedenUitschrijvingPrincipal;
import nl.topicus.eduarte.web.components.autoform.EduArteAjaxRefreshModifier;
import nl.topicus.eduarte.web.components.menu.BeheerMenuItem;
import nl.topicus.eduarte.web.components.panels.bottomrow.ProbeerTeVerwijderenButton;
import nl.topicus.eduarte.web.pages.IModuleEditPage;
import nl.topicus.eduarte.web.pages.SecurePage;
import nl.topicus.eduarte.web.pages.beheer.AbstractBeheerPage;
import nl.topicus.eduarte.zoekfilters.BPVInschrijvingZoekFilter;
import nl.topicus.eduarte.zoekfilters.OrganisatieEenheidLocatieAuthorizationContext;
import nl.topicus.eduarte.zoekfilters.VerbintenisZoekFilter;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.security.checks.AlwaysGrantedSecurityCheck;

@PageInfo(title = "Reden uitschrijving", menu = "Beheer > Reden uitschrijving > [Reden uitschrijving]")
@InPrincipal(RedenUitschrijvingPrincipal.class)
public class RedenUitschrijvingEditPage extends AbstractBeheerPage<RedenUitschrijving> implements
		IModuleEditPage<RedenUitschrijving>
{
	private Form<Void> form;

	private AutoFieldSet<RedenUitschrijving> fieldSet;

	/**
	 * Constructor voor de ToevoegenButton.
	 * 
	 * @param returnPage
	 */
	public RedenUitschrijvingEditPage(SecurePage returnPage)
	{
		this(ModelFactory.getModel(new RedenUitschrijving()), returnPage);
	}

	/**
	 * Constructor voor de BewerkenButton.
	 * 
	 * @param redenUitschrijvingModel
	 * @param returnPage
	 */
	public RedenUitschrijvingEditPage(IModel<RedenUitschrijving> redenUitschrijvingModel,
			SecurePage returnPage)
	{
		super(ModelFactory.getCompoundChangeRecordingModel(redenUitschrijvingModel.getObject(),
			new DefaultModelManager(RedenUitschrijving.class)), BeheerMenuItem.RedenUitschrijving);
		setReturnPage(returnPage);
		createForm();
	}

	private void createForm()
	{
		form = new Form<Void>("form");
		add(form);

		fieldSet =
			new AutoFieldSet<RedenUitschrijving>("redenUitschrijving", getContextModel(),
				"Reden uitschrijving");
		fieldSet.setPropertyNames("code", "naam", "actief", "redenUitval", "uitstroomredenWI",
			"overlijden", "geslaagd", "tonenBijVerbintenis", "tonenBijBPV");
		fieldSet.setSortAccordingToPropertyNames(true);
		fieldSet.setRenderMode(RenderMode.EDIT);
		form.add(fieldSet);

		fieldSet.addModifier("code", new HoofdletterAjaxHandler(HoofdletterMode.Alles));

		UniqueConstraintFormValidator uniqueValidator =
			new UniqueConstraintFormValidator(fieldSet, "reden overlijden", "overlijden")
			{
				private static final long serialVersionUID = 1L;

				@Override
				@SuppressWarnings("hiding")
				public void validate(Form< ? > form)
				{
					if (getEditableFormComponent().getConvertedInput() != null
						&& ((Boolean) getEditableFormComponent().getConvertedInput()))
						super.validate(form);
				}

			};
		uniqueValidator.setProperties("actief");

		fieldSet.addFieldModifier(new EnableModifier(redenIsInGebruikOverlijden(), "overlijden"));

		fieldSet.addFieldModifier(new EnableModifier(new LoadableDetachableModel<Boolean>()
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected Boolean load()
			{
				RedenUitschrijving ru = (RedenUitschrijving) getDefaultModelObject();
				return !ru.isOverlijden();
			}
		}, "actief"));

		fieldSet.addFieldModifier(new EduArteAjaxRefreshModifier("overlijden")
		{

			private static final long serialVersionUID = 1L;

			@Override
			protected void onUpdate(AjaxRequestTarget target)
			{
				RedenUitschrijving ru =
					(RedenUitschrijving) RedenUitschrijvingEditPage.this.getDefaultModelObject();
				if (ru.isOverlijden())
					ru.setActief(true);
				target.addComponent(fieldSet.findFieldComponent("actief"));
			}
		});

		form.add(uniqueValidator);

		createComponents();
	}

	private boolean redenIsInGebruikOverlijden()
	{
		RedenUitschrijving redenUitschrijving = (RedenUitschrijving) getDefaultModelObject();

		BPVInschrijvingZoekFilter bpvInschrijvingZoekFilter = new BPVInschrijvingZoekFilter();
		bpvInschrijvingZoekFilter.setRedenUitschrijving(redenUitschrijving);
		bpvInschrijvingZoekFilter.setPeildatum(null);
		bpvInschrijvingZoekFilter
			.setAuthorizationContext(new OrganisatieEenheidLocatieAuthorizationContext(
				new AlwaysGrantedSecurityCheck()));
		BPVInschrijvingDataAccessHelper bpvHelper =
			DataAccessRegistry.getHelper(BPVInschrijvingDataAccessHelper.class);

		VerbintenisZoekFilter verbintenisFilter = new VerbintenisZoekFilter();
		verbintenisFilter.setRedenUitschrijving(redenUitschrijving);
		verbintenisFilter.setPeildatum(null);
		verbintenisFilter
			.setAuthorizationContext(new OrganisatieEenheidLocatieAuthorizationContext(
				new AlwaysGrantedSecurityCheck()));

		VerbintenisDataAccessHelper verbintenisHelper =
			DataAccessRegistry.getHelper(VerbintenisDataAccessHelper.class);

		return !redenUitschrijving.isOverlijden()
			|| (bpvHelper.list(bpvInschrijvingZoekFilter).isEmpty() && verbintenisHelper.list(
				verbintenisFilter).isEmpty());
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
				RedenUitschrijving reden =
					(RedenUitschrijving) RedenUitschrijvingEditPage.this.getDefaultModelObject();
				reden.saveOrUpdate();
				reden.commit();

				EduArteRequestCycle.get().setResponsePage(
					RedenUitschrijvingEditPage.this.getReturnPageClass());
			}
		});

		panel.addButton(new AnnulerenButton(panel, RedenUitschrijvingEditPage.this
			.getReturnPageClass()));

		panel.addButton(new ProbeerTeVerwijderenButton(panel, getContextModel(),
			"deze reden uitschrijving", RedenUitschrijvingZoekenPage.class));
	}

	@Override
	public Component createTitle(String id)
	{
		return new Label(id, new PropertyModel<String>(getDefaultModel(), "naam"));
	}
}
