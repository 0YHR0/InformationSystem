package pojo;

/**
 * @author YHR
 * @date 2022/10/17 11:0:31
 * @description
 */

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * This is a class for solr metadata
 * @Author Yang Haoran
 */
@NoArgsConstructor
@Data
@AllArgsConstructor
public class SolrMetadata {
    private String solrId;
    private String abstra;
    private String keywords;
}
