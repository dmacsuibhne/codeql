import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;

public class ZipSlipBad {
void writeZipEntry(ZipEntry entry, File destinationDir) throws IOException {
    File file = new File(destinationDir, entry.getName());
    FileOutputStream fos = new FileOutputStream(file); // BAD
    // ... write entry to fos ...
}
}
