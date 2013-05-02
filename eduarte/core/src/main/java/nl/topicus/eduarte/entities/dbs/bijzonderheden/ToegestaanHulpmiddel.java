package nl.topicus.eduarte.entities.dbs.bijzonderheden;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import nl.topicus.eduarte.entities.organisatie.LandelijkOfInstellingEntiteit;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.ForeignKey;
import org.hibernate.annotations.Index;

@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
public class ToegestaanHulpmiddel extends LandelijkOfInstellingEntiteit
{
	private static final long serialVersionUID = 1L;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "bijzonderheidCategorie", nullable = false)
	@Index(name = "idx_CategBijz_ToegestHulpm")
	@ForeignKey(name = "fk_CategBijz_ToegestHulpm")
	private BijzonderheidCategorie bijzonderheidCategorie;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "hulpmiddel", nullable = false)
	@Index(name = "idx_Hulpmiddel_ToegestHulpm")
	@ForeignKey(name = "fk_Hulpmiddel_ToegestHulpm")
	private Hulpmiddel hulpmiddel;

	public ToegestaanHulpmiddel()
	{
	}

	public ToegestaanHulpmiddel(BijzonderheidCategorie bijzonderheidCategorie, Hulpmiddel hulpmiddel)
	{
		setBijzonderheidCategorie(bijzonderheidCategorie);
		setHulpmiddel(hulpmiddel);
	}

	public Hulpmiddel getHulpmiddel()
	{
		return hulpmiddel;
	}

	public void setHulpmiddel(Hulpmiddel hulpmiddel)
	{
		this.hulpmiddel = hulpmiddel;
	}

	public BijzonderheidCategorie getBijzonderheidCategorie()
	{
		return bijzonderheidCategorie;
	}

	public void setBijzonderheidCategorie(BijzonderheidCategorie bijzonderheidCategorie)
	{
		this.bijzonderheidCategorie = bijzonderheidCategorie;
	}
}
