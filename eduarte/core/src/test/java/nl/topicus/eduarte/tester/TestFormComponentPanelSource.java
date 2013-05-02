package nl.topicus.eduarte.tester;

import org.apache.wicket.IClusterable;
import org.apache.wicket.markup.html.form.FormComponentPanel;

public interface TestFormComponentPanelSource extends IClusterable
{
	/**
	 * Defines a <code>FormComponentPanel</code> instance source for
	 * <code>WicketTester</code>.
	 * 
	 * @param panelId
	 *            <code>Component</code> id of the test <code>FormComponentPanel</code>
	 * @return test <code>Panel</code> instance -- note that the test
	 *         <code>FormComponentPanel</code>'s <code>Component</code> id must use the
	 *         given <code>panelId</code>.
	 */
	FormComponentPanel< ? > getTestPanel(final String panelId);
}
