package goit.homeworks;

import java.util.List;

public class Main {

    public static void main(String[] args) {
        JsonPlaceholderHttpService jsonPlaceholderHttpService = new JsonPlaceholderHttpService();

        NewUser newUer = new NewUser();

        createMethodAndTest(jsonPlaceholderHttpService, newUer);
        updateMethodAndTest(jsonPlaceholderHttpService);
        getAllUsersMethodAndTest(jsonPlaceholderHttpService);
        getUserMethodsAndTests(jsonPlaceholderHttpService, 2);
        createJsonWithCommentsWithCommentsOfPost(jsonPlaceholderHttpService, 1);
        getUncompletedUserToDoTasksById(jsonPlaceholderHttpService, 1);
    }

    private static void getUncompletedUserToDoTasksById(JsonPlaceholderHttpService jsonPlaceholderHttpService, long id) {
        System.err.println("\nОтримання всіх не виконаних задач юзера з id: " + id);
        var tasks = jsonPlaceholderHttpService.getUncompletedTodosForUser(id);
        System.out.println("\nОтриманно" + tasks.size() + "задач з completed = false для юзера з id: " + id);
        tasks.forEach(System.out::println);
    }

    private static void createJsonWithCommentsWithCommentsOfPost(JsonPlaceholderHttpService jsonPlaceholderHttpService, long id) {
        System.err.println("\nОтримання всіх коментарів для останнього поста у юзера з id: " + id);
        jsonPlaceholderHttpService.writeCommentsOfLastPostToFile(id);
    }

    private static void getAllUsersMethodAndTest(JsonPlaceholderHttpService jsonPlaceholderHttpService) {
        System.err.println("\nОтримання всіх юзерів");
        List<User> users = jsonPlaceholderHttpService.getAllUsers();
        System.out.println("Отримано " + users.size() + " юзерів з імейлами: ");
        users.forEach(user -> System.out.println(user.getEmail() + " "));
    }

    private static void createMethodAndTest(JsonPlaceholderHttpService jsonPlaceholderHttpService, NewUser newUer) {
        System.err.println("\nСтворення нового User");
        System.out.println("JSON нового юзера: " + newUer.json);
        var createdUser = jsonPlaceholderHttpService.createUser(newUer.json);
        var newId = createdUser.getId();
        var newEmail = createdUser.getEmail();
        newUer.newUser.setId(newId); // оновлення тестового об'єкту згідно з данними з сайту
        System.out.println("Новий User id: " + newId + ", з імейлом: " + newEmail);
        assert newEmail.equals(newUer.newUser.getEmail());
    }

    private static void updateMethodAndTest(JsonPlaceholderHttpService jsonPlaceholderHttpService) {
        System.err.println("\nОновлення імейлу юзера");
        var brandNewEmail = "brand-new-email-ever@email.email";
        var user = getUserById(jsonPlaceholderHttpService, 1);
        System.out.println("Оновлення User з id: " + user.getId() + " і встановлення імейлу: " + brandNewEmail);
        user.setEmail(brandNewEmail);
        var updatedUser = jsonPlaceholderHttpService.updateUser(Utils.parseToJson(user), user.getId());
        var newEmail = updatedUser.getEmail();
        System.out.println("Новий імейл для id " + user.getId() + ": " + newEmail);
        assert newEmail.equals(brandNewEmail);
    }

    private static User getUserById(JsonPlaceholderHttpService jsonPlaceholderHttpService, long id) {
        var user = jsonPlaceholderHttpService.getUserById(id);
        System.out.println("Знайдено User з імейлом: " + user.getEmail() + " по id " + user.getId());
        assert user.getId().equals(id);
        return user;
    }

    private static User getUserByUsername(JsonPlaceholderHttpService jsonPlaceholderHttpService, String username) {
        var user = jsonPlaceholderHttpService.getUserByUsername(username);
        System.out.println("Знайдено User з імейлом: " + user.getEmail() + " по username " + user.getUsername());
        assert user.getUsername().equals(username);
        return user;
    }

    private static void getUserMethodsAndTests(JsonPlaceholderHttpService jsonPlaceholderHttpService, long id) {
        System.err.println("\nЗнаходження User по id та по username");
        var user = getUserById(jsonPlaceholderHttpService, id);
        System.out.println("User з id: " + user.getId() + " має username " + user.getUsername());
        var user1 = getUserByUsername(jsonPlaceholderHttpService, user.getUsername());
        System.out.println("User з username: " + user1.getUsername() + " має id " + user.getId());
        assert user1.getId().equals(id);
    }


    public static class NewUser {
        public User newUser;
        public String json =
                """
                        {
                            "name": "Kelly",
                            "username": "Mikael",
                            "email": "DJIEcwd@adw.eef",
                            "address": {
                              "street": "Just avenue",
                              "suite": "Apt. 12987",
                              "city": "Vice city",
                              "zipcode": "29387-8765",
                              "geo": {
                                "lat": "25.761681",
                                "lng": "-80.191788"
                              }
                            },
                            "phone": "9-7897--87687-887",
                            "website": "byby.com",
                            "company": {
                              "name": "Cherry pop",
                              "catchPhrase": "One two three I'm gonna find you",
                              "bs": "1 day"
                            }
                          }""";

        public NewUser() {
            this.newUser = Utils.parseFromJson(json, User.class);
        }
    }

}
