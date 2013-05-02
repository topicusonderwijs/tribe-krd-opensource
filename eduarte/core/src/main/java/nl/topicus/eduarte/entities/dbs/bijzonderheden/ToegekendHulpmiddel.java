package nl.topicus.eduarte.entities.dbs.bijzonderheden;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import nl.topicus.cobra.dao.helpers.IgnoreInGebruik;
import nl.topicus.eduarte.entities.organisatie.InstellingEntiteit;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.ForeignKey;
import org.hibernate.annotations.Index;

@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
public class ToegekendHulpmiddel extends InstellingEntiteit
{
	private static final long serialVersionUID = 1L;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "bijzonderheid", nullable = false)
	@Index(name = "idx_Bijzonder_ToegekHulpm")
	@ForeignKey(name = "fk_Bijzonder_ToegekHulpm")
	@IgnoreInGebruik
	private Bijzonderheid bijzonderheid;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "hulpmiddel", nullable = false)
	@Index(name = "idx_Hulpmiddel_ToegekHulpm")
	@ForeignKey(name = "fk_Hulpmiddel_ToegekHulpm")
	private Hulpmiddel hulpmiddel;

	public ToegekendHulpmiddel()
	{
	}

	public ToegekendHulpmiddel(Bijzonderheid bijzonderheid, Hulpmiddel hulpmiddel)
	{
		setBijzonderheid(bijzonderheid);
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

	public Bijzonderheid getBijzonderheid()
	{
		return bijzonderheid;
	}

	public void setBijzonderheid(Bijzonderheid bijzonderheid)
	{
		this.bijzonderheid = bijzonderheid;
	}
}
