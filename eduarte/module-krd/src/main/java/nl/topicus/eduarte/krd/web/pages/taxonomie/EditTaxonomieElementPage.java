package nl.topicus.eduarte.krd.web.pages.taxonomie;

import java.util.HashMap;
import java.util.Map;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.modelsv2.DefaultModelManager;
import nl.topicus.cobra.modelsv2.ModelFactory;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.util.ComponentUtil;
import nl.topicus.cobra.web.components.ComponentFactory;
import nl.topicus.cobra.web.components.form.VersionedForm;
import nl.topicus.cobra.web.components.panels.bottomrow.AnnulerenButton;
import nl.topicus.cobra.web.components.panels.bottomrow.BottomRowPanel;
import nl.topicus.cobra.web.components.panels.bottomrow.OpslaanButton;
import nl.topicus.cobra.web.components.panels.bottomrow.VerwijderButton;
import nl.topicus.cobra.web.components.text.DatumField;
import nl.topicus.cobra.web.components.text.RequiredDatumField;
import nl.topicus.cobra.web.validators.BegindatumVoorEinddatumValidator;
import nl.topicus.cobra.web.validators.UniqueConstraintValidator;
import nl.topicus.eduarte.entities.taxonomie.Deelgebied;
import nl.topicus.eduarte.entities.taxonomie.Taxonomie;
import nl.topicus.eduarte.entities.taxonomie.TaxonomieElement;
import nl.topicus.eduarte.entities.taxonomie.TaxonomieEnum;
import nl.topicus.eduarte.entities.taxonomie.Verbintenisgebied;
import nl.topicus.eduarte.krd.principals.onderwijs.TaxonomieElementVerwijderen;
import nl.topicus.eduarte.krd.principals.onderwijs.TaxonomieElementWrite;
import nl.topicus.eduarte.web.behavior.NullableInstellingEntiteitEditLinkVisibleBehavior;
import nl.topicus.eduarte.web.components.menu.TaxonomieElementMenuItem;
import nl.topicus.eduarte.web.pages.IModuleEditPage;
import nl.topicus.eduarte.web.pages.SecurePage;
import nl.topicus.eduarte.web.pages.onderwijs.taxonomie.AbstractTaxonomieElementPage;
import nl.topicus.eduarte.web.pages.onderwijs.taxonomie.TaxonomieElementZoekenPage;

import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.security.checks.ClassSecurityCheck;
import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.validator.AbstractValidator;

/**
 * Pagina voor toevoegen/wijzigen van een taxonomie-element.
 * 
 * @author loite
 */
@PageInfo(title = "Taxonomie-element bewerken", menu = {"Onderwijs > Taxonomie > [taxonomie-element] > Bewerken"})
@InPrincipal(TaxonomieElementWrite.class)
public class EditTaxonomieElementPage extends AbstractTaxonomieElementPage implements
		IModuleEditPage<TaxonomieElement>
{
	private final class ReservedTaxonomieEnumValidator extends AbstractValidator<String>
	{
		private static final long serialVersionUID = 1L;

		@Override
		protected void onValidate(IValidatable<String> validatable)
		{
			if (TaxonomieEnum.isGereserveerd(validatable.getValue()))
			{
				Map<String, Object> params = new HashMap<String, Object>();
				params.put("eersteVrijeCode", Integer.toString(TaxonomieEnum.EERSTE_VRIJE_CODE));
				error(validatable, "ReservedTaxonomieEnumValidator.error", params);
			}
		}
	}

	@InPrincipal(TaxonomieElementVerwijderen.class)
	private final class VerwijderTaxonoieButton extends VerwijderButton
	{
		private static final long serialVersionUID = 1L;

		private VerwijderTaxonoieButton(BottomRowPanel bottomRow, String label,
				String confirmMessage)
		{
			super(bottomRow, label, confirmMessage);
			ComponentUtil.setSecurityCheck(this, new ClassSecurityCheck(
				VerwijderTaxonoieButton.class));
		}

		@Override
		public boolean isVisible()
		{
			return getContextTaxonomieElement().isVerwijderbaar();
		}

		@Override
		protected void onClick()
		{
			// Verwijder dit element.
			getContextTaxonomieElement().delete();
			getContextTaxonomieElement().commit();
			setResponsePage(new TaxonomieElementZoekenPage());
		}
	}

	private static final long serialVersionUID = 1L;

	private final VersionedForm<TaxonomieElement> form;

	private final SecurePage returnToPage;

	public EditTaxonomieElementPage(TaxonomieElement taxonomieElement, SecurePage returnToPage)
	{
		super(TaxonomieElementMenuItem.Algemeen, ModelFactory.getCompoundChangeRecordingModel(
			taxonomieElement, new DefaultModelManager(TaxonomieElement.class, Deelgebied.class,
				Taxonomie.class, Verbintenisgebied.class)));
		this.returnToPage = returnToPage;
		form = new VersionedForm<TaxonomieElement>("form", getContextTaxonomieElementModel())
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void onSubmit()
			{
				TaxonomieElement element = getModelObject();
				element.saveOrUpdate();
				element.commit();
				setResponsePage(EditTaxonomieElementPage.this.returnToPage);
			}

		};
		add(form);
		form.add(ComponentUtil.fixLength(new RequiredTextField<String>("afkorting"),
			TaxonomieElement.class));
		form.add(ComponentUtil.fixLength(new RequiredTextField<String>("naam"),
			TaxonomieElement.class));
		form.add(ComponentFactory.getDataLabel("parentTaxonomiecodeMetScheidingsteken"));
		RequiredTextField<String> codeField = new RequiredTextField<String>("code");
		codeField.add(new UniqueConstraintValidator<String>(form, taxonomieElement
			.getTaxonomieElementType().getNaam(), "code", "organisatie", "parent"));
		if (taxonomieElement.getParent() == null)
			codeField.add(new ReservedTaxonomieEnumValidator());
		form.add(ComponentUtil.fixLength(codeField, TaxonomieElement.class));
		form.add(ComponentUtil.fixLength(new TextField<String>("externeCode"),
			TaxonomieElement.class));
		form.add(ComponentFactory.getDataLabel("taxonomie.naam"));
		form.add(ComponentFactory.getDataLabel("taxonomieElementType.naam"));
		form.add(ComponentFactory.getDataLabel("taxonomie.landelijkOmschrijving"));
		RequiredDatumField begindatumField = new RequiredDatumField("begindatum");
		DatumField einddatumField = new DatumField("einddatum");
		form.add(begindatumField);
		form.add(einddatumField);
		form.add(new BegindatumVoorEinddatumValidator(begindatumField, einddatumField));
		createComponents();
	}

	@Override
	protected void fillBottomRow(BottomRowPanel panel)
	{
		super.fillBottomRow(panel);
		panel.addButton(new OpslaanButton(panel, form));
		panel.addButton(new AnnulerenButton(panel, returnToPage));
		VerwijderButton verwijderButton =
			new VerwijderTaxonoieButton(panel, "Verwijderen", "Weet u zeker dat u "
				+ getContextTaxonomieElement().toString() + " wilt verwijderen?");
		panel.addButton(verwijderButton);
		verwijderButton.add(new NullableInstellingEntiteitEditLinkVisibleBehavior(
			getContextTaxonomieElementModel()));
	}

}
