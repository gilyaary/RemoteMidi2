<template>
  <div id="file_app">
    <!-- <div class="widget">
        <h1>Score Control Buttons</h1>
        <button v-on:click="showFiles">Show Files</button>
    </div>
    <br><br><br> -->
    <br><br><br>

    <div class="file-container">
        <div id="file-display" v-for="file in fileInfo.files" v-bind:key="file" :class="{ selectedItem: isSelected(file) }" @click="selectFile(file)" @dblClick="openFile(file)">
            {{file}}
        </div>
    </div>
    
    
    <br><br>
    <div v-if="fileInfo && fileInfo.loadedSequence" class="file-info">
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
</div>

</template>

<style scoped>
    .file-container{
        float: left;
    }
    #file-display{
        margin-top: 15px;
    }
    .file-info{
        float: left;
    }
    .selectedItem{
        background: red;
    }
</style>

<script>
    import axios from 'axios'
    export default {
        name: 'file-display',
        data: () =>  { 
            return {
                fileInfo:{
                      name: 'GIL',
                      files: [],
                      selectedFile: '',
                      loadedSequence: null,
                },
            }
        },
        methods: {
            showFiles: function(){
                  this.fileNames = [];
                  var instance = this;
                  let url = "http://localhost:8080/sequencer/files";
                  axios.get(url).then ((responseData) => {
                      this.fileInfo.files = responseData.data;
                  });
            },
            isSelected: function(fileName){
                  return this.fileInfo.selectedFile === fileName;
                  //return true;
            },
            selectFile: function(fileName){
                  console.log('selectFile()');
                  this.fileInfo.selectedFile = fileName;
                  this.openFile(fileName);
            },
            openFile: function(fileName){
                  console.log('openFile()');
                  var instance = this;
                  var url = "http://localhost:8080/sequencer/load/" + encodeURI(fileName);
                  console.log('put to url: ' + url);
                  let responseData = axios.put(url, {}).then((responseData) => {
                      //console.log('recieved back data: ' );
                      //console.log( responseData.data );
                      this.fileInfo.loadedSequence = responseData.data;
                  });
                  

            },
        },
        updated: function(){
            console.log('updated');
            this.showFiles();
        },
        mounted: function(){
            console.log('mounted');
        },
        created(){
            this.showFiles();
        }
    }
</script>