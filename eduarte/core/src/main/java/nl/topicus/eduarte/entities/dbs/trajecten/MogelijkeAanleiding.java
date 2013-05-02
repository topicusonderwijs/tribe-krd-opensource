package nl.topicus.eduarte.entities.dbs.trajecten;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.*;

import nl.topicus.cobra.dao.BatchDataAccessHelper;
import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.entities.IdObject;
import nl.topicus.cobra.web.components.form.AutoForm;
import nl.topicus.eduarte.entities.Entiteit;
import nl.topicus.eduarte.entities.ViewEntiteit;
import nl.topicus.eduarte.entities.dbs.ZorgvierkantObject;
import nl.topicus.eduarte.entities.dbs.bijzonderheden.Bijzonderheid;
import nl.topicus.eduarte.entities.dbs.gedrag.Incident;
import nl.topicus.eduarte.entities.dbs.gedrag.Notitie;
import nl.topicus.eduarte.entities.dbs.testen.DeelnemerTest;
import nl.topicus.eduarte.entities.personen.Deelnemer;
import nl.topicus.eduarte.web.components.choice.ZorglijnCombobox;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Index;

@Entity
@Immutable
@Cache(usage = CacheConcurrencyStrategy.READ_ONLY, region = "Instelling")
@Table(name = "MogelijkeAanleiding")
public class MogelijkeAanleiding extends ViewEntiteit implements ZorgvierkantObject
{
	public enum MogelijkeAanleidingType
	{
		INCIDENT(Incident.class, "Incident", Incident.INCIDENT, Incident.VERTROUWELIJK_INCIDENT)
		{
			@Override
			public void fillAanleiding(Aanleiding aanleiding, IdObject entiteit)
			{
				aanleiding.setIncident((Incident) entiteit);
			}
		},
		NOTITIE(Notitie.class, "Notitie", Notitie.NOTITIES, Notitie.VERTROUWELIJKE_NOTITIES)
		{
			@Override
			public void fillAanleiding(Aanleiding aanleiding, IdObject entiteit)
			{
				aanleiding.setNotitie((Notitie) entiteit);
			}
		},
		DEELNEMER_TEST(DeelnemerTest.class, "Test", DeelnemerTest.DEELNEMERTEST,
				DeelnemerTest.VERTROUWELIJKE_DEELNEMERTEST)
		{
			@Override
			public void fillAanleiding(Aanleiding aanleiding, IdObject entiteit)
			{
				aanleiding.setDeelnemerTest((DeelnemerTest) entiteit);
			}
		},
		BIJZONDERHEID(Bijzonderheid.class, "Bijzonderheid", Bijzonderheid.BIJZONDERHEID,
				Bijzonderheid.VERTROUWELIJKE_BIJZONDERHEID)
		{
			@Override
			public void fillAanleiding(Aanleiding aanleiding, IdObject entiteit)
			{
				aanleiding.setBijzonderheid((Bijzonderheid) entiteit);
			}
		},
		GESPREK(Gesprek.class, "Gesprek", Gesprek.GESPREK, Traject.VERTROUWELIJK_TRAJECT)
		{
			@Override
			public void fillAanleiding(Aanleiding aanleiding, IdObject entiteit)
			{
				aanleiding.setBegeleidingsHandeling((BegeleidingsHandeling) entiteit);
			}
		},
		TEST_AFNAME(TestAfname.class, "Testafname", Traject.TRAJECT, Traject.VERTROUWELIJK_TRAJECT)
		{
			@Override
			public void fillAanleiding(Aanleiding aanleiding, IdObject entiteit)
			{
				aanleiding.setBegeleidingsHandeling((BegeleidingsHandeling) entiteit);
			}
		},
		TAAK(Taak.class, "Taak", Traject.TRAJECT, Traject.VERTROUWELIJK_TRAJECT)
		{
			@Override
			public void fillAanleiding(Aanleiding aanleiding, IdObject entiteit)
			{
				aanleiding.setBegeleidingsHandeling((BegeleidingsHandeling) entiteit);
			}
		};

		private String label;

		private String securityId;

		private String vertrouwelijkSecurityId;

		private Class< ? extends Entiteit> entiteitClass;

		MogelijkeAanleidingType(Class< ? extends Entiteit> entiteitClass, String label,
				String securityId, String vertrouwelijkSecurityId)
		{
			this.entiteitClass = entiteitClass;
			this.label = label;
			this.securityId = securityId;
			this.vertrouwelijkSecurityId = vertrouwelijkSecurityId;
		}

		abstract public void fillAanleiding(Aanleiding aanleiding, IdObject entiteit);

		public Class< ? extends Entiteit> getEntiteitClass()
		{
			return entiteitClass;
		}

		public String getSecurityId()
		{
			return securityId;
		}

		public String getVertrouwelijkSecurityId()
		{
			return vertrouwelijkSecurityId;
		}

		@Override
		public String toString()
		{
			return label;
		}

		public static List<MogelijkeAanleidingType> getTypes(String securityId)
		{
			List<MogelijkeAanleidingType> ret = new ArrayList<MogelijkeAanleidingType>();
			for (MogelijkeAanleidingType curType : values())
			{
				if (curType.getSecurityId().equals(securityId)
					|| curType.getVertrouwelijkSecurityId().equals(securityId))
					ret.add(curType);
			}
			return ret;
		}
	}

	private static final long serialVersionUID = 1L;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "deelnemer", nullable = false)
	@Index(name = "idx_mogAanleiding_deelnemer")
	private Deelnemer deelnemer;

	@Temporal(value = TemporalType.TIMESTAMP)
	@Column(nullable = false)
	private Date datum;

	@Column(nullable = true, length = 2)
	@AutoForm(editorClass = ZorglijnCombobox.class)
	private Integer zorglijn;

	@Column(nullable = false)
	private boolean vertrouwelijk;

	@Column(nullable = false, length = 255)
	private String omschrijving;

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private MogelijkeAanleidingType type;

	@Column(nullable = false)
	private long entiteitId;

	public Deelnemer getDeelnemer()
	{
		return deelnemer;
	}

	public void setDeelnemer(Deelnemer deelnemer)
	{
		this.deelnemer = deelnemer;
	}

	public Date getDatum()
	{
		return datum;
	}

	public void setDatum(Date datum)
	{
		this.datum = datum;
	}

	public Integer getZorglijn()
	{
		return zorglijn;
	}

	public void setZorglijn(Integer zorglijn)
	{
		this.zorglijn = zorglijn;
	}

	public boolean isVertrouwelijk()
	{
		return vertrouwelijk;
	}

	public void setVertrouwelijk(boolean vertrouwelijk)
	{
		this.vertrouwelijk = vertrouwelijk;
	}

	public String getOmschrijving()
	{
		return omschrijving;
	}

	public void setOmschrijving(String omschrijving)
	{
		this.omschrijving = omschrijving;
	}

	public MogelijkeAanleidingType getType()
	{
		return type;
	}

	public void setType(MogelijkeAanleidingType type)
	{
		this.type = type;
	}

	public long getEntiteitId()
	{
		return entiteitId;
	}

	public void setEntiteitId(long entiteitId)
	{
		this.entiteitId = entiteitId;
	}

	@Override
	public String getSecurityId()
	{
		return getType().getSecurityId();
	}

	@Override
	public String getVertrouwelijkSecurityId()
	{
		return getType().getVertrouwelijkSecurityId();
	}

	@SuppressWarnings("unchecked")
	public IdObject getEntiteit()
	{
		BatchDataAccessHelper<IdObject> helper =
			DataAccessRegistry.getHelper(BatchDataAccessHelper.class);
		return helper.get(getType().getEntiteitClass(), entiteitId);
	}

	@Override
	public String toString()
	{
		return getType() + " - " + omschrijving;
	}

	public void fillAanleiding(Aanleiding aanleiding)
	{
		getType().fillAanleiding(aanleiding, getEntiteit());
	}

	@Override
	public boolean isTonenInZorgvierkant()
	{
		return false;
	}
}
