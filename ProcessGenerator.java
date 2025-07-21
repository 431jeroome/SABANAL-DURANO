import java.util.*;

public class ProcessGenerator {
    private static Random random = new Random();
    
    public static List<Process> generateRandomProcesses(int numProcesses) {
        List<Process> processes = new ArrayList<>();
        
        for (int i = 1; i <= numProcesses; i++) {
            int arrivalTime = random.nextInt(10); // 0-9
            int burstTime = random.nextInt(20) + 1; // 1-20
            processes.add(new Process(i, arrivalTime, burstTime));
        }
        
        return processes;
    }
    
    public static List<Process> generateSampleProcesses() {
        List<Process> processes = new ArrayList<>();
        
        // Sample processes for demonstration
        processes.add(new Process(1, 0, 6));
        processes.add(new Process(2, 2, 4));
        processes.add(new Process(3, 4, 2));
        processes.add(new Process(4, 6, 8));
        processes.add(new Process(5, 8, 3));
        
        return processes;
    }
    
    public static void displayProcesses(List<Process> processes) {
        System.out.println("\nPROCESS LIST:");
        System.out.println("-".repeat(40));
        System.out.printf("%-10s %-12s %-10s%n", "Process", "Arrival", "Burst");
        System.out.println("-".repeat(40));
        
        for (Process p : processes) {
            System.out.printf("%-10s %-12d %-10d%n", 
                             "P" + p.getProcessId(), p.getArrivalTime(), p.getBurstTime());
        }
        System.out.println();
    }
} 