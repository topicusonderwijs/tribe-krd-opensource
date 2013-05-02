package nl.topicus.eduarte.krd.event.handler;

import nl.topicus.eduarte.app.signalering.handler.PageLinkEventHandler;
import nl.topicus.eduarte.krd.entities.NieuweMentorVoorDeelnemerEvent;
import nl.topicus.eduarte.web.pages.groep.GroepKaartPage;

public class NieuweMentorVoorDeelnemerEventHandler extends
		PageLinkEventHandler<NieuweMentorVoorDeelnemerEvent>
{
	public NieuweMentorVoorDeelnemerEventHandler()
	{
		super(GroepKaartPage.class);
	}
}
