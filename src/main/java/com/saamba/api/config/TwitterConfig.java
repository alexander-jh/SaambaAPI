package com.saamba.api.config;

import com.saamba.api.enums.ClientTypes;
import com.saamba.api.entity.user.Tweet;

import io.github.redouane59.twitter.TwitterClient;
import io.github.redouane59.twitter.dto.endpoints.AdditionalParameters;
import io.github.redouane59.twitter.dto.tweet.TweetV2;
import io.github.redouane59.twitter.dto.user.UserList;
import io.github.redouane59.twitter.dto.user.UserV2;
import io.github.redouane59.twitter.signature.TwitterCredentials;

import lombok.extern.slf4j.Slf4j;
import opennlp.tools.tokenize.WhitespaceTokenizer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.File;
import java.util.*;

@Component
@Slf4j
public class TwitterConfig implements ClientConfig {

    @Value("${client.twitter.results.max}")
    private int maxResults;

    @Value("${client.twitter.accesskey}")
    private String accessKey;

    @Value("${client.twitter.secretkey}")
    private String secretKey;

    @Value("${client.twitter.bearer.token}")
    private String bearerToken;

    @Value("${client.twitter.accesstoken}")
    private String accessToken;

    @Value("${client.twitter.accesssecret}")
    private String accessTokenSecret;

    protected TwitterClient twitterClient;

    public TwitterConfig() { }

    /**
     * Necessary function which instantiates after the no-arg constructor
     * is invoked. Needed since values aren't injected until after creation
     * of the class.
     * @return      - twitter agent
     */
    @PostConstruct
    public TwitterConfig init() {
        this.twitterClient = new TwitterClient(
                TwitterCredentials.builder().accessToken(accessToken)
                        .accessTokenSecret(accessTokenSecret)
                        .apiKey(accessKey)
                        .apiSecretKey(secretKey)
                        .build());
        return this;
    }

    @Override
    public ClientTypes getClientType() {
        return ClientTypes.Twitter;
    }

    @Override
    public void refreshCredentials() {
        // Undefined
    }

    /**
     * Gets pinned tweet for an account name
     * @param accountName   - string twitter handle
     * @return              - string of pinned tweet
     */
    public String getPinnedTweet(String accountName) {
        String pinned = "";
        try {
            pinned = twitterClient.getUserFromUserName(accountName)
                    .getPinnedTweet()
                    .getText();
        } catch(Exception e) {
            log.error("Account " + accountName + " has no pinned tweet.");
        }
        return pinned;
    }

    /**
     * Returns a list of all tweets associated with a twitter handle. The
     * current limitation is that this call assumes a profiles must
     * be public.
     * @param accountName   - string twitter handle
     * @return              - a list of tweet objects
     */
    private List<Tweet> getTweets(String accountName) {
        Tweet t;
        List<Tweet> tweets = new ArrayList<>();
        try {
            for (TweetV2.TweetData td : twitterClient.getUserTimeline(
                    twitterClient.getUserFromUserName(accountName).getId(),
                    AdditionalParameters.builder()
                            .recursiveCall(false)
                            .maxResults(maxResults)
                            .build()
            ).getData()) {
                t = new Tweet();
                t.setDate(td.getCreatedAt().toString());
                t.setMessage(td.getText());
                tweets.add(t);
            }
        } catch (Exception e) {
            //TODO: figure out graceful resolution to frontend
        }
        return tweets;
    }

    /**
     *
     * @param accountName   - string twitter handle
     * @return              - a list of the text from tweets
     */
    public List<String> tweetTexts(String accountName){
        if(accountName.indexOf("@")==0){
            accountName = accountName.substring(1);
        }
        List<Tweet> twl = getTweets(accountName);
        List<String> l = new ArrayList<>();
        int i = 0;
        if(twl.size()>0) {
            while (i < twl.toArray().length) {
                Tweet t = twl.get(i);
                l.add(t.getMessage());
                i++;
            }
        }
        // returns empty arraylist if no tweets, could change it to null if easier.
        return l;
    }

    private Map<String, String> artistMap = fillArtistMap();

    private Map<String, String> fillArtistMap() {
        Map<String, String> artistMap = new HashMap<>();
        try{
            //System.out.println(new File(".").getAbsolutePath());
            File f = new File("data/followerData.csv");
            Scanner s = new Scanner(f);
            while(s.hasNext()){
                String str = s.nextLine();
                artistMap.put(str.substring(str.indexOf("@") + 1), str.substring(0, str.indexOf("@") - 1));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return artistMap;
    }

    /**
     *
     * @param accountName   - string twitter handle
     * @return              - a list of the accounts they are following
     */
    public List<String> getFollowingList(String accountName){
        if(accountName.indexOf("@")==0){
            accountName = accountName.substring(1);
        }
        UserList uL = twitterClient.getFollowing(twitterClient.getUserFromUserName(accountName).getId());
        List<String> uL2 = new ArrayList<>();
        List<UserV2.UserData> l2 = uL.getData();
        for (UserV2.UserData userData : l2)
            if (artistMap.containsKey(userData.getName()))
                uL2.add(artistMap.get(userData.getName()));
        return uL2;
    }

   public List<String> getConcepts(List<String> tweets) {
       WhitespaceTokenizer tokenizer = WhitespaceTokenizer.INSTANCE;
       List<String> words = new ArrayList<>();
       String[] stop = new String[] {"&amp;", "rt", "a", "able", "about", "above", "according", "accordingly", "across", "actually", "after", "afterwards", "again", "against", "ain't", "all", "allow", "allows", "almost", "alone", "along", "already", "also", "although", "always", "am", "among", "amongst", "an", "and", "another", "any", "anybody", "anyhow", "anyone", "anything", "anyway", "anyways", "anywhere", "apart", "appear", "appreciate", "appropriate", "are", "aren't", "around", "as", "a's", "aside", "ask", "asking", "associated", "at", "available", "away", "awfully", "be", "became", "because", "become", "becomes", "becoming", "been", "before", "beforehand", "behind", "being", "believe", "below", "beside", "besides", "best", "better", "between", "beyond", "both", "brief", "but", "by", "came", "can", "cannot", "cant", "can't", "cause", "causes", "certain", "certainly", "changes", "clearly", "c'mon", "co", "com", "come", "comes", "concerning", "consequently", "consider", "considering", "contain", "containing", "contains", "corresponding", "could", "couldn't", "course", "c's", "currently", "definitely", "described", "despite", "did", "didn't", "different", "do", "does", "doesn't", "doing", "don", "done", "don't", "down", "downwards", "during", "each", "edu", "eg", "eight", "either", "else", "elsewhere", "enough", "entirely", "especially", "et", "etc", "even", "ever", "every", "everybody", "everyone", "everything", "everywhere", "ex", "exactly", "example", "except", "far", "few", "fifth", "first", "five", "followed", "following", "follows", "for", "former", "formerly", "forth", "four", "from", "further", "furthermore", "get", "gets", "getting", "given", "gives", "go", "goes", "going", "gone", "got", "gotten", "greetings", "had", "hadn't", "happens", "hardly", "has", "hasn't", "have", "haven't", "having", "he", "he'd", "he'll", "hello", "help", "hence", "her", "here", "hereafter", "hereby", "herein", "here's", "hereupon", "hers", "herself", "he's", "hi", "him", "himself", "his", "hither", "hopefully", "how", "howbeit", "however", "how's", "i", "i'd", "ie", "if", "ignored", "i'll", "i'm", "immediate", "in", "inasmuch", "inc", "indeed", "indicate", "indicated", "indicates", "inner", "insofar", "instead", "into", "inward", "is", "isn't", "it", "it'd", "it'll", "its", "it's", "itself", "i've", "just", "keep", "keeps", "kept", "know", "known", "knows", "last", "lately", "later", "latter", "latterly", "least", "less", "lest", "let", "let's", "like", "liked", "likely", "little", "look", "looking", "looks", "ltd", "mainly", "many", "may", "maybe", "me", "mean", "meanwhile", "merely", "might", "more", "moreover", "most", "mostly", "much", "must", "mustn't", "my", "myself", "name", "namely", "nd", "near", "nearly", "necessary", "need", "needs", "neither", "never", "nevertheless", "new", "next", "nine", "no", "nobody", "non", "none", "noone", "nor", "normally", "not", "nothing", "novel", "now", "nowhere", "obviously", "of", "off", "often", "oh", "ok", "okay", "old", "on", "once", "one", "ones", "only", "onto", "or", "other", "others", "otherwise", "ought", "our", "ours", "ourselves", "out", "outside", "over", "overall", "own", "particular", "particularly", "per", "perhaps", "placed", "please", "plus", "possible", "presumably", "probably", "provides", "que", "quite", "qv", "rather", "rd", "re", "really", "reasonably", "regarding", "regardless", "regards", "relatively", "respectively", "right", "s", "said", "same", "saw", "say", "saying", "says", "second", "secondly", "see", "seeing", "seem", "seemed", "seeming", "seems", "seen", "self", "selves", "sensible", "sent", "serious", "seriously", "seven", "several", "shall", "shan't", "she", "she'd", "she'll", "she's", "should", "shouldn't", "since", "six", "so", "some", "somebody", "somehow", "someone", "something", "sometime", "sometimes", "somewhat", "somewhere", "soon", "sorry", "specified", "specify", "specifying", "still", "sub", "such", "sup", "sure", "t", "take", "taken", "tell", "tends", "th", "than", "thank", "thanks", "thanx", "that", "thats", "that's", "the", "their", "theirs", "them", "themselves", "then", "thence", "there", "thereafter", "thereby", "therefore", "therein", "theres", "there's", "thereupon", "these", "they", "they'd", "they'll", "they're", "they've", "think", "third", "this", "thorough", "thoroughly", "those", "though", "three", "through", "throughout", "thru", "thus", "to", "together", "too", "took", "toward", "towards", "tried", "tries", "truly", "try", "trying", "t's", "twice", "two", "un", "under", "unfortunately", "unless", "unlikely", "until", "unto", "up", "upon", "us", "use", "used", "useful", "uses", "using", "usually", "value", "various", "very", "via", "viz", "vs", "want", "wants", "was", "wasn't", "way", "we", "we'd", "welcome", "well", "we'll", "went", "were", "we're", "weren't", "we've", "what", "whatever", "what's", "when", "whence", "whenever", "when's", "where", "whereafter", "whereas", "whereby", "wherein", "where's", "whereupon", "wherever", "whether", "which", "while", "whither", "who", "whoever", "whole", "whom", "who's", "whose", "why", "why's", "will", "willing", "wish", "with", "within", "without", "wonder", "won't", "would", "wouldn't", "yes", "yet", "you", "you'd", "you'll", "your", "you're", "yours", "yourself", "yourselves", "you've", "zero"};
       List<String> stopwords = Arrays.asList(stop);
       for(String tweet : tweets) {
           String lowerCaseTweet = tweet.toLowerCase();
           words.addAll(Arrays.asList(tokenizer.tokenize(lowerCaseTweet)));
       }
       words.removeAll(stopwords);
       Map<String, Integer> hm = new HashMap<String, Integer>();
       for (String i : words) {
           if(i.charAt(0) != '@' && (i.length()>4 && !i.substring(0,4).equals("http"))) {
               Integer j = hm.get(i);
               hm.put(i, (j == null) ? 1 : j + 1);
           }
       }
       List<Map.Entry<String, Integer>> frequencyList = new ArrayList<Map.Entry<String, Integer>>(hm.entrySet());
       frequencyList.sort(Comparator.comparing(Map.Entry<String, Integer>::getValue).reversed());
       List<String> concepts = new ArrayList<>();
       for(Map.Entry<String, Integer> entry : frequencyList) {
           concepts.add(entry.getKey());
       }
       return concepts;

   }


}
