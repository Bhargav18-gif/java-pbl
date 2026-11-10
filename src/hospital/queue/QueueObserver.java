package hospital.queue;

import hospital.model.QueueToken;

/**
 * Observer interface for queue state changes, ensuring dynamic UI updates.
 */
public interface QueueObserver {
    void onQueueUpdated();
    void onPatientCalled(QueueToken token);
    void onConsultationCompleted(QueueToken token);
}
