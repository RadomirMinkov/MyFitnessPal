package myfitnesspal.users;

public record User(String name, String salt, String hash) {
}
