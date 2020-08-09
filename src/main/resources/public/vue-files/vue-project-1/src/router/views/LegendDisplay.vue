<template>
     <canvas width="1200" height="100"></canvas>
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
    name: 'legend-display',
    data: () => {
      return {
          
      };
    },
    props: {
        position: Number, //parent will change the value of this component position property
        bars: Number,
        start: Number,
        sequence: Object,
    },
    

    methods: {
        displayLegend: function(el,parent){
            console.log('displayLegend');
            canvasDisplayFunctions.displayLegend(el, this.bars, this.start, this.getTicks());
        },
        getTicks(){
            return 4 * this.sequence.resolution;
        },

    },
    updated: function(){
        console.log('updated');
        this.displayLegend(this.$el, this.$parent);
    },
    mounted: function(){
        console.log('mounted');
        this.displayLegend(this.$el, this.$parent);
    },
    watch: {
        track: function(val){
            canvasDisplayFunctions.displayLegend(this.$el, this.bars, this.start, this.getTicks());
        },
        start: function(val){
            canvasDisplayFunctions.displayLegend(this.$el, this.bars, this.start, this.getTicks());
        },
        bars: function(val){
            canvasDisplayFunctions.displayLegend(this.$el, this.bars, this.start, this.tigetTicks());
        },
        ticks: function(val){
            canvasDisplayFunctions.displayLegend(this.$el, this.bars, this.start, this.getTicks());
        },
        position: function(){
            //when the position is changed we want to make a change to: this.start
            let startBar = Math.round(this.position / (this.getTicks() ) );
            if(startBar != this.start){
                console.log('New Bar: ' + startBar);
                this.start = startBar;
                //canvasDisplayFunctions.displayTrackOnCanvas(this.$el, this.track, this.bars, this.start, this.ticks);
            }
            
        },
        sequence: function(){
            canvasDisplayFunctions.displayLegend(this.$el, this.bars, this.start, this.getTicks());
        },
    },
    
  }

</script>