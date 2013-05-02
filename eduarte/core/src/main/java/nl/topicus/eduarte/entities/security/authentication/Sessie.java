package nl.topicus.eduarte.entities.security.authentication;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import nl.topicus.cobra.util.TimeUtil;
import nl.topicus.eduarte.entities.organisatie.LandelijkOfInstellingEntiteit;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Index;

/**
 * Sessie van een gebruiker op EduArte.
 * 
 * @author loite
 * 
 */
@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
public class Sessie extends LandelijkOfInstellingEntiteit
{
	private static final long serialVersionUID = 1L;

	@Temporal(value = TemporalType.TIMESTAMP)
	@Column(nullable = false)
	private Date loginTime;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "account", nullable = false)
	@Index(name = "idx_Sessie_account")
	private Account account;

	/**
	 * Default constructor voor Hibernate.
	 */
	protected Sessie()
	{
	}

	/**
	 * Maakt een nieuw sessieobject voor het gegeven account met inlogtijd gelijk aan de
	 * huidige systeemtijd.
	 * 
	 * @param account
	 */
	public Sessie(Account account)
	{
		setAccount(account);
		setLoginTime(TimeUtil.getInstance().currentDateTime());
	}

	public Date getLoginTime()
	{
		return loginTime;
	}

	public void setLoginTime(Date loginTime)
	{
		this.loginTime = loginTime;
	}

	public Account getAccount()
	{
		return account;
	}

	public void setAccount(Account account)
	{
		this.account = account;
	}

}
