package pojo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
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
@ApiModel("metadata of the document")
public class Metadata {

    @ApiModelProperty("author of the document")
    public Author author;

    @ApiModelProperty("organization of the document")
    public Organization organization;

    @ApiModelProperty("publication id of the document")
    public int pubid;

    @ApiModelProperty("title of the document")
    public String title;

    @ApiModelProperty("date of the document")
    public String date;

    @ApiModelProperty("filename of the document")
    public String filename;

    @ApiModelProperty("filesize of the document")
    public String fileSize;

    @ApiModelProperty("filetype of the document")
    public String fileType;


}
