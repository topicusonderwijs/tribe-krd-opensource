(function($) {
	jQuery.fn.slidingFeedback = function() {
		var slidingFeedback = this;

		slidingFeedback.slideDown('normal', triggerDone);
		slidingFeedback.bind('click', function() {
			slidingFeedback.removeClass("subtract-from-contbox");
			slidingFeedback.slideUp('normal', triggerDone);
		});

		function triggerDone() {
			slidingFeedback.trigger('animationDone');
		}
	}
})(jQuery);