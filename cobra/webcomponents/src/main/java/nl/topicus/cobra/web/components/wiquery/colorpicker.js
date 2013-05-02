$('#${pickerid}').ColorPicker({
	color: $("#${selectedid}").val(),
	onShow: function (colpkr) {
		$(colpkr).slideDown(100);
		return false;
	},
	onHide: function (colpkr) {
		$(colpkr).slideUp(100);
		return false;
	},
	onChange: function (hsb, hex, rgb) {
		$('#${displayid}').css('background-color', '#' + hex);
		$("#${selectedid}").val('#' + hex);
	}
});
$('#${displayid}').css('background-color', $("#${selectedid}").val());
