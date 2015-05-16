var t, t1, t2, run=false, ms=0, ts=0, dn=false, dx=0, interval=1000,
	x, y, oSplit, oTag, oTime;

var cookie = {
	run: false,
	ms: 0,
	t1: 0,
	t2: 0,
	getCookie: function(name) {
		var a = Array(),
			cookies = document.cookie.split(";");
		for (var i=0; i<cookies.length; i++) {
			a = cookies[i].split("=");
			if (a[0] === name) return a[1];
		}
		return null;
	},
	getBool: function(name) {
		var s = this.getCookie(name);
		if (s == null) return false;
		return (s == "true" ? true : false);
	},
	getInt: function(name) {
		var s = this.getCookie(name);
		if (s != null) return parseInt(s);
	}
};

function init() {
	oSplit = document.getElementById("split");
	oTag   = document.getElementById("tag");
	oTime  = document.getElementById("time");
	if (cookie.getBool("run")) {
		// Restore cookies
		ms = cookie.getInt("ms"));
		start();
	} else alert(document.cookie);
}

function onBackPressed() {
	setCookie("run",run);
	setCookie("ms",ms);
	setCookie("t1",t1);
	setCookie("t2",t2);
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

function tick() {
	t2 = new Date().getTime();
	dx = ms + t2 - t1;
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
	if (run)
		stop ();
	else
		start ();
}

function start() {
	t1 = new Date().getTime();
	t = setInterval(function(){ tick(); },interval);
	setColor("#ff9");
	run = true;
}

function stop() {
  clearInterval(t);
  if (t2) ms += t2 - t1;
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
function setCookie(name,value) {
	if (value === undefined)	// Delete
		document.cookie = name+"=; expires=Thu, 01 Jan 1970 00:00:00 UTC";
	else
		document.cookie = name+"="+value;
}

function getCookie4(name) {
	var a = Array(),
		cookies = document.cookie.split(";"),
		value = null;
	for (var i=0; i<cookies.length; i++) {
		a = cookies[i].split("=");
		if (a[0] === name) return a[1];
	}
	return null;
}
