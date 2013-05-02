package nl.topicus.eduarte.app.security.checks;

import java.io.Serializable;

import nl.topicus.cobra.dao.BatchDataAccessHelper;
import nl.topicus.cobra.dao.DataAccessRegistry;
import nl.topicus.cobra.security.Actions;
import nl.topicus.eduarte.app.EduArteApp;
import nl.topicus.eduarte.app.security.actions.Begeleider;
import nl.topicus.eduarte.app.security.actions.Docent;
import nl.topicus.eduarte.app.security.actions.Instelling;
import nl.topicus.eduarte.app.security.actions.OrganisatieEenheid;
import nl.topicus.eduarte.app.security.actions.Uitvoerend;
import nl.topicus.eduarte.app.security.actions.Verantwoordelijk;
import nl.topicus.eduarte.entities.dbs.ZorgvierkantObject;
import nl.topicus.eduarte.entities.security.authentication.Account;
import nl.topicus.eduarte.providers.DeelnemerProvider;

import org.apache.wicket.security.actions.Enable;
import org.apache.wicket.security.actions.Render;
import org.apache.wicket.security.actions.WaspAction;
import org.apache.wicket.security.checks.ISecurityCheck;
import org.apache.wicket.security.checks.SecurityChecks;
import org.apache.wicket.security.swarm.checks.DataSecurityCheck;
import org.hibernate.Hibernate;

@Actions( {Instelling.class, OrganisatieEenheid.class, Begeleider.class, Docent.class,
	Verantwoordelijk.class, Uitvoerend.class})
public class ZorgvierkantObjectSecurityCheck extends EduArteSecurityCheck
{
	private static final long serialVersionUID = 1L;

	private Serializable id;

	private Class<ZorgvierkantObject> clazz;

	@SuppressWarnings("unchecked")
	public ZorgvierkantObjectSecurityCheck(ISecurityCheck wrapped, ZorgvierkantObject object)
	{
		super(wrapped);
		if (object != null)
		{
			id = object.getIdAsSerializable();
			clazz = Hibernate.getClass(object);
		}
	}

	@Override
	protected boolean isEntitySet()
	{
		return id != null;
	}

	@SuppressWarnings("unchecked")
	protected ZorgvierkantObject getZorgvierkantObject()
	{
		BatchDataAccessHelper<ZorgvierkantObject> helper =
			DataAccessRegistry.getHelper(BatchDataAccessHelper.class);
		return helper.get(clazz, id);
	}

	private boolean valtBinnenZorglijn(ZorgvierkantObject object, Account account)
	{
		if (object.getZorglijn() == null)
			return true;
		if (account.getMaxZorglijn() == null)
			return false;
		return object.getZorglijn() <= account.getMaxZorglijn();
	}

	@Override
	protected final boolean verify(WaspAction action)
	{
		ZorgvierkantObject object = getZorgvierkantObject();
		Account account = getAccount();

		if (!valtBinnenZorglijn(object, account))
			return false;

		if (!object.isVertrouwelijk())
			return true;

		Class< ? extends WaspAction> check = isEditTarget() ? Enable.class : Render.class;
		return new DeelnemerSecurityCheck(
			new DataSecurityCheck(object.getVertrouwelijkSecurityId()), object)
			.isActionAuthorized(getAction(check));
	}

	public static boolean isAllowed(Class< ? extends WaspAction> action,
			final ZorgvierkantObject object)
	{
		DataSecurityCheck dataCheck = new DataSecurityCheck(object.getSecurityId());
		return SecurityChecks.and(new ZorgvierkantObjectSecurityCheck(dataCheck, object)
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected boolean isEntitySet()
			{
				return true;
			}

			@Override
			protected ZorgvierkantObject getZorgvierkantObject()
			{
				return object;
			}
		}, new DeelnemerSecurityCheck(dataCheck, object.getDeelnemer())).isActionAuthorized(
			EduArteApp.get().getActionFactory().getAction(action));
	}

	public static boolean isAllowed(Class< ? extends WaspAction> action, String securityId,
			DeelnemerProvider deelnemerProvider)
	{
		return new DeelnemerSecurityCheck(new DataSecurityCheck(securityId), deelnemerProvider)
			.isActionAuthorized(EduArteApp.get().getActionFactory().getAction(action));
	}
}
