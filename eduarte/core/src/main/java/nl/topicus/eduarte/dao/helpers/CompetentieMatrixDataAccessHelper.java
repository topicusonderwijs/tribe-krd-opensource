package nl.topicus.eduarte.dao.helpers;

import java.util.Date;
import java.util.List;

import nl.topicus.cobra.dao.helpers.BatchZoekFilterDataAccessHelper;
import nl.topicus.eduarte.entities.personen.Deelnemer;
import nl.topicus.eduarte.entities.taxonomie.MBONiveau;
import nl.topicus.eduarte.entities.taxonomie.mbo.cgo.CompetentieMatrix;
import nl.topicus.eduarte.entities.taxonomie.mbo.cgo.DeelnemerMatrix;
import nl.topicus.eduarte.entities.taxonomie.mbo.cgo.LLBMatrix;
import nl.topicus.eduarte.entities.taxonomie.mbo.cgo.VrijeMatrix;
import nl.topicus.eduarte.zoekfilters.CompetentieMatrixZoekFilter;

/**
 * Data access methodes voor competentie matrices.
 */
public interface CompetentieMatrixDataAccessHelper extends
		BatchZoekFilterDataAccessHelper<CompetentieMatrix, CompetentieMatrixZoekFilter>
{

	public LLBMatrix getLLB(Date peildatum, MBONiveau niveau);

	public List<VrijeMatrix> getVrijeMatrices(Deelnemer deelnemer);

	public List<VrijeMatrix> getGlobaleVrijeMatrices();

	public List<DeelnemerMatrix> getGekoppeldeVrijeMatrices(Deelnemer deelnemer);

}
