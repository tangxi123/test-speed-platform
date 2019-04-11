package org.tangxi.testplatform.model.checkPoint;

/**
 * 不同List判断类型:
 *      大小：size
 *      包含：contains
 *      成员：get
 *
 * @author Tangx
 * 2019-02-28 12:28
 */
public enum ListCheckPointType implements CheckPointType {
    LIST_SIZE("size","大小"),
    LIST_CONTAINS("contains","包含"),
    LIST_GET("get","成员");
    private String abbr;
    private String titile;

    private ListCheckPointType(String abbr, String titile){
        this.abbr = abbr;
        this.titile = titile;
    }

    public String getAbbr(){
        return abbr;
    }

    public String getTitile(){
        return titile;
    }

    public static ListCheckPointType fromAbbr(String abbr){
        for(ListCheckPointType listCheckPointType : ListCheckPointType.values()){
            if(listCheckPointType.getAbbr().equals(abbr)){
                return listCheckPointType;
            }
        }
        return null;
    }
}
