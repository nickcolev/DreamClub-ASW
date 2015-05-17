var t=null, t1=0, ms=0, ts=0, dn=false, interval=100,
	x, y, oSplit, oTag, oTime;

function init() {
	oSplit = document.getElementById("split");
	oTag   = document.getElementById("tag");
	oTime  = document.getElementById("time");
}

function reset(e) {
	var wasStopped = !running();
	stop();
	if (wasStopped && oTag.innerText) {
		Android.save(oTime.innerText, oTag.innerText);
		oTag.innerHTML = "";
	}
	oTime.innerText = "00:00:00";
	oTag.innerText = oSplit.innerText = "";
	ms = t1 = 0;
}

function tick() {
	var dx = ms + new Date().getTime () - t1;
	var dd = Math.floor (dx/1000/60/60/24);
	dx -= dd * 1000*60*60*24;
	var hh = Math.floor (dx/1000/60/60);
	dx -= hh * 1000*60*60;
	var mm = Math.floor (dx/1000/60);
	dx -= mm * 1000*60;
	var ss = Math.floor (dx/1000);
	oTime.innerText = fmtTime(hh,mm,ss);
}

function setTag(e) {
	var	s = prompt("Tag", oTag.innerText);
	if (s) tag.innerHTML = s;
}

function ctrl() {
	if (running())
		stop();
	else
		start ();
}

function start() {
	t1 = new Date().getTime();
	t = setInterval(function(){ tick(); },interval);
	setColor("#ff9");
}

function stop() {
	clearInterval(t);
	t = null;
	setColor("");
}

function split(e) {
	e.stopPropagation();
	e.preventDefault();
	if (running()) oSplit.innerHTML += oTime.innerText + "<br/>";
}

function kDown(e) {
	e.preventDefault();
	e.stopPropagation();
	dn = true;
	x = e.pageX; y = e.pageY;
	ts = e.timeStamp;
	setColor("#997");
}

function kUp(e) {
	e.preventDefault();
	e.stopPropagation();
	dn = false;
	x = e.pageX; y = e.pageY;
	if ((e.timeStamp - ts) > 500) {		// Long-press (adjust if necessary
		reset();
	} else {
		ctrl();
	}
}

// Helpers
function running() {
	return (t != null);
}
function pad2(n) {
  return (n < 10 ? '0' : '') + n;
}
function fmtTime(h,m,s) {
	return pad2(h)+':'+pad2(m)+':'+pad2(s);
}
function setColor(color) {
	oTime.style.setProperty("color", color);
}
