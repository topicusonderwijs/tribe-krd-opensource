package nl.topicus.eduarte.krd.web.validators;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import nl.topicus.cobra.util.TimeUtil;
import nl.topicus.cobra.web.components.text.DatumField;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.providers.BPVInschrijvingProvider;

import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.validation.AbstractFormValidator;

public class BPVBegindatumVerwachteEindatumValidator extends AbstractFormValidator
{
	private static final long serialVersionUID = 1L;

	private DatumField ingangsdatum;

	private DatumField geplandeEinddatum;

	private BPVInschrijvingProvider bpvProvider;

	public BPVBegindatumVerwachteEindatumValidator(BPVInschrijvingProvider bpvProvider,
			DatumField ingangsdatum, DatumField geplandeEinddatum)
	{
		this.bpvProvider = bpvProvider;
		this.ingangsdatum = ingangsdatum;
		this.geplandeEinddatum = geplandeEinddatum;
	}

	@Override
	public FormComponent< ? >[] getDependentFormComponents()
	{
		return new FormComponent[] {ingangsdatum, geplandeEinddatum};
	}

	@Override
	public void validate(Form< ? > form)
	{
		Date begindatum = ingangsdatum.getDatum();
		Date einddatum = geplandeEinddatum.getDatum();
		if (bpvProvider.getBPV() != null && begindatum != null)
		{
			if (bpvProvider.getBPV().getBpvBedrijf() != null)
			{
				if (!bpvProvider.getBPV().getBpvBedrijf().isActief(begindatum))
				{
					error(ingangsdatum);
				}
			}
		}
		Verbintenis verbintenis = bpvProvider.getBPV().getVerbintenis();
		if (begindatum != null && verbintenis.getBegindatum() != null
			&& begindatum.before(verbintenis.getBegindatum()))
		{
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("beginverbintenis", TimeUtil.getInstance().formatDate(
				verbintenis.getBegindatum()));
			error(ingangsdatum, "voorverbintenis", params);
		}
		if (einddatum != null && verbintenis.getGeplandeEinddatum() != null
			&& einddatum.after(verbintenis.getGeplandeEinddatum()))
		{
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("eindverbintenis", TimeUtil.getInstance().formatDate(
				verbintenis.getGeplandeEinddatum()));
			error(geplandeEinddatum, "naverbintenis", params);
		}
	}
}