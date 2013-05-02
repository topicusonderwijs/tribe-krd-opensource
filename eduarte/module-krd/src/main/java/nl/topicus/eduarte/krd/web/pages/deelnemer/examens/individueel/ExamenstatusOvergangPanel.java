package nl.topicus.eduarte.krd.web.pages.deelnemer.examens.individueel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import nl.topicus.cobra.dataproviders.CollectionDataProvider;
import nl.topicus.cobra.modelsv2.ModelFactory;
import nl.topicus.cobra.modelsv2.ModelManager;
import nl.topicus.cobra.util.ComponentUtil;
import nl.topicus.cobra.util.TimeUtil;
import nl.topicus.cobra.web.components.ComponentFactory;
import nl.topicus.cobra.web.components.text.RequiredDatumField;
import nl.topicus.eduarte.entities.examen.Examendeelname;
import nl.topicus.eduarte.entities.examen.Examenstatus;
import nl.topicus.eduarte.entities.examen.ExamenstatusOvergang;
import nl.topicus.eduarte.entities.examen.ToegestaneExamenstatusOvergang;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.entities.onderwijsproduct.OnderwijsproductAfnameContext;
import nl.topicus.eduarte.krd.web.validators.MinimumDateValidator;
import nl.topicus.eduarte.util.criteriumbank.CriteriumbankControle;
import nl.topicus.eduarte.web.components.choice.ExamenstatusCombobox;
import nl.topicus.eduarte.web.components.panels.EduArteDataPanel;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.EmptyPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.validation.validator.RangeValidator;

class ExamenstatusOvergangPanel extends Panel
{
	private static final long serialVersionUID = 1L;

	private final Form<ExamenstatusOvergang> form;

	private static final IModel<ExamenstatusOvergang> getModel(Examendeelname examendeelname,
			ModelManager modelManager)
	{
		ExamenstatusOvergang overgang = new ExamenstatusOvergang();
		overgang.setDatumTijd(TimeUtil.getInstance().currentDateTime());
		overgang.setExamendeelname(examendeelname);
		overgang.setVanStatus(examendeelname.getExamenstatus());
		IModel<ExamenstatusOvergang> model =
			ModelFactory.getCompoundChangeRecordingModel(overgang, modelManager);
		return model;
	}

	ExamenstatusOvergangPanel(String id, Examendeelname examendeelname,
			ToegestaneExamenstatusOvergang toegestaneOvergang, ModelManager modelManager)
	{
		super(id);
		final boolean criteriumbankControle =
			toegestaneOvergang.getNaarExamenstatus() != null
				&& toegestaneOvergang.getNaarExamenstatus().isCriteriumbankControle();
		final boolean setDatumUitslag = toegestaneOvergang.isBepaaltDatumUitslag();
		form = new Form<ExamenstatusOvergang>("form", getModel(examendeelname, modelManager))
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void onSubmit()
			{
				ExamenstatusOvergang statusovergang = getModelObject();

				/**
				 * @Mantis #0050301
				 * 
				 *         Datum uitslag leegmaken als volgende status 'verwijderd' is.
				 */
				if (statusovergang.getNaarStatus() != null
					&& statusovergang.getNaarStatus().isVerwijderd())
				{
					statusovergang.getExamendeelname().setDatumUitslag(null);
				}

				statusovergang.getExamendeelname().setExamenstatus(statusovergang.getNaarStatus());
				statusovergang.getExamendeelname().pasAfnameContextenAan(
					statusovergang.getNaarStatus());
				statusovergang.getExamendeelname().setDatumLaatsteStatusovergang(
					statusovergang.getDatumTijd());
				if (setDatumUitslag)
				{
					statusovergang.getExamendeelname().setExamenjaar(
						statusovergang.getExamendeelname().getDatumUitslag() == null ? null
							: TimeUtil.getInstance().getYear(
								statusovergang.getExamendeelname().getDatumUitslag()));
				}
				if (!statusovergang.getExamendeelname().isSaved())
				{
					statusovergang.getExamendeelname().getVerbintenis().getExamendeelnames().add(
						statusovergang.getExamendeelname());
				}
				statusovergang.getExamendeelname().getStatusovergangen().add(statusovergang);
				statusovergang.getExamendeelname().saveOrUpdate();
				statusovergang.saveOrUpdate();
				statusovergang.commit();
			}

		};
		form.getModelObject();
		add(form);
		// Bepaal naarstatus(sen).
		List<Examenstatus> mogelijkeStatussen = new ArrayList<Examenstatus>();
		ExamenstatusOvergang overgang = form.getModelObject();
		if (criteriumbankControle)
		{
			// Naar-status wordt automatisch bepaald
			CriteriumbankControle controle =
				new CriteriumbankControle(examendeelname.getVerbintenis());
			boolean voldoet = controle.voldoetAanCriteriumbank();
			if (voldoet && !controle.isHeeftFouten())
			{
				overgang.setNaarStatus(toegestaneOvergang.getNaarExamenstatus());
			}
			else
			{
				overgang.setNaarStatus(toegestaneOvergang.getAfgewezenExamenstatus());
				overgang.setOpmerkingen(controle.getAlleMeldingenFormatted());
			}
		}
		else
		{
			if (toegestaneOvergang.getNaarExamenstatus() != null)
			{
				overgang.setNaarStatus(toegestaneOvergang.getNaarExamenstatus());
				mogelijkeStatussen.add(toegestaneOvergang.getNaarExamenstatus());
			}
			else
			{
				overgang.setNaarStatus(examendeelname.getExamenstatus());
			}
			if (toegestaneOvergang.getAfgewezenExamenstatus() != null)
			{
				mogelijkeStatussen.add(toegestaneOvergang.getAfgewezenExamenstatus());
			}
			// TODO: Dit is tijdelijk voor de acceptatie. Daarna zal dit permanenter
			// gefixed moeten worden.
			if (toegestaneOvergang.getNaarExamenstatus() != null
				&& toegestaneOvergang.getAfgewezenExamenstatus() != null)
			{
				// Bepaal de meest waarschijnlijke.
				if (examendeelname.getExamenstatus().getNaam().toLowerCase().contains(
					toegestaneOvergang.getNaarExamenstatus().getNaam().toLowerCase()))
					overgang.setNaarStatus(toegestaneOvergang.getNaarExamenstatus());
				else
					overgang.setNaarStatus(toegestaneOvergang.getAfgewezenExamenstatus());
			}
		}
		if (setDatumUitslag)
		{
			overgang.getExamendeelname().setDatumUitslag(overgang.getDatumTijd());
		}
		if (toegestaneOvergang.isTijdvakAangeven())
		{
			if (overgang.getExamendeelname().getTijdvak() != null)
			{
				overgang.getExamendeelname().setTijdvak(
					Math.min(3, overgang.getExamendeelname().getTijdvak() + 1));
			}
		}

		form.add(ComponentFactory.getDataLabel("vanStatus.naam"));
		ExamenstatusCombobox examenstatus =
			new ExamenstatusCombobox("naarStatus", null, mogelijkeStatussen);
		examenstatus.setNullValid(false);
		examenstatus.setRequired(true);
		form.add(examenstatus);
		form.add(ComponentFactory.getDatumTijdLabel("datumTijd"));
		form.add(new TextArea<String>("opmerkingen"));
		WebMarkupContainer examennummerContainer = new WebMarkupContainer("examennummerContainer");
		examennummerContainer.setVisible(toegestaneOvergang.isExamennummersToekennen());
		examennummerContainer.add(ComponentUtil.fixLength(new TextField<String>(
			"examendeelname.examennummerPrefix"), Examendeelname.class));
		examennummerContainer.add(new RequiredTextField<Integer>("examendeelname.examennummer",
			Integer.class));
		form.add(examennummerContainer);
		WebMarkupContainer tijdvakContainer = new WebMarkupContainer("tijdvakContainer");
		tijdvakContainer.setVisible(toegestaneOvergang.isTijdvakAangeven());
		tijdvakContainer
			.add(new RequiredTextField<Integer>("examendeelname.tijdvak", Integer.class)
				.add(new RangeValidator<Integer>(2, 3)));
		form.add(tijdvakContainer);
		WebMarkupContainer datumUitslagContainer = new WebMarkupContainer("datumUitslagContainer");
		datumUitslagContainer.setVisible(setDatumUitslag);
		RequiredDatumField requredDatumField =
			new RequiredDatumField("examendeelname.datumUitslag");

		if (examendeelname != null)
		{
			Verbintenis verbintenis = examendeelname.getVerbintenis();
			if (verbintenis != null)
			{
				MinimumDateValidator validator =
					new MinimumDateValidator(examendeelname.getVerbintenis().getBegindatum());
				requredDatumField.add(validator);
			}
		}
		datumUitslagContainer.add(requredDatumField);
		form.add(datumUitslagContainer);

		if (examendeelname != null && toegestaneOvergang.isTijdvakAangeven())
		{
			// Voeg een lijst toe met alle onderwijsproductafnamecontexten, inclusief de
			// mogelijkheid om aan te geven welke doorverwezen moeten worden naar het
			// volgende tijdvak.
			ExamenOnderwijsproductAfnameContextTable table =
				new ExamenOnderwijsproductAfnameContextTable(new PropertyModel<Verbintenis>(form
					.getModel(), "examendeelname.verbintenis"));
			List<OnderwijsproductAfnameContext> list =
				new ArrayList<OnderwijsproductAfnameContext>(examendeelname.getVerbintenis()
					.getAfnameContexten());
			Collections.sort(list);
			CollectionDataProvider<OnderwijsproductAfnameContext> provider =
				new CollectionDataProvider<OnderwijsproductAfnameContext>(list)
				{
					private static final long serialVersionUID = 1L;

					@Override
					public IModel<OnderwijsproductAfnameContext> model(
							OnderwijsproductAfnameContext object)
					{
						return new CompoundPropertyModel<OnderwijsproductAfnameContext>(super
							.model(object));
					}

				};
			EduArteDataPanel<OnderwijsproductAfnameContext> datapanel =
				new EduArteDataPanel<OnderwijsproductAfnameContext>(
					"onderwijsproductAfnameContexten", provider, table);
			datapanel.setItemsPerPage(Integer.MAX_VALUE);
			datapanel.setReuseItems(true);
			form.add(datapanel);
		}
		else
		{
			form.add(new EmptyPanel("onderwijsproductAfnameContexten").setVisible(false));
		}
	}

	public Form<ExamenstatusOvergang> getForm()
	{
		return form;
	}

}
