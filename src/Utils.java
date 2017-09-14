
import javax.swing.text.MaskFormatter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

class Utils {
    private static Pattern endFile = Pattern.compile(".+\\.txt");
    private static Pattern phoneRegex = Pattern.compile("^((8|\\+7)[\\s- ]?)?(\\(?\\d{3}\\)?[\\s- ]?)?[\\d\\s- ]{5,17}$");
    private static Pattern deleteSymbols = Pattern.compile("(\\+|-| |\\(|\\))");
    private static Scanner sc = new Scanner(System.in);
    private static List<File> getAllFiles(){
        List<File> files = new ArrayList<>();
        System.out.println("Enter the path to directory/file or write 'default' : ");
        String pathToDirOrFile = sc.next();

        if(Objects.equals(pathToDirOrFile, "default")){
            pathToDirOrFile = "C://TestData/";
        }

        try {
            files =  Files.walk(Paths.get(pathToDirOrFile))
                    .parallel()
                    .filter(Files::isRegularFile)
                    .map(Path::toFile)
                    .collect(Collectors.toList());

        } catch (IOException e) {
            System.out.println("Sorry:( Wrong path!");
        }
        return files;
    }
    public static void getNumbers(){
        List<File> files = getAllFiles();
        List<String> allNumbers = new ArrayList<>();
        for (File file : files) {
            if (endFile.matcher(file.getName()).matches()) {
                try {
                  byte [] mass = Files.readAllBytes(Paths.get(file.getAbsolutePath()));
                  String[] lines = new String(mass, StandardCharsets.UTF_8).split("\n");
                    for (String line: lines ) {
                        Matcher m = phoneRegex.matcher(line);
                        if(m.find()){
                            allNumbers.add(m.group().replaceAll(deleteSymbols.toString(),"").trim());
                        }

                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }
        printNumbers(allNumbers);
        repeat();

    }
    private static void repeat(){
        System.out.println("Do you want to try again? Y/N : ");
        String answer = sc.next();
        while (answer.equals("Y")) {
            getNumbers();
            repeat();
        }
        System.exit(0);
    }
    private static void printNumbers(List<String> list) {
        String mask = "+# (###) ###-####";
        String[] numbers = list.toArray(new String[list.size()]);
        SortedSet<String> forPrint = new TreeSet<>();
        for (String num : numbers) {
            if (num.length() < 8) {
                mask = "+7 (812) ###-####";
            }else if(num.length()<11){
                mask = "+7 (###) ###-####";
            }
            try {
                MaskFormatter mf = new MaskFormatter(mask);
                mf.setValueContainsLiteralCharacters(false);
                String result = mf.valueToString(num);
                forPrint.add(result);
            } catch (ParseException e) {
                e.printStackTrace();
            }

        }
        forPrint.forEach(System.out::println);
    }
    }

