<template>
  <div>
    <div>
        <p>Position:</p>
        {{song_position}}
    </div>
    <div>
        <p>Midi Devices:</p>
        {{midi_devices}}
    </div>
  </div>
</template>

<script>
  //import Files from './Files.vue'
  export default {
    name: 'player-info',
    data: () => {
        return {
            song_position: 1,
            midi_devices: {},        
        };
    },
    methods: {
        
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