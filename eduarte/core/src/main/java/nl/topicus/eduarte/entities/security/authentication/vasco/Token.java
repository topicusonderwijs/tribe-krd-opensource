package nl.topicus.eduarte.entities.security.authentication.vasco;

import static nl.topicus.eduarte.entities.security.authentication.vasco.TokenStatus.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.entities.RestrictedAccess;
import nl.topicus.cobra.util.Asserts;
import nl.topicus.cobra.web.components.form.AutoForm;
import nl.topicus.eduarte.dao.helpers.vasco.VascoDataAccessHelper;
import nl.topicus.eduarte.entities.organisatie.InstellingEntiteit;
import nl.topicus.eduarte.entities.security.authentication.Account;
import nl.topicus.eduarte.web.components.quicksearch.account.AccountSearchEditor;
import nl.topicus.vasco.VascoResponseCode;
import nl.topicus.vasco.VascoValidationResponse;

import org.hibernate.annotations.Index;

/**
 * Record voor het vasthouden van de gegevens van een Vasco token. Een token kan aan één
 * gebruiker uitgegeven worden.
 */
@Entity
@Table(name = "VASCO_TOKENS")
public class Token extends InstellingEntiteit
{
	private static final long serialVersionUID = 1L;

	/** uniek identificerend nummer uitgegeven door Vasco dat een token identificeert. */
	@Column(nullable = false, length = 32)
	@RestrictedAccess(hasSetter = false)
	@AutoForm(readOnly = true, label = "Serienummer", description = "Het serienummer van het token (zie achterkant van het apparaat)")
	private String serienummer;

	/**
	 * applicatienaam zoals door Vasco aangeleverd (zal waarschijnlijk altijd GO3DEFAULT
	 * zijn).
	 */
	@Column(nullable = false, length = 32)
	@RestrictedAccess(hasSetter = false, hasGetter = false)
	private String applicatie;

	/** Initiele data voor de digipass, hiermee kan de pas gereset worden. */
	@Column(nullable = false, length = 248)
	@RestrictedAccess(hasSetter = false, hasGetter = false)
	private String initieleData;

	/** Werk data voor de digipass, hiermee wordt de pas gevalideerd. */
	@Column(nullable = false, length = 248)
	@RestrictedAccess(hasSetter = false, hasGetter = false)
	private String digipassData;

	/** de status van het token. */
	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	@RestrictedAccess(hasSetter = false)
	@AutoForm(readOnly = true, label = "Status", description = "De Status van dit token")
	private TokenStatus status;

	/** de gebruiker waaraan dit token is uitgegeven, mag leeg zijn. */
	@ManyToOne(targetEntity = Account.class)
	@JoinColumn(name = "gebruiker", nullable = true)
	@Index(name = "IDX_VASCOTOKEN_ACCOUNT")
	@RestrictedAccess(hasSetter = false)
	@AutoForm(editorClass = AccountSearchEditor.class, label = "Gebruiker", description = "De gebruiker die dit token bezit")
	private Account gebruiker;

	/**
	 * Constructor voor het creeeren van een token.
	 * 
	 * @param serienummer
	 *            het serienummer van het token, mag de applicatienaam bevatten
	 * @param applicatie
	 *            de applicatienaam van het token zoals door Vasco aangeleverd
	 * @param blob
	 *            de initiele blob voor het token
	 */
	public Token(String serienummer, String applicatie, String blob)
	{
		Asserts.assertNotEmpty("serienummer", serienummer);
		Asserts.assertNotEmpty("applicatie", applicatie);
		Asserts.assertNotEmpty("blob", blob);

		this.applicatie = applicatie;
		this.serienummer = serienummer;
		this.initieleData = blob;
		this.digipassData = blob;
		this.status = Beschikbaar;

		verwijderApplicatieVanSerienummer();
	}

	/**
	 * Serienummers worden aangedragen als: "0065045114GO3DEFAULT" hierin zit de
	 * applicatienaam verwerkt. Dit is natuurlijk niet wat we in de database willen
	 * hebben, dus wordt de applicatienaam er van afgestript.
	 */
	private void verwijderApplicatieVanSerienummer()
	{
		if (serienummer.endsWith(applicatie))
		{
			serienummer = serienummer.substring(0, serienummer.indexOf(applicatie));
		}
	}

	/**
	 * Voor hibernate.
	 */
	protected Token()
	{
	}

	/**
	 * Verifieert het gegenereerde wachtwoord, hierbij wordt het token aangepast (de
	 * digipass data bevat de state van het token in een geëncrypte vorm).
	 * 
	 * @param password
	 *            het gegenereerde wachtwoord door het token
	 * @return <code>true</code> als het wachtwoord goed gevalideerd is.
	 */
	public boolean verifieerPassword(String password)
	{
		VascoDataAccessHelper helper = DataAccessRegistry.getHelper(VascoDataAccessHelper.class);
		VascoValidationResponse response =
			helper.verifieer(getVascoSerienummer(), digipassData, password);
		digipassData = response.getData();

		// als de digipass aangeeft dat de token geblokkeerd is, dan ook hier blokkeren.
		if (response.getResponse() == VascoResponseCode.GEBLOKKEERD)
		{
			blokkeer();
		}
		saveOrUpdate();
		commit();
		return response.getResponse() == VascoResponseCode.SUCCES;
	}

	/**
	 * Geeft het token uit aan de gebruiker.
	 * <p>
	 * Precondities:
	 * <ul>
	 * <li>het token is uitgegeven aan een organisatie en beschikbaar binnen de
	 * organisatie.</li>
	 * <li>de gebruiker valt onder de organisatie waaraan het token is uitgegeven.</li>
	 * </ul>
	 * 
	 * @param gebruiker
	 *            de gebruiker waaraan het token uitgegeven wordt.
	 */
	@SuppressWarnings("hiding")
	public void geefUit(Account gebruiker)
	{
		Asserts.assertNotNull("gebruiker", gebruiker);

		// Controleren of er echt geen gebruiker gekoppeld is aan het tokenaccount
		if (this.getGebruiker() != null)
		{
			throw new IllegalStateException("Token " + serienummer + " is al uitgegeven aan "
				+ this.getGebruiker());
		}

		this.status = Uitgegeven;
		this.gebruiker = gebruiker;

		saveOrUpdate();
	}

	/**
	 * Neemt het token terug van de gebruiker (inverse van geefUit())
	 */
	public void neemIn()
	{
		// alleen de status terugzetten als de status uitgegeven was, Defect en
		// Geblokkeerd moeten bewaard blijven.
		if (this.status == Uitgegeven)
		{
			this.status = Beschikbaar;
		}
		this.gebruiker = null;

		saveOrUpdate();
	}

	/**
	 * Meldt het token defect.
	 */
	public void meldDefect()
	{
		status = Defect;
		saveOrUpdate();
	}

	/**
	 * Repareert een token waardoor deze weer gebruikt kan worden (de vraag is of dit
	 * daadwerkelijk ooit gebruikt zal worden).
	 */
	public void repareer()
	{
		assertStatus(Defect);

		// reset het token (vorige status is mogelijk niet meer geldig)
		this.digipassData = this.initieleData;

		// daarna deblokkeren
		status = gebruiker == null ? Beschikbaar : Uitgegeven;

		saveOrUpdate();
	}

	/**
	 * Blokkeert het token.
	 */
	public void blokkeer()
	{
		status = Geblokkeerd;
		saveOrUpdate();
	}

	/**
	 * Deblokkeert het token.
	 */
	public void deblokkeer()
	{
		assertStatus(Geblokkeerd);

		// reset het token
		this.digipassData = this.initieleData;

		// daarna deblokkeren
		status = gebruiker == null ? Beschikbaar : Uitgegeven;

		saveOrUpdate();
	}

	private void assertStatus(TokenStatus andereStatus)
	{
		if (this.status != andereStatus)
		{
			throw new IllegalStateException("Token met serienummer " + getSerienummer() + " niet "
				+ andereStatus + ", maar " + this.status);
		}
	}

	public TokenStatus getStatus()
	{
		return status;
	}

	public Account getGebruiker()
	{
		return gebruiker;
	}

	/**
	 * Geeft het Vasco serienummer terug, welke gelijk is aan het serienummer plus de
	 * applicatienaam.
	 * 
	 * @return het serienummer voor gebruik binnen Vasco
	 */
	public String getVascoSerienummer()
	{
		return serienummer + applicatie;
	}

	/**
	 * Geeft het identificerende serie nummer voor dit token.
	 */
	public String getSerienummer()
	{
		return serienummer;
	}
}
