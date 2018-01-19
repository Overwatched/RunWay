package william.course.summer.umeo.runway.Model;

import android.content.Context;
import android.util.Log;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;
import java.io.File;

/**
 * Manages the functionallity to store and retrieve runs
 * from XML-files.
 * However this class seems to have a bug somewhere, the
 * idea is for the XML-files to be persistent. However, they
 * only exists during runtime. And i can't seem to figure out why.
 */
public class StorageManager {
    private static final String TAG = "StorageManager";
    private File xmlfile;

    /**
     * Writes data to the xml file
     * @param ctx the context
     * @param run the class to be written
     * @throws Exception file not found, etc
     */
    public void write(Context ctx, Run run) throws Exception {
        Serializer serializer = new Persister();
        xmlfile = new File(ctx.getFilesDir() + "/runs.xml");
        serializer.write(run, xmlfile);
        Log.d(TAG, "write: Serializer done");
    }

    /**
     * Reads data from the xml file
     * @param ctx the context
     * @return A composit class retrieved from the xml file
     * @throws Exception file not found, etc
     */
    public Run read(Context ctx) throws Exception {
        Serializer serializer = new Persister();
        File source = new File(ctx.getFilesDir() + "/runs.xml");
        Run runs = serializer.read(Run.class, source);
        Log.d(TAG, "read: " + runs.getRuns().size());
        return runs;
    }

}
