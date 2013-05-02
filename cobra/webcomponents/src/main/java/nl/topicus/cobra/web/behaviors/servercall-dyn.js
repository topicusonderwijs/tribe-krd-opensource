var args = arguments;
if (isArray(methodName)) {
	args = methodName;
	methodName = args[0];
}
var argCount = args.length - 1;
var count = 1;
var methodArgs = '';
while (count <= argCount) {
	methodArgs = methodArgs + '&arg'+count+'='+Wicket.Form.encode(args[count]);
	count++;
}
${ajaxCall}