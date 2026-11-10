package hospital.model;

/**
 * Interface defining search capability for hospital entities.
 */
public interface Searchable {
    boolean matches(String query);
}
