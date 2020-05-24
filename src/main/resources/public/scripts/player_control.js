  $( function() {
        var player_control_app = new Vue({
              el: '#player_control_app',
              data: {
                  song_position: 0,
                  midi_devices: '',
                  status: 'STOP'
              },
              methods: {
                  setSongPosition: function(msg){
                        this.song_position = msg;
                  },
                  setMidiDevices: function(msg){
                        this.midi_devices = msg;
                  },
                  setStatus: function(msg){
                        this.status = msg;
                  },
              }
        });

        $( ".widget input[type=submit], .widget a, .widget button" ).button();

        var loaded = 0;
        $( "#play" ).click( function( event ) {
             var url = loaded == 0 ? "/sequencer/play" : "/sequencer/resume";
             put( url, {}, function( data ) {
                  //alert( data );
                  loaded = 1
                  player_control_app.setStatus('PLAY');
             });
        } );
        $( "#stop" ).click( function( event ) {
             put( "/sequencer/stop", {}, function( data ) {
                  //alert( data );
                  player_control_app.setStatus('STOP');
             });
        } );
        $( "#resume" ).click( function( event ) {
             put( "/sequencer/resume", {}, function( data ) {
                  //alert( data );
                  player_control_app.setStatus('PLAY');
             });
        } );
        $( "#info" ).click( function( event ) {
             $.get( "/sequencer/ports", function( data ) {
                  //alert( data );
                  player_control_app.setMidiDevices(data);
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
            max: 300,
            value: 0,
            slide: function( event, ui ) {
                //$( "#amount" ).val( "$" + ui.value  );
                if(player_control_app.status == 'STOP'){
                    put( "/sequencer/position", {'position': ui.value}, function( data ) {
                          //alert( data );
                          player_control_app.setSongPosition(ui.value);
                    });
                }
            }
        });


        ws = new WebSocket('ws://localhost:8080/song_status');
        ws.onmessage = function(data) {
            if(player_control_app.status == 'PLAY'){
                var value = parseInt(data.data, 10) / 1000000;
                player_control_app.setSongPosition(value);
                slider.slider( "value", value );
            }
        }

  } );