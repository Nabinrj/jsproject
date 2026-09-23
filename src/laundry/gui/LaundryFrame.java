package laundry.gui;

import laundry.model.CustomerTask;
import laundry.model.LaundryFacility;
import laundry.model.Statistics;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/** Swing dashboard that renders laundry activity while all simulation work stays off the EDT. */
public final class LaundryFrame extends JFrame {
    private static final long serialVersionUID = 1L;
    private final transient LaundryFacility facility = new LaundryFacility();
    private final transient Statistics statistics = new Statistics();
    private transient ExecutorService customers;
    private transient Thread arrivalThread;
    private final JButton normalButton = new JButton("Start Normal Simulation");
    private final JButton congestedButton = new JButton("Start Congested Scenario");
    private final JButton stopButton = new JButton("Stop");
    private final JLabel statisticsLabel = new JLabel("Served: 0 | Average: 0.0 s | Max washers: 0 | Max dryers: 0 | Payment queue: 0");
    private final JTextArea log = new JTextArea();
    private final MachinePanel washers = new MachinePanel("Washers", LaundryFacility.WASHERS);
    private final MachinePanel dryers = new MachinePanel("Dryers", LaundryFacility.DRYERS);
    private final MachinePanel kiosks = new MachinePanel("Payment kiosks", LaundryFacility.KIOSKS);

    public LaundryFrame() {
        super("Smart Laundry Facility Simulation");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE); setSize(900, 680); setLocationByPlatform(true);
        facility.setListener(new LaundryFacility.Listener() {
            public void machinesChanged() { SwingUtilities.invokeLater(LaundryFrame.this::refreshMachines); }
            public void ownerCalled() { SwingUtilities.invokeLater(() -> { append("Owner called: 30 customers are waiting to pay."); JOptionPane.showMessageDialog(LaundryFrame.this, "Payment queue reached 30 customers. Owner has been called.", "Congestion alert", JOptionPane.WARNING_MESSAGE); }); }
        });
        JPanel controls = new JPanel(new FlowLayout(FlowLayout.LEFT)); controls.add(normalButton); controls.add(congestedButton); controls.add(stopButton); stopButton.setEnabled(false);
        normalButton.addActionListener(e -> start(false)); congestedButton.addActionListener(e -> start(true)); stopButton.addActionListener(e -> stop());
        JPanel machines = new JPanel(new GridLayout(1, 3, 12, 0)); machines.setBorder(new EmptyBorder(10, 12, 10, 12)); machines.add(washers); machines.add(dryers); machines.add(kiosks);
        log.setEditable(false); log.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        statisticsLabel.setBorder(new EmptyBorder(8, 12, 8, 12));
        JPanel footer = new JPanel(new BorderLayout()); footer.add(statisticsLabel, BorderLayout.NORTH); footer.add(new JScrollPane(log), BorderLayout.CENTER); footer.setPreferredSize(new Dimension(900, 230));
        add(controls, BorderLayout.NORTH); add(machines, BorderLayout.CENTER); add(footer, BorderLayout.SOUTH);
        // The timer reads atomics and posts display changes only on the Swing event dispatch thread.
        new Timer(250, e -> refreshStatistics()).start();
    }
    private void start(boolean congested) {
        facility.setKiosksBrokenForDay(congested); customers = Executors.newCachedThreadPool();
        normalButton.setEnabled(false); congestedButton.setEnabled(false); stopButton.setEnabled(true);
        append(congested ? "Congested scenario started: both kiosks remain broken." : "Normal simulation started.");
        arrivalThread = new Thread(() -> {
            for (int id = 1; id <= 50 && !Thread.currentThread().isInterrupted(); id++) {
                customers.submit(new CustomerTask(id, facility, statistics, (customer, message) -> SwingUtilities.invokeLater(() -> append(message))));
                try { Thread.sleep((long) (Math.random() * 3_001)); } catch (InterruptedException interrupted) { Thread.currentThread().interrupt(); }
            }
        }, "Arrival-generator");
        arrivalThread.setDaemon(true); arrivalThread.start();
    }
    private void stop() {
        if (arrivalThread != null) arrivalThread.interrupt();
        if (customers != null) customers.shutdownNow(); facility.setKiosksBrokenForDay(false);
        normalButton.setEnabled(true); congestedButton.setEnabled(true); stopButton.setEnabled(false); append("Simulation stopped.");
    }
    private void refreshMachines() { washers.update(facility.washerState()); dryers.update(facility.dryerState()); kiosks.update(facility.kioskState()); }
    private void refreshStatistics() {
        Statistics.Snapshot s = statistics.snapshot();
        statisticsLabel.setText("Served: " + s.served() + " | Average: " + String.format("%.1f", s.averageMillis() / 1000.0) + " s | Max washers: " + s.maxWashers() + " | Max dryers: " + s.maxDryers() + " | Payment queue: " + facility.paymentQueueSize());
    }
    private void append(String line) { log.append(line + "\n"); log.setCaretPosition(log.getDocument().getLength()); }

    /** Small reusable view that makes individual machine allocations visible. */
    private static final class MachinePanel extends JPanel {
        private static final long serialVersionUID = 1L;
        private final JLabel[] labels;
        MachinePanel(String title, int count) { setLayout(new GridLayout(count + 1, 1, 4, 4)); setBorder(BorderFactory.createTitledBorder(title)); labels = new JLabel[count]; for (int i = 0; i < count; i++) { labels[i] = new JLabel(title.substring(0, title.length() - (title.endsWith("s") ? 1 : 0)) + " " + (i + 1), SwingConstants.CENTER); labels[i].setOpaque(true); labels[i].setBackground(new Color(215, 245, 215)); add(labels[i]); } }
        void update(boolean[] busy) { for (int i = 0; i < labels.length; i++) labels[i].setBackground(busy[i] ? new Color(255, 224, 120) : new Color(215, 245, 215)); }
    }
}
