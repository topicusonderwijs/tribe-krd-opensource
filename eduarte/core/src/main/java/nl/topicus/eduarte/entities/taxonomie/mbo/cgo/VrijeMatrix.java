package nl.topicus.eduarte.entities.taxonomie.mbo.cgo;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import nl.topicus.eduarte.entities.organisatie.IOrganisatieEntiteit;
import nl.topicus.eduarte.entities.personen.Deelnemer;
import nl.topicus.eduarte.providers.DeelnemerProvider;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Index;

@Entity
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
public class VrijeMatrix extends CompetentieMatrix implements IOrganisatieEntiteit,
		DeelnemerProvider
{
	private static final long serialVersionUID = 1L;

	@OneToMany(mappedBy = "matrix")
	private List<DeelnemerMatrix> deelnemerMatrices = new ArrayList<DeelnemerMatrix>();

	@ManyToOne(fetch = FetchType.LAZY)
	@Basic(optional = true)
	@JoinColumn(name = "deelnemer", nullable = true)
	@Index(name = "idx_vrijematrix_deelnemer")
	private Deelnemer deelnemer;

	@ManyToOne(fetch = FetchType.LAZY)
	@Basic(optional = true)
	@JoinColumn(name = "meeteenheid", nullable = true)
	@Index(name = "idx_vrijematrix_ME")
	private Meeteenheid meeteenheid;

	public Meeteenheid getMeeteenheid()
	{
		return meeteenheid;
	}

	public void setMeeteenheid(Meeteenheid meeteenheid)
	{
		this.meeteenheid = meeteenheid;
	}

	public VrijeMatrix()
	{
	}

	public List<DeelnemerMatrix> getDeelnemerMatrices()
	{
		return deelnemerMatrices;
	}

	public void setDeelnemerMatrices(List<DeelnemerMatrix> deelnemerMatrices)
	{
		this.deelnemerMatrices = deelnemerMatrices;
	}

	public Deelnemer getDeelnemer()
	{
		return deelnemer;
	}

	public void setDeelnemer(Deelnemer deelnemer)
	{
		this.deelnemer = deelnemer;
	}

	@Override
	public String toString()
	{
		if (getDeelnemer() != null)
		{
			return getNaam() + " (individueel)";
		}
		return getNaam();
	}

	@Override
	public String getType()
	{
		return "Vrije matrix";
	}

}
