package nl.topicus.eduarte.krd.entities.bron.meldingen;

import java.util.List;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import nl.topicus.eduarte.krd.entities.bron.meldingen.aanlevering.BronExamenresultaatVOMelding;

@Entity
@DiscriminatorValue(value = "EX")
public class BronBatchVOExamengegevens extends AbstractBronBatchVO<BronExamenresultaatVOMelding>
{
	private static final long serialVersionUID = 1L;

	@SuppressWarnings("unchecked")
	@Override
	public List<BronExamenresultaatVOMelding> getMeldingen()
	{
		return (List<BronExamenresultaatVOMelding>) super.getMeldingen();
	}
}
