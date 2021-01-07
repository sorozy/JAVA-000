import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;


public class Counter {

    private static JedisPool jedisPool = new JedisPool(new JedisPoolConfig(), "192.168.0.106", 6379, 2000, null);

    public static void des(String key) {
        try (Jedis jedis = jedisPool.getResource()) {
            Long count = jedis.decr(key);
            System.out.println(count);
        }
    }

    public static void main(String[] args) {
        for (int i = 0; i < 5; i++) {
            Thread thread = new Thread(() -> {
                des("testDes");
            });
            thread.start();
        }
    }

}
