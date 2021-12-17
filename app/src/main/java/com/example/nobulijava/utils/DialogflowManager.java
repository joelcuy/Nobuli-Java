package com.example.nobulijava.utils;
//
//import androidx.lifecycle.ViewModel;
//
//import com.google.android.gms.common.api.ApiException;
//import com.google.api.gax.paging.Page;
//import com.google.auth.oauth2.GoogleCredentials;
//import com.google.cloud.dialogflow.v2.DetectIntentResponse;
//import com.google.cloud.dialogflow.v2.QueryInput;
//import com.google.cloud.dialogflow.v2.QueryResult;
//import com.google.cloud.dialogflow.v2.SessionName;
//import com.google.cloud.dialogflow.v2.SessionsClient;
//import com.google.cloud.dialogflow.v2.TextInput;
//import com.google.cloud.storage.Bucket;
//import com.google.cloud.storage.Storage;
//import com.google.cloud.storage.StorageOptions;
//import com.google.common.collect.Lists;
//import com.google.common.collect.Maps;
//
//import java.io.FileInputStream;
//import java.io.IOException;
//import java.io.InputStream;
//import java.util.List;
//import java.util.Map;
//import java.util.concurrent.Executors;
//import java.util.concurrent.ThreadLocalRandom;
//
//import kotlinx.coroutines.Dispatchers;
//
//public class DialogflowManager extends ViewModel {
//
//    public static void authExplicit(InputStream jsonPath) throws IOException {
//        Executors.newSingleThreadExecutor().execute(() -> {
//            // You can specify a credential file by providing a path to GoogleCredentials.
//            // Otherwise credentials are read from the GOOGLE_APPLICATION_CREDENTIALS environment variable.
//            GoogleCredentials credentials = null;
//            try {
//                credentials = GoogleCredentials.fromStream(jsonPath)
//                        .createScoped(Lists.newArrayList("https://www.googleapis.com/auth/cloud-platform"));
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            Storage storage = StorageOptions.newBuilder().setCredentials(credentials).build().getService();
//
//            System.out.println("Buckets:");
//            Page<Bucket> buckets = storage.list();
//            for (Bucket bucket : buckets.iterateAll()) {
//                System.out.println(bucket.toString());
//            }
//        });
//    }
//
//    // DialogFlow API Detect Intent sample with text inputs.
//    public static Map<String, QueryResult> detectIntentTexts(String projectId, List<String> texts, String sessionId, String languageCode) throws IOException, ApiException {
//        Map<String, QueryResult> queryResults = Maps.newHashMap();
//        // Instantiates a client
//        try (SessionsClient sessionsClient = SessionsClient.create()) {
//            // Set the session name using the sessionId (UUID) and projectID (my-project-id)
//            SessionName session = SessionName.of(projectId, sessionId);
//            System.out.println("Session Path: " + session.toString());
//
//            // Detect intents for each text input
//            for (String text : texts) {
//                // Set the text (hello) and language code (en-US) for the query
//                TextInput.Builder textInput =
//                        TextInput.newBuilder().setText(text).setLanguageCode(languageCode);
//
//                // Build the query with the TextInput
//                QueryInput queryInput = QueryInput.newBuilder().setText(textInput).build();
//
//                // Performs the detect intent request
//                DetectIntentResponse response = sessionsClient.detectIntent(session, queryInput);
//
//                // Display the query result
//                QueryResult queryResult = response.getQueryResult();
//
//                System.out.println("====================");
//                System.out.format("Query Text: '%s'\n", queryResult.getQueryText());
//                System.out.format(
//                        "Detected Intent: %s (confidence: %f)\n",
//                        queryResult.getIntent().getDisplayName(), queryResult.getIntentDetectionConfidence());
//                System.out.format(
//                        "Fulfillment Text: '%s'\n",
//                        queryResult.getFulfillmentMessagesCount() > 0 ? queryResult.getFulfillmentMessages(0).getText()
//                                : "Triggered Default Fallback Intent");
//
//                queryResults.put(text, queryResult);
//            }
//        }
//        return queryResults;
//    }
//
//    public static String generateSessionID() {
//        int sessionID = ThreadLocalRandom.current().nextInt(1000000, 9999999);
//
//        return Integer.toString(sessionID);
//    }
//}
