/*
 * Copyright (c) 2007, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.entities.security.authentication;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.UniqueConstraint;

import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.dao.helpers.ExtendableDataViewDataAccessHelper;
import nl.topicus.cobra.entities.FieldPersistance;
import nl.topicus.cobra.entities.FieldPersistenceMode;
import nl.topicus.cobra.entities.IActiefEntiteit;
import nl.topicus.cobra.entities.dataview.IExtendableDataViewComponentSetting;
import nl.topicus.cobra.security.RechtenSoort;
import nl.topicus.cobra.web.components.form.AutoForm;
import nl.topicus.cobra.web.components.labels.JaNeeLabel;
import nl.topicus.eduarte.dao.helpers.AccountDataAccessHelper;
import nl.topicus.eduarte.dao.helpers.SettingsDataAccessHelper;
import nl.topicus.eduarte.entities.Entiteit;
import nl.topicus.eduarte.entities.organisatie.IOrganisatieEenheidLocatieKoppelbaarEntiteit;
import nl.topicus.eduarte.entities.organisatie.OrganisatieEntiteit;
import nl.topicus.eduarte.entities.personen.Deelnemer;
import nl.topicus.eduarte.entities.personen.Medewerker;
import nl.topicus.eduarte.entities.personen.Persoon;
import nl.topicus.eduarte.entities.security.authorization.AuthorisatieNiveau;
import nl.topicus.eduarte.entities.security.authorization.Rol;
import nl.topicus.eduarte.entities.settings.AccountSetting;
import nl.topicus.eduarte.entities.sidebar.IContextInfoObject;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Index;

/**
 * Basis classe voor accounts. Normaliter dient er altijd een subclass gebruikt te worden
 * die de account aan bv een medewerker of deelnemer koppeld. Alleen in uitzonderlijke
 * gevallen (het root account) kan deze class direct gebruikt worden.
 * 
 * @author loite
 */
@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
@javax.persistence.Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"gebruikersnaam",
	"organisatie"})})
public abstract class Account extends OrganisatieEntiteit implements IContextInfoObject,
		IActiefEntiteit
{
	private static final long serialVersionUID = 1L;

	@Column(nullable = false, length = 50)
	@Index(name = "idx_Account_gebruikersnaam")
	@AutoForm(htmlClasses = "unit_max")
	private String gebruikersnaam;

	@FieldPersistance(FieldPersistenceMode.SKIP)
	@Column(nullable = false, length = 50, name = "wachtwoord")
	private String encryptedWachtwoord;

	@Column(nullable = false)
	@AutoForm(displayClass = JaNeeLabel.class)
	private boolean actief = true;

	// @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.REFRESH}, fetch =
	// FetchType.LAZY)
	// @JoinTable(name = "Account_rol", joinColumns = {@JoinColumn(name = "account_id")},
	// inverseJoinColumns = {@JoinColumn(name = "roles_id")})
	// fetch type is eager causes duplicates values in the list because it also joins with
	// rechtenset

	@BatchSize(size = 20)
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "account")
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
	private List<AccountRol> roles = new ArrayList<AccountRol>();

	@Column(nullable = false)
	@Enumerated(value = EnumType.STRING)
	private AuthorisatieNiveau authorisatieNiveau;

	/**
	 * Een eventuele white list van ip-adressen waarvandaan dit account mag inloggen. Als
	 * de string leeg is, mag de gebruiker van overal vandaan inloggen.
	 */
	@Column(nullable = true, length = 200)
	private String ipAdressen;

	public Account()
	{
	}

	public abstract boolean isValid();

	public abstract Persoon getEigenaar();

	public abstract Medewerker getMedewerker();

	public abstract Deelnemer getDeelnemer();

	public abstract IOrganisatieEenheidLocatieKoppelbaarEntiteit< ? > getOrganisatieEenheidLocatie();

	public String getGebruikersnaam()
	{
		return gebruikersnaam;
	}

	public void setGebruikersnaam(String gebruikersnaam)
	{
		this.gebruikersnaam = gebruikersnaam;
	}

	public String getEncryptedWachtwoord()
	{
		return encryptedWachtwoord;
	}

	/**
	 * This now automaticly encrypts the password with help from the
	 * AccountDataAccessHelper.
	 * 
	 * @param wachtwoord
	 *            The wachtwoord to set.
	 */
	@AutoForm(htmlClasses = "unit_max")
	public void setWachtwoord(String wachtwoord)
	{
		if (wachtwoord == null)
		{
			this.encryptedWachtwoord = null;
		}
		else
		{
			this.encryptedWachtwoord = encrypt(wachtwoord);
		}
	}

	/**
	 * Set het wachtwoord zonder dit te encrypten. De aanroeper moet het dus al encrypt
	 * hebben.
	 * 
	 * @param encryptedWachtwoord
	 *            het encrypte wachtwoord
	 */
	public void setEncryptedWachtwoord(String encryptedWachtwoord)
	{
		this.encryptedWachtwoord = encryptedWachtwoord;
	}

	/**
	 * Controleert of de input overeenkomt met het wachtwoord. Input wordt auto-encrypt
	 * voor de vergelijking. Als de input null is wordt ook true gereturned als het
	 * wachtwoord ook null is.
	 * 
	 * @param verifyMe
	 *            de string waarvan men wil weten of die gelijk is aan het wachtwoord
	 * @return true als de input gelijk is aan het wacbhtwoord, anders false.
	 */
	public boolean verifyPassword(String verifyMe)
	{
		if (verifyMe == null)
			return getEncryptedWachtwoord() == null;
		return encrypt(verifyMe).equals(getEncryptedWachtwoord());
	}

	/**
	 * Utility methode om een wachtwoord te encrypten.
	 * 
	 * @param password
	 * @return het ge-encrypte wachtwoord
	 */
	public String encrypt(String password)
	{
		return (getHelper().encrypt(password));
	}

	private AccountDataAccessHelper getHelper()
	{
		return DataAccessRegistry.getHelper(AccountDataAccessHelper.class);
	}

	/**
	 * Generates a new random String usuable as password. You still need to set it on the
	 * account though.
	 * 
	 * @return a random string
	 */
	public static String generateWachtwoord()
	{
		int length = 16;
		int offset = 0;
		StringBuffer password = new StringBuffer(length);
		for (int i = 0; i < length; i++)
		{
			offset = (int) (Math.random() * 100);
			if (offset % 2 == 0)
				offset = 65;
			else
				offset = 97;
			password.append((char) ((int) (Math.random() * 26) + offset));
		}
		return password.toString();
	}

	public List<AccountRol> getRoles()
	{
		return roles;
	}

	public void setRoles(List<AccountRol> roles)
	{
		this.roles = roles;
	}

	public Set<Rol> getRollenAsRol()
	{
		Set<Rol> ret = new HashSet<Rol>();
		for (AccountRol curRol : getRoles())
			ret.add(curRol.getRol());
		return ret;
	}

	/**
	 * Flag of de account actief en dus geldig is.
	 * 
	 * @return true als de account actief is, anders false
	 */
	public boolean isActief()
	{
		return actief;
	}

	/**
	 * Enable of disable de account. Een niet actieve account kan niet inloggen.
	 * 
	 * @param actief
	 */
	public void setActief(boolean actief)
	{
		this.actief = actief;
	}

	/**
	 * Bepaald het authorisatie niveau opniew aan de hand van de rollen. De nieuwe waarde
	 * wordt niet automatisch gezet op deze instantie.
	 * 
	 * @return het authorisatie niveau dat deze user zou moeten hebben gebaseerd op de
	 *         rollen.
	 */
	public final AuthorisatieNiveau berekenAuthorisatieNiveau()
	{
		AuthorisatieNiveau max = AuthorisatieNiveau.REST;
		for (AccountRol role : getRoles())
		{
			if (role.getRol().getAuthorisatieNiveau().niveau() > max.niveau())
				max = role.getRol().getAuthorisatieNiveau();
		}
		return max;
	}

	@AutoForm(label = "Autorisatieniveau")
	public AuthorisatieNiveau getAuthorisatieNiveau()
	{
		return authorisatieNiveau;
	}

	public void setAuthorisatieNiveau(AuthorisatieNiveau authorisatieNiveau)
	{
		this.authorisatieNiveau = authorisatieNiveau;
	}

	/**
	 * De {@link RechtenSoort} van deze account.
	 */
	@AutoForm(label = "Type")
	public RechtenSoort getRechtenSoort()
	{
		return getOrganisatie().getRechtenSoort();
	}

	/**
	 * @return Een comma-separated string van de namen van de rollen van dit account
	 */
	public String getRolNamen()
	{
		StringBuilder res = new StringBuilder();
		int count = 0;
		for (AccountRol rol : getRoles())
		{
			if (count > 0)
			{
				res.append(", ");
			}
			res.append(rol.getRol());
			count++;
		}
		return res.toString();
	}

	/**
	 * @param rolnaam
	 * @return true als dit account een rol heeft met de gegeven naam
	 */
	public boolean heeftRol(String rolnaam)
	{
		for (AccountRol rol : getRoles())
		{
			if (rol.getRol().getNaam().equals(rolnaam))
			{
				return true;
			}
		}
		return false;
	}

	public Integer getMaxZorglijn()
	{
		Integer max = null;
		for (AccountRol rol : getRoles())
		{
			Integer zorglijn = rol.getRol().getZorglijn();
			if (max == null || (zorglijn != null && zorglijn > max))
				max = zorglijn;
		}
		return max;
	}

	@Override
	public void delete()
	{
		List<AccountSetting< ? >> settings =
			DataAccessRegistry.getHelper(SettingsDataAccessHelper.class).getSettings(this);
		for (AccountSetting< ? > setting : settings)
			setting.delete();
		List<IExtendableDataViewComponentSetting> settings2 =
			DataAccessRegistry.getHelper(ExtendableDataViewDataAccessHelper.class).getSettings(
				getId());
		for (IExtendableDataViewComponentSetting setting : settings2)
			((Entiteit) setting).delete();
		super.delete();
	}

	/**
	 * @return Ja indien actief, en anders Nee
	 */
	@AutoForm(label = "Actief")
	public String getActiefOmschrijving()
	{
		return isActief() ? "Ja" : "Nee";
	}

	@Override
	public String getContextInfoOmschrijving()
	{
		return getGebruikersnaam();
	}

	public String getIpAdressen()
	{
		return ipAdressen;
	}

	public void setIpAdressen(String ipAdressen)
	{
		this.ipAdressen = ipAdressen;
	}

}
