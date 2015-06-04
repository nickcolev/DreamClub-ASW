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

function reset(save) {
	if (!running() && oTag.innerText && save)
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
	var d = new Date(ms + elapsed());
	if (d.getMilliseconds() < 100)
		oTime.innerHTML = fmtTime(d);
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
	e.preventDefault();
	e.stopPropagation();
	if (running()) {
		var msec = ms + elapsed();
		lap = msec - lap;
		splits = "," + msec + splits;
		var d = new Date(lap);
		var decs = Math.round(d.getMilliseconds() / 100);
		if (decs == 10) decs = 9;	// skew, but otherwise .10 has been shown
		oSplit.innerHTML = fmtTime(d)
			+ "<small>."+decs+"</small><br/>"
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
	//e.stopPropagation();
	dn = false;
	if (Math.abs(e.pageX-x) > 8 || Math.abs(e.pageY-y) > 7)
		return setLastColor();
	x = e.pageX; y = e.pageY;
	if ((e.timeStamp - ts) > 580) {		// Long-press (adjust if necessary
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
function fmtTime(date) {
	return pad2(date.getUTCHours())
		+":"+pad2(date.getMinutes())
		+":"+pad2(date.getSeconds());
}
function setColor(color) {
	oTime.style.setProperty("color", color);
}
function setLastColor() {
	setColor(running() ? "#ff9" : "");
	return true;
}
