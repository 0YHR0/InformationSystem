package pojo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * The return Type of the query(completed)
 * @author Yang Haoran
 * @date 2022/10/15 42:11:34
 * @description
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ApiModel("Document")
public class CompleteDoc {
    //    public Metadata metadata;
    @ApiModelProperty("name of the document")
    public String name;

    @ApiModelProperty("title of the document")
    public String title;

    @ApiModelProperty("date of the document")
    public String date;

    @ApiModelProperty("filename of the document")
    public String filename;

    @ApiModelProperty("size of the document")
    public String size;

    @ApiModelProperty("type of the document")
    public String type;

    @ApiModelProperty("mount path of the document")
    public String mntpath;

    @ApiModelProperty("objectId of the document")
    public String ObjectId;

    @ApiModelProperty("Id of the document")
    public int docId;

    @ApiModelProperty("whether the file is deleted")
    public boolean isdeleted;

    @ApiModelProperty("additional metadata of the user")
    public String publication_json;

    @ApiModelProperty("keywords of the doc")
    public String keywords;

    @ApiModelProperty("abstract of the doc")
    public String abstra;

}

