<?output "../javascript/random.js"?>
var qfilename = new Array();
var ix = 0;
<?nextrec?>
qfilename[ix] = "w=$itemid&r4$=.html";
ix++;
<?loop?>
var max = ix;

var now = new Date();
var seed = now.getTime() % 0xffffffff;

randomQuote();

function rand(n) {
  seed = (0x015a4e35 * seed) % 0x7fffffff;
  return (seed >> 16) % n;
}

function setContent(n) {
  var page = qfilename[n];
  window.location.replace(page);
}

function randomQuote() {
  rq = rand(max);
  if (rq < 0) {
    rq = 0;
  }
  if (rq >= max) {
    rq = max - 1;
  }
  setContent (rq);
}
