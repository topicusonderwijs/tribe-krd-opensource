package nl.topicus.eduarte.core.principals;

import java.util.List;

import nl.topicus.cobra.security.Actions;
import nl.topicus.cobra.security.Description;
import nl.topicus.eduarte.app.security.AbstractEduArtePrincipalSource;
import nl.topicus.eduarte.app.security.EduArtePrincipal;
import nl.topicus.eduarte.app.security.actions.Instelling;
import nl.topicus.eduarte.app.security.actions.OrganisatieEenheid;

import org.apache.wicket.Component;
import org.apache.wicket.security.actions.WaspActionFactory;
import org.apache.wicket.security.hive.BasicHive;

@Actions( {Instelling.class, OrganisatieEenheid.class})
@Description("Snelzoekveld")
public class Snelzoeken extends AbstractEduArtePrincipalSource
{
	private static final long serialVersionUID = 1L;

	@Override
	public List<EduArtePrincipal> addPrincipalsToHive(BasicHive hive,
			WaspActionFactory actionFactory, List<Class< ? extends Component>> componentClasses)
	{
		return super.addPrincipalsToHive(hive, actionFactory, componentClasses);
	}
}
