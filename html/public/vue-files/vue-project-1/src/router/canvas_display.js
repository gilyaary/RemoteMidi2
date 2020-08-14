//generic static export functions
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

export function displayLegend(canvas, barsToDisplay, startBar, ticksPerBar){
    var markers = getLegendMarkers(barsToDisplay, startBar, ticksPerBar);//based on 100 * 100 scale
    var markerText = getMarkerText(barsToDisplay, startBar, ticksPerBar);//based on 100 * 100 scale
    markers = addToArray(markers, markerText);
    //var ticks = getTicks(barsToDisplay, startBar, ticksPerBar);//based on 100 * 100 scale
    //markers = addToArray(markers, ticks);
    var beats = getLegendBeats(barsToDisplay, startBar, 4);//based on 100 * 100 scale
    markers = addToArray(markers, beats);

    var width = canvas.width;
    var height = canvas.height;
    var scaledMarkers = scale(markers, width, height);
    drawLines(scaledMarkers, canvas);
}


export function displayTrackOnCanvas(canvas, track, barsToDisplay, startBar, ticksPerBar){
    console.log('displayTrackOnCanvas');
    var markers = getBars(barsToDisplay, startBar, ticksPerBar);//based on 100 * 100 scale
    var ticks = getTicks(barsToDisplay, startBar, ticksPerBar);//based on 100 * 100 scale
    markers = addToArray(markers, ticks);
    var beats = getBeats(barsToDisplay, startBar, 4);//based on 100 * 100 scale
    markers = addToArray(markers, beats);
    try{
        var midiMessages = getMidiMessages(track, barsToDisplay, startBar, ticksPerBar);
        markers = addToArray(markers, midiMessages);
    }catch(ex){
        //alert(ex);
    }
    var width = canvas.width;
    var height = canvas.height;
    var scaledMarkers = scale(markers, width, height);
    drawLines(scaledMarkers, canvas);
}

export function getMarkerText(barsToDisplay, startBar, ticksPerBar){
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

export function getBars(barsToDisplay, startBar, ticksPerBar){
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
        markers[i].lineWidth = 0.1;
    }
    return markers;
}

export function getTicks(barsToDisplay, startBar, ticksPerBar){
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

export function getMidiMessages(track, barsToDisplay, startBar, ticksPerBar){
    var trackEvents = track.events;
    var markers = [];
    var tickCount = barsToDisplay*ticksPerBar;
    var tickWidth = 100 / tickCount;

    var startTick = (startBar-1) * ticksPerBar;
    var endTick = (startBar+barsToDisplay-1) * ticksPerBar;

    processNoteEvents(trackEvents);

    for( var i=0; i<trackEvents.length; i++){
        var ev = trackEvents[i];
        var eventTick = ev.tick;
        var message = ev.message;
        var cmd = message.command;
        var data1 = message.data1;
        var data2 = message.data2;

        var channel = cmd & 15; //and xxxx cccc with 0000 1111 to get the channel's 4 digits
        var command = cmd >> 4; //get the command 4 digits by shifting 4 places to the right: xxxxcccc >> 4 = 0000xxxx (all the c's are truncated)
        var pitch = data1; //0-127
        var velocity = data2; //0-127

        if(eventTick >= startTick && eventTick <= endTick){
            //todo: if this is a note start event then we must also look for the note end event later in the events array
            //alternatively we can process the array and create another array of Note events
            //we do it once when the new sequence is loaded (when "display sequence" button clicked and ajax returns)
            //then we look which event {startTick, endTick} are in range
            var x1 = tickWidth * (eventTick - startTick) + 0.1;
            var x2 = x1 + tickWidth + 0.1;


            //note off (1000 CCCC)
            var startPitch = 50;
            var endPitch = 100;
            var pitchesToShow = endPitch - startPitch;

            if(command == 8 || (velocity === 0)){
                var y1 = 100 - ((pitch-startPitch)/pitchesToShow * 100);
                var y2 = y1;
                var marker = new Line(x1,x2,y1,y2);
                marker.color = 'green';
                marker.type = 'line';
                marker.x1 = x1;
                marker.x2 = x2;
                marker.y1 = y1;
                marker.y2 = y2;
                marker.lineWidth = 0.3;
                markers.push(marker);
            }
            else if(command == 9 && ev.endEvent){
                let x2 = tickWidth * (ev.endEvent.tick - startTick) + 0.1;
                let y1 = 100 - ((pitch-startPitch)/pitchesToShow * 100);
                let y2 = y1;
                let marker = new Line(x1,x2,y1,y2);
                marker.color = 'red';
                marker.type = 'line';
                marker.x1 = x1;
                marker.x2 = x2;
                marker.y1 = y1;
                marker.y2 = y2;
                marker.lineWidth = 0.3;
                markers.push(marker);
            }
        }
    }
     return markers;
}

//couple start note events with end note events
export function processNoteEvents(trackEvents){
    var startedNotesByPitch = [];
    for( var i=0; i<trackEvents.length; i++){
        var ev = trackEvents[i];
        var eventTick = ev.tick;
        var message = ev.message;
        var cmd = message.command;
        var data1 = message.data1;
        var data2 = message.data2;

        var channel = cmd & 15; //and xxxx cccc with 0000 1111 to get the channel's 4 digits
        var command = cmd >> 4; //get the command 4 digits by shifting 4 places to the right: xxxxcccc >> 4 = 0000xxxx (all the c's are truncated)
        var pitch = data1; //0-127
        var velocity = data2; //0-127
        //note off (1000 CCCC)
        if(command == 8 || (velocity === 0)){
            if( startedNotesByPitch[pitch] ){
                //associate the start event with end event and end event with start event
                startedNotesByPitch[pitch].endEvent = ev;
                ev.startEvent = startedNotesByPitch[pitch];
                startedNotesByPitch[pitch] = null;
            }
        }
        //note on
        else if(command == 9){
            startedNotesByPitch[pitch] = ev;
        }
    }
}

export function getBeats(barsToDisplay, startBar, beatsPerBar){
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

export function getLegendMarkers(barsToDisplay, startBar, ticksPerBar){
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

export function getLegendBeats(barsToDisplay, startBar, beatsPerBar){
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

export function scale(markers, width, height){
    var scaled = []
    for(var i=0; i<markers.length; i++){
        //Make this an instance method: Line scaled = Line.scale()
        scaled[i] = markers[i].scale(width, height);
    }
    return scaled;
}

export function drawLines(markers, canvas){
    var ctx = canvas.getContext("2d");
    //ctx.clearRect(0,0,canvas.width, canvas.height);
    ctx.fillStyle="#DDDDFF";
    ctx.fillRect(0,0,canvas.width, canvas.height);
    
    for(var i=1; i<markers.length; i++){
        markers[i].draw(ctx);
    }
    ctx.closePath();
    
       
}

//import "core-js/modules/es.array.concat";
//import "core-js/modules/es.array.filter";
export function addToArray(arr1, arr2){
    for(var i=0; i<arr2.length; i++){
        arr1.push(arr2[i]);
    
    }
    return arr1;
}

/*
    TODO:
    Add the notes display.
    Add refresh logic to move a position marker over the canvas. We may be able to overlay with another div absolute position (z-index)
    Once position marker gets to the end repaint with the new starting bar
*/

