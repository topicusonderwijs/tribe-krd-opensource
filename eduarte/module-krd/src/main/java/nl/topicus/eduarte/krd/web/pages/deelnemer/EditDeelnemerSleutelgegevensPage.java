/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.krd.web.pages.deelnemer;

import java.util.ArrayList;
import java.util.List;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.modelsv2.IChangeRecordingModel;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.web.behaviors.AjaxFormComponentValidatingBehavior;
import nl.topicus.cobra.web.components.form.AutoFieldSet;
import nl.topicus.cobra.web.components.form.RenderMode;
import nl.topicus.cobra.web.components.form.modifier.BehaviorModifier;
import nl.topicus.cobra.web.components.form.modifier.ConstructorArgModifier;
import nl.topicus.cobra.web.components.form.modifier.RequiredModifier;
import nl.topicus.cobra.web.components.form.modifier.ValidateModifier;
import nl.topicus.cobra.web.components.panels.bottomrow.AnnulerenButton;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.cobra.web.components.panels.bottomrow.OpslaanButton;
import nl.topicus.cobra.web.validators.BsnValidator;
import nl.topicus.cobra.web.validators.DatumInVerledenValidator;
import nl.topicus.cobra.web.validators.OnderwijsnummerValidator;
import nl.topicus.cobra.web.validators.UniqueConstraintValidator;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.entities.personen.Deelnemer;
import nl.topicus.eduarte.entities.personen.Persoon.ToepassingGeboortedatum;
import nl.topicus.eduarte.krd.principals.deelnemer.DeelnemerSleutelgegevensWrite;
import nl.topicus.eduarte.krd.web.validators.GeboortedatumMinMaxWarningValidator;
import nl.topicus.eduarte.providers.VerbintenisProvider;
import nl.topicus.eduarte.web.components.menu.DeelnemerMenuItem;
import nl.topicus.eduarte.web.pages.IModuleEditPage;
import nl.topicus.eduarte.web.pages.deelnemer.AbstractDeelnemerPage;

import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.markup.html.form.RadioChoice;
import org.apache.wicket.markup.html.form.validation.IFormValidator;
import org.apache.wicket.model.PropertyModel;

/**
 * @author hop
 */
@PageInfo(title = "Sleutelgegevens bewerken", menu = {
	"Deelnemer > [deelnemer], Personalia, Bewerken, Sleutelgegevens",
	"Groep > [groep] > [deelnemer], Personalia, Bewerken, Sleutelgegevens"})
@InPrincipal(DeelnemerSleutelgegevensWrite.class)
public class EditDeelnemerSleutelgegevensPage extends AbstractDeelnemerPage implements
		VerbintenisProvider, IModuleEditPage<Deelnemer>
{
	private AutoFieldSet<Deelnemer> fieldset;

	private Form<Void> form;

	private EditDeelnemerPersonaliaPage returnToPage;

	public EditDeelnemerSleutelgegevensPage(IChangeRecordingModel<Deelnemer> deelnemerModel,
			EditDeelnemerPersonaliaPage returnToPage)
	{
		super(DeelnemerMenuItem.Personalia, returnToPage.getContextDeelnemer(), returnToPage
			.getContextVerbintenis());
		setDefaultModel(deelnemerModel);

		this.returnToPage = returnToPage;

		form = new Form<Void>("sleutelgegevensForm");
		fieldset =
			new AutoFieldSet<Deelnemer>("sleutelgegevens", getContextDeelnemerModel(),
				"Sleutelgegevens");
		fieldset.setPropertyNames("persoon.bsn", "persoon.geslacht", "persoon.geboortedatum");
		fieldset.setRenderMode(RenderMode.EDIT);
		fieldset.setSortAccordingToPropertyNames(true);
		fieldset.addFieldModifier(new RequiredModifier(true, "persoon.geslacht",
			"persoon.geboortedatum"));
		fieldset.addFieldModifier(new ConstructorArgModifier("persoon.geboortedatum",
			new PropertyModel<ToepassingGeboortedatum>(getDefaultModel(),
				"persoon.toepassingGeboortedatum")));
		fieldset.addFieldModifier(new ValidateModifier(new UniqueConstraintValidator<Long>(
			fieldset, "deelnemer", "persoon.bsn", "organisatie"), "persoon.bsn"));
		fieldset.addFieldModifier(new BehaviorModifier(new AjaxFormComponentValidatingBehavior(
			"onchange"), "persoon.bsn"));
		fieldset.addFieldModifier(new BehaviorModifier(new AjaxFormComponentValidatingBehavior(
			"onchange"), "onderwijsnummer"));
		fieldset.addFieldModifier(new ValidateModifier(new BsnValidator(), "persoon.bsn"));
		fieldset.addFieldModifier(new ValidateModifier(new OnderwijsnummerValidator(),
			"onderwijsnummer"));

		fieldset.addFieldModifier(new ValidateModifier(new DatumInVerledenValidator(),
			"persoon.geboortedatum"));
		fieldset.addFieldModifier(new ValidateModifier(
			new GeboortedatumMinMaxWarningValidator(this), "persoon.geboortedatum"));

		form.add(createNegeerSleutelWijzigingenBronRadioChoice("negeerSleutelWijzigingenBron"));

		form.add(fieldset);
		form.add(new BsnOfOnderwijsnummerValidator());

		add(form);
		createComponents();
	}

	private RadioChoice<Boolean> createNegeerSleutelWijzigingenBronRadioChoice(String id)
	{
		List<Boolean> keuzes = new ArrayList<Boolean>();
		keuzes.add(Boolean.FALSE);
		keuzes.add(Boolean.TRUE);

		IChoiceRenderer<Boolean> renderer = new IChoiceRenderer<Boolean>()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public Object getDisplayValue(Boolean object)
			{
				if (object)
					return "Nee. De gegevens in BRON zijn correct en moeten in het KRD worden gecorrigeerd.";
				else
					return "Ja. De gegevens in BRON zijn onjuist en moeten worden verwijderd en opnieuw toegevoegd.";
			}

			@Override
			public String getIdValue(Boolean object, int index)
			{
				return object.toString();
			}
		};

		RadioChoice<Boolean> group =
			new RadioChoice<Boolean>(id, new PropertyModel<Boolean>(getDefaultModel(),
				"negeerSleutelWijzigingenBron"), keuzes, renderer);

		return group;
	}

	private class BsnOfOnderwijsnummerValidator implements IFormValidator
	{
		private static final long serialVersionUID = 1L;

		@Override
		public FormComponent< ? >[] getDependentFormComponents()
		{
			FormComponent< ? > bsn = null;
			FormComponent< ? > onderwijsnummer = null;
			try
			{
				bsn = (FormComponent< ? >) fieldset.findFieldComponent("persoon.bsn");
			}
			catch (Exception e)
			{
				// ignore
			}
			try
			{
				onderwijsnummer =
					(FormComponent< ? >) fieldset.findFieldComponent("onderwijsnummer");
			}
			catch (Exception e)
			{
				// ignore
			}
			List<FormComponent< ? >> list = new ArrayList<FormComponent< ? >>();
			if (bsn != null)
				list.add(bsn);
			if (onderwijsnummer != null)
				list.add(onderwijsnummer);

			FormComponent< ? >[] components = new FormComponent< ? >[list.size()];
			list.toArray(components);
			return components;
		}

		@Override
		public void validate(Form< ? > aform)
		{
			if (getDeelnemer().getOnderwijsnummer() == null
				&& getDeelnemer().getPersoon().getBsn() == null)
				form.error("BSN of Onderwijsnummer dient ingevoerd te zijn");
		}
	}

	@Override
	public Verbintenis getVerbintenis()
	{
		if (!getDeelnemer().getVerbintenissen().isEmpty())
			return getDeelnemer().getVerbintenissen().get(0);
		else
			return null;
	}

	private Deelnemer getDeelnemer()
	{
		return (Deelnemer) getDefaultModelObject();
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
				returnToPage.setNegeerSleutelWijzigingen(getDeelnemer()
					.isNegeerSleutelWijzigingenBron());
				setResponsePage(returnToPage);
			}
		});
		panel.addButton(new AnnulerenButton(panel, returnToPage));
	}
}