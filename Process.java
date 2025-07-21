public class Process {
    private int processId;
    private int arrivalTime;
    private int burstTime;
    private int remainingTime;
    private int completionTime;
    private int turnaroundTime;
    private int responseTime;
    private int startTime;
    private boolean isStarted;
    private int priorityLevel; // For MLFQ

    public Process(int processId, int arrivalTime, int burstTime) {
        this.processId = processId;
        this.arrivalTime = arrivalTime;
        this.burstTime = burstTime;
        this.remainingTime = burstTime;
        this.completionTime = 0;
        this.turnaroundTime = 0;
        this.responseTime = 0;
        this.startTime = -1;
        this.isStarted = false;
        this.priorityLevel = 0; // Start at highest priority for MLFQ
    }

    // Getters and Setters
    public int getProcessId() { return processId; }
    public int getArrivalTime() { return arrivalTime; }
    public int getBurstTime() { return burstTime; }
    public int getRemainingTime() { return remainingTime; }
    public void setRemainingTime(int remainingTime) { this.remainingTime = remainingTime; }
    public int getCompletionTime() { return completionTime; }
    public void setCompletionTime(int completionTime) { this.completionTime = completionTime; }
    public int getTurnaroundTime() { return turnaroundTime; }
    public void setTurnaroundTime(int turnaroundTime) { this.turnaroundTime = turnaroundTime; }
    public int getResponseTime() { return responseTime; }
    public void setResponseTime(int responseTime) { this.responseTime = responseTime; }
    public int getStartTime() { return startTime; }
    public void setStartTime(int startTime) { this.startTime = startTime; }
    public boolean isStarted() { return isStarted; }
    public void setStarted(boolean started) { isStarted = started; }
    public int getPriorityLevel() { return priorityLevel; }
    public void setPriorityLevel(int priorityLevel) { this.priorityLevel = priorityLevel; }

    public void calculateMetrics() {
        if (completionTime > 0) {
            turnaroundTime = completionTime - arrivalTime;
            responseTime = startTime - arrivalTime;
        }
    }

    @Override
    public String toString() {
        return "P" + processId;
    }
} 