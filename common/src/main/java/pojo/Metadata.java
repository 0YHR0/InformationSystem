package pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The pojo class for the metadata of the doc
 * @Author: Yang Haoran
 * @Date: 01-08-2022 11:28:12
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Metadata {
    private Author[] author;
    private Organization organization;
    private String title;
    private String date;
    private String filename;
    private String fileSize;
    private String fileType;

}
