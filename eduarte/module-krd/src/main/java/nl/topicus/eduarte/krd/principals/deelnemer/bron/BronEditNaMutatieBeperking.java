package nl.topicus.eduarte.krd.principals.deelnemer.bron;

import nl.topicus.cobra.security.Actions;
import nl.topicus.cobra.security.Description;
import nl.topicus.cobra.security.ExtraPermission;
import nl.topicus.cobra.security.ExtraPermissions;
import nl.topicus.cobra.security.Write;
import nl.topicus.eduarte.app.EduArteModuleKey;
import nl.topicus.eduarte.app.Module;
import nl.topicus.eduarte.app.security.AbstractEduArtePrincipalSource;
import nl.topicus.eduarte.app.security.Display;
import nl.topicus.eduarte.app.security.actions.Instelling;
import nl.topicus.eduarte.krd.bron.BronBpvWijzigingToegestaanCheck;

import org.apache.wicket.security.actions.Enable;
import org.apache.wicket.security.hive.authorization.permissions.DataPermission;

@Actions(Instelling.class)
@Description("BRON wijzigingen na mutatiebeperking")
@Module(EduArteModuleKey.KERNREGISTRATIE_DEELNEMERS)
@ExtraPermissions(@ExtraPermission(type = DataPermission.class, name = BronBpvWijzigingToegestaanCheck.WIJZIGEN_NA_MUTATIEBEPERKING, actions = Enable.class))
@Write
@Display(parent = "Beheer", label = "BRON wijzigingen na mutatiebeperking", module = EduArteModuleKey.KERNREGISTRATIE_DEELNEMERS)
public class BronEditNaMutatieBeperking extends AbstractEduArtePrincipalSource
{
	private static final long serialVersionUID = 1L;
}