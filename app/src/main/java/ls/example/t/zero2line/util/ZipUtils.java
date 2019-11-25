package ls.example.t.zero2line.util;

import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

/**
 * <pre>
 *     author: Blankj
 *     blog  : http://blankj.com
 *     time  : 2016/08/27
 *     desc  : utils about zip
 * </pre>
 */
public final class ZipUtils {

    private static final int BUFFER_LEN = 8192;

    private ZipUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    /**
     * Zip the files.
     *
     * @param srcFiles    The source of files.
     * @param zipFilePath The path of ZIP file.
     * @return {@code true}: success<br>{@code false}: fail
     * @throws IOException if an I/O error has occurred
     */
    public static boolean zipFiles(final java.util.Collection<String> srcFiles,
                                   final String zipFilePath)
            throws java.io.IOException {
        return zipFiles(srcFiles, zipFilePath, null);
    }

    /**
     * Zip the files.
     *
     * @param srcFilePaths The paths of source files.
     * @param zipFilePath  The path of ZIP file.
     * @param comment      The comment.
     * @return {@code true}: success<br>{@code false}: fail
     * @throws IOException if an I/O error has occurred
     */
    public static boolean zipFiles(final java.util.Collection<String> srcFilePaths,
                                   final String zipFilePath,
                                   final String comment)
            throws java.io.IOException {
        if (srcFilePaths == null || zipFilePath == null) return false;
        java.util.zip.ZipOutputStream zos = null;
        try {
            zos = new java.util.zip.ZipOutputStream(new java.io.FileOutputStream(zipFilePath));
            for (String srcFile : srcFilePaths) {
                if (!zipFile(getFileByPath(srcFile), "", zos, comment)) return false;
            }
            return true;
        } finally {
            if (zos != null) {
                zos.finish();
                zos.close();
            }
        }
    }

    /**
     * Zip the files.
     *
     * @param srcFiles The source of files.
     * @param zipFile  The ZIP file.
     * @return {@code true}: success<br>{@code false}: fail
     * @throws IOException if an I/O error has occurred
     */
    public static boolean zipFiles(final java.util.Collection<java.io.File> srcFiles, final java.io.File zipFile)
            throws java.io.IOException {
        return zipFiles(srcFiles, zipFile, null);
    }

    /**
     * Zip the files.
     *
     * @param srcFiles The source of files.
     * @param zipFile  The ZIP file.
     * @param comment  The comment.
     * @return {@code true}: success<br>{@code false}: fail
     * @throws IOException if an I/O error has occurred
     */
    public static boolean zipFiles(final java.util.Collection<java.io.File> srcFiles,
                                   final java.io.File zipFile,
                                   final String comment)
            throws java.io.IOException {
        if (srcFiles == null || zipFile == null) return false;
        java.util.zip.ZipOutputStream zos = null;
        try {
            zos = new java.util.zip.ZipOutputStream(new java.io.FileOutputStream(zipFile));
            for (java.io.File srcFile : srcFiles) {
                if (!zipFile(srcFile, "", zos, comment)) return false;
            }
            return true;
        } finally {
            if (zos != null) {
                zos.finish();
                zos.close();
            }
        }
    }

    /**
     * Zip the file.
     *
     * @param srcFilePath The path of source file.
     * @param zipFilePath The path of ZIP file.
     * @return {@code true}: success<br>{@code false}: fail
     * @throws IOException if an I/O error has occurred
     */
    public static boolean zipFile(final String srcFilePath,
                                  final String zipFilePath)
            throws java.io.IOException {
        return zipFile(getFileByPath(srcFilePath), getFileByPath(zipFilePath), null);
    }

    /**
     * Zip the file.
     *
     * @param srcFilePath The path of source file.
     * @param zipFilePath The path of ZIP file.
     * @param comment     The comment.
     * @return {@code true}: success<br>{@code false}: fail
     * @throws IOException if an I/O error has occurred
     */
    public static boolean zipFile(final String srcFilePath,
                                  final String zipFilePath,
                                  final String comment)
            throws java.io.IOException {
        return zipFile(getFileByPath(srcFilePath), getFileByPath(zipFilePath), comment);
    }

    /**
     * Zip the file.
     *
     * @param srcFile The source of file.
     * @param zipFile The ZIP file.
     * @return {@code true}: success<br>{@code false}: fail
     * @throws IOException if an I/O error has occurred
     */
    public static boolean zipFile(final java.io.File srcFile,
                                  final java.io.File zipFile)
            throws java.io.IOException {
        return zipFile(srcFile, zipFile, null);
    }

    /**
     * Zip the file.
     *
     * @param srcFile The source of file.
     * @param zipFile The ZIP file.
     * @param comment The comment.
     * @return {@code true}: success<br>{@code false}: fail
     * @throws IOException if an I/O error has occurred
     */
    public static boolean zipFile(final java.io.File srcFile,
                                  final java.io.File zipFile,
                                  final String comment)
            throws java.io.IOException {
        if (srcFile == null || zipFile == null) return false;
        java.util.zip.ZipOutputStream zos = null;
        try {
            zos = new java.util.zip.ZipOutputStream(new java.io.FileOutputStream(zipFile));
            return zipFile(srcFile, "", zos, comment);
        } finally {
            if (zos != null) {
                zos.close();
            }
        }
    }

    private static boolean zipFile(final java.io.File srcFile,
                                   String rootPath,
                                   final java.util.zip.ZipOutputStream zos,
                                   final String comment)
            throws java.io.IOException {
        rootPath = rootPath + (isSpace(rootPath) ? "" : java.io.File.separator) + srcFile.getName();
        if (srcFile.isDirectory()) {
            java.io.File[] fileList = srcFile.listFiles();
            if (fileList == null || fileList.length <= 0) {
                java.util.zip.ZipEntry entry = new java.util.zip.ZipEntry(rootPath + '/');
                entry.setComment(comment);
                zos.putNextEntry(entry);
                zos.closeEntry();
            } else {
                for (java.io.File file : fileList) {
                    if (!zipFile(file, rootPath, zos, comment)) return false;
                }
            }
        } else {
            java.io.InputStream is = null;
            try {
                is = new java.io.BufferedInputStream(new java.io.FileInputStream(srcFile));
                java.util.zip.ZipEntry entry = new java.util.zip.ZipEntry(rootPath);
                entry.setComment(comment);
                zos.putNextEntry(entry);
                byte buffer[] = new byte[BUFFER_LEN];
                int len;
                while ((len = is.read(buffer, 0, BUFFER_LEN)) != -1) {
                    zos.write(buffer, 0, len);
                }
                zos.closeEntry();
            } finally {
                if (is != null) {
                    is.close();
                }
            }
        }
        return true;
    }

    /**
     * Unzip the file.
     *
     * @param zipFilePath The path of ZIP file.
     * @param destDirPath The path of destination directory.
     * @return the unzipped files
     * @throws IOException if unzip unsuccessfully
     */
    public static java.util.List<java.io.File> unzipFile(final String zipFilePath,
                                                         final String destDirPath)
            throws java.io.IOException {
        return unzipFileByKeyword(zipFilePath, destDirPath, null);
    }

    /**
     * Unzip the file.
     *
     * @param zipFile The ZIP file.
     * @param destDir The destination directory.
     * @return the unzipped files
     * @throws IOException if unzip unsuccessfully
     */
    public static java.util.List<java.io.File> unzipFile(final java.io.File zipFile,
                                                         final java.io.File destDir)
            throws java.io.IOException {
        return unzipFileByKeyword(zipFile, destDir, null);
    }

    /**
     * Unzip the file by keyword.
     *
     * @param zipFilePath The path of ZIP file.
     * @param destDirPath The path of destination directory.
     * @param keyword     The keyboard.
     * @return the unzipped files
     * @throws IOException if unzip unsuccessfully
     */
    public static java.util.List<java.io.File> unzipFileByKeyword(final String zipFilePath,
                                                                  final String destDirPath,
                                                                  final String keyword)
            throws java.io.IOException {
        return unzipFileByKeyword(getFileByPath(zipFilePath), getFileByPath(destDirPath), keyword);
    }

    /**
     * Unzip the file by keyword.
     *
     * @param zipFile The ZIP file.
     * @param destDir The destination directory.
     * @param keyword The keyboard.
     * @return the unzipped files
     * @throws IOException if unzip unsuccessfully
     */
    public static java.util.List<java.io.File> unzipFileByKeyword(final java.io.File zipFile,
                                                                  final java.io.File destDir,
                                                                  final String keyword)
            throws java.io.IOException {
        if (zipFile == null || destDir == null) return null;
        java.util.List<java.io.File> files = new java.util.ArrayList<>();
        java.util.zip.ZipFile zip = new java.util.zip.ZipFile(zipFile);
        java.util.Enumeration<?> entries = zip.entries();
        try {
            if (isSpace(keyword)) {
                while (entries.hasMoreElements()) {
                    java.util.zip.ZipEntry entry = ((java.util.zip.ZipEntry) entries.nextElement());
                    String entryName = entry.getName().replace("\\", "/");
                    if (entryName.contains("../")) {
                        android.util.Log.e("ZipUtils", "entryName: " + entryName + " is dangerous!");
                        continue;
                    }
                    if (!unzipChildFile(destDir, files, zip, entry, entryName)) return files;
                }
            } else {
                while (entries.hasMoreElements()) {
                    java.util.zip.ZipEntry entry = ((java.util.zip.ZipEntry) entries.nextElement());
                    String entryName = entry.getName().replace("\\", "/");
                    if (entryName.contains("../")) {
                        android.util.Log.e("ZipUtils", "entryName: " + entryName + " is dangerous!");
                        continue;
                    }
                    if (entryName.contains(keyword)) {
                        if (!unzipChildFile(destDir, files, zip, entry, entryName)) return files;
                    }
                }
            }
        } finally {
            zip.close();
        }
        return files;
    }

    private static boolean unzipChildFile(final java.io.File destDir,
                                          final java.util.List<java.io.File> files,
                                          final java.util.zip.ZipFile zip,
                                          final java.util.zip.ZipEntry entry,
                                          final String name) throws java.io.IOException {
        java.io.File file = new java.io.File(destDir, name);
        files.add(file);
        if (entry.isDirectory()) {
            return createOrExistsDir(file);
        } else {
            if (!createOrExistsFile(file)) return false;
            java.io.InputStream in = null;
            java.io.OutputStream out = null;
            try {
                in = new java.io.BufferedInputStream(zip.getInputStream(entry));
                out = new java.io.BufferedOutputStream(new java.io.FileOutputStream(file));
                byte buffer[] = new byte[BUFFER_LEN];
                int len;
                while ((len = in.read(buffer)) != -1) {
                    out.write(buffer, 0, len);
                }
            } finally {
                if (in != null) {
                    in.close();
                }
                if (out != null) {
                    out.close();
                }
            }
        }
        return true;
    }

    /**
     * Return the files' path in ZIP file.
     *
     * @param zipFilePath The path of ZIP file.
     * @return the files' path in ZIP file
     * @throws IOException if an I/O error has occurred
     */
    public static java.util.List<String> getFilesPath(final String zipFilePath)
            throws java.io.IOException {
        return getFilesPath(getFileByPath(zipFilePath));
    }

    /**
     * Return the files' path in ZIP file.
     *
     * @param zipFile The ZIP file.
     * @return the files' path in ZIP file
     * @throws IOException if an I/O error has occurred
     */
    public static java.util.List<String> getFilesPath(final java.io.File zipFile)
            throws java.io.IOException {
        if (zipFile == null) return null;
        java.util.List<String> paths = new java.util.ArrayList<>();
        java.util.zip.ZipFile zip = new java.util.zip.ZipFile(zipFile);
        java.util.Enumeration<?> entries = zip.entries();
        while (entries.hasMoreElements()) {
            String entryName = ((java.util.zip.ZipEntry) entries.nextElement()).getName().replace("\\", "/");;
            if (entryName.contains("../")) {
                android.util.Log.e("ZipUtils", "entryName: " + entryName + " is dangerous!");
                paths.add(entryName);
            } else {
                paths.add(entryName);
            }
        }
        zip.close();
        return paths;
    }

    /**
     * Return the files' comment in ZIP file.
     *
     * @param zipFilePath The path of ZIP file.
     * @return the files' comment in ZIP file
     * @throws IOException if an I/O error has occurred
     */
    public static java.util.List<String> getComments(final String zipFilePath)
            throws java.io.IOException {
        return getComments(getFileByPath(zipFilePath));
    }

    /**
     * Return the files' comment in ZIP file.
     *
     * @param zipFile The ZIP file.
     * @return the files' comment in ZIP file
     * @throws IOException if an I/O error has occurred
     */
    public static java.util.List<String> getComments(final java.io.File zipFile)
            throws java.io.IOException {
        if (zipFile == null) return null;
        java.util.List<String> comments = new java.util.ArrayList<>();
        java.util.zip.ZipFile zip = new java.util.zip.ZipFile(zipFile);
        java.util.Enumeration<?> entries = zip.entries();
        while (entries.hasMoreElements()) {
            java.util.zip.ZipEntry entry = ((java.util.zip.ZipEntry) entries.nextElement());
            comments.add(entry.getComment());
        }
        zip.close();
        return comments;
    }

    private static boolean createOrExistsDir(final java.io.File file) {
        return file != null && (file.exists() ? file.isDirectory() : file.mkdirs());
    }

    private static boolean createOrExistsFile(final java.io.File file) {
        if (file == null) return false;
        if (file.exists()) return file.isFile();
        if (!createOrExistsDir(file.getParentFile())) return false;
        try {
            return file.createNewFile();
        } catch (java.io.IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    private static java.io.File getFileByPath(final String filePath) {
        return isSpace(filePath) ? null : new java.io.File(filePath);
    }

    private static boolean isSpace(final String s) {
        if (s == null) return true;
        for (int i = 0, len = s.length(); i < len; ++i) {
            if (!Character.isWhitespace(s.charAt(i))) {
                return false;
            }
        }
        return true;
    }
}
