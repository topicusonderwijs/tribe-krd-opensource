/**
 * Dit javascript zorgt ervoor dat op een textarea een iframe gezet wordt waar in een rich text editor 
 * text bewerken met html opmaak
 */
{
	controls : {
	/**
	 * Standaard functionalitijd instellen:
	 * 	-	instellen welke knoppen er weergegeven moeten worden
	 *  - 	Title meegeven voor de tooltip 
	 *      	(anders wordt standaard de knop naam gebruikt)
	 *  - 	separatoren zijn de verticale schijdings tekens in de balk
	 *  
	 *  Voor alle mogelijke standaardt functinalitieit kijk in het 
	 *  jquery.wysiwyg.js onder TOOLBAR
	 * 
	 */

	//knoppen verbergen
	insertImage 	: { visible : false},
	createLink 		: { visible : false},
	separator05 	: { visible : false},
	separator06 	: { visible : false},
	h1mozilla 		: { visible : false},
	h2mozilla 		: { visible : false},
	h3mozilla 		: { visible : false},
	h1 				: { visible : false},
	h2 				: { visible : false},
	h3 				: { visible : false},
	removeFormat	: { visible : false},
	separator09 	: { visible : false},
	undo			: { visible : false},
	redo			: { visible : false},
	separator07 	: { visible : false},
	cut   			: { visible : false},
	copy  			: { visible : false},
	paste 			: { visible : false},
	increaseFontSize: { visible : false},	
	decreaseFontSize: { visible : false},
	separator08		: { visible : false},

	//knoppen weergeven
	strikeThrough 	: { visible : true , title : 'doorstreepen'},
	underline     	: { visible : true , title : 'onderstreepen'},

	//Title meegeven
	bold			: { title : 'vet gedrukt'},
	italic			: { title : 'schuin gedrukt'}, 
           			
	/**
	 * Eigen functionalitijd instellen:
	 * 							
	 * Een eigen knop bestaat uit de volgende onderdelen:
	 * 
	 * de seperator is optioneel en kan een verticale lijn tonen
	 * separatornNaam : { 
	 * 		separator : [true of false],
	 * 		visible   : [true of false],
	 * }, 
	 * 
	 * 	[functie naam] : 
	 * 	{
	 *  	visible   : [true of false],
	 *   	exec      : function() 
	 *   				{
	 *   					[de javascript/jquery functie die
	 *   					 uitgevoerd moet worden] 
	 *   				},
	 *     	className : '[css class naam]',
	 *      title 	  : '[tooltip]'
	 *   },
	 * 
	 * Voorbeeld:
	 *  
	 *	clearField 	: {
	 *   	visible   : false,
	 *   	exec      : function() 
	 *   					{ 
	 *   						$('#${textAreaId}').wysiwyg('clear');
	 *   					},
	 *   	className : 'removeFormat'
	 *   },
	 * 
	 * het is ook mogelijk om een functie alleen voor een bepaalde browser of juist niet
	 * voor een bepaalde browser te maken.
	 * 
	 * in het volgende voorbeeld wordt visible op true gezet behalve voor apple safari 
	 * visible : true && !($.browser.safari)
	 * 
	 * mogelijke browsers zijn: 
	 * 	AOL Explorer 		- $.browser.aol       
	 *	Camino    			- $.browser.camino    
	 * 	Firefox  			- $.browser.firefox 
	 *	Flock    			- $.browser.floc    
	 *	iCab      			- $.browser.icab   
	 *	Konqueror 			- $.browser.konqueror
	 *	Mozilla  			- $.browser.mozilla 
	 *	Internet Explorer	- $.browser.msie  
	 *	Netscape 			- $.browser.netscape
	 *	Opera  				- $.browser.opera 
	 *	Safari    			- $.browser.safari   
	 * 
	 * Vergeet niet de css class naam niet toe tevoegen aan de jquery.wysiwyg.css
	 * over het algemeen bevat deze css alleen een image voor de knop
	 * Voorbeeld:
	 * .fullscreen { background: url('../../img/jquery/jquery.wysiwyg.fullscreen.png') 
	 * 	no-repeat !important; }
	 */  			
            			
	//omdat IE in de standaart functionalitiet anders omgaat 
	//met het het vergroten en verkeinen van text dan FF is 
	//er een functie gemaakt die in zowel FF als IE het zelfe
	//werkt. De font grotes gaan van 1 t/m 7;

	incrFontSizeSep : {
		separator : true,
		visible : true && !($.browser.safari)
	},			        

	increasIefontSize :{ 
		visible : true && !($.browser.safari),
		exec : function()
		{
		var oldFontSize = parseInt(this.editorDoc.queryCommandValue('fontSize'));
		if (isNaN(oldFontSize))
			oldFontSize = 3;
		if (oldFontSize < 7)					        	
		{

			var newFontSize = oldFontSize + 1;
			this.editorDoc.execCommand('fontSize', false, [newFontSize]);
		}
		},
		className : 'increaseFontSize',
		title : 'geselecteerde text vergroten'
	},


	decreasIefontSize :{ 
		visible : true && !($.browser.safari),
		exec : function()
		{
		var oldFontSize = parseInt(this.editorDoc.queryCommandValue('fontSize'));
		if (isNaN(oldFontSize))
			oldFontSize = 3;
		if (oldFontSize > 1)					        	
		{
			var newFontSize = oldFontSize - 1;
			this.editorDoc.execCommand('fontSize', false, [newFontSize]);
		}
		},
		className : 'decreaseFontSize',
		title : 'geselecteerde text verkleinen'
	},




	/* 
	 * de standaart jwysiwyg heeft geen mogelijkheid om
	 * om de text editor fullschreen te maken.
	 * deze functie vergroot de div waar de editor
	 * de header, menu en footer blijven wel zichtbaar
	 */


	fullScreenSep : { separator : true },
	fullScreen : {
		visible   : true,
		exec      : function() {
			var panel = $(this.element);
			var container = panel.find('.wysiwyg-iframe-container');
			var curSize = container.outerHeight();
			if (panel.hasClass('wysiwyg_large')) {
				panel.removeClass('wysiwyg_large');
				container.css({height: (curSize-300)+'px'});
			} 
			else {
				panel.addClass('wysiwyg_large');
				container.css({height: (curSize+300)+'px'});
			}
		},
		className : 'fullscreen',
		title : 'maximalizeren'
	},


	// Om het hele textarea leeg te gooien roept deze functue.wysiwyg('clear') aan
	clearSep 	: { 
		visible   : false,
		separator : true 
	},
	clearField 	: {
		visible   : false,
		exec      : function() { 
		$('#${textAreaId}').wysiwyg('clear');
	},
	className : 'removeFormat'
	}


	/**
	 * SamenvattingTextAreaPanel extend van textArea panel
	 * extraControls variable wordt gebruikt om knoppen voor
	 * specifieke gevallen toe te voegen 
	 */

	${extraControls}
}}