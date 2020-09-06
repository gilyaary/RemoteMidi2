<template>
  <div id="canvas_control_app">
        <!-- <div>{{message}}</div>
        <div>
            Bars To Display: <input v-model.number="barsToDisplay" type="number" />
        </div>
        <div>
            Start Bar: <input v-model.number="startBar" type="number" />
        </div>
        -->
<!--
<div v-if="fileInfo && fileInfo.loadedSequence">
    <div>
        Ticks: {{fileInfo.loadedSequence.tickLength}}
    </div>
    <div>
        Resolution: {{fileInfo.loadedSequence.resolution}}
    </div>
    <div>
        Time: {{fileInfo.loadedSequence.microsecondLength}}
    </div>
    <div>
        Tracks: {{fileInfo.loadedSequence.trackCount}}
    </div>
</div>
-->

        <!-- <div>
            <button v-on:click="getSequenceInfo">Display Sequence</button>
        </div>
        <br><br> -->
        <div v-if="fileInfo && fileInfo.loadedSequence">
            <!--
            <div>
                Ticks: {{fileInfo.loadedSequence.tickLength}}
            </div>
            <div>
                Ticks[0]: {{fileInfo.loadedSequence.tickLength}}
            </div>
            <div>
                Time: {{fileInfo.loadedSequence.microsecondLength}}
            </div>
            <div>
                Tracks: {{fileInfo.loadedSequence.trackCount}}
            </div>
            -->
            <!-- <canvas id="canvas1" width="1200" height="30"></canvas><br> -->
            <legend-display 
                v-on:mouseUp="mouseUp($event)" 
                v-on:mouseDown="mouseDown($event)" 
                :track="ti" :start="startBar" 
                :bars="barsToDisplay" 
                :position="playerPosition" 
                :sequence="fileInfo.loadedSequence"
                :width="1200"
                :height="100"
            />
            <!-- fileInfo.loadedSequence.trackInfo[0].events[0].message -->
            <!--
            <span>Event Count: {{ti.eventCount}}</span>
            <span>Ticks: {{ti.ticks}}</span>
            -->
            <!-- div v-for="ev in ti.events">
                &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span>{{ev}}</span>
            </div -->
            <track-display 
                v-bind:key="ti" 
                v-for="ti in fileInfo.loadedSequence.trackInfo" 
                :position="playerPosition" 
                :track="ti" 
                :start="startBar" 
                :bars="barsToDisplay" 
                :ticks="ticksPerBar" 
                :sequence="fileInfo.loadedSequence"
                :width="1200"
                :height="300"
            />
        </div>
        <br><br>
    </div>
</template>
<style>
    div{
        padding: 10 10 10 10;
    }
    canvas{
        border:1px solid #000000;
    }
    #canvas_control_app{
        margin-top: 100px;
    }
</style>
    
<script>
  
  import TrackDisplay from './TrackDisplay.vue'
  import LegendDisplay from './LegendDisplay.vue'
  import Home from './Home.vue'
  import Vue from 'vue'
  import axios from 'axios'

  import * as canvasDisplayFunctions from '../canvas_display.js' 
  import ApplicationState from '../applicationState'
  import Subscriber from '../subscriber'


  export default {
    data: () => {
        return {
            fileInfo: {
                loadedSequence: null,
            },
            barsToDisplay: 2,
            startBar:1,
            message: 'Tracks.vue',
            playerPosition: 0,
        };
    },
    methods: {
        getSequenceInfo: function(){
            //we need to call this otherwise Vue just updates the tracks that changed their properties
            //Vue.set(this.fileInfo, 'loadedSequence', {});
            //this.loadedSequence = {};

            //TODO: this should also be converted into a component
            var instance = this;
            var url = `http://${this.$api_base_url}/sequencer/sequenceInfo`;
            axios.get(url).then ((responseData) => {
                //Vue.set(instance.fileInfo, 'loadedSequence', responseData.data);
                this.fileInfo.loadedSequence = responseData.data;
            });
        },
        mouseDown(event){
            console.info('mouse down');
        },
        mouseUp(event){
            console.info('mouse up');
        },
    },
    props: {
        
    },
    watch: {
        // playerPosition: function(){
        //      //this.song_position = this.position; //we will change a data element by the position property value
        // }
    },
    components: {
        TrackDisplay, LegendDisplay
    },
    updated: function(){
        console.log('updated');
        //var canvas = document.getElementById('canvas1');
        //canvasDisplayFunctions.displayLegend(canvas, this.barsToDisplay, this.startBar, this.ticksPerBar);
        //this.getSequenceInfo();
    },
    mounted: function(){
        console.log('mounted');
        let instance = this;
        //var canvas = document.getElementById('canvas1');
        //canvasDisplayFunctions.displayLegend(canvas, this.barsToDisplay, this.startBar, this.ticksPerBar);
        ApplicationState.getInstance().subscribe(
            {
                stateChanged: function (state, oldValue, newValue) {
                    //console.info('Tracks got State event value: ' + newValue);
                    instance.playerPosition = newValue;
                },
            }, 
            'playerPosition');

        console.log('*************** Tracks subscribed *******************');
        
    },
    created: function(){
        this.getSequenceInfo();
    },
  }
</script>

