( function($) {
	var lastActivity = new Date();
	var sessieTimeoutTime;
	var screensaver;

	jQuery.fn.screenSaver = function(timeout, sessieTimeout, keepAliveCycle,
			timerId, initVisible) {
		screensaver = this;
		$(document).mousemove(userActivity);
		$(document).mousedown(userActivity);
		$(document).keydown(userActivity);

		if (initVisible)
			show();
		else
			init();
		
		$(document).everyTime(keepAliveCycle, "alive", keepAlive, 0);
		return this;

		function check() {
			var elapsed = new Date().getTime() - lastActivity.getTime();
			if (elapsed >= timeout) {
				show();
			}
		}

		function keepAlive() {
			screensaver.serverCall("keepAlive");
		}

		function logout() {
			screensaver.serverCall("logoff");
		}

		function userActivity() {
			lastActivity = new Date();
		}

		function show() {
			$(document).stopTime("check");
			screensaver.serverCall("show");
			if (sessieTimeout != 0) {
				sessieTimeoutTime = new Date().getTime() + sessieTimeout;
				updateTimer();
				$(document).everyTime(1000, "timerUpdate", updateTimer, 0);
				$(document).oneTime(sessieTimeout, "sessieTimeout", logout);
			}
		}

		function init() {
			$(document).stopTime("check");
			$(document).stopTime("alive");
			$(document).stopTime("sessieTimeout");
			$(document).stopTime("timerUpdate");
			$(document).everyTime(1000, "check", check, 0);
		}

		function updateTimer() {
			var timeoutMs = sessieTimeoutTime - new Date().getTime();
			var min = Math.floor(timeoutMs / 60000);
			timeoutMs -= min * 60000;
			var sec = Math.floor(timeoutMs / 1000);
			if (sec < 10)
				sec = "0" + sec;
			$(timerId).text(min + ":" + sec);
		}
	}
})(jQuery);

function changeMaskStyle(style) {
	var myWindow = Wicket.Window.get();
	if (myWindow) {
		while (myWindow.oldWindow)
			myWindow = myWindow.oldWindow;
		if (myWindow.mask) {
			if (myWindow.mask.element)
				myWindow.mask.element.className = style;
		}
	}
}
