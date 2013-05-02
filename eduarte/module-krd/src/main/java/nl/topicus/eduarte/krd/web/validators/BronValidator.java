package nl.topicus.eduarte.krd.web.validators;

import java.util.ArrayList;
import java.util.List;

import nl.topicus.cobra.web.components.panels.bottomrow.VolgendeButton;
import nl.topicus.cobra.web.components.panels.bottomrow.VorigeButton;
import nl.topicus.eduarte.entities.inschrijving.Verbintenis;
import nl.topicus.eduarte.providers.VerbintenisProvider;

import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.IFormSubmittingComponent;
import org.apache.wicket.markup.html.form.SubmitLink;
import org.apache.wicket.validation.IValidatable;

public class BronValidator<T> extends AbstractVerbintenisValidator<T>
{
	private static final long serialVersionUID = 1L;

	private List<AbstractVerbintenisValidator<T>> validators =
		new ArrayList<AbstractVerbintenisValidator<T>>();

	private Form< ? > form;

	public BronValidator(VerbintenisProvider provider)
	{
		this(provider, null);
	}

	public BronValidator(VerbintenisProvider provider, Form< ? > form)
	{
		super(provider);
		this.form = form;
		validators.add(new GeboortedatumVerplichtValidator<T>(provider));
		validators.add(new VoornamenVerplichtZonderBsnValidator<T>(provider));
		validators.add(new AdresVerplichtValidator<T>(provider));
		validators.add(new GeslachtVerplichtValidator<T>(provider));
		validators.add(new VooropleidingVerplichtValidator<T>(provider));
		validators.add(new DeelnemerOuderDan4Validator<T>(provider));
		validators.add(new PlaatsingVOVerplichtValidator<T>(provider));
		validators.add(new EnigeVoVerbintenisValidator<T>(provider));
		validators.add(new GeenOverlapVoVerbintenisValidator<T>(provider));
	}

	@Override
	public void onValidate(IValidatable<T> validatable, Verbintenis verbintenis)
	{
		if (form != null)
		{
			IFormSubmittingComponent submitButton = form.findSubmittingButton();
			if (submitButton instanceof SubmitLink)
			{
				SubmitLink link = (SubmitLink) submitButton;
				// Op deze manier gaat de bronvalidator niet af op de vorigen en volgende
				// button,maar alleen bij de voltooien
				if (link.findParent(VorigeButton.class) != null
					|| link.findParent(VolgendeButton.class) != null)
					return;
			}
		}
		for (AbstractVerbintenisValidator<T> validator : validators)
		{
			validator.onValidate(validatable, verbintenis);
		}
	}
}
