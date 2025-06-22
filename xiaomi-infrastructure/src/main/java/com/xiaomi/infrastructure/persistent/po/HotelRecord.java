package com.xiaomi.infrastructure.persistent.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * ClassName: HotelRecord
 * Package: com.example.cn.infrastructure.dao.po
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HotelRecord {
    private Integer id;
    private String hotel_name;
    private Integer score;
    private Integer price;
    private String address;
    private String manager_name;
}

