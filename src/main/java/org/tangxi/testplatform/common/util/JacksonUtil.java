package org.tangxi.testplatform.common.util;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**************************************************************
 * JacksonUtil
 *
 * - jackson的简单封装，fromJson和toJson分别实现json和Object之间的转换
 *
 ************************************************************/

/**
 * @author zhongqiu
 */
@JsonIgnoreProperties
public class JacksonUtil {
    private static ObjectMapper m  = new ObjectMapper();
    private static JsonFactory jf = new JsonFactory();

    static {
        m.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
    }

    /*
     * 从json串反序列化为Object
     */
    public static <T> T fromJson(String jsonAsString, Class<T> pojoClass) {
        try {
            m.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            return m.readValue(jsonAsString, pojoClass);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> Object fromJsonToList(String jsonAsString, Class<T> pojoClass) {
        try {
            m.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            JavaType type = m.getTypeFactory().constructParametricType(ArrayList.class, pojoClass);
            return m.readValue(jsonAsString, type);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> Object fromJson(FileReader fr, Class<T> pojoClass) {
        try {
            return m.readValue(fr, pojoClass);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T fromJson(String jsonAsString, TypeReference<T> typeReference){
        try{
            return m.readValue(jsonAsString,typeReference);
        }catch (IOException e){
            throw new RuntimeException(e);
        }
    }

    /*
     * 将Object序列化为json字符串
     */
    public static String toJson(Object pojo) {
        return toJson(pojo, false);
    }

    public static String toJson(Object pojo, boolean prettyPrint) {
        StringWriter sw = new StringWriter();
        JsonGenerator jg = null;
        try {
            jg = jf.createGenerator(sw);
            if (prettyPrint) {
                jg.useDefaultPrettyPrinter();
            }
            m.writeValue(jg, pojo);
            return sw.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public static void toJson(Object pojo, FileWriter fw) {
        toJson(pojo, fw, false);
    }

    public static void toJson(Object pojo, FileWriter fw, boolean prettyPrint) {
        JsonGenerator jg = null;
        try {
            jg = jf.createGenerator(fw);
            if (prettyPrint) {
                jg.useDefaultPrettyPrinter();
            }
            m.writeValue(jg, pojo);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public static void main(String[] args) throws Exception {
        String base64 = "PCFET0NUWVBFIGh0bWwgUFVCTElDICItLy9XM0MvL0RURCBYSFRNTCAxLjAgVHJhbnNpdGlvbmFsLy9FTiIgImh0dHA6Ly93d3cudzMub3JnL1RSL3hodG1sMS9EVEQveGh0bWwxLXRyYW5zaXRpb25hbC5kdGQiPg0KPGh0bWwgeG1sbnM9Imh0dHA6Ly93d3cudzMub3JnLzE5OTkveGh0bWwiPg0KPGhlYWQ+DQo8dGl0bGU+5beo5r2u6LWE6K6v572RPC90aXRsZT4NCjwhLS0g5YWs5YWx5byV55SoIC0tPg0KPGxpbmsgaHJlZj0iL2NuaW5mby1uZXcvY3NzL2J1bGxldGluLWRldGFpbC5jc3MiIHJlbD0ic3R5bGVzaGVldCIgdHlwZT0idGV4dC9jc3MiIC8+DQo8bGluayBocmVmPSIvY25pbmZvLW5ldy9jc3MvdGFnLmNzcyIgcmVsPSJzdHlsZXNoZWV0IiB0eXBlPSJ0ZXh0L2NzcyIgLz4NCjxsaW5rIGhyZWY9Ii9jbmluZm8tbmV3L2Nzcy9iYXNlLmNzcyIgcmVsPSJzdHlsZXNoZWV0IiB0eXBlPSJ0ZXh0L2NzcyIgLz4NCjxzY3JpcHQgdHlwZT0idGV4dC9qYXZhc2NyaXB0IiBzcmM9Ii9jbmluZm8tbmV3L2pzL3NoYXJlV2IuanMiPjwvc2NyaXB0Pg0KPHNjcmlwdCB0eXBlPSJ0ZXh0L2phdmFzY3JpcHQiIHNyYz0iL2NuaW5mby1uZXcvanMvanF1ZXJ5LmpzIj48L3NjcmlwdD4NCjxzY3JpcHQgdHlwZT0idGV4dC9qYXZhc2NyaXB0Ij4NCgl2YXIgc2hhcmVDb250ZW50ID0gIuS4reaWsOi1m+WFi++8iDAwMjkxMu+8iSAgIg0KCQkJCQkJKyLlnKggQOW3qOa9rui1hOiur+e9kSBodHRwOi8vd3d3LmNuaW5mby5jb20uY24g5Y+R5biD5LqG5YWs5ZGK77ya5Zu95L+h6K+B5Yi46IKh5Lu95pyJ6ZmQ6LSj5Lu75YWs5Y+45YWz5LqO5YWs5Y+45oyB57ut552j5a+85pyfMjAxOOW5tOWfueiureaDheWGteaKpeWRiiI7DQo8L3NjcmlwdD4NCjwvaGVhZD4NCjxib2R5Pg0KCTxkaXYgY2xhc3M9Im1pZGRsZSI+DQoJCTxkaXYgY2xhc3M9Im1haW4iPg0KCQkJPGRpdiBjbGFzcz0iYmQtdG9wIj4NCgkJCQk8ZGl2IGNsYXNzPSJiZC1jYWxlbmRhciI+DQoJCQkJCTxhIHN0eWxlPSJjdXJzb3I6IHBvaW50ZXIiPg0KCQkJCQkJPGRpdiBjbGFzcz0ieWVhciI+MjAxOOW5tDAxPC9kaXY+DQoJCQkJCQkJPGRpdiBjbGFzcz0ibmV3X2RheSI+MTE8L2Rpdj4NCgkJCQkJPC9hPg0KCQkJCTwvZGl2Pg0KCQkJCTxoMj4NCgkJCQkJMDAyOTEyIOS4reaWsOi1m+WFizxiciAvPg0KCQkJCQnlm73kv6Hor4HliLjogqHku73mnInpmZDotKPku7vlhazlj7jlhbPkuo7lhazlj7jmjIHnu63nnaPlr7zmnJ8yMDE45bm05Z+56K6t5oOF5Ya15oql5ZGKDQoJCQkJPC9oMj4NCg0KCQkJPC9kaXY+DQoJCQk8ZGl2IGNsYXNzPSJiZC1ydCI+DQoJCQkJPGRpdiBjbGFzcz0iYmQtZG93bmxvYWQiPg0KCQkJCQk8ZGl2IGNsYXNzPSJidG4tYmx1ZSBiZC1idG4iPg0KCQkJCQkJPGEgaHJlZj0iL2NuaW5mby1uZXcvZGlzY2xvc3VyZS9zenNlL2Rvd25sb2FkLzEyMDQzMTk3ODQ/YW5ub3VuY2VUaW1lPTIwMTgtMDEtMTEiPg0KCQkJCQkJCTxpbWcgc3JjPSIvY25pbmZvLW5ldy9pbWFnZXMvaWNvbl9kb3dubG9hZC5wbmciIC8+5YWs5ZGK5LiL6L29DQoJCQkJCQk8L2E+Jm5ic3A7DQoJCQkJCQko5paH5Lu25aSn5bCPOjIxNktCKQ0KCQkJCQk8L2Rpdj4NCgkJCQk8L2Rpdj4NCgkJCQk8ZGl2IGNsYXNzPSJiZC1mdCI+DQoJCQkJCTxzcGFuIGNsYXNzPSJmdC1pY29uIj4gDQoJCQkJCTwvc3Bhbj4gDQoJCQkJCQ0KCQkJCQnliIbkuqvvvJogPHNwYW4gY2xhc3M9ImZ0LXNoYXJlLWJ0biB3YiI+DQoJCQkJCQk8YSBocmVmPSJqYXZhc2NyaXB0OnNoYXJlVG9TaW5hV2IoKSIgPg0KCQkJCQkJCTxpbWcgc3JjPSIvY25pbmZvLW5ldy9pbWFnZXMvZGV0YWlsLWljb24td2IucG5nIiB0aXRsZT0i5YiG5Lqr5Yiw5paw5rWq5b6u5Y2aIiAvPg0KCQkJCQkJPC9hPg0KCQkJCQkJPC9zcGFuPg0KCQkJCQkJPHNwYW4gY2xhc3M9ImZ0LXNoYXJlLWJ0biB0Y3diIj4NCgkJCQkJCQk8c2NyaXB0IHR5cGU9InRleHQvamF2YXNjcmlwdCI+DQoJCQkJCQkJCWRvY3VtZW50LndyaXRlKCc8YSBocmVmPSJodHRwOi8vc25zLnF6b25lLnFxLmNvbS9jZ2ktYmluL3F6c2hhcmUvY2dpX3F6c2hhcmVfb25la2V5P3VybD0nDQoJCQkJCQkJCStlbmNvZGVVUklDb21wb25lbnQobG9jYXRpb24uaHJlZikrJyZ0aXRsZT0wMDI5MTIg5Lit5paw6LWb5YWLIOWbveS/oeivgeWIuOiCoeS7veaciemZkOi0o+S7u+WFrOWPuOWFs+S6juWFrOWPuOaMgee7reedo+WvvOacnzIwMTjlubTln7norq3mg4XlhrXmiqXlkYomc3VtbWFyeT0nDQoJCQkJCQkJCStzaGFyZUNvbnRlbnQucmVwbGFjZSgiQOW3qOa9rui1hOiur+e9kSBodHRwOi8vd3d3LmNuaW5mby5jb20uY24iLCLlt6jmva7otYTorq/nvZEiKQ0KCQkJCQkJCQkrJyZkZXNjPScrc2hhcmVDb250ZW50LnJlcGxhY2UoIkDlt6jmva7otYTorq/nvZEgaHR0cDovL3d3dy5jbmluZm8uY29tLmNuIiwi5beo5r2u6LWE6K6v572RIGh0dHA6Ly93d3cuY25pbmZvLmNvbS5jbiIpDQoJCQkJCQkJCSsnIiB0YXJnZXQ9Il9ibGFuayI+PGltZyBzcmM9Ii9jbmluZm8tbmV3L2ltYWdlcy9kZXRhaWwtaWNvbi1xcS5wbmciIHRpdGxlPSLliIbkuqvliLBRUeepuumXtCIgLz48L2E+Jyk7DQoJCQkJCQkJPC9zY3JpcHQ+DQoJCQkJCQk8L3NwYW4+DQoJCQkJPC9kaXY+DQoJCQk8L2Rpdj4NCgkJCTxkaXYgY2xhc3M9ImJkLWN0IiBzdHlsZT0iei1pbmRleDoxOyI+DQoJCQkJIDxpZnJhbWUgc3R5bGU9IndpZHRoOiAxMTc4cHg7IGhlaWdodDogMTAwJTsiIHNyYz0iaHR0cDovL3d3dy5jbmluZm8uY29tLmNuL2ZpbmFscGFnZS8yMDE4LTAxLTExLzEyMDQzMTk3ODQuUERGIj48L2lmcmFtZT4gDQoJCQk8L2Rpdj4NCgkJPC9kaXY+DQoJCTxkaXYgY2xhc3M9ImNsZWFyIj48L2Rpdj4NCgk8L2Rpdj4NCgk8IS0tIOe8k+WKoOi9veaWh+S7tiAtLT4NCgk8c2NyaXB0IGxhbmd1YWdlPSJqYXZhc2NyaXB0IiB0eXBlPSJ0ZXh0L2phdmFzY3JpcHQiDQoJCXNyYz0iL2NuaW5mby1uZXcvanMvYmFzZS5qcyI+PC9zY3JpcHQ+DQoJPHNjcmlwdCBsYW5ndWFnZT0iamF2YXNjcmlwdCIgdHlwZT0idGV4dC9qYXZhc2NyaXB0Ig0KCQlzcmM9Ii9jbmluZm8tbmV3L2pzL2J1bGxldGluLWRldGFpbC5qcyI+PC9zY3JpcHQ+DQo8L2JvZHk+DQo8L2h0bWw+DQo=";
//        byte[] content = Base64.decodeBase64(base64);
//        FileOutputStream bw = new FileOutputStream(new File("pdf.pdf"));
//        bw.write(content);
//        bw.flush();
//        bw.close();
        JacksonUtil.fromJson(base64, new TypeReference<Object>() {});
//
//        class s extends TypeReference<Object>{
//
//        }
    }

}
