(function($) {
	$.fn.slidebar = function() {
		var sidebar = this;

		if (screen.width < 1280) { 
			sidebar.find(".slider").click(function() {
				sidebar.animate( {
					marginLeft : "-170px",
					paddingRight : "15px",
					marginRight: "0px",
					right : "0px"
				}, 750);
			sidebar.find(".slider").hide();
			});

			$(document).click(function() {
				sidebar.animate( {			
					marginRight : "-400px" 			
				}, 750);
				sidebar.find(".slider").show();
			});
			$(".slider").bind("click", function(event){ 
			  event.stopPropagation(); 
			});
					
		} else {
			sidebar.find(".slider").hide();
		}

	}
})(jQuery);