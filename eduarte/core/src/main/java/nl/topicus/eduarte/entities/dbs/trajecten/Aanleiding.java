package nl.topicus.eduarte.entities.dbs.trajecten;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.dao.helpers.IgnoreInGebruik;
import nl.topicus.cobra.entities.FieldPersistance;
import nl.topicus.cobra.entities.FieldPersistenceMode;
import nl.topicus.eduarte.dao.helpers.dbs.MogelijkeAanleidingDataAccessHelper;
import nl.topicus.eduarte.entities.dbs.ZorgvierkantObject;
import nl.topicus.eduarte.entities.dbs.bijzonderheden.Bijzonderheid;
import nl.topicus.eduarte.entities.dbs.gedrag.Incident;
import nl.topicus.eduarte.entities.dbs.gedrag.Notitie;
import nl.topicus.eduarte.entities.dbs.testen.DeelnemerTest;
import nl.topicus.eduarte.entities.organisatie.InstellingEntiteit;
import nl.topicus.eduarte.entities.personen.Deelnemer;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.ForeignKey;
import org.hibernate.annotations.Index;

@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
public class Aanleiding extends InstellingEntiteit implements ZorgvierkantObject
{
	private static final long serialVersionUID = 1L;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "incident", nullable = true)
	@ForeignKey(name = "FK_Aanleiding_incident")
	@Index(name = "idx_Aanlei_incident")
	private Incident incident;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "notitie", nullable = true)
	@ForeignKey(name = "FK_Aanleiding_notitie")
	@Index(name = "idx_Aanlei_notitie")
	private Notitie notitie;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "deelnemerTest", nullable = true)
	@ForeignKey(name = "FK_Aanleiding_test")
	@Index(name = "idx_Aanlei_test")
	private DeelnemerTest deelnemerTest;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "Bijzonderheid", nullable = true)
	@ForeignKey(name = "FK_Aanleiding_bijzheid")
	@Index(name = "idx_Aanlei_bijzheid")
	private Bijzonderheid bijzonderheid;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "begeleidingsHandeling", nullable = true)
	@ForeignKey(name = "FK_Aanleiding_begHand")
	@Index(name = "idx_Aanlei_BegHand")
	@FieldPersistance(FieldPersistenceMode.SAVE)
	private BegeleidingsHandeling begeleidingsHandeling;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "Traject", nullable = false)
	@ForeignKey(name = "FK_Aanleiding_traject")
	@Index(name = "idx_Aanlei_traject")
	@IgnoreInGebruik
	private Traject traject;

	public Aanleiding()
	{
	}

	public Aanleiding(Traject traject, MogelijkeAanleiding mogelijkeAanleiding)
	{
		setTraject(traject);
		setMogelijkeAanleiding(mogelijkeAanleiding);
	}

	public Aanleiding(Traject traject, Bijzonderheid bijzonderheid)
	{
		setTraject(traject);
		setBijzonderheid(bijzonderheid);
	}

	public Aanleiding(Traject traject, DeelnemerTest deelnemerTest)
	{
		setTraject(traject);
		setDeelnemerTest(deelnemerTest);
	}

	public MogelijkeAanleiding getMogelijkeAanleiding()
	{
		return DataAccessRegistry.getHelper(MogelijkeAanleidingDataAccessHelper.class).get(
			MogelijkeAanleiding.class, getMogelijkeAanleidingId());
	}

	public void setMogelijkeAanleiding(MogelijkeAanleiding mogelijkeAanleiding)
	{
		mogelijkeAanleiding.fillAanleiding(this);
	}

	public String getMogelijkeAanleidingId()
	{
		if (getIncident() != null)
			return "I_" + getIncident().getId();
		else if (getNotitie() != null)
			return "N_" + getNotitie().getId();
		else if (getDeelnemerTest() != null)
			return "T_" + getDeelnemerTest().getId();
		else if (getBijzonderheid() != null)
			return "B_" + getBijzonderheid().getId();
		else if (getBegeleidingsHandeling() != null)
			return "H_" + getBegeleidingsHandeling().getId();
		else
			throw new IllegalStateException();
	}

	public DeelnemerTest getDeelnemerTest()
	{
		return deelnemerTest;
	}

	public void setDeelnemerTest(DeelnemerTest deelnemerTest)
	{
		this.deelnemerTest = deelnemerTest;
	}

	public Bijzonderheid getBijzonderheid()
	{
		return bijzonderheid;
	}

	public void setBijzonderheid(Bijzonderheid bijzonderheid)
	{
		this.bijzonderheid = bijzonderheid;
	}

	public BegeleidingsHandeling getBegeleidingsHandeling()
	{
		return begeleidingsHandeling;
	}

	public void setBegeleidingsHandeling(BegeleidingsHandeling begeleidingsHandeling)
	{
		this.begeleidingsHandeling = begeleidingsHandeling;
	}

	public Traject getTraject()
	{
		return traject;
	}

	public void setTraject(Traject traject)
	{
		this.traject = traject;
	}

	public ZorgvierkantObject getObject()
	{
		if (getBegeleidingsHandeling() != null)
			return getBegeleidingsHandeling();
		else if (getBijzonderheid() != null)
			return getBijzonderheid();
		else if (getDeelnemerTest() != null)
			return getDeelnemerTest();
		else if (getIncident() != null)
			return getIncident();
		else if (getNotitie() != null)
			return getNotitie();
		throw new IllegalStateException();
	}

	@Override
	public String getSecurityId()
	{
		return getObject().getSecurityId();
	}

	@Override
	public String getVertrouwelijkSecurityId()
	{
		return getObject().getVertrouwelijkSecurityId();
	}

	@Override
	public Integer getZorglijn()
	{
		return getObject().getZorglijn();
	}

	@Override
	public boolean isTonenInZorgvierkant()
	{
		return getObject().isTonenInZorgvierkant();
	}

	@Override
	public boolean isVertrouwelijk()
	{
		return getObject().isVertrouwelijk();
	}

	@Override
	public Deelnemer getDeelnemer()
	{
		return getTraject().getDeelnemer();
	}

	public void setNotitie(Notitie notitie)
	{
		this.notitie = notitie;
	}

	public Notitie getNotitie()
	{
		return notitie;
	}

	public void setIncident(Incident incident)
	{
		this.incident = incident;
	}

	public Incident getIncident()
	{
		return incident;
	}

}
