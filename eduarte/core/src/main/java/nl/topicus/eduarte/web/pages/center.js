(function($) {
	var container;
	
	jQuery.fn.center = function() {
		container = this;
		recenter();
		$(window).resize(recenter);
		
		function recenter() {
			var width = container.width();
			var schermbreedte = document.documentElement.clientWidth;
			var margin = (schermbreedte - width)/2;
			container.css("margin-left", margin + "px");
		}
	}
})(jQuery);