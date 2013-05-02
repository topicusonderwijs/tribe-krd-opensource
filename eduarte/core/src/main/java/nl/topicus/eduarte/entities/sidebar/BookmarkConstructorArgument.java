package nl.topicus.eduarte.entities.sidebar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.UniqueConstraint;

import nl.topicus.eduarte.entities.organisatie.InstellingEntiteit;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Index;

/**
 * Argument voor een constructor van een pagina voor een bookmark.
 * 
 * @author loite
 */
@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
@javax.persistence.Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"bookmark",
	"volgorde"})})
public class BookmarkConstructorArgument extends InstellingEntiteit
{
	private static final long serialVersionUID = 1L;

	/**
	 * Volgorde van de constructor args.
	 */
	@Column(nullable = false)
	private int volgorde;

	/**
	 * Class name van het constructor argument. Het kan voorkomen dat de waarde als een
	 * ander soort object is opgeslagen dan het gedefinieerde type van het constructor
	 * argument, namelijk als de waarde een subclass is van het type van het constructor
	 * argument, of als de waarde in een IModel opgeslagen is.
	 */
	@Column(nullable = false, length = 256)
	private String className;

	/**
	 * XML-representatie van de waarde van dit argument.
	 */
	@Lob()
	private String waarde;

	/**
	 * Geeft aan of deze waarde uit de context gehaald moet worden. Mag alleen op true
	 * gezet worden als deze waarde verwijst naar een class die de interface
	 * IContextInfoObject implementeert.
	 */
	@Column(nullable = false)
	private boolean haalUitContext;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "bookmark", nullable = false)
	@Index(name = "idx_bmctorarg_bookmark")
	private Bookmark bookmark;

	public BookmarkConstructorArgument()
	{
	}

	public BookmarkConstructorArgument(Bookmark bookmark)
	{
		setBookmark(bookmark);
	}

	public int getVolgorde()
	{
		return volgorde;
	}

	public void setVolgorde(int volgorde)
	{
		this.volgorde = volgorde;
	}

	public String getClassName()
	{
		return className;
	}

	public void setClassName(String className)
	{
		this.className = className;
	}

	public String getWaarde()
	{
		return waarde;
	}

	public void setWaarde(String waarde)
	{
		this.waarde = waarde;
	}

	public Bookmark getBookmark()
	{
		return bookmark;
	}

	public void setBookmark(Bookmark bookmark)
	{
		this.bookmark = bookmark;
	}

	public boolean isHaalUitContext()
	{
		return haalUitContext;
	}

	public void setHaalUitContext(boolean haalUitContext)
	{
		this.haalUitContext = haalUitContext;
	}

}
