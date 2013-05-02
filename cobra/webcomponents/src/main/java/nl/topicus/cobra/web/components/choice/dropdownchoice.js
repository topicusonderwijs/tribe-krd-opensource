(function($) {
	jQuery.fn.dropDownChoice = function(options) {
		options = jQuery.extend( {
			useYaief : false,
			autoselect : false
		}, options);

		if (options.useYaief) {
			this.yaief();
		}

		if (options.autoselect) {
			var availableOptions = this.find("option[value!='']");
			if (availableOptions.length == 1) {
				var currentValue = this.val();
				var availableValue = availableOptions.attr("value");
				if (currentValue != availableValue) {
					this.val(availableValue);
					this.change();
				}
			}
		}
	}
})(jQuery);