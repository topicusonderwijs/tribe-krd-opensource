package nl.topicus.eduarte.web.behavior;

import nl.topicus.cobra.app.CobraRequestCycle;
import nl.topicus.cobra.security.RechtenSoort;
import nl.topicus.eduarte.entities.organisatie.LandelijkOfInstellingEntiteit;

import org.apache.wicket.Component;
import org.apache.wicket.behavior.AbstractBehavior;
import org.apache.wicket.model.IModel;

/**
 * Behavior die checkt of de ingelogde gebruiker editrechten heeft op een nullable
 * instellingentiteit. De gebruiker heeft hier rechten voor als hij/zij landelijk
 * beheerder is, of als het object gekoppeld is aan een instelling.
 * 
 * @author loite
 */
public class NullableInstellingEntiteitEditLinkVisibleBehavior extends AbstractBehavior
{
	private static final long serialVersionUID = 1L;

	private final IModel< ? extends LandelijkOfInstellingEntiteit> entiteitModel;

	/**
	 * Constructor
	 * 
	 * @param entiteitModel
	 *            Het model dat het object oplevert waartegen gecontroleerd moet worden.
	 *            Het model moet een NullableInstellingEntiteit opleveren.
	 */
	public NullableInstellingEntiteitEditLinkVisibleBehavior(
			IModel< ? extends LandelijkOfInstellingEntiteit> entiteitModel)
	{
		this.entiteitModel = entiteitModel;
	}

	@Override
	public void bind(Component component)
	{
		super.beforeRender(component);
		LandelijkOfInstellingEntiteit entiteit = entiteitModel.getObject();
		if (entiteit.isLandelijk()
			&& CobraRequestCycle.get().getAccountRechtenSoort() != RechtenSoort.BEHEER)
		{
			component.setVisibilityAllowed(false);
		}
	}

}
