package nl.topicus.eduarte.entities.taxonomie.mbo.cgo;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;

import nl.topicus.eduarte.entities.organisatie.EntiteitContext;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Index;

/**
 * Uitstroom in het CGO.
 * 
 * @author loite
 */
@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Landelijk")
public class Uitstroom extends CompetentieMatrix
{
	private static final long serialVersionUID = 1L;

	@ManyToOne
	@Basic(optional = false)
	@JoinColumn(name = "dossier", nullable = true)
	@Index(name = "idx_uitstroom_dossier")
	private Kwalificatiedossier dossier;

	@OneToMany
	@JoinColumn(name = "uitstroom")
	private List<Taalvaardigheidseis> taalvaardigheidseisen = new ArrayList<Taalvaardigheidseis>();

	@Column(nullable = true)
	private Integer hoofdstuk;

	@Column(nullable = true)
	private int nummer;

	@Column(nullable = true)
	@Enumerated(EnumType.STRING)
	private UitstroomType uitstroomType;

	public Uitstroom()
	{
		super();
	}

	public Uitstroom(EntiteitContext context)
	{
		super(context);
	}

	public void setUitstroomType(UitstroomType type)
	{
		this.uitstroomType = type;
	}

	public UitstroomType getUitstroomType()
	{
		return uitstroomType;
	}

	public Kwalificatiedossier getDossier()
	{
		return dossier;
	}

	public void setDossier(Kwalificatiedossier dossier)
	{
		this.dossier = dossier;
	}

	public List<Taalvaardigheidseis> getTaalvaardigheidseisen()
	{
		return taalvaardigheidseisen;
	}

	public void addTaalvaardigheidseis(Taalvaardigheidseis eis)
	{
		taalvaardigheidseisen.add(eis);
	}

	public Integer getHoofdstuk()
	{
		return hoofdstuk;
	}

	public void setHoofdstuk(Integer hoofdstuk)
	{
		this.hoofdstuk = hoofdstuk;
	}

	public Integer getNummer()
	{
		return nummer;
	}

	public void setNummer(Integer nummer)
	{
		this.nummer = nummer;
	}

	@Override
	public String getType()
	{
		return uitstroomType.toString();
	}

	public void setTaalvaardigheidseisen(List<Taalvaardigheidseis> taalvaardigheidseisen)
	{
		this.taalvaardigheidseisen = taalvaardigheidseisen;
	}

	public void setNummer(int nummer)
	{
		this.nummer = nummer;
	}
}
