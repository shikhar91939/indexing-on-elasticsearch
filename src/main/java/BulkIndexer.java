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
 * Created by shikhar.prasoon on 10/26/18.
 */
public class BulkIndexer {
    private static final String INDEX_NAME = "ohsumed_2000";
    private static final String ohsumedDir_path = "/Users/Elasticsearch/Downloads/ohsumed-first-20000-docs";

    public static void main(String[] args) {
        Gson gson = new Gson();
        try {
            // getting all files
            List<File> files = DirWalker.getFiles(ohsumedDir_path);
            System.out.println("about to index " + files.size() + "files");
            RestClient restClient = RestClient.builder(
                    new HttpHost("localhost", 9200, "http"),
                    new HttpHost("localhost", 9205, "http")).build();

            /*use this after this point : https://stackoverflow.com/questions/43339120/elasticsearch-bulk-insert-using-rest-client*/

            long total_indexedFiles = 0;
            for (File file : files) {
                 System.out.print(".");
                try  {
                    String content = new String(Files.readAllBytes(Paths.get(file.getPath())));
                    String name = file.getName();

                    // Using Gson to make java Objects and then convert them to json strings
                    Ohsumed_doc obj_ohsOhsumed_doc = new Ohsumed_doc(name, content);
                    String jsonSource_gson = gson.toJson(obj_ohsOhsumed_doc);

                    HttpEntity httpEntity = new NStringEntity(
                            jsonSource_gson,
                            ContentType.APPLICATION_JSON);

                    Response response = restClient.performRequest(
                            "PUT",
                            INDEX_NAME +"/document/"+(++total_indexedFiles),
                            Collections.emptyMap(),
                            httpEntity);
                } catch (Exception e) {
                    e.printStackTrace();
                }


                if (total_indexedFiles % 1000 == 0)
                    System.out.println("\n" + total_indexedFiles + " files indexed");
            }
            System.out.println("indexed a total of"+total_indexedFiles+".");
        }catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }
}