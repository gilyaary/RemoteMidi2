  $( function() {
        var player_control_app = new Vue({
              el: '#player_control_app',
              data: {
                  message: 'Hello Vue!'
              },
              methods: {
                  displayMessage: function(msg){this.message = msg;}
              }
        });

        $( ".widget input[type=submit], .widget a, .widget button" ).button();

        var loaded = 0;
        $( "#play" ).click( function( event ) {
             var url = loaded == 0 ? "/sequencer/play" : "/sequencer/resume";
             put( url, {}, function( data ) {
                  //alert( data );
                  loaded = 1
             });
        } );
        $( "#stop" ).click( function( event ) {
             put( "/sequencer/stop", {}, function( data ) {
                  //alert( data );
             });
        } );
        $( "#resume" ).click( function( event ) {
             put( "/sequencer/resume", {}, function( data ) {
                  //alert( data );
             });
        } );
        $( "#info" ).click( function( event ) {
             $.get( "/sequencer/ports", function( data ) {
                  alert( data );
             });
        } );
        function put(url, data, cb){
            $.ajax({
              url: url,
              type: 'PUT',
              data: data,
              success: function(data) {
                  cb(data);
              }
            });
        };


        var slider = $( "#slider" ).slider({
            range: "min",
            min: 0,
            max: 500,
            value: 75,
            slide: function( event, ui ) {
                //$( "#amount" ).val( "$" + ui.value  );
            }
        });


        var call_num = 1;
        ws = new WebSocket('ws://localhost:8080/song_status');
        ws.onmessage = function(data) {
            var value = data.data.substring(15);
            player_control_app.displayMessage(value);
            slider.slider( "value", value );
        }

  } );