package pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * The pojo class for the metadata of the doc
 * @Author: Yang Haoran
 * @Date: 01-08-2022 11:28:12
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class Metadata {
    public Author author;
    public Organization organization;
    public int pubid;
    public String title;
    public String date;
    public String filename;
    public String fileSize;
    public String fileType;


}
