import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by shikhar.prasoon on 5/20/17.
 */
public class Ohsumed_doc {
    private String fileName;
    private String content;
    private String split;
    private Set<String> codes;

    Ohsumed_doc(String f, String txt) {
        fileName = f;
        content = txt;
        codes = new HashSet<>();
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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
}