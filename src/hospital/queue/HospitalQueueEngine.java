package hospital.queue;

import hospital.exceptions.QueueEmptyException;
import hospital.model.PatientType;
import hospital.model.QueueToken;

import javax.swing.SwingUtilities;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Thread-safe Priority Queue Engine using Producer-Consumer pattern.
 * Uses synchronized methods, wait(), and notifyAll().
 * Enforces strict priority: Emergency patients are consumed before Regular patients.
 */
public class HospitalQueueEngine {
    private final LinkedList<QueueToken> emergencyQueue = new LinkedList<>();
    private final LinkedList<QueueToken> regularQueue = new LinkedList<>();
    private final int capacity;
    private final List<QueueObserver> observers = new ArrayList<>();

    // State tracking
    private QueueToken currentlyServing = null;
    private boolean simulationRunning = false;
    private Thread consumerThread = null;

    public HospitalQueueEngine(int capacity) {
        this.capacity = capacity;
    }

    public HospitalQueueEngine() {
        this(100);
    }

    /**
     * PRODUCER METHOD: Adds a new token to the queue.
     * Uses wait() when queue is full and notifyAll() after adding.
     */
    public synchronized void produceToken(QueueToken token) throws InterruptedException {
        while ((emergencyQueue.size() + regularQueue.size()) >= capacity) {
            System.out.println("[Queue Producer] Buffer full. Waiting for space...");
            wait();
        }

        if (token.isEmergency()) {
            emergencyQueue.addLast(token);
            System.out.println("[Queue Producer] Added EMERGENCY Token: " + token.getTokenNumber() + " - " + token.getPatientName());
        } else {
            regularQueue.addLast(token);
            System.out.println("[Queue Producer] Added REGULAR Token: " + token.getTokenNumber() + " - " + token.getPatientName());
        }

        // Wake up consumers waiting for tokens
        notifyAll();
        notifyObserversQueueUpdated();
    }

    /**
     * CONSUMER METHOD: Retrieves the next priority token.
     * Uses wait() when queues are empty and notifyAll() after removal.
     * Strict priority: Emergency patients are always served first.
     */
    public synchronized QueueToken consumeToken() throws InterruptedException {
        while (emergencyQueue.isEmpty() && regularQueue.isEmpty()) {
            System.out.println("[Queue Consumer] Queue is empty. Waiting for patients...");
            wait();
        }

        QueueToken token;
        if (!emergencyQueue.isEmpty()) {
            token = emergencyQueue.removeFirst();
            System.out.println("[Queue Consumer] Consumed EMERGENCY Token: " + token.getTokenNumber());
        } else {
            token = regularQueue.removeFirst();
            System.out.println("[Queue Consumer] Consumed REGULAR Token: " + token.getTokenNumber());
        }

        this.currentlyServing = token;
        token.setStatus("CALLING");

        // Wake up producers waiting for space
        notifyAll();
        notifyObserversQueueUpdated();
        notifyObserversPatientCalled(token);
        return token;
    }

    /**
     * Non-blocking immediate consumption for manual UI actions.
     */
    public synchronized QueueToken pollNextImmediate() throws QueueEmptyException {
        if (emergencyQueue.isEmpty() && regularQueue.isEmpty()) {
            throw new QueueEmptyException("No patients currently waiting in the queue.");
        }

        QueueToken token;
        if (!emergencyQueue.isEmpty()) {
            token = emergencyQueue.removeFirst();
        } else {
            token = regularQueue.removeFirst();
        }

        this.currentlyServing = token;
        token.setStatus("IN_CONSULTATION");

        notifyAll();
        notifyObserversQueueUpdated();
        notifyObserversPatientCalled(token);
        return token;
    }

    public synchronized QueueToken peekNext() {
        if (!emergencyQueue.isEmpty()) {
            return emergencyQueue.peekFirst();
        }
        return regularQueue.peekFirst();
    }

    public synchronized boolean removeToken(String tokenNumber) {
        boolean removed = false;
        removed = emergencyQueue.removeIf(t -> t.getTokenNumber().equalsIgnoreCase(tokenNumber));
        if (!removed) {
            removed = regularQueue.removeIf(t -> t.getTokenNumber().equalsIgnoreCase(tokenNumber));
        }
        if (removed) {
            notifyAll();
            notifyObserversQueueUpdated();
        }
        return removed;
    }

    public synchronized void clearAll() {
        emergencyQueue.clear();
        regularQueue.clear();
        currentlyServing = null;
        notifyAll();
        notifyObserversQueueUpdated();
    }

    public synchronized List<QueueToken> getEmergencyQueueSnapshot() {
        return new ArrayList<>(emergencyQueue);
    }

    public synchronized List<QueueToken> getRegularQueueSnapshot() {
        return new ArrayList<>(regularQueue);
    }

    public synchronized List<QueueToken> getAllWaitingSnapshot() {
        List<QueueToken> combined = new ArrayList<>(emergencyQueue);
        combined.addAll(regularQueue);
        return combined;
    }

    public synchronized int getEmergencyCount() {
        return emergencyQueue.size();
    }

    public synchronized int getRegularCount() {
        return regularQueue.size();
    }

    public synchronized int getTotalWaitingCount() {
        return emergencyQueue.size() + regularQueue.size();
    }

    public synchronized QueueToken getCurrentlyServing() {
        return currentlyServing;
    }

    public synchronized void setCurrentlyServing(QueueToken token) {
        this.currentlyServing = token;
        notifyObserversQueueUpdated();
    }

    public synchronized void markCurrentConsultationComplete() {
        if (this.currentlyServing != null) {
            this.currentlyServing.setStatus("COMPLETED");
            QueueToken completedToken = this.currentlyServing;
            this.currentlyServing = null;
            notifyObserversConsultationCompleted(completedToken);
            notifyObserversQueueUpdated();
        }
    }

    // --- Observer pattern support ---
    public synchronized void addObserver(QueueObserver observer) {
        if (observer != null && !observers.contains(observer)) {
            observers.add(observer);
        }
    }

    public synchronized void removeObserver(QueueObserver observer) {
        observers.remove(observer);
    }

    private void notifyObserversQueueUpdated() {
        List<QueueObserver> copy;
        synchronized (this) {
            copy = new ArrayList<>(observers);
        }
        SwingUtilities.invokeLater(() -> {
            for (QueueObserver obs : copy) {
                try {
                    obs.onQueueUpdated();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
    }

    private void notifyObserversPatientCalled(QueueToken token) {
        List<QueueObserver> copy;
        synchronized (this) {
            copy = new ArrayList<>(observers);
        }
        SwingUtilities.invokeLater(() -> {
            for (QueueObserver obs : copy) {
                try {
                    obs.onPatientCalled(token);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
    }

    private void notifyObserversConsultationCompleted(QueueToken token) {
        List<QueueObserver> copy;
        synchronized (this) {
            copy = new ArrayList<>(observers);
        }
        SwingUtilities.invokeLater(() -> {
            for (QueueObserver obs : copy) {
                try {
                    obs.onConsultationCompleted(token);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
    }

    // --- Background Consumer Thread (Simulation Mode) ---
    public synchronized boolean isSimulationRunning() {
        return simulationRunning;
    }

    public synchronized void startSimulation(ConsultationProcessorCallback callback) {
        if (simulationRunning) {
            return;
        }
        simulationRunning = true;
        consumerThread = new Thread(() -> {
            System.out.println("[Simulation Thread] Auto-consultation engine started.");
            while (simulationRunning) {
                try {
                    QueueToken token = consumeToken();
                    if (token != null) {
                        if (callback != null) {
                            callback.onConsultationStarted(token);
                        }
                        // Simulate consultation processing duration (e.g. 4 seconds)
                        for (int i = 0; i < 4; i++) {
                            if (!simulationRunning) break;
                            Thread.sleep(1000);
                        }
                        if (simulationRunning) {
                            markCurrentConsultationComplete();
                            if (callback != null) {
                                callback.onConsultationFinished(token);
                            }
                        }
                    }
                    Thread.sleep(1500); // Small interval between patients
                } catch (InterruptedException e) {
                    System.out.println("[Simulation Thread] Interrupted.");
                    Thread.currentThread().interrupt();
                    break;
                }
            }
            simulationRunning = false;
            System.out.println("[Simulation Thread] Auto-consultation engine stopped.");
        }, "DoctorConsultationConsumerThread");

        consumerThread.setDaemon(true);
        consumerThread.start();
    }

    public synchronized void stopSimulation() {
        simulationRunning = false;
        if (consumerThread != null) {
            consumerThread.interrupt();
            consumerThread = null;
        }
    }

    public interface ConsultationProcessorCallback {
        void onConsultationStarted(QueueToken token);
        void onConsultationFinished(QueueToken token);
    }
}
