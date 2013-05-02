( function($) {
	var orderSelectContainer = "";
	
	jQuery.fn.orderSelect = function(params) {
		$("#"+params.containerId+" ul.os-orderselect").sortable(
				{
					cursor : "move",
					scrollSpeed : 20,
					connectWith : [ "#" + params.availableId,
							"#" + params.selectedId ],
					stop : updateSelectionValue
				}).disableSelection();

		$("#"+params.containerId+" ul.os-orderselect li .os-up").click( function() {
			var li = $(this).parent("li");
			var prev = li.prev();
			if (prev.length > 0) {
				li.insertBefore(prev);
				updateSelectionValueInternal(li.closest("#"+params.containerId));
			}
		});
		$("#"+params.containerId+" ul.os-orderselect li .os-down").click( function() {
			var li = $(this).parent("li");
			var next = li.next();
			if (next.length > 0) {
				li.insertAfter(next);
				updateSelectionValueInternal(li.closest("#"+params.containerId));
			}
		});
		$("#"+params.containerId+" ul.os-orderselect li .os-add").click( function() {
			var li = $(this).parent("li");
			$("#"+params.containerId+" .os-selection").append(li);
			updateSelectionValueInternal(li.closest("#"+params.containerId));
		});
		$("#"+params.containerId+" ul.os-orderselect li .os-remove").click( function() {
			var li = $(this).parent("li");
			$("#"+params.containerId+" .os-available").append(li);
			updateSelectionValueInternal(li.closest("#"+params.containerId));
		});
	}

	function updateSelectionValue() {
		updateSelectionValueInternal($(this).parent());
	}

	function updateSelectionValueInternal(root) {
		root.find("input").val(
				root.find(".os-selection").sortable("toArray"));
	}
})(jQuery);