/* Copyright (c) 2007-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package twitter;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

import java.time.Instant;
import java.util.*;

/**
 * SocialNetworkTest provides unit tests for the SocialNetwork class.
 */
public class SocialNetworkTest {

    @Test
    public void testGuessFollowsGraphEmptyList() {
        List<Tweet> tweets = new ArrayList<>();
        Map<String, Set<String>> followsGraph = SocialNetwork.guessFollowsGraph(tweets);
        assertTrue(followsGraph.isEmpty());
    }

    @Test
    public void testGuessFollowsGraphNoMentions() {
        List<Tweet> tweets = Arrays.asList(
            new Tweet(1, "alishba", "Hello world!", Instant.now()),
            new Tweet(2, "yousuf", "Another day, another tweet.", Instant.now())
        );
        Map<String, Set<String>> followsGraph = SocialNetwork.guessFollowsGraph(tweets);
        assertTrue(followsGraph.get("alishba").isEmpty());
        assertTrue(followsGraph.get("yousuf").isEmpty());
    }

    @Test
    public void testGuessFollowsGraphSingleMention() {
        List<Tweet> tweets = Arrays.asList(
            new Tweet(1, "alishba", "Hello @yousuf!", Instant.now())
        );
        Map<String, Set<String>> followsGraph = SocialNetwork.guessFollowsGraph(tweets);
        assertTrue(followsGraph.containsKey("alishba"));
        assertTrue(followsGraph.get("alishba").contains("yousuf"));
    }

    @Test
    public void testGuessFollowsGraphMultipleMentions() {
        List<Tweet> tweets = Arrays.asList(
            new Tweet(1, "alishba", "Hi @yousuf and @abdullah", Instant.now())
        );
        Map<String, Set<String>> followsGraph = SocialNetwork.guessFollowsGraph(tweets);
        assertTrue(followsGraph.containsKey("alishba"));
        assertTrue(followsGraph.get("alishba").contains("yousuf"));
        assertTrue(followsGraph.get("alishba").contains("abdullah"));
    }

    @Test
    public void testGuessFollowsGraphMultipleTweetsSameUser() {
        List<Tweet> tweets = Arrays.asList(
            new Tweet(1, "alishba", "Hi @yousuf", Instant.now()),
            new Tweet(2, "alishba", "@abdullah how are you?", Instant.now())
        );
        Map<String, Set<String>> followsGraph = SocialNetwork.guessFollowsGraph(tweets);
        assertTrue(followsGraph.containsKey("alishba"));
        assertTrue(followsGraph.get("alishba").contains("yousuf"));
        assertTrue(followsGraph.get("alishba").contains("abdullah"));
    }

    @Test
    public void testInfluencersEmptyGraph() {
        Map<String, Set<String>> followsGraph = new HashMap<>();
        List<String> influencers = SocialNetwork.influencers(followsGraph);
        assertTrue(influencers.isEmpty());
    }

    @Test
    public void testInfluencersSingleUserNoFollowers() {
        Map<String, Set<String>> followsGraph = new HashMap<>();
        followsGraph.put("alishba", new HashSet<>());
        List<String> influencers = SocialNetwork.influencers(followsGraph);
        assertTrue(influencers.isEmpty());
    }

    @Test
    public void testInfluencersSingleInfluencer() {
        Map<String, Set<String>> followsGraph = new HashMap<>();
        followsGraph.put("alishba", Set.of("yousuf"));
        List<String> influencers = SocialNetwork.influencers(followsGraph);
        assertEquals(Arrays.asList("yousuf"), influencers);
    }

    @Test
    public void testInfluencersMultipleInfluencers() {
        Map<String, Set<String>> followsGraph = new HashMap<>();
        followsGraph.put("alishba", Set.of("yousuf", "abdullah"));
        followsGraph.put("dave", Set.of("abdullah"));
        List<String> influencers = SocialNetwork.influencers(followsGraph);
        assertEquals(Arrays.asList("abdullah", "yousuf"), influencers);
    }

    @Test
    public void testInfluencersTiedInfluence() {
        Map<String, Set<String>> followsGraph = new HashMap<>();
        followsGraph.put("alishba", Set.of("yousuf"));
        followsGraph.put("abdullah", Set.of("yousuf"));
        List<String> influencers = SocialNetwork.influencers(followsGraph);
        assertEquals(Arrays.asList("yousuf"), influencers);
    }
}
