package org.tangxi.testplatform.execution.steps.verifys;

import ch.qos.logback.core.joran.action.ActionUtil;
import com.jayway.restassured.path.json.JsonPath;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.tangxi.testplatform.common.exception.testcase.TestCaseRunException;
import org.tangxi.testplatform.common.exception.testcase.UnexpectedTestCaseException;
import org.tangxi.testplatform.common.util.JacksonUtil;
import org.tangxi.testplatform.execution.Execution;
import org.tangxi.testplatform.model.checkPoint.*;
import org.testng.asserts.SoftAssert;

import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.jayway.restassured.path.json.JsonPath.with;

public class VerifyUtil {

    private static final Logger LOG = LoggerFactory.getLogger(VerifyUtil.class);

    private final static String STR_CHECK_POINT = "StrCheckPoint";
    private final static String NUM_CHECK_POINT = "NumCheckPoint";
    private final static String LIST_CHECK_POINT = "ListCheckPoint";


    public static String verifyCheck(JsonPath requestResults, CheckPoint checkPoint, SoftAssert assertion) {
        String type = checkPoint.getType();
        Execution.logs.add("【检查点校验】检查类型为："+type);
        switch (type) {
            case STR_CHECK_POINT:
                return verifyStrCheckType(requestResults, checkPoint, assertion);
            case NUM_CHECK_POINT:
                return verifyNumCheckType(requestResults, checkPoint, assertion);
            case LIST_CHECK_POINT:
                return verifyListCheckType(requestResults, checkPoint, assertion);
            default:
                throw new UnexpectedTestCaseException("不存在的检查点校验类型" + type);
        }

    }

    /**
     * 校验字符串类型的检查点
     *
     * @param requestResults
     * @param checkPoint
     * @param assertion
     */
    private static String verifyStrCheckType(JsonPath requestResults, CheckPoint checkPoint, SoftAssert assertion) {
        CheckPointType checkType = checkPoint.getCheckPointType();
        StrCheckPointType type = (StrCheckPointType) checkType;
        String checkKey = checkPoint.getCheckKey();
        String expected = checkPoint.getExpected();
        String actual = requestResults.getString(checkKey);
        Execution.logs.add("【检查点校验】检查的关键字为：" + checkKey);
        Execution.logs.add("【检查点校验】期望的结果为：" + expected);
        switch (type) {
            case STREQUAL:
                assertion.assertEquals(actual, expected);
                Execution.logs.add("【检查点校验】字符串相等判断:");
                Execution.logs.add("【检查点校验】接口返回的json字符串中:" + checkKey + "的值为:" + actual);
                Execution.logs.add("【检查点校验】接期望的json字符串中:" + checkKey + "的值为：" + expected);
                return ("字符串相等判断:期望值为【" + expected + "】——实际值为【" + actual + "】");
            case STRNOTEQUAL:
                assertion.assertNotEquals(actual, expected);
                Execution.logs.add("【检查点校验】字符串不相等判断:");
                Execution.logs.add("【检查点校验】接口返回的json字符串中:" + checkKey + "的值为:" + actual);
                Execution.logs.add("【检查点校验】接期望的json字符串中:" + checkKey + "的值为：" + expected);
                return ("字符串不相等判断:期望值为【" + expected + "】——实际值为【" + actual + "】");
            case STRINCLUDE:
                assertion.assertTrue(actual.contains(expected));
                Execution.logs.add("【检查点校验】字符串包含判断:");
                Execution.logs.add("【检查点校验】接口返回的json字符串中:" + checkKey + "的值为:" + actual);
                Execution.logs.add("【检查点校验】接期望的json字符串中:" + checkKey + "的值为：" + expected);
                return ("字符串包含判断:期望值为【" + expected + "】——实际值为【" + actual + "】");
            case STRNOTINCLUDE:
                assertion.assertFalse(actual.contains(expected));
                Execution.logs.add("【检查点校验】字符串不包含判断:");
                Execution.logs.add("【检查点校验】接口返回的json字符串中:" + checkKey + "的值为:" + actual);
                Execution.logs.add("【检查点校验】接期望的json字符串中:" + checkKey + "的值为：" + expected);
                return ("字符串不包含判断:期望值为【" + expected + "】——实际值为【" + actual + "】");
            default:
                throw new UnexpectedTestCaseException("不存在的字符串检查类型" + type);
        }
    }

    /**
     * 校验数字类型的检查点
     *
     * @param requestResults
     * @param checkPoint
     * @param assertion
     */
    private static String verifyNumCheckType(JsonPath requestResults, CheckPoint checkPoint, SoftAssert assertion) {
        CheckPointType checkType = checkPoint.getCheckPointType();
        NumCheckPointType type = (NumCheckPointType) checkType;
        String checkKey = checkPoint.getCheckKey();
        String expected = checkPoint.getExpected();
        Execution.logs.add("【检查点校验】检查的关键字为：" + checkKey);
        Execution.logs.add("【检查点校验】期望的结果为：" + expected);
        String actua = ""+requestResults.get(checkKey);
        if (isDouble(actua)) {
            double actual = requestResults.getDouble(checkKey);
            double expect;
            try{
                expect = Double.parseDouble(expected);
            }catch (NumberFormatException e){
                throw new TestCaseRunException(e);
            }
            switch (type) {
                case NUM_EQ:
                    assertion.assertEquals(actual, expect);
                    Execution.logs.add("【检查点校验】数值相等判断:");
                    Execution.logs.add("【检查点校验】接口返回的json字符串中:" + checkKey + "的值为:" + actual);
                    Execution.logs.add("【检查点校验】接期望的json字符串中:" + checkKey + "的值为：" + expected);
                    return ("数值相等判断:期望值为【" + expected + "】——实际值为【" + actual + "】");

                case NUM_GT:
                    assertion.assertTrue(actual > expect);
                    Execution.logs.add("【检查点校验】数值大于判断:");
                    Execution.logs.add("【检查点校验】接口返回的json字符串中:" + checkKey + "的值为:" + actual);
                    Execution.logs.add("【检查点校验】接期望的json字符串中:" + checkKey + "的值为：" + expected);
                    return ("数值大于判断:期望值为【" + expected + "】——实际值为【" + actual + "】");
                case NUM_LT:
                    assertion.assertTrue(actual < expect);
                    Execution.logs.add("【检查点校验】数值小于判断:");
                    Execution.logs.add("【检查点校验】接口返回的json字符串中:" + checkKey + "的值为:" + actual);
                    Execution.logs.add("【检查点校验】接期望的json字符串中:" + checkKey + "的值为：" + expected);
                    return ("数值小于判断:期望值为【" + expected + "】——实际值为【" + actual + "】");
                case NUM_GT_EQ:
                    assertion.assertTrue(actual >= expect);
                    Execution.logs.add("【检查点校验】数值大于等于判断:");
                    Execution.logs.add("【检查点校验】接口返回的json字符串中:" + checkKey + "的值为:" + actual);
                    Execution.logs.add("【检查点校验】接期望的json字符串中:" + checkKey + "的值为：" + expected);
                    return ("数值大于等于判断:期望值为【" + expected + "】——实际值为【" + actual + "】");
                case NUM_LT_EQ:
                    assertion.assertTrue(actual <= expect);
                    Execution.logs.add("【检查点校验】数值小于等于判断:");
                    Execution.logs.add("【检查点校验】接口返回的json字符串中:" + checkKey + "的值为:" + actual);
                    Execution.logs.add("【检查点校验】接期望的json字符串中:" + checkKey + "的值为：" + expected);
                    return ("数值小于等于判断:期望值为【" + expected + "】——实际值为【" + actual + "】");
                default:
                    throw new UnexpectedTestCaseException("不存在的数字检查类型" + type);
            }
        } else if (isInteger(actua)) {
            int actual = requestResults.getInt(checkKey);
            int expect;
            try{
                expect = Integer.parseInt(expected);
            }catch (NumberFormatException e){
                throw new TestCaseRunException(e);
            }
            switch (type) {
                case NUM_EQ:
                    assertion.assertEquals(actual, expect);
                    Execution.logs.add("【检查点校验】数值相等判断:");
                    Execution.logs.add("【检查点校验】接口返回的json字符串中:" + checkKey + "的值为:" + actual);
                    Execution.logs.add("【检查点校验】接期望的json字符串中:" + checkKey + "的值为：" + expected);
                    return ("数值相等判断:期望值为【" + expected + "】——实际值为【" + actual + "】");

                case NUM_GT:
                    assertion.assertTrue(actual > expect);
                    Execution.logs.add("【检查点校验】数值大于判断:");
                    Execution.logs.add("【检查点校验】接口返回的json字符串中:" + checkKey + "的值为:" + actual);
                    Execution.logs.add("【检查点校验】接口期望的json字符串中:" + checkKey + "的值为：" + expected);
                    return ("数值大于判断:期望值为【" + expected + "】——实际值为【" + actual + "】");
                case NUM_LT:
                    assertion.assertTrue(actual < expect);
                    Execution.logs.add("【检查点校验】数值小于判断:");
                    Execution.logs.add("【检查点校验】接口返回的json字符串中:" + checkKey + "的值为:" + actual);
                    Execution.logs.add("【检查点校验】接期望的json字符串中:" + checkKey + "的值为：" + expected);
                    return ("数值小于判断:期望值为【" + expected + "】——实际值为【" + actual + "】");
                case NUM_GT_EQ:
                    assertion.assertTrue(actual >= expect);
                    Execution.logs.add("【检查点校验】数值大于等于判断:");
                    Execution.logs.add("【检查点校验】接口返回的json字符串中:" + checkKey + "的值为:" + actual);
                    Execution.logs.add("【检查点校验】接期望的json字符串中:" + checkKey + "的值为：" + expected);
                    return ("数值大于等于判断:期望值为【" + expected + "】——实际值为【" + actual + "】");
                case NUM_LT_EQ:
                    assertion.assertTrue(actual <= expect);
                    Execution.logs.add("【检查点校验】数值小于等于判断:");
                    Execution.logs.add("【检查点校验】接口返回的json字符串中:" + checkKey + "的值为:" + actual);
                    Execution.logs.add("【检查点校验】接期望的json字符串中:" + checkKey + "的值为：" + expected);
                    return ("数值小于等于判断:期望值为【" + expected + "】——实际值为【" + actual + "】");
                default:
                    throw new UnexpectedTestCaseException("不存在的数字检查类型" + type);
            }
        }
        return null;


    }

    private static String verifyListCheckType(JsonPath requestResults, CheckPoint checkPoint, SoftAssert assertion) {
        CheckPointType checkType = checkPoint.getCheckPointType();
        ListCheckPointType type = (ListCheckPointType) checkType;
        String checkKey = checkPoint.getCheckKey();
        String expected = checkPoint.getExpected();
        Object o = requestResults.get(checkKey);
        List<Integer> actual = requestResults.getList(checkKey);

        Execution.logs.add("【检查点校验】检查的关键字为：" + checkKey);
        Execution.logs.add("【检查点校验】期望的结果为：" + expected);
        switch (type){
            case LIST_SIZE:
                assertion.assertEquals(actual.size(),Integer.parseInt(expected));
                Execution.logs.add("【检查点校验】List长度判断：");
                Execution.logs.add("【检查点校验】接口返回的json字符串："+ JacksonUtil.toJson(actual)+",长度为："+actual.size());
                Execution.logs.add("【检查点校验】期望的长度为："+expected);
                return ("【检查点校验】List长度判断：期望的的长度为【"+expected+"】——实际值为【"+ actual.size()+"】");
            case LIST_CONTAINS:
                assertion.assertTrue(actual.contains(expected));
                Execution.logs.add("【检查点校验】List包含判断：");
                Execution.logs.add("【检查点校验】接口返回的json字符串："+ JacksonUtil.toJson(actual));
                Execution.logs.add("【检查点校验】期望包含的字符串为："+expected);
                return ("【检查点校验】List包含判断：期望包含的字符串为【"+expected+"】——实际返回的字符串为【"+ actual+"】");
            case LIST_GET:

        }
        return null;

    }

    /**
     * 判断字符串是否是浮点类型
     *
     * @param str
     * @return
     */
    private static boolean isDouble(String str) {
        if (null == str || "".equals(str)) {
            return false;
        }
        boolean contains = str.contains(".");
        return contains;
    }

    /**
     * 判断字符串是否是整数
     *
     * @param str
     * @return
     */
    private static boolean isInteger(String str) {
        if (null == str || "".equals(str)) {
            return false;
        }
        Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
        Matcher matcher = pattern.matcher(str);
        boolean matches = matcher.matches();
        return pattern.matcher(str).matches();
}



}
