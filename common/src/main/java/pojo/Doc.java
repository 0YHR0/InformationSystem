package pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * The return Type of the query
 * @author YHR
 * @date 2022/8/15 42:11:34
 * @description
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class Doc {
//    public Metadata metadata;
    public String name;
    public String title;
    public String date;
    public String filename;
    public String size;
    public String type;
    public String mntpath;
    public String ObjectId;
    public int docId;

}
