<template>
  <div>
      <div class="float-left-child control">
           State Name: {{playerControlSequencerState}}
          <!-- <files></files><br><br> -->
          <player-control></player-control><hr><br><br>
          <!-- <player-position :position="position"></player-position><hr><br><br> -->
          <player-info></player-info><hr><br><br>
      </div>
      <div class="float-left-child tracks">
          <tracks/>        
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
  import ApplicationState from '../applicationState'
  import Subscriber from '../subscriber'
  export default {
    data: () => {
        return {
            playerControlSequencerState: '',
        };
    },
    methods: {
        
    },
    components: {
        //uncomment to see files in home page
        //Files
        PlayerControl, PlayerInfo, Tracks,
        //PlayerPosition,  
    },
    updated: function(){
        console.log('updated');
        
    },
    mounted: function(){
        let instance = this;
        // ApplicationState.getInstance().subscribeWithCallback(this.playerControlSequencerStateChanged, 
        //     'playerControlSequencerState');

        ApplicationState.getInstance().subscribe(
            {
                stateChanged: function (state, oldValue, newValue) {    
                    console.info('Home got State event value: ' + newValue);
                    instance.playerControlSequencerState = newValue;
                },
            }, 
            'playerControlSequencerState');

        console.log('**************** Home subscribed ');
    },
    created: function() {
        console.log("Starting connection to WebSocket Server")
        this.connection = new WebSocket("ws://localhost:8080/song_status")
        //var player_control_app = this;
        var home = this;
        let instance = this;

        this.connection.onmessage = function(event) {
            //player_control_app.setMessage(event.data);
            //this.message = {test:2};

            var value = parseInt(event.data, 10) ; /// 1000000
            //player_control_app.setSongPosition(value);
            //console.log(instance.playerControlSequencerState);
            if(instance.playerControlSequencerState === 'Play'){
                ApplicationState.getInstance().publish('playerPosition', value);
                //console.log("Got Position: " + value);
            }
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