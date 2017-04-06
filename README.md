# Table of Contents
1. [How To Execute](README.md#how-to-execute)
2. [Implementation Summary](README.md#implementation-summary)
3. [Details of Implementation](README.md#details-of-implementation)

# How To Execute

The code is implemented in Java. You need to have JDK 8 ready to run the code.

The source code doesn't depend on any third party libraries and could directly be compile and executed using the command line in `run.sh`. This would be enough to generate the result files.

The unit test code is under `./test` and is depending on junit library. The jar files are already put under `./lib` so executing `./run_unit_test` would verify if all unit tests pass or not. 

# Implementation Summary

For each feature request, I created a model class to hold the states such as `Host`, `Resource`, `SlidingWindow` and `BlockerWindow`. I also had a model for the input log as `FanSiteLog`.

THe main class will read the log, and delegate it to LogProcessor for any necessary processing. To make the logic clear, I created four dedicated processors, `TopHostProcessor`, `TopHourProcessor`, `TopResourceProcessor`, and `LoginAttemptProcessor` to handle specific feature request. This ensures the code is decoupled and easy to maintain and test in the long term.

The util package provides some sharable function implementation across the board.

## Details of Implementation
I mainly used two data structures to solve these four features. For top 10 most active hosts and top 10 resources, I used a min-heap in a size of 10 to keep the 10 maximum value. Suppose we have a total of N records and want to get the top K, the time complexity will be Nlog(K). This will be cheaper than a complete quick sort Nlog(N).

For the features of busy hours and blocking requests, I used a sliding window in a size of the duration of seconds. For busy hour, the size is 3000. For blocking requests, it is 20. By using the sliding window I could override the previous information which is no longer part of the current window and keep track of the most accurate information.  

### Feature 1: 
After collecting the visit count of all hosts, we build a min heap in the size of 10. For each host, we compare its count with the root of the min heap. If it is larger then the root(which is the smallest element in the heap), we will just replace the root by this new host. In this way, we could always keep top 10 hosts in the heap without a complete quick sort.

### Feature 2: 
The approach is similar for this feature as above.

### Feature 3:
I buffer all logs in the same second and when it moves to a different second, I will clear all the buffer and update the sliding window at the correct index. It will also mark seconds from last second to latest second as 0 since no logs show in this duration.
As long as one unit in the window updates, we will also update the total count and put it into the priority queue which keeps track of the top 10 busy hours. 

### Feature 4: 
Using similar algorithm we could keep track of the total count of failed login attempts in the last 20 seconds. The difference is for each host, we will have such a sliding window. The update strategy is also more complex. When there is a successfully login the sliding window will be re-instantiated. If this host is already in blocked state, we will just log it without doing anything more.