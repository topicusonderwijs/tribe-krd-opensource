package nl.topicus.eduarte.zoekfilters;

import nl.topicus.cobra.util.StringUtil;
import nl.topicus.cobra.web.components.form.AutoForm;
import nl.topicus.eduarte.entities.personen.Persoon;
import nl.topicus.eduarte.entities.signalering.Signaal;

import org.apache.wicket.model.IModel;

public class SignaalZoekFilter extends AbstractOrganisatieEenheidLocatieZoekFilter<Signaal>
{
	private static final long serialVersionUID = 1L;

	public static enum SignaalStatus
	{
		AlleSignalen,
		OpenstaandeSignalen,
		NieuweSignalen;

		@Override
		public String toString()
		{
			return StringUtil.convertCamelCase(name());
		}
	}

	private IModel<Persoon> ontvanger;

	@AutoForm(htmlClasses = "unit_160", required = true)
	private SignaalStatus status = SignaalStatus.NieuweSignalen;

	public SignaalZoekFilter(Persoon ontvanger)
	{
		setOntvanger(ontvanger);
	}

	public Persoon getOntvanger()
	{
		return getModelObject(ontvanger);
	}

	public void setOntvanger(Persoon ontvanger)
	{
		this.ontvanger = makeModelFor(ontvanger);
	}

	public SignaalStatus getStatus()
	{
		return status;
	}

	public void setStatus(SignaalStatus status)
	{
		this.status = status;
	}
}
