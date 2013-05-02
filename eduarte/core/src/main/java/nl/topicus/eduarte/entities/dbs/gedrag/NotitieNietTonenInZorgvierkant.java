package nl.topicus.eduarte.entities.dbs.gedrag;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import nl.topicus.cobra.dao.helpers.IgnoreInGebruik;
import nl.topicus.eduarte.entities.dbs.NietTonenInZorgvierkant;
import nl.topicus.eduarte.entities.personen.Medewerker;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Index;

@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
public class NotitieNietTonenInZorgvierkant extends NietTonenInZorgvierkant
{
	private static final long serialVersionUID = 1L;

	@Basic(optional = false)
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "notitie", nullable = true)
	@Index(name = "idx_TonenInZorgvierkant_notitie")
	@IgnoreInGebruik
	private Notitie notitie;

	public NotitieNietTonenInZorgvierkant()
	{
	}

	public NotitieNietTonenInZorgvierkant(Notitie notitie, Medewerker medewerker)
	{
		super(medewerker);
		setNotitie(notitie);
	}

	public void setNotitie(Notitie notitie)
	{
		this.notitie = notitie;
	}

	public Notitie getNotitie()
	{
		return notitie;
	}
}
