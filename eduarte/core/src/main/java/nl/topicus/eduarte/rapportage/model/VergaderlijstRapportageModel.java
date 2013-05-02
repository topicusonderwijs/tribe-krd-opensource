package nl.topicus.eduarte.rapportage.model;

import java.util.List;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRField;
import nl.topicus.cobra.modelsv2.ModelFactory;
import nl.topicus.cobra.util.Asserts;
import nl.topicus.cobra.util.ComponentUtil;
import nl.topicus.eduarte.entities.examen.Examendeelname;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.rapportage.entities.OnderwijsproductAfnameContext_SE_CE_Eindresultaat;

import org.apache.wicket.model.IDetachable;
import org.apache.wicket.model.IModel;

public class VergaderlijstRapportageModel implements IDetachable, JRDataSource
{
	private static final long serialVersionUID = 1L;

	private IModel<List<OnderwijsproductAfnameContext_SE_CE_Eindresultaat>> resultatenModel;

	private IModel<Verbintenis> verbintenisModel;

	public VergaderlijstRapportageModel(final IModel<Verbintenis> verbintenisModel)
	{
		Verbintenis verbintenis = verbintenisModel.getObject();
		this.verbintenisModel = verbintenisModel;
		Asserts.assertNotEmpty("verbintenis mag niet null zijn", verbintenis);
		List<OnderwijsproductAfnameContext_SE_CE_Eindresultaat> res =
			verbintenis.getVergaderlijstCijfers();
		resultatenModel = ModelFactory.getModel(res);
	}

	private int toetsIndex = -1;

	public List<OnderwijsproductAfnameContext_SE_CE_Eindresultaat> getResultaten()
	{
		return resultatenModel.getObject();
	}

	public Verbintenis getVerbintenis()
	{
		return verbintenisModel.getObject();
	}

	@Override
	public void detach()
	{
		ComponentUtil.detachQuietly(resultatenModel);
	}

	@Override
	public Object getFieldValue(JRField jrField)
	{
		if (toetsIndex / 3 == getResultaten().size())
		{
			if ("toets".equals(jrField.getName()))
				return "Eind";
			if ("onderwijsproduct".equals(jrField.getName()))
				return "Ex_Uit";
			if ("cijfer".equals(jrField.getName()))
			{
				Examendeelname deeln = getVerbintenis().getExamendeelname();
				if (deeln.getExamenstatus().isGeslaagd())
					return "Gesl";
				if (deeln.getExamenstatus().isAfgewezen())
					return "Afg";
				if (deeln.getExamenstatus().isCertificaten())
					return "Cert";
				if (deeln.getExamenstatus().isGespreidExamen())
					return "Gespr";
				if (deeln.getExamenstatus().isVerwijderd())
					return "Verw";
				if (deeln.getExamenstatus().isAangemeld())
					return "Aang";
				if (deeln.getExamenstatus().isVoorlopigGeslaagd())
					return "V-Gesl";
				if (deeln.getExamenstatus().isVoorlopigAfgewezen())
					return "V-Afg";
				if (deeln.getExamenstatus().isTeruggetrokken())
					return "Terug";
			}
			return null;
		}
		OnderwijsproductAfnameContext_SE_CE_Eindresultaat resultaat =
			getResultaten().get(toetsIndex % getResultaten().size());
		if ("toets".equals(jrField.getName()))
		{
			if (toetsIndex < getResultaten().size())
				return "SE";
			if (toetsIndex < (getResultaten().size() * 2))
				return "CE";
			if (toetsIndex < (getResultaten().size() * 3))
				return "Eind";
		}

		if ("onderwijsproduct".equals(jrField.getName()))
		{
			String titel =
				resultaat.getKeuze().getOnderwijsproductAfname().getOnderwijsproduct().getCode();
			if (resultaat.getKeuze().isHogerNiveau())
				titel += "*";
			return titel;
		}

		if ("cijfer".equals(jrField.getName()))
		{
			if (toetsIndex < getResultaten().size())
			{
				if (resultaat.getSeResultaat() != null)
					return resultaat.getSeResultaat().getFormattedDisplayWaarde();
				return null;
			}
			if (toetsIndex < (getResultaten().size() * 2))
			{
				if (resultaat.getCeResultaat() != null)
					return resultaat.getCeResultaat().getFormattedDisplayWaarde();
				return null;
			}
			if (toetsIndex < (getResultaten().size() * 3))
			{
				if (resultaat.getEindresultaat() != null)
					return resultaat.getEindresultaat().getFormattedDisplayWaarde();
				return null;
			}
		}
		return null;
	}

	@Override
	public boolean next()
	{
		if (((toetsIndex / 3) + 1) > getResultaten().size())
			return false;

		toetsIndex++;
		return (toetsIndex / 3) <= getResultaten().size();
	}
}
