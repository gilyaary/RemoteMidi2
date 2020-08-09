<template>
  <div>
    <div class="widget">
        <h1>Sequencer Widget Buttons</h1>
        <button id="play" @click="play">Play</button>
        <button id="stop" @click="stop">Stop</button>
        <button id="setPosition" @click="setPosition">Set position</button>
    </div>
</div>
</template>

<script>
  //import Files from './Files.vue'
  import axios from 'axios'
  var loaded = 0;
  export default {
    name: 'player-control',
    data: () => {
        return {
            loaded: 0,
            position: 80,
        };
    },
    methods: {
          play: () => {
              let url = loaded == 0 ? "http://localhost:8080/sequencer/play" : "/sequencer/resume";
              let responseData = axios.put(url, {}).then((responseData) => {
                  loaded == 1        
              });
          },
          stop: () => {
              let url =  "http://localhost:8080/sequencer/stop";
              let responseData = axios.put(url, {}).then((responseData) => {
                      
              });            
          },
          setPosition: () => {
              /*
              let url = "http://localhost:8080/sequencer/position";
              let data = 'position=' + this.position;
              let responseData = axios.put(url, data).then((responseData) => {
                  //loaded == 1        
              });
              */
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