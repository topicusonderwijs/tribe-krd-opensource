function printdiv(pageName)
{
   	var divToPrint=document.getElementById('contBox');
   	var newWin = window.open('','','height=842,width=650');
   	newWin.document.writeln('<HTML>\n<HEAD>');
   	newWin.document.writeln('<TITLE>'+pageName+'</TITLE>');
   	newWin.document.writeln('</HEAD>');
   	newWin.document.write('<link rel="stylesheet" type="text/css" href="../assets/css/print.css" />');
   	newWin.document.writeln('<BODY>');
   	newWin.document.write(divToPrint.innerHTML);
   	newWin.document.writeln('</BODY>');
   	newWin.document.writeln('</HTML>');
   	newWin.document.close();
  	newWin.print();
}
