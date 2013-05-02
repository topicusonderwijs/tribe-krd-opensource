jQuery.fn.serverCall = function(methodName){
	var args = Array();
	var i = 0;
	while (i < arguments.length) {
		args[i] = arguments[i];
		i++;
	}
	return this.each(function(){
		this.serverCall(args);
	});
}

function bindServerCall(bindElement, callFunction) {
	bindElement.serverCall = callFunction;
}

function isArray(obj) {
	return obj.constructor == Array;
}
