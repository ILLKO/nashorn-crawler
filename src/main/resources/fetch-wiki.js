(function (context) {
  'use strict';

var url =  "http://opennet.ru/"
//"https://en.wikipedia.org/w/api.php?action=query&format=json&prop=&meta=siteinfo&siprop=statistics"
print("fetching" + url);

//setTimeout(function() { context.__nashorn_polyfill_timer.cancel(); }, 3 * 1000);

fetch(url)
  .then(function(response) {
//    if (response.status != 200) throw new Error();
//    if (response.status == 200) throw new Error();

    print("Got response")
    var response = JSON.stringify(response)
    print(response);
  })
  .catch(function(error) {
    print("Got error")
    print('There has been a problem with your fetch operation: ' + error.message);
   });
})(this);


while (true) {
    global.nashornEventLoop.process();
}
