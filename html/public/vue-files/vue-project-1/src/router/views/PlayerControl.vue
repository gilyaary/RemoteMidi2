<template>
  <div>
    <div class="widget">
        <h1>Sequencer Widget Buttons</h1>
        <button id="play" class="player-control" @click="play">Play</button>
        <button id="stop" class="player-control" @click="stop">Stop</button>
        <!--             
            <button id="setPosition" class="player-control" @click="setPosition">Set position</button>
        -->
    </div>
    <div>
        <div id="slider">
            <vue-slider v-model="position" min="0" max="100000"></vue-slider>
        </div>
        <div>
            <p>Position: {{position}}</p>
            <p>Message: {{message}}</p>
            State Name: {{playerControlSequencerState}}
        </div>
    </div>
    
</div>
</template>
<style scoped>
    .player-control{
        margin-right: 20px;
        margin-bottom: 20px;
        font-size: 20px;
    }
</style>
<script>

/*
    TODO:
    The way the position is set means that we reply on properties and watches to communicate 
    changes in position.
    The WebSocket sends position messages to the Home.vue parent which changes the position property
    of concerned elements.
    Elements such as legend, track, tracks and playerControl all watch for the position property changes
    When player is in stop mode we need to detect changes in the slider and communicate to the parent
    but that communication would lead to the following:
    child.position.changed->childToParentCall->parent.position.changed->child.position.changed

    This makes it necessary to not rely on properties (automatic behaviour)
    Rather we need to Publish a positionChange message and any interested component can subscribe to it
    The message can include the SENDER. Such that when consuming the message 
    we first check the source and we ignore messages which source is the same component
    receiving the message.

    Sources:
    PlayerControl(slider moved),
    Home.vue (API Notification, initial setting)
    LegendDisplay (User drags the legend)


*/



//import Files from './Files.vue'
  import axios from 'axios'
  import ApplicationState from '../applicationState'
  import Subscriber from '../subscriber'

  var loaded = 0;
  export default {
    name: 'player-control',
    data: () => {
        return {
            loaded: 0,
            connection: null,
            message: {},
            state: 'PLAY',
            playerControlSequencerState: 'Stop',
        };
    },
    props: ['position'],
    //the issue is that whis watch never gets called. Maybe it should be called only when there are changes to values
    watch: {
      position: function(){
          //if position was changed when we are stopped then call the setPosiotion API
          if(this.state == 'STOP'){
              this.setPosition();
          }
      },  
    },
    methods: {
          play: function() {
              let url = (!this.loaded || this.loaded == 0) ? "http://localhost:8080/sequencer/play" : "http://localhost:8080/sequencer/resume";
              //let url = "http://localhost:8080/sequencer/resume";
              let responseData = axios.put(url, {}).then((responseData) => {
                  this.state = 'PLAY';
                  this.loaded = 1;        
                  console.log('play returned');
              });
              ApplicationState.getInstance().publish('playerControlSequencerState', 'Play');
          },
          stop: function() {
              let url =  "http://localhost:8080/sequencer/stop";
              let responseData = axios.put(url, {}).then((responseData) => {
                  console.log('stop returned'); 
                  this.state = 'STOP';    
                  ApplicationState.getInstance().publish('playerControlSequencerState', 'Stop');
              });            
          },
          setPosition: function() {
              let url = "http://localhost:8080/sequencer/position";
              let payload = 'position=' + this.position;
              let responseData = axios.put(url, payload).then((responseData) => {
                  this.loaded = 1; 
                  console.log('setPosition returned');       
              });
          },
        // axios.get(url).then ((responseData) => {
        //     this.fileInfo.files = responseData.data;
        // });
    },
    
    components: {
        //uncomment to see files in home page
        //Files
    },
    updated: function(){
        console.log('updated');
    },
    mounted: function(){
        this.stateName = 'Mounted';
        let instance = this;
        console.log('mounted');
        
        ApplicationState.getInstance().subscribe(
            {
                stateChanged: function (state, oldValue, newValue) {
                    console.info('Player Control got State event value: ' + newValue);
                    instance.playerControlSequencerState = newValue;
                },
            }, 
            'playerControlSequencerState');

        console.log('*************** PlayerControl subscribed *******************');
        
    },
    
  }
</script>