function verifyModalWindow(containerId, windowId, contentsId) {
	var container = $('#' + containerId);
	if (container.find('#' + windowId).size() == 0) {
		container.append('<div id="' + windowId
				+ '" style="display: none"><div id="' + contentsId
				+ '" style="display: none"></div></div>');
	}
}