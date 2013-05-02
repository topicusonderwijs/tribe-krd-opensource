var editors          = new Array();
var currentEditor;
var curItem          = null;
var requestBody      = new UpdateQueue();
var askingUser       = false;
var saveButtons      = [""]; //buttons that don't leave this page

/**
 * Start functie. Deze dient na de load van de pagina aangeroepen te worden.
 */
function startUp()
{
	// voor evt extra dropdown editors
	if (typeof(setupEditors) != 'undefined')
		setupEditors();
	registerEditor(proto('editor'));
	
	var registerContinueListener = function(element) {
                        Event.observe(element, 'click', escapeEditorOnEvent, true);
                    };
	Behaviour.register({'a': registerContinueListener});
	Behaviour.apply();
	Event.observe(document.body, 'click', escapeEditorOnEventAllowSave, true);
	activateEditor(proto('input_0'));	
}

function registerEditor(editor)
{
	editor.style['visibility'] = 'hidden';
	editors.push(editor);
	//GEEN keydown !!, dan werkt de suppressDefaultBehavior voor dropdowns niet meer
	//met keypress werkt het selecteren van de text bij focus niet meer
	Event.observe(editor, 'keypress', arrowListener, true);	
	Event.observe(editor, 'keyup', escapeEditor, true);
	if (editor.nodeName == 'SELECT')
	{
		Event.observe(editor, 'keydown', suppressDefaultBehaviour, true);
		Event.observe(editor, 'keyup', suppressDefaultBehaviour, true);
	}
}
function getEditor(id)
{
	for(var i=0;i<editors.length;i++)
	{
		if(editors[i].id == id)
			return editors[i];
	}
	return null;
}
function showEditor(view, tabindex, fieldType)
{
	prepareEditor(view, getEditor('editor'), tabindex, 10, fieldType);
}
function showDetail(view)
{		
	if(currentEditor && currentEditor.currentView)
		hideEditor(currentEditor);
}
function showDropdown(view, editorId, tabindex)
{
	prepareEditor(view,getEditor(editorId),tabindex,1,null);
}
function prepareEditor(view, editor, tabindex, fieldLength, fieldType)
{
	if (!editor || !view)
		return true;
		
	if(currentEditor && currentEditor.currentView)
		hideEditor(currentEditor);
	if (editor.currentView != null) 
		hideEditor(editor);
		
	editor.tabIndex=tabindex;
	editor.className=view.className;
	editor.fieldType=fieldType;
	editor.currentView = view;
	view.initValue=view.innerHTML;
	if (editor.nodeName == 'TEXTAREA') 
	{
		editor.style['width'] = view.offsetWidth + "px";
		editor.style['height'] = view.offsetHeight + "px";
	}
	if (editor.nodeName == 'SELECT')
	{
		Element.addClassName(editor,'dropdown');
		for(var i=0; i<editor.options.length;i++)
		{
			if(view.innerHTML == editor.options[i].innerHTML)
			{
				editor.selectedIndex=i;
				break;
			}
		}
		editor.previousIndex=editor.selectedIndex;
	}
	else
	{
		editor.value = view.innerHTML;
		if(browser.isIE)
			editor.setAttribute("maxLength",fieldLength);
		else
			editor.setAttribute("maxlength",fieldLength);
	}
	editor.style['visibility'] = 'visible';
	// eerste (deeltoets)kolom kan ingeklapt zijn
	editor.style.display = 'block';
	view.parentNode.replaceChild(editor,view);
	// Belachelijke IE7 fix voor redraw van element
	if(browser.isIE)
	{
		editor.style['visibility'] = 'hidden';
		editor.style['visibility'] = 'visible';
	}
	// Einde fix
	Field.activate(editor);
	currentEditor = editor;
	// gore hack voor IE (aargh): display kan anders grijs worden
	proto('contBox').style['height']='100%';
}
function hasChanged(editor)
{
	if(!editor)
		return false;
	var view =editor.currentView;
	if (editor.nodeName == 'SELECT')
		return editor.options[editor.selectedIndex].text!=view.initValue;
	else
		return editor.value!=view.initValue;
}
function hideEditor(editor)
{
	var view = editor.currentView;
	var changed=false;
	if(!view)
		return;
	if (!editor.abandonChanges && hasChanged(editor))
	{
		if (editor.nodeName == 'SELECT')
		{
			view.innerHTML=editor.options[editor.selectedIndex].innerHTML;
		}
		else
			view.innerHTML = editor.value;
		if(view.initValue!=view.innerHTML)
		{
			registerChanges(editor,view);
			view.style.color = 'black';
			changed=true;
		}
	}
	editor.abandonChanges = false;
	editor.style['visibility'] = 'hidden';
	editor.value = ''; // fixes firefox 1.0 bug
	editor.parentNode.replaceChild(view,editor);
	editor.currentView = null;
	editor.fieldType=null;
	currentEditor=null;
	if (changed)
		saveChanges();
}
function hideEditorOnEvent(event)
{
	hideEditor(currentEditor,null);
}
function escapeEditorOnEvent(event)
{	
	var source = Event.findElement(event, 'a');
	if(event.returnValue==false || source==null || saveButtons.indexOf(source.id)>=0)
		return;
	if(currentEditor)
	{
		if(hasChanged(currentEditor))
		{
			var abandon=false;
			if(confirm('Klik op Ok om door te gaan zonder de wijzigingen op te slaan, klik op Annuleren om de huidige actie af te breken.'))
				abandon=true;
			if(abandon)
			{
				currentEditor.abandonChanges = true;
				hideEditor(currentEditor,null);
			}
			else
			{
				Event.stop(event);
				currentEditor.activate();
				return false;
			}
		}
		else
		{
			currentEditor.abandonChanges = true;
			hideEditor(currentEditor,null);
		}
	}
}
function escapeEditorOnEventAllowSave(event)
{
	if(!event)
		event=window.event;		
	if(askingUser)
		return;
	var source = Event.element(event);
	if(source==null)
		return;
	var asource=Event.findElement(event, 'a');
	if((source.id != null && source.id.indexOf('input_') == 0) || source==currentEditor || source.parentNode==currentEditor)
		return;
	if(currentEditor)
	{
		var abandon=true;
		if(hasChanged(currentEditor))
		{
			askingUser=true;
			if(confirm('Wilt u de laatste wijzigingen opslaan?'))
				abandon=false;
			askingUser=false;
		}
		currentEditor.abandonChanges = abandon;
		hideEditor(currentEditor,null);
	}
}
/**
 * escape editor when esc key is pressed.
 */
function escapeEditor(event)
{
	var editor = Event.element(event);
	if (event.keyCode == Event.KEY_ESC)
	{
		editor.abandonChanges = true;
		hideEditor(editor,null);
		return false;
	}
	return true;
}
function busy()
{
	if(proto('saveDisk'))
	{
		proto('saveDisk').src = '../assets/img/icons/save1.png';
		proto('saveMsg').innerHTML = "Status: opslaan...";
	}
}
function ready(msg, img)
{
	if(!img)
		img='../assets/img/icons/save0.png';
	if(proto('saveDisk'))
	{
		proto('saveDisk').src = img;
		proto('saveMsg').innerHTML = "Status: " + msg;		
	}
}

/**
 * Verwijdert de achtergrondkleur welke aangeeft dat er bij dit cijfer een fout is opgetreden.
 */
function removeHighlight(ids)
{
	if (!ids)
		return;
	for (var i = 0; i < ids.length; i++)
	{
		resolveView(ids[i]).style.background = "";
	}
}

/**
 * Geeft de achtergrond van een cijfer een kleur om aan te geven dat er iets fout is gegaan.
 */
function highlightError(ids) 
{
	if (!ids)
		return;
	for (var i = 0; i < ids.length; i++)
	{
		resolveView(ids[i]).style.background = "red";
	}
}
//
/**
 * wrapper om proto(id) die rekening houdt met de mogelijkheid dat de editor de view vervangen heeft.
 */
function resolveView(id)
{
	if(typeof id=="string")
	{
		var view=proto(id);
		if(view==null)
		{
			if(currentEditor!=null)
				view=currentEditor.currentView;
			else
				alert("kan view niet vinden");
		}
		if(view!=null && view.id!=id)
			alert("zocht view "+id+", vond "+view.id);
		else
			return view;
		return null;
	}
	else
		return id;
}
function updateField(keys, values)
{
	var size = keys.length;
	for (var i = 0; i < size; i++)
	{
		var key   = keys[i];
		var value = values[i];
		
		var component=resolveView(key);
		if (component==null)
			alert("View niet gevonden, key="+key+"\nvalue="+value+"\ncomponent="+component);
		if (component.tagName=='INPUT')
		{
			component.value=value;
		}
		else if (component.tagName=='SELECT')
		{
			for(var j=0;j<component.options.length;j++)
			{
				if(value ==component[j].value)
				{
					component.selectedIndex=j;
					break;
				}
			}
		}
		else
		{
			//update editor if we are editing this field
			if(currentEditor && currentEditor.currentView && currentEditor.currentView.id==key)
			{
				updateField([currentEditor],[value]);
			}
			component.innerHTML=value;
		}
	}
}
function registerChanges(editor, view)
{
	var type=editor.fieldType;
	if(!type)
	{
		if (editor.nodeName == 'SELECT')
			type="label";
		else
			type="double";
	}
	requestBody.add(view.id,editor.value,type);
}
function popChanges()
{
	var temp = requestBody;
	requestBody = new UpdateQueue();
	return temp;
}
function UpdateQueue()
{
	this._components=new Array();
	this._values=new Array();
	this._types=new Array();
	
	if(typeof UpdateQueue._initialized == "undefined")
	{
		UpdateQueue.prototype.add=function(componentId,value,valueType)
		{
			this._components.push(componentId);
			this._values.push(value);
			this._types.push(valueType);
		};
		UpdateQueue.prototype.busy = function()
		{			
			busy();
		};
		UpdateQueue.prototype.ready = function(msg)
		{			
			ready(msg);
		};
		UpdateQueue.prototype.toString = function()
		{
			// request body bouwen
			var string = "";
			var first = true;
			for(var i = 0; i < this._components.length; i++)
			{				
				if (!first)
					string = string + '&';
				else
					first = false;
				string = string + wicketEncode(this._components[i]);
				string = string + '=' + wicketEncode(this._values[i]);
				string = string + ':' + wicketEncode(this._types[i]);
			}
			return string;
		};		
		UpdateQueue.prototype.isEmpty=function()
		{
			return this._components.length==0;
		};
		UpdateQueue._initialized=true;
	}
}

function isVisibleInDocument(element)
{
	if(element==null || element.style==null)
		return true;
	//stopt niet zodra false ontdekt wordt
	return Element.visible(element) && isVisibleInDocument(element.parentNode);
}

/*
 * Stopt default gedrag tenzij de ctrl toets is ingedrukt.
 * Voornamelijk voor navigatie over dropdowns.
 */
function suppressDefaultBehaviour(myEvent)
{
	if(myEvent==null) //null in ie, not in firefox
		myEvent=event;
	if(browser.isGecko||browser.isOpera)
	{
		//workaround for firefox bug: https://bugzilla.mozilla.org/show_bug.cgi?id=291082 opera heeft hetzelfde probleem
		var eventSource=Event.element(myEvent);
		eventSource.previousIndex=eventSource.selectedIndex;
	}
	if(!myEvent.ctrlKey && (myEvent.keyCode==Event.KEY_UP || myEvent.keyCode==Event.KEY_DOWN || myEvent.keyCode==Event.KEY_RETURN || myEvent.keyCode==Event.KEY_LEFT || myEvent.keyCode==Event.KEY_RIGHT))
		Event.stop(myEvent);
}
function arrowListener(myEvent)
{		
	if(myEvent==null) //null in ie, not in firefox
		myEvent=event;	
	if(! (browser.isGecko||browser.isOpera))
	{
		if(myEvent.ctrlKey || myEvent.shiftKey)
			return;
	}
	var eventSource=Event.element(myEvent);
	if(myEvent.keyCode==Event.KEY_UP)
	{
		if(eventSource.type=="select-one" && (browser.isGecko||browser.isOpera))
		{
			if(myEvent.ctrlKey)
			{
				eventSource.previousIndex=eventSource.selectedIndex;
				return true;
			}
			else
				eventSource.selectedIndex=eventSource.previousIndex;
		}
		goUp();
	}
	//not possible to intercept tab
	else if(myEvent.keyCode==Event.KEY_DOWN || myEvent.keyCode==Event.KEY_RETURN)
	{
		if(eventSource.type=="select-one" && (browser.isGecko||browser.isOpera))
		{
			if(myEvent.ctrlKey)
			{
				eventSource.previousIndex=eventSource.selectedIndex;
				return true;
			}
			else
				eventSource.selectedIndex=eventSource.previousIndex;
		}		
		goDown();		
	}
	else if(!myEvent.shiftKey && !myEvent.ctrlKey && (myEvent.keyCode==Event.KEY_LEFT || myEvent.keyCode==Event.KEY_RIGHT))
	{
		//firefox / opera hack undo last selectbox change
		if(eventSource.type=="select-one" && (browser.isGecko||browser.isOpera))
			eventSource.selectedIndex=eventSource.previousIndex;
		if(myEvent.keyCode==Event.KEY_LEFT)
			goLeft();
		else if(myEvent.keyCode==Event.KEY_RIGHT)
			goRight();
	}
	else if(eventSource.type=="select-one" && (browser.isGecko||browser.isOpera) && !myEvent.shiftKey && !myEvent.ctrlKey && myEvent.keyCode!=Event.KEY_LEFT && myEvent.keyCode!=Event.KEY_RIGHT)
	{
		//normale toetsen
		eventSource.previousIndex=eventSource.selectedIndex;
		return true;
	}
}
/**
 * Verplaatst editor naar vorige kolom, wrapped naar de laatste kolom indien nodig.
 */
function goLeft()
{
	if (!currentEditor || !currentEditor.currentView)
		return;		
	var view = currentEditor.currentView;
	var array = view.id.split("_");
	var curIndex = parseInt(array[1]);
	var nextIndex = curIndex - aantalDeelnemers;
	if (nextIndex >= 0)
	{		
		var nextView = proto(array[0] + "_" + nextIndex);	
		activateEditor(nextView);
	}
}
/**
 * Verplaatst editor naar volgende kolom, wrapped naar de eerste kolom indien nodig.
 */
function goRight()
{
	if (!currentEditor || !currentEditor.currentView)
		return;		
	var view = currentEditor.currentView;
	var array = view.id.split("_");
	var curIndex = parseInt(array[1]);
	var nextIndex = curIndex + aantalDeelnemers;
	var nextView = proto(array[0] + "_" + nextIndex);
	if (!nextView)
		nextView = proto(array[0] + "_" + curIndex % aantalDeelnemers);	
	activateEditor(nextView);
}
/**
 * Verplaatst editor naar rij hieronder, wrapped naar bovenste rij indien nodig.
 */
function goDown()
{
	if (!currentEditor || !currentEditor.currentView)
		return;		
	var view = currentEditor.currentView;
	var array = view.id.split("_");
	var curIndex = parseInt(array[1]);
	var nextIndex = curIndex + 1;
	if (nextIndex % aantalDeelnemers == 0)
		nextIndex = curIndex - aantalDeelnemers + 1;		
	var nextView = proto(array[0] + "_" + nextIndex);	
	activateEditor(nextView);
}
/**
 * Veplaatst editor naar de rij hierboven, wrapped naar onderste rij indien nodig.
 */
function goUp()
{
	if (!currentEditor || !currentEditor.currentView)
		return;		
	var view = currentEditor.currentView;
	var array = view.id.split("_");
	var curIndex = parseInt(array[1]);
	var nextIndex = curIndex - 1;
	if (curIndex % aantalDeelnemers == 0)
		nextIndex = curIndex + aantalDeelnemers - 1;		
	var nextView = proto(array[0] + "_" + nextIndex);	
	activateEditor(nextView);
}
/**
 * verbergt de editor. indien nodig worden de wijzigingen van de editor opgeslagen.
 */
function deactivateEditor()
{
	if (currentEditor)
		hideEditor(currentEditor);
}
/**
 * toont de editor op de plek van deze child.
 * @param next de child waar de editor getoond wordt.
 */
function activateEditor(next)
{
	var valid = next!=null && isVisibleInDocument(next) && !next.disabled;
	if(valid && next.onclick)
	{
		next.onclick(); // gaat naar showEditor
		if(currentEditor)
			Field.activate(currentEditor); //nodig voor IE
		return true;
	}
	else 
		return false;
}	

/**
 * Uitklappen van de verticale kolom met een bepaalde name.
 * Alle elementen binnen het tabel element (th, td) worden getoond. 
 */

function klapUit(kolomId)
{
	var elems = document.getElementsByName(kolomId);
	for (var i = 0; i<elems.length; i++)
	{
		recursiveShowHide(elems[i], '');
	}
}	

/**
 * Inklappen van de verticale kolom met een bepaalde name.
 * Alle elementen binnen het tabel element (th, td) worden verborgen. 
 */
function klapIn(kolomId)
{
	var elems = document.getElementsByName(kolomId);
	for (var i = 0; i<elems.length; i++)
	{
		recursiveShowHide(elems[i], 'none');
	}
}

/**
 * Een element tonen/verbergen en alle children tonen/verbergen.
 */ 
function recursiveShowHide(elem, display)
{
	if (!elem || !elem.style)
		return;
	var childs = elem.childNodes;
	for (var j=0; j<childs.length; j++)
	{
		if (childs[j].style)
		{
			recursiveShowHide(childs[j], display);
		}
	}
	elem.style.display = display;
}
