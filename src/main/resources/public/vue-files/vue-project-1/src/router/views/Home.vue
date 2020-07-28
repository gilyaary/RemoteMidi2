<template>
  <div>
      <files></files><br><br>
      <player-control></player-control><hr><br><br>
      <player-position></player-position><hr><br><br>
      <player-info></player-info><hr><br><br>
  </div>
</template>

<script>
  import Files from './Files.vue'
  import PlayerControl from './PlayerControl.vue'
  import PlayerPosition from './PlayerPosition.vue'
  import PlayerInfo from './PlayerInfo.vue'
  export default {
    data: () => {
        return {
            
        };
    },
    methods: {
        
    },
    components: {
        //uncomment to see files in home page
        //Files
        PlayerControl, PlayerPosition, PlayerInfo 
    },
    updated: function(){
        console.log('updated');
    },
    mounted: function(){
        console.log('mounted');
    },
  }
</script>