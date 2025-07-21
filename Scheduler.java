import java.util.*;

public abstract class Scheduler {
    protected List<Process> processes;
    protected List<String> ganttChart;
    protected int currentTime;
    protected double avgTurnaroundTime;
    protected double avgResponseTime;

    public Scheduler(List<Process> processes) {
        this.processes = new ArrayList<>();
        for (Process p : processes) {
            this.processes.add(new Process(p.getProcessId(), p.getArrivalTime(), p.getBurstTime()));
        }
        this.ganttChart = new ArrayList<>();
        this.currentTime = 0;
    }

    public abstract void schedule();

    public void displayResults() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("SCHEDULING RESULTS");
        System.out.println("=".repeat(60));
        
        // Display Gantt Chart
        System.out.println("\nGANTT CHART:");
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

    protected void calculateAverages() {
        double totalTurnaround = 0;
        double totalResponse = 0;
        
        for (Process p : processes) {
            totalTurnaround += p.getTurnaroundTime();
            totalResponse += p.getResponseTime();
        }
        
        avgTurnaroundTime = totalTurnaround / processes.size();
        avgResponseTime = totalResponse / processes.size();
    }

    protected void addToGanttChart(Process process, int duration) {
        for (int i = 0; i < duration; i++) {
            ganttChart.add(process.toString());
        }
    }

    protected void addToGanttChart(String process, int duration) {
        for (int i = 0; i < duration; i++) {
            ganttChart.add(process);
        }
    }
} 