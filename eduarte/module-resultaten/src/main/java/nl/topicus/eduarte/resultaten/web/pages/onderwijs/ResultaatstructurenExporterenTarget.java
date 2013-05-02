package nl.topicus.eduarte.resultaten.web.pages.onderwijs;

import java.util.HashSet;

import nl.topicus.cobra.web.components.datapanel.selection.ISelectionComponent;
import nl.topicus.eduarte.entities.onderwijsproduct.Onderwijsproduct;
import nl.topicus.eduarte.resultaten.jobs.ResultaatstructurenExporterenJobDataMap;
import nl.topicus.eduarte.web.pages.shared.AbstractSelectieTarget;

import org.apache.wicket.markup.html.link.Link;

public class ResultaatstructurenExporterenTarget extends
		AbstractSelectieTarget<Onderwijsproduct, Onderwijsproduct>
{
	private static final long serialVersionUID = 1L;

	public ResultaatstructurenExporterenTarget()
	{
		super(ResultaatstructurenExporterenOverzichtPage.class, "Exporteren");
	}

	@Override
	public Link<Void> createLink(String linkId,
			final ISelectionComponent<Onderwijsproduct, Onderwijsproduct> base)
	{
		return new Link<Void>(linkId)
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick()
			{
				ResultaatstructurenExporterenOverzichtPage overzichtPage =
					new ResultaatstructurenExporterenOverzichtPage();
				overzichtPage.startJob(new ResultaatstructurenExporterenJobDataMap(
					new HashSet<Onderwijsproduct>(base.getSelectedElements())));
				setResponsePage(overzichtPage);
			}
		};
	}
}
