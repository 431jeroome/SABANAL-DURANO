import java.util.*;

public class FCFSScheduler extends Scheduler {
    
    public FCFSScheduler(List<Process> processes) {
        super(processes);
    }

    @Override
    public void schedule() {
        System.out.println("\nFIRST-COME FIRST-SERVED (FCFS) SCHEDULING");
        System.out.println("=".repeat(50));
        
        // Sort processes by arrival time
        processes.sort((p1, p2) -> Integer.compare(p1.getArrivalTime(), p2.getArrivalTime()));
        
        currentTime = 0;
        
        for (Process process : processes) {
            // Wait until process arrives
            if (currentTime < process.getArrivalTime()) {
                addToGanttChart("IDLE", process.getArrivalTime() - currentTime);
                currentTime = process.getArrivalTime();
            }
            
            // Set start time if process hasn't started yet
            if (!process.isStarted()) {
                process.setStartTime(currentTime);
                process.setStarted(true);
            }
            
            // Execute the process
            addToGanttChart(process, process.getBurstTime());
            currentTime += process.getBurstTime();
            process.setCompletionTime(currentTime);
        }
    }
} 