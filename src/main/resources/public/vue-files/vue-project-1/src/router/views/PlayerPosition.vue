<template>
  <div>
      <div id="slider"></div>
  </div>
</template>

<script>
  //import Files from './Files.vue'
  export default {
    name: 'player-position',
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