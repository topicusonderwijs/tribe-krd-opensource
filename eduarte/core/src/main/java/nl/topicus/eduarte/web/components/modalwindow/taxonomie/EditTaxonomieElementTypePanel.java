package nl.topicus.eduarte.web.components.modalwindow.taxonomie;

import nl.topicus.cobra.modelsv2.DefaultModelManager;
import nl.topicus.cobra.modelsv2.ModelFactory;
import nl.topicus.cobra.util.ComponentUtil;
import nl.topicus.cobra.web.components.ComponentFactory;
import nl.topicus.cobra.web.components.form.VersionedForm;
import nl.topicus.cobra.web.validators.UniqueConstraintValidator;
import nl.topicus.eduarte.entities.taxonomie.SoortTaxonomieElement;
import nl.topicus.eduarte.entities.taxonomie.Taxonomie;
import nl.topicus.eduarte.entities.taxonomie.TaxonomieElementType;
import nl.topicus.eduarte.providers.TaxonomieProvider;
import nl.topicus.eduarte.web.components.choice.TaxonomieElementTypeCombobox;

import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.panel.Panel;

/**
 * Panel voor het editen van een taxonomie-elementtype.
 * 
 * @author loite
 */
public class EditTaxonomieElementTypePanel extends Panel
{
	private static final long serialVersionUID = 1L;

	private final Form<TaxonomieElementType> form;

	public EditTaxonomieElementTypePanel(String id, TaxonomieElementType taxonomieElementType)
	{
		super(id);
		form =
			new VersionedForm<TaxonomieElementType>("form", ModelFactory
				.getCompoundChangeRecordingModel(taxonomieElementType, new DefaultModelManager(
					TaxonomieElementType.class)))
			{
				private static final long serialVersionUID = 1L;

				@Override
				public void onSubmit()
				{
					TaxonomieElementType type = getModelObject();
					type.saveOrUpdate();
					type.commit();
				}
			};
		add(form);
		RequiredTextField<String> codeField = new RequiredTextField<String>("afkorting");
		ComponentUtil.fixLength(codeField, TaxonomieElementType.class);
		form.add(codeField);
		RequiredTextField<String> naamField = new RequiredTextField<String>("naam");
		ComponentUtil.fixLength(naamField, TaxonomieElementType.class);
		form.add(naamField);
		form.add(ComponentFactory.getDataLabel("parent.naam").setVisible(
			taxonomieElementType.getParent() != null));
		form.add(ComponentFactory.getDataLabel("soort"));
		CheckBox inschrijfbaar = new CheckBox("inschrijfbaar");
		inschrijfbaar
			.setEnabled(taxonomieElementType.getSoort() == SoortTaxonomieElement.Verbintenisgebied);
		form.add(inschrijfbaar);
		CheckBox diplomeerbaar = new CheckBox("diplomeerbaar");
		diplomeerbaar
			.setEnabled(taxonomieElementType.getSoort() == SoortTaxonomieElement.Verbintenisgebied);
		form.add(diplomeerbaar);
		RequiredTextField<Integer> volgnummer =
			new RequiredTextField<Integer>("volgnummer", Integer.class);
		form.add(volgnummer);
		naamField.add(new UniqueConstraintValidator<String>(form, "Taxonomie-elementtype", "naam",
			"taxonomie"));
		volgnummer.add(new UniqueConstraintValidator<Integer>(form, "Taxonomie-elementtype",
			"volgnummer", "taxonomie"));

		TaxonomieElementTypeCombobox typeCombo =
			new TaxonomieElementTypeCombobox("parent", new TaxonomieProvider()
			{
				private static final long serialVersionUID = 1L;

				@Override
				public Taxonomie getTaxonomie()
				{
					return getTaxonomieElementType().getTaxonomie();
				}

			}, true);
		typeCombo.setNullValid(false);
		typeCombo.setRequired(true);
		typeCombo.setVisible(taxonomieElementType.getParent() == null);
		form.add(typeCombo);
	}

	/**
	 * @return Taxonomie-elementtype dat gekoppeld is aan dit panel
	 */
	public TaxonomieElementType getTaxonomieElementType()
	{
		return form.getModelObject();
	}

	public Form<TaxonomieElementType> getForm()
	{
		return form;
	}
}
