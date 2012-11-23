window.onload = function() {
  if ((window.innerWidth && window.innerWidth < 481) || (document.body.clientWidth && document.body.clientWidth < 481)) {
    location.replace ("mobile.html");
  } else {
    location.replace ("classic.html");
  }
}