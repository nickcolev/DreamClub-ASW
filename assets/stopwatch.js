var t, t1, t2, o, run = false, ms = 0, b, dn = false, long = 500,
	x, y, ts;

var sw = {	// Future development
	ts: 0,	// time stamp
	t: 0,
	run: false,
	ms: 0,
	x: 0,
	y: 0,
	start: function(e) { },
	stop: function(e) { }
}

function init() {
}

function reset(e) {
	var wasStopped = !run;
	stop(e);
	ms = 0;
	var time = getTarget(e),
		tag = time.parentElement.previousElementSibling.firstElementChild;
	if (wasStopped && tag.innerHTML != "&nbsp;") {
		Android.save(time.innerText, tag.innerText);
		tag.innerHTML = "&nbsp;";
	}
	time.innerHTML = "00:00:00";
}
function tick(e) {
	t2 = new Date ();
	var dx = ms + t2.getTime() - t1.getTime();
	dx = ms + t2.getTime() - e.timeStamp;
	var dd = Math.floor (dx/1000/60/60/24);
	dx -= dd * 1000*60*60*24;
	var hh = Math.floor (dx/1000/60/60);
	dx -= hh * 1000*60*60;
	var mm = Math.floor (dx/1000/60);
	dx -= mm * 1000*60;
	var ss = Math.floor (dx/1000);
	setValue(e,fmtTime(hh,mm,ss));
}

function setTag(e) {
	var	tag = getTarget(e),
		old = tag.innerHTML.replace(/^&nbsp;/, ""),
		s = prompt("Tag", old);
	if (s) tag.innerHTML = s;
}

function ctrl (e) {
  if (run)
    stop (e);
  else
    start (e);
}

function start (e) {
	ts = e.timeStamp;
	t1 = new Date ();
	t = setInterval(function(){ tick(e); },200);
	setColor(e,"#ff7");
	run = true;
}

function stop (e) {
  clearInterval(t);
  if (t2) ms += t2.getTime() - t1.getTime();
  setColor(e,"");
  run = false;
}

function kDown(e) {
	e.preventDefault();
	dn = true;
	x = e.pageX; y = e.pageY;
	b = new Date().getTime();
	setColor(e,"#776");
}

function kUp(e) {
	e.preventDefault();
	document.removeEventListener("mousemove");
	dn = false;
	setColor(e,"#ff7");
	if ((new Date().getTime() - b) > long) {	// Long-press
		testOut("Long-press detected: "+(new Date().getTime() - b)+"ms");
		reset(e);
		x = e.pageX; y = e.pageY;
	} else {
		testOut("&nbsp;");
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
function getTarget(e) {
	if (e.target.style) return e.target;
	if (e.currentTarget) return e.currentTarget;
}
function setColor(event,color) {
	getTarget(event).style.color = color;
}
function setValue(obj,value) {
	if (obj.target.nodeValue) obj.target.nodeValue = value;
	else if (obj.target.innerText) obj.target.innerText = value;
}

// Test
function testOut(s) {
	var test = document.getElementById("test");
	test.innerHTML = s;
}
function props(obj) {
	var s = "", v;
	for(var p in obj)
		if(obj[p]) {
			if(typeof obj[p] === "function")
				s += p+"()<br/>";
			else {
				v = obj[p].toString().replace(/</, "&lt;");
				v = v.replace(/>/, "&gt;");
				s += p+"="+v+"<br/>";
			}
		}
	testOut(s);
}
if (!window.Android) {
	var Android = {
		save: function(time,tag) { console.log(time,tag); },
		Tooltip: function(msg) { console.log(msg); }
	}
}

var mx, my;
function kmDown(e) {
	e.preventDefault();
	mx = e.pageX; my = e.pageY;
	//console.log(e);
}
function kmUp(e) {
	e.preventDefault();
	if (my > e.pageY) newTimer(e);
	console.log(e, e.pageX - mx, e.pageY - my);
}
function newTimer(e) {
	alert("New timer under development");
	//Android.Tooltip("under devel");
}
