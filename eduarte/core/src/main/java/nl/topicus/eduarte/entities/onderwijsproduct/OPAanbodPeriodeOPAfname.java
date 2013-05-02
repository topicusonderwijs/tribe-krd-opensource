package nl.topicus.eduarte.entities.onderwijsproduct;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import nl.topicus.eduarte.entities.inschrijving.OnderwijsproductAfname;
import nl.topicus.eduarte.entities.organisatie.InstellingEntiteit;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Index;

@Entity
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
public class OPAanbodPeriodeOPAfname extends InstellingEntiteit
{

	private static final long serialVersionUID = 1L;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false, name = "onderwijsproductaanbodperiode")
	@Index(name = "idx_ProdAanbodPer_per")
	private OnderwijsproductAanbodPeriode onderwijsProductAanbodPeriode;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false, name = "onderwijsproductafname")
	@Index(name = "idx_prodAfname_aanbod")
	private OnderwijsproductAfname onderwijsProductAfname;

	public OnderwijsproductAanbodPeriode getOnderwijsProductAanbodPeriode()
	{
		return onderwijsProductAanbodPeriode;
	}

	public void setOnderwijsProductAanbodPeriode(
			OnderwijsproductAanbodPeriode onderwijsProductAanbodPeriode)
	{
		this.onderwijsProductAanbodPeriode = onderwijsProductAanbodPeriode;
	}

	public OnderwijsproductAfname getOnderwijsProductAfname()
	{
		return onderwijsProductAfname;
	}

	public void setOnderwijsProductAfname(OnderwijsproductAfname onderwijsProductAfname)
	{
		this.onderwijsProductAfname = onderwijsProductAfname;
	}

}
