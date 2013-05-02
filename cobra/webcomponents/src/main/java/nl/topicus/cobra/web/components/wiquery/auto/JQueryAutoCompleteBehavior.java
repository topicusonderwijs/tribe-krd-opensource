package nl.topicus.cobra.web.components.wiquery.auto;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import nl.topicus.cobra.util.StringUtil;

import org.apache.wicket.Application;
import org.apache.wicket.IRequestTarget;
import org.apache.wicket.RequestCycle;
import org.apache.wicket.ajax.AbstractDefaultAjaxBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.protocol.http.WebResponse;

public class JQueryAutoCompleteBehavior<T> extends AbstractDefaultAjaxBehavior
{
	private static final long serialVersionUID = 1L;

	private IAutoCompleteBase<T> parent;

	public JQueryAutoCompleteBehavior(IAutoCompleteBase<T> parent)
	{
		this.parent = parent;
	}

	@Override
	protected void respond(AjaxRequestTarget target)
	{
		RequestCycle cycle = RequestCycle.get();
		final String query = cycle.getRequest().getParameter("term");
		cycle.setRequestTarget(new IRequestTarget()
		{
			@Override
			public void detach(RequestCycle requestCycle)
			{
			}

			@Override
			public void respond(RequestCycle requestCycle)
			{
				IAutoCompleteChoiceRenderer< ? super T> renderer = parent.getRenderer();
				WebResponse r = (WebResponse) requestCycle.getResponse();

				// Determine encoding
				final String encoding =
					Application.get().getRequestCycleSettings().getResponseRequestEncoding();
				r.setCharacterEncoding(encoding);
				r.setContentType("text/xml; charset=" + encoding);

				// Make sure it is not cached by a
				r.setHeader("Expires", "Mon, 26 Jul 1997 05:00:00 GMT");
				r.setHeader("Cache-Control", "no-cache, must-revalidate");
				r.setHeader("Pragma", "no-cache");

				T exactMatch = null;
				List<T> startMatch = new ArrayList<T>();
				List<T> choices = new ArrayList<T>(parent.getChoices(query));
				Iterator<T> it = choices.iterator();
				while (it.hasNext())
				{
					T curChoice = it.next();
					if (exactMatch == null
						&& renderer.getDisplayValue(curChoice).equalsIgnoreCase(query))
					{
						it.remove();
						exactMatch = curChoice;
					}
					else if (renderer.getDisplayValue(curChoice).toUpperCase().startsWith(
						query.toUpperCase()))
					{
						it.remove();
						startMatch.add(curChoice);
					}
				}
				choices.addAll(0, startMatch);
				if (exactMatch != null)
					choices.add(0, exactMatch);

				r.write("[");
				boolean first = true;
				for (T curObject : choices)
				{
					if (!first)
					{
						r.write(",");
					}
					first = false;

					String displayValue =
						StringUtil.escapeForJavascriptString(renderer.getDisplayValue(curObject));
					String idValue =
						StringUtil.escapeForJavascriptString(renderer.getIdValue(curObject));
					String fieldValue =
						StringUtil.escapeForJavascriptString(renderer.getFieldValue(curObject));

					r.write(" { ");
					r.write("\"id\" : \"" + idValue + "\" , ");
					r.write("\"label\" : \"" + fieldValue + "\" , ");
					r.write("\"value\" : \"" + displayValue + "\"");
					r.write(" } ");
				}
				r.write("]");
			}
		});
	}
}
