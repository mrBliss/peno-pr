Robot programm main class: geel.WallTracker2
lejos version: 0.8.5


source files are available under
src/

if lejos is installed then the ant buildfiles can be used to compile,
link and upload the programs to the robots.

A prebuild and prelinked nxj file is available:
dist/geel.Walltracker2.nxj

Note that the blue robot has 2 touch sensors but only the left one is used.
Both robots actually run the exact same program which is quite simple.

There are only 3 behaviours, in decreasing priority they are:
 - manual control by computer,
 - when the touch sensor is presses, drive back and rotate a bit,
 - follor the wall using the wall sensor pointed to the left.
 
The sensors are sampled with a specific frequency and transmitted to the PC 
side for visualization where the two thresholds for the color identification
can also be changed interactively.

To start the demo you first need to start the program on the robot and then
connect to it from the pc-side.

known bugs:
 - sometimes the robot seems to stall in which case pressing the touch sensor
   seems to solve it.
 