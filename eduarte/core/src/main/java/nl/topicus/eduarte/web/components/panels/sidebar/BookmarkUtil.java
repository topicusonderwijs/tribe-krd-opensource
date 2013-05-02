package nl.topicus.eduarte.web.components.panels.sidebar;

import nl.topicus.cobra.reflection.InvocationFailedException;
import nl.topicus.cobra.reflection.ReflectionUtil;
import nl.topicus.eduarte.entities.sidebar.Bookmark;
import nl.topicus.eduarte.entities.sidebar.BookmarkConstructorArgument;
import nl.topicus.eduarte.entities.sidebar.IContextInfoObject;
import nl.topicus.eduarte.util.XmlSerializer;
import nl.topicus.eduarte.web.pages.SecurePage;

import org.apache.wicket.markup.html.WebPage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.thoughtworks.xstream.XStreamException;

public class BookmarkUtil
{
	private static final Logger log = LoggerFactory.getLogger(BookmarkUtil.class);

	public static final class GotoBookmarkException extends Exception
	{
		private static final long serialVersionUID = 1L;

		public GotoBookmarkException(String message, Throwable cause)
		{
			super(message, cause);
		}
	}

	@SuppressWarnings("unchecked")
	public WebPage goToBookmark(Bookmark bookmark, SecurePage currentPage)
			throws GotoBookmarkException
	{
		try
		{
			Class< ? > clazz = Class.forName(bookmark.getPageClass());
			Class< ? >[] types = bookmark.getArgumentTypes();
			String[] serializedValues = bookmark.getArgumentValues();
			Object[] values = XmlSerializer.deserializeValues(serializedValues, types);
			int index = 0;
			for (BookmarkConstructorArgument arg : bookmark.getArguments())
			{
				if (arg.isHaalUitContext())
				{
					values[index] =
						currentPage
							.getContextValue((Class< ? extends IContextInfoObject>) types[index]);
				}
				index++;
			}
			return (WebPage) ReflectionUtil.invokeConstructor(clazz, values);
		}
		catch (ClassNotFoundException e)
		{
			log.error(e.toString(), e);
			throw new GotoBookmarkException("Kon pagina niet vinden: " + bookmark.getPageClass(), e);
		}
		catch (SecurityException e)
		{
			log.error(e.toString(), e);
			throw new GotoBookmarkException(
				"Security manager verbiedt het opvragen/aanroepen van constructor voor "
					+ bookmark.getPageClass(), e);
		}
		catch (ClassCastException e)
		{
			log.error(e.toString(), e);
			throw new GotoBookmarkException(bookmark.getPageClass() + " is geen pagina.", e);
		}
		catch (IllegalArgumentException e)
		{
			log.error(e.toString(), e);
			throw new GotoBookmarkException(bookmark.getPageClass()
				+ " kon niet aangemaakt worden. Waarschijnlijk is de bookmark gemaakt met een "
				+ "oudere versie van de applicatie en is deze niet meer compatible.", e);
		}
		catch (InvocationFailedException e)
		{
			log.error(e.toString(), e);
			throw new GotoBookmarkException(
				"De pagina waar naar wordt verwezen bestaat niet meer.", e);
		}
		catch (XStreamException e)
		{
			log.error(e.toString(), e);
			throw new GotoBookmarkException(
				"Opgeslagen argumenten voor pagina konden niet vertaald worden van tekst naar objecten.",
				e);
		}
	}
}