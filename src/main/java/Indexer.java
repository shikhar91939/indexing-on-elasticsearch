import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.entity.ContentType;
import org.apache.http.nio.entity.NStringEntity;
import org.apache.http.util.EntityUtils;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Created by shikhar.prasoon on 5/20/17.
 */
public class Indexer {
    private static Gson gson;
    private static final String INDEX_NAME = "ohsumed_20000";
    private static String ohsumedDir_path = "/Users/Elasticsearch/Downloads/ohsumed-first-20000-docs";
    private static long total_indexedFiles = 0;
    private static Random random = new Random();

    public static void main(String[] args) {
        gson = new Gson();
        JsonParser parser = new JsonParser();
        try {
            // getting all files
            List<File> files = DirWalker.getFiles(ohsumedDir_path);
            System.out.println("about to index " + files.size() + "files");
            RestClient restClient = RestClient.builder(
                    new HttpHost("localhost", 9200, "http"),
                    new HttpHost("localhost", 9205, "http")).build();

            for (File file : files) {
                String name = file.getName();
                File parent = file.getParentFile();
                String parentName = parent.getName();
                String grandParentName = parent.getParentFile().getName();
                validateCodeAndSplit(parentName, grandParentName, file);

                /* for debugging: */
//                if (name.equals("0012431")) {
//                    System.out.println(",");
//                }


                Map<Long, String> updatedDocsJson = updateDocsIfAlreadyIndexed(restClient, parser, name, parentName);
//                if (updatedDocsJson.isEmpty()) {
//                    System.out.println("Unexpected: Doc already indexed with correct code."); // wrong: updatedDocsJson could be empty if the doc has never been indexed before
//                }
                if (updatedDocsJson.size() > 1) {
                    System.out.println("!");
                    // System.out.println(String.format("Unexpected: Found more than one doc saved with the same name, %s", name));
                }

                for (Map.Entry<Long, String> entry : updatedDocsJson.entrySet()) {
                    indexOhsumedDoc(restClient, entry.getValue(), entry.getKey());
                }

                // Using Gson to make java Objects and then convert them to json strings
                String content = new String(Files.readAllBytes(Paths.get(file.getPath())));
                OhsumedDoc ohsumedDoc = new OhsumedDoc(name, content);
                ohsumedDoc.addCode(parentName);
                ohsumedDoc.setSplit(grandParentName);
                randomlyPutInValidationSet(ohsumedDoc);
                indexOhsumedDoc(restClient, gson.toJson(ohsumedDoc), ++total_indexedFiles);

                if (total_indexedFiles % 1000 == 0)
                    System.out.println(String.format("\n %d files indexed", total_indexedFiles));
            }
            System.out.println("indexed a total of"+total_indexedFiles+".");
        }catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }

    /**
     * we need to reserve  20% of the training documents for the validation set.
     *
     * This method has 20% probability of marking the split field of the given document as “valid”.
     */
    private static void randomlyPutInValidationSet(OhsumedDoc ohsumedDoc) {
        if (random.nextInt(5) == 0) {
            ohsumedDoc.setSplit("valid");
        }
    }

    private static Response indexOhsumedDoc(RestClient restClient, String docJson, long index) throws IOException {
        System.out.print("."); // appears in console whenever indexing/updating a doc
        HttpEntity httpEntity = new NStringEntity(docJson, ContentType.APPLICATION_JSON);
        Response response = restClient.performRequest(
                "PUT",
                INDEX_NAME +"/document/"+index,
                Collections.emptyMap(),
                httpEntity);
        return response;
    }

    private static void validateCodeAndSplit(String code, String split, File file) {
        if (!code.startsWith("C")) {
            System.out.println(String.format("ERROR: directory name %s, for file %s not starting with 'C'." +
                    " Might not be 'document code' as expected", code, file.toString()));
        }
        if (!(split.equals("test") || split.equals("training"))) {
            System.out.println(String.format("ERROR: 'split' set to %s for file %s. But split can be either 'test' or 'training' only."));
        }
    }

    private static Map<Long, String> updateDocsIfAlreadyIndexed(RestClient restClient, JsonParser parser, String fileName, String newCode) throws IOException {
        Map<Long, String> updatedDocsJson = new HashMap();

        HttpEntity entityForSearch = new NStringEntity(
                String.format("{\"query\": {\"term\": { \"file_name\": \"%s\"} } }", fileName),
                ContentType.APPLICATION_JSON);

        Response responseGetDoc = restClient.performRequest(
                "GET",
                INDEX_NAME +"/document/"+"_search",
                Collections.emptyMap(),
                entityForSearch);

        String searchResult = EntityUtils.toString(responseGetDoc.getEntity());
        JsonObject searchResultJson = parser.parse(searchResult).getAsJsonObject();
        JsonArray searchHits = searchResultJson.getAsJsonObject("hits").getAsJsonArray("hits");
        if (searchHits.size() > 1) {
            System.out.println("breakpoint");
        }
        for (JsonElement searchHit : searchHits) { // there should not be more than one search result. because we are keeping one doc and updating it
            JsonObject searchHitObject = searchHit.getAsJsonObject();
            long index = searchHitObject.get("_id").getAsLong();
            JsonObject docJson = searchHitObject.get("_source").getAsJsonObject();
            //String split = docJson.get("split").getAsString(); // add regardless of split=test or training.
            JsonArray codes = docJson.get("codes").getAsJsonArray();
            if (addCodeIfNotAlreadyPresent(codes, newCode)) {
                updatedDocsJson.put(index, gson.toJson(docJson));
            }
        }
        return updatedDocsJson;
    }


    /**
     * adds the newCode to the json array, 'codes', if not already present
     *
     * @param codes codes already present in the indexed document
     * @param newCode the new code we want to add. This is the directory where we
     *                found the file in. (We want to index the file, and if already indexed, we just want to add the code in the)
     * @return {@code true} if the newCode was added, {@code false} if the newCode was not added
     */
    private static boolean addCodeIfNotAlreadyPresent(JsonArray codes, String newCode) {
        for (JsonElement code : codes) {
            if (code.getAsString().equals(newCode)) {
                return false; // code already present. change nothing
            }
        }
        codes.add(newCode);
        return true;
    }

//    private static Map<String, List<File>> generateDocFilesMap(List<File> files) {
//        Map<String, List<File>> map = new HashMap<>();
//        for (File file : files) {
//            String fileName = file.getName();
//            if (map.containsKey(fileName)) {
//                map.get(fileName).add(file);
//            } else {
//                map.put(fileName, Arrays.asList(file));
//            }
//        }
//        return map;
//    }
}