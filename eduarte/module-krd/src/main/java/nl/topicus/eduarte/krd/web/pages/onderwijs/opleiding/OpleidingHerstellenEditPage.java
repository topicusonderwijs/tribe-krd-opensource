package nl.topicus.eduarte.krd.web.pages.onderwijs.opleiding;

import nl.topicus.cobra.app.PageInfo;
import nl.topicus.cobra.security.InPrincipal;
import nl.topicus.cobra.web.components.Enclosure;
import nl.topicus.eduarte.entities.opleiding.Opleiding;
import nl.topicus.eduarte.krd.principals.onderwijs.OpleidingWrite;
import nl.topicus.eduarte.krd.web.components.choice.LeerwegCombobox;
import nl.topicus.eduarte.web.pages.SecurePage;
import nl.topicus.eduarte.web.pages.onderwijs.opleiding.OpleidingkaartHerstellenPage;

/**
 * @author idserda
 */
@PageInfo(title = "Opleiding bewerken (herstellen)", menu = {"Onderwijs > Opleidingen > Opleidingen Herstellen > [opleiding] > Bewerken"})
@InPrincipal(OpleidingWrite.class)
public class OpleidingHerstellenEditPage extends OpleidingEditPage
{
	public OpleidingHerstellenEditPage(Opleiding opleiding, SecurePage returnToPage)
	{
		super(opleiding, returnToPage);
	}

	@Override
	protected void addLeerweg()
	{
		Enclosure enclosure = new Enclosure("leerweg");
		form.add(enclosure);

		LeerwegCombobox comboBox = new LeerwegCombobox("leerweg", this)
		{
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isEnabled()
			{
				return true;
			}

		};
		enclosure.add(comboBox);
	}

	@Override
	protected SecurePage getResponsePageNaOpslaan()
	{
		return new OpleidingkaartHerstellenPage(getContextOpleiding());
	}
}
