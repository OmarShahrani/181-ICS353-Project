package omar.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Omar
 */
public class FileReaderWriter {

    private String inputFileName;
    private String outputFileName = "";
    private String ext = "txt";

    public FileReaderWriter() {
    }

    public FileReaderWriter(String inputFileName) {
        this.inputFileName = inputFileName;
    }

    public FileReaderWriter(String inputFileName, String outputFileName) {
        this.inputFileName = inputFileName;
        this.outputFileName = outputFileName;
    }

    public String getExt() {
        return ext;
    }

    public void setExt(String ext) {
        this.ext = ext;
    }

    public String getInputFileName() {
        return inputFileName;
    }

    public void setInputFileName(String inputFileName) {
        this.inputFileName = inputFileName;
    }

    public String getOutputFileName() {
        if (this.outputFileName == "") {
            String[] parts = this.inputFileName.split("/", -1);
            String name = "";
            parts[parts.length - 1] = "output_" + parts[parts.length - 1];
            name = String.join("/", parts);
            return name;
        } else {
            return outputFileName;
        }
    }

    public void setOutputFileName(String outputFileName) {
        this.outputFileName = outputFileName;
    }

    public List<String> read() {
        List<String> retStr = new ArrayList<>();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(this.inputFileName));
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().length() > 1) {
                    retStr.add(line);
                }
            }
            reader.close();
        } catch (FileNotFoundException ex) {
            String err = "Error: the file " + this.inputFileName + " was not found";
            throw new RuntimeException(err);
        } catch (IOException ex) {
            String err = "Error: something wrong happened during reading " + this.inputFileName;
            throw new RuntimeException(err);
        }
        return retStr;
    }

    public void write(String str) {
        String outputFileName = this.getOutputFileName();
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(outputFileName));
            writer.write(str);
            writer.close();
        } catch (IOException ex) {
            String err = "Error: something wrong happened during writing into " + this.outputFileName;
            throw new RuntimeException(err);
        }
    }

    public void write(List<String> strList) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(this.outputFileName));
            for (String line : strList) {
                writer.write(line);
                writer.newLine();
            }
            writer.close();
        } catch (IOException ex) {
            String err = "Error: something wrong happened during writing into " + this.outputFileName;
            throw new RuntimeException(err);
        }
    }
}
