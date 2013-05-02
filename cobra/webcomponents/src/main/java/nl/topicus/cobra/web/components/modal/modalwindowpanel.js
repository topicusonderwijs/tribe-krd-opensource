( function($) {
	jQuery.fn.modalWindowPanel = function(params) {
		var panel = this;

		panel.keypress(keypressed);
		// remove submit handlers added by AbstractAjaxDropDownChoice
		panel.find("select").removeAttr("onkeypress");

		function keypressed(event) {
			if (event.keyCode == 13) {
				var form = $(event.target).parents("form:eq(0)");
				var input = form.find(":submit,:image");
				if (input.size() > 0) {
					input.get(0).click();
					event.stopPropagation();
					event.preventDefault();
					return false;
				}
			}
		}
	}
})(jQuery);