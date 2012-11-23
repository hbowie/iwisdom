
function rand(n) {
  seed = (0x015a4e35 * seed) % 0x7fffffff;
  return (seed >> 16) % n;
}

var now = new Date();
var seed = now.getTime() % 0xffffffff;
randomQuote();

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
