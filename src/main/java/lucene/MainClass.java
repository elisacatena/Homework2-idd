package lucene;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

public class MainClass {

    public static void main(String args[]) throws Exception {

        Scanner scanner = new Scanner(System.in);
        Directory directory = null;

        Path path = Paths.get("/Users/elisacatena/Desktop/documenti/capitoli");
        Path indexPath = Paths.get("/Users/elisacatena/Desktop/documenti/index");
        System.out.println(path.toString());

        try {
            directory = FSDirectory.open(indexPath);
            LuceneIndex.createIndex(directory, path);
            IndexReader reader = DirectoryReader.open(directory);
            IndexSearcher searcher = new IndexSearcher(reader);

            while (true) {
                System.out.print("Inserisci una query oppure 'exit' per uscire: ");
                String queryReader = scanner.nextLine();
                String[] input = queryReader.split(":");
                String field = input[0].trim();
                QueryParser queryParser = new QueryParser(field, new StandardAnalyzer());

                Query query;
                
                if (field.equals("nome")) {
                    query = queryParser.parse(input[1] + ".txt");
                    LuceneIndex.runQuery(searcher, query);
                } else if (field.equals("contenuto")) {
                    query = queryParser.parse(input[1]);
                    LuceneIndex.runQuery(searcher, query);
                } else if (field.equals("exit")) {
                    break;
                } else {
                    System.out.println("Campo non valido");
                }


            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            directory.close();
            scanner.close();
        }
    }

}
