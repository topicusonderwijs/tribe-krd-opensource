function clickTriStateCheckBox(event)
{
	var inputField = $(this).children("input");
	var dualmode = $(this).hasClass("dualmode");
	var startpartial = $(this).hasClass("startpartial");
	if (inputField.val() == "On") {
		$(this).removeClass("on");
		if (dualmode && startpartial) {
			inputField.val("Partial");
			$(this).addClass("partial");
		} else {
			inputField.val("Off");
			$(this).addClass("off");
		}
	} else if (inputField.val() == "Partial") {
		$(this).removeClass("partial");
		inputField.val("On");
		$(this).addClass("on");
	} else {
		$(this).removeClass("off");
		if (startpartial) {
			inputField.val("Partial");
			$(this).addClass("partial");
		} else {
			inputField.val("On");
			$(this).addClass("on");
		}
	}
	inputField.change();
}
