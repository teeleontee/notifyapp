package edu.java;

import com.google.gson.Gson;
import edu.java.clients.details.GithubCommitInfo;
import edu.java.clients.details.StackOverflowAnswersResponse;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class Packer {

    static Gson gson = new Gson();

    public static String toJson(Object object) {
        return gson.toJson(object);
    }

    public static GithubCommitInfo[] githubCommitInfoFromJson(String json) {
        return gson.fromJson(json, GithubCommitInfo[].class);
    }

    public static StackOverflowAnswersResponse stackOverflowAnswersFromJson(String json) {
        return gson.fromJson(json, StackOverflowAnswersResponse.class);
    }
}
