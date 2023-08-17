// A port of the FlowControl extension for use with Selenium IDE
// (Firefox Plugin). Open the Selenium IDE and select the Options 
// menu item. Select Options and add the full path and filename
// of this file in the Selenium Core Extensions (user-extensions.js)
// field.  Close and re-open the IDE to begin using it.  For more
// information see the following URL:
// http://51elliot.blogspot.com/2008/02/selenium-ide-goto.html

var gotoLabels= {};
var whileLabels = {}; 

// overload the oritinal Selenium reset function
Selenium.prototype.reset = function() {
   // reset the labels
   this.initialiseLabels();
   // proceed with original reset code
   this.defaultTimeout = Selenium.DEFAULT_TIMEOUT; 
   this.browserbot.selectWindow("null"); 
   this.browserbot.resetPopups();
}

Selenium.prototype.initialiseLabels = function()
{
    gotoLabels  = {};
    whileLabels = { ends: {}, whiles: {} };
    var command_rows = [];
    var numCommands = testCase.commands.length;
    for (var i = 0; i < numCommands; ++i) {
       var x = testCase.commands[i];
       command_rows.push(x);
    }    
    var cycles = [];
    for( var i = 0; i < command_rows.length; i++ ) {
        if (command_rows[i].type == 'command')
        switch( command_rows[i].command.toLowerCase() ) {
            case "label":
                gotoLabels[ command_rows[i].target ] = i;
                break;
            case "while":
            case "endwhile":
                cycles.push( [command_rows[i].command.toLowerCase(), i] )
                break;    
        }
    }
    var i = 0;    
    while( cycles.length ) {
        if( i >= cycles.length ) {
            throw new Error( "non-matching while/endWhile found" );
        }
        switch( cycles[i][0] ) {
            case "while":
                 if(    ( i+1 < cycles.length )  && ( "endwhile" == cycles[i+1][0] ) ) {
                     // pair found
                     whileLabels.ends[ cycles[i+1][1] ] = cycles[i][1];
                     whileLabels.whiles[ cycles[i][1] ] = cycles[i+1][1];
                     cycles.splice( i, 2 );
                     i = 0;
                 } else ++i;
                 break;
             case "endwhile":
                 ++i;
                 break;
        }
    } 
}    

Selenium.prototype.continueFromRow = function( row_num ) 
{
    if(row_num == undefined || row_num == null || row_num < 0) {
        throw new Error( "Invalid row_num specified." );
    }
    testCase.debugContext.debugIndex = row_num;
}

// do nothing. simple label
Selenium.prototype.doLabel      = function(){};

Selenium.prototype.doGotolabel  = function( label ) 
{
    if( undefined == gotoLabels[label] ) {
        throw new Error( "Specified label '" + label + "' is not found." );
    }
    this.continueFromRow( gotoLabels[ label ] );
};
    
Selenium.prototype.doGoto = Selenium.prototype.doGotolabel;

Selenium.prototype.doGotoIf = function( condition, label ) 
{
    if( eval(condition) ) this.doGotolabel( label );
}

Selenium.prototype.doWhile = function( condition ) 
{
    if( !eval(condition) ) {
        var last_row = testCase.debugContext.debugIndex;
        var end_while_row = whileLabels.whiles[ last_row ];
        if( undefined == end_while_row ) throw new Error( "Corresponding 'endWhile' is not found." );
        this.continueFromRow( end_while_row );
    }
}

Selenium.prototype.doEndWhile = function() 
{
    var last_row = testCase.debugContext.debugIndex;
    var while_row = whileLabels.ends[ last_row ] - 1;
    if( undefined == while_row ) throw new Error( "Corresponding 'While' is not found." );
    this.continueFromRow( while_row );
}

//
// Locates an element by a partial match on id
//
PageBot.prototype.locateElementByPartialId = function(text, inDocument) {
    var result = this.locateElementByXPath("//*[contains(./@id, 'Z')][1]".replace(/Z/,text), inDocument);
    if (result == null) {
        result = this.locateElementByXPath("//*[contains(./@name, 'Z')][1]".replace(/Z/,text), inDocument);
    }
    return result;
};

// Random generator
Selenium.prototype.doRandomString = function( options, varName ) {

    var length = 8;
    var type   = 'alphanumeric';
    var o = options.split( '|' );
    for ( var i = 0 ; i < 2 ; i ++ ) {
        if ( o[i] && o[i].match( /^\d+$/ ) )
            length = o[i];

        if ( o[i] && o[i].match( /^(?:alpha)?(?:numeric)?$/ ) )
            type = o[i];
    }

    switch( type ) {
        case 'alpha'        : storedVars[ varName ] = randomAlpha( length ); break;
        case 'numeric'      : storedVars[ varName ] = randomNumeric( length ); break;
        case 'alphanumeric' : storedVars[ varName ] = randomAlphaNumeric( length ); break;
        default             : storedVars[ varName ] = randomAlphaNumeric( length );
    };
};

function randomNumeric ( length ) {
    return generateRandomString( length, '0123456789'.split( '' ) );
}

function randomAlpha ( length ) {
    var alpha = 'abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ'.split( '' );
    return generateRandomString( length, alpha );
}

function randomAlphaNumeric ( length ) {
    var alphanumeric = '01234567890abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ'.split( '' );
    return generateRandomString( length, alphanumeric );
}

function generateRandomString( length, chars ) {
    var string = '';
    for ( var i = 0 ; i < length ; i++ )
        string += chars[ Math.floor( Math.random() * chars.length ) ];
    return string;
}
