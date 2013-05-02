package nl.topicus.eduarte.entities.dbs.trajecten;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import nl.topicus.cobra.templates.annotations.Exportable;
import nl.topicus.eduarte.app.security.checks.ZorgvierkantObjectSecurityCheck;
import nl.topicus.eduarte.entities.dbs.testen.DeelnemerTest;
import nl.topicus.eduarte.entities.dbs.testen.TestDefinitie;
import nl.topicus.eduarte.entities.personen.Medewerker;

import org.apache.wicket.security.actions.Render;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.ForeignKey;
import org.hibernate.annotations.Index;

/**
 * @author maatman
 */
@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
public class TestAfname extends GeplandeBegeleidingsHandeling
{
	private static final long serialVersionUID = 1L;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "TestDefinitie", nullable = true)
	@Basic(optional = false)
	@ForeignKey(name = "FK_TestAfname_definitie")
	@Index(name = "idx_TestAfname_definitie")
	private TestDefinitie testDefinitie;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "deelnemerTest", nullable = true)
	@ForeignKey(name = "FK_TestAfname_deelnemerTest")
	@Index(name = "idx_TestAfname_deelnemerTest")
	private DeelnemerTest deelnemerTest;

	public TestAfname()
	{
		soort = "Testafname";
	}

	public TestAfname(Medewerker auteur, Traject traject)
	{
		this();
		setEigenaar(auteur);
		setTraject(traject);
	}

	public TestDefinitie getTestDefinitie()
	{
		return testDefinitie;
	}

	public void setTestDefinitie(TestDefinitie testDefinitie)
	{
		this.testDefinitie = testDefinitie;
	}

	public DeelnemerTest getDeelnemerTest()
	{
		return deelnemerTest;
	}

	public void setDeelnemerTest(DeelnemerTest deelnemerTest)
	{
		this.deelnemerTest = deelnemerTest;
	}

	public boolean isTestVeldenVisible()
	{
		if (getStatus() != null)
		{
			if (getStatus().equals(BegeleidingsHandelingsStatussoort.Bespreken))
				return true;
			if (getStatus().equals(BegeleidingsHandelingsStatussoort.Voltooid))
				return true;
		}
		return false;
	}

	@Override
	public String handelingsSoort()
	{
		if (getTestDefinitie() != null)
			return getTestDefinitie().getNaam();
		return "";
	}

	/**************************************************************************************************************************
	 *** Sectie met getters voor samenvoeg velden
	 ************************************************************************************************************************** 
	 */

	private boolean doVertrouwlijkeSercurityCheck()
	{
		return ZorgvierkantObjectSecurityCheck.isAllowed(Render.class, this);
	}

	@Exportable
	public String getExportableSoortHandeling()
	{
		if (doVertrouwlijkeSercurityCheck())
			return getSoortHandeling();
		return null;
	}

	@Exportable
	public TestDefinitie getExportableTestDefinitie()
	{
		if (doVertrouwlijkeSercurityCheck())
			return getTestDefinitie();
		return null;
	}

	@Exportable
	public DeelnemerTest getExportableDeelnemerTest()
	{
		if (doVertrouwlijkeSercurityCheck())
			return getDeelnemerTest();
		return null;
	}
}
