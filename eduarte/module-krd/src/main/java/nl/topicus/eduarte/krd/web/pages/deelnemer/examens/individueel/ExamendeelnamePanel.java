package nl.topicus.eduarte.krd.web.pages.deelnemer.examens.individueel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;

import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.dataproviders.CollectionDataProvider;
import nl.topicus.cobra.dataproviders.IModelDataProvider;
import nl.topicus.cobra.modelsv2.DefaultModelManager;
import nl.topicus.cobra.modelsv2.ExtendedHibernateModel;
import nl.topicus.cobra.util.TimeUtil;
import nl.topicus.cobra.web.behaviors.AjaxFormComponentSaveBehaviour;
import nl.topicus.cobra.web.components.ComponentFactory;
import nl.topicus.cobra.web.components.choice.JaNeeCombobox;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanel;
import nl.topicus.cobra.web.components.datapanel.columns.PanelColumn;
import nl.topicus.cobra.web.components.panels.TypedPanel;
import nl.topicus.cobra.web.components.text.DatumField;
import nl.topicus.eduarte.dao.helpers.OnderwijsproductAfnameContextDataAccessHelper;
import nl.topicus.eduarte.dao.helpers.ProductregelDataAccessHelper;
import nl.topicus.eduarte.entities.examen.Examendeelname;
import nl.topicus.eduarte.entities.examen.ExamenstatusOvergang;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.entities.onderwijsproduct.OnderwijsproductAfnameContext;
import nl.topicus.eduarte.entities.productregel.Productregel;
import nl.topicus.eduarte.krd.web.components.panels.datapanel.table.ExamenstatusOvergangTable;
import nl.topicus.eduarte.krd.web.validators.MinimumDateValidator;
import nl.topicus.eduarte.web.components.panels.EduArteDataPanel;
import nl.topicus.eduarte.web.components.panels.columns.EindresultaatColumnPanel;
import nl.topicus.eduarte.web.components.panels.columns.OndPrAfnKeuzeColumnPanel;
import nl.topicus.eduarte.web.components.panels.columns.VolgendTijdvakColumnPanel;
import nl.topicus.eduarte.web.components.panels.columns.WerkstuktitelColumnPanel;
import nl.topicus.eduarte.web.components.panels.datapanel.table.ProductregelTable;
import nl.topicus.eduarte.web.pages.deelnemer.AbstractDeelnemerPage;
import nl.topicus.eduarte.zoekfilters.ProductregelZoekFilter;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.form.validation.AbstractFormValidator;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.validation.validator.RangeValidator;

/**
 * Panel met de gegevens van een examendeelname van een deelnemer.
 * 
 * @author loite
 * 
 */
public class ExamendeelnamePanel extends TypedPanel<Examendeelname>
{
	private static final long serialVersionUID = 1L;

	private Form<Void> form;

	private DatumField datumField;

	private TextField<Integer> examenjaar;

	private final class ProductregelsModel extends LoadableDetachableModel<List<Productregel>>
	{
		private static final long serialVersionUID = 1L;

		@Override
		protected List<Productregel> load()
		{
			if (getExamendeelname() != null)
			{
				ProductregelDataAccessHelper helper =
					DataAccessRegistry.getHelper(ProductregelDataAccessHelper.class);
				ProductregelZoekFilter filter =
					new ProductregelZoekFilter(getExamendeelname().getVerbintenis().getOpleiding(),
						getExamendeelname().getVerbintenis().getCohort());
				filter.addOrderByProperty("soortProductregel");
				List<Productregel> regels = helper.list(filter);
				List<Productregel> res = new ArrayList<Productregel>(regels.size());
				Map<Productregel, OnderwijsproductAfnameContext> keuzes =
					DataAccessRegistry.getHelper(
						OnderwijsproductAfnameContextDataAccessHelper.class).list(
						getExamendeelname().getVerbintenis());
				for (Productregel regel : regels)
				{
					if (regel.isVerplicht() || keuzes.get(regel) != null)
					{
						res.add(regel);
					}
				}
				return res;
			}
			return Collections.emptyList();
		}
	}

	private final class ListModel extends LoadableDetachableModel<List<ExamenstatusOvergang>>
	{
		private static final long serialVersionUID = 1L;

		@Override
		protected List<ExamenstatusOvergang> load()
		{
			if (getExamendeelname() == null)
				return Collections.emptyList();
			List<ExamenstatusOvergang> res =
				new ArrayList<ExamenstatusOvergang>(getExamendeelname().getStatusovergangen());
			Collections.sort(res, new Comparator<ExamenstatusOvergang>()
			{
				@Override
				public int compare(ExamenstatusOvergang o1, ExamenstatusOvergang o2)
				{
					return o2.getDatumTijd().compareTo(o1.getDatumTijd());
				}
			});
			return res;
		}

	}

	public ExamendeelnamePanel(String id, Examendeelname examendeelname, boolean write,
			Form<Void> form)
	{
		super(id, new CompoundPropertyModel<Examendeelname>(
			new ExtendedHibernateModel<Examendeelname>(examendeelname, new DefaultModelManager(
				Examendeelname.class))));
		this.form = form;
		add(ComponentFactory.getDataLabel("verbintenis.opleiding.naam"));
		add(ComponentFactory.getDataLabel("examenstatus.naam"));
		WebMarkupContainer readContainer = new WebMarkupContainer("read");
		readContainer.setRenderBodyOnly(true).setVisible(!write);
		readContainer.add(ComponentFactory.getDataLabel("datumUitslag"));
		readContainer.add(ComponentFactory.getDataLabel("examenjaar"));
		readContainer.add(ComponentFactory.getDataLabel("bekostigdOmschrijving"));
		readContainer.add(ComponentFactory.getDataLabel("examennummerMetPrefix"));
		readContainer.add(ComponentFactory.getDataLabel("tijdvak"));
		readContainer.add(ComponentFactory.getDataLabel("meenemenInVolgendeBronBatchOmsch"));
		readContainer.add(ComponentFactory.getDataLabel("bronStatus"));
		readContainer.add(ComponentFactory.getDataLabel("bronDatum"));
		add(readContainer);

		WebMarkupContainer writeContainer = new WebMarkupContainer("write");
		writeContainer.setRenderBodyOnly(true).setVisible(write);
		examenjaar = new TextField<Integer>("examenjaar", Integer.class);
		examenjaar.add(new AjaxFormComponentSaveBehaviour());
		examenjaar.setOutputMarkupId(true);
		writeContainer.add(examenjaar);
		datumField = new DatumField("datumUitslag")
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onUpdate(AjaxRequestTarget target, Date newValue)
			{
				if (newValue != null)
				{
					examenjaar.setModelObject(TimeUtil.getInstance().getYear(newValue));
					target.addComponent(examenjaar);
				}
			}
		};

		writeContainer.add(datumField);

		if (examendeelname != null)
		{
			Verbintenis verbintenis = examendeelname.getVerbintenis();
			if (verbintenis != null)
			{
				MinimumDateValidator validator =
					new MinimumDateValidator(examendeelname.getVerbintenis().getBegindatum());
				datumField.add(validator);
			}
		}

		writeContainer.add(new JaNeeCombobox("bekostigd"));
		writeContainer.add(new TextField<String>("examennummerPrefix"));
		writeContainer.add(new TextField<Integer>("examennummer", Integer.class));
		writeContainer.add(new TextField<Integer>("tijdvak", Integer.class)
			.add(new RangeValidator<Integer>(1, 3)));
		add(writeContainer);

		addExamenjaarDatumUitslagFormValidator();

		IModel<List<Productregel>> productRegels = new ProductregelsModel();
		IModelDataProvider<Productregel> keuzeprovider =
			new IModelDataProvider<Productregel>(productRegels);
		ProductregelTable table = new ProductregelTable();
		table.addColumn(new PanelColumn<Productregel>("Keuze", "Keuze")
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected Panel getPanel(String componentId, WebMarkupContainer row,
					IModel<Productregel> model)
			{
				return new OndPrAfnKeuzeColumnPanel(componentId, model,
					((AbstractDeelnemerPage) getPage()).getContextVerbintenisModel());
			}
		});
		table.addColumn(new PanelColumn<Productregel>("Resultaat", "Resultaat")
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected Panel getPanel(String componentId, WebMarkupContainer row,
					IModel<Productregel> model)
			{
				return new EindresultaatColumnPanel(componentId, model,
					((AbstractDeelnemerPage) getPage()).getContextVerbintenisModel());
			}
		});
		table.addColumn(new PanelColumn<Productregel>("Werkstuktitel", "Werkstuktitel")
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected Panel getPanel(String componentId, WebMarkupContainer row,
					IModel<Productregel> model)
			{
				return new WerkstuktitelColumnPanel(componentId, model,
					((AbstractDeelnemerPage) getPage()).getContextVerbintenisModel());
			}
		});
		if (examendeelname != null
			&& (examendeelname.getVerbintenis().isVOVerbintenis() || examendeelname
				.getVerbintenis().isVAVOVerbintenis()))
		{
			table.addColumn(new PanelColumn<Productregel>("Verwezen naar volgend tijdvak",
				"Volg. tijdvak")
			{
				private static final long serialVersionUID = 1L;

				@Override
				protected Panel getPanel(String componentId, WebMarkupContainer row,
						IModel<Productregel> model)
				{
					return new VolgendTijdvakColumnPanel(componentId, model,
						((AbstractDeelnemerPage) getPage()).getContextVerbintenisModel());
				}
			});
		}
		CustomDataPanel<Productregel> datapanel =
			new EduArteDataPanel<Productregel>("productregels", keuzeprovider, table)
			{
				private static final long serialVersionUID = 1L;

				@Override
				public boolean isVisible()
				{
					return getExamendeelname() != null;
				}

			};
		datapanel.setItemsPerPage(Integer.MAX_VALUE);
		add(datapanel);

		CollectionDataProvider<ExamenstatusOvergang> provider =
			new CollectionDataProvider<ExamenstatusOvergang>(new ListModel());
		EduArteDataPanel<ExamenstatusOvergang> statusovergangen =
			new EduArteDataPanel<ExamenstatusOvergang>("statusovergangen", provider,
				new ExamenstatusOvergangTable());
		add(statusovergangen);
	}

	private void addExamenjaarDatumUitslagFormValidator()
	{
		if (form != null)
			form.add(new ExamenjaarDatumUitslagFormValidator());
	}

	public Examendeelname getExamendeelname()
	{
		return getModelObject();
	}

	public IModel<Examendeelname> getExamendeelnameModel()
	{
		return getModel();
	}

	void setExamendeelname(Examendeelname examendeelname)
	{
		setDefaultModelObject(examendeelname);
	}

	private class ExamenjaarDatumUitslagFormValidator extends AbstractFormValidator
	{
		private static final long serialVersionUID = 1L;

		@Override
		public FormComponent< ? >[] getDependentFormComponents()
		{
			return new FormComponent[] {datumField, examenjaar};
		}

		@Override
		public void validate(@SuppressWarnings("hiding") Form< ? > form)
		{
			Date datumUitslag = getExamendeelname().getDatumUitslag();
			Integer examenJaar = getExamendeelname().getExamenjaar();

			if (datumUitslag != null && examenJaar != null
				&& TimeUtil.getInstance().getYear(datumUitslag) != examenJaar)
				error(datumField);
		}
	}
}
