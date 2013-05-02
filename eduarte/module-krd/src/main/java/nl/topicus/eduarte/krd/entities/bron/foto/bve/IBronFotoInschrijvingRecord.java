package nl.topicus.eduarte.krd.entities.bron.foto.bve;

import java.util.Date;

import nl.topicus.onderwijs.duo.bron.bve.waardelijsten.HoogsteVooropleiding;
import nl.topicus.onderwijs.duo.bron.bve.waardelijsten.Intensiteit;
import nl.topicus.onderwijs.duo.bron.bve.waardelijsten.Leerweg;

public interface IBronFotoInschrijvingRecord extends IBronFotoRecord
{
	public Long getId();

	public Long getPgn();

	public Integer getVolgnummerNumeriek();

	public String getInschrijvingsvolgnummer();

	public Date getDatumInschrijving();

	public Date getGeplandeUitschrijfdatum();

	public Date getWerkelijkeUitschrijfdatum();

	public String getCodeOpleiding();

	public boolean isIndicatieBekostigingInschrijvingOpTeldatumInRecord();

	public String getIndicatieBekostigingInschrijvingOpTeldatumInRecordOmschrijving();

	public boolean isIndicatieBekostigingInschrijvingOp1Februari();

	public String getIndicatieBekostigingInschrijvingOp1FebruariOmschrijving();

	public Boolean getIndicatieGehandicapt();

	public String getIndicatieGehandicaptOmschrijving();

	public HoogsteVooropleiding getHoogsteVooropleiding();

	public Leerweg getLeerweg();

	public Intensiteit getIntensiteit();

	public String getRedenUitstroom();

	public Class< ? extends IBronFotoOnderwijsontvangendeRecord> getOnderwijsontvangendeRecordClass();

}
