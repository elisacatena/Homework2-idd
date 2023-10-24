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
        System.out.println(path.toString());

        try {
        	directory = FSDirectory.open(path);
            LuceneIndex.createIndex(directory, path);
            IndexReader reader = DirectoryReader.open(directory);
            IndexSearcher searcher = new IndexSearcher(reader);
            
            
            System.out.print("Inserisci una query: ");
            String queryReader = scanner.nextLine();
            String[] words = queryReader.split(":");
            
            QueryParser queryParser = new QueryParser(words[0], new WhitespaceAnalyzer());  
            
            Query query;
            if(words[0].equals("nome")) {
            	 query = queryParser.parse(words[1]+".txt");
            }
            else if(words[0].equals("contenuto")) {
            	query = queryParser.parse(words[1]);
            }
            else {
            	System.out.println("Campo non valido");
            	return;
            }
            
            LuceneIndex.runQuery(searcher, query);

                
            } finally {
                directory.close();
                scanner.close();
            }
        }

}
