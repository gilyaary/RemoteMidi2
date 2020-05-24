  $( function() {
        var player_control_app = new Vue({
              el: '#player_control_app',
              data: {
                  song_position: 0,
                  midi_devices: ''
              },
              methods: {
                  songPosition: function(msg){this.song_position = msg;},
                  midiDevices: function(msg){this.midi_devices = msg;}
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
                  //alert( data );
                  player_control_app.midiDevices(data);
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


        ws = new WebSocket('ws://localhost:8080/song_status');
        ws.onmessage = function(data) {
            var value = parseInt(data.data, 10) / 1000000;
            player_control_app.songPosition(value);
            slider.slider( "value", value );
        }

  } );