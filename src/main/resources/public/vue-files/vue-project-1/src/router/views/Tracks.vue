<template>
  <div id="canvas_control_app">
        <div>{{message}}</div>
        <div>
            Bars To Display: <input v-model.number="barsToDisplay" type="number" />
        </div>
        <div>
            Start Bar: <input v-model.number="startBar" type="number" />
        </div>
        <div>
            Ticks Per Bar: <input v-model.number="ticksPerBar" type="number" />
        </div>
        <div>
            <button v-on:click="getSequenceInfo">Display Sequence</button>
        </div>
        <br><br>
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
            <legend-display :track="ti" :start="startBar" :bars="barsToDisplay" :ticks="ticksPerBar"/>
            <!-- fileInfo.loadedSequence.trackInfo[0].events[0].message -->
            <!--
            <span>Event Count: {{ti.eventCount}}</span>
            <span>Ticks: {{ti.ticks}}</span>
            -->
            <!-- div v-for="ev in ti.events">
                &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span>{{ev}}</span>
            </div -->
            <track-display v-bind:key="ti.ticks" v-for="ti in fileInfo.loadedSequence.trackInfo" :track="ti" :start="startBar" :bars="barsToDisplay" :ticks="ticksPerBar"/>
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
</style>
    
<script>
  
  import TrackDisplay from './TrackDisplay.vue'
  import LegendDisplay from './LegendDisplay.vue'
  import Home from './Home.vue'
  import Vue from 'vue'
  import axios from 'axios'

  import * as canvasDisplayFunctions from '../canvas_display.js' 

  export default {
    data: () => {
        return {
            fileInfo: {
                loadedSequence: {
                    
                }
            },
            barsToDisplay: 2,
            startBar:1,
            ticksPerBar: 32,
            message: 'Tracks.vue',
        };
    },
    methods: {
        getSequenceInfo: function(){
            //we need to call this otherwise Vue just updates the tracks that changed their properties
            //Vue.set(this.fileInfo, 'loadedSequence', {});
            //this.loadedSequence = {};

            //TODO: this should also be converted into a component
            var instance = this;
            var url = "http://localhost:8080/sequencer/sequenceInfo";
            axios.get(url).then ((responseData) => {
                //Vue.set(instance.fileInfo, 'loadedSequence', responseData.data);
                this.fileInfo.loadedSequence = responseData.data;
            });
        },
    },
    components: {
        TrackDisplay, LegendDisplay
    },
    updated: function(){
        console.log('updated');
        //var canvas = document.getElementById('canvas1');
        //canvasDisplayFunctions.displayLegend(canvas, this.barsToDisplay, this.startBar, this.ticksPerBar);
    },
    mounted: function(){
        console.log('mounted');
        //var canvas = document.getElementById('canvas1');
        //canvasDisplayFunctions.displayLegend(canvas, this.barsToDisplay, this.startBar, this.ticksPerBar);
    },
  }
</script>

