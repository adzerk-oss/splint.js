jQuery(function($) {
  with(splint) {
    var assert = function(msg, x, y) {
      if(equals(x, y)) {
        console.log("Assert " + msg);
      } else {
        throw new Error(msg);
      }
    };

    var inputText = cell("some new text");
    $("#welcome").text(inputText);
    reset(inputText, "even newer text");

    assert(".text() method accepts and reacts to cell argument",
           document.getElementById("welcome").innerHTML,
           "even newer text");

    assert("ClojureScript functions basically work",
           get(apply(hashMap, mapcat(juxt(str, identity), range(3))), "2"),
           2);
  }
})
