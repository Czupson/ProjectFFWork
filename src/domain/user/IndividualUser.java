package domain.user;

public class IndividualUser extends User {

    private final String studentId;

    public IndividualUser(String email, String displayName) {
        this(email, displayName, null);
    }

    public IndividualUser(String email, String displayName, String studentId) {
        super(email, displayName);

        if (studentId != null && studentId.isBlank()) {
            throw new IllegalArgumentException("studentId cannot be blank");
        }

        this.studentId = studentId;
    }

    public String getStudentId() {
        return studentId;
    }

    @Override
    public String toString() {
        if (studentId != null && !studentId.isBlank()) {
            return super.toString() + " (studentId: " + studentId + ")";
        }
        return super.toString();
    }
}