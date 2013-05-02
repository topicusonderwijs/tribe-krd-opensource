$.fn.wait = function(time, type) 
{
    time = time || 1000;
    type = type || "fx";
    return this.queue(type, function() 
    {
        var self = this;
        setTimeout(function() 
		{
            $(self).dequeue();
        }, time);
    });
};

(function($)
{
	jQuery.fn.popupFeedback = function()
	{
		$(this).css({ 'z-index' : 5100 });
		$(this).slideToggle('slow', function()
		{
			$(this).wait(8000).slideToggle();
		});
	}
})(jQuery); 