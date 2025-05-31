package org.example;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;

public class FeesManagementSystem {

    private JFrame mainFrame;
    private JTabbedPane tabbedPane;
    private Color primaryColor = new Color(0, 102, 204); // Blue
    private Color successColor = new Color(40, 167, 69); // Green
    private Color dangerColor = new Color(220, 53, 69); // Red
    private Color warningColor = new Color(255, 193, 7); // Yellow
    private Color secondaryColor = new Color(108, 117, 125); // Gray

    private List<Student> students = new ArrayList<>();
    private DefaultTableModel studentTableModel;
    private DefaultTableModel paymentTableModel;

    public FeesManagementSystem() {
        // Add some sample students
        initializeTableModels();

        populateStudentTable();

        
        initializeUI();
    }

    private void populateStudentTable() {
        for (Student student : students) {
            studentTableModel.addRow(new Object[]{
                    student.id,
                    student.name,
                    student.course,
                    student.totalFees,
                    0.0,  // Initially paid amount is 0
                    student.totalFees,  // Full amount due initially
                    "Pending"  // Initial status
            });
        }
    }

    private void initializeTableModels() {
        // Initialize student table model
        String[] studentColumns = {"ID", "Name", "Course", "Total Fees", "Paid", "Due", "Status"};
        studentTableModel = new DefaultTableModel(studentColumns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }

            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 3 || columnIndex == 4 || columnIndex == 5) {
                    return Double.class;
                }
                return Object.class;
            }
        };

        // Initialize payment table model
        String[] paymentColumns = {"ID", "Student ID", "Student Name", "Amount", "Date", "Method"};
        paymentTableModel = new DefaultTableModel(paymentColumns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
    }


    private void initializeUI() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        mainFrame = new JFrame("Fee Management System");
        mainFrame.setSize(1000, 700);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setLocationRelativeTo(null);

        // Main panel with border layout
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Header panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        JLabel titleLabel = new JLabel("Fee Management System", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(primaryColor);

        // Date label
        JLabel dateLabel = new JLabel("Today: " + LocalDate.now().format(DateTimeFormatter.ofPattern("EEEE, MMMM d, yyyy")));
        dateLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        headerPanel.add(titleLabel, BorderLayout.CENTER);
        headerPanel.add(dateLabel, BorderLayout.SOUTH);

        // Tabbed pane
        tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Dashboard", createDashboardPanel());
        tabbedPane.addTab("Students", createStudentPanel());
        tabbedPane.addTab("Payments", createPaymentPanel());
        tabbedPane.addTab("Reports", createReportsPanel());

        // Add components to main panel
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(tabbedPane, BorderLayout.CENTER);

        mainFrame.add(mainPanel);
        mainFrame.setVisible(true);
    }


    private JPanel createReportsPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(new EmptyBorder(15, 15, 15, 15));

        // Report options panel
        JPanel optionsPanel = new JPanel(new GridLayout(1, 3, 15, 15));

        JPanel studentReportPanel = new JPanel(new BorderLayout());
        studentReportPanel.setBorder(new TitledBorder("Student Reports"));
        JButton studentListBtn = createStyledButton("Generate Student List", primaryColor);
        studentReportPanel.add(studentListBtn, BorderLayout.CENTER);

        JPanel paymentReportPanel = new JPanel(new BorderLayout());
        paymentReportPanel.setBorder(new TitledBorder("Payment Reports"));
        JButton paymentHistoryBtn = createStyledButton("Payment History", successColor);
        paymentReportPanel.add(paymentHistoryBtn, BorderLayout.CENTER);

        JPanel financialReportPanel = new JPanel(new BorderLayout());
        financialReportPanel.setBorder(new TitledBorder("Financial Reports"));
        JButton financialSummaryBtn = createStyledButton("Financial Summary", warningColor);
        financialReportPanel.add(financialSummaryBtn, BorderLayout.CENTER);

        optionsPanel.add(studentReportPanel);
        optionsPanel.add(paymentReportPanel);
        optionsPanel.add(financialReportPanel);

        // Report display area
        JTextArea reportArea = new JTextArea();
        reportArea.setEditable(false);
        reportArea.setText("Select a report to generate...");
        JScrollPane reportScroll = new JScrollPane(reportArea);

        // Add action listeners
        studentListBtn.addActionListener(e -> {
            reportArea.setText("Student List Report:\n\n" + generateStudentListReport());
        });

        paymentHistoryBtn.addActionListener(e -> {
            reportArea.setText("Payment History Report:\n\n" + generatePaymentHistoryReport());
        });

        financialSummaryBtn.addActionListener(e -> {
            reportArea.setText("Financial Summary Report:\n\n" + generateFinancialSummaryReport());
        });

        panel.add(optionsPanel, BorderLayout.NORTH);
        panel.add(reportScroll, BorderLayout.CENTER);

        return panel;
    }

    private String generateStudentListReport() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%-10s %-20s %-20s %-10s %-10s %-10s\n",
                "ID", "Name", "Course", "Fees", "Paid", "Due"));
        sb.append("------------------------------------------------------------\n");

        for (Student student : students) {
            double paid = student.totalFees - getDueAmount(student.id);
            double due = getDueAmount(student.id);
            sb.append(String.format("%-10d %-20s %-20s Rs%-9.2f Rs%-9.2f Rs%-9.2f\n",
                    student.id, student.name, student.course, student.totalFees, paid, due));
        }

        sb.append("\nTotal Students: ").append(students.size());
        return sb.toString();
    }

    private String generatePaymentHistoryReport() {
        StringBuilder sb = new StringBuilder();
        sb.append("Payment History Report\n");
        sb.append("Generated on: ").append(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))).append("\n\n");

        if (paymentTableModel == null || paymentTableModel.getRowCount() == 0) {
            sb.append("No payment records found.\n");
            return sb.toString();
        }

        // Header
        sb.append(String.format("%-8s %-20s %-12s %-15s %-10s\n",
                "ID", "Student", "Date", "Amount", "Method"));
        sb.append("------------------------------------------------------------\n");

        // Payment records
        double totalAmount = 0;
        for (int i = 0; i < paymentTableModel.getRowCount(); i++) {
            int id = (int) paymentTableModel.getValueAt(i, 0);
            String student = (String) paymentTableModel.getValueAt(i, 1);
            String amountStr = (String) paymentTableModel.getValueAt(i, 2);
            String date = (String) paymentTableModel.getValueAt(i, 3);
            String method = (String) paymentTableModel.getValueAt(i, 4);

            // Remove currency symbol if present and parse amount
            double amount = Double.parseDouble(amountStr.replaceAll("[^\\d.]", ""));
            totalAmount += amount;

            sb.append(String.format("%-8d %-20s %-12s %-15s %-10s\n",
                    id, student, date, "Rs" + amount, method));
        }

        sb.append("\nTotal Payments: ").append(paymentTableModel.getRowCount()).append("\n");
        sb.append("Total Amount Collected: Rs").append(String.format("%.2f", totalAmount)).append("\n");

        return sb.toString();
    }

    private String generateFinancialSummaryReport() {
        double totalFees = students.stream().mapToDouble(s -> s.totalFees).sum();

        // Calculate total paid from payment records
        double totalPaid = 0;
        if (paymentTableModel != null) {
            for (int i = 0; i < paymentTableModel.getRowCount(); i++) {
                String amountStr = (String) paymentTableModel.getValueAt(i, 2);
                totalPaid += Double.parseDouble(amountStr.replaceAll("[^\\d.]", ""));
            }
        }

        double totalDue = totalFees - totalPaid;
        double completionPercentage = totalFees > 0 ? (totalPaid / totalFees * 100) : 0;

        return String.format(
                "Financial Summary Report\n\n" +
                        "Generated on: %s\n\n" +
                        "Total Students: %d\n" +
                        "Total Fees: Rs%.2f\n" +
                        "Total Paid: Rs%.2f\n" +
                        "Total Due: Rs%.2f\n\n" +
                        "Payment Completion: %.1f%%",
                LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),
                students.size(),
                totalFees,
                totalPaid,
                totalDue,
                completionPercentage
        );
    }

    private JPanel createPaymentPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(new EmptyBorder(15, 15, 15, 15));

        // Payment form
        JPanel formPanel = new JPanel(new GridLayout(6, 2, 10, 10)); // Changed to 6 rows
        formPanel.setBorder(new TitledBorder("Record Payment"));

        JLabel studentLabel = new JLabel("Student:");
        JComboBox<String> studentCombo = new JComboBox<>();
        refreshStudentComboBox(studentCombo); // Initial population

        JLabel amountLabel = new JLabel("Amount:");
        JTextField amountField = new JTextField();

        JLabel dateLabel = new JLabel("Date:");
        JTextField dateField = new JTextField(LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE));

        JLabel methodLabel = new JLabel("Payment Method:");
        JComboBox<String> methodCombo = new JComboBox<>(new String[]{"Cash", "Credit Card", "Bank Transfer", "Check"});

        // Add refresh button
        JButton refreshBtn = createStyledButton("Refresh List", new Color(75, 0, 130)); // Purple color
        refreshBtn.addActionListener(e -> refreshStudentComboBox(studentCombo));

        formPanel.add(studentLabel);
        formPanel.add(studentCombo);
        formPanel.add(amountLabel);
        formPanel.add(amountField);
        formPanel.add(dateLabel);
        formPanel.add(dateField);
        formPanel.add(methodLabel);
        formPanel.add(methodCombo);
        formPanel.add(new JLabel()); // Empty cell
        formPanel.add(refreshBtn); // Add refresh button
        formPanel.add(new JLabel()); // Empty cell
        JButton recordBtn = createStyledButton("Record Payment", successColor);
        formPanel.add(recordBtn);

        // Payment history table
        String[] columns = {"ID", "Student ID", "Student Name", "Amount", "Date", "Method"};
        paymentTableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable paymentTable = new JTable(paymentTableModel);
        paymentTable.setFillsViewportHeight(true);

        // Add action listener for record button
        recordBtn.addActionListener(e -> {
            String selectedStudent = (String) studentCombo.getSelectedItem();
            if (selectedStudent == null || selectedStudent.isEmpty()) {
                JOptionPane.showMessageDialog(panel, "Please select a student", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Extract student ID and name from the combo box selection
            String[] parts = selectedStudent.split(" - ");
            int studentId = Integer.parseInt(parts[0]);
            String studentName = parts[1];

            try {
                double amount = Double.parseDouble(amountField.getText());
                if (amount <= 0) {
                    JOptionPane.showMessageDialog(panel, "Amount must be positive", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Record payment
                int paymentId = paymentTableModel.getRowCount() + 1;
                String date = dateField.getText();
                String method = (String) methodCombo.getSelectedItem();

                // Add to payment table with both ID and Name as separate columns
                paymentTableModel.addRow(new Object[]{
                        paymentId,
                        studentId,
                        studentName,
                        String.format("Rs%.2f", amount),
                        date,
                        method
                });

                // Update student status in student table
                updateStudentPaymentStatus(studentId, amount);

                JOptionPane.showMessageDialog(panel,
                        "Payment recorded for " + studentName + " (ID: " + studentId + ")",
                        "Success", JOptionPane.INFORMATION_MESSAGE);

                // Clear form
                amountField.setText("");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(panel, "Please enter a valid amount", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Add tab change listener to refresh when payment tab is selected
        tabbedPane.addChangeListener(e -> {
            if (tabbedPane.getSelectedIndex() == 2) { // Payment tab index
                refreshStudentComboBox(studentCombo);
            }
        });

        panel.add(formPanel, BorderLayout.NORTH);
        panel.add(new JScrollPane(paymentTable), BorderLayout.CENTER);

        return panel;
    }

    private void refreshStudentComboBox(JComboBox<String> comboBox) {
        comboBox.removeAllItems();
        for (Student student : students) {
            comboBox.addItem(student.id + " - " + student.name);
        }
    }

    private Student findStudentById(int id) {
        for (Student student : students) {
            if (student.id == id) {
                return student;
            }
        }
        return null;
    }

    private void updateStudentPaymentStatus(int studentId, double amount) {
        for (int i = 0; i < studentTableModel.getRowCount(); i++) {
            if ((int) studentTableModel.getValueAt(i, 0) == studentId) {
                double totalFees = (double) studentTableModel.getValueAt(i, 3);
                double paid = (double) studentTableModel.getValueAt(i, 4) + amount;
                double due = totalFees - paid;

                studentTableModel.setValueAt(paid, i, 4);
                studentTableModel.setValueAt(due, i, 5);

                if (due <= 0) {
                    studentTableModel.setValueAt("Paid", i, 6);
                } else {
                    studentTableModel.setValueAt("Partial", i, 6);
                }
                break;
            }
        }
    }

    private double getDueAmount(int studentId) {
        for (int i = 0; i < studentTableModel.getRowCount(); i++) {
            if ((int) studentTableModel.getValueAt(i, 0) == studentId) {
                return (double) studentTableModel.getValueAt(i, 5);
            }
        }
        return 0;
    }

    private JPanel createDashboardPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(new EmptyBorder(15, 15, 15, 15));

        // Stats panel with cards
        JPanel statsPanel = new JPanel(new GridLayout(1, 3, 15, 15));
        statsPanel.add(createStatCard("Total Students", String.valueOf(students.size()), primaryColor));

        // Calculate active students (students with payments)
        long activeStudents = students.stream()
                .filter(s -> getDueAmount(s.id) < s.totalFees)
                .count();
        statsPanel.add(createStatCard("Active Students", String.valueOf(activeStudents), successColor));

        // Calculate pending fees
        double totalDue = students.stream()
                .mapToDouble(s -> getDueAmount(s.id))
                .sum();
        statsPanel.add(createStatCard("Pending Fees", String.format("Rs%.2f", totalDue), dangerColor));

        // Quick actions panel
        JPanel actionsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15));
        JButton addStudentBtn = createStyledButton("Add Student", successColor);
        JButton recordPaymentBtn = createStyledButton("Record Payment", primaryColor);
        JButton generateReportBtn = createStyledButton("Generate Report", warningColor);

        // Add action listeners
        addStudentBtn.addActionListener(e -> showAddStudentDialog());
        recordPaymentBtn.addActionListener(e -> tabbedPane.setSelectedIndex(2));
        generateReportBtn.addActionListener(e -> tabbedPane.setSelectedIndex(3));

        actionsPanel.add(addStudentBtn);
        actionsPanel.add(recordPaymentBtn);
        actionsPanel.add(generateReportBtn);

        // Chart panel with improved layout
        JPanel chartPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(5, 5, 5, 5);

        // First chart - make it smaller
        JPanel barChart = createSimpleBarChart("Monthly Collection");
        barChart.setPreferredSize(new Dimension(300, 250));
        chartPanel.add(barChart, gbc);

        // Second chart
        gbc.gridx = 1;
        JPanel pieChart = createSimplePieChart("Course Distribution");
        pieChart.setPreferredSize(new Dimension(300, 250));
        chartPanel.add(pieChart, gbc);

        // Recent activity
        JPanel activityPanel = new JPanel(new BorderLayout());
        activityPanel.setBorder(new TitledBorder("Recent Activity"));
        JTextArea activityArea = new JTextArea();
        activityArea.setEditable(false);
        activityArea.setText("Recent Activity:\n- Payment received from John Doe (Rs500)\n- New student registered: Jane Smith\n- Fee reminder sent to 5 students");
        JScrollPane activityScroll = new JScrollPane(activityArea);
        activityPanel.add(activityScroll, BorderLayout.CENTER);

        // Main content panel
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.add(chartPanel, BorderLayout.CENTER);
        contentPanel.add(activityPanel, BorderLayout.SOUTH);

        // Add components to main panel
        panel.add(statsPanel, BorderLayout.NORTH);
        panel.add(contentPanel, BorderLayout.CENTER);
        panel.add(actionsPanel, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createStatCard(String title, String value, Color color) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBorder(new CompoundBorder(
                new LineBorder(color.darker(), 1),
                new EmptyBorder(15, 15, 15, 15)
        ));
        card.setBackground(color.brighter().brighter());

        JLabel titleLabel = new JLabel(title, SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        titleLabel.setForeground(Color.DARK_GRAY);

        JLabel valueLabel = new JLabel(value, SwingConstants.CENTER);
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        valueLabel.setForeground(color.darker());

        card.add(titleLabel, BorderLayout.NORTH);
        card.add(valueLabel, BorderLayout.CENTER);

        return card;
    }

    private JPanel createSimplePieChart(String title) {
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;

                // Calculate course distribution from actual data
                Map<String, Integer> courseCounts = new HashMap<>();
                for (Student student : students) {
                    courseCounts.put(student.course, courseCounts.getOrDefault(student.course, 0) + 1);
                }

                // Prepare data for chart
                List<String> labels = new ArrayList<>();
                List<Double> values = new ArrayList<>();
                List<Color> colors = Arrays.asList(
                        new Color(255, 99, 132),  // Red
                        new Color(54, 162, 235),  // Blue
                        new Color(255, 206, 86),  // Yellow
                        new Color(75, 192, 192),  // Teal
                        new Color(153, 102, 255)  // Purple
                );

                for (Map.Entry<String, Integer> entry : courseCounts.entrySet()) {
                    labels.add(entry.getKey());
                    values.add(entry.getValue().doubleValue());
                }

                // Draw pie chart only if we have data
                int width = 0;
                if (!values.isEmpty()) {
                    width = getWidth();
                    int height = getHeight();
                    int diameter = Math.min(width, height) - 40;
                    int x = (width - diameter) / 2;
                    int y = (height - diameter) / 2;

                    double total = values.stream().mapToDouble(Double::doubleValue).sum();
                    double startAngle = 0;

                    for (int i = 0; i < values.size(); i++) {
                        double extent = 360 * (values.get(i) / total);
                        g2d.setColor(colors.get(i % colors.size()));
                        g2d.fill(new Arc2D.Double(x, y, diameter, diameter, startAngle, extent, Arc2D.PIE));
                        startAngle += extent;
                    }

                    // Draw legend
                    int legendX = 20;
                    int legendY = height - 20 - (labels.size() * 20);
                    for (int i = 0; i < labels.size(); i++) {
                        g2d.setColor(colors.get(i % colors.size()));
                        g2d.fillRect(legendX, legendY + i * 20, 15, 15);
                        g2d.setColor(Color.BLACK);
                        g2d.drawString(String.format("%s (%d)", labels.get(i), values.get(i).intValue()),
                                legendX + 20, legendY + i * 20 + 12);
                    }
                }

                // Draw title
                g2d.setFont(new Font("Segoe UI", Font.BOLD, 16));
                FontMetrics fm = g2d.getFontMetrics();
                int titleWidth = fm.stringWidth(title);
                g2d.drawString(title, (width - titleWidth) / 2, 20);
            }
        };
        panel.setBorder(new TitledBorder(title));
        return panel;
    }

    private JPanel createSimpleBarChart(String title) {
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;

                // Calculate monthly payments from actual data
                Map<String, Double> monthlyPayments = new LinkedHashMap<>();
                DateTimeFormatter monthFormatter = DateTimeFormatter.ofPattern("MMM");

                // Initialize all months
                for (int i = 1; i <= 12; i++) {
                    monthlyPayments.put(LocalDate.of(2023, i, 1).format(monthFormatter), 0.0);
                }

                // Sum payments by month
                if (paymentTableModel != null) {
                    for (int i = 0; i < paymentTableModel.getRowCount(); i++) {
                        try {
                            String dateStr = (String) paymentTableModel.getValueAt(i, 4);
                            LocalDate date = LocalDate.parse(dateStr, DateTimeFormatter.ISO_LOCAL_DATE);
                            String month = date.format(monthFormatter);
                            String amountStr = ((String) paymentTableModel.getValueAt(i, 3)).replace("Rs", "");
                            double amount = Double.parseDouble(amountStr);
                            monthlyPayments.put(month, monthlyPayments.get(month) + amount);
                        } catch (Exception e) {
                            // Skip invalid entries
                        }
                    }
                }

                int width = getWidth();
                int height = getHeight();

                if (!monthlyPayments.isEmpty()) {
                    List<String> months = new ArrayList<>(monthlyPayments.keySet());
                    List<Double> values = new ArrayList<>(monthlyPayments.values());

                    double maxValue = values.stream().max(Double::compare).orElse(1.0);
                    if (maxValue == 0) maxValue = 1; // Avoid division by zero

                    int barWidth = width / (months.size() + 2);
                    int maxBarHeight = height - 80;

                    // Draw bars
                    for (int i = 0; i < months.size(); i++) {
                        int barHeight = (int) (values.get(i) / maxValue * maxBarHeight);
                        int x = (i + 1) * barWidth;
                        int y = height - 60 - barHeight;

                        g2d.setColor(new Color(70, 130, 180)); // Steel blue
                        g2d.fillRect(x, y, barWidth - 10, barHeight);

                        // Draw value label
                        g2d.setColor(Color.BLACK);
                        g2d.drawString(String.format("Rs%.0f", values.get(i)), x, y - 5);

                        // Draw month label
                        g2d.drawString(months.get(i), x, height - 40);
                    }

                    // Draw Y-axis labels
                    g2d.setColor(Color.BLACK);
                    for (int i = 0; i <= 5; i++) {
                        int y = height - 60 - (i * (maxBarHeight / 5));
                        g2d.drawString(String.format("Rs%.0f", i * (maxValue / 5)), 5, y + 5);
                    }
                }

                // Draw title and axes
                g2d.setFont(new Font("Segoe UI", Font.BOLD, 16));
                FontMetrics fm = g2d.getFontMetrics();
                int titleWidth = fm.stringWidth(title);
                g2d.drawString(title, (width - titleWidth) / 2, 20);
                g2d.drawLine(40, height - 60, 40, 40);
                g2d.drawLine(40, height - 60, width - 20, height - 60);
            }
        };
        panel.setBorder(new TitledBorder(title));
        return panel;
    }

    private JPanel createStudentPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(new EmptyBorder(15, 15, 15, 15));

        // Search panel
        JPanel searchPanel = new JPanel(new BorderLayout(10, 10));
        JTextField searchField = new JTextField();
        searchField.setBorder(new RoundBorder(15, Color.LIGHT_GRAY));

        JButton searchBtn = createStyledButton("Search", secondaryColor);
        JButton addBtn = createStyledButton("Add Student", successColor);
        addBtn.addActionListener(e -> showAddStudentDialog());

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonPanel.add(searchBtn);
        buttonPanel.add(addBtn);

        searchPanel.add(searchField, BorderLayout.CENTER);
        searchPanel.add(buttonPanel, BorderLayout.EAST);

        // Student table
        String[] columns = {"ID", "Name", "Course", "Total Fees", "Paid", "Due", "Status"};
        studentTableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make table non-editable
            }

            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 3 || columnIndex == 4 || columnIndex == 5) {
                    return Double.class;
                }
                return Object.class;
            }
        };

        // Populate table with initial students
        for (Student student : students) {
            studentTableModel.addRow(new Object[]{
                    student.id,
                    student.name,
                    student.course,
                    student.totalFees,
                    0.0,  // Initially paid amount is 0
                    student.totalFees,  // Full amount due initially
                    "Pending"  // Initial status
            });
        }

        JTable studentTable = new JTable(studentTableModel);
        studentTable.setFillsViewportHeight(true);

        // Custom renderer for status column
        studentTable.getColumnModel().getColumn(6).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                           boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                String status = (String) value;

                if ("Paid".equals(status)) {
                    c.setBackground(new Color(220, 255, 220));
                    c.setForeground(new Color(0, 100, 0));
                } else if ("Partial".equals(status)) {
                    c.setBackground(new Color(255, 255, 200));
                    c.setForeground(new Color(153, 102, 0));
                } else {
                    c.setBackground(new Color(255, 220, 220));
                    c.setForeground(new Color(139, 0, 0));
                }
                setHorizontalAlignment(SwingConstants.CENTER);
                return c;
            }
        });

        // Add action listener for search button
        searchBtn.addActionListener(e -> {
            String searchText = searchField.getText().trim();
            if (searchText.isEmpty()) {
                // Reset table to show all students
                TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(studentTableModel);
                studentTable.setRowSorter(sorter);
                sorter.setRowFilter(null);
                return;
            }

            // Filter rows based on search text (ID or name)
            TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(studentTableModel);
            studentTable.setRowSorter(sorter);

            RowFilter<DefaultTableModel, Object> filter = new RowFilter<DefaultTableModel, Object>() {
                public boolean include(Entry<? extends DefaultTableModel, ? extends Object> entry) {
                    // Search in ID (column 0) and Name (column 1)
                    String id = entry.getStringValue(0);
                    String name = entry.getStringValue(1).toString().toLowerCase();
                    return id.contains(searchText) || name.contains(searchText.toLowerCase());
                }
            };
            sorter.setRowFilter(filter);
        });

        // Add components
        panel.add(searchPanel, BorderLayout.NORTH);
        panel.add(new JScrollPane(studentTable), BorderLayout.CENTER);

        return panel;
    }

    private void showAddStudentDialog() {
        JDialog dialog = new JDialog(mainFrame, "Add Student", true);
        dialog.setSize(400, 450);
        dialog.setLocationRelativeTo(mainFrame);

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(new EmptyBorder(15, 15, 15, 15));

        JLabel titleLabel = new JLabel("Add New Student", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(primaryColor);

        // Form fields
        JPanel formPanel = new JPanel(new GridLayout(7, 2, 10, 10));
        String[] labels = {"Full Name:", "Course:", "Total Fees:", "Email:", "Phone:", "Gender:", "Address:"};

        JTextField nameField = new JTextField();
        JTextField courseField = new JTextField();
        JTextField feesField = new JTextField();
        JTextField emailField = new JTextField();
        JTextField phoneField = new JTextField();
        JComboBox<String> genderCombo = new JComboBox<>(new String[]{"Male", "Female", "Other"});
        JTextArea addressArea = new JTextArea(2, 20);

        formPanel.add(new JLabel(labels[0]));
        formPanel.add(nameField);
        formPanel.add(new JLabel(labels[1]));
        formPanel.add(courseField);
        formPanel.add(new JLabel(labels[2]));
        formPanel.add(feesField);
        formPanel.add(new JLabel(labels[3]));
        formPanel.add(emailField);
        formPanel.add(new JLabel(labels[4]));
        formPanel.add(phoneField);
        formPanel.add(new JLabel(labels[5]));
        formPanel.add(genderCombo);
        formPanel.add(new JLabel(labels[6]));
        formPanel.add(new JScrollPane(addressArea));

        // Buttons
        JButton saveBtn = createStyledButton("Save", successColor);
        JButton cancelBtn = createStyledButton("Cancel", dangerColor);

        // Add action listener for save button
        saveBtn.addActionListener(e -> {
            // Validate and save student
            if (nameField.getText().isEmpty() || courseField.getText().isEmpty() || feesField.getText().isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Please fill in all required fields", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                // Create new student
                int id = 1000 + students.size() + 1;
                String name = nameField.getText();
                String course = courseField.getText();
                double totalFees = Double.parseDouble(feesField.getText());
                String email = emailField.getText();
                String phone = phoneField.getText();
                String gender = (String) genderCombo.getSelectedItem();
                String address = addressArea.getText();

                // Add to students list
                Student student = new Student(id, name, course, totalFees, email, phone, gender, address);
                students.add(student);

                // Add to table
                studentTableModel.addRow(new Object[]{
                        id,
                        name,
                        course,
                        totalFees,
                        0.0,  // Initially paid amount is 0
                        totalFees,  // Full amount due initially
                        "Pending"  // Initial status
                });

                JOptionPane.showMessageDialog(dialog, "Student added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                dialog.dispose();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "Please enter a valid number for fees", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        cancelBtn.addActionListener(e -> dialog.dispose());

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonPanel.add(cancelBtn);
        buttonPanel.add(saveBtn);

        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(formPanel, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        dialog.add(panel);
        dialog.setVisible(true);
    }

    // Student class (inner class for simplicity)
    class Student {
        int id;
        String name;
        String course;
        double totalFees;
        String email;
        String phone;
        String gender;
        String address;

        public Student(int id, String name, String course, double totalFees, String email, String phone, String gender, String address) {
            this.id = id;
            this.name = name;
            this.course = course;
            this.totalFees = totalFees;
            this.email = email;
            this.phone = phone;
            this.gender = gender;
            this.address = address;
        }

        @Override
        public String toString() {
            return name + " (" + id + ")";
        }
    }

    private JButton createStyledButton(String text, Color bgColor) {
        JButton btn = new JButton(text);

        // Different colors for different button types
        if (text.contains("Add")) {
            bgColor = new Color(46, 125, 50); // Dark green
        } else if (text.contains("Record")) {
            bgColor = new Color(93, 64, 155); // Purple
        } else if (text.contains("Generate") || text.contains("Search")) {
            bgColor = new Color(239, 108, 0); // Orange
        } else if (text.contains("Refresh")) {
            bgColor = new Color(74, 20, 140); // Dark purple
        } else if (text.contains("Cancel")) {
            bgColor = new Color(198, 40, 40); // Red
        }

        btn.setBackground(bgColor);
        btn.setForeground(Color.BLACK);
        btn.setFocusPainted(false);
        btn.setBorder(new EmptyBorder(10, 20, 10, 20));
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));

        // Hover effect
        Color finalBgColor = bgColor;
        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                btn.setBackground(finalBgColor.brighter());
            }
            public void mouseExited(MouseEvent e) {
                btn.setBackground(finalBgColor);
            }
        });
        return btn;
    }

    // Custom border class for rounded text fields
    class RoundBorder extends AbstractBorder {
        private int radius;
        private Color color;

        public RoundBorder(int radius, Color color) {
            this.radius = radius;
            this.color = color;
        }

        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(color);
            g2.draw(new RoundRectangle2D.Double(x, y, width - 1, height - 1, radius, radius));
        }

        @Override
        public Insets getBorderInsets(Component c) {
            return new Insets(this.radius + 1, this.radius + 1, this.radius + 1, this.radius + 1);
        }

        @Override
        public Insets getBorderInsets(Component c, Insets insets) {
            insets.left = insets.right = insets.top = insets.bottom = this.radius + 1;
            return insets;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new FeesManagementSystem());
    }
}