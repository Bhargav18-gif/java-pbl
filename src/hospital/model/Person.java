package hospital.model;

import java.io.Serializable;
import java.util.Objects;

/**
 * Abstract base class representing a person in the hospital system.
 * Demonstrates OOP Abstraction and Inheritance.
 */
public abstract class Person implements Searchable, Serializable {
    private static final long serialVersionUID = 1L;

    protected String id;
    protected String name;
    protected int age;
    protected String gender;
    protected String phone;
    protected String email;
    protected String address;

    public Person() {
    }

    public Person(String id, String name, int age, String gender, String phone, String email, String address) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.phone = phone;
        this.email = email;
        this.address = address;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * Polymorphic method to get role or display title.
     */
    public abstract String getRoleTitle();

    @Override
    public boolean matches(String query) {
        if (query == null || query.trim().isEmpty()) {
            return true;
        }
        String q = query.toLowerCase().trim();
        return (id != null && id.toLowerCase().contains(q)) ||
               (name != null && name.toLowerCase().contains(q)) ||
               (phone != null && phone.toLowerCase().contains(q));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Person person = (Person) o;
        return Objects.equals(id, person.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return name + " (" + id + ")";
    }
}
