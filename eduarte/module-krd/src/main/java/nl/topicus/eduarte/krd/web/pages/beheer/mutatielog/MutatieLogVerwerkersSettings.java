package nl.topicus.eduarte.krd.web.pages.beheer.mutatielog;

import static nl.topicus.cobra.util.TimeUtil.*;

import java.sql.Timestamp;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.dao.BatchDataAccessHelper;
import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.dataproviders.GeneralDataProvider;
import nl.topicus.cobra.modelsv2.DefaultModelManager;
import nl.topicus.cobra.modelsv2.IChangeRecordingModel;
import nl.topicus.cobra.modelsv2.ModelFactory;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.web.components.form.AutoFieldSet;
import nl.topicus.cobra.web.components.form.RenderMode;
import nl.topicus.cobra.web.components.panels.bottomrow.AnnulerenButton;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.cobra.web.components.panels.bottomrow.OpslaanButton;
import nl.topicus.cobra.web.pages.IEditPage;
import nl.topicus.eduarte.app.EduArteContext;
import nl.topicus.eduarte.krd.dao.helpers.MutatieLogVerwerkersLogDataAccessHelper;
import nl.topicus.eduarte.krd.entities.mutatielog.MutatieLogVerwerkersLog;
import nl.topicus.eduarte.krd.principals.beheer.systeemtabellen.MutatieLogVerwerkenPrincipal;
import nl.topicus.eduarte.krd.web.components.panels.datapanel.table.MutatieLogVerwerkersLogEditTable;
import nl.topicus.eduarte.krd.zoekfilters.MutatieLogVerwerkersLogZoekFilter;
import nl.topicus.eduarte.web.components.menu.BeheerMenuItem;
import nl.topicus.eduarte.web.components.panels.EduArteDataPanel;
import nl.topicus.eduarte.web.pages.beheer.AbstractBeheerPage;

import org.apache.wicket.markup.html.form.Form;

@PageInfo(title = "Mutatielog verwerkers test", menu = "Beheer > Mutatielog verwerkers test")
@InPrincipal(MutatieLogVerwerkenPrincipal.class)
public class MutatieLogVerwerkersSettings extends AbstractBeheerPage<MutatieLogVerwerkersLog>
		implements IEditPage
{
	private Form<MutatieLogVerwerkersLog> formNieuweVerwerker;

	private Form<Void> formBestaandeVerwerkers;

	private IChangeRecordingModel<MutatieLogVerwerkersLog> recordingModel;

	public MutatieLogVerwerkersSettings()
	{
		super(BeheerMenuItem.MutatieLogVerwerkersTester);

		recordingModel =
			ModelFactory.getCompoundChangeRecordingModel(new MutatieLogVerwerkersLog(),
				new DefaultModelManager(MutatieLogVerwerkersLog.class));
		setDefaultModel(recordingModel);

		add(formNieuweVerwerker =
			new Form<MutatieLogVerwerkersLog>("nieuweVerwerker", getContextModel())
			{
				private static final long serialVersionUID = 1L;

				@Override
				protected void onSubmit()
				{
					MutatieLogVerwerkersLog verwerkersLog = recordingModel.getObject();
					verwerkersLog.setLaatstVerwerktOp(new Timestamp(nu().getTime()));
					verwerkersLog.setOrganisatie(getIngelogdeGebruiker().getOrganisatie());
					recordingModel.saveObject();
					verwerkersLog.setLaatstVerwerktId(verwerkersLog.getId());
					verwerkersLog.saveOrUpdate();
					verwerkersLog.commit();

					recordingModel.setObject(new MutatieLogVerwerkersLog());
				}
			});

		AutoFieldSet<MutatieLogVerwerkersLog> fieldSet =
			new AutoFieldSet<MutatieLogVerwerkersLog>("autofieldset", getContextModel(),
				"Nieuwe verwerker toevoegen");
		fieldSet.setPropertyNames("verwerker", "prodEndpointAddress", "prodServiceNameSpaceURI",
			"prodServiceName", "testEndpointAddress", "testServiceNameSpaceURI", "testServiceName");
		fieldSet.setRenderMode(RenderMode.EDIT);
		formNieuweVerwerker.add(fieldSet);

		add(formBestaandeVerwerkers = new Form<Void>("bestaandeVerwerkers"));

		MutatieLogVerwerkersLogZoekFilter filter = new MutatieLogVerwerkersLogZoekFilter();
		filter.setOrganisatie(EduArteContext.get().getOrganisatie());

		GeneralDataProvider<MutatieLogVerwerkersLog, MutatieLogVerwerkersLogZoekFilter> mutatieLogVerwerkersLogProvider =
			GeneralDataProvider.of(filter, MutatieLogVerwerkersLogDataAccessHelper.class);

		EduArteDataPanel<MutatieLogVerwerkersLog> mutatieLogVerwerkersLogDatapanel =
			new EduArteDataPanel<MutatieLogVerwerkersLog>("verwerkers",
				mutatieLogVerwerkersLogProvider, new MutatieLogVerwerkersLogEditTable());

		add(mutatieLogVerwerkersLogDatapanel);

		mutatieLogVerwerkersLogDatapanel.setReuseItems(true);
		mutatieLogVerwerkersLogDatapanel.setItemsPerPage(Integer.MAX_VALUE);
		formBestaandeVerwerkers.add(mutatieLogVerwerkersLogDatapanel);

		createComponents();
	}

	@Override
	protected void onBeforeRender()
	{
		super.onBeforeRender();

		formNieuweVerwerker.visitChildren(MutatieLogVerwerkerDropDownChoice.class,
			new IVisitor<MutatieLogVerwerkerDropDownChoice>()
			{
				@Override
				public Object component(MutatieLogVerwerkerDropDownChoice verwerkerSelectie)
				{
					formNieuweVerwerker.setVisible(!verwerkerSelectie.getChoices().isEmpty());
					return STOP_TRAVERSAL;
				}
			});
	}

	@Override
	protected void fillBottomRow(BottomRowPanel panel)
	{
		panel.addButton(new OpslaanButton(panel, formBestaandeVerwerkers)
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onSubmit()
			{
				DataAccessRegistry.getHelper(BatchDataAccessHelper.class).batchExecute();
				setResponsePage(MutatieLogVerwerkersTesterPage.class);
			}
		});
		panel.addButton(new AnnulerenButton(panel, MutatieLogVerwerkersTesterPage.class));
	}
}
