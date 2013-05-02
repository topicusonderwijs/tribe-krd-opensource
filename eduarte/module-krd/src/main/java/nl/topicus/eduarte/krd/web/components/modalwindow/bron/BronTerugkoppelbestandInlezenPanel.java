package nl.topicus.eduarte.krd.web.components.modalwindow.bron;

import static nl.topicus.onderwijs.duo.bron.BronOnderwijssoort.*;

import java.util.List;
import java.util.Map;

import nl.topicus.cobra.modelsv2.ModelFactory;
import nl.topicus.cobra.web.components.modal.CobraModalWindow;
import nl.topicus.cobra.web.components.modal.ModalWindowBasePanel;
import nl.topicus.cobra.web.components.panels.bottomrow.AbstractBottomRowButton;
import nl.topicus.cobra.web.components.panels.bottomrow.AjaxAnnulerenButton;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.cobra.web.components.panels.bottomrow.ButtonAlignment;
import nl.topicus.cobra.web.components.shortcut.ActionKey;
import nl.topicus.cobra.web.components.shortcut.CobraKeyAction;
import nl.topicus.eduarte.app.EduArteApp;
import nl.topicus.eduarte.app.EduArteContext;
import nl.topicus.eduarte.app.EduArteScheduler;
import nl.topicus.eduarte.krd.bron.jobs.BronTerugkoppelbestandInlezenJob;
import nl.topicus.eduarte.krd.dao.helpers.BronAanleverpuntDataAccessHelper;
import nl.topicus.eduarte.krd.dao.helpers.BronDataAccessHelper;
import nl.topicus.eduarte.krd.entities.bron.BronAanleverpunt;
import nl.topicus.eduarte.krd.web.components.choice.BronAanleverpuntComboBox;
import nl.topicus.eduarte.krd.web.pages.beheer.bron.BronAlgemeenPage;
import nl.topicus.eduarte.krd.web.pages.beheer.bron.terugkoppeling.BronTerugkoppelbestandInlezenPage;
import nl.topicus.onderwijs.duo.bron.BronOnderwijssoort;
import nl.topicus.onderwijs.duo.bron.bve.waardelijsten.Sectordeel;

import org.apache.wicket.RestartResponseException;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.markup.html.IHeaderContributor;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.SubmitLink;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.lang.Bytes;
import org.quartz.JobDataMap;

public class BronTerugkoppelbestandInlezenPanel extends ModalWindowBasePanel<Void> implements
		IHeaderContributor
{
	private static final String VOORLOOP_BVE = "400";

	private static final String VOORLOOP_VO = "200";

	private static final long serialVersionUID = 1L;

	private final Form<Void> uploadForm;

	private final FileUploadField uploadField;

	@SpringBean
	private BronDataAccessHelper bron;

	@SpringBean
	private BronAanleverpuntDataAccessHelper aanleverpunten;

	private final WebMarkupContainer nummersContainer;

	private final IModel<BronAanleverpunt> aanleverpuntModel;

	private String filename;

	public BronTerugkoppelbestandInlezenPanel(String id, CobraModalWindow<Void> modalWindow)
	{
		super(id, modalWindow);

		uploadForm = new Form<Void>("form");
		uploadForm.setMultiPart(true);
		uploadForm.setMaxSize(Bytes.megabytes(30));

		List<BronAanleverpunt> bronAanleverpunten = aanleverpunten.getBronAanleverpunten();
		if (bronAanleverpunten.isEmpty())
		{
			getSession()
				.error(
					"Er zijn nog geen aanleverpunten gedefinieerd, kan geen terugkoppelbestand inlezen");
			throw new RestartResponseException(BronAlgemeenPage.class);
		}
		aanleverpuntModel = ModelFactory.getModel(bronAanleverpunten.get(0));

		uploadForm.add(new BronAanleverpuntComboBox("aanleverpunt", aanleverpuntModel)
			.add(new AjaxFormComponentUpdatingBehavior("onchange")
			{
				private static final long serialVersionUID = 1L;

				@Override
				protected void onUpdate(AjaxRequestTarget target)
				{
					target.addComponent(nummersContainer);
				}
			}));

		VerwachtBatchnummersModel model = new VerwachtBatchnummersModel();

		nummersContainer = new WebMarkupContainer("nummers");
		nummersContainer.setOutputMarkupPlaceholderTag(true);
		uploadForm.add(nummersContainer);

		nummersContainer
			.add(new Label("bo", new VerwachtBatchnummerModel(BEROEPSONDERWIJS, model)));
		nummersContainer.add(new Label("ed", new VerwachtBatchnummerModel(EDUCATIE, model)));
		nummersContainer.add(new Label("vavo", new VerwachtBatchnummerModel(VAVO, model)));
		nummersContainer.add(new Label("vo", new VerwachtBatchnummerModel(VOORTGEZETONDERWIJS,
			model)));

		uploadField = new FileUploadField("file", new Model<FileUpload>());
		uploadField.setRequired(true);
		uploadField.setLabel(new Model<String>("Terugkoppelbestand"));

		uploadForm.add(uploadField);

		add(uploadForm);

		createComponents();
	}

	protected void onFileUploaded()
	{
		FileUpload upload = uploadField.getFileUpload();

		if (upload == null)
		{
			error("Geen bestand ingevuld");
			return;
		}
		try
		{
			byte[] bytes = upload.getBytes();
			filename = upload.getClientFileName();

			String tekst = new String(bytes, 0, Math.min(bytes.length, 20));
			if (!controleerVoorlooprecord(tekst))
			{
				setResponsePage(BronTerugkoppelbestandInlezenPage.class);
				return;
			}
			JobDataMap data = new JobDataMap();
			data.put(BronTerugkoppelbestandInlezenJob.KEY_CONTENTS, bytes);
			data.put(BronTerugkoppelbestandInlezenJob.KEY_FILENAME, upload.getClientFileName());

			EduArteScheduler scheduler = EduArteApp.get().getEduarteScheduler();
			scheduler.triggerJob(BronTerugkoppelbestandInlezenJob.class, data);
		}
		catch (Exception e)
		{
			getSession().error(
				"Kon bestand " + upload.getClientFileName() + " niet verwerken: " + e.getMessage());
			setResponsePage(BronTerugkoppelbestandInlezenPage.class);
			return;
		}
		finally
		{
			upload.delete();
		}
		setResponsePage(BronTerugkoppelbestandInlezenPage.class);
	}

	private boolean controleerVoorlooprecord(String tekst)
	{
		if (!controleerRecordType(tekst))
			return false;

		if (!controleerBrincode(tekst))
			return false;

		if (!controleerAanleverpunt(tekst))
			return false;

		if (tekst.startsWith(VOORLOOP_VO))
			return controleerVoVoorlooprecord(tekst);
		else
			return controleerBveVoorlooprecord(tekst);
	}

	private boolean controleerRecordType(String tekst)
	{
		if (!tekst.startsWith(VOORLOOP_VO) && !tekst.startsWith(VOORLOOP_BVE))
		{
			getSession().error(
				"Bestand " + filename
					+ " is geen geldig BRON bestand (start niet met een 200 of 400 record)");
			return false;
		}
		return true;
	}

	private boolean controleerBrincode(String tekst)
	{
		if (!tekst.contains(EduArteContext.get().getInstelling().getBrincode().getCode()))
		{
			getSession().error(
				"Bestand " + filename + " bevat een andere BRIN code dan de huidige instelling("
					+ EduArteContext.get().getInstelling().getBrincode().getCode() + ")");
			return false;
		}
		return true;
	}

	private boolean controleerAanleverpunt(String tekst)
	{
		String aanleverpuntNr = tekst.substring(7, 9);
		BronAanleverpunt aanleverpunt = getAanleverpunt();
		if (!String.format("%02d", aanleverpunt.getNummer()).equals(aanleverpuntNr))
		{
			String message =
				String.format(
					"Bestand %s is voor een ander aanleverpunt %02d dan het geselecteerde %s",
					filename, aanleverpunt.getNummer(), aanleverpuntNr);
			getSession().error(message);
			return false;
		}
		return true;
	}

	private boolean controleerVoVoorlooprecord(String tekst)
	{
		String batchnr = tekst.substring(9, 12);
		return controleerBatchnummer(batchnr, BronOnderwijssoort.VOORTGEZETONDERWIJS);
	}

	private boolean controleerBveVoorlooprecord(String tekst)
	{
		Sectordeel sector = Sectordeel.Basiseducatie.parse(tekst.substring(9, 11));
		BronOnderwijssoort soort = BronOnderwijssoort.valueOf(sector);

		String batchnr = tekst.substring(11, 14);
		return controleerBatchnummer(batchnr, soort);
	}

	private boolean controleerBatchnummer(String batchnr, BronOnderwijssoort soort)
	{
		BronAanleverpunt aanleverpunt = getAanleverpunt();
		Map<BronOnderwijssoort, Integer> nummerPerOnderwijssoort =
			bron.getVerwachteTerugkoppelBatchnummers(aanleverpunt);

		Integer verwachtBatchnummer = nummerPerOnderwijssoort.get(soort);
		if (verwachtBatchnummer != null
			&& !String.format("%03d", verwachtBatchnummer).equals(batchnr))
		{
			String message =
				String
					.format(
						"Bestand %s heeft een ander batchnummer (%s) dan verwacht (%03d) voor sector %s",
						filename, batchnr, verwachtBatchnummer, soort);
			getSession().error(message);
			return false;
		}
		return true;
	}

	@Override
	protected void fillBottomRow(BottomRowPanel panel)
	{
		panel.addButton(new InlezenBottomRowButton(panel, "Inlezen", CobraKeyAction.OPSLAAN,
			ButtonAlignment.RIGHT));

		panel.addButton(new AjaxAnnulerenButton(panel)
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onClick(AjaxRequestTarget target)
			{
				getModalWindow().close(target);
			}
		});
	}

	@Override
	public void renderHead(IHeaderResponse response)
	{
		response.renderJavascript("Wicket.Window.unloadConfirmation=false;", "disable-unload");
	}

	private BronAanleverpunt getAanleverpunt()
	{
		return aanleverpuntModel.getObject();
	}

	private final class InlezenBottomRowButton extends AbstractBottomRowButton
	{
		private static final long serialVersionUID = 1L;

		private InlezenBottomRowButton(BottomRowPanel bottomRow, String label, ActionKey action,
				ButtonAlignment alignment)
		{
			super(bottomRow, label, action, alignment);
		}

		@Override
		protected WebMarkupContainer getLink(String linkId)
		{
			return new SubmitLink(linkId, uploadForm)
			{
				private static final long serialVersionUID = 1L;

				@Override
				public void onSubmit()
				{
					onFileUploaded();
				}
			};
		}
	}

	private final class VerwachtBatchnummersModel extends
			LoadableDetachableModel<Map<BronOnderwijssoort, Integer>>
	{

		private static final long serialVersionUID = 1L;

		@Override
		protected Map<BronOnderwijssoort, Integer> load()
		{
			BronAanleverpunt aanleverpunt = getAanleverpunt();
			return bron.getVerwachteTerugkoppelBatchnummers(aanleverpunt);
		}
	}

	private final class VerwachtBatchnummerModel extends AbstractReadOnlyModel<String>
	{
		private static final long serialVersionUID = 1L;

		private final BronOnderwijssoort soort;

		private final IModel<Map<BronOnderwijssoort, Integer>> batchnummers;

		private VerwachtBatchnummerModel(BronOnderwijssoort soort,
				IModel<Map<BronOnderwijssoort, Integer>> batchnummers)
		{
			this.soort = soort;
			this.batchnummers = batchnummers;
		}

		@Override
		public String getObject()
		{
			Map<BronOnderwijssoort, Integer> nummers = batchnummers.getObject();
			Integer aantal = nummers.get(soort);
			if (aantal == null)
			{
				return "nog niet bekend";
			}
			return String.format("%03d", aantal);
		}

		@Override
		public void detach()
		{
			batchnummers.detach();
			aanleverpuntModel.detach();
		}
	}
}
