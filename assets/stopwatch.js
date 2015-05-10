var t, t1, t2, run=false, ms=0, ts=0, dn=false,
	x, y, oSplit, oTag, oTime;

function init() {
	oSplit = document.getElementById("split");
	oTag   = document.getElementById("tag");
	oTime  = document.getElementById("time");
}

function reset(e) {
	var wasStopped = !run;
	stop(e);
	ms = 0;
	if (wasStopped && oTag.innerText) {
		Android.save(oTime.innerText, oTag.innerText);
		oTag.innerHTML = "";
	}
	oTime.innerText = "00:00:00";
	oTag.innerText = oSplit.innerText = "";
}

function tick(e) {
	t2 = new Date ();
	var dx = ms + t2.getTime() - t1.getTime();
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

function ctrl (e) {
	if (run)
		stop (e);
	else
		start (e);
}

function start (e) {
	t1 = new Date ();
	t = setInterval(function(){ tick(e); },1000);
	setColor("#ff9");
	run = true;
}

function stop (e) {
  clearInterval(t);
  if (t2) ms += t2.getTime() - t1.getTime();
  setColor("");
  run = false;
}

function split(e) {
	e.stopPropagation();
	e.preventDefault();
	if (run) oSplit.innerHTML += oTime.innerText + "<br/>";
}

function kDown(e) {
	e.preventDefault();
	e.stopPropagation();
	dn = true;
	x = e.pageX; y = e.pageY;
	ts = e.timeStamp;
	setColor("#776");
}

function kUp(e) {
	e.preventDefault();
	e.stopPropagation();
	dn = false;
	x = e.pageX; y = e.pageY;
	if ((e.timeStamp - ts) > 500) {		// Long-press (adjust if necessary
		reset(e);
	} else {
		ctrl(e);
	}
}

// Helpers
function pad2(n) {
  return (n < 10 ? '0' : '') + n;
}
function fmtTime(h,m,s) {
	return pad2(h)+':'+pad2(m)+':'+pad2(s);
}
function setColor(color) {
	oTime.style.setProperty("color", color);
}
