package pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author YHR
 * @date 2022/9/28 12:20:46
 * @description
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Organization {
    private int orgId;
    private String name;
    private String address;
}
