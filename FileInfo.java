import java.util.ArrayList;

public class FileInfo {
    String line = "36c8b555 src/main/java/storage/ReadFromFile.java   (Kcube 2021-12-30 00:13:47 +0800  1) package storage;";
//        String line = "1bc0a2de (EndireKT 2021-12-19 11:02:35 +0800  1) package contributionChecker;";
    line = line.replaceAll("(^\\s+|\\s+$)", "");
    int a = line.indexOf("(");
    int b = line.indexOf("\s");
    String temp = line.substring(b,a);
        if (!temp.isBlank() || temp == "\s"){
        line = line.replaceAll(temp," ");
    }
    String[] parts = line.split("\\s+");
        System.out.println(line);

    String test2;
    String test3;
    String test4;
    String test5;
    String test6;
}
