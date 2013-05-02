/*
 * Copyright (c) 2005, Topicus B.V.
 * All rights reserved.
 */
package nl.topicus.eduarte.app.security.actions;

import nl.topicus.cobra.security.Description;
import nl.topicus.cobra.security.RechtenSoort;
import nl.topicus.cobra.security.RechtenSoorten;
import nl.topicus.eduarte.app.EduArteModuleKey;
import nl.topicus.eduarte.app.Module;

import org.apache.wicket.security.actions.WaspAction;

/**
 * Actie om aan te geven dat iemand daadwerkelijk begeleider is, maw deze persoon heeft
 * direct een permissie gekregen op het niveau begeleider.
 * 
 * @author marrink
 */
@RechtenSoorten(RechtenSoort.INSTELLING)
@Module(EduArteModuleKey.GROEPS_AUTHORISATIE)
@Description("Mentor")
public interface Begeleider extends WaspAction
{
}
