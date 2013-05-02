package nl.topicus.eduarte.entities.bpv;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import nl.topicus.eduarte.entities.bpv.BPVCriteria.BPVCriteriaStatus;
import nl.topicus.eduarte.entities.organisatie.ExterneOrganisatie;
import nl.topicus.eduarte.entities.organisatie.InstellingEntiteit;

import org.hibernate.annotations.Index;

@Entity
public class BPVCriteriaExterneOrganisatie extends InstellingEntiteit
{

	private static final long serialVersionUID = 1L;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "externeOrganisatie", nullable = false)
	@Index(name = "idx_bpvCrExto_exto")
	private ExterneOrganisatie externeOrganisatie;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "bpvCriteria", nullable = false)
	@Index(name = "idx_bpvCrExto_crit")
	private BPVCriteria bpvCriteria;

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private BPVCriteriaStatus status;

	public BPVCriteriaExterneOrganisatie()
	{
	}

	public BPVCriteriaExterneOrganisatie(ExterneOrganisatie externeOrganisatie)
	{
		setExterneOrganisatie(externeOrganisatie);
	}

	public void setExterneOrganisatie(ExterneOrganisatie externeOrganisatie)
	{
		this.externeOrganisatie = externeOrganisatie;
	}

	public ExterneOrganisatie getExterneOrganisatie()
	{
		return externeOrganisatie;
	}

	public void setStatus(BPVCriteriaStatus status)
	{
		this.status = status;
	}

	public BPVCriteriaStatus getStatus()
	{
		return status;
	}

	public void setBpvCriteria(BPVCriteria bpvCriteria)
	{
		this.bpvCriteria = bpvCriteria;
	}

	public BPVCriteria getBpvCriteria()
	{
		return bpvCriteria;
	}
}