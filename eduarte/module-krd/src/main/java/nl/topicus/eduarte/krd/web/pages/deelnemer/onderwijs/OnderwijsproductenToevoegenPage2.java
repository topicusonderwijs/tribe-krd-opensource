package nl.topicus.eduarte.krd.web.pages.deelnemer.onderwijs;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.dao.BatchDataAccessHelper;
import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.modelsv2.DefaultModelManager;
import nl.topicus.cobra.modelsv2.ModelFactory;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.util.ComponentUtil;
import nl.topicus.cobra.web.components.panels.bottomrow.AnnulerenButton;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.cobra.web.components.panels.bottomrow.OpslaanButton;
import nl.topicus.cobra.web.components.text.DatumField;
import nl.topicus.cobra.web.validators.BegindatumVoorEinddatumValidator;
import nl.topicus.cobra.web.validators.DatumGroterOfGelijkDatumValidator;
import nl.topicus.cobra.web.validators.DatumKleinerOfGelijkDatumValidator;
import nl.topicus.eduarte.app.security.actions.Instelling;
import nl.topicus.eduarte.app.security.actions.OrganisatieEenheid;
import nl.topicus.eduarte.app.security.actions.SearchImplementsActions;
import nl.topicus.eduarte.dao.helpers.OnderwijsproductAfnameDataAccessHelper;
import nl.topicus.eduarte.entities.inschrijving.OnderwijsproductAfname;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.entities.landelijk.Cohort;
import nl.topicus.eduarte.entities.onderwijsproduct.Onderwijsproduct;
import nl.topicus.eduarte.entities.personen.Deelnemer;
import nl.topicus.eduarte.krd.principals.deelnemer.onderwijsproduct.DeelnemerOnderwijsproductenWrite;
import nl.topicus.eduarte.web.components.choice.CohortCombobox;
import nl.topicus.eduarte.web.pages.AbstractDynamicContextPage;
import nl.topicus.eduarte.web.pages.IModuleEditPage;
import nl.topicus.eduarte.web.pages.SecurePage;
import nl.topicus.eduarte.web.pages.SubpageContext;
import nl.topicus.eduarte.web.pages.deelnemer.onderwijs.DeelnemerAfgenomenOnderwijsproductenPage;
import nl.topicus.eduarte.zoekfilters.OnderwijsproductAfnameZoekFilter;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

/**
 * @author vandekamp
 */
@PageInfo(title = "Onderwijsproducten toevoegen stap 2", menu = {"Deelnemer > [deelnemer] > Onderwijs -> Afgenomen onderwijsproducten -> Toevoegen -> Volgende"})
@InPrincipal(DeelnemerOnderwijsproductenWrite.class)
@SearchImplementsActions( {Instelling.class, OrganisatieEenheid.class})
public class OnderwijsproductenToevoegenPage2 extends AbstractDynamicContextPage<Void> implements
		IModuleEditPage<Verbintenis>
{
	private static final long serialVersionUID = 1L;

	private SecurePage returnPage;

	private Form<Void> form;

	private IModel<List<OnderwijsproductAfname>> afnameListModel;

	private IModel<Verbintenis> verbintenisModel;

	public OnderwijsproductenToevoegenPage2(Verbintenis verbintenis,
			OnderwijsproductenToevoegenPage1 returnPage, List<Onderwijsproduct> selection)
	{
		super(new SubpageContext(returnPage));
		this.returnPage = returnPage;
		this.verbintenisModel = ModelFactory.getModel(verbintenis);

		afnameListModel = createOnderwijsproductAfnameListModel(selection);
		form = new Form<Void>("form");
		form.add(new ListView<OnderwijsproductAfname>("afnameList", afnameListModel)
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(ListItem<OnderwijsproductAfname> item)
			{
				Onderwijsproduct onderwijspr = item.getModelObject().getOnderwijsproduct();

				item.add(new Label("onderwijsproduct.code", new PropertyModel<String>(item
					.getModel(), "onderwijsproduct.code")));
				item.add(new Label("onderwijsproduct.titel", new PropertyModel<String>(item
					.getModel(), "onderwijsproduct.titel")));

				DatumField begindatum =
					new DatumField("begindatum", new PropertyModel<Date>(item.getModel(),
						"begindatum"));
				begindatum.setRequired(true);
				item.add(begindatum);

				DatumField einddatum =
					new DatumField("einddatum", new PropertyModel<Date>(item.getModel(),
						"einddatum"));
				item.add(einddatum);

				addValidaties(onderwijspr, begindatum, einddatum);

				CohortCombobox cohortCombo =
					new CohortCombobox("cohort", new PropertyModel<Cohort>(item.getModel(),
						"cohort"), onderwijspr.getBegindatum(), onderwijspr.getEinddatum());
				cohortCombo.setNullValid(false).setRequired(true);
				item.add(cohortCombo);
			}
		}.setReuseItems(true));
		add(form);
		createComponents();
	}

	protected void addValidaties(Onderwijsproduct onderwijspr, DatumField begindatum,
			DatumField einddatum)
	{
		form.add(new DatumGroterOfGelijkDatumValidator(begindatum, onderwijspr.getBegindatum()));
		form.add(new DatumKleinerOfGelijkDatumValidator(begindatum, onderwijspr.getEinddatum()));
		form.add(new DatumGroterOfGelijkDatumValidator(einddatum, onderwijspr.getBegindatum()));
		form.add(new DatumKleinerOfGelijkDatumValidator(einddatum, onderwijspr.getEinddatum()));
		form.add(new BegindatumVoorEinddatumValidator(begindatum, einddatum));
	}

	private IModel<List<OnderwijsproductAfname>> createOnderwijsproductAfnameListModel(
			List<Onderwijsproduct> selection)
	{
		List<OnderwijsproductAfname> afnames = new ArrayList<OnderwijsproductAfname>();
		for (Onderwijsproduct onderwijsproduct : selection)
		{
			OnderwijsproductAfname afname = new OnderwijsproductAfname();
			afname.setOnderwijsproduct(onderwijsproduct);
			afname.setDeelnemer(getContextDeelnemer());
			afname.setCohort(getContextVerbintenis().getCohort());
			afname.setBegindatum(getContextVerbintenis().getBegindatum());
			afnames.add(afname);
		}
		return ModelFactory.getListModel(afnames, new DefaultModelManager(
			OnderwijsproductAfname.class));
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
				OnderwijsproductAfnameZoekFilter filter =
					new OnderwijsproductAfnameZoekFilter(getContextDeelnemer());
				List<OnderwijsproductAfname> bestaandeAfnamen =
					DataAccessRegistry.getHelper(OnderwijsproductAfnameDataAccessHelper.class)
						.list(filter);

				for (OnderwijsproductAfname afname : afnameListModel.getObject())
				{
					if (!afnameBestaat(afname, bestaandeAfnamen))
						afname.saveOrUpdate();
				}
				DataAccessRegistry.getHelper(BatchDataAccessHelper.class).batchExecute();
				setResponsePage(new DeelnemerAfgenomenOnderwijsproductenPage(
					getContextVerbintenis()));
			}
		});
		panel.addButton(new AnnulerenButton(panel, returnPage));
	}

	protected boolean afnameBestaat(OnderwijsproductAfname afname,
			List<OnderwijsproductAfname> bestaandeAfnamen)
	{
		for (OnderwijsproductAfname bestAfname : bestaandeAfnamen)
		{
			if (bestAfname.getOnderwijsproduct().equals(afname.getOnderwijsproduct())
				&& bestAfname.getCohort().equals(afname.getCohort()))
				return true;
		}
		return false;
	}

	private Deelnemer getContextDeelnemer()
	{
		return getContextVerbintenis().getDeelnemer();
	}

	private Verbintenis getContextVerbintenis()
	{
		return verbintenisModel.getObject();
	}

	@Override
	protected void onDetach()
	{
		ComponentUtil.detachQuietly(afnameListModel);
		ComponentUtil.detachQuietly(verbintenisModel);
		ComponentUtil.detachQuietly(returnPage);
		super.onDetach();
	}
}
