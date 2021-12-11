package com.haoke91.a91edu.utils;

import java.math.BigDecimal;

/**
 * 项目名称：91haoke_Android
 * 类描述：
 * 创建人：weiyimeng
 * 创建时间：2018/8/28 上午11:55
 * 修改人：weiyimeng
 * 修改时间：2018/8/28 上午11:55
 * 修改备注：
 */
public class ArithUtils {
    
    /**
     * 3      * 提供精确加法计算的add方法
     * 4      * @param value1 被加数
     * 5      * @param value2 加数
     * 6      * @return 两个参数的和
     * 7
     */
    public static BigDecimal add(BigDecimal value1, BigDecimal value2) {
        // BigDecimal b1 = new BigDecimal(Double.valueOf(value1));
        // BigDecimal b2 = new BigDecimal(Double.valueOf(value2));
        return value1.add(value2);
    }
    
    /**
     * 15      * 提供精确减法运算的sub方法
     * 16      * @param value1 被减数
     * 17      * @param value2 减数
     * 18      * @return 两个参数的差
     * 19
     */
    public static double sub(double value1, double value2) {
        BigDecimal b1 = new BigDecimal(Double.valueOf(value1));
        BigDecimal b2 = new BigDecimal(Double.valueOf(value2));
        return b1.subtract(b2).doubleValue();
    }
    
    /**
     * 27      * 提供精确乘法运算的mul方法
     * 28      * @param value1 被乘数
     * 29      * @param value2 乘数
     * 30      * @return 两个参数的积
     * 31
     */
    public static double mul(double value1, double value2) {
        BigDecimal b1 = new BigDecimal(Double.valueOf(value1));
        BigDecimal b2 = new BigDecimal(Double.valueOf(value2));
        return b1.multiply(b2).doubleValue();
    }
    
}
