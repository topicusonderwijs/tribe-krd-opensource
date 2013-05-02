package nl.topicus.eduarte.web.components.panels.datapanel.table;

import nl.topicus.cobra.web.components.datapanel.CustomDataPanelContentDescription;
import nl.topicus.cobra.web.components.datapanel.GroupProperty;
import nl.topicus.cobra.web.components.datapanel.columns.CustomPropertyColumn;
import nl.topicus.cobra.web.components.datapanel.columns.ImageColumn;
import nl.topicus.eduarte.entities.participatie.AbsentieMelding;

import org.apache.wicket.behavior.SimpleAttributeModifier;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.model.IModel;

public class AbsentiemeldingTable extends CustomDataPanelContentDescription<AbsentieMelding>
{
	private static final long serialVersionUID = 1L;

	public AbsentiemeldingTable()
	{
		super("Absentiemeldingen");
		createColumns();
		createGroupProperties();
	}

	private void createColumns()
	{
		addColumn(new CustomPropertyColumn<AbsentieMelding>("Reden", "Reden",
			"absentieReden.omschrijving", "absentieReden.omschrijving"));
		addColumn(new CustomPropertyColumn<AbsentieMelding>("Datum/tijd", "Datum/tijd",
			"beginDatumTijd", "beginEind"));
		addColumn(new CustomPropertyColumn<AbsentieMelding>("Begindatum/tijd", "Begindatum/tijd",
			"beginDatumTijd", "beginDatumTijd").setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<AbsentieMelding>("Einddatum/tijd", "Einddatum/Tijd",
			"eindDatumTijd", "eindDatumTijd").setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<AbsentieMelding>("Beginlesuur", "Beginlesuur",
			"beginLesuur", "beginLesuur").setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<AbsentieMelding>("Eindlesuur", "Eindlesuur",
			"eindLesuur", "eindLesuur").setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<AbsentieMelding>("Opmerkingen", "Opmerkingen",
			"opmerkingen", "opmerkingen"));
		addColumn(new ImageColumn<AbsentieMelding>("Herhalend", "Her.",
			new ImageColumn.ImageFactory<AbsentieMelding>()
			{
				private static final long serialVersionUID = 1L;

				@Override
				public Image getImage(String id, IModel<AbsentieMelding> data)
				{
					AbsentieMelding melding = data.getObject();
					Image img = new Image(id);
					if (melding.getHerhalendeAbsentieMelding() != null)
					{
						img
							.add(new SimpleAttributeModifier("src",
								"../assets/img/icons/repeat.png"));
						img.add(new SimpleAttributeModifier("title", "Herhalend"));
					}
					else
						img.setVisible(false);
					return img;
				}
			}));
		addColumn(new CustomPropertyColumn<AbsentieMelding>("Afgehandeld", "Afgehandeld",
			"afgehandeld", "afgehandeldOmschrijving").setDefaultVisible(false));
		addColumn(new CustomPropertyColumn<AbsentieMelding>("EigenMelding", "Eigen melding",
			"eigenmelding", "isEigenaarOmschrijving").setDefaultVisible(false));
	}

	private void createGroupProperties()
	{
		addGroupProperty(new GroupProperty<AbsentieMelding>("groepeerDatumOmschrijving",
			"Datum/tijd", "beginDatumTijd"));
		addGroupProperty(new GroupProperty<AbsentieMelding>("absentieReden.omschrijving", "Reden",
			"absentieReden.omschrijving"));
		addGroupProperty(new GroupProperty<AbsentieMelding>("afgehandeldOmschrijving",
			"Afgehandeld", "afgehandeld"));
	}
}
