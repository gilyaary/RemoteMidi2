<template>
  <div>
      <div id="slider">
          <vue-slider v-model="song_position" min="0" max="100000"></vue-slider>
      </div>
      <div>
        <p>Position: {{song_position}}</p>
        <p>Message: {{message}}</p>
        State Name: {{playerControlSequencerState}}
      </div>
  </div>
</template>

<script>
  //import Files from './Files.vue'
  import axios from 'axios'
  export default {
    name: 'player-position',
    data: () => {
        return {
            song_position: 1,
            connection: null,
            message: {},
        };
    },
    props: {
        //position: 0 //parent will change the value of this component position property
    },
    watch: {
        position: function(){
            this.song_position = this.position; //we will change a data element by the position property value
        }
    },
    methods: {
        sendMessage: function(message) {
          //console.log(this.connection);
          this.connection.send(message);
        },
        setMessage: function(msg){
            this.message = msg;
        },
        setSongPosition: function(msg){
            this.song_position = msg;
        },
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
    created: function() {

    },

    
};
 
</script>