# REconnaisance Aircraft GUI
Author: Martin Michalski<br/>
In development: 8/2018 - 5/2019<br/><br/>

A GUI written to display flight telemetry for a UAV built as part of Northrop Grumman's HIP Program. Makes use of Google's Static Maps API.
To run, needs a flight history file, with one provided in the repository as a sample. Currently, there is no support for 
the reading of live flight data. To see a video of a drone flight side-by-side with this software, click 
[here](https://www.youtube.com/watch?v=ujrxqKpUfvo&t=128s). <br/><br/>
<h4>Reflection:</h4>
The software written is not optimal. Text files are used as a medium to interface between the display program and the data feed program,
which introduces unnecessary latency. File paths are poorly managed, and scattered throughout classes in the program. That said, the program serves its defined purpose in forcing me to learn more Java to accomplish the goals I set for myself.

<br/><br/><h2>To Run:</h2>
1. Download all files in repository into a Java Project <br/>
2. Replace "MAPS_API_KEY" in GPSTrackingMap.java with your Google Static Maps API key <br/>
3. Change constants storing absolute paths to match paths on your machine<br/> 
4. Run
