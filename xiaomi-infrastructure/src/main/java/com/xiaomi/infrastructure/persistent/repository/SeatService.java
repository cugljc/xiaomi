//package com.xiaomi.infrastructure.persistent.repository;
//
//import com.xiaomi.infrastructure.persistent.dao.TrainSeatDao;
//import com.xiaomi.infrastructure.persistent.redis.IRedisService;
//import org.redisson.api.RBlockingQueue;
//import org.redisson.api.RDelayedQueue;
//import org.redisson.api.RLock;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Repository;
//
//import java.time.LocalDateTime;
//import java.util.*;
//import java.util.concurrent.TimeUnit;
//
///**
// * ClassName: TrianRepository
// * Package: com.xiaomi.infrastructure.persistent.repository
// */
//@Repository
//public class SeatService {
//
//    private static final String CACHE_KEY = "ticket:余票:%s:%s";
//    private static final long CACHE_TTL_MS = 2 * 60 * 1000;             // 2 分钟
//    private static final long LOCK_WAIT_SEC = 3;
//    private static final long LOCK_LEASE_SEC = 10;
//
//    @Autowired
//    private IRedisService redisService;
//
//    @Autowired
//    private TrainSeatDao seatDao;
//
//    // 延迟队列和对应的阻塞队列
//    private final RBlockingQueue<String> releaseQueue;
//    private final RDelayedQueue<String> delayedQueue;
//
//    @Autowired
//    public SeatService(IRedisService redisService) {
//        this.redisService = redisService;
//        this.releaseQueue = redisService.getBlockingQueue("seat:release:queue");
//        this.delayedQueue = redisService.getDelayedQueue(releaseQueue);
//    }
//
//    /**
//     * 查询某车厢余票（缓存+DB；防穿透双查）
//     */
//    public int queryAvailableSeats(String trainNo, String carriageNo) {
//        String key = String.format(CACHE_KEY, trainNo, carriageNo);
//        Integer cached = redisService.getValue(key);
//        if (cached != null) {
//            return cached;
//        }
//        // 加锁避免缓存穿透
//        String lockKey = key + ":lock";
//        RLock lock = redisService.getLock(lockKey);
//        try {
//            if (lock.tryLock(LOCK_WAIT_SEC, LOCK_LEASE_SEC, TimeUnit.SECONDS)) {
//                // 再次读取
//                cached = redisService.getValue(key);
//                if (cached != null) {
//                    return cached;
//                }
//                // DB 查询
//                int available = seatDao.countByStatus(trainNo, carriageNo, (byte)0);
//                // 回写缓存
//                redisService.setValue(key, available, CACHE_TTL_MS);
//                return available;
//            } else {
//                // 未拿到锁，短等待后递归重试
//                Thread.sleep(50);
//                return queryAvailableSeats(trainNo, carriageNo);
//            }
//        } catch (InterruptedException e) {
//            Thread.currentThread().interrupt();
//            return 0;
//        } finally {
//            if (lock.isHeldByCurrentThread()) {
//                lock.unlock();
//            }
//        }
//    }
//
//    /**
//     * 预占座位（分布式锁 + DB 乐观锁 + 延时双删 + 幂等ID）
//     */
//    public boolean reserveSeat(String trainNo, String carriageNo, String seatNo, Long userId) {
//        String lockKey = String.format("lock:seat:%s:%s:%s", trainNo, carriageNo, seatNo);
//        RLock lock = redisService.getLock(lockKey);
//        boolean acquired = false;
//        try {
//            acquired = lock.tryLock(LOCK_WAIT_SEC, LOCK_LEASE_SEC, TimeUnit.SECONDS);
//            if (!acquired) {
//                return false;
//            }
//            // 幂等 key
//            String idempKey = String.format("reserve:idem:%s:%s:%s:%s", userId, trainNo, carriageNo, seatNo);
//            if (redisService.isExists(idempKey)) {
//                return true;
//            }
//            String token = UUID.randomUUID().toString();
//            redisService.setValue(idempKey, token, 15 * 60 * 1000);
//            // 延时双删
//            String cacheKey = String.format(CACHE_KEY, trainNo, carriageNo);
//            redisService.remove(cacheKey);
//            // DB 乐观锁预占
//            int version = seatDao.maxVersion(trainNo, carriageNo);
//            LocalDateTime expireAt = LocalDateTime.now().plusMinutes(15);
//            int updated = seatDao.reserveOne(trainNo, carriageNo, seatNo, expireAt, version);
//            if (updated != 1) {
//                return false;
//            }
//
//
//            redisService.remove(cacheKey);
//            //睡一会延时双删？
//            delayedQueue.offer(String.format( trainNo, carriageNo,seatNo), 15, TimeUnit.MINUTES);//送入延时队列，通过定时任务取出超时任务，取消扣减
//            return true;
//        } catch (InterruptedException e) {
//            Thread.currentThread().interrupt();
//            return false;
//        } finally {
//            if (acquired && lock.isHeldByCurrentThread()) {
//                lock.unlock();
//            }
//        }
//    }
//
//    /**
//     * 释放超时未支付预占（从延时队列中批量消费）
//     */
//    @Scheduled(fixedRate = 60000)
//    public void releaseExpiredReservations() {
//        while (true) {
//            String seatKey = releaseQueue.poll();
//            if (seatKey == null) break;
//            // 解析 trainNo, carriageNo
//            String[] parts = seatKey.split(":"); // train:carriage
//            String trainNo = parts[0];
//            String carriageNo = parts[1];
//            String seatNo = parts[2];
//            // DB 释放
//            seatDao.releaseOne(trainNo,carriageNo,seatNo,LocalDateTime.now());
//        }
//    }
//}
//
