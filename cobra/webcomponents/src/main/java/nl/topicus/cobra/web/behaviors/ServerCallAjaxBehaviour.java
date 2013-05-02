package nl.topicus.cobra.web.behaviors;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import nl.topicus.cobra.reflection.ReflectionUtil;

import org.apache.wicket.Page;
import org.apache.wicket.Request;
import org.apache.wicket.RequestCycle;
import org.apache.wicket.WicketRuntimeException;
import org.apache.wicket.ajax.AbstractDefaultAjaxBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.behavior.HeaderContributor;
import org.apache.wicket.util.template.PackagedTextTemplate;
import org.apache.wicket.util.template.TextTemplate;
import org.odlabs.wiquery.core.commons.IWiQueryPlugin;
import org.odlabs.wiquery.core.commons.WiQueryCoreHeaderContributor;
import org.odlabs.wiquery.core.commons.WiQueryResourceManager;
import org.odlabs.wiquery.core.javascript.JsStatement;

public class ServerCallAjaxBehaviour extends AbstractDefaultAjaxBehavior implements IWiQueryPlugin
{
	private static final long serialVersionUID = 1L;

	private Object callback;

	public ServerCallAjaxBehaviour()
	{
		this(null);
	}

	public ServerCallAjaxBehaviour(Object callback)
	{
		this.callback = callback;
	}

	protected Object getCallbackObject()
	{
		return callback == null ? getComponent() : callback;
	}

	@Override
	protected void onBind()
	{
		WiQueryCoreHeaderContributor contributor =
			WiQueryCoreHeaderContributor.bindToRequestCycle();
		contributor.addPlugin(this);
		getComponent().add(new HeaderContributor(contributor));
		super.onBind();
	}

	@Override
	public void contribute(WiQueryResourceManager wiQueryResourceManager)
	{
		wiQueryResourceManager
			.addJavaScriptResource(ServerCallAjaxBehaviour.class, "servercall.js");
	}

	@Override
	public JsStatement statement()
	{
		boolean isPage = getComponent() instanceof Page;
		String serverCallScript =
			"bindServerCall(document"
				+ (isPage ? "" : ".getElementById('" + getComponent().getMarkupId() + "')")
				+ ", function(methodName){\n" + getCallbackScript() + "\n});";
		return new JsStatement().append(serverCallScript);
	}

	@Override
	protected CharSequence getCallbackScript()
	{
		TextTemplate scriptTemplate =
			new PackagedTextTemplate(ServerCallAjaxBehaviour.class, "servercall-dyn.js");
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("ajaxCall", generateCallbackScript("wicketAjaxPost('" + getCallbackUrl()
			+ "', 'methodName=' + methodName + '&argCount=' + argCount + methodArgs"));
		return scriptTemplate.asString(params);
	}

	@Override
	protected void respond(AjaxRequestTarget target)
	{
		Request req = RequestCycle.get().getRequest();
		int argCount = Integer.parseInt(req.getParameter("argCount"));
		String methodName = req.getParameter("methodName");
		for (Method curMethod : getCallbackObject().getClass().getMethods())
		{
			if (curMethod.isAnnotationPresent(ClientSideCallable.class)
				&& curMethod.getName().equals(methodName))
			{
				Object[] args = null;
				if (curMethod.getParameterTypes().length == argCount)
					args = collectArgs(req, curMethod, argCount);
				else if (curMethod.getParameterTypes().length == argCount + 1)
				{
					if (isAjaxRequestTargetArg(curMethod, 0))
						args = collectArgs(req, curMethod, argCount, target);
					else if (isComponentArg(curMethod, 0))
						args = collectArgs(req, curMethod, argCount, getComponent());
				}
				else if (curMethod.getParameterTypes().length == argCount + 2
					&& isAjaxRequestTargetArg(curMethod, 0) && isComponentArg(curMethod, 1))
					args = collectArgs(req, curMethod, argCount, target, getComponent());

				if (args != null)
				{
					ReflectionUtil.invokeMethod(curMethod, getCallbackObject(), args);
					return;
				}
			}
		}
		throw new WicketRuntimeException("Method '" + methodName + "' (with " + argCount
			+ " arguments) not found on '" + getComponent().getClass()
			+ "', check if it exists and has the ClientSideCallable annotation.");
	}

	private boolean isAjaxRequestTargetArg(Method method, int argNr)
	{
		return AjaxRequestTarget.class.equals(method.getParameterTypes()[argNr]);
	}

	private boolean isComponentArg(Method method, int argNr)
	{
		return method.getParameterTypes()[argNr].isAssignableFrom(getComponent().getClass());
	}

	private Object[] collectArgs(Request request, Method method, int argCount,
			Object... extraArguments)
	{
		Object[] args = new Object[method.getParameterTypes().length];
		System.arraycopy(extraArguments, 0, args, 0, extraArguments.length);
		for (int count = 1; count <= argCount; count++)
			args[extraArguments.length + count - 1] =
				convertArgument(request.getParameter("arg" + count),
					method.getParameterTypes()[extraArguments.length + count - 1]);
		return args;
	}

	@SuppressWarnings("unchecked")
	protected <T> T convertArgument(String argument, Class<T> targetType)
	{
		return (T) ReflectionUtil.invokeStaticMethod(targetType.isPrimitive() ? ReflectionUtil
			.getWrapperClass(targetType) : targetType, "valueOf", argument);
	}
}
