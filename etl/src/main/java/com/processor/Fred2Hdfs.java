package com.processor;

import java.net.URI;
import java.util.Properties;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

import com.util.PropertyFileReader;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Fred2Hdfs {
    
    public enum APITYPE{
        SEARCH("series/search");
        OBSERVATION("series/observations");

        private String apiType;

        APITYPE(String apiType){
            this.apiType = apiType;
        }
    }

    public enum FREQUENCY{
        MONTH('M');
        YEAR('A');

        private char freq;

        FREQUENCY(char freq){
            this.freq = freq;
        }
    }
    
    private Properties fredProp = null;
    private ObjectMapper mapper = null;
    private FileSystem hadoopFs = null;
    
    public Fred2Hdfs() throws Exception{
        fredProp = PropertyFileReader.readPropertyFile("SystemConfig.properties");
        String HADOOP_CONF_DIR = fredProp.getProperty("hadoop.conf.dir");

        mapper = new ObjectMapper();

        Configuration conf = new Configuration();
        conf.addResource(new Path("file:///" + HADOOP_CONF_DIR + "/core-site.xml"));
        conf.addResource(new Path("file:///" + HADOOP_CONF_DIR + "/hdfs-site.xml"));

        String namenode = fredProp.getProperty("hdfs.namenode.url");
        hadoopFs = FileSystem.get(new URI(namenode), conf);
    }
}
