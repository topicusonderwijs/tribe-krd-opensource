package nl.topicus.eduarte.entities.dbs.trajecten.templates;

import javax.persistence.Basic;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import nl.topicus.eduarte.entities.opleiding.Opleiding;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.ForeignKey;
import org.hibernate.annotations.Index;

@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
@DiscriminatorValue("TTKoppelingOpleiding")
public class TrajectTemplateKoppelingOpleiding extends TrajectTemplateKoppeling
{
	private static final long serialVersionUID = 1L;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "opleiding", nullable = true)
	@Basic(optional = false)
	@ForeignKey(name = "FK_KoppOpl_Opl")
	@Index(name = "idx_KoppOpl_Opl")
	private Opleiding opleiding;

	public TrajectTemplateKoppelingOpleiding()
	{
	}

	public Opleiding getOpleiding()
	{
		return opleiding;
	}

	public void setOpleiding(Opleiding opleiding)
	{
		this.opleiding = opleiding;
	}

}
