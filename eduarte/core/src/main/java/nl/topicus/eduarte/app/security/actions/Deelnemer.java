package nl.topicus.eduarte.app.security.actions;

import nl.topicus.cobra.security.Description;
import nl.topicus.cobra.security.RechtenSoort;
import nl.topicus.cobra.security.RechtenSoorten;

import org.apache.wicket.security.actions.WaspAction;

/**
 * Actie om aan te geven dat iemand daadwerkelijk deelnemer is, maw deze persoon heeft
 * direct een permissie gekregen op het niveau deelnemer.
 * 
 * @author papegaaij
 */
@RechtenSoorten(RechtenSoort.DEELNEMER)
@Description("Deelnemer")
public interface Deelnemer extends WaspAction
{
}
