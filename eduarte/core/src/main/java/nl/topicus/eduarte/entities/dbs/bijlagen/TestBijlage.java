package nl.topicus.eduarte.entities.dbs.bijlagen;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import nl.topicus.eduarte.entities.bijlage.IBijlageKoppelEntiteit;
import nl.topicus.eduarte.entities.dbs.testen.DeelnemerTest;
import nl.topicus.eduarte.entities.personen.DeelnemerBijlage;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.ForeignKey;
import org.hibernate.annotations.Index;

@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
public class TestBijlage extends DeelnemerBijlage
{
	private static final long serialVersionUID = 1L;

	@Basic(optional = false)
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "test", nullable = true)
	@ForeignKey(name = "FK_TestBijlage_test")
	@Index(name = "idx_Bijlage_test")
	private DeelnemerTest test;

	public TestBijlage()
	{
	}

	public DeelnemerTest getTest()
	{
		return test;
	}

	public void setTest(DeelnemerTest test)
	{
		this.test = test;
	}

	@Override
	public IBijlageKoppelEntiteit<TestBijlage> getEntiteit()
	{
		return getTest();
	}
}
