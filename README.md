Nostalgia Games - Street Rider
==============================

The object of the game is to score as many points as you can. You earn points by bunnyhopping over obstacles. 
Small Ramps jump you 1 space, while big ramps jump you 3 spaces.

Use the up and down arrow keys to change lanes, the right arrow key to start moving, and the space bar to jump.

Note that I wasn't using maven back in the day; I've added a pom to hopefully simplify the building process. 

    mvn clean compile

You can then open ./target/classes/index.html in a browser to run the applet. Note that you may have to hit that
URL from a web server for it to work properly; if you run OSX you can cd into the folder in terminal and run:

    python -m SimpleHTTPServer 8111
    
and then open up http://localhost:8111 in a browser.

Note also that instead, you can run this from the appletviewer standalone utility bundled with the JDK:

    appletviewer index.html
    
(run that also from within target/classes)
