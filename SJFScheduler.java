import java.util.*;

public class SJFScheduler extends Scheduler {
    
    public SJFScheduler(List<Process> processes) {
        super(processes);
    }

    @Override
    public void schedule() {
        System.out.println("\nSHORTEST JOB FIRST (NON-PREEMPTIVE) SCHEDULING");
        System.out.println("=".repeat(55));
        
        List<Process> remainingProcesses = new ArrayList<>(processes);
        currentTime = 0;
        
        while (!remainingProcesses.isEmpty()) {
            // Find processes that have arrived
            List<Process> arrivedProcesses = new ArrayList<>();
            for (Process p : remainingProcesses) {
                if (p.getArrivalTime() <= currentTime) {
                    arrivedProcesses.add(p);
                }
            }
            
            if (arrivedProcesses.isEmpty()) {
                // No process has arrived yet, wait
                Process nextProcess = remainingProcesses.stream()
                    .min((p1, p2) -> Integer.compare(p1.getArrivalTime(), p2.getArrivalTime()))
                    .orElse(null);
                
                if (nextProcess != null) {
                    addToGanttChart("IDLE", nextProcess.getArrivalTime() - currentTime);
                    currentTime = nextProcess.getArrivalTime();
                }
                continue;
            }
            
            // Select the process with shortest burst time
            Process selectedProcess = arrivedProcesses.stream()
                .min((p1, p2) -> Integer.compare(p1.getBurstTime(), p2.getBurstTime()))
                .orElse(null);
            
            if (selectedProcess != null) {
                // Set start time if process hasn't started yet
                if (!selectedProcess.isStarted()) {
                    selectedProcess.setStartTime(currentTime);
                    selectedProcess.setStarted(true);
                }
                
                // Execute the process
                addToGanttChart(selectedProcess, selectedProcess.getBurstTime());
                currentTime += selectedProcess.getBurstTime();
                selectedProcess.setCompletionTime(currentTime);
                
                // Remove from remaining processes
                remainingProcesses.remove(selectedProcess);
            }
        }
    }
} 