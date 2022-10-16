package pojo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * The pojo class for the author of the doc
 * @Author: Yang Haoran
 * @Date: 01-08-2022 11:27:47
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ApiModel("Author of the document")
public class Author implements Serializable {

    @ApiModelProperty("authorid in the author table")
    public int authorid;
    @ApiModelProperty("name in the author table")
    public String name;

    //    @ApiModelProperty("orgid in the author table")
//    private int orgid;
}
