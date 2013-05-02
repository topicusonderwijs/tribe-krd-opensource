package nl.topicus.eduarte.web.components.panels.datapanel.columns;

import java.util.Map;

import nl.topicus.cobra.security.Description;
import nl.topicus.cobra.web.components.datapanel.columns.AbstractCustomColumn;
import nl.topicus.cobra.web.components.datapanel.columns.ExportHeaderColumn;
import nl.topicus.cobra.web.components.wiquery.tristate.TriState;
import nl.topicus.eduarte.app.EduArteApp;
import nl.topicus.eduarte.app.Module;
import nl.topicus.eduarte.app.security.EduArtePrincipal;
import nl.topicus.eduarte.app.security.PrincipalGroup;
import nl.topicus.eduarte.app.security.actions.*;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;
import org.apache.wicket.security.actions.WaspAction;

public class RechtenColumn extends AbstractCustomColumn<PrincipalGroup> implements
		ExportHeaderColumn<PrincipalGroup>
{
	private static final long serialVersionUID = 1L;

	private Class< ? extends WaspAction> action;

	private Map<EduArtePrincipal, TriState> selectionState;

	private boolean write;

	public RechtenColumn(Class< ? extends WaspAction> action, boolean write,
			Map<EduArtePrincipal, TriState> selectionState)
	{
		super(
			action.getAnnotation(Description.class).value() + (write ? "(schrijven)" : "(lezen)"),
			write ? "schrijf" : "lees");
		this.action = action;
		this.write = write;
		this.selectionState = selectionState;
	}

	@Override
	public boolean isVisible()
	{
		Module module = action.getAnnotation(Module.class);
		return module == null || EduArteApp.get().isModuleActive(module.value());
	}

	public boolean isWrite()
	{
		return write;
	}

	public Class< ? extends WaspAction> getAction()
	{
		return action;
	}

	@Override
	public void populateItem(WebMarkupContainer cell, String componentId, WebMarkupContainer row,
			IModel<PrincipalGroup> rowModel, int span)
	{
		PrincipalGroup group = rowModel.getObject();
		TriState selected = selectionState.get(group.getPrincipal(action, write));
		String display;
		if (selected == null)
			display = "";
		else if (selected.equals(TriState.On))
			display = "Aan";
		else if (selected.equals(TriState.Off))
			display = "Uit";
		else
			display = "Geërfd";
		cell.add(new Label(componentId, display));
	}

	@Override
	public String getExportHeader()
	{
		String header;
		if (Docent.class.equals(action))
			header = "D";
		else if (Begeleider.class.equals(action))
			header = "M";
		else if (Verantwoordelijk.class.equals(action))
			header = "V";
		else if (Uitvoerend.class.equals(action))
			header = "U";
		else if (OrganisatieEenheid.class.equals(action))
			header = "O";
		else if (Instelling.class.equals(action))
			header = "I";
		else if (Beheer.class.equals(action))
			header = "B";
		else if (Deelnemer.class.equals(action))
			header = "D";
		else
			throw new IllegalStateException("Onknown action: " + action);

		return header + " (" + (write ? "w" : "r") + ")";
	}
}
