package IK.http;

import com.fasterxml.jackson.core.type.TypeReference;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.net.http.HttpRequest.BodyPublishers;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.List;

public class JsonPlaceholderHttpService {

    private final HttpClient client;
    private final String BASE_URL = "https://jsonplaceholder.typicode.com";

    private final static String USERS = "/users";
    private final static String POSTS = "/posts";
    public JsonPlaceholderHttpService() {
        client = HttpClient.newHttpClient();
    }

    public User createUser(String jsonUser) {
        HttpResponse<String> response;
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + USERS))
                    .header("Content-Type", "application/json")
                    .POST(BodyPublishers.ofString(jsonUser))
                    .build();
            response = client.send(request, BodyHandlers.ofString());
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }

        return Utils.parseFromJson(response.body(), User.class);
    }

    public User updateUser(String jsonUser, long id) {
        HttpResponse<String> response;
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + USERS + "/" + id))
                    .header("Content-Type", "application/json")
                    .PUT(BodyPublishers.ofString(jsonUser))
                    .build();
            response = client.send(request, BodyHandlers.ofString());
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
        return Utils.parseFromJson(response.body(), User.class);
    }

    public List<User> getAllUsers() {
        HttpResponse<String> response;
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + USERS))
                    .GET()
                    .build();

            response = client.send(request, BodyHandlers.ofString());
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
        return Utils.parseObjectsJson(response.body(), new TypeReference<List<User>>() {});
    }

    public User getUserById(long userId) {
        HttpResponse<String> response;
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + USERS + "/" + userId))
                    .GET()
                    .build();

            response = client.send(request, BodyHandlers.ofString());
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
        return Utils.parseFromJson(response.body(), User.class);
    }

    public int deleteUser(int userId) {
        HttpResponse<Void> response;
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + USERS + "/" + userId))
                    .DELETE()
                    .build();

            response = client.send(request, BodyHandlers.discarding());
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
        return response.statusCode();
    }

    public User getUserByUsername(String username) {
        HttpResponse<String> response;
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + USERS + "?username=" + username))
                    .GET()
                    .build();

            response = client.send(request, BodyHandlers.ofString());
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }

        var list = Utils.parseObjectsJsonArray(response.body(), new TypeReference<User[]>() {});
        return list.isEmpty() ? null : list.get(0);
    }

    public void writeCommentsOfLastPostToFile(long userId) {

        List<Post> posts;

        try {
            HttpRequest postsRequest = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + USERS + "/" + userId + POSTS))
                    .build();
            HttpResponse<String> postsResponse = client.send(postsRequest, HttpResponse.BodyHandlers.ofString());
            posts = Utils.parseObjectsJson(postsResponse.body(), new TypeReference<List<Post>>() {});
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e.getMessage(), e);
        }

        long lastPostId = posts.stream().map(Post::getId).sorted(Comparator.reverseOrder()).toList().get(0);

        try {
            HttpRequest commentsRequest = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + POSTS + "/" + lastPostId + "/comments"))
                    .build();
            HttpResponse<String> commentsResponse = client.send(commentsRequest, HttpResponse.BodyHandlers.ofString());
            Path path = Paths.get("user-" + userId + "-post-" + lastPostId + "-comments.json");
            System.out.println("Файл " + path + " буде створено і буде мати записи про коментарі з останнього поста який належить юзеру з id " + userId);
            Files.writeString(path, commentsResponse.body());
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public List<Task> getUncompletedTodosForUser(long userId) {
        HttpResponse<String> todosResponse;

        try {
            HttpRequest todosRequest = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + USERS + "/" + userId + "/todos?completed=false"))
                    .build();
            todosResponse = client.send(todosRequest, HttpResponse.BodyHandlers.ofString());
            return Utils.parseObjectsJsonArray(todosResponse.body(), new TypeReference<Task[]>() {});
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

}
