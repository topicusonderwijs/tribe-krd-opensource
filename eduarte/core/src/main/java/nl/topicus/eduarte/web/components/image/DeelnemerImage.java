package nl.topicus.eduarte.web.components.image;

import nl.topicus.eduarte.entities.personen.Deelnemer;
import nl.topicus.eduarte.entities.personen.Persoon;

import org.apache.wicket.behavior.SimpleAttributeModifier;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

/**
 * Easy to use Image voor een deelnemer zijn afbeelding.
 * 
 * @author hoeve
 */
public class DeelnemerImage extends PersoonImage
{
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor met format = "jpg"
	 * 
	 * @param id
	 *            het id van de Image
	 * @param deelnemerModel
	 *            de deelnemer in question.
	 */
	public DeelnemerImage(String id, IModel<Deelnemer> deelnemerModel)
	{
		this(id, deelnemerModel, "jpg");
	}

	/**
	 * @param id
	 *            het id van de Image
	 * @param deelnemerModel
	 *            de deelnemer in question.
	 * @param format
	 *            het formaat van de resource
	 */
	public DeelnemerImage(String id, IModel<Deelnemer> deelnemerModel, String format)
	{
		super(id, new PropertyModel<Persoon>(deelnemerModel, "persoon"));

		Deelnemer deelnemer = deelnemerModel.getObject();
		if (deelnemer != null)
			add(new SimpleAttributeModifier("alt", "Foto van deelnemer "
				+ deelnemer.getDeelnemernummer() + "."));
		else
			add(new SimpleAttributeModifier("alt", "Deelnemer niet gevonden."));
	}
}
