var t, ms=0, ts=0, dn=false, interval=250,
	x, y, oSplit, oTag, oTime;

var cookie = {
	getCookie: function(name) {
		var re = new RegExp(name+"=(\\w+)");
		try {
			var m = re.exec(document.cookie);
			return m[1];
		} catch(e) {
		}
	},
	getBool: function(name) {
		var s = this.getCookie(name);
		if (!s) return false;
		return (s == "true" ? true : false);
	},
	getInt: function(name) {
		var s = this.getCookie(name);
		if (!s) return 0;
		if (isNaN(s)) return 0;
		return parseInt(s);
	}
};

function init() {
	oSplit = document.getElementById("split");
	oTag   = document.getElementById("tag");
	oTime  = document.getElementById("time");
	// Restore status from cookies
	ms = cookie.getInt("ms");
	ts = cookie.getInt("ts");
	tick();
	if (ts > 0) {
		ms += new Date().getTime() - ts;
		start();
	}
}

function onBackPressed() {
	setCookie("ms",ms);
	if (running()) {	// Set cookies
		setCookie("ts",new Date().getTime());
	} else {			// Clear cookies
		setCookie("ts");
	}
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
	ms = 0;
}

function tick() {
	ms += interval;
	//if ((ms % 1000) < 1000) return;
	var dx = ms;
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
function setCookie(name,value) {
	if (value)
		document.cookie = name+"="+value;
	else	// Delete
		document.cookie = name+"=; expires=Thu, 01 Jan 1970 00:00:00 UTC";
}
