var t=null, t1=0, ms=0, ts=0, dn=false, interval=100,
	x, y, oSplit, oTag, oTime;

if (!window.Android) {
	window.Android = {
		save: function() { console.log("Save"); }
	}
}

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
		oTag.innerHTML = '';
	}
	oTime.innerText = "00:00:00";
	oTag.innerText = oSplit.innerText = "";
	ms = t1 = 0;
}

function tick() {
	var now = new Date(ms + elapsed());
	oTime.innerHTML = fmtTime(now.getUTCHours(),now.getMinutes(),now.getSeconds(),now.getMilliseconds()%10);
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
	setColor("");
}

function split(e) {
	e.stopPropagation();
	e.preventDefault();
	//var s = oTime.innerText.replace(/00:/g,'');
	if (running()) {
		//oSplit.innerHTML += oTime.innerText + "<small>."+((ms+elapsed()) % 10)+"</small><br/>";
		oSplit.innerHTML = oTime.innerText + "<small>."+((ms+elapsed()) % 10)+"</small><br/>"
			+ oSplit.innerHTML;
		//oSplit.innerHTML += s.replace(/^0/,'') + "<small>."+((ms+elapsed()) % 10)+"</small><br/>";
	}
}

function kDown(e) {
	//e.preventDefault();
	//e.stopPropagation();
	dn = true;
	x = e.pageX; y = e.pageY;
	ts = e.timeStamp;
	setColor("#997");
}

function kUp(e) {
	e.preventDefault();
	e.stopPropagation();
	dn = false;
	if (Math.abs(e.pageX-x) > 3 || Math.abs(e.pageY-y) > 3)
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
function fmtTime(h,m,s) {
	return pad2(h)+':'+pad2(m)+':'+pad2(s);
}
function setColor(color) {
	oTime.style.setProperty("color", color);
}
function setLastColor() {
	setColor(running() ? "#ff9" : "");
	return true;
}
