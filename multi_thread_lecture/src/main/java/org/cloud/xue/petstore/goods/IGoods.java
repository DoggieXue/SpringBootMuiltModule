package org.cloud.xue.petstore.goods;

import org.cloud.xue.common.util.RandomUtil;

public interface IGoods extends Comparable<IGoods>{

    enum Type {
        PET,
        FOOD,
        CLOTHES;

        public static Type randType() {
            int length = values().length;
            int typeNo = RandomUtil.randInMod(length) - 1;
            return values()[typeNo];
        }
    }

    /**
     * 获取商品ID
     * @return
     */
    int getID();

    /**
     * 设置商品ID
     * @param id
     */
    void setID(int id);

    /**
     * 获取商品价格
     * @return
     */
    float getPrice();

    /**
     * 设置商品价格
     * @param price
     */
    void setPrice(float price);

    /**
     * 获取商品名称
     * @return
     */
    String getName();

    /**
     * 取的商品数量
     * @return
     */
    int getAmount();

    /**
     * 取的商品类型
     * @return
     */
    Type getType();
}
