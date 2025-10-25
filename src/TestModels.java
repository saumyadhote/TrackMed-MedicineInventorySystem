import model.*;

public class TestModels {
    public static void main(String[] args) {
        User user = new User(1, "supplier1", "pass", "supplier", "s1@mail.com");
        System.out.println(user);

        Medicine med = new Medicine(1, "Paracetamol", "Painkiller", "2025-12-12", 100, 25.0);
        System.out.println(med);
    }
}
