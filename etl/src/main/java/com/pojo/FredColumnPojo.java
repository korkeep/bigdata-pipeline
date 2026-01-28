package com.pojo;

import java.time.LocalDate;
import java.time.LocalDateTime;
import com.util.DefaultLocalDateTimeDeserializer;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
@JsonPropertyOrder({
    "id", "realtime_start", "realtime_end", "title", "observation_start", "observation_end", 
    "frequency", "frequency_short", "units", "units_short", "seasonal_adjustment", 
    "seasonal_adjustment_short", "last_updated", "popularity", "group_popularity", "notes"
})
public class FredColumnPojo {
    
    @JsonProperty("id")
    private String id;

    @JsonProperty("realtime_start")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private LocalDate realtime_start;
    
    @JsonProperty("realtime_end")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private LocalDate realtime_end;

    @JsonProperty("title")
    private String title;

    @JsonProperty("observation_start")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private LocalDate observation_start;

    @JsonProperty("observation_end")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private LocalDate observation_end;

    @JsonProperty("frequency")
    private String frequency;

    @JsonProperty("frequency_short")
    private String frequency_short;

    @JsonProperty("units")
    private String units;

    @JsonProperty("units_short")
    private String units_short;

    @JsonProperty("seasonal_adjustment")
    private String seasonal_adjustment;
    
    @JsonProperty("seasonal_adjustment_short")
    private String seasonal_adjustment_short;

    @JsonProperty("last_updated")
    @JsonDeserialize(using = DefaultLocalDateTimeDeserializer.class)
    private LocalDateTime last_updated;

    @JsonProperty("popularity")
    private short popularity;
    
    @JsonProperty("group_popularity")
    private short group_popularity;

    @JsonProperty("notes")
    private String notes;

    @Override
    public String toString(){
        return "FredColumnPojo [" +
            "id=\"" + id + "\"" +
            ", realtime_start=\"" + realtime_start + "\"" +
            ", realtime_end=\"" + realtime_end + "\"" +
            ", title=\"" + title + "\"" +
            ", observation_start=\"" + observation_start + "\"" +
            ", observation_end=\"" + observation_end + "\"" +
            ", frequency=\"" + frequency + "\"" +
            ", frequency_short=\"" + frequency_short + "\"" +
            ", units=\"" + units + "\"" +
            ", units_short=\"" + units_short + "\"" +
            ", seasonal_adjustment=\"" + seasonal_adjustment + "\"" +
            ", seasonal_adjustment_short=\"" + seasonal_adjustment_short + "\"" +
            ", last_updated=\"" + last_updated + "\"" +
            ", popularity=\"" + popularity + "\"" +
            ", group_popularity=\"" + group_popularity + "\"" +
            ", notes=\"" + notes + "\"" +
            "]";
    }
}
