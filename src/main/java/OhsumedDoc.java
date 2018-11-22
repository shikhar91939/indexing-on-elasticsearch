import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * Created by shikhar.prasoon on 5/20/17.
 */
public class OhsumedDoc {
    private String file_name;
    private String body;
    private String split;
    private Set<String> codes;
    private int body_field_length;

    OhsumedDoc(String f, String txt) {
        file_name = f;
        body = txt;
        codes = new HashSet<>();
        body_field_length = countWords(body);
    }

    private int countWords(String body) {
        return body.split("\\s").length;
    }

    public String getFile_name() {
        return file_name;
    }

    public void setFile_name(String file_name) {
        this.file_name = file_name;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getSplit() {
        return split;
    }

    public void setSplit(String split) {
        this.split = split;
    }

    public Set<String> getCodes() {
        return codes;
    }

    public void setCodes(Set<String> codes) {
        this.codes = codes;
    }

    public void addCode(String code) {
        codes.add(code);
    }

    public int getBody_field_length() {
        return body_field_length;
    }

    public void setBody_field_length(int body_field_length) {
        this.body_field_length = body_field_length;
    }

    public static void main(String[] args) {
        Random random = new Random();
//        IntStream.range(1, 30).forEach(ignore->System.out.println(random.nextInt(5)));
        IntStream.generate(() -> random.nextInt(5)).limit(20).forEach(System.out::println);
    }
}