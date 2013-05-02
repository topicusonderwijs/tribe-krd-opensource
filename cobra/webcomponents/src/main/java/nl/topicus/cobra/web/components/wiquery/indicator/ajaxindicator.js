( function($) {
	jQuery.fn.ajaxIndicator = function() {
		Wicket.Ajax.registerPreCallHandler(preCall);
		Wicket.Ajax.registerPostCallHandler(postCall);
		Wicket.Ajax.registerFailureHandler(postCall);

		$("body").attr("onbeforeunload",
				"$('#" + $(this).attr("id") + "').css('visibility', 'visible').find('.image').show()");

		var image = $(this).find(".image");
		var indicator = this;
		var timer;

		return this;

		function preCall() {
			indicator.css( {
				visibility : "visible"
			});
			timer = setTimeout(fadeIn, 200);
		}

		function fadeIn() {
			image.fadeIn(800);
		}

		function postCall() {
			if (timer)
				clearTimeout(timer);
			image.hide();
			indicator.css( {
				visibility : "hidden"
			});
		}
	}
})(jQuery);