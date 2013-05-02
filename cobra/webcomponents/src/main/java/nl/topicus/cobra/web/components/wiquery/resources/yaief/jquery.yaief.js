
jQuery.fn.extend({
  yaief: function() {
	if($.browser.msie && parseInt(jQuery.browser.version) > 6)
	{
	    $(this).wrap('<div class="select-wrapper"></div>');
	    $(this).after('<img class="select-wrapper-img" src="../assets/img/icons/dropdown.png" />');
	    $(this).css({'width': 'auto', 'border': 'none'});
	    
	    if($(this).width() < 165)
	    	$(this).css({'width' : '165px'});
	    
	    $('.select-wrapper-img').click(function() { $(this).siblings('select').focus(); });
	}
  }
});
