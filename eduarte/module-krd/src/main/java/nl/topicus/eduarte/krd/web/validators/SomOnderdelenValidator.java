package nl.topicus.eduarte.krd.web.validators;

import java.math.BigDecimal;
import java.util.HashMap;

import nl.topicus.eduarte.entities.contract.Contract;
import nl.topicus.eduarte.entities.contract.ContractOnderdeel;

import org.apache.wicket.model.IModel;
import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.validator.AbstractValidator;

public class SomOnderdelenValidator extends AbstractValidator<BigDecimal>
{
	private static final long serialVersionUID = 1L;

	private IModel<Contract> contractModel;

	public SomOnderdelenValidator(IModel<Contract> contractModel)
	{
		this.contractModel = contractModel;
	}

	@Override
	protected void onValidate(IValidatable<BigDecimal> validatable)
	{
		final BigDecimal kostprijs = (contractModel.getObject()).getKostprijs();
		BigDecimal huidigeSom = new BigDecimal(0);
		for (ContractOnderdeel contractOnderdeel : (contractModel.getObject())
			.getContractOnderdelen())
		{
			BigDecimal prijs = contractOnderdeel.getPrijs();
			if (prijs != null)
				huidigeSom = huidigeSom.add(prijs);
		}
		final BigDecimal totaalSom = huidigeSom.add(validatable.getValue());
		if (totaalSom.compareTo(kostprijs) > 0)
		{
			error(validatable, new HashMap<String, Object>()
			{
				private static final long serialVersionUID = 1L;

				{
					put("sum", totaalSom.toPlainString());
					put("prijs", kostprijs.toPlainString());
				}
			});
		}
	}
}