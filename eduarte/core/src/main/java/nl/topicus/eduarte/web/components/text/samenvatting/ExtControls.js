,
alertSep3 : { separator : true },
zinToevoegen : {
	visible   : true,
	exec      : function(){ 

	var oldContent = $( $(this.editor).document() ).find('body').html();
	var newContent = $('#${comboboxid}').children("option:selected").text();
	if ( newContent != '[Maak uw keuze]')
	{
		$( $(this.editor).document() ).find('body').html(oldContent + newContent + '<br/>');
		if ( this.original )
		{
			var content = $( $(this.editor).document() ).find('body').html();
			if ( this.options.rmUnwantedBr )
			{
				content = ( content.substr(-4) == '<br>' ) ? content.substr(0, content.length - 4) : content;
			}
			$(this.original).val(content);
		}
	}
},
className : 'zinToevoegen',
title : 'Geselecteerde zin toevoegen'
}