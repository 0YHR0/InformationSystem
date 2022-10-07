package pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author YHR
 * @date 2022/9/28 12:20:46
 * @description
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class Organization {
    public int orgId;
    public String name;
    public String address;
}
