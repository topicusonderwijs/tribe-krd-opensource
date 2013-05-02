package nl.topicus.eduarte.web.components.panels.rechten;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import nl.topicus.cobra.modules.CobraAnnotationAuthorizationModule;
import nl.topicus.cobra.modules.IModuleKey;
import nl.topicus.cobra.web.components.panels.TypedPanel;
import nl.topicus.cobra.web.components.wiquery.IEDisabledAnimation;
import nl.topicus.eduarte.app.EduArteApp;
import nl.topicus.eduarte.app.security.EduArtePrincipal;
import nl.topicus.eduarte.app.security.PrincipalGroup;
import nl.topicus.eduarte.entities.security.authorization.Recht;
import nl.topicus.eduarte.entities.security.authorization.Rol;

import org.apache.wicket.behavior.SimpleAttributeModifier;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.model.IModel;
import org.odlabs.wiquery.core.options.LiteralOption;
import org.odlabs.wiquery.ui.accordion.Accordion;
import org.odlabs.wiquery.ui.accordion.AccordionHeader;
import org.odlabs.wiquery.ui.tabs.Tabs;

public class RechtenPanel extends TypedPanel<Rol>
{
	private static final long serialVersionUID = 1L;

	private Map<EduArtePrincipal, Set<EduArtePrincipal>> impliesRelation;

	private Map<EduArtePrincipal, String> checkboxIds = new HashMap<EduArtePrincipal, String>();

	private boolean readOnly;

	public RechtenPanel(String id, IModel<Rol> rolModel, boolean readOnly)
	{
		super(id, rolModel);

		this.readOnly = readOnly;

		impliesRelation = EduArteApp.get().getImpliesRelation();

		RepeatingView labelView = new RepeatingView("moduleTabHeaders");
		Map<IModuleKey, Label> moduleLabels = createModuleLabels(labelView);

		RepeatingView moduleView = new RepeatingView("modules");
		createModulePanels(moduleView, moduleLabels);

		Tabs moduleTabs = new Tabs("moduleTabs");
		moduleTabs.add(labelView);
		moduleTabs.add(moduleView);
		add(moduleTabs);
	}

	public IModel<Rol> getRolModel()
	{
		return getModel();
	}

	public boolean isReadOnly()
	{
		return readOnly;
	}

	public void addCheckbox(EduArtePrincipal principal, RechtenTriStateCheckBox checkbox)
	{
		checkboxIds.put(principal, checkbox.getCheckboxId());
	}

	public List<String> getImpliedCheckboxIds(EduArtePrincipal principal)
	{
		List<String> ret = new ArrayList<String>();
		for (EduArtePrincipal curImplied : impliesRelation.get(principal))
		{
			if (checkboxIds.containsKey(curImplied))
				ret.add("'#" + checkboxIds.get(curImplied) + "'");
		}
		return ret;
	}

	public int calcImpliedCount(EduArtePrincipal principal, Rol rol)
	{
		int ret = 0;
		for (Recht curRecht : rol.getRechten())
		{
			try
			{
				EduArtePrincipal curPrincipal = curRecht.getPrincipal();
				if (impliesRelation.containsKey(curPrincipal)
					&& impliesRelation.get(curPrincipal).contains(principal))
					ret++;
			}
			catch (ClassNotFoundException e)
			{
				// ignore de exception als een principal niet bestaat
			}
		}
		return ret;
	}

	private Map<IModuleKey, Label> createModuleLabels(RepeatingView labelView)
	{
		Map<IModuleKey, Label> ret = new HashMap<IModuleKey, Label>();
		for (CobraAnnotationAuthorizationModule curModule : EduArteApp.get().getModules(
			CobraAnnotationAuthorizationModule.class))
		{
			IModuleKey curKey = curModule.getKey();
			WebMarkupContainer labelContainer = new WebMarkupContainer(labelView.newChildId());
			labelView.add(labelContainer);
			Label label = new Label("label", curKey.getName());
			labelContainer.add(label);
			ret.put(curKey, label);
		}
		return ret;
	}

	private void createModulePanels(RepeatingView moduleView, Map<IModuleKey, Label> moduleLabels)
	{
		List<PrincipalGroup> principalGroups = EduArteApp.get().getPrincipalGroups();
		for (CobraAnnotationAuthorizationModule curModule : EduArteApp.get().getModules(
			CobraAnnotationAuthorizationModule.class))
		{
			Accordion moduleAccordion =
				createModuleAccordion(moduleView.newChildId(), curModule.getKey(), principalGroups);
			if (moduleAccordion == null)
			{
				moduleLabels.get(curModule.getKey()).setVisible(false);
			}
			else
			{
				moduleLabels.get(curModule.getKey()).add(
					new SimpleAttributeModifier("href", "#" + moduleAccordion.getMarkupId()));
				moduleView.add(moduleAccordion);
			}
		}
	}

	private Accordion createModuleAccordion(String id, IModuleKey module,
			List<PrincipalGroup> principalGroups)
	{
		Accordion accordion = new Accordion(id);
		accordion.setHeader(new AccordionHeader(new LiteralOption("h3")));
		accordion.setAnimated(new IEDisabledAnimation());
		accordion.setAutoHeight(false);
		boolean enabledActions = false;

		RepeatingView parentsView = new RepeatingView("parents");
		accordion.add(parentsView);
		for (String curParent : collectParents(principalGroups))
		{
			WebMarkupContainer parentPanel =
				createParentPanel(parentsView.newChildId(), module, curParent, principalGroups);
			if (parentPanel != null)
			{
				parentsView.add(parentPanel);
				enabledActions = true;
			}
		}
		return enabledActions ? accordion : null;
	}

	private SortedSet<String> collectParents(List<PrincipalGroup> principalGroups)
	{
		SortedSet<String> ret = new TreeSet<String>();
		for (PrincipalGroup curGroup : principalGroups)
			ret.add(curGroup.getParent());
		return ret;
	}

	private WebMarkupContainer createParentPanel(String id, IModuleKey module, String parent,
			List<PrincipalGroup> principalGroups)
	{
		WebMarkupContainer container = new WebMarkupContainer(id);
		ModuleRechtenPanel parentRechtenPanel =
			new ModuleRechtenPanel("moduleParentRechten", getRolModel(),
				getGroupsForModuleAndParent(principalGroups, module, parent), this);
		if (parentRechtenPanel.getUsedActions().isEmpty())
		{
			return null;
		}
		container.add(new Label("label", parent));
		container.add(parentRechtenPanel);
		return container;
	}

	private SortedSet<PrincipalGroup> getGroupsForModuleAndParent(
			List<PrincipalGroup> principalGroups, IModuleKey module, String parent)
	{
		SortedSet<PrincipalGroup> ret = new TreeSet<PrincipalGroup>();
		for (PrincipalGroup curGroup : principalGroups)
		{
			if (curGroup.getModule().equals(module) && curGroup.getParent().equals(parent))
				ret.add(curGroup);
		}
		return ret;
	}
}
