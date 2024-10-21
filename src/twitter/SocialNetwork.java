package twitter;

import java.util.*;

/**
 * SocialNetwork provides methods that operate on a social network.
 */
public class SocialNetwork {

    /**
     * Guess who might follow whom, from evidence found in tweets.
     *
     * @param tweets
     *            a list of tweets providing the evidence, not modified by this
     *            method.
     * @return a social network (as defined above) in which Ernie follows Bert
     *         if and only if there is evidence for it in the given list of
     *         tweets.
     */
    public static Map<String, Set<String>> guessFollowsGraph(List<Tweet> tweets) {
        Map<String, Set<String>> followsGraph = new HashMap<>();

        for (Tweet tweet : tweets) {
            String author = tweet.getAuthor().toLowerCase();
//            Set<String> mentions = extractMentions(tweet);
            Set<String> mentions = new HashSet<>();
            String[] words = tweet.getText().split("\\s+");

            for (String word : words) {
                if (word.startsWith("@") && word.length() > 1) {
                    String mention = word.substring(1).toLowerCase().replaceAll("[^a-zA-Z0-9_]", "");
                    if (!mention.isEmpty()) {
                        mentions.add(mention);
                    }
                }
            }
            if (!followsGraph.containsKey(author)) {
                followsGraph.put(author, new HashSet<>());
            }
            followsGraph.get(author).addAll(mentions);
        }

        return followsGraph;
    }


    /**
     * Find the people in a social network who have the greatest influence, in
     * the sense that they have the most followers.
     *
     * @param followsGraph
     *            a social network (as defined above)
     * @return a list of all distinct Twitter usernames in followsGraph, in
     *         descending order of follower count.
     */
    public static List<String> influencers(Map<String, Set<String>> followsGraph) {
        Map<String, Integer> followerCount = new HashMap<>();

        for (Set<String> followers : followsGraph.values()) {
            for (String followed : followers) {
                followerCount.put(followed, followerCount.getOrDefault(followed, 0) + 1);
            }
        }

        List<String> influencers = new ArrayList<>(followerCount.keySet());
        influencers.sort((a, b) -> followerCount.get(b) - followerCount.get(a));

        return influencers;
    }
}
