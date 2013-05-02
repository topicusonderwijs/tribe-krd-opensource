package nl.topicus.eduarte.entities.dbs.trajecten.templates;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.dao.helpers.IgnoreInGebruik;
import nl.topicus.eduarte.dao.helpers.dbs.BijzonderheidDataAccessHelper;
import nl.topicus.eduarte.dao.helpers.dbs.DeelnemerTestDataAccessHelper;
import nl.topicus.eduarte.entities.dbs.bijzonderheden.Bijzonderheid;
import nl.topicus.eduarte.entities.dbs.bijzonderheden.BijzonderheidCategorie;
import nl.topicus.eduarte.entities.dbs.testen.DeelnemerTest;
import nl.topicus.eduarte.entities.dbs.testen.TestDefinitie;
import nl.topicus.eduarte.entities.dbs.trajecten.Aanleiding;
import nl.topicus.eduarte.entities.dbs.trajecten.Traject;
import nl.topicus.eduarte.entities.organisatie.InstellingEntiteit;
import nl.topicus.eduarte.zoekfilters.dbs.BijzonderheidZoekFilter;
import nl.topicus.eduarte.zoekfilters.dbs.DeelnemerTestZoekFilter;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.ForeignKey;
import org.hibernate.annotations.Index;

/**
 * @author maatman
 */
@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
public class AanleidingTemplate extends InstellingEntiteit
{
	private static final long serialVersionUID = 1L;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "trajectTemplate", nullable = false)
	@ForeignKey(name = "FK_AanlTempl_trajectTemplate")
	@Index(name = "idx_AanlTempl_trajectTemplate")
	@IgnoreInGebruik
	private TrajectTemplate trajectTemplate;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "testDefinitie", nullable = true)
	@ForeignKey(name = "FK_AanlTempl_testDefinitie")
	@Index(name = "idx_AanlTempl_testDefinitie")
	private TestDefinitie testDefinitie;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "bijzonderheidCategorie", nullable = true)
	@ForeignKey(name = "FK_AanlTempl_categorie")
	@Index(name = "idx_AanlTempl_categorie")
	private BijzonderheidCategorie bijzonderheidCategorie;

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private AanleidingType type;

	public AanleidingTemplate()
	{
	}

	public TrajectTemplate getTrajectTemplate()
	{
		return trajectTemplate;
	}

	public void setTrajectTemplate(TrajectTemplate trajectTemplate)
	{
		this.trajectTemplate = trajectTemplate;
	}

	public TestDefinitie getTestDefinitie()
	{
		return testDefinitie;
	}

	public void setTestDefinitie(TestDefinitie testDefinitie)
	{
		this.testDefinitie = testDefinitie;
	}

	public BijzonderheidCategorie getBijzonderheidCategorie()
	{
		return bijzonderheidCategorie;
	}

	public void setBijzonderheidCategorie(BijzonderheidCategorie bijzonderheidCategorie)
	{
		this.bijzonderheidCategorie = bijzonderheidCategorie;
	}

	public AanleidingType getType()
	{
		return type;
	}

	public void setType(AanleidingType type)
	{
		this.type = type;
	}

	public List<Aanleiding> createAanleidingen(Traject traject)
	{
		List<Aanleiding> ret = new ArrayList<Aanleiding>();
		if (getType().equals(AanleidingType.Bijzonderheid))
		{
			BijzonderheidZoekFilter filter = new BijzonderheidZoekFilter();
			filter.setDeelnemer(traject.getDeelnemer());
			filter.setCategorie(getBijzonderheidCategorie());
			List<Bijzonderheid> bijzonderheden =
				DataAccessRegistry.getHelper(BijzonderheidDataAccessHelper.class).list(filter);
			for (Bijzonderheid curBijzonderheid : bijzonderheden)
			{
				ret.add(new Aanleiding(traject, curBijzonderheid));
			}
		}
		else
		{
			DeelnemerTestZoekFilter filter = new DeelnemerTestZoekFilter();
			filter.setDeelnemer(traject.getDeelnemer());
			filter.setTestDefinitie(getTestDefinitie());
			List<DeelnemerTest> testen =
				DataAccessRegistry.getHelper(DeelnemerTestDataAccessHelper.class).list(filter);
			for (DeelnemerTest curDeelnemerTest : testen)
			{
				ret.add(new Aanleiding(traject, curDeelnemerTest));
			}
		}
		return ret;
	}
}
