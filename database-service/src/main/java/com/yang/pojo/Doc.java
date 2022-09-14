package com.yang.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The return Type of the query
 * @author Liu Yuxin, Yang Haoran
 * @date 2022/8/15 42:11:34
 * @description
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Doc{
    public Metadata metadata;
    public String path;
    public String ObjectId;
    public int docId;

}
