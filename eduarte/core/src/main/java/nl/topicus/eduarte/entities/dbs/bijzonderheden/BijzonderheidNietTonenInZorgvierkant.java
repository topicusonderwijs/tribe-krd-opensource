package nl.topicus.eduarte.entities.dbs.bijzonderheden;

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
public class BijzonderheidNietTonenInZorgvierkant extends NietTonenInZorgvierkant
{
	private static final long serialVersionUID = 1L;

	@Basic(optional = false)
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "bijzonderheid", nullable = true)
	@Index(name = "idx_TonenInZorgv_bijzonderhei")
	@IgnoreInGebruik
	private Bijzonderheid bijzonderheid;

	public BijzonderheidNietTonenInZorgvierkant()
	{
	}

	public BijzonderheidNietTonenInZorgvierkant(Bijzonderheid bijzonderheid, Medewerker medewerker)
	{
		super(medewerker);
		setBijzonderheid(bijzonderheid);
	}

	public Bijzonderheid getBijzonderheid()
	{
		return bijzonderheid;
	}

	public void setBijzonderheid(Bijzonderheid bijzonderheid)
	{
		this.bijzonderheid = bijzonderheid;
	}
}
