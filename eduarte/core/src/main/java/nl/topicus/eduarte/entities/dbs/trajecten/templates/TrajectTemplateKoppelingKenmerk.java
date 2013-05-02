package nl.topicus.eduarte.entities.dbs.trajecten.templates;

import javax.persistence.Basic;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import nl.topicus.eduarte.entities.kenmerk.Kenmerk;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.ForeignKey;
import org.hibernate.annotations.Index;

@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
@DiscriminatorValue("TTKoppelingKenmerk")
public class TrajectTemplateKoppelingKenmerk extends TrajectTemplateKoppeling
{
	private static final long serialVersionUID = 1L;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "kenmerk", nullable = true)
	@Basic(optional = false)
	@ForeignKey(name = "FK_KoppKenCat_Kenm")
	@Index(name = "idx_KoppKenCa_Kenm")
	private Kenmerk kenmerk;

	public TrajectTemplateKoppelingKenmerk()
	{
	}

	public Kenmerk getKenmerk()
	{
		return kenmerk;
	}

	public void setKenmerk(Kenmerk kenmerk)
	{
		this.kenmerk = kenmerk;
	}
}
