package nl.topicus.eduarte.krdparticipatie.web.pages.verzuimloket;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.ws.soap.SOAPFaultException;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.web.components.choice.AjaxRadioChoice;
import nl.topicus.cobra.web.components.form.AutoFieldSet;
import nl.topicus.cobra.web.components.form.RenderMode;
import nl.topicus.cobra.web.components.panels.bottomrow.AnnulerenButton;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.cobra.web.components.panels.bottomrow.OpslaanButton;
import nl.topicus.cobra.web.components.shortcut.ActionKey;
import nl.topicus.cobra.web.components.shortcut.CobraKeyAction;
import nl.topicus.cobra.web.pages.IEditPage;
import nl.topicus.eduarte.dao.helpers.SSLCertificaatDataAccessHelper;
import nl.topicus.eduarte.entities.ibgverzuimloket.IbgVerzuimdag;
import nl.topicus.eduarte.entities.ibgverzuimloket.IbgVerzuimmelding;
import nl.topicus.eduarte.entities.ibgverzuimloket.SSLCertificaat;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.entities.organisatie.Locatie;
import nl.topicus.eduarte.krdparticipatie.principals.deelnemer.DeelnemerVerzuimloketWrite;
import nl.topicus.eduarte.krdparticipatie.util.IbgVerzuimloketUtil;
import nl.topicus.eduarte.krdparticipatie.web.components.menu.enums.ParticipatieDeelnemerMenuItem;
import nl.topicus.eduarte.web.components.choice.LocatieCombobox;
import nl.topicus.eduarte.web.pages.SecurePage;
import nl.topicus.onderwijs.ibgverzuimloket.model.IbgVerzuimException;

import org.apache.wicket.RestartResponseException;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxCheckBox;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RadioChoice;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;

@PageInfo(title = "Melding opvoeren", menu = "Verzuimloket")
@InPrincipal(DeelnemerVerzuimloketWrite.class)
public class EditIbgVerzuimmeldingMeldingPage extends AbstractVerzuimmeldingPage implements
		IEditPage
{
	private static final long serialVersionUID = 1L;

	private Form<IbgVerzuimmelding> form;

	private VerzuimdagEditPanel verzuimdagPanel;

	private SecurePage returnPage;

	private SecurePage previousPage;

	private TextArea<String> actiegewenstToelichting;

	private WebMarkupContainer panelContainer;

	private static final ArrayList<Boolean> values = new ArrayList<Boolean>(2);

	private static final IModel<ArrayList<Boolean>> jaNeeModel =
		new Model<ArrayList<Boolean>>(values);
	static
	{
		values.add(Boolean.TRUE);
		values.add(Boolean.FALSE);
	}

	public EditIbgVerzuimmeldingMeldingPage(Verbintenis inschrijving,
			IbgVerzuimmelding verzuimmelding, SecurePage returnPage)
	{
		super(ParticipatieDeelnemerMenuItem.Verzuimloket, inschrijving.getDeelnemer(),
			inschrijving, verzuimmelding);
		this.returnPage = returnPage;
		createMarkup();
	}

	public EditIbgVerzuimmeldingMeldingPage(Verbintenis inschrijving,
			IModel<IbgVerzuimmelding> verzuimmeldingModel, SecurePage returnPage,
			SecurePage previousPage)
	{
		super(ParticipatieDeelnemerMenuItem.Verzuimloket, inschrijving.getDeelnemer(),
			inschrijving, verzuimmeldingModel);
		this.returnPage = returnPage;
		this.previousPage = previousPage;
		createMarkup();
	}

	public EditIbgVerzuimmeldingMeldingPage(Verbintenis inschrijving,
			IModel<IbgVerzuimmelding> verzuimmeldingModel, SecurePage returnPage)
	{
		super(ParticipatieDeelnemerMenuItem.Verzuimloket, inschrijving.getDeelnemer(),
			inschrijving, verzuimmeldingModel);
		this.returnPage = returnPage;
		createMarkup();

	}

	public void createMarkup()
	{
		form = new Form<IbgVerzuimmelding>("form", verzuimmeldingModel);

		AutoFieldSet<IbgVerzuimmelding> fieldsetVerzuim =
			new AutoFieldSet<IbgVerzuimmelding>("verzuim", verzuimmeldingModel, "Verzuim");
		fieldsetVerzuim.setPropertyNames("verzuimsoort", "begindatum", "einddatum",
			"vermoedelijkeReden");
		form.add(fieldsetVerzuim);

		form.add(new TextArea<String>("toelichting", new PropertyModel<String>(verzuimmeldingModel,
			"toelichting")));

		actiegewenstToelichting =
			new TextArea<String>("toelichtingActieGewenst", new PropertyModel<String>(
				verzuimmeldingModel, "toelichtingActieGewenst"));
		actiegewenstToelichting.setOutputMarkupId(true);

		if (!getContextIbgVerzuimmelding().isActieOndernemen())
		{
			actiegewenstToelichting.setRequired(true);
		}
		else
		{
			actiegewenstToelichting.setEnabled(false);
		}

		CheckBox actieGewenst =
			new AjaxCheckBox("actieOndernemen", new PropertyModel<Boolean>(verzuimmeldingModel,
				"actieOndernemen"))
			{

				private static final long serialVersionUID = 1L;

				@Override
				protected void onUpdate(AjaxRequestTarget target)
				{
					if (Boolean.parseBoolean(getValue()))
					{
						EditIbgVerzuimmeldingMeldingPage.this.actiegewenstToelichting.clearInput();
						EditIbgVerzuimmeldingMeldingPage.this.actiegewenstToelichting
							.setModelObject(null);
						EditIbgVerzuimmeldingMeldingPage.this.actiegewenstToelichting
							.setEnabled(false);
						EditIbgVerzuimmeldingMeldingPage.this.actiegewenstToelichting
							.setRequired(false);

					}
					else
					{
						EditIbgVerzuimmeldingMeldingPage.this.actiegewenstToelichting
							.setEnabled(true);
						EditIbgVerzuimmeldingMeldingPage.this.actiegewenstToelichting
							.setRequired(true);
					}

					target.addComponent(actiegewenstToelichting);
				}

			};
		fieldsetVerzuim.setRenderMode(RenderMode.EDIT);

		form.add(actieGewenst);
		form.add(actiegewenstToelichting);

		form.add(new TextArea<String>("ondernomenActie", new PropertyModel<String>(
			verzuimmeldingModel, "toelichtingOndernomenactie")));
		form.add(new LocatieCombobox("locatie", new PropertyModel<Locatie>(verzuimmeldingModel,
			"locatie")));

		RadioChoice<Boolean> dagspecificatie =
			new AjaxRadioChoice<Boolean>("specificatiechoice", new PropertyModel<Boolean>(
				verzuimmeldingModel, "isVerzuimdagGespecificeerd"), jaNeeModel,
				new BooleanChoiceRenderer("Ja, specificeren", "Nee, niet van toepassing"))
			{
				private static final long serialVersionUID = 1L;

				@Override
				protected void onUpdate(AjaxRequestTarget target, Object newSelection)
				{
					verzuimdagPanel.setVisible((Boolean) newSelection);
					target.addComponent(panelContainer);
				}

			};
		form.add(dagspecificatie);

		panelContainer = new WebMarkupContainer("panelContainer");
		panelContainer.setOutputMarkupId(true);

		verzuimdagPanel =
			new VerzuimdagEditPanel("specificatie", new PropertyModel<List<IbgVerzuimdag>>(
				verzuimmeldingModel, "verzuimdagen"), verzuimmeldingModel);

		panelContainer.add(verzuimdagPanel);
		form.add(panelContainer);

		add(form);

		IbgVerzuimmelding melding = verzuimmeldingModel.getObject();
		if (!melding.isVerzuimdagGespecificeerd())
		{
			verzuimdagPanel.setVisible(false);
		}

		createComponents();
	}

	@Override
	public void fillBottomRow(BottomRowPanel panel)
	{

		if (previousPage != null)
		{
			panel.addButton(new OpslaanButton(panel, form)
			{
				private static final long serialVersionUID = 1L;

				@Override
				protected void onSubmit()
				{
					setResponsePage(previousPage);
				}

				@Override
				public ActionKey getAction()
				{
					return CobraKeyAction.TERUG;
				}

				@Override
				public String getLabel()
				{
					return "Vorige stap";
				}
			});
		}

		panel.addButton(new OpslaanButton(panel, form)
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onSubmit()
			{
				if (!controleerVerzuimmeldingDagen())
				{
					error("Er zijn dubbele dagen gespecificeerd.");
					return;
				}
				saveVerzuimmelding();
				setResponsePage(returnPage);
			}
		});

		panel.addButton(new OpslaanButton(panel, form)
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onSubmit()
			{
				if (!controleerVerzuimmeldingDagen())
				{
					error("Er zijn dubbele dagen gespecificeerd.");
					return;
				}

				List<String> fouten;
				IbgVerzuimloketUtil verzuimloketUtil = new IbgVerzuimloketUtil();
				IbgVerzuimmelding melding = getContextIbgVerzuimmelding();

				SSLCertificaatDataAccessHelper helper =
					DataAccessRegistry.getHelper(SSLCertificaatDataAccessHelper.class);
				SSLCertificaat certificaat = helper.findCertificaatOfInstelling();

				if (certificaat == null)
				{
					error("Geen certificaat beschikbaar, deze kan ingevoerd worden in beheer");
					throw new RestartResponseException(getPage());
				}

				try
				{
					if (melding.isVerzonden())
					{
						fouten =
							verzuimloketUtil.aanvraagGewijzigdeMeldingRelatiefVerzuim(melding,
								certificaat);
						if (fouten.isEmpty())
						{
							fouten =
								verzuimloketUtil
									.mutatieMeldingRelatiefVerzuim(melding, certificaat);
						}
					}
					else
					{

						fouten = verzuimloketUtil.meldenRelatiefVerzuim(melding, certificaat);
					}
				}
				catch (IbgVerzuimException e)
				{
					error("Fout bij het verzenden van bericht.\n" + e.getLocalizedMessage());
					throw new RestartResponseException(getPage());
				}
				catch (SOAPFaultException se)
				{
					error("Fout tijden communicatie.\n" + se.getLocalizedMessage());
					throw new RestartResponseException(getPage());
				}
				finally
				{
					certificaat.evict();
				}

				if (fouten.size() > 0)
				{
					StringBuilder builder = new StringBuilder();
					for (String fout : fouten)
					{
						builder.append(fout);
					}
					error("Fout bij het verzenden van bericht.\n" + builder.toString());
					throw new RestartResponseException(getPage());
				}
				saveVerzuimmelding();
				setResponsePage(returnPage);
			}

			@Override
			public ActionKey getAction()
			{
				return CobraKeyAction.GEEN;
			}

			@Override
			public String getLabel()
			{
				return "Verzenden";
			}
		});
		panel.addButton(new AnnulerenButton(panel, returnPage));
	}

	private void saveVerzuimmelding()
	{
		getVerzuimmeldingModel().saveObject();
		IbgVerzuimmelding melding = getContextIbgVerzuimmelding();
		melding.commit();
	}

	private boolean controleerVerzuimmeldingDagen()
	{
		if (getContextIbgVerzuimmelding() == null)
			return true;

		IbgVerzuimmelding melding = getContextIbgVerzuimmelding();
		if (!melding.isVerzuimdagGespecificeerd())
			return true;

		List<Date> data = new ArrayList<Date>();

		for (IbgVerzuimdag dag : melding.getVerzuimdagen())
		{
			if (dag.isHeledag())
			{
				if (data.contains(dag.getDatum()))
					return false;
				else
					data.add(dag.getDatum());
			}
		}
		return true;
	}
}
