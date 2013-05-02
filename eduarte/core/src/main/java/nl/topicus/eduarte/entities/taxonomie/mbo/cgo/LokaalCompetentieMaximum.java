package nl.topicus.eduarte.entities.taxonomie.mbo.cgo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

/**
 * Een {@link LokaalCompetentieMaximum} definieert een verzameling met competentieniveaus,
 * die als maximum zullen gelden voor alle beoordelingen bij de opleiding waar het bij
 * hoort. Bij het invullen van een competentiematrix kan dus voor elke cel geen hogere
 * waarde gekozen worden dan het corresponderende maximum uit deze verzameling.
 * 
 * @author vanharen
 */
@Entity
public class LokaalCompetentieMaximum extends CompetentieNiveauVerzameling
{

	private static final long serialVersionUID = 1L;

	@Column(nullable = true)
	@Enumerated(EnumType.STRING)
	private MeeteenheidKoppelType type;

	public LokaalCompetentieMaximum()
	{
	}

	public MeeteenheidKoppelType getType()
	{
		return type;
	}

	public void setType(MeeteenheidKoppelType type)
	{
		this.type = type;
	}

	@Override
	public String getTypeNaam()
	{
		return "Lokaal maximum";
	}
}
