package hms;

public class Staff {
    private String id;
    private String name;
    private String role;
    private String gender;

    public Staff(String id, String name, String role, String gender) {
        this.id = id;
        this.name = name;
        this.role = role;
        this.gender = gender;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    @Override
    public String toString() {
        return "Staff{" +
                "ID='" + id + '\'' +
                ", Name='" + name + '\'' +
                ", Role='" + role + '\'' +
                ", Gender='" + gender + '\'' +
                '}';
    }
}

