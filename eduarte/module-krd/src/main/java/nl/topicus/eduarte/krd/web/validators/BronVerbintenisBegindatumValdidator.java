package nl.topicus.eduarte.krd.web.validators;

import java.util.Date;

import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis.Bekostigd;
import nl.topicus.eduarte.krd.bron.BronVerbintenisWijzigingToegestaanCheck;

import org.apache.wicket.model.IModel;
import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.validator.AbstractValidator;

/**
 * Validator die alleen gezet wordt op de Intake stap 4 waar de gebruiker de mogelijkheid
 * heeft, om een verbintenis toe te voegen aan de intake. Er wordt gecontroleerd als de
 * status broncommuniceerbaar is, en de verbinenis is BO of VO, en als het BO is of de
 * status bekostigd is, of de begindatum nog wel mag.
 * 
 * @author Arjan
 */

public class BronVerbintenisBegindatumValdidator extends AbstractValidator<Date>
{
	private static final long serialVersionUID = 1L;

	private IModel<Verbintenis> verbintenisModel;

	public BronVerbintenisBegindatumValdidator(IModel<Verbintenis> verbintenisModel)
	{
		this.verbintenisModel = verbintenisModel;
	}

	@Override
	protected void onValidate(IValidatable<Date> validatable)
	{
		Verbintenis verbintenis = verbintenisModel.getObject();
		BronVerbintenisWijzigingToegestaanCheck check =
			new BronVerbintenisWijzigingToegestaanCheck();
		Date begindatum = validatable.getValue();
		if (verbintenis.isBronCommuniceerbaar())
		{
			if (verbintenis.isBOVerbintenis() && Bekostigd.Nee != verbintenis.getBekostigd())
			{
				Date peildatumBo = check.getPeildatumBO();
				if (!check.isVolgensBronStatusMutatieToegestaan(verbintenis, begindatum,
					peildatumBo).isToegestaan())
					error(validatable);
			}
			if (verbintenis.isVOVerbintenis())
			{
				Date peildatumVo = check.getPeildatumVo();
				if (!check.isVolgensBronStatusMutatieToegestaan(verbintenis, begindatum,
					peildatumVo).isToegestaan())
					error(validatable);

			}
		}
	}
}