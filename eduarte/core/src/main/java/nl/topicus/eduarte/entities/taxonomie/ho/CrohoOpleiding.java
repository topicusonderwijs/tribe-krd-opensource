package nl.topicus.eduarte.entities.taxonomie.ho;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;

import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.templates.annotations.Exportable;
import nl.topicus.cobra.util.Asserts;
import nl.topicus.eduarte.dao.helpers.CrohoOpleidingAanbodDataAccessHelper;
import nl.topicus.eduarte.dao.helpers.CrohoOpleidingDataAccessHelper;
import nl.topicus.eduarte.entities.hogeronderwijs.OpleidingsVorm;
import nl.topicus.eduarte.entities.organisatie.Brin;
import nl.topicus.eduarte.entities.taxonomie.Verbintenisgebied;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity()
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Landelijk")
@Exportable
public class CrohoOpleiding extends Verbintenisgebied
{
	private static final long serialVersionUID = 1L;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "crohoOpleiding")
	@OrderBy(value = "brin")
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "Instelling")
	private List<CrohoOpleidingAanbod> aanbod = new ArrayList<CrohoOpleidingAanbod>();

	public void setAanbod(List<CrohoOpleidingAanbod> aanbod)
	{
		this.aanbod = aanbod;
	}

	public List<CrohoOpleidingAanbod> getAanbod()
	{
		return aanbod;
	}

	public List<CrohoOpleidingAanbod> getAanbod(Brin brin)
	{
		Asserts.assertNotNull("brin", brin);

		return DataAccessRegistry.getHelper(CrohoOpleidingAanbodDataAccessHelper.class).getAanbod(
			this, brin);
	}

	public EnumSet<OpleidingsVorm> getOpleidingsVormen(Brin brin)
	{
		Asserts.assertNotNull("brin", brin);

		List<CrohoOpleidingAanbod> coas = getAanbod(brin);
		EnumSet<OpleidingsVorm> result = EnumSet.noneOf(OpleidingsVorm.class);
		for (CrohoOpleidingAanbod coa : coas)
			result.add(coa.getOpleidingsvorm());

		return result;
	}

	public CrohoOpleidingAanbod getAanbod(Brin brin, OpleidingsVorm opleidingsvorm)
	{
		Asserts.assertNotNull("brin", brin);
		Asserts.assertNotNull("opleidingsvorm", opleidingsvorm);

		for (CrohoOpleidingAanbod coa : aanbod)
		{
			if (brin.equals(coa.getBrin()) && opleidingsvorm.equals(coa.getOpleidingsvorm()))
				return coa;
		}

		return null;
	}

	public static CrohoOpleiding get(String isatCode)
	{
		Asserts.assertNotNull("isatCode", isatCode);

		return DataAccessRegistry.getHelper(CrohoOpleidingDataAccessHelper.class)
			.getCrohoOpleiding(isatCode);
	}

}
