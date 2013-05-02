package nl.topicus.eduarte.entities.taxonomie.mbo.cgo;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import nl.topicus.eduarte.entities.organisatie.InstellingEntiteit;
import nl.topicus.eduarte.entities.personen.Deelnemer;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Index;
import org.hibernate.annotations.Table;

@Entity
@Table(appliesTo = "DeelnemerMatrix")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
public class DeelnemerMatrix extends InstellingEntiteit
{
	private static final long serialVersionUID = 1L;

	@ManyToOne(optional = false, fetch = FetchType.EAGER)
	@JoinColumn(name = "deelnemer", nullable = false)
	@Index(name = "idx_matrix_deelnemer")
	private Deelnemer deelnemer;

	@ManyToOne(optional = false, fetch = FetchType.EAGER)
	@JoinColumn(name = "matrix", nullable = false)
	@Index(name = "idx_deelnemer_matrix")
	private VrijeMatrix matrix;

	@ManyToOne
	@Basic(optional = true)
	@JoinColumn(name = "meeteenheid", nullable = true)
	@Index(name = "idx_deelnemermatrix_ME")
	private Meeteenheid meeteenheid;

	public DeelnemerMatrix()
	{
	}

	public Meeteenheid getMeeteenheid()
	{
		return meeteenheid;
	}

	public void setMeeteenheid(Meeteenheid meeteenheid)
	{
		this.meeteenheid = meeteenheid;
	}

	public Deelnemer getDeelnemer()
	{
		return deelnemer;
	}

	public String getNaam()
	{
		return matrix.getNaam();
	}

	public String getType()
	{
		return matrix.getType();
	}

	public void setDeelnemer(Deelnemer deelnemer)
	{
		this.deelnemer = deelnemer;
	}

	public VrijeMatrix getMatrix()
	{
		return matrix;
	}

	public void setMatrix(VrijeMatrix matrix)
	{
		this.matrix = matrix;
	}

}
