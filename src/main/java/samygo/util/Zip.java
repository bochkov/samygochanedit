package samygo.util;

import java.io.*;
import java.util.zip.*;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class Zip {

    private static final int BUFFER = 2048;

    /**
     * stores all file found in targetDir into the archive specified
     * @param zipfile file to be created
     * @param dir     directory of which all files will be compressed into the zipfile
     * @return number of files stored in the archive
     */
    public static int compress(File zipfile, File dir) throws IOException {
        int i = 0;
        try (CheckedOutputStream checksum = new CheckedOutputStream(new FileOutputStream(zipfile), new Adler32());
             ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(checksum))) {
            // out.setMethod(ZipOutputStream.DEFLATED)
            byte[] data = new byte[BUFFER];

            // get a list of files from current directory
            String[] files = dir.list();
            if (files == null)
                throw new IOException("no files in directory " + dir);

            for (String file : files) {
                LOG.info("Adding: {}", file);
                File f = new File(dir, file);
                try (BufferedInputStream origin = new BufferedInputStream(new FileInputStream(f), BUFFER)) {
                    ZipEntry entry = new ZipEntry(f.getName());
                    out.putNextEntry(entry);
                    int count;
                    while ((count = origin.read(data, 0, BUFFER)) != -1) {
                        out.write(data, 0, count);
                    }
                }
            }
            LOG.info("checksum: {}", checksum.getChecksum().getValue());
            return i;
        }
    }

    /**
     * extracts all files found in archive into the targetDir
     * @param archive   file to be extracted
     * @param targetDir directory of which all files will be compressed into the zipfile
     * @return number of files extracted off the archive
     */
    public static int decompress(File archive, File targetDir) throws IOException {
        int files = 0;
        try (CheckedInputStream cis = new CheckedInputStream(new FileInputStream(archive), new Adler32());
             ZipInputStream zis = new ZipInputStream(new BufferedInputStream(cis))) {
            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {
                LOG.info("Extracting: {}", entry);
                byte[] data = new byte[BUFFER];
                // write the files to the disk
                try (FileOutputStream fos = new FileOutputStream(new File(targetDir, entry.getName()));
                     BufferedOutputStream dest = new BufferedOutputStream(fos, BUFFER)) {
                    int count;
                    while ((count = zis.read(data, 0, BUFFER)) != -1) {
                        dest.write(data, 0, count);
                    }
                    ++files;
                }
            }
            LOG.info("Checksum: {}", cis.getChecksum().getValue());
            return files;
        }
    }

    private Zip() {
    }
}