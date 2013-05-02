package nl.topicus.eduarte.web.components.resultaat;

import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.entities.onderwijsproduct.OnderwijsproductAfnameContext;
import nl.topicus.eduarte.entities.resultaatstructuur.Resultaatstructuur;

public class VerbintenisAfhankelijkeToetsComparator extends StructuurToetsComparator
{
	private static final long serialVersionUID = 1L;

	private Verbintenis verbintenis;

	public VerbintenisAfhankelijkeToetsComparator(boolean ascending, Verbintenis verbintenis)
	{
		super(ascending);
		this.verbintenis = verbintenis;
	}

	@Override
	protected String getPathPrefix(Resultaatstructuur structuur)
	{
		String ret = super.getPathPrefix(structuur);
		OnderwijsproductAfnameContext afnameContext =
			verbintenis == null ? null : verbintenis.getAfnameContext(structuur);
		if (afnameContext == null)
			return ret;

		return volgnummerToString(afnameContext.getProductregel().getSoortProductregel()
			.getVolgnummer())
			+ "/" + volgnummerToString(afnameContext.getProductregel().getVolgnummer()) + "/" + ret;
	}
}
