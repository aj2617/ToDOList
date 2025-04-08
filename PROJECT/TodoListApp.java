import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import javax.swing.Timer;

public class TodoListApp extends JFrame {

    // Inner class to represent a Task
    class Task {
        String description;
        int priority; // 1 = High, 2 = Medium, 3 = Low
        boolean completed;
        JCheckBox checkBox;
        JLabel label;
        JPanel panel;

        public Task(String description, int priority) {
            this.description = description;
            this.priority = priority;
            this.completed = false;
            initTaskPanel();
        }

        private void initTaskPanel() {
            // Set background color based on priority
            Color backgroundColor;
            switch (priority) {
                case 1: 
                    backgroundColor = new Color(255, 204, 204); // Light Red for High
                    break;
                case 2: 
                    backgroundColor = new Color(255, 255, 204); // Light Yellow for Medium
                    break;
                case 3: 
                    backgroundColor = new Color(204, 255, 204); // Light Green for Low
                    break;
                default: 
                    backgroundColor = new Color(240, 248, 255);
            }
            
            panel = new JPanel(new BorderLayout());
            panel.setBackground(backgroundColor);
            panel.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(Color.LIGHT_GRAY),
                    new EmptyBorder(5, 5, 5, 5)
            ));

            checkBox = new JCheckBox();
            checkBox.setBackground(backgroundColor);
            checkBox.addActionListener(e -> toggleCompleted());

            label = new JLabel(description + " (Priority: " + getPriorityText() + ")");
            label.setFont(new Font("SansSerif", Font.PLAIN, 14));

            panel.add(checkBox, BorderLayout.WEST);
            panel.add(label, BorderLayout.CENTER);
        }

        private void toggleCompleted() {
            completed = checkBox.isSelected();
            if (completed) {
                label.setFont(new Font("SansSerif", Font.ITALIC, 14));
                label.setForeground(Color.GRAY);
            } else {
                label.setFont(new Font("SansSerif", Font.PLAIN, 14));
                label.setForeground(Color.BLACK);
            }
        }

        private String getPriorityText() {
            switch (priority) {
                case 1: return "High";
                case 2: return "Medium";
                case 3: return "Low";
                default: return "";
            }
        }
    }

    private ArrayList<Task> tasks;
    private JPanel tasksPanel;
    private JTextField taskField;
    private JComboBox<String> priorityCombo;
    private JButton addButton;
    private JButton removeButton;
    private JLabel timerLabel;
    private JButton startTimerButton;
    private JButton pauseTimerButton;
    private JButton resetTimerButton;
    private JComboBox<String> timerOptions;
    private Timer timer;
    private int countdownSeconds = 1500; // default: 25 minutes

    public TodoListApp() {
        tasks = new ArrayList<>();
        setTitle("ToDoList created by Shihab, Muhit, Rakib");
        setSize(600, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        initUI();
    }

    private void initUI() {
        // Top panel for adding tasks
        JPanel inputPanel = new JPanel();
        inputPanel.setBackground(new Color(173, 216, 230));
        inputPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        inputPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));

        taskField = new JTextField(20);
        String[] priorities = {"High", "Medium", "Low"};
        priorityCombo = new JComboBox<>(priorities);
        addButton = new JButton("Add Task");
        removeButton = new JButton("Remove Selected Task");

        inputPanel.add(new JLabel("Task:"));
        inputPanel.add(taskField);
        inputPanel.add(new JLabel("Priority:"));
        inputPanel.add(priorityCombo);
        inputPanel.add(addButton);
        inputPanel.add(removeButton);

        add(inputPanel, BorderLayout.NORTH);

        // Center panel for tasks list
        tasksPanel = new JPanel();
        tasksPanel.setLayout(new BoxLayout(tasksPanel, BoxLayout.Y_AXIS));
        JScrollPane scrollPane = new JScrollPane(tasksPanel);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Your Tasks: "));
        add(scrollPane, BorderLayout.CENTER);

        // Bottom panel for countdown timer and timer options
        JPanel timerPanel = new JPanel();
        timerPanel.setLayout(new BoxLayout(timerPanel, BoxLayout.Y_AXIS));
        timerPanel.setBackground(new Color(255, 228, 225));
        timerPanel.setBorder(BorderFactory.createTitledBorder("Productivity Timer :       Don’t prioritize what's urgent—prioritize what's important. "));

        // Timer label with Arial Black Bold font size 17
        timerLabel = new JLabel(formatTime(countdownSeconds), SwingConstants.CENTER);
        timerLabel.setFont(new Font("Arial Black", Font.BOLD, 17));
        timerLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        timerLabel.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Timer options combo box with an additional "30 minutes" option
        String[] timerDurations = {"5 minutes", "10 minutes", "15 minutes", "25 minutes", "30 minutes", "1 hour"};
        timerOptions = new JComboBox<>(timerDurations);
        timerOptions.setSelectedItem("25 minutes");
        timerOptions.setAlignmentX(Component.CENTER_ALIGNMENT);
        timerOptions.addActionListener(e -> updateCountdownSeconds());

        startTimerButton = new JButton("Start");
        pauseTimerButton = new JButton("Pause");
        resetTimerButton = new JButton("Reset");

        // Button panel for timer controls
        JPanel timerButtonsPanel = new JPanel(new FlowLayout());
        timerButtonsPanel.add(startTimerButton);
        timerButtonsPanel.add(pauseTimerButton);
        timerButtonsPanel.add(resetTimerButton);

        timerPanel.add(timerLabel);
        timerPanel.add(timerOptions);
        timerPanel.add(timerButtonsPanel);

        add(timerPanel, BorderLayout.SOUTH);

        // Add action listeners
        addButton.addActionListener(e -> addTask());
        removeButton.addActionListener(e -> removeSelectedTask());
        startTimerButton.addActionListener(e -> startTimer());
        pauseTimerButton.addActionListener(e -> pauseTimer());
        resetTimerButton.addActionListener(e -> resetTimer());

        // Setup the timer
        timer = new Timer(1000, e -> updateCountdown());
    }

    // Update countdownSeconds based on selected timer duration
    private void updateCountdownSeconds() {
        String selected = (String) timerOptions.getSelectedItem();
        if (selected != null) {
            switch (selected) {
                case "5 minutes":
                    countdownSeconds = 5 * 60;
                    break;
                case "10 minutes":
                    countdownSeconds = 10 * 60;
                    break;
                case "15 minutes":
                    countdownSeconds = 15 * 60;
                    break;
                case "25 minutes":
                    countdownSeconds = 25 * 60;
                    break;
                case "30 minutes":
                    countdownSeconds = 30 * 60;
                    break;
                case "1 hour":
                    countdownSeconds = 60 * 60;
                    break;
                default:
                    countdownSeconds = 25 * 60;
            }
            timerLabel.setText(formatTime(countdownSeconds));
        }
    }

    private void addTask() {
        String description = taskField.getText().trim();
        if (description.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a task description.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        String priorityText = (String) priorityCombo.getSelectedItem();
        int priorityValue = 2; // default Medium
        if (priorityText != null) {
            switch (priorityText) {
                case "High": priorityValue = 1; break;
                case "Medium": priorityValue = 2; break;
                case "Low": priorityValue = 3; break;
            }
        }
        Task newTask = new Task(description, priorityValue);
        tasks.add(newTask);
        sortTasks();
        refreshTasksPanel();
        taskField.setText("");
    }

    private void removeSelectedTask() {
        ArrayList<Task> toRemove = new ArrayList<>();
        for (Task task : tasks) {
            if (task.checkBox.isSelected()) {
                toRemove.add(task);
            }
        }
        if (toRemove.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Select task(s) to remove by checking the box.", "No Selection", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        tasks.removeAll(toRemove);
        refreshTasksPanel();
    }

    private void sortTasks() {
        // Lower numeric value means higher priority
        Collections.sort(tasks, Comparator.comparingInt(task -> task.priority));
    }

    private void refreshTasksPanel() {
        tasksPanel.removeAll();
        for (Task task : tasks) {
            tasksPanel.add(task.panel);
            tasksPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        }
        tasksPanel.revalidate();
        tasksPanel.repaint();
    }

    private void startTimer() {
        timer.start();
    }

    private void pauseTimer() {
        timer.stop();
    }

    private void resetTimer() {
        timer.stop();
        updateCountdownSeconds();
    }

    private void updateCountdown() {
        if (countdownSeconds > 0) {
            countdownSeconds--;
            timerLabel.setText(formatTime(countdownSeconds));
        } else {
            timer.stop();
            JOptionPane.showMessageDialog(this, "Time's up! Take a break and Recharge yourself ", "Timer", JOptionPane.INFORMATION_MESSAGE);
            resetTimer();
        }
    }

    private String formatTime(int totalSeconds) {
        int minutes = totalSeconds / 60;
        int seconds = totalSeconds % 60;
        return String.format("%02d:%02d", minutes, seconds);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            TodoListApp app = new TodoListApp();
            app.setVisible(true);
        });
    }
}
