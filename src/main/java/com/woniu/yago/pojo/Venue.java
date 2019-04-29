package com.woniu.yago.pojo;

import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

/**
 * @Description: java类作用描述Venue
 * @Author: lxy
 * @time: 2019/4/25 17:13
 */
@Data
@Entity
@DynamicUpdate
@DynamicInsert
@Table(name = "venue")
public class Venue {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Integer venueId;
    private Integer userId;
    private Integer clicks;
    private String venueDetail;
    private Integer venueFlag;

}
