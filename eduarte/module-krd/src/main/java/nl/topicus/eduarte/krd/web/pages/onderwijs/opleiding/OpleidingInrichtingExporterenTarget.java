package nl.topicus.eduarte.krd.web.pages.onderwijs.opleiding;

import java.util.HashSet;

import nl.topicus.cobra.web.components.datapanel.selection.ISelectionComponent;
import nl.topicus.eduarte.entities.opleiding.Opleiding;
import nl.topicus.eduarte.krd.jobs.OpleidingInrichtingExporterenJobDataMap;
import nl.topicus.eduarte.web.pages.shared.AbstractSelectieTarget;

import org.apache.wicket.markup.html.link.Link;

public class OpleidingInrichtingExporterenTarget extends
		AbstractSelectieTarget<Opleiding, Opleiding>
{
	private static final long serialVersionUID = 1L;

	public OpleidingInrichtingExporterenTarget()
	{
		super(OpleidingInrichtingExporterenOverzichtPage.class, "Exporteren");
	}

	@Override
	public Link<Void> createLink(String linkId, final ISelectionComponent<Opleiding, Opleiding> base)
	{
		return new Link<Void>(linkId)
		{
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick()
			{
				OpleidingInrichtingExporterenOverzichtPage overzichtPage =
					new OpleidingInrichtingExporterenOverzichtPage();
				overzichtPage.startJob(new OpleidingInrichtingExporterenJobDataMap(
					new HashSet<Opleiding>(base.getSelectedElements())));
				setResponsePage(overzichtPage);
			}
		};
	}
}
