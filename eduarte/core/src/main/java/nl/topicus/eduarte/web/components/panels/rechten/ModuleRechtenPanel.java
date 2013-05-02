package nl.topicus.eduarte.web.components.panels.rechten;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;

import nl.topicus.cobra.security.Description;
import nl.topicus.cobra.web.components.panels.TypedPanel;
import nl.topicus.eduarte.app.EduArteApp;
import nl.topicus.eduarte.app.security.EduArtePrincipal;
import nl.topicus.eduarte.app.security.PrincipalGroup;
import nl.topicus.eduarte.entities.security.authorization.Rol;

import org.apache.wicket.Component;
import org.apache.wicket.behavior.SimpleAttributeModifier;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.security.actions.WaspAction;

public class ModuleRechtenPanel extends TypedPanel<Rol>
{
	private static final long serialVersionUID = 1L;

	private SortedSet<PrincipalGroup> principalGroups;

	private RechtenPanel parent;

	public ModuleRechtenPanel(String id, IModel<Rol> rolModel,
			SortedSet<PrincipalGroup> principalGroups, RechtenPanel parent)
	{
		super(id, rolModel);
		this.principalGroups = principalGroups;
		this.parent = parent;

		addActionLabels();
		addRechtenClusters();
	}

	public IModel<Rol> getRolModel()
	{
		return getModel();
	}

	public Rol getRol()
	{
		return getModelObject();
	}

	public List<Class< ? extends WaspAction>> getUsedActions()
	{
		Set<Class< ? extends WaspAction>> availActions =
			new LinkedHashSet<Class< ? extends WaspAction>>(EduArteApp.get().getActions(
				getRol().getRechtenSoort()));
		Set<Class< ? extends WaspAction>> usedActions = new HashSet<Class< ? extends WaspAction>>();
		for (PrincipalGroup curGroep : principalGroups)
		{
			for (EduArtePrincipal curPrincipal : curGroep.getPrincipals())
			{
				usedActions.add(curPrincipal.getActionClass());
			}
		}

		availActions.retainAll(usedActions);
		return new ArrayList<Class< ? extends WaspAction>>(availActions);
	}

	private void addActionLabels()
	{
		RepeatingView actionLabels = new RepeatingView("actionLabel");
		RepeatingView rwLabels = new RepeatingView("rwLabel");
		for (Class< ? extends WaspAction> curAction : getUsedActions())
		{
			actionLabels.add(new Label(actionLabels.newChildId(), curAction.getAnnotation(
				Description.class).value()));
			rwLabels.add(new WebMarkupContainer(rwLabels.newChildId()));
		}
		add(actionLabels);
		add(rwLabels);
	}

	private void addRechtenClusters()
	{
		RepeatingView principalGroupsView = new RepeatingView("principalGroups");
		for (PrincipalGroup curPrincipalGroup : principalGroups)
		{
			WebMarkupContainer principalGroupContainer =
				new WebMarkupContainer(principalGroupsView.newChildId());
			principalGroupContainer.add(new Label("omschrijving", curPrincipalGroup
				.getOmschrijving()));
			principalGroupContainer.add(createActionsForPrincipalGroup(curPrincipalGroup));
			principalGroupsView.add(principalGroupContainer);
		}
		add(principalGroupsView);
	}

	private RepeatingView createActionsForPrincipalGroup(PrincipalGroup principalGroup)
	{
		RepeatingView actions = new RepeatingView("actions");
		for (Class< ? extends WaspAction> curAction : getUsedActions())
		{
			WebMarkupContainer actionContainer = new WebMarkupContainer(actions.newChildId());
			actionContainer.add(createCheckBox("readCheck", curAction, false, principalGroup));
			actionContainer.add(createCheckBox("writeCheck", curAction, true, principalGroup));
			actions.add(actionContainer);
		}
		return actions;
	}

	private Component createCheckBox(String checkboxId, Class< ? extends WaspAction> action,
			boolean write, PrincipalGroup principalGroup)
	{
		final EduArtePrincipal principal = principalGroup.getPrincipal(action, write);
		if (principal == null)
			return new WebMarkupContainer(checkboxId);

		RechtenTriStateCheckBox ret =
			new RechtenTriStateCheckBox(checkboxId, new RechtenSetRolModel(principal,
				getRolModel(), parent.calcImpliedCount(principal, getRol())))
			{
				private static final long serialVersionUID = 1L;

				@Override
				protected List<String> getImpliedCheckboxes()
				{
					return parent.getImpliedCheckboxIds(principal);
				}
			};
		parent.addCheckbox(principal, ret);
		ret.setEnabled(!parent.isReadOnly()
			&& !principal.getAuthorisatieNiveau().implies(getRol().getAuthorisatieNiveau()));
		ret.add(new SimpleAttributeModifier("title", principal.getDescription()));
		return ret;
	}
}
