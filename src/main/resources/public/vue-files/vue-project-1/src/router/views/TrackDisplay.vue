<template>
     <canvas width="1200" height="200"></canvas>
</template>
<style>
    div{
        padding: 10 10 10 10;
    }
    canvas{
        border:1px solid #000000;
    }
</style>
    
<script>
  
  import * as canvasDisplayFunctions from '../canvas_display.js' 

  export default {
    name: 'track-display',
    data: () => {
      return {

      };
    },
    props: ['track', 'start', 'bars', 'ticks'],
    methods: {
        displayTrack: function(el,parent){
            /*
            //we expose properties for all these so no need to access parent - Independence.
            var ticksPerBar = parent.ticksPerBar;
            var barsToDisplay = parent.barsToDisplay;
            var startBar = parent.startBar;
            */
           console.log('displayTrack');
            canvasDisplayFunctions.displayTrackOnCanvas(el, this.track, this.bars, this.start, this.ticks);
            //now we need to display events
        }
    },
    updated: function(){
        //this.$el.style.background = 'red';
        console.log('updated');
        this.displayTrack(this.$el, this.$parent);
    },
    mounted: function(){
        console.log('mounted');
        //this.$el.style.background = 'green';
        this.displayTrack(this.$el, this.$parent);
    },
    //if any watched property is modified we can take action. We can repaint or we can modify a computed property
    watch: {
        track: function(val){
            canvasDisplayFunctions.displayTrackOnCanvas(this.$el, this.track, this.bars, this.start, this.ticks);
        },
        start: function(val){
            canvasDisplayFunctions.displayTrackOnCanvas(this.$el, this.track, this.bars, this.start, this.ticks);
        },
        bars: function(val){
            canvasDisplayFunctions.displayTrackOnCanvas(this.$el, this.track, this.bars, this.start, this.ticks);
        },
        ticks: function(val){
            canvasDisplayFunctions.displayTrackOnCanvas(this.$el, this.track, this.bars, this.start, this.ticks);
        },
    },

  }

</script>