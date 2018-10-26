import com.google.gson.Gson;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.entity.ContentType;
import org.apache.http.nio.entity.NStringEntity;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;

/**
 * Created by shikhar.prasoon on 5/20/17.
 */
public class Indexer {
    private static String ohsumedDir_path = "/Users/Elasticsearch/Downloads/ohsumed-first-20000-docs";

    public static void main(String[] args) {
        try {
            // getting all files
            List<File> files = DirWalker.getFiles(ohsumedDir_path);
            System.out.println("files.size(): " + files.size());
            System.exit(0);
//            System.out.println("entered for loop");
//            RestClient restClient = RestClient.builder(
//                    new HttpHost("localhost", 9200, "http"),
//                    new HttpHost("localhost", 9205, "http")).build();
//
//            long total_indexedFiles = 0;
//            for (File file : files) {
//                // System.out.println(file.getName());
//                try  {
//                    String content = new String(Files.readAllBytes(Paths.get(file.getPath())));
//                    String name = file.getName();
//
//                    // Using Gson to make java Objects and then convert them to json strings
//                    Ohsumed_doc obj_ohsOhsumed_doc = new Ohsumed_doc(name, content);
//                    String jsonSource_gson = (new Gson()).toJson(obj_ohsOhsumed_doc);
//
//
//                    HttpEntity httpEntity = new NStringEntity(
//                            jsonSource_gson,
//                            ContentType.APPLICATION_JSON);
//                    Response response = restClient.performRequest(
//                            "PUT",
//                            "ohsumed/document/"+(++total_indexedFiles),
//                            Collections.emptyMap(),
//                            httpEntity);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//
//
//                if (total_indexedFiles % 1000 == 0)
//                    System.out.println("indexed " + total_indexedFiles + "th file");
//            }
//            System.out.println("indexed a total of"+total_indexedFiles+".");
        }catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }
}