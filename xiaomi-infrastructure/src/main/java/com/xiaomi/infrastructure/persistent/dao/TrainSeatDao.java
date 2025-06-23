//package com.xiaomi.infrastructure.persistent.dao;
//
//import com.xiaomi.infrastructure.persistent.po.TrainSeat;
//import org.apache.ibatis.annotations.Mapper;
//import org.apache.ibatis.annotations.Param;
//
//import java.time.LocalDateTime;
//import java.util.List;
//
///**
// * ClassName: TrainSeatDao
// * Package: com.xiaomi.infrastructure.persistent.dao
// */
//@Mapper
//public interface TrainSeatDao {
//    /**
//     * 统计指定车次车厢中、状态等于给定值的座位数量
//     */
//    int countByStatus(
//            @Param("trainNo") String trainNo,
//            @Param("carriageNo") String carriageNo,
//            @Param("status") byte status
//    );
//
//    /**
//     * 统计指定车次车厢的总座位数
//     */
//    int countTotal(
//            @Param("trainNo") String trainNo,
//            @Param("carriageNo") String carriageNo
//    );
//
//    /**
//     * 查询指定车次车厢下最大版本号
//     */
//    int maxVersion(
//            @Param("trainNo") String trainNo,
//            @Param("carriageNo") String carriageNo
//    );
//
//    /**
//     * 预占一个座位：status 0->1, version+1, 设置过期时间
//     * 返回影响行数，1 表示成功预占
//     */
//    int reserveOne(
//            @Param("trainNo") String trainNo,
//            @Param("carriageNo") String carriageNo,
//            @Param("seatNo") String seatNo,
//            @Param("expireAt") LocalDateTime expireAt,
//            @Param("version") int version
//    );
//
//    /**
//     * 释放超时预占：status 1->0, version+1, 清空 lock_until
//     * 返回影响行数，1 表示成功释放
//     */
//    int releaseOne(
//            @Param("trainNo") String trainNo,
//            @Param("carriageNo") String carriageNo,
//            @Param("seatNo") String seatNo,
//            @Param("now") LocalDateTime now
//    );
//
//
//    List<TrainSeat> findExpired(@Param("trainNo")String trainNo,
//                                @Param("carriageNo") String carriageNo,
//                                @Param("now")LocalDateTime now);
//}
