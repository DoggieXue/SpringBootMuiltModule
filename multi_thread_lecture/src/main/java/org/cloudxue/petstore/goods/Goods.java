package org.cloudxue.petstore.goods;

import org.cloudxue.common.util.RandomUtil;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @ClassName Goods
 * @Description 请描述类的业务用途
 * @Author xuexiao
 * @Date 2022/3/30 下午2:47
 * @Version 1.0
 **/
public class Goods implements IGoods{

    protected int id;
    protected String goodName;
    protected float price;
    protected int amount;
    protected IGoods.Type goodsType;

    private static int goodsNo;//商品对象累加编号

    protected Goods() {
        this.id = ++goodsNo;
        this.amount = 1;
        this.price = 0;
        this.goodName = "位置商品";
    }

    @Override
    public String toString() {
        return "Goods{" +
                "ID=" + id +
                ", 名称='" + goodName + '\'' +
                ", 价格=" + price +
                '}';
    }

    /**
     * 生成一个随机类型商品
     *
     * @return 随机类型商品
     */
    public static IGoods produceOne() {
        Type randomType = Type.randType();
        return produceByType(randomType);
    }

    /**
     * 生成一个指定类型商品
     *
     * @param type
     * @return 指定类型商品
     */
    public static IGoods produceByType(Type type) {
        switch (type) {
            case PET:
                return new GoodsPet();
            case FOOD:
                return new GoodsFood();
            case CLOTHES:
                return new GoodsClothes();
        }
        return new Goods();
    }

    @Override
    public int getID() {
        return id;
    }

    @Override
    public void setID(int id) {
        this.id = id;
    }

    @Override
    public float getPrice() {
        return price;
    }

    @Override
    public void setPrice(float price) {

    }

    @Override
    public String getName() {
        return goodName;
    }

    @Override
    public int getAmount() {
        return amount;
    }

    @Override
    public Type getType() {
        return goodsType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Goods goods = (Goods) o;

        return id == goods.id;
    }

    @Override
    public int hashCode() {
        return id;
    }

    @Override
    public int compareTo(IGoods o) {
        if (o == null) throw new NullPointerException("Good object is null");
        return this.id - o.getID();
    }

    private static class GoodsPet extends Goods {
        private final static AtomicInteger PET_NO = new AtomicInteger(0);  //编号

        public GoodsPet() {
            super();
            this.goodsType = Type.CLOTHES;
            this.goodName = "宠物-" + PET_NO.incrementAndGet();
            price = RandomUtil.randInRange(1000, 10000);
            amount = RandomUtil.randInMod(5);
        }
    }

    private static class GoodsClothes extends Goods {
        private final static AtomicInteger CLOTHES_NO = new AtomicInteger(0);  //编号

        public GoodsClothes() {
            super();
            this.goodsType = Type.CLOTHES;
            this.goodName = "宠物衣服-" + CLOTHES_NO.incrementAndGet();
            price = RandomUtil.randInRange(50, 100);
            amount = RandomUtil.randInMod(5);
        }
    }

    private static class GoodsFood extends Goods {
        private final static AtomicInteger FOOD_NO = new AtomicInteger(0);  //编号

        public GoodsFood() {
            super();
            this.goodsType = Type.FOOD;
            this.goodName = "宠物粮食-" + FOOD_NO.incrementAndGet();
            price = RandomUtil.randInRange(50, 100);
            amount = RandomUtil.randInMod(5);
        }
    }
}
