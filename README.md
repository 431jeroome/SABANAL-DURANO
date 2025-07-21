# CPU Scheduling Visualization Simulator

A comprehensive Java-based CPU scheduling algorithm simulator that demonstrates and visualizes various CPU scheduling policies with detailed metrics and Gantt charts.

## Features

### Implemented Algorithms
1. **First-Come First-Served (FCFS/FIFO)** - Non-preemptive scheduling based on arrival time
2. **Shortest Job First (SJF)** - Non-preemptive scheduling based on burst time
3. **Shortest Remaining Time First (SRTF)** - Preemptive scheduling based on remaining time
4. **Round Robin (RR)** - Preemptive scheduling with configurable time quantum
5. **Multilevel Feedback Queue (MLFQ)** - Multi-level priority scheduling with 4 queues

### Key Features
- **Interactive User Interface** - Console-based menu system
- **Multiple Input Methods**:
  - Sample processes for quick testing
  - Random process generation
  - Manual process input
- **Detailed Visualization**:
  - ASCII Gantt charts showing execution order
  - Process metrics (Completion, Turnaround, Response times)
  - Average performance metrics
- **Configurable Parameters**:
  - Time quantum for Round Robin
  - Time quantum and allotment time for MLFQ
- **Comprehensive Metrics**:
  - Process ID, Arrival Time, Burst Time
  - Completion Time, Turnaround Time, Response Time
  - Average Turnaround Time, Average Response Time

## File Structure

```
├── CPUSchedulingVisualization.java  # Main program entry point
├── Process.java                     # Process class with all attributes
├── Scheduler.java                   # Abstract base scheduler class
├── FCFSScheduler.java              # First-Come First-Served implementation
├── SJFScheduler.java               # Shortest Job First implementation
├── SRTFScheduler.java              # Shortest Remaining Time First implementation
├── RoundRobinScheduler.java        # Round Robin implementation
├── MLFQScheduler.java              # Multilevel Feedback Queue implementation
├── ProcessGenerator.java           # Utility for generating test processes
└── README.md                       # This file
```

## How to Compile and Run

### Prerequisites
- Java Development Kit (JDK) 8 or higher

### Compilation
```bash
javac *.java
```

### Execution
```bash
java CPUSchedulingVisualization
```

## Usage Instructions

### 1. Program Startup
The program starts with a welcome screen and prompts you to choose how to input processes.

### 2. Process Input Options
- **Option 1**: Use sample processes (5 predefined processes for testing)
- **Option 2**: Generate random processes (specify number, random arrival/burst times)
- **Option 3**: Manually input processes (specify arrival and burst times for each)

### 3. Algorithm Selection
Choose from the 5 available scheduling algorithms:
1. FCFS
2. SJF (Non-preemptive)
3. SRTF (Preemptive)
4. Round Robin (requires time quantum input)
5. MLFQ (requires time quantum and allotment time input)

### 4. Results Display
For each algorithm, the program displays:
- **Gantt Chart**: Visual representation of process execution order
- **Process Details**: Complete metrics for each process
- **Average Metrics**: Overall performance statistics

## Algorithm Details

### First-Come First-Served (FCFS)
- **Type**: Non-preemptive
- **Selection**: Processes are executed in order of arrival
- **Advantages**: Simple, fair
- **Disadvantages**: Poor performance with varying burst times
- <img width="1919" height="1079" alt="image" src="https://github.com/user-attachments/assets/6b6762f7-67eb-43fd-a0ba-80123dd451f3" />


### Shortest Job First (SJF)
- **Type**: Non-preemptive
- **Selection**: Process with shortest burst time is selected
- **Advantages**: Minimizes average waiting time
- **Disadvantages**: Requires knowledge of burst times
- <img width="1919" height="1079" alt="image" src="https://github.com/user-attachments/assets/af5bc207-87de-4f33-bbc5-9591537185c2" />


### Shortest Remaining Time First (SRTF)
- **Type**: Preemptive
- **Selection**: Process with shortest remaining time is selected
- **Advantages**: Optimal for minimizing average waiting time
- **Disadvantages**: Complex implementation, starvation possible
- <img width="1919" height="1079" alt="image" src="https://github.com/user-attachments/assets/537aef8c-2e1c-4816-a5f8-ecc074c08401" />


### Round Robin (RR)
- **Type**: Preemptive
- **Selection**: Processes are executed in FIFO order with time quantum
- **Advantages**: Fair, no starvation
- **Disadvantages**: Performance depends on time quantum size
- <img width="1919" height="1079" alt="image" src="https://github.com/user-attachments/assets/08c257eb-9554-48d5-abcd-e21a79b7877e" />


### Multilevel Feedback Queue (MLFQ)
- **Type**: Preemptive, multi-level
- **Queues**: 4 priority levels (Q0, Q1, Q2, Q3)
- **Selection**: Highest priority non-empty queue
- **Behavior**: 
  - Q0-Q2: Round Robin with time quantum
  - Q3: FCFS (until completion)
  - Processes can be demoted to lower priority queues
- **Advantages**: Balances response time and throughput
- **Disadvantages**: Complex, potential starvation
- <img width="1919" height="1079" alt="image" src="https://github.com/user-attachments/assets/6207dff9-0ce1-4639-a24a-198c4ce7c614" />


## Sample Output

```
============================================================
    CPU SCHEDULING VISUALIZATION SIMULATOR
============================================================

PROCESS INPUT OPTIONS:
1. Use sample processes
2. Generate random processes
3. Manually input processes

Select algorithm (1-3): 1

PROCESS LIST:
----------------------------------------
Process    Arrival      Burst     
----------------------------------------
P1         0           6         
P2         2           4         
P3         4           2         
P4         6           8         
P5         8           3         

============================================================
SCHEDULING ALGORITHMS
============================================================
1. First-Come First-Served (FCFS)
2. Shortest Job First (Non-Preemptive)
3. Shortest Remaining Time First (Preemptive)
4. Round Robin
5. Multilevel Feedback Queue (MLFQ)
6. Exit

Select algorithm (1-6): 1

FIRST-COME FIRST-SERVED (FCFS) SCHEDULING
==================================================

============================================================
SCHEDULING RESULTS
============================================================

GANTT CHART:
------------------------------------------------------------
| P1 | P1 | P1 | P1 | P1 | P1 | P2 | P2 | P2 | P2 | P3 | P3 | P4 | P4 | P4 | P4 | P4 | P4 | P4 | P4 | P5 | P5 | P5 |
0    1    2    3    4    5    6    7    8    9    10   11   12   13   14   15   16   17   18   19   20   21   22   23

PROCESS DETAILS:
--------------------------------------------------------------------------------
Process    Arrival      Burst       Completion   Turnaround   Response    
--------------------------------------------------------------------------------
P1         0           6           6            6            0           
P2         2           4           10           8            4           
P3         4           2           12           8            6           
P4         6           8           20           14           6           
P5         8           3           23           15           12          
--------------------------------------------------------------------------------
                                    AVERAGE:    10.20        5.60        
```

## Technical Assumptions

- All processes are CPU-bound (no I/O operations)
- No context switching overhead
- Single-core processor
- All processes are known at simulation start
- Valid input data (no negative values)
- Processes arrive at different times

## Performance Metrics Explained

- **Completion Time**: Time when process finishes execution
- **Turnaround Time**: Total time from arrival to completion
- **Response Time**: Time from arrival to first CPU burst
- **Average Turnaround Time**: Mean turnaround time across all processes
- **Average Response Time**: Mean response time across all processes

## Contributing

This is a educational project demonstrating CPU scheduling algorithms. Feel free to extend it with additional algorithms or features.

## License

This project is open source and available for educational use. 
