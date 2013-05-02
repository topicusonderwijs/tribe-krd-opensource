(function($) {
	jQuery.fn.contBox = function(params) {
		var contBox = this;
		$("#" + params.feedback).live("animationDone", calcContBoxMaxHeight);

		$(window).resize(calcContBoxMaxHeight);
		calcContBoxMaxHeight();

		contBox.find("[class]:input:visible:eq(0)").focus();

		function calcContBoxMaxHeight() {
			var max = document.documentElement.clientHeight;
			// niet zo'n hele nette oplossing om te compenseren voor 50px margin en infobutton
			max -= 60;
			$(".subtract-from-contbox").each(function(index) {
				max -= $(this).outerHeight();
			});
			contBox.css("max-height", max + "px");
		}
	}
})(jQuery);