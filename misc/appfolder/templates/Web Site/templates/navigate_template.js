<?output "../javascript/navigate.js"?>
var qfilename = new Array();
var ix = 0;
<?nextrec?>
qfilename[ix] = "w=$itemid&r4$=.html";
ix++;
<?loop?>
var max = ix;

var now = new Date();
var seed = now.getTime() % 0xffffffff;

function rand(n) {
  seed = (0x015a4e35 * seed) % 0x7fffffff;
  return (seed >> 16) % n;
}

function setContent(n) {
  var thisURL = document.URL;
  var wisdomIx = thisURL.indexOf("/wisdom/");
  var authorsIx = thisURL.indexOf("/authors/");
  var tagsIx = thisURL.indexOf("/tags/");
  var page = qfilename[n];
  if (wisdomIx < 0) {
    if (authorsIx > 0 || tagsIx > 0) {
      page = "../wisdom/" + page;
    } else {
      page = "wisdom/" + page;
    }
  }
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

function firstQuote() {
  var ix = 0;
  setContent(ix);
}

function lastQuote() {
  var ix = max - 1;
  setContent(ix);
}

function nextQuote() {
  bumpQuote(1);
}

function priorQuote() {
  bumpQuote(-1);
}

function bumpQuote(inc) {
  var thisURL = document.URL;
  var lastSlash = thisURL.lastIndexOf("/");
  var thisPage = thisURL.substr(lastSlash + 1);
  var ix = 0;
  var found = false;
  while (ix < max && (! found)) {
    var page = qfilename[ix];
    if (thisPage == page) {
      found = true;
    } else {
      ix++;
    }
  }
  if (found) {
    ix = ix + inc;
    if (ix >= max) {
      ix = 0;
    }
    if (ix < 0) {
      ix = max - 1;
    }
    setContent(ix);
  } else {
    setContent(0);
  }
}
