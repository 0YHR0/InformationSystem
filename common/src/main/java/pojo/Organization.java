package pojo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author Yang Haoran
 * @date 2022/9/28 12:20:46
 * @description
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ApiModel("Organization")
public class Organization {

    @ApiModelProperty("organization Id")
    public int orgId;

    @ApiModelProperty("name of the organization")
    public String name;

    @ApiModelProperty("address of the organization")
    public String address;
}
