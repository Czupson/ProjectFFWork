package domain.user;

import java.util.Objects;

public abstract class User {
    private final String email;
    private final String displayName;

    protected User(String email, String displayName) {
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Email cannot be null or empty");
        }
        if (displayName == null || displayName.isBlank()) {
            throw new IllegalArgumentException("Display name cannot be null or empty");
        }
        this.email = email;
        this.displayName = displayName;
    }

    public String getEmail() {
        return email;
    }
    public String getDisplayName() {
        return displayName;
    }

    @Override
    public String toString() {
        return displayName + " <" + email + ">";
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (!(object instanceof User)) return false;
        User user = (User) object;
        return email.equals(user.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email);
    }
}
