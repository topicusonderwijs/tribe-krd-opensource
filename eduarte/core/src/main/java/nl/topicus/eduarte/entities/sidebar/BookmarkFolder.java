package nl.topicus.eduarte.entities.sidebar;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import nl.topicus.cobra.util.StringUtil;
import nl.topicus.eduarte.entities.organisatie.OrganisatieEntiteit;
import nl.topicus.eduarte.entities.security.authentication.Account;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Index;

@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
public class BookmarkFolder extends OrganisatieEntiteit
{
	private static final long serialVersionUID = 1L;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "account", nullable = false)
	@Index(name = "idx_bookmarkFolder_account")
	private Account account;

	@Column(length = 60, nullable = false)
	private String naam;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "bookmarkFolder")
	private List<Bookmark> bookmarks;

	@Column(nullable = false)
	private int volgorde = -1;

	public BookmarkFolder()
	{
	}

	public Account getAccount()
	{
		return account;
	}

	public void setAccount(Account account)
	{
		this.account = account;
	}

	public void setNaam(String naam)
	{
		this.naam = naam;
	}

	public String getNaam()
	{
		return naam;
	}

	public void setBookmarks(List<Bookmark> bookmarks)
	{
		this.bookmarks = bookmarks;
	}

	public List<Bookmark> getBookmarks()
	{
		return bookmarks;
	}

	@Override
	public String toString()
	{
		return StringUtil.firstCharUppercase(naam);
	}

	public void setVolgorde(int volgorde)
	{
		this.volgorde = volgorde;
	}

	public int getVolgorde()
	{
		return volgorde;
	}
}
