function clickTriStateCheckBox(event)
{
	var jcheckbox = $(this);
	var valueField = jcheckbox.children("input.valueField");
	if (valueField.val() == "On") {
		jcheckbox.removeClass("on");
		var impliesField = jcheckbox.children("input.impliesField");
		if (impliesField.val() > 0) {
			valueField.val("Partial");
			jcheckbox.addClass("partial");
			removeCheck(jcheckbox.get(0).implies);
		} else {
			valueField.val("Off");
			jcheckbox.addClass("off");
			removeCheck(jcheckbox.get(0).implies);
		}
	} else if (valueField.val() == "Partial") {
		valueField.val("On");
		jcheckbox.removeClass("partial");
		jcheckbox.addClass("on");
		addCheck(jcheckbox.get(0).implies);
	} else {
		valueField.val("On");
		jcheckbox.removeClass("off");
		jcheckbox.addClass("on");
		addCheck(jcheckbox.get(0).implies);
	}
}

function addCheck(implies) {
	for (count=0; count<implies.length; count++) {
		var jcheckbox = $(implies[count]);
		var impliesField = jcheckbox.children("input.impliesField");
		if (impliesField.val() == 0) {
			var valueField = jcheckbox.children("input.valueField");
			if (valueField.val() == "Off") {
				valueField.val("Partial");
				jcheckbox.removeClass("off");
				jcheckbox.addClass("partial");
			}
		}
		impliesField.val(parseInt(impliesField.val())+1);
	}
}

function removeCheck(implies) {
	for (count=0; count<implies.length; count++) {
		var jcheckbox = $(implies[count]);
		var impliesField = jcheckbox.children("input.impliesField");
		impliesField.val(parseInt(impliesField.val())-1);
		if (impliesField.val() == 0) {
			var valueField = jcheckbox.children("input.valueField");
			var valueField = jcheckbox.children("input.valueField");
			if (valueField.val() == "Partial") {
				valueField.val("Off");
				jcheckbox.removeClass("partial");
				jcheckbox.addClass("off");
			}
		}
	}
}
