<template>
  <div>
    <div>
        <p>Midi Devices:</p>
        {{midiDevices}}
    </div>
  </div>
</template>

<script>
  //import Files from './Files.vue'
  import axios from 'axios'
  export default {
    name: 'player-info',
    data: () => {
        return {
            midiDevices: {},        
        };
    },
    methods: {
      // $.get( "/sequencer/ports", function( data ) {
      //     //alert( data );
      //     player_control_app.setMidiDevices(data);
      // });
      getMidiDeviceInfo: function() {
          console.log('getMidiDevicedInfo()');
          let url = `http://${this.$api_base_url}/sequencer/ports`;
          axios.get(url).then ((responseData) => {
              //TODO: Parse the JSON and display correctly
              this.midiDevices = responseData.data;
          });
      },
      
    },
    components: {
        //uncomment to see files in home page
        //Files
    },
    mounted: function (){
        console.log('mounted()');
        this.getMidiDeviceInfo();
    },
    updated: function () {
        console.log('updated()');
        this.getMidiDeviceInfo();
    },      
  }
</script>