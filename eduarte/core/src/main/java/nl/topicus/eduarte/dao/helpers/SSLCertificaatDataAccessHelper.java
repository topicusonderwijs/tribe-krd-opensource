package nl.topicus.eduarte.dao.helpers;

import nl.topicus.cobra.dao.BatchDataAccessHelper;
import nl.topicus.eduarte.entities.ibgverzuimloket.SSLCertificaat;

public interface SSLCertificaatDataAccessHelper extends BatchDataAccessHelper<SSLCertificaat>
{
	SSLCertificaat findCertificaatOfInstelling();
}
