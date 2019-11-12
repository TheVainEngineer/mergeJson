import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.util.Scanner;
import java.util.Set;

public class MergeJson {
    static String filePath;
    static String inputPrefixName;
    static String outputFileName;
    static Long maxSize;
    static JSONParser jsonParser;
    static int fileCount = 1;
    static FileReader fileReader;
    static JSONObject jsonObject;
    static JSONObject mergedJson;
    static FileWriter fileWriter;

    public static String completeFilePath(String filePath, String fileName,int count) {
        if (filePath.charAt(filePath.length() - 1) != '/') {
            return filePath + "/" + fileName + Integer.toString(count) + ".json";
        }
        return filePath + fileName + Integer.toString(count) + ".json";
    }

    public static JSONObject readFile(String filePath, String fileName) throws IOException, ParseException {
        String completePath = completeFilePath(filePath,fileName,fileCount);
        fileReader = new FileReader(completePath);
        fileCount = fileCount + 1;
        JSONObject jsonObjectResult = (JSONObject) jsonParser.parse(fileReader);
        return jsonObjectResult;
    }

    public static void mergeJsonFiles(JSONObject jsonObject) {
        JSONArray keyValues = new JSONArray();
        for (Object key : jsonObject.keySet()) {
            if (mergedJson.containsKey(key)) {
                keyValues = (JSONArray) mergedJson.get(key);
            }
            keyValues.addAll((JSONArray)jsonObject.get(key));
            mergedJson.put(key,keyValues);
        }
    }

    public static void writeFile(String outputFileName,int counter) throws IOException {
        String completePath = completeFilePath(filePath,outputFileName,counter);
        fileWriter = new FileWriter(completePath);
        fileWriter.write(mergedJson.toString());
        fileWriter.flush();
    }

    public static void main (String args[]) {
        File outputFile;
        int counter = 0;
        Scanner s = new Scanner(System.in);
        System.out.println("Enter file path:");
        filePath = s.nextLine();
        System.out.println("Enter the prefix of the input file base Name");
        inputPrefixName = s.nextLine();
        System.out.println("Enter the output file base name");
        outputFileName = s.nextLine();
        System.out.println("Enter the maximum file size");
        maxSize = s.nextLong();
        jsonParser = new JSONParser();
        mergedJson = new JSONObject();
        jsonObject = new JSONObject();
        do {
            counter =  counter + 1;
            outputFile = new File(completeFilePath(filePath, outputFileName, counter));
        } while (outputFile.length() > 0);
        try {
            do {
                jsonObject = readFile(filePath,inputPrefixName);
                mergeJsonFiles(jsonObject);
                writeFile(outputFileName,counter);
            } while (outputFile.length() < maxSize);
        } catch (FileNotFoundException e) {

        } catch (ParseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
