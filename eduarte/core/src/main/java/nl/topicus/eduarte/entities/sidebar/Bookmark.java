package nl.topicus.eduarte.entities.sidebar;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;

import nl.topicus.cobra.util.Asserts;
import nl.topicus.cobra.util.StringUtil;
import nl.topicus.eduarte.entities.organisatie.OrganisatieEntiteit;
import nl.topicus.eduarte.entities.security.authentication.Account;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Index;

/**
 * Bookmark in de sidebar die onder andere constructor argumenten bevat waarmee een
 * specifieke pagina in een specifieke staat opgeroepen kan worden.
 * 
 * @author loite
 */
@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
public class Bookmark extends OrganisatieEntiteit implements Comparable<Bookmark>
{
	private static final long serialVersionUID = 1L;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "account", nullable = false)
	@Index(name = "idx_sidebarBookmark_account")
	private Account account;

	@Column(length = 100, nullable = false)
	private String omschrijving;

	/**
	 * Indicatie of deze bookmark alleen getoond moet worden wanneer de gebruiker op de
	 * pagina zit waarvoor deze bookmark is, of ook op andere pagina's.
	 */
	@Column(nullable = false)
	private boolean pagePrivate;

	/**
	 * Class name van de pagina van deze bookmark
	 */
	@Column(length = 256, nullable = false)
	private String pageClass;

	public enum SoortBookmark
	{
		Bookmark,
		ToDo;
	}

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private SoortBookmark soort;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "bookmark")
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
	@OrderBy(value = "volgorde")
	@BatchSize(size = 100)
	private List<BookmarkConstructorArgument> arguments;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "bookmarkFolder", nullable = true)
	@Index(name = "idx_Bookmark_bookmarkFolder")
	private BookmarkFolder bookmarkFolder;

	public Bookmark()
	{
	}

	/**
	 * Maakt een nieuwe lijst met constructor arguments uit de gegeven arrays.
	 * 
	 * @param types
	 *            de constructor argument types
	 * @param values
	 *            de waarden in geserialiseerde vorm
	 * @param contextParameterTypes
	 *            Alle classes waarvoor een context argument gemaakt moet worden
	 * @return de lijst met ctor arguments voor deze bookmark
	 */
	public List<BookmarkConstructorArgument> createBookmarkConstructorArguments(
			List<Class< ? >> types, List<String> values,
			List<Class< ? extends IContextInfoObject>> contextParameterTypes)
	{
		Asserts.assertNotNull("types", types);
		Asserts.assertNotNull("values", values);
		assert (types.size() == values.size()) : "Gegeven lijsten moeten dezelfde lengte hebben";
		List<BookmarkConstructorArgument> res = new ArrayList<BookmarkConstructorArgument>();
		for (int index = 0; index < types.size(); index++)
		{
			BookmarkConstructorArgument arg = new BookmarkConstructorArgument(this);
			arg.setClassName(types.get(index).getName());
			arg.setVolgorde(index);
			if (contextParameterTypes.contains(types.get(index)))
			{
				arg.setHaalUitContext(true);
			}
			else
			{
				arg.setWaarde(values.get(index));
			}
			res.add(arg);
		}
		return res;
	}

	/**
	 * @return Een array van classes die de constructor argument types voor deze bookmark
	 *         weergeven.
	 * @throws ClassNotFoundException
	 *             Als een van de classes van de arguments niet gevonden kan worden.
	 */
	public Class< ? >[] getArgumentTypes() throws ClassNotFoundException
	{
		List<BookmarkConstructorArgument> args = getArguments();
		Class< ? >[] types = new Class< ? >[args.size()];
		int index = 0;
		for (BookmarkConstructorArgument arg : args)
		{
			types[index] = Class.forName(arg.getClassName());
			index++;
		}
		return types;
	}

	/**
	 * @return Een array met daarin de geserialiseerde waarden van de constructor
	 *         arguments van deze bookmark.
	 */
	public String[] getArgumentValues()
	{
		List<BookmarkConstructorArgument> args = getArguments();
		String[] values = new String[args.size()];
		int index = 0;
		for (BookmarkConstructorArgument arg : args)
		{
			values[index] = arg.getWaarde();
			index++;
		}
		return values;
	}

	/**
	 * @return true als deze bookmark minimaal 1 contextgevoelig argument bevat
	 */
	public boolean containsContextArguments()
	{
		for (BookmarkConstructorArgument arg : getArguments())
		{
			if (arg.isHaalUitContext())
			{
				return true;
			}
		}
		return false;
	}

	/**
	 * @return Een lijst met alle ctor argumenten die afhankelijk van de context zijn.
	 * @throws ClassNotFoundException
	 */
	@SuppressWarnings("unchecked")
	public List<Class< ? extends IContextInfoObject>> getContextArgumentTypes()
			throws ClassNotFoundException
	{
		List<Class< ? extends IContextInfoObject>> res =
			new ArrayList<Class< ? extends IContextInfoObject>>();
		for (BookmarkConstructorArgument arg : getArguments())
		{
			if (arg.isHaalUitContext())
			{
				res.add((Class< ? extends IContextInfoObject>) Class.forName(arg.getClassName()));
			}
		}
		return res;
	}

	public Account getAccount()
	{
		return account;
	}

	public void setAccount(Account account)
	{
		this.account = account;
	}

	public String getOmschrijving()
	{
		return omschrijving;
	}

	public void setOmschrijving(String omschrijving)
	{
		this.omschrijving = omschrijving;
	}

	public List<BookmarkConstructorArgument> getArguments()
	{
		return arguments;
	}

	public void setArguments(List<BookmarkConstructorArgument> arguments)
	{
		this.arguments = arguments;
	}

	public String getPageClass()
	{
		return pageClass;
	}

	public void setPageClass(String pageClass)
	{
		this.pageClass = pageClass;
	}

	public boolean isPagePrivate()
	{
		return pagePrivate;
	}

	public void setPagePrivate(boolean pagePrivate)
	{
		this.pagePrivate = pagePrivate;
	}

	@Override
	public void delete()
	{
		for (BookmarkConstructorArgument arg : getArguments())
		{
			arg.delete();
		}
		super.delete();
	}

	@Override
	public int compareTo(Bookmark arg0)
	{
		if (arg0 == null || StringUtil.isEmpty(arg0.getOmschrijving()))
			return 1;

		return getOmschrijving().compareTo(arg0.getOmschrijving());
	}

	public void setSoort(SoortBookmark soort)
	{
		this.soort = soort;
	}

	public SoortBookmark getSoort()
	{
		return soort;
	}

	public void setBookmarkFolder(BookmarkFolder folder)
	{
		this.bookmarkFolder = folder;
	}

	public BookmarkFolder getBookmarkFolder()
	{
		return bookmarkFolder;
	}
}
