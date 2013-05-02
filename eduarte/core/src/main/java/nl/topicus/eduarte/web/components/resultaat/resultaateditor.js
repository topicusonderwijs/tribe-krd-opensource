(function($) {
	var editor;
	var editorInfo;
	var selectedCell;
	var editorDisplay;
	var editorCell;
	var editorDisplayAlt;
	var editorCellAlt;
	var editorLabel;
	var editorTable;
	var editorInputField;
	var messageContainer;

	var leftPart;
	var leftPartSlider;
	var rightPart;
	var rightPartSlider;
	var rightPartSliderTable;
	var editPart;
	var alternatiefLabel;
	var alternatiefLabelContainer;

	jQuery.fn.resultaatEditor = function(params) {
		editor = this;
		editorInfo = this.data("editorInfo");
		editorTable = $(params.table);
		var editorDisplayTables = editor.find("table");

		editorDisplay = new Array();
		editorLabel = new Array();
		editorCell = new Array();
		for ( var count = 0; count <= editorInfo.maxCells + 1; count++) {
			editorCell[count] = editorDisplayTables
					.find("td.ciPoging-" + count);
			editorDisplay[count] = editorCell[count].find("div.ciValue");
			editorLabel[count] = editorDisplayTables.find("th.ciPoging-"
					+ count + " div.ciLabel");
		}
		editorCellAlt = editorDisplayTables.find("td.ciAlternatief");
		editorDisplayAlt = editorCellAlt.find("div.ciValue");

		editorInputField = editor.find("input.ciResultaatInput");
		this.click(function(event) {
			event.stopPropagation();
		});
		editor.find("td").click(changePoging);
		editor.find(".ciResultaatInput").blur(updateValue);
		editor.find(".ciButtons div").click(showDetails);
		editor.find(".ciResultaatInput").keydown(editorKeypress);
		$(document).click(closeEditor);
		editorTable.find(".ciField").click(moveEditor);
		if (editorInfo.multiToets) {
			var colCount = editorTable.find("th").size();
			for ( var index = 0; index < colCount; index++) {
				var headerCell = editorTable.find("th:eq(" + index + ")");
				if (headerCell.is(".ciColumn")
						&& jQuery.inArray("#" + headerCell.attr("id"),
								editorInfo.initialVisible) == -1) {
					hideColumn(index);
				}
			}
		}
		messageContainer = editor.find(".ciMessage");
		messageContainer.click(function() {
			hideMessage(true);
		});
		leftPart = editor.find("div.ciLeftPart");
		leftPartSlider = editor.find("div.ciLeftPart div.ciSlider");
		rightPart = editor.find("div.ciRightPart");
		rightPartSlider = editor.find("div.ciRightPart div.ciSlider");
		rightPartSliderTable = editor
				.find("div.ciRightPart div.ciSlider table");
		editPart = editor.find("div.ciEditPart");
		alternatiefLabel = editor.find("th.ciAlternatief div.ciLabel");
		alternatiefLabelContainer = editor
				.find("th.ciAlternatief div.ciLabelContainer");

		if (jQuery.browser.msie) {
			// Force IE to redraw the table to prevent phantom cells
			editorTable.find("tr:eq(0)").addClass("hide").removeClass("hide");
		}
	}

	function showMessage(message) {
		messageContainer.text(message);
		messageContainer.slideDown();
	}

	function hideMessage(animate) {
		if (animate)
			messageContainer.slideUp();
		else
			messageContainer.hide();
	}

	function headerSelector(index) {
		return editorTable.find("th:nth-child(" + (index + 1)
				+ ") .ciHeaderContainer");
	}

	function columnSelector(index) {
		return editorTable.find("th:nth-child(" + (index + 1)
				+ "),  td:nth-child(" + (index + 1) + ")");
	}

	function hideColumn(index) {
		columnSelector(index).hide();
	}

	function showColumn(index) {
		columnSelector(index).show();
	}

	function moveColumn(fromCell, toCell, doAnimate) {
		if (!editorInfo.multiToets || fromCell.get(0) == toCell.get(0))
			return;

		var oldIndex = fromCell.prevAll().size();
		var newIndex = toCell.prevAll().size();
		if (newIndex == oldIndex)
			return;

		hideColumn(oldIndex, doAnimate);
		showColumn(newIndex, doAnimate);
	}

	function resetColumn(doAnimate) {
		if (!editorInfo.multiToets || editorInfo.edit)
			return;

		var oldCell = selectedCell;
		var toetsInfo = $(oldCell.data("toetsInfo").resolveId)
				.data("toetsInfo");
		var newCell = $(toetsInfo.cells[0]);
		moveColumn(oldCell, newCell, doAnimate);
	}

	function editorKeypress(event) {
		switch (event.keyCode) {
		case 9:
			if (event.shiftKey)
				moveEditorForKey($(this), -1, 0);
			else
				moveEditorForKey($(this), 1, 0);
			event.preventDefault();
			return;
		case 38:
			moveEditorForKey($(this), 0, -1);
			event.preventDefault();
			return;
		case 13:
		case 40:
			moveEditorForKey($(this), 0, 1);
			event.preventDefault();
			return;
		}

		if (event.keyCode >= 48 && event.keyCode <= 90) {
			if (editorInfo.editable) {
				var pogingInfo = editor.data("pogingInfo");
				if (pogingInfo.message)
					showMessage(pogingInfo.message);
			} else
				showMessage("Klik op bewerken om cijfers in te voeren");
		}
	}

	function moveEditorForKey(inputField, xChange, yChange) {
		var xIndex = selectedCell.prevAll().size() + xChange;
		var yIndex = selectedCell.parent().prevAll().size() + yChange;
		// try the next cell when move fails to skip over disabled fields
		while (tryToMoveEditorForKey(inputField, editorInfo, xIndex, yIndex)) {
			xIndex += xChange;
			yIndex += yChange;
		}
	}

	function tryToMoveEditorForKey(inputField, editorInfo, xIndex, yIndex) {
		// stupid IE fix
		if (xIndex < 0 || yIndex < 0)
			return false;
		var newSelectedCell = editorTable.find("tbody tr:eq(" + yIndex
				+ ") td:eq(" + xIndex + ")");
		if (newSelectedCell.size() == 0)
			return false;
		else if (newSelectedCell.css("display") == "none")
			return true;
		else if (newSelectedCell.is(".ciField")) {
			inputField.blur();
			moveEditorToCell(newSelectedCell, false);
			return false;
		}
		return true;
	}

	function moveEditor(event) {
		moveEditorToCell($(this), true);
		event.stopPropagation();
	}

	function changePoging(event) {
		var poging = $(this).prevAll().size();
		if ($(this).is(".ciRightPart td") > 0)
			poging++;
		var toetsInfo = $(selectedCell.data("toetsInfo").resolveId).data(
				"toetsInfo");
		if (!toetsInfo.alternatief && poging > 0)
			poging--;
		if (editorInfo.multiToets)
			moveEditorToPoging($(toetsInfo.cells[poging]), true)
		else
			moveEditorToCell($(toetsInfo.cells[poging]), true);
	}

	function updateValue(event) {
		var value = editorInputField.val();
		selectedCell.find("span.ciValueDisplay span").text(value);
		selectedCell.find("input.ciResultaat").val(value);
	}

	function moveEditorToCell(cell, doAnimate) {
		if (cell.data("toetsInfo") == undefined)
			return;

		var toetsInfo = $(cell.data("toetsInfo").resolveId).data("toetsInfo");
		var selectCell = cell;
		if (editorInfo.multiToets) {
			var moveToFirstPoging;
			if (!editorInfo.edit)
				moveToFirstPoging = !selectedCell
						|| selectedCell.prevAll().size() != cell.prevAll()
								.size();
			else
				moveToFirstPoging = cell.data("toetsInfo").poging == 0;

			if (moveToFirstPoging) {
				var index = toetsInfo.alternatief ? 2 : 1;
				while (toetsInfo.cells[index] == null
						&& toetsInfo.cells.length >= index)
					index++;
				if (toetsInfo.cells.length <= index)
					index = 0;
				selectCell = $(toetsInfo.cells[index]);
			}
		}
		var pogingInfo = selectCell.data("toetsInfo");
		var cellNr = pogingInfo.poging;
		var maxCells = editorInfo.maxCells;
		var hasAlternatief = toetsInfo.alternatief;
		var altAdjust = 0;

		if (hasAlternatief || !editorInfo.alternatief) {
			altAdjust = 1;
			if (cellNr == -1)
				cellNr = 1;
			else if (cellNr > 0) {
				if (editorInfo.alternatief)
					cellNr++;
			}
		}

		var pos = cell.position();

		var left = pos.left + editorTable.offsetParent().scrollLeft();
		var rightPartMarginLeft = cellNr * -60 - 6;
		var rightPartLeft = left + cell.outerWidth();
		var rightPartTop = pos.top - 21 + cell.offsetParent().scrollTop();
		var leftPartMarginRight = (maxCells - cellNr - 1 + altAdjust) * -60 - 6;
		var leftPartLeft = left + rightPartMarginLeft - 3;
		var leftPartTop = rightPartTop;
		var editPartTop = leftPartTop + 21;
		var editPartLeft = left;
		var tableStrip = (maxCells - toetsInfo.cellCount + altAdjust) * -60
				+ 10;

		moveColumn(cell, selectCell, doAnimate);

		if (editorInfo.visible) {
			if (cell != selectCell)
				resetColumn(doAnimate);
			animate(leftPartSlider, {
				marginRight : leftPartMarginRight + "px"
			}, doAnimate);
			animate(leftPart, {
				left : leftPartLeft + "px",
				top : leftPartTop + "px"
			}, doAnimate);
			animate(rightPartSliderTable, {
				marginRight : tableStrip + "px"
			}, doAnimate);
			animate(rightPartSlider, {
				marginLeft : rightPartMarginLeft + "px"
			}, doAnimate);
			animate(rightPart, {
				left : rightPartLeft + "px",
				top : rightPartTop + "px"
			}, doAnimate);
			animate(editPart, {
				left : editPartLeft + "px",
				top : editPartTop + "px"
			}, doAnimate);
			animate(alternatiefLabelContainer, {
				width : hasAlternatief ? "60px" : "0px"
			}, doAnimate);
			animate(alternatiefLabel, {
				width : hasAlternatief ? "50px" : "0px"
			});
		} else {
			$(document).trigger("hideCluetip")
			editor.show();
			editorTable.addClass("editorVisible");
			editorInfo.visible = true;
			var leftPartMarginStart = (maxCells + altAdjust - 1) * -60 - 10;
			var rightPartMarginStart = (toetsInfo.cellCount - 1) * -60 - 10;

			var tableStripStart = maxCells * -60;
			leftPartSlider.css( {
				marginRight : leftPartMarginStart + "px"
			});
			leftPart.css( {
				left : left + "px",
				top : leftPartTop + "px"
			});
			rightPartSliderTable.css( {
				marginRight : tableStrip + "px"
			});
			rightPartSlider.css( {
				marginLeft : rightPartMarginStart + "px"
			});
			rightPart.css( {
				left : rightPartLeft + "px",
				top : rightPartTop + "px"
			});
			editPart.css( {
				left : editPartLeft + "px",
				top : editPartTop + "px"
			});
			alternatiefLabelContainer.css( {
				width : hasAlternatief ? "60px" : "0px"
			});
			alternatiefLabel.css( {
				width : hasAlternatief ? "50px" : "0px"
			});

			animate(leftPartSlider, {
				marginRight : leftPartMarginRight + "px"
			}, doAnimate);
			animate(leftPart, {
				left : leftPartLeft + "px",
				top : leftPartTop + "px"
			}, doAnimate);
			animate(rightPartSlider, {
				marginLeft : rightPartMarginLeft + "px"
			}, doAnimate);
		}

		updateEditor(toetsInfo, selectCell, doAnimate);
	}

	function moveEditorToPoging(cell, doAnimate) {
		var pogingInfo = cell.data("toetsInfo");
		if (pogingInfo == null)
			return;

		var toetsInfo = $(pogingInfo.resolveId).data("toetsInfo");
		var cellNr = pogingInfo.poging;
		var maxCells = editorInfo.maxCells;
		var hasAlternatief = toetsInfo.alternatief;
		var altAdjust = 0;

		if (hasAlternatief || !editorInfo.alternatief) {
			altAdjust = 1;
			if (cellNr == -1)
				cellNr = 1;
			else if (cellNr > 0) {
				if (editorInfo.alternatief)
					cellNr++;
			}
		}

		var pos = selectedCell.position();

		var left = pos.left + editorTable.offsetParent().scrollLeft();
		var rightPartMarginLeft = cellNr * -60 - 6;
		var leftPartMarginRight = (maxCells - cellNr - 1 + altAdjust) * -60 - 6;
		var leftPartLeft = left + rightPartMarginLeft - 3;
		var tableStrip = (maxCells - toetsInfo.cellCount + altAdjust) * -60
				+ 10;

		moveColumn(selectedCell, cell, doAnimate);

		animate(leftPartSlider, {
			marginRight : leftPartMarginRight + "px"
		}, doAnimate);
		animate(leftPart, {
			left : leftPartLeft + "px"
		}, doAnimate);
		animate(rightPartSliderTable, {
			marginRight : tableStrip + "px"
		}, doAnimate);
		animate(rightPartSlider, {
			marginLeft : rightPartMarginLeft + "px"
		}, doAnimate);

		updateEditor(toetsInfo, cell, doAnimate);
	}

	function updateEditor(toetsInfo, cell, doAnimate) {
		selectedCell = cell;
		var pogingInfo = cell.data("toetsInfo");
		editor.data("pogingInfo", pogingInfo);
		var newMessage = pogingInfo.message;
		if (newMessage && pogingInfo.immediate) {
			showMessage(newMessage);
		} else {
			hideMessage(doAnimate);
		}

		updateEditorValues(toetsInfo);
		updateDetails(pogingInfo, toetsInfo);

		var value = selectedCell.find("span.ciValueDisplay span").text();
		var disabled = selectedCell.find("input.ciResultaat").is(":disabled");
		editorInputField.val("").val(value).attr("readOnly",
				disabled ? "readonly" : "").focus();
		if (!disabled)
			editorInputField.select();
	}

	function updateEditorValues(toetsInfo) {
		var cell0 = editorTable.find("td" + toetsInfo.cells[0]);
		var row = cell0.parent().parent();
		copyDataToEditor(cell0, editorCell[0], editorDisplay[0], "ciPoging-0");
		editorDisplay[0].text(cell0.find("span.ciValueDisplay span").text());
		if (toetsInfo.alternatief)
			copyDataToEditor(row.find("td" + toetsInfo.cells[1]),
					editorCellAlt, editorDisplayAlt, "ciAlternatief");
		else
			editorDisplayAlt.text("");

		var lastCell = toetsInfo.cellCount - (toetsInfo.alternatief ? 1 : 0);
		for ( var curPoging = 1; curPoging < lastCell; curPoging++) {
			copyDataToEditor(row.find("td"
					+ toetsInfo.cells[toetsInfo.alternatief ? curPoging + 1
							: curPoging]), editorCell[curPoging],
					editorDisplay[curPoging], "ciPoging-" + curPoging);
			editorLabel[curPoging].show();
		}
		for ( var curPoging = lastCell; curPoging <= editorInfo.maxCells + 1; curPoging++) {
			editorLabel[curPoging].hide();
			editorCell[curPoging].attr("class", "ciPoging-" + curPoging);
			editorDisplay[curPoging].text("");
		}
	}

	function copyDataToEditor(tableCell, editorCell, editorDisplay, pogingClass) {
		editorDisplay.text(tableCell.find("span.ciValueDisplay span").text());
		editorCell.attr("class", pogingClass + " " + tableCell.attr("class"))
				.removeClass("ciField").removeClass("ciColumn");
	}

	function closeEditor(event) {
		if (editorInfo.visible && $(event.target).parents("#ui-datepicker-div").length == 0) {
			resetColumn(false);
			editorInfo.visible = false;
			hideMessage(false);
			editor.hide();
			editor.find(".ciDetails").hide()
			editor.find(".ciButtons div").removeClass("ciSelectedButton");
			editorTable.removeClass("editorVisible");
		}
	}

	function animate(obj, options, doAnimate, callback) {
		if (!doAnimate || (jQuery.browser.msie && jQuery.browser.version < 8.0)) {
			obj.css(options);
			if (callback)
				callback();
		} else
			obj.animate(options, 400, "swing", callback);
	}

	function showDetails(event) {
		var button = $(this);
		var detailPanel = editor.find("div.ciDetails");
		var selected = button.hasClass("ciSelectedButton");
		if (selected) {
			button.removeClass("ciSelectedButton");
			detailPanel.slideUp();
		} else {
			editor.find("div.ciButtons div").removeClass("ciSelectedButton");
			button.addClass("ciSelectedButton");
			detailPanel.slideDown();
		}
		var pogingInfo = editor.data("pogingInfo");
		var toetsInfo = $(pogingInfo.resolveId).data("toetsInfo");
		updateDetails(pogingInfo, toetsInfo);
	}

	function updateDetails(pogingInfo, toetsInfo) {
		var button = $("div.ciSelectedButton", editor);
		var details = $("div.ciDetails", editor).empty();

		if (button.is(".ciNote"))
			renderNote(details);
		else if (button.is(".ciHistory"))
			renderHistory(details, pogingInfo.history);
		else if (button.is(".ciInfo"))
			renderInfo(details, toetsInfo.info);
		else if (button.is(".ciTrace"))
			renderTrace(details);
	}

	function renderNote(details) {
		var cellNote = selectedCell.find("input.ciNotitie");
		if (editorInfo.editable) {
			var textarea = $("<textarea cols='50' rows='50'></textarea>");
			textarea.val(cellNote.val());
			details.append(textarea);
			textarea.blur(function() {
				cellNote.val(textarea.val());
			});

			var datumBehaald = selectedCell.find("input.ciDatumBehaald");
			var html = "<table class='tblData movedown_5'><tbody><tr>";
			html += "<td class='tblTag unit_120'>Datum behaald</td>";
			html += "<td class='tblInput'>";
			html += "<input type='text' class='unit_80 noMargin' />";
			html += "</td></tbody></table>";
			details.append(html);
			var datumBehaaldEdit =  details.find("input");
			datumBehaaldEdit.datepicker( {
				duration : 'fast',
				showAnim : 'fadeIn',
				buttonImageOnly : true,
				buttonImage : '../assets/img/icons/calendar2.png',
				showOn : 'button',
				dateFormat : 'dd-mm-yy',
				showButtonPanel : true
			});
			datumBehaaldEdit.val(datumBehaald.val());
			datumBehaaldEdit.change(function() {
				datumBehaald.val(datumBehaaldEdit.val());
			});
		} else {
			details.append($("<span>" + cellNote.val() + "</span>"))
		}
	}

	function renderHistory(details, historyJSON) {
		var html = "<table>";
		html += "<thead><tr><th>Res.</th><th>Ingevoerd door</th><th>Datum</th></thead>";
		html += "<tbody>";
		$.each(historyJSON, function(index, entry) {
			html += "<tr>";
			html += "<td>" + entry.cijfer + "</td>";
			html += "<td>" + entry.ingevoerd + "</td>";
			html += "<td>" + entry.datum + "</td>";
			html += "</tr>";
		});
		html += "</tbody>";
		html += "</table>";
		details.append(html);
	}

	function renderInfo(details, infoJSON) {
		var html = "<table>";
		html += "<tbody>";
		$.each(infoJSON, function(index, entry) {
			html += "<tr>";
			html += "<th>" + entry.label + "</th>";
			html += "<td>" + entry.value + "</td>";
			html += "</tr>";
		});
		html += "</tbody>";
		html += "</table>";
		details.append(html);
	}

	function renderTrace(details) {
		var cellTrace = selectedCell.find("span.ciTrace");
		details.append($("<span>" + cellTrace.html() + "</span>"))
	}
})(jQuery);