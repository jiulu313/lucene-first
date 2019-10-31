package com.lucene.study;

import org.apache.commons.io.FileUtils;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.*;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

public class LuceneTest {
    public static void main(String[] args) throws Exception {
        LuceneTest luceneTest = new LuceneTest();
//        luceneTest.createIndex();

        luceneTest.searchIndex();
    }

    public void searchIndex() throws IOException {
        Directory directory = FSDirectory.open(new File("/Users/zhanghongjun/test/index").toPath());
        IndexReader indexReader = DirectoryReader.open(directory);
        IndexSearcher indexSearcher = new IndexSearcher(indexReader);
        Query query = new TermQuery(new Term("content","android"));
        TopDocs topDocs = indexSearcher.search(query,10);
        System.out.println("查询总数为=" + topDocs.totalHits);

        ScoreDoc[] scoreDocs = topDocs.scoreDocs;
        for (ScoreDoc doc : scoreDocs){
            int docId = doc.doc;
            Document document = indexSearcher.doc(docId);
            System.out.println(document.get("name"));
            System.out.println(document.get("path"));
            System.out.println(document.get("size"));
            System.out.println(document.get("content"));
            System.out.println("---------分隔线--------------------");

        }

        indexReader.close();
    }


    public void createIndex() throws Exception {
        //1 创建一个Director对象，指定索引库保存位置
        Directory directory = FSDirectory.open(new File("/Users/zhanghongjun/test/index").toPath());
        IndexWriter indexWriter = new IndexWriter(directory, new IndexWriterConfig());
        File dir = new File("/Users/zhanghongjun/test/file");
        File[] files = dir.listFiles();
        for (File file : files) {
            String fileName = file.getName();
            String filePath = file.getPath();
            String fileContent = FileUtils.readFileToString(file, "utf-8");
            long fileSize = FileUtils.sizeOf(file);

            //创建field
            Field fieldName = new TextField("name", fileName, Field.Store.YES);
            Field fieldPath = new TextField("path",filePath,Field.Store.YES);
            Field fieldContent = new TextField("content",fileContent,Field.Store.YES);
            Field fieldSize = new TextField("size",fileSize + "",Field.Store.YES);

            Document document = new Document();
            document.add(fieldName);
            document.add(fieldPath);
            document.add(fieldContent);
            document.add(fieldSize);

            indexWriter.addDocument(document);
        }

        indexWriter.close();
    }



}
