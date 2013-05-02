package nl.topicus.eduarte.web.pages.beheer.rapportage;

import java.io.StringWriter;

import nl.topicus.cobra.templates.ExtractPropertyUtil;
import nl.topicus.eduarte.web.components.panels.templates.RapportageConfiguratiePanel;
import nl.topicus.eduarte.web.components.panels.templates.RapportageConfiguratieRegistratie;

public class RapportageConfiguratieMergeFieldResolver implements MergeFieldResolver
{
	private static final long serialVersionUID = 1L;

	private RapportageConfiguratieRegistratie configReg;

	public RapportageConfiguratieMergeFieldResolver(
			Class< ? extends RapportageConfiguratiePanel< ? >> configPanel)
	{
		configReg = configPanel.getAnnotation(RapportageConfiguratieRegistratie.class);
	}

	@Override
	public void resolveMergeFields(ExtractPropertyUtil propertyExtractor, StringWriter writer)
	{
		propertyExtractor.extractClass(configReg.factoryType(), "instellingen", writer);
		propertyExtractor.extractClass(configReg.configuratieType(), configReg.naam(), writer);
	}
}
