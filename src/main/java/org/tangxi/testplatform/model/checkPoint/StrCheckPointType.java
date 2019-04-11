package org.tangxi.testplatform.model.checkPoint;

/**
 * 不同字符串判断类型:
 *      等于：==
 *      不等于：！=
 *      包含：in
 *      不包含：!in
 *
 * @author Tangx
 * 2019-02-13 15:02
 */
public enum StrCheckPointType implements CheckPointType{
    STREQUAL("==","等于"),
    STRNOTEQUAL("!=","不等于"),
    STRINCLUDE("in","包含"),
    STRNOTINCLUDE("!in","不包含");

    private String abbr;
    private String titile;

    private StrCheckPointType(String abbr, String titile){
        this.abbr = abbr;
        this.titile = titile;
    }

    public String getAbbr(){
        return abbr;
    }

    public String getTitile(){
        return titile;
    }

    public static StrCheckPointType fromAbbr(String abbr){
        for(StrCheckPointType strCheckPointType : StrCheckPointType.values()){
            if(strCheckPointType.getAbbr().equals(abbr)){
                return strCheckPointType;
            }
        }
        return null;
    }


}