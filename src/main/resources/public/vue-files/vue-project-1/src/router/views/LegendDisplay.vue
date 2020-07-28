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
    props: ['track', 'start', 'bars', 'ticks'],
    methods: {
        displayLegend: function(el,parent){
            console.log('displayLegend');
            canvasDisplayFunctions.displayLegend(el, this.bars, this.start, this.ticks);
        }
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
            canvasDisplayFunctions.displayLegend(this.$el, this.bars, this.start, this.ticks);
        },
        start: function(val){
            canvasDisplayFunctions.displayLegend(this.$el, this.bars, this.start, this.ticks);
        },
        bars: function(val){
            canvasDisplayFunctions.displayLegend(this.$el, this.bars, this.start, this.ticks);
        },
        ticks: function(val){
            canvasDisplayFunctions.displayLegend(this.$el, this.bars, this.start, this.ticks);
        },
    },

  }

</script>