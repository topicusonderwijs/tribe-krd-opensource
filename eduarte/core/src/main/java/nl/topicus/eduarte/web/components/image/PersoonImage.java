package nl.topicus.eduarte.web.components.image;

import nl.topicus.cobra.web.components.image.Silhouet;
import nl.topicus.eduarte.entities.personen.Persoon;

import org.apache.wicket.markup.html.PackageResource;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.image.resource.DynamicImageResource;
import org.apache.wicket.model.IModel;

/**
 * Easy to use Image voor een persoon zijn afbeelding.
 * 
 * @author hoeve
 */
public class PersoonImage extends Image
{
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor met format = "jpg"
	 * 
	 * @param id
	 *            het id van de Image
	 * @param persoonModel
	 *            de persoon in question.
	 */
	public PersoonImage(String id, IModel<Persoon> persoonModel)
	{
		this(id, persoonModel, "jpg");
	}

	/**
	 * @param id
	 *            het id van de Image
	 * @param persoonModel
	 *            de persoon in question.
	 * @param format
	 *            het formaat van de resource
	 */
	public PersoonImage(String id, IModel<Persoon> persoonModel, String format)
	{
		super(id, persoonModel);

		byte[] afbeelding = getAfbeelding();
		if (afbeelding != null && afbeelding.length > 0)
		{
			setImageResource(new DynamicImageResource(format)
			{
				private static final long serialVersionUID = 1L;

				@Override
				protected byte[] getImageData()
				{
					return getAfbeelding();
				}
			});
		}
		else
			setImageResource(PackageResource.get(Silhouet.class, "smiley.jpg"));
	}

	public Persoon getPersoon()
	{
		return (Persoon) getDefaultModelObject();
	}

	public byte[] getAfbeelding()
	{
		Persoon persoon = getPersoon();

		if (persoon == null)
			return null;

		return persoon.getAfbeeldingBytes();
	}
}
