$( function() {
    var canvas_control_app = new Vue({
          el: '#canvas_control_app',
          data: {
              song_position: 0,
              message: "Tracks Notes",
              barsToDisplay: 4,
              startBar: 1,
              ticksPerBar: 32,
          },
          methods: {
              displayTracks: function(){
                    var canvas1 = document.getElementById("canvas1");
                    var canvas2 = document.getElementById("canvas2");
                    //document.getElementById('canvas1').style.border='outset'
                    document.getElementById('canvas1').style.border='groove'
                    document.getElementById('canvas2').style.border='groove'
                    displayLegend(canvas1, this.barsToDisplay, this.startBar, this.ticksPerBar);
                    displayTrackOnCanvas(canvas2, this.barsToDisplay, this.startBar, this.ticksPerBar);                
              },
          }
    });

    class Line {
        constructor(x1,x2,y1,y2) {
            this.x1 = x1;
            this.x2 = x2;
            this.y1 = y1;
            this.y2 = y2;
        }
        scale(width, height){
            var x1 = this.x1 * (width/100);
            var x2 = this.x2 * (width/100);
            var y1 = this.y1 * (height/100);
            var y2 = this.y2 * (height/100);
            var scaled = new Line(x1,x2,y1,y2);
            scaled.lineWidth = this.lineWidth * (width/100);
            scaled.color = this.color;
            scaled.type = this.type;
            return scaled;
        }
        draw(ctx){
            var x1 = this.x1;
            var x2 = this.x2;
            var y1 = this.y1;
            var y2 = this.y2;
            ctx.beginPath();//ADD THIS LINE!<<<<<<<<<<<<<
            ctx.lineWidth = this.lineWidth;
            ctx.strokeStyle = this.color;
            ctx.moveTo(x1, y1);
            ctx.lineTo(x2, y2);
            ctx.stroke();
        }
    }

    class Text {
        constructor(x1,x2,text) {
            this.x1 = x1;
            this.x2 = x2;
            this.text = text;
        }
        scale(width, height){
            var x1 = this.x1 * (width/100);
            var x2 = this.x2 * (width/100);
            var text = this.text;
            var scaled = new Text(x1,x2,text);
            scaled.lineWidth = this.lineWidth * (width/100);
            scaled.color = this.color;
            scaled.type = this.type;
            return scaled;
        }
        draw(ctx){
            ctx.fillStyle = "red";
            ctx.textAlign = "left";
            ctx.font = "15px Arial";
            ctx.fillText(this.text, this.x1, 15);
        }
    }
    //mycar = new Car("Ford");

    function displayLegend(canvas, barsToDisplay, startBar, ticksPerBar){
        var markers = getLegendMarkers(barsToDisplay, startBar, ticksPerBar);//based on 100 * 100 scale
        var markerText = getMarkerText(barsToDisplay, startBar, ticksPerBar);//based on 100 * 100 scale
        markers = markers.concat(markerText);
        //var ticks = getTicks(barsToDisplay, startBar, ticksPerBar);//based on 100 * 100 scale
        //markers = markers.concat(ticks);
        var beats = getLegendBeats(barsToDisplay, startBar, 4);//based on 100 * 100 scale
        markers = markers.concat(beats);

        var width = canvas.width;
        var height = canvas.height;
        var scaledMarkers = scale(markers, width, height);
        drawLines(scaledMarkers, canvas);
    }


    function displayTrackOnCanvas(canvas, barsToDisplay, startBar, ticksPerBar){
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

    function getMarkerText(barsToDisplay, startBar, ticksPerBar){
        var markers = [];
        var barWidth = 100 / barsToDisplay;
        for(var i=0; i<barsToDisplay; i++){
            var x1 = barWidth * (i) + 1;
            var x2 = x1;
            markers[i] = new Text(x1,x2,parseInt(startBar)+i);
            markers[i].color = 'FFDDFF';
            markers[i].type = 'line';
            markers[i].lineWidth = 0.5;
        }
        return markers;
    }

    function getMarkers(barsToDisplay, startBar, ticksPerBar){
        var markers = [];
        var barWidth = 100 / barsToDisplay;
        for(var i=0; i<barsToDisplay; i++){
            var x1 = barWidth * (i);
            var x2 = x1;
            var y1 = 0;
            var y2 = 100;
            markers[i] = new Line(x1,x2,y1,y2);
            markers[i].color = 'FFDDFF';
            markers[i].type = 'line';
            markers[i].lineWidth = 0.5;
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
            var y2 = 100;
            markers[i] = new Line(x1,x2,y1,y2);
            markers[i].x1 = x1; 
            markers[i].x2 = x2; 
            markers[i].y1 = y1; 
            markers[i].y2 = y2;
            markers[i].color = '#EEEEFF';
            markers[i].type = 'line';
            markers[i].lineWidth = 0.1;
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
            var y2 = 100;
            markers[i] = new Line(x1,x2,y1,y2);
            markers[i].x1 = x1; 
            markers[i].x2 = x2; 
            markers[i].y1 = y1; 
            markers[i].y2 = y2;
            markers[i].color = '#DDFFFF';
            markers[i].type = 'line';
            markers[i].lineWidth = 0.2;
        }
        return markers;
    }

    function getLegendMarkers(barsToDisplay, startBar, ticksPerBar){
        var markers = [];
        var barWidth = 100 / barsToDisplay;
        for(var i=0; i<barsToDisplay; i++){
            var x1 = barWidth * (i);
            var x2 = x1;
            var y1 = 0;
            var y2 = 100;
            markers[i] = new Line(x1,x2,y1,y2);
            markers[i].color = 'black';
            markers[i].type = 'line';
            markers[i].lineWidth = 0.5;
        }
        return markers;
    }

    function getLegendBeats(barsToDisplay, startBar, beatsPerBar){
        var markers = [];
        var tickCount = barsToDisplay*beatsPerBar;
        var barWidth = 100 / tickCount;
        for(var i=0; i<tickCount; i++){
            var x1 = barWidth * (i);
            var x2 = x1;
            var y1 = 50;
            var y2 = 100;
            markers[i] = new Line(x1,x2,y1,y2);
            markers[i].x1 = x1; 
            markers[i].x2 = x2; 
            markers[i].y1 = y1; 
            markers[i].y2 = y2;
            markers[i].color = 'gray';
            markers[i].type = 'line';
            markers[i].lineWidth = 0.2;
        }
        return markers;
    }

    function scale(markers, width, height){
        var scaled = []
        for(var i=0; i<markers.length; i++){
            //Make this an instance method: Line scaled = Line.scale()
            scaled[i] = markers[i].scale(width, height);
        }
        return scaled;
    }

    function drawLines(markers, canvas){
        var ctx = canvas.getContext("2d");
        //ctx.clearRect(0,0,canvas.width, canvas.height);
        ctx.fillStyle="#DDDDFF";
        ctx.fillRect(0,0,canvas.width, canvas.height);
        
        for(var i=1; i<markers.length; i++){
            markers[i].draw(ctx);
        }
        ctx.closePath();
        
           
    }

} );