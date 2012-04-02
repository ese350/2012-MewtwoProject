---
layout: post
title: "How to set up the Kinect, the easy way ... and the hard way"
date: 2012-03-29 20:38
comments: true
categories: 
author: Steven Chen
---

So as we all know, there are usually two ways of doing things, the
easy way and the hard way. For setting up the Kinect, we erroneously
chose the hard way. Given, when you learn how to work with something
for the first time, it usually takes a while to set up. But when
you're working with multiple things at the first time together, then
things get a lot more hectic.

So long story short, we found some tutorials on how to install OpenNI
for the kinect as well as various other dependencies needed to run the
Kinect hardware. These worked and gave us some pretty cool bitmaps
painted onto the screen that varied in color based on depth. Some of
the example code could even track the user and showed a few of their
joints with a little skeleton connecting them. Pretty neat
really. I'll post some pictures next time.

This time, I wanted to focus on how to set up the Kinect because that
gave us a whole lot of trouble that could have taken much less
time. For one, don't try the following on VMWare Player running an iso
of Ubuntu 11.10. That for sure didn't work. And probably VMWare Player
in general? We had some pretty bad luck there. The following
instructions should work on any (newer?) 32-bit Ubuntu distros.

Install dependencies first:

    sudo apt-get update
    sudo apt-get install g++ python libusb-1.0-0-dev freeglut3-dev openjdk-6-jdk doxygen

Then install OpenNI:

    git clone https://github.com/OpenNI/OpenNI.git
    cd OpenNI/Platform/Linux/CreateRedist
    chmod a+x RedistMaker
    ./RedistMaker
    cd ../Redist/OpenNI-Bin-Dev-Linux-x86-v1.5.2.23
    sudo ./install.sh

Next up is SensorKinect:

    git clone https://github.com/avin2/SensorKinect.git
    cd SensorKinect/Bin
    tar xvf SensorKinect091-Bin-Linux32-v5.1.0.25.tar.bz2
    cd Sensor-Bin-Linux-x86-v5.1.0.25/
    sudo ./install.sh

Now you can plug in the Kinect and run `lsusb` and check for 3
Microsoft Corp entries. If you see these, you've succesfully installed
everything and the sample code should run on your machine! To test it
out, go to the OpenNI/Platform/Linux/Bin/x86-Release/ folder and run
any of the code in there.

<!-- more -->

And now for the part that a stronger background in Java would have
made so much easier. Go to OpenNI/Platform/Linux/Build/. In here, you
can run `make` and it'll use the `Makefile` that's already set up for
all the code in the `Samples` directory. If you want to `make` the bin
files again, you have to type `make clean` and then `make` again.

Additionally, you can `make` each of the Samples individually by
running the same commands when in their Build/Samples/ProjectNameHere/
directory. Meanwhile, the actual source .java files are in the
OpenNI/Samples directory. So if you want to edit the source code or
make a new project, that's the place to go. Meanwhile, if you make a
new project, you'll have to set up some `Makefile`s in the Build
folder, but at least you have templates there.

Man how all this knowledge could have helped last night. At least we
got things working and were able to modify the UserTracker program
that can find your joints and calculated a few angles to demonstrate
for the physical therapist today. She seemed to like our demo and
suggested to start with some easy exercises for which to monitor
form. We'll see where that takes us and we'll try to have something to
show to her again soon.