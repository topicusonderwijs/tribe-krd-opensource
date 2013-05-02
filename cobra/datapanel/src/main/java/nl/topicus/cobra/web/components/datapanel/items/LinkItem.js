function linkItemDoClick(event,url, srcElement)
{
	if (!event)
		event = window.event;
	var eventSource=event.target;
	if(event.srcElement)
		eventSource=event.srcElement;
	if(eventSource==srcElement || (eventSource.tagName!="A" && !eventSource.onclick))
		window.location=url;
}