<template>
  <div>
      <div class="float-left-child control">
          <!-- <files></files><br><br> -->
          <player-control></player-control><hr><br><br>
          <player-position :position="position"></player-position><hr><br><br>
          <player-info></player-info><hr><br><br>
      </div>
      <div class="float-left-child tracks">
          <tracks :position="position"/>        
      </div>
  </div>
</template>
<style scoped>
    .float-left-child {
        float: left;
    }
    .control {
        width: 200px;
        margin-right: 100px;
    }
    .tracks {
        width: 800px;
    }
    
</style>

<script>
  //import Files from './Files.vue'
  import PlayerControl from './PlayerControl.vue'
  import PlayerPosition from './PlayerPosition.vue'
  import PlayerInfo from './PlayerInfo.vue'
  import Tracks from './Tracks.vue'
  export default {
    data: () => {
        return {
            position: 100
        };
    },
    methods: {
        setSongPosition: function(pos){
            this.position = pos;
        }
    },
    components: {
        //uncomment to see files in home page
        //Files
        PlayerControl, PlayerPosition, PlayerInfo, Tracks 
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
        //var player_control_app = this;
        var home = this;

        this.connection.onmessage = function(event) {
            //player_control_app.setMessage(event.data);
            //this.message = {test:2};

            var value = parseInt(event.data, 10) ; /// 1000000
            //player_control_app.setSongPosition(value);
            home.setSongPosition(value);
        }

        this.connection.onopen = function(event) {
            //console.log(event)
            //console.log("Successfully connected to the echo websocket server...")
            //this.message = {test:1};
        }

    },
  }
</script>
<!--
Vue.js allows component communication in the following ways:-
1. Parent to child communication (Using Props).
2. Child to parent communication (Using Events).
3. Communication between any component (Using Event Bus).
-->