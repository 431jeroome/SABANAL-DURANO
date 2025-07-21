import java.util.*;

public class MLFQScheduler extends Scheduler {
    private int timeQuantum;
    private int allotmentTime;
    private List<Queue<Process>> priorityQueues;
    
    public MLFQScheduler(List<Process> processes, int timeQuantum, int allotmentTime) {
        super(processes);
        this.timeQuantum = timeQuantum;
        this.allotmentTime = allotmentTime;
        this.priorityQueues = new ArrayList<>();
        
        // Initialize 4 priority queues (Q0, Q1, Q2, Q3)
        for (int i = 0; i < 4; i++) {
            priorityQueues.add(new LinkedList<>());
        }
    }

    @Override
    public void schedule() {
        System.out.println("\nMULTILEVEL FEEDBACK QUEUE SCHEDULING");
        System.out.println("Time Quantum: " + timeQuantum + ", Allotment Time: " + allotmentTime);
        System.out.println("=".repeat(60));
        
        List<Process> remainingProcesses = new ArrayList<>(processes);
        Map<Process, Integer> processAllotment = new HashMap<>();
        currentTime = 0;
        
        // Sort processes by arrival time
        remainingProcesses.sort((p1, p2) -> Integer.compare(p1.getArrivalTime(), p2.getArrivalTime()));
        
        while (!remainingProcesses.isEmpty() || hasProcessesInQueues()) {
            // Add arrived processes to highest priority queue (Q0)
            Iterator<Process> iterator = remainingProcesses.iterator();
            while (iterator.hasNext()) {
                Process p = iterator.next();
                if (p.getArrivalTime() <= currentTime) {
                    priorityQueues.get(0).add(p);
                    processAllotment.put(p, 0);
                    iterator.remove();
                }
            }
            
            if (!hasProcessesInQueues()) {
                // No process in any queue, wait for next arrival
                if (!remainingProcesses.isEmpty()) {
                    Process nextProcess = remainingProcesses.get(0);
                    addToGanttChart("IDLE", nextProcess.getArrivalTime() - currentTime);
                    currentTime = nextProcess.getArrivalTime();
                }
                continue;
            }
            
            // Find highest priority queue with processes
            int currentQueue = 0;
            while (currentQueue < 4 && priorityQueues.get(currentQueue).isEmpty()) {
                currentQueue++;
            }
            
            if (currentQueue < 4) {
                Process currentProcess = priorityQueues.get(currentQueue).poll();
                
                // Set start time if process hasn't started yet
                if (!currentProcess.isStarted()) {
                    currentProcess.setStartTime(currentTime);
                    currentProcess.setStarted(true);
                }
                
                // Determine execution time based on queue level
                int executionTime;
                if (currentQueue < 3) {
                    // Q0, Q1, Q2: Use time quantum
                    executionTime = Math.min(timeQuantum, currentProcess.getRemainingTime());
                } else {
                    // Q3: Use FCFS (execute until completion)
                    executionTime = currentProcess.getRemainingTime();
                }
                
                // Execute process
                addToGanttChart(currentProcess.toString() + "Q" + currentQueue, executionTime);
                currentTime += executionTime;
                currentProcess.setRemainingTime(currentProcess.getRemainingTime() - executionTime);
                
                // Update allotment time
                int currentAllotment = processAllotment.get(currentProcess) + executionTime;
                processAllotment.put(currentProcess, currentAllotment);
                
                // Check if process is completed
                if (currentProcess.getRemainingTime() == 0) {
                    currentProcess.setCompletionTime(currentTime);
                } else {
                    // Determine next queue level
                    int nextQueue = currentQueue;
                    if (currentAllotment >= allotmentTime && currentQueue < 3) {
                        // Demote to lower priority queue
                        nextQueue = currentQueue + 1;
                        processAllotment.put(currentProcess, 0); // Reset allotment
                    }
                    
                    // Add to appropriate queue
                    priorityQueues.get(nextQueue).add(currentProcess);
                    currentProcess.setPriorityLevel(nextQueue);
                }
            }
        }
    }
    
    private boolean hasProcessesInQueues() {
        for (Queue<Process> queue : priorityQueues) {
            if (!queue.isEmpty()) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public void displayResults() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("SCHEDULING RESULTS");
        System.out.println("=".repeat(60));
        
        // Display Gantt Chart with queue information
        System.out.println("\nGANTT CHART (with Queue Levels):");
        System.out.println("-".repeat(60));
        for (int i = 0; i < ganttChart.size(); i++) {
            System.out.print("| " + ganttChart.get(i) + " ");
        }
        System.out.println("|");
        System.out.print("0");
        for (int i = 1; i <= ganttChart.size(); i++) {
            System.out.printf("%" + (ganttChart.get(i-1).length() + 2) + "d", i);
        }
        System.out.println();
        
        // Display Process Details
        System.out.println("\nPROCESS DETAILS:");
        System.out.println("-".repeat(80));
        System.out.printf("%-10s %-12s %-10s %-12s %-12s %-12s%n", 
                         "Process", "Arrival", "Burst", "Completion", "Turnaround", "Response");
        System.out.println("-".repeat(80));
        
        for (Process p : processes) {
            p.calculateMetrics();
            System.out.printf("%-10s %-12d %-10d %-12d %-12d %-12d%n",
                             "P" + p.getProcessId(), p.getArrivalTime(), p.getBurstTime(),
                             p.getCompletionTime(), p.getTurnaroundTime(), p.getResponseTime());
        }

        // Calculate and display averages
        calculateAverages();
        System.out.println("-".repeat(80));
        System.out.printf("%-10s %-12s %-10s %-12s %-12.2f %-12.2f%n",
                         "", "", "", "AVERAGE:", avgTurnaroundTime, avgResponseTime);
    }
} 