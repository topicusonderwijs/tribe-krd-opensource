package nl.topicus.eduarte.entities.organisatie;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import nl.topicus.cobra.util.StringUtil;

import org.springframework.util.Assert;

@Embeddable
public class BrinVolgnummer implements Serializable
{
	private static final long serialVersionUID = 1L;

	@Column(nullable = true, length = 2)
	private String brincodeToevoeging;

	public BrinVolgnummer()
	{

	}

	public BrinVolgnummer(String brincodeToevoeging)
	{
		setBrincodeToevoeging(brincodeToevoeging);
	}

	public String getBrincodeToevoeging()
	{
		return brincodeToevoeging;
	}

	public void setBrincodeToevoeging(String brincodeToevoeging)
	{
		Assert.isTrue(testBrincodeToevoeging(brincodeToevoeging));
		this.brincodeToevoeging = brincodeToevoeging;
	}

	/**
	 * Test of de meegegeven brincode voldoet aan 2 cijfers en 2 letters.
	 * 
	 * @param brincodeToevoeging
	 * @return true als de meegegeven brincode voldoet aan de test, en anders false
	 */
	public static boolean testBrincodeToevoeging(String brincodeToevoeging)
	{
		return StringUtil.checkMatchesRegExp("brincodeToevoeging", brincodeToevoeging, "[0-9]{2}?");
	}

	@Override
	public String toString()
	{
		return getBrincodeToevoeging();
	}
}
