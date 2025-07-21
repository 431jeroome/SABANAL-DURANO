import java.util.*;
import java.util.Scanner;

public class CPUSchedulingVisualization {
    private static Scanner scanner = new Scanner(System.in);
    
    public static void main(String[] args) {
        System.out.println("=".repeat(60));
        System.out.println("    CPU SCHEDULING VISUALIZATION SIMULATOR");
        System.out.println("=".repeat(60));
        
        List<Process> processes = getProcesses();
        
        while (true) {
            displayMenu();
            int choice = getValidChoice(1, 6);
            
            if (choice == 6) {
                System.out.println("\nThank you for using CPU Scheduling Visualization!");
                break;
            }
            
            runSchedulingAlgorithm(choice, processes);
            
            System.out.println("\nPress Enter to continue...");
            scanner.nextLine();
        }
        
        scanner.close();
    }
    
    private static List<Process> getProcesses() {
        System.out.println("\nPROCESS INPUT OPTIONS:");
        System.out.println("1. Use sample processes");
        System.out.println("2. Generate random processes");
        System.out.println("3. Manually input processes");
        
        int choice = getValidChoice(1, 3);
        
        switch (choice) {
            case 1:
                return ProcessGenerator.generateSampleProcesses();
            case 2:
                System.out.print("Enter number of processes to generate: ");
                int numProcesses = getValidPositiveInteger();
                return ProcessGenerator.generateRandomProcesses(numProcesses);
            case 3:
                return getManualProcesses();
            default:
                return ProcessGenerator.generateSampleProcesses();
        }
    }
    
    private static List<Process> getManualProcesses() {
        List<Process> processes = new ArrayList<>();
        
        System.out.print("Enter number of processes: ");
        int numProcesses = getValidPositiveInteger();
        
        for (int i = 1; i <= numProcesses; i++) {
            System.out.println("\nProcess " + i + ":");
            System.out.print("Enter arrival time: ");
            int arrivalTime = getValidNonNegativeInteger();
            System.out.print("Enter burst time: ");
            int burstTime = getValidPositiveInteger();
            
            processes.add(new Process(i, arrivalTime, burstTime));
        }
        
        return processes;
    }
    
    private static void displayMenu() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("SCHEDULING ALGORITHMS");
        System.out.println("=".repeat(60));
        System.out.println("1. First-Come First-Served (FCFS)");
        System.out.println("2. Shortest Job First (Non-Preemptive)");
        System.out.println("3. Shortest Remaining Time First (Preemptive)");
        System.out.println("4. Round Robin");
        System.out.println("5. Multilevel Feedback Queue (MLFQ)");
        System.out.println("6. Exit");
        System.out.print("\nSelect algorithm (1-6): ");
    }
    
    private static void runSchedulingAlgorithm(int choice, List<Process> processes) {
        ProcessGenerator.displayProcesses(processes);
        
        Scheduler scheduler = null;
        
        switch (choice) {
            case 1:
                scheduler = new FCFSScheduler(processes);
                break;
            case 2:
                scheduler = new SJFScheduler(processes);
                break;
            case 3:
                scheduler = new SRTFScheduler(processes);
                break;
            case 4:
                System.out.print("Enter time quantum for Round Robin: ");
                int timeQuantum = getValidPositiveInteger();
                scheduler = new RoundRobinScheduler(processes, timeQuantum);
                break;
            case 5:
                System.out.print("Enter time quantum for MLFQ: ");
                int mlfqTimeQuantum = getValidPositiveInteger();
                System.out.print("Enter allotment time for MLFQ: ");
                int allotmentTime = getValidPositiveInteger();
                scheduler = new MLFQScheduler(processes, mlfqTimeQuantum, allotmentTime);
                break;
        }
        
        if (scheduler != null) {
            scheduler.schedule();
            scheduler.displayResults();
        }
    }
    
    private static int getValidChoice(int min, int max) {
        while (true) {
            try {
                int choice = Integer.parseInt(scanner.nextLine().trim());
                if (choice >= min && choice <= max) {
                    return choice;
                } else {
                    System.out.printf("Please enter a number between %d and %d: ", min, max);
                }
            } catch (NumberFormatException e) {
                System.out.printf("Please enter a valid number between %d and %d: ", min, max);
            }
        }
    }
    
    private static int getValidPositiveInteger() {
        while (true) {
            try {
                int value = Integer.parseInt(scanner.nextLine().trim());
                if (value > 0) {
                    return value;
                } else {
                    System.out.print("Please enter a positive number: ");
                }
            } catch (NumberFormatException e) {
                System.out.print("Please enter a valid positive number: ");
            }
        }
    }
    
    private static int getValidNonNegativeInteger() {
        while (true) {
            try {
                int value = Integer.parseInt(scanner.nextLine().trim());
                if (value >= 0) {
                    return value;
                } else {
                    System.out.print("Please enter a non-negative number: ");
                }
            } catch (NumberFormatException e) {
                System.out.print("Please enter a valid non-negative number: ");
            }
        }
    }
} 