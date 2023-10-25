package lucene;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.CharArraySet;
import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.analysis.it.ItalianAnalyzer;
import org.apache.lucene.analysis.miscellaneous.PerFieldAnalyzerWrapper;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.codecs.Codec;
import org.apache.lucene.codecs.simpletext.SimpleTextCodec;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;


public class LuceneIndex {
    
    public static void createIndex(Directory directory, Path path) throws Exception {
    	
    	Codec codec = new SimpleTextCodec();
        Analyzer defaultAnalyzer = new StandardAnalyzer();
        Map<String, Analyzer> perFieldAnalyzers = new HashMap<>();
        
        perFieldAnalyzers.put("contenuto", new ItalianAnalyzer());
        perFieldAnalyzers.put("nome", new WhitespaceAnalyzer());

        Analyzer analyzer = new PerFieldAnalyzerWrapper(defaultAnalyzer, perFieldAnalyzers);
        IndexWriterConfig config = new IndexWriterConfig(analyzer);
        if (codec != null) {
            config.setCodec(codec);
        }
        
        IndexWriter writer = new IndexWriter(directory, config);
        writer.deleteAll(); 

        try {
        	File dir = new File(path.toString());
            File[] files = dir.listFiles();
            if (files != null) {
	            for (File file : files) {
	                Document document = new Document();
	                document.add(new TextField("nome",file.getName(), Field.Store.YES));
	                // read file content...
	                Reader reader = new FileReader(file.getCanonicalPath());
	                // ... and add it in the field "contenuto" 
	                document.add(new TextField("contenuto", reader));
	                writer.addDocument(document);
	                reader.close();
	            }
            }
            writer.commit();
            writer.close();

        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("deprecation")
	public static void runQuery(IndexSearcher searcher, Query query) throws IOException {
        TopDocs hits = searcher.search(query, 8);
        System.out.println("Sono stati trovati " + hits.scoreDocs.length + " " + "documenti");
        for (int i = 0; i < hits.scoreDocs.length; i++) {
            ScoreDoc scoreDoc = hits.scoreDocs[i];
            Document doc = searcher.doc(scoreDoc.doc);
            System.out.println("doc"+scoreDoc.doc + ":"+ doc.get("nome") + " (" + scoreDoc.score +")");
        }
        System.out.println("FINE RUN QUERY");
    }
    

}

