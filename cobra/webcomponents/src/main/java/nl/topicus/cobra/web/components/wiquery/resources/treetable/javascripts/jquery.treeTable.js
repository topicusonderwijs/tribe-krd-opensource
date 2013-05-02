/* jQuery treeTable Plugin 2.2.1 - http://ludo.cubicphuse.nl/jquery-plugins/treeTable/ */
( function($) {
	// Helps to make options available to all functions
	// TODO: The options shouldn't be global to all these instances!
	var options;

	$.fn.treeTable = function(opts) {
		options = $.extend( {}, $.fn.treeTable.defaults, opts);

		return this.each( function() {
			$(this).addClass("treeTable").children("tbody").children("tr").each( function() {
				initialize($(this));
			});
		});
	};

	$.fn.treeTable.defaults = {
		childPrefix : "child-of-",
		indent : 19,
		initialState : "collapsed",
		treeColumn : 0,
		callback : null
	};

	// Recursively hide all node's children in a tree
	$.fn.collapse = function() {
		childrenOf($(this)).each( function() {
			if ($(this).is(".expanded.parent")) {
				$(this).collapse();
			}

			$(this).hide();
		});
		return this;
	};

	// Recursively show all node's children in a tree
	$.fn.expand = function() {
		childrenOf($(this)).each( function() {
			if ($(this).is(".expanded.parent")) {
				$(this).expand();
			}

			$(this).show();
		});
		return this;
	};

	// Toggle an entire branch
	$.fn.toggleBranch = function() {
		var expand = $(this).hasClass("collapsed");
		if (expand) {
			$(this).removeClass("collapsed").addClass("expanded").expand();
		} else {
			$(this).removeClass("expanded").addClass("collapsed").collapse();
		}
		if (jQuery.isFunction(options.callback)) {
			options.callback($(this), expand);
		}
		return this;
	};

	// === Private functions

	function ancestorsOf(node) {
		var ancestors = [];
		while (node = parentOf(node)) {
			ancestors[ancestors.length] = node[0];
		}
		return ancestors;
	}

	function isAncestorsOrSelfCollapsed(node) {
		do {
			if (node.hasClass("collapsed"))
				return true;
		} while (node = parentOf(node));
		return false;
	}

	function childrenOf(node) {
		return $("table.treeTable tbody tr." + options.childPrefix + node[0].id);
	}

	function initialize(node) {
		if (!node.hasClass("initialized")) {
			var childNodes = childrenOf(node);

			if (!node.hasClass("parent") && childNodes.length > 0) {
				node.addClass("parent");
			}

			var cell = $(node.children("td")[options.treeColumn]);
			var padding = (ancestorsOf(node).length + 1) * options.indent;
			cell.css("padding-left", padding + "px");
			if (node.hasClass("parent")) {
				cell.prepend('<span style="margin-left: -' + options.indent
						+ 'px; padding-left: ' + options.indent
						+ 'px" class="expander"></span>');
				$(cell[0].firstChild).click( function(event) {
					node.toggleBranch();
					event.stopPropagation();
				});

				// Check for a class set explicitly by the user, otherwise set
				// the default class
				if (!(node.hasClass("expanded") || node.hasClass("collapsed"))) {
					node.addClass(options.initialState);
				}

				if (isAncestorsOrSelfCollapsed(node)) {
					node.collapse();
				}
			}
			node.addClass("initialized");
		}
	}

	function parentOf(node) {
		var classNames = node[0].className.split(' ');

		for (key in classNames) {
			if (classNames[key].match("child-of-")) {
				return $("#" + classNames[key].substring(9));
			}
		}
	}
})(jQuery);