var t=null, t1=0, ms=0, ts=0,
	x1,y1,oSplit, oTag, oTime, splits='', lap=0, cnt=0;

if (!window.Android) {
	window.Android = {
		save: function(tag,times) { console.log("Save:",tag,times+"ms"); }
	}
}

function ctrl() {
	if (running())
		stop();
	else
		start ();
}

function getTimes() {
	return '' + ms + splits;
}

function init() {
	oSplit = document.getElementById("split");
	oTag   = document.getElementById("tag");
	oTime  = document.getElementById("time");
}

function kDown(e) {
	e.preventDefault();
	x1 = e.pageX;
	y1 = e.pageY;
	ts = e.timeStamp;
	setColor("#aaa");
}

function kUp(e) {
	e.preventDefault();
	// Move?
	if (Math.abs(x1-e.pageX) > 5 || Math.abs(y1-e.pageY) > 5) return;
	if ((e.timeStamp - ts) > 560) {		// Long-press (adjust if necessary)
		reset(true);
	} else {
		ctrl();
	}
}

function reset(save) {
	if (!running() && oTag.innerText && save)
		Android.save(oTag.innerText,getTimes());
	stop();
	oTime.innerHTML = "<span>00:00:00</span><small>.0</small>";
	oTag.innerText = oSplit.innerText = splits = "";
	ms = t1 = lap = 0;
}

function split(e) {
	if (running()) {
		var msec = ms + elapsed();
		lap = msec - lap;
		splits = ',' + msec + splits;
		var d = new Date(lap);
		var decs = Math.round(d.getMilliseconds() / 100);
		if (decs == 10) decs = 9;	// skew, but otherwise .10 has been shown
		oSplit.innerHTML = fmtTime(d) + "<small>."+decs+"</small><br/>"
			+ "<span style=\"font-size:0.9em\">"+oSplit.innerHTML+"</span>";
		lap = msec;
	}
}

function setTag(e) {
	var	s = prompt("Tag", oTag.innerText);
	if (s != null) tag.innerHTML = s;
}

function start() {
	t1 = new Date().getTime();
	t = setInterval(tick,100);
	setColor("#ff9");
}

function stop() {
	clearInterval(t);
	ms += elapsed();
	t = null;
	setColor('');
}

function tick() {
	oTime.children[1].innerText = "."+cnt;
	if (++cnt > 9) cnt = 0;
	var d = new Date(ms + elapsed());
	if (d.getMilliseconds() < 100)
		oTime.children[0].innerText = fmtTime(d);
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
