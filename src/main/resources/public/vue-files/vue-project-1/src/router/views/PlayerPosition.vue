<template>
  <div>
      <div id="slider"></div>
      <div>
        <p>Position: {{song_position}}</p>
        <p>Message: {{message}}</p>
      </div>
    
  </div>
</template>

<script>
  //import Files from './Files.vue'
  import axios from 'axios'
  export default {
    name: 'player-position',
    data: () => {
        return {
            song_position: 1,
            connection: null,
            message: {},
        };
    },
    methods: {
        sendMessage: function(message) {
          //console.log(this.connection);
          this.connection.send(message);
        },
        setMessage: function(msg){
            this.message = msg;
        },
        setSongPosition: function(msg){
            this.song_position = msg;
        },
    },
    components: {
        //uncomment to see files in home page
        //Files
    },
    updated: function(){
        console.log('updated');
    },
    mounted: function(){
        console.log('mounted');
    },
    created: function() {
        console.log("Starting connection to WebSocket Server")
        this.connection = new WebSocket("ws://localhost:8080/song_status")
        var player_control_app = this;

        this.connection.onmessage = function(event) {
            //console.log(event);
            var value = parseInt(event.data, 10) / 1000000;
            //player_control_app.setMessage(event.data);
            player_control_app.setSongPosition(value);
            //slider.slider( "value", value );
            //this.message = {test:2};
        }

        this.connection.onopen = function(event) {
            //console.log(event)
            //console.log("Successfully connected to the echo websocket server...")
            //this.message = {test:1};
        }

    },

    
};
 
</script>