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
          ticks: 0,
      };
    },
    //props: ['track', 'start', 'bars', 'position', 'sequence'],
    props: {
        position: Number, //parent will change the value of this component position property
        track: Object,
        bars: Number,
        start: Number,
        sequence:Object,
    },
    methods: {
        displayTrack: function(el,parent){
            /*
            //we expose properties for all these so no need to access parent - Independence.
            var ticksPerBar = parent.ticksPerBar;
            var barsToDisplay = parent.barsToDisplay;
            var startBar = parent.startBar;
            */
           console.log('displayTrack');
            canvasDisplayFunctions.displayTrackOnCanvas(el, this.track, this.bars, this.start, this.getTicks());
            //now we need to display events
        },
        getTicks(){
            return 4 * this.sequence.resolution;
        },

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
            canvasDisplayFunctions.displayTrackOnCanvas(this.$el, this.track, this.bars, this.start, this.getTicks());
        },
        start: function(val){
            canvasDisplayFunctions.displayTrackOnCanvas(this.$el, this.track, this.bars, this.start, this.getTicks());
        },
        bars: function(val){
            canvasDisplayFunctions.displayTrackOnCanvas(this.$el, this.track, this.bars, this.start, this.getTicks());
        },
        ticks: function(val){
            canvasDisplayFunctions.displayTrackOnCanvas(this.$el, this.track, this.bars, this.start, this.getTicks());
        },
        position: function(){
            //when the position is changed we want to make a change to: this.start
            let startBar = Math.round( this.position / (this.getTicks() ) );
            if(startBar != this.start){
                console.log('New Bar: ' + startBar);
                this.start = startBar;
                //canvasDisplayFunctions.displayTrackOnCanvas(this.$el, this.track, this.bars, this.start, this.getTick());
            }
        },
        sequence: function(){
            canvasDisplayFunctions.displayTrackOnCanvas(this.$el, this.track, this.bars, this.start, this.getTicks());
        },
    },

  }

</script>