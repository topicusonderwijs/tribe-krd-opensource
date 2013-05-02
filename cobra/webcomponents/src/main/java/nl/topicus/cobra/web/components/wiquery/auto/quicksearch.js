(function($) {
	jQuery.fn.quickSearch = function(params) {
		var defaults = {

		};

		var input = this;

		var opts = $.extend(defaults, params);
		
		input.autocomplete( {
			source : opts.source,
			minLength : 0
		});

		input.bind("autocompleteselect", function(event, data) {
			$("#" + opts.hiddenField).val(data.item.id);
			// console.log("autocompleteselect");
			if (opts.callback) {
				doCallBack();
			}
		});
		input.bind("autocompletechange", function() {
			// console.log("autocompletechange");
			if (opts.callback) {
				doCallBack();
			}
		});

		function doCallBack() {
			opts.callback(input, input.val(), $("#" + opts.hiddenField).val());
		}
	}
})(jQuery);