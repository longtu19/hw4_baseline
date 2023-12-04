package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * ExpenseTrackerModel class acts as the M (model) in the MVC design pattern.
 * It observes the changes from the state and apply it to the encapusulated data
 */
public class ExpenseTrackerModel {

  // encapsulation - data integrity
  private List<Transaction> transactions;
  private List<Integer> matchedFilterIndices;
  private List<ExpenseTrackerModelListener> listeners;

  // This is applying the Observer design pattern.
  // Specifically, this is the Observable class.

  public ExpenseTrackerModel() {
    transactions = new ArrayList<Transaction>();
    matchedFilterIndices = new ArrayList<Integer>();
    listeners = new ArrayList<ExpenseTrackerModelListener>();
  }

  public void addTransaction(Transaction t) {
    // Perform input validation to guarantee that all transactions added are
    // non-null.
    if (t == null) {
      throw new IllegalArgumentException("The new transaction must be non-null.");
    }
    transactions.add(t);
    // The previous filter is no longer valid.
    matchedFilterIndices.clear();
  }

  public void removeTransaction(Transaction t) {
    transactions.remove(t);
    // The previous filter is no longer valid.
    matchedFilterIndices.clear();
  }

  public List<Transaction> getTransactions() {
    // encapsulation - data integrity
    return Collections.unmodifiableList(new ArrayList<>(transactions));
  }

  public void setMatchedFilterIndices(List<Integer> newMatchedFilterIndices) {
    // Perform input validation
    if (newMatchedFilterIndices == null) {
      throw new IllegalArgumentException("The matched filter indices list must be non-null.");
    }
    for (Integer matchedFilterIndex : newMatchedFilterIndices) {
      if ((matchedFilterIndex < 0) || (matchedFilterIndex > this.transactions.size() - 1)) {
        throw new IllegalArgumentException(
            "Each matched filter index must be between 0 (inclusive) and the number of transactions (exclusive).");
      }
    }
    // For encapsulation, copy in the input list
    this.matchedFilterIndices.clear();
    this.matchedFilterIndices.addAll(newMatchedFilterIndices);
  }

  public List<Integer> getMatchedFilterIndices() {
    // For encapsulation, copy out the output list
    List<Integer> copyOfMatchedFilterIndices = new ArrayList<Integer>();
    copyOfMatchedFilterIndices.addAll(this.matchedFilterIndices);
    return copyOfMatchedFilterIndices;
  }

  /**
   * Registers the given ExpenseTrackerModelListener for
   * state change events.
   *
   * @param listener The ExpenseTrackerModelListener to be registered
   * @return If the listener is non-null and not already registered,
   *         returns true. If not, returns false.
   */
  public boolean register(ExpenseTrackerModelListener listener) {
    // Checks if the listener is non-null and not registered
    if (listener != null && !containsListener(listener)) {
      listeners.add(listener);
      return true;
    }
    return false;
  }

  /**
   * @return the number of registered listeners
   */
  public int numberOfListeners() {
    return listeners.size();
  }

  /**
   * @return If the listener is registered, returns true.
   *         If not returns false
   */
  public boolean containsListener(ExpenseTrackerModelListener listener) {
    // returns true if the listener is registered
    return listeners.contains(listener);
  }

  /**
   * Update each listener in the registered listeners list when
   * the state changed
   */
  protected void stateChanged() {
    // loop through each registered listener and update
    for (ExpenseTrackerModelListener listener : listeners) {
      listener.update(this);
    }
  }

  public void triggerStateChanged() {
    stateChanged();
  }
}
