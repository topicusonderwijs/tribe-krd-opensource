package nl.topicus.eduarte.web.components.factory;

import java.util.List;

import nl.topicus.cobra.modules.AbstractModuleComponentFactory;
import nl.topicus.cobra.web.components.datapanel.CustomDataPanelContentDescription;
import nl.topicus.eduarte.entities.personen.Deelnemer;
import nl.topicus.eduarte.entities.resultaatstructuur.Resultaatstructuur;
import nl.topicus.eduarte.entities.resultaatstructuur.Toets;
import nl.topicus.eduarte.web.components.modalwindow.DummyLink;
import nl.topicus.eduarte.web.components.panels.EduArteDataPanel;

import org.apache.wicket.Component;
import org.apache.wicket.markup.html.link.Link;

public class ResultatenDummyComponentFactory extends AbstractModuleComponentFactory implements
		StructuurLinkFactory, InleverenLinkFactory

{
	private static class DisabledDummyLink extends DummyLink
	{
		private static final long serialVersionUID = 1L;

		private DisabledDummyLink(String id)
		{
			super(id);
		}

		@Override
		public boolean isEnabled()
		{
			return false;
		}
	}

	public ResultatenDummyComponentFactory()
	{
		super(0);
	}

	@Override
	public <T> void postProcessDatapanel(EduArteDataPanel<T> datapanel)
	{
		// do nothing
	}

	@Override
	public void addInleverenColumn(CustomDataPanelContentDescription<Toets> table,
			List<Deelnemer> deelnemers)
	{
		// do nothing
	}

	@Override
	public Link< ? > createStructuurLink(String id, Resultaatstructuur structuur, Component parent)
	{
		return new DisabledDummyLink(id);
	}
}
