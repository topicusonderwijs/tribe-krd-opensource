/*
* @Copyright (c) 2009 Aurélio Saraiva (aureliosaraiva@gmail.com)
* @Page http://github.com/plentz/jquery-maskmoney
* try at http://inovaideia.com.br/maskInputMoney/

* Special thanks to Raul Pereira da Silva (contato@raulpereira.com) and Diego Plentz (http://plentz.org)

* Permission is hereby granted, free of charge, to any person
* obtaining a copy of this software and associated documentation
* files (the "Software"), to deal in the Software without
* restriction, including without limitation the rights to use,
* copy, modify, merge, publish, distribute, sublicense, and/or sell
* copies of the Software, and to permit persons to whom the
* Software is furnished to do so, subject to the following
* conditions:
* The above copyright notice and this permission notice shall be
* included in all copies or substantial portions of the Software.
*
* THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
* EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
* OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
* NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
* HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
* WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
* FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
* OTHER DEALINGS IN THE SOFTWARE.
*/

/*
* @Version: 0.3
* @Release: 2009-10-29
* 
* @author schimmel
* 
* Wijzigingen aangebracht!
* Gebruikt voor de financiele module. Wijzigingen zorgen voor een beperkte input en dwingen het correcte formaat af.
* Originele functionaliteit bepertke ook het gebruik van de arrow-keys, en backspace op cursor locatie
*/
(function($) {
	$.fn.maskMoney = function(settings) {
		settings = $.extend( {
			symbol : 'US$',
			decimal : ',',
			precision : 2,
			thousands : '.',
			allowZero : false,
			showSymbol : false
		}, settings);
		settings.symbol = settings.symbol + ' ';
		return this.each(function() {
			var input = $(this);
			function keypressEvent(e) {
				var selection = input.getSelection();
				e = e || window.event;
				var k = e.charCode || e.keyCode || e.which;
				//backspace, tab, left-arrow, right-arrow
				if (k == 8 || k == 9 || k == 37 || k == 39) {
					return true;
				}
				//comma and dot
				else if (k == 44 || k == 46) {
					preventDefault(e);
					if (input.val().indexOf(',') >= 0) {
						return false;
					}
					var start = selection.start;
					if (start > input.val().length-3) {
						var bstring = input.val().substring(0, start);
						var cstring = input.val().substring(start);
						input.val(bstring + ',' + cstring);
						return true;	
					}
				}
				//dash toggle
				else if (k == 45) {
					preventDefault(e);
					if (input.val().indexOf('-') != -1) {
						input.val(input.val().substring(1));
						return true;
					}
					input.val('-' + input.val());
					return true;
				}
				//everything but numbers
				else if (k < 48 || k > 57) {
					preventDefault(e);
					return true;
				} else if (input.val().length == input.attr('maxlength')) {
					return false;
				} else {
					//number
					//insert always, except if there are 2 numbers after the comma already, and we try to insert there
					var start = selection.start;
					var end = selection.end;
					var commapos = input.val().length-3;
					if (input.val().charAt(commapos) == ',' && (start > commapos || end > commapos)) {
						return false;
					} else {
						return true;
					}
				}
			}
			function focusEvent(e) {
				if (input.val() == '') {
					input.val(setSymbol());
				} else {
					input.val(setSymbol(input.val()));
				}
				if (this.createTextRange) {
					var textRange = this.createTextRange();
					textRange.collapse(false);
					textRange.select();
				}
			}
			function blurEvent(e) {
				if ($.browser.msie) {
					keypressEvent(e);
				}
				input.val(input.val().replace(settings.symbol, ''));
			}
			function preventDefault(e) {
				if (e.preventDefault) {
					e.preventDefault();
				} else {
					e.returnValue = false
				}
			}
			
			function setSymbol(v) {
				if (settings.showSymbol) {
					return settings.symbol + v;
				}
				return v;
			}
			
			input.bind('keypress', keypressEvent);
			input.bind('blur', blurEvent);
			input.bind('focus', focusEvent);
			input.one('unmaskMoney', function() {
				input.unbind('focus', focusEvent);
				input.unbind('blur', blurEvent);
				input.unbind('keypress', keypressEvent);
				if ($.browser.msie) {
					this.onpaste = null;
				} else if ($.browser.mozilla) {
					this.removeEventListener('input', blurEvent, false);
				}
			});
		});
	}
	$.fn.unmaskMoney = function() {
		return this.trigger('unmaskMoney');
	};
})(jQuery);