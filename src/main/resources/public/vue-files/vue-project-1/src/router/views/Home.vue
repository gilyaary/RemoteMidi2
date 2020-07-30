<template>
  <div>
      <div class="float-left-child control">
          <!-- <files></files><br><br> -->
          <player-control></player-control><hr><br><br>
          <player-position></player-position><hr><br><br>
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
        margin-top: 100px;
    }
    .tracks {
        width: 800px;
        margin-top: 100px;
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
            
        };
    },
    methods: {
        
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
  }
</script>