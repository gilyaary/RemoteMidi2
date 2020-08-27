<template>
  <div>
    <div class="widget">
        <h1>Sequencer Widget Buttons</h1>
        <button id="play" class="player-control" @click="play">Play</button>
        <button id="stop" class="player-control" @click="stop">Stop</button>
        <button id="setPosition" class="player-control" @click="setPosition">Set position</button>
    </div>
    <div>
        <div id="slider">
            <vue-slider v-model="position" min="0" max="100000"></vue-slider>
        </div>
        <div>
            <p>Position: {{position}}</p>
            <p>Message: {{message}}</p>
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
  //import Files from './Files.vue'
  import axios from 'axios'
  var loaded = 0;
  export default {
    name: 'player-control',
    data: () => {
        return {
            loaded: 0,
            connection: null,
            message: {},
        };
    },
    props: ['position'],
    //the issue is that whis watch never gets called. Maybe it should be called only when there are changes to values
    
    methods: {
          play: function() {
              let url = (!this.loaded || this.loaded == 0) ? "http://localhost:8080/sequencer/play" : "http://localhost:8080/sequencer/resume";
              //let url = "http://localhost:8080/sequencer/resume";
              let responseData = axios.put(url, {}).then((responseData) => {
                  this.loaded = 1;        
                  console.log('play returned');
              });
          },
          stop: function() {
              let url =  "http://localhost:8080/sequencer/stop";
              let responseData = axios.put(url, {}).then((responseData) => {
                  console.log('stop returned');    
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
        console.log('mounted');
    },
  }
</script>