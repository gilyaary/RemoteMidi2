$( function() {
    var canvas_control_app = new Vue({
          el: '#canvas_control_app',
          data: {
              song_position: 0,
              message: "Tracks Notes",
              barsToDisplay: 4,
              startBar: 0,
              ticksPerBar: 32,
          },
          methods: {
              displayTracks: function(){
                    var canvas1 = document.getElementById("canvas1");
                    var ctx = canvas1.getContext("2d"); 
                    displayTrackOnCanvas(canvas1, this.barsToDisplay, this.startBar, this.ticksPerBar);                
              },
          }
    });

    function displayTrackOnCanvas(canvas, barsToDisplay, startBar, ticksPerBar){
    
        /*
        var ctx = canvas.getContext("2d");
        ctx.moveTo(0, 0);
        ctx.lineTo(200, 100);
        ctx.moveTo(200, 100);
        ctx.lineTo(400, 0);
        ctx.stroke();
        
        var barsToDisplay = 4;
        var startBar = 15;
        var ticksPerBar = 32;
        */
        var markers = getMarkers(barsToDisplay, startBar, ticksPerBar);//based on 100 * 100 scale
        var ticks = getTicks(barsToDisplay, startBar, ticksPerBar);//based on 100 * 100 scale
        markers = markers.concat(ticks);
        var beats = getBeats(barsToDisplay, startBar, 4);//based on 100 * 100 scale
        markers = markers.concat(beats);


        var width = canvas.width;
        var height = canvas.height;
        var scaledMarkers = scale(markers, width, height);
        drawLines(scaledMarkers, canvas);
    }

    function getMarkers(barsToDisplay, startBar, ticksPerBar){
        var markers = [];
        var barWidth = 100 / barsToDisplay;
        for(var i=0; i<barsToDisplay; i++){
            var x1 = barWidth * (i);
            var x2 = x1;
            var y1 = 0;
            var y2 = 100;
            markers[i] = [];
            markers[i][0] = x1; markers[i][1] = x2; markers[i][2] = y1; markers[i][3] = y2;
        }
        return markers;
    }

    function getBarLegend(barsToDisplay, startBar, ticksPerBar){
        var markers = [];
        var barWidth = 100 / barsToDisplay;
        for(var i=0; i<barsToDisplay; i++){
            var x1 = barWidth * (i);
            var y1 = 0;
            
        }
        return markers;
    }

    function getTicks(barsToDisplay, startBar, ticksPerBar){
        var markers = [];
        var tickCount = barsToDisplay*ticksPerBar;
        var barWidth = 100 / tickCount;
        for(var i=0; i<tickCount; i++){
            var x1 = barWidth * (i);
            var x2 = x1;
            var y1 = 0;
            var y2 = 2;
            markers[i] = [];
            markers[i][0] = x1; markers[i][1] = x2; markers[i][2] = y1; markers[i][3] = y2;
        }
        return markers;
    }
    function getBeats(barsToDisplay, startBar, beatsPerBar){
        var markers = [];
        var tickCount = barsToDisplay*beatsPerBar;
        var barWidth = 100 / tickCount;
        for(var i=0; i<tickCount; i++){
            var x1 = barWidth * (i);
            var x2 = x1;
            var y1 = 0;
            var y2 = 10;
            markers[i] = [];
            markers[i][0] = x1; markers[i][1] = x2; markers[i][2] = y1; markers[i][3] = y2;
        }
        return markers;
    }

    function scale(markers, width, height){
        var scaled = []
        for(var i=0; i<markers.length; i++){
            scaled[i] = []
            scaled[i][0] = markers[i][0] * (width/100);
            scaled[i][1] = markers[i][1] * (width/100);
            scaled[i][2] = markers[i][2] * (height/100);
            scaled[i][3] = markers[i][3] * (height/100);
        }
        return scaled;
    }

    function drawLines(markers, canvas){
        var ctx = canvas.getContext("2d");
        //ctx.clearRect(0,0,canvas.width, canvas.height);
        ctx.fillStyle="#DDDDFF";
        ctx.fillRect(0,0,canvas.width, canvas.height);
        ctx.beginPath();//ADD THIS LINE!<<<<<<<<<<<<<

        for(var i=1; i<markers.length; i++){
            var x1 = markers[i][0];
            var x2 = markers[i][1];
            var y1 = markers[i][2];
            var y2 = markers[i][3];
            ctx.moveTo(x1, y1);
            ctx.lineTo(x2, y2);    
        }
        ctx.closePath();
        ctx.lineWidth = 1;
        ctx.strokeStyle = 'blue';
        ctx.stroke();    
    }

} );