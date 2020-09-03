<template>
    <canvas 
        draggable
        @dragstart='startDrag($event, item)'
        @drop='dropped($event, item)' 
        
        @touchstart="touchStart($event)"
        @touchmove="touchMove($event)"
        @touchend="touchEnd($event)"
        @touchcancel="touchCancel($event)"
        
        width="1200" 
        height="100">
    </canvas>
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
  import axios from 'axios'
  import ApplicationState from '../applicationState'
  import Subscriber from '../subscriber'

  export default {
    name: 'legend-display',
    data: () => {
      return {
          touchState: {pointCount: 0, touch1:{x1:0,y1:0,x2:0,y2:0,id:-1}, touch2:{x1:0,y1:0,x2:0,y2:0,id:-1}}
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
        startDrag(ev, item){
            console.log('drag started');
        },
        dropped(ev, item){
            console.log('dropped');
        },





        //TOUCH EVENTS
        touchStart(ev){
            //console.log('TOUCH START EVENT ' + ev.touches.length);
            //console.log(ev);
            if(ev.touches){
                let points = ev.touches.length;
                if(points == 1){
                    //remember the start position and later process the touchMove event
                    this.touchState.pointCount = points;
                    //identifier, screenX, screenY, target
                    this.touchState.touch1.x1 = ev.touches[0].screenX;
                    this.touchState.touch1.y1 = ev.touches[0].screenY;
                    this.touchState.touch1.id = ev.touches[0].identifier; 
                }
                if(points == 2){
                    //remember the start position and later process the touchMove event
                    this.touchState.pointCount = points;
                    //identifier, screenX, screenY, target
                    this.touchState.touch1.x1 = ev.touches[0].screenX;
                    this.touchState.touch1.y1 = ev.touches[0].screenY;
                    this.touchState.touch1.id = ev.touches[0].identifier;

                    this.touchState.touch2.x1 = ev.touches[1].screenX;
                    this.touchState.touch2.y1 = ev.touches[1].screenY;
                    this.touchState.touch2.id = ev.touches[1].identifier;
                }
            } 
        },
        touchMove(ev){
            //console.log('TOUCH MOVE EVENT ' + ev.touches.length);
            //console.log(ev);
            //this.position += 1000;
            //let newPosition = this.position + 1000;
            //newPosition = 100000;
            //this.setPosition(newPosition);
            if(ev.touches){
                let points = ev.touches.length;
                if(points == 1){
                    //calculate how much each point moved
                    //if(this.touchState.pointCount == 1){}
                    this.touchState.touch1.x2 = ev.touches[0].screenX;
                    this.touchState.touch1.y2 = ev.touches[0].screenY;
                    this.touchState.touch1.id = ev.touches[0].identifier;
                    console.log(this.touchState.touch1.x2 - this.touchState.touch1.x1);
                }
                if(points == 2){
                    //calculate how much each point moved
                    //if(this.touchState.pointCount == 1){}
                    this.touchState.touch1.x2 = ev.touches[0].screenX;
                    this.touchState.touch1.y2 = ev.touches[0].screenY;
                    this.touchState.touch1.id = ev.touches[0].identifier;

                    this.touchState.touch2.x2 = ev.touches[1].screenX;
                    this.touchState.touch2.y2 = ev.touches[1].screenY;
                    this.touchState.touch2.id = ev.touches[1].identifier;

                    console.log( this.touchState.touch1.x2 - this.touchState.touch1.x1 );
                    console.log( this.touchState.touch2.x2 - this.touchState.touch2.x1 );
                }
            }
        },
        touchEnd(ev){
            //console.log('TOUCH END EVENT ' + ev.touches.length);
            //console.log(ev);
            //cancel tracking the event
            this.touchState.pointCount = 0;
        },
        touchCancel(ev){
            //console.log('TOUCH CANCEL EVENT ' + ev.touches.length);
            //console.log(ev);
            //cancel tracking the event
            this.touchState.pointCount = 0;
        },





        setPosition: function(newPosition) {
            let url = "http://localhost:8080/sequencer/position";
            let payload = 'position=' + newPosition;
            let responseData = axios.put(url, payload).then((responseData) => {
                this.loaded = 1; 
                console.log('setPosition returned');
                this.position = newPosition;
                ApplicationState.getInstance().publish('playerPosition', newPosition);
                
            });
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