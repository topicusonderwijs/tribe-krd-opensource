package nl.topicus.eduarte.krd.entities.bron.meldingen;

import java.util.List;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import nl.topicus.eduarte.krd.entities.bron.meldingen.aanlevering.BronInschrijvingsgegevensVOMelding;

@Entity
@DiscriminatorValue(value = "IS")
public class BronBatchVOInschrijvingen extends
		AbstractBronBatchVO<BronInschrijvingsgegevensVOMelding>
{
	private static final long serialVersionUID = 1L;

	@SuppressWarnings("unchecked")
	@Override
	public List<BronInschrijvingsgegevensVOMelding> getMeldingen()
	{
		return (List<BronInschrijvingsgegevensVOMelding>) super.getMeldingen();
	}
}
