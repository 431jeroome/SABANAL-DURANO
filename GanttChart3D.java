import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;
import java.util.List;

public class GanttChart3D extends JPanel {
    private List<String> ganttChart;
    private int timeUnits;
    private Color[] processColors;
    private static final int BAR_HEIGHT = 40;
    private static final int BAR_DEPTH = 15;
    private static final int MARGIN = 50;
    private static final int TIME_LABEL_HEIGHT = 30;
    private static final int PROCESS_LABEL_WIDTH = 80;
    
    public GanttChart3D(List<String> ganttChart) {
        this.ganttChart = ganttChart;
        this.timeUnits = ganttChart.size();
        initializeColors();
        setPreferredSize(new Dimension(800, 300));
        setBackground(new Color(20, 20, 20));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
    }
    
    private void initializeColors() {
        // Create a palette of distinct colors for processes
        processColors = new Color[]{
            new Color(52, 152, 219),   // Blue
            new Color(46, 204, 113),   // Green
            new Color(155, 89, 182),   // Purple
            new Color(241, 196, 15),   // Yellow
            new Color(231, 76, 60),    // Red
            new Color(26, 188, 156),   // Teal
            new Color(230, 126, 34),   // Orange
            new Color(142, 68, 173),   // Dark Purple
            new Color(52, 73, 94),     // Dark Blue
            new Color(149, 165, 166)   // Gray
        };
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        
        // Enable anti-aliasing for smooth rendering
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        
        if (ganttChart == null || ganttChart.isEmpty()) {
            drawEmptyChart(g2d);
            return;
        }
        
        // Calculate dimensions
        int width = getWidth() - 2 * MARGIN;
        int height = getHeight() - 2 * MARGIN;
        int barWidth = Math.max(20, width / timeUnits);
        
        // Draw background grid
        drawGrid(g2d, width, height, barWidth);
        
        // Draw 3D Gantt bars
        draw3DGanttBars(g2d, width, height, barWidth);
        
        // Draw time labels
        drawTimeLabels(g2d, width, height, barWidth);
        
        // Draw legend
        drawLegend(g2d, width, height);
    }
    
    private void drawEmptyChart(Graphics2D g2d) {
        int width = getWidth();
        int height = getHeight();
        
        g2d.setColor(new Color(200, 200, 200));
        g2d.setFont(new Font("Arial", Font.ITALIC, 16));
        
        String message = "No simulation data available";
        FontMetrics fm = g2d.getFontMetrics();
        int textWidth = fm.stringWidth(message);
        int x = (width - textWidth) / 2;
        int y = height / 2;
        
        g2d.drawString(message, x, y);
    }
    
    private void drawGrid(Graphics2D g2d, int width, int height, int barWidth) {
        g2d.setColor(new Color(173, 216, 230));
        g2d.setStroke(new BasicStroke(1));
        
        // Draw vertical grid lines
        for (int i = 0; i <= timeUnits; i++) {
            int x = MARGIN + i * barWidth;
            g2d.drawLine(x, MARGIN, x, MARGIN + BAR_HEIGHT);
        }
        
        // Draw horizontal grid lines
        g2d.drawLine(MARGIN, MARGIN, MARGIN + width, MARGIN);
        g2d.drawLine(MARGIN, MARGIN + BAR_HEIGHT, MARGIN + width, MARGIN + BAR_HEIGHT);
    }
    
    private void draw3DGanttBars(Graphics2D g2d, int width, int height, int barWidth) {
        // Group consecutive same processes
        String currentProcess = null;
        int startTime = 0;
        int duration = 0;
        
        for (int i = 0; i < ganttChart.size(); i++) {
            String process = ganttChart.get(i);
            
            if (currentProcess == null) {
                currentProcess = process;
                startTime = i;
                duration = 1;
            } else if (process.equals(currentProcess)) {
                duration++;
            } else {
                // Draw the completed bar
                draw3DBar(g2d, currentProcess, startTime, duration, barWidth);
                
                // Start new process
                currentProcess = process;
                startTime = i;
                duration = 1;
            }
        }
        
        // Draw the last bar
        if (currentProcess != null) {
            draw3DBar(g2d, currentProcess, startTime, duration, barWidth);
        }
    }
    
    private void draw3DBar(Graphics2D g2d, String process, int startTime, int duration, int barWidth) {
        if (process.equals("IDLE")) {
            drawIdleBar(g2d, startTime, duration, barWidth);
            return;
        }
        
        int x = MARGIN + startTime * barWidth;
        int y = MARGIN;
        int width = duration * barWidth;
        
        // Get color for process
        Color processColor = getProcessColor(process);
        
        // Create 3D effect with gradient and shadow
        GradientPaint gradient = new GradientPaint(
            x, y, processColor.brighter(),
            x, y + BAR_HEIGHT, processColor.darker()
        );
        
        // Draw main bar (top face)
        g2d.setPaint(gradient);
        g2d.fillRect(x, y, width, BAR_HEIGHT);
        
        // Draw right face (depth)
        Color rightFaceColor = processColor.darker().darker();
        g2d.setColor(rightFaceColor);
        int[] rightX = {x + width, x + width + BAR_DEPTH, x + width + BAR_DEPTH, x + width};
        int[] rightY = {y, y - BAR_DEPTH, y + BAR_HEIGHT - BAR_DEPTH, y + BAR_HEIGHT};
        g2d.fillPolygon(rightX, rightY, 4);
        
        // Draw bottom face
        Color bottomFaceColor = processColor.darker();
        g2d.setColor(bottomFaceColor);
        int[] bottomX = {x, x + BAR_DEPTH, x + width + BAR_DEPTH, x + width};
        int[] bottomY = {y + BAR_HEIGHT, y + BAR_HEIGHT - BAR_DEPTH, y + BAR_HEIGHT - BAR_DEPTH, y + BAR_HEIGHT};
        g2d.fillPolygon(bottomX, bottomY, 4);
        
        // Draw border
        g2d.setColor(Color.BLACK);
        g2d.setStroke(new BasicStroke(1));
        g2d.drawRect(x, y, width, BAR_HEIGHT);
        
        // Draw process label
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Arial", Font.BOLD, 12));
        FontMetrics fm = g2d.getFontMetrics();
        String label = process;
        int textWidth = fm.stringWidth(label);
        int textX = x + (width - textWidth) / 2;
        int textY = y + BAR_HEIGHT / 2 + fm.getAscent() / 2;
        
        // Add text shadow for better readability
        g2d.setColor(new Color(0, 0, 0, 100));
        g2d.drawString(label, textX + 1, textY + 1);
        g2d.setColor(Color.WHITE);
        g2d.drawString(label, textX, textY);
    }
    
    private void drawIdleBar(Graphics2D g2d, int startTime, int duration, int barWidth) {
        int x = MARGIN + startTime * barWidth;
        int y = MARGIN;
        int width = duration * barWidth;
        
        // Create striped pattern for idle time
        g2d.setColor(new Color(200, 200, 200));
        g2d.fillRect(x, y, width, BAR_HEIGHT);
        
        // Draw diagonal stripes
        g2d.setColor(new Color(180, 180, 180));
        g2d.setStroke(new BasicStroke(2));
        for (int i = 0; i < width; i += 8) {
            g2d.drawLine(x + i, y, x + i + 4, y + BAR_HEIGHT);
        }
        
        // Draw border
        g2d.setColor(Color.BLACK);
        g2d.setStroke(new BasicStroke(1));
        g2d.drawRect(x, y, width, BAR_HEIGHT);
        
        // Draw "IDLE" label
        g2d.setColor(Color.BLACK);
        g2d.setFont(new Font("Arial", Font.BOLD, 10));
        FontMetrics fm = g2d.getFontMetrics();
        String label = "IDLE";
        int textWidth = fm.stringWidth(label);
        int textX = x + (width - textWidth) / 2;
        int textY = y + BAR_HEIGHT / 2 + fm.getAscent() / 2;
        g2d.drawString(label, textX, textY);
    }
    
    private void drawTimeLabels(Graphics2D g2d, int width, int height, int barWidth) {
        g2d.setColor(new Color(173, 216, 230));
        g2d.setFont(new Font("Arial", Font.PLAIN, 10));
        
        // Draw time labels below the chart
        for (int i = 0; i <= timeUnits; i += Math.max(1, timeUnits / 10)) {
            int x = MARGIN + i * barWidth;
            int y = MARGIN + BAR_HEIGHT + TIME_LABEL_HEIGHT;
            
            String label = String.valueOf(i);
            FontMetrics fm = g2d.getFontMetrics();
            int textWidth = fm.stringWidth(label);
            g2d.drawString(label, x - textWidth / 2, y);
        }
    }
    
    private void drawLegend(Graphics2D g2d, int width, int height) {
        // Get unique processes
        java.util.Set<String> uniqueProcesses = new java.util.HashSet<>(ganttChart);
        uniqueProcesses.remove("IDLE");
        
        int legendX = MARGIN;
        int legendY = MARGIN + BAR_HEIGHT + TIME_LABEL_HEIGHT + 40;
        int legendItemHeight = 20;
        int legendItemWidth = 100;
        
        g2d.setFont(new Font("Arial", Font.BOLD, 12));
        g2d.setColor(new Color(173, 216, 230));
        g2d.drawString("Process Legend:", legendX, legendY - 10);
        
        int itemCount = 0;
        for (String process : uniqueProcesses) {
            int x = legendX + (itemCount % 3) * legendItemWidth;
            int y = legendY + (itemCount / 3) * legendItemHeight;
            
            // Draw color box
            Color processColor = getProcessColor(process);
            g2d.setColor(processColor);
            g2d.fillRect(x, y - 15, 15, 15);
            g2d.setColor(new Color(173, 216, 230));
            g2d.drawRect(x, y - 15, 15, 15);
            
            // Draw process name
            g2d.setColor(new Color(173, 216, 230));
            g2d.drawString(process, x + 20, y - 3);
            
            itemCount++;
        }
    }
    
    private Color getProcessColor(String process) {
        if (process.equals("IDLE")) {
            return new Color(200, 200, 200);
        }
        
        // Extract process number for consistent coloring
        try {
            String processNum = process.replaceAll("[^0-9]", "");
            if (!processNum.isEmpty()) {
                int index = Integer.parseInt(processNum) % processColors.length;
                return processColors[index];
            }
        } catch (NumberFormatException e) {
            // Fall back to hash-based color
        }
        
        // Use hash-based color for processes without numbers
        int hash = process.hashCode();
        return new Color(
            Math.abs(hash) % 200 + 55,
            Math.abs(hash >> 8) % 200 + 55,
            Math.abs(hash >> 16) % 200 + 55
        );
    }
    
    public void updateGanttChart(List<String> newGanttChart) {
        this.ganttChart = newGanttChart;
        this.timeUnits = newGanttChart.size();
        repaint();
    }
} 