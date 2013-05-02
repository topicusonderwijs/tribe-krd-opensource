package nl.topicus.eduarte.web.components.panels.datapanel.table;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import nl.topicus.cobra.web.components.datapanel.CustomDataPanelContentDescription;
import nl.topicus.cobra.web.components.datapanel.columns.CustomPropertyColumn;
import nl.topicus.cobra.web.components.wiquery.tristate.TriState;
import nl.topicus.eduarte.app.EduArteApp;
import nl.topicus.eduarte.app.security.EduArtePrincipal;
import nl.topicus.eduarte.app.security.PrincipalGroup;
import nl.topicus.eduarte.entities.security.authentication.Account;
import nl.topicus.eduarte.entities.security.authorization.Recht;
import nl.topicus.eduarte.entities.security.authorization.Rol;
import nl.topicus.eduarte.web.components.panels.datapanel.columns.RechtenColumn;

import org.apache.wicket.security.actions.WaspAction;

public class RechtenTable extends CustomDataPanelContentDescription<PrincipalGroup>
{
	private static final long serialVersionUID = 1L;

	public RechtenTable(Account account)
	{
		super("Rechten");

		Map<EduArtePrincipal, TriState> selectionState = calcSelectionState(account);

		addColumn(new CustomPropertyColumn<PrincipalGroup>("Naam", "Naam", "omschrijving",
			"omschrijving"));
		addColumn(new CustomPropertyColumn<PrincipalGroup>("Module", "Module", "module.name",
			"module.name"));
		addColumn(new CustomPropertyColumn<PrincipalGroup>("Categorie", "Categorie", "parent",
			"parent"));
		for (Class< ? extends WaspAction> curAction : EduArteApp.get().getActions(
			account.getRechtenSoort()))
		{
			addColumn(new RechtenColumn(curAction, true, selectionState));
			addColumn(new RechtenColumn(curAction, false, selectionState));
		}
	}

	private Map<EduArtePrincipal, TriState> calcSelectionState(Account account)
	{
		Map<EduArtePrincipal, Set<EduArtePrincipal>> impliesRelation =
			EduArteApp.get().getImpliesRelation();
		Set<EduArtePrincipal> accountPrincipal = new HashSet<EduArtePrincipal>();
		for (Rol curRol : account.getRollenAsRol())
		{
			for (Recht curRecht : curRol.getRechten())
				try
				{
					accountPrincipal.add(curRecht.getPrincipal());
				}
				catch (ClassNotFoundException e)
				{
					// ignore de exception als een principal niet bestaat
				}
		}

		Map<EduArtePrincipal, TriState> selectionState = new HashMap<EduArtePrincipal, TriState>();
		for (EduArtePrincipal curPrincipal : EduArteApp.get().getPrincipals())
		{
			if (accountPrincipal.contains(curPrincipal))
			{
				selectionState.put(curPrincipal, TriState.On);
				for (EduArtePrincipal curImpliedPrincipal : impliesRelation.get(curPrincipal))
				{
					if (!TriState.On.equals(selectionState.get(curImpliedPrincipal)))
						selectionState.put(curImpliedPrincipal, TriState.Partial);
				}
			}
			else
			{
				if (!selectionState.containsKey(curPrincipal))
					selectionState.put(curPrincipal, TriState.Off);
			}
		}
		return selectionState;
	}
}
