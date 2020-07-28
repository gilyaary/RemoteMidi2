<template>
  <div>
    <div class="widget">
        <h1>Sequencer Widget Buttons</h1>
        <button id="play">Play</button>
        <button id="stop">Stop</button>
        <button id="info">Info</button>
    </div>
</div>
</template>

<script>
  //import Files from './Files.vue'
  export default {
    name: 'player-control',
    data: () => {
        return {
            
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