import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by shikhar.prasoon on 5/9/17.
 */
public class DirWalker {

    public static List<File> getFiles(String dir) {
        List<File> list = new ArrayList<>();
        recursiveAdd(new File(dir), list);
        return  list;
    }

    private static void recursiveAdd(File file, List<File> list) {
        // recursion
        if (file.isDirectory()) {
            String[] files_or_dirs = file.list(); // files_or_dirs can be files and directories
            // form javadoc:
            // The array will be empty if the directory is empty.
            // Returns null if this abstract pathname does not denote a directory, or if an I/O error occurs.

//            if (files_or_dirs != null) {
//                for (String component : files_or_dirs) {
//
//                }
//            }
            for (String file_or_dir : files_or_dirs)
                recursiveAdd(new File(file, file_or_dir), list);

        } else {
            //base case
            if (! file.getName().endsWith(".DS_Store"))
                list.add(file);
        }
    }
}
