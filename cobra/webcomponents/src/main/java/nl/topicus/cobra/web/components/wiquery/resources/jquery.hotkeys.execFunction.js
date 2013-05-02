function followLink(id) {
	var link = $(id);
	if (link.length != 0) {
		if (link.get(0).nodeName == 'INPUT' || link.get(0).nodeName == 'SELECT') {
			link.focus();
		} else {
			var event = jQuery.Event('click');
			jQuery.event.trigger(event, [], link[0]);
			if (event.result != false && link.attr('href') != '#') {
				window.location.href = link.attr('href');
			}
		}
	}
};