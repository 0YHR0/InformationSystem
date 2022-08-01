package com.yang.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
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
public class Author implements Serializable {
    private int authorid;
    private String name;
    private int orgid;
}
