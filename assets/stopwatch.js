var t=null, t1=0, ms=0, ts=0, dn=false, interval=100,
	x, y, oSplit, oTag, oTime, splits = "", lap=0;

if (!window.Android) {
	window.Android = {
		save: function(tag,times) { console.log("Save:",tag,times+"ms"); }
	}
}

function init() {
	oSplit = document.getElementById("split");
	oTag   = document.getElementById("tag");
	oTime  = document.getElementById("time");
}

function reset() {
	if (!running() && oTag.innerText)
		Android.save(oTag.innerText,getTimes());
	stop();
	oTime.innerText = "00:00:00";
	oTag.innerText = oSplit.innerText = splits = "";
	ms = t1 = lap = 0;
}

function getTimes() {
	return '' + ms + splits;
}

function tick() {
	oTime.innerHTML = fmtTime(ms + elapsed());
}

function setTag(e) {
	var	s = prompt("Tag", oTag.innerText);
	if (s != null) tag.innerHTML = s;
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
	ms += elapsed();
	t = null;
	setColor('');
}

function split(e) {
	e.stopPropagation();
	e.preventDefault();
	if (running()) {
		var msec = ms + elapsed();
		lap = msec - lap;
		splits = "," + msec + splits;
		oSplit.innerHTML = fmtTime(lap)
			+ "<small>."+Math.round((lap%1000)/100)+"</small><br/>"
			+ "<span style=\"font-size:0.9em\">"+oSplit.innerHTML+"</span>";
		lap = msec;
	}
}

function kDown(e) {
	dn = true;
	x = e.pageX; y = e.pageY;
	ts = e.timeStamp;
	setColor("#997");
}

function kUp(e) {
	e.preventDefault();
	e.stopPropagation();
	dn = false;
	if (Math.abs(e.pageX-x) > 7 || Math.abs(e.pageY-y) > 6)
		return setLastColor();
	x = e.pageX; y = e.pageY;
	if ((e.timeStamp - ts) > 570) {		// Long-press (adjust if necessary
		reset();
	} else {
		ctrl();
	}
}

// Helpers
function elapsed() {
	return new Date().getTime() - t1;
}
function running() {
	return (t != null);
}
function pad2(n) {
  return (n < 10 ? '0' : '') + n;
}
function fmtTime(msec) {
	var now = new Date(msec);
	return pad2(now.getUTCHours())
		+":"+pad2(now.getMinutes())
		+":"+pad2(now.getSeconds());
}
function setColor(color) {
	oTime.style.setProperty("color", color);
}
function setLastColor() {
	setColor(running() ? "#ff9" : "");
	return true;
}
