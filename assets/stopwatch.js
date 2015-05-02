var t, t1, t2, o, run = false, ms = 0, x, y, b, dn = false, tag, long = 500;
function about () {
  alert ("(c) vera5.com, 2011");
}
function ctrl () {
  if (run)
    stop ();
  else
    start ();
}
function init () {
  o = document.getElementById ('t');
}
function pad2 (n) {
  return (n < 10 ? '0' : '') + n;
}
function reset () {
  stop ();
  ms = 0;
  var tag = document.getElementById("tag");
  if (tag.innerHTML != "&nbsp;") {
	  Android.save(tag, o.innerHTML);
	  tag.innerHTML = "&nbsp;";
  }
  o.innerHTML = "00:00:00";
}
function tick () {
  t2 = new Date ();
  var dx = ms + t2.getTime () - t1.getTime ();
  var dd = Math.floor (dx/1000/60/60/24);
  dx -= dd * 1000*60*60*24;
  var hh = Math.floor (dx/1000/60/60);
  dx -= hh * 1000*60*60;
  var mm = Math.floor (dx/1000/60);
  dx -= mm * 1000*60;
  var ss = Math.floor (dx/1000);
  o.innerHTML = pad2 (hh) + ':' + pad2 (mm) + ':' + pad2 (ss);
}
function stop () {
  clearInterval (t);
  ms += t2.getTime () - t1.getTime ();
  o.title = "Start";
  o.style.color = "";
  run = false;
}
function start () {
  t1 = new Date ();
  t = setInterval ("tick()", 200);
  o.title = "Stop";
  o.style.color = "#ff7";
  run = true;
}
// https://developer.mozilla.org/en-US/docs/Web/API/Touch_events
function kDown(e) {
	dn = true;
	if (!e) e = window.event;
	x = e.pageX;
	y = e.pageY;
	b = new Date().getTime();
	o.style.color = "#776";
	o.addEventListener("mousemove", function(e){
		if (dn) alert("move dx="+(x - e.pageX)+" dy="+(y - e.pageY));
	}, true);
	return true;
}
function kUp(e) {
	dn = false;
	if (!e) e = window.event;
	o.removeEventListener("move", this, false);
	o.style.color = "#ff7";
	if ((new Date().getTime() - b) > long) {	// Long-press
		var s = prompt("Tag", tag);
		if (s) document.getElementById("tag").innerHTML = s;
		//alert("Long-press detected\nx: "+x+"->"+e.pageX+"\ny: "+y+"->"+e.pageY+"\nt: "+(new Date().getTime() - b));
	} else
		ctrl();
}
