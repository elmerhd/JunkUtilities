package com.junk.application;

import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.awt.Toolkit;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

/**
 * Collection of utility classes for various stuff.
 * @author Elmer Duron 
 * @version 0.1.5
 */
public interface JunkUtilities {
    public static void parameterRequireNonNull(Object parameter){
        if(parameter == null){
            throw new NonSenseException("parameter is null :");
        }
    }

    //<editor-fold defaultstate="collapsed" desc="NonSenseException.class">
        /**
         * An Exception that extends {@link java.lang.RuntimeException}
         * dont worry its nonsense
         */
        public static class NonSenseException extends RuntimeException{
            private static final long serialVersionUID = -3850176899732829689L;
            /**
             * Prints the exception message.
             * @param message the message exception.
             */
            public NonSenseException(String message) {
                super(message);
            }
            /**
             * Print
             * @param cause the Throwable cause
             */
            public NonSenseException(Throwable cause) {
                super(cause);
            }
            public NonSenseException(String message, Throwable cause) {
                super(message, cause);
            }
        }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="CommonUtilities.class">
        /**
         * A class for advance programming minds like us, LOL.
         */
        public static class CommonUtilities {
            /**
             * captures strings by parameter.
             *@param string the string
             *@param params the params
             * @return all numbers
             */
            public static String captureChars(String string,String params){
                String pat = "[^";
                for (int i = 0; i < params.length(); i++) {
                    pat+=""+params.charAt(i);
                }
                pat += "]+";
                return string.replaceAll(pat, "");
            }
            /**
             * converts string to 1d array splitting chars
             * 
             * @param string the string 
             * @param splitter the splitter
             * @return the new array
             */
            public String [] string2arr1d(String string,String splitter){
                String[] newArray = string.split(splitter);
                return newArray;
            }
            
            /**
             * converts 1d array to 2d array by column and rows filling all rows 
             * with null value if array is not sufficient to fill the row.
             * - equivalent to splice in javascript
             * @param array the array
             * @param col the numbers of col
             * @param row the numbers of row
             * @return the new array;
             */
            public String[][] arr1d2arr2d(String[]array,int col,int row){
                String [][] newArray = new String[row][col];
                int k = 0;
                for (int i = 0; i < row; i++) {
                    for (int j = 0; j < col; j++) {
                        try {
                            newArray[i][j]=array[k];
                            k++;
                        } catch (ArrayIndexOutOfBoundsException e) {
                            newArray[i][j]="null";
                            k++;
                        }

                    }
                }
                return newArray;
            }
            /**
             * the scan type return
             */
            public enum SCAN_RESULT {
                USED,CONTAINS,NOT_USED
            }
            /**
             * Search a string in an array.
             * @param array the array
             * @param key the element key
             * @return 
             */
            public static SCAN_RESULT hasElement(String []array,String key){
                parameterRequireNonNull(array);
                parameterRequireNonNull(key);
                SCAN_RESULT scan_result = SCAN_RESULT.NOT_USED;
                for (String element : array) {
                    if(key.trim().matches(element.trim())){
                        scan_result = SCAN_RESULT.USED;
                        break;
                    }else if(element.trim().contains(key.trim())){
                        scan_result = SCAN_RESULT.CONTAINS;
                        break;
                    }else{
                        scan_result =  SCAN_RESULT.NOT_USED;
                    }
                }
                return scan_result;
            }
            /**
             * Scan a string if it is a valid email.
             * @param email the email
             * @return true if valid else if not
             */
            public static boolean validateEmail(String email){
                return !((email.length() < 4) || !email.contains("@") || !email.contains("."));
            }
            /**
             * Make the file as an URL and <p>
             * opens a new InputStream then load the file by an InputStreamReader.<p>
             * Useful when reading files inside the jar file.
             * @param theCurrentClass a relative class of the file.
             * @param file the file.
             * @return BufferedReader.
             * @throws IOException if the file is not a relative to the class or file was not found.
             */
            public static BufferedReader getBufferedReaderInputStream(Class theCurrentClass,String file) throws IOException{
                parameterRequireNonNull(theCurrentClass);
                parameterRequireNonNull(file);
                URL url = theCurrentClass.getResource(file);
                InputStream is = url.openStream();
                InputStreamReader isr = new InputStreamReader(is, "UTF-8");
                BufferedReader reader = new BufferedReader(isr);
                return reader;
            }
            /**
             * A method for converting a {@link java.util.List} to Array String.
             * @param list the list.
             * @return the array string. 
             */
            public static String [] convertList2ArrayString(List list){
                parameterRequireNonNull(list);
                String[]newString = new String[list.size()];
                for (int i = 0; i < list.size(); i++) {
                    newString[i]= list.get(i).toString();
                }
                return newString;
            }
            /**
             * A method for converting a {@link java.util.List} to Array Integer.
             * @param list the list.
             * @return the array string. 
             */
            public static int [] convertList2ArrayInt(List list){
                parameterRequireNonNull(list);
                int[]newInt = new int[list.size()];
                for (int i = 0; i < list.size(); i++) {
                    newInt[i]= Integer.parseInt(list.get(i).toString());
                }
                return newInt;
            }

            /**
             * A method for converting a {@link java.util.List} to Array Integer.
             * @param list the list.
             * @return the array string. 
             */
            public static char [] convertList2ArrayCharacter(List list){
                parameterRequireNonNull(list);
                char[]newChar = new char[list.size()];
                for (int i = 0; i < list.size(); i++) {
                    newChar[i]= (char) list.get(i);
                }
                return newChar;
            }

            /**
             * Statically imports the given font. It can then be used directly in
             * calls to <code>new Font()</code>.
             * @param stream the input stream of font location.
             */
            public static void importFont(InputStream stream) {
                try {
                    parameterRequireNonNull(stream);
                    Font font = Font.createFont(Font.TRUETYPE_FONT, stream);
                    GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(font);
                } catch (FontFormatException | IOException ex) {
                    throw new NonSenseException(ex);
                }
            }



            /**
             * Opens the given resource path as an {@link java.io.InputStream}. Don't forget
             * to close it!
             * @param filePath the location of the file.
             * @return the input stream of the file.
             */
            public static InputStream getStream(String filePath) {
                parameterRequireNonNull(filePath);
                InputStream is = JunkUtilities.class.getResourceAsStream(filePath);
                return is;
            }

            /**
             * Gets {@link URL} from the given path.
             * @param path the location of the file.
             * @return the URL of the file.
             */
            public static URL getUrl(String path) {
                parameterRequireNonNull(path);
                URL url = JunkUtilities.class.getResource(path);
                return url;
            }
        }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="ThreadUtilities.class">

        /**
         * A Class for accessing the class {@link java.lang.Thread}<p>
         * Useful for playing {@link java.lang.Thread}
         */
        public static class ThreadUtilities {

            /**
             * plays the specific thread<p>
             * invoking <code>thread.start()</code>
             * @param thread the thread to start.
             */
            public static void play(Thread thread){
                parameterRequireNonNull(thread);
                thread.start();
            }

            /**
             * Pauses the current thread running.<p>
             * invoking the  <code>thread.suspend()</code>
             * @param thread the thread to pause.
             */
            public static void pause(Thread thread){
                parameterRequireNonNull(thread);
                thread.suspend();
            }

            /**
             * Resuming the current paused thread.<p>
             * invoking the <code>thread.resume()</code>
             * @param thread the thread to resume.
             */
            public static void resume(Thread thread){
                parameterRequireNonNull(thread);
                thread.resume();
            }

            /**
             * Stops the current running thread.<p>
             * invoking the <code>thread.stop()</code>
             * @param thread the thread to stop.
             */
            public static void stop(Thread thread){
                parameterRequireNonNull(thread);
                thread.stop();
            }
        }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="FileUtilities.class">
        /**
         * <p>
         * A class that enables to create, copy, read, delete a file and etc..
         */
        public static class FileUtilities {

            /**
             * Creates a relative file.
             * @param fileName the file name or location.
             * @throws java.io.IOException if the file is exist or unable<p>
             * to create a file.
             */
            public static void createFile(String fileName) throws IOException{
                parameterRequireNonNull(fileName);
                File myfile = new File(fileName);
                BufferedWriter writer = new BufferedWriter(new FileWriter(myfile,true));
                writer.close();
            }

            /**
             * Forces to create a file and folders.
             * @param fileLocation the absolute file location.
             * @throws java.io.IOException if the jvm cannot create an absolute path.
             */
            public static void createAbsolutePath(String fileLocation) throws IOException{
                parameterRequireNonNull(fileLocation);
                File myF = new File(fileLocation);
                myF.getParentFile().mkdirs();
                PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(myF,true)));
                writer.close();
            }

            /**
             * Writes a String in the specified file.
             * @param string the string to be written.
             * @param filePath the file to rewrite.
             * @throws IOException if the file is in use or not found.
             */
            public static void writeStringToFile(String string,String filePath)throws IOException {
                parameterRequireNonNull(string);
                parameterRequireNonNull(string);
                File newFile = new File(filePath);
                PrintWriter writer;
                writer = new PrintWriter(new BufferedWriter(new FileWriter(newFile,true)));
                writer.println(string);
                writer.close();
            }

            /**
             * Clears the file.
             * be sure your path is not an executable file, this will make the file useless.
             * @param filePath the file to be cleared.
             * @throws IOException if the file is in use or not found
             */
            public static void clearFile(String filePath)throws IOException{
                parameterRequireNonNull(filePath);
                File newFile = new File(filePath);
                BufferedWriter writer;
                writer = new BufferedWriter(new FileWriter(newFile));
                writer.write("");
                writer.close();
            }

            /**
             * Deletes the specified file.
             * @param file the file to delete.
             */
            public static void deleteFile(File file){
                parameterRequireNonNull(file);
                boolean deleted  = file.delete();
                if(!deleted){
                    file.deleteOnExit();
                }
            }

            /**
             * Reads the string inside the file.
             * @param file the file to read.
             * @return the string containing characters from  file.
             * @throws IOException if file is not found.
             */
            public static String readFile(String file) throws IOException{
                parameterRequireNonNull(file);
                String readed = "";
                FileInputStream inputStream = new FileInputStream(file);
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                    while (reader.ready()) {
                        readed += reader.readLine()+"\n";
                    }
                    return readed;
            }


            /**
             * The file copy buffer size limit
             */
            private static final long FILE_COPY_BUFFER_SIZE = Long.MAX_VALUE;
            /**
             * Copy a file.
             * @param srcFile the source file to copy
             * @param destFile the destination path of the new copied file
             * @param preserveFileDate if date will be preserve
             * @throws IOException if file destination exist or file source not found
             */
            public static void copyFile(File srcFile,File destFile,boolean preserveFileDate) throws  IOException{
                parameterRequireNonNull(srcFile);
                parameterRequireNonNull(destFile);
                parameterRequireNonNull(preserveFileDate);

                FileInputStream fis = null;
                FileOutputStream fos = null;
                FileChannel input = null;
                FileChannel output = null;
                try {
                    fis = new FileInputStream(srcFile);
                    fos = new FileOutputStream(destFile);
                    input  = fis.getChannel();
                    output = fos.getChannel();
                    long size = input.size();
                    long pos = 0;
                    long count = 0;
                    while (pos < size) {
                        count = (size - pos) > FILE_COPY_BUFFER_SIZE ? FILE_COPY_BUFFER_SIZE : (size - pos);
                        pos += output.transferFrom(input, pos, count);
                    }
                } finally {
                    close(output);
                    close(fos);
                    close(input);
                    close(fis);
                }

                if (srcFile.length() != destFile.length()) {
                    throw new NonSenseException("Failed to copy full contents from '" +
                            srcFile + "' to '" + destFile + "'");
                }
                if (preserveFileDate) {
                    destFile.setLastModified(srcFile.lastModified());
                }
            }
            /**
             * Close all the closeable input/output.
             * @param closeable 
             */
            private static void close(Closeable closeable){
                try {
                    if (closeable != null) {
                        closeable.close();
                    }
                } catch (IOException ioe) {}
            }
            /**
             * Moves the specific file.<p>
             * performing <code>copyFile()</code> then <code>deleteFile()</code>
             * @param src the source file
             * @param dest the destination file
             */
            public static void moveFile(File src,File dest){
                parameterRequireNonNull(src);
                parameterRequireNonNull(dest);
                try{
                    copyFile(src, dest, true);
                }catch(IOException ioe){
                    throw new NonSenseException(ioe);
                }finally{
                    deleteFile(src);
                }
            }
            /**
             * Prints the specified file
             * @param file the file to print
             * @throws IOException if the file is not found or file is not supported by the platform.
             */
            public static void printFile(File file) throws IOException{
                Desktop d = Desktop.getDesktop();
                d.print(file);
            }
            /**
             * Copy the folder specified and its sub folders
             * @param src the source folder
             * @param dest the destination folder
             * @throws IOException if the file in the folder is in used and the file source is not found
             */
            public static void copyFolder(File src, File dest)throws IOException{

            if(src.isDirectory()){
                if(!dest.exists()){
                   dest.mkdir();
                }
                String files[] = src.list();

                for (String file : files) {

                   File srcFile = new File(src, file);
                   File destFile = new File(dest, file);
                   copyFolder(srcFile,destFile);
                }

            }else{
                InputStream in = new FileInputStream(src);
                OutputStream out = new FileOutputStream(dest); 

                byte[] buffer = new byte[1024];

                int length;
                while ((length = in.read(buffer)) > 0){
                   out.write(buffer, 0, length);
                }
                close(in);
                close(out);
            }
        }
        /**
         * Hide the specified file
         * @param file the file to hide
         * @throws IOException if the file is not found
         */
        public static void hideFile(File file) throws IOException{
            parameterRequireNonNull(file);
            if(!file.isHidden()){
                Runtime.getRuntime().exec("attrib +H "+file.getAbsolutePath());
            }
        }
        /**
         * un hide the specified file
         * @param file the file to un hide
         * @throws IOException if the file is not found
         */
        public static void unhideFile(File file) throws IOException{
            parameterRequireNonNull(file);
            if(file.isHidden()){
                Runtime.getRuntime().exec("attrib -H "+file.getAbsolutePath());
            }
        }
        /**
         * Set the file to read only
         * @param file the target file
         * @throws IOException if the file is not found
         */
        public static void setReadOnlyFile(File file) throws IOException{
            parameterRequireNonNull(file);
            if(file.canWrite()){
                Runtime.getRuntime().exec("attrib -R "+file.getAbsolutePath());
            }
        }
        /**
         * sets the file to re writable 
         * @param file the target file 
         * @throws IOException  if the file is not found
         */
        public static void setWritableFile(File file) throws IOException{
            parameterRequireNonNull(file);
            if(!file.canWrite()){
                Runtime.getRuntime().exec("attrib R "+file.getAbsolutePath());
            }
        }
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="SystemUtils.class">

            /**
             *A class for Accessing native action of windows.
             */
            public static class SystemUtilities {

            private static final Runtime runtime = Runtime.getRuntime();
            /**
             * Run or execute the specified program that ends with .exe.
             * @param filelocation
             * @throws IOException 
             */
            public static void runEXEFile(String filelocation) throws IOException{
                parameterRequireNonNull(filelocation);
                if(!filelocation.endsWith(".exe")) {
                    throw new NonSenseException(filelocation+" is not an executable file!");
                } else {
                    runtime.exec(filelocation);
                }
            }

            /**
             * Gets the Screen size of the System.
             * @return 
             */
            public static Dimension getSystemScreenDimension(){
                Toolkit toolkit = Toolkit.getDefaultToolkit();
                return toolkit.getScreenSize();
            }
            /**
             * Restart system.
             * @throws IOException if unable to restart
             */
            public static void RestartComputer() throws IOException{
                runtime.exec("shutdown -r");
            }

            /**
             * Restart after specific time.
             * @param seconds the time in second to restart
             * @throws IOException if unable to restart
             */
            public static void RestartComputer(int seconds) throws IOException{
                parameterRequireNonNull(seconds);
                runtime.exec("shutdown -r -t "+seconds);
            }

            /**
             * Shutdown after specific time.
             * @param seconds the time in second to shutdown
             * @throws IOException if unable to shutdown
             */
            public static void ShutdownComputer(int seconds) throws IOException{
                parameterRequireNonNull(seconds);
                runtime.exec("shutdown -s -t "+seconds);
            }

            /**
             * Shutdown system.
             * @throws IOException if unable to shutdown
             */
            public static void ShutdownComputer() throws IOException{
                runtime.exec("shutdown -r");
            }
            /**
             * Gets the name of the user.
             * @return the user
             */
            public static String getSystemUserName(){
                return System.getProperty("user.name");
            }
            /**
             * Gets the language of the system.
             * @return the language
             */
            public static String getSystemLanguage(){
                return System.getProperty("user.language");
            }
            /**
             * Gets the User home location.
             * @return the user home
             */
            public static String getSystemUserHome(){
                return System.getProperty("user.home");
            }
            /**
             * Gets the OS of the system
             * @return the os name
             */
            public static String getSystemOSName(){
                return System.getProperty("os.name");
            }
            /**
             * Gets the OS version of the system
             * @return the os version
             */
            public static String getSystemOSVersion(){
                return System.getProperty("os.version");
            }
            /**
             * Gets the bit version of the Operating System
             * @return the bit version.
             */
            public static String getSystemOSBit(){
                return System.getProperty("sun.arch.data.model");
            }
            /**
             * Gets the OS architecture of the system
             * @return the os bit
             */
            public static String getSystemOSArchitecture(){
                return System.getProperty("os.arch");
            }
            /**
             * Gets the Class path of this java application
             * @return the project class path
             */
            public static String getSystemClassPath(){
                return System.getProperty("java.class.path");
            }
            /**
             * Gets the User Directory location
             * @return the directory of the currently running program
             */
            public static String getSystemUserDirectory(){
                return System.getProperty("user.dir");
            }
            /**
             * Gets the User Temporary file Directory location
             * @return the temporary file directory
             */
            public static String getSystemUserTempDirectory(){
                return System.getProperty("java.io.tmpdir");
            }
            /**
             * Gets all the Application class libraries.
             * @return the libraries
             * @throws IOException if the files are in used. <p>
             * or unable to access the file
             */
            public static String[] getApplicationClassLibraries() throws IOException{
                List<String>list = new ArrayList<>();
                Files.walk(Paths.get("lib/")).forEach(files->{
                    String file = files.toFile().getAbsolutePath();
                    if (file.endsWith(".jar")) {
                        list.add(files.toFile().getName());
                    }
                });
                if(list.isEmpty()){
                    list.add("no java archive libraries.");
                }
                return CommonUtilities.convertList2ArrayString(list);
            }
        }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="MathOps.class">
        /**
         * Solves a Logical operators like AND and OR operator, and etc..
         */
        public static class MathOps {
            /**
             * Solves the two arguments in Implication operation.
             * @param arg1 the argument
             * @param arg2 the argument
             * @return the answer
             */
            public static boolean solveLogicalImplication(boolean arg1,boolean arg2){
                if(arg1 == true && arg2 == true){
                    return true;
                }else if(arg1 == true && arg2 == false){
                    return false;
                }else if(arg1 == false && arg2 == true){
                    return true;
                }else if(arg1 == false && arg2 == false){
                    return true;
                }
                return true;
            }

            /**
             * Solves the two arguments in Double Implication operation.
             * @param arg1 the argument
             * @param arg2 the argument
             * @return the answer
             */
            public static boolean solveLogicalDoubleImplication(boolean arg1,boolean arg2){
                if(arg1 == true && arg2 == true){
                    return true;
                }else if(arg1 == true && arg2 == false){
                    return false;
                }else if(arg1 == false && arg2 == true){
                    return false;
                }else if(arg1 == false && arg2 == false){
                    return true;
                }
                return true;
            }

            /**
             * Solve the two arguments in X-OR operation.
             * @param arg1 the argument
             * @param arg2 the argument
             * @return the answer
             */
            public static boolean solveLogicalXOR(boolean arg1,boolean arg2){
                return arg1 ^ arg2;
            }

            /**
             * Solve the two arguments in OR operation.
             * @param arg1 the argument
             * @param arg2 the argument
             * @return the answer
             */
            public static boolean solveLogicalOR(boolean arg1,boolean arg2){
                return arg1 || arg2;
            }

            /**
             * Solve the two arguments in AND operation.
             * @param arg1 the argument
             * @param arg2 the argument
             * @return the answer
             */
            public static boolean solveLogicalAND(boolean arg1,boolean arg2){
                return arg1 && arg2;
            }
            /**
             * Solve the argument in negation operation.
             * @param arg1 the argument
             * @return the answer.
             */
            public static boolean solveNegationOf(boolean arg1){
                return !arg1; 
            }

            /**
             * Validate a String if it is a binary number system.
             * @param binary the binary string
             * @return true if valid else throw an exception.
             */
            public static boolean scanAsBinary(String binary){
                Objects.requireNonNull(binary);
                for (int i = 0; i < binary.length(); i++) {
                    if(solveLogicalOR(binary.charAt(i) == '0', binary.charAt(i) == '1')){
                    }else{
                        throw new NonSenseException(binary+" is not a valid binaty number.");
                    }
                }
                return true;
            }
            /**
             * Validate a String if it is an octal number system.
             * @param octal the binary string
             * @return true if valid else throw an exception.
             */
            public static boolean scanAsOctal(String octal){
                Objects.requireNonNull(octal);
                for (int i = 0; i < octal.length(); i++) {
                    char charIndex = octal.charAt(i);
                    if(!(charIndex == '0' || charIndex == '1' || charIndex == '2' || charIndex == '3'
                            || charIndex == '4' || charIndex == '5' || charIndex == '6' || charIndex =='7')){
                        throw new NonSenseException(octal+" is not a valid octal number.");
                    }
                }
                return true;
            }
            /**
             * Validate a String if it is a decimal number system.
             * @param decimal the binary string
             * @return true if valid else throw an exception.
             */
            public static boolean scanAsDecimal(String decimal){
                Objects.requireNonNull(decimal);
                for (int i = 0; i < decimal.length(); i++) {
                    char charIndex = decimal.charAt(i);
                    if(!(charIndex == '0' || charIndex == '1' || charIndex == '2' || charIndex == '3'
                            || charIndex == '4' || charIndex == '5' || charIndex == '6' || charIndex =='7'
                            || charIndex == '8' || charIndex == '9')){
                        throw new NonSenseException(decimal+" is not a valid decimal number.");
                    }
                }
                return true;
            }
            /**
             * Firstly scan the strings if it is a binary.<p>
             * if the string is not a binary number throw new exception.<p>
             * if binary digits add the two binary digits
             * @param bin1 the first binary digit
             * @param bin2 the second binary digit
             * @return the answer
             */
            public static String getSumOfBinaryNumbers(String bin1,String bin2){
                Objects.requireNonNull(bin1);
                Objects.requireNonNull(bin2);
                scanAsBinary(bin1);
                scanAsBinary(bin2);
                int number0 = Integer.parseInt(bin1, 2);
                int number1 = Integer.parseInt(bin2, 2);
                int sum = number0 + number1;
                return Integer.toBinaryString(sum);
            }
            /**
             * Firstly scan the strings if it is a binary.<p>
             * if the string is not a binary number throw new exception.<p>
             * if binary digits, subtract the two binary digits
             * @param bin1 the first binary digit
             * @param bin2 the second binary digit
             * @return the answer
             */
            public static String getDifferenceOfBinaryNumbers(String bin1,String bin2){
                Objects.requireNonNull(bin1);
                Objects.requireNonNull(bin2);
                scanAsBinary(bin1);
                scanAsBinary(bin2);
                int number0 = Integer.parseInt(bin1, 2);
                int number1 = Integer.parseInt(bin2, 2);
                int dif = number0 - number1;
                return Integer.toBinaryString(dif);
            }
            /**
             * Firstly scan the strings if it is a binary.<p>
             * if the string is not a binary number throw new exception.<p>
             * if binary digits, multiply the two binary digits
             * @param bin1 the first binary digit
             * @param bin2 the second binary digit
             * @return the answer
             */
            public static String getProductOfBinaryNumbers(String bin1,String bin2){
                Objects.requireNonNull(bin1);
                Objects.requireNonNull(bin2);
                scanAsBinary(bin1);
                scanAsBinary(bin2);
                int number0 = Integer.parseInt(bin1, 2);
                int number1 = Integer.parseInt(bin2, 2);
                int pro = number0 * number1;
                return Integer.toBinaryString(pro);
            }
            /**
             * Converts binary to decimal number system.
             * @param bin the binary number
             * @return the decimal number
             */
            public static String convertBinaryToDecimalNumberSystem(String bin){
                Objects.requireNonNull(bin);
                scanAsBinary(bin);
                int digit = Integer.parseInt(bin, 2);
                return Integer.toString(digit);
            }
            /**
             * Converts binary to octal number system.
             * @param bin the binary number
             * @return the octal number
             */
            public static String convertBinaryToOctalNumberSystem(String bin){
                Objects.requireNonNull(bin);
                scanAsBinary(bin);
                int digit = Integer.parseInt(bin, 2);
                return Integer.toOctalString(digit);
            }
            /**
             * Converts binary to hexadecimal number system.
             * @param bin the binary.
             * @return the hexadecimal.
             */
            public static String convertBinaryToHexadecimalNumberSystem(String bin){
                Objects.requireNonNull(bin);
                scanAsBinary(bin);
                int digit = Integer.parseInt(bin, 2);
                return Integer.toHexString(digit);
            }
            /**
             * Converts hexadecimal to binary number system.
             * @param hex the hexadecimal number.
             * @return the binary number.
             */
            public static String convertHexadecimalToBinaryNumberSystem(String hex){
                Objects.requireNonNull(hex);
                String binAddr = Integer.toBinaryString(Integer.parseInt(hex, 16)); 
                return binAddr;
            }
            /**
             * Converts hexadecimal to decimal number system.
             * @param hex the hexadecimal number.
             * @return the decimal number.
             */
            public static String convertHexadecimalToDecimalNumberSystem(String hex){
                Objects.requireNonNull(hex);
                String binAddr = Integer.toBinaryString(Integer.parseInt(hex, 16)); 
                String dec = convertBinaryToDecimalNumberSystem(binAddr);
                return dec;
            }
            /**
             * Converts hexadecimal to octal number system.
             * @param hex the hexadecimal number.
             * @return the octal number.
             */
            public static String convertHexadecimalToOctalNumberSystem(String hex){
                Objects.requireNonNull(hex);
                String binAddr = Integer.toBinaryString(Integer.parseInt(hex, 16));
                String octal = convertBinaryToOctalNumberSystem(binAddr);
                return octal;
            }
            /**
             * Converts decimal to binary number system.
             * @param dec the decimal number.
             * @return the binary number.
             */
            public static String convertDecimalToBinaryNumberSystem(String dec){
                return Integer.toBinaryString(Integer.parseInt(dec));
            }
            /**
             * Converts decimal to octal number system.
             * @param dec the decimal number.
             * @return the octal number.
             */
            public static String convertDecimalToOctalNumberSystem(String dec){
                return Integer.toOctalString(Integer.parseInt(dec));
            }
            /**
             * Converts decimal to hexadecimal number system.
             * @param dec the decimal number.
             * @return the hexadecimal number.
             */
            public static String convertDecimalToHexadecimalNumberSystem(String dec){
                return Integer.toHexString(Integer.parseInt(dec));
            }
            /**
             * Converts octal to decimal number system.
             * @param octal the octal number.
             * @return the decimal number.
             */
            public static String convertOctalToDecimalNumberSystem(String octal){
                Objects.requireNonNull(octal);
                scanAsOctal(octal);
                int outputDecimal = Integer.parseInt(octal, 8);
                return Integer.toString(outputDecimal);
            }
            /**
             * Converts octal to binary number system.
             * @param octal the octal number.
             * @return the binary number.
             */
            public static String convertOctalToBinaryNumberSystem(String octal){
                Objects.requireNonNull(octal);
                scanAsOctal(octal);
                String dec = convertOctalToDecimalNumberSystem(octal);
                return convertDecimalToBinaryNumberSystem(dec);
            }
            /**
             * Converts octal to hexadecimal number system.
             * @param octal the octal number.
             * @return the hexadecimal number.
             */
            public static String convertOctalToHexadecimalNumberSystem(String octal){
                Objects.requireNonNull(octal);
                scanAsOctal(octal);
                String dec = convertOctalToDecimalNumberSystem(octal);
                return convertDecimalToHexadecimalNumberSystem(dec);
            }
            /**
             * scan if the number is negative
             * @param i the number
             * @return true if negative else false
             */
            public static boolean isNegative(int i){
                return i < 0;
            }
            /**
             * scan if the number is positive
             * @param i the number
             * @return true if positive else false
             */
            public static boolean isPositive(int i){
                return i > 0;
            }
            /**
             * Scan if the number is zero
             * @param i the number
             * @return true if zero else false
             */
            public static boolean isZero(int i){
                return  i == 0;
            }
            /**
             * scan for the lowest number.
             * statically stores the first number as lowest number
             * if the next number is lower than the lowest number,
             * the new number will be the lowest number
             * @param arrayInt the array
             * @return the lowest
             */
            public static int getTheLowest(int[]arrayInt){
                int theLowest = arrayInt[0];
                for (int i = 0; i < arrayInt.length; i++) {
                    if(arrayInt[i] < theLowest){
                        theLowest = arrayInt[i];
                    }
                }
                return theLowest;
            }

            /**
             * Scans for the highest number.
             * statically stores the first number as highest number 
             * if the next number is higher than the higher number,
             * the new number will be the highest number
             * @param arrayInt the array
             * @return the lowest
             */
            public static int getTheHighest(int[]arrayInt){
                int theHighest = arrayInt[0];
                for (int i = 0; i < arrayInt.length; i++) {
                    if(arrayInt[i] > theHighest){
                        theHighest = arrayInt[i];
                    }
                }
                return theHighest;
            }
        }

    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="ZipUtils.class">
    /**
     * The zip utils contain the Zip Archiver Class and
     */
    public static class ZipUtils {
        /**
         * This class is for Archiving Files to ZIP
         */
        public static class Archiver {
            
            List<String> fileList;
            private String OUTPUT_ZIP_FILE = null;
            private String SOURCE_FOLDER = null;
            /**
             * The class responsible for achiving files.
             * @param src the souce folder
             * @param dst the destination path
             */
            Archiver(String src,String dst){
                fileList = new ArrayList<>();
                SOURCE_FOLDER = src;
                OUTPUT_ZIP_FILE = dst;
                generateFileList(new File(src));
            }
            
            /**
             * Zips the file/s
             * @throws FileNotFoundException
             * @throws IOException
             */
            public void archive() throws FileNotFoundException, IOException{
                byte[] buffer = new byte[1024];
                FileOutputStream fos = new FileOutputStream(OUTPUT_ZIP_FILE);
                ZipOutputStream zos = new ZipOutputStream(fos);
                for(String file : this.fileList){
                    ZipEntry ze= new ZipEntry(file);
                    zos.putNextEntry(ze);
                    
                    FileInputStream in = new FileInputStream(SOURCE_FOLDER + File.separator + file);
                    int len;
                    while ((len = in.read(buffer)) > 0) {
                        zos.write(buffer, 0, len);
                    }
                    in.close();
                }
                
                zos.closeEntry();
                zos.close();
            }
            
            /**
             * Traverse a directory and get all files,
             * and add the file into fileList
             * @param node file or directory
             */
            private void generateFileList(File node){
                
                //add file only
                if(node.isFile()){
                    fileList.add(generateZipEntry(node.getAbsoluteFile().toString()));
                }
                
                if(node.isDirectory()){
                    String[] subNote = node.list();
                    for(String filename : subNote){
                        generateFileList(new File(node, filename));
                    }
                }
                
            }
            
            /**
             * Format the file path for zip
             * @param file file path
             * @return Formatted file path
             */
            private String generateZipEntry(String file){
                return file.substring(SOURCE_FOLDER.length(), file.length());
            }
        }
        /**
         * This class is for Extracting Zip Files.
         */
        public static class Extractor {
            
            private ZipFile zFile;
            private File dFile;
            /**
             * The class responsible for extracting zip archives.
             * @param arch the archive
             * @param dest the destination folder
             * @throws IOException 
             */
            public Extractor(String arch,String dest) throws IOException {
                File zpFile = new File(arch);
                zFile = new ZipFile(zpFile);
                dFile = new File(dest);
            }
            /**
             * Unzip archive file such as .zip / jars.
             * @throws java.io.IOException
             */
            public void extract() throws IOException{
                Enumeration entries = zFile.entries();
                do {
                    if(!entries.hasMoreElements())
                        break;
                    ZipEntry entry = (ZipEntry)entries.nextElement();
                    File extractToFile;
                    if(entry.isDirectory()){
                        extractToFile = new File(dFile, entry.getName());
                        extractToFile.mkdirs();
                    } else{
                        extractToFile = new File(dFile, entry.getName());
                        extractToFile.getParentFile().mkdirs();
                        extractToFile.createNewFile();
                        copyInputStream(zFile.getInputStream(entry), new BufferedOutputStream(new FileOutputStream(extractToFile)));
                    }
                } while(true);
                zFile.close();
            }
            /**
             * Copy Input stream and write.
             * @param in the input stream
             * @param out the output stream
             * @throws IOException
             */
            private static void copyInputStream(InputStream in, OutputStream out) throws IOException{
                byte buffer[] = new byte[1024];
                int len;
                while((len = in.read(buffer)) >= 0) {
                    out.write(buffer, 0, len);
                }
                in.close();
                out.close();
            }
        }
    }
//</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="XMLUtils.class">
    public static class XMLUtils {
        /**
         * The Class for Writing XML files
         */
        public static class Writer {
            private DocumentBuilderFactory docFactory;
            private DocumentBuilder docBuilder;
            private Document doc;
            private Element root;
            private List<Element> elements = new ArrayList<>();
            /*
            *
            */
            public Writer() throws ParserConfigurationException {
                docFactory = DocumentBuilderFactory.newInstance();
		docBuilder = docFactory.newDocumentBuilder();
                doc = docBuilder.newDocument();
            }
            public void setRoot(String rootElement){
                root = doc.createElement(rootElement);
		doc.appendChild(root);
            }
            public Element getRoot(){
                return root;
            }
            public Attr createAttribute(String attr,String value){
                Attr attrib = doc.createAttribute(attr);
                attrib.setValue(value);
                return attrib;
            }
            
            public Document getDocument(){
                return doc;
            }
            public void addElementWithAttributes(Element parent,String tagName,Attr[] attrib){
                Element newElement = doc.createElement(tagName);
		parent.appendChild(newElement);
                if(attrib != null){
                    for (Attr attrib1 : attrib) {
                        newElement.setAttribute(attrib1.getName(), attrib1.getValue());
                    }
                }
		elements.add(newElement);
            }
            public void addElementWithAttributesAndValue(Element parent,String tagName,String value,Attr[] attrib){
                parameterRequireNonNull(value);
                Element newElement = doc.createElement(tagName);
		parent.appendChild(newElement);
                if(attrib != null){
                    for (Attr attrib1 : attrib) {
                        newElement.setAttribute(attrib1.getName(), attrib1.getValue());
                    }
                }
                newElement.appendChild(doc.createTextNode(value));
		elements.add(newElement);
            }
            public void addElementWithValue(Element parent,String tagName,String value){
                Element newElement = doc.createElement(tagName);
		parent.appendChild(newElement);
                
                if(value != null){
                    newElement.appendChild(doc.createTextNode(value));
                }
		elements.add(newElement);
            }
            public Element getElementAt(int id){
                return elements.get(id);
            }
            public Element getElement(String tagName){
                Element e = null;
                for (Element element : elements) {
                    String eString = element.getTagName();
                    if(eString.equals(tagName)){
                        e = element;
                    }
                }
                return e;
            }
            public void save(String file) throws TransformerException{
                TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		DOMSource source = new DOMSource(doc);
		StreamResult result = new StreamResult(new File(file));
		transformer.transform(source, result);
            }
            
            
            
        }
        public static abstract class Reader {
            private final URL xmlUrl;
            private final InputStream is;
            private final Document doc;
            
            public Reader(String file) throws MalformedURLException, IOException, ParserConfigurationException, SAXException {
                xmlUrl = new URL(checkAndGetURL(file));
                System.out.println(xmlUrl);
                is = xmlUrl.openStream();
                doc = parse(is);
                doc.getDocumentElement().normalize();
            }
            /**
             * check the file if it is a local file or a file from web and gets its URL
             * @param file the file
             * @return the URL of the file
             */
            private String checkAndGetURL(String file){
                if(isHTTP(file)){
                    return file;
                }else{
                    return filePathAsURL(file);
                }
            }
            /**
             * check if the file is a http file/ file from the web
             * @param file the file
             * @return true if http
             */
            private boolean isHTTP(String file){
                return  file.startsWith("http://") || file.startsWith("https://");
            }
            /**
             * gets the document
             * @return the xml document
             */
            public Document getDocument(){
                return doc;
            }
            /**
             * changing the file path as file:///... if the file is a local file
             * @param xfile the xml file
             * @return the true file path
             */
            private String filePathAsURL(String xfile){
                String filePrefix = "file:///";
                String newPrex = xfile.replace("\\", "/").replaceAll(" ", "%20");
                return filePrefix+newPrex;
            }
            /**
             * Reads the file
             * note: you can override this method
             */
            public abstract void read();
            /**
             * Parse the File input stream to a document
             * @param is the input stream
             * @return the document
             * @throws SAXException
             * @throws ParserConfigurationException
             * @throws IOException
             */
            private Document parse(InputStream is) throws SAXException, ParserConfigurationException, IOException {
                Document parsedDoc = null;
                DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
                domFactory.setValidating(false);
                domFactory.setNamespaceAware(false);
                DocumentBuilder builder = domFactory.newDocumentBuilder();
                parsedDoc = builder.parse(is);
                return parsedDoc;
            }
        }
    }
//</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="RandomChar.class">
    /**
     * A class for generation random character sequence.
     * <br>to generate a random string 
     * RandomChar rc = new RandomChar(20, RandomChar.Type.DIGITS_LETTERS_UPPERLOWERCASE);
     * System.out.println(rc.generate());
     * so ez right?
     */
    public static class RandomChar {
        /**
         * enum type of choices
         */
        public enum Type {DIGITS_ONLY,LETTERS_LOWERCASE,LETTERS_UPPERCASE
        ,LETTERS_UPPERLOWERCASE,DIGITS_LETTERS_LOWERCASE,DIGITS_LETTERS_UPPERCASE,DIGITS_LETTERS_UPPERLOWERCASE}
        
        private int size = 0;
        private Type type = null;
        /**
         * 
         * @param size the size of the string sequence.
         * @param type the type of the generation e.g digits only.
         */
        public RandomChar(int size,Type type) {
            this.size = size;
            this.type = type;
        }
        /**
         * returns a generated random strings based on <br> size and type.
         * @return the generated random sequence
         */
        public String generate(){
            String randString = "";
            switch(type){
                case DIGITS_ONLY:
                    for (int i = 0; i < size; i++) {
                        int d = generateDigitsOnly();
                        randString+=d;
                    }
                    break;
                case LETTERS_LOWERCASE:
                    for (int i = 0; i < size; i++) {
                        char d = generateLettersLowerCase();
                        randString+=d;
                    }
                    break;
                case LETTERS_UPPERCASE:
                    for (int i = 0; i < size; i++) {
                        char d = generateLettersUpperCase();
                        randString+=d;
                    }
                    break;
                case LETTERS_UPPERLOWERCASE:
                    for (int i = 0; i < size; i++) {
                        char d = generateLetterUpperLowerCase();
                        randString+=d;
                    }
                    break;
                case DIGITS_LETTERS_LOWERCASE:
                    for (int i = 0; i < size; i++) {
                        String d = generateDigitLetterLowerCase();
                        randString+=d;
                    }
                    break;
                case DIGITS_LETTERS_UPPERCASE:
                    for (int i = 0; i < size; i++) {
                        String d = generateDigitLetterUpperCase();
                        randString+=d;
                    }
                    break;
                case DIGITS_LETTERS_UPPERLOWERCASE:
                    for (int i = 0; i < size; i++) {
                        String d = generateDigitLetterUpperLowerCase();
                        randString+=d;
                    }
                    break;
            }
            
            return randString;
        }
        /**
         * Gets a random number from 0 - 9.
         * @return the random int
         */
        private int generateDigitsOnly(){
            int [] number_0_9 = {0,1,2,3,4,5,6,7,8,9};
            int rnd = new Random().nextInt(number_0_9.length);
            return number_0_9[rnd];
        }
        /**
         * Gets a random lowercase char from a - z.
         * @return the random char
         */
        private char generateLettersLowerCase(){
            char[] alphabet_lowercase = {'a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z'};
            int rnd = new Random().nextInt(alphabet_lowercase.length);
            return alphabet_lowercase[rnd];
        }
        /**
         * Gets a random uppercase char from A - Z.
         * @return the random char
         */
        private char generateLettersUpperCase(){
            char[] alphabet_uppercase = {'A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z'};
            int rnd = new Random().nextInt(alphabet_uppercase.length);
            return alphabet_uppercase[rnd];
        }
        /**
         * Gets a random uppercase or lowercase char <br> the code will decide to pick a type<br> if the return value will uppercase or lowercase char .
         * @return the random char
         */
        private char generateLetterUpperLowerCase(){
            int [] r2 = {1,2};
            int rnd = new Random().nextInt(r2.length);
            int res = r2[rnd];
            if(res == 1){
                return generateLettersLowerCase();
            }else{
                return generateLettersUpperCase();
            }
        }
        /**
         * Gets a random digit or uppercase char  <br> the code will decide to pick a type<br> if the return value will digit or uppercase char .
         * @return the random char
         */
        private String generateDigitLetterUpperCase(){
            int [] r2 = {1,2};
            int rnd = new Random().nextInt(r2.length);
            int res = r2[rnd];
            if(res == 1){
                return ""+generateLettersUpperCase();
            }else{
                return ""+generateDigitsOnly();
            }
        }
        /**
         * Gets a random digit or lowercase char  <br> the code will decide to pick a type<br> if the return value will digit or lowercase char .
         * @return the random char
         */
        private String generateDigitLetterLowerCase(){
            int [] r2 = {1,2};
            int rnd = new Random().nextInt(r2.length);
            int res = r2[rnd];
            if(res == 1){
                return ""+generateLettersLowerCase();
            }else{
                return ""+generateDigitsOnly();
            }
        }
        /**
         * Gets a random digit or  uppercase or lowercase  char <br> the code will decide to pick a type<br> if the return value will digit or uppercase or lowercase char .
         * @return the random char
         */
        private String generateDigitLetterUpperLowerCase(){
            int [] r2 = {1,2,3};
            int rnd = new Random().nextInt(r2.length);
            int res = r2[rnd];
            switch (res) {
                case 1:
                    return ""+generateLettersLowerCase();
                case 2:
                    return ""+generateLettersUpperCase();
                case 3:
                    return ""+generateDigitsOnly();
                default:
                    assert false;
                    return "";
            }
        }
        /**
         * Gets the size.
         * @return the size
         */
        public int getSize() {
            return size;
        }
        /**
         * Gets the type.
         * @return the type
         */
        public Type getType() {
            return type;
        }
        /**
         * Sets the size.
         * @param size the size of the string 
         */
        public void setSize(int size) {
            this.size = size;
        }
        /**
         * Sets the type.
         * @param type the type of the sequence
         */
        public void setType(Type type) {
            this.type = type;
        }
    }
//</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="AESJunkCryption.class">
    /**
     * An implementation of aes iv in java
     *
     */
    public static class AESJunkCryption {
        /**
         * encrypt a string
         * @param str
         * @return
         */
        public static String encrypt(String str) {
            try {
                IvParameterSpec iv = new IvParameterSpec("RandomInitVector".getBytes("UTF-8"));
                SecretKeySpec skeySpec = new SecretKeySpec("Bar12345Bar12345".getBytes("UTF-8"), "AES");
                
                Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
                cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
                
                byte[] encrypted = cipher.doFinal(str.getBytes());
                return DatatypeConverter.printBase64Binary(encrypted);
            } catch (UnsupportedEncodingException | InvalidAlgorithmParameterException | InvalidKeyException | NoSuchAlgorithmException | BadPaddingException | IllegalBlockSizeException | NoSuchPaddingException ex) {}
            
            return null;
        }
        /**
         * decrypt an encrypted string
         * @param encrypted
         * @return
         */
        public static String decrypt(String encrypted){
            try {
                IvParameterSpec iv = new IvParameterSpec("RandomInitVector".getBytes("UTF-8"));
                SecretKeySpec skeySpec = new SecretKeySpec("Bar12345Bar12345".getBytes("UTF-8"), "AES");
                
                Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
                cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
                
                byte[] original = cipher.doFinal(DatatypeConverter.parseBase64Binary(encrypted‌​));
                
                return new String(original);
            } catch (UnsupportedEncodingException | InvalidAlgorithmParameterException | InvalidKeyException | NoSuchAlgorithmException | BadPaddingException | IllegalBlockSizeException | NoSuchPaddingException ex) {}
            
            return null;
        }
        
    }
//</editor-fold>
}
