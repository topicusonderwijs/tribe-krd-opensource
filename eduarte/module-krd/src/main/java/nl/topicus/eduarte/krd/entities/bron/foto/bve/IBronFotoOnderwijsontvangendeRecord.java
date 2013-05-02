package nl.topicus.eduarte.krd.entities.bron.foto.bve;

import java.util.Date;

import nl.topicus.cobra.types.personalia.Geslacht;

public interface IBronFotoOnderwijsontvangendeRecord extends IBronFotoRecord
{

	public Geslacht getGeslacht();

	public Date getGeboortedatum();

	public String getGeboorteJaarEnMaand();

	public String getPostcodecijfers();

	public Date getOverlijdensdatum();

	public Date getDatumVestigingInNederland();

	public Date getDatumVertrekUitNederland();

	public String getCodeGeboorteland();

	public String getCodeGeboortelandOuder1();

	public Geslacht getGeslachtOuder1();

	public String getCodeGeboortelandOuder2();

	public Geslacht getGeslachtOuder2();

	public String getCodeLandWaarnaarVertrokken();

	public String getCodeVerblijfstitel();

	public String getCodeNationaliteit1();

	public String getCodeNationaliteit2();

	public Integer getLeeftijdOpMeetdatum1();

	public Date getLeeftijdmeetdatum1();

	public Integer getLeeftijdOpMeetdatum2();

	public Date getLeeftijdmeetdatum2();

	public Integer getLeeftijdOpMeetdatum3();

	public Date getLeeftijdmeetdatum3();

	public Integer getLeeftijdOpMeetdatum4();

	public Date getLeeftijdmeetdatum4();

	public Integer getLeeftijdOpMeetdatum5();

	public Date getLeeftijdmeetdatum5();

}