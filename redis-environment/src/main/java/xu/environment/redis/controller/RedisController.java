package xu.environment.redis.controller;

import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/redis")
public class RedisController {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private RedissonClient redissonClient;

    @GetMapping("/redisTemplate")
    public String testRedisTemplate() {
        String key = "k1";
        String value = "v1";
        redisTemplate.opsForValue().set(key, value);
        String result = redisTemplate.opsForValue().get(key);
        return result;
    }

    @RequestMapping("/addData")
    public void addData() {
        redisTemplate.opsForValue().set("stock", "1");
        System.out.println("添加数据成功！stock = 1");
    }

    @GetMapping("/redisClient")
    public void testRedisClient() throws InterruptedException {
        //对数据进行加锁
        RLock lock = redissonClient.getLock("MoonCake");
        //加锁
        lock.lock();
        System.out.println(Thread.currentThread().getName());
        String stocks = redisTemplate.opsForValue().get("stock");
        int stock = Integer.parseInt(stocks);
        if (stock > 0) {
            //下单
            stock -= 1;
            redisTemplate.opsForValue().set("stock", String.valueOf(stock));
            System.out.println("扣减成功，库存stock：" + stock);
            Thread.sleep(5000);
        } else {
            //没库存
            System.out.println("扣减失败，库存不足");
        }
        //解锁
        lock.unlock();
    }
}
