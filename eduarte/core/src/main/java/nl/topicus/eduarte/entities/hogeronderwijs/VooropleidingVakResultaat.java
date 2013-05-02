package nl.topicus.eduarte.entities.hogeronderwijs;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import nl.topicus.cobra.web.components.form.AutoFormEmbedded;
import nl.topicus.eduarte.entities.inschrijving.Vooropleiding;
import nl.topicus.eduarte.entities.inschrijving.VooropleidingVak;
import nl.topicus.eduarte.entities.organisatie.InstellingEntiteit;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Index;

@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
public class VooropleidingVakResultaat extends InstellingEntiteit
{
	private static final long serialVersionUID = 1L;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "vooropleiding", nullable = false)
	@Index(name = "idx_VooroplVakRes_Vooropldng")
	private Vooropleiding vooropleiding;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "vak", nullable = false)
	@Index(name = "idx_VooroplVakRes_VooroplVak")
	@AutoFormEmbedded
	private VooropleidingVak vak;

	@Column(nullable = true)
	private Integer score;

	@Column(nullable = true)
	private Character letter;

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private VooropleidingVakVerificatieStatus status;

	public VooropleidingVakResultaat()
	{
	}

	public VooropleidingVakResultaat(Vooropleiding vooropleiding)
	{
		setVooropleiding(vooropleiding);
	}

	public Vooropleiding getVooropleiding()
	{
		return vooropleiding;
	}

	public void setVooropleiding(Vooropleiding vooropleiding)
	{
		this.vooropleiding = vooropleiding;
	}

	public VooropleidingVak getVak()
	{
		return vak;
	}

	public void setVak(VooropleidingVak vak)
	{
		this.vak = vak;
	}

	public Integer getScore()
	{
		return score;
	}

	public void setScore(Integer score)
	{
		this.score = score;
	}

	public Character getLetter()
	{
		return letter;
	}

	public void setLetter(Character letter)
	{
		this.letter = letter;
	}

	public VooropleidingVakVerificatieStatus getStatus()
	{
		return status;
	}

	public void setStatus(VooropleidingVakVerificatieStatus status)
	{
		this.status = status;
	}
}
