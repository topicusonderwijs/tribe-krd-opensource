package nl.topicus.eduarte.web.components.panels.datapanel.table;

import nl.topicus.cobra.web.components.datapanel.CustomDataPanelContentDescription;
import nl.topicus.cobra.web.components.datapanel.GroupProperty;
import nl.topicus.cobra.web.components.datapanel.CustomColumn.Positioning;
import nl.topicus.cobra.web.components.datapanel.columns.BooleanPropertyColumn;
import nl.topicus.cobra.web.components.datapanel.columns.CustomPropertyColumn;
import nl.topicus.cobra.web.components.datapanel.columns.ImageColumn;
import nl.topicus.eduarte.entities.curriculum.Curriculum;
import nl.topicus.eduarte.entities.curriculum.CurriculumOnderwijsproduct;

import org.apache.wicket.behavior.SimpleAttributeModifier;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.model.IModel;

public class CurriculumOnderwijsproductTable extends
		CustomDataPanelContentDescription<CurriculumOnderwijsproduct>
{
	private static final long serialVersionUID = 1L;

	private boolean overzicht;

	public CurriculumOnderwijsproductTable()
	{
		this(null);
	}

	public CurriculumOnderwijsproductTable(Curriculum curriculum)
	{
		super(curriculum != null ? "Curriculum voor cohort " + curriculum.getCohort().toString()
			: "Curriculumoverzicht");

		overzicht = (curriculum == null);

		createGroupBy();
		createColumns();
	}

	private void createGroupBy()
	{
		addGroupProperty(new GroupProperty<CurriculumOnderwijsproduct>("curriculum", "Curriculum",
			"curriculum"));
		addGroupProperty(new GroupProperty<CurriculumOnderwijsproduct>("curriculum.opleiding",
			"Opleiding", "curriculum.opleiding"));
		addGroupProperty(new GroupProperty<CurriculumOnderwijsproduct>("onderwijsproduct",
			"Onderwijsproduct", "onderwijsproduct"));

	}

	private void createColumns()
	{
		createCurriculumVelden();
		createOnderwijsproductVelden();
		createCurriculumOnderwijsproductVelden();
		createVolledigAfgenomenIconVeld();
		createOrgEnhLocatieVelden();
	}

	private void createCurriculumOnderwijsproductVelden()
	{
		addColumn(new CustomPropertyColumn<CurriculumOnderwijsproduct>("Curriculum", "Curriculum",
			"curriculum", "curriculum").setDefaultVisible(false));

		addColumn(new CustomPropertyColumn<CurriculumOnderwijsproduct>("Leerjaar", "Leerjaar",
			"leerjaar", "leerjaar"));
		addColumn(new CustomPropertyColumn<CurriculumOnderwijsproduct>("Periode", "Periode",
			"periode", "periode"));
		addColumn(new CustomPropertyColumn<CurriculumOnderwijsproduct>("Onderwijstijd",
			"Onderwijstijd", "onderwijstijd", "onderwijstijd"));

		addColumn(new CustomPropertyColumn<CurriculumOnderwijsproduct>(
			"OnderwijstijdOnderwijsproduct", "Onderwijstijd Onderwijsproduct",
			"totaleOnderwijstijdOnderwijsproduct").setDefaultVisible(!overzicht));
	}

	private void createOnderwijsproductVelden()
	{
		addColumn(new CustomPropertyColumn<CurriculumOnderwijsproduct>("Onderwijsproduct",
			"Onderwijsproduct", "onderwijsproduct.titel", "onderwijsproduct.titel"));
		addColumn(new CustomPropertyColumn<CurriculumOnderwijsproduct>("Onderwijsproductcode",
			"Onderwijsproductcode", "onderwijsproduct.code", "onderwijsproduct.code")
			.setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<CurriculumOnderwijsproduct>("Soort Onderwijsproduct",
			"Soort Onderwijsproduct", "onderwijsproduct.soortProduct",
			"onderwijsproduct.soortProduct").setDefaultVisible(false));
		addColumn(new BooleanPropertyColumn<CurriculumOnderwijsproduct>("Individueel",
			"Individueel", "onderwijsproduct.individueel", "onderwijsproduct.individueel")
			.setDefaultVisible(false));
		addColumn(new BooleanPropertyColumn<CurriculumOnderwijsproduct>("Onafhankelijk",
			"Onafhankelijk", "onderwijsproduct.onafhankelijk", "onderwijsproduct.onafhankelijk")
			.setDefaultVisible(false));
		addColumn(new BooleanPropertyColumn<CurriculumOnderwijsproduct>("Begeleid", "Begeleid",
			"onderwijsproduct.begeleid", "onderwijsproduct.begeleid").setDefaultVisible(false));
	}

	private void createCurriculumVelden()
	{
		addColumn(new CustomPropertyColumn<CurriculumOnderwijsproduct>("Opleiding", "Opleiding",
			"curriculum.opleiding.naam").setDefaultVisible(overzicht));
		addColumn(new CustomPropertyColumn<CurriculumOnderwijsproduct>("Opleidingscode",
			"Opleidingscode", "curriculum.opleiding.code").setDefaultVisible(false));

		addColumn(new CustomPropertyColumn<CurriculumOnderwijsproduct>("Cohort", "Cohort",
			"curriculum.cohort", "curriculum.cohort").setDefaultVisible(overzicht));
	}

	private void createOrgEnhLocatieVelden()
	{
		addColumn(new CustomPropertyColumn<CurriculumOnderwijsproduct>("Organisatie-eenheid",
			"Organisatie-eenheid", "curriculum.organisatieEenheid", "curriculum.organisatieEenheid")
			.setDefaultVisible(false));

		addColumn(new CustomPropertyColumn<CurriculumOnderwijsproduct>(
			"Organisatie-eenheid afkorting", "Organisatie-eenheid afkorting",
			"curriculum.organisatieEenheid.afkorting").setDefaultVisible(false));

		addColumn(new CustomPropertyColumn<CurriculumOnderwijsproduct>("Locatie", "Locatie",
			"curriculum.locatie", "curriculum.locatie").setDefaultVisible(false));
	}

	private void createVolledigAfgenomenIconVeld()
	{
		addColumn(new ImageColumn<CurriculumOnderwijsproduct>("VolledigAfgenomen", "",
			new ImageColumn.ImageFactory<CurriculumOnderwijsproduct>()
			{
				private static final long serialVersionUID = 1L;

				@Override
				public Image getImage(String id, IModel<CurriculumOnderwijsproduct> data)
				{
					CurriculumOnderwijsproduct curOndpr = data.getObject();
					Image img = new Image(id);
					if (!curOndpr.isVolledigAfgenomen())
					{
						img.add(new SimpleAttributeModifier("src",
							"../assets/img/icons/warning.gif"));
						img.add(new SimpleAttributeModifier("title", "Niet volledig afgenomen"));
					}
					else
						img.setVisible(false);
					return img;
				}
			}).setPositioning(Positioning.FIXED_LEFT));
	}
}
