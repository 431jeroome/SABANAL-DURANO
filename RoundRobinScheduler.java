import java.util.*;

public class RoundRobinScheduler extends Scheduler {
    private int timeQuantum;
    
    public RoundRobinScheduler(List<Process> processes, int timeQuantum) {
        super(processes);
        this.timeQuantum = timeQuantum;
    }

    @Override
    public void schedule() {
        System.out.println("\nROUND ROBIN SCHEDULING (Time Quantum: " + timeQuantum + ")");
        System.out.println("=".repeat(50));
        
        List<Process> remainingProcesses = new ArrayList<>(processes);
        Queue<Process> readyQueue = new LinkedList<>();
        currentTime = 0;
        
        // Sort processes by arrival time
        remainingProcesses.sort((p1, p2) -> Integer.compare(p1.getArrivalTime(), p2.getArrivalTime()));
        
        while (!remainingProcesses.isEmpty() || !readyQueue.isEmpty()) {
            // Add arrived processes to ready queue
            Iterator<Process> iterator = remainingProcesses.iterator();
            while (iterator.hasNext()) {
                Process p = iterator.next();
                if (p.getArrivalTime() <= currentTime) {
                    readyQueue.add(p);
                    iterator.remove();
                }
            }
            
            if (readyQueue.isEmpty()) {
                // No process in ready queue, wait for next arrival
                if (!remainingProcesses.isEmpty()) {
                    Process nextProcess = remainingProcesses.get(0);
                    addToGanttChart("IDLE", nextProcess.getArrivalTime() - currentTime);
                    currentTime = nextProcess.getArrivalTime();
                }
                continue;
            }
            
            // Get process from ready queue
            Process currentProcess = readyQueue.poll();
            
            // Set start time if process hasn't started yet
            if (!currentProcess.isStarted()) {
                currentProcess.setStartTime(currentTime);
                currentProcess.setStarted(true);
            }
            
            // Execute process for time quantum or remaining time, whichever is smaller
            int executionTime = Math.min(timeQuantum, currentProcess.getRemainingTime());
            addToGanttChart(currentProcess, executionTime);
            currentTime += executionTime;
            currentProcess.setRemainingTime(currentProcess.getRemainingTime() - executionTime);
            
            // Check if process is completed
            if (currentProcess.getRemainingTime() == 0) {
                currentProcess.setCompletionTime(currentTime);
            } else {
                // Add back to ready queue
                readyQueue.add(currentProcess);
            }
        }
    }
} 