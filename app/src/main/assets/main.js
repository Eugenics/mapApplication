var layer = new ol.layer.Tile({ source: new ol.source.OSM() });

var map = new ol.Map({
        target: 'map',
        view: new ol.View({
          center: ol.proj.fromLonLat([37.41, 8.82]),
          zoom: 4
        })
      });

 map.addLayer(layer);

 navigator.geolocation.watchPosition(
   function (pos) {
     const coords = [pos.coords.longitude, pos.coords.latitude];
     const accuracy = ol.geom.Polygon(coords, pos.coords.accuracy);
     source.clear(true);
     source.addFeatures([
       new ol.Feature(
         accuracy.transform('EPSG:4326', map.getView().getProjection())
       ),
       new Feature(new ol.geom.Point(fromLonLat(coords))),
     ]);
   },
   function (error) {
     alert(`ERROR: ${error.message}`);
   },
   {
     enableHighAccuracy: true,
   }
 );

 const locate = document.createElement('div');
 locate.className = 'ol-control ol-unselectable locate';
 locate.innerHTML = '<button title="Locate me">◎</button>';
 locate.addEventListener('click', function () {
   if (!source.isEmpty()) {
     map.getView().fit(source.getExtent(), {
       maxZoom: 18,
       duration: 500,
     });
   }
 });

 map.addControl(
   new ol.control.Control({
     element: locate,
   })
 );
