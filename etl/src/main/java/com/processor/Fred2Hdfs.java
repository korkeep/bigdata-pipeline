package com.processor;

import java.net.URI;
import java.net.URL;
import java.util.List;
import java.util.Properties;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

import com.pojo.EtlColumnPojo;
import com.pojo.FredColumnPojo;
import com.util.PropertyFileReader;
import com.util.US_STATES;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;

public class Fred2Hdfs {
    
    public enum APITYPE{
        SEARCH("series/search"),
        OBSERVATION("series/observations");

        private String apiType;

        APITYPE(String apiType){
            this.apiType = apiType;
        }
    }

    public enum FREQUENCY{
        MONTH('M'),
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

    public List<EtlColumnPojo> getEtlListData(FREQUENCY freq, US_STATES state, String searchText) throws Exception {
        String fredUrl = fredProp.getProperty("fred.url");
        String apiKey = fredProp.getProperty("fred.apiKey");
        String fileType = "json";
        String searchUrl = fredUrl + APITYPE.valueOf("SEARCH").apiType + "?search_text=" + searchText.replace(' ', '+') + 
                            state.getName().replaceAll(" ", "+") + "&api_key=" + apiKey + "&file_type=" + fileType;
        
        System.out.println(searchUrl);
    
        JsonNode rootNode = mapper.readTree(new URL(searchUrl));
        Thread.sleep(500);
        ArrayNode nodeSeries = (ArrayNode)rootNode.get("series");
        List<FredColumnPojo> listFredData = mapper.readValue(nodeSeries.traverse(), new TypeReference<List<FredColumnPojo>>(){});
        Predicate<FredColumnPojo> predi = 
            fred -> (
                fred.getTitle().equals(searchText + state.getName()) &&
                (fred.getFrequency_short().charAt(0) == freq.freq) &&
                fred.getSeasonal_adjustment().equals("NSA")
            );
        List<EtlColumnPojo> listData = listFredData.stream().filter(predi).flatMap(pojo -> {
            String observeUrl = fredUrl + APITYPE.valueOf("OBSERVATION").apiType +
                                "?series_id=" + pojo.getId() + "&api_key=" + apiKey + "&file_type=" + fileType;
    
            System.out.println(observeUrl);
    
            try{
                JsonNode nodeValue = mapper.readTree(new URL(observeUrl));
                Thread.sleep(500);
                ArrayNode nodeValueObserve = (ArrayNode)nodeValue.get("observations");
                List<EtlColumnPojo> listEtlData = mapper.readValue(nodeValueObserve.traverse(), new TypeReference<List<EtlColumnPojo>>(){});
    
                for(EtlColumnPojo valuePojo : listEtlData){
                    valuePojo.setState(state.getName());
                    valuePojo.setId(pojo.getId());
                    valuePojo.setTitle(pojo.getTitle().replace(',','_'));
                    valuePojo.setFrequency_short(pojo.getFrequency_short());
                    valuePojo.setUnits_short(pojo.getUnits_short());
                    valuePojo.setSeasonal_adjustment_short(pojo.getSeasonal_adjustment_short());           
                }
                return listEtlData.stream();
            }
            catch(Exception e) {
                e.printStackTrace();
            }
            return Stream.empty();
            
        }).collect(Collectors.toList());  
        
        return listData;
    }
}
