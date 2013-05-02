package nl.topicus.eduarte.tester;

import nl.topicus.cobra.web.components.modal.ModalWindowContainer;

import org.apache.wicket.util.tester.DummyPanelPage;
import org.apache.wicket.util.tester.TestPanelSource;

public class EduArteDummyPanelPage extends DummyPanelPage
{

	public EduArteDummyPanelPage(TestPanelSource testPanelSource)
	{
		super(testPanelSource);

		add(new ModalWindowContainer("modalWindowContainer", this));
	}
}
