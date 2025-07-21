import java.util.*;

public class SRTFScheduler extends Scheduler {
    
    public SRTFScheduler(List<Process> processes) {
        super(processes);
    }

    @Override
    public void schedule() {
        System.out.println("\nSHORTEST REMAINING TIME FIRST (PREEMPTIVE) SCHEDULING");
        System.out.println("=".repeat(60));
        
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
            
            // Select the process with shortest remaining time
            Process selectedProcess = arrivedProcesses.stream()
                .min((p1, p2) -> Integer.compare(p1.getRemainingTime(), p2.getRemainingTime()))
                .orElse(null);
            
            if (selectedProcess != null) {
                // Set start time if process hasn't started yet
                if (!selectedProcess.isStarted()) {
                    selectedProcess.setStartTime(currentTime);
                    selectedProcess.setStarted(true);
                }
                
                // Execute for 1 time unit
                addToGanttChart(selectedProcess, 1);
                currentTime++;
                selectedProcess.setRemainingTime(selectedProcess.getRemainingTime() - 1);
                
                // Check if process is completed
                if (selectedProcess.getRemainingTime() == 0) {
                    selectedProcess.setCompletionTime(currentTime);
                    remainingProcesses.remove(selectedProcess);
                }
            }
        }
    }
} 