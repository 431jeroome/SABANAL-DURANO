import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;
import javax.swing.Timer;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class CPUSimulator extends JFrame {
    private List<Process> processes;
    private JTable processTable;
    private DefaultTableModel tableModel;
    private GanttChart3D ganttChart3D;
    private JTextArea resultsArea;
    private JComboBox<String> algorithmCombo;
    private JSpinner timeQuantumSpinner;
    private JSpinner allotmentTimeSpinner;
    private JSpinner numProcessesSpinner;
    private JSlider simulationSpeedSlider;
    private JLabel speedLabel;
    
    // Button references
    private JButton loadSampleBtn;
    private JButton generateRandomBtn;
    private JButton addProcessBtn;
    private JButton removeProcessBtn;
    private JButton clearBtn;
    private JButton runBtn;
    
    public CPUSimulator() {
        setTitle("CPU Scheduling Simulator");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1400, 900);
        setLocationRelativeTo(null);
        
        // Set dark theme
        setDarkTheme();
        
        processes = new ArrayList<>();
        initializeComponents();
        setupLayout();
        setupEventHandlers();
        
        // Load sample processes by default
        loadSampleProcesses();
    }
    
    private void setDarkTheme() {
        // Set dark background
        setBackground(new Color(20, 20, 20));
        
        // Set dark theme for all components
        UIManager.put("Panel.background", new Color(20, 20, 20));
        UIManager.put("Panel.foreground", Color.WHITE);
        UIManager.put("Label.background", new Color(20, 20, 20));
        UIManager.put("Label.foreground", Color.WHITE);
        UIManager.put("Button.background", new Color(80, 80, 80));
        UIManager.put("Button.foreground", Color.BLACK);
        UIManager.put("Button.border", BorderFactory.createLineBorder(new Color(173, 216, 230), 2));
        UIManager.put("TextField.background", new Color(30, 30, 30));
        UIManager.put("TextField.foreground", Color.WHITE);
        UIManager.put("TextField.caretForeground", new Color(173, 216, 230));
        UIManager.put("TextArea.background", new Color(200, 200, 200));
        UIManager.put("TextArea.foreground", Color.BLACK);
        UIManager.put("Table.background", new Color(30, 30, 30));
        UIManager.put("Table.foreground", Color.WHITE);
        UIManager.put("Table.gridColor", new Color(173, 216, 230));
        UIManager.put("TableHeader.background", new Color(200, 200, 200));
        UIManager.put("TableHeader.foreground", Color.BLACK);
        UIManager.put("ComboBox.background", new Color(200, 200, 200));
        UIManager.put("ComboBox.foreground", Color.BLACK);
        UIManager.put("ComboBox.selectionBackground", new Color(173, 216, 230));
        UIManager.put("ComboBox.selectionForeground", Color.BLACK);
        UIManager.put("Spinner.background", new Color(30, 30, 30));
        UIManager.put("Spinner.foreground", Color.WHITE);
        UIManager.put("TabbedPane.background", new Color(20, 20, 20));
        UIManager.put("TabbedPane.foreground", Color.BLACK);
        UIManager.put("TabbedPane.selected", new Color(173, 216, 230));
        UIManager.put("ScrollPane.background", new Color(20, 20, 20));
        UIManager.put("ScrollPane.border", BorderFactory.createLineBorder(new Color(173, 216, 230), 1));
    }
    
    private void initializeComponents() {
        // Process table
        String[] columnNames = {"Process ID", "Arrival Time", "Burst Time"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column > 0; // Only arrival and burst time are editable
            }
        };
        processTable = new JTable(tableModel);
        processTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        // Style the table headers specifically
        processTable.getTableHeader().setBackground(new Color(200, 200, 200));
        processTable.getTableHeader().setForeground(Color.BLACK);
        processTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        
        // Algorithm selection
        String[] algorithms = {"First-Come First-Served (FCFS)", 
                             "Shortest Job First (SJF)", 
                             "Shortest Remaining Time First (SRTF)", 
                             "Round Robin", 
                             "Multilevel Feedback Queue (MLFQ)"};
        algorithmCombo = new JComboBox<>(algorithms);
        
        // Style the combo box specifically
        algorithmCombo.setBackground(new Color(200, 200, 200));
        algorithmCombo.setForeground(Color.BLACK);
        algorithmCombo.setFont(new Font("Arial", Font.BOLD, 12));
        
        // Spinners
        SpinnerNumberModel quantumModel = new SpinnerNumberModel(2, 1, 20, 1);
        timeQuantumSpinner = new JSpinner(quantumModel);
        
        SpinnerNumberModel allotmentModel = new SpinnerNumberModel(5, 1, 20, 1);
        allotmentTimeSpinner = new JSpinner(allotmentModel);
        
        SpinnerNumberModel processModel = new SpinnerNumberModel(5, 1, 20, 1);
        numProcessesSpinner = new JSpinner(processModel);
        
        // Simulation speed slider
        simulationSpeedSlider = new JSlider(JSlider.HORIZONTAL, 1, 10, 5);
        simulationSpeedSlider.setMajorTickSpacing(1);
        simulationSpeedSlider.setPaintTicks(true);
        simulationSpeedSlider.setPaintLabels(true);
        simulationSpeedSlider.setBackground(new Color(20, 20, 20));
        simulationSpeedSlider.setForeground(new Color(173, 216, 230));
        
        speedLabel = new JLabel("Simulation Speed: 5");
        speedLabel.setForeground(new Color(173, 216, 230));
        speedLabel.setFont(new Font("Arial", Font.BOLD, 12));
        
        // 3D Gantt chart component
        ganttChart3D = new GanttChart3D(new ArrayList<>());
        
        // Results text area
        resultsArea = new JTextArea();
        resultsArea.setEditable(false);
        resultsArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        resultsArea.setBackground(new Color(200, 200, 200));
        resultsArea.setForeground(Color.BLACK);
        resultsArea.setCaretColor(Color.BLACK);
        
        // Create buttons
        loadSampleBtn = new JButton("Load Sample");
        generateRandomBtn = new JButton("Generate Random");
        addProcessBtn = new JButton("Add Process");
        removeProcessBtn = new JButton("Remove Process");
        clearBtn = new JButton("Clear All");
        runBtn = new JButton("Run Simulation");
        
        // Style buttons with dark theme
        styleButton(loadSampleBtn);
        styleButton(generateRandomBtn);
        styleButton(addProcessBtn);
        styleButton(removeProcessBtn);
        styleButton(clearBtn);
        styleButton(runBtn);
        
        // Special styling for run button
        runBtn.setBackground(new Color(173, 216, 230));
        runBtn.setForeground(Color.BLACK);
        runBtn.setFont(new Font("Arial", Font.BOLD, 14));
        runBtn.setBorder(BorderFactory.createLineBorder(new Color(255, 255, 255), 2));
        
        // Add hover effect for run button
        runBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                runBtn.setBackground(new Color(135, 206, 235));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                runBtn.setBackground(new Color(173, 216, 230));
            }
        });
    }
    
    private void setupLayout() {
        setLayout(new BorderLayout());
        
        // Main container with scroll pane
        JPanel mainPanel = new JPanel(new BorderLayout());
        
        // Left panel - Controls and Process Table
        JPanel leftPanel = createLeftPanel();
        
        // Right panel - Results
        JPanel rightPanel = createRightPanel();
        
        // Split pane
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftPanel, rightPanel);
        splitPane.setDividerLocation(500);
        splitPane.setResizeWeight(0.4);
        
        mainPanel.add(splitPane, BorderLayout.CENTER);
        add(mainPanel, BorderLayout.CENTER);
    }
    
    private JPanel createLeftPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.setBackground(new Color(20, 20, 20));
        
        // Title
        JLabel titleLabel = new JLabel("CPU Scheduling Simulator", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(new Color(173, 216, 230));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        panel.add(titleLabel, BorderLayout.NORTH);
        
        // Process management panel
        JPanel processPanel = createProcessManagementPanel();
        panel.add(processPanel, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createProcessManagementPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Process Management"));
        panel.setBackground(new Color(20, 20, 20));
        
        // Style the titled border
        TitledBorder border = (TitledBorder) panel.getBorder();
        border.setTitleColor(new Color(173, 216, 230));
        border.setTitleFont(new Font("Arial", Font.BOLD, 14));
        panel.setBorder(border);
        
        // Process table
        JScrollPane tableScrollPane = new JScrollPane(processTable);
        tableScrollPane.setPreferredSize(new Dimension(450, 200));
        tableScrollPane.setBackground(new Color(20, 20, 20));
        panel.add(tableScrollPane, BorderLayout.CENTER);
        
        // Buttons panel
        JPanel buttonsPanel = new JPanel(new FlowLayout());
        buttonsPanel.setBackground(new Color(20, 20, 20));
        
        buttonsPanel.add(loadSampleBtn);
        buttonsPanel.add(generateRandomBtn);
        buttonsPanel.add(addProcessBtn);
        buttonsPanel.add(removeProcessBtn);
        buttonsPanel.add(clearBtn);
        
        panel.add(buttonsPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private JPanel createRightPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.setBackground(new Color(20, 20, 20));
        
        // Algorithm selection panel
        JPanel algorithmPanel = createAlgorithmPanel();
        panel.add(algorithmPanel, BorderLayout.NORTH);
        
        // Results panel
        JPanel resultsContainer = new JPanel(new BorderLayout());
        resultsContainer.setBorder(BorderFactory.createTitledBorder("Simulation Results"));
        resultsContainer.setBackground(new Color(20, 20, 20));
        
        // Style the titled border
        TitledBorder border = (TitledBorder) resultsContainer.getBorder();
        border.setTitleColor(new Color(173, 216, 230));
        border.setTitleFont(new Font("Arial", Font.BOLD, 14));
        resultsContainer.setBorder(border);
        
        // Tabbed pane for Gantt chart and results
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setBackground(new Color(20, 20, 20));
        tabbedPane.setForeground(Color.BLACK);
        
        // Gantt chart tab
        JScrollPane ganttScrollPane = new JScrollPane(ganttChart3D);
        ganttScrollPane.setPreferredSize(new Dimension(600, 300));
        ganttScrollPane.setBackground(new Color(20, 20, 20));
        tabbedPane.addTab("Gantt Chart", ganttScrollPane);
        
        // Results tab
        JScrollPane resultsScrollPane = new JScrollPane(resultsArea);
        resultsScrollPane.setPreferredSize(new Dimension(600, 300));
        resultsScrollPane.setBackground(new Color(20, 20, 20));
        tabbedPane.addTab("Process Details", resultsScrollPane);
        
        resultsContainer.add(tabbedPane, BorderLayout.CENTER);
        panel.add(resultsContainer, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createAlgorithmPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Algorithm Selection"));
        panel.setBackground(new Color(20, 20, 20));
        
        // Style the titled border
        TitledBorder border = (TitledBorder) panel.getBorder();
        border.setTitleColor(new Color(173, 216, 230));
        border.setTitleFont(new Font("Arial", Font.BOLD, 14));
        panel.setBorder(border);
        
        // Algorithm selection
        JPanel selectionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        selectionPanel.setBackground(new Color(20, 20, 20));
        JLabel algoLabel = new JLabel("Algorithm:");
        algoLabel.setForeground(Color.WHITE);
        selectionPanel.add(algoLabel);
        selectionPanel.add(algorithmCombo);
        
        // Parameters panel
        JPanel paramsPanel = new JPanel(new GridLayout(2, 2, 10, 5));
        paramsPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        paramsPanel.setBackground(new Color(20, 20, 20));
        
        JLabel quantumLabel = new JLabel("Time Quantum:");
        quantumLabel.setForeground(Color.WHITE);
        JLabel allotmentLabel = new JLabel("Allotment Time (MLFQ):");
        allotmentLabel.setForeground(Color.WHITE);
        
        paramsPanel.add(quantumLabel);
        paramsPanel.add(timeQuantumSpinner);
        paramsPanel.add(allotmentLabel);
        paramsPanel.add(allotmentTimeSpinner);
        
        // Number of processes for random generation
        JPanel randomPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        randomPanel.setBackground(new Color(20, 20, 20));
        JLabel processLabel = new JLabel("Number of Processes:");
        processLabel.setForeground(Color.WHITE);
        randomPanel.add(processLabel);
        randomPanel.add(numProcessesSpinner);
        
        // Simulation speed panel
        JPanel speedPanel = new JPanel(new BorderLayout());
        speedPanel.setBackground(new Color(20, 20, 20));
        speedPanel.add(speedLabel, BorderLayout.NORTH);
        speedPanel.add(simulationSpeedSlider, BorderLayout.CENTER);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(new Color(20, 20, 20));
        buttonPanel.add(runBtn);
        
        // Combine all panels
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(new Color(20, 20, 20));
        topPanel.add(selectionPanel, BorderLayout.NORTH);
        topPanel.add(paramsPanel, BorderLayout.CENTER);
        
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBackground(new Color(20, 20, 20));
        bottomPanel.add(randomPanel, BorderLayout.NORTH);
        bottomPanel.add(speedPanel, BorderLayout.CENTER);
        
        topPanel.add(bottomPanel, BorderLayout.SOUTH);
        
        panel.add(topPanel, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private void setupEventHandlers() {
        // Algorithm combo box listener
        algorithmCombo.addActionListener(e -> {
            String selected = (String) algorithmCombo.getSelectedItem();
            boolean isRR = selected.contains("Round Robin");
            boolean isMLFQ = selected.contains("MLFQ");
            
            timeQuantumSpinner.setEnabled(isRR || isMLFQ);
            allotmentTimeSpinner.setEnabled(isMLFQ);
        });
        
        // Simulation speed slider listener
        simulationSpeedSlider.addChangeListener(e -> {
            int speed = simulationSpeedSlider.getValue();
            speedLabel.setText("Simulation Speed: " + speed);
        });
        
        // Table model listener for automatic updates
        tableModel.addTableModelListener(e -> {
            if (e.getColumn() >= 0) {
                updateProcessesFromTable();
            }
        });
        
        // Button event handlers
        loadSampleBtn.addActionListener(e -> loadSampleProcesses());
        generateRandomBtn.addActionListener(e -> generateRandomProcesses());
        addProcessBtn.addActionListener(e -> addProcess());
        removeProcessBtn.addActionListener(e -> removeProcess());
        clearBtn.addActionListener(e -> clearProcesses());
        runBtn.addActionListener(e -> runSimulation());
    }
    
    private void styleButton(JButton button) {
        button.setBackground(new Color(80, 80, 80));
        button.setForeground(Color.BLACK);
        button.setBorder(BorderFactory.createLineBorder(new Color(173, 216, 230), 2));
        button.setFocusPainted(false);
        button.setFont(new Font("Arial", Font.BOLD, 12));
        
        // Add hover effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(100, 100, 100));
                button.setForeground(Color.WHITE);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(80, 80, 80));
                button.setForeground(Color.BLACK);
            }
        });
    }
    
    private void loadSampleProcesses() {
        processes = ProcessGenerator.generateSampleProcesses();
        updateTableFromProcesses();
    }
    
    private void generateRandomProcesses() {
        int numProcesses = (Integer) numProcessesSpinner.getValue();
        processes = ProcessGenerator.generateRandomProcesses(numProcesses);
        updateTableFromProcesses();
    }
    
    private void addProcess() {
        String processId = "P" + (processes.size() + 1);
        processes.add(new Process(processes.size() + 1, 0, 1));
        updateTableFromProcesses();
    }
    
    private void removeProcess() {
        int selectedRow = processTable.getSelectedRow();
        if (selectedRow >= 0 && selectedRow < processes.size()) {
            processes.remove(selectedRow);
            updateTableFromProcesses();
        }
    }
    
    private void clearProcesses() {
        processes.clear();
        updateTableFromProcesses();
    }
    
    private void updateTableFromProcesses() {
        tableModel.setRowCount(0);
        for (Process p : processes) {
            tableModel.addRow(new Object[]{p.getProcessId(), p.getArrivalTime(), p.getBurstTime()});
        }
    }
    
    private void updateProcessesFromTable() {
        processes.clear();
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            try {
                int processId = i + 1;
                int arrivalTime = Integer.parseInt(tableModel.getValueAt(i, 1).toString());
                int burstTime = Integer.parseInt(tableModel.getValueAt(i, 2).toString());
                processes.add(new Process(processId, arrivalTime, burstTime));
            } catch (NumberFormatException ex) {
                // Ignore invalid entries
            }
        }
    }
    
    private void runSimulation() {
        if (processes.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please add some processes first!", "No Processes", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String selectedAlgorithm = (String) algorithmCombo.getSelectedItem();
        Scheduler scheduler = null;
        
        try {
            switch (selectedAlgorithm) {
                case "First-Come First-Served (FCFS)":
                    scheduler = new FCFSScheduler(processes);
                    break;
                case "Shortest Job First (SJF)":
                    scheduler = new SJFScheduler(processes);
                    break;
                case "Shortest Remaining Time First (SRTF)":
                    scheduler = new SRTFScheduler(processes);
                    break;
                case "Round Robin":
                    int timeQuantum = (Integer) timeQuantumSpinner.getValue();
                    scheduler = new RoundRobinScheduler(processes, timeQuantum);
                    break;
                case "Multilevel Feedback Queue (MLFQ)":
                    int mlfqTimeQuantum = (Integer) timeQuantumSpinner.getValue();
                    int allotmentTime = (Integer) allotmentTimeSpinner.getValue();
                    scheduler = new MLFQScheduler(processes, mlfqTimeQuantum, allotmentTime);
                    break;
            }
            
            if (scheduler != null) {
                // Capture output
                scheduler.schedule();
                displayResults(scheduler);
            }
            
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error running simulation: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void displayResults(Scheduler scheduler) {
        // Update the 3D Gantt chart
        ganttChart3D.updateGanttChart(scheduler.ganttChart);
        
        // Create results output
        StringBuilder resultsOutput = new StringBuilder();
        
        // Process details
        resultsOutput.append("PROCESS DETAILS:\n");
        resultsOutput.append("-".repeat(80)).append("\n");
        resultsOutput.append(String.format("%-10s %-12s %-10s %-12s %-12s %-12s%n", 
                         "Process", "Arrival", "Burst", "Completion", "Turnaround", "Response"));
        resultsOutput.append("-".repeat(80)).append("\n");
        
        for (Process p : scheduler.processes) {
            p.calculateMetrics();
            resultsOutput.append(String.format("%-10s %-12d %-10d %-12d %-12d %-12d%n",
                             "P" + p.getProcessId(), p.getArrivalTime(), p.getBurstTime(),
                             p.getCompletionTime(), p.getTurnaroundTime(), p.getResponseTime()));
        }
        
        scheduler.calculateAverages();
        resultsOutput.append("-".repeat(80)).append("\n");
        resultsOutput.append(String.format("%-10s %-12s %-10s %-12s %-12.2f %-12.2f%n",
                         "", "", "", "AVERAGE:", scheduler.avgTurnaroundTime, scheduler.avgResponseTime));
        
        // Update the results area
        resultsArea.setText(resultsOutput.toString());
        
        // Automatically switch to Gantt Chart tab
        JTabbedPane tabbedPane = findTabbedPane();
        if (tabbedPane != null) {
            tabbedPane.setSelectedIndex(0); // Switch to 3D Gantt Chart tab
        }
        
        // Animate the Gantt chart based on simulation speed
        animateGanttChart(scheduler.ganttChart);
    }
    
    private JTabbedPane findTabbedPane() {
        // Find the tabbed pane in the component hierarchy
        for (Component comp : getContentPane().getComponents()) {
            if (comp instanceof JTabbedPane) {
                return (JTabbedPane) comp;
            }
            if (comp instanceof Container) {
                JTabbedPane found = findTabbedPaneInContainer((Container) comp);
                if (found != null) return found;
            }
        }
        return null;
    }
    
    private JTabbedPane findTabbedPaneInContainer(Container container) {
        for (Component comp : container.getComponents()) {
            if (comp instanceof JTabbedPane) {
                return (JTabbedPane) comp;
            }
            if (comp instanceof Container) {
                JTabbedPane found = findTabbedPaneInContainer((Container) comp);
                if (found != null) return found;
            }
        }
        return null;
    }
    
    private void animateGanttChart(List<String> ganttChart) {
        int speed = simulationSpeedSlider.getValue();
        int delay = (11 - speed) * 100; // Faster speed = less delay
        
        // Create a timer to animate the Gantt chart
        Timer timer = new Timer(delay, new ActionListener() {
            int currentIndex = 0;
            
            @Override
            public void actionPerformed(ActionEvent e) {
                if (currentIndex < ganttChart.size()) {
                    // Update Gantt chart progressively
                    List<String> partialChart = ganttChart.subList(0, currentIndex + 1);
                    ganttChart3D.updateGanttChart(partialChart);
                    currentIndex++;
                } else {
                    // Animation complete
                    ((Timer) e.getSource()).stop();
                    // Show final result
                    ganttChart3D.updateGanttChart(ganttChart);
                }
            }
        });
        
        // Start with empty chart
        ganttChart3D.updateGanttChart(new ArrayList<>());
        timer.start();
    }
    
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        SwingUtilities.invokeLater(() -> {
            CPUSimulator simulator = new CPUSimulator();
            simulator.setVisible(true);
        });
    }
} 