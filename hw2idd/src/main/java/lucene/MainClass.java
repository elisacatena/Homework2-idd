package lucene;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
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

        Path path = Paths.get("/Users/elisacatena/Desktop/capitoli");
        Path indexPath = Paths.get("/Users/elisacatena/Desktop/documenti/index");
        System.out.println(path.toString());

        try {
        	directory = FSDirectory.open(indexPath);
            LuceneIndex.createIndex(directory, path);
            IndexReader reader = DirectoryReader.open(directory);
            IndexSearcher searcher = new IndexSearcher(reader);
            
            while(true) {
            	 System.out.print("Inserisci una query oppure 'exit' per uscire: ");
                 String queryReader = scanner.nextLine();
                 String[] words = queryReader.split(":");
                 
                 QueryParser queryParser = new QueryParser(words[0], new StandardAnalyzer());  
                 
                 Query query;
                 if(words[0].equals("nome")) {
                 	 query = queryParser.parse(words[1]+".txt");
                 }
                 else if(words[0].equals("contenuto")) {
                 	query = queryParser.parse(words[1]);
                 }
                 else if (words[0].equals("exit")) {
                	 break;
                 }
                 else {
                 	System.out.println("Campo non valido");
                 }
                 
                 LuceneIndex.runQuery(searcher, query);
            }
            catch(Exception e) {
                e.printStackTrace();
            }
                
            } finally {
                directory.close();
                scanner.close();
            }
        }

}
